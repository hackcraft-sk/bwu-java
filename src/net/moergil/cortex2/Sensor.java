package net.moergil.cortex2;

public class Sensor implements NeuronOutput
{
	private float output;

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
