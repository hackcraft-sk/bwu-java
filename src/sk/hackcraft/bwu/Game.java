package sk.hackcraft.bwu;

import java.util.HashMap;

import javabot.model.Map;
import javabot.model.Player;

import sk.hackcraft.bwu.selection.UnitSet;
import sk.hackcraft.bwu.types.BulletTypes;
import sk.hackcraft.bwu.types.DamageTypes;
import sk.hackcraft.bwu.types.ExplosionTypes;
import sk.hackcraft.bwu.types.OrderTypes;
import sk.hackcraft.bwu.types.TechTypes;
import sk.hackcraft.bwu.types.UnitTypes;
import sk.hackcraft.bwu.types.WeaponTypes;


/**
 * Class representing a single instance of a game match. It is created when the match
 * is started and should be thrown away as the game ends (<code>Bot.onGameEnded()</code>)
 * 
 * @author nixone
 *
 */
public class Game {
	final protected Bot bot;
	
	private HashMap<Integer, Unit> units = new HashMap<Integer, Unit>();
	
	final private BulletTypes bulletTypes;
	final private DamageTypes damageTypes;
	final private ExplosionTypes explosionTypes;
	final private OrderTypes orderTypes;
	final private TechTypes techTypes;
	final private UnitTypes unitTypes;
	final private WeaponTypes weaponTypes;
	
	final private UnitSet enemyUnits = new UnitSet();
	final private UnitSet myUnits = new UnitSet();
	final private UnitSet neutralUnits = new UnitSet();
	final private UnitSet allyUnits = new UnitSet();
	
	protected Game(Bot bot, boolean bwta) {
		this.bot = bot;
		
		bot.BWAPI.loadTypeData();
		
		bulletTypes = new BulletTypes(bot.BWAPI);
		damageTypes = new DamageTypes(bot.BWAPI);
		explosionTypes = new ExplosionTypes(bot.BWAPI);
		orderTypes = new OrderTypes(bot.BWAPI);
		techTypes = new TechTypes(bot.BWAPI);
		unitTypes = new UnitTypes(bot.BWAPI);
		weaponTypes = new WeaponTypes(bot.BWAPI);
		
		bot.BWAPI.loadMapData(bwta);
	}
	
	/**
	 * Returns the bot this game corresponds to
	 * @return
	 */
	public Bot getBot() {
		return bot;
	}
	
	protected Unit getUnit(int unitID) {
		if(!units.containsKey(unitID)) {
			Unit unit = new Unit(this, bot.BWAPI.getUnit(unitID));
			
			units.put(unitID, unit);
			
			if(unit.getPlayer().isSelf()) {
				myUnits.add(unit);
			}
			if(unit.getPlayer().isNeutral()) {
				neutralUnits.add(unit);
			}
			if(unit.getPlayer().isEnemy()) {
				enemyUnits.add(unit);
			}
			if(unit.getPlayer().isAlly()) {
				allyUnits.add(unit);
			}
				
			return unit;
		}
		return units.get(unitID);
	}
	
	protected void removeUnit(int unitID) {
		if(units.containsKey(unitID)) {
			Unit unit = units.get(unitID);
			
			myUnits.remove(unit);
			neutralUnits.remove(unit);
			enemyUnits.remove(unit);
			allyUnits.remove(unit);
			
			units.remove(unitID);
		}
	}
	
	/**
	 * Returns all available bullet types in this game
	 * @return
	 */
	public BulletTypes getBulletTypes() {
		return bulletTypes;
	}
	
	/**
	 * Returns all available damage types in this game
	 * @return
	 */
	public DamageTypes getDamageTypes() {
		return damageTypes;
	}
	
	/**
	 * Returns all available explosion types in this game
	 * @return
	 */
	public ExplosionTypes getExplosionTypes() {
		return explosionTypes;
	}
	
	/**
	 * Returns all available order types in this game
	 * @return
	 */
	public OrderTypes getOrderTypes() {
		return orderTypes;
	}
	
	/**
	 * Returns all available tech types in this game
	 * @return
	 */
	public TechTypes getTechTypes() {
		return techTypes;
	}
	
	/**
	 * Returns all available unit types in this game
	 * @return
	 */
	public UnitTypes getUnitTypes() {
		return unitTypes;
	}
	
	/**
	 * Returns all available weapon types in this game
	 * @return
	 */
	public WeaponTypes getWeaponTypes() {
		return weaponTypes;
	}
	
	/**
	 * Returns the self player in this game
	 * @return
	 */
	public Player getSelf() {
		return bot.BWAPI.getSelf();
	}
	
	/**
	 * Returns <code>UnitSet</code> containing all current accessible ally units.
	 * @return
	 */
	public UnitSet getAllyUnits() {
		return allyUnits;
	}
	
	/**
	 * Returns <code>UnitSet</code> containing all current accessible my units.
	 * @return
	 */
	public UnitSet getMyUnits() {
		return myUnits;
	}
	
	/**
	 * Returns <code>UnitSet</code> containing all current accessible enemy units.
	 * @return
	 */
	public UnitSet getEnemyUnits() {
		return enemyUnits;
	}
	
	/**
	 * Returns <code>UnitSet</code> containing all current accessible neutral units.
	 * @return
	 */
	public UnitSet getNeutralUnits() {
		return neutralUnits;
	}
	
	/**
	 * Get map info for this game
	 * @return
	 */
	public Map getMap() {
		return bot.BWAPI.getMap();
	}
}
