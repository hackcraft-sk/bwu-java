package net.moergil.cortex;

public class NeuronInput
{
	private final Output input;
	private final float weight;
	
	public NeuronInput(Output input, float weight)
	{
		this.input = input;
		this.weight = weight;
	}
	
	public float value()
	{
		return input.outputValue() * weight;
	}
}