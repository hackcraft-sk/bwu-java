package sk.hackcraft.bwu.sample.scmai3;

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

public class MicroQueen3 extends Bot {
	static public double IS_AT_TOLERANCE = 100;
	static public double GROUP_DISTANCE_TOLERANCE = 200;
	
	static public Vector2D LEFT_BOTTOM_BASE = new Vector2D(520, 2745);
	static public Vector2D LEFT_TOP_BASE = new Vector2D(530, 409);
	
	static public Vector2D RIGHT_BOTTOM_BASE = new Vector2D(3454, 2636);
	static public Vector2D RIGHT_TOP_BASE = new Vector2D(3449, 417);
	
	static public Vector2D CENTER = new Vector2D(1500, 1500);
	
	static public Vector2D LEFT_START = new Vector2D(233, 1504);
	static public Vector2D RIGHT_START = new Vector2D(3512, 1599);
	
	static public Vector2D [] STARTING_POSITIONS = new Vector2D[]{ LEFT_START, RIGHT_START };
	
	static public void main(String [] arguments) {
		Bot bot = new MicroQueen3();
		bot.disableGraphics();
		bot.start();
	}
	
	private Game game = null;
	private Queue<Vector2D> positionsToExplore = new LinkedList<>();
	private Vector2D exploringPosition = null;
	
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
			// find my starting position
			Vector2D startingPosition = null;
			Vector2D armyPosition = getAttackGroup().getArithmeticCenter();
			
			for(Vector2D position : STARTING_POSITIONS) {
				if(startingPosition == null) {
					startingPosition = position;
				} else {
					double currentDistance = startingPosition.sub(armyPosition).length;
					double newDistance = position.sub(armyPosition).length;
					
					if(newDistance < currentDistance) {
						startingPosition = position;
					}
				}
			}
			
			// depeding on starting position, add positions to explore
			if(startingPosition == LEFT_START) {
				positionsToExplore.add(CENTER);
				positionsToExplore.add(RIGHT_TOP_BASE);
				positionsToExplore.add(CENTER);
				positionsToExplore.add(RIGHT_BOTTOM_BASE);
				positionsToExplore.add(CENTER);
				positionsToExplore.add(RIGHT_START);
			} else {
				positionsToExplore.add(CENTER);
				positionsToExplore.add(LEFT_TOP_BASE);
				positionsToExplore.add(CENTER);
				positionsToExplore.add(LEFT_BOTTOM_BASE);
				positionsToExplore.add(CENTER);
				positionsToExplore.add(LEFT_START);
			}
		}
		
		UnitSet buildingsUnderAttack = getBuildingUnderAttack();
		
		if(buildingsUnderAttack.size() > 0) {
			exploringPosition = buildingsUnderAttack.first().getPosition();
		} else {
			// discover new positions if nesessary
			if(positionsToExplore.size() <= 3) {
				Vector2D enemyCenter = game.getEnemyUnits().where(UnitSelector.IS_VISIBLE).getArithmeticCenter();
				if(enemyCenter != null) {
					positionsToExplore.add(enemyCenter);
				}
			}
		}
		
		// manage explore positions
		if(exploringPosition == null || getAttackGroup().areAt(exploringPosition, IS_AT_TOLERANCE)) {
			exploringPosition = positionsToExplore.poll();
		}
		
		handleRegroupingAndAttack();
	}
	
	@Override
	public void onDraw(Graphics graphics) {
		graphics.setScreenCoordinates();
		graphics.drawText(new Vector2D(10, 10), "MicroQueen3 by nixone");
		
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
			//graphics.drawText(unit, unit.getPosition());
		}
		
		if(exploringPosition != null) {
			graphics.setColor(Graphics.Color.ORANGE);
			graphics.setGameCoordinates();
			graphics.fillCircle(exploringPosition, 40);
		}
		
		graphics.setColor(Graphics.Color.PURPLE);
		graphics.setGameCoordinates();
		graphics.fillCircle(getAttackGroup().getArithmeticCenter(), 40);
	}
	
	public void handleRegroupingAndAttack() {
		Vector2D armyPosition = getAttackGroup().getArithmeticCenter();
		Vector2D shouldBePosition = armyPosition.scale(0.5).add(exploringPosition.scale(0.5));
		
		for(Unit unit : getAttackGroup()) {
			// if unit is not attacking and is too far
			if(!unit.isAttackFrame() && game.getFrameCount() % 50 == 17 && shouldBePosition.sub(unit.getPosition()).length > GROUP_DISTANCE_TOLERANCE) {
				unit.attack(shouldBePosition);
			// or, should attack
			} else if(exploringPosition != null && (unit.isIdle() || unit.isStuck()) && game.getFrameCount() % 30 == 7) {
				unit.attack(exploringPosition);
			}
		}
	}
	
	public UnitSet getAttackGroup() {
		return game.getMyUnits().whereNot(UnitSelector.IS_BUILDING).whereTypeNot(game.getUnitTypes().Zerg_Larva);
	}
	
	public UnitSet getBuildings() {
		return game.getMyUnits().whereType(game.getUnitTypes().Zerg_Hatchery);
	}
	
	public UnitSet getBuildingUnderAttack() {
		return getBuildings().where(UnitSelector.IS_UNDER_ATTACK);
	}
	
	public void say(Object something) {
		System.out.println("MicroQueen2: "+something.toString());
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
