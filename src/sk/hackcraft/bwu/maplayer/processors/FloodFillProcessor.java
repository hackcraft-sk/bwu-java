package sk.hackcraft.bwu.maplayer.processors;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import sk.hackcraft.bwu.Comparison;
import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.LayerUtil;

public class FloodFillProcessor
{
	public static void fillGradient(Layer layer, Map<LayerPoint, Integer> startPoints)
	{
		fillGradient(layer, startPoints, -1, Comparison.LESS);
	}
	
	public static void fillGradient(Layer layer, Map<LayerPoint, Integer> startPoints, int step, Comparison comparison)
	{
		for (Map.Entry<LayerPoint, Integer> entry : startPoints.entrySet())
		{
			LayerPoint point = entry.getKey();
			int value = entry.getValue();
			
			layer.set(point, value);
		}
		
		fillGradient(layer, startPoints.keySet(), step, comparison);
	}

	public static void fillGradient(Layer layer, Set<LayerPoint> startPoints)
	{
		fillGradient(layer, startPoints, -1, Comparison.LESS);
	}
	
	public static void fillGradient(Layer layer, Set<LayerPoint> startPoints, int step, Comparison comparison)
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
	
	public static Set<LayerPoint> defillGradient(Layer layer, int clearValue, Set<LayerPoint> startPoints)
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
	
	public static void fillValue(Layer layer, Map<LayerPoint, Integer> startPoints, int fillableValue)
	{
		Set<LayerPoint> startPointsSet = new HashSet<>();
		
		for (Map.Entry<LayerPoint, Integer> entry : startPoints.entrySet())
		{
			LayerPoint point = entry.getKey();
			int value = entry.getValue();
			
			layer.set(point, value);
			
			startPointsSet.add(point);
		}
		
		fillValue(layer, startPointsSet, fillableValue);
	}
	
	public static void fillValue(Layer layer, Set<LayerPoint> startPoints, int fillableValue)
	{
		Queue<LayerPoint> continuePoints = new LinkedList<>();

		while (!continuePoints.isEmpty())
		{
			LayerPoint point = continuePoints.remove();
			
			int value = layer.get(point);
			
			for (LayerPoint directon : LayerUtil.DIRECTIONS)
			{
				LayerPoint neighboorPoint = point.add(directon);
			
				if (layer.get(neighboorPoint) == fillableValue)
				{
					layer.set(neighboorPoint, value);
					continuePoints.add(neighboorPoint);
				}
			}
		}
	}
}
