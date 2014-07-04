package sk.hackcraft.bwu.maplayer.processors;

import java.util.Set;

import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerPoint;

public class DrawProcessor
{
	public static void putValue(Layer layer, Set<LayerPoint> points, int value)
	{
		for (LayerPoint point : points)
		{
			layer.set(point, value);
		}
	}
}
