package sk.hackcraft.bwu;

import jnibwapi.Player;
import jnibwapi.Unit;

public interface Bot
{
	/**
	 * BWAPI calls this at the start of a match. Typically an AI will execute 
	 * set up code in this method (initialize data structures, load build orders, etc).
	 */
	abstract public void gameStarted();

	/**
	 * BWAPI calls this at the end of the match. isWinner will be true if the AIModule 
	 * won the game. If the game is a replay, isWinner will always be false.
	 * 
	 * @param isWinner indicator, whether your bot won the match or not
	 */
	abstract public void gameEnded(boolean isWinner);

	/**
	 * BWAPI calls this on every logical frame in the game.
	 */
	abstract public void gameUpdated();

	/**
	 * Key is pressed through StarCraft User Interface.
	 * 
	 * @param keyCode code identification of the key that was pressed
	 */
	abstract public void keyPressed(int keyCode);

	/**
	 * BWAPI calls this when a player leaves the game.
	 * 
	 * @param player
	 */
	abstract public void playerLeft(Player player);

	/**
	 * Certain player was dropped from the current match.
	 * 
	 * @param player
	 */
	abstract public void playerDropped(Player player);

	/**
	 * BWAPI calls this when a nuclear launch has been detected. 
	 * If the target position is visible, or if Complete Map Information 
	 * is enabled, the target position will also be provided. If Complete 
	 * Map Information is disabled and the target position is not visible, 
	 * target will be set to <code>null</code>.
	 * 
	 * @param position position of nuke or null if not available
	 */
	abstract public void nukeDetected(Vector2D target);

	/**
	 * BWAPI calls this when a unit becomes accessible. If 
	 * Complete Map Information is enabled, this will be called 
	 * at the same time as <code>onUnitCreated()</code>, otherwise it 
	 * will be called at the same time as <code>onUnitShown()</code>.
	 * 
	 * @param unit that was discovered
	 */
	abstract public void unitDiscovered(Unit unit);

	/**
	 * BWAPI calls this when a unit dies or otherwise removed from the 
	 * game (i.e. a mined out mineral patch). When a Zerg drone 
	 * becomes an extractor, the Vespene geyser changes to the 
	 * Zerg Extractor type and the drone is destroyed. If the 
	 * unit is not accessible at the time of destruction, (i.e. 
	 * if the unit is invisible and Complete Map Information is 
	 * disabled), then this callback will NOT be called. If 
	 * the unit was visible at the time of destruction, 
	 * <code>onUnitHidden()</code> will also be called.
	 * 
	 * @param unit that was destroyed
	 */
	abstract public void unitDestroyed(Unit unit);

	/**
	 * BWAPI calls this right before a unit becomes inaccessible. 
	 * If Complete Map Information is enabled, this will be called 
	 * at the same time as <code>onUnitDestroyed()</code>, otherwise it 
	 * will be called at the same time as <code>onUnitHidden</code>.
	 * 
	 * @param unit that was evaded
	 */
	abstract public void unitEvaded(Unit unit);

	/**
	 * BWAPI calls this when an accessible unit is created. Note that this 
	 * is NOT called when a unit changes type (such as larva into 
	 * egg or egg into drone). Building a refinery/assimilator/extractor 
	 * will not produce an onUnitCreate call since the vespene 
	 * geyser changes to the unit type of the refinery/assimilator/extractor. 
	 * If the unit is not accessible at the time of creation (i.e. if the 
	 * unit is invisible and Complete Map Information is disabled), then 
	 * this callback will NOT be called. If the unit is visible at the 
	 * time of creation, <code>onUnitShown()</code> will also be called.
	 * 
	 * @param unit that was created
	 */
	abstract public void unitCreated(Unit unit);

	/**
	 * No BWAPI documentation available.
	 * 
	 * @param unit that was completed
	 */
	abstract public void unitCompleted(Unit unit);

	/**
	 * BWAPI calls this when an accessible unit changes type, such as from a 
	 * Zerg Drone to a Zerg Hatchery, or from a Terran Siege Tank Tank Mode 
	 * to Terran Siege Tank Siege Mode. This is not called when the type 
	 * changes to or from <code>UnitType.UnitTypes.Unknown</code> (which happens when a unit is 
	 * transitioning to or from inaccessibility).
	 * 
	 * @param unit that was morphed
	 */
	abstract public void unitMorphed(Unit unit);

	/**
	 * BWAPI calls this when a unit becomes visible. If Complete Map 
	 * Information is disabled, this also means that the unit has 
	 * just become accessible.
	 * 
	 * @param unit that was shown
	 */
	abstract public void unitShowed(Unit unit);

	/**
	 * BWAPI calls this right before a unit becomes invisible. If Complete 
	 * Map Information is disabled, this also means that the unit is 
	 * about to become inaccessible.
	 * 
	 * @param unit that was hidden
	 */
	abstract public void unitHid(Unit unit);

	/**
	 * BWAPI calls this when an accessible unit changes ownership.
	 * 
	 * @param unit that renegaded
	 */
	abstract public void unitRenegaded(Unit unit);

	/**
	 * BWU calls this when graphics is enabled in this bot and this is called
	 * after each <code>onGameUpdate()<code> call.
	 * 
	 * @param graphics state machine for drawing
	 */
	abstract public void draw(Graphics graphics);
	
	/**
	 * If <code>Game.enableUserInput()</code> is enabled, BWAPI will call this each time a user 
	 * enters a message into the chat. If you want the message to actually 
	 * show up in chat, you can use <code>Game.sendMessage()</code> to send the message to 
	 * other players (if the game is multiplayer), or use <code>Bot.getPrintStream()</code> if 
	 * you want the message to just show up locally.
	 * 
	 * @param message message to send
	 */
	abstract public void messageSent(String message);

	/**
	 * BWAPI calls this each time it receives a message from another player in the chat.
	 * 
	 * @param message message that was sent
	 */
	abstract public void messageReceived(String message);
	
	/**
	 * BWAPI calls this when the user saves the match. The gameName will 
	 * be the name that the player entered in the save game screen.
	 * 
	 * @param gameName
	 */
	abstract public void gameSaved(String gameName);
}
