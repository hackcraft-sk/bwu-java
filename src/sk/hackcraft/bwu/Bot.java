package sk.hackcraft.bwu;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javabot.BWAPIEventListener;
import javabot.JNIBWAPI;
import javabot.model.Player;

abstract public class Bot {
	abstract public void onConnected();
	abstract public void onGameStarted(Game game);
	abstract public void onGameEnded();
	abstract public void onDisconnected();
	abstract public void onGameUpdate();
	abstract public void onKeyPressed(int keyCode);
	abstract public void onMatchEnded(boolean isWinner);
	abstract public void onPlayerLeft(Player player);
	abstract public void onNukeDetected(Vector2D position);
	abstract public void onUnitDiscovered(Unit unit);
	abstract public void onUnitDestroyed(Unit unit);
	abstract public void onUnitEvaded(Unit unit);
	abstract public void onUnitCreated(Unit unit);
	abstract public void onUnitMorphed(Unit unit);
	abstract public void onUnitShown(Unit unit);
	abstract public void onUnitHidden(Unit unit);
	abstract public void onDraw(Graphics graphics);
	
	private BWAPIEventListener listener = new BWAPIEventListener() {
		public void connected() {
			try {
				onConnected();
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}

		public void gameStarted() {
			try {
				graphicsOutputStream = new GraphicsOutputStream();
				printStream = new PrintStream(graphicsOutputStream);
				game = new Game(Bot.this, bwta);
				
				onGameStarted(game);
				
				for(javabot.model.Unit jUnit : BWAPI.getAllUnits()) {
					onUnitDiscovered(game.getUnit(jUnit.getID()));
				}
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}

		public void gameUpdate() {
			try {
				onGameUpdate();
				if(graphicsEnabled) {
					graphicsOutputStream.drawTo(graphics);
					onDraw(graphics);
				}
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void gameEnded() {
			try {
				onGameEnded();
				game = null;
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void keyPressed(int keyCode) {
			try {
				onKeyPressed(keyCode);
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void matchEnded(boolean winner) {
			try {
				onMatchEnded(winner);
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void playerLeft(int id) {
			try {
				onPlayerLeft(BWAPI.getPlayer(id));
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void nukeDetect(int x, int y) {
			try {
				onNukeDetected(new Vector2D(x, y));
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void nukeDetect() {
			try {
				onNukeDetected(null);
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitDiscover(int unitID) {
			try {
				if(game == null)
					return;
				
				onUnitDiscovered(game.getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitEvade(int unitID) {
			try {
				if(game == null)
					return;
				
				onUnitEvaded(game.getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitShow(int unitID) {
			try {
				if(game == null)
					return;
				
				onUnitShown(game.getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitHide(int unitID) {
			try {
				if(game == null)
					return;
				
				Unit unit = game.getUnit(unitID);
				
				onUnitHidden(unit);
				
				if(!unit.getType().isBuilding()) {
					game.removeUnit(unitID);
				}
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitCreate(int unitID) {
			try {
				if(game == null)
					return;
				
				onUnitCreated(game.getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitDestroy(int unitID) {
			try {
				if(game == null)
					return;
				
				Unit unit = game.getUnit(unitID);
				if(unit == null) {
					return;
				}
				
				onUnitDestroyed(unit);
				
				game.removeUnit(unitID);
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitMorph(int unitID) {
			try {
				if(game == null)
					return;
				
				onUnitMorphed(game.getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				t.printStackTrace(printStream);
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
	};
	
	private Game game = null;
	private boolean failFast = false;
	private int failFastReturnCode = 1;
	private boolean bwta = false;
	private Graphics graphics;
	private boolean graphicsEnabled = true;
	private GraphicsOutputStream graphicsOutputStream = new GraphicsOutputStream();
	private PrintStream printStream = new PrintStream(graphicsOutputStream);
	
	final protected JNIBWAPI BWAPI;
	
	public Bot() {
		BWAPI = new JNIBWAPI(listener);
		graphics = new Graphics(this);
	}
	
	protected JNIBWAPI getBWAPI() {
		return BWAPI;
	}
	
	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}
	
	public void setFailFastReturnCode(int returnCode) {
		this.failFastReturnCode = returnCode;
	}
	
	public void setBWTA(boolean enabled) {
		this.bwta = enabled;
	}
	
	public void start() {
		try {
			BWAPI.start();
		} catch(Throwable t) {
			t.printStackTrace();
			if(failFast) {
				System.exit(failFastReturnCode);
			}
		}
	}
	
	public void disableGraphics() {
		this.graphicsEnabled = false;
	}
	
	public PrintStream getPrintStream() {
		return printStream;
	}
}