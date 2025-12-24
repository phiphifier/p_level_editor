package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Inputs implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {

	// VARS
		// DRAWING PANEL REFERENCE
	private static Panel drawingPanel;
		// INPUT STATES
	private static boolean upPressed = false;
	private static boolean leftPressed = false;
	private static boolean downPressed = false;
	private static boolean rightPressed = false;
	private static boolean leftClicked = false;
	private static boolean rightClicked = false;
	private static int mouseX = 0;
	private static int mouseY = 0;
	private static TypedInputListener typedInputListener = null;
	private static ScrollInputListener scrollInputListener = null;
	
	// GET UP PRESSED
	public static boolean isUpPressed() {
		return upPressed;
	}

	// GET LEFT PRESSED
	public static boolean isLeftPressed() {
		return leftPressed;
	}

	// GET DOWN PRESSED
	public static boolean isDownPressed() {
		return downPressed;
	}

	// GET RIGHT PRESSED
	public static boolean isRightPressed() {
		return rightPressed;
	}

	// GET LEFT CLICKED
	public static boolean isLeftClicked() {
		return leftClicked;
	}

	// GET RIGHT CLICKED
	public static boolean isRightClicked() {
		return rightClicked;
	}
	
	// GET MOUSE X
	public static int getMouseX() {
		return mouseX;
	}
	
	// GET MOUSE Y
	public static int getMouseY() {
		return mouseY;
	}
	
	// MOUSE DRAGGED
	@Override
	public void mouseDragged(MouseEvent e) {
		if (drawingPanel != null) {
			mouseX = (int)Math.round(e.getX()/drawingPanel.getScale());
			mouseY = (int)Math.round(e.getY()/drawingPanel.getScale());
		}
	}

	// MOUSE MOVED
	@Override
	public void mouseMoved(MouseEvent e) {
		if (drawingPanel != null) {
			mouseX = (int)Math.round(e.getX()/drawingPanel.getScale());
			mouseY = (int)Math.round(e.getY()/drawingPanel.getScale());
		}
	}

	// MOUSE CLICKED
	@Override
	public void mousePressed(MouseEvent e) {
		switch(e.getButton()) {
		case MouseEvent.BUTTON1: leftClicked = true;
		break;
		case MouseEvent.BUTTON3: rightClicked = true;
		break;
		}
	}

	// MOUSE RELEASED
	@Override
	public void mouseReleased(MouseEvent e) {
		switch(e.getButton()) {
		case MouseEvent.BUTTON1: leftClicked = false;
		break;
		case MouseEvent.BUTTON3: rightClicked = false;
		}
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if (scrollInputListener != null) {
			int clicks = e.getWheelRotation();
			if (clicks > 0) scrollInputListener.scrolledDown();
			else scrollInputListener.scrolledUp();
		}
	}

	// KEY TYPED
	@Override
	public void keyTyped(KeyEvent e) {
		if (typedInputListener != null) typedInputListener.charTyped(e.getKeyChar());
	}

	// KEY PRESSED
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W: upPressed = true;
		break;
		case KeyEvent.VK_A: leftPressed = true;
		break;
		case KeyEvent.VK_S: downPressed = true;
		break;
		case KeyEvent.VK_D: rightPressed = true;
		break;
		}
	}

	// KEY RELEASED
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
		case KeyEvent.VK_W: upPressed = false;
		break;
		case KeyEvent.VK_A: leftPressed = false;
		break;
		case KeyEvent.VK_S: downPressed = false;
		break;
		case KeyEvent.VK_D: rightPressed = false;
		break;
		}
	}

	// SET DRAWING PANEL
	public static void setDrawingPanel(Panel panel) {
		drawingPanel = panel;
	}
	
	// GET TYPED INPUT LISTENER
	public static TypedInputListener getTypedInputListener() {
		return typedInputListener;
	}
	
	// SET TYPED INPUT LISTENER
	public static void setTypedInputListener(TypedInputListener listener) {
		typedInputListener = listener;
	}
	
	// GET SCROLL INPUT LISTENER
	public static ScrollInputListener getScrollInputListener() {
		return scrollInputListener;
	}
	
	// SET SCROLL INPUT LISTENER
	public static void setScrollInputListener(ScrollInputListener listener) {
		scrollInputListener = listener;
	}
	
	// UNUSED
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
}