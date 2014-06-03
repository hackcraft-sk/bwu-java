package net.moergil.cortex;

import java.util.Random;

public class OperandOrganism extends Organism
{
	private final ManualOutput operand1, operand2;
	
	public OperandOrganism(Genome genome)
	{
		super(genome);
		
		operand1 = new ManualOutput();
		operand2 = new ManualOutput();
		
		Neuron[] neurons = getNeurons();
		neurons[0].connectTo(new NeuronInput(operand1, 1));
		neurons[1].connectTo(new NeuronInput(operand2, 1));
	}
	
	public void setInput(float value1, float value2)
	{
		operand1.setOutput(value1);
		operand2.setOutput(value2);
	}
	
	public boolean hasResult()
	{
		float output = getNeurons()[2].outputValue();
		return output > 0.5f;
	}
	
	public float getResult()
	{
		float output = getNeurons()[3].outputValue();
		return output;
	}
}
