package sk.hackcraft.bwu.map;

public abstract class LayerIterator
{
	private final Layer layer;

	public LayerIterator(Layer layer)
	{
		this.layer = layer;
	}

	public void iterate()
	{
		Dimension dimension = layer.getDimension();
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				Point coordinates = new Point(x, y);
				int value = layer.get(coordinates);
				
				nextCell(coordinates, value);
			}
		}
	}
	
	protected abstract void nextCell(Point coordinates, int value);
}
