package ui;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import engine.Particle;
import engine.World;
public class ToolBoxPane extends JPanel implements MouseListener
{
	private int paneWidth, paneHeight;
	private World w;
	private CustomButton plusMass, minusMass, plusDiameter, minusDiameter, plusRestit, minusRestit, plusGravity, minusGravity, plusFriction, minusFriction;
	private boolean particleSelected;
	
	public ToolBoxPane()
	{
		paneWidth = 200; 
		paneHeight = 600;
		particleSelected = false;
		setPreferredSize(new Dimension(paneWidth, paneHeight));
		setFocusable(true);
		
		w = WorldPane.getWorld();
		
		addMouseListener(this);
	
		try
		{
			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			plusMass = new CustomButton(120, 32, ImageIO.read(new File(s + "/plus.png")));
			plusDiameter = new CustomButton(120, 52, ImageIO.read(new File(s + "/plus.png")));
			plusRestit = new CustomButton(120, 72, ImageIO.read(new File(s + "/plus.png")));
			plusGravity = new CustomButton(120, 430, ImageIO.read(new File(s + "/plus.png")));
			plusFriction = new CustomButton(120, 92, ImageIO.read(new File(s + "/plus.png")));
			minusMass = new CustomButton(140, 32, ImageIO.read(new File(s + "/minus.png")));
			minusDiameter = new CustomButton(140, 52, ImageIO.read(new File(s + "/minus.png")));
			minusRestit = new CustomButton(140, 72, ImageIO.read(new File(s + "/minus.png")));
			minusGravity = new CustomButton(140, 430, ImageIO.read(new File(s + "/minus.png")));
			minusFriction = new CustomButton(140, 92, ImageIO.read(new File(s + "/minus.png")));
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
	//	g.setColor(Color.BLACK);
		g.fillRect(0, 0, 200, 600);
		g.setColor(Color.WHITE);
		g.drawString("PARTICLE", 75, 20);
		g.drawString("Mass:", 10, 40);
		g.drawString("Diameter:", 10, 60);
		g.drawString("Restitution:", 10, 80);
		g.drawString("Friction:", 10, 100);
		g.setColor(Color.GRAY);
		g.fillRect(0, 400, 200, 200);
		g.setColor(Color.WHITE);
		g.drawString("WORLD", 80, 420);
		g.drawString("Gravity: ", 10, 440);
	
		w = WorldPane.getWorld();
		for (int i = 0; i < w.getParticles().size(); i++)
		{
			if (w.getParticles().get(i).isSelected())
			{
				g.setColor(Color.WHITE);
				Particle p = w.getParticles().get(i);
				g.drawString("" + p.getMass(), 170, 40);
				g.drawString("" + p.getDiameter(), 170, 60);
				g.drawString("" + p.getRestitutionCoeff(), 170, 80);
				g.drawString("" + p.getFriction(), 170, 100);
				plusMass.setActive(true);
				minusMass.setActive(true);
				plusDiameter.setActive(true);
				minusDiameter.setActive(true);
				plusRestit.setActive(true);
				minusRestit.setActive(true);
				plusFriction.setActive(true);
				minusFriction.setActive(true);
				break;
			}
			else
			{
				plusMass.setActive(false);
				minusMass.setActive(false);
				plusDiameter.setActive(false);
				minusDiameter.setActive(false);
				plusRestit.setActive(false);
				minusRestit.setActive(false);
				plusFriction.setActive(false);
				minusFriction.setActive(false);
			}
		}
		
		plusMass.paint(g);
		plusDiameter.paint(g);
		plusRestit.paint(g);
		plusFriction.paint(g);
		minusMass.paint(g);
		minusDiameter.paint(g);
		minusRestit.paint(g);
		minusFriction.paint(g);
		plusGravity.paint(g);
		minusGravity.paint(g);
		g.drawString(w.getGravity() + "", 170, 440);
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if (plusMass.isActive() && plusMass.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected())
				{
					w.getParticles().get(i).setMass(w.getParticles().get(i).getMass() + 1);
				}
			}
		}
		
		if (minusMass.isActive() && minusMass.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected())
				{
					w.getParticles().get(i).setMass(w.getParticles().get(i).getMass() - 1);
				}
			}
		}
		
		if (plusDiameter.isActive() && plusDiameter.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected())
				{
					w.getParticles().get(i).setDiameter(w.getParticles().get(i).getDiameter() + 1);
				}
			}
		}
		
		if (minusDiameter.isActive() && minusDiameter.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected())
				{
					w.getParticles().get(i).setDiameter(w.getParticles().get(i).getDiameter() - 1);
				}
			}
		}
		
		if (plusRestit.isActive() && plusRestit.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected() && w.getParticles().get(i).getRestitutionCoeff() < 1)
				{
					w.getParticles().get(i).setRestitutionCoeff(w.getParticles().get(i).getRestitutionCoeff() + 0.1000);
				}
			}
		}
		
		if (minusRestit.isActive() && minusRestit.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected() && w.getParticles().get(i).getRestitutionCoeff() > 0)
				{
					w.getParticles().get(i).setRestitutionCoeff(w.getParticles().get(i).getRestitutionCoeff() - 0.1000);
				}
			}
		}
		
		if (plusFriction.isActive() && plusFriction.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected())
				{
					w.getParticles().get(i).setFriction(w.getParticles().get(i).getFriction() + 0.0100);
				}
			}
		}
		
		if (minusFriction.isActive() && minusFriction.contains(e.getX(), e.getY()))
		{
			for (int i = 0; i < w.getParticles().size(); i++)
			{
				if (w.getParticles().get(i).isSelected())
				{
					w.getParticles().get(i).setFriction(w.getParticles().get(i).getFriction() - 0.0100);
				}
			}
		}
		
		if (plusGravity.isActive() && plusGravity.contains(e.getX(), e.getY()))
		{
			w.setGravity(w.getGravity() + 0.1);
		}
		
		if (minusGravity.isActive() && minusGravity.contains(e.getX(), e.getY()))
		{
			w.setGravity(w.getGravity() - 0.1);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		// TODO Auto-generated method stub
		
	}
}
