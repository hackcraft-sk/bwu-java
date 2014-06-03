package net.moergil.cortex;

public class Organism
{
	private final Genome genome;
	
	private final SigmoidNeuron[] neurons;
	
	public Organism(Genome genome)
	{
		this.genome = genome;
		
		int neuronsCount = genome.getNeuronsCount();
		neurons = new SigmoidNeuron[neuronsCount];

		for (int i = 0; i < neuronsCount; i++)
		{
			neurons[i] = new SigmoidNeuron();
		}
		
		GenomeConnection connections[] = genome.getConnections();
		for (int i = 0; i < connections.length; i = i + 2)
		{
			GenomeConnection connection = connections[i];
			
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
	
	public SigmoidNeuron[] getNeurons()
	{
		return neurons;
	}
	
	public void update()
	{
		for (SigmoidNeuron neuron : neurons)
		{
			neuron.updateInputs();
		}
		
		for (SigmoidNeuron neuron : neurons)
		{
			neuron.updateOutput();
		}
	}
}
