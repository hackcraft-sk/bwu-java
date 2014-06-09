package net.moergil.cortex.evosim;

import java.util.List;

import sk.hackcraft.bwu.Updateable;
import net.moergil.cortex.Genome;
import net.moergil.cortex.NeuralNetwork;

public class Organism implements Updateable
{
	private final Environment environment;
	
	private NeuralNetwork brain;
	
	private int energy;
	
	public Organism(Environment environment, Genome genome)
	{
		this.environment = environment;
		
		brain.getNeuron(0);
	}
	
	public void propel(float force, float turn)
	{
		
	}
	
	public void consume()
	{
		
	}
	
	@Override
	public void update()
	{
		brain.update();
		
		float visibilityRadius = 10;
	}
}
