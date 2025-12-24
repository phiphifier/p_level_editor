package ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import config.BackgroundDefinitionManager;
import level.BackgroundDataHolder;
import main.GameManager;
import main.Inputs;

public class MenuBackgroundConfig extends Menu implements StringInputFieldListener {

	// VARS
		// MENU PROPERTIES
	private static final String MENU_TITLE = "Configure";
	private static final int MENU_WIDTH = 304;
	private static final int MENU_HEIGHT = 406;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final int BUTTON_ID_CANCEL = 0;
	private static final int BUTTON_ID_RESET = 1;
	private static final int BUTTON_ID_CONFIGURE = 2;
	private static final int BUTTON_ID_NEGATIVE_HORI_PARALLAX = 3;
	private static final int BUTTON_ID_NEGATIVE_VERT_PARALLAX = 4;
	private static final int BUTTON_ID_IS_TILED = 5;
	private static final int BUTTONS_WIDTH = 96;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTONS_TOGGLE_SIZE = 32;
	private static final int INPUT_FIELDS_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int INPUT_FIELDS_WIDTH = MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING*2;
	private static final int INPUT_FIELDS_WITH_TOGGLE_WIDTH = MENU_WIDTH-BUTTONS_TOGGLE_SIZE-DEFAULT_BOX_HORIZONTAL_PADDING*3;
	private static final String BUTTON_TOGGLED_ON = ""+'\u2611';
	private static final String BUTTON_TOGGLED_OFF = ""+'\u2610';
		// INSTANCE FIELDS
	private SplashString isTiledLabel;
	private SplashString currentSplashString;
	private BackgroundDataHolder background;
	private Button configureButton;
	private Button xNegative;
	private Button yNegative;
	private Button isTiled;
	private MenuStringInputField xInput;
	private MenuStringInputField yInput;
	private MenuStringInputField xOffsetPerScreenWrapInput;
	private MenuStringInputField yOffsetPerScreenWrapInput;
	private ArrayList<MenuStringInputField> inputFields = new ArrayList<MenuStringInputField>();
	private int currentX = 0;
	private int currentY = 0;
	private int currentXOffsetPerScreenWrap = 0;
	private int currentYOffsetPerScreenWrap = 0;
	
