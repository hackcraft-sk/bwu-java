package net.moergil.cortex2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.moergil.cortex.Updateable;

public class NeuralNetwork implements Updateable
{
	private final List<Neuron> neurons;
	
	public NeuralNetwork()
	{
		this.neurons = new ArrayList<>();
	}
	
	public void addNeuron(Neuron neuron)
	{
		neurons.add(neuron);
	}
	
	public Neuron getNeuron(int index)
	{
		return neurons.get(index);
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
