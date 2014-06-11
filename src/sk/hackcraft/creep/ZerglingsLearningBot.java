package sk.hackcraft.creep;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import net.moergil.cortex.Genome;
import net.moergil.cortex.GenomeCreator;
import net.moergil.cortex.NeuralNetwork;
import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import jnibwapi.types.UnitType.UnitTypes;
import sk.hackcraft.bwu.AbstractBot;
import sk.hackcraft.bwu.BWU;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.Vector2DMath;
import sk.hackcraft.bwu.controller.Brain;
import sk.hackcraft.bwu.controller.ScreamerAgent;
import sk.hackcraft.bwu.controller.StateMachine;
import sk.hackcraft.bwu.controller.StateMachine.State;
import sk.hackcraft.bwu.controller.StateMachine.StateTransition;
import sk.hackcraft.bwu.production.LarvaProductionAgent;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.NearestPicker;
import sk.hackcraft.bwu.selection.TypeSelector;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class ZerglingsLearningBot extends AbstractBot
{	
	public static void main(String[] args)
	{
		while (true)
		{
			new BWU()
			{
				@Override
				protected Bot createBot(Game game)
				{
					return new ZerglingsLearningBot(game);
				}
			}.start();
		}
	}
	
	private final GenomeCreator genomeCreator;
	private final Random random;
	
	private float averageUnitKillEfficiency;

	private List<ScreamerAgent> screamers;
	
	private Position attackPosition;

	private LarvaProductionAgent production;
	
	private Queue<Genome> genomePool;
	
	public ZerglingsLearningBot(Game game)
	{
		super(game);

		genomeCreator = new GenomeCreator();
		random = new Random();
		
		screamers = new ArrayList<ScreamerAgent>();
		
		production = new LarvaProductionAgent();
		
		genomePool = new LinkedList<Genome>();
		
		for (int i = 0; i < 30; i++)
		{
			int[] inputs = {0, 1, 2};
			int[] outputs = {10, 11};
			
			Genome genome = genomeCreator.generate("G0-" + i, random, 25, inputs, outputs);
			
			genomePool.add(genome);
		}
	}
	
	@Override
	public void gameStarted()
	{
		game.enableUserInput();

		UnitSet zerglings = game.getMyUnits().where(new TypeSelector(UnitTypes.Zerg_Zergling));
		for (Unit zergling : zerglings)
		{
			initializeScreamer(zergling);
		}
		
		Player enemyPlayer = game.getJNIBWAPI().getEnemies().iterator().next();
		attackPosition = enemyPlayer.getStartLocation();
		
		UnitSet hatcheries = game.getMyUnits().where(UnitSelector.IS_SPAWNING_LARVAE);
		for (Unit hatchery : hatcheries)
		{
			production.addHatchery(hatchery);
		}
	}
	
	private void initializeScreamer(Unit unit)
	{
		NeuralNetwork brain = null;
		ScreamerAgent screamer = new ScreamerAgent(game, unit, brain);
		
		screamers.add(screamer);

		screamer.attack(attackPosition);
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameUpdated()
	{
		for (Updateable updateable : screamers)
		{
			updateable.update();
		}
		
		for (Unit larva : production.getAvailableLarvae())
		{
			production.produce(UnitTypes.Zerg_Zergling, larva);
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
		if (unit.getPlayer() == game.getSelf())
		{
			
		}
	}

	@Override
	public void unitEvaded(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitCreated(Unit unit)
	{
		if (game.getMyUnits().contains(unit) && unit.getType() == UnitTypes.Zerg_Zergling)
		{
			initializeScreamer(unit);
		}
	}

	@Override
	public void unitCompleted(Unit unit)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unitMorphed(Unit unit)
	{
		// TODO Auto-generated method stub
		
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
