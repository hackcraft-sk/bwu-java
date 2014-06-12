package sk.hackcraft.bwu.maplayer.visualization;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import sk.hackcraft.bwu.Updateable;
import sk.hackcraft.bwu.maplayer.ColorAssigner;
import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerIterator;
import sk.hackcraft.bwu.maplayer.LayerPoint;

public class LayersPainter implements Updateable
{
	private final List<Layer> layers;
	private final Map<Layer, ColorAssigner<Color>> colorAssigners;
	
	private BufferedImage backBuffer;

	public LayersPainter(LayerDimension mapDimension)
	{
		this.layers = new ArrayList<Layer>();
		this.colorAssigners = new HashMap<Layer, ColorAssigner<Color>>();

		backBuffer = new BufferedImage(mapDimension.getWidth(), mapDimension.getHeight(), BufferedImage.TYPE_INT_ARGB);
	}
	
	public synchronized void addLayer(Layer layer, ColorAssigner<Color> colorAssigner)
	{
		layers.add(layer);
		colorAssigners.put(layer, colorAssigner);
	}
	
	public synchronized void addLayer(Layer layer, ColorAssigner<Color> colorAssigner, int index)
	{
		layers.add(index, layer);
		colorAssigners.put(layer, colorAssigner);
	}
	
	public synchronized void removeLayer(Layer layer)
	{
		layers.remove(layer);
		colorAssigners.remove(layer);
	}
	
	@Override
	public synchronized void update()
	{
		final Graphics2D g2d = (Graphics2D)backBuffer.getGraphics();
		
		for (final Layer layer : layers)
		{			
			new LayerIterator(layer)
			{
				@Override
				protected void nextCell(LayerPoint coordinates, int value)
				{
					int x = coordinates.getX();
					int y = coordinates.getY();
					
					ColorAssigner<Color> colorAssigner = colorAssigners.get(layer);
					Color color = colorAssigner.assignColor(value);
					
					g2d.setColor(color);
					g2d.fillRect(x, y, 1, 1);
				}
			}.iterate();
		}
	}
	
	public synchronized void drawTo(Graphics2D graphics, java.awt.Dimension dimension)
	{
		int dx1 = 0;
		int dy1 = 0;
		int dx2 = (int)dimension.getWidth();
		int dy2 = (int)dimension.getHeight();
		int sx1 = 0;
		int sy1 = 0;
		int sx2 = backBuffer.getWidth();
		int sy2 = backBuffer.getHeight();

		graphics.drawImage(backBuffer, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
	}
}
