package engine;

import java.awt.Graphics;
import java.util.ArrayList;


public class World
{
	private ArrayList<Particle> particles;
	private Box ground, leftWall, rightWall;
	private double gravity;
	private int worldWidth, worldHeight;

	public World(int w, int h, double gravity)
	{
		worldWidth = w;
		worldHeight = h;

		this.gravity = gravity;

		// world's ground
		ground = new Box(0, worldHeight - 80, worldWidth, 80);
		leftWall = new Box(-1, 0, 1, worldHeight);
		rightWall = new Box(worldWidth, 0, 1, worldHeight);
		// empty particle list;
		particles = new ArrayList<Particle>(0);
	}


	public void addParticle(double xPos, double yPos)
	{
		Particle p = new Particle(xPos, yPos);
		// Assign the yAcceleration of the particle the world's gravity
		//p.setYAcc(gravity);
		p.setMass(1);
		p.setGravity(gravity);
		particles.add(p);
	}


	/**
	 * Checks for collisions between all particles present on the screen and applies the correct final velocities according
	 * to the conservation of momentum, and conservation of energy.
	 */
	public void checkParticleParticleCollisions()
	{
		for (int i = 0; i < particles.size(); i++)
		{
			for (int j = i+1; j < particles.size(); j++)//compare every particle to every other particle
			{
				Particle pA = particles.get(i);
				Particle pB = particles.get(j);

				if (pA.intersects(pB)&&pA.isDragged()==false&&pB.isDragged()==false)
				{
					double pAXVel = pA.getXVel();
					double pBXVel = pB.getXVel();
					double pAYVel = pA.getYVel();
					double pBYVel = pB.getYVel();
					//many of the following equations are set up using the conservation of momentum and conservation of energy equations.
					//constants just to make calculations less complicated.
					double momentumConstantX = pA.getMass()*pAXVel + pB.getMass()*pBXVel;
					double initialEnergyConstantX = pA.getMass()*pAXVel*pAXVel + pB.getMass()*pBXVel*pBXVel;
					double constantZX = ((pA.getMass()*initialEnergyConstantX)-momentumConstantX*momentumConstantX)/pB.getMass();
					//when the energy equation is set up to be factored, it is a quadratic in the form ax^2 + bx + c, these are the coefficients:
					double coeffA = pA.getMass() + pB.getMass();
					double coeffB = -2*momentumConstantX;
					double coeffC = -1*constantZX;
					double pBFinalVelocityX = quadraticFormulaX(coeffA, coeffB, coeffC, pA.getMass(),pB.getMass(),pAXVel,pBXVel);//get the root(answer) of the quadratic equation
					double pAFinalVelocityX = (momentumConstantX-pB.getMass()*pBFinalVelocityX)/pA.getMass();
					pA.setXVel(pAFinalVelocityX);
					pB.setXVel(pBFinalVelocityX);
					//when applying y velocities, some adjustments are made to account for things like a particle dropping on the smooth edge of the other particle
					//A simplification will be made: if a particle lands on the edge of a grounded particle, it will bounce slightly to the left or right
					if(pA.isGrounded()==true)
					{
						double xPosDifference = pA.getXPos()-pB.getXPos();
						if(pA.getXVel()==0&&pB.getXVel()==0&&xPosDifference!=0)//pB is spawned above pA slightly left or right of it
						{
							if(xPosDifference>0)//pA is right of pB
							{
								pB.setXVel(-0.5);
								pB.setYVel(-pB.getYVel());
								pA.setXVel(0.5);
							}
							else//pA is left of pB
							{
								pB.setXVel(0.5);
								pB.setYVel(-pB.getYVel());
								pA.setXVel(-0.5);
							}
						}
						else//directly above each other
						{
							pB.setYVel(-pB.getYVel()+((pB.getYAcc() + gravity) * pB.getMass()));
						}
					}
					else if(pB.isGrounded()==true)
					{
						double xPosDifference = pB.getXPos()-pA.getXPos();
						if(pA.getXVel()==0&&pB.getXVel()==0&&xPosDifference!=0)//pA is spawned above pB slightly left or right of it
						{
							if(xPosDifference>0)//pB is right of pA
							{
								pA.setXVel(-0.5);
								pA.setYVel(-pA.getYVel());
								pB.setXVel(0.5);
							}
							else//pB is left of pA
							{
								pA.setXVel(0.5);
								pA.setYVel(-pA.getYVel());
								pB.setXVel(-0.5);
							}
						}
						else//directly above
						{
							pA.setYVel(-pA.getYVel()+((pA.getYAcc() + gravity) * pA.getMass()));
						}

					}
					else//apply normal velocities by conservation of momentum and energy
					{
						double momentumConstantY = pA.getMass()*pA.getYVel() + pB.getMass()*pB.getYVel();
						double initialEnergyConstantY = pA.getMass()*pA.getYVel()*pA.getYVel() + pB.getMass()*pB.getYVel()*pB.getYVel();
						double constantZY = ((pA.getMass()*initialEnergyConstantY)-momentumConstantY*momentumConstantY)/pB.getMass();
						//when the energy equation is set up to be factored, it is a quadratic in the form ax^2 + bx + c, these are the coefficients:
						double coeffAY = pA.getMass() + pB.getMass();
						double coeffBY = -2*momentumConstantY;
						double coeffCY = -1*constantZY;
						double pBFinalVelocityY = quadraticFormulaY(coeffAY, coeffBY, coeffCY, pA.getMass(),pB.getMass(),pA.getYVel(),pB.getYVel()); //get the roots(answers) of the quadratic equation
						double pAFinalVelocityY = (momentumConstantY-pB.getMass()*pBFinalVelocityY)/pA.getMass();
						pA.setYVel(pAFinalVelocityY);
						pB.setYVel(pBFinalVelocityY);
					}
				}



			}
		}
	}
	/**
	 * returns the appropriate root of a quadratic equation in the form ax^2 + bx + c
	 * @param a coefficient a
	 * @param b coefficient b
	 * @param c coefficient c
	 * @param pAMass mass of particle A
	 * @param pBMass mass of particle B
	 * @param initialVelA initial velocity of particle A
	 * @param initialVelB initial velocity of particle B
	 * @return the appropriate admissible root
	 */
	public double quadraticFormulaX(double a, double b, double c, double pAMass, double pBMass, double initialVelA, double initialVelB)
	{
		double finalVel1 = (-b + Math.sqrt(b*b - 4*a*c))/(2*a);
		double finalVel2 = (-b - Math.sqrt(b*b - 4*a*c))/(2*a);
		if((b*b-4*a*c)<0)//negative discriminant, value is "NaN" program crashes when that is returned/applied, so return 0 instead
		{
			return 0;
		}
		//particle B in  a collision will either speed up in the direction it is going, slow down in the direction its going
		//or reverse its direction and have its speed change

		if(initialVelA*initialVelB>0)//particles are going in the same direction
		{
			//particle B speeds up
			if(Math.abs(initialVelA)>Math.abs(initialVelB))
			{
				//find which root has a greater absolute velocity (greater absolute value = particle speeds up)
				double absOfFinalVel = Math.max(Math.abs(finalVel1),Math.abs(finalVel2));
				if(absOfFinalVel==Math.abs(finalVel1))//cant return the absolute value of the velocity
				{
					return finalVel1;
				}
				else
				{
					return finalVel2;
				}
			}
			//particle A speeds up ie. particle B gets the velocity with the lower absolute value
			else
			{
				double absOfFinalVel = Math.min(Math.abs(finalVel1),Math.abs(finalVel2));
				if(absOfFinalVel==Math.abs(finalVel1))//cant return the absolute value of the velocity
				{
					return finalVel1;
				}
				else
				{
					return finalVel2;
				}
			}
		}
		//reverse its direction
		else if(initialVelA*initialVelB<0)//particles colliding head on (coming in at different direction)
		{
			if(initialVelB*finalVel1<0)//this means finalVel1 is in the opposite direction of initial velocity
			{
				return finalVel1;
			}
			else
			{
				return finalVel2;
			}
		}
		else//initial*initial==0 (one of the particles is stationary)
		{
			if(initialVelB!=finalVel1)//when one particle is stationary, the roots are the same as the 2 initial velocites, return the root that isnt the same
				// as the initial velocity
			{
				return finalVel1;
			}
			else
			{
				return finalVel2;
			}
		}

	}
	/**
	 * 
	 * returns the appropriate root of a quadratic equation in the form ax^2 + bx + c
	 * @param a coefficient a
	 * @param b coefficient b
	 * @param c coefficient c
	 * @param pAMass mass of particle A
	 * @param pBMass mass of particle B
	 * @param initialVelA initial velocity of particle A
	 * @param initialVelB initial velocity of particle B
	 * @return the appropriate admissible root
	 */
	public double quadraticFormulaY(double a, double b, double c, double pAMass, double pBMass, double initialVelA, double initialVelB)
	{
		double finalVel1 = (-b + Math.sqrt(b*b - 4*a*c))/(2*a);
		double finalVel2 = (-b - Math.sqrt(b*b - 4*a*c))/(2*a);
		//particle B in  a collision will either speed up in the direction it is going, slow down in the direction its going
		//or reverse its direction and have its speed change
		if((b*b-4*a*c)<0)//negative discriminant, value is "NaN" program crashes when that is returned/applied, so return 0 instead
		{
			return 0;
		}
		if(initialVelA*initialVelB>0)//particles are going in the same direction
		{
			//particle B speeds up
			if(Math.abs(initialVelA)>Math.abs(initialVelB))
			{
				//find which root has a greater absolute velocity (greater absolute value = particle speeds up)
				double absOfFinalVel = Math.max(Math.abs(finalVel1),Math.abs(finalVel2));
				if(absOfFinalVel==Math.abs(finalVel1))//cant return the absolute value of the velocity
				{
					return finalVel1;
				}
				else
				{
					return finalVel2;
				}
			}
			//particle A speeds up ie. particle B gets the velocity with the lower absolute value
			else
			{
				double absOfFinalVel = Math.min(Math.abs(finalVel1),Math.abs(finalVel2));
				if(absOfFinalVel==Math.abs(finalVel1))//cant return the absolute value of the velocity
				{
					return finalVel1;
				}
				else
				{
					return finalVel2;
				}
			}
		}
		//reverse its direction
		else if(initialVelA*initialVelB<0)//particles colliding head on (coming in at different direction)
		{
			if(initialVelB*finalVel1<0)//this means finalVel1 is in the opposite direction of initial velocity
			{
				return finalVel1;
			}
			else
			{
				return finalVel2;
			}
		}
		else//initial*initial==0 (one of the particles is stationary)
		{
			if(initialVelB!=finalVel1)//when one particle is stationary, the roots are the same as the 2 initial velocites, return the root that isnt the same
				// as the initial velocity
			{
				return finalVel1;
			}
			else
			{
				return finalVel2;
			}
		}
	}


