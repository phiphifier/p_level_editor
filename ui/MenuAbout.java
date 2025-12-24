package ui;

import java.awt.Graphics2D;
import java.awt.Font;

public class MenuAbout extends Menu {

	// VARS
		// MENU PROPERTIES
	private static final int MENU_WIDTH = 304;
	private static final int MENU_HEIGHT = 208;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final String MENU_TITLE = "PLevel Editor Info";
	private static final String EXPLANATION = "Indev\nCode and art by phiphifier.\n\nThis is the dedicated editor for creating and modifying .plvl format files.";
	private static final Font BODY_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.PLAIN, UIConstants.MENU_FONT_BODY_SIZE);
		// BUTTON PROPERTIES
	private static final int BUTTON_ID_CLOSE = 0;
	private static final int BUTTON_VERTICAL_PADDING = UIConstants.UI_BOX_SHADOW_DEPTH+2;
	private static final int BUTTON_HORIZONTAL_PADDING = 2;
	private static final int BUTTON_WIDTH = 80;
	private static final int BUTTON_HEIGHT = 32;
	private static final int BUTTON_X = MENU_X+MENU_WIDTH-BUTTON_WIDTH-BUTTON_HORIZONTAL_PADDING;
	private static final int BUTTON_Y = MENU_Y+MENU_HEIGHT-BUTTON_HEIGHT-BUTTON_VERTICAL_PADDING;
	
	// CONSTRUCTOR
	public MenuAbout(Menu owner) {
		super(owner);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle(MENU_TITLE, HorizontalAlignment.MIDDLE);
		addButton(BUTTON_ID_CLOSE, BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, "Close", false);
	}

	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
		// GET FONT INFO
		g2.setFont(BODY_FONT);
		UIUtils.drawWrappedString(g2, shadowColor, EXPLANATION, MENU_WIDTH-BODY_TEXT_HORIZONTAL_PADDING, MENU_X+BODY_TEXT_HORIZONTAL_PADDING, MENU_Y+BODY_TEXT_VERTICAL_PADDING);
	}

	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
		case BUTTON_ID_CLOSE: close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
		}
	}

	// UNUSED
	@Override
	public void buttonMoused(int buttonID) {}
	@Override
	public void buttonUnMoused(int buttonID) {}
	@Override
	public void buttonLeftPressed(int buttonID) {}
	@Override
	public void buttonRightPressed(int buttonID) {}
	@Override
	public void buttonRightReleased(int buttonID) {}
}