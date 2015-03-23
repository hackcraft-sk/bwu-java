package sk.hackcraft.creep;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import jnibwapi.BaseLocation;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.AbstractBot;
import sk.hackcraft.bwu.BWU;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Comparison;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.EntitiesContract;
import sk.hackcraft.bwu.EntitiesServerContract;
import sk.hackcraft.bwu.EnvironmentTime;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.GameEnvironmentTime;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.UnitOwning;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.grid.GameLayerFactory;
import sk.hackcraft.bwu.grid.Grid;
import sk.hackcraft.bwu.grid.GridDimension;
import sk.hackcraft.bwu.grid.GridPoint;
import sk.hackcraft.bwu.grid.GridUpdater;
import sk.hackcraft.bwu.grid.GridUtil;
import sk.hackcraft.bwu.grid.grids.UnitsLayer;
import sk.hackcraft.bwu.grid.processors.BorderLayerProcessor;
import sk.hackcraft.bwu.grid.processors.DrawProcessor;
import sk.hackcraft.bwu.grid.processors.FloodFillProcessor;
import sk.hackcraft.bwu.grid.processors.ValuesChangerLayerProcessor;
import sk.hackcraft.bwu.grid.updaters.TemperatureLayerUpdater;
import sk.hackcraft.bwu.grid.visualization.ColorAssigner;
import sk.hackcraft.bwu.grid.visualization.LayerColorDrawable;
import sk.hackcraft.bwu.grid.visualization.LayerDrawable;
import sk.hackcraft.bwu.grid.visualization.colorassigners.MapExactColorAssigner;
import sk.hackcraft.bwu.grid.visualization.colorassigners.MapGradientColorAssignment;
import sk.hackcraft.bwu.grid.visualization.colorassigners.RandomColorAssigner;
import sk.hackcraft.bwu.grid.visualization.swing.LayersPainter;
import sk.hackcraft.bwu.grid.visualization.swing.SwingLayersVisualization;
import sk.hackcraft.bwu.intelligence.EnemyIntelligence;
import sk.hackcraft.bwu.mining.MapResourcesAgent;
import sk.hackcraft.bwu.mining.MiningAgent;
import sk.hackcraft.bwu.mining.MapResourcesAgent.ExpandInfo;
import sk.hackcraft.bwu.moving.FlockingManager;
import sk.hackcraft.bwu.production.BuildingConstructionAgent;
import sk.hackcraft.bwu.production.BuildingConstructionAgent.ConstructionListener;
import sk.hackcraft.bwu.production.LarvaProductionAgent;
import sk.hackcraft.bwu.production.LarvaProductionAgent.ProductionStatus;
import sk.hackcraft.bwu.resource.EntityPool;
import sk.hackcraft.bwu.resource.EntityPool.Contract;
import sk.hackcraft.bwu.resource.FirstTakeEntityPool;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.NearestPicker;
import sk.hackcraft.bwu.selection.Pickers;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import sk.hackcraft.bwu.util.PositionUtils;
import sk.hackcraft.creep.pool5.Scout;
import sk.hackcraft.creep.pool5.SpawningPoolConstructor;

public class CreepBot extends AbstractBot
{
	private static int wins, games;

	public static void main(String[] args)
	{
		while (true)
		{
			System.out.println("Starting BWU");
			
			BWU bwu = new BWU()
			{
				@Override
				protected Bot createBot(Game game)
				{
					return new CreepBot(game);
				}
			};
	
			bwu.start();
		}
	}

	private final Random random;
	private final EnvironmentTime time;

	private final List<Updateable> updateables;
	private final List<Drawable> drawables;

	private Scout scout;

	private MiningAgent miningAgent;
	private EntitiesServerContract<Unit> minersContract;
	
	private SpawningPoolConstructor spawningPoolConstructor;
	
	private Unit resourceDepot;
	
	private Position attackPosition;

	public CreepBot(Game game)
	{
		super(game);

		random = new Random(0);
		time = new GameEnvironmentTime(game);

		updateables = new ArrayList<>();
		drawables = new ArrayList<>();
	}

