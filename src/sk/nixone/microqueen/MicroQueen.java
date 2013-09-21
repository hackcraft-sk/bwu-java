package sk.nixone.microqueen;

import java.util.HashMap;
import java.util.LinkedList;

import javabot.BWAPIEventListener;
import javabot.JNIBWAPI;
import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class MicroQueen implements BWAPIEventListener {
	static final public int TILE_SIZE = 32;
	
	final public JNIBWAPI BWAPI;
	
	private HashMap<Unit, UnitAgent> agents = new HashMap<Unit, UnitAgent>();
	
	private int frameCount = 0;
	
	private AttackPlanner attackPlanner;
	private AttackPlanner.Position attackingPosition;
	
	public static void main(String[] args) {
		new MicroQueen();
	}
	
	public MicroQueen() {
		BWAPI = new JNIBWAPI(this);		
		BWAPI.start();
	}
	
	public void print(String what) {
		System.out.println("MicroQueen says: "+what);
	}
	
	private void handleNewUnit(Unit unit) {
		if(agents.containsKey(unit))
			return;
		
		if(unit.getPlayerID() != BWAPI.getSelf().getID())
			return;
		
		UnitAgent agent = null;
		
		if(unit.getTypeID() == UnitTypes.Protoss_Zealot.ordinal()) {
			agent = new ZealotAgent(this, unit);
		} else if(unit.getTypeID() == UnitTypes.Protoss_Dragoon.ordinal()) {
			agent = new DragoonAgent(this, unit);
		} else if(unit.getTypeID() == UnitTypes.Protoss_High_Templar.ordinal()) {
			agent = new HighTemplarAgent(this, unit);
		} else if(unit.getTypeID() == UnitTypes.Protoss_Reaver.ordinal()) {
			agent = new ReeverAgent(this, unit);
		} else if(unit.getTypeID() == UnitTypes.Protoss_Archon.ordinal()) {
			agent = new ArchonAgent(this, unit);
		} else if(unit.getTypeID() == UnitTypes.Protoss_Shuttle.ordinal()) {
			agent = new ShuttleAgent(this, unit);
		} else if(unit.getTypeID() == UnitTypes.Protoss_Corsair.ordinal()) {
			agent = new CorshairAgent(this, unit);
		}
		
		if(agent != null) {
			agents.put(unit, agent);
		}
	}
	
	public UnitGroup getEnemyUnits() {
		return new UnitGroup(this, BWAPI.getEnemyUnits());
	}
	
	public UnitGroup getMyUnits() {
		return new UnitGroup(this, BWAPI.getMyUnits());
	}
	
	@Override
	public void connected() {
		print("MicroQueen: Connected");
		
		BWAPI.loadTypeData();
	}

	@Override
	public void gameStarted() {
		try {
			print("Game started");
			
			BWAPI.enableUserInput();
			BWAPI.loadMapData(true);
			BWAPI.setGameSpeed(30);
			
			for(Unit unit : BWAPI.getMyUnits()) {
				handleNewUnit(unit);
			}
			
			attackPlanner = new AttackPlanner(this);
			attackingPosition = attackPlanner.getNextAttackPosition();
			
			for(UnitAgent agent : agents.values()) {
				agent.move(attackingPosition.x, attackingPosition.y);
			}
			
		} catch(Throwable cause) {
			cause.printStackTrace();
		}
	}
	
	public UnitAgentSkeleton getAgent(int unitId) {
		return getAgent(BWAPI.getUnit(unitId));
	}
	
	public UnitAgentSkeleton getAgent(Unit unit) {
		return agents.get(unit);
	}
	
	private int color = 100;
	
	private void update() {
		LinkedList<Unit> toRemove = new LinkedList<Unit>();
		for(Unit unit : agents.keySet()) {
			if(agents.get(unit) == null || !unit.isExists()) {
				toRemove.add(unit);
			}
		}
		
		for(Unit r : toRemove) {
			agents.remove(r);
		}
		
		for(Unit unit : agents.keySet()) {
			UnitAgentSkeleton agent = agents.get(unit);
			
			agent.onNextFrame(frameCount);
		}
		
		attackPlanner.update();
		
		if(attackPlanner.hasAcquiredPosition(attackingPosition)) {
			attackingPosition = attackPlanner.getNextAttackPosition();
			
			for(UnitAgent agent : agents.values()) {
				agent.move(attackingPosition.x, attackingPosition.y);
			}
		}
	}
	
	private void draw() {
		BWAPI.drawText(25, 25, "NIXONE", true);
		BWAPI.drawText(25, 35, "Frame: "+frameCount, true);
		
		UnitGroup myUnits = getMyUnits();
		Unit closest = myUnits.getClosest(attackingPosition.x, attackingPosition.y);
		
		BWAPI.drawLine(closest.getX(), closest.getY(), attackingPosition.x, attackingPosition.y, 113, false);
		BWAPI.drawCircle(attackingPosition.x, attackingPosition.y, (int)AttackPlanner.COLLISION_DISTANCE, 113, false, false);
		
		attackPlanner.draw();
	}
	
	@Override
	public void gameUpdate() {
		try {
			update();
			draw();
			
			frameCount++;
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public void gameEnded() {
		print("Game ended");
		
		agents.clear();
	}

	@Override
	public void keyPressed(int keyCode) {
	}

	@Override
	public void matchEnded(boolean winner) {
	}

	@Override
	public void playerLeft(int id) {
	}

	@Override
	public void nukeDetect(int x, int y) {
	}

	@Override
	public void nukeDetect() {
	}

	@Override
	public void unitDiscover(int unitID) {
	}

	@Override
	public void unitEvade(int unitID) {
	}

	@Override
	public void unitShow(int unitID) {
	}

	@Override
	public void unitHide(int unitID) {
	}

	@Override
	public void unitCreate(int unitID) {
	}

	@Override
	public void unitDestroy(int unitID) {
	}

	@Override
	public void unitMorph(int unitID) {
	}
}
