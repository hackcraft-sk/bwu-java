package net.moergil.cortex;

import java.util.Random;

public class IntOrganism extends Organism implements NeuronOutput
{
	private float output;
	
	public IntOrganism(Random random, Genome genome)
	{
		super(random, genome);

		SigmoidNeuron[] neurons = getNeurons();
		neurons[0].connectToInput(this);
	}
	
	public void setInput(float input)
	{
		this.output = input;
	}
	
	public boolean hasResult()
	{
		float output = getNeurons()[1].output();
		return output > 1;
	}
	
	public float getResult()
	{
		float output = getNeurons()[2].output();
		return output;
	}
	
	@Override
	public float output()
	{
		return output;
	}
}
