package ui;

import java.awt.Graphics2D;
import java.util.ArrayList;

import config.BackgroundDefinitionManager;
import level.BackgroundDataHolder;
import level.Level;
import main.GameManager;

public class MenuBackgroundManager extends Menu {

	// VARS
		// MENU PROPERTIES
	private static final String MENU_LABEL = "Background Manager";
	private static final int MENU_WIDTH = 374;
	private static final int MENU_HEIGHT = UIConstants.SCREEN_HEIGHT;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH-MENU_WIDTH;
	private static final int MENU_Y = 0;
	private static final int BUTTONS_WIDTH = 40;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTONS_MENU_X = MENU_X-BUTTONS_WIDTH;
	private static final int BUTTON_ID_CLOSE = -1;
	private static final int BUTTON_ID_ADD = -2;
	private static final int BUTTON_CLOSE_Y = MENU_Y+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_ADD_Y = BUTTON_CLOSE_Y+(int)(BUTTONS_HEIGHT*1.5)+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTONS_BACKGROUND_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTONS_BACKGROUND_WIDTH = MENU_WIDTH-BUTTONS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING*2;
	private static final int BUTTONS_BACKGROUND_HEIGHT = BUTTONS_HEIGHT*3+DEFAULT_BOX_VERTICAL_PADDING*2;
	private static final int BUTTONS_BACKGROUND_OPTIONS_X = BUTTONS_BACKGROUND_X+BUTTONS_BACKGROUND_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTONS_PER_BACKGROUND = 4;
	private static final int BACKGROUND_BUTTON_ID_CONFIGURE = 0;
	private static final int BACKGROUND_BUTTON_ID_UP = 1;
	private static final int BACKGROUND_BUTTON_ID_DELETE = 2;
	private static final int BACKGROUND_BUTTON_ID_DOWN = 3;
	private static final String UP_ARROW = ""+'\u2191';
	private static final String DOWN_ARROW = ""+'\u2193';
		// INSTANCE VARS
	private ArrayList<BackgroundDataHolder> backgrounds;
	private ArrayList<Button> backgroundButtons = new ArrayList<Button>();
	private Level currentLevel;
	private SplashString splashLabel;
	
	// CONSTRUCTOR
	public MenuBackgroundManager(Menu owner, Level currentLevel) {
		super(owner);
		this.currentLevel = currentLevel;
		backgrounds = currentLevel.getBackgrounds();
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		splashLabel = new SplashString(MENU_X, MENU_Y+MENU_HEIGHT/2, UIConstants.UI_COLOR_TEXT, MENU_LABEL, -1, HorizontalAlignment.LEFT, true);
		addButton(BUTTON_ID_CLOSE, BUTTONS_MENU_X, BUTTON_CLOSE_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "X", false);
		addButtonWithMouseHoverTip(BUTTON_ID_ADD, BUTTONS_MENU_X, BUTTON_ADD_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "+", true, "Add a new background", HorizontalAlignment.LEFT);
		refreshBackgroundButtons();
	}

