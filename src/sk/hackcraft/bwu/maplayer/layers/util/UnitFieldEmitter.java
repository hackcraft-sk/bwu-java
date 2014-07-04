package sk.hackcraft.bwu.maplayer.layers.util;

import jnibwapi.Unit;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.layers.PotentialFieldMatrixLayer.FieldEmitter;

public class UnitFieldEmitter implements FieldEmitter<Unit>
{
	private final Unit unit;
	
	public UnitFieldEmitter(Unit unit)
	{
		this.unit = unit;
	}
	
	@Override
	public int getValue()
	{
		return unit.getType().getGroundWeapon().getMaxRange() / 32;
	}

	@Override
	public LayerPoint getPosition()
	{
		return Convert.toLayerPoint(unit.getPosition());
	}

	@Override
	public Unit getSource()
	{
		return unit;
	}
}
