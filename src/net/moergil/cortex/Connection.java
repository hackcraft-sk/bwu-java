package net.moergil.cortex;

public class Connection
{
	private final int from, to;
	private final float weight;
	
	public Connection(int from, int to, float weight)
	{
		this.from = from;
		this.to = to;
		this.weight = weight;
	}
	
	public int getFrom()
	{
		return from;
	}
	
	public int getTo()
	{
		return to;
	}
	
	public float getWeight()
	{
		return weight;
	}
	
	@Override
	public String toString()
	{
		return String.format("(%d,%d)[%.2f]", from, to, weight);
	}
}
