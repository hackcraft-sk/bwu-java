package sk.hackcraft.bwu.intelligence;

import java.util.Map;
import java.util.Set;

import sk.hackcraft.bwu.ResourceType;
import sk.hackcraft.bwu.Updateable;
import jnibwapi.Unit;
import jnibwapi.types.UnitType;

public interface EnemyIntelligence extends Updateable
{
	/**
	 * Called when enemy unit was spotted. This can be called multiple times on
	 * same unit, for example when it leaves visible range and is spotted again
	 * later.
	 * 
	 * @param unit
	 *            spotted unit
	 */
	void unitSpotted(Unit unit);

	/**
	 * Called when enemy unit was visibly destroyed.
	 * 
	 * @param unit
	 *            destroyed unit
	 */
	void unitDestroyed(Unit unit);

	/**
	 * Called when resource occupied resource is spotted. Through this method
	 * it's possible to approximate how much resources enemy gathered.
	 * 
	 * @param resource
	 *            spotted resource
	 */
	void enemyControlledResourceSpotted(Unit resource);

	Map<ResourceType, Integer> getApproximateEnemySpentResouces();

	/**
	 * Returns set of types of enemy buildings, which are known to be
	 * constructed. This can be used to determine which kind of tech is
	 * available to enemy. This method can also returns buildings which can be
	 * already destroyed (and thus not available to enemy), as it is not simple
	 * to determine if all instances of specific building type were destroyed
	 * during the game.
	 * 
	 * @return set of building types, which enemy probably owns
	 */
	Set<UnitType> getEnemyTech();

	/**
	 * Returns approximate enemy army size, based on available informations, as
	 * spotted units, tech available to enemy, or spent resources. Returned set
	 * have to contains only battle units, not units used primarily for economic
	 * part of game (workers, overlords) or buildings.
	 * 
	 * @return map contains battle unit types and approximate amount of
	 *         instances of that type
	 */
	Map<UnitType, Integer> getApproximateEnemyArmySize();
}
