package sk.hackcraft.bwu.mining;

import jnibwapi.Unit;
import sk.hackcraft.bwu.mining.MiningAgent.Resource;

public class UnitMineralResource implements Resource
{
	private Unit mineral;
	
	public UnitMineralResource(Unit mineral)
	{
		this.mineral = mineral;
	}
	
	@Override
	public boolean areDataAvailable()
	{
		return mineral.isVisible();
	}
	
	@Override
	public boolean isMiningPossible()
	{
		return true;
	}
	
	@Override
	public int getValue()
	{
		return mineral.getResources();
	}

	@Override
	public boolean isMinedOut()
	{
		return getValue() <= 0;
	}
	
	@Override
	public String toString()
	{
		return "UnitMineralResource #" + mineral.getID();
	}
}
