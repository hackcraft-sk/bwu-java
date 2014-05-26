package sk.hackcraft.bwu;

import java.io.PrintStream;

import jnibwapi.BWAPIEventListener;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;

abstract public class Bot
{
	abstract public void onConnected();

	abstract public void onGameStarted(Game game);

	abstract public void onGameEnded(boolean isWinner);

	abstract public void onDisconnected();

	abstract public void onGameUpdate();

	abstract public void onKeyPressed(int keyCode);

	abstract public void onPlayerLeft(Player player);

	abstract public void onPlayerDropped(Player player);

	abstract public void onNukeDetected(Vector2D position);

	abstract public void onUnitDiscovered(Unit unit);

	abstract public void onUnitDestroyed(Unit unit);

	abstract public void onUnitEvaded(Unit unit);

	abstract public void onUnitCreated(Unit unit);

	abstract public void onUnitCompleted(Unit unit);

	abstract public void onUnitMorphed(Unit unit);

	abstract public void onUnitShown(Unit unit);

	abstract public void onUnitHidden(Unit unit);

	abstract public void onUnitRenegade(Unit unit);

	abstract public void onDraw(Graphics graphics);

	private BWAPIEventListener listener = new BWAPIEventListener()
	{
		public void connected()
		{
			try
			{
				onConnected();
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void matchStart()
		{
			try
			{
				graphicsOutputStream = new GraphicsOutputStream();
				printStream = new PrintStream(graphicsOutputStream);
				game = new Game(Bot.this);

				onGameStarted(game);

				for (jnibwapi.Unit jUnit : BWAPI.getAllUnits())
				{
					onUnitDiscovered(game.getUnit(jUnit.getID()));
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void matchFrame()
		{
			try
			{
				onGameUpdate();
				if (graphicsEnabled)
				{
					graphicsOutputStream.drawTo(graphics);
					onDraw(graphics);
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void keyPressed(int keyCode)
		{
			try
			{
				onKeyPressed(keyCode);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void matchEnd(boolean winner)
		{
			try
			{
				onGameEnded(winner);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void playerLeft(int id)
		{
			try
			{
				onPlayerLeft(BWAPI.getPlayer(id));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void nukeDetect(Position p)
		{
			try
			{
				onNukeDetected(new Vector2D(p.getPX(), p.getPY()));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void nukeDetect()
		{
			try
			{
				onNukeDetected(null);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void unitDiscover(int unitID)
		{
			try
			{
				if (game == null)
					return;

				onUnitDiscovered(game.getUnit(unitID));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void unitEvade(int unitID)
		{
			try
			{
				if (game == null)
					return;

				onUnitEvaded(game.getUnit(unitID));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void unitShow(int unitID)
		{
			try
			{
				if (game == null)
					return;

				onUnitShown(game.getUnit(unitID));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void unitHide(int unitID)
		{
			try
			{
				if (game == null)
					return;

				Unit unit = game.getUnit(unitID);

				onUnitHidden(unit);

				if (!unit.getType().isBuilding())
				{
					game.removeUnit(unitID);
				}
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void unitCreate(int unitID)
		{
			try
			{
				if (game == null)
					return;

				onUnitCreated(game.getUnit(unitID));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void unitDestroy(int unitID)
		{
			try
			{
				if (game == null)
					return;

				Unit unit = game.getUnit(unitID);
				if (unit == null)
				{
					return;
				}

				onUnitDestroyed(unit);

				game.removeUnit(unitID);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		public void unitMorph(int unitID)
		{
			try
			{
				if (game == null)
					return;

				onUnitMorphed(game.getUnit(unitID));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		@Override
		public void playerDropped(int id)
		{
			try
			{
				onPlayerDropped(BWAPI.getPlayer(id));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		@Override
		public void unitComplete(int unitID)
		{
			try
			{
				if (game == null)
					return;

				onUnitCompleted(game.getUnit(unitID));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		@Override
		public void unitRenegade(int unitID)
		{
			try
			{
				if (game == null)
					return;

				onUnitRenegade(game.getUnit(unitID));
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				t.printStackTrace(printStream);
				if (failFast)
				{
					System.exit(failFastReturnCode);
				}
			}
		}

		@Override
		public void sendText(String text)
		{
			// TODO Implement
			throw new UnsupportedOperationException();
		}

		@Override
		public void receiveText(String text)
		{
			// TODO Implement
			throw new UnsupportedOperationException();
		}

		@Override
		public void saveGame(String gameName)
		{
			// TODO Implement
			throw new UnsupportedOperationException();
		}
	};

	private Game game = null;
	private boolean failFast = false;
	private int failFastReturnCode = 1;
	private Graphics graphics;
	private boolean graphicsEnabled = true;
	private GraphicsOutputStream graphicsOutputStream = new GraphicsOutputStream();
	private PrintStream printStream = new PrintStream(graphicsOutputStream);
	private ModelFactory modelFactory;

	final protected JNIBWAPI BWAPI;

	public Bot(boolean enableBWTA)
	{
		modelFactory = new ModelFactory();
		BWAPI = new JNIBWAPI(listener, enableBWTA, modelFactory);
		graphics = new Graphics(this);
	}

	@Deprecated
	protected JNIBWAPI getBWAPI()
	{
		return BWAPI;
	}

	public void setFailFast(boolean failFast)
	{
		this.failFast = failFast;
	}

	public void setFailFastReturnCode(int returnCode)
	{
		this.failFastReturnCode = returnCode;
	}

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

	public void disableGraphics()
	{
		this.graphicsEnabled = false;
	}

	public PrintStream getPrintStream()
	{
		return printStream;
	}
}