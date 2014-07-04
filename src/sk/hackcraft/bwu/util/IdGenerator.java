package sk.hackcraft.bwu.util;

public class IdGenerator
{
	private int nextId = 0;
	
	public int generateId()
	{
		if (nextId == Integer.MAX_VALUE)
		{
			throw new IndexOutOfBoundsException("Id's are exhausted.");
		}
		
		return nextId++;
	}
}
