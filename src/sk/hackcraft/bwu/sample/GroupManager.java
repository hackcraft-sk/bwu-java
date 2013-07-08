package sk.hackcraft.bwu.sample;

import java.util.HashMap;

import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class GroupManager {
	public interface Special {
		public boolean isActive(Game game);
		public void onGameUpdate(Game game);
	}
	
	private UnitSet units;
	private HashMap<Unit, Special> specials = new HashMap<>();
	private Vector2D target = null;
	
	public void setTarget(Vector2D target) {
		this.target = target;
	}
	
	public void onGameUpdate(Game game) {
		this.units = game.getMyUnits();
		for(Unit unit : units) {
			if(!specials.containsKey(unit)) {
				Special special = null;
				
				if(unit.getType() == game.getUnitTypes().Terran_Siege_Tank_Tank_Mode || unit.getType() == game.getUnitTypes().Terran_Siege_Tank_Siege_Mode) {
					special = new TankSpecial(unit);
				} else if(unit.getType() == game.getUnitTypes().Terran_Ghost) {
					special = new GhostSpecial(unit);
				} else if(unit.getType() == game.getUnitTypes().Terran_Science_Vessel) {
					special = new SVSpecial(unit);
				} else if(unit.getType() == game.getUnitTypes().Terran_Battlecruiser) {
					special = new BCSpecial(unit);
				} else if(unit.getType() == game.getUnitTypes().Terran_Dropship) {
					special = new DropshipSpecial(unit);
				}
				
				if(special != null) {
					specials.put(unit, special);
				}
			}
		}
		
		UnitSet nonSpecialUnits = new UnitSet();
		
		for(Unit unit : units) {
			Special special = specials.get(unit);
			
			if(special != null && special.isActive(game)) {
				special.onGameUpdate(game);
			} else {
				nonSpecialUnits.add(unit);
			}
		}
		
		UnitSet nearEnemies = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(nonSpecialUnits.getArithmeticCenter()), 300);
		
		Vector2D center = nonSpecialUnits.getArithmeticCenter();
		
		for(Unit unit : nonSpecialUnits) {
			if(nearEnemies.size() > 0) {
				if(unit.isIdle()) {
					unit.attack(nearEnemies.getArithmeticCenter());
				}
			} else if(target != null) {
				if(unit.getPosition().sub(center).length > 200) {
					unit.attack(center.sub(unit.getPosition()).scale(0.3).add(unit.getPosition()));
				} else {
					if(unit.isIdle()) {
						unit.attack(target);
					}
				}
			}
		}
	}
}
