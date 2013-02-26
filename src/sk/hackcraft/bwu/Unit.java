package sk.hackcraft.bwu;

import javabot.model.Player;
import javabot.types.OrderType;
import javabot.types.TechType;
import javabot.types.UnitCommandType;
import javabot.types.UnitType;
import javabot.types.UpgradeType;
import javabot.types.WeaponType;

/**
 * The Unit class is used to get information about individual units as well as issue orders to units.
 * Each unit in the game has a unique Unit object, and Unit objects are not deleted until the end of
 * the match.
 * 
 * Note: There are 4 tiers of unit accessibility that determine which functions AIs will have access
 * to. If the Complete Map Information cheat flag is enabled, you have perfect information of every
 * unit. Otherwise, the functions that will return information will be limited depending on the
 * unit's visibility. To learn more about unit accessibility, read the 
 * <code>http://code.google.com/p/bwapi/wiki/BWAPIOverview</code>
 * 
 * @author nixone
 */
public class Unit {
	final protected Game game;
	final protected javabot.model.Unit originalUnit;
	
	protected Unit(Game game, javabot.model.Unit originalUnit) {
		this.game = game;
		this.originalUnit = originalUnit;
	}
	
	/**
	 * Current type of the unit.
	 * @return
	 */
	public UnitType getType() {
		return game.bot.BWAPI.getUnitType(originalUnit.getTypeID());
	}
	
	/**
	 * Unit's ground weapon type.
	 * @return
	 */
	public WeaponType getGroundWeaponType() {
		return game.bot.BWAPI.getWeaponType(getType().getGroundWeaponID());
	}
	
	/**
	 * Unit's air weapon type
	 * @return
	 */
	public WeaponType getAirWeaponType() {
		return game.bot.BWAPI.getWeaponType(getType().getAirWeaponID());
	}
	
	/**
	 * Bridge to original JNIBWAPI unit. About to be deprecated. Do not use
	 * it if not nessecary.
	 * @return
	 */
	public javabot.model.Unit getOriginalUnit() {
		return originalUnit;
	}
	
	/**
	 * Internal ID unique for unit in the context of a match.
	 * @return
	 */
	public int getID() {
        return originalUnit.getID();
    }

	/**
	 * Current player owning this unit
	 * @return
	 */
    public Player getPlayer() {
        return game.bot.BWAPI.getPlayer(originalUnit.getPlayerID());
    }

    /**
     * Current game position of a unit
     * @return
     */
    public Vector2D getPosition() {
        return new Vector2D(originalUnit.getX(), originalUnit.getY());
    }

    /**
     * Current tile position of a unit
     * @return
     */
    public Vector2D getTilePosition() {
    	return new Vector2D(originalUnit.getX(), originalUnit.getY());
    }

    /**
     * Returns the direction the unit is facing, measured in radians. An angle of 0 means the unit is facing east.
     * @return
     */
    public double getAngle() {
        return originalUnit.getAngle();
    }

    /**
     * Returns the unit's velocity measured in pixels per frame.
     * @return
     */
    public Vector2D getVelocity() {
    	return new Vector2D(originalUnit.getVelocityX(), originalUnit.getVelocityY());
    }

    /**
     * Returns the unit's current amount of hit points.
     * @return
     */
    public int getHitPoints() {
        return originalUnit.getHitPoints();
    }

    /**
     * Returns the unit's current amount of shields.
     * @return
     */
    public int getShield() {
        return originalUnit.getShield();
    }

    /**
     * Returns the unit's current amount of energy.
     * @return
     */
    public int getEnergy() {
        return originalUnit.getEnergy();
    }

    /**
     * Returns the unit's current amount of containing resources. Useful for determining how much minerals are left in a mineral patch, or how much gas is left in a geyser (can also be called on a refinery/assimilator/extractor).
     * @return
     */
    public int getResources() {
        return originalUnit.getResources();
    }

    /**
     * Returns the group index of a resource. Can be used to identify which resources belong to an expansion.
     * @return
     */
    public int getResourceGroup() {
    	return originalUnit.getResourceGroup();
    }

    /**
     * Returns the frame of the last successful command sent using BWAPI. For example, issuing an Attack command with BWAPI will record the frame it was issued in, and you retrieve the frame with this.
     * @return
     */
    public int getLastCommandFrame() {
        return originalUnit.getLastCommandFrame();
    }

