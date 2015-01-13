package engine;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Box extends Rectangle
{
	public Box(int x, int y, int width, int height)
	{
		super(x, y, width, height);
	}
		
	public void draw(Graphics g)
	{
		g.setColor(new Color(205, 179, 128));
		g.fillRect(x, y, width, height);
	}
}
