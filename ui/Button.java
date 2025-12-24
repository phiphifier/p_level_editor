package ui;

import main.Updatable;
import main.Drawable;
import main.Inputs;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

public class Button implements Updatable, Drawable {

	// CLICK ENUM
	private enum MouseClick {
		LEFT,
		RIGHT,
		NONE
	}
	
	// BUTTON STATE ENUM
	private enum ButtonState {
		NORMAL,
		HIGHLIGHTED,
		PRESSED
	}
	
	// VARS
	private ButtonState state = ButtonState.NORMAL;
	private int buttonID;
	private int x, y;
	private int width, height;
	private String bodyText;
	private boolean stickyMouseButton;
	private Color normalColor;
	private Color highlightedColor;
	private Color pressedColor;
	private Color shadowColor;
	private int shadowDepth;
	private Font buttonFont;
	private Color textColor;
	private HorizontalAlignment hoverTipAlignment = HorizontalAlignment.LEFT;
	private Font tipFont;
	private String tipText;
	private Color tipBackgroundColor;
	private int tipBackgroundHeight;
	private int tipXOffset = 0;
	private ButtonListener buttonListener;
	private boolean isEnabled = true;
	private boolean useHoverTip = false;
	private float alpha = 0;
	private MouseClick lastClick = MouseClick.NONE;
	private boolean stayHighlighted = false;
	
