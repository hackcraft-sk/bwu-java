package sk.hackcraft.bwu.sample;

import javabot.types.WeaponType;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.sample.GroupManager.Special;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class TankSpecial implements Special {
	private Unit unit;
	private int timeFromLastSiegeChange = 0;
	
	public TankSpecial(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean isActive(Game game) {
		WeaponType siegeCannon = game.getWeaponTypes().Arclite_Shock_Cannon;
		
		return unit.isSieged() || game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(unit), siegeCannon.getMaxRange()).size() > 3;
	}

	@Override
	public void onGameUpdate(Game game) {
		timeFromLastSiegeChange++;
		
		WeaponType siegeCannon = game.getWeaponTypes().Arclite_Shock_Cannon;
		
		UnitSet possibleTargets = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(unit), siegeCannon.getMaxRange());
		
		if(possibleTargets.size() > 3 && !unit.isSieged() && timeFromLastSiegeChange > 100) {
			unit.siege();
			timeFromLastSiegeChange = 0;
		} else if(possibleTargets.size() <= 3 && unit.isSieged() && timeFromLastSiegeChange > 100) {
			unit.unsiege();
			timeFromLastSiegeChange = 0;
		}
	}
}
