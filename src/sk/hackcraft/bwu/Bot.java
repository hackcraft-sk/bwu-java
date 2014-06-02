package sk.hackcraft.bwu;

import java.io.PrintStream;

import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;

/**
 * Class representing a programmed bot able to play many successive games. Bot
 * reacts to certain in-game events through abstract callbacks that are called from
 * BWAPI client that is connected to StarCraft immediately when corresponding event happens.
 * These callbacks are the main way to asynchronously (you don't need to actively 
 * check for changed state) receive information about the game world.
 * 
 * @author nixone
 *
 */
abstract public class Bot
{
	/**
	 * Bot connects to StarCraft (not starting the game, only connecting to BWAPI)
	 */
	abstract public void onConnected();

	/**
	 * BWAPI calls this at the start of a match. Typically an AI will execute 
	 * set up code in this method (initialize data structures, load build orders, etc).
	 * 
	 * @param game representation of match for retrieving units etc.
	 */
	abstract public void gameStarted(Game game);

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
	
	private BWAPIEventListener listener = new BWAPIEventListener()
	{
		@Override
		public void connected()
		{
			onConnected();
		}

		@Override
		public void matchStart()
		{
			graphicsOutputStream = new GraphicsOutputStream();
			printStream = new PrintStream(graphicsOutputStream);
			game = new Game(Bot.this);
			modelFactory.setGame(game);

			gameStarted(game);

			for (jnibwapi.Unit jUnit : BWAPI.getAllUnits())
			{
				unitDiscovered(game.getUnit(jUnit.getID()));
			}
		}

		@Override
		public void matchFrame()
		{
			gameUpdated();
			if (graphicsEnabled)
			{
				graphicsOutputStream.drawTo(screenGraphics);
				draw(screenGraphics);
			}
		}

		@Override
		public void keyPressed(int keyCode)
		{
			keyPressed(keyCode);
		}

		@Override
		public void matchEnd(boolean winner)
		{
			gameEnded(winner);
			game = null;
			modelFactory.setGame(game);
		}

		@Override
		public void playerLeft(int id)
		{
			Bot.this.playerLeft(BWAPI.getPlayer(id));
		}

		@Override
		public void nukeDetect(Position p)
		{
			if(p.isValid()) {
				nukeDetected(new Vector2D(p.getPX(), p.getPY()));
			} else {
				nukeDetected(null);
			}
		}

		@Override
		public void nukeDetect()
		{
			nukeDetected(null);
		}

		@Override
		public void unitDiscover(int unitID)
		{
			if (game == null)
				return;

			unitDiscovered(game.getUnit(unitID));
		}

		@Override
		public void unitEvade(int unitID)
		{
			if (game == null)
				return;

			unitEvaded(game.getUnit(unitID));
		}

		@Override
		public void unitShow(int unitID)
		{
			if (game == null)
				return;

			unitShowed(game.getUnit(unitID));
		}

		@Override
		public void unitHide(int unitID)
		{
			if (game == null)
				return;

			Unit unit = game.getUnit(unitID);

			unitHid(unit);

			if (!unit.getType().isBuilding())
			{
				game.removeUnit(unitID);
			}
		}

		@Override
		public void unitCreate(int unitID)
		{
			if (game == null)
				return;

			unitCreated(game.getUnit(unitID));
		}

		@Override
		public void unitDestroy(int unitID)
		{
			if (game == null)
				return;

			Unit unit = game.getUnit(unitID);
			if (unit == null)
			{
				return;
			}

			unitDestroyed(unit);

			game.removeUnit(unitID);
		}

		@Override
		public void unitMorph(int unitID)
		{
			if (game == null)
				return;

			unitMorphed(game.getUnit(unitID));
		}

		@Override
		public void playerDropped(int id)
		{
			Bot.this.playerDropped(BWAPI.getPlayer(id));
		}

		@Override
		public void unitComplete(int unitID)
		{
			if (game == null)
				return;

			unitCompleted(game.getUnit(unitID));
		}

		@Override
		public void unitRenegade(int unitID)
		{
			if (game == null)
				return;

			unitRenegaded(game.getUnit(unitID));
		}

		@Override
		public void sendText(String text)
		{
			messageSent(text);
		}

		@Override
		public void receiveText(String text)
		{
			messageReceived(text);
		}

		@Override
		public void saveGame(String gameName)
		{
			gameSaved(gameName);
		}
	};

	private Game game = null;
	private boolean failFast = false;
	private int failFastReturnCode = 1;
	private Graphics screenGraphics, mapGraphics;
	private boolean graphicsEnabled = true;
	private GraphicsOutputStream graphicsOutputStream = new GraphicsOutputStream();
	private PrintStream printStream = new PrintStream(graphicsOutputStream);
	private ModelFactory modelFactory;

	final protected JNIBWAPI BWAPI;

	/**
	 * Initializes BWAPI, this doesn't actually connect to BWAPI yet.
	 * In order to start your bot, call <code>start()</code>. This constructor
	 * doesn't enable terrain analyzer by default.
	 */
	public Bot()
	{
		this(false);
	}
	
	/**
	 * Initializes BWAPI, this doesn't actually connect to BWAPI yet.
	 * In order to start your bot, call <code>start()</code>
	 * 
	 * @param enableBWTA whether terrain analyzer should be enabled
	 */
	public Bot(boolean enableBWTA)
	{
		modelFactory = new ModelFactory();
		BWAPI = new JNIBWAPI(listener, enableBWTA, modelFactory);
		screenGraphics = new Graphics(this, true);
		mapGraphics = new Graphics(this, false);
	}

	@Deprecated
	protected JNIBWAPI getBWAPI()
	{
		return BWAPI;
	}
	
	public Graphics getScreenGraphics()
	{
		return screenGraphics;
	}
	
	public Graphics getMapGraphics()
	{
		return mapGraphics;
	}

	/**
	 * Sets whether the bot should end itself and crash at first occurence of uncaught
	 * exception when handling some event. Default state of this is disabled.
	 *  
	 * @param failFast whether the bot should crash immediately
	 */
	public void setFailFast(boolean failFast)
	{
		this.failFast = failFast;
	}

	/**
	 * If the fail fast is enabled, this sets the returned error code from JVM.
	 * 
	 * @param returnCode
	 */
	public void setFailFastReturnCode(int returnCode)
	{
		this.failFastReturnCode = returnCode;
	}

	/**
	 * Stars the JNIBWAPI, connects to BWAPI and starts listenning for BWAPI events.
	 */
	public void start()
	{
		try
		{
			BWAPI.start();
		}
		catch (Throwable t)
		{
			t.printStackTrace();
			if (failFast)
			{
				System.exit(failFastReturnCode);
			}
		}
	}

	/**
	 * Disables graphics. Default state of graphics is enabled. This
	 * forces the bot to stop calling <code>onDraw()</code> callback.
	 */
	public void disableGraphics()
	{
		this.graphicsEnabled = false;
	}

	/**
	 * Returns the printing stream which output is directed onto screen through
	 * <code>Graphics</code> when enabled.
	 * 
	 * @return stream for printing any output
	 */
	public PrintStream getPrintStream()
	{
		return printStream;
	}
}