package ui;

import level.Level;
import main.GameManager;
import main.Inputs;
import java.util.ArrayList;
import java.awt.Color;
import java.awt.Graphics2D;

public class MenuLayerSelect extends Menu {

	// VARS
	private boolean buttonsReAddedToDrawable = false;
	private boolean mouseInMenuLastFrame = false;
	private boolean scrollingWithBar = false;
	private int mouseYScrollOffset = 0;
	private int totalScrollableZoneHeight = 0;
	private double scrollPixelsPerSliderPixel = 0;
	private Button scrollBar;
	private MenuEditorTools editorTools;
	private ArrayList<Button> layerButtons = new ArrayList<Button>();
	private Level currentLevel;
	private Button currentlyActiveButton = null;
	private static final int BUTTON_ID_SCROLL_BAR = -1;
	private static final int BUTTONS_LAYER_WIDTH = 44;
	private static final int BUTTONS_LAYER_HEIGHT = 40;
	private static final int BUTTON_SCROLL_BAR_WIDTH = 10;
	private static final int BUTTON_SCROLL_BAR_SLIDER_HEIGHT = UIConstants.SCREEN_HEIGHT;
	private static final int BUTTON_SCROLL_BAR_START_Y = 0;
	private static final int MENU_X = 0;
	private static final int MENU_Y = 0;
	private static final int MENU_WIDTH = BUTTONS_LAYER_WIDTH+BUTTON_SCROLL_BAR_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING*4;
	private static final int MENU_HEIGHT = UIConstants.SCREEN_HEIGHT;
	private static final int BUTTON_SCROLL_BAR_X = MENU_X+MENU_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING-BUTTON_SCROLL_BAR_WIDTH;
	
	// CONSTRUCTOR
	public MenuLayerSelect(Menu owner, Level currentLevel, MenuEditorTools editorTools) {
		super(owner);
		this.editorTools = editorTools;
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		this.currentLevel = currentLevel;
		// CREATE LAYER BUTTONS
		/* They're created first so releasing the mouse button while using the scroll bar over a layer button doesn't activate it.
		 * They're initially removed, then re-added in the update method to ensure the tip text is drawn over the scroll bar.
		 */
		// CREATE LAYER BUTTONS
		int buttonEntireHeight = DEFAULT_BOX_VERTICAL_PADDING+BUTTONS_LAYER_HEIGHT;
		for (int i = 0; i < currentLevel.getLayerCount(); i++) {
			Button button = addButtonWithMouseHoverTip(i, DEFAULT_BOX_VERTICAL_PADDING, buttonEntireHeight*currentLevel.getLayerCount()-buttonEntireHeight-buttonEntireHeight*i, BUTTONS_LAYER_WIDTH, BUTTONS_LAYER_HEIGHT, "L" + i, false, "Right click for more options", HorizontalAlignment.RIGHT);
			layerButtons.add(button);
			GameManager.removeDrawable(button);
		}
		currentlyActiveButton = buttons.get(editorTools.getCurrentLayer());
		currentlyActiveButton.setStayHighlighted(true);
		// CREATE SCROLL BAR
		scrollBar = addButton(BUTTON_ID_SCROLL_BAR, BUTTON_SCROLL_BAR_X, BUTTON_SCROLL_BAR_START_Y, BUTTON_SCROLL_BAR_WIDTH, BUTTON_SCROLL_BAR_SLIDER_HEIGHT, "", false);
		scrollBar.setStickyMouse(true);
		// SETUP SCROLL BAR
		int newScrollBarHeight = Math.min((int)(MENU_HEIGHT*((double)MENU_HEIGHT/((double)layerButtons.size()*(BUTTONS_LAYER_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)))), MENU_HEIGHT);
		scrollBar.setHeight(newScrollBarHeight);
		scrollBar.setY(0);
		totalScrollableZoneHeight = layerButtons.size()*(BUTTONS_LAYER_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)-MENU_HEIGHT;
		int sliderHeight = MENU_HEIGHT-scrollBar.getHeight();
		if (sliderHeight > 0 && totalScrollableZoneHeight > 0) scrollPixelsPerSliderPixel = (double)totalScrollableZoneHeight/(double)sliderHeight;
	}
	
