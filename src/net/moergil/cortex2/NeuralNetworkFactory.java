package net.moergil.cortex2;

import net.moergil.cortex2.Genome.Synapse;

public class NeuralNetworkFactory
{
	public static NeuralNetwork create(Genome genome)
	{
		NeuralNetwork network = new NeuralNetwork();
		
		int neuronsCount = genome.getNeuronsCount();
		Neuron[] neurons = new Neuron[neuronsCount];
		
		float[] thresholds = genome.getNeuronsThresholds();
		for (int i = 0; i < neuronsCount; i++)
		{
			float threshold = thresholds[i];
			Neuron neuron = new SimpleNeuron(threshold);
			
			neurons[i] = neuron;
			network.addNeuron(neuron);
		}
		
		Synapse[] synapses = genome.getSynapses();
		for (Synapse synapse : synapses)
		{
			int from = synapse.getFrom();
			int to = synapse.getTo();
			float weight = synapse.getWeight();
			
			Neuron sourceNeuron = neurons[from];
			Neuron targetNeuron = neurons[to];
			targetNeuron.addInput(sourceNeuron, weight);
		}
		
		return network;
	}
}
