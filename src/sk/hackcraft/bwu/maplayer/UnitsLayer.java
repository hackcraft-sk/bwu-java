package sk.hackcraft.bwu.maplayer;

import java.awt.Point;

import jnibwapi.Player;
import jnibwapi.Unit;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.selection.UnitSet;

public class UnitsLayer extends SparseLayer implements Updateable
{
	public static final int MINE = 1;
	public static final int ALLY = 2;
	public static final int ENEMY = 3;
	public static final int NEUTRAL = 4;
	
	private final Game game;
	
	public UnitsLayer(LayerDimension dimenson, Game game)
	{
		super(dimenson);
		
		this.game = game;
	}

	@Override
	public void update()
	{
		Layers.fill(this, 0);
		
		UnitSet units = game.getAllUnits();

		for (Unit unit : units)
		{
			LayerPoint coordinates = Convert.toLayerPoint(unit.getPosition());
			
			Player unitPlayer = unit.getPlayer();
			
			int value;
			if (unitPlayer.isSelf())
			{
				value = MINE;
			}
			else if (unitPlayer.isAlly())
			{
				value = ALLY;
			}
			else if (unitPlayer.isEnemy())
			{
				value = ENEMY;
			}
			else
			{
				value = NEUTRAL;
			}

			set(coordinates, value);
		}
	}
}
