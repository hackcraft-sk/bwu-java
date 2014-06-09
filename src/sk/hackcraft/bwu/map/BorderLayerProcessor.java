package sk.hackcraft.bwu.map;

public class BorderLayerProcessor implements LayerProcessor
{
	private final int newValue, sourceValue;
	
	public BorderLayerProcessor(int newValue, int sourceValue)
	{
		this.newValue = newValue;
		this.sourceValue = sourceValue;
	}
	
	@Override
	public Layer process(Layer layer)
	{
		Layer newLayer = new MatrixLayer(layer.getDimension());
		
		Dimension dimension = layer.getDimension();
		
		Point[] directions = {
			new Point( 1,  0),
			new Point( 0,  1),
			new Point(-1,  0),
			new Point( 0, -1),
		};

		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				Point cellPosition = new Point(x, y);
				
				if (layer.get(cellPosition) != sourceValue)
				{
					continue;
				}
				
				boolean border = false;
				
				for (Point direction : directions)
				{
					Point point = cellPosition.add(direction);
					
					if (!layer.isValid(point))
					{
						continue;
					}
					
					if (layer.get(point) != sourceValue)
					{
						border = true;
						break;
					}
				}
				
				if (border)
				{
					newLayer.set(cellPosition, newValue);
				}
			}
		}
		
		return newLayer;
	}
}
