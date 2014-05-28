package sk.hackcraft.bwu.map;

import com.sun.corba.se.impl.ior.WireObjectKeyTemplate;

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
		int i = y * height + x;
		return matrix[i];
	}
	
	public void set(int x, int y, int value)
	{
		int i = y * height + x;
		matrix[i] = value;
	}
}
