package sk.hackcraft.bwu.layer;

public abstract class AbstractLayer implements Layer
{	
	protected final LayerDimension dimension;
	
	public AbstractLayer(LayerDimension dimension)
	{
		this.dimension = dimension;
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
	public int get(LayerPoint coordinates)
	{
		return get(coordinates.getX(), coordinates.getY());
	}
	
	@Override
	public void set(LayerPoint coordinates, int value)
	{
		set(coordinates.getX(), coordinates.getY(), value);
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
}
