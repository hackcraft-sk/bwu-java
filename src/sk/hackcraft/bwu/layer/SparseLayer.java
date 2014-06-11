package sk.hackcraft.bwu.layer;

import java.util.HashMap;
import java.util.Map;

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
}
