package sk.hackcraft.bwu.maplayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public abstract class RandomColorAssigner<C> implements ColorAssigner<C>
{
	private final Random random;
	private final Map<Integer, C> usedColors;
	
	public RandomColorAssigner(Random random)
	{
		this.random = random;

		usedColors = new HashMap<>();
	}
	
	@Override
	public C assignColor(int value)
	{
		if (!usedColors.containsKey(value))
		{
			int r = random.nextInt(256);
			int g = random.nextInt(256);
			int b = random.nextInt(256);
			C color = createColor(r, g, b);
			
			usedColors.put(value, color);
		}
		
		return usedColors.get(value);
	}
	
	protected abstract C createColor(int r, int g, int b);
}
