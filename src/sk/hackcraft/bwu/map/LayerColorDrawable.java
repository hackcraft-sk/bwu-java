package sk.hackcraft.bwu.map;

import java.util.Map;

import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Vector2D;

public class LayerColorDrawable extends LayerDrawable
{
	private final ColorAssigner colorAssigner;
	
	public LayerColorDrawable(Layer layer, int cellDimension, ColorAssigner colorAssigner)
	{
		super(layer, cellDimension);
		
		this.colorAssigner = colorAssigner;
	}

	@Override
	protected void drawCell(Graphics graphics, Point cellPosition, Dimension cellDimension, int value)
	{
		Vector2D topLeftCorner = new Vector2D(cellPosition.getX() + 1, cellPosition.getY() + 1);
		Vector2D bottomRightCorner = new Vector2D(cellPosition.getX() + cellDimension.getWidth() - 1, cellPosition.getY() + cellDimension.getHeight() - 1);
		
		BWColor color = colorAssigner.assignColor(value);
		
		if (color != null)
		{
			graphics.setColor(color);
			graphics.drawBox(topLeftCorner, bottomRightCorner);
		}
	}
}
