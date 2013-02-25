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
}
