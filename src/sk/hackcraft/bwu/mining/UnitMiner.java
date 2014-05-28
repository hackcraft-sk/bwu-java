package sk.hackcraft.bwu.mining;

import java.util.Map;

import jnibwapi.JNIBWAPI;
import jnibwapi.types.OrderType;
import jnibwapi.types.OrderType.OrderTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.mining.MiningAgent.Miner;
import sk.hackcraft.bwu.mining.MiningAgent.Resource;

public class UnitMiner implements Miner
{
	private JNIBWAPI bwapi = JNIBWAPI.getInstance();

	private final Unit unit;
	
	private final Map<Resource, Unit> resources;
	private Resource resource;
	
	public UnitMiner(Unit unit, Map<Resource, Unit> resources)
	{
		this.unit = unit;
		
		this.resources = resources;
	}
	
	@Override
	public void update()
	{
		if (unit.isIdle())
		{
			jnibwapi.Unit target = getResourceUnit();
			
			if (target != null)
			{
				unit.rightClick(target, false);
			}
			
			return;
		}
		
		if (getState() == State.MOVING_TO_RESOURCE)
		{
			Resource assignedResource = getAssignedResource();
			jnibwapi.Unit resourceUnit = resources.get(assignedResource);
			
			jnibwapi.Unit orderTarget = unit.getOrderTarget();
			
			if (resourceUnit != orderTarget)
			{
				unit.rightClick(resourceUnit, false);
				return;
			}
		}
		
		// TODO debug drawings
		State state = getState();

		if (state == State.MOVING_TO_RESOURCE)
		{
			Resource assignedResource = getAssignedResource();
			
			// hack, sometimes jnibwapi is messing up orders, targets, etc
			if (assignedResource != null)
			{
				jnibwapi.Unit resourceUnit = resources.get(assignedResource);
				
				bwapi.drawLine(unit.getPosition(), resourceUnit.getPosition(), BWColor.Blue, false);
			}
		}
		
		if (getState() == State.RETURNING_RESOURCES)
		{
			jnibwapi.Unit target = unit.getOrderTarget();
			
			if(target != null)
			{
				bwapi.drawLine(unit.getPosition(), target.getPosition(), BWColor.Green, false);
			}
		}
		
		if (getState() == State.WAITING_FOR_RESOURCE)
		{
			bwapi.drawCircle(unit.getPosition(), 3, BWColor.Yellow, true, false);
		}
		
		if (getState() == State.MINING_RESOURCE)
		{
			bwapi.drawCircle(unit.getPosition(), 3, BWColor.Green, true, false);
		}
	}
	
	private jnibwapi.Unit getResourceUnit()
	{
		return resources.get(resource);
	}

	@Override
	public Resource getAssignedResource()
	{
		return resource;
	}

	@Override
	public void setResource(Resource resource)
	{
		this.resource = resource;
	}

	@Override
	public State getState()
	{
		OrderType orderType = unit.getOrder();
		
		if (orderType == OrderTypes.MoveToMinerals || orderType == OrderTypes.MoveToGas)
		{
			return State.MOVING_TO_RESOURCE;
		}
		
		if (orderType == OrderTypes.WaitForMinerals || orderType == OrderTypes.WaitForGas)
		{
			return State.WAITING_FOR_RESOURCE;
		}
		
		if (orderType == OrderTypes.MiningMinerals || orderType == OrderTypes.HarvestGas)
		{
			return State.MINING_RESOURCE;
		}
		
		if (orderType == OrderTypes.ReturnMinerals || orderType == OrderTypes.ReturnGas)
		{
			return State.RETURNING_RESOURCES;
		}
		
		return State.UNDEFINED;
	}

	@Override
	public boolean isWorking()
	{
		return !unit.isIdle();
	}

	@Override
	public boolean canWork()
	{
		return unit.getHitPoints() > 0;
	}
	
	@Override
	public String toString()
	{
		return "UnitMiner #" + unit.getID();
	}
}
