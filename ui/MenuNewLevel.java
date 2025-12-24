package ui;

import main.Inputs;
import level.Level;
import java.awt.Color;
import java.awt.Graphics2D;

public class MenuNewLevel extends Menu implements StringInputFieldListener {
	
	// VARS
		// MENU PROPERTIES
	private static final String MAIN_MENU_TITLE = "Enter level dimensions:";
	private static final int MENU_WIDTH = 336;
	private static final int MENU_HEIGHT = 165;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
		// BUTTONS PROPERTIES
	private static final int BUTTON_ID_CREATE = 0;
	private static final int BUTTON_ID_RESET = 1;
	private static final int BUTTON_ID_CANCEL = 2;
	private static final int BUTTONS_WIDTH = 96;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTON_CREATE_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_RESET_X = MENU_X+MENU_WIDTH/2-BUTTONS_WIDTH/2;
	private static final int BUTTON_CANCEL_X = MENU_X+MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING-BUTTONS_WIDTH;
	private static final int BUTTONS_Y = MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING;
		// INPUT FIELDS PROPERTIES
	private static final int INPUT_INDEX_WIDTH = 0;
	private static final int INPUT_INDEX_HEIGHT = 1;
	private static final int INPUT_INDEX_LAYERS = 2;
	private static final int INPUT_INDEX_FINISHED = 3;
	private static final int INPUT_FIELDS_WIDTH = 99;
	private static final int INPUT_FIELDS_Y = MENU_Y+UIConstants.MENU_FONT_TITLE_SIZE;
	private static final int INPUT_FIELD_WIDTH_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int INPUT_FIELD_HEIGHT_X = MENU_X+MENU_WIDTH/2-INPUT_FIELDS_WIDTH/2;
	private static final int INPUT_FIELD_LAYERS_X = MENU_X+MENU_WIDTH-INPUT_FIELDS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
		// INPUT FIELD VARS
	private Button createButton;
	private MenuStringInputField[] inputFields = new MenuStringInputField[3];
	private int[] levelDimensionsData = new int[3];
	private int currentInputField = INPUT_INDEX_WIDTH;
	private SplashString currentSplashString = null;
	
	// CONSTRUCTOR
	public MenuNewLevel(Menu owner) {
		super(owner);
		setTitle(MAIN_MENU_TITLE, HorizontalAlignment.LEFT);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		addButton(BUTTON_ID_CANCEL, BUTTON_CANCEL_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Cancel", false);
		addButtonWithMouseHoverTip(BUTTON_ID_RESET, BUTTON_RESET_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Reset", false, "Reset input fields and restart", HorizontalAlignment.RIGHT);
		createButton = addButtonWithMouseHoverTip(BUTTON_ID_CREATE, BUTTON_CREATE_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Create", false, "Create a new level with these dimensions", HorizontalAlignment.RIGHT);
		createButton.setEnabled(false);
		resetInputBoxes();
	}

	// RESET INPUT BOXES
	private void resetInputBoxes() {
		if (inputFields[INPUT_INDEX_WIDTH] != null) inputFields[INPUT_INDEX_WIDTH].close(AdditionalCloseOperation.NOTHING);
		if (inputFields[INPUT_INDEX_HEIGHT] != null) inputFields[INPUT_INDEX_HEIGHT].close(AdditionalCloseOperation.NOTHING);
		if (inputFields[INPUT_INDEX_LAYERS] != null) inputFields[INPUT_INDEX_LAYERS].close(AdditionalCloseOperation.NOTHING);
		inputFields[INPUT_INDEX_WIDTH] = new MenuStringInputField(this, "Width", INPUT_FIELD_WIDTH_X, INPUT_FIELDS_Y, INPUT_FIELDS_WIDTH, 5, true, false, this);
		inputFields[INPUT_INDEX_WIDTH].setEnabled(true);
		Inputs.setTypedInputListener(inputFields[INPUT_INDEX_WIDTH]);
		addSubMenu(inputFields[INPUT_INDEX_WIDTH], false);
		inputFields[INPUT_INDEX_HEIGHT] = new MenuStringInputField(this, "Height", INPUT_FIELD_HEIGHT_X, INPUT_FIELDS_Y, INPUT_FIELDS_WIDTH, 5, true, false, this);
		addSubMenu(inputFields[INPUT_INDEX_HEIGHT], false);
		inputFields[INPUT_INDEX_LAYERS] = new MenuStringInputField(this, "Layers", INPUT_FIELD_LAYERS_X, INPUT_FIELDS_Y, INPUT_FIELDS_WIDTH, 2, true, false, this);
		addSubMenu(inputFields[INPUT_INDEX_LAYERS], false);
		currentInputField = INPUT_INDEX_WIDTH;
		levelDimensionsData = new int[3];
		createButton.setEnabled(false);
		if (currentSplashString != null) currentSplashString.remove();
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
	}

	// CREATE LEVEL
	private void createLevel() {
		currentSplashString.remove();
		new LoadingOverlay("Creating level...");
		close(AdditionalCloseOperation.CLOSE_ENTIRE_HIERARCHY);
		Level currentLevel = new Level(Math.max(levelDimensionsData[INPUT_INDEX_WIDTH], 1), Math.max(levelDimensionsData[INPUT_INDEX_HEIGHT], 1), Math.max(levelDimensionsData[INPUT_INDEX_LAYERS], 1));
		new MenuEditorTools(null, currentLevel, null);
	}
	
	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch (buttonID) {
			case BUTTON_ID_CREATE: createLevel();
		break;
			case BUTTON_ID_RESET: ; resetInputBoxes();
		break;
			case BUTTON_ID_CANCEL: {
				if (currentSplashString != null) currentSplashString.remove();
				close(AdditionalCloseOperation.ENABLE_OWNER);
			}
		}
	}

	// ATTEMPTED INVALID INPUT
	@Override
	public void attemptInvalidInput(int errorID) {
		switch(errorID) {
		case StringInputFieldListener.ERROR_EMPTY: {
			if (currentSplashString != null) currentSplashString.remove();
			currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Current field is empty!", 0.75f, HorizontalAlignment.MIDDLE, false);
			startShake();
		}
		break;
		case StringInputFieldListener.ERROR_FULL: {
			if (currentSplashString != null) currentSplashString.remove();
			currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Current field is full!", 0.75f, HorizontalAlignment.MIDDLE, false);
			startShake();
		}
		break;
		case StringInputFieldListener.ERROR_LETTER: {
			if (currentSplashString != null) currentSplashString.remove();
			currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Only numbers are accepted!", 0.75f, HorizontalAlignment.MIDDLE, false);
			startShake();
		}
		break;
		}
	}

	// STRING FINISHED
	@Override
	public void stringFinished(String finalString) {
		levelDimensionsData[currentInputField] = Integer.valueOf(finalString);
		currentInputField++;
		if (currentInputField < INPUT_INDEX_FINISHED) {
			inputFields[currentInputField].setEnabled(true);
			Inputs.setTypedInputListener(inputFields[currentInputField]);
		}
		else {
			Inputs.setTypedInputListener(null);
			if (currentSplashString != null) currentSplashString.remove();
			currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.GREEN, "Press create if this is correct!", -1, HorizontalAlignment.MIDDLE, false);
			createButton.setEnabled(true);
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