package sk.hackcraft.bwu.controller;

import jnibwapi.Position;
import jnibwapi.Unit;
import net.moergil.cortex.Actuator;
import net.moergil.cortex.NeuralNetwork;
import net.moergil.cortex.Sensor;
import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Game;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.Vector2DMath;
import sk.hackcraft.bwu.selection.DistanceSelector;
import sk.hackcraft.bwu.selection.NearestPicker;

public class ScreamerAgent implements Updateable, Drawable
{
	private final Game game;
	private final Unit unit;
	
	private final NeuralNetwork brain;
	
	private final Sensor runningAway, enemyNearby, isGoingToDie;
	private final Actuator attack, runAway;
	
	private Position attackPosition;
	
	public ScreamerAgent(final Game game, final Unit unit, NeuralNetwork brain)
	{
		this.game = game;
		this.unit = unit;
		
		this.brain = brain;
		
		runningAway = new Sensor();
		enemyNearby = new Sensor();
		isGoingToDie = new Sensor();
		
		attack = new Actuator(brain.getNeuron(10))
		{
			@Override
			protected void activate()
			{
				int sightRange = unit.getType().getSightRange();
				Unit nearestEnemy = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(unit), sightRange).pick(new NearestPicker(unit));
				
				if (nearestEnemy != null)
				{
					unit.attack(nearestEnemy.getPosition(), false);
				}
			}
			
			@Override
			protected void deactivate()
			{
				unit.stop(false);
			}
		};
		
		runAway = new Actuator(brain.getNeuron(11))
		{
			@Override
			protected void activate()
			{
				int sightRange = unit.getType().getSightRange();
				Unit nearestEnemy = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(unit), sightRange).pick(new NearestPicker(unit));
				
				int attackRange = nearestEnemy.getType().getSeekRange();
				int safeOffset = attackRange + 100;
				Position runPosition = Vector2DMath.createVector(nearestEnemy, unit).normalize().scale(attackRange + safeOffset).toPosition();
				
				unit.move(runPosition, false);
				
				runningAway.setOutput(1);
			}
			
			@Override
			protected void deactivate()
			{
				unit.stop(false);
				
				runningAway.setOutput(0);
			}
		};
		
		brain.getNeuron(0).addInput(runningAway, 1);
		brain.getNeuron(1).addInput(enemyNearby, 1);
		brain.getNeuron(2).addInput(isGoingToDie, 1);
	}
	
	public void attack(Position target)
	{
		this.attackPosition = target;
	}
	
	@Override
	public void update()
	{
		brain.update();
		
		if (unit.isIdle())
		{
			unit.attack(attackPosition, false);
		}
		
		isGoingToDie.setOutput(unit.isUnderAttack() ? 1 : 0);
		
		int sightRange = unit.getType().getSightRange();
		Unit nearestEnemy = game.getEnemyUnits().whereLessOrEqual(new DistanceSelector(unit), sightRange).pick(new NearestPicker(unit));
		enemyNearby.setOutput(nearestEnemy != null ? 1 : 0);
	}
	
	@Override
	public void draw(Graphics graphics)
	{		
	}
}
