package sk.hackcraft.bwu.selection;

import java.util.Collection;
import java.util.HashSet;

import sk.hackcraft.bwu.Unit;

/**
 * Represents a set of units and helper operations for unit selection.
 * @author nixone
 *
 */
public class UnitSet extends HashSet<Unit> {
	/**
	 * Creates an empty unit set
	 */
	public UnitSet() {
		// empty for empty unitset
	}
	
	/**
	 * Creates an unit set from collection of units. If a single unit appears in this
	 * collection more than once, unit set will contain only one instance of the unit.
	 * 
	 * @param units collection of units
	 */
	public UnitSet(Collection<Unit> units) {
		for(Unit unit : units) {
			add(unit);
		}
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that are
	 * approved by the selector.
	 * 
	 * @param selector
	 * @return new unit set
	 */
	public UnitSet where(UnitSelector.BooleanInformation selector) {
		UnitSet result = new UnitSet();
		for(Unit unit : this) {
			if(selector.isTrueFor(unit)) {
				result.add(unit);
			}
		}
		return result;
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that are not
	 * approved by the selector.
	 * 
	 * @param selector
	 * @return
	 */
	public UnitSet whereNot(UnitSelector.BooleanInformation selector) {
		UnitSet result = new UnitSet();
		for(Unit unit : this) {
			if(!selector.isTrueFor(unit)) {
				result.add(unit);
			}
		}
		return result;
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have the
	 * selector value less or equal to the value provided as a parameter.
	 * @param selector integer information selector
	 * @param value threshold
	 * @return
	 */
	public UnitSet whereLessOrEqual(UnitSelector.IntegerInformation selector, int value) {
		UnitSet result = new UnitSet();
		for(Unit unit : this) {
			if(selector.getValue(unit) <= value) {
				result.add(unit);
			}
		}
		return result;
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have the
	 * selector value less or equal to the value provided as a parameter.
	 * @param selector real information selector
	 * @param value threshold
	 * @return
	 */
	public UnitSet whereLessOrEqual(UnitSelector.RealInformation selector, double value) {
		UnitSet result = new UnitSet();
		for(Unit unit : this) {
			if(selector.getValue(unit) <= value) {
				result.add(unit);
			}
		}
		return result;
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have the
	 * selector value greather or equal to the value provided as a parameter.
	 * @param selector integer information selector
	 * @param value threshold
	 * @return
	 */
	public UnitSet whereGreatherOrEqual(UnitSelector.IntegerInformation selector, int value) {
		UnitSet result = new UnitSet();
		for(Unit unit : this) {
			if(selector.getValue(unit) >= value) {
				result.add(unit);
			}
		}
		return result;
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have the
	 * selector value greather or equal to the value provided as a parameter.
	 * @param selector real information selector
	 * @param value threshold
	 * @return
	 */
	public UnitSet whereGreatherOrEqual(UnitSelector.RealInformation selector, double value) {
		UnitSet result = new UnitSet();
		for(Unit unit : this) {
			if(selector.getValue(unit) >= value) {
				result.add(unit);
			}
		}
		return result;
	}
}
