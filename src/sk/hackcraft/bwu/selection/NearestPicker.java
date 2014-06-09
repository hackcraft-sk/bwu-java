package sk.hackcraft.bwu.selection;

import java.util.Set;

import jnibwapi.Unit;

public class NearestPicker implements Picker
{
	private final Unit unit;
	
	public NearestPicker(Unit unit)
	{
		this.unit = unit;
	}
	
	@Override
	public Unit pickFrom(Set<Unit> units)
	{
		Unit selectedUnit = null;
		double shortestDistance = Double.POSITIVE_INFINITY;
		
		for (Unit setUnit : units)
		{
			double distance = setUnit.getDistance(unit);
			
			if (distance < shortestDistance)
			{
				shortestDistance = distance;
				selectedUnit = setUnit;
			}
		}
		
		return selectedUnit;
	}
}
