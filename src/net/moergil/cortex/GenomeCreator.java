package net.moergil.cortex;

import java.util.Arrays;
import java.util.Random;

public class GenomeCreator
{	
	public Genome generate(Random random, int neuronsCount)
	{		
		int connectionsCount = 5;
		GenomeConnection[] connections = new GenomeConnection[neuronsCount * connectionsCount];
		int index = 0;
		for (int i = 0; i < neuronsCount; i++)
		{
			for (int j = 0; j < connectionsCount; j++)
			{
				int from = random.nextInt(neuronsCount);
				int to = random.nextInt(neuronsCount);
				
				float weight = generateWeight(random, 0, 10);
				
				connections[index] = new GenomeConnection(from, to, weight);
				index++;
			}
		}
		
		return new Genome(neuronsCount, connections);
	}
	
	public Genome mutate(Random random, Genome genome, int stepsMutationsCount, int connectionsMutationsCount)
	{
		GenomeConnection[] connections = Arrays.copyOf(genome.getConnections(), genome.getConnections().length);
		for (int i = 0; i < connectionsMutationsCount; i++)
		{
			int index = random.nextInt(connections.length);
			
			int from = random.nextInt(genome.getNeuronsCount());
			int to = random.nextInt(genome.getNeuronsCount());
			float weight = generateWeight(random, 0, 10);
			
			connections[index] = new GenomeConnection(from, to, weight);
		}
		
		return new Genome(genome.getNeuronsCount(), connections);
	}
	
	private float generateWeight(Random random, float center, int range)
	{
		return (random.nextFloat() - 0.5f) * random.nextInt(range) - center;
	}
}
