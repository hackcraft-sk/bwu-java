package sk.hackcraft.bwu;

public interface UnitSelector {
	public interface BooleanInformation {
		public boolean isTrueFor(Unit unit);
	}
	
	public interface IntegerInformation {
		public int getValue(Unit unit);
	}
	
	public interface RealInformation {
		public double getValue(Unit unit);
	}
}
