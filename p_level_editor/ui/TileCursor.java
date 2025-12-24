package ui;

import main.Updatable;
import main.Drawable;
import main.Inputs;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;

public class TileCursor implements Updatable, Drawable {

	// VARS
		// INSTANCE FIELDS
	private boolean leftClicked = false;
	private boolean rightClicked = false;
	private TileCursorListener listener;
	private int x, y;
	private int gridXOffset, gridYOffset;
	private boolean isEnabled = true;
	private float alpha = ALPHA_FADE_MIN;
	private boolean isAlphaIncreasing = true;
	private Color currentColor = CURSOR_COLOR_NORMAL;
	private int size;
		// CONSTANTS
	public static final Color CURSOR_COLOR_NORMAL = new Color(255, 255, 255);
	public static final Color CURSOR_COLOR_INVALID = new Color (190, 0, 0);
	private static final float ALPHA_FADE_MAX = 0.75f;
	private static final float ALPHA_FADE_MIN = 0.15f;
	private static final float ALPHA_CLICKED = 0.95f;
	private static final float ALPHA_CHANGE_RATE = 0.035f;
	
	// CONSTRUCTOR
	public TileCursor(TileCursorListener listener, int size, int gridXOffset, int gridYOffset) {
		this.listener = listener;
		this.size = size;
		this.gridXOffset = gridXOffset;
		this.gridYOffset = gridYOffset;
	}
	
	// ENABLE
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	// UPDATE POSITION
	private void updatePosition() {
		x = (Inputs.getMouseX()-(Inputs.getMouseX()+gridXOffset)%size);
		y = (Inputs.getMouseY()-(Inputs.getMouseY()+gridYOffset)%size);
	}
	
	// NOTIFY LEFT CLICK
	private void notifyLeftClick() {
		alpha = ALPHA_CLICKED;
		isAlphaIncreasing = false;
		listener.leftClicked(x, y);
	}
	
	// NOTIFY RIGHT CLICK
	private void notifyRightClick() {
		alpha = ALPHA_CLICKED;
		isAlphaIncreasing = true;
		listener.rightClicked(x, y);
	}
	
	// NOTIFY LEFT RELEASED
	private void notifyLeftRelease() {
		listener.leftReleased(x, y);
		leftClicked = false;
	}
	
	// NOTIFY RIGHT RELEASED
	private void notifyRightRelease() {
		listener.rightReleased(x, y);
		rightClicked = false;
	}
	
	// UPDATE ALPHA
	private void updateAlpha() {
		if (isAlphaIncreasing) alpha = Math.min(ALPHA_FADE_MAX, alpha+ALPHA_CHANGE_RATE);
		else alpha = Math.max(ALPHA_FADE_MIN, alpha-ALPHA_CHANGE_RATE);
		if (alpha == ALPHA_FADE_MIN || alpha == ALPHA_FADE_MAX) isAlphaIncreasing = !isAlphaIncreasing;
	}
	
	// CHANGE CURSOR COLOR
	public void changeColor(Color newColor) {
		currentColor = newColor;
	}
	
	// GET SIZE
	public int getSize() {
		return size;
	}
	
	// GET X
	public int getX() {
		return x;
	}
	
	// GET Y
	public int getY() {
		return y;
	}
	
	// UPDATE
	@Override
	public void update() {
		updateAlpha();
		updatePosition();
		if (Inputs.isLeftClicked()) {
			notifyLeftClick();
			leftClicked = true;
		}
		else if (leftClicked) notifyLeftRelease();
		if (Inputs.isRightClicked()) {
			notifyRightClick();
			rightClicked = true;
		}
		else if (rightClicked) notifyRightRelease();
	}
	
	// GET IS CURSOR ENABLED
	public boolean getIsEnabled() {
		return isEnabled;
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		if (isEnabled) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.setColor(currentColor);
			g2.fillRect(x, y, size, size);
		}
	}
}