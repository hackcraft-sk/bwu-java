package sk.hackcraft.bwu.maplayer.layers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.SetUtil;
import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.processors.FloodFillProcessor;
import sk.hackcraft.bwu.util.IdGenerator;

public class BuildingsLayer extends MatrixLayer
{
	private final IdGenerator idGenerator;
	
	private final Set<Unit> buildings;
	private final Map<UnitType, Integer> buildingTypeIdsMap;
	
	public BuildingsLayer(LayerDimension dimension)
	{
		super(dimension);
	
		this.idGenerator = new IdGenerator();
		
		this.buildings = new HashSet<>();
		this.buildingTypeIdsMap = new HashMap<UnitType, Integer>();
	}
	
	public Set<Unit> getAllBuildings()
	{
		return buildings;
	}
	
	public void addBuilding(Unit building)
	{
		UnitType buildingType = building.getType();
		
		int id = assignId(buildingType);
		
		LayerPoint position = Convert.toLayerPoint(building.getPosition());
		LayerDimension size = new LayerDimension(buildingType.getTileWidth(), buildingType.getTileHeight());
		paintBuilding(position, size, id);
		
		buildings.add(building);
	}
	
	public void removeBuilding(Position position)
	{
		LayerPoint layerPosition = Convert.toLayerPoint(position);
		
		FloodFillProcessor.fillValue(this, SetUtil.create(layerPosition), 0);
	}
	
	private int assignId(UnitType buildingType)
	{
		if (!buildingTypeIdsMap.containsKey(buildingType))
		{
			int id = idGenerator.generateId();
			buildingTypeIdsMap.put(buildingType, id);
		}
		
		return buildingTypeIdsMap.get(buildingType);
	}
	
	private void paintBuilding(LayerPoint position, LayerDimension size, int value)
	{
		for (int by = 0; by < size.getHeight(); by++)
		{
			for (int bx = 0; bx < size.getWidth(); bx++)
			{
				int x = bx + position.getX();
				int y = by + position.getY();
				
				set(new LayerPoint(x, y), value);
			}
		}
	}
	
	public class BuildingInfo
	{
		private UnitType buildingType;
		private Player owner;
		private LayerPoint position;
		
		public BuildingInfo(UnitType buildingType, Player owner, LayerPoint position)
		{
			this.buildingType = buildingType;
			this.owner = owner;
			this.position = position;
		}
		
		public UnitType getBuildingType()
		{
			return buildingType;
		}
		
		public Player getOwner()
		{
			return owner;
		}
		
		public LayerPoint getPosition()
		{
			return position;
		}
	}
}
