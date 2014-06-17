package sk.hackcraft.bwu.maplayer.updaters;

import sk.hackcraft.bwu.maplayer.Layer;
import sk.hackcraft.bwu.maplayer.LayerIterator;
import sk.hackcraft.bwu.maplayer.LayerPoint;
import sk.hackcraft.bwu.maplayer.LayerProcessor;
import sk.hackcraft.bwu.maplayer.LayerUpdater;

public class TemperatureLayerUpdater implements LayerUpdater
{
	private final Layer layer;
	private final int temperatureDelta, frameSkip;
	private final int temperatureMin, temperatureMax;
	
	private int frame;
	
	public TemperatureLayerUpdater(Layer layer, int temperatureDelta, int frameSkip, int temperatureMin, int temperatureMax)
	{
		this.layer = layer;
		this.temperatureDelta = temperatureDelta;
		this.frameSkip = frameSkip;
		
		this.temperatureMin = temperatureMin;
		this.temperatureMax = temperatureMax;
	}
	
	@Override
	public void update()
	{
		if (frame % frameSkip == 0)
		{
			layer.createLayerIterator(new LayerIterator.IterateListener()
			{
				@Override
				public void nextCell(LayerPoint cellCoordinates, int cellValue)
				{
					int newValue = cellValue += temperatureDelta;
					
					if (newValue < temperatureMin)
					{
						newValue = temperatureMin;
					}
					
					if (newValue > temperatureMax)
					{
						newValue = temperatureMax;
					}
					
					layer.set(cellCoordinates, newValue);
				}
			}).iterateAll();
		}
		
		frame++;
	}
}