	@Override
	public void gameStarted()
	{
		// creating layers visualizations

		jnibwapi.Map map = game.getMap();
		GridDimension dimension = Convert.toGridDimension(map.getSize());

		// setting up game parameters

		game.enableUserInput();
		game.setSpeed(0);

		resourceDepot = game.getMyUnits().where(UnitSelector.IS_RESOURCE_DEPOT).pick(Pickers.FIRST);
		UnitSet resources = game.getStaticNeutralUnits().whereLessOrEqual(new DistanceSelector(resourceDepot), 500);
		minersContract = new EntitiesServerContract<Unit>()
		{
			@Override
			public void entityReturned(Unit entity)
			{
			}
		};
		miningAgent = new MiningAgent(bwapi, resourceDepot, resources, minersContract);
		updateables.add(miningAgent);
		drawables.add(miningAgent);
		
		EnvironmentTime environmentTime = new GameEnvironmentTime(game);
		Position referencePosition = game.getSelf().getStartLocation();
		spawningPoolConstructor = new SpawningPoolConstructor(game, bwapi, referencePosition, environmentTime);
		updateables.add(spawningPoolConstructor);
		drawables.add(spawningPoolConstructor);
		
		Position startPosition = game.getSelf().getStartLocation();
		Set<Position> scoutPositions = new HashSet<>();
		for (BaseLocation bl : game.getMap().getStartLocations())
		{
			scoutPositions.add(bl.getCenter());
		}
		Position nearestPositionToStartPosition = PositionUtils.getNearest(scoutPositions, startPosition);
		scoutPositions.remove(nearestPositionToStartPosition);
		
		if (scoutPositions.isEmpty())
		{
			System.out.println("Strange map data, 0 start locations, leaving game...");
			bwapi.leaveGame();
		}

		scout = new Scout(bwapi, startPosition, scoutPositions)
		{
			@Override
			protected void enemyPositionFound(Position position)
			{
				game.sendMessage("Found enemy at " + position);
				
				attackPosition = position;
			}
			
			@Override
			protected void droneReturned(Unit unit)
			{
				unit.stop(false);
				
				if (unit.getType().isWorker())
				{
					minersContract.addEntity(unit);
				}
				else
				{
					unit.move(game.getSelf().getStartLocation(), false);
				}
			}
		};

		updateables.add(scout);
		drawables.add(scout);
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		games++;
		if (isWinner)
		{
			wins++;
		}

		System.out.println(games + " " + wins);
	}

