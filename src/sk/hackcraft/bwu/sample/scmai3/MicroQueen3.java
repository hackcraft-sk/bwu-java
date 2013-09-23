package sk.hackcraft.bwu.sample.scmai3;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javabot.model.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.LogicalSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import sk.hackcraft.bwu.util.VectorGraph;

public class MicroQueen3 extends Bot {
	static public double IS_AT_TOLERANCE = 200;
	static public double GROUP_DISTANCE_TOLERANCE = 300;
	
	static public Vector2D LEFT_BOTTOM_BASE = new Vector2D(452, 2776);
	static public Vector2D LEFT_TOP_BASE = new Vector2D(456, 292);
	
	static public Vector2D RIGHT_BOTTOM_BASE = new Vector2D(3632, 2748);
	static public Vector2D RIGHT_TOP_BASE = new Vector2D(3588, 304);
	
	static public Vector2D CENTER = new Vector2D(2031, 1526);
	
	static public Vector2D LEFT_START = new Vector2D(233, 1504);
	static public Vector2D RIGHT_START = new Vector2D(3512, 1599);
	
	static public Vector2D [] STARTING_POSITIONS = new Vector2D[]{ LEFT_START, RIGHT_START };
	
	static public void main(String [] arguments) {
		Bot bot = new MicroQueen3();
		//bot.disableGraphics();
		bot.start();
	}
	
	static enum State {
		DEFENDING_BUILDING, DEFENDING_UNITS, MOVING
	}
	
	private Game game = null;
	private Queue<Vector2D> positionsToExplore = new LinkedList<>();
	
	private State state = State.MOVING;
	private VectorGraph routeFinder = new MQ3VectorGraph();
	
	private Vector2D targetPosition = null;
	private Vector2D nextToGoPosition = null;
	private List<Vector2D> currentPath = null;
	
	@Override
	public void onGameStarted(Game game) {
		this.game = game;
		game.enableUserInput();
		game.setSpeed(20);
		
		positionsToExplore.clear();
	}

	@Override
	public void onGameEnded() {
		game = null;
	}
	
