package ui;

import main.Updatable;
import main.Drawable;
import main.GameManager;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.awt.Graphics2D;

public abstract class Menu implements Updatable, Drawable, ButtonListener {

	// CLOSE MODE ENUM
	protected enum AdditionalCloseOperation {
		NOTHING,
		ENABLE_OWNER,
		CLOSE_ENTIRE_HIERARCHY
	}

	//VARS
	private HorizontalAlignment titleAlignment = HorizontalAlignment.MIDDLE;
	protected int x, y;
	protected int width, height;
	protected Color bodyColor;
	protected Color borderColor;
	protected Color shadowColor;
	protected Color textColor;
	protected String titleText;
	protected Font titleFont;
	protected int borderThickness;
	protected int shadowDepth;
	protected boolean isEnabled = true;
	protected Menu owner;
	protected ArrayList<Menu> subMenus = new ArrayList<Menu>();
	protected ArrayList<Button> buttons = new ArrayList<Button>();
	protected float alpha = 0;
	protected static final int BODY_TEXT_VERTICAL_PADDING = 8;
	protected static final int BODY_TEXT_HORIZONTAL_PADDING = 2;
	protected static final int DEFAULT_BOX_VERTICAL_PADDING = 4+UIConstants.UI_BOX_SHADOW_DEPTH;
	protected static final int DEFAULT_BOX_HORIZONTAL_PADDING = 4;
	protected int xShakeOffset = 0;
	protected static final float SHAKE_DECAY_RATE = 0.85f;
	protected static final int SHAKE_BEGIN_OFFSET = 20;
	
	// CONSTRUCTOR
	public Menu(Menu owner) {
		this.owner = owner;
		bodyColor = UIConstants.MENU_COLOR_BODY;
		borderColor = UIConstants.MENU_COLOR_BORDER;
		shadowColor = UIConstants.UI_COLOR_SHADOW;
		textColor = UIConstants.UI_COLOR_TEXT;
		titleFont = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.MENU_FONT_TITLE_SIZE);
		borderThickness = UIConstants.MENU_BORDER_THICKNESS;
		shadowDepth = UIConstants.UI_BOX_SHADOW_DEPTH;
		GameManager.addUpdatable(this);
		GameManager.addDrawable(this);
	}
	
	// UPDATE SHAKE
	private void updateShake() {
		xShakeOffset = -(int)(xShakeOffset*SHAKE_DECAY_RATE);
		if (xShakeOffset == 1 || xShakeOffset == -1) xShakeOffset = 0;
	}
	
	// START SHAKE
	protected void startShake() {
		xShakeOffset = SHAKE_BEGIN_OFFSET;
	}
	
	// UPDATE
	public void update() {
		// UPDATE ALPHA
		alpha = UIUtils.getUpdatedAlpha(alpha, isEnabled);
		updateShake();
	}
	
	// DRAW MENU BODY
	protected void drawMenuBody(Graphics2D g2) {
		// APPLY ALPHA
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		// DRAW BORDER
		g2.setColor(borderColor);
		g2.fillRect(x-borderThickness+xShakeOffset, y-borderThickness, width+borderThickness*2+xShakeOffset, height+borderThickness*2);
		g2.setColor(shadowColor);
		g2.fillRect(x+xShakeOffset, y, width+borderThickness+xShakeOffset, height+borderThickness);
		// DRAW BODY
		g2.setColor(bodyColor);
		g2.fillRect(x+xShakeOffset, y, width+xShakeOffset, height);
		// SETUP g2 TO DRAW TITLE
		if (titleText != null) {
			g2.setColor(textColor);
			g2.setFont(titleFont);
			FontMetrics fm = g2.getFontMetrics();
			int titleTextWidth = fm.stringWidth(titleText);
			int titleTextCenterToBaslineOffset = (fm.getAscent()-fm.getDescent())/2;
			int titleDrawX = 0;
			if (titleText != null) switch(titleAlignment) {
			case HorizontalAlignment.LEFT: titleDrawX = x;
			break;
			case HorizontalAlignment.MIDDLE: titleDrawX = x+width/2-titleTextWidth/2;
			break;
			case HorizontalAlignment.RIGHT: titleDrawX = x+width-titleTextWidth;
			break;
			}
			// DRAW TITLE
			UIUtils.drawStringWithShadow(g2, shadowColor, titleText, titleDrawX+xShakeOffset, y-titleTextCenterToBaslineOffset/2);
		}
	}
	
	// SET POSITION
	protected void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	// SET SIZE
	protected void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	// SET TITLE
	protected void setTitle(String title, HorizontalAlignment alignment) {
		titleText = title;
		titleAlignment = alignment;
	}
	
	// SET ENABLED
	protected void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
		for(int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setEnabled(isEnabled);
		}
	}
	
	// ADD BUTTON
	/*
	 * This method AND the addButtonWithMouseTip method are preferred over manually
	 * instantiating a button, as this ensures it gets added to all necessary lists.
	 */
	protected Button addButton(int buttonID, int x, int y, int width, int height, String bodyText, boolean useLargeFont) {
		Button b = new Button(buttonID, x, y, width, height, bodyText, useLargeFont, this);
		GameManager.addUpdatable(b);
		GameManager.addDrawable(b);
		buttons.add(b);
		return b;
	}
	
	// ADD BUTTON WITH MOUSE HOVER TIP
	protected Button addButtonWithMouseHoverTip(int buttonID, int x, int y, int width, int height, String bodyText, boolean useLargeFont, String tipText, HorizontalAlignment tipAlignment) {
		Button b = addButton(buttonID, x, y, width, height, bodyText, useLargeFont);
		b.enableMouseHoverTip(tipText, tipAlignment);
		return b;
	}
	
	// ADD SUB MENU
	protected void addSubMenu(Menu newSubMenu, boolean disableSelf) {
		subMenus.add(newSubMenu);
		if (isEnabled != !disableSelf) {
			setEnabled(!disableSelf);
		}
	}
	
	// CLOSE
	protected void close(AdditionalCloseOperation doWhat) {
		// CLOSE SUBMENUS
		for(int i = 0; i < subMenus.size(); i++) {
			subMenus.get(i).close(AdditionalCloseOperation.NOTHING);
		}
		subMenus.clear();
		// CLOSE BUTTONS
		for(int i = 0; i < buttons.size(); i++) {
			Button b = buttons.get(i);
			GameManager.removeUpdatable(b);
			GameManager.removeDrawable(b);
		}
		buttons.clear();
		// TRIGGER OWNER TO ENABLE OR CLOSE ITS OWENER
		if (owner != null) switch(doWhat) {
		case NOTHING: ;
		break;
		case ENABLE_OWNER: owner.setEnabled(true);
		break;
		case CLOSE_ENTIRE_HIERARCHY: owner.close(AdditionalCloseOperation.CLOSE_ENTIRE_HIERARCHY);
		break;
		}
		GameManager.removeUpdatable(this);
		GameManager.removeDrawable(this);
	}
}