package sk.hackcraft.bwu;

import static sk.hackcraft.bwu.Vector2DMath.*;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position.PosType;
import jnibwapi.types.WeaponType;

/**
 * The Unit class is used to get information about individual units as well as
 * issue orders to units. Each unit in the game has a unique Unit object, and
 * Unit objects are not deleted until the end of the match.
 * 
 * Note: There are 4 tiers of unit accessibility that determine which functions
 * AIs will have access to. If the Complete Map Information cheat flag is
 * enabled, you have perfect information of every unit. Otherwise, the functions
 * that will return information will be limited depending on the unit's
 * visibility. To learn more about unit accessibility, read the
 * <code>http://code.google.com/p/bwapi/wiki/BWAPIOverview</code>
 * 
 * @author nixone
 */
public class Unit extends jnibwapi.Unit
{
	final protected Game game;

	protected Unit(Game game, int id, JNIBWAPI jnibwapi)
	{
		super(id, jnibwapi);
		this.game = game;
	}

	public boolean isAt(Vector2D position, double tolerance)
	{
		return sub(getPositionVector(), position).getLength() <= tolerance;
	}

	@Deprecated
	public Vector2D getPositionVector()
	{
		return toVector(getPosition(), PosType.PIXEL);
	}

	public void attack(Vector2D position)
	{
		attack(position, false);
	}

	public void attack(Vector2D vector, boolean queued)
	{
		attack(toPosition(vector, PosType.PIXEL), false);
	}

	public WeaponType getGroundWeaponType()
	{
		return getType().getGroundWeapon();
	}

	public WeaponType getAirWeaponType()
	{
		return game.bot.BWAPI.getWeaponType(getType().getAirWeaponID());
	}
}
