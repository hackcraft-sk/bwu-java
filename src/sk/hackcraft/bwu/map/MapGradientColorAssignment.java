package sk.hackcraft.bwu.map;

import java.util.Map;
import java.util.TreeMap;

import jnibwapi.util.BWColor;

public class MapGradientColorAssignment implements ColorAssigner
{
	private final TreeMap<Integer, BWColor> colors;

	public MapGradientColorAssignment(TreeMap<Integer, BWColor> colors)
	{
		this.colors = colors;
	}
	
	@Override
	public BWColor assignColor(int value)
	{
		Map.Entry<Integer, BWColor> entry = colors.floorEntry(value);
		
		if (entry == null)
		{
			return null;
		}
		else
		{
			return entry.getValue();
		}
	}
}
