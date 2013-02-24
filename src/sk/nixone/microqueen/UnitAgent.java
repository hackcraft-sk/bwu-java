package sk.nixone.microqueen;

import javabot.model.Unit;

abstract public class UnitAgent extends UnitAgentSkeleton {
	public class MoveOrder extends Order {
		private int x, y;
		
		public MoveOrder(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void run(MicroQueen queen, Unit unit) {
			queen.BWAPI.move(unit.getID(), x, y);
		}
		
		@Override
		public boolean isCompleted() {
			return true;
		}
		
		@Override
		public int getTimeout() {
			return 30;
		}
		
		@Override
		public String toString() {
			return "Move";
		}
	}
	
	public class AttackMoveOrder extends Order {
		private int x, y;
		
		public AttackMoveOrder(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public void run(MicroQueen queen, Unit unit) {
			queen.BWAPI.attack(unit.getID(), x, y);
		}
		
		@Override
		public boolean isCompleted() {
			return true;
		}
		
		@Override
		public int getTimeout() {
			return 30;
		}
		
		@Override
		public String toString() {
			return "Attack move";
		}
	}
	
	public class AvoidStormOrder extends Order {
		private int vx, vy;
		private Unit unit;
		
		public AvoidStormOrder(Unit unit, Unit enemy) {
			this.unit = unit;
			vx = unit.getX() - enemy.getX();
			vy = unit.getY() - enemy.getY();
		}

		@Override
		public int getTimeout() {
			return 15;
		}

		@Override
		public void run(MicroQueen queen, Unit unit) {
			queen.BWAPI.move(unit.getID(), unit.getX()+vx*5, unit.getY()+vy*5);
		}

		@Override
		public boolean isCompleted() {
			return !unit.isUnderStorm();
		}
		
		@Override
		public String toString() {
			return "Avoid Storm";
		}
	}
	
	private int moveTargetX, moveTargetY;
	
	private AvoidStormOrder avoidStormOrder = null;
	private AttackMoveOrder attackMoveOrder = null;
	
	public UnitAgent(MicroQueen queen, Unit unit) {
		super(queen, unit);
	}
	
	@Override
	public void onNextFrame(int frameNumber) {
		updateControl();
		
		if(unit.isUnderStorm() && !hasControl() && (avoidStormOrder == null || avoidStormOrder.isFinished()) && isReadyForOrders()) {
			Unit closest = queen.getEnemyUnits().getClosest(unit);
			if(closest != null) {
				avoidStormOrder = new AvoidStormOrder(unit, closest);
				order(avoidStormOrder);
			}
		}
		else if((attackMoveOrder == null || attackMoveOrder.isFinished()) && !hasControl() && isReadyForOrders()) {
			attackMoveOrder = new AttackMoveOrder(moveTargetX, moveTargetY);
			order(attackMoveOrder);
		}
		
		super.onNextFrame(frameNumber);
	}
	
	public void move(int x, int y) {
		this.moveTargetX = x;
		this.moveTargetY = y;
	}
	
	public int getMoveTargetX() {
		return moveTargetX;
	}
	
	public int getMoveTargetY() {
		return moveTargetY;
	}
	
	abstract public boolean hasControl();
	abstract public void updateControl();
}
