package sk.hackcraft.bwu.selection;

import javabot.types.UnitType;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.selection.UnitSelector.BooleanSelector;

public class UnitTypeSelector implements BooleanSelector {
	private UnitType unitType;
	
	public UnitTypeSelector(UnitType unitType) {
		this.unitType = unitType;
	}
	
	@Override
	public boolean isTrueFor(Unit unit) {
		return unit.getType() == unitType;
	}
}
