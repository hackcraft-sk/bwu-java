package sk.hackcraft.bwu.selection;

import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.selection.UnitSelector.BooleanSelector;
import javabot.model.Player;

public class PlayerSelector implements BooleanSelector {
	private Player player;
	
	public PlayerSelector(Player player) {
		this.player = player;
	}

	@Override
	public boolean isTrueFor(Unit unit) {
		return unit.getPlayer() == this.player;
	}
}
