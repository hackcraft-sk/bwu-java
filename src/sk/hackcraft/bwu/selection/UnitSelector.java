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
	public interface BooleanInformation {
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
	public interface IntegerInformation {
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
	public interface RealInformation {
		/**
		 * Get a value for this selector from a unit
		 * @param unit
		 * @return
		 */
		public double getValue(Unit unit);
	}
	
	static public final BooleanInformation IS_GROUND = new BooleanInformation() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return !unit.getType().isFlyer();
		}
	};
	
	static public final BooleanInformation IS_FLYER = new BooleanInformation() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return unit.getType().isFlyer();
		}
	};
	
	static public final IntegerInformation HIT_POINTS = new IntegerInformation() {
		@Override
		public int getValue(Unit unit) {
			return unit.getHitPoints();
		}
	};
}
