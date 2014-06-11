package sk.hackcraft.bwu.layer;

import java.util.HashMap;
import java.util.Map;

import jnibwapi.JNIBWAPI;
import jnibwapi.Position;
import jnibwapi.util.BWColor;
import sk.hackcraft.bwu.Graphics;
import sk.hackcraft.bwu.Vector2D;

public class LayerColorDrawable extends LayerDrawable
{
	public static LayerColorDrawable createBoolean(Layer layer, int cellDimension)
	{
		HashMap<Integer, BWColor> colors = new HashMap<>();
		colors.put(0, BWColor.Black);
		colors.put(0, BWColor.White);
		ColorAssigner<BWColor> colorAssigner = new MapExactColorAssigner<>(colors);
		
		return new LayerColorDrawable(layer, cellDimension, colorAssigner);
	}
	
	private final ColorAssigner<BWColor> colorAssigner;

	public LayerColorDrawable(Layer layer, int cellDimension, ColorAssigner<BWColor> colorAssigner)
	{
		super(layer, cellDimension);
		
		this.colorAssigner = colorAssigner;
	}

	@Override
	protected void drawCell(Graphics graphics, LayerPoint cellPosition, LayerDimension cellDimension, int value)
	{
		Vector2D topLeftCorner = new Vector2D(cellPosition.getX() + 1, cellPosition.getY() + 1);
		Vector2D bottomRightCorner = new Vector2D(cellPosition.getX() + cellDimension.getWidth() - 1, cellPosition.getY() + cellDimension.getHeight() - 1);
		
		BWColor color = colorAssigner.assignColor(value);
		
		if (color != null)
		{
			graphics.setColor(color);
			graphics.drawBox(topLeftCorner, bottomRightCorner);
		}
		
		// if this is active, strange things are happening and bot is not working
		//JNIBWAPI.getInstance().drawText(new Position(cellPosition.getX() + 10, cellPosition.getY() + 10), Integer.toString(value), false);
	}
}
