package net.moergil.cortex;

import java.util.Arrays;

public class Genome
{
	private final int[] steps;
	private final int[] connections;
	
	public Genome(int[] steps, int[] connections)
	{
		this.steps = steps;
		this.connections = connections;
	}
	
	public int getNeuronsCount()
	{
		return steps.length;
	}
	
	public int[] getSteps()
	{
		return steps;
	}
	
	public int[] getConnections()
	{
		return connections;
	}
	
	@Override
	public String toString()
	{
		return "steps: " + Arrays.toString(steps) + ", connections: " + Arrays.toString(connections);
	}
}
