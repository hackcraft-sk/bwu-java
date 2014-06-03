package sk.hackcraft.bwu.map;

public class Layer
{
	private final int width, height;
	private final int[] matrix;
	
	public Layer(int width, int height)
	{
		this.width = width;
		this.height = height;
		
		this.matrix = new int[width * height];
	}
	
	public Layer(Layer layer, int offsetX, int lengthX, int offsetY, int lengthY)
	{
		this(lengthX, lengthY);
		
		int thisX = 0, thisY = 0;
		
		for (int x = 0; x < layer.width; x++)
		{
			if (x < offsetX || x >= offsetX + lengthX)
			{
				continue;
			}
			
			for (int y = 0; y < layer.height; y++)
			{
				if (y < offsetY || y >= offsetY + lengthY)
				{
					continue;
				}
				
				int value = layer.get(x, y);
				set(thisX, thisY, value);
				
				thisY++;
			}
			
			thisX++;
			thisY = 0;
		}
	}
	
	public Layer(Layer layer)
	{
		this(layer, 0, layer.width, 0, layer.height);
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public int get(int x, int y)
	{
		int i = positionToIndex(x, y);
		return matrix[i];
	}
	
	public void set(int x, int y, int value)
	{
		int i = positionToIndex(x, y);
		matrix[i] = value;
	}
	
	private int positionToIndex(int x, int y)
	{
		return  y * width + x;
	}
	
	@Override
	public String toString()
	{
		return String.format("Layer %dx%d", width, height);
	}
}