	@Override
	public void gameUpdated()
	{
		if (!game.getMyUnits().where(UnitSelector.IS_UNDER_ATTACK).isEmpty())
		{
			game.setSpeed(20);
		}
		else
		{
			game.setSpeed(0);
			bwapi.setFrameSkip(16);
		}

		bwapi.drawText(new Position(10, 10), "Frame: " + game.getFrameCount(), true);

		for (Updateable updateable : updateables)
		{
			updateable.update();
		}

		if (game.getFrameCount() == 0)
		{
			UnitSet overlords = game.getMyUnits().whereType(UnitTypes.Zerg_Overlord);
			scout.addOverlord(overlords.iterator().next());
		}

		if (game.getFrameCount() == 0)
		{
			UnitSet drones = game.getMyUnits().where(UnitSelector.IS_WORKER);
			
			minersContract.addEntities(drones);
		}

		if (game.getFrameCount() == 100)
		{
			UnitSet drones = game.getMyUnits().where(UnitSelector.IS_WORKER);
			
			Unit drone = drones.iterator().next();
			minersContract.removeEntity(drone);
			scout.addDrone(drone);
		}
		
		Unit spawningPool = game.getMyUnits().whereType(UnitTypes.Zerg_Spawning_Pool).pick(Pickers.FIRST);
		
		if (game.getSelf().getMinerals() >= 190 && !spawningPoolConstructor.isConstructing())
		{
			Unit drone = game.getMyUnits().where(UnitSelector.IS_WORKER).whereLessOrEqual(new DistanceSelector(resourceDepot), 300).pick(Pickers.FIRST);
			
			if (drone != null)
			{
				spawningPoolConstructor.construct(drone);
			}
		}
		
		if (spawningPool != null && spawningPool.isCompleted())
		{
			UnitSet larvae = game.getMyUnits().whereType(UnitTypes.Zerg_Larva);
			
			larvae.forEach(l -> l.morph(UnitTypes.Zerg_Zergling));
		}
		
		Player self = game.getSelf();
		int supplyTotal = self.getSupplyTotal();
		int supplyUsed = self.getSupplyUsed();
		if (supplyTotal - supplyUsed == 0 && self.getMinerals() > 100)
		{
			UnitSet larvae = game.getMyUnits().whereType(UnitTypes.Zerg_Larva);
			
			larvae.forEach(l -> l.morph(UnitTypes.Zerg_Overlord));
		}

		if (attackPosition != null && game.getFrameCount() % 30 == 0)
		{
			Position lingAttackPosition;
			
			Unit enemyBuilding = game.getEnemyUnits().where(UnitSelector.IS_BUILDING).pick(Pickers.FIRST);
			if (enemyBuilding != null)
			{
				lingAttackPosition = enemyBuilding.getPosition();
			}
			else
			{
				int range = 1000;
				int x = attackPosition.getPX() + random.nextInt(range) - range / 2;
				int y = attackPosition.getPY() + random.nextInt(range) - range / 2;
				lingAttackPosition = new Position(x, y);
			}
			
			UnitSet zerglings = game.getMyUnits().whereType(UnitTypes.Zerg_Zergling);
			List<Unit> idleZerglings = zerglings.stream()
			.filter(u -> !u.isAttackFrame())
			.collect(Collectors.toList());
			
			for (Unit ling : idleZerglings)
			{
				UnitSet enemyUnits = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(ling), 1000);
				
				if (enemyUnits.isEmpty())
				{
					ling.attack(lingAttackPosition, false);
				}
				else
				{
					Unit unit = enemyUnits.where(UnitSelector.CAN_ATTACK_GROUND).whereNot(UnitSelector.IS_WORKER).pick(new NearestPicker(ling));
					
					if (unit != null)
					{
						ling.attack(unit, false);
					}
					else
					{
						unit = enemyUnits.where(UnitSelector.IS_WORKER).pick(new NearestPicker(ling));
						
						if (unit != null)
						{
							ling.attack(unit, false);
						}
						else
						{
							unit = enemyUnits.whereType(UnitTypes.Protoss_Pylon).pick(new NearestPicker(ling));
							
							if (unit != null)
							{
								ling.attack(unit, false);
							}
							else
							{
								ling.attack(lingAttackPosition, false);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void keyPressed(int keyCode)
	{
	}

	@Override
	public void playerLeft(Player player)
	{
	}

	@Override
	public void playerDropped(Player player)
	{
	}

	@Override
	public void nukeDetected(Vector2D target)
	{
	}

	@Override
	public void unitDiscovered(Unit unit)
	{
	}

	@Override
	public void unitDestroyed(Unit unit)
	{
	}

	@Override
	public void unitEvaded(Unit unit)
	{
	}

	@Override
	public void unitCreated(Unit unit)
	{
	}

	@Override
	public void unitCompleted(Unit unit)
	{
	}

	@Override
	public void unitMorphed(Unit unit)
	{
	}

	@Override
	public void unitShowed(Unit unit)
	{
	}

	@Override
	public void unitHid(Unit unit)
	{
	}

	@Override
	public void unitRenegaded(Unit unit)
	{
	}

	@Override
	public void draw(Graphics graphics)
	{
		for (Drawable drawable : drawables)
		{
			drawable.draw(graphics);
		}
		
		for (Unit unit : game.getMyUnits().whereType(UnitTypes.Zerg_Zergling))
		{
			graphics.setGameCoordinates();
			graphics.setColor(BWColor.Red);
			
			if (unit.getOrderTarget() != null)
			{
				graphics.drawLine(unit.getPositionVector(), unit.getOrderTarget().getPositionVector());
			}
		}
	}

	@Override
	public void messageSent(String message)
	{
	}

	@Override
	public void messageReceived(String message)
	{
	}

	@Override
	public void gameSaved(String gameName)
	{
	}
}
