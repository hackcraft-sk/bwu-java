package sk.nixone.microqueen;

import javabot.model.Unit;

public class DragoonAgent extends UnitAgent {
	public DragoonAgent(MicroQueen queen, Unit unit) {
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
