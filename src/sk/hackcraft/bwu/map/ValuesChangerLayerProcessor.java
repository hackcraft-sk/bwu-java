package sk.hackcraft.bwu.map;

import java.util.Map;

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
			protected void nextCell(Point coordinates, int value)
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
