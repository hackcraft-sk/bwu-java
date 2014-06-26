package sk.hackcraft.bwu.maplayer;

import java.awt.Color;

public class GradientColorAssigner implements ColorAssigner<Color>
{
	private int from, to;
	
	public GradientColorAssigner(int from, int to)
	{
		this.from = from;
		this.to = to;
	}

	@Override
	public Color assignColor(int value)
	{
		int lightness;
		if (value < from)
		{
			lightness = 0;
		}
		else if (value > to)
		{
			lightness = 255;
		}
		else
		{ 
			int range = to - from;
			int rangeValue = value - from;
			
			int percentage = rangeValue * 100 / range;
			lightness = 255 * percentage / 100;
		}
		
		return new Color(lightness, lightness, lightness);
	}
}
