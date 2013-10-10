package sk.hackcraft.bwu.sample.scmai3;

import java.util.Iterator;
import java.util.List;

import javabot.model.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Minimap;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.LogicalSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import sk.hackcraft.bwu.util.VectorGraph;
import sk.hackcraft.bwu.util.VectorGraph.InformationSystem;

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
		bot.disableGraphics();
		bot.start();
	}
	
	private Game game = null;
	
	private VectorGraph routeFinder;
	
	private Vector2D nextToGoAttackPosition;
	private Vector2D nextToGoDefensePosition;
	
	private UnitSet defenseGroup = new UnitSet();
	
	private InformationSystem defenseSystem = null;
	private InformationSystem attackSystem = null;
	
	@Override
	public void onGameStarted(Game game) {
		this.game = game;
		game.enableUserInput();
		game.setSpeed(20);
		
		routeFinder = new MQ3VectorGraph();
		defenseSystem = new MQ3DefenseInformationSystem(game);
		attackSystem = new MQ3AttackInformationSystem(game);
		nextToGoAttackPosition = null;
		nextToGoDefensePosition = null;
	}

	@Override
	public void onGameEnded() {
		game = null;
	}
	
	private void initialize() {
		// create groups
		createDefenseGroup();
		
		// send scouts to corners 
		Iterator<Unit> it = getScoutGroup().iterator();
		
		it.next().attack(new Vector2D(0.1, 0.1).scale(game.getMap()));
		it.next().attack(new Vector2D(0.9, 0.1).scale(game.getMap()));
		it.next().attack(new Vector2D(0.9, 0.9).scale(game.getMap()));
		it.next().attack(new Vector2D(0.1, 0.9).scale(game.getMap()));
	}
	
	private void createDefenseGroup() {
		defenseGroup = game.getMyUnits().firstNOf(12, game.getUnitTypes().Zerg_Zergling)
				.union(game.getMyUnits().firstNOf(8, game.getUnitTypes().Zerg_Hydralisk));
	}
	
	@Override
	public void onGameUpdate() {
		routeFinder.update(defenseSystem, 10);
		routeFinder.update(attackSystem, 10);
		handleBot();
	}
	
	private void handleBot() {
		if(game.getFrameCount() == 0) {
			initialize();
		}
		
		if(nextToGoAttackPosition == null || getAttackGroup().areAt(nextToGoAttackPosition, IS_AT_TOLERANCE)) {
			List<Vector2D> path = routeFinder.getUphillPath(attackSystem, getAttackGroup().getArithmeticCenter());
			do {
				nextToGoAttackPosition = path.remove(0);
			} while(path.size() > 0 && nextToGoAttackPosition != null && getAttackGroup().areAt(nextToGoAttackPosition, IS_AT_TOLERANCE));
		}
		
		if(nextToGoDefensePosition == null || defenseGroup.areAt(nextToGoDefensePosition, IS_AT_TOLERANCE)) {
			List<Vector2D> path = routeFinder.getUphillPath(defenseSystem, defenseGroup.getArithmeticCenter());
			do {
				nextToGoDefensePosition = path.remove(0);
			} while(path.size() > 0 && nextToGoDefensePosition != null && defenseGroup.areAt(nextToGoDefensePosition, IS_AT_TOLERANCE));
		}
		
		if(game.getFrameCount() > 0) {
			handleRegroupingAndAttack();
			handleScouting();
		}
	}
	
	@Override
	public void onDraw(Graphics graphics) {
		graphics.setScreenCoordinates();
		graphics.drawText(new Vector2D(10, 10), "MicroQueen3 by nixone");
		
		graphics.setGameCoordinates();

		renderAttackMinimap(graphics);
		renderDefenseMinimap(graphics);
	}
	
	private void renderAttackMinimap(Graphics graphics) {
		Minimap minimap = graphics.createMinimap(game.getMap(), new Vector2D(50, 100), new Vector2D(240, 160));
		minimap.setColor(Graphics.Color.RED);
		minimap.drawBounds();
		routeFinder.renderGraph(minimap);
		
		minimap.setColor(Graphics.Color.BLUE);
		minimap.fillCircle(getAttackGroup().getArithmeticCenter(), 5);
		
		if(nextToGoAttackPosition != null) {
			minimap.setColor(Graphics.Color.ORANGE);
			minimap.fillCircle(nextToGoAttackPosition, 5);
		}
		
		routeFinder.renderSystem(minimap, attackSystem);
	}
	
	private void renderDefenseMinimap(Graphics graphics) {
		Minimap minimap = graphics.createMinimap(game.getMap(), new Vector2D(340, 100), new Vector2D(240, 160));
		minimap.setColor(Graphics.Color.YELLOW);
		minimap.drawBounds();
		routeFinder.renderGraph(minimap);
		
		minimap.setColor(Graphics.Color.BLUE);
		minimap.fillCircle(defenseGroup.getArithmeticCenter(), 5);
		
		if(nextToGoDefensePosition != null) {
			minimap.setColor(Graphics.Color.ORANGE);
			minimap.fillCircle(nextToGoDefensePosition, 5);
		}
		
		routeFinder.renderSystem(minimap, defenseSystem);
	}
	
	public void handleRegroupingAndAttack() {
		if(nextToGoAttackPosition != null && getAttackGroup().size() > 0) {
			Vector2D armyPosition = getAttackGroup().getArithmeticCenter();
			Vector2D shouldBePosition = nextToGoAttackPosition
					.sub(armyPosition)
					.normalize()
					.scale(IS_AT_TOLERANCE)
					.scale(0.75)
					.add(nextToGoAttackPosition);
			
			for(Unit unit : getAttackGroup()) {
				if(
					(unit.isIdle() && !unit.isAt(nextToGoAttackPosition, IS_AT_TOLERANCE)) ||
					(!unit.isAttackFrame() && game.getFrameCount() % 50 == 13)
				) {
					unit.attack(shouldBePosition);
				}
			}
		}
		
		if(nextToGoDefensePosition != null) {
			Vector2D armyPosition = defenseGroup.getArithmeticCenter();
			Vector2D shouldBePosition = nextToGoDefensePosition
					.sub(armyPosition)
					.normalize()
					.scale(IS_AT_TOLERANCE)
					.scale(0.75)
					.add(nextToGoDefensePosition);
			
			for(Unit unit : defenseGroup) {
				if(
					(unit.isIdle() && !unit.isAt(nextToGoDefensePosition, IS_AT_TOLERANCE)) ||
					(!unit.isAttackFrame() && game.getFrameCount() % 50 == 13)
				) {
					unit.attack(shouldBePosition);
				}
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
		)).minus(defenseGroup);
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
