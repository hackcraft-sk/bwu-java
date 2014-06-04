package sk.hackcraft.bwu.map;

public class Dimension
{
	private final int width, height;

	public Dimension(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}
	
	@Override
	public boolean equals(Object object)
	{
		if (!(object instanceof Dimension))
		{
			return false;
		}
		
		Dimension dimension = (Dimension)object;
		
		return dimension.width == width && dimension.height == height;
	}
	
	@Override
	public int hashCode()
	{
		return width * 31 + height;
	}
}