    /**
     * Returns the last successful command sent using BWAPI. For example, issuing a Move command with BWAPI will immediately result in this function returning a Move command (with the same target position).
     * @return
     */
    public UnitCommandType getLastCommand() {
        return game.bot.BWAPI.getUnitCommandType(originalUnit.getLastCommandID());
    }

    /**
     * Returns the initial type of the unit or <code>UnitTypes.Unknown</code> if it wasn't a neutral unit at the beginning of the game.
     * @return
     */
    public UnitType getInitialUnitType() {
        return game.bot.BWAPI.getUnitType(originalUnit.getInitialTypeID());
    }

    /**
     * Returns the initial position of the unit on the map, or <code>Vector2D(0, 0)</code> if the unit wasn't a neutral unit at the beginning of the game.
     * @return
     */
    public Vector2D getInitialPosition() {
        return new Vector2D(originalUnit.getInitialX(), originalUnit.getInitialY());
    }
    
    /**
     * Returns the initial build tile position of the unit on the map, or <code>Vector2D(0, 0)</code> if the unit wasn't a neutral unit at the beginning of the game. The tile position is of the top left corner of the building.
     * @return
     */
    public Vector2D getInitialTilePosition() {
    	return new Vector2D(originalUnit.getInitialTileX(), originalUnit.getInitialTileY());
    }

    /**
     * Returns the unit's initial amount of hit points, or 0 if it wasn't a neutral unit at the beginning of the game.
     * @return
     */
    public int getInitialHitPoints() {
        return originalUnit.getInitialHitPoints();
    }

    /**
     * Returns the unit's initial amount of containing resources, or 0 if the unit wasn't a neutral unit at the beginning of the game.
     * @return
     */
    public int getInitialResources() {
        return originalUnit.getInitialResources();
    }

    /**
     * Returns the unit's current kill count.
     * @return
     */
    public int getKillCount() {
        return originalUnit.getKillCount();
    }

    /**
     * Returns the unit's acid spore count.
     * @return
     */
    public int getAcidSporeCount() {
        return originalUnit.getAcidSporeCount();
    }

    /**
     * Returns the number of interceptors the Protoss Carrier has.
     * @return
     */
    public int getInterceptorCount() {
        return originalUnit.getInterceptorCount();
    }

    /**
     * Returns the number of scarabs in the Protoss Reaver.
     * @return
     */
    public int getScarabCount() {
        return originalUnit.getScarabCount();
    }

    /**
     * Returns the number of spider mines in the Terran Vulture.
     * @return
     */
    public int getSpiderMineCount() {
        return originalUnit.getSpiderMineCount();
    }

    /**
     * Returns unit's ground weapon cooldown. It is 0 if the unit is ready to attack.
     * @return
     */
    public int getGroundWeaponCooldown() {
        return originalUnit.getGroundWeaponCooldown();
    }

    /**
     * Returns unit's air weapon cooldown. It is 0 if the unit is ready to attack.
     * @return
     */
    public int getAirWeaponCooldown() {
        return originalUnit.getAirWeaponCooldown();
    }

    /**
     * Returns unit's spell cooldown. It is 0 if the unit is ready to cast a spell.
     * @return
     */
    public int getSpellCooldown() {
        return originalUnit.getSpellCooldown();
    }

    /**
     * Returns the remaining hit points of the defense matrix. Initially a defense Matrix has 250 points.
     * @return
     */
    public int getDefenseMatrixPoints() {
        return originalUnit.getDefenseMatrixPoints();
    }

    /**
     * Returns the time until the defense matrix wears off. 0 -> No defense Matrix present.
     * @return
     */
    public int getDefenseMatrixTimer() {
        return originalUnit.getDefenseMatrixTimer();
    }

    /**
     * Returns the time until the ensnare effect wears off. 0 -> No ensnare effect present.
     * @return
     */
    public int getEnsnareTimer() {
        return originalUnit.getEnsnareTimer();
    }

    /**
     * Returns the time until the radiation wears off. 0 -> No radiation present.
     * @return
     */
    public int getIrradiateTimer() {
        return originalUnit.getIrradiateTimer();
    }

    /**
     * Returns the time until the lockdown wears off. 0 -> No lockdown present.
     * @return
     */
    public int getLockdownTimer() {
        return originalUnit.getLockdownTimer();
    }

    /**
     * Returns the time until the maelstrom wears off. 0 -> No maelstrom present.
     * @return
     */
    public int getMaelstromTimer() {
        return originalUnit.getMaelstromTimer();
    }

