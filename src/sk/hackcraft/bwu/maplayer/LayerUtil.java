package sk.hackcraft.bwu.maplayer;

import java.util.HashSet;
import java.util.Set;

public class LayerUtil
{
	public static final LayerPoint[] DIRECTIONS = {
			new LayerPoint( 1,  0),
			new LayerPoint( 0,  1),
			new LayerPoint(-1,  0),
			new LayerPoint( 0, -1)
	};
	
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
	
	public static void fill(final Layer layer, final int fillValue)
	{
		layer.createLayerIterator(new LayerIterator.IterateListener()
		{
			@Override
			public void nextCell(LayerPoint cellCoordinates, int cellValue)
			{
				layer.set(cellCoordinates, fillValue);
			}
		}).iterateAll();
	}
	
	public static Set<LayerPoint> getPointsWithValue(Layer layer, final int specifiedValue)
	{
		final Set<LayerPoint> points = new HashSet<>();
		
		layer.createLayerIterator(new LayerIterator.IterateListener()
		{
			
			@Override
			public void nextCell(LayerPoint cellCoordinates, int cellValue)
			{
				if (cellValue == specifiedValue)
				{
					points.add(cellCoordinates);
				}
			}
		}).iterateAll();

		return points;
	}
}
