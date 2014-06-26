package sk.hackcraft.creep;

import java.util.HashSet;
import java.util.Set;

import sk.hackcraft.bwu.maplayer.GradientColorAssigner;
import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerDimension;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.LayerUtil;
import sk.hackcraft.bwu.maplayer.MatrixLayer;
import sk.hackcraft.bwu.maplayer.processors.GradientFloodFillProcessor;
import sk.hackcraft.bwu.maplayer.visualization.LayersPainter;
import sk.hackcraft.bwu.maplayer.visualization.SwingLayersVisualization;

public class FloodFillTest
{
	public static void main(String[] args)
	{
		LayerDimension dimension = new LayerDimension(256, 256);
		LayersPainter lp = new LayersPainter(dimension);
		SwingLayersVisualization v = new SwingLayersVisualization(lp);
		
		v.start();
		
		Layer l = new MatrixLayer(dimension);
		lp.addLayer(l, new GradientColorAssigner(0, 40));

		int x = 10;
		int y = 0;
		while (true)
		{
			{
				Set<LayerPoint> startPoints = new HashSet<>();
				startPoints.add(new LayerPoint(x, y));
				GradientFloodFillProcessor.floodDefill(l, 0, startPoints);
			}
			
			x = (x + 1) % 256;
			y = (y + 1) % 256;
			
			
			{
				Set<LayerPoint> startPoints = new HashSet<>();
				startPoints.add(new LayerPoint(x, y));
				GradientFloodFillProcessor.floodFill(l, 40, startPoints);
			}
			
			lp.update();
			
			
			
			/*try
			{
				Thread.sleep(10);
			}
			catch (InterruptedException e)
			{
			}*/
		}
	}
}
