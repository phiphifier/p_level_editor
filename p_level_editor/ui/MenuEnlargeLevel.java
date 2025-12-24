package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import level.LevelEnlarger;
import main.Inputs;

public class MenuEnlargeLevel extends Menu implements StringInputFieldListener {

	// FIELDS
		// INSTANCE FIELDS
	private MenuEditorTools editorTools;
	private Button enlargeButton;
	private MenuStringInputField inputFieldExtraWidth;
	private MenuStringInputField inputFieldExtraHeight;
	private int currentExtraWidth;
	private int currentExtraHeight;
	private LevelEnlarger.ResizeHoriAlign horiAlignment = LevelEnlarger.ResizeHoriAlign.MIDDLE;
	private LevelEnlarger.ResizeVertAlign vertAlignment = LevelEnlarger.ResizeVertAlign.MIDDLE;
	private SplashString splashString;
	private Button topLeftButton;
	private Button topMiddleButton;
	private Button topRightButton;
	private Button middleLeftButton;
	private Button middleButton;
	private Button middleRightButton;
	private Button bottomLeftButton;
	private Button bottomMiddleButton;
	private Button bottomRightButton;
		// MENU PROPERTIES
	private static final String MENU_TITLE = "By how much:";
	private static final int MENU_WIDTH = 304;
	private static final int MENU_HEIGHT = 322;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final int BUTTON_ID_ENLARGE = 0;
	private static final int BUTTON_ID_RESET = 1;
	private static final int BUTTON_ID_CANCEL = 2;
	private static final int BUTTON_ID_TOP_LEFT = 3;
	private static final int BUTTON_ID_TOP_MIDDLE = 4;
	private static final int BUTTON_ID_TOP_RIGHT = 5;
	private static final int BUTTON_ID_MIDDLE_LEFT = 6;
	private static final int BUTTON_ID_MIDDLE = 7;
	private static final int BUTTON_ID_MIDDLE_RIGHT = 8;
	private static final int BUTTON_ID_BOTTOM_LEFT = 9;
	private static final int BUTTON_ID_BOTTOM_MIDDLE = 10;
	private static final int BUTTON_ID_BOTTOM_RIGHT = 11;
	private static final int BUTTONS_WIDTH = 96;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTON_ENLARGE_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_RESET_X = MENU_X+MENU_WIDTH/2-BUTTONS_WIDTH/2;
	private static final int BUTTON_CANCEL_X = MENU_X+MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING-BUTTONS_WIDTH;
	private static final int BUTTONS_Y = MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING;
	private static final int INPUT_FIELDS_WIDTH = MENU_WIDTH/2-DEFAULT_BOX_HORIZONTAL_PADDING*3;
	private static final int INPUT_FIELDS_Y = MENU_Y+UIConstants.MENU_FONT_TITLE_SIZE;
	private static final int INPUT_FIELD_EXTRA_WIDTH_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int INPUT_FIELD_EXTRA_HEIGHT_X = MENU_X+MENU_WIDTH-INPUT_FIELDS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTONS_EXPANSION_ORIGIN_WIDTH = 40;
	private static final int BUTTONS_EXPANSION_ORIGIN_HEIGHT = 40;
	private static final int BUTTONS_EXPANSION_ORIGIN_MIDDLE_X = MENU_X+MENU_WIDTH/2;
	private static final int BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y = (int)(BUTTONS_Y-BUTTONS_EXPANSION_ORIGIN_HEIGHT*1.6f)-DEFAULT_BOX_VERTICAL_PADDING*2-UIConstants.SPLASH_FONT_SIZE;
	private static final char CHAR_SELECTED = '\u2610';
	private static final char CHAR_L_ARROW = '\u2190';
	private static final char CHAR_U_ARROW = '\u2191';
	private static final char CHAR_R_ARROW = '\u2192';
	private static final char CHAR_D_ARROW = '\u2193';
	private static final char CHAR_UL_ARROW = '\u2196';
	private static final char CHAR_UR_ARROW = '\u2197';
	private static final char CHAR_DR_ARROW = '\u2198';
	private static final char CHAR_DL_ARROW = '\u2199';

