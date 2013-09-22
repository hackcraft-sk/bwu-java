package sk.hackcraft.bwu.selection;

import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.selection.UnitSelector.BooleanSelector;

public class LogicalSelector {
	static public class Or implements BooleanSelector {
		BooleanSelector [] selectors;
		
		public Or(BooleanSelector... selectors) {
			this.selectors = selectors;
		}
		
		public boolean isTrueFor(Unit unit) {
			for(BooleanSelector s : selectors) {
				if(s.isTrueFor(unit)) {
					return true;
				}
			}
			return false;
		}
	}
}
