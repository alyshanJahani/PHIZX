package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import engine.Particle;
import engine.World;

public class WorldPane extends JPanel implements MouseListener, MouseMotionListener
{
	
	private ArrayList<double[]> coordinatesOfTravel;
	private int paneWidth, paneHeight;
	private static World w;
	private double initialMouseX, initialMouseY;
	private double finalMouseX, finalMouseY;
	private double initialTime, finalTime;

	public WorldPane()
	{
		paneWidth = 1000;
		paneHeight = 600;


		setPreferredSize(new Dimension(paneWidth, paneHeight));
		setFocusable(true);


		// Create world and add a particle
		w = new World(paneWidth, paneHeight, 0.1);
		w.addParticle(480, 100);
		
		coordinatesOfTravel = new ArrayList<double[]>(0);


		addMouseListener(this);
		addMouseMotionListener(this);
	}

	public static World getWorld()
	{
		return w;
	}


	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.clearRect(0, 0, paneWidth, paneHeight);
		// SLOWS DOWN THE THREAD
		// Program runs far too fast to see changes otherwise
		try {
			Thread.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Draw everything in the world
		w.draw(g);
		repaint();
	}
	
	/**
	 * Spawns particles on right clicks
	 * @param e Mouse event
	 */
	public void mouseClicked(MouseEvent e)
	{
		boolean spawn = true;//this will help prevent alot of crashes that are caused when too many particles are spawned in the same location
		if(e.getButton() == MouseEvent.BUTTON3)//spawn on right click only
		{
			for(int i=0;i<w.getParticles().size();i++)
			{
				if(w.getParticles().get(i).notSafeSpawn(e.getX(), e.getY())==true)
				{
					spawn = false;
				}
			
			}
			if(spawn==true)
			{
			w.addParticle(e.getX(), e.getY());
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Selects particles so that they can be altered with using toolbox
	 * @param e
	 */
	public void mousePressed(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)//only want it to work with the left click
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				w.getParticles().get(i).setSelected(false);//so that only one particle can be selected at a time
				if (w.getParticles().get(i).contains(e.getX(), e.getY()))
				{
					w.getParticles().get(i).setSelected(true);
					w.getParticles().get(i).setDragged(true);	
				}
			}
		}
		
	}

