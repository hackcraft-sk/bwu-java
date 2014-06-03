package net.moergil.cortex;

import java.util.HashSet;
import java.util.Set;

public class Neuron implements Output, Input
{
	private float tmpValue;
	private float outputValue;
	
	private final Set<NeuronInput> inputs;
	
	public Neuron()
	{
		inputs = new HashSet<>();
	}
	
	@Override
	public void connectTo(NeuronInput input)
	{
		inputs.add(input);
	}
	
	public void updateInputs()
	{
		float inputValue = 0;
		for (NeuronInput input : inputs)
		{
			inputValue += input.value();
		}
		
		tmpValue = activationFunction(inputValue);
	}
	
	public void updateOutput()
	{
		outputValue = tmpValue;
	}
	
	private float activationFunction(float value)
	{
		// approximated sigmoid function
		if (value >= 4f)
		{
			return 1f;
		}
				 
		float tmp = 1f - 0.25f * value;
		tmp *= tmp;
		tmp *= tmp;
		tmp *= tmp;
		tmp *= tmp;
		return 1f / (1f + tmp);
		
		// precise sigmoid function
		//return (float)(1 / (1 + Math.pow(Math.E, -value)));
	}
	
	@Override
	public float outputValue()
	{
		return outputValue;
	}
	
	public Set<NeuronInput> getInputs()
	{
		return inputs;
	}
	
	@Override
	public String toString()
	{
		return String.format("Neuron (%.2f) %d", outputValue, inputs.size());
	}
}
