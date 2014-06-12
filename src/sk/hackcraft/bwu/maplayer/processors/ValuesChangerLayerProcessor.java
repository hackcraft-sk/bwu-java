package sk.hackcraft.bwu.maplayer.processors;

import java.util.Map;

import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerIterator;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.LayerProcessor;
import sk.hackcraft.bwu.maplayer.MatrixLayer;

public class ValuesChangerLayerProcessor implements LayerProcessor
{
	private final Map<Integer, Integer> changeMap;

	public ValuesChangerLayerProcessor(Map<Integer, Integer> changeMap)
	{
		this.changeMap = changeMap;
	}

	@Override
	public Layer process(Layer layer)
	{
		final MatrixLayer newLayer = new MatrixLayer(layer.getDimension());
		
		LayerIterator iterator = new LayerIterator(layer)
		{
			@Override
			protected void nextCell(LayerPoint coordinates, int value)
			{
				if (changeMap.containsKey(value))
				{
					int newValue = changeMap.get(value);
					newLayer.set(coordinates, newValue);
				}
				else
				{
					newLayer.set(coordinates, value);
				}
			}
		};
		
		iterator.iterate();
		
		return newLayer;
	}

}
