package net.moergil.cortex;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Test
{
	public static void main(String[] args)
	{
		//runNeuroevolution();
		test();
	}
	
	private static void test()
	{
		Random random = new Random(1);
		
		try (Scanner scanner = new Scanner(System.in))
		{
			while (true)
			{
				float input = scanner.nextFloat();
				
				IntOrganism organism = new IntOrganism(random, new HardcodedGenome());
				
				organism.setInput(input);
				
				int tries = 0;
				while (!organism.hasResult() && tries < 1000)
				{
					organism.update();
					tries++;
				}
				
				System.out.println(organism.getResult() + " " + tries + " iterations");
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
			genomes.add(creator.generate(random, 10));
		}
		
		PriorityQueue<Score> scores = new PriorityQueue<>();

		Genome bestGenome = null;
		int highestSuccess = 0;
		
		int generation = 0;
		for (int t = 0; t < 100000; t++)
		{
			for (Genome genome : genomes)
			{
				float[] inputs = { 1, 2, 3, 4, 5 };
				float[] outputs = { 1, 4, 6, 8, 10 };
				
				int successCount = 0;

				IntOrganism organism = new IntOrganism(random, genome);

				for (int i = 0; i < inputs.length; i++)
				{
					organism.setInput(inputs[i]);

					int tries = 0;
					while (!organism.hasResult() && tries < 1000)
					{
						organism.update();
						tries++;
					}

					float result = organism.getResult();

					if (Math.abs(result - outputs[i]) < 0.1)
					{
						successCount++;
					}
				}
				
				scores.add(new Score(successCount, genome));
				
				if (successCount > highestSuccess)
				{
					highestSuccess = successCount;
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
				System.out.println("Highest success: " + highestSuccess);
				System.out.println("Genome: " + bestGenome);
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
					Genome mutant = creator.mutate(random, genome, 3, 3);
					genomes.add(mutant);
				}
			}
		}
	}
	
	private static class Score implements Comparable<Score>
	{
		private float score;
		private Genome genome;
		
		public Score(int score, Genome genome)
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
