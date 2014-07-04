package sk.hackcraft.bwu.maplayer.processors;

import java.util.Map;

import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerIterator;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.layers.MatrixLayer;

public class ValuesChangerLayerProcessor
{
	private final Map<Integer, Integer> changeMap;

	public ValuesChangerLayerProcessor(Map<Integer, Integer> changeMap)
	{
		this.changeMap = changeMap;
	}

	public Layer process(Layer layer)
	{
		final MatrixLayer newLayer = new MatrixLayer(layer.getDimension());
		
		layer.createLayerIterator(new LayerIterator.IterateListener()
		{
			
			@Override
			public void nextCell(LayerPoint cellCoordinates, int cellValue)
			{
				if (changeMap.containsKey(cellValue))
				{
					int newValue = changeMap.get(cellValue);
					newLayer.set(cellCoordinates, newValue);
				}
				else
				{
					newLayer.set(cellCoordinates, cellValue);
				}
			}
		}).iterateAll();
		
		return newLayer;
	}

}
