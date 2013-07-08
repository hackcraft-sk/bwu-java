package sk.nixone.microqueen;

import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;
import javabot.types.WeaponType.WeaponTypes;

public class ShuttleAgent extends UnitAgent {
	static public class LoadOrder extends Order {
		private Unit what;
		private Unit where;
		
		public LoadOrder(Unit what, Unit where) {
			this.what = what;
			this.where = where;
		}
		
		@Override
		public int getTimeout() {
			return 30;
		}

		@Override
		public void run(MicroQueen queen, Unit unit) {
			queen.BWAPI.load(where.getID(), what.getID());
		}

		@Override
		public boolean isCompleted() {
			return what.isLoaded();
		}
		
		@Override
		public String toString() {
			return "Load";
		}
	}
	
	static public class UnloadOrder extends Order {
		private Unit what;
		private Unit where;
		
		public UnloadOrder(Unit what, Unit where) {
			this.what = what;
			this.where = where;
		}
		
		@Override
		public int getTimeout() {
			return 30;
		}

		@Override
		public void run(MicroQueen queen, Unit unit) {
			queen.BWAPI.unloadAll(where.getID());
		}

		@Override
		public boolean isCompleted() {
			return !what.isLoaded();
		}
		
		@Override
		public String toString() {
			return "Unload";
		}
	}
	
	private ReeverAgent reeverAgent = null;
	private Unit reever = null;
	private boolean loaded = false;
	private double reeverAttackRadius;
	
	private LoadOrder loadOrder = null;
	private UnloadOrder unloadOrder = null;
	
	public ShuttleAgent(MicroQueen queen, Unit unit) {
		super(queen, unit);

		reeverAttackRadius = queen.BWAPI.getWeaponType(WeaponTypes.Scarab.ordinal()).getMaxRange();
	}
	
	@Override
	public boolean hasControl() {
		return true;
	}
	
	@Override
	public void updateControl() {
		if(reever == null || reeverAgent == null) {
			reever = queen.getMyUnits().byType(UnitTypes.Protoss_Reaver).element();
			reeverAgent = (ReeverAgent)queen.getAgent(reever);
		}
		
		if(loadOrder != null && loadOrder.isFinished()) {
			queen.print("Loading finished");
			loadOrder = null;
			loaded = true;
		}

		if(unloadOrder != null && unloadOrder.isFinished()) {
			queen.print("Unloading finished");
			unloadOrder = null;
			loaded = false;
		}
		
		UnitGroup interests = queen.getEnemyUnits().onGround().inRange(reever, reeverAttackRadius * 2f);
		
		if(interests.size() == 0 && !loaded && loadOrder == null) {
			queen.print("Loading!");
			loadOrder = new LoadOrder(reever, unit);
			order(loadOrder);
		} else if(interests.size() > 0 && loaded && unloadOrder == null) {
			queen.print("Unloading!");
			unloadOrder = new UnloadOrder(reever, unit);
			order(unloadOrder);
		}
		
		if(loaded && isReadyForOrders()) {
			order(new AttackMoveOrder(reeverAgent.getMoveTargetX(), reeverAgent.getMoveTargetY()));
		}
	}
}
