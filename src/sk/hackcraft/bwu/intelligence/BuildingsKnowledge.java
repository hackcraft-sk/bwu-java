package sk.hackcraft.bwu.intelligence;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.SetUtil;
import sk.hackcraft.bwu.grid.Grid;
import sk.hackcraft.bwu.grid.GridDimension;
import sk.hackcraft.bwu.grid.GridPoint;
import sk.hackcraft.bwu.grid.grids.SparseLayer;
import sk.hackcraft.bwu.grid.processors.FloodFillProcessor;
import sk.hackcraft.bwu.util.IdGenerator;

public class BuildingsKnowledge
{
	private final JNIBWAPI bwapi;
	
	private final IdGenerator idGenerator;
	
	private final Map<Unit, BuildingInfo> buildings;
	private final Map<UnitType, Integer> buildingTypeCounts;
	
	private final Grid buildingsLayer;
	
	public BuildingsKnowledge(GridDimension dimension, JNIBWAPI bwapi)
	{
		buildingsLayer = new SparseLayer(dimension);
		
		this.bwapi = bwapi;
	
		this.idGenerator = new IdGenerator();

		this.buildings = new HashMap<>();
		this.buildingTypeCounts = new HashMap<>();
	}
	
	public Set<Unit> getAllBuildings()
	{
		return buildings.keySet();
	}
	
	public void addBuilding(int timestamp, Unit building)
	{
		buildings.put(building, new BuildingInfo(timestamp, building));
		paintBuilding(building);
		
		Integer buildingTypeCount = buildingTypeCounts.get(building);
		
		if (buildingTypeCount == null)
		{
			buildingTypeCount = 0;
		}
		
		buildingTypeCounts.put(building.getType(), ++buildingTypeCount);
	}
	
	public void removeBuilding(Unit building)
	{
		BuildingInfo info = buildings.get(building);
		Position position = info.getPosition();
		unpaintBuilding(position);
		
		buildings.remove(building);
		
		UnitType buildingType = building.getType();
		int buildingTypeCount = buildingTypeCounts.get(buildingType);
		buildingTypeCounts.put(buildingType, --buildingTypeCount);
	}
	
	public void paintBuilding(Unit building)
	{
		UnitType buildingType = building.getType();

		GridPoint position = Convert.toLayerPoint(building.getPosition());
		GridDimension size = new GridDimension(buildingType.getTileWidth(), buildingType.getTileHeight());
		paintBuilding(position, size, building.getID());
	}
	
	public void unpaintBuilding(Position position)
	{
		GridPoint layerPosition = Convert.toLayerPoint(position);
		
		FloodFillProcessor.fillValue(buildingsLayer, SetUtil.create(layerPosition), 0);
	}
	
	private void paintBuilding(GridPoint position, GridDimension size, int value)
	{
		for (int by = 0; by < size.getHeight(); by++)
		{
			for (int bx = 0; bx < size.getWidth(); bx++)
			{
				int x = bx + position.getX();
				int y = by + position.getY();
				
				buildingsLayer.set(x, y, value);
			}
		}
	}
	
	public class BuildingInfo
	{
		private final int timestamp;
		
		private final UnitType buildingType;
		private final Player owner;
		private final Position position;
		
		public BuildingInfo(int timestamp, Unit building)
		{
			this.timestamp = timestamp;
			
			this.buildingType = building.getType();
			this.owner = building.getPlayer();
			this.position = building.getPosition();
		}
		
		public int getLastUpdate()
		{
			return timestamp;
		}
		
		public UnitType getBuildingType()
		{
			return buildingType;
		}
		
		public Player getOwner()
		{
			return owner;
		}
		
		public Position getPosition()
		{
			return position;
		}
	}
}