	// CONSTRUCTOR
	public MenuEnlargeLevel(Menu owner, MenuEditorTools editorTools) {
		super(owner);
		this.editorTools = editorTools;
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle(MENU_TITLE, HorizontalAlignment.LEFT);
		addButton(BUTTON_ID_CANCEL, BUTTON_CANCEL_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Cancel", false);
		addButtonWithMouseHoverTip(BUTTON_ID_RESET, BUTTON_RESET_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Reset", false, "Reset the input fields and restart", HorizontalAlignment.RIGHT);
		enlargeButton = addButtonWithMouseHoverTip(BUTTON_ID_ENLARGE, BUTTON_ENLARGE_X, BUTTONS_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Enlarge", false, "Enlarge this level", HorizontalAlignment.RIGHT);
		topLeftButton = addButton(BUTTON_ID_TOP_LEFT, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X-(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*1.7), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y-(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*1.7), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		topMiddleButton = addButton(BUTTON_ID_TOP_MIDDLE, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X-(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*0.6), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y-(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*1.7), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		topRightButton = addButton(BUTTON_ID_TOP_RIGHT, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X+(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*0.5), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y-(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*1.7), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		middleLeftButton = addButton(BUTTON_ID_MIDDLE_LEFT, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X-(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*1.7), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y-(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*0.6), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		middleButton = addButton(BUTTON_ID_MIDDLE, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X-(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*0.6), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y-(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*0.6), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		middleRightButton = addButton(BUTTON_ID_MIDDLE_RIGHT, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X+(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*0.5), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y-(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*0.6), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		bottomLeftButton = addButton(BUTTON_ID_BOTTOM_LEFT, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X-(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*1.7), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y+(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*0.5), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		bottomMiddleButton = addButton(BUTTON_ID_BOTTOM_MIDDLE, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X-(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*0.6), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y+(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*0.5), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		bottomRightButton = addButton(BUTTON_ID_BOTTOM_RIGHT, BUTTONS_EXPANSION_ORIGIN_MIDDLE_X+(int)(BUTTONS_EXPANSION_ORIGIN_WIDTH*0.5), BUTTONS_EXPANSION_ORIGIN_MIDDLE_Y+(int)(BUTTONS_EXPANSION_ORIGIN_HEIGHT*0.5), BUTTONS_EXPANSION_ORIGIN_WIDTH, BUTTONS_EXPANSION_ORIGIN_HEIGHT, "", true);
		createInputFields();
		buttonLeftReleased(BUTTON_ID_MIDDLE);
	}
	
	// RESET ALIGNMENT BUTTON LABELS
	private void resetAlignmentButtonLabels() {
		topLeftButton.setBodyText("");
		topMiddleButton.setBodyText("");
		topRightButton.setBodyText("");
		middleLeftButton.setBodyText("");
		middleButton.setBodyText("");
		middleRightButton.setBodyText("");
		bottomLeftButton.setBodyText("");
		bottomMiddleButton.setBodyText("");
		bottomRightButton.setBodyText("");
	}

	// CREATE INPUT FIELDS
	private void createInputFields() {
		enlargeButton.setEnabled(false);
		if (inputFieldExtraWidth != null) inputFieldExtraWidth.close(AdditionalCloseOperation.NOTHING);
		inputFieldExtraWidth = new MenuStringInputField(this, "Extra width", INPUT_FIELD_EXTRA_WIDTH_X, INPUT_FIELDS_Y, INPUT_FIELDS_WIDTH, 4, true, false, this);
		inputFieldExtraWidth.setEnabled(true);
		Inputs.setTypedInputListener(inputFieldExtraWidth);
		addSubMenu(inputFieldExtraWidth, false);
		if (inputFieldExtraHeight != null) inputFieldExtraHeight.close(AdditionalCloseOperation.NOTHING);
		inputFieldExtraHeight = new MenuStringInputField(this, "Extra height", INPUT_FIELD_EXTRA_HEIGHT_X, INPUT_FIELDS_Y, INPUT_FIELDS_WIDTH, 4, true, false, this);
		addSubMenu(inputFieldExtraHeight, false);
		if (splashString != null) splashString.remove();
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
			case BUTTON_ID_CANCEL: close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
			case BUTTON_ID_RESET: createInputFields();
		break;
			case BUTTON_ID_ENLARGE: editorTools.enlargeLevel(currentExtraWidth, currentExtraHeight, vertAlignment, horiAlignment);
		break;
			case BUTTON_ID_TOP_LEFT: {
				resetAlignmentButtonLabels();
				topLeftButton.setBodyText(""+CHAR_SELECTED);
				topMiddleButton.setBodyText(""+CHAR_R_ARROW);
				middleLeftButton.setBodyText(""+CHAR_D_ARROW);
				middleButton.setBodyText(""+CHAR_DR_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.LEFT;
				vertAlignment = LevelEnlarger.ResizeVertAlign.TOP;
			}
		break;
			case BUTTON_ID_TOP_MIDDLE: {
				resetAlignmentButtonLabels();
				topMiddleButton.setBodyText(""+CHAR_SELECTED);
				topLeftButton.setBodyText(""+CHAR_L_ARROW);
				middleLeftButton.setBodyText(""+CHAR_DL_ARROW);
				middleButton.setBodyText(""+CHAR_D_ARROW);
				middleRightButton.setBodyText(""+CHAR_DR_ARROW);
				topRightButton.setBodyText(""+CHAR_R_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.MIDDLE;
				vertAlignment = LevelEnlarger.ResizeVertAlign.TOP;
			}
		break;
			case BUTTON_ID_TOP_RIGHT: {
				resetAlignmentButtonLabels();
				topRightButton.setBodyText(""+CHAR_SELECTED);
				topMiddleButton.setBodyText(""+CHAR_L_ARROW);
				middleButton.setBodyText(""+CHAR_DL_ARROW);
				middleRightButton.setBodyText(""+CHAR_D_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.RIGHT;
				vertAlignment = LevelEnlarger.ResizeVertAlign.TOP;
			}
		break;
			case BUTTON_ID_MIDDLE_LEFT: {
				resetAlignmentButtonLabels();
				middleLeftButton.setBodyText(""+CHAR_SELECTED);
				topLeftButton.setBodyText(""+CHAR_U_ARROW);
				topMiddleButton.setBodyText(""+CHAR_UR_ARROW);
				middleButton.setBodyText(""+CHAR_R_ARROW);
				bottomMiddleButton.setBodyText(""+CHAR_DR_ARROW);
				bottomLeftButton.setBodyText(""+CHAR_D_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.LEFT;
				vertAlignment = LevelEnlarger.ResizeVertAlign.MIDDLE;
			}
		break;
			case BUTTON_ID_MIDDLE: {
				resetAlignmentButtonLabels();
				middleButton.setBodyText(""+CHAR_SELECTED);
				topLeftButton.setBodyText(""+CHAR_UL_ARROW);
				topMiddleButton.setBodyText(""+CHAR_U_ARROW);
				topRightButton.setBodyText(""+CHAR_UR_ARROW);
				middleLeftButton.setBodyText(""+CHAR_L_ARROW);
				middleRightButton.setBodyText(""+CHAR_R_ARROW);
				bottomLeftButton.setBodyText(""+CHAR_DL_ARROW);
				bottomMiddleButton.setBodyText(""+CHAR_D_ARROW);
				bottomRightButton.setBodyText(""+CHAR_DR_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.MIDDLE;
				vertAlignment = LevelEnlarger.ResizeVertAlign.MIDDLE;
			}
		break;
			case BUTTON_ID_MIDDLE_RIGHT: {
				resetAlignmentButtonLabels();
				middleRightButton.setBodyText(""+CHAR_SELECTED);
				topRightButton.setBodyText(""+CHAR_U_ARROW);
				topMiddleButton.setBodyText(""+CHAR_UL_ARROW);
				middleButton.setBodyText(""+CHAR_L_ARROW);
				bottomMiddleButton.setBodyText(""+CHAR_DL_ARROW);
				bottomRightButton.setBodyText(""+CHAR_D_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.RIGHT;
				vertAlignment = LevelEnlarger.ResizeVertAlign.MIDDLE;
			}
		break;
			case BUTTON_ID_BOTTOM_LEFT: {
				resetAlignmentButtonLabels();
				bottomLeftButton.setBodyText(""+CHAR_SELECTED);
				middleLeftButton.setBodyText(""+CHAR_U_ARROW);
				middleButton.setBodyText(""+CHAR_UR_ARROW);
				bottomMiddleButton.setBodyText(""+CHAR_R_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.LEFT;
				vertAlignment = LevelEnlarger.ResizeVertAlign.BOTTOM;
			}
		break;
			case BUTTON_ID_BOTTOM_MIDDLE: {
				resetAlignmentButtonLabels();
				bottomMiddleButton.setBodyText(""+CHAR_SELECTED);
				bottomLeftButton.setBodyText(""+CHAR_L_ARROW);
				middleLeftButton.setBodyText(""+CHAR_UL_ARROW);
				middleButton.setBodyText(""+CHAR_U_ARROW);
				middleRightButton.setBodyText(""+CHAR_UR_ARROW);
				bottomRightButton.setBodyText(""+CHAR_R_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.MIDDLE;
				vertAlignment = LevelEnlarger.ResizeVertAlign.BOTTOM;
			}
		break;
			case BUTTON_ID_BOTTOM_RIGHT: {
				resetAlignmentButtonLabels();
				bottomRightButton.setBodyText(""+CHAR_SELECTED);
				bottomMiddleButton.setBodyText(""+CHAR_L_ARROW);
				middleButton.setBodyText(""+CHAR_UL_ARROW);
				middleRightButton.setBodyText(""+CHAR_U_ARROW);
				horiAlignment = LevelEnlarger.ResizeHoriAlign.RIGHT;
				vertAlignment = LevelEnlarger.ResizeVertAlign.BOTTOM;
			}
		break;
		}
	}

	// ATTEMPTED INVALID INPUT
	@Override
	public void attemptInvalidInput(int errorID) {
		if (splashString != null) splashString.remove();
		switch(errorID) {
			case StringInputFieldListener.ERROR_EMPTY: {
				splashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Current field is empty!", 0.75f, HorizontalAlignment.MIDDLE, false);
				startShake();
			}
		break;
			case StringInputFieldListener.ERROR_FULL: {
				splashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Current field is full!", 0.75f, HorizontalAlignment.MIDDLE, false);
				startShake();
			}
		break;
			case StringInputFieldListener.ERROR_LETTER: {
				splashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.RED, "Only numbers are accepted!", 0.75f, HorizontalAlignment.MIDDLE, false);
				startShake();
			}
		break;
		}
	}

	// STRING FINISHED
	@Override
	public void stringFinished(String finalString) {
		if (Inputs.getTypedInputListener() == inputFieldExtraWidth) {
			currentExtraWidth = Integer.valueOf(finalString);
			Inputs.setTypedInputListener(inputFieldExtraHeight);
			inputFieldExtraHeight.setEnabled(true);
		}
		else {
			Inputs.setTypedInputListener(null);
			currentExtraHeight = Integer.valueOf(finalString);
			enlargeButton.setEnabled(true);
			if (splashString != null) splashString.remove();
			splashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.GREEN, "Press enlarge if this is correct!", -1, HorizontalAlignment.MIDDLE, false);
		}
	}

	// CLOSE
	@Override
	public void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		if (splashString != null) splashString.remove();
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