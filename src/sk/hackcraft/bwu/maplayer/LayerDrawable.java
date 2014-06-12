package sk.hackcraft.bwu.maplayer;

import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Graphics;

public abstract class LayerDrawable implements Drawable
{
	private final Layer layer;
	private final LayerDimension cellDimension;
	
	private Bounds bounds;
	
	public LayerDrawable(Layer layer, int cellDimension)
	{
		this.layer = layer;
		this.cellDimension = new LayerDimension(cellDimension, cellDimension);

		this.bounds = new Bounds(LayerPoint.ORIGIN, layer.getDimension());
	}
	
	public void setBounds(Bounds bounds)
	{
		this.bounds = bounds;
	}
	
	@Override
	public void draw(Graphics graphics)
	{
		graphics.setGameCoordinates();
		
		LayerDimension dimension = layer.getDimension();
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				LayerPoint cellPosition = new LayerPoint(x, y);
				
				if (!bounds.isInside(cellPosition))
				{
					continue;
				}
				
				int value = layer.get(cellPosition);
				
				LayerPoint drawPosition = new LayerPoint(x * cellDimension.getWidth(), y * cellDimension.getHeight());
				
				drawCell(graphics, drawPosition, cellDimension, value);
			}
		}
	}
	
	protected abstract void drawCell(Graphics graphics, LayerPoint cellPosition, LayerDimension cellDimension, int value);
}
