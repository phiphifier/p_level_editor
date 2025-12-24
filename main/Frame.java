package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Frame implements ComponentListener, ActionListener {
	
	// VARS
	private JFrame frame;
	private Panel drawingPanel;
	private static final int WINDOW_RESIZE_DELAY = 120;
	private Timer resizeTimer;
	private BufferedImage icon;
	private static final String WINDOW_TITLE = "Level Editor";
	
	// CONSTRUCTOR
	public Frame(Panel panel) {
		resizeTimer = new Timer(WINDOW_RESIZE_DELAY, this);
		resizeTimer.setRepeats(false);
		icon = Resources.getIcon();
		drawingPanel = panel;
		frame = new JFrame();
		frame.setIconImage(icon);
		frame.addComponentListener(this);
		frame.setTitle(WINDOW_TITLE);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(drawingPanel);
		frame.pack();
		frame.getContentPane().setLayout(null);
		frame.setVisible(true);
	}
	
	// RESIZE TIMER ENDED, RESIZE
	@Override
	public void actionPerformed(ActionEvent e) {
		double panelWidth = (double)Panel.SCREEN_SIZE.width;
		double panelHeight = (double)Panel.SCREEN_SIZE.height;
		double frameWidth = (double)frame.getContentPane().getWidth();
		double frameHeight = (double)frame.getContentPane().getHeight();
		int newX = 0;
		int newY = 0;
		// TALLER THAN IT IS WIDE
		if (panelHeight/frameHeight >= panelWidth/frameWidth) {
			drawingPanel.setScale(frameHeight/panelHeight);
			newX = Math.round((float)(frameWidth/2-panelWidth*drawingPanel.getScale()/2));
		}
		else {
			drawingPanel.setScale(frameWidth/panelWidth);
			newY = Math.round((float)(frameHeight/2-panelHeight*drawingPanel.getScale()/2));
		}
		drawingPanel.setBounds(newX, newY, Math.round((float)(panelWidth*drawingPanel.getScale())), Math.round((float)(panelHeight*drawingPanel.getScale())));
	}
	
	// DETECT RESIZES
	@Override
	public void componentResized(ComponentEvent e) {
		resizeTimer.restart();
	}
	
	// UNUSED
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentHidden(ComponentEvent e) {}
}