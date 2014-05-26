package sk.hackcraft.bwu;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;

public class GraphicsOutputStream extends OutputStream
{
	private LinkedList<String> lines = new LinkedList<>();

	@Override
	public void write(int b) throws IOException
	{
		char c = (char) b;

		if (lines.isEmpty())
		{
			lines.add("");
		}

		if (c == '\n')
		{
			lines.addFirst("");
		}
		else
		{
			lines.set(0, lines.get(0) + ("" + c));
		}

		while (lines.size() > 100)
		{
			lines.removeFirst();
		}
	}

	public void drawTo(Graphics graphics)
	{
		boolean before = graphics.isGameCoordinates();
		graphics.setScreenCoordinates();

		int offset = 300;

		for (String line : lines)
		{
			graphics.drawText(new Vector2D(10, offset), line);

			offset -= 12;
		}

		graphics.setGameCoordinates(before);
	}
}
