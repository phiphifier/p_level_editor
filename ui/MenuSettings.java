package ui;

import java.awt.Graphics2D;

public class MenuSettings extends Menu {

	// VARS
		// MENU PROPERTIES
	private static final int BUTTONS_WIDTH = 256;
	private static final int BUTTONS_HEIGHT = 48;
	private static final int BUTTON_ID_CHANGE_SAVE_LOCATION = 0;
	private static final int BUTTON_ID_MODIFY_ENTITY_DEFINITIONS = 1;
	private static final int BUTTON_ID_MODIFY_BACKGROUND_DEFINITIONS = 2;
	private static final int BUTTON_ID_CLOSE = 3;
	private static final int MENU_WIDTH = BUTTONS_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING*4;
	private static final int MENU_HEIGHT = (BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*4+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final String MENU_TITLE = "Settings";
	private static final int BUTTONS_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING*2;
	private static final int BUTTON_CHANGE_SAVE_LOCATION_Y = MENU_Y+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_MODIFY_ENTITY_DEFINITIONS_Y = BUTTON_CHANGE_SAVE_LOCATION_Y+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_MODIFY_BACKGROUND_DEFINITIONS_Y = BUTTON_MODIFY_ENTITY_DEFINITIONS_Y+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_CLOSE_Y = BUTTON_MODIFY_BACKGROUND_DEFINITIONS_Y+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	
	// CONSTRUCTOR
	public MenuSettings(Menu owner) {
		super(owner);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle(MENU_TITLE, HorizontalAlignment.MIDDLE);
		addButtonWithMouseHoverTip(BUTTON_ID_CHANGE_SAVE_LOCATION, BUTTONS_X, BUTTON_CHANGE_SAVE_LOCATION_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Level Storage Location", false, "Specify a new directory for saving/loading levels", HorizontalAlignment.RIGHT);
		addButtonWithMouseHoverTip(BUTTON_ID_MODIFY_ENTITY_DEFINITIONS, BUTTONS_X, BUTTON_MODIFY_ENTITY_DEFINITIONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Entity Definition List", false, "Add or remove entity definitions", HorizontalAlignment.RIGHT);
		addButtonWithMouseHoverTip(BUTTON_ID_MODIFY_BACKGROUND_DEFINITIONS, BUTTONS_X, BUTTON_MODIFY_BACKGROUND_DEFINITIONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Background Definition List", false, "Add or remove background definitions", HorizontalAlignment.RIGHT);
		addButton(BUTTON_ID_CLOSE, BUTTONS_X, BUTTON_CLOSE_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Close", false);
	}

	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
	}

	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
		case BUTTON_ID_CHANGE_SAVE_LOCATION: addSubMenu(new MenuChangeLevelPath(this, true), true);;
		break;
		case BUTTON_ID_MODIFY_ENTITY_DEFINITIONS: addSubMenu(new MenuEntitySelect(this, null), true);
		break;
		case BUTTON_ID_MODIFY_BACKGROUND_DEFINITIONS: addSubMenu(new MenuBackgroundSelect(this, null), true);
		break;
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