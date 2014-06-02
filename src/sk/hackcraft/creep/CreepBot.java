package sk.hackcraft.creep;

import java.util.HashSet;
import java.util.Set;

import jnibwapi.Player;
import jnibwapi.Unit;
import jnibwapi.types.UnitType.UnitTypes;
import sk.hackcraft.bwu.AbstractBot;
import sk.hackcraft.bwu.BWU;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.mining.MiningAgent;
import sk.hackcraft.bwu.production.LarvaProductionAgent;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class CreepBot extends AbstractBot
{
	public static void main(String[] args)
	{
		BWU bwu = new BWU()
		{
			@Override
			protected Bot createBot(Game game)
			{
				return new CreepBot(game);
			}
		};
		
		bwu.start();
	}
	
	public CreepBot(Game game)
	{
		super(game);
		
		miningAgents = new HashSet<>();
		productionAgent = new LarvaProductionAgent();
	}
	
	private final Set<MiningAgent> miningAgents;
	private final LarvaProductionAgent productionAgent;

	@Override
	public void gameStarted()
	{
		game.enableUserInput();
		game.setSpeed(15);

		UnitSet myUnits = game.getMyUnits();
		
		UnitSet hatcheries = myUnits.where(UnitSelector.IS_SPAWNING_LARVAE);
		for (Unit hatchery : hatcheries)
		{
			productionAgent.addHatchery(hatchery);
			
			Set<Unit> nearbyResources = game.getStaticNeutralUnits().where(UnitSelector.IS_MINERAL).whereLessOrEqual(new DistanceSelector(hatchery), 500);
			
			if (!nearbyResources.isEmpty())
			{
				MiningAgent miningAgent = new MiningAgent(hatchery, nearbyResources);
				
				Set<Unit> nearbyWorkers = myUnits.where(UnitSelector.IS_WORKER).whereLessOrEqual(new DistanceSelector(hatchery), 300);
				
				for (Unit worker : nearbyWorkers)
				{
					miningAgent.addMiner(worker);
				}
				
				miningAgents.add(miningAgent);
			}
		}
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameUpdated()
	{
		for (MiningAgent agent : miningAgents)
		{
			agent.update();
		}
		
		int availableMinerals = game.getSelf().getMinerals(); 
		if (availableMinerals >= 50)
		{
			boolean result = productionAgent.produce(UnitTypes.Zerg_Drone);
			
			if (!result && availableMinerals > 100)
			{
				productionAgent.produce(UnitTypes.Zerg_Overlord);
			}
		}
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
		// TODO Auto-generated method stub
		
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
		
	}

	@Override
	public void unitCompleted(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitMorphed(Unit unit)
	{
		if (unit.getType().isWorker())
		{
			miningAgents.iterator().next().addMiner(unit);
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
		for (MiningAgent agent : miningAgents)
		{
			agent.draw(graphics);
		}
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
