package sk.hackcraft.bwu.selection;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import javabot.types.UnitType;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;

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
	public UnitSet where(UnitSelector.BooleanSelector selector) {
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
	public UnitSet whereNot(UnitSelector.BooleanSelector selector) {
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
	public UnitSet whereLessOrEqual(UnitSelector.IntegerSelector selector, int value) {
		return where(new IntegerComparisonSelector(selector, value, Comparison.LESS_OR_EQUAL));
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have the
	 * selector value less or equal to the value provided as a parameter.
	 * @param selector real information selector
	 * @param value threshold
	 * @return
	 */
	public UnitSet whereLessOrEqual(UnitSelector.RealSelector selector, double value) {
		return where(new RealComparisonSelector(selector, value, Comparison.LESS_OR_EQUAL));
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have the
	 * selector value greather or equal to the value provided as a parameter.
	 * @param selector integer information selector
	 * @param value threshold
	 * @return
	 */
	public UnitSet whereGreatherOrEqual(UnitSelector.IntegerSelector selector, int value) {
		return where(new IntegerComparisonSelector(selector, value, Comparison.GREATHER_OR_EQUAL));
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have the
	 * selector value greather or equal to the value provided as a parameter.
	 * @param selector real information selector
	 * @param value threshold
	 * @return
	 */
	public UnitSet whereGreatherOrEqual(UnitSelector.RealSelector selector, double value) {
		return where(new RealComparisonSelector(selector, value, Comparison.GREATHER_OR_EQUAL));
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that have
	 * the type of <code>unitType</code>
	 * @param unitType type to be equal
	 * @return
	 */
	public UnitSet whereType(UnitType unitType) {
		return where(new UnitSelector.UnitTypeSelector(unitType));
	}
	
	/**
	 * Creates a new unit set containing units from this unit set that do not have
	 * the type of <code>unitType</code>
	 * @param unitType
	 * @return
	 */
	public UnitSet whereTypeNot(UnitType unitType) {
		return this.minus(this.whereType(unitType));
	}
	
	/**
	 * Computes arithmetic center of all units in this set.
	 * 
	 * @return vector representing center of this unit set
	 */
	public Vector2D getArithmeticCenter() {
		if(size() == 0) {
			return null;
		}
		
		Vector2D center = Vector2D.ZERO;
		
		for(Unit unit : this) {
			center = center.add(unit.getPosition());
		}
		center = center.scale(1.0/size());
		
		return center;
	}
	
	/**
	 * Computes average distance of this unit set (average over all units) from a certain given point.
	 * 
	 * @param point 
	 * @return average distance from point
	 */
	public double getAverageDistanceFrom(Vector2D point) {
		double accumulated = 0;
		
		for(Unit unit : this) {
			accumulated += point.sub(unit.getPosition()).length;
		}
		
		return accumulated/size();
	}
	
	/**
	 * Returns the first unit of specified types that exists in this set.
	 * 
	 * @param unitTypes possible types ordered by preference
	 * @return
	 */
	public Unit firstOf(UnitType... unitTypes) {
		for(UnitType type : unitTypes) {
			UnitSet subSet = whereType(type);
			if(subSet.size() > 0) {
				return subSet.first();
			}
		}
		return null;
	}
	
	public UnitSet firstNOf(int n, UnitType... unitTypes) {
		UnitSet result = new UnitSet();
		
		for(UnitType type : unitTypes) {
			UnitSet subSet = whereType(type);
			Iterator<Unit> it = subSet.iterator();
			
			while(it.hasNext() && result.size() < n) {
				result.add(it.next());
			}
		}
		
		return result;
	}
	
	/**
	 * Returns any single (first) unit from this set.
	 * 
	 * @return
	 */
	public Unit first() {
		if(size() == 0) {
			return null;
		}
		return iterator().next();
	}
	
	/**
	 * Returns the set of all units that are in this set but are not in the specified set.
	 * 
	 * @param units
	 * @return
	 */
	public UnitSet minus(UnitSet units) {
		UnitSet result = new UnitSet();
		for(Unit unit : this) {
			if(!units.contains(unit)) {
				result.add(unit);
			}
		}
		return result;
	}
	
	/**
	 * Returns the set of all units that are in both this and specified set.
	 * 
	 * @param set
	 * @return
	 */
	public UnitSet intersection(UnitSet set) {
		UnitSet result = new UnitSet();
		
		for(Unit unit : this) {
			if(set.contains(unit)) {
				result.add(unit);
			}
		}
		
		return result;
	}
	
	/**
	 * Returns all units that are in this set or in the specified set.
	 * 
	 * @param set
	 * @return
	 */
	public UnitSet union(UnitSet set) {
		UnitSet result = new UnitSet(this);
		
		this.addAll(set);
		
		return result;
	}
	
	/**
	 * Decides if these units are at specified position, decides if the average distance of
	 * all units from specified position in this set is smaller or equal to the tolerance.
	 * 
	 * @param position
	 * @param tolerance
	 * @return
	 */
	public boolean areAt(Vector2D position, double tolerance) {
		return getAverageDistanceFrom(position) <= tolerance;
	}
}
