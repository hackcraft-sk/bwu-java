package sk.nixone.scmai4;

import jnibwapi.types.UnitType;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import sk.hackcraft.bwu.util.VectorGraph.InformationSystem;
import sk.hackcraft.bwu.util.VectorGraph.VertexValue;

public class MQ4InformationSystem implements InformationSystem {
	protected Game game = null;
	
	public MQ4InformationSystem(Game game) {
		this.game = game;
	}
	
	private double getMyPureValues(Vector2D position) {
		double value = 0;
		
		for(Unit unit : game.getMyUnits().whereLessOrEqual(new DistanceSelector(position), 400)) {
			if(unit.getType() == UnitType.UnitTypes.Zerg_Hatchery) {
				value += 10000;
			} else {
				value += unit.getHitPoints();
			}
		}
		
		return value;
	}
	
	private double getEnemyPureValues(Vector2D position) {
		double value = 0;
		
		for(Unit unit : game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(position), 400)) {
			if(unit.getType() == UnitType.UnitTypes.Zerg_Hatchery) {
				value += 20000;
			} else {
				value -= unit.getHitPoints();
			}
		}
		
		return value;
	}
	
	private double getCloseToEnemyHatcheryBonus(Vector2D position) {
		UnitSet enemyHatcheries = game.getEnemyUnits().whereType(UnitType.UnitTypes.Zerg_Hatchery);
		UnitSet closeEnemyHatcheries = enemyHatcheries.whereLessOrEqual(new DistanceSelector(position), 400);
		Unit closeEnemyHatchery = closeEnemyHatcheries.first();
		
		if(closeEnemyHatchery != null) {
			// last hatchery, brutal attack!
			if(enemyHatcheries.size() == 1) {
				return 50000;
			// hatchery already wounded, attack!
			} else if(closeEnemyHatchery.getHitPoints() < closeEnemyHatchery.getInitialHitPoints()) {
				return 10000;
			}
		}
		
		return 0;
	}
	
	private double getDefendingMyHatcheriesBonus(Vector2D position) {
		UnitSet myHatcheries = game.getMyUnits().whereType(UnitType.UnitTypes.Zerg_Hatchery);
		UnitSet myCloseHatcheries = myHatcheries.whereLessOrEqual(new DistanceSelector(position), 400);
		Unit myCloseHatchery = myCloseHatcheries.first();
		
		if(myCloseHatchery != null) {
			UnitSet enemiesClose = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(myCloseHatchery), 1000);
			
			// last hatchery, brutal defense!
			if(myHatcheries.size() == 1) {
				return 50000;
			// hatchery about to get wounded
			} else if(enemiesClose.size() > 0) {
				return 10000;
			}
		}
		
		return 0;
	}
	
	@Override
	public double getValueFor(Vector2D position) {
		return 
				getMyPureValues(position)+
				getEnemyPureValues(position)+
				getCloseToEnemyHatcheryBonus(position)+
				getDefendingMyHatcheriesBonus(position)
		;
	}

	@Override
	public double combineSelfAndSiblings(VertexValue me, VertexValue[] siblings) {
		double maxSiblingValue = Double.MIN_VALUE;
		
		for(VertexValue sibling : siblings) {
			maxSiblingValue = Math.max(maxSiblingValue, sibling.value);
		}
		
		return Math.max(me.value, maxSiblingValue - 250);
	}
}
