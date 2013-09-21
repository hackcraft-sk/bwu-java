package sk.nixone.microqueen;

import javabot.model.Map;
import javabot.model.Unit;

public class CorshairAgent extends UnitAgent {
	public class LongAttackMoveOrder extends Order {
		private int x, y;
		
		public LongAttackMoveOrder(int x, int y) {
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
			return "Long attack move";
		}
	}
	
	private int[][] strategic = null;
	private int strategicIndex = 0;
	private int offset = 1;
	
	public CorshairAgent(MicroQueen queen, Unit unit) {
		super(queen, unit);
		
		Map map = queen.BWAPI.getMap();
		
		strategic = new int[][]{
				{ (int)(0.05f * map.getWidth() * MicroQueen.TILE_SIZE), (int)(0.05f * map.getHeight() * MicroQueen.TILE_SIZE) }, // left top
				{ (int)(0.05f * map.getWidth() * MicroQueen.TILE_SIZE), (int)(0.95f * map.getHeight() * MicroQueen.TILE_SIZE) }, // left bottom
				{ (int)(0.5f * map.getWidth() * MicroQueen.TILE_SIZE), (int)(0.5f * map.getHeight() * MicroQueen.TILE_SIZE) }, // center
				{ (int)(0.95f * map.getWidth() * MicroQueen.TILE_SIZE), (int)(0.95f * map.getHeight() * MicroQueen.TILE_SIZE) }, // right bottom
				{ (int)(0.95f * map.getWidth() * MicroQueen.TILE_SIZE), (int)(0.05f * map.getHeight() * MicroQueen.TILE_SIZE) } // right top
		};
	}

	@Override
	public boolean hasControl() {
		return true;
	}

	@Override
	public void updateControl() {
		UnitGroup threats = queen.getEnemyUnits().canAttackAir().inRange(unit, 400);
		
		// air threat avoidance
		if(threats.size() > 0 && isReadyForOrders()) {
			Unit closest = threats.getClosest(unit);
			
			if(closest != null) {
				int vx = unit.getX() - closest.getX();
				int vy = unit.getY() - closest.getY();
				
				// avoid the closest threat
				order(new MoveOrder(unit.getX()+vx*5, unit.getY()+vy*5));
			}
		}
		
		int closestIndex = 0;
		double closestDistance = Double.MAX_VALUE;
		
		// strategic choosing
		for(int i=0; i<strategic.length; i++) {
			int[] position = strategic[i];
			
			int dx = unit.getX() - position[0];
			int dy = unit.getY() - position[1];
			
			double distance = Math.sqrt(dx*dx+dy*dy);
			
			if(distance < closestDistance) {
				closestIndex = i;
				closestDistance = distance;
			}
		}
		
		if(closestDistance < 200) {
			strategicIndex = (closestIndex+offset+strategic.length) % strategic.length;
		}
		
		// strategic moving
		if(isReadyForOrders()) {
			order(new LongAttackMoveOrder(strategic[strategicIndex][0], strategic[strategicIndex][1]));
		}
	}
}
