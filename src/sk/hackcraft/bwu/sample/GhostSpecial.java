package sk.hackcraft.bwu.sample;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.sample.GroupManager.Special;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class GhostSpecial implements Special {
	private Unit unit;
	
	public GhostSpecial(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean isActive(Game game) {
		return unit.getEnergy() >= 100;
	}

	@Override
	public void onGameUpdate(Game game) {
		Unit possibleTarget = game.getEnemyUnits().firstOf(
			game.getUnitTypes().Terran_Battlecruiser,
			game.getUnitTypes().Terran_Siege_Tank_Siege_Mode,
			game.getUnitTypes().Terran_Siege_Tank_Tank_Mode
		);
		
		if(possibleTarget == null) {
			return;
		}
		
		unit.useTech(game.getTechTypes().Lockdown, possibleTarget);
	}
}
