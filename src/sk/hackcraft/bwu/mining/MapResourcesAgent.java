package sk.hackcraft.bwu.mining;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jnibwapi.BaseLocation;
import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.Position.PosType;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class MapResourcesAgent implements Updateable, Drawable
{
	private final JNIBWAPI bwapi;
	
	private final Set<MiningAgent> miningAgents;
	private final Map<Unit, MiningAgent> resourceDepotsAssignments;
	
	private final Set<ExpandInfo> expandsInformations;
	
	private final Set<ExpandInfo> freeExpands;
	private final Map<Unit, ExpandInfo> spawningMap;
	
	private final Set<Unit> freeWorkers;
	private final Set<Unit> workers;
	
	public MapResourcesAgent(JNIBWAPI bwapi)
	{
		this.bwapi = bwapi;
		
		miningAgents = new HashSet<>();
		resourceDepotsAssignments = new HashMap<>();
		
		expandsInformations = new HashSet<>();
		
		freeExpands = new HashSet<>();
		spawningMap = new HashMap<>();
		
		freeWorkers = new HashSet<>();
		workers = new HashSet<>();
	}
	
	public void addExpand(ExpandInfo expand)
	{
		expandsInformations.add(expand);
		freeExpands.add(expand);
	}
	
	public void addWorker(Unit worker)
	{
		freeWorkers.add(worker);
		workers.add(worker);
	}
	
	public Set<ExpandInfo> getExpandInfos()
	{
		return expandsInformations;
	}
	
	public Set<MiningAgent> getMiningAgents()
	{
		return miningAgents;
	}
	
	public boolean spawnMiningOperation(Unit worker)
	{		
		ExpandInfo targetExpand = getNearestExpand(worker.getPosition(), freeExpands);
		
		if (targetExpand != null)
		{			
			spawningMap.put(worker, targetExpand);
			freeExpands.remove(targetExpand);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private ExpandInfo getNearestExpand(Position position, Collection<ExpandInfo> expands)
	{
		ExpandInfo nearestExpand = null;
		double nearestDistance = Double.POSITIVE_INFINITY;
		
		for (ExpandInfo expand : expands)
		{
			Position expandPosition = expand.getPosition();
			
			jnibwapi.Map map = bwapi.getMap();
			
			if (!map.isConnected(position, expandPosition))
			{
				continue;
			}
			
			double distance = map.getGroundDistance(position, expandPosition);
			
			if (distance < nearestDistance)
			{
				nearestDistance = distance;
				nearestExpand = expand;
			}
		}
		
		return nearestExpand;
	}
	
	@Override
	public void update()
	{
		Set<Unit> workersCopy = new HashSet<>(workers);
		for (Unit worker : workersCopy)
		{
			if (!worker.isExists())
			{
				workers.remove(worker);
				freeWorkers.remove(worker);
				
				for (MiningAgent agent : miningAgents)
				{
					agent.removeMiner(worker);
				}
			}
		}
		checkFreeExpands();
		
		if (bwapi.getSelf().getMinerals() >= 300)
		{
			Map<Unit, ExpandInfo> spawningMapCopy = new HashMap<>(spawningMap);
			for (Map.Entry<Unit, ExpandInfo> entry : spawningMapCopy.entrySet())
			{
				Unit worker = entry.getKey();
				ExpandInfo expandInfo = entry.getValue();
				Position expandPosition = expandInfo.getPosition();
				
				if (worker.isIdle())
				{
					worker.move(expandPosition, false);
				}
				
				Position workerPosition = worker.getPosition();
				if (workerPosition.getPDistance(expandPosition) < 30 && !worker.isConstructing())
				{
					UnitType resourceDepotType = UnitTypes.Zerg_Hatchery;
					
					worker.build(expandPosition, resourceDepotType);
					spawningMap.remove(worker);
				}
			}
		}
		
		UnitSet resourceDepots = new UnitSet(bwapi.getMyUnits()).where(UnitSelector.IS_RESOURCE_DEPOT);
		for (Unit resourceDepot : resourceDepots)
		{
			if (resourceDepotsAssignments.containsKey(resourceDepot))
			{
				continue;
			}
			
			ExpandInfo expand = getNearestExpand(resourceDepot.getPosition(), expandsInformations);
			
			if (expand == null)
			{
				continue;
			}
			
			MiningAgent agent = new MiningAgent(resourceDepot, expand.getResources());
			miningAgents.add(agent);
			resourceDepotsAssignments.put(resourceDepot, agent);
			freeExpands.remove(expand);
		}
		
		for (MiningAgent agent : miningAgents)
		{
			int saturationDifference = agent.getSaturationDifference();
			if (saturationDifference > 0)
			{
				if (!freeWorkers.isEmpty())
				{
					Unit worker = freeWorkers.iterator().next();
					freeWorkers.remove(worker);
					
					agent.addMiner(worker);
				}
			}
			else if (saturationDifference < 0)
			{
				Set<Unit> agentFreeWorkers = agent.getFreeMiners();
				for (Unit worker : agentFreeWorkers)
				{
					agent.removeMiner(worker);
					freeWorkers.add(worker);
				}
			}

			agent.update();
		}
	}
	
	@Override
	public void draw(Graphics graphics)
	{
		for (MiningAgent miningAgent : miningAgents)
		{
			miningAgent.draw(graphics);
		}
	}
	
	private void checkFreeExpands()
	{
		Set<ExpandInfo> freeExpandsCopy = new HashSet<>(freeExpands);
		for (ExpandInfo expand : freeExpandsCopy)
		{
			Position position = expand.getPosition();
			
			if (bwapi.isVisible(position))
			{
				List<Unit> units = bwapi.getUnitsOnTile(position);
				
				boolean enemyBuilding = false;
				for (Unit unit : units)
				{
					if (unit.getPlayer().isEnemy())
					{
						if (unit.getType().isBuilding())
						{
							enemyBuilding = true;
							break;
						}
					}
				}
				
				if (enemyBuilding)
				{
					freeExpands.remove(expand);
				}
			}
		}
	}

	public static class ExpandInfo
	{
		private final Position positon;
		private final UnitSet resources;
		
		private boolean baseOperational;
		private boolean baseConstructed;
		private final boolean mineralsAvailable;
		private final boolean gasAvailable;
		private boolean mineralsExhausted;
		private boolean gasDepleted;

		public ExpandInfo(Position position, UnitSet resources)
		{
			this.positon = position;
			this.resources = resources;
			
			UnitSet minerals = resources.where(UnitSelector.IS_MINERAL);
			mineralsAvailable = !minerals.isEmpty();
						
			UnitSet geysers = resources.where(UnitSelector.IS_VESPENE_GEYSER);
			gasAvailable = !geysers.isEmpty();
		}
		
		public Position getPosition()
		{
			return positon;
		}
		
		public UnitSet getResources()
		{
			return resources;
		}
		
		public void setBaseConstructed(boolean baseConstructed)
		{
			this.baseConstructed = baseConstructed;
		}
		
		public void setBaseOperational(boolean baseOperational)
		{
			this.baseOperational = baseOperational;
		}
		
		public void setGasDepleted(boolean gasDepleted)
		{
			this.gasDepleted = gasDepleted;
		}
		
		public void setMimneralsExhausted(boolean mimneralsExhausted)
		{
			this.mineralsExhausted = mimneralsExhausted;
		}
		
		public boolean isBaseOperational()
		{
			return baseOperational;
		}
		
		public boolean isBaseConstructed()
		{
			return baseConstructed;
		}
		
		public boolean isMineralsAvailable()
		{
			return mineralsAvailable;
		}
		
		public boolean isGasAvailable()
		{
			return gasAvailable;
		}
		
		public boolean isGasDepleted()
		{
			return gasDepleted;
		}
		
		public boolean isMimneralsExhausted()
		{
			return mineralsExhausted;
		}
	}

	public int getWorkersDeficit()
	{
		int deficit = 0;
		for (MiningAgent agent : miningAgents)
		{
			deficit += agent.getSaturationDifference();
		}
		
		return deficit;
	}
}
