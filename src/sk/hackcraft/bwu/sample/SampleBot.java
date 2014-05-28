package sk.hackcraft.bwu.sample;

import jnibwapi.Player;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Unit;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.Vector2DMath;
import sk.hackcraft.bwu.selection.UnitSet;

public class SampleBot extends Bot
{
	static public void main(String[] arguments)
	{
		SampleBot bot = new SampleBot();
		bot.start();
	}

	private Game game = null;

	public SampleBot()
	{
		super(false);
	}

	public void onConnected()
	{
	}

	public void onGameStarted(Game game)
	{
		this.game = game;
	}

	public void onGameEnded()
	{
		this.game = null;
	}

	public void onGameUpdate()
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
	public void onKeyPressed(int keyCode)
	{
	}

	@Override
	public void onGameEnded(boolean isWinner)
	{
	}

	@Override
	public void onPlayerDropped(Player player)
	{
	}

	@Override
	public void onNukeDetected(Vector2D position)
	{
	}

	@Override
	public void onUnitDiscovered(Unit unit)
	{
	}

	@Override
	public void onUnitDestroyed(Unit unit)
	{
	}

	@Override
	public void onUnitEvaded(Unit unit)
	{
	}

	@Override
	public void onUnitCreated(Unit unit)
	{
	}

	@Override
	public void onUnitMorphed(Unit unit)
	{
	}

	@Override
	public void onUnitShown(Unit unit)
	{
	}

	@Override
	public void onUnitHidden(Unit unit)
	{
	}

	@Override
	public void onDraw(Graphics graphics)
	{
	}

	@Override
	public void onPlayerLeft(Player player)
	{
	}

	@Override
	public void onUnitCompleted(Unit unit)
	{
	}

	@Override
	public void onUnitRenegade(Unit unit)
	{
	}

	@Override
	public void onSentMessage(String message)
	{
	}

	@Override
	public void onReceivedMessage(String message)
	{
	}

	@Override
	public void onSavedGame(String gameName)
	{
	}
}
