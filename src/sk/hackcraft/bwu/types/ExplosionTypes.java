package sk.hackcraft.bwu.types;

import javabot.JNIBWAPI;
import javabot.types.ExplosionType;

public class ExplosionTypes {
	final public ExplosionType None;
	final public ExplosionType Normal;
	final public ExplosionType Radial_Splash;
	final public ExplosionType Enemy_Splash;
	final public ExplosionType Lockdown;
	final public ExplosionType Nuclear_Missile;
	final public ExplosionType Parasite;
	final public ExplosionType Broodlings;
	final public ExplosionType EMP_Shockwave;
	final public ExplosionType Irradiate;
	final public ExplosionType Ensnare;
	final public ExplosionType Plague;
	final public ExplosionType Stasis_Field;
	final public ExplosionType Dark_Swarm;
	final public ExplosionType Consume;
	final public ExplosionType Yamato_Gun;
	final public ExplosionType Restoration;
	final public ExplosionType Disruption_Web;
	final public ExplosionType Corrosive_Acid;
	final public ExplosionType Mind_Control;
	final public ExplosionType Feedback;
	final public ExplosionType Optical_Flare;
	final public ExplosionType Maelstrom;
	final public ExplosionType Air_Splash;
	final public ExplosionType Unknown;
	
	public ExplosionTypes(JNIBWAPI jnibwapi) {
		None = jnibwapi.getExplosionType(0);
		Normal = jnibwapi.getExplosionType(1);
		Radial_Splash = jnibwapi.getExplosionType(2);
		Enemy_Splash = jnibwapi.getExplosionType(3);
		Lockdown = jnibwapi.getExplosionType(4);
		Nuclear_Missile = jnibwapi.getExplosionType(5);
		Parasite = jnibwapi.getExplosionType(6);
		Broodlings = jnibwapi.getExplosionType(7);
		EMP_Shockwave = jnibwapi.getExplosionType(8);
		Irradiate = jnibwapi.getExplosionType(9);
		Ensnare = jnibwapi.getExplosionType(10);
		Plague = jnibwapi.getExplosionType(11);
		Stasis_Field = jnibwapi.getExplosionType(12);
		Dark_Swarm = jnibwapi.getExplosionType(13);
		Consume = jnibwapi.getExplosionType(14);
		Yamato_Gun = jnibwapi.getExplosionType(15);
		Restoration = jnibwapi.getExplosionType(16);
		Disruption_Web = jnibwapi.getExplosionType(17);
		Corrosive_Acid = jnibwapi.getExplosionType(18);
		Mind_Control = jnibwapi.getExplosionType(19);
		Feedback = jnibwapi.getExplosionType(20);
		Optical_Flare = jnibwapi.getExplosionType(21);
		Maelstrom = jnibwapi.getExplosionType(22);
		Air_Splash = jnibwapi.getExplosionType(24);
		Unknown = jnibwapi.getExplosionType(25);		
	}
}
