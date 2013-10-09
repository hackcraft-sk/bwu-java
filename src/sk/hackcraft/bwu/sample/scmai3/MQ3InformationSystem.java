package sk.hackcraft.bwu.sample.scmai3;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import sk.hackcraft.bwu.util.VectorGraph.InformationSystem;
import sk.hackcraft.bwu.util.VectorGraph.VertexValue;

public class MQ3InformationSystem implements InformationSystem {
	static public final double HATCHERY_BONUS = 2000;
	
	private Game game = null;
	private double myCoeficient;
	private double enemyCoeficient;
	
	public MQ3InformationSystem(Game game, double myCoeficient, double enemyCoeficient) {
		this.game = game;
		this.myCoeficient = myCoeficient;
		this.enemyCoeficient = enemyCoeficient;
	}
	
	@Override
	public double getValueFor(Vector2D position) {
		double enemyValue = 0;
		
		UnitSet enemiesNearby = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(position), 500);

		for(Unit unit : enemiesNearby) {
			enemyValue += unit.getHitPoints();
			if(unit.getType() == game.getUnitTypes().Zerg_Hatchery) {
				enemyValue += HATCHERY_BONUS;
			}
		}
	
		double myValue = 0;
		
		UnitSet myNearby = game.getMyUnits().whereLessOrEqual(new DistanceSelector(position), 500);

		for(Unit unit : myNearby) {
			myValue += unit.getHitPoints();
			if(unit.getType() == game.getUnitTypes().Zerg_Hatchery) {
				myValue += HATCHERY_BONUS;
			}
		}
		
		return enemyValue*enemyCoeficient - myValue*myCoeficient;
	}

	@Override
	public double combineSelfAndSiblings(VertexValue me, VertexValue[] siblings) {
		double siblingValues = 0;
		
		for(VertexValue sibling : siblings) {
			siblingValues += sibling.value;
		}
		
		return me.value * 0.92 + siblingValues * 0.08;
	}
}
