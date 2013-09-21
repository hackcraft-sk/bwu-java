package sk.nixone.microqueen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javabot.model.Map;
import javabot.model.Unit;

public class AttackPlanner {
	static public class Position {
		final public int x, y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Position(float x, float y) {
			this(Math.round(x), Math.round(y));
		}
		
		public boolean collidesWith(int x, int y) {
			int dx = this.x-x;
			int dy = this.y-y;
			
			return Math.sqrt(dx*dx + dy*dy) < COLLISION_DISTANCE / 2;
		}
	}
	
	static public final double COLLISION_DISTANCE = 150;
	
	final private MicroQueen queen;
	
	private ArrayList<Position> strategicPositions = new ArrayList<Position>(4);
	private LinkedList<Position> knownEnemyPositions = new LinkedList<AttackPlanner.Position>();
	
	public AttackPlanner(MicroQueen queen) {
		this.queen = queen;
		
		Map map = queen.BWAPI.getMap();
		
		strategicPositions.add(new Position(map.getWidth()*0.15f * MicroQueen.TILE_SIZE, map.getHeight()*0.5f * MicroQueen.TILE_SIZE));
		strategicPositions.add(new Position(map.getWidth()*0.5f * MicroQueen.TILE_SIZE, map.getHeight()*0.7f * MicroQueen.TILE_SIZE));
		strategicPositions.add(new Position(map.getWidth()*0.85f * MicroQueen.TILE_SIZE, map.getHeight()*0.5f * MicroQueen.TILE_SIZE));
		strategicPositions.add(new Position(map.getWidth()*0.5f * MicroQueen.TILE_SIZE, map.getHeight()*0.3f * MicroQueen.TILE_SIZE));
	}
	
	public void update() {
		Iterator<Position> it = knownEnemyPositions.iterator();
		
		while(it.hasNext()) {
			Position pos = it.next();
			if(hasAcquiredPosition(pos)) {
				it.remove();
			}
		}
		
		for(Unit enemy : queen.getEnemyUnits()) {
			int x = enemy.getX();
			int y = enemy.getY();
			
			boolean collides = false;
			
			for(Position p : knownEnemyPositions) {
				if(p.collidesWith(x, y)) {
					collides = true;
					break;
				}
			}
			
			if(!collides) {
				knownEnemyPositions.addLast(new Position(x, y));
			}
		}
	}
	
	public void draw() {
		for(Position p : knownEnemyPositions) {
			queen.BWAPI.drawCircle(p.x, p.y, (int)COLLISION_DISTANCE, 150, false, false);
		}
		queen.BWAPI.drawText(20, 100, "AttackPlanner positions: "+knownEnemyPositions.size(), true);
	}
	
	public Position getNextAttackPosition() {
		UnitGroup myUnits = queen.getMyUnits();
		
		Position closestPosition = null;
		double closestPositionDistance = Double.MAX_VALUE;
		
		for(Position known : knownEnemyPositions) {
			double distance = myUnits.getDistance(known.x, known.y);
			
			if(distance < closestPositionDistance) {
				closestPositionDistance = distance;
				closestPosition = known;
			}
		}
		
		if(closestPosition != null) {
			return closestPosition;
		}
			
		
		closestPosition = null;
		closestPositionDistance = Double.MAX_VALUE;
		
		for(Position known : strategicPositions) {
			double distance = myUnits.getDistance(known.x, known.y);
			
			if(distance < closestPositionDistance) {
				closestPositionDistance = distance;
				closestPosition = known;
			}
		}
		
		for(int i=0; i<strategicPositions.size(); i++) {
			if(strategicPositions.get(i) == closestPosition) {
				return strategicPositions.get((i+1) % strategicPositions.size());
			}
		}
		
		return null;
	}
	
	public boolean hasAcquiredPosition(Position pos) {
		UnitGroup myAllUnits = queen.getMyUnits();
		UnitGroup insideUnits = queen.getMyUnits().inRange(pos.x, pos.y, COLLISION_DISTANCE);
		UnitGroup nearbyUnits = queen.getMyUnits().inRange(pos.x, pos.y, COLLISION_DISTANCE*2);
		UnitGroup enemyUnits = queen.getEnemyUnits().inRange(pos.x, pos.y, COLLISION_DISTANCE);
		
		return (nearbyUnits.size() >= Math.ceil(myAllUnits.size()*0.8) || (insideUnits.size() >= Math.ceil(myAllUnits.size()*0.7))) && enemyUnits.size() == 0;
	}
}