	/**
	 * Determines the final trajectory of a particle after it has been dragged
	 * @param e Mouse event
	 */
	public void mouseReleased(MouseEvent e) 
	{
		double lastTime = System.currentTimeMillis();//will compare this time to the last time in the coordinates traveled array (to see if mouse was held down
													// in one position for some period of time after being dragged around)
		for (int i = 0; i < w.getParticles().size(); i++)
		{
			if (w.getParticles().get(i).isSelected()&&coordinatesOfTravel.size()>1)
			{
				//to determine the characteristics of the direction of travel
				boolean xIncrease;
				boolean yIncrease;
				//see the beginning direction of travel to compare rest of the points to.
				double x1 = coordinatesOfTravel.get(0)[0];
				double y1 = coordinatesOfTravel.get(0)[1];
				double x2 = coordinatesOfTravel.get(1)[0];
				double y2 = coordinatesOfTravel.get(1)[1];
				if(x2>x1)
				{
					xIncrease = true;
				}
				else
				{
					xIncrease = false;
				}
				if(y2>y1)
				{
					yIncrease = true;
				}
				else
				{
					yIncrease = false;
				}
				ArrayList<double[]> trajectoryLinePoints = new ArrayList<double[]>(0);
				double[] xyTime = new double[3];
				for(int j=1;j<coordinatesOfTravel.size()-1;j++)
				{
					//basically check if the next coordinate is following the same pattern of x/y increase, if so add that to the new arrayList that will contain
					//final trajectory.

					if(xIncrease==true&&yIncrease==true)
					{
						if((coordinatesOfTravel.get(j+1)[0]>coordinatesOfTravel.get(j)[0])&&(coordinatesOfTravel.get(j+1)[1]>(coordinatesOfTravel.get(j)[1])))
						{
							xyTime = coordinatesOfTravel.get(j);
							trajectoryLinePoints.add(xyTime);
						}
						else//If the next coordinate does not follow that pattern, establish the new pattern and clear the final trajectory arraylist(since clearly
							//the direction has changed so those coordinates were not part of the final trajectory)
						{
							if(coordinatesOfTravel.get(j+1)[0]<=coordinatesOfTravel.get(j)[0])
							{
								xIncrease = false;
							}
							if(coordinatesOfTravel.get(j+1)[1]<=coordinatesOfTravel.get(j)[1])
							{
								yIncrease = false;
							}
							trajectoryLinePoints.clear();
						}
					}
					else if(xIncrease==true&&yIncrease==false)
					{
						if((coordinatesOfTravel.get(j+1)[0]>coordinatesOfTravel.get(j)[0])&&(coordinatesOfTravel.get(j+1)[1]<=(coordinatesOfTravel.get(j)[1])))
						{
								xyTime = coordinatesOfTravel.get(j);
								trajectoryLinePoints.add(xyTime);
						}
						else
						{
							if(coordinatesOfTravel.get(j+1)[0]<=coordinatesOfTravel.get(j)[0])
							{
								xIncrease = false;
							}
							if(coordinatesOfTravel.get(j+1)[1]>coordinatesOfTravel.get(j)[1])
							{
								yIncrease = true;
							}
							trajectoryLinePoints.clear();
						}
					}
					else if(xIncrease==false&&yIncrease==false)
					{
						if((coordinatesOfTravel.get(j+1)[0]<=coordinatesOfTravel.get(j)[0])&&(coordinatesOfTravel.get(j+1)[1]<=(coordinatesOfTravel.get(j)[1])))
						{
								xyTime = coordinatesOfTravel.get(j);
								trajectoryLinePoints.add(xyTime);
						}
						else
						{
							if(coordinatesOfTravel.get(j+1)[0]>coordinatesOfTravel.get(j)[0])
							{
								xIncrease = true;
							}
							if(coordinatesOfTravel.get(j+1)[1]>coordinatesOfTravel.get(j)[1])
							{
								yIncrease = true;
							}
							trajectoryLinePoints.clear();
						}
					}
					else//x false y true
					{
						if((coordinatesOfTravel.get(j+1)[0]<=coordinatesOfTravel.get(j)[0])&&(coordinatesOfTravel.get(j+1)[1]>(coordinatesOfTravel.get(j)[1])))
						{
								xyTime = coordinatesOfTravel.get(j);
								trajectoryLinePoints.add(xyTime);
						}
						else
						{
							if(coordinatesOfTravel.get(j+1)[0]>coordinatesOfTravel.get(j)[0])
							{
								xIncrease = true;
							}
							if(coordinatesOfTravel.get(j+1)[1]<=coordinatesOfTravel.get(j)[1])
							{
								yIncrease = false;
							}
							trajectoryLinePoints.clear();
						}
					}
			}
				
				
				
				if (trajectoryLinePoints.size() != 0)
				{
					int lastPointIndex = trajectoryLinePoints.size()-1;
					//get the final slope to determine the velocity
					initialMouseX = trajectoryLinePoints.get(0)[0];
					initialMouseY = trajectoryLinePoints.get(0)[1];
					initialTime = trajectoryLinePoints.get(0)[2];
					finalMouseX = trajectoryLinePoints.get(lastPointIndex)[0];
					finalMouseY = trajectoryLinePoints.get(lastPointIndex)[1];
					finalTime = trajectoryLinePoints.get(lastPointIndex)[2];
					double time = finalTime-initialTime;
					if(time==0)//mouse was launched too quickly, initial time and final time were equal. cant have time equaling 0
					{
						time = 1;
					}
					if((lastTime-finalTime)<60)//mouse was released around the same time user was done dragging
					{
						double flingXVelocity = ((finalMouseX - initialMouseX)/(time))*10;
						double flingYVelocity = ((finalMouseY - initialMouseY)/(time))*10;
						w.getParticles().get(i).setDragged(false);
						w.getParticles().get(i).setXVel(flingXVelocity);
						w.getParticles().get(i).setYVel(flingYVelocity);
					}
					else//dragged the mouse, kept particle held in one position then released
					{
						w.getParticles().get(i).setDragged(false);//just drops the particle straight down
					}
				}
				else
				{
					w.getParticles().get(i).setDragged(false);
				}
			}
		}


	}

	@Override
	public void mouseDragged(MouseEvent e) 
	{
		for (int i = 0; i < w.getParticles().size(); i++)
		{
			if (w.getParticles().get(i).isDragged())
			{
				//get the coordinates the particle is being dragged through along with the time registered at those points
				double[] xyTime = {e.getX(),e.getY(),(double)System.currentTimeMillis()};
				coordinatesOfTravel.add(xyTime);
				w.getParticles().get(i).setXPos(e.getX() - (w.getParticles().get(i).getDiameter()/2));//centers the particle around the mouse
				w.getParticles().get(i).setYPos(e.getY() - (w.getParticles().get(i).getDiameter()/2));
				//particle is stationary when being dragged
				w.getParticles().get(i).setYVel(0);
				w.getParticles().get(i).setXVel(0);
				
			}
		}	
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		
	}
}
