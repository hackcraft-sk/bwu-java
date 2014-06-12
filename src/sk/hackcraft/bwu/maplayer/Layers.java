package sk.hackcraft.bwu.maplayer;

import java.util.HashSet;
import java.util.Set;

public class Layers
{
	public static void copy(Layer from, Layer to, int offsetX, int lengthX, int offsetY, int lengthY)
	{
		int thisX = 0, thisY = 0;
		
		for (int x = 0; x < from.getDimension().getWidth(); x++)
		{
			if (x < offsetX || x >= offsetX + lengthX)
			{
				continue;
			}
			
			for (int y = 0; y < from.getDimension().getHeight(); y++)
			{
				if (y < offsetY || y >= offsetY + lengthY)
				{
					continue;
				}
				
				int value = from.get(new LayerPoint(x, y));
				to.set(thisX, thisY, value);
				
				thisY++;
			}
			
			thisX++;
			thisY = 0;
		}
	}
	
	public static void copy(Layer from, Layer to)
	{
		copy(from, to, 0, from.getDimension().getWidth(), 0, from.getDimension().getHeight());
	}
	
	public static void fill(final Layer layer, final int value)
	{
		new LayerIterator(layer)
		{
			
			@Override
			protected void nextCell(LayerPoint coordinates, int actualValue)
			{
				layer.set(coordinates, value);
			}
		}.iterate();;
	}
	
	public static Set<LayerPoint> getPointsWithValue(Layer layer, final int value)
	{
		final Set<LayerPoint> points = new HashSet<>();
		
		new LayerIterator(layer)
		{
			@Override
			protected void nextCell(LayerPoint coordinates, int actualValue)
			{
				if (actualValue == value)
				{
					points.add(coordinates);
				}
			}
		}.iterate();
		
		return points;
	}
}
