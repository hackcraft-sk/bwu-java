package net.moergil.cortex2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.moergil.cortex2.Genome;
import net.moergil.cortex2.Genome.Synapse;

public class GenomeCreator
{	
	public Genome generate(String name, Random random, int neuronsCount, int[] inputs, int[] outputs)
	{
		float[] thresholds = new float[neuronsCount];
		for (int i = 0; i < neuronsCount; i++)
		{
			thresholds[i] = generate(random, 0, 10);
		}

		List<Synapse> synapsesList = new ArrayList<>();
		for (int i = 0; i < neuronsCount; i++)
		{
			for (int j = 0; j < random.nextInt(5); j++)
			{
				int from = random.nextInt(neuronsCount);
				int to = random.nextInt(neuronsCount);
				
				float weight = generate(random, 0, 10);
				
				Synapse synapse = new Synapse(from, to, weight);
				synapsesList.add(synapse);
			}
		}
		
		Synapse[] synapses = new Synapse[synapsesList.size()];
		synapses = synapsesList.toArray(synapses);
		
		return new Genome(name, inputs, outputs, thresholds, synapses);
	}
	
	public Genome mutate(Random random, Genome genome, String newName, int thresholdsMutationsCount, int weightsMutationsCount, int synapsesChangesCount)
	{
		float[] thresholds = Arrays.copyOf(genome.getNeuronsThresholds(), genome.getNeuronsThresholds().length);
		for (int i = 0; i < thresholdsMutationsCount; i++)
		{
			int index = random.nextInt(thresholds.length);
			
			thresholds[index] = generate(random, 0, 10);
		}
		
		Synapse[] synapses = Arrays.copyOf(genome.getSynapses(), genome.getSynapses().length);
		for (int i = 0; i < weightsMutationsCount; i++)
		{
			int index = random.nextInt(synapses.length);
			
			Synapse originalSynapse = synapses[index];
			
			int from = originalSynapse.getFrom();
			int to = originalSynapse.getTo();
			float weight = mutate(random, originalSynapse.getWeight());
			
			synapses[index] = new Synapse(from, to, weight);
		}
		
		int neuronsCount = genome.getNeuronsCount();
		for (int i = 0; i < synapsesChangesCount; i++)
		{	
			int index = random.nextInt(synapses.length);
			
			int from = random.nextInt(neuronsCount);
			int to = random.nextInt(neuronsCount);
			
			float weight = generate(random, 0, 10);			
			Synapse synapse = new Synapse(from, to, weight);

			synapses[index] = synapse;
		}
		
		return new Genome(newName, genome.getInputs(), genome.getOutputs(), thresholds, synapses);
	}
	
	private float generate(Random random, float center, int range)
	{
		return (random.nextFloat() - 0.5f) * random.nextInt(range) - center;
	}
	
	private float mutate(Random random, float original)
	{
		return (random.nextFloat() - 0.5f) + original;
	}
}