    /**
     * Returns the time until the last order wars off. 0 -> No order present.
     * @return
     */
    public int getOrderTimer() {
        return originalUnit.getOrderTimer();
    }

    /**
     * Returns the time until the plague wears off. 0 -> No plague present.
     * @return
     */
    public int getPlagueTimer() {
        return originalUnit.getPlagueTimer();
    }

    /**
     * Returns the amount of time until the unit is removed, or 0 if the unit does not have a remove timer. Used to determine how much time remains before hallucinated units, dark swarm, etc have until they are removed.
     * @return
     */
    public int getRemoveTimer() {
        return originalUnit.getRemoveTimer();
    }

    /**
     * Returns the time until the stasis field wears off. 0 -> No stasis field present.
     * @return
     */
    public int getStatisTimer() {
        return originalUnit.getStatisTimer();
    }

    /**
     * Returns the time until the stimpack wears off. 0 -> No stimpack boost present.
     * @return
     */
    public int getStimTimer() {
        return originalUnit.getStimTimer();
    }

    /**
     * Returns the building type a worker is about to construct. If the unit is a morphing Zerg unit or an incomplete building, this returns the <code>UnitType</code> the unit is about to become upon completion.
     * @return
     */
    public UnitType getBuildType() {
        return game.bot.BWAPI.getUnitType(originalUnit.getBuildTypeID());
    }

    /**
     * Returns the number of units queued up to be trained.
     * @return
     */
    public int getTrainingQueueSize() {
        return originalUnit.getTrainingQueueSize();
    }

    /**
     * Returns the current researching tech type, or <code>TechTypes.None</code> if none is researching.
     * @return
     */
    public TechType getResearchingTechID() {
        return game.bot.BWAPI.getTechType(originalUnit.getResearchingTechID());
    }

    /**
     * Returns the current upgrading upgrade type, or <code>UpgradeType.None</code> if none is upgrading.
     * @return
     */
    public UpgradeType getUpgradingUpgrade() {
        return game.bot.BWAPI.getUpgradeType(originalUnit.getUpgradingUpgradeID());
    }

    /**
     * Returns the remaining build time of a unit/building that is being constructed.
     * @return
     */
    public int getRemainingBuildTime() {
        return originalUnit.getRemainingBuildTimer();
    }

    /**
     * Returns the remaining time of the unit that is currently being trained. If the unit is a Hatchery, Lair, or Hive, this returns the amount of time until the next larva spawns, or 0 if the unit already has 3 larva.
     * @return
     */
    public int getRemainingTrainTime() {
        return originalUnit.getRemainingTrainTime();
    }

    /**
     * Returns the amount of time until the unit is done researching its current tech. If the unit is not researching anything, 0 is returned.
     * @return
     */
    public int getRemainingResearchTime() {
        return originalUnit.getRemainingResearchTime();
    }

    /**
     * Returns the amount of time until the unit is done upgrading its current upgrade. If the unit is not upgrading anything, 0 is returned.
     * @return
     */
    public int getRemainingUpgradeTime() {
        return originalUnit.getRemainingUpgradeTime();
    }

    /**
     * Returns the building type a worker is about to construct. If the unit is a morphing Zerg unit or an incomplete building, this returns the UnitType the unit is about to become upon completion.
     * @return
     */
    public UnitType getConstructingType() {
        return game.bot.BWAPI.getUnitType(originalUnit.getConstructingTypeID());
    }

    /**
     * After issuing an order to a target unit, this returns the target that the unit should move to and returns null after the unit has reached its target.
     * @return
     */
    public Unit getTargetUnit() {
        return game.getUnit(originalUnit.getTargetUnitID());
    }
    
    /**
     * Returns the target position the unit is moving to (provided a valid path to the target position exists).
     * @return
     */
    public Vector2D getTargetPosition() {
        return new Vector2D(originalUnit.getTargetX(), originalUnit.getTargetY());
    }

    /**
     * Returns the <code>OrderType</code> of Order that this unit is currently executing.
     * @return
     */
    public OrderType getOrder() {
        return game.bot.BWAPI.getOrderType(originalUnit.getOrderID());
    }

    /**
     * This is set when your unit has performed an order to a target unit and for targets that are automatically acquired.
     * @return
     */
    public Unit getOrderTarget() {
        return game.getUnit(originalUnit.getOrderTargetID());
    }