	// UPDATE SCROLL BAR
	private void updateScrollBar() {
		int buttonEntireHeight = BUTTONS_LAYER_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		if (scrollingWithBar) {
			if (Inputs.getMouseY() != scrollBar.getY()+mouseYScrollOffset) {
				scrollBar.setY(Math.max(Math.min(Inputs.getMouseY()-mouseYScrollOffset, MENU_HEIGHT-scrollBar.getHeight()), MENU_Y));
				for (int i = 0; i < layerButtons.size(); i++) {
					Button b = layerButtons.get(i);
					int newButtonY = (int)(buttonEntireHeight*currentLevel.getLayerCount()-buttonEntireHeight-buttonEntireHeight*i-(scrollBar.getY()*scrollPixelsPerSliderPixel));
					b.setY(newButtonY);
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
	
	// CHECK MOUSE IN MENU
	private boolean checkMouseInMenu() {
		int mx = Inputs.getMouseX();
		int my = Inputs.getMouseY();
		if (mx < x+width && mx > x && my < y+height && my > y) return true;
		else return false;
	}
	
	// ADD LAYER BUTTONS TO DRAWABLE
	private void addLayerButtonsToDrawable() {
		for (int i = 0; i < layerButtons.size(); i++) {
			Button button = layerButtons.get(i);
			GameManager.removeDrawable(button);
			GameManager.addDrawable(button);
		}
	}
	
	// UPDATE
	@Override
	public void update() {
		super.update();
		if (!buttonsReAddedToDrawable) {
			buttonsReAddedToDrawable = true;
			addLayerButtonsToDrawable();
		}
		if (isEnabled) {
			updateScrollBar();
			if (checkMouseInMenu() || scrollingWithBar) {
				editorTools.setButtonHovered();
				mouseInMenuLastFrame = true;
			}
			else if (mouseInMenuLastFrame && !scrollingWithBar) {
				editorTools.setButtonUnHovered();
				mouseInMenuLastFrame = false;
			}
		}
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
		g2.setColor(Color.BLACK);
		g2.fillRect(BUTTON_SCROLL_BAR_X+2, 0, BUTTON_SCROLL_BAR_WIDTH-4, MENU_HEIGHT);
	}

	// BUTTON_PRESSED
	@Override
	public void buttonLeftPressed(int buttonID) {
		switch(buttonID) {
		case BUTTON_ID_SCROLL_BAR: startScrollBar();
		break;
		}
	}

	// LEFT RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
		case BUTTON_ID_SCROLL_BAR: {
			stopScrollBar();
			if (checkMouseInMenu()) editorTools.setButtonUnHovered();
			break;
		}
		default: if (!scrollingWithBar) {
				editorTools.setLayer(buttonID);
				if (currentlyActiveButton != null) currentlyActiveButton.setStayHighlighted(false);
				currentlyActiveButton = buttons.get(buttonID);
				currentlyActiveButton.setStayHighlighted(true);
				new SplashString(MENU_X+MENU_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, layerButtons.get(buttonID).getY()+BUTTONS_LAYER_HEIGHT/2, Color.GREEN,"Switched to layer " + buttonID, 1f, HorizontalAlignment.RIGHT, false);
				break;
			}
		}
	}
	
	// RIGHT RELEASED
	@Override
	public void buttonRightReleased(int buttonID) {
		if (buttonID >= 0) addSubMenu(new MenuLayerOptions(this, buttonID, currentLevel, editorTools), true);
	}
	
	// UNUSED
	@Override
	public void buttonMoused(int buttonID) {}
	@Override
	public void buttonUnMoused(int buttonID) {}
	@Override
	public void buttonRightPressed(int buttonID) {}
}