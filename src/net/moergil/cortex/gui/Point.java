package net.moergil.cortex.gui;

public class Point
{
	private final int x, y;

	public Point(double x, double y)
	{
		this.x = (int)x;
		this.y = (int)y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}
