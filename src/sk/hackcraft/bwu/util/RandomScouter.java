package sk.hackcraft.bwu.util;

import javabot.types.UnitType;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.UnitSet;

public class RandomScouter {
	public interface UnitListener {
		public void onUnitObtained(Unit unit);
		public void onUnitReleased(Unit unit);
	}
	
	private UnitType [] priorities;
	
	private int unitCountForScout = 0;
	private float percentageOfUnitsForScout = 0.0f;
	
	private boolean usingPercentage = false;
	private boolean usingCount = false;
	
	private UnitSet currentScoutingUnits = new UnitSet();
	
	private UnitListener listener = null;
	
	public RandomScouter(UnitListener listener, float ratio, UnitType... priorities) {
		if(ratio > 1f || ratio < 0f) {
			throw new IllegalArgumentException("Ratio of of range <0f; 1f>");
		}
		
		this.listener = listener;
		this.percentageOfUnitsForScout = ratio;
		this.priorities = priorities;
		this.usingPercentage = true;
	}
	
	public RandomScouter(float ratio, UnitType... priorities) {
		this(null, ratio, priorities);
	}
	
	public RandomScouter(UnitType... priorities) {
		this(1f, priorities);
	}
	
	public RandomScouter(UnitListener listener, int count, UnitType... priorities) {
		if(count < 0) {
			throw new IllegalArgumentException("Count of of range <0; +inf)");
		}
		
		this.listener = listener;
		this.unitCountForScout = count;
		this.priorities = priorities;
		this.unitCountForScout = count;
		this.usingCount = true;
	}
	
	public RandomScouter(int count, UnitType... priorities) {
		this(null, count, priorities);
	}
	
	private void manageUnitCounts(Game game) {
		int targetUnitCount = 0;
		int currentUnitCount = currentScoutingUnits.size();
		
		if(usingPercentage) {
			targetUnitCount = (int)Math.ceil(currentUnitCount*percentageOfUnitsForScout);
		} else {
			targetUnitCount = unitCountForScout;
		}
		
		if(currentUnitCount > targetUnitCount) {
			UnitSet unitsToRelease = currentScoutingUnits.minus(currentScoutingUnits.firstNOf(targetUnitCount, priorities));
			
			for(Unit unit : unitsToRelease) {
				currentScoutingUnits.remove(unit);
				if(listener != null) {
					listener.onUnitReleased(unit);
				}
			}
		} else if(currentUnitCount < targetUnitCount) {
			int numberOfUnitsToObtain = targetUnitCount-currentUnitCount;
			UnitSet unitsToObtain = game.getMyUnits().minus(currentScoutingUnits).firstNOf(numberOfUnitsToObtain, priorities);
			
			for(Unit unit : unitsToObtain) {
				currentScoutingUnits.add(unit);
				if(listener != null) {
					listener.onUnitObtained(unit);
				}
			}
		}
	}
	
	private void manageUnitOrders(Game game) {
		for(Unit unit : currentScoutingUnits) {
			if(unit.isIdle() || unit.isStuck()) {
				unit.move(Vector2D.random().scale(
					game.getMap().getWidth()*Game.TILE_SIZE, 
					game.getMap().getHeight()*Game.TILE_SIZE
				));
			}
		}
	}
	
	public void onGameUpdate(Game game) {
		manageUnitCounts(game);
		manageUnitOrders(game);
	}
}
