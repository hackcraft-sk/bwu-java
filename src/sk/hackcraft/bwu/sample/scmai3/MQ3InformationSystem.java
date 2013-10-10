package sk.hackcraft.bwu.sample.scmai3;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.util.VectorGraph.InformationSystem;
import sk.hackcraft.bwu.util.VectorGraph.VertexValue;

abstract public class MQ3InformationSystem implements InformationSystem {
	protected Game game = null;
	
	public MQ3InformationSystem(Game game) {
		this.game = game;
	}
	
	abstract public double getValueFor(Vector2D point);

	@Override
	public double combineSelfAndSiblings(VertexValue me, VertexValue[] siblings) {
		double siblingValues = 0;
		
		for(VertexValue sibling : siblings) {
			siblingValues += sibling.value;
		}
		siblingValues /= siblings.length;
		
		return me.value * 0.85 + siblingValues * 0.15;
	}
}
