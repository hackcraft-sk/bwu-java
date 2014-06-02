package sk.hackcraft.bwu.sample;

import jnibwapi.JNIBWAPI;
import jnibwapi.Player;
import jnibwapi.Unit;
import sk.hackcraft.bwu.AbstractBot;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.BWU;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.Vector2DMath;
import sk.hackcraft.bwu.selection.UnitSet;

public class SampleBot extends AbstractBot
{
	static public void main(String[] arguments)
	{
		new BWU()
		{
			@Override
			protected Bot createBot(Game game)
			{
				return new SampleBot(game);
			}
		};
	}
	
	public SampleBot(Game game)
	{
		super(game);
	}

	@Override
	public void gameUpdated()
	{
		UnitSet myUnits = game.getMyUnits();

		for (Unit unit : myUnits)
		{
			if (unit.isIdle() || unit.isStuck())
			{
				// generate new position
				Vector2D position = Vector2DMath.randomVector().scale(game.getMap());
				// attack move!
				unit.attack(position);
			}
		}
	}

	@Override
	public void onConnected()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameStarted()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		// TODO Auto-generated method stub
		
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
