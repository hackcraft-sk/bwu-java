package sk.hackcraft.bwu.sample;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.sample.GroupManager.Special;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class MedicSpecial implements Special {
	private Unit unit;
	
	public MedicSpecial(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean isActive(Game game) {
		return unit.getEnergy() >= 50 && game.getMyUnits().where(UnitSelector.IS_LOCKED_DOWN).size() > 0;
	}

	@Override
	public void onGameUpdate(Game game) {
		if(unit.getEnergy() < 50) {
			return;
		}
		
		UnitSet lockedDown = game.getMyUnits().where(UnitSelector.IS_LOCKED_DOWN);
		if(lockedDown.size() == 0) {
			return;
		}
	}
}
