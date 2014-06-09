package sk.hackcraft.bwu.map;

public class Bounds
{
	private final Point position;
	private final Dimension dimension;
	
	public Bounds(Point position, Dimension dimension)
	{
		this.position = position;
		this.dimension = dimension;
	}
	
	public Bounds(int x, int y, int width, int height)
	{
		this(new Point(x, y), new Dimension(width, height));
	}
	
	public Point getPosition()
	{
		return position;
	}
	
	public Dimension getDimension()
	{
		return dimension;
	}
	
	public boolean isInside(Point point)
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
