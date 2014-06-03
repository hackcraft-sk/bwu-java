package sk.hackcraft.bwu.selection;

import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import sk.hackcraft.bwu.selection.UnitSelector.BooleanSelector;

public class TypeSelector implements BooleanSelector
{
	private final UnitType type;
	
	public TypeSelector(UnitType type)
	{
		this.type = type;
	}
	
	@Override
	public boolean isTrueFor(Unit unit)
	{
		return unit.getType() == type;
	}
}
