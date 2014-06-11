package sk.hackcraft.bwu;

import sk.hackcraft.bwu.layer.LayerDimension;
import sk.hackcraft.bwu.layer.LayerPoint;
import jnibwapi.Position;
import jnibwapi.Position.PosType;

public class Convert
{
	public static Vector2D toPositionVector(Position p)
	{
		return Vector2DMath.toVector(p, PosType.PIXEL);
	}
	
	public static Position toPosition(Vector2D vector)
	{
		int x = Math.round(vector.getX());
		int y = Math.round(vector.getY());
		
		return new Position(x, y);		
	}
	
	public static LayerPoint toLayerPoint(Position position)
	{
		int x = position.getBX();
		int y = position.getBY();
		
		return new LayerPoint(x, y);
	}
	
	public static LayerDimension toLayerDimension(Position position)
	{
		int width = position.getBX();
		int height = position.getBY();
		
		return new LayerDimension(width, height);
	}
}
