package sk.hackcraft.bwu.sample;

import java.util.Random;

import javabot.model.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;

public class SampleBot extends Bot {	
	static public void main(String [] arguments) {
		Bot bot = new SampleBot();
		bot.start();
	}
	
	private Random random = new Random();
	private Game game = null;
	
	@Override
	public void onGameStarted(Game game) {
		this.game = game;
	}

	@Override
	public void onGameEnded() {
		game = null;
	}
	
	@Override
	public void onGameUpdate() {
		for(Unit unit : game.getMyUnits()) {
			if(unit.isIdle()) {
				unit.attack(new Vector2D(
					random.nextDouble()*game.getMap().getWidth(),
					random.nextDouble()*game.getMap().getHeight()
				));
			}
		}
	}
	
	public void onConnected() {}
	public void onDisconnected() {}
	public void onKeyPressed(int keyCode) {}
	public void onMatchEnded(boolean isWinner) {}
	public void onPlayerLeft(Player player) {}
	public void onNukeDetected(Vector2D position) {}
	public void onUnitDiscovered(Unit unit) {}
	public void onUnitDestroyed(Unit unit) {}
	public void onUnitEvaded(Unit unit) {}
	public void onUnitCreated(Unit unit) {}
	public void onUnitMorphed(Unit unit) {}
	public void onUnitShown(Unit unit) {}
	public void onUnitHidden(Unit unit) {}
}
