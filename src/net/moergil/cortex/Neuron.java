package net.moergil.cortex;

import java.util.HashSet;
import java.util.Set;

public class Neuron implements NeuronOutput
{
	private final int step;
	private float inputValue;
	private float outputValue;
	
	private final Set<NeuronOutput> inputs;
	
	public Neuron(int step)
	{
		this.step = step;
		inputs = new HashSet<>();
	}
	
	public void connectToInput(NeuronOutput inputNeuron)
	{
		inputs.add(inputNeuron);
	}
	
	public void update()
	{
		inputValue = 0;
		for (NeuronOutput input : inputs)
		{
			inputValue += input.output();
		}
		
		float difference = inputValue - outputValue;
		
		if (difference > step)
		{
			difference = (difference / difference) * step;
		}
		
		outputValue += difference;
	}
	
	public float output()
	{
		return outputValue;
	}
	
	@Override
	public String toString()
	{
		return String.format("Neuron (%.2f) %d", outputValue, inputs.size());
	}
}
