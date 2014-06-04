package sk.hackcraft.creep;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.AbstractBot;
import sk.hackcraft.bwu.BWU;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.map.BorderLayerProcessor;
import sk.hackcraft.bwu.map.Dimension;
import sk.hackcraft.bwu.map.GameLayerFactory;
import sk.hackcraft.bwu.map.GradientFloodFillProcessor;
import sk.hackcraft.bwu.map.Layer;
import sk.hackcraft.bwu.map.LayerDrawable;
import sk.hackcraft.bwu.map.LayerColorDrawable;
import sk.hackcraft.bwu.map.MapColorAssigner;
import sk.hackcraft.bwu.map.Point;
import sk.hackcraft.bwu.mining.MiningAgent;
import sk.hackcraft.bwu.production.DroneBuildingConstructionAgent;
import sk.hackcraft.bwu.production.LarvaProductionAgent;
import sk.hackcraft.bwu.selection.Convert;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.TypeSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

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

	private final Set<MiningAgent> miningAgents;
	private final LarvaProductionAgent productionAgent;
	
	private DroneBuildingConstructionAgent constructionAgent;
	private boolean spawningPoolBuilt;
	
	private int lastHatcheryBuilt = 2000;
	
	private Unit constructorWorker;
	
	private final Map<Unit, Position> enemyBuildings;
	
	private Layer plainsLayer;
	private LayerDrawable plainsLayerDrawable;
	
	public CreepBot(Game game)
	{
		super(game);
		
		miningAgents = new HashSet<>();
		productionAgent = new LarvaProductionAgent();
		
		enemyBuildings = new HashMap<>();
	}

	@Override
	public void gameStarted()
	{
		game.enableUserInput();
		game.setSpeed(0);
		
		plainsLayer = GameLayerFactory.createBuildableLayer(jnibwapi, 0, 1);
		
		BorderLayerProcessor borderLayerProcessor = new BorderLayerProcessor(10, 1);
		Layer bordersLayer = borderLayerProcessor.process(plainsLayer);
		
		plainsLayer = plainsLayer.add(bordersLayer);

		GradientFloodFillProcessor gradientFloofFillProcessor = new GradientFloodFillProcessor(11, 1)
		{
			@Override
			protected boolean evaluateCell(int cellValue, int newValue)
			{
				return cellValue < newValue;
			}
		};
		
		//plainsLayer = gradientFloofFillProcessor.process(plainsLayer);
		
		HashMap<Integer, BWColor> colors = new HashMap<>();
		colors.put(1, BWColor.Red);
		colors.put(11, BWColor.Green);
		MapColorAssigner colorAssigner = new MapColorAssigner(colors);
		plainsLayerDrawable = new LayerColorDrawable(plainsLayer, 32, colorAssigner);

		UnitSet myUnits = game.getMyUnits();
		
		UnitSet hatcheries = myUnits.where(UnitSelector.IS_SPAWNING_LARVAE);
		for (Unit hatchery : hatcheries)
		{
			productionAgent.addHatchery(hatchery);
			
			Set<Unit> nearbyResources = game.getStaticNeutralUnits().where(UnitSelector.IS_MINERAL).whereLessOrEqual(new DistanceSelector(hatchery), 500);
			
			if (!nearbyResources.isEmpty())
			{
				MiningAgent miningAgent = new MiningAgent(hatchery, nearbyResources);
				
				Set<Unit> nearbyWorkers = myUnits.where(UnitSelector.IS_WORKER).whereLessOrEqual(new DistanceSelector(hatchery), 300);
				
				for (Unit worker : nearbyWorkers)
				{
					miningAgent.addMiner(worker);
				}
				
				miningAgents.add(miningAgent);
			}
		}
		
		Position startLocation = game.getSelf().getStartLocation();
		
		constructionAgent = new DroneBuildingConstructionAgent(jnibwapi, startLocation);
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
		jnibwapi.drawText(new Position(10, 10), Integer.toString(game.getFrameCount()), true);
		
		game.setSpeed(25);
		
		for (MiningAgent agent : miningAgents)
		{
			agent.update();
		}
		
		if (constructorWorker != null && !spawningPoolBuilt)
		{
			constructionAgent.construct(constructorWorker, UnitTypes.Zerg_Spawning_Pool, new DroneBuildingConstructionAgent.ConstructionListener()
			{
				@Override
				public void onFailed()
				{
				}
			});
			
			spawningPoolBuilt = true;
			constructorWorker = null;
		}
		
		if (constructorWorker != null && game.getFrameCount() - lastHatcheryBuilt > 2000 && game.getMyUnits().where(UnitSelector.IS_WORKER).size() > 10 && game.getSelf().getMinerals() >= 300)
		{
			constructionAgent.construct(constructorWorker, UnitTypes.Zerg_Hatchery, new DroneBuildingConstructionAgent.ConstructionListener()
			{
				
				@Override
				public void onFailed()
				{
				}
			});
			
			constructorWorker = null;
		}
		
		constructionAgent.update();
		
		int availableMinerals = game.getSelf().getMinerals(); 
		UnitSet workers = game.getMyUnits().where(UnitSelector.IS_WORKER);
		UnitSet spawningPools = game.getMyUnits().where(new TypeSelector(UnitTypes.Zerg_Spawning_Pool));
		if ((workers.size() <= 8 && spawningPools.isEmpty()) || !spawningPools.isEmpty())
		{
			if (availableMinerals >= 50)
			{
				boolean zerglings = new Random().nextBoolean();
				
				UnitType type;
				if (zerglings && !spawningPools.isEmpty() || workers.size() > 25)
				{
					type = UnitTypes.Zerg_Zergling;
				}
				else
				{
					type = UnitTypes.Zerg_Drone;
				}
				
				boolean result = productionAgent.produce(type);
				
				if (!result && availableMinerals > 100)
				{
					productionAgent.produce(UnitTypes.Zerg_Overlord);
				}
			}
		}
		
		Random random = new Random();
		UnitSet overlords = game.getMyUnits().where(new TypeSelector(UnitTypes.Zerg_Overlord));
		for (Unit overlord : overlords)
		{
			if (overlord.isIdle())
			{
				Position position = overlord.getPosition();
				int x = random.nextInt(1000) - 500 + position.getPX();
				int y = random.nextInt(1000) - 500 + position.getPY();
				overlord.move(new Position(x, y), false);
			}
		}
		
		// check visible enemy buildins
		{
			UnitSet visibleEnemyBuildings = game.getEnemyUnits().where(UnitSelector.IS_BUILDING);
			
			for (Unit unit : visibleEnemyBuildings)
			{
				enemyBuildings.put(unit, unit.getPosition());
			}
		}
		
		// check outdated attack
		Map<Unit, Position> enemyBuildingsCopy = new HashMap<>(enemyBuildings);
		for (Unit unit : enemyBuildingsCopy.keySet())
		{
			if (unit.isVisible() && !unit.isExists())
			{
				enemyBuildings.remove(unit);
			}
		}
		
		Vector2D startPosition = Convert.toPositionVector(game.getSelf().getStartLocation());
		UnitSet enemies = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(startPosition), 1000);
		UnitSet zerglings = game.getMyUnits().where(new TypeSelector(UnitTypes.Zerg_Zergling));
		
		if (!enemies.isEmpty())
		{
			game.setSpeed(25);
			Unit unit = enemies.iterator().next();
			
			for (Unit zergling : zerglings)
			{	
				if (zergling.isIdle())
				{
					zergling.attack(unit.getPosition(), false);
				}
			}
		}
		else
		{
			if (game.getFrameCount() % 1000 == 0 && !enemyBuildings.isEmpty())
			{
				Position attackPosition = enemyBuildings.values().iterator().next();
				
				for (Unit zergling : zerglings)
				{
					zergling.attack(attackPosition, false);
				}
			}
		}
	}

	@Override
	public void keyPressed(int keyCode)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerLeft(Player player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerDropped(Player player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nukeDetected(Vector2D target)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitDiscovered(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitDestroyed(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitEvaded(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitCreated(Unit unit)
	{
		
	}

	@Override
	public void unitCompleted(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitMorphed(Unit unit)
	{
		if (unit.getType().isWorker())
		{
			if (game.getMyUnits().where(UnitSelector.IS_WORKER).size() > 8 && constructorWorker == null)
			{
				constructorWorker = unit;
			}
			else
			{
				miningAgents.iterator().next().addMiner(unit);
			}
		}
		
		if (UnitSelector.IS_SPAWNING_LARVAE.isTrueFor(unit))
		{
			productionAgent.addHatchery(unit);
		}
	}

	@Override
	public void unitShowed(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitHid(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitRenegaded(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics graphics)
	{
		for (MiningAgent agent : miningAgents)
		{
			agent.draw(graphics);
		}
		
		plainsLayerDrawable.draw(graphics);
	}

	@Override
	public void messageSent(String message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(String message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameSaved(String gameName)
	{
		// TODO Auto-generated method stub
		
	}
}