	// CONSTRUCTOR
	public Button(int buttonID, int x, int y, int width, int height, String bodyText, boolean useLargeFont, ButtonListener listener) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.bodyText = bodyText;
		if (useLargeFont) buttonFont = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.BUTTON_FONT_SIZE_LARGE);
		else buttonFont = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.BUTTON_FONT_SIZE_SMALL);
		textColor = UIConstants.UI_COLOR_TEXT;
		buttonListener = listener;
		normalColor = UIConstants.BUTTON_COLOR_NORMAL;
		highlightedColor = UIConstants.BUTTON_COLOR_HIGHLIGHTED;
		pressedColor = UIConstants.BUTTON_COLOR_PRESSED;
		shadowColor = UIConstants.UI_COLOR_SHADOW;
		shadowDepth = UIConstants.UI_BOX_SHADOW_DEPTH;
	}
	
	// IS MOUSE HOVERING
	private boolean isMouseHovering() {
		if (Inputs.getMouseX() >= x && Inputs.getMouseX() < x+width && Inputs.getMouseY() >= y && Inputs.getMouseY() < y+height) return true;
		else return false;
	}
	
	// IS MOUSE CLICKED
	private MouseClick checkMouseClick() {
		if (Inputs.isLeftClicked()) return MouseClick.LEFT;
		if (Inputs.isRightClicked()) return MouseClick.RIGHT;
		else return MouseClick.NONE;
	}
	
	// UPDATE
	@Override
	public void update() {
		MouseClick currentClick = checkMouseClick();
		// UPDATE ALPHA
		alpha = UIUtils.getUpdatedAlpha(alpha, isEnabled);
		if (isEnabled) {
			// BUTTON WAS JUST MOUSED OVER 
			if (state == ButtonState.NORMAL && isMouseHovering()) {
				if (buttonListener != null) buttonListener.buttonMoused(buttonID);
				state = ButtonState.HIGHLIGHTED;
			}
			// BUTTON WAS ALREADY MOUSED OVER AND JUST PRESSED
			else if (state == ButtonState.HIGHLIGHTED && currentClick != MouseClick.NONE) {
				if (buttonListener != null) {
					if (currentClick == MouseClick.LEFT) buttonListener.buttonLeftPressed(buttonID);
					if (currentClick == MouseClick.RIGHT) buttonListener.buttonRightPressed(buttonID);
				}
				state = ButtonState.PRESSED;
			}
			// BUTTON WAS JUST RELEASED AFTER ALREADY BEING PRESSED, A CLICK HAS HAPPENED
			else if (!stickyMouseButton && state == ButtonState.PRESSED && lastClick != MouseClick.NONE && currentClick == MouseClick.NONE && isMouseHovering() || stickyMouseButton && state == ButtonState.PRESSED && currentClick == MouseClick.NONE) {
				if (buttonListener != null) {
					if (lastClick == MouseClick.LEFT) buttonListener.buttonLeftReleased(buttonID);
					if (lastClick == MouseClick.RIGHT) buttonListener.buttonRightReleased(buttonID);
				}
				state = ButtonState.HIGHLIGHTED;
			}
			// MOUSE HAS LEFT THE BUTTON, UNMOUSED (IF NOT STICKY MOUSE BUTTON)
			else if (!stickyMouseButton && state != ButtonState.NORMAL && !isMouseHovering()) {
				if (buttonListener != null) buttonListener.buttonUnMoused(buttonID);
				state = ButtonState.NORMAL;
			}
			// IS STICKY MOUSE BUTTON
			else {
				if (state != ButtonState.NORMAL && currentClick == MouseClick.NONE) {
					if (isMouseHovering()) state = ButtonState.HIGHLIGHTED;
					else state = ButtonState.NORMAL;
				}
			}
		}
		else {
			state = ButtonState.NORMAL;
		}
		lastClick = currentClick;
	}
	
	// ENABLE MOUSE HOVER TIP
	public void enableMouseHoverTip(String tipText, HorizontalAlignment hoverTipAlignment) {
		this.tipText = tipText;
		this.hoverTipAlignment = hoverTipAlignment;
		useHoverTip = true;
		tipXOffset = UIConstants.BUTTON_TIP_XOFFSET;
		tipFont = UIConstants.DEFAULT_FONT.deriveFont(Font.ITALIC+Font.BOLD, UIConstants.BUTTON_FONT_SIZE_TIP);
		tipBackgroundColor = UIConstants.BUTTON_COLOR_HOVER_TIP_BACKGROUND;
		tipBackgroundHeight = UIConstants.BUTTON_TIP_BACKGROUND_HEIGHT;
	}
	
	// SET STAY HIGHLIGHTED
	public void setStayHighlighted(boolean stayPressed) {
		this.stayHighlighted = stayPressed;
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		// APPLY ALPHA
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		// DRAW SHADOW
		g2.setColor(shadowColor);
		g2.fillRect(x, y+shadowDepth, width, height);
		// DRAW BODY WHEN IDLE
		if (state == ButtonState.NORMAL && !stayHighlighted) {
			g2.setColor(normalColor);
			g2.fillRect(x, y, width, height);
		}
		// DRAW BODY WHEN HIGHLIGHTED
		if (state == ButtonState.HIGHLIGHTED || stayHighlighted) {
			g2.setColor(highlightedColor);
			g2.fillRect(x, y, width, height);
		}
		// DRAW BODY WHEN PRESSED
		if (state == ButtonState.PRESSED) {
			g2.setColor(pressedColor);
			g2.fillRect(x, y+shadowDepth-1, width, height);
		}
		// SETUP g2 TO DRAW BODY TEXT
		g2.setFont(buttonFont);
		g2.setColor(textColor);
		FontMetrics fm = g2.getFontMetrics();
		int bodyTextWidth = fm.stringWidth(bodyText);
		int bodyTextCenterToBaselineOffset = (fm.getAscent()-fm.getDescent())/2;
		// DRAW BODY TEXT
		int bodyTextDrawX;
		if (bodyTextWidth <= width) bodyTextDrawX = x+width/2-bodyTextWidth/2;
		else bodyTextDrawX = x;
		int bodyTextDrawY = y+height/2+bodyTextCenterToBaselineOffset;
		if (state == ButtonState.PRESSED) bodyTextDrawY = bodyTextDrawY+(shadowDepth-1);
		UIUtils.drawLimitedWidthStringWithShadow(g2, shadowColor, bodyText, bodyTextDrawX, bodyTextDrawY, width, false);
		// DRAW HOVER TIP IF ENABLED
		if (useHoverTip && state == ButtonState.HIGHLIGHTED) {
			// SETUP g2 FONT TO TIP FONT
			g2.setFont(tipFont);
			FontMetrics fmt = g2.getFontMetrics();
			int tipTextWidth = fmt.stringWidth(tipText);
			int tipTextCenterToBaslineOffset = (fmt.getAscent()-fmt.getDescent())/2;
			// IF LEFT ALIGNED
			if (hoverTipAlignment == HorizontalAlignment.LEFT) {
				// DRAW BACKGROUND
				g2.setColor(tipBackgroundColor);
				g2.fillRect(Inputs.getMouseX()-tipXOffset-tipTextWidth-2, Inputs.getMouseY()-tipBackgroundHeight/2, tipTextWidth+4, tipBackgroundHeight);
				// DRAW TIP TEXT
				g2.setColor(textColor);
				UIUtils.drawStringWithShadow(g2, shadowColor, tipText, Inputs.getMouseX()-tipXOffset-tipTextWidth, Inputs.getMouseY()-tipBackgroundHeight/2+tipBackgroundHeight/2+tipTextCenterToBaslineOffset);
			}
			// ELSE DEFAULT TO RIGHT ALIGNED
			else {
				// DRAW BACKGROUND
				g2.setColor(tipBackgroundColor);
				g2.fillRect(Inputs.getMouseX()+tipXOffset-2, Inputs.getMouseY()-tipBackgroundHeight/2, tipTextWidth+4, tipBackgroundHeight);
				// DRAW TIP TEXT
				g2.setColor(textColor);
				UIUtils.drawStringWithShadow(g2, shadowColor, tipText, Inputs.getMouseX()+tipXOffset, Inputs.getMouseY()-tipBackgroundHeight/2+tipBackgroundHeight/2+tipTextCenterToBaslineOffset);
			}
		}
	}
	
	// SET STICKY MOUSE
	public void setStickyMouse(boolean isEnabled) {
		stickyMouseButton = isEnabled;
	}
	
	// SET BODY TEXT
	public void setBodyText(String newBodyText) {
		bodyText = newBodyText;
	}
	
	// GET BODY TEXT
	public String getBodyText() {
		return bodyText;
	}
	
	// SET ENABLED
	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	// SET TEXT SIZE
	public void setBodyTextSize(int size) {
		buttonFont = buttonFont.deriveFont(buttonFont.getStyle(), size);
	}
	
	// GET ID
	public int getID() {
		return buttonID;
	}
	
	// GET X
	public int getX() {
		return x;
	}
	
	// SET X
	public void setX(int x) {
		this.x = x;
	}
	
	// GET Y
	public int getY() {
		return y;
	}
	
	// SET Y
	public void setY(int y) {
		this.y = y;
	}
	
	// GET WIDTH
	public int getWidth() {
		return width;
	}
	
	// SET WIDTH
	public void setWidth(int width) {
		this.width = width;
	}
	
	// GET HEIGHT
	public int getHeight() {
		return height;
	}
	
	// SET HEIGHT
	public void setHeight(int height) {
		this.height = height;
	}
}