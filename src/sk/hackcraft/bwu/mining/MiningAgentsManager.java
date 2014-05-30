package sk.hackcraft.bwu.mining;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.Unit;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.mining.MiningAgent.Resource;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class MiningAgentsManager implements Updateable
{
	private final Game game;
	
	private final Map<Vector2D, MiningAgent> agents;
	private final Map<Unit, MiningAgent> resourcesAssignments;

	public MiningAgentsManager(Game game)
	{
		this.game = game;
		
		agents = new HashMap<>();
		resourcesAssignments = new HashMap<>();
	}
	
	public void spawnMiningOperation(Vector2D position, MiningOperationListener listener)
	{
		agents.put(position, new MiningAgent());
	}
	
	@Override
	public void update()
	{
		for (MiningAgent agent : agents.values())
		{
			agent.update();
		}
		
		UnitSet neutralUnits = game.getNeutralUnits();
		UnitSet minerals = neutralUnits.where(UnitSelector.IS_MINERAL);

		for (Map.Entry<Vector2D, MiningAgent> entry : agents.entrySet())
		{
			Vector2D position = entry.getKey();
			MiningAgent agent = entry.getValue();
			
			UnitSet nearbyMinerals = minerals.whereLessOrEqual(new DistanceSelector(position), 1000);
			
			for (Unit mineralUnit : nearbyMinerals)
			{
				if (!resourcesAssignments.containsKey(mineralUnit))
				{
					UnitMineralResource resource = new UnitMineralResource(mineralUnit);
					agent.addResource(resource);
					
					resourcesAssignments.put(mineralUnit, agent);
				}
			}
			jnibwapi.Map map = JNIBWAPI.getInstance().getMap();
			
			//map.get
		}
	}
	
	public interface MiningOperationListener
	{
		void onSpawned();
	}
}
