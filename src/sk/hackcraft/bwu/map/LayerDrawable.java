package sk.hackcraft.bwu.map;

import sk.hackcraft.bwu.Drawable;
import sk.hackcraft.bwu.Graphics;

public abstract class LayerDrawable implements Drawable
{
	private final Layer layer;
	private final Dimension cellDimension;
	
	public LayerDrawable(Layer layer, int cellDimension)
	{
		this.layer = layer;
		this.cellDimension = new Dimension(cellDimension, cellDimension);
	}
	
	@Override
	public void draw(Graphics graphics)
	{
		graphics.setGameCoordinates();
		
		Dimension dimension = layer.getDimension();
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				Point cellPosition = new Point(x, y);
				int value = layer.get(cellPosition);
				
				Point drawPosition = new Point(x * cellDimension.getWidth(), y * cellDimension.getHeight());
				
				drawCell(graphics, drawPosition, cellDimension, value);
			}
		}
	}
	
	protected abstract void drawCell(Graphics graphics, Point cellPosition, Dimension cellDimension, int value);
}