    /**
     * Returns the secondary <code>OrderType</code> of Order that this unit is currently executing.
     * @return
     */
    public OrderType getSecondaryOrder() {
        return game.bot.BWAPI.getOrderType(originalUnit.getSecondaryOrderID());
    }

    /**
     * Returns the position the building is rallied to. If the building does not produce units, Positions::None is returned.
     * An unset rally position will return (0,0).
     * @return
     */
    public Vector2D getRallyPosition() {
        return new Vector2D(originalUnit.getRallyX(), originalUnit.getRallyY());
    }

    /**
     * Returns the unit the building is rallied to. If the building is not rallied to any unit, <code>null</code> is returned.
     * @return
     */
    public Unit getRallyUnit() {
        return game.getUnit(originalUnit.getRallyUnitID());
    }

    /**
     * Returns the add-on of this unit, or NULL if the unit doesn't have an add-on.
     * @return
     */
    public Unit getAddOn() {
        return game.getUnit(originalUnit.getAddOnID());
    }

    /**
     * Returns the dropship, shuttle, overlord, or bunker that is this unit is loaded in to.
     * @return
     */
    public Unit getTransport() {
        return game.getUnit(originalUnit.getTransportID());
    }

    /**
     * Returns the number of loaded units in this transport.
     * @return
     */
    public int getNumLoadedUnits() {
        return originalUnit.getNumLoadedUnits();
    }

    /**
     * Returns the number of larva spawned by this unit.
     * @return
     */
    public int getNumLarva() {
        return originalUnit.getNumLarva();
	}

    /**
     * 3 cases to consider:
     * 
     * 1. If exists() returns true, the unit exists.
     * 2. If exists() returns false and the unit is owned by self(), then the unit does not exist.
     * 3. If exists() returns false and the unit is not owned by self(), then the unit may or may not exist (for example: an enemy unit outside of the player's vision).
     * 
     * @return
     */
    public boolean exists() {
        return originalUnit.isExists();
    }

    /**
     * Returns true if the unit is a Terran Nuclear Missile Silo and has a Terran Nuclear Missile trained and ready to go.
     * @return
     */
    public boolean isNukeReady() {
        return originalUnit.isNukeReady();
    }

    /**
     * Returns true if the unit is currently accelerating.
     * @return
     */
    public boolean isAccelerating() {
        return originalUnit.isAccelerating();
    }

    /**
     * Returns true if the unit is currently attacking.
     * @return
     */
    public boolean isAttacking() {
        return originalUnit.isAttacking();
    }

    /**
     * Returns true if the unit is currently starting an attack or playing an attack animation. Don't issue the unit another command until this returns false or else the next attack sequence may be interrupted.
     * @return
     */
    public boolean isAttackFrame() {
        return originalUnit.isAttackFrame();
    }

    /**
     * Returns true if the unit is being constructed. Always true for incomplete Protoss and Zerg buildings, and true for incomplete Terran buildings that have an SCV constructing them. If the SCV halts construction, isBeingConstructed will return false.
     * @return
     */
    public boolean isBeingConstructed() {
        return originalUnit.isBeingConstructed();
    }

    /**
     * Returns true if the unit is a mineral patch or refinery that is being gathered.
     * @return
     */
    public boolean isBeingGathered() {
        return originalUnit.isBeingGathered();
    }

    /**
     * Returns true if the unit is currently being healed by a Terran Medic.
     * @return
     */
    public boolean isBeingHealed() {
        return originalUnit.isBeingHealed();
    }

    /**
     * Returns true if the unit is currently blind from a Medic's Optical Flare.
     * @return
     */
    public boolean isBlind() {
        return originalUnit.isBlind();
    }

    /**
     * Returns true if the unit is currently braking/slowing down.
     * @return
     */
    public boolean isBraking() {
        return originalUnit.isBraking();
    }

    /**
     * Returns true if the unit is a Zerg unit that is current burrowed.
     * @return
     */
    public boolean isBurrowed() {
        return originalUnit.isBurrowed();
    }

    /**
     * Returns true if the unit is a worker that is carrying gas.
     * @return
     */
    public boolean isCarryingGas() {
        return originalUnit.isCarryingGas();
    }

    /**
     * Returns true if the unit is a worker that is carrying minerals.
     * @return
     */
    public boolean isCarryingMinerals() {
        return originalUnit.isCarryingMinerals();
    }

    /**
     * Returns true if the unit is cloaked.
     * @return
     */
    public boolean isCloaked() {
        return originalUnit.isCloaked();
    }

