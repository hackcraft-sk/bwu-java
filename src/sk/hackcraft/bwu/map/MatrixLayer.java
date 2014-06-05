package sk.hackcraft.bwu.map;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MatrixLayer implements Layer
{
	private final Dimension dimension;
	private final int[] matrix;
	
	public MatrixLayer(Dimension dimension)
	{
		this.dimension = dimension;
		
		this.matrix = new int[dimension.getWidth() * dimension.getHeight()];
	}
	
	public MatrixLayer(Layer layer, int offsetX, int lengthX, int offsetY, int lengthY)
	{
		this(new Dimension(lengthX, lengthY));
		
		int thisX = 0, thisY = 0;
		
		for (int x = 0; x < layer.getDimension().getWidth(); x++)
		{
			if (x < offsetX || x >= offsetX + lengthX)
			{
				continue;
			}
			
			for (int y = 0; y < layer.getDimension().getHeight(); y++)
			{
				if (y < offsetY || y >= offsetY + lengthY)
				{
					continue;
				}
				
				int value = layer.get(new Point(x, y));
				set(thisX, thisY, value);
				
				thisY++;
			}
			
			thisX++;
			thisY = 0;
		}
	}
	
	public MatrixLayer(Layer layer)
	{
		this(layer, 0, layer.getDimension().getWidth(), 0, layer.getDimension().getHeight());
	}
	
	@Override
	public Dimension getDimension()
	{
		return dimension;
	}
	
	@Override
	public boolean isValid(Point coordinates)
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
	public int get(Point coordinates)
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
	public void set(Point coordinates, int value)
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
		Dimension dimension = getDimension();
		return String.format("Layer %dx%d", dimension.getWidth(), dimension.getHeight());
	}

	@Override
	public Layer add(Layer layer)
	{
		Dimension dimension = getDimension();
		
		MatrixLayer newLayer = new MatrixLayer(dimension);
		
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				Point point = new Point(x, y);
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
}
