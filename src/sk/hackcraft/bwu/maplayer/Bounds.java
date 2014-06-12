package sk.hackcraft.bwu.maplayer;

public class Bounds
{
	private final LayerPoint position;
	private final LayerDimension dimension;
	
	public Bounds(LayerPoint position, LayerDimension dimension)
	{
		this.position = position;
		this.dimension = dimension;
	}
	
	public Bounds(int x, int y, int width, int height)
	{
		this(new LayerPoint(x, y), new LayerDimension(width, height));
	}
	
	public LayerPoint getPosition()
	{
		return position;
	}
	
	public LayerDimension getDimension()
	{
		return dimension;
	}
	
	public boolean isInside(LayerPoint point)
	{
		int x = point.getX();
		int y = point.getY();
		
		if (x < position.getX() || x >= position.getX() + dimension.getWidth())
		{
			return false;
		}
		
		if (y < position.getY() || y >= position.getY() + dimension.getHeight())
		{
			return false;
		}
		
		return true;
	}
}
