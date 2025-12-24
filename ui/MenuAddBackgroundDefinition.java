package ui;

import main.Inputs;
import config.BackgroundDefinitionManager;
import java.awt.Color;
import java.awt.Graphics2D;

public class MenuAddBackgroundDefinition extends Menu implements StringInputFieldListener {

	// VARS
		// INSTANCE FIELDS
	private Button createButton;
	private MenuStringInputField inputFieldName;
	private MenuStringInputField inputFieldID;
	private String currentName;
	private int currentID;
	private SplashString currentSplashString;
		// MENU PROPERTIES
	private static final String MENU_TITLE = "Enter background details:";
	private static final int MENU_WIDTH = 304;
	private static final int MENU_HEIGHT = 165;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final int BUTTON_ID_CREATE = 0;
	private static final int BUTTON_ID_RESET = 1;
	private static final int BUTTON_ID_CANCEL = 2;
	private static final int BUTTONS_WIDTH = 96;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTON_CREATE_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_RESET_X = MENU_X+MENU_WIDTH/2-BUTTONS_WIDTH/2;
	private static final int BUTTON_CANCEL_X = MENU_X+MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING-BUTTONS_WIDTH;
	private static final int BUTTONS_Y = MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING;
	private static final int INPUT_FIELDS_WIDTH = MENU_WIDTH/2-DEFAULT_BOX_HORIZONTAL_PADDING*3;
	private static final int INPUT_FIELDS_Y = MENU_Y+UIConstants.MENU_FONT_TITLE_SIZE;
	private static final int INPUT_FIELD_NAME_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int INPUT_FIELD_ID_X = MENU_X+MENU_WIDTH-INPUT_FIELDS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
	
	// CONSTRUCTOR
	public MenuAddBackgroundDefinition(Menu owner) {
		super(owner);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle(MENU_TITLE, HorizontalAlignment.LEFT);
		addButton(BUTTON_ID_CANCEL, BUTTON_CANCEL_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Cancel", false);
		addButtonWithMouseHoverTip(BUTTON_ID_RESET, BUTTON_RESET_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Reset", false, "Reset the input fields and restart", HorizontalAlignment.RIGHT);
		createButton = addButtonWithMouseHoverTip(BUTTON_ID_CREATE, BUTTON_CREATE_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Create", false, "Add this background definition", HorizontalAlignment.RIGHT);
		createInputFields();
	}

	// CREATE INPUT FIELDS
	private void createInputFields() {
		createButton.setEnabled(false);
		if (inputFieldName != null) inputFieldName.close(AdditionalCloseOperation.NOTHING);
		inputFieldName = new MenuStringInputField(this, "Name", INPUT_FIELD_NAME_X, INPUT_FIELDS_Y, INPUT_FIELDS_WIDTH, 24, false, true, this);
		inputFieldName.setEnabled(true);
		Inputs.setTypedInputListener(inputFieldName);
		addSubMenu(inputFieldName, false);
		if (inputFieldID != null) inputFieldID.close(AdditionalCloseOperation.NOTHING);
		inputFieldID = new MenuStringInputField(this, "ID", INPUT_FIELD_ID_X, INPUT_FIELDS_Y, INPUT_FIELDS_WIDTH, 5, true, false, this);
		addSubMenu(inputFieldID, false);
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
			case BUTTON_ID_CREATE: {
				if (currentSplashString != null) currentSplashString.remove();
				if (BackgroundDefinitionManager.addNewBackgroundDefinition(currentName, currentID)) {
					currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.GREEN, "Defintion added!", 1.5f, HorizontalAlignment.MIDDLE, false);
					MenuBackgroundSelect myOwner = (MenuBackgroundSelect)owner;
					myOwner.updateBackgroundDefinitionButtons();
					createInputFields();
				}
				else {
					currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "That ID already exists!", 1.5f, HorizontalAlignment.MIDDLE, false);
					startShake();
				}
			}
		break;
			case BUTTON_ID_RESET: {
				createInputFields();
				if (currentSplashString != null) currentSplashString.remove();
			}
		break;
			case BUTTON_ID_CANCEL: close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
		}
	}

	// ATTEMPTED INVALID INPUT
	@Override
	public void attemptInvalidInput(int errorID) {
		if (currentSplashString != null) currentSplashString.remove();
		switch(errorID) {
			case StringInputFieldListener.ERROR_EMPTY: {
				currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Current field is empty!", 0.75f, HorizontalAlignment.MIDDLE, false);
				startShake();
			}
		break;
			case StringInputFieldListener.ERROR_FULL: {
				currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Current field is full!", 0.75f, HorizontalAlignment.MIDDLE, false);
				startShake();
			}
		break;
			case StringInputFieldListener.ERROR_LETTER: {
				currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Only numbers are accepted!", 0.75f, HorizontalAlignment.MIDDLE, false);
				startShake();
			}
		break;
		}
	}

	// STRING FINISHED
	@Override
	public void stringFinished(String finalString) {
		if (Inputs.getTypedInputListener() == inputFieldName) {
			currentName = finalString;
			Inputs.setTypedInputListener(inputFieldID);
			inputFieldID.setEnabled(true);
		}
		else {
			Inputs.setTypedInputListener(null);
			currentID = Integer.valueOf(finalString);
			createButton.setEnabled(true);
			if (currentSplashString != null) currentSplashString.remove();
			currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.GREEN, "Press create if this is correct!", -1, HorizontalAlignment.MIDDLE, false);
		}
	}

	// CLOSE
	@Override
	public void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		if (currentSplashString != null) currentSplashString.remove();
		createButton = null;
		inputFieldName = null;
		inputFieldID = null;
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