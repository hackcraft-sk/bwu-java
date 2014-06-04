package net.moergil.cortex2;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;

public class Genome
{
	private final String name;
	
	private final int[] inputs;
	private final int[] outputs;
	private final float[] thresholds;
	private final Synapse[] synapses;
	
	public Genome(String name, int[] inputs, int[] outputs, float[] thresholds, Synapse[] connections)
	{
		this.name = name;
		this.inputs = inputs;
		this.outputs = outputs;
		this.thresholds = thresholds;
		this.synapses = connections;
	}
	
	public Genome(DataInputStream input) throws IOException
	{
		name = input.readUTF();

		inputs = new int[input.readInt()];
		readIntArray(input, inputs);
		
		outputs = new int[input.readInt()];
		readIntArray(input, outputs);
		
		thresholds = new float[input.readInt()];
		readFloatArray(input, thresholds);
		
		synapses = new Synapse[input.readInt()];
		for (int i = 0; i < synapses.length; i++)
		{
			int from = input.readInt();
			int to = input.readInt();
			float weight = input.readFloat();

			synapses[i] = new Synapse(from, to, weight);
		}
	}
	
	private void readIntArray(DataInput input, int[] storeArray) throws IOException
	{
		for (int i = 0; i < storeArray.length; i++)
		{
			storeArray[i] = input.readInt();
		}
	}
	
	private void readFloatArray(DataInput input, float[] storeArray) throws IOException
	{
		for (int i = 0; i < storeArray.length; i++)
		{
			storeArray[i] = input.readFloat();
		}
	}
	
	private void writeIntArray(DataOutput output, int[] saveArray) throws IOException
	{
		for (int i = 0; i < saveArray.length; i++)
		{
			output.writeInt(saveArray[i]);
		}
	}
	
	private void writeFloatArray(DataOutput output, float[] saveArray) throws IOException
	{
		for (int i = 0; i < saveArray.length; i++)
		{
			output.writeFloat(saveArray[i]);
		}
	}
	
	public void writeTo(DataOutputStream output) throws IOException
	{
		output.writeUTF(name);
		
		writeIntArray(output, inputs);
		writeIntArray(output, inputs);
		writeFloatArray(output, thresholds);
		
		output.writeInt(synapses.length);
		for (int i = 0; i < synapses.length; i++)
		{
			Synapse connection = synapses[i];
			
			output.writeInt(connection.getFrom());
			output.writeInt(connection.getTo());
			output.writeFloat(connection.getWeight());
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public int[] getInputs()
	{
		return inputs;
	}
	
	public int[] getOutputs()
	{
		return outputs;
	}
	
	public int getNeuronsCount()
	{
		return thresholds.length;
	}
	
	public float[] getNeuronsThresholds()
	{
		return thresholds;
	}
	
	public Synapse[] getSynapses()
	{
		return synapses;
	}
	
	@Override
	public String toString()
	{
		return String.format("Genome %s,  %d neurons, %d synapses", name, thresholds.length, synapses.length);
	}
	
	public static class Synapse
	{
		private final int from, to;
		private final float weight;
		
		public Synapse(int from, int to, float weight)
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
}
