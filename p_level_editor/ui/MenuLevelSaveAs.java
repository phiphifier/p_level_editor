package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import config.Config;
import level.Level;
import level.LevelLoader;
import level.LevelSaver;
import main.Inputs;

public class MenuLevelSaveAs extends Menu implements StringInputFieldListener {

	// VARS
		// INSTANCE FIELDS
	private MenuStringInputField inputField;
	private Button saveButton;
	private SplashString splashString;
	private Level currentLevel;
	private String finishedInput;
		// MENU PROPERTIES
	private static final String LEVEL_FILE_FORMAT = ".plvl";
	private static final String MENU_TITLE = "Save as:";
	private static final int MENU_WIDTH = 320;
	private static final int MENU_HEIGHT = 140;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final int BUTTON_ID_SAVE = 0;
	private static final int BUTTON_ID_RESET = 1;
	private static final int BUTTON_ID_CANCEL = 3;
	private static final int BUTTONS_WIDTH = 96;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTON_SAVE_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_RESET_X = MENU_X+MENU_WIDTH/2-BUTTONS_WIDTH/2;
	private static final int BUTTON_CANCEL_X = MENU_X+MENU_WIDTH-BUTTONS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTONS_Y = MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING;
	private static final int INPUT_FIELD_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int INPUT_FIELD_Y = MENU_Y+DEFAULT_BOX_VERTICAL_PADDING*3;
	
	// CONSTRUCTOR
	public MenuLevelSaveAs(Menu owner, Level currentLevel) {
		super(owner);
		this.currentLevel = currentLevel;
		setTitle(MENU_TITLE, HorizontalAlignment.LEFT);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		addButton(BUTTON_ID_CANCEL, BUTTON_CANCEL_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Cancel", false);
		addButtonWithMouseHoverTip(BUTTON_ID_RESET, BUTTON_RESET_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Reset", false, "Reset the text field and start over", HorizontalAlignment.RIGHT);
		saveButton = addButton(BUTTON_ID_SAVE, BUTTON_SAVE_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Save", false);
		saveButton.setEnabled(false);
		resetMenu();
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
		case BUTTON_ID_SAVE: savePressed();
		break;
		case BUTTON_ID_RESET: resetMenu();
		break;
		case BUTTON_ID_CANCEL: close(AdditionalCloseOperation.ENABLE_OWNER);
		}
	}

	// STRING INPUT FINISHED
	@Override
	public void stringFinished(String finalString) {
		finishedInput = finalString+LEVEL_FILE_FORMAT;
		String[] levelNames = LevelLoader.getLevelInFileNames(Config.getProperty(Config.PROPERTY_LEVEL_STORAGE_PATH));
		boolean matchFound = false;
		for (int i = 0; i < levelNames.length; i++) {
			if (levelNames[i].equals(finishedInput)) matchFound = true;
		}
		saveButton.setEnabled(true);
		Inputs.setTypedInputListener(null);
		if (splashString != null) splashString.remove();
		splashString = new SplashString(MENU_X+MENU_WIDTH/2, BUTTONS_Y-BUTTONS_HEIGHT/2, Color.GREEN, "Press create to save this level!", -1, HorizontalAlignment.MIDDLE, false);
		if (matchFound) {
			addSubMenu(new MenuWarningLevelExists(this), true);
		}
	}
	
	// SAVE PRESSED
	private void savePressed() {
		new LoadingOverlay("Saving level...");
		LevelSaver.saveLevelToFile(currentLevel, finishedInput);
		MenuEditorTools myOwner = (MenuEditorTools)owner;
		myOwner.setLevelName(finishedInput);
		close(AdditionalCloseOperation.ENABLE_OWNER);
	}
	
	// SETUP MENU
	private void resetMenu() {
		saveButton.setEnabled(false);
	if (inputField != null) inputField.close(AdditionalCloseOperation.NOTHING);
	inputField = new MenuStringInputField(owner, "Name:", INPUT_FIELD_X, INPUT_FIELD_Y, MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING*2, -1, false, false, this);
	addSubMenu(inputField, false);
	inputField.setEnabled(true);
	Inputs.setTypedInputListener(inputField);
	finishedInput = "";
	if (splashString != null) splashString.remove();
	}

	// CLOSE
	@Override
	public void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		if (splashString != null) splashString.remove();
	}
	
	// ATTEMPTED FINISHED INPUT
	@Override
	public void attemptInvalidInput(int errorID) {
		startShake();
		switch (errorID) {
		case StringInputFieldListener.ERROR_EMPTY: splashString = new SplashString(MENU_X+MENU_WIDTH/2, BUTTONS_Y-BUTTONS_HEIGHT/2, Color.RED, "Input field can't be empty!", 0.75f, HorizontalAlignment.MIDDLE, false);
		break;
		case StringInputFieldListener.ERROR_FILE_PATH_CHAR: splashString = new SplashString(MENU_X+MENU_WIDTH/2, BUTTONS_Y-BUTTONS_HEIGHT/2, Color.RED, "Can't contain /, \\, :, *, ?, \", <, >!", 0.75f, HorizontalAlignment.MIDDLE, false);
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