package net.moergil.cortex;

import java.util.Arrays;
import java.util.Random;

public class GenomeCreator
{	
	public Genome generate(Random random, int neuronsCount)
	{
		int[] neuronsSteps = new int[neuronsCount];
		for (int i = 0; i < neuronsSteps.length; i++)
		{
			neuronsSteps[i] = random.nextInt(5);
		}
		
		int connectionsCount = 5;
		int[] connections = new int[neuronsCount * connectionsCount * 2];
		int index = 0;
		for (int i = 0; i < neuronsCount; i++)
		{
			for (int j = 0; j < connectionsCount; j++)
			{
				int to = random.nextInt(neuronsCount);
				
				connections[index] = i;
				connections[index + 1] = to;
				
				index += 2;
			}
		}
		
		return new Genome(neuronsSteps, connections);
	}
	
	public Genome mutate(Random random, Genome genome, int stepsMutationsCount, int connectionsMutationsCount)
	{
		int[] steps = Arrays.copyOf(genome.getSteps(), genome.getSteps().length);

		for (int i = 0; i < stepsMutationsCount; i++)
		{
			int index = random.nextInt(steps.length);
			steps[index] = random.nextInt(5);
		}
		
		int[] connections = Arrays.copyOf(genome.getConnections(), genome.getConnections().length);
		for (int i = 0; i < connectionsMutationsCount; i++)
		{
			int neuronIndex = random.nextInt(genome.getNeuronsCount());
			int index = random.nextInt(connections.length);
			
			connections[index] = neuronIndex;
		}
		
		return new Genome(steps, connections);
	}
}
