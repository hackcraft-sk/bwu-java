package sk.hackcraft.bwu.layer;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class GradientFloodFillProcessor implements LayerProcessor
{
	private final int startValue;
	private final int addFactor;
	
	public GradientFloodFillProcessor(int startValue, int addFactor)
	{
		this.startValue = startValue;
		this.addFactor = addFactor;
	}
	
	@Override
	public Layer process(Layer layer)
	{
		final Layer newLayer = new MatrixLayer(layer.getDimension());
		Layers.copy(layer, newLayer);
		
		final Queue<LayerPoint> continuePoints = new LinkedList<>();
		
		LayerIterator iterator = new LayerIterator(newLayer)
		{
			@Override
			protected void nextCell(LayerPoint coordinates, int value)
			{
				if (newLayer.get(coordinates) == startValue)
				{
					continuePoints.add(coordinates);
				}
			}
		};
		
		iterator.iterate();

		LayerPoint[] directions = {
				new LayerPoint( 1,  0),
				new LayerPoint( 0,  1),
				new LayerPoint(-1,  0),
				new LayerPoint( 0, -1)
		};
		
		while (!continuePoints.isEmpty())
		{
			LayerPoint point = continuePoints.remove();
			
			int actualValue = newLayer.get(point);
			
			int newValue = actualValue + addFactor;
			
			for (LayerPoint direction : directions)
			{
				LayerPoint cellPosition = point.add(direction);
				
				if (!newLayer.isValid(cellPosition))
				{
					continue;
				}
				
				int cellValue = newLayer.get(cellPosition);
				
				if (fillCell(cellValue, newValue))
				{
					newLayer.set(cellPosition, newValue);
					continuePoints.add(cellPosition);
				}
			}
		}
		
		return newLayer;
	}
	
	protected abstract boolean fillCell(int cellValue, int newValue);
}
