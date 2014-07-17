package sk.hackcraft.creep;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

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

	private final EnvironmentTime time;

	private final List<Updateable> updateables;
	private final List<Drawable> drawables;

	private final Scout scout;

	private MiningAgent miningAgent;
	private EntitiesServerContract<Unit> minersContract;
	
	private SpawningPoolConstructor spawningPoolConstructor;
	
	private Unit resourceDepot;

	public CreepBot(Game game)
	{
		super(game);

		time = new GameEnvironmentTime(game);

		updateables = new ArrayList<>();
		drawables = new ArrayList<>();

		Position startPosition = game.getSelf().getStartLocation();
		Set<Position> scoutPositions = new HashSet<>();
		for (BaseLocation bl : game.getMap().getStartLocations())
		{
			scoutPositions.add(bl.getCenter());
		}
		Position nearestPositionToStartPosition = PositionUtils.getNearest(scoutPositions, startPosition);
		scoutPositions.remove(nearestPositionToStartPosition);

		scout = new Scout(bwapi, startPosition, scoutPositions)
		{
			@Override
			protected void enemyPositionFound(Position position)
			{
				game.sendMessage("Found enemy at " + position);
			}
			
			@Override
			protected void droneReturned(Unit unit)
			{
			}
		};

		updateables.add(scout);
		drawables.add(scout);
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
		spawningPoolConstructor = new SpawningPoolConstructor(game, referencePosition, environmentTime);
		updateables.add(spawningPoolConstructor);
		drawables.add(spawningPoolConstructor);
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
		game.setSpeed(10);
		bwapi.setFrameSkip(0);

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
		
		if (game.getSelf().getMinerals() >= 190 && !spawningPoolConstructor.isConstructing())
		{
			Unit drone = game.getMyUnits().where(UnitSelector.IS_WORKER).whereLessOrEqual(new DistanceSelector(resourceDepot), 300).pick(Pickers.FIRST);
			spawningPoolConstructor.construct(drone);
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
