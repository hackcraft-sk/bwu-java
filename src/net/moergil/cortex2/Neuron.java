package net.moergil.cortex2;

public interface Neuron extends NeuronOutput
{
	void addInput(NeuronOutput input, float weight);
	
	void readInputs();
	void updateOutput();
}
