package sk.hackcraft.bwu.sample.scmai3;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class MQ3DefenseInformationSystem extends MQ3InformationSystem {
	public MQ3DefenseInformationSystem(Game game) {
		super(game);
	}

	@Override
	public double getValueFor(Vector2D position) {
		double value = 0;
		
		UnitSet myNearby = game.getMyUnits().whereLessOrEqual(new DistanceSelector(position), 500);

		for(Unit unit : myNearby) {
			value += unit.getHitPoints();
			if(unit.getType() == game.getUnitTypes().Zerg_Hatchery) {
				value += 30000;
			}
		}
		
		return value;
	}
}
