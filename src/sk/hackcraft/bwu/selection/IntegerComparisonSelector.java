package sk.hackcraft.bwu.selection;

import jnibwapi.Unit;
import sk.hackcraft.bwu.selection.UnitSelector.BooleanSelector;
import sk.hackcraft.bwu.selection.UnitSelector.IntegerSelector;

public class IntegerComparisonSelector implements BooleanSelector
{
	private IntegerSelector selector;
	private int value;
	private Comparison comparison;

	public IntegerComparisonSelector(IntegerSelector selector, int value, Comparison comparison)
	{
		if (selector == null)
		{
			throw new NullPointerException("Selector cannot be null");
		}
		if (comparison == null)
		{
			throw new NullPointerException("Comparison cannot be null");
		}

		this.selector = selector;
		this.value = value;
		this.comparison = comparison;
	}

	@Override
	public boolean isTrueFor(Unit unit)
	{
		if (comparison == Comparison.GREATHER_OR_EQUAL)
		{
			return selector.getValue(unit) >= value;
		}
		else if (comparison == Comparison.GREATHER)
		{
			return selector.getValue(unit) > value;
		}
		else if (comparison == Comparison.LESS)
		{
			return selector.getValue(unit) < value;
		}
		else if (comparison == Comparison.LESS_OR_EQUAL)
		{
			return selector.getValue(unit) <= value;
		}
		return false;
	}
}
