package sk.hackcraft.bwu;

import javabot.JNIBWAPI;
import javabot.types.DamageType;

public class DamageTypes {
	final public DamageType Independent;
	final public DamageType Explosive;
	final public DamageType Concussive;
	final public DamageType Normal;
	final public DamageType Ignore_Armor;
	final public DamageType None;
	final public DamageType Unknown;
	
	protected DamageTypes(JNIBWAPI jnibwapi) {
		Independent = jnibwapi.getDamageType(0);
		Explosive = jnibwapi.getDamageType(1);
		Concussive = jnibwapi.getDamageType(2);
		Normal = jnibwapi.getDamageType(3);
		Ignore_Armor = jnibwapi.getDamageType(4);
		None = jnibwapi.getDamageType(5);
		Unknown = jnibwapi.getDamageType(6);
	}
}
