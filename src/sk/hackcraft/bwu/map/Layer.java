package sk.hackcraft.bwu.map;

import java.util.Iterator;
import java.util.Map;

public interface Layer
{
	Dimension getDimension();
	
	boolean isValid(Point point);
	
	@Deprecated
	int get(int x, int y);
	int get(Point point);
	
	@Deprecated
	void set(int x, int y, int value);
	void set(Point point, int value);
	
	Layer add(Layer layer);
	Layer substract(Layer layer);
	Layer multiply(Layer layer);
	Layer divide(Layer layer);
}
