package net.moergil.cortex2;

import java.util.HashSet;
import java.util.Set;

import net.moergil.cortex.Updateable;

public class NeuralNetwork implements Updateable
{
	private final Set<Neuron> neurons;
	
	public NeuralNetwork()
	{
		this.neurons = new HashSet<>();
	}
	
	public void addNeuron(Neuron neuron)
	{
		neurons.add(neuron);
	}
	
	@Override
	public void update()
	{
		for (Neuron neuron : neurons)
		{
			neuron.readInputs();
		}
		
		for (Neuron neuron : neurons)
		{
			neuron.updateOutput();
		}
	}
}
