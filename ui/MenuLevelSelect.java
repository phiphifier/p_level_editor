package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import config.Config;
import main.GameManager;
import main.Inputs;
import level.LevelLoader;

public class MenuLevelSelect extends Menu {

	// VARS
		// INSTANCE FIELDS
	private ArrayList<Button> levelEntryButtons = new ArrayList<Button>();
	private String[] levelEntryNames;
	private boolean scrollingWithBar = false;
	private int mouseYScrollOffset = 0;
	private int totalScrollableZoneHeight = 0;
	private double scrollPixelsPerSliderPixel = 0;
	private SplashString splashLabel;
	private Button scrollBar;
	private boolean removingLevels = false;
	private Button removeLevelsButton;
		// MENU PROPERTIES
	private static final int FILE_FORMAT_STRING_LENGTH = ".plvl".length();
	private static final String NORMAL_LABEL = "Choose a level to load";
	private static final String REMOVING_LEVELS_LABEL = "Choose which level to delete";
	private static final String CHAR_CHECK_BOX = Character.toString('\u2611');
	private static final String CHAR_REMOVE = "-";
	private static final int MENU_X = 0;
	private static final int MENU_Y = 0;
	private static final int MENU_WIDTH = 256;
	private static final int MENU_HEIGHT = UIConstants.SCREEN_HEIGHT-UIConstants.MENU_BORDER_THICKNESS/2;
	private static final int BUTTON_ID_CLOSE = -1;
	private static final int BUTTON_ID_REMOVE = -2;
	private static final int BUTTON_ID_SCROLL_BAR = -3;
	private static final int BUTTON_SCROLL_BAR_WIDTH = 10;
	private static final int BUTTON_SCROLL_BAR_SLIDER_HEIGHT = UIConstants.SCREEN_HEIGHT;
	private static final int BUTTON_SCROLL_BAR_START_Y = 0;
	private static final int BUTTONS_WIDTH = 40;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTONS_X = MENU_X+MENU_WIDTH;
	private static final int BUTTON_CLOSE_Y = MENU_Y+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_REMOVE_Y = BUTTON_CLOSE_Y+(int)(BUTTONS_HEIGHT*1.5)+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTONS_LEVEL_ENTRY_WIDTH = MENU_WIDTH-BUTTON_SCROLL_BAR_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING*4;
	private static final int BUTTONS_LEVEL_ENTRY_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_SCROLL_BAR_X = MENU_WIDTH-BUTTON_SCROLL_BAR_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTONS_LEVEL_LABEL_TEXT_SIZE = 18;

	// CONSTRUCTOR
	public MenuLevelSelect(Menu owner, MenuEditorTools editorTools) {
		super(owner);
		splashLabel = new SplashString(MENU_X+MENU_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, MENU_HEIGHT/2, Color.WHITE, NORMAL_LABEL, -1, HorizontalAlignment.RIGHT, true);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		addButton(BUTTON_ID_CLOSE, BUTTONS_X, BUTTON_CLOSE_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "X", false);
		removeLevelsButton = addButtonWithMouseHoverTip(BUTTON_ID_REMOVE, BUTTONS_X, BUTTON_REMOVE_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "-", true, "Toggle click to delete a level", HorizontalAlignment.RIGHT);
		scrollBar = addButton(BUTTON_ID_SCROLL_BAR, BUTTON_SCROLL_BAR_X, BUTTON_SCROLL_BAR_START_Y, BUTTON_SCROLL_BAR_WIDTH, BUTTON_SCROLL_BAR_SLIDER_HEIGHT, "", false);
		scrollBar.setStickyMouse(true);
		resetLevelEntryButtons();
		
	}
	
	// RESET LEVEL ENTRY BUTTONS
	protected void resetLevelEntryButtons() {
		if (removingLevels) removePressed();
		// CLEAR MENU STATE
		if (!levelEntryButtons.isEmpty()) for (int i = 0; i < levelEntryButtons.size(); i++) {
			Button b = levelEntryButtons.get(i);
			GameManager.removeUpdatable(b);
			GameManager.removeDrawable(b);
		}
		levelEntryButtons.clear();
		// GET AND CREATE BUTTONS
		levelEntryNames = LevelLoader.getLevelInFileNames(Config.getProperty(Config.PROPERTY_LEVEL_STORAGE_PATH));
		int buttonTotalHeight = BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		for (int i = 0; i < levelEntryNames.length; i++) {
			Button b = addButton(i, BUTTONS_LEVEL_ENTRY_X, buttonTotalHeight*i, BUTTONS_LEVEL_ENTRY_WIDTH, BUTTONS_HEIGHT, getLevelButtonLabel(levelEntryNames[i]), false);
			b.setBodyTextSize(BUTTONS_LEVEL_LABEL_TEXT_SIZE);
			levelEntryButtons.add(b);
		}
		// RESET SCROLL BAR
		int newScrollBarHeight = Math.min((int)(MENU_HEIGHT*((double)MENU_HEIGHT/((double)levelEntryButtons.size()*(BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)))), MENU_HEIGHT);
		scrollBar.setHeight(newScrollBarHeight);
		scrollBar.setY(0);
		totalScrollableZoneHeight = levelEntryButtons.size()*(BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)-MENU_HEIGHT;
		int sliderHeight = MENU_HEIGHT-scrollBar.getHeight();
		if (sliderHeight > 0 && totalScrollableZoneHeight > 0) scrollPixelsPerSliderPixel = (double)totalScrollableZoneHeight/(double)sliderHeight;
	}
	
