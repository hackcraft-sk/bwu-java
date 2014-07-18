package sk.hackcraft.bwu.buildorder;

public interface BuildOrder
{
	public boolean isAchieved(BuildOrderContext context);
	public void tryToAchieve(BuildOrderContext context);
}