	public void checkParticleBoxCollisions()
	{
		for (int i = 0; i < particles.size(); i++)
		{
			// GROUND CHECK
			if (ground.y - (particles.get(i).getYPos() + particles.get(i).getDiameter()) < particles.get(i).getYVel())
			{
				if (particles.get(i).getYVel() > 2.0)
				{
					particles.get(i).setYPos(ground.y - particles.get(i).getDiameter());
					particles.get(i).setGrounded(true);
					particles.get(i).setYVel(-particles.get(i).getYVel() * particles.get(i).getRestitutionCoeff());
				}
				else
				{
					particles.get(i).setYPos(ground.y - particles.get(i).getDiameter());
					particles.get(i).setGrounded(true);
					particles.get(i).setYVel(0);
				}
				if(Math.abs(particles.get(i).getXVel())<0.05)//i noticed particles moving ever so slightly on their own sometimes...
				{
					particles.get(i).setXVel(0);
				}


			}
			else
			{
				particles.get(i).setGrounded(false);
			}

			// LEFT WALL CHECK
			if (particles.get(i).getXPos() - leftWall.x < particles.get(i).getXVel())
			{
				particles.get(i).setXPos(leftWall.x);
				particles.get(i).setXVel(-particles.get(i).getXVel() * particles.get(i).getRestitutionCoeff());
			}

			// RIGHT WALL CHECK
			if (rightWall.x - (particles.get(i).getXPos() + particles.get(i).getDiameter())< particles.get(i).getXVel())
			{
				particles.get(i).setXPos(rightWall.x - particles.get(i).getDiameter());
				particles.get(i).setXVel(-particles.get(i).getXVel() * particles.get(i).getRestitutionCoeff());
			}
		}
	}


