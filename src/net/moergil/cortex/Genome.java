package net.moergil.cortex;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class Genome
{
	private final int neuronsCount;
	private final Connection[] connections;
	
	public Genome(int neuronsCount, Connection[] connections)
	{
		this.neuronsCount = neuronsCount;
		this.connections = connections;
	}
	
	public Genome(DataInputStream input) throws IOException
	{
		neuronsCount = input.readInt();
		
		connections = new Connection[input.readInt()];
		for (int i = 0; i < connections.length; i++)
		{
			int from = input.readInt();
			int to = input.readInt();
			float weight = input.readFloat();

			connections[i] = new Connection(from, to, weight);
		}
	}
	
	public void writeTo(DataOutputStream output) throws IOException
	{
		output.writeInt(neuronsCount);
		
		output.writeInt(connections.length);
		for (int i = 0; i < connections.length; i++)
		{
			Connection connection = connections[i];
			
			output.writeInt(connection.getFrom());
			output.writeInt(connection.getTo());
			output.writeFloat(connection.getWeight());
		}
	}
	
	public int getNeuronsCount()
	{
		return neuronsCount;
	}
	
	public Connection[] getConnections()
	{
		return connections;
	}
	
	@Override
	public String toString()
	{
		return "connections: " + Arrays.toString(connections);
	}
}
