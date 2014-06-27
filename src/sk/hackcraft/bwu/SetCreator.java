package sk.hackcraft.bwu;

import java.util.HashSet;
import java.util.Set;

public class SetCreator
{
	@SafeVarargs
	public static <T> Set<T> create(T... objects)
	{
		Set<T> set = new HashSet<>();
		
		for (T object : objects)
		{
			set.add(object);
		}
		
		return set;
	}
}
