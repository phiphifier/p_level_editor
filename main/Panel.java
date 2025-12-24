package main;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Panel extends JPanel {
	// VARS
		// PANEL STUFF
	public static final Dimension SCREEN_SIZE = new Dimension(1280, 720);
	private double scale = 1;
	private BufferedImage currentScreenBuffer = null;
	
	// CONSTRUCTOR
	public Panel(Inputs inputs) {
		setFocusable(true);
		setPreferredSize(SCREEN_SIZE);
		Inputs.setDrawingPanel(this);
		addKeyListener(inputs);
		addMouseListener(inputs);
		addMouseMotionListener(inputs);
		addMouseWheelListener(inputs);
	}
	
	// SET SCREEN BUFFER
	public void setScreenBuffer(BufferedImage newScreenBuffer) {
		currentScreenBuffer = newScreenBuffer;
	}
	
	// PAINT/DRAW
	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(currentScreenBuffer, 0, 0, (int)(SCREEN_SIZE.width*scale), (int)(SCREEN_SIZE.height*scale), null);
	}
	
	// GET SCALE
	public double getScale() {
		return scale;
	}
	
	// SET SCALE
	public void setScale(double newScale) {
		scale = newScale;
	}
}