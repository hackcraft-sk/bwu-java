package sk.hackcraft.bwu.sample.scmai4;

import sk.hackcraft.bwu.Vector2D;
import sk.hackcraft.bwu.util.VectorGraph;

public class MQ4VectorGraph extends VectorGraph {
	public MQ4VectorGraph() {
		super(new Vector2D[]{
			new Vector2D(564, 360), 	// 0
			new Vector2D(3514, 352),	// 1
			
			new Vector2D(1292, 781),	// 2
			new Vector2D(2008, 979),	// 3
			new Vector2D(2761, 830),	// 4
			
			new Vector2D(448, 1516),	// 5
			new Vector2D(1133, 1551),	// 6
			new Vector2D(2046, 1502), // 7
			new Vector2D(2838, 1485), // 8
			new Vector2D(3685, 1540), // 9
			
			new Vector2D(1298, 2222), // 10
			new Vector2D(2052, 2079), // 11
			new Vector2D(2734, 2228), // 12
			
			new Vector2D(572, 2678), // 13
			new Vector2D(3520, 2668) // 14
		});
		
		// top horizontal
		connectBothWays(0, 2);
		connectBothWays(2, 3);
		connectBothWays(3, 4);
		connectBothWays(4, 1);
		
		// top vertical
		connectBothWays(2, 6);
		connectBothWays(3, 7);
		connectBothWays(4, 8);
		
		// center horizontal
		connectBothWays(5, 6);
		connectBothWays(6, 7);
		connectBothWays(7, 8);
		connectBothWays(8, 9);
		
		// bottom vertical
		connectBothWays(6, 10);
		connectBothWays(7, 11);
		connectBothWays(8, 12);
		
		// bottom horizontal
		connectBothWays(13, 10);
		connectBothWays(10, 11);
		connectBothWays(11, 12);
		connectBothWays(12, 14);
		
		/*connectBothWays(0, 5);
		connectBothWays(5, 13);
		connectBothWays(1, 9);
		connectBothWays(9, 14);*/
	}
}
