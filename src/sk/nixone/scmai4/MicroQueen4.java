package sk.nixone.scmai4;

import java.util.Iterator;
import java.util.List;

import jnibwapi.Player;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.AbstractBot;
import sk.hackcraft.bwu.BWU;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Minimap;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.Vector2DMath;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.LogicalSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import sk.hackcraft.bwu.util.Clustering;
import sk.hackcraft.bwu.util.VectorGraph;
import sk.hackcraft.bwu.util.VectorGraph.InformationSystem;

public class MicroQueen4 extends AbstractBot
{
	static public float IS_AT_TOLERANCE = 400;

	static public Vector2D LEFT_BOTTOM_BASE = new Vector2D(452, 2776);
	static public Vector2D LEFT_TOP_BASE = new Vector2D(456, 292);

	static public Vector2D RIGHT_BOTTOM_BASE = new Vector2D(3632, 2748);
	static public Vector2D RIGHT_TOP_BASE = new Vector2D(3588, 304);

	static public Vector2D CENTER = new Vector2D(2031, 1526);

	static public Vector2D LEFT_START = new Vector2D(233, 1504);
	static public Vector2D RIGHT_START = new Vector2D(3512, 1599);

	static public Vector2D[] STARTING_POSITIONS = new Vector2D[] { LEFT_START, RIGHT_START };

	static public void main(String[] arguments)
	{
		BWU bwu = new BWU()
		{
			@Override
			protected Bot createBot(Game game)
			{
				return new MicroQueen4(game);
			}
		};
		
		bwu.start();
	}

	private VectorGraph routeFinder;

	private InformationSystem informationSystem = null;

	private Clustering clustering = null;

	public MicroQueen4(Game game)
	{
		super(game);
	}

	@Override
	public void gameStarted()
	{
		game.enableUserInput();
		game.setSpeed(20);

		routeFinder = new MQ4VectorGraph();
		informationSystem = new MQ4InformationSystem(game);
		clustering = new Clustering(game.getMap(), 3);
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
	}

	private void initialize()
	{
		// send scouts to corners
		Iterator<Unit> it = getScoutGroup().iterator();

		Vector2D[] firstPositionsToScout = new Vector2D[] { new Vector2D(0.1f, 0.1f).scale(game.getMap()), new Vector2D(0.9f, 0.1f).scale(game.getMap()), new Vector2D(0.9f, 0.9f).scale(game.getMap()), new Vector2D(0.1f, 0.9f).scale(game.getMap()) };

		for (Vector2D positionToScout : firstPositionsToScout)
		{
			it.next().attack(positionToScout);
		}
	}

	@Override
	public void gameUpdated()
	{
		routeFinder.update(informationSystem, 10);
		if (game.getFrameCount() % 15 == 13)
		{
			clustering.updateFor(getAttackGroup());
		}
		handleBot();
	}

	private void handleBot()
	{
		if (game.getFrameCount() == 0)
		{
			initialize();
		}

		if (game.getFrameCount() > 0)
		{
			handleAttack();
		}

		if (game.getFrameCount() > 20)
		{
			handleScouting();
		}
	}

	@Override
	public void draw(Graphics graphics)
	{
		graphics.setScreenCoordinates();

		graphics.setGameCoordinates();

		renderAttackMinimap(graphics);
	}

	private void renderAttackMinimap(Graphics graphics)
	{
		Minimap minimap = graphics.createMinimap(game.getMap(), new Vector2D(220, 25), new Vector2D(300, 300));
		minimap.setColor(BWColor.Yellow);
		minimap.drawBounds();
		routeFinder.renderGraph(minimap);

		routeFinder.renderSystem(minimap, informationSystem);
		clustering.drawOn(minimap);
	}

