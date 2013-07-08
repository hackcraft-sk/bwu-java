package sk.nixone.microqueen;

import javabot.model.Unit;
import javabot.types.TechType.TechTypes;
import javabot.types.WeaponType.WeaponTypes;

public class HighTemplarAgent extends UnitAgent {
	static final public int STORM_ENERGY = 75;
	
	static public class StormOrder extends Order {
		private Unit target;
		
		public StormOrder(Unit target) {
			this.target = target;
		}
		
		@Override
		public int getTimeout() {
			return 45;
		}

		@Override
		public void run(MicroQueen queen, Unit unit) {
			queen.BWAPI.useTech(unit.getID(), TechTypes.Psionic_Storm.ordinal(), target.getID());
		}

		@Override
		public boolean isCompleted() {
			return true;
		}
		
	}
	
	private boolean hasControl = false;
	private float stormRadius;
	private float stormSplashRadius;
	
	public HighTemplarAgent(MicroQueen queen, Unit unit) {
		super(queen, unit);
		stormRadius = queen.BWAPI.getWeaponType(WeaponTypes.Psionic_Storm.ordinal()).getMaxRange();
		stormSplashRadius = queen.BWAPI.getWeaponType(WeaponTypes.Psionic_Storm.ordinal()).getOuterSplashRadius();
	}
	
	@Override
	public boolean hasControl() {
		return hasControl;
	}

	@Override
	public void updateControl() {
		if(unit.getEnergy() >= STORM_ENERGY) {
			UnitGroup reachable = queen.getEnemyUnits().inRange(unit, stormRadius*1.5f);
			
			int bestTargetSplashCount = 0;
			Unit bestTarget = null;
			
			for(Unit target : reachable) {
				int splashCount = queen.getEnemyUnits().inRange(target, stormSplashRadius).size() - queen.getMyUnits().inRange(target, stormSplashRadius).size();
				
				if(splashCount > bestTargetSplashCount) {
					bestTargetSplashCount = splashCount;
					bestTarget = target;
				}
			}
			
			hasControl = bestTargetSplashCount >= 2;
			
			if(hasControl && isReadyForOrders()) {
				order(new StormOrder(bestTarget));
			}
		} else {
			UnitGroup enemies = queen.getEnemyUnits().inRange(unit, 300f);
			
			hasControl = enemies.size() > 0;
			
			if(hasControl && isReadyForOrders()) {
				Unit closest = enemies.getClosest(unit);
				
				int vx = unit.getX() - closest.getX();
				int vy = unit.getY() - closest.getY();
				
				MoveOrder avoidOrder = new MoveOrder(unit.getX()+vx*3, unit.getY()+vy*3);
				
				order(avoidOrder);
			}
		}
	}
}
