package sk.hackcraft.bwu.layer;

import java.util.Map;

import jnibwapi.util.BWColor;

public class MapExactColorAssigner<C> implements ColorAssigner<C>
{
	private final Map<Integer, C> colors;
	
	public MapExactColorAssigner(Map<Integer, C> colors)
	{
		this.colors = colors;
	}

	@Override
	public C assignColor(int value)
	{
		return colors.get(value);
	}
}
