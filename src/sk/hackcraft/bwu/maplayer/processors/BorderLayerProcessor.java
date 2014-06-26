package sk.hackcraft.bwu.maplayer.processors;

import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.MatrixLayer;

public class BorderLayerProcessor
{
	private final int newValue, sourceValue;
	
	public BorderLayerProcessor(int newValue, int sourceValue)
	{
		this.newValue = newValue;
		this.sourceValue = sourceValue;
	}

	public Layer process(Layer layer)
	{
		Layer newLayer = new MatrixLayer(layer.getDimension());
		
		LayerDimension dimension = layer.getDimension();
		
		LayerPoint[] directions = {
			new LayerPoint( 1,  0),
			new LayerPoint( 0,  1),
			new LayerPoint(-1,  0),
			new LayerPoint( 0, -1),
		};

		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				LayerPoint cellPosition = new LayerPoint(x, y);
				
				if (layer.get(cellPosition) != sourceValue)
				{
					continue;
				}
				
				boolean border = false;
				
				for (LayerPoint direction : directions)
				{
					LayerPoint point = cellPosition.add(direction);
					
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
