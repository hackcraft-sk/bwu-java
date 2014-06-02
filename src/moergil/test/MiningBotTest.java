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
	
	private Set<Unit> minersUnits;
	private Set<UnitMiner> miners;
	
	private Map<Resource, Unit> resourcesMapping;

	public MiningBotTest()
	{
	}
	
	@Override
	public void onConnected()
	{
	}

	@Override
	public void gameStarted(Game game)
	{		
		this.game = game;
		
		game.enableUserInput();
		game.setSpeed(15);
		
		minersUnits = new HashSet<>();
		miners = new HashSet<>();
		resourcesMapping = new HashMap<>();
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		game = null;

		miningAgent = null;
		
		minersUnits = null;
		miners = null;
		resourcesMapping = null;
	}

	@Override
	public void gameUpdated()
	{
		if (game.getFrameCount() == 1)
		{
			UnitSet myUnits = new UnitSet(game.getMyUnits());
			Unit resourceDepot = myUnits.where(UnitSelector.IS_RESOURCE_DEPOT).first();
	
			UnitSet neutralUnits = game.getNeutralUnits();
			UnitSet nearbyMinerals = neutralUnits
					.where(UnitSelector.IS_MINERAL)
					.whereLessOrEqual(new DistanceSelector(resourceDepot), 500);
			
			miningAgent = new MiningAgent();

			for (Unit unit : nearbyMinerals)
			{
				Resource resource = new UnitMineralResource(unit);

				miningAgent.addResource(resource);
				
				resourcesMapping.put(resource, unit);
			}
			
			UnitSet workers = myUnits.where(UnitSelector.IS_WORKER);
			for (Unit unit : workers)
			{
				if (minersUnits.contains(unit))
				{
					continue;
				}
				
				minersUnits.add(unit);
				
				UnitMiner miner = new UnitMiner(unit, resourcesMapping);
				miners.add(miner);

				miningAgent.addMiner(miner);
			}
		}
		
		System.out.println(game.getFrameCount());
		
		Graphics graphics = getMapGraphics();
		
		if (game.getFrameCount() > 1)
		{
			miningAgent.update();
			for (UnitMiner miner : miners)
			{
				if (miner.canWork())
				{
					miner.update();
				}
				
				miner.draw(graphics);
			}
		}
		

		throw new RuntimeException();
	}

	@Override
	public void keyPressed(int keyCode)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerLeft(Player player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playerDropped(Player player)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nukeDetected(Vector2D target)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitDiscovered(Unit unit)
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
	public void unitDestroyed(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitEvaded(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitCreated(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitCompleted(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitMorphed(Unit unit)
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
	public void unitShowed(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitHid(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitRenegaded(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Graphics graphics)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageSent(String message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageReceived(String message)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameSaved(String gameName)
	{
		// TODO Auto-generated method stub
		
	}
}
