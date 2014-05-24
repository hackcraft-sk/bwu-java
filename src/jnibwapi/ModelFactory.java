package jnibwapi;

public interface ModelFactory {
	public Unit createUnit(int id, JNIBWAPI jnibwapi);
	public Player createPlayer(int[] data, int index, String name);
	public Position createPositionFromPixel(int x, int y);
	public Position createPositionFromTile(int x, int y);
}
