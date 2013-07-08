package sk.hackcraft.bwu.sample;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.sample.GroupManager.Special;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSelector.BooleanSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class DropshipSpecial implements Special {
	static private int index = 0;
	
	private Unit unit;
	
	public DropshipSpecial(Unit unit) {
		this.unit = unit;
	}
	
	@Override
	public boolean isActive(Game game) {
		return true;
	}

	@Override
	public void onGameUpdate(Game game) {
		UnitSet dangers = game.getEnemyUnits().where(new BooleanSelector() {
			@Override
			public boolean isTrueFor(Unit unit) {
				return unit.getType().isCanAttackAir();
			}
		}).whereLessOrEqual(new DistanceSelector(unit), 500);
		
		if(dangers.size() > 0) {
			if(game.getFrameCount() % 10 == 5) {
				Vector2D avoidance = dangers.getArithmeticCenter().sub(unit.getPosition()).invert();
				Vector2D[] orthogonal = avoidance.getOrthogonal();
				avoidance = avoidance.add(orthogonal[0].scale(0.5));
				
				unit.move(avoidance.add(unit.getPosition()));
			}
		} else {
			if(unit.isIdle()) {
				Vector2D random = Vector2D.random().scale(0.8).add(new Vector2D(0.1, 0.1)).scale(game.getMap().getWidth()*Game.TILE_SIZE, game.getMap().getHeight()*Game.TILE_SIZE);
				unit.move(random);
			}
		}
	}
}
