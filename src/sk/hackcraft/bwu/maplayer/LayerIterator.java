package sk.hackcraft.bwu.maplayer;

import java.util.function.Predicate;

public abstract class LayerIterator
{
	private final Layer layer;
	protected final IterateListener listener;

	public LayerIterator(Layer layer, IterateListener listener)
	{
		this.layer = layer;
		this.listener = listener;
	}

	public void iterateAll()
	{
		LayerDimension dimension = layer.getDimension();
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				LayerPoint coordinates = new LayerPoint(x, y);
				int value = layer.get(coordinates);
				
				listener.nextCell(coordinates, value);
			}
		}
	}
	
	public abstract void iterateFeature();

	public interface IterateListener
	{
		void nextCell(LayerPoint cellCoordinates, int cellValue);
	}
}
