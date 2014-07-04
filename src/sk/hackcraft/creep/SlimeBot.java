package sk.hackcraft.creep;

import java.util.HashMap;
import java.util.Map;

import jnibwapi.Player;
import jnibwapi.Position;
import jnibwapi.Unit;
import sk.hackcraft.bwu.AbstractBot;
import sk.hackcraft.bwu.BWU;
import sk.hackcraft.bwu.Bot;
import sk.hackcraft.bwu.Convert;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.maplayer.GradientColorAssigner;
import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.layers.PotentialFieldMatrixLayer;
import sk.hackcraft.bwu.maplayer.layers.PotentialFieldMatrixLayer.FieldEmitter;
import sk.hackcraft.bwu.maplayer.layers.util.UnitFieldEmitter;
import sk.hackcraft.bwu.maplayer.visualization.LayersPainter;
import sk.hackcraft.bwu.maplayer.visualization.SwingLayersVisualization;
import sk.hackcraft.bwu.selection.UnitSelector;
import sk.hackcraft.bwu.selection.UnitSet;

public class SlimeBot extends AbstractBot
{
	public static void main(String[] args)
	{
		BWU bwu = new BWU()
		{
			@Override
			protected Bot createBot(Game game)
			{
				return new SlimeBot(game);
			}
		};
		
		bwu.start();
	}
	
	private LayersPainter layersPainter;
	private SwingLayersVisualization visualization;
	
	private PotentialFieldMatrixLayer<Unit> dangerLayer;
	private Map<Unit, FieldEmitter<Unit>> dangerEmitters;
	
	public SlimeBot(Game game)
	{
		super(game);
		
		dangerEmitters = new HashMap<>();
	}
	
	@Override
	public void gameStarted()
	{
		game.enableUserInput();

		Position mapSize = game.getMap().getSize();
		LayerDimension mapDimension = Convert.toLayerDimension(mapSize);
		dangerLayer = new PotentialFieldMatrixLayer<Unit>(mapDimension);
		
		createVisualization();
		
		visualization.start();
		
		UnitSet enemyUnits = game.getEnemyUnits().where(UnitSelector.CAN_ATTACK);
		for (Unit unit : enemyUnits)
		{
			createDangerEmitter(unit);
		}
	}
	
	private void createVisualization()
	{
		Position mapSize = game.getMap().getSize();
		LayerDimension mapDimension = Convert.toLayerDimension(mapSize);
		layersPainter = new LayersPainter(mapDimension);
		visualization = new SwingLayersVisualization(layersPainter);
		
		layersPainter.addLayer(dangerLayer, new GradientColorAssigner(0, 10));
	}
	
	private void createDangerEmitter(final Unit unit)
	{
		FieldEmitter<Unit> emitter = new UnitFieldEmitter(unit);
		
		dangerEmitters.put(unit, emitter);
		dangerLayer.addFieldEmitter(emitter);
	}

	@Override
	public void gameEnded(boolean isWinner)
	{
		visualization.close();
	}

	@Override
	public void gameUpdated()
	{
		dangerLayer.update();
		layersPainter.update();
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
	public void unitDiscovered(final Unit unit)
	{
		if (unit.getPlayer().isEnemy() && unit.getType().isAttackCapable())
		{
			createDangerEmitter(unit);
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
		FieldEmitter emitter = dangerEmitters.remove(unit);
		
		if (emitter != null)
		{
			dangerLayer.removeFieldEmitter(emitter);
		}
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