	// CONSTRUCTOR
	public MenuBackgroundConfig(Menu owner, BackgroundDataHolder background) {
		super(owner);
		this.background = background;
		setTitle(MENU_TITLE + " " + BackgroundDefinitionManager.getBackgroundDefinitionByID(background.getID()).getStringName(), HorizontalAlignment.MIDDLE);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		addButton(BUTTON_ID_CANCEL, MENU_X+MENU_WIDTH-BUTTONS_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING, MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Cancel", false);
		addButtonWithMouseHoverTip(BUTTON_ID_RESET, MENU_X+MENU_WIDTH/2-BUTTONS_WIDTH/2, MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Reset", false, "Reset input fields, and restart", HorizontalAlignment.RIGHT);
		configureButton = addButtonWithMouseHoverTip(BUTTON_ID_CONFIGURE, MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING, MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Configure", false, "Configure this background with these settings", HorizontalAlignment.RIGHT);
		resetMenu();
	}

	// RESET MENU
	private void resetMenu() {
		if (currentSplashString != null) currentSplashString.remove();
		if (isTiledLabel != null) isTiledLabel.remove();
		// CLEAR OLD STATE
		for (int i = 0; i < inputFields.size(); i++) {
			MenuStringInputField m = inputFields.get(i);
			m.close(AdditionalCloseOperation.NOTHING);
		}
		if (xNegative != null) {
			GameManager.removeDrawable(xNegative);
			GameManager.removeUpdatable(xNegative);
		}
		if (yNegative != null) {
			GameManager.removeDrawable(yNegative);
			GameManager.removeUpdatable(yNegative);
		}
		if (isTiled != null) {
			GameManager.removeDrawable(isTiled);
			GameManager.removeUpdatable(isTiled);
		}
		// CREATE NEW INPUT FIELDS
		xInput = new MenuStringInputField(this, "Start X", INPUT_FIELDS_X, MENU_Y+DEFAULT_BOX_VERTICAL_PADDING*3, INPUT_FIELDS_WIDTH, 5, true, false, this);
		inputFields.add(xInput);
		xInput.setEnabled(true);
		int inputFieldTotalHeight = xInput.height+xInput.titleFont.getSize()+DEFAULT_BOX_VERTICAL_PADDING*3;
		Inputs.setTypedInputListener(xInput);
		xInput.appendText(""+background.getX());
		yInput = new MenuStringInputField(this, "Start Y", INPUT_FIELDS_X, xInput.y+inputFieldTotalHeight, INPUT_FIELDS_WIDTH, 5, true, false, this);
		inputFields.add(yInput);
		yInput.appendText(""+background.getY());
		xOffsetPerScreenWrapInput = new MenuStringInputField(this, "X offset per screen wrap", INPUT_FIELDS_X, yInput.y+inputFieldTotalHeight, INPUT_FIELDS_WITH_TOGGLE_WIDTH, 4, true, false, this);
		inputFields.add(xOffsetPerScreenWrapInput);
		xOffsetPerScreenWrapInput.appendText(""+background.getXOffsetPerScreenWrap());
		yOffsetPerScreenWrapInput = new MenuStringInputField(this, "Y offset per screen wrap", INPUT_FIELDS_X, xOffsetPerScreenWrapInput.y+inputFieldTotalHeight, INPUT_FIELDS_WITH_TOGGLE_WIDTH, 4, true, false, this);
		inputFields.add(yOffsetPerScreenWrapInput);
		yOffsetPerScreenWrapInput.appendText(""+background.getYOffsetPerScreenWrap());
		xNegative = addButtonWithMouseHoverTip(BUTTON_ID_NEGATIVE_HORI_PARALLAX, MENU_X+MENU_WIDTH-BUTTONS_TOGGLE_SIZE-DEFAULT_BOX_HORIZONTAL_PADDING, xOffsetPerScreenWrapInput.y, BUTTONS_TOGGLE_SIZE, BUTTONS_TOGGLE_SIZE, BUTTON_TOGGLED_ON, false, "Invert value", HorizontalAlignment.RIGHT);
		if (background.getXOffsetPerScreenWrap() > 0) toggleButton(xNegative);
		yNegative = addButtonWithMouseHoverTip(BUTTON_ID_NEGATIVE_VERT_PARALLAX, MENU_X+MENU_WIDTH-BUTTONS_TOGGLE_SIZE-DEFAULT_BOX_HORIZONTAL_PADDING, yOffsetPerScreenWrapInput.y, BUTTONS_TOGGLE_SIZE, BUTTONS_TOGGLE_SIZE, BUTTON_TOGGLED_ON, false, "Invert value", HorizontalAlignment.RIGHT);
		if (background.getYOffsetPerScreenWrap() > 0) toggleButton(yNegative);
		isTiled = addButtonWithMouseHoverTip(BUTTON_ID_IS_TILED, MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING, yOffsetPerScreenWrapInput.y+yOffsetPerScreenWrapInput.height*2, BUTTONS_TOGGLE_SIZE, BUTTONS_TOGGLE_SIZE, BUTTON_TOGGLED_ON, false, "Toggle whether or not this is tiled/repeats forever", HorizontalAlignment.RIGHT);
		if (background.getIsTiled() == 0) toggleButton(isTiled);
		currentX = background.getX();
		currentY = background.getY();
		if (xNegative.getBodyText() == BUTTON_TOGGLED_ON) currentXOffsetPerScreenWrap = background.getXOffsetPerScreenWrap()*-1;
		else currentXOffsetPerScreenWrap = background.getXOffsetPerScreenWrap();
		if (yNegative.getBodyText() == BUTTON_TOGGLED_ON) currentYOffsetPerScreenWrap = background.getYOffsetPerScreenWrap()*-1;
		else currentYOffsetPerScreenWrap = background.getYOffsetPerScreenWrap();
		isTiledLabel = new SplashString(isTiled.getX()+isTiled.getWidth(), isTiled.getY()+isTiled.getHeight()/2, UIConstants.UI_COLOR_TEXT, "Background repeats", -1, HorizontalAlignment.RIGHT, true);
		GameManager.removeDrawable(isTiled);
		GameManager.addDrawable(isTiled);
	}
	
	// TOGGLE BUTTON
	private void toggleButton(Button b) {
		if (b.getBodyText() == BUTTON_TOGGLED_ON) b.setBodyText(BUTTON_TOGGLED_OFF);
		else b.setBodyText(BUTTON_TOGGLED_ON);
	}
	
	// CONFIGURE PRESSED
	private void configurePressed() {
		background.setX(currentX);
		background.setY(currentY);
		if (xNegative.getBodyText() == BUTTON_TOGGLED_OFF) background.setXOffsetPerScreenWrap(currentXOffsetPerScreenWrap);
		else background.setXOffsetPerScreenWrap(currentXOffsetPerScreenWrap*-1);
		if (yNegative.getBodyText() == BUTTON_TOGGLED_OFF) background.setYOffsetPerScreenWrap(currentYOffsetPerScreenWrap);
		else background.setYOffsetPerScreenWrap(currentYOffsetPerScreenWrap*-1);
		if (isTiled.getBodyText() == BUTTON_TOGGLED_ON) background.setIsTiled(1);
		else background.setIsTiled(0);
		close(AdditionalCloseOperation.ENABLE_OWNER);
	}
	
	// UPDATE
	@Override
	public void update() {
		super.update();
		isTiledLabel.setAlpha(alpha);
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
	}

	// BUTTON PRESSED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
			case BUTTON_ID_CANCEL: {
				close(AdditionalCloseOperation.ENABLE_OWNER);
				MenuBackgroundManager myOwner = (MenuBackgroundManager)owner;
				myOwner.refreshBackgroundButtons();
			}
		break;
			case BUTTON_ID_RESET: resetMenu();
		break;
			case BUTTON_ID_CONFIGURE: {
				configurePressed();
				MenuBackgroundManager myOwner = (MenuBackgroundManager)owner;
				myOwner.refreshBackgroundButtons();
			}
		break;
			case BUTTON_ID_NEGATIVE_HORI_PARALLAX: toggleButton(xNegative);
		break;
			case BUTTON_ID_NEGATIVE_VERT_PARALLAX: toggleButton(yNegative);
		break;
			case BUTTON_ID_IS_TILED: toggleButton(isTiled);
		break;
		}
	}

	// INVALID INPUT ATTEMPTED
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
		if (Inputs.getTypedInputListener() == xInput) {
			yInput.setEnabled(true);
			Inputs.setTypedInputListener(yInput);
			currentX = Integer.valueOf(finalString);
			return;
		}
		if (Inputs.getTypedInputListener() == yInput) {
			xOffsetPerScreenWrapInput.setEnabled(true);
			Inputs.setTypedInputListener(xOffsetPerScreenWrapInput);
			currentY = Integer.valueOf(finalString);
			return;
		}
		if (Inputs.getTypedInputListener() == xOffsetPerScreenWrapInput) {
			yOffsetPerScreenWrapInput.setEnabled(true);
			Inputs.setTypedInputListener(yOffsetPerScreenWrapInput);
			currentXOffsetPerScreenWrap = Integer.valueOf(finalString);
			return;
		}
		if (Inputs.getTypedInputListener() == yOffsetPerScreenWrapInput) {
			Inputs.setTypedInputListener(null);
			if (currentSplashString != null) currentSplashString.remove();
			currentSplashString = new SplashString(MENU_X+width/2, (int)(MENU_Y+MENU_HEIGHT-BUTTONS_HEIGHT*1.75), Color.GREEN, "Press configure if this is correct!", -1f, HorizontalAlignment.MIDDLE, false);
			configureButton.setEnabled(true);
			currentYOffsetPerScreenWrap = Integer.valueOf(finalString);
			return;
		}
	}
	
	// CLOSE
	@Override
	public void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		for (int i = 0; i < inputFields.size(); i++) {
			MenuStringInputField m = inputFields.get(i);
			m.close(AdditionalCloseOperation.NOTHING);
		}
		if (currentSplashString != null) currentSplashString.remove();
		if (isTiledLabel != null) isTiledLabel.remove();
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