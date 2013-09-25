package sk.hackcraft.bwu.sample;

import javabot.model.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;

public class SampleBot extends Bot {
	static public void main(String [] arguments) {
		SampleBot bot = new SampleBot();
		bot.start();
	}
	
	public void onConnected() {}
	public void onGameStarted(Game game) {}
	public void onGameEnded() {}
	public void onDisconnected() {}
	public void onGameUpdate() {}
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
	public void onDraw(Graphics graphics) {}
}