	public ArrayList<Particle> getParticles()
	{
		return particles;
	}
	public void clearParticles()
	{
		particles.clear();
	}
	/**
	 * When particles are caught or forced inside each other, they are repositioned appropriately
	 */
	public void checkFlawedCollisions()
	{
		for (int i = 0; i < particles.size(); i++)
		{
			for (int j = i+1; j < particles.size(); j++)
			{
				Particle pA = particles.get(i);
				Particle pB = particles.get(j);
				if(pA.contains(pB))//particle is stuck inside the other particle
				{		
					//always repositioning the particle above relative to the particle below
					if (pA.getYPos() > pB.getYPos())//A is below B
					{
						pB.setYPos(pA.getYPos()-pB.getDiameter()-1);//reposition B ontop of A

						if(pA.getXPos()>pB.getXPos())
						{
							pB.setXPos(pA.getXPos()-pB.getDiameter()-1);//reposition B to the left of A
						}
						else if(pA.getXPos()<pB.getXPos())
						{
							pB.setXPos((pA.getXPos()+pA.getDiameter()+1));//reposition B to the right of A
						}
					}
					else if (pA.getYPos()<pB.getYPos())//B is below A
					{
						pA.setYPos(pB.getYPos()-pA.getDiameter()-1);//reposition A ontop of B

						if(pA.getXPos()<pB.getXPos())
						{
							pA.setXPos(pB.getXPos()-pA.getDiameter()-1);//reposition A to the left of B
						}
						else if(pA.getXPos()>pB.getXPos())
						{
							pA.setXPos((pB.getXPos()+pB.getDiameter()+1));//reposition A to the right of B
						}
					}

					else//A and B are on equal level
					{
						if (pA.getXPos() > pB.getXPos())//particle A is right of particle B needs to be repositioned further right
						{
							pA.setXPos(pB.getXPos() + pB.getDiameter() + 1); //reposition particle A to the right of particle B
						}
						else if (pA.getXPos() < pB.getXPos())//particle A is left of particle B needs to be repositioned further left
						{
							pA.setXPos(pB.getXPos() - pA.getDiameter() - 1);//reposition particle A to the left of particle B
						}
					}
				}
			}
		}
	}
	public double getGravity()
	{
		return gravity;
	}

	public void setGravity(double g)
	{
		gravity = g;
		for (int i = 0; i < particles.size(); i++)
		{
			particles.get(i).setGravity(gravity);
		}
	}

	public void draw(Graphics g)
	{
		// Collision checks
		checkParticleParticleCollisions();
		checkParticleBoxCollisions();
		checkFlawedCollisions();

		for (int i = 0; i < particles.size(); i++)
		{
			particles.get(i).draw(g);
			//			if (particles.size() > 1 && i < particles.size()-1)
			//			{
			//				g.drawLine((int)(particles.get(i).getXPos() + particles.get(i).getDiameter()/2),(int) (particles.get(i).getYPos() + particles.get(i).getDiameter()/2),
			//						(int) (particles.get(i + 1).getXPos() + particles.get(i+1).getDiameter()/2), (int) (particles.get(i+1).getYPos() + particles.get(i+1).getDiameter()/2));
			//			}

		}
		ground.draw(g);
	}
}