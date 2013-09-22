package sk.hackcraft.bwu.sample.scmai3;

import java.util.LinkedList;
import java.util.List;

import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.util.RouteFinder;

public class MQ3RouteFinder extends RouteFinder {
	static private List<Vector2D> getVertices() {
		List<Vector2D> list = new LinkedList<>();
		
		list.add(new Vector2D(564, 360)); 	// 0
		list.add(new Vector2D(3514, 352));	// 1
		
		list.add(new Vector2D(1292, 781));	// 2
		list.add(new Vector2D(2008, 979));	// 3
		list.add(new Vector2D(2761, 830));	// 4
		
		list.add(new Vector2D(448, 1516));	// 5
		list.add(new Vector2D(1133, 1551));	// 6
		list.add(new Vector2D(2046, 1502)); // 7
		list.add(new Vector2D(2838, 1485)); // 8
		list.add(new Vector2D(3685, 1540)); // 9
		
		list.add(new Vector2D(1298, 2222)); // 10
		list.add(new Vector2D(2052, 2079)); // 11
		list.add(new Vector2D(2734, 2228)); // 12
		
		list.add(new Vector2D(572, 2678)); // 13
		list.add(new Vector2D(3520, 2668)); // 14
		
		return list;
	}
	
	public MQ3RouteFinder() {
		super(getVertices());
		
		// top horizontal
		connectBoth(0, 2);
		connectBoth(2, 3);
		connectBoth(3, 4);
		connectBoth(4, 1);
		
		// top vertical
		connectBoth(2, 6);
		connectBoth(3, 7);
		connectBoth(4, 8);
		
		// center horizontal
		connectBoth(5, 6);
		connectBoth(6, 7);
		connectBoth(7, 8);
		connectBoth(8, 9);
		
		// bottom vertical
		connectBoth(6, 10);
		connectBoth(7, 11);
		connectBoth(8, 12);
		
		// bottom horizontal
		connectBoth(13, 10);
		connectBoth(10, 11);
		connectBoth(11, 12);
		connectBoth(12, 14);
	}
}
