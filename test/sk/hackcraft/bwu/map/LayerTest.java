package sk.hackcraft.bwu.map;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerUtil;
import sk.hackcraft.bwu.maplayer.layers.MatrixLayer;

public class LayerTest
{
	@Test
	public void testCreateBlank()
	{
		MatrixLayer layer = new MatrixLayer(new LayerDimension(10, 5));
		
		assertEquals(10, layer.getDimension().getWidth());
		assertEquals(5, layer.getDimension().getHeight());
	}
	
	@Test
	public void testCreateCopy()
	{
		MatrixLayer layer = new MatrixLayer(new LayerDimension(3, 2));
		
		MatrixLayer layer2 = new MatrixLayer(layer.getDimension());
		LayerUtil.copy(layer, layer2);
		
		assertEquals(3, layer2.getDimension().getWidth());
		assertEquals(2, layer2.getDimension().getHeight());
	}
	
	@Test
	public void testCreatePartialCopy()
	{
		MatrixLayer layer = new MatrixLayer(new LayerDimension(3, 4));
		layer.set(0,0, 1);
		layer.set(1,1, 2);
		layer.set(2,2, 3);
		layer.set(2, 3, 4);
		
		MatrixLayer layer2 = new MatrixLayer(new LayerDimension(2, 2));
		LayerUtil.copy(layer, layer2, 1, 2, 1, 2);
		
		assertEquals(2, layer2.getDimension().getWidth());
		assertEquals(2, layer2.getDimension().getHeight());
		
		assertEquals(2, layer2.get(0, 0));
		assertEquals(3, layer2.get(1, 1));
	}
}
