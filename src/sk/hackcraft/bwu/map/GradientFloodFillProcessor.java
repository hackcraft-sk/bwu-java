package sk.hackcraft.bwu.map;

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
		final Layer newLayer = new MatrixLayer(layer);
		
		final Queue<Point> continuePoints = new LinkedList<>();
		
		LayerIterator iterator = new LayerIterator(newLayer)
		{
			@Override
			protected void nextCell(Point coordinates, int value)
			{
				if (newLayer.get(coordinates) == startValue)
				{
					continuePoints.add(coordinates);
				}
			}
		};
		
		iterator.iterate();

		Point[] directions = {
				new Point( 1,  0),
				new Point( 0,  1),
				new Point(-1,  0),
				new Point( 0, -1)
		};
		
		while (!continuePoints.isEmpty())
		{
			Point point = continuePoints.remove();
			
			int actualValue = newLayer.get(point);
			
			int newValue = actualValue + addFactor;
			
			for (Point direction : directions)
			{
				Point cellPosition = point.add(direction);
				
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
