package net.moergil.cortex.gui;

import java.awt.Graphics2D;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.moergil.cortex.Genome;
import net.moergil.cortex.OperandOrganism;
import net.moergil.cortex.Organism;

public class Visualization
{
	public static void main(String[] args)
	{
		File file = new File("Genome-1401133365281.gnm");
		
		try
		(
				FileInputStream fileInput = new FileInputStream(file);
				DataInputStream dataInput = new DataInputStream(fileInput);
		)
		{
			Genome genome = new Genome(dataInput);
			
			OperandOrganism organism = new OperandOrganism(genome);
			Visualization visualization = new Visualization(organism, 25);
		}
		catch (IOException e)
		{
			System.out.println("Can't save genome: " + e.getMessage());
		}
	}
	
	private final JFrame frame;
	
	private final NeuroCanvas canvas;
	
	public Visualization(final OperandOrganism organism, int input)
	{
		canvas = new NeuroCanvas(organism);
		
		JPanel panel = new JPanel()
		{
			public void paint(java.awt.Graphics g)
			{
				Graphics2D g2d = (Graphics2D)g;
				
				canvas.draw(g2d);
			}
		};
		
		frame = new JFrame();
		frame.setSize(300, 300);
		
		frame.getContentPane().add(panel);
		
		frame.setVisible(true);
		
		//organism.setInput(input);
		
		new Timer().schedule(new TimerTask()
		{
			
			@Override
			public void run()
			{
				organism.update();
				frame.repaint();
				
				System.out.println("Updated");
			}
		}, 0, 1000);
	}
}
