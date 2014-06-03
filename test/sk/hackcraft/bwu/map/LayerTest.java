package sk.hackcraft.bwu.map;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

public class LayerTest
{
	@Test
	public void testCreateBlank()
	{
		Layer layer = new Layer(10, 5);
		
		assertEquals(10, layer.getWidth());
		assertEquals(5, layer.getHeight());
	}
	
	@Test
	public void testCreateCopy()
	{
		Layer layer = new Layer(3, 2);
		
		Layer layer2 = new Layer(layer);
		
		assertEquals(3, layer2.getWidth());
		assertEquals(2, layer2.getHeight());
	}
	
	@Test
	public void testCreatePartialCopy()
	{
		Layer layer = new Layer(3, 4);
		layer.set(0,0, 1);
		layer.set(1,1, 2);
		layer.set(2,2, 3);
		layer.set(2, 3, 4);
		
		Layer layer2 = new Layer(layer, 1, 2, 1, 2);
		
		assertEquals(2, layer2.getWidth());
		assertEquals(2, layer2.getHeight());
		
		assertEquals(2, layer2.get(0, 0));
		assertEquals(3, layer2.get(1, 1));
	}
}
