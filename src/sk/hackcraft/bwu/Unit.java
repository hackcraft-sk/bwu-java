package sk.hackcraft.bwu;

import javabot.JNIBWAPI;
import javabot.types.UnitType;
import javabot.types.WeaponType;

public class Unit {
	final protected JNIBWAPI jnibwapi;
	final protected javabot.model.Unit originalUnit;
	
	public Unit(JNIBWAPI jnibwapi, javabot.model.Unit originalUnit) {
		this.jnibwapi = jnibwapi;
		this.originalUnit = originalUnit;
	}
	
	public UnitType getUnitType() {
		return jnibwapi.getUnitType(originalUnit.getTypeID());
	}
	
	public WeaponType getGroundWeaponType() {
		return jnibwapi.getWeaponType(getUnitType().getGroundWeaponID());
	}
	
	public WeaponType getAirWeaponType() {
		return jnibwapi.getWeaponType(getUnitType().getAirWeaponID());
	}
	
	public javabot.model.Unit getOriginalUnit() {
		return originalUnit;
	}
}
