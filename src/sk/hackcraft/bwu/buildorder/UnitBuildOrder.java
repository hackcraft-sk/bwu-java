package sk.hackcraft.bwu.buildorder;

import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class UnitBuildOrder implements BuildOrder
{
	private int amount;
	private UnitType unitType;
	
	public UnitBuildOrder(UnitType unitType)
	{
		this(unitType, 1);
	}
	
	public UnitBuildOrder(UnitType unitType, int amount)
	{
		this.unitType = unitType;
		this.amount = amount;
	}

	private int getAchievedAmount(BuildOrderContext context)
	{
		return context.getGame().getMyUnits().whereType(unitType).size();
	}
	
	@Override
	public boolean isAchieved(BuildOrderContext context)
	{
		return getAchievedAmount(context) >= amount;
	}

	@Override
	public void tryToAchieve(BuildOrderContext context)
	{
		int current = getAchievedAmount(context);
		int toAchieve = amount - current;
		
		for(int i=0; i < toAchieve; ++i)
		{
			if(unitType.isBuilding())
			{
				
				
				context.getBuildingConstructionAgent().construct(unitType, new ConstructionListener()
				{
					
					@Override
					public void finished()
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void failed()
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void buildingCreated(Unit building)
					{
						// doesn't matter to me
					}
				}, false, null, null);
			}
		}
	}
	
}
