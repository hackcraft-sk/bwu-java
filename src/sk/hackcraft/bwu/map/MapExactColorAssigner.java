package sk.hackcraft.bwu.map;

import java.util.Map;

import jnibwapi.util.BWColor;

public class MapExactColorAssigner implements ColorAssigner
{
	private final Map<Integer, BWColor> colors;
	
	public MapExactColorAssigner(Map<Integer, BWColor> colors)
	{
		this.colors = colors;
	}

	@Override
	public BWColor assignColor(int value)
	{
		return colors.get(value);
	}
}
