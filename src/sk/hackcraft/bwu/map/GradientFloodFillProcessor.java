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
		Layer newLayer = new MatrixLayer(layer.getDimension());
		
		Queue<Point> continuePoints = new LinkedList<>();
		
		Dimension dimension = layer.getDimension();
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getWidth(); y++)
			{
				Point point = new Point(x, y);
				
				if (layer.get(point) == startValue)
				{
					continuePoints.add(point);
				}
			}
		}

		Point[] directions = {
				new Point( 1,  0),
				new Point( 0,  1),
				new Point(-1,  0),
				new Point( 0, -1)
		};
		
		while (!continuePoints.isEmpty())
		{
			Point point = continuePoints.remove();
			
			int actualValue = layer.get(point);
			
			int newValue = actualValue += addFactor;
			
			for (Point direction : directions)
			{
				Point cellPosition = point.add(direction);
				
				if (!layer.isValid(cellPosition))
				{
					continue;
				}
				
				int cellValue = layer.get(cellPosition);
				
				if (evaluateCell(cellValue, newValue))
				{
					newLayer.set(cellPosition, newValue);
					//continuePoints.add(cellPosition);
				}
			}
		}
		
		return newLayer;
	}
	
	protected abstract boolean evaluateCell(int cellValue, int newValue);
}
