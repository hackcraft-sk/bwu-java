package net.moergil.cortex2;

import java.util.HashMap;
import java.util.Map;

public class SimpleNeuron implements Neuron
{
	private final float threshold;
	
	private Map<NeuronOutput, Float> inputs;

	private float tempOutput;
	private float output;
	
	public SimpleNeuron(float threshold)
	{
		this.threshold = threshold;
		this.inputs = new HashMap<>();
	}
	
	@Override
	public void addInput(NeuronOutput input, float weight)
	{
		inputs.put(input, weight);
	}

	@Override
	public void readInputs()
	{
		float inputsSum = 0;
		
		for (Map.Entry<NeuronOutput, Float> entry : inputs.entrySet())
		{
			NeuronOutput neuronOutput = entry.getKey();
			float weight = entry.getValue();
			
			inputsSum += neuronOutput.getOutput() * weight;
		}
		
		tempOutput = activationFunction(inputsSum);
	}
	
	private float activationFunction(float value)
	{
		value = value - threshold;
		
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
	public void updateOutput()
	{
		output = tempOutput;
	}

	@Override
	public float getOutput()
	{
		return output;
	}
	
	@Override
	public String toString()
	{
		return String.format("BinaryNeuron O(%.2f)", output);
	}
}
