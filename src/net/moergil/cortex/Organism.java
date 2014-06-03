package net.moergil.cortex;

public class Organism
{
	private final Genome genome;
	
	private final Neuron[] neurons;
	
	public Organism(Genome genome)
	{
		this.genome = genome;
		
		int neuronsCount = genome.getNeuronsCount();
		neurons = new Neuron[neuronsCount];

		for (int i = 0; i < neuronsCount; i++)
		{
			neurons[i] = new Neuron();
		}
		
		Connection connections[] = genome.getConnections();
		for (int i = 0; i < connections.length; i = i + 2)
		{
			Connection connection = connections[i];
			
			int from = connection.getFrom();
			int to = connection.getTo();
			float weight = connection.getWeight();
			
			neurons[from].connectTo(new NeuronInput(neurons[to], weight));
		}
	}
	
	public Genome getGenome()
	{
		return genome;
	}
	
	public Neuron[] getNeurons()
	{
		return neurons;
	}
	
	public void update()
	{
		for (Neuron neuron : neurons)
		{
			neuron.updateInputs();
		}
		
		for (Neuron neuron : neurons)
		{
			neuron.updateOutput();
		}
	}
}
