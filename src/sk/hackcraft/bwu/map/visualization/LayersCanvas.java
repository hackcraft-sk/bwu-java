package sk.hackcraft.bwu.map.visualization;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class LayersCanvas extends JPanel
{
	private final LayersPainter layersPainter;

	public LayersCanvas(LayersPainter layersPainter)
	{
		this.layersPainter = layersPainter;
	}
	
	@Override
	protected void paintComponent(Graphics g)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		layersPainter.drawTo(g2d, getSize());
	}
}