	public void handleAttack()
	{
		for (Unit unit : getAttackGroup())
		{
			List<Vector2D> path = routeFinder.getUphillPath(informationSystem, unit.getPositionVector());

			Vector2D nextToVisit = null;

			while (!path.isEmpty() && (nextToVisit == null || unit.isAt(nextToVisit, IS_AT_TOLERANCE)))
			{
				nextToVisit = path.remove(0);
			}

			if (nextToVisit == null)
			{
				return;
			}

			if (((unit.isIdle() || unit.isStuck()) && !unit.isAt(nextToVisit, IS_AT_TOLERANCE)) || (!unit.isAttackFrame() && game.getFrameCount() % 20 == 13))
			{
				Vector2D shouldBePosition = nextToVisit.sub(unit.getPositionVector()).normalize().scale(IS_AT_TOLERANCE * 2).add(nextToVisit);
				unit.attack(shouldBePosition);
			}
		}
	}

	public void handleScouting()
	{
		for (Unit scout : getScoutGroup())
		{
			UnitSet closeThreats = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(scout), 400).where(UnitSelector.CAN_ATTACK_AIR);
			// is under threat

			if (closeThreats.size() > 0)
			{
				Vector2D avoidance = scout.getPositionVector().sub(closeThreats.getArithmeticCenter()).normalize().scale(800).add(scout.getPositionVector());

				if (game.getFrameCount() % 20 == 3)
				{
					scout.move(avoidance.toPosition(), false);
				}
				// nope
			}
			else
			{
				if (scout.isIdle())
				{
					scout.move(Vector2DMath.randomVector().scale(game.getMap()).toPosition(), false);
				}
			}
		}
	}

	public boolean isUnderAttack()
	{
		UnitSet allMy = getAttackGroup();
		UnitSet myUnderAttack = allMy.where(UnitSelector.IS_UNDER_ATTACK);

		return myUnderAttack.size() >= 2;
	}

	public UnitSet getAttackGroup()
	{
		return game.getMyUnits().where(new LogicalSelector.Or(new UnitSelector.UnitTypeSelector(UnitType.UnitTypes.Zerg_Zergling), new UnitSelector.UnitTypeSelector(UnitType.UnitTypes.Zerg_Ultralisk), new UnitSelector.UnitTypeSelector(UnitType.UnitTypes.Zerg_Hydralisk)));
	}

	public UnitSet getScoutGroup()
	{
		return game.getMyUnits().where(new LogicalSelector.Or(new UnitSelector.UnitTypeSelector(UnitType.UnitTypes.Zerg_Scourge)));
	}

	public UnitSet getBuildings()
	{
		return game.getMyUnits().whereType(UnitType.UnitTypes.Zerg_Hatchery);
	}

	public UnitSet getBuildingUnderAttack()
	{
		return getBuildings().where(UnitSelector.IS_UNDER_ATTACK);
	}

	public void say(Object something)
	{
		getPrintStream().println("MicroQueen4: " + something.toString());
	}

	@Override
	public void onConnected()
	{
	}

	@Override
	public void keyPressed(int keyCode)
	{
	}

	@Override
	public void playerDropped(Player player)
	{
	}

	@Override
	public void nukeDetected(Vector2D position)
	{
	}

	@Override
	public void unitDiscovered(Unit unit)
	{
	}

	@Override
	public void unitDestroyed(Unit unit)
	{
	}

	@Override
	public void unitEvaded(Unit unit)
	{
	}

	@Override
	public void unitCreated(Unit unit)
	{
	}

	@Override
	public void unitMorphed(Unit unit)
	{
	}

	@Override
	public void unitShowed(Unit unit)
	{
	}

	@Override
	public void unitHid(Unit unit)
	{
	}

	@Override
	public void playerLeft(Player player)
	{
	}

	@Override
	public void unitCompleted(Unit unit)
	{
	}

	@Override
	public void unitRenegaded(Unit unit)
	{
	}

	@Override
	public void messageSent(String message)
	{
	}

	@Override
	public void messageReceived(String message)
	{
	}

	@Override
	public void gameSaved(String gameName)
	{
	}
}
