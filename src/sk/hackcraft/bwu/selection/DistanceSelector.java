package sk.hackcraft.bwu.selection;

import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.UnitSelector.RealSelector;

/**
 * Class usable as a selector of units that are in a certain relation in the context of a distance from
 * some unit or a fixed position.
 * 
 * @author nixone
 *
 */
public class DistanceSelector implements RealSelector {
	private Vector2D position = null;
	private Unit unit = null;
	
	/**
	 * Creates a distance selector from certain position
	 * 
	 * @param position
	 */
	public DistanceSelector(Vector2D position) {
		this.position = position;
	}
	
	/**
	 * Creates a distance selector from certain unit (position is gathered later, so it can be re-used in
	 * many frames).
	 * 
	 * @param unit
	 */
	public DistanceSelector(Unit unit) {
		this.unit = unit;
	}

	@Override
	public double getValue(Unit unit) {
		if(position != null) {
			return unit.getPositionVector().sub(position).length;
		}
		return this.unit.getPositionVector().sub(unit.getPositionVector()).length;
	}
}