    /**
     * Returns true if the unit has been completed.
     * @return
     */
    public boolean isCompleted() {
        return originalUnit.isCompleted();
    }

    /**
     * Returns true if the unit has a defense matrix from a Terran Science Vessel.
     * @return
     */
    public boolean isDefenseMatrixed() {
        return originalUnit.isDefenseMatrixed();
    }

    /**
     * Returns true if the unit is visible and detected. If this is <code>false</code> and <code>Unit.isVisible</code>
     * returns <code>true</code>, then the unit is only partially visible, and requires detection (such as from a
     * science vessel or overlord) to become fully visible and selectable/targetable.
     * @return
     */
    public boolean isDetected() {
        return originalUnit.isDetected();
    }

    /**
     * Returns true if the unit has been ensnared by a Zerg Queen.
     * @return
     */
    public boolean isEnsnared() {
        return originalUnit.isEnsnared();
    }

    /**
     * Returns true if the unit is following another unit.
     * @return
     */
    public boolean isFollowing() {
        return originalUnit.isFollowing();
    }

    /**
     * Returns true if the unit is in one of the four states for gathering gas (MoveToGas, WaitForGas, HarvestGas, ReturnGas).
     * @return
     */
    public boolean isGatheringGas() {
        return originalUnit.isGatheringGas();
    }

    /**
     * Returns true if the unit is in one of the four states for gathering minerals (MoveToMinerals, WaitForMinerals, MiningMinerals, ReturnMinerals).
     * @return
     */
    public boolean isGatheringMinerals() {
        return originalUnit.isGatheringMinerals();
    }

    /**
     * Returns true for hallucinated units, false for normal units. Returns true for hallucinated enemy units only if Complete Map Information is enabled.
     * @return
     */
    public boolean isHallucination() {
        return originalUnit.isHallucination();
    }

    /**
     * Returns true for units that are holding position.
     * @return
     */
    public boolean isHoldingPosition() {
        return originalUnit.isHoldingPosition();
    }

    /**
     * Returns true if the unit is not doing anything.
     * @return
     */
    public boolean isIdle() {
        return originalUnit.isIdle();
    }

    /**
     * Returns true if the unit can be interrupted by another order like move or attack, otherwise there are very few orders that can be issued such as Unburrow, LiftOff, and Cancel.
     * @return
     */
    public boolean isInterruptable() {
        return originalUnit.isInterruptable();
    }

    /**
     * Return true if the unit is currently invincible (stasis field, preplaced invincible setting(can apply to critters in melee), or Use Map Settings invincible trigger).
     * @return
     */
    public boolean isInvincible() {
        return originalUnit.isInvincible();
    }

    /**
     * Returns true if the unit is being irradiated by a Terran Science Vessel.
     * @return
     */
    public boolean isIrradiated() {
        return originalUnit.isIrradiated();
    }

    /**
     * Returns true if the unit is a Terran building that is currently lifted off the ground.
     * @return
     */
    public boolean isLifted() {
        return originalUnit.isLifted();
    }

    /**
     * Return true if the unit is loaded into a Terran Bunker, Terran Dropship, Protoss Shuttle, or Zerg Overlord.
     * @return
     */
    public boolean isLoaded() {
        return originalUnit.isLoaded();
    }

    /**
     * Returns true if the unit is locked down by a Terran Ghost.
     * @return
     */
    public boolean isLockedDown() {
        return originalUnit.isLockedDown();
    }

    /**
     * Returns true if the unit is being maelstrommed.
     * @return
     */
    public boolean isMaelstrommed() {
        return originalUnit.isMaelstrommed();
    }

    /**
     * Returns true if the unit is a zerg unit that is morphing.
     * @return
     */
    public boolean isMorphing() {
        return originalUnit.isMorphing();
    }

    /**
     * Returns true if the unit is moving.
     * @return
     */
    public boolean isMoving() {
        return originalUnit.isMoving();
    }

    /**
     * Returns true if the unit has been parasited by some other player.
     * @return
     */
    public boolean isParasited() {
        return originalUnit.isParasited();
    }

    /**
     * Returns true if the unit is patrolling between two positions.
     * @return
     */
    public boolean isPatrolling() {
        return originalUnit.isPatrolling();
    }

    /**
     * Returns true if the unit has been plagued by a Zerg Defiler.
     * @return
     */
    public boolean isPlagued() {
        return originalUnit.isPlagued();
    }

