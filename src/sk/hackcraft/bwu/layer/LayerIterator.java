package sk.hackcraft.bwu.layer;

public abstract class LayerIterator
{
	private final Layer layer;

	public LayerIterator(Layer layer)
	{
		this.layer = layer;
	}

	public void iterate()
	{
		LayerDimension dimension = layer.getDimension();
		for (int x = 0; x < dimension.getWidth(); x++)
		{
			for (int y = 0; y < dimension.getHeight(); y++)
			{
				LayerPoint coordinates = new LayerPoint(x, y);
				int value = layer.get(coordinates);
				
				nextCell(coordinates, value);
			}
		}
	}
	
	protected abstract void nextCell(LayerPoint coordinates, int actualValue);
}
