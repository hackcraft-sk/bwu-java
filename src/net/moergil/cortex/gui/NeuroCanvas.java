package net.moergil.cortex.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import net.moergil.cortex.SigmoidNeuron;
import net.moergil.cortex.NeuronInput;
import net.moergil.cortex.Organism;

public class NeuroCanvas
{
	private Organism organism;
	
	private Map<SigmoidNeuron, Point> neuronsPositions;
	
	private float step;
	private float radius = 100;
	
	public NeuroCanvas(Organism organism)
	{
		this.organism = organism;
		
		neuronsPositions = new HashMap<>();
		
		SigmoidNeuron[] neurons = organism.getNeurons();
		step = ((float)Math.PI * 2) / neurons.length;
		
		for (int i = 0; i < neurons.length; i++)
		{
			SigmoidNeuron neuron = neurons[i];
			Point position = getNeuronPosition(i);
			
			neuronsPositions.put(neuron, position);
		}
	}
	
	public void draw(Graphics2D graphics)
	{
		/*graphics.translate(150, 150);
		
		Neuron[] neurons = organism.getNeurons();
		
		for (int i = 0; i < neurons.length; i++)
		{
			Neuron neuron = neurons[i];
			Point destination = getNeuronPosition(i);
			
			Set<NeuronInput> inputs = neuron.getInputs();

			for (NeuronInput input : inputs)
			{
				Point source = neuronsPositions.get(input.getSourceNeuronOutput());
				
				if (source == null)
				{
					source = new Point(-100, -100);
				}
				
				drawConnection(graphics, destination, source, input);
			}
		}
		
		for (int i = 0; i < neurons.length; i++)
		{
			Neuron neuron = neurons[i];
			Point destination = getNeuronPosition(i);

			drawNeuron(graphics, destination, neuron);
		}*/
	}
	
	private void drawNeuron(Graphics2D graphics, Point position, SigmoidNeuron neuron)
	{
		/*float red = neuron.outputValue() / 100;
		if (red > 1)
		{
			red = 1;
		}
		
		graphics.setColor(new Color(red, 0, 0));
		
		int radius = 10;
		graphics.fillArc(position.getX() - radius, position.getY() - radius, 20, 20, 0, 360);
		
		graphics.setColor(Color.BLUE);
		graphics.drawString(Float.toString(neuron.outputValue()), position.getX() + 15, position.getY() + 15);*/
	}
	
	private void drawConnection(Graphics2D graphics, Point destination, Point source, NeuronInput input)
	{
		/*float red = input.value() / 100;
		if (red > 1)
		{
			red = 1;
		}
		
		graphics.setColor(new Color(red, 0, 0));
		graphics.drawLine(source.getX(), source.getY(), destination.getX(), destination.getY());
		
		graphics.setColor(Color.GREEN);
		graphics.drawString(Float.toString(input.value()), source.getX() + 15, source.getY() + 25);*/
	}
	
	private Point getNeuronPosition(int index)
	{
		/*float value = step * index;
		float x = (float)Math.cos(value) * radius;
		float y = (float)Math.sin(value) * radius;
		
		return new Point(x, y);*/
		return null;
	}
}
