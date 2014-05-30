package sk.hackcraft.bwu.mining;

import java.util.Map;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position.PosType;
import jnibwapi.types.OrderType;
import jnibwapi.types.OrderType.OrderTypes;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2DMath;
import sk.hackcraft.bwu.mining.MiningAgent.Miner;
import sk.hackcraft.bwu.mining.MiningAgent.Resource;

public class UnitMiner implements Miner, Updateable, Drawable
{
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
			// TODO add mechanics when mineral is not visible
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
	}

	@Override
	public void draw(Graphics graphics)
	{
		State state = getState();

		switch (state)
		{
			case MOVING_TO_RESOURCE:
				Resource assignedResource = getAssignedResource();

				jnibwapi.Unit resourceUnit = resources.get(assignedResource);

				graphics.setColor(BWColor.Blue);
				graphics.drawLine(unit.getPositionVector(), Vector2DMath.toVector(resourceUnit.getPosition(), PosType.PIXEL));
				break;
			case RETURNING_RESOURCES:
				jnibwapi.Unit target = unit.getOrderTarget();

				graphics.setColor(BWColor.Green);
				graphics.drawLine(unit.getPositionVector(), Vector2DMath.toVector(target.getPosition(), PosType.PIXEL));
				break;
			case WAITING_FOR_RESOURCE:
				graphics.setColor(BWColor.Yellow);
				graphics.fillCircle(unit, 3);
				break;
			case MINING_RESOURCE:
				graphics.setColor(BWColor.Green);
				graphics.fillCircle(unit, 3);
				break;
			default:
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

		if (unit.getOrderTarget() == null)
		{
			return State.UNDEFINED;
		}

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
