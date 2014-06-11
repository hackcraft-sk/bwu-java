package sk.hackcraft.bwu.layer;

public class LayerDimension
{
	private final int width, height;

	public LayerDimension(int width, int height)
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
		if (!(object instanceof LayerDimension))
		{
			return false;
		}
		
		LayerDimension dimension = (LayerDimension)object;
		
		return dimension.width == width && dimension.height == height;
	}
	
	@Override
	public int hashCode()
	{
		return width * 31 + height;
	}
	
	@Override
	public String toString()
	{
		return String.format("Dimension %dx%d", width, height);
	}
}
