package net.moergil.cortex;

public class ManualOutput implements Output
{
	private float output;
	
	public void setOutput(float output)
	{
		this.output = output;
	}
	
	@Override
	public float outputValue()
	{
		return output;
	}
}
