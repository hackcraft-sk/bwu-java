package sk.hackcraft.bwu;

import javabot.model.Map;

public class Graphics {
	public enum Color {
		RED(111), BLUE(165), TEAL(159), PURPLE(164), ORANGE(179),
		BROWN(19), WHITE(255), YELLOW(135), PINK(113), GREEN(117),
		CYAN(128), BLACK(0), GREY(74);
		
		private int value;
		
		private Color(int value) {
			this.value = value;
		}
	}
	
	final protected Bot bot;
	
	private boolean screen = false;
	private Color color = Color.WHITE;
	
	protected Graphics(Bot bot) {
		this.bot = bot;
	}
	
	public void drawBox(Vector2D topLeftCorner, Vector2D bottomRightCorner) {
		bot.BWAPI.drawBox(
				(int)Math.round(topLeftCorner.x), 
				(int)Math.round(topLeftCorner.y), 
				(int)Math.round(bottomRightCorner.x), 
				(int)Math.round(bottomRightCorner.y), 
				color.value, false, screen);
	}
	
	public void fillBox(Vector2D topLeftCorner, Vector2D bottomRightCorner) {
		bot.BWAPI.drawBox(
				(int)Math.round(topLeftCorner.x), 
				(int)Math.round(topLeftCorner.y), 
				(int)Math.round(bottomRightCorner.x), 
				(int)Math.round(bottomRightCorner.y), 
				color.value, true, screen);
	}
	
	public void drawCircle(Unit unit, int radius) {
		boolean savedScreen = isScreenCoordinates();
		setGameCoordinates(true);
		
		drawCircle(unit.getPosition(), radius);
		
		setScreenCoordinates(savedScreen);
	}
	
	public void drawCircle(Vector2D position, int radius) {
		bot.BWAPI.drawCircle((int)Math.round(position.x), (int)Math.round(position.y), radius, color.value, false, screen);
	}
	
	public void fillCircle(Unit unit, int radius) {
		boolean savedScreen = isScreenCoordinates();
		setGameCoordinates(true);
		
		fillCircle(unit.getPosition(), radius);
		
		setScreenCoordinates(savedScreen);
	}
	
	public void fillCircle(Vector2D position, int radius) {
		bot.BWAPI.drawCircle((int)Math.round(position.x), (int)Math.round(position.y), radius, color.value, true, screen);
	}
	
	public void drawDot(Vector2D position) {
		bot.BWAPI.drawDot((int)Math.round(position.x), (int)Math.round(position.y), color.value, screen);
	}
	
	public void drawDot(Unit unit) {
		boolean savedScreen = isScreenCoordinates();
		setGameCoordinates(true);
		
		drawDot(unit.getPosition());
		
		setScreenCoordinates(savedScreen);
	}
	
	public void drawLine(Vector2D from, Vector2D to) {
		bot.BWAPI.drawLine(
				(int)Math.round(from.x), 
				(int)Math.round(from.y), 
				(int)Math.round(to.x), 
				(int)Math.round(to.y), 
				color.value, screen);
	}
	
	public void drawText(Vector2D position, Object text) {
		bot.BWAPI.drawText((int)Math.round(position.x), (int)Math.round(position.y), text.toString(), screen);
	}
	
	public void drawText(Unit unit, Object text, Vector2D offset) {
		boolean savedScreen = isScreenCoordinates();
		setGameCoordinates(true);		
		
		drawText(offset.add(unit.getPosition()), text);
		
		setScreenCoordinates(savedScreen);
	}
	
	public void drawText(Unit unit, Object text) {
		drawText(unit, text, Vector2D.ZERO);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public void setScreenCoordinates(boolean screen) {
		this.screen = screen;
	}
	
	public void setScreenCoordinates() {
		setScreenCoordinates(true);
	}
	
	public boolean isScreenCoordinates() {
		return this.screen;
	}
	
	public void setGameCoordinates(boolean game) {
		this.screen = !game;
	}
	
	public void setGameCoordinates() {
		setGameCoordinates(true);
	}
	
	public boolean isGameCoordinates() {
		return !screen;
	}
	
	public Minimap createMinimap(Map map, Vector2D position, Vector2D size) {
		return new Minimap(this, map, position, size);
	}
}
