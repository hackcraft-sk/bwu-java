package net.moergil.cortex2;

public interface Neuron
{
	void addInput(Neuron input, float weight);
	
	void readInputs();
	void updateOutput();
	
	float getOutput();
}
