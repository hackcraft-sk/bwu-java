package sk.hackcraft.bwu.sample;

import java.util.LinkedList;
import java.util.Queue;

import javabot.model.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class MicroQueen2 extends Bot {
	static public double IS_AT_TOLERANCE = 300;
	
	static public Vector2D LEFT_HIDE_PLACE = new Vector2D(29, 1965);
	static public Vector2D RIGHT_HIDE_PLACE = new Vector2D(4000, 1970);
	
	static public Vector2D LEFT_START = new Vector2D(620, 329);
	static public Vector2D RIGHT_START = new Vector2D(3500, 300);
	static public Vector2D TOP_CHOKE = new Vector2D(1942, 329);
	
	static public Vector2D [] STARTING_POSITIONS = new Vector2D[] { LEFT_START, RIGHT_START };
	
	static public void main(String [] arguments) {
		Bot bot = new MicroQueen2();
		bot.start();
	}
	
	private Game game = null;
	private Queue<Vector2D> positionsToExplore = new LinkedList<>();
	private Vector2D exploringPosition = null;
	private GroupManager groupManager = new GroupManager();
	
	@Override
	public void onGameStarted(Game game) {
		this.game = game;
		game.enableUserInput();
		game.setSpeed(10);
	}

	@Override
	public void onGameEnded() {
		game = null;
	}
	
	@Override
	public void onGameUpdate() {
		if(game.getFrameCount() == 0) {
			positionsToExplore.add(TOP_CHOKE);
			positionsToExplore.add(getEnemyStartingPosition());
		}
		
		if(exploringPosition == null || getAttackGroup().areAt(exploringPosition, IS_AT_TOLERANCE)) {
			exploringPosition = positionsToExplore.poll();
			groupManager.setTarget(exploringPosition);
			say("Target set to "+exploringPosition);
		}
		
		if(positionsToExplore.size() <= 3) {
			Vector2D enemyCenter = game.getEnemyUnits().where(UnitSelector.IS_VISIBLE).getArithmeticCenter();
			if(enemyCenter != null) {
				positionsToExplore.add(enemyCenter);
			}
		}
		
		groupManager.onGameUpdate(game);
	}
	
	@Override
	public void onDraw(Graphics graphics) {
		graphics.setGameCoordinates();
		
		for(Unit unit : game.getMyUnits()) {
			if(unit.isAttacking()) {
				graphics.setColor(Graphics.Color.RED);
				Unit target = unit.getTargetUnit();
				if(target == null) {
					graphics.drawLine(unit.getPosition(), unit.getTargetPosition());
				} else {
					graphics.drawLine(unit.getPosition(), target.getPosition());
				}
			} else if(unit.isMoving()) {
				graphics.setColor(Graphics.Color.BLUE);
				graphics.drawLine(unit.getPosition(), unit.getTargetPosition());
			}
			graphics.drawText(unit, unit.getPosition());
		}
		
		if(exploringPosition != null) {
			graphics.setColor(Graphics.Color.ORANGE);
			graphics.setGameCoordinates();
			graphics.fillCircle(exploringPosition, 40);
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

	public void say(Object something) {
		System.out.println("MicroQueen2: "+something.toString());
	}
	
	public Vector2D getEnemyStartingPosition() {
		Vector2D furthest = null;
		double furthestDistance = Double.MIN_VALUE;
		
		Vector2D myPosition = getAttackGroup().getArithmeticCenter();
		
		for(Vector2D possiblePosition : STARTING_POSITIONS) {
			double distance = possiblePosition.sub(myPosition).length;
			
			if(possiblePosition.sub(myPosition).length > furthestDistance) {
				furthest = possiblePosition;
				furthestDistance = distance;
			}
		}
		
		return furthest;
	}
	
	public void regroup() {
		Vector2D center = getAttackGroup().getArithmeticCenter();
		
		for(Unit unit : getAttackGroup()) {
			double distance = unit.getPosition().sub(center).length;
			if(distance > 150 || (distance > 75 && unit.isIdle())) {
				unit.attack(center);
			}
			if(distance > 75 && unit.isStuck()) {
				unit.stop();
			}
		}
	}
	
	public void attackOnExploringPosition() {
		for(Unit unit : getAttackGroup()) {
			if(unit.isIdle()) {
				unit.attack(exploringPosition);
			}
		}
	}
	
	public void doGhostLockdownIfPossible() {

	}
	
	public UnitSet getAttackGroup() {
		return game.getMyUnits();
	}
}
