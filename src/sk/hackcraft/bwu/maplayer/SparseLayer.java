package sk.hackcraft.bwu.maplayer;

import java.util.HashMap;
import java.util.Map;

import sk.hackcraft.bwu.maplayer.LayerIterator.IterateListener;

public class SparseLayer extends AbstractLayer
{
	private Map<LayerPoint, Integer> map;
	
	public SparseLayer(LayerDimension dimenson)
	{
		super(dimenson);
		
		this.map = new HashMap<LayerPoint, Integer>();
	}

	@Override
	public int get(int x, int y)
	{
		LayerPoint point = new LayerPoint(x, y);
		return get(point);
	}
	
	@Override
	public int get(LayerPoint coordinates)
	{
		if (!map.containsKey(coordinates))
		{
			return 0;
		}
		else
		{
			return map.get(coordinates);
		}
	}

	@Override
	public void set(int x, int y, int value)
	{
		LayerPoint point = new LayerPoint(x, y);
		set(point, value);
	}
	
	@Override
	public void set(LayerPoint coordinates, int value)
	{
		if (value == 0)
		{
			map.remove(coordinates);
		}
		else
		{
			map.put(coordinates, value);
		}
	}
	
	@Override
	public LayerIterator createLayerIterator(IterateListener listener)
	{
		return new LayerIterator(this, listener)
		{
			@Override
			public void iterateFeature()
			{
				for (Map.Entry<LayerPoint, Integer> entry : map.entrySet())
				{
					listener.nextCell(entry.getKey(), entry.getValue());
				}
			}
		};
	}
	
	protected void clear()
	{
		map.clear();
	}
}
