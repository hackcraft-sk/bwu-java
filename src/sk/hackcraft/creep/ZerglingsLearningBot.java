package sk.hackcraft.creep;

import java.util.ArrayList;
import java.util.List;

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
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.Vector2DMath;
import sk.hackcraft.bwu.controller.Brain;
import sk.hackcraft.bwu.controller.StateMachine;
import sk.hackcraft.bwu.controller.StateMachine.State;
import sk.hackcraft.bwu.controller.StateMachine.StateTransition;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.NearestPicker;
import sk.hackcraft.bwu.selection.TypeSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class ZerglingsLearningBot extends AbstractBot
{
	public static void main(String[] args)
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

	private List<StateMachine> zerglingsStateMachines;
	private List<Brain> brains;
	
	public ZerglingsLearningBot(Game game)
	{
		super(game);

		zerglingsStateMachines = new ArrayList<StateMachine>();
		brains = new ArrayList<>();
	}
	
	@Override
	public void gameStarted()
	{
		game.enableUserInput();

		UnitSet zerglings = game.getMyUnits().where(new TypeSelector(UnitTypes.Zerg_Zergling));
		for (Unit zergling : zerglings)
		{
			initializeZergling(zergling);
		}
	}
	
	private void initializeZergling(final Unit zergling)
	{
		final State runAway = new State()
		{
			@Override
			public void update()
			{
				if (!zergling.isMoving() || zergling.isAttacking())
				{
					Unit nearestEnemy = game.getEnemyUnits().pick(new NearestPicker(zergling));
					
					if (nearestEnemy != null)
					{
						Vector2D directionToEnemy = Vector2DMath.createVector(zergling, nearestEnemy);
						
						Vector2D position = Convert.toPositionVector(zergling.getPosition());
						Vector2D runVector = directionToEnemy.invert().normalize().scale(100);
						
						Position runPosition = Convert.toPosition(position.add(runVector));
						
						zergling.move(runPosition, false);
					}
				}
			}
			
			@Override
			public String toString()
			{
				return "RUN_AWAY";
			}
		};
		
		final State attack = new State()
		{
			@Override
			public void update()
			{
				if (!zergling.isAttacking())
				{
					int sightRange = zergling.getType().getSightRange();
					UnitSet enemyUnits = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(zergling), sightRange);
					Unit target = enemyUnits.pick(new NearestPicker(zergling));
					
					if (target != null)
					{
						zergling.attack(target, false);
					}
				}
			}
			
			@Override
			public String toString()
			{
				return "ATTACK";
			}
		};

		final StateMachine machine = new StateMachine(runAway);
		machine.addState(attack);
		
		machine.addTransition(runAway, attack, StateTransition.ALWAYS);
		machine.addTransition(attack, runAway, StateTransition.ALWAYS);
		
		zerglingsStateMachines.add(machine);
		
		Brain brain = new Brain()
		{
			int runTimeout = 100;
			@Override
			public void update()
			{
				if (runTimeout > 0)
				{
					runTimeout--;
				}
				
				if (machine.getCurrentState() != runAway && zergling.isUnderAttack())
				{
					machine.changeState(runAway);
					runTimeout = 100;
				}
				
				Unit nearestEnemy = game.getEnemyUnits().pick(new NearestPicker(zergling));
				if (machine.getCurrentState() != attack && (nearestEnemy == null || Vector2DMath.createVector(nearestEnemy, zergling).getLength() < 100 && runTimeout <= 0))
				{
					machine.changeState(attack);
				}
			}
		};
		
		brains.add(brain);
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameUpdated()
	{
		for (StateMachine machine : zerglingsStateMachines)
		{
			machine.update();
		}
		
		for (Brain brain : brains)
		{
			brain.update();
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
