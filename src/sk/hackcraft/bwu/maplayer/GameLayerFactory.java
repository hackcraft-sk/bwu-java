package sk.hackcraft.bwu.maplayer;

import jnibwapi.JNIBWAPI;
import jnibwapi.Map;
import jnibwapi.Position;
import jnibwapi.Position.PosType;

public class GameLayerFactory
{
	public static Layer createBuildableLayer(final Map map)
	{
		Position dimension = map.getSize();
		int width = dimension.getBX();
		int height = dimension.getBY();
		
		MapLayerCreator creator = new MapLayerCreator(width, height)
		{
			@Override
			protected boolean evaluate(int x, int y)
			{
				Position position = new Position(x, y, PosType.BUILD);
				return map.isBuildable(position);
			}
		};
		
		return creator.create();
	}
	
	public static Layer createLowResWalkableLayer(final Map map)
	{
		Position dimension = map.getSize();
		int width = dimension.getBX();
		int height = dimension.getBY();
		
		MapLayerCreator creator = new MapLayerCreator(width, height)
		{
			@Override
			protected boolean evaluate(int x, int y)
			{
				Position position = new Position(x, y, PosType.BUILD);
				return map.isLowResWalkable(position);
			}
		};
		
		return creator.create();
	}
	
	public static Layer createWalkableLayer(final Map map)
	{
		Position dimension = map.getSize();
		int width = dimension.getWX();
		int height = dimension.getWY();
		
		MapLayerCreator creator = new MapLayerCreator(width, height)
		{
			@Override
			protected boolean evaluate(int x, int y)
			{
				Position position = new Position(x, y, PosType.WALK);
				return map.isWalkable(position);
			}
		};
		
		return creator.create();
	}
	
	private static abstract class MapLayerCreator
	{
		private final int width, height;
		
		public MapLayerCreator(int width, int height)
		{
			this.width = width;
			this.height = height;
		}
		
		public Layer create()
		{
			final MatrixLayer layer = new MatrixLayer(new LayerDimension(width, height));
			
			LayerIterator iterator = new LayerIterator(layer)
			{
				@Override
				protected void nextCell(LayerPoint coordinates, int value)
				{
					int x = coordinates.getX();
					int y = coordinates.getY();

					int newValue = evaluate(x, y) ? 1 : 0;

					layer.set(coordinates, newValue);
				}
			};
			
			iterator.iterate();
			
			return layer;
		}
		
		protected abstract boolean evaluate(int x, int y);
	}
}
