package net.moergil.cortex2;

import java.util.HashMap;
import java.util.Map;

public class BinaryNeuron implements Neuron
{
	private final float threshold;
	
	private Map<Neuron, Float> inputs;

	private float tempOutput;
	private float output;
	
	public BinaryNeuron(float threshold)
	{
		this.threshold = threshold;
		this.inputs = new HashMap<>();
	}
	
	@Override
	public void addInput(Neuron input, float weight)
	{
		inputs.put(input, weight);
	}

	@Override
	public void readInputs()
	{
		float inputsSum = 0;
		
		for (Map.Entry<Neuron, Float> entry : inputs.entrySet())
		{
			Neuron neuron = entry.getKey();
			float weight = entry.getValue();
			
			inputsSum += neuron.getOutput() * weight;
		}
		
		tempOutput = (inputsSum > threshold) ? 1 : 0;
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
}
