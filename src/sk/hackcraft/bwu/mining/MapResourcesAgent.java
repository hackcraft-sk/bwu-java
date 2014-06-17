package sk.hackcraft.bwu.mining;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.types.UnitType.UnitTypes;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.production.BuildingConstructionAgent;
import sk.hackcraft.bwu.production.BuildingConstructionAgent.ConstructionListener;
import sk.hackcraft.bwu.resource.EntityPool.Contract;
import sk.hackcraft.bwu.resource.EntityPool.ContractListener;
import sk.hackcraft.bwu.selection.Pickers;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class MapResourcesAgent implements Updateable, Drawable
{
	private final JNIBWAPI bwapi;
	private final Contract<Unit> unitsContract;
	private final BuildingConstructionAgent buildingConstructionAgent;
	private final UnitType resourceDepotType;
	
	private final Set<MiningAgent> miningAgents;
	
	private final Set<ExpandInfo> expandsInformations;
	
	private final Set<ExpandInfo> freeExpands;
	private final Set<ExpandInfo> dangerousExpands;
	
	private Position mainBasePosition;
	
	public MapResourcesAgent(JNIBWAPI bwapi, Contract<Unit> unitsContract, BuildingConstructionAgent buildingConstructionAgent, UnitType resourceDepotType)
	{
		this.bwapi = bwapi;
		this.unitsContract = unitsContract;
		this.buildingConstructionAgent = buildingConstructionAgent;
		this.resourceDepotType = resourceDepotType;
		
		miningAgents = new HashSet<>();
		
		expandsInformations = new HashSet<>();
		dangerousExpands = new HashSet<>();
		
		freeExpands = new HashSet<>();
	}
	
	public void setMainBasePosition(Position mainBasePosition)
	{
		this.mainBasePosition = mainBasePosition;
	}
	
	public void addExpand(ExpandInfo expand)
	{
		expandsInformations.add(expand);
		freeExpands.add(expand);
	}
	
	public Set<ExpandInfo> getExpandInfos()
	{
		return expandsInformations;
	}
	
	public Set<MiningAgent> getMiningAgents()
	{
		return miningAgents;
	}
	
	public boolean spawnMiningOperation()
	{
		final ExpandInfo targetExpand = getNearestExpand(mainBasePosition, getFreeSafeExpands());
		
		if (targetExpand != null)
		{
			ConstructionListener listener = new ConstructionListener()
			{
				private Unit resurceDepot;
				
				@Override
				public void failed()
				{
					dangerousExpands.add(targetExpand);
				}

				@Override
				public void buildingCreated(Unit building)
				{
					this.resurceDepot = building;
				}

				@Override
				public void finished()
				{
					MiningAgent agent = new MiningAgent(bwapi ,resurceDepot, targetExpand.getResources());
					miningAgents.add(agent);
				}
			};
			
			buildingConstructionAgent.construct(resourceDepotType, listener, true, targetExpand.getPosition(), null);
			freeExpands.remove(targetExpand);
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean spawnMiningOperation(Unit resourceDepot)
	{
		final ExpandInfo targetExpand = getNearestExpand(mainBasePosition, getFreeSafeExpands());
		
		if (targetExpand == null)
		{
			return false;
		}
		
		MiningAgent agent = new MiningAgent(bwapi, resourceDepot, targetExpand.getResources());
		miningAgents.add(agent);
		
		freeExpands.remove(targetExpand);
		
		return true;
	}
	
	private Set<ExpandInfo> getFreeSafeExpands()
	{
		Set<ExpandInfo> expands = new HashSet<>(freeExpands);
		for (ExpandInfo dangerousExpand : dangerousExpands)
		{
			expands.remove(dangerousExpand);
		}
		
		return expands;
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
		checkFreeExpands();
		
		Set<MiningAgent> miningAgentsCopy = new HashSet<>(miningAgents);
		for (final MiningAgent agent : miningAgentsCopy)
		{
			Unit resourceDepot = agent.getResourceDepot();
			if (!resourceDepot.isExists())
			{
				Set<Unit> miners = agent.getMiners();
				for (Unit miner : miners)
				{
					unitsContract.returnEntity(miner);
				}

				miningAgents.remove(agent);
				
				Position position = resourceDepot.getPosition();
				ExpandInfo expand = getNearestExpand(position, expandsInformations);
				freeExpands.add(expand);
			}

			int saturationDeficit = agent.getSaturationDeficit();
			if (saturationDeficit > 0)
			{
				UnitSet workers = new UnitSet(unitsContract.getAcquirableEntities(false)).where(UnitSelector.IS_WORKER);
				
				if (!workers.isEmpty())
				{
					Unit worker = workers.pick(Pickers.FIRST);
					
					ContractListener<Unit> listener = new ContractListener<Unit>()
					{
						@Override
						public void entityRemoved(Unit entity)
						{
							agent.removeMiner(entity);
						}
					};
					unitsContract.requestEntity(worker, listener, false);
					
					agent.addMiner(worker);
				}
			}
			else if (saturationDeficit < 0)
			{
				Set<Unit> agentFreeWorkers = new HashSet<>(agent.getFreeMiners());
				for (Unit worker : agentFreeWorkers)
				{
					unitsContract.returnEntity(worker);
					agent.removeMiner(worker);
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
			deficit += agent.getSaturationDeficit();
		}
		
		return deficit;
	}
}
