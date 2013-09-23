package sk.hackcraft.bwu.selection;

import javabot.model.Player;
import javabot.types.UnitType;
import javabot.types.WeaponType;
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
	
	public abstract class ObjectEqualitySelector<C> implements BooleanSelector {
		private C toBeEqualTo;
		
		public ObjectEqualitySelector(C toBeEqualTo) {
			this.toBeEqualTo = toBeEqualTo;
		}
		
		@Override
		public boolean isTrueFor(Unit unit) {
			return getObjectFrom(unit) == toBeEqualTo;
		}
		
		public abstract C getObjectFrom(Unit unit);
	}
	
	static public class AirWeaponTypeSelector extends ObjectEqualitySelector<WeaponType> {
		public AirWeaponTypeSelector(WeaponType toBeEqualTo) {
			super(toBeEqualTo);
		}

		@Override
		public WeaponType getObjectFrom(Unit unit) {
			return unit.getAirWeaponType();
		}
	}
	
	static public class GroundWeaponTypeSelector extends ObjectEqualitySelector<WeaponType> {
		public GroundWeaponTypeSelector(WeaponType toBeEqualTo) {
			super(toBeEqualTo);
		}
		
		@Override
		public WeaponType getObjectFrom(Unit unit) {
			return unit.getGroundWeaponType();
		}
	}
	
	static public class PlayerSelector extends ObjectEqualitySelector<Player> {
		public PlayerSelector(Player toBeEqualTo) {
			super(toBeEqualTo);
		}
		
		@Override
		public Player getObjectFrom(Unit unit) {
			return unit.getPlayer();
		}
	}
	
	static public class UnitTypeSelector extends ObjectEqualitySelector<UnitType> {
		public UnitTypeSelector(UnitType toBeEqualTo) {
			super(toBeEqualTo);
		}
		
		@Override
		public UnitType getObjectFrom(Unit unit) {
			return unit.getType();
		}
	}
	
	/**
	 * Selector for units by hit points.
	 */
	static public final IntegerSelector HIT_POINTS = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getHitPoints();
		}
	};
	
	static public final IntegerSelector SHIELD = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getShield();
		}
	};
	
	static public final IntegerSelector ENERGY = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getEnergy();
		}
	};
	
	static public final IntegerSelector RESOURCES = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getResources();
		}
	};
	
	static public final IntegerSelector INITIAL_HIT_POINTS = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getInitialHitPoints();
		}
	};
	
	static public final IntegerSelector KILL_COUNT = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getKillCount();
		}
	};
	
	static public final IntegerSelector ACID_SPORE_COUNT = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getAcidSporeCount();
		}
	};
	
	static public final IntegerSelector INTERCEPTOR_COUNT = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getInterceptorCount();
		}
	};
	
	static public final IntegerSelector SCARAB_COUNT = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getScarabCount();
		}
	};
	
	static public final IntegerSelector GROUND_WEAPON_COOLDOWN = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getGroundWeaponCooldown();
		}
	};
	
	static public final IntegerSelector AIR_WEAPON_COOLDOWN = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getAirWeaponCooldown();
		}
	};
	
	static public final IntegerSelector SPELL_COOLDOWN = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getSpellCooldown();
		}
	};
	
	static public final IntegerSelector DEFENSE_MATRIX_POINTS = new IntegerSelector() {
		@Override
		public int getValue(Unit unit) {
			return unit.getDefenseMatrixPoints();
		}
	};
	
	// TIMERS
	
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
	

	
	static public final BooleanSelector IS_LOCKED_DOWN = new BooleanSelector() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return unit.isLockedDown();
		}
	};
	
	static public final BooleanSelector IS_VISIBLE = new BooleanSelector() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return unit.isVisible();
		}
	};
	
	static public final BooleanSelector IS_UNDER_ATTACK = new BooleanSelector() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return unit.isUnderAttack();
		}
	};
	
	static public final BooleanSelector IS_BUILDING = new BooleanSelector() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return unit.getType().isBuilding();
		}
	};
	
	static public final BooleanSelector CAN_ATTACK_AIR = new BooleanSelector() {
		@Override
		public boolean isTrueFor(Unit unit) {
			return unit.getType().isCanAttackAir();
		}
	};
}
