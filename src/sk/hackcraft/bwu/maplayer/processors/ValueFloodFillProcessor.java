package sk.hackcraft.bwu.maplayer.processors;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.LayerProcessor;
import sk.hackcraft.bwu.maplayer.Layers;
import sk.hackcraft.bwu.maplayer.MatrixLayer;

public class ValueFloodFillProcessor implements LayerProcessor
{
	private final Set<LayerPoint> startPoints;
	private final int emptyValue;
	
	public ValueFloodFillProcessor(Set<LayerPoint> startPoints, int emptyValue)
	{
		this.startPoints = startPoints;
		this.emptyValue = emptyValue;
	}
	
	@Override
	public Layer process(Layer layer)
	{
		final Layer newLayer = new MatrixLayer(layer.getDimension());
		Layers.copy(layer, newLayer);
		
		final Queue<LayerPoint> continuePoints = new LinkedList<>(startPoints);

		LayerPoint[] directions = {
				new LayerPoint( 1,  0),
				new LayerPoint( 0,  1),
				new LayerPoint(-1,  0),
				new LayerPoint( 0, -1)
		};
		
		while (!continuePoints.isEmpty())
		{
			LayerPoint point = continuePoints.remove();
			
			int value = newLayer.get(point);
			
			for (LayerPoint direction : directions)
			{
				LayerPoint cellPosition = point.add(direction);
				
				if (!newLayer.isValid(cellPosition))
				{
					continue;
				}
				
				int cellValue = newLayer.get(cellPosition);
				
				if (cellValue == emptyValue)
				{
					newLayer.set(cellPosition, value);
					continuePoints.add(cellPosition);
				}
			}
		}
		
		return newLayer;
	}
}
