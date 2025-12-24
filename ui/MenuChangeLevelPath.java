package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import config.Config;
import main.Inputs;

public class MenuChangeLevelPath extends Menu implements StringInputFieldListener {

	// VARS
		// INSTANCE FIELDS
	private Button finishButton;
	private Button cancelButton;
	private MenuStringInputField textInput;
	private SplashString splashString;
	private String inputPath = null;
		// MENU PROPERTIES
	private static final int MENU_WIDTH = (int)(UIConstants.SCREEN_WIDTH*0.75);
	private static final int MENU_HEIGHT = 480;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2- MENU_HEIGHT/2;
	private static final String MENU_TITLE = "Specify a folder path for level storage:";
	private static final int BUTTON_ID_FINISH = 0;
	private static final int BUTTON_ID_CANCEL = 1;
	private static final int BUTTON_ID_CLEAR = 2;
	private static final int BUTTONS_WIDTH = 96;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTON_FINISH_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_CANCEL_X = MENU_X+MENU_WIDTH-BUTTONS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_CLEAR_X = MENU_X+MENU_WIDTH/2-BUTTONS_WIDTH/2;
	private static final int BUTTONS_Y = MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING;
	private static final int TEXT_FIELD_WIDTH = MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING*2;
	private static final int TEXT_FIELD_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int TEXT_FIELD_Y = MENU_Y+DEFAULT_BOX_VERTICAL_PADDING;
	private static final Font MENU_BODY_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.PLAIN, UIConstants.MENU_FONT_BODY_SIZE);
	private static final String MENU_BODY_TEXT = "-Paths are case sensitive.\n\n-The folder you specify must exist. If it doesn't, navigate to the intended directory and create it via your file manager.\n\n-Paths entered are relative to your home directory, remember to omit that part.\n\nINCORRECT EXAMPLE INCLUDING HOME DIRECTORY:\n[User]/Documents/folder\n\nCORRECT EXAMPLE STARTING FROM HOME DIRECTORY:\nDocuments/folder";
	
	// CONSTRUCTOR
	public MenuChangeLevelPath(Menu owner, boolean allowCancel) {
		super(owner);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle(MENU_TITLE, HorizontalAlignment.LEFT);
		cancelButton = addButton(BUTTON_ID_CANCEL, BUTTON_CANCEL_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Cancel", false);
		cancelButton.setEnabled(allowCancel);
		addButtonWithMouseHoverTip(BUTTON_ID_CLEAR, BUTTON_CLEAR_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Clear", false, "Reset the text field", HorizontalAlignment.RIGHT);
		finishButton = addButtonWithMouseHoverTip(BUTTON_ID_FINISH, BUTTON_FINISH_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Finish", false, "Set the saved level directory to the specified folder", HorizontalAlignment.RIGHT);
		finishButton.setEnabled(false);
		reset();
	}
	
	// RESET BUTTON
	private void reset() {
		if (textInput != null) textInput.close(AdditionalCloseOperation.NOTHING);
		textInput = new MenuStringInputField(this, "", TEXT_FIELD_X, TEXT_FIELD_Y, TEXT_FIELD_WIDTH, -1, false, true, this);
		textInput.setEnabled(true);
		addSubMenu(textInput, false);
		Inputs.setTypedInputListener(textInput);
		inputPath = null;
		if (splashString != null) splashString.remove();
		finishButton.setEnabled(false);
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
		g2.setColor(UIConstants.UI_COLOR_TEXT);
		g2.setFont(MENU_BODY_FONT);
		UIUtils.drawWrappedString(g2, UIConstants.UI_COLOR_SHADOW, MENU_BODY_TEXT, MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING, MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING, (int)(MENU_Y+BUTTONS_HEIGHT*2.25));
	}

	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
			case BUTTON_ID_FINISH: {
				Config.setProperty(Config.PROPERTY_FIRST_PROMPT_SHOWN, "true");
				Config.setProperty(Config.PROPERTY_LEVEL_STORAGE_PATH, inputPath);
				close(AdditionalCloseOperation.ENABLE_OWNER);
			}
		break;
			case BUTTON_ID_CANCEL: close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
			case BUTTON_ID_CLEAR: reset();
		break;
		}
	}

	// ATTEMPED INVALID INPUT
	@Override
	public void attemptInvalidInput(int errorID) {
		// The only possible error in this case is input empty
		if (splashString != null) splashString.remove();
		splashString = new SplashString(MENU_X+MENU_WIDTH/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Input field is empty!", 0.85f, HorizontalAlignment.MIDDLE, false);
		startShake();
	}

	// STRING INPUT FINISHED
	@Override
	public void stringFinished(String finalString) {
		if (Config.validatePath(finalString)) {
			if (splashString != null) splashString.remove();
			splashString = new SplashString(MENU_X+MENU_WIDTH/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.GREEN, "Path found. Press finish to set this directory as the level storage folder!", -1, HorizontalAlignment.MIDDLE, false);
			finishButton.setEnabled(true);
			Inputs.setTypedInputListener(null);
			inputPath = finalString;
		}
		else {
			if (splashString != null) splashString.remove();
			splashString = new SplashString(MENU_X+MENU_WIDTH/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "No file found at this location!", 0.85f, HorizontalAlignment.MIDDLE, false);
			startShake();
		}
	}
	
	// CLOSE
	@Override
	protected void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		finishButton = null;
		cancelButton = null;
		textInput = null;
		Inputs.setTypedInputListener(null);
		if (splashString != null) {
			splashString.remove();
			splashString = null;
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