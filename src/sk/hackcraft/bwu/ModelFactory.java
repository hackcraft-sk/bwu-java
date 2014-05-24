package sk.hackcraft.bwu;

import jnibwapi.JNIBWAPI;

public class ModelFactory implements jnibwapi.ModelFactory {
	private Game game = null;

	protected void setGame(Game game) {
		this.game = game;
	}
	
	@Override
	public jnibwapi.Unit createUnit(int id, JNIBWAPI jnibwapi) {
		return new Unit(game, id, jnibwapi);
	}

	@Override
	public jnibwapi.Player createPlayer(int[] data, int index, String name) {
		return new jnibwapi.Player(data, index, name);
	}
}