    /**
     * Returns true if the unit is a Terran SCV that is repairing or moving to repair another unit.
     * @return
     */
    public boolean isRepairing() {
        return originalUnit.isRepairing();
    }

    /**
     * Returns true if the unit is a Terran Siege Tank that is currently in Siege mode.
     * @return
     */
    public boolean isSieged() {
        return originalUnit.isSieged();
    }

    /**
     * Returns true if the unit is starting to attack.
     * @return
     */
    public boolean isStartingAttack() {
        return originalUnit.isStartingAttack();
    }

    /**
     * Returns true if the unit has been stasised by a Protoss Arbiter.
     * @return
     */
    public boolean isStasised() {
        return originalUnit.isStasised();
    }

    /**
     * Returns true if the unit is currently stimmed.
     * @return
     */
    public boolean isStimmed() {
        return originalUnit.isStimmed();
    }

    /**
     * Returns true if the unit is "stuck" on another unit, but not stuck behind a wall.
     * @return
     */
    public boolean isStuck() {
        return originalUnit.isStuck();
    }

    /**
     * Returns true if the unit is training units (i.e. a Barracks training Marines).
     * @return
     */
    public boolean isTraining() {
        return originalUnit.isTraining();
    }

    /**
     * Returns true if the unit is under a Protoss Psionic Storm.
     * @return
     */
    public boolean isUnderStorm() {
        return originalUnit.isUnderStorm();
    }

    /**
     * Returns true if the unit is a Protoss building that is unpowered because no pylons are in range.
     * @return
     */
    public boolean isUnpowered() {
        return originalUnit.isUnpowered();
    }

    /**
     * Returns true if the unit is a building that is upgrading. See <code>UpgradeTypes</code> for the complete list of available upgrades in Broodwar.
     * @return
     */
    public boolean isUpgrading() {
        return originalUnit.isUpgrading();
    }

    /**
     * Returns true if the unit is visible. If the CompleteMapInformation cheat flag is enabled, existing units hidden by the fog of war will be accessible, but isVisible will still return false.
     * @return
     */
    public boolean isVisible() {
        return originalUnit.isVisible();
    }

    /**
     * Returns true if the unit was recently attacked.
     * @return
     */
    public boolean isUnderAttack() {
        return originalUnit.isUnderAttack();
    }
    
    /**
	 * Orders the unit to attack move to a specified location.
	 * @param position
     */
    public void attack(Vector2D position) {
    	game.bot.BWAPI.attack(originalUnit.getID(), (int)Math.round(position.x), (int)Math.round(position.y));
    }
    
