package sk.hackcraft.bwu.selection;

import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.selection.UnitSelector.BooleanSelector;
import sk.hackcraft.bwu.selection.UnitSelector.RealSelector;

public class RealComparisonSelector implements BooleanSelector
{
	private RealSelector selector;
	private double value;
	private Comparison comparison;

	public RealComparisonSelector(RealSelector selector, double value, Comparison comparison)
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
