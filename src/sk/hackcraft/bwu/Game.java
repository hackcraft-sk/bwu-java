package sk.hackcraft.bwu;

import java.util.HashMap;

import jnibwapi.Map;
import jnibwapi.Player;

import sk.hackcraft.bwu.selection.UnitSet;

/**
 * Class representing a single instance of a game match. It is created when the
 * match is started and should be thrown away as the game ends (
 * <code>Bot.onGameEnded()</code>)
 * 
 * @author nixone
 * 
 */
public class Game
{
	static public final int TILE_SIZE = 32;

	final protected Bot bot;

	private HashMap<Integer, Unit> units = new HashMap<Integer, Unit>();

	final private UnitSet enemyUnits = new UnitSet();
	final private UnitSet myUnits = new UnitSet();
	final private UnitSet neutralUnits = new UnitSet();
	final private UnitSet allyUnits = new UnitSet();

	protected Game(Bot bot)
	{
		this.bot = bot;
	}

	/**
	 * Returns the bot this game corresponds to
	 * 
	 * @return
	 */
	public Bot getBot()
	{
		return bot;
	}

	protected Unit getUnit(int unitID)
	{
		if (!units.containsKey(unitID))
		{
			jnibwapi.Unit originalUnit = bot.BWAPI.getUnit(unitID);
			if (originalUnit == null)
			{
				return null;
			}
			Unit unit = (Unit) originalUnit;

			units.put(unitID, unit);

			if (unit.getPlayer().isSelf())
			{
				myUnits.add(unit);
			}
			if (unit.getPlayer().isNeutral())
			{
				neutralUnits.add(unit);
			}
			if (unit.getPlayer().isEnemy())
			{
				enemyUnits.add(unit);
			}
			if (unit.getPlayer().isAlly())
			{
				allyUnits.add(unit);
			}

			return unit;
		}
		return units.get(unitID);
	}

	protected void removeUnit(int unitID)
	{
		if (units.containsKey(unitID))
		{
			Unit unit = units.get(unitID);

			myUnits.remove(unit);
			neutralUnits.remove(unit);
			enemyUnits.remove(unit);
			allyUnits.remove(unit);

			units.remove(unitID);
		}
	}

	/**
	 * Returns the self player in this game
	 * 
	 * @return
	 */
	public Player getSelf()
	{
		return bot.BWAPI.getSelf();
	}

	/**
	 * Returns <code>UnitSet</code> containing all current accessible ally
	 * units.
	 * 
	 * @return
	 */
	public UnitSet getAllyUnits()
	{
		return allyUnits;
	}

	/**
	 * Returns <code>UnitSet</code> containing all current accessible my units.
	 * 
	 * @return
	 */
	public UnitSet getMyUnits()
	{
		return myUnits;
	}

	/**
	 * Returns <code>UnitSet</code> containing all current accessible enemy
	 * units.
	 * 
	 * @return
	 */
	public UnitSet getEnemyUnits()
	{
		return enemyUnits;
	}

	/**
	 * Returns <code>UnitSet</code> containing all current accessible neutral
	 * units.
	 * 
	 * @return
	 */
	public UnitSet getNeutralUnits()
	{
		return neutralUnits;
	}

	/**
	 * Get map info for this game
	 * 
	 * @return
	 */
	public Map getMap()
	{
		return bot.BWAPI.getMap();
	}

	/**
	 * Sets game speed
	 * 
	 * @param speed
	 *            game speed
	 */
	public void setSpeed(int speed)
	{
		bot.BWAPI.setGameSpeed(speed);
	}

	public void enableUserInput()
	{
		bot.BWAPI.enableUserInput();
	}

	public void enablePerfectInformation()
	{
		bot.BWAPI.enablePerfectInformation();
	}

	public int getFrameCount()
	{
		return bot.BWAPI.getFrameCount();
	}
}
