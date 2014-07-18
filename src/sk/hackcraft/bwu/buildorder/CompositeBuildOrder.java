package sk.hackcraft.bwu.buildorder;

public class CompositeBuildOrder implements BuildOrder
{
	final private BuildOrder [] orders;
	
	public CompositeBuildOrder(BuildOrder... orders)
	{
		this.orders = orders;
	}
	
	@Override
	public boolean isAchieved(BuildOrderContext context)
	{
		for(BuildOrder order : orders)
		{
			if(!order.isAchieved(context))
			{
				return false;
			}
		}
		
		return true;
	}

	@Override
	public void tryToAchieve(BuildOrderContext context)
	{
		for(BuildOrder order : orders)
		{
			if(!order.isAchieved(context))
			{
				order.tryToAchieve(context);
				return;
			}
		}
	}
}
