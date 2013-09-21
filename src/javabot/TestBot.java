package javabot;

import java.util.Random;

import javabot.model.Unit;

public class TestBot implements BWAPIEventListener {
	private Random random = new Random();
	private int frameNumber = 0;
	
	JNIBWAPI bwapi;
	public static void main(String[] args) {
		new TestBot();
	}
	
	public TestBot() {
		bwapi = new JNIBWAPI(this);
		bwapi.start();
	} 
	
	@Override
	public void connected() {
		System.out.println("MicroQueen: Connected");
	}

	@Override
	public void gameStarted() {
		System.out.println("MicroQueen: Game started");
	}

	@Override
	public void gameUpdate() {
		frameNumber++;
		
		if(frameNumber % 30 == 0) {
			for(Unit unit : bwapi.getMyUnits()) {
				bwapi.move(unit.getID(), random.nextInt(1000), random.nextInt(1000));
			}
		}
	}

	@Override
	public void gameEnded() {
		System.out.println("MicroQueen: Game ended");
	}

	@Override
	public void keyPressed(int keyCode) {
		System.out.println("MicroQueen: Key pressed");
	}

	@Override
	public void matchEnded(boolean winner) {
		System.out.println("MicroQueen: Match ended");
	}

	@Override
	public void playerLeft(int id) {
		System.out.println("MicroQueen: Player left");
	}

	@Override
	public void nukeDetect(int x, int y) {
		System.out.println("MicroQueen: Nuke detected");
	}

	@Override
	public void nukeDetect() {
		System.out.println("MicroQueen: Nuke");
	}

	@Override
	public void unitDiscover(int unitID) {
		System.out.println("MicroQueen: Unit discovered");
	}

	@Override
	public void unitEvade(int unitID) {
		System.out.println("MicroQueen: Unit evaded");
	}

	@Override
	public void unitShow(int unitID) {
		System.out.println("MicroQueen: Unit show");
	}

	@Override
	public void unitHide(int unitID) {
		System.out.println("MicroQueen: Unit hide");
	}

	@Override
	public void unitCreate(int unitID) {
		System.out.println("MicroQueen: Unit create");
	}

	@Override
	public void unitDestroy(int unitID) {
		System.out.println("MicroQueen: Unit destroy");
	}

	@Override
	public void unitMorph(int unitID) {
		System.out.println("MicroQueen: Unit morph");
	}

}
