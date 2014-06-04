package sk.hackcraft.bwu.map;

public class Point
{
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
}
