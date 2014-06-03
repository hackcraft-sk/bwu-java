package net.moergil.cortex2;

public class ManualNeuron implements Neuron
{
	private float output;
	
	@Override
	public void addInput(Neuron input, float weight)
	{
	}

	@Override
	public void readInputs()
	{
	}

	@Override
	public void updateOutput()
	{
	}
	
	public void setOutput(float output)
	{
		this.output = output;
	}

	@Override
	public float getOutput()
	{
		return output;
	}
}
