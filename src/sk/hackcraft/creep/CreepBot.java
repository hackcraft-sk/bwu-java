package sk.hackcraft.creep;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.EnvironmentTime;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.GameEnvironmentTime;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.maplayer.ColorAssigner;
import sk.hackcraft.bwu.maplayer.GameLayerFactory;
import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerColorDrawable;
import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerDrawable;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.Layers;
import sk.hackcraft.bwu.maplayer.MapExactColorAssigner;
import sk.hackcraft.bwu.maplayer.MapGradientColorAssignment;
import sk.hackcraft.bwu.maplayer.RandomColorAssigner;
import sk.hackcraft.bwu.maplayer.UnitsLayer;
import sk.hackcraft.bwu.maplayer.processors.BorderLayerProcessor;
import sk.hackcraft.bwu.maplayer.processors.GradientFloodFillProcessor;
import sk.hackcraft.bwu.maplayer.processors.ValueFloodFillProcessor;
import sk.hackcraft.bwu.maplayer.processors.ValuesChangerLayerProcessor;
import sk.hackcraft.bwu.maplayer.visualization.LayersPainter;
import sk.hackcraft.bwu.maplayer.visualization.SwingLayersVisualization;
import sk.hackcraft.bwu.mining.MapResourcesAgent;
import sk.hackcraft.bwu.mining.MapResourcesAgent.ExpandInfo;
import sk.hackcraft.bwu.production.BuildingConstructionAgent;
import sk.hackcraft.bwu.production.LarvaProductionAgent;
import sk.hackcraft.bwu.resource.EntityPool;
import sk.hackcraft.bwu.resource.EntityPool.Contract;
import sk.hackcraft.bwu.resource.FirstTakeEntityPool;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class CreepBot extends AbstractBot
{
	private static int wins, games;
	
	public static void main(String[] args)
	{
		while (true)
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
	}
	
	private final EnvironmentTime time;
	
	private final List<Updateable> updateables;
	private final List<Drawable> drawables;
	
	private final EntityPool<Unit> unitsPool;

	private final MapResourcesAgent mapResourcesAgent;
	private final LarvaProductionAgent productionAgent;
	
	private BuildingConstructionAgent constructionAgent;
	private boolean spawningPoolBuilt;
	
	private final Map<Position, Unit> enemyBuildings;
	
	private Layer plainsLayer;
	private UnitsLayer unitsLayer;
	private LayerDrawable plainsLayerDrawable;
	
	// visualizaton
	
	SwingLayersVisualization visualization;
	LayersPainter layersPainter;
	
	public CreepBot(Game game)
	{
		super(game);
		
		time = new GameEnvironmentTime(game);
		
		updateables = new ArrayList<>();
		drawables = new ArrayList<>();
		
		unitsPool = new FirstTakeEntityPool<>();
		
		{
			Contract<Unit> contract = unitsPool.createContract("MapResources");
			mapResourcesAgent = new MapResourcesAgent(bwapi, contract);
			updateables.add(mapResourcesAgent);
			drawables.add(mapResourcesAgent);
		}
		
		{
			Contract<Unit> contract = unitsPool.createContract("Production");
			productionAgent = new LarvaProductionAgent(contract);
			updateables.add(productionAgent);
			drawables.add(productionAgent);
		}
		
		enemyBuildings = new HashMap<>();
	}

	@Override
	public void gameStarted()
	{
		jnibwapi.Map map = game.getMap();
		LayerDimension dimension = Convert.toLayerDimension(map.getSize());
				
		layersPainter = new LayersPainter(dimension);
		visualization = new SwingLayersVisualization(layersPainter);
		
		// ---
		
		game.enableUserInput();
		game.setSpeed(0);

		UnitSet myUnits = game.getMyUnits();
		for (Unit unit : myUnits)
		{
			unitsPool.add(unit);
		}
		
		Position startLocation = game.getSelf().getStartLocation();
		
		{
			EntityPool.Contract<Unit> contract = unitsPool.createContract("BuildingConstruction");
			constructionAgent = new BuildingConstructionAgent(bwapi, time, startLocation, contract);
			updateables.add(constructionAgent);
			drawables.add(constructionAgent);
		}
		
		// map layers
		
		int maxDistance = 5;
		
		plainsLayer = GameLayerFactory.createLowResWalkableLayer(game.getMap());
		
		BorderLayerProcessor borderLayerProcessor = new BorderLayerProcessor(2, 0);
		Layer bordersLayer = borderLayerProcessor.process(plainsLayer);
		
		plainsLayer = plainsLayer.add(bordersLayer);

		HashMap<Integer, Integer> changeMap = new HashMap<>();
		changeMap.put(2, maxDistance);
		changeMap.put(0, maxDistance + 1);
		changeMap.put(1, 0);
		plainsLayer = new ValuesChangerLayerProcessor(changeMap).process(plainsLayer);

		Set<LayerPoint> startPoints = Layers.getPointsWithValue(plainsLayer, maxDistance);
		GradientFloodFillProcessor gradientFloofFillProcessor = new GradientFloodFillProcessor(startPoints, -1)
		{
			@Override
			protected boolean fillCell(int cellValue, int newValue)
			{
				return cellValue < newValue;
			}
		};
		
		plainsLayer = gradientFloofFillProcessor.process(plainsLayer);
		
		TreeMap<Integer, BWColor> colors = new TreeMap<>();
		colors.put(maxDistance + 1, BWColor.Red);
		colors.put(maxDistance, BWColor.Orange);
		colors.put(1, BWColor.Green);
		colors.put(0, BWColor.Blue);
		ColorAssigner<BWColor> colorAssigner = new MapGradientColorAssignment<>(colors);
		plainsLayerDrawable = new LayerColorDrawable(plainsLayer, jnibwapi.Map.BUILD_TILE_SIZE, colorAssigner);
		
		TreeMap<Integer, Color> colors2 = new TreeMap<>();
		colors2.put(maxDistance + 1, Color.RED);
		colors2.put(maxDistance, Color.ORANGE);
		colors2.put(1, Color.GREEN);
		colors2.put(0, Color.BLUE);
		ColorAssigner<Color> colorAssigner2 = new MapGradientColorAssignment<>(colors2);
		//layersPainter.addLayer(plainsLayer, colorAssigner2);
		
		visualization.start();

		unitsLayer = new UnitsLayer(dimension, game);
		
		TreeMap<Integer, Color> colors3 = new TreeMap<>();
		colors3.put(UnitsLayer.MINE, Color.GREEN);
		colors3.put(UnitsLayer.ALLY, Color.BLUE);
		colors3.put(UnitsLayer.ENEMY, Color.RED);
		colors3.put(UnitsLayer.NEUTRAL, Color.GRAY);
		colors3.put(0, new Color(0, 0, 0, 0));
		ColorAssigner<Color> colorAssigner3 = new MapExactColorAssigner<>(colors3);
		//layersPainter.addLayer(unitsLayer, colorAssigner3);
		
		Layer resourcesPartitioningLayer = GameLayerFactory.createLowResWalkableLayer(map);
		List<BaseLocation> baseLocations = map.getBaseLocations();
		final Set<LayerPoint> startPoints2 = new HashSet<>();
		Map<Integer, BaseLocation> baseLocationsIndex = new HashMap<>();
		int value = 2;
		for (BaseLocation baseLocation : baseLocations)
		{
			Position position = baseLocation.getCenter();
			
			if (!position.isValid())
			{
				continue;
			}
			
			LayerPoint point = Convert.toLayerPoint(position);
			
			startPoints2.add(point);
			
			resourcesPartitioningLayer.set(point, value);
			baseLocationsIndex.put(value, baseLocation);
			value++;
		}
		
		resourcesPartitioningLayer = new ValueFloodFillProcessor(startPoints2, 1).process(resourcesPartitioningLayer);
		Map<BaseLocation, Set<Unit>> resourceClusters = new HashMap<>();
		for (Unit unit : game.getStaticNeutralUnits().where(UnitSelector.IS_RESOURCE))
		{
			LayerPoint coordinates = Convert.toLayerPoint(unit.getPosition());
			
			int resourceGroup = resourcesPartitioningLayer.get(coordinates);
			BaseLocation baseLocation = baseLocationsIndex.get(resourceGroup);
			
			if (!resourceClusters.containsKey(baseLocation))
			{
				resourceClusters.put(baseLocation, new HashSet<Unit>());
			}
			
			resourceClusters.get(baseLocation).add(unit);
		}

		for (Map.Entry<BaseLocation, Set<Unit>> entry : resourceClusters.entrySet())
		{
			Position positon = entry.getKey().getPosition();
			UnitSet resources = new UnitSet(entry.getValue());
			ExpandInfo expand = new ExpandInfo(positon, resources);
			mapResourcesAgent.addExpand(expand);
		}
		
		Random r = new Random();
		ColorAssigner<Color> randomColorAssigner = new RandomColorAssigner<Color>(r)
		{
			@Override
			protected Color createColor(int r, int g, int b)
			{
				return new Color(r, g, b);
			}
		};
		layersPainter.addLayer(resourcesPartitioningLayer, randomColorAssigner);
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		visualization.close();
		
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
		game.setSpeed(0);
		bwapi.setFrameSkip(0);
		
		bwapi.drawText(new Position(10, 10), "Frame: " + game.getFrameCount(), true);
		
		for (Updateable updateable : updateables)
		{
			updateable.update();
		}
		
		//////
		
		if (game.getFrameCount() % 500 == 0 && game.getSelf().getMinerals() >= 300)
		{
			//mapResourcesAgent.spawnMiningOperation();
		}
		
		if (!spawningPoolBuilt && game.getMyUnits().where(UnitSelector.IS_WORKER).size() >= 8)
		{
			constructionAgent.construct(UnitTypes.Zerg_Spawning_Pool, new BuildingConstructionAgent.ConstructionListener()
			{
				@Override
				public void onFailed()
				{
				}
			}, true);
			
			spawningPoolBuilt = true;
		}
		
		int availableMinerals = game.getSelf().getMinerals(); 
		UnitSet workers = game.getMyUnits().where(UnitSelector.IS_WORKER);
		UnitSet spawningPools = game.getMyUnits().whereType(UnitTypes.Zerg_Spawning_Pool);
		if ((workers.size() <= 8 && spawningPools.isEmpty()) || !spawningPools.isEmpty())
		{
			if ((availableMinerals >= 50 && game.getFrameCount() < 3000) || availableMinerals >= 400)
			{
				boolean zerglings = new Random().nextBoolean();
				
				UnitType type;
				if (zerglings && !spawningPools.isEmpty() || workers.size() > mapResourcesAgent.getWorkersDeficit() + 5)
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
		UnitSet overlords = game.getMyUnits().whereType(UnitTypes.Zerg_Overlord);
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
				enemyBuildings.put(unit.getPosition(), unit);
			}
		}
		
		// check outdated attack
		Map<Position, Unit> enemyBuildingsCopy = new HashMap<>(enemyBuildings);
		for (Position position : enemyBuildingsCopy.keySet())
		{
			if (bwapi.isVisible(position))
			{
				Unit unit = enemyBuildings.get(position);
				
				if (!unit.isExists())
				{
					enemyBuildings.remove(position);
				}
				else
				{
					bwapi.drawText(position, unit.isExists() + " " + unit.getHitPoints(), false);
				}
			}
		}

		UnitSet zerglings = game.getMyUnits().whereType(UnitTypes.Zerg_Zergling);
		
		bwapi.drawText(new Position(10, 30), "Zerglings: " + zerglings.size(), true);
		
		UnitSet buildingsUnderAttack = game.getMyUnits().where(UnitSelector.IS_BUILDING).where(UnitSelector.IS_UNDER_ATTACK);

		if (!buildingsUnderAttack.isEmpty())
		{
			game.setSpeed(25);
			
			Position defendPosition = buildingsUnderAttack.iterator().next().getPosition();
			UnitSet enemies = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(Convert.toPositionVector(defendPosition)), 1000);

			Position attackPos = (!enemies.isEmpty()) ? enemies.iterator().next().getPosition() : defendPosition;

			for (Unit zergling : zerglings)
			{	
				if (zergling.isIdle())
				{
					zergling.attack(attackPos, false);
				}
			}
		}
		else if (!enemyBuildings.isEmpty() && zerglings.size() > 150)
		{
			Position attackPosition = enemyBuildings.keySet().iterator().next();
			
			for (Unit zergling : zerglings)
			{
				if (zergling.isIdle())
				{
					zergling.attack(attackPosition, false);
				}
			}
		}
		else
		{
			Position attackPosition = game.getMap().getSize();
			attackPosition = new Position(attackPosition.getPX() / 2, attackPosition.getPY() / 2);
			
			for (Unit zergling : zerglings)
			{
				if (zergling.isIdle())
				{
					zergling.attack(attackPosition, false);
				}
			}
		}
		
		unitsLayer.update();
		
		layersPainter.update();
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
		if (unit.getPlayer().isSelf())
		{
			unitsPool.remove(unit);
		}
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
		if (unit.getPlayer().isSelf())
		{
			unitsPool.add(unit);
		}
	}

	@Override
	public void unitMorphed(Unit unit)
	{
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
		unitsPool.remove(unit);
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
