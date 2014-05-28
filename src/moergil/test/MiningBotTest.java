package moergil.test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jnibwapi.BaseLocation;
import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.types.UnitType;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.mining.MiningAgent;
import sk.hackcraft.bwu.mining.MiningAgent.Miner;
import sk.hackcraft.bwu.mining.MiningAgent.Resource;
import sk.hackcraft.bwu.mining.UnitMiner;
import sk.hackcraft.bwu.mining.UnitMineralResource;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;
import sk.nixone.scmai4.MicroQueen4;

public class MiningBotTest extends Bot
{
	static public void main(String[] arguments)
	{
		Bot bot = new MiningBotTest();
		bot.start();
	}
	
	private Game game;
	
	private MiningAgent miningAgent;
	private Set<Miner> miners;
	
	private Map<Resource, Unit> resourcesMapping;

	public MiningBotTest()
	{
	}
	
	@Override
	public void onConnected()
	{
	}

	@Override
	public void onGameStarted(Game game)
	{		
		this.game = game;
		
		game.enableUserInput();
		game.setSpeed(15);
		
		miners = new HashSet<>();
		resourcesMapping = new HashMap<>();
	}

	@Override
	public void onGameEnded(boolean isWinner)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGameUpdate()
	{
		if (game.getFrameCount() == 1)
		{
			UnitSet myUnits = new UnitSet(game.getMyUnits());
			Unit resourceDepot = myUnits.where(UnitSelector.IS_RESOURCE_DEPOT).first();
	
			UnitSet neutralUnits = game.getNeutralUnits();
			UnitSet nearbyMinerals = neutralUnits
					.where(UnitSelector.IS_MINERAL)
					.whereLessOrEqual(new DistanceSelector(resourceDepot), 500);
			
			Set<Resource> resources = new HashSet<>();
			
			for (Unit unit : nearbyMinerals)
			{
				Resource resource = new UnitMineralResource(unit);
				resources.add(resource);
				
				resourcesMapping.put(resource, unit);
			}
			
			miningAgent = new MiningAgent(resources);
			
			UnitSet workers = myUnits.where(UnitSelector.IS_WORKER);
			for (Unit unit : workers)
			{
				UnitMiner miner = new UnitMiner(unit, resourcesMapping);
				miners.add(miner);

				miningAgent.addMiner(miner);
			}
		}
		
		System.out.println(game.getFrameCount());
		
		if (game.getFrameCount() > 1)
		{
			miningAgent.update();
			for (Miner miner : miners)
			{
				if (miner.canWork())
				{
					miner.update();
				}
			}
		}
	}

	@Override
	public void onKeyPressed(int keyCode)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerLeft(Player player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerDropped(Player player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNukeDetected(Vector2D target)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitDiscovered(Unit unit)
	{
		if (miningAgent != null)
		{
			if (UnitSelector.IS_WORKER.isTrueFor(unit))
			{
				UnitMiner miner = new UnitMiner(unit, resourcesMapping);
				miners.add(miner);
	
				miningAgent.addMiner(miner);
			}
		}
	}

	@Override
	public void onUnitDestroyed(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitEvaded(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitCreated(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitCompleted(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitMorphed(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitShown(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitHidden(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnitRenegade(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDraw(Graphics graphics)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSentMessage(String message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceivedMessage(String message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSavedGame(String gameName)
	{
		// TODO Auto-generated method stub
		
	}
}
