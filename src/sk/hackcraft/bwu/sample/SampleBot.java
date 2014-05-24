package sk.hackcraft.bwu.sample;

import jnibwapi.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.UnitSet;

public class SampleBot extends Bot {
	static public void main(String [] arguments) {
		SampleBot bot = new SampleBot();
		bot.start();
	}
	
	private Game game = null;
	
	public SampleBot() {
		super(false);
	}
	
	public void onConnected() {}
	public void onGameStarted(Game game) {
		this.game = game;		
	}
	public void onGameEnded() {
		this.game = null;
	}
	public void onGameUpdate() {
		UnitSet myUnits = game.getMyUnits();
		
		for(Unit unit : myUnits) {
			if(unit.isIdle() || unit.isStuck()) {
				// generate new position
				Vector2D position = Vector2D.random().scale(
					game.getMap().getWidth()*Game.TILE_SIZE,
					game.getMap().getHeight()*Game.TILE_SIZE
				);
				// attack move!
				unit.attack(position);
			}
		}
	}
	
	public void onDisconnected() {}
	public void onKeyPressed(int keyCode) {}
	public void onGameEnded(boolean isWinner) {}
	public void onPlayerDropped(Player player) {}
	public void onNukeDetected(Vector2D position) {}
	public void onUnitDiscovered(Unit unit) {}
	public void onUnitDestroyed(Unit unit) {}
	public void onUnitEvaded(Unit unit) {}
	public void onUnitCreated(Unit unit) {}
	public void onUnitMorphed(Unit unit) {}
	public void onUnitShown(Unit unit) {}
	public void onUnitHidden(Unit unit) {}
	public void onDraw(Graphics graphics) {}
	public void onPlayerLeft(Player player) {}
	public void onUnitCompleted(Unit unit) {}
	public void onUnitRenegade(Unit unit) {}
}
