package net.moergil.cortex;

import java.util.Random;
import java.util.Scanner;

public class NeuroTest
{
	private static Sensor sensor1 = new Sensor();
	private static Sensor sensor2 = new Sensor();

	private static Neuron outputNeuron;
	
	public static void main(String[] args)
	{
		Genome bestGenome = null;
		float bestScore = Float.NEGATIVE_INFINITY;
		
		Random random = new Random();
		GenomeCreator creator = new GenomeCreator();
		
		int[] inputNeurons = {0, 1};
		int[] outputNeurons = {2};
		
		Genome genome = creator.generate("GO", random, 10, inputNeurons, outputNeurons);
		/*float[] thresholds = {1, 1, 1};
		Genome.Synapse[] synapses = {new Genome.Synapse(0, 2, 1), new Genome.Synapse(1, 2, 1)};
		Genome initialGenome = new Genome("GO", inputNeurons, outputNeurons, thresholds, synapses);
		Genome genome = initialGenome;*/
		
		int updatesCount = 2;
		
		NeuralNetwork network = prepareNetwork(genome);
		
		int[][] data = {
				{0, 0, 0},
				{0, 1, 1},
				{1, 0, 0},
				{1, 1, 0}
		};

		int testCases = 100;
		int iteration = 0;
		while (true)
		{
			float score = 0;
			
			for (int i = 0; i < testCases; i++)
			{
				int[] line = data[random.nextInt(data.length)];
				
				int input1 = line[0];
				int input2 = line[1];
				int expectedOutput = line[2];
				
				sensor1.setOutput(input1);
				sensor2.setOutput(input2);

				for (int u = 0; u < updatesCount; u++)
				{
					network.update();
				}
				
				float result = outputNeuron.getOutput();

				float testScore = Math.abs(expectedOutput - result);
				
				score -= testScore;
			}
			
			if (score > bestScore)
			{
				bestGenome = genome;
				bestScore = score;
				
				if (Math.abs(bestScore) < 0.01f)
				{
					break;
				}
			}

			if (iteration % 10000 == 0)
			{
				System.out.println("Iteration: " + iteration + "\t\t Score: " + bestScore);
			}

			genome = creator.mutate(random, bestGenome, "G" + iteration, 2, 2, 1);
			network = prepareNetwork(genome);

			iteration++;
		}
		
		System.out.println("Finished! Iteratons: " + iteration);
		
		NeuralNetwork finalNetwork = prepareNetwork(bestGenome);
		
		try (Scanner scanner = new Scanner(System.in);)
		{
			while (true)
			{
				int input1 = scanner.nextInt();
				int input2 = scanner.nextInt();
				
				sensor1.setOutput(input1);
				sensor2.setOutput(input2);
				
				for (int i = 0; i < updatesCount; i++)
				{
					finalNetwork.update();
				}
				
				float output = outputNeuron.getOutput();
				System.out.printf("Result: %.2f%n", output);
			}
		}
	}

	private static NeuralNetwork prepareNetwork(Genome genome)
	{
		NeuralNetwork network = NeuralNetworkFactory.create(genome);
		
		int[] inputs = {0, 1};
		for (int index : inputs)
		{
			Neuron neuron = network.getNeuron(index);
			
			neuron.addInput(sensor1, 1.5f);
			neuron.addInput(sensor2, 1.5f);
		}
		
		outputNeuron = network.getNeuron(2);
		
		return network;
	}
}