    /**
     * Orders the unit to attack a single unit.
     * @param unit
     */
    public void attack(Unit target) {
    	game.bot.BWAPI.attack(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to build the given unit type at the given position. Note that if the player does not have enough resources when the unit attempts to place the building down, the order will fail. The tile position specifies where the top left corner of the building will be placed. After issuing the unit's order will become <code>OrderTypes.PlaceBuilding</code>.
     * @param tilePosition
     * @param buildingType
     */
    public void build(Vector2D tilePosition, UnitType buildingType) {
    	game.bot.BWAPI.build(originalUnit.getID(), (int)Math.round(tilePosition.x), (int)Math.round(tilePosition.y), buildingType.getID());
    }
    
    /**
     * Orders the unit to build the given add-on. The unit must be a Terran building that can have an add-on and the specified unit type must be an add-on unit type. The build() command can also be used to build add-ons.
     * @param addonType
     */
    public void buildAddon(UnitType addonType) {
    	game.bot.BWAPI.buildAddon(originalUnit.getID(), addonType.getID());
    }
    
    /**
     * Orders this unit to add the specified unit type to the training queue. Note that the player must have sufficient resources to train. If you wish to make units from a hatchery, use getLarva to get the larva associated with the hatchery and then call morph on the larva you want to morph. This command can also be used to make interceptors and scarabs.
     * @param unitType
     */
    public void train(UnitType unitType) {
    	game.bot.BWAPI.train(originalUnit.getID(), unitType.getID());
    }
    
    /**
     * Orders the unit to morph into the specified unit type.
     * @param type
     */
    public void morph(UnitType unitType) {
    	game.bot.BWAPI.morph(originalUnit.getID(), unitType.getID());
    }
    
    /**
     * Orders the unit to research the given tech type.
     * @param techType
     */
    public void research(TechType techType) {
    	game.bot.BWAPI.research(originalUnit.getID(), techType.getID());
    }
    
    /**
     * Orders the unit to upgrade the given upgrade type.
     * @param upgradeType
     */
    public void upgrade(UpgradeType upgradeType) {
    	game.bot.BWAPI.upgrade(originalUnit.getID(), upgradeType.getID());
    }
    
    /**
     * Orders the unit to set its rally position to the specified position.
     * @param position
     */
    public void setRallyPoint(Vector2D position) {
    	game.bot.BWAPI.setRallyPoint(originalUnit.getID(), (int)Math.round(position.x), (int)Math.round(position.y));
    }
    
    /**
     * Orders the unit to set its rally position to the specified target.
     * @param position
     */
    public void setRallyPoint(Unit target) {
    	game.bot.BWAPI.setRallyPoint(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to move to the specified position.
     * @param position
     */
    public void move(Vector2D position) {
    	game.bot.BWAPI.move(originalUnit.getID(), (int)Math.round(position.x), (int)Math.round(position.y));
    }
    
    /**
     * Orders the unit to patrol between its current position and the specified position.
     * @param position
     */
    public void patrol(Vector2D position) {
    	game.bot.BWAPI.patrol(originalUnit.getID(), (int)Math.round(position.x), (int)Math.round(position.y));
    }
    
    /**
     * Orders the unit to hold its position. Note: Reavers and Carriers can only hold position if they have at least one Scarab or Interceptor.
     */
    public void holdPosition() {
    	game.bot.BWAPI.holdPosition(originalUnit.getID());
    }
    
    /**
     * Orders the unit to stop. After issuing, the unit's order will become <code>OrderTypes.Stop</code>.
     */
    public void stop() {
    	game.bot.BWAPI.stop(originalUnit.getID());
    }

    /**
     * Orders the unit to follow the specified unit. After issuing, the unit's order will become <code>OrderTypes.Follow</code>.
     * @param target
     */
    public void follow(Unit target) {
    	game.bot.BWAPI.follow(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to gather the specified unit. Only workers can be ordered to gather, and the target must be a mineral patch, Refinery, Assimilator, or Extractor.
     * @param target
     */
    public void gather(Unit target) {
    	game.bot.BWAPI.gather(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to return its cargo to a nearby resource depot such as a Command Center. Only workers that are carrying minerals or gas can be ordered to return cargo.
     * @param target
     */
    public void returnCargo() {
    	game.bot.BWAPI.returnCargo(originalUnit.getID());
    }
    
    /**
     * Orders the unit to repair the specified unit. Only Terran SCVs can be ordered to repair, and the target must be a mechanical Terran unit or building. After issuing, the unit's order will become <code>OrderTypes.Repair</code>.
     * @param target
     */
    public void repair(Unit target) {
    	game.bot.BWAPI.repair(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to burrow. Either the unit must be a Zerg Lurker, or the unit must be a Zerg ground unit and burrow tech must be researched. After issuing, the unit's order will become <code>OrderTypes.Burrowing</code> while the unit transitions.
     * @param target
     */
    public void burrow() {
    	game.bot.BWAPI.burrow(originalUnit.getID());
    }
    
    /**
     * Orders the burrowed unit to unburrow. After issuing, the unit's order will become <code>OrderTypes.Unburrowing</code> while the unit transitions.
     * @param target
     */
    public void unburrow() {
    	game.bot.BWAPI.unburrow(originalUnit.getID());
    }
    
    /**
     * Orders the unit to cloak.
     * @param target
     */
    public void cloak() {
    	game.bot.BWAPI.cloak(originalUnit.getID());
    }
    
    /**
     * Orders the unit to decloak.
     * @param target
     */
    public void decloak() {
    	game.bot.BWAPI.decloak(originalUnit.getID());
    }
    
    /**
     * Orders the unit to siege. Note: unit must be a Terran siege tank. After issuing, the unit's order will become <code>OrderTypes.Sieging</code> while the siege tank transitions.
     * @param target
     */
    public void siege() {
    	game.bot.BWAPI.siege(originalUnit.getID());
    }
    
    /**
     * Orders the unit to unsiege. Note: unit must be a Terran siege tank. After issuing, the unit's order will become <code>OrderTypes.Unsieging</code> while the siege tank transitions.
     * @param target
     */
    public void unsiege() {
    	game.bot.BWAPI.unsiege(originalUnit.getID());
    }
    
    /**
     * Orders the unit to lift. Note: unit must be a Terran building that can be lifted.
     * @param target
     */
    public void lift() {
    	game.bot.BWAPI.lift(originalUnit.getID());
    }
    
    /**
     * Orders the unit to land. Note: unit must be a Terran building that is currently lifted.
     * @param target
     */
    public void land(Vector2D tilePosition) {
    	game.bot.BWAPI.land(originalUnit.getID(), (int)Math.round(tilePosition.x), (int)Math.round(tilePosition.y));
    }
    
    /**
     * Orders the unit to load the target unit.
     * @param target
     */
    public void load(Unit target) {
    	game.bot.BWAPI.load(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to unload the target unit.
     * @param target
     */
    public void unload(Unit target) {
    	game.bot.BWAPI.load(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to unload all loaded units at the unit's current position.
     * @param target
     */
    public void unloadAll() {
    	game.bot.BWAPI.unloadAll(originalUnit.getID());
    }
    
    /**
     * Orders the unit to unload all loaded units at the specified location. Unit should be a Terran Dropship, Protoss Shuttle, or Zerg Overlord. If the unit is a Terran Bunker, the units will be unloaded right outside the bunker, like in the first version of unloadAll.
     * @param target
     */
    public void unloadAll(Vector2D position) {
    	game.bot.BWAPI.unloadAll(originalUnit.getID(), (int)Math.round(position.x), (int)Math.round(position.y));
    }
    
    /**
     * Works like the right click in the GUI. Right click on a mineral patch to order a worker to mine, right click on an enemy to attack it.
     * @param target
     */
    public void rightClick(Vector2D targetPosition) {
    	game.bot.BWAPI.rightClick(originalUnit.getID(), (int)Math.round(targetPosition.x), (int)Math.round(targetPosition.y));
    }
    
    /**
     * Works like the right click in the GUI. Right click on a mineral patch to order a worker to mine, right click on an enemy to attack it.
     * @param target
     */
    public void rightClick(Unit target) {
    	game.bot.BWAPI.rightClick(originalUnit.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the SCV to stop constructing the building, and the building is left in a partially complete state until it is canceled, destroyed, or completed.
     * @param target
     */
    public void haltConstruction() {
    	game.bot.BWAPI.haltConstruction(originalUnit.getID());
    }
    
    /**
     * Orders the building to stop being constructed.
     * @param target
     */
    public void cancelConstruction() {
    	game.bot.BWAPI.cancelConstruction(originalUnit.getID());
    }
    
    /**
     * Orders the unit to stop making the addon.
     * @param target
     */
    public void cancelAddon() {
    	game.bot.BWAPI.cancelAddon(originalUnit.getID());
    }
    
    /**
     * Orders the unit to remove the last unit from the training queue. Same as <code>cancelTrain(-2)</code>
     * @param target
     */
    public void cancelTrain() {
    	cancelTrain(-2);
    }
    
    /**
     * Orders the unit to remove the specified unit from its training queue.
     * @param target
     */
    public void cancelTrain(int slot) {
    	game.bot.BWAPI.cancelTrain(originalUnit.getID(), slot);
    }
    
    /**
     * Orders the unit to stop morphing.
     * @param target
     */
    public void cancelMorph() {
    	game.bot.BWAPI.cancelMorph(originalUnit.getID());
    }
    
    /**
     * Orders the unit to cancel a research in progress.
     * @param target
     */
    public void cancelResearch() {
    	game.bot.BWAPI.cancelResearch(originalUnit.getID());
    }
    
    /**
     * Orders the unit to cancel an upgrade in progress.
     * @param target
     */
    public void cancelUpgrade() {
    	game.bot.BWAPI.cancelUpgrade(originalUnit.getID());
    }
    
    /**
     * Orders the unit to use a technology not requiring a target (i.e. Stim Pack).
     * @param target
     */
    public void useTech(TechType techType) {
    	game.bot.BWAPI.useTech(originalUnit.getID(), techType.getID());
    }
    
    /**
     * Orders the unit to use a technology requiring a target unit (i.e. Irradiate).
     * @param target
     */
    public void useTech(TechType techType, Unit target) {
    	game.bot.BWAPI.useTech(originalUnit.getID(), techType.getID(), target.originalUnit.getID());
    }
    
    /**
     * Orders the unit to use a technology requiring a target position (i.e. Spider Mines).
     * @param target
     */
    public void useTech(TechType techType, Vector2D position) {
    	game.bot.BWAPI.useTech(originalUnit.getID(), techType.getID(), (int)Math.round(position.x), (int)Math.round(position.y));
    }
}