	private void initialize() {
		
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
			positionsToExplore.add(LEFT_BOTTOM_BASE);
			positionsToExplore.add(RIGHT_BOTTOM_BASE);
			positionsToExplore.add(RIGHT_TOP_BASE);
		} else {
			positionsToExplore.add(RIGHT_BOTTOM_BASE);
			positionsToExplore.add(LEFT_BOTTOM_BASE);
			positionsToExplore.add(LEFT_TOP_BASE);
		}
	}
	
	@Override
	public void onGameUpdate() {		
		handleBot();
	}
	
	private void handleBot() {
		if(game.getFrameCount() == 0) {
			initialize();
		}
		
		// discover new positions if nesessary
		if(positionsToExplore.size() <= 3) {
			Vector2D enemyCenter = game.getEnemyUnits().where(UnitSelector.IS_VISIBLE).getArithmeticCenter();
			if(enemyCenter != null) {
				positionsToExplore.add(enemyCenter);
			}
		}
		
		if(targetPosition == null || getAttackGroup().areAt(targetPosition, IS_AT_TOLERANCE)) {
			say("Selecting next TARGET");
			targetPosition = positionsToExplore.poll();
		}
		
		if(nextToGoPosition == null || getAttackGroup().areAt(nextToGoPosition, IS_AT_TOLERANCE)) {
			say("Selecting next NEXT TO GO");
			currentPath = routeFinder.getShortestPath(getAttackGroup().getArithmeticCenter(), targetPosition);
			nextToGoPosition = currentPath.get(0);
		}
		
		handleRegroupingAndAttack();
		handleScouting();
	}
	
	@Override
	public void onDraw(Graphics graphics) {
		graphics.setScreenCoordinates();
		graphics.drawText(new Vector2D(10, 10), "MicroQueen3 by nixone");
		graphics.drawText(new Vector2D(10, 20), state);
		graphics.drawText(new Vector2D(10, 30), "Count of positions: "+positionsToExplore.size());
		if(currentPath != null) {
			graphics.drawText(new Vector2D(10, 40), "Length of current path: "+currentPath.size());
		}
		
		
		graphics.setGameCoordinates();
		
		List<Vector2D> travelingPath = new LinkedList<>();
		travelingPath.add(getAttackGroup().getArithmeticCenter());
		travelingPath.addAll(currentPath);
		
		graphics.setColor(Graphics.Color.WHITE);
		
		for(int i=0; i<(travelingPath.size()-1); i++) {
			graphics.drawLine(travelingPath.get(i), travelingPath.get(i+1));
		}
		
		if(nextToGoPosition != null) {
			graphics.setColor(Graphics.Color.ORANGE);
			graphics.setGameCoordinates();
			graphics.fillCircle(nextToGoPosition, 10);
			graphics.drawCircle(nextToGoPosition, (int)IS_AT_TOLERANCE);
		}
		
		if(targetPosition != null) {
			graphics.setColor(Graphics.Color.RED);
			graphics.setGameCoordinates();
			graphics.fillCircle(targetPosition.add(new Vector2D(2,2)), 10);
			graphics.drawCircle(targetPosition.add(new Vector2D(2, 2)), (int)IS_AT_TOLERANCE);
		}
		
		graphics.setColor(Graphics.Color.PURPLE);
		graphics.setGameCoordinates();
		graphics.fillCircle(getAttackGroup().getArithmeticCenter(), 40);
		
		routeFinder.render(graphics, game.getMap(), new Vector2D(10, 60), new Vector2D(150, 100),
			getAttackGroup().getArithmeticCenter(),
			nextToGoPosition,
			targetPosition
		);
	}
	
	public void handleRegroupingAndAttack() {
		if(nextToGoPosition == null) {
			return;
		}
		
		Vector2D armyPosition = getAttackGroup().getArithmeticCenter();
		Vector2D shouldBePosition = nextToGoPosition
				.sub(armyPosition)
				.normalize()
				.scale(IS_AT_TOLERANCE)
				.scale(0.75)
				.add(nextToGoPosition);
		
		for(Unit unit : getAttackGroup()) {
			if(
				(unit.isIdle() && !unit.isAt(nextToGoPosition, IS_AT_TOLERANCE)) ||
				(!unit.isAttackFrame() && game.getFrameCount() % 50 == 13)
			) {
				unit.attack(shouldBePosition);
			}
		}
	}
	
	public void handleScouting() {
		for(Unit scout : getScoutGroup()) {
			UnitSet closeThreats = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(scout), 400).where(UnitSelector.CAN_ATTACK_AIR);
			// is under threat
			if(closeThreats.size() > 0) {
				Vector2D avoidance = scout.getPosition().sub(closeThreats.getArithmeticCenter()).normalize().scale(800).add(scout.getPosition());
				
				if(game.getFrameCount() % 20 == 3) {
					scout.move(avoidance);
				}
			// nope
			} else {
				if(scout.isIdle()) {
					scout.move(Vector2D.random().scale(
						game.getMap().getWidth()*Game.TILE_SIZE,
						game.getMap().getHeight()*Game.TILE_SIZE
					));
				}
			}
		}
	}
	
	public void handleQueen() {
		
	}
	
	public boolean isUnderAttack() {
		UnitSet allMy = getAttackGroup();
		UnitSet myUnderAttack = allMy.where(UnitSelector.IS_UNDER_ATTACK);
		
		return myUnderAttack.size() >= 2;
	}
	
	public UnitSet getAttackGroup() {
		return game.getMyUnits().where(new LogicalSelector.Or(
			new UnitSelector.UnitTypeSelector(game.getUnitTypes().Zerg_Zergling),
			new UnitSelector.UnitTypeSelector(game.getUnitTypes().Zerg_Ultralisk),
			new UnitSelector.UnitTypeSelector(game.getUnitTypes().Zerg_Hydralisk)
		));
	}
	
	public UnitSet getScoutGroup() {
		return game.getMyUnits().where(new LogicalSelector.Or(
			new UnitSelector.UnitTypeSelector(game.getUnitTypes().Zerg_Scourge)
		));
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
