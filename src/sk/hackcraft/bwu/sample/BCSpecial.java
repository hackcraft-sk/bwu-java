package sk.hackcraft.bwu.sample;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.sample.GroupManager.Special;
import sk.hackcraft.bwu.selection.DistanceSelector;

public class BCSpecial implements Special {
	private Unit unit;
	
	public BCSpecial(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean isActive(Game game) {
		if(unit.getEnergy() < 150) {
			return false;
		}
		
		Unit possibleTarget = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(unit), game.getWeaponTypes().Yamato_Gun.getMaxRange()).firstOf(
				game.getUnitTypes().Terran_Battlecruiser,
				game.getUnitTypes().Terran_Siege_Tank_Siege_Mode,
				game.getUnitTypes().Terran_Siege_Tank_Tank_Mode
			);
		
		if(possibleTarget == null) {
			return false;
		}
		return true;
	}
	
	@Override
	public void onGameUpdate(Game game) {
		Unit possibleTarget = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(unit), game.getWeaponTypes().Yamato_Gun.getMaxRange()).firstOf(
				game.getUnitTypes().Terran_Battlecruiser,
				game.getUnitTypes().Terran_Siege_Tank_Siege_Mode,
				game.getUnitTypes().Terran_Siege_Tank_Tank_Mode
			);
		
		if(possibleTarget == null) {
			return;
		}
		
		if(unit.isIdle()) {
			unit.useTech(game.getTechTypes().Yamato_Gun, possibleTarget);
		}
	}
}
