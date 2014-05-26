package net.moergil.cortex;

import java.util.Random;

public class Organism
{
	private final Random random;
	private final Genome genome;
	
	private final Neuron[] neurons;
	
	public Organism(Random random, Genome genome)
	{
		this.random = random;
		this.genome = genome;
		
		int neuronsCount = genome.getNeuronsCount();
		neurons = new Neuron[neuronsCount];
		
		int[] neuronsSteps = genome.getSteps();
		for (int i = 0; i < neuronsCount; i++)
		{
			int step = neuronsSteps[i];
			neurons[i] = new Neuron(step);
		}
		
		int connections[] = genome.getConnections();
		for (int i = 0; i < connections.length; i = i + 2)
		{
			int from = connections[i];
			int to = connections[i + 1];
			neurons[from].connectToInput(neurons[to]);
		}
	}
	
	public Genome getGenome()
	{
		return genome;
	}
	
	protected Neuron[] getNeurons()
	{
		return neurons;
	}
	
	public void update()
	{
		int i = random.nextInt(neurons.length);
		neurons[i].update();
	}
}
