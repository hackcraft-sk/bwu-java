package sk.hackcraft.bwu.sample;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.sample.GroupManager.Special;

public class SVSpecial implements Special {
	private Unit unit;
	
	public SVSpecial(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean isActive(Game game) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onGameUpdate(Game game) {
		// TODO Auto-generated method stub
		
	}
}
