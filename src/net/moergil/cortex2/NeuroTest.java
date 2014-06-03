package net.moergil.cortex2;

public class NeuroTest
{
	public static void main(String[] args)
	{
		ManualNeuron input1 = new ManualNeuron();
		ManualNeuron input2 = new ManualNeuron();
		
		Neuron output = new BinaryNeuron(1.5f);
		
		// AND functon
		
		output.addInput(input1, 1);
		output.addInput(input2, 1);
		
		NeuralNetwork network = new NeuralNetwork();
		network.addNeuron(input1);
		network.addNeuron(input2);
		network.addNeuron(output);
		
		input1.setOutput(1);
		input1.setOutput(1);
		network.update();
		
		System.out.println(output.getOutput());
	}
}
