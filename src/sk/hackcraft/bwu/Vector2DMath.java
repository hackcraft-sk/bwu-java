package sk.hackcraft.bwu;

import jnibwapi.Position;
import jnibwapi.Position.PosType;

public class Vector2DMath
{
	public static Vector2D toVector(Position position, PosType posType)
	{
		int x = position.getX(posType);
		int y = position.getY(posType);
		
		return new Vector2D(x, y);
	}
	
	public static Position toPosition(Vector2D vector, PosType posType)
	{
		int x = (int)vector.getX();
		int y = (int)vector.getY();
		
		return new Position(x, y, posType);
	}
	
	public static Vector2D add(Vector2D leftVector, Vector2D rightVector)
	{
		float x = leftVector.getX() + rightVector.getX();
		float y = leftVector.getY() + rightVector.getY();
		
		return new Vector2D(x, y);
	}
	
	public static Vector2D sub(Vector2D leftVector, Vector2D rightVector)
	{
		float x = leftVector.getX() - rightVector.getX();
		float y = leftVector.getY() - rightVector.getY();
		
		return new Vector2D(x, y);
	}
	
	public static Vector2D scale(Vector2D vector, float scale)
	{
		return scale(vector, scale, scale);
	}
	
	public static Vector2D scale(Vector2D vector, float scaleX, float scaleY)
	{
		float x = vector.getX() * scaleX;
		float y = vector.getY() * scaleY;
		
		return new Vector2D(x, y);
	}
	
	public static Vector2D scale(Vector2D vector, Vector2D scalingVector)
	{
		return scale(vector, scalingVector.getX(), scalingVector.getY());
	}
	
	public static Vector2D normalize(Vector2D vector)
	{
		float x = vector.getX() / vector.getLength();
		float y = vector.getY() / vector.getLength();
		
		return new Vector2D(x, y);
	}
	
	public static Vector2D invert(Vector2D vector)
	{
		float x = -vector.getX();
		float y = -vector.getY();
		
		return new Vector2D(x, y);
	}
	
	public static Vector2D[] getOrthogonal(Vector2D vector)
	{
		float x = vector.getX();
		float y = vector.getY();
		
		return new Vector2D[] { new Vector2D(-y, x), new Vector2D(y, -x) };
	}
}
