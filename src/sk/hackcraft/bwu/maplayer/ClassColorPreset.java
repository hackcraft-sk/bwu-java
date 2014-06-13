package sk.hackcraft.bwu.maplayer;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ClassColorPreset
{
	private static final Map<Integer, Color> preset;
	
	static
	{
		preset = new HashMap<>();
	}
	
	public static Map<Integer, Color> get()
	{
		return preset;
	}
}
