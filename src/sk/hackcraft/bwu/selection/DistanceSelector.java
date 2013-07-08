package sk.hackcraft.bwu.selection;

import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.UnitSelector.RealSelector;

public class DistanceSelector implements RealSelector {
	private Vector2D position = null;
	private Unit unit = null;
	
	/**
	 * 
	 * 
	 * @param position
	 */
	public DistanceSelector(Vector2D position) {
		this.position = position;
	}
	
	public DistanceSelector(Unit unit) {
		this.unit = unit;
	}

	@Override
	public double getValue(Unit unit) {
		if(position != null) {
			return unit.getPosition().sub(position).length;
		}
		return this.unit.getPosition().sub(unit.getPosition()).length;
	}
}
