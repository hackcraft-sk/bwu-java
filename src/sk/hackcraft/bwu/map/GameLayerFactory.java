package sk.hackcraft.bwu.map;

import jnibwapi.JNIBWAPI;
import jnibwapi.Map;
import jnibwapi.Position;
import jnibwapi.Position.PosType;

public class GameLayerFactory
{
	public static Layer createBuildableLayer(JNIBWAPI jnibwapi, int positiveValue, int negativeValue)
	{
		Map map = jnibwapi.getMap();
		
		Position dimension = map.getSize();
		int width = dimension.getBX();
		int height = dimension.getBY();
		
		MatrixLayer layer = new MatrixLayer(new Dimension(width, height));
		
		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				Position position = new Position(x, y, PosType.BUILD);
				int value = (map.isBuildable(position)) ? positiveValue : negativeValue;

				layer.set(x, y, value);
			}
		}
		
		return layer;
	}
}
