package engine;

public class Line
{
	double x1;
	double y1;
	double x2;
	double y2;
	double constant;
	//components of normal vector to the line, using scalar equation so i dont have to worry about undefined slope
	double nx;
	double ny;
	
	public Line(double x1, double y1, double x2, double y2)
	{
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
		//im sure you can follow this
		nx = -1*(y2-y1);
		ny = (x2-x1);
		constant = (-1*(nx*x1)-(ny*y1));
	}
	public boolean contains(double x, double y)
	{
		System.out.println(nx+" "+ x + " " + ny + " " + y + " " + constant);
		if((nx*(x)+ny*(y)+constant)==0)//if the equation is satisfied
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	

	

}
