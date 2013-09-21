package sk.nixone.microqueen;

import javabot.model.Unit;
import javabot.types.UnitType.UnitTypes;

public class ReeverAgent extends UnitAgent {
	static public class TrainScarabOrder extends Order {
		@Override
		public void run(MicroQueen queen, Unit unit) {
			queen.BWAPI.train(unit.getID(), UnitTypes.Protoss_Scarab.ordinal());
		}
		
		@Override
		public boolean isCompleted() {
			return true;
		}
		
		@Override
		public int getTimeout() {
			return 1;
		}
	}
	
	private boolean trained = false;
	
	public ReeverAgent(MicroQueen queen, Unit unit) {
		super(queen, unit);
		
		for(int i=0; i<4; i++)
			order(new TrainScarabOrder());
	}
	
	@Override
	public void onNextFrame(int frameCount) {
		super.onNextFrame(frameCount);
		
		if(frameCount > 30 && frameCount < 60 && frameCount % 3 == 0) {
			queen.print("Training scarab!");

			queen.BWAPI.train(unit.getID(), UnitTypes.Protoss_Scarab.ordinal());
		}
		
		if(frameCount == 60)
			trained = true;
	}
	
	public boolean hasScarabs() {
		return trained;
	}

	@Override
	public boolean hasControl() {
		return !trained;
	}
	
	@Override
	public void updateControl() {
	}
}
