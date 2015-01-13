package ui;


import java.awt.Graphics;

import javax.swing.JFrame;

public class Main extends JFrame
{
	private WorldPane worldPane;
	private ToolBoxPane toolBox;
	
	public Main()
	{
		setTitle("PHIZX");
		setVisible(true);
		setSize(1200, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		
		initComponents();
	}
	
	public void initComponents()
	{
		toolBox = new ToolBoxPane();
		toolBox.setBounds(0, 0, 200, 600);
		toolBox.setLocation(0, 0);
		getContentPane().add(toolBox);
		
		worldPane = new WorldPane();
		worldPane.setBounds(200, 0, 1000, 600);
		worldPane.setLocation(200, 0);
		getContentPane().add(worldPane);
	}
	

	
	public static void main(String[] args)
	{
		Main m = new Main();
	}

}
