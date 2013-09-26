package sk.hackcraft.bwu;

import sk.hackcraft.bwu.Graphics.Color;
import javabot.model.Map;

public class Minimap {
	private Vector2D position;
	private Vector2D scaler;
	private Graphics graphics;
	private Map map;
	
	protected Minimap(Graphics graphics, Map map, Vector2D position, Vector2D size) {
		this.position = position;
		this.graphics = graphics.createSubGraphics();
		this.graphics.setGameCoordinates(true);
		this.map = map;
		
		this.scaler = new Vector2D(
				size.x / (map.getWidth()*Game.TILE_SIZE),
				size.y / (map.getHeight()*Game.TILE_SIZE)
			);
	}

	public void drawBox(Vector2D topLeftCorner, Vector2D bottomRightCorner) {
		graphics.drawBox(topLeftCorner.scale(scaler).add(position), bottomRightCorner.scale(scaler).add(position));
	}
	
	public void fillBox(Vector2D topLeftCorner, Vector2D bottomRightCorner) {
		graphics.fillBox(topLeftCorner.scale(scaler).add(position), bottomRightCorner.scale(scaler).add(position));
	}
	
	public void drawCircle(Unit unit, int radius) {
		drawCircle(unit.getPosition(), radius);
	}
	
	public void drawCircle(Vector2D position, int radius) {
		graphics.drawCircle(position.scale(scaler).add(this.position), radius);
	}
	
	public void fillCircle(Unit unit, int radius) {
		fillCircle(unit.getPosition(), radius);
	}
	
	public void fillCircle(Vector2D position, int radius) {
		graphics.fillCircle(position.scale(scaler).add(this.position), radius);
	}
	
	public void drawDot(Vector2D position) {
		graphics.drawDot(position.scale(scaler).add(this.position));
	}
	
	public void drawDot(Unit unit) {
		drawDot(unit.getPosition());
	}
	
	public void drawLine(Vector2D from, Vector2D to) {
		graphics.drawLine(from.scale(scaler).add(this.position), to.scale(scaler).add(this.position));
	}
	
	public void drawText(Vector2D position, Object text) {
		graphics.drawText(position.scale(scaler).add(this.position), text);
	}
	
	public void drawText(Unit unit, Object text, Vector2D offset) {
		drawText(offset.add(unit.getPosition()), text);
	}
	
	public void drawText(Unit unit, Object text) {
		drawText(unit, text, Vector2D.ZERO);
	}
	
	public void setColor(Color color) {
		graphics.setColor(color);
	}
	
	public void drawBounds() {
		drawBox(Vector2D.ZERO, new Vector2D(map.getWidth()*Game.TILE_SIZE, map.getHeight()*Game.TILE_SIZE));
	}
}