	// GET LEVEL BUTTON NAME
	private String getLevelButtonLabel(String levelFileName) {
		String buttonLabel = levelFileName.substring(0, levelFileName.length()-FILE_FORMAT_STRING_LENGTH);
		buttonLabel = buttonLabel.concat(" ("+LevelLoader.getLevelFileSizeInString(Config.getProperty(Config.PROPERTY_LEVEL_STORAGE_PATH), levelFileName).toString()+")");
		return buttonLabel;
	}
	
	// UPDATE SCROLL BAR
	private void updateScrollBar() {
		int buttonEntireHeight = BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		if (scrollingWithBar) {
			if (Inputs.getMouseY() != scrollBar.getY()+mouseYScrollOffset) {
				scrollBar.setY(Math.max(Math.min(Inputs.getMouseY()-mouseYScrollOffset, MENU_HEIGHT-scrollBar.getHeight()), MENU_Y));
				for (int i = 0; i < levelEntryButtons.size(); i++) {
					Button b = levelEntryButtons.get(i);
					int newButtonY = (int)(buttonEntireHeight*i-(scrollBar.getY()*scrollPixelsPerSliderPixel));
					b.setY(newButtonY);
				}
			}
		}
	}

	// UPDATE
	@Override
	public void update() {
		super.update();
		splashLabel.setAlpha(alpha);
		updateScrollBar();
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
		g2.setColor(Color.BLACK);
		g2.fillRect(BUTTON_SCROLL_BAR_X+2, 0, BUTTON_SCROLL_BAR_WIDTH-4, MENU_HEIGHT);
	}

	// BUTTON PRESSED
	@Override
	public void buttonLeftPressed(int buttonID) {
		if (buttonID == BUTTON_ID_SCROLL_BAR) startScrollBar();
	}
	
	// ENABLE REMOVING ENTITIES
	private void removePressed() {
		if (!removingLevels) {
			removingLevels = true;
			splashLabel.remove();
			splashLabel = new SplashString(MENU_X+MENU_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, MENU_HEIGHT/2, Color.RED, REMOVING_LEVELS_LABEL, -1, HorizontalAlignment.RIGHT, true);
			removeLevelsButton.setBodyText(CHAR_CHECK_BOX);
		}
		else {
			removingLevels = false;
			splashLabel.remove();
			splashLabel = new SplashString(MENU_X+MENU_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, MENU_HEIGHT/2, Color.WHITE, NORMAL_LABEL, -1, HorizontalAlignment.RIGHT, true);
			removeLevelsButton.setBodyText(CHAR_REMOVE);
		}
	}

	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
			case BUTTON_ID_CLOSE: close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
			case BUTTON_ID_REMOVE: ; removePressed();
		break;
			case BUTTON_ID_SCROLL_BAR: stopScrollBar();
		break;
			default: if (!scrollingWithBar) {
				if (!removingLevels) {
					if (owner instanceof MenuEditorTools) addSubMenu(new MenuWarningLevelLoadUnsaved(this, levelEntryNames[buttonID]), true);
					else {
						new LoadingOverlay("Loading level...");
						close(AdditionalCloseOperation.CLOSE_ENTIRE_HIERARCHY);
						new MenuEditorTools(null, LevelLoader.loadLevel(levelEntryNames[buttonID]), levelEntryNames[buttonID]);
					}
				}
				else {
					addSubMenu(new MenuWarningDeleteLevel(this, levelEntryNames[buttonID]), true);
				}
			}
		}
	}
	
	// START SCROLL BAR
	public void startScrollBar() {
		mouseYScrollOffset = Inputs.getMouseY()-scrollBar.getY();
		scrollingWithBar = true;
	}
	
	// STOP SCROLL BAR
	private void stopScrollBar() {
		scrollingWithBar = false;
	}
	
	// CLOSE
	@Override
	public void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		if (splashLabel != null) splashLabel.remove();
		splashLabel = null;
		if (Inputs.getScrollInputListener() == this) Inputs.setScrollInputListener(null);
	}

	// UNUSED
	@Override
	public void buttonMoused(int buttonID) {}
	@Override
	public void buttonUnMoused(int buttonID) {}
	@Override
	public void buttonRightPressed(int buttonID) {}
	@Override
	public void buttonRightReleased(int buttonID) {}
}