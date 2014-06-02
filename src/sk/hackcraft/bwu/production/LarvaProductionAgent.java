package sk.hackcraft.bwu.production;

import java.util.HashSet;
import java.util.Set;

import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public class LarvaProductionAgent
{
	private final Set<Unit> hatcheries;
	
	public LarvaProductionAgent()
	{
		this.hatcheries = new HashSet<>();
	}
	
	public void addHatchery(Unit hatchery)
	{
		hatcheries.add(hatchery);
	}
	
	public void removeHatchery(Unit hatchery)
	{
		hatcheries.remove(hatchery);
	}
	
	public boolean produce(UnitType type)
	{
		Set<Unit> availableLarvae = getAvailableLarvae();
		
		if (availableLarvae.isEmpty())
		{
			return false;
		}
		
		Unit larva = availableLarvae.iterator().next();
		
		return produce(type, larva);
	}
	
	public boolean produce(UnitType type, Unit larva)
	{
		return larva.morph(type);
	}
	
	public Set<Unit> getAvailableLarvae()
	{
		Set<Unit> availableLarvae = new HashSet<>();
		
		for (Unit hatchery : hatcheries)
		{
			availableLarvae.addAll(hatchery.getLarva());
		}
		
		return availableLarvae;
	}
}
