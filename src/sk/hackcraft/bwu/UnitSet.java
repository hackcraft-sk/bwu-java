package sk.hackcraft.bwu;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class UnitSet extends HashSet<Unit> {
	public UnitSet(Collection<Unit> units) {
		for(Unit unit : units) {
			add(unit);
		}
	}
	
	public UnitSet where(UnitSelector.BooleanInformation selector) {
		Set<Unit> result = new HashSet<Unit>();
		for(Unit unit : this) {
			if(selector.isTrueFor(unit)) {
				result.add(unit);
			}
		}
		return new UnitSet(result);
	}
	
	public UnitSet whereNot(UnitSelector.BooleanInformation selector) {
		Set<Unit> result = new HashSet<Unit>();
		for(Unit unit : this) {
			if(!selector.isTrueFor(unit)) {
				result.add(unit);
			}
		}
		return new UnitSet(result);
	}
	
	public UnitSet whereLessOrEqual(UnitSelector.IntegerInformation selector, int value) {
		Set<Unit> result = new HashSet<Unit>();
		for(Unit unit : this) {
			if(selector.getValue(unit) <= value) {
				result.add(unit);
			}
		}
		return new UnitSet(result);
	}
	
	public UnitSet whereLessOrEqual(UnitSelector.RealInformation selector, double value) {
		Set<Unit> result = new HashSet<Unit>();
		for(Unit unit : this) {
			if(selector.getValue(unit) <= value) {
				result.add(unit);
			}
		}
		return new UnitSet(result);
	}
	
	public UnitSet whereGreatherOrEqual(UnitSelector.IntegerInformation selector, int value) {
		Set<Unit> result = new HashSet<Unit>();
		for(Unit unit : this) {
			if(selector.getValue(unit) >= value) {
				result.add(unit);
			}
		}
		return new UnitSet(result);
	}
	
	public UnitSet whereGreatherOrEqual(UnitSelector.RealInformation selector, double value) {
		Set<Unit> result = new HashSet<Unit>();
		for(Unit unit : this) {
			if(selector.getValue(unit) >= value) {
				result.add(unit);
			}
		}
		return new UnitSet(result);
	}
}
