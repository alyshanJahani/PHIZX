package ui;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
public class CustomButton extends Rectangle
{
	// Images for the button
	private BufferedImage icon, inactive, alternate;
	// Button states
	private boolean hovered, active, useAlternate;
	
	/**
	 * CustomButton Constructor
	 * @param x the x position of the button
	 * @param y the y position of the button
	 * @param img Default image of the button
	 */
	public CustomButton(int x, int y, BufferedImage img)
	{
		// Call the parent constructor, with the img width and height as the width/hight of the bounding Rectangle
		super(x, y, img.getWidth(), img.getHeight());
		// Assign image and default states
		icon = img;
		active = true;
		// Set other images and states as null/false
		inactive = null;
		alternate = null;
		useAlternate = false;
		hovered = false;
	}
	
	/**
	 * Sets the alternate image if the alternate state will be used
	 * @param alternate Alternate imagee
	 */
	public void setAlternate(BufferedImage alternate)
	{
		this.alternate = alternate;
	}
	
	/**
	 * Assign the hovered state of the button
	 * @param h true or false if hovered or not
	 */
	public void setHovered(boolean h)
	{
		hovered = h;
	}
	
	/**
	 * Assign the active(clickable) state of the button
	 * @param i true or false if the button is active or not
	 */
	public void setActive(boolean i)
	{
		active = i;
	}
	
	/**
	 * Indicate if the button should draw the alternate image
	 * @param b true or false if the alternate image should be used or not
	 */
	public void useAlternate(boolean b)
	{
		useAlternate = b;
	}
	
	/**
	 * State of the button
	 * @return true if the button is active and clickable, false otherwise
	 */
	public boolean isActive()
	{
		return active;
	}
	
	/**
	 * Set the image of the button when it is inactive/unclickable
	 * @param i Inactive image
	 */
	public void setInactiveIcon(BufferedImage i)
	{
		inactive = i;
	}
	
	/**
	 * Drawing method for the button containing all the possible draw options based on button state
	 * @param g Graphics object
	 */
	public void paint(Graphics g)
	{
		if (hovered && active && useAlternate)
		{
			g.drawRoundRect(super.x, super.y, icon.getWidth(), icon.getHeight(), 20, 20);
			g.drawImage(alternate, super.x, super.y, null);
		}
		else if (hovered && active)
		{
			g.drawRoundRect(super.x, super.y, icon.getWidth(), icon.getHeight(), 20, 20);
			g.drawImage(icon, super.x, super.y, null);
		}
		else if (!active && inactive != null)
		{
			g.drawImage(inactive, super.x, super.y, null);
		}
		else if (!active && inactive == null)
		{
			g.clearRect(0, 0, width, height);
		}
		else if (useAlternate)
		{
			g.drawImage(alternate, super.x, super.y, null);
		}
		else
		{
			g.drawImage(icon, super.x, super.y, null);
		}
	}
}