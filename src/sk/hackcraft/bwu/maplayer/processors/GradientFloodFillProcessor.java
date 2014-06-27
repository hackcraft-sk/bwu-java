package sk.hackcraft.bwu.maplayer.processors;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import sk.hackcraft.bwu.Comparison;
import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.LayerUtil;

public class GradientFloodFillProcessor
{
	public static void floodFill(Layer layer, int value, Set<LayerPoint> startPoints)
	{
		for (LayerPoint point : startPoints)
		{
			layer.set(point, value);
		}
		
		floodFill(layer, startPoints);
	}
	
	public static void floodFill(Layer layer, Set<LayerPoint> startPoints)
	{
		floodFill(layer, startPoints, -1, Comparison.LESS);
	}
	
	public static void floodFill(Layer layer, Set<LayerPoint> startPoints, int step, Comparison comparison)
	{
		Queue<LayerPoint> continuePoints = new LinkedList<LayerPoint>(startPoints);
		
		while (!continuePoints.isEmpty())
		{
			LayerPoint point = continuePoints.remove();
			
			int actualValue = layer.get(point);
			
			int newValue = actualValue + step;
			
			for (LayerPoint direction : LayerUtil.DIRECTIONS)
			{
				LayerPoint cellPosition = point.add(direction);
				
				if (!layer.isValid(cellPosition))
				{
					continue;
				}
				
				int cellValue = layer.get(cellPosition);
				
				if (comparison.compare(cellValue, newValue))
				{
					layer.set(cellPosition, newValue);
					continuePoints.add(cellPosition);
				}
			}
		}
	}
	
	public static Set<LayerPoint> floodDefill(Layer layer, int clearValue, Set<LayerPoint> startPoints)
	{
		Set<LayerPoint> borderPoints = new HashSet<>();
		Queue<LayerPoint> continuePoints = new LinkedList<LayerPoint>(startPoints);
		
		while (!continuePoints.isEmpty())
		{
			LayerPoint point = continuePoints.remove();
			
			int actualValue = layer.get(point);
			
			for (LayerPoint direction : LayerUtil.DIRECTIONS)
			{
				LayerPoint cellPosition = point.add(direction);
				
				if (!layer.isValid(cellPosition))
				{
					continue;
				}
				
				int cellValue = layer.get(cellPosition);
				
				if (cellValue == clearValue)
				{
					continue;
				}
				
				if (cellValue < actualValue)
				{
					continuePoints.add(cellPosition);
				}
				else
				{
					borderPoints.add(cellPosition);
				}
			}
			
			layer.set(point, clearValue);
		}
		
		return borderPoints;
	}
}
