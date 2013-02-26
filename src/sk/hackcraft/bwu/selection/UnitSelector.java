package sk.hackcraft.bwu.selection;

import sk.hackcraft.bwu.Unit;

/**
 * Conatiner for UnitSelectors by boolean, integer or real information
 * @author nixone
 *
 */
public interface UnitSelector {
	/**
	 * Selector for boolean information from a Unit
	 * @author nixone
	 *
	 */
	public interface BooleanSelector {
		/**
		 * Get a value for this selector from a unit
		 * @param unit
		 * @return
		 */
		public boolean isTrueFor(Unit unit);
	}
	
	/**
	 * Selector for integer information from a Unit 
	 * @author nixone
	 *
	 */
	public interface IntegerSelector {
		/**
		 * Get a value for this selector from a unit
		 * @param unit
		 * @return
		 */
		public int getValue(Unit unit);
	}
	
	/**
	 * Selector for real information form a unit
	 * @author nixone
	 *
	 */
	public interface RealSelector {
		/**
		 * Get a value for this selector from a unit
		 * @param unit
		 * @return
		 */
		public double getValue(Unit unit);
	}
	
	/**
	 * Selector for units that are not flyers (therefore are on a ground).
	 */
	static public final BooleanSelector IS_GROUND = new BooleanSelector() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return !unit.getType().isFlyer();
		}
	};
	
	/**
	 * Selector for units that are flyers (therefore are not on a ground).
	 */
	static public final BooleanSelector IS_FLYER = new BooleanSelector() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return unit.getType().isFlyer();
		}
	};
	
	/**
	 * Selector for units by hit points.
	 */
	static public final IntegerSelector HIT_POINTS = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getHitPoints();
		}
	};
}
