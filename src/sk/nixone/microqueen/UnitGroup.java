package sk.nixone.microqueen;

import java.util.Collection;
import java.util.LinkedList;

import javabot.model.Unit;
import javabot.types.UnitType;
import javabot.types.UnitType.UnitTypes;

public class UnitGroup extends LinkedList<Unit> {
	private MicroQueen queen;
	
	public UnitGroup(MicroQueen queen, Collection<Unit> units) {
		this.queen = queen;
		
		for(Unit unit : units) {
			if(unit.isExists())
				add(unit);
		}
	}
	
	public UnitGroup inRange(Unit referer, double radius) {
		return inRange(referer.getX(), referer.getY(), radius);
	}
	
	public UnitGroup inRange(int rx, int ry, double radius) {
		LinkedList<Unit> result = new LinkedList<Unit>();
		
		for(Unit unit : this) {
			if(!unit.isExists())
				continue;
			
			int dx = rx-unit.getX();
			int dy = ry-unit.getY();
			
			double distance = Math.sqrt(dx*dx+dy*dy);
			
			if(distance <= radius) {
				result.add(unit);
			}
		}
		
		return new UnitGroup(queen, result);
	}
	
	public Unit getClosest(Unit referer) {
		return getClosest(referer.getX(), referer.getY());
	}
	
	public Unit getClosest(int rx, int ry) {		
		double closestDistance = Double.MAX_VALUE;
		Unit closest = null;
		
		for(Unit unit : this) {
			if(!unit.isExists())
				continue;
			
			int dx = rx-unit.getX();
			int dy = ry-unit.getY();
			
			double distance = Math.sqrt(dx*dx+dy*dy);
			
			if(distance < closestDistance) {
				closestDistance = distance;
				closest = unit;
			}
		}
		
		return closest;
	}
	
	public UnitGroup onGround() {
		LinkedList<Unit> result = new LinkedList<Unit>();
		
		for(Unit unit : this) {
			UnitType type = queen.BWAPI.getUnitType(unit.getTypeID());
			if(type != null && !type.isFlyer() && !type.isFlyingBuilding()) {
				result.add(unit);
			}
		}
		
		return new UnitGroup(queen, result);
	}
	
	public UnitGroup inAir() {
		LinkedList<Unit> result = new LinkedList<Unit>();
		
		for(Unit unit : this) {
			UnitType type = queen.BWAPI.getUnitType(unit.getTypeID());
			if(type != null && type.isFlyer()) {
				result.add(unit);
			}
		}
		
		return new UnitGroup(queen, result);
	}
	
	public double getDistance(int x, int y) {
		double closest = Double.MAX_VALUE;
		
		for(Unit unit : this) {
			if(!unit.isExists())
				continue;
			
			int dx = x-unit.getX();
			int dy = y-unit.getY();
			
			double distance = Math.sqrt(dx*dx+dy*dy);
			
			if(distance < closest) {
				closest = distance;
			}
		}
		
		return closest;
	}
	
	public UnitGroup byType(UnitTypes type) {
		LinkedList<Unit> result = new LinkedList<Unit>();
		
		for(Unit unit : this) {
			if(!unit.isExists())
				continue;
			
			if(unit.getTypeID() == type.ordinal()) {
				result.add(unit);
			}
		}
		
		return new UnitGroup(queen, result);
	}
	
	public UnitGroup canAttackAir() {
		LinkedList<Unit> result = new LinkedList<Unit>();
		
		for(Unit unit : this) {
			if(!unit.isExists())
				continue;
			
			UnitType type = queen.BWAPI.getUnitType(unit.getTypeID());
			
			if(type != null && type.isCanAttackAir()) {
				result.add(unit);
			}
		}
		
		return new UnitGroup(queen, result);
	}
	
	/*public UnitGroup slowerThan(Unit unit) {
		UnitTypes type = queen.BWAPI.getUnitType(unit.getTypeID());
		
		LinkedList<Unit> result = new LinkedList<Unit>();
		
		for(Unit unit : this) {
			UnitTypes type = queen.BWAPI.getUnitType(unit.getTypeID());
			if(!type.isFlyer() && !type.isFlyingBuilding()) {
				result.add(unit);
			}
		}
		
		return new UnitGroup(queen, result);
	}*/
}
