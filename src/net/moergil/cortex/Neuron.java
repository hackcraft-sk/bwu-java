package net.moergil.cortex;

public interface Neuron extends NeuronOutput
{
	void addInput(NeuronOutput input, float weight);
	
	void readInputs();
	void updateOutput();
}
