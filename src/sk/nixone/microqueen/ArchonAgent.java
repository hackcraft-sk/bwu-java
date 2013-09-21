package sk.nixone.microqueen;

import javabot.model.Unit;

public class ArchonAgent extends UnitAgent {
	public ArchonAgent(MicroQueen queen, Unit unit) {
		super(queen, unit);
	}
	
	@Override
	public boolean hasControl() {
		return false;
	}

	@Override
	public void updateControl() {
	}
}
