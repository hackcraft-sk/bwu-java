package sk.hackcraft.bwu;

import java.util.HashMap;

import javabot.BWAPIEventListener;
import javabot.JNIBWAPI;
import javabot.model.Player;

abstract public class Bot {
	abstract public void onConnected();
	abstract public void onGameStarted();
	abstract public void onGameEnded();
	abstract public void onDisconnected();
	abstract public void onGameUpdate();
	abstract public void onKeyPressed(int keyCode);
	abstract public void onMatchEnded(boolean isWinner);
	abstract public void onPlayerLeft(Player player);
	abstract public void onNukeDetected(Position position);
	abstract public void onUnitDiscovered(Unit unit);
	abstract public void onUnitDestroyed(Unit unit);
	abstract public void onUnitEvaded(Unit unit);
	abstract public void onUnitCreated(Unit unit);
	abstract public void onUnitMorphed(Unit unit);
	abstract public void onUnitShown(Unit unit);
	abstract public void onUnitHidden(Unit unit);

	private BWAPIEventListener listener = new BWAPIEventListener() {
		@Override
		public void connected() {
			try {
				onConnected();
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}

		@Override
		public void gameStarted() {
			try {
				onGameStarted();
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}

		@Override
		public void gameUpdate() {
			try {
				onGameUpdate();
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		@Override
		public void gameEnded() {
			try {
				onGameEnded();
			} catch(Throwable t) {
				t.printStackTrace();
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
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		@Override
		public void playerLeft(int id) {
			try {
				onPlayerLeft(BWAPI.getPlayer(id));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void nukeDetect(int x, int y) {
			try {
				onNukeDetected(new Position(x, y));
			} catch(Throwable t) {
				t.printStackTrace();
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
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitDiscover(int unitID) {
			try {
				onUnitDiscovered(getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitEvade(int unitID) {
			try {
				onUnitEvaded(getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitShow(int unitID) {
			try {
				onUnitShown(getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitHide(int unitID) {
			try {
				onUnitHidden(getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitCreate(int unitID) {
			try {
				onUnitCreated(getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitDestroy(int unitID) {
			try {
				onUnitDestroyed(getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
		
		public void unitMorph(int unitID) {
			try {
				onUnitMorphed(getUnit(unitID));
			} catch(Throwable t) {
				t.printStackTrace();
				if(failFast) {
					System.exit(failFastReturnCode);
				}
			}
		}
	};
	
	private HashMap<Integer, Unit> units = new HashMap<Integer, Unit>();
	
	private boolean failFast = true;
	private int failFastReturnCode = 1;
	
	final public JNIBWAPI BWAPI;
	
	public Bot() {
		BWAPI = new JNIBWAPI(listener);
	}
	
	public void setFailFast(boolean failFast) {
		this.failFast = failFast;
	}
	
	public void setFailFastReturnCode(int returnCode) {
		this.failFastReturnCode = returnCode;
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
	
	public Unit getUnit(int unitID) {
		if(!units.containsKey(unitID)) {
			units.put(unitID, new Unit(BWAPI, BWAPI.getUnit(unitID)));
		}
		return units.get(unitID);
	}
}