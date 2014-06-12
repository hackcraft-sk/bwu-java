package sk.hackcraft.bwu.maplayer;

public class LayerPoint
{
	public static final LayerPoint ORIGIN = new LayerPoint(0, 0);

	private final int x, y;
	
	public LayerPoint(int x, int y)
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
	
	public LayerPoint add(LayerPoint point)
	{
		return new LayerPoint(x + point.x, y + point.y);
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof LayerPoint))
		{
			return false;
		}
		
		LayerPoint point = (LayerPoint)object;
		
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
