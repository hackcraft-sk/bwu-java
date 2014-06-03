package net.moergil.cortex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Test
{
	public static void main(String[] args)
	{
		playground();
		//runNeuroevolution();
		//tryOrganism();
	}
	
	private static void playground()
	{
		while (true)
		{
			SigmoidNeuron neuron = new SigmoidNeuron();
			
			ManualOutput manualOutput = new ManualOutput();
			manualOutput.setOutput(-1);
			
			neuron.connectTo(new NeuronInput(manualOutput, 1));
			
			neuron.updateInputs();
			neuron.updateOutput();
			
			System.out.println(neuron.outputValue());
		}
	}
	
	private static void tryOrganism()
	{
		try (Scanner scanner = new Scanner(System.in))
		{
			String genomeId = scanner.next();
			
			File file = new File("Genome-" + genomeId + ".gnm");
			
			try
			(
					FileInputStream fileInput = new FileInputStream(file);
					DataInputStream dataInput = new DataInputStream(fileInput);
			)
			{
				Genome genome = new Genome(dataInput);

				while (true)
				{
					OperandOrganism organism = new OperandOrganism(genome);
					
					float value1 = scanner.nextFloat();
					float value2 = scanner.nextFloat();
					
					organism.setInput(value1, value2);
					
					int tries = 0;
					while (/*!organism.hasResult() && */tries < 100)
					{
						organism.update();
						tries++;
					}
					
					System.out.println(organism.getResult() + " " + tries + " iterations");
				}
			}
			catch (IOException e)
			{
				System.out.println("Can't save genome: " + e.getMessage());
			}
		}
	}
	
	private static void runNeuroevolution()
	{
		final Random random = new Random();

		GenomeCreator creator = new GenomeCreator();

		Set<Genome> genomes = new HashSet<>();

		for (int i = 0; i < 100; i++)
		{
			genomes.add(creator.generate(random, 30));
		}
		
		PriorityQueue<Score> scores = new PriorityQueue<>();

		float highestScore = 0;
		
		final float[][] data = {
				{1, 1, 1},
				{1, 0, 0},
				{0, 1, 0},
				{0, 0, 0}
		};
		
		Genome bestGenome = null;
		int generation = 0;
		while (true)
		{
			for (Genome genome : genomes)
			{
				float accuracy = 0;
				float freeEnergy = 0;

				OperandOrganism organism = new OperandOrganism(genome);

				for (int i = 0; i < data.length; i++)
				{
					final float value1 = data[i][0];
					final float value2 = data[i][1];
					final float expectedResult = data[i][2];
					
					organism.setInput(value1, value2);

					int tries = 0;
					int limit = 1000;
					while (!organism.hasResult() && tries < limit)
					{
						organism.update();
						tries++;
					}

					float result = organism.getResult();

					accuracy -= Math.abs(result - expectedResult);
					
					/*if (Math.abs(result - expectedResult) < 0.1)
					{
						accuracy++;
						freeEnergy += (limit - tries) * 0.00001;
					}*/
				}
				
				float score = accuracy + freeEnergy;
				scores.add(new Score(score, genome));
				
				if (score > highestScore)
				{
					highestScore = score;
					bestGenome = genome;
				}
			}

			generation++;
			
			float sum = 0;
			int total = scores.size();
			for (Score score : scores)
			{
				sum += score.getScore();
			}
			
			if (generation % 100 == 0)
			{
				System.out.println("Generation " + generation);
				System.out.println("Average score: " + sum / total);
			}
			
			if (generation % 1000 == 0)
			{
				System.out.println();
				System.out.println("Highest score:" + highestScore);
			}
			
			if (generation % 1000 == 0)
			{
				System.out.println();
				System.out.println("Saving...");
				
				if (bestGenome != null)
				{
					File file = new File("Genome-" + System.currentTimeMillis() + ".gnm");
					
					try
					(
							FileOutputStream fileOutput = new FileOutputStream(file);
							DataOutputStream dataOutput = new DataOutputStream(fileOutput);
					)
					{
						bestGenome.writeTo(dataOutput);
					}
					catch (IOException e)
					{
						System.out.println("Can't save genome: " + e.getMessage());
					}
				}
			}

			Set<Genome> survivors = new HashSet<>();
			for (int i = 0; i < 25; i++)
			{
				Score score = scores.remove();
				survivors.add(score.getGenome());
			}
			
			scores.clear();
			
			genomes.clear();
			genomes.addAll(survivors);
			
			for (Genome genome : survivors)
			{
				for (int i = 0; i < 3; i++)
				{
					Genome mutant = creator.mutate(random, genome, 1, 1);
					genomes.add(mutant);
				}
			}
		}
	}
	
	private static class Score implements Comparable<Score>
	{
		private float score;
		private Genome genome;
		
		public Score(float score, Genome genome)
		{
			this.score = score;
			this.genome = genome;
		}
		
		public float getScore()
		{
			return score;
		}
		
		public Genome getGenome()
		{
			return genome;
		}

		@Override
		public int compareTo(Score o)
		{
			if (score < o.score)
			{
				return 1;
			}
			else if (score > o.score)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
		
		@Override
		public String toString()
		{
			return String.format("Score %.2f", score);
		}
	}
}
