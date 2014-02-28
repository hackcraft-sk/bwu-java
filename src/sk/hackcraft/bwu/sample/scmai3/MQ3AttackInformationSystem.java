package sk.hackcraft.bwu.sample.scmai3;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class MQ3AttackInformationSystem extends MQ3DefenseInformationSystem {
	public MQ3AttackInformationSystem(Game game) {
		super(game);
	}

	@Override
	public double getValueFor(Vector2D position) {
		double value = 0;
		
		UnitSet enemiesNearby = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(position), 300);

		for(Unit unit : enemiesNearby) {
			value += unit.getHitPoints();
			if(unit.getType() == game.getUnitTypes().Zerg_Hatchery) {
				value += 500;
			}
		}
		
		UnitSet myHatcheriesUnderAttack = game.getMyUnits()
				.whereType(game.getUnitTypes().Zerg_Hatchery)
				.where(UnitSelector.IS_UNDER_ATTACK)
				.whereLessOrEqual(new DistanceSelector(position), 500);
		
		value += myHatcheriesUnderAttack.size()*2000;
		
	
		return value;
	}
}
