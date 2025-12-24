package ui;

import java.awt.Graphics2D;
import java.awt.Font;
import level.LevelLoader;

public class MenuWarningLevelLoadUnsaved extends Menu {

	// VARS
		// INSTANCE FIELDS
	private String bodyText = "Any unsaved changes will be lost. Do you still want to proceed?";
	private String levelName;
	// MENU PROPERTIES
	private static final Font BODY_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.MENU_FONT_BODY_SIZE);
	private static final String MENU_TITLE = "Warning";
	private static final int MENU_WIDTH = 256;
	private static final int MENU_HEIGHT = 96;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final int BUTTON_ID_CONFIRM = 0;
	private static final int BUTTON_ID_CANCEL = 1;
	private static final int BUTTONS_WIDTH = 96;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTON_CONFIRM_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_CANCEL_X = MENU_X+MENU_WIDTH-BUTTONS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTONS_Y = MENU_Y+MENU_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING-BUTTONS_HEIGHT;
	
	// CONSTRUCTOR
	public MenuWarningLevelLoadUnsaved(Menu owner, String levelName) {
		super(owner);
		this.levelName = levelName;
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle(MENU_TITLE, HorizontalAlignment.MIDDLE);
		addButton(BUTTON_ID_CANCEL, BUTTON_CANCEL_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Cancel", false);
		addButtonWithMouseHoverTip(BUTTON_ID_CONFIRM, BUTTON_CONFIRM_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Load", false, "Proceed with loading this level", HorizontalAlignment.RIGHT);
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
		g2.setColor(textColor);
		g2.setFont(BODY_FONT);
		UIUtils.drawWrappedString(g2, shadowColor, bodyText, MENU_WIDTH, MENU_X, MENU_Y+DEFAULT_BOX_VERTICAL_PADDING);
	}
		
		// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
			case BUTTON_ID_CONFIRM: {
				new LoadingOverlay("Loading level...");
				new MenuEditorTools(null, LevelLoader.loadLevel(levelName), levelName);
				close(AdditionalCloseOperation.CLOSE_ENTIRE_HIERARCHY);
			}
		break;
			case BUTTON_ID_CANCEL: close(AdditionalCloseOperation.ENABLE_OWNER);
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