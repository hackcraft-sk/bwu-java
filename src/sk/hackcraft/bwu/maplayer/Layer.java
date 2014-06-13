package sk.hackcraft.bwu.maplayer;

import java.util.Iterator;
import java.util.Map;

import sk.hackcraft.bwu.maplayer.LayerIterator.IterateListener;

public interface Layer
{
	LayerDimension getDimension();
	
	boolean isValid(LayerPoint point);
	
	int get(int x, int y);
	int get(LayerPoint point);
	
	void set(int x, int y, int value);
	void set(LayerPoint point, int value);
	
	LayerIterator createLayerIterator(IterateListener listener);
	
	Layer add(Layer layer);
	Layer substract(Layer layer);
	Layer multiply(Layer layer);
	Layer divide(Layer layer);
}
