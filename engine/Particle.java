package engine;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

public class Particle 
{
	private double xPos, yPos;
	private double xVel, yVel;
	private double xAcc, yAcc, gravity, groundFriction;
	private double mass, diameter;
	private double restitutionCoeff;
	private boolean selected, grounded, dragged;

	private Color c;

	public Particle(double xPos, double yPos)
	{ 
		this.xPos = xPos;
		this.yPos = yPos;
		xVel = 0;
		yVel = 0;
		xAcc = 0;
		yAcc = 0;

		diameter = 40;
		gravity = 0.1;
		groundFriction = 0.01;
		mass = 1;

		restitutionCoeff = 0.6;

		selected = false;
		grounded = false;
		dragged = false;

		c = chooseRandomColor();
	}
	public double getFriction()
	{
		return groundFriction;
	}

	public void setFriction(double f)
	{
		groundFriction = f;
	}
	public void setGravity(double g)
	{
		gravity = g;
	}
	public boolean isSelected()
	{
		return selected;
	}

	public void setSelected(boolean s)
	{
		selected = s;
	}

	public boolean isGrounded()
	{
		return grounded;
	}

	public void setGrounded(boolean b)
	{
		grounded = b;
	}

	public double getRestitutionCoeff()
	{
		return restitutionCoeff;
	}

	public void setRestitutionCoeff(double c)
	{
		restitutionCoeff = c;
	}

	public double getXPos()
	{
		return xPos;
	}

	public void setXPos(double xPos)
	{
		this.xPos = xPos;
	}

	public double getYPos()
	{
		return yPos;
	}

	public void setYPos(double yPos)
	{
		this.yPos = yPos;
	}

	public double getXVel()
	{
		return xVel;
	}

	public void setXVel(double xVel)
	{
		this.xVel = xVel;
	}

	public double getYVel()
	{
		return yVel;
	}

	public void setYVel(double yVel)
	{
		this.yVel = yVel;
	}

	public double getXAcc()
	{
		return xAcc;
	}

	public void setXAcc(double xAcc)
	{
		this.xAcc = xAcc;
	}

	public double getYAcc()
	{
		return yAcc;
	}

	public void setYAcc(double yAcc)
	{
		this.yAcc = yAcc;
	}

	public double getDiameter()
	{
		return diameter;
	}

	public void setMass(double mass)
	{
		this.mass = mass;
	}

	public double getMass() 
	{
		return mass;
	}
	public void setDragged(boolean s)
	{
		dragged = s;
	}
	public boolean isDragged()
	{
		return dragged;
	}
	public void setDiameter(double diameter)
	{
		this.diameter = diameter;
	}


	public boolean contains(double x, double y)
	{
		if (x >= xPos && x <= xPos + diameter &&
				y >= yPos && y <= yPos + diameter)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Checks if point of spawn is safe (not in the way of other particles)
	 * @param x X coordinate of spawn
	 * @param y Y coordinate of spawn
	 * @return true if spawn is NOT safe, false if spawn is safe
	 */
	public boolean notSafeSpawn(double x, double y)
	{
		if (this.contains(x, y))//point of spawn is inside the particle
		{
			return true;
		}
		else if((x>=(xPos-40)&&x<=(xPos+diameter))&&(y>=(yPos-40)&&y<=(yPos+diameter)))//point of spawn would create a particle with some area inside the other particle
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Checks to see if the inner rectangles of two particles are intersecting
	 * @param p Particle
	 * @return true if inner rectangle of one particle intersects the inner rectangle of another particle
	 */
	public boolean contains(Particle p)
	{
		Rectangle rA = this.getInnerRectangle();
		Rectangle rB = p.getInnerRectangle();
		if(rA.intersects(rB))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	/**
	 * Finds the rectangle of maximum area inscribed in the particle
	 * @return the inner rectangle of maximum area inscribed in the particle
	 */
	public Rectangle getInnerRectangle()
	{
		//the rectangle with maximum area is a square, the diagonal forms a right angle triangle with the two side lengths, at an angle of 45 degrees.
		int sideLength = (int)(Math.sin(Math.PI/4)*diameter);
		/*the X and Y position of the particle represent the upper left corner of the rectangle in which the circle is drawn in.
		from this the x position of the square inside the particle would be the x position + (the length of the outer rectangle (diameter) - side length of
		the square divided by 2). Draw a square inside a circle inside a rectangle for a visual aid
		 */
		Rectangle r = new Rectangle ((int)(xPos + (diameter - sideLength)/2),(int)(yPos + (diameter - sideLength)/2),sideLength,sideLength);
		return r;
	}
	public boolean intersects(Box b)
	{
		if (yPos + diameter >= b.y)
		{
			return true;
		}
		else 
		{
			return false;
		}
	}

	/**
	 * Checks to see if particles are intersecting
	 * @param p Particle
	 * @return true if particles are intersecting, false if particles are not intersecting
	 */
	public boolean intersects(Particle p)
	{
		Rectangle rA = this.getInnerRectangle();
		Rectangle rB = p.getInnerRectangle();
		//center point of the particle is the center point of the inner rectangle
		double x1 = rA.x + (rA.width/2);//half way horizontally
		double y1 = rA.y + (rA.height/2);//half way vertically 
		double x2 = rB.x + (rB.width/2);
		double y2 = rB.y + (rB.height/2);
		//compares the distance from the center point of the particles, to the closest distance possible(the two radiuses of the circles)
		if ((Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2) < Math.pow((diameter/2 ) + (p.getDiameter()/2),2)))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	

	// Currently only moves in the y direction
	private void move()
	{
		yPos += yVel;
		yVel += (yAcc + gravity) * mass;
		if (yVel > -0.1 && yVel < 0.1)
		{
			yVel = 0;
		}


	//	xVel += xAcc * mass; never assign value to xAcc anywhere in program... its not doing anything

		xPos += xVel;
		if (xVel > 0 && grounded)
		{
			xVel -= groundFriction;
		}
		else if (xVel < 0 && grounded)
		{
			xVel += groundFriction;
		}

	}


	public void draw(Graphics g)
	{
		int sideLength = (int)(Math.sin(Math.PI/4)*diameter);
		g.setColor(c);
		g.fillOval((int)xPos,(int) yPos, (int)diameter,(int) diameter);
		g.setColor(Color.BLACK);
		//g.drawRect((int)(xPos + (diameter - sideLength)/2),(int)(yPos + (diameter - sideLength)/2),sideLength,sideLength);
		if (selected)
		{
			g.setColor(Color.RED);
			g.drawOval((int)xPos, (int)yPos, (int)diameter, (int)diameter);
		}
		if (!dragged)
			move();
	}

	private Color chooseRandomColor()
	{
		Random r = new Random();
		int color = r.nextInt(3);
		switch (color)
		{
		case 0:
			return new Color(3, 101, 100);
		case 1:
			return new Color(3, 54, 73);
		case 2:
			return new Color(3, 22, 52);
		default:
			return null;
		}
	}
}