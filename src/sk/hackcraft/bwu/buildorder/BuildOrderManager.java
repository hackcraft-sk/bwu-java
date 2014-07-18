package sk.hackcraft.bwu.buildorder;

import sk.hackcraft.bwu.Updateable;

public class BuildOrderManager implements Updateable
{
	private BuildOrderContext context;
	private BuildOrder buildOrder;
	
	public BuildOrderManager(BuildOrderContext context, BuildOrder defaultBuildOrder)
	{
		this.context = context;
		this.buildOrder = defaultBuildOrder;
	}
	
	public void setBuildOrder(BuildOrder buildOrder)
	{
		this.buildOrder = buildOrder;
	}

	@Override
	public void update()
	{
		if(!buildOrder.isAchieved(context))
		{
			buildOrder.tryToAchieve(context);
		}
	}
}
