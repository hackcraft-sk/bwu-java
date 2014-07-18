package sk.hackcraft.bwu.buildplan;

import jnibwapi.Map;
import jnibwapi.Position;
import jnibwapi.types.UnitType;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.SetUtil;
import sk.hackcraft.bwu.grid.GameLayerFactory;
import sk.hackcraft.bwu.grid.Grid;
import sk.hackcraft.bwu.grid.GridUtil;
import sk.hackcraft.bwu.grid.grids.MatrixGrid;
import sk.hackcraft.bwu.grid.processors.FloodFillProcessor;

// TODO
public class BuildingsPositionFinder
{
	public static BuildingsPositionFinder createFromGameData(Position baseCenter, Map map, Grid buildableLayer)
	{		
		Grid groundDistanceLayer = GameLayerFactory.createLowResWalkableLayer(map);
		
		FloodFillProcessor.fillGradient(groundDistanceLayer, SetUtil.create(Convert.toLayerPoint(baseCenter)));
		
		return new BuildingsPositionFinder(buildableLayer, groundDistanceLayer);
	}

	private final Grid buildableLayer, groundDistanceLayer, buildingsLayer;
	
	public BuildingsPositionFinder(Grid buildableLayer, Grid groundDistanceLayer)
	{
		this.buildableLayer = buildableLayer;
		this.groundDistanceLayer = groundDistanceLayer;
		
		buildingsLayer = new MatrixGrid(buildableLayer.getDimension());
		GridUtil.copy(buildableLayer, buildingsLayer);
	}
	
	public Position find(UnitType buildingType)
	{
		// TODO
		return null;
	}
}
