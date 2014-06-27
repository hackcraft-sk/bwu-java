package sk.hackcraft.bwu.buildplan;

import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.SetCreator;
import sk.hackcraft.bwu.maplayer.GameLayerFactory;
import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.processors.GradientFloodFillProcessor;
import jnibwapi.Map;
import jnibwapi.Position;
import jnibwapi.types.UnitType;

public class BuildingsGroundPlanner
{
	public static BuildingsGroundPlanner createFromGameData(Position baseCenter, Map map)
	{
		// modify in that way, that buildable layer will be update with creep or another buildings
		Layer buildableLayer = GameLayerFactory.createBuildableLayer(map);
		
		Layer groundDistanceLayer = GameLayerFactory.createLowResWalkableLayer(map);
		
		GradientFloodFillProcessor.floodFill(groundDistanceLayer, SetCreator.create(Convert.toLayerPoint(baseCenter)));
		
		return new BuildingsGroundPlanner(buildableLayer, groundDistanceLayer);
	}
	
	private final Layer buildableLayer, groundDistanceLayer;
	
	public BuildingsGroundPlanner(Layer buildableLayer, Layer groundDistanceLayer)
	{
		this.buildableLayer = buildableLayer;
		this.groundDistanceLayer = groundDistanceLayer;
	}
	
	public Position getBuildPositionFor(UnitType buildingType)
	{
		// TODO
		return null;
	}
}
