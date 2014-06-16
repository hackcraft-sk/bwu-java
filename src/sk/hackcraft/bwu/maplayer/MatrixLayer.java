package sk.hackcraft.bwu.maplayer;

import sk.hackcraft.bwu.maplayer.LayerIterator.IterateListener;

public class MatrixLayer extends AbstractLayer
{
	private final int[] matrix;
	
	public MatrixLayer(LayerDimension dimension)
	{
		super(dimension);
		
		this.matrix = new int[dimension.getWidth() * dimension.getHeight()];
	}
	
	@Override
	public LayerDimension getDimension()
	{
		return dimension;
	}
	
	@Override
	public boolean isValid(LayerPoint coordinates)
	{
		int x = coordinates.getX();
		if (x < 0 || x >= dimension.getWidth())
		{
			return false;
		}
		
		int y = coordinates.getY();
		if (y < 0 || y >= dimension.getHeight())
		{
			return false;
		}
		
		return true;	
	}
	
	@Override
	public int get(int x, int y)
	{
		int i = positionToIndex(x, y);
		return matrix[i];
	}
	
	@Override
	public int get(LayerPoint coordinates)
	{
		return get(coordinates.getX(), coordinates.getY());
	}
	
	@Override
	public void set(int x, int y, int value)
	{
		int i = positionToIndex(x, y);
		matrix[i] = value;
	}
	
	@Override
	public void set(LayerPoint coordinates, int value)
	{
		set(coordinates.getX(), coordinates.getY(), value);
	}
	
	private int positionToIndex(int x, int y)
	{
		int width = dimension.getWidth();
		return  y * width + x;
	}
	
	@Override
	public String toString()
	{
		LayerDimension dimension = getDimension();
		return String.format("Layer %dx%d", dimension.getWidth(), dimension.getHeight());
	}

	@Override
	public Layer add(Layer layer)
	{
		LayerDimension dimension = getDimension();
		
		MatrixLayer newLayer = new MatrixLayer(dimension);
		
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				LayerPoint point = new LayerPoint(x, y);
				int value = get(point) + layer.get(point);
				newLayer.set(point, value);
			}
		}
		
		return newLayer;
	}

	@Override
	public Layer substract(Layer layer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Layer multiply(Layer layer)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Layer divide(Layer layer)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public LayerIterator createLayerIterator(IterateListener listener)
	{
		return new LayerIterator(this, listener)
		{
			@Override
			public void iterateFeature()
			{
				iterateAll();
			}
		};
	}
}
