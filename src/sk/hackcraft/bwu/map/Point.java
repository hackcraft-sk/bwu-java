package sk.hackcraft.bwu.map;

public class Point
{
	public static final Point ORIGIN = new Point(0, 0);

	private final int x, y;
	
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public Point add(Point point)
	{
		return new Point(x + point.x, y + point.y);
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof Point))
		{
			return false;
		}
		
		Point point = (Point)object;
		
		return point.x == x && point.y == y;
	}
	
	@Override
	public int hashCode()
	{
		return x * 31 + y;
	}
	
	@Override
	public String toString()
	{
		return String.format("Point [%d,%d]", x, y);
	}
}