	// REFRESH BACKGROUND BUTTONS
	protected void refreshBackgroundButtons() {
		// REMOVE OLD BUTTONS
		for (int i = 0; i < backgroundButtons.size(); i++) {
			Button b = backgroundButtons.get(i);
			GameManager.removeUpdatable(b);
			GameManager.removeDrawable(b);
			buttons.remove(b);
		}
		backgroundButtons.clear();
		// ADD NEW BUTTONS
		for (int i = 0; i < backgrounds.size(); i++) {
			BackgroundDataHolder bd = backgrounds.get(i);
			if (BackgroundDefinitionManager.getBackgroundDefinitionByID(bd.getID()) != null) {
				backgroundButtons.add(addButtonWithMouseHoverTip(i*BUTTONS_PER_BACKGROUND, BUTTONS_BACKGROUND_X, (BUTTONS_BACKGROUND_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*i, BUTTONS_BACKGROUND_WIDTH, BUTTONS_BACKGROUND_HEIGHT, BackgroundDefinitionManager.getBackgroundDefinitionByID(bd.getID()).getStringName(), true, "Click to configure this background", HorizontalAlignment.LEFT));
				Button upButton = addButtonWithMouseHoverTip(i*BUTTONS_PER_BACKGROUND+BACKGROUND_BUTTON_ID_UP, BUTTONS_BACKGROUND_OPTIONS_X, (BUTTONS_BACKGROUND_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*i, BUTTONS_WIDTH, BUTTONS_HEIGHT, UP_ARROW, true, "Move background up (drawn first)", HorizontalAlignment.LEFT);
				if (i == 0) upButton.setEnabled(false);
				backgroundButtons.add(upButton);
				backgroundButtons.add(addButtonWithMouseHoverTip(i*BUTTONS_PER_BACKGROUND+BACKGROUND_BUTTON_ID_DELETE, BUTTONS_BACKGROUND_OPTIONS_X, (BUTTONS_BACKGROUND_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*i+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING, BUTTONS_WIDTH, BUTTONS_HEIGHT, "-", true, "Delete", HorizontalAlignment.LEFT));
				Button downButton = addButtonWithMouseHoverTip(i*BUTTONS_PER_BACKGROUND+BACKGROUND_BUTTON_ID_DOWN, BUTTONS_BACKGROUND_OPTIONS_X, (BUTTONS_BACKGROUND_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*i+(BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*2, BUTTONS_WIDTH, BUTTONS_HEIGHT, DOWN_ARROW, true, "Move background down (drawn after)", HorizontalAlignment.LEFT);
				if (i == backgrounds.size()-1) downButton.setEnabled(false);
				backgroundButtons.add(downButton);
			}
			else currentLevel.getBackgrounds().remove(i);
		}
	}
	
	// UPDATE
	@Override
	public void update() {
		super.update();
		if (splashLabel != null) splashLabel.setAlpha(alpha);
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
		case BUTTON_ID_CLOSE: close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
		case BUTTON_ID_ADD: addSubMenu(new MenuBackgroundSelect(this, this), true);;
		break;
		default: {
			if (buttonID%BUTTONS_PER_BACKGROUND == BACKGROUND_BUTTON_ID_CONFIGURE) {
				addSubMenu(new MenuBackgroundConfig(this, currentLevel.getBackgrounds().get(buttonID/BUTTONS_PER_BACKGROUND)), true);
			}
			if (buttonID%BUTTONS_PER_BACKGROUND == BACKGROUND_BUTTON_ID_UP) {
				int backgroundButtonIndex = buttonID/BUTTONS_PER_BACKGROUND;
				int newIndex = backgroundButtonIndex-1;
				BackgroundDataHolder bd = currentLevel.getBackgrounds().get(backgroundButtonIndex);
				currentLevel.getBackgrounds().remove(backgroundButtonIndex);
				currentLevel.getBackgrounds().add(newIndex, bd);
				refreshBackgroundButtons();
			}
			if (buttonID%BUTTONS_PER_BACKGROUND == BACKGROUND_BUTTON_ID_DELETE) {
				currentLevel.getBackgrounds().remove(buttonID/BUTTONS_PER_BACKGROUND);
				refreshBackgroundButtons();
			}
			if (buttonID%BUTTONS_PER_BACKGROUND == BACKGROUND_BUTTON_ID_DOWN) {
				int backgroundButtonIndex = buttonID/BUTTONS_PER_BACKGROUND;
				int newIndex = backgroundButtonIndex+1;
				BackgroundDataHolder bd = currentLevel.getBackgrounds().get(backgroundButtonIndex);
				currentLevel.getBackgrounds().remove(backgroundButtonIndex);
				currentLevel.getBackgrounds().add(newIndex, bd);
				refreshBackgroundButtons();
			}
		}
		break;
		}
	}
	
	// ADD BACKGROUND
	public void addBackground(int id) {
		backgrounds.add(new BackgroundDataHolder(id, 0, 0, 0, 0, 1));
		refreshBackgroundButtons();
	}
	
	// CLOSE
	@Override
	protected void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		if (splashLabel != null) splashLabel.remove();;
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