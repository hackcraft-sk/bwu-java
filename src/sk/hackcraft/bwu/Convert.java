package sk.hackcraft.bwu;

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
}
