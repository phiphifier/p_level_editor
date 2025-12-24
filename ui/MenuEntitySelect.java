package ui;

import config.EntityDefinition;
import config.EntityDefinitionManager;
import main.GameManager;
import main.Inputs;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class MenuEntitySelect extends Menu {

	// VARS
		// INSTANCE FIELDS
	private MenuEditorTools editorTools = null;
	private ArrayList<Button> entityDefinitionButtons = new ArrayList<Button>();
	private boolean scrollingWithBar = false;
	private int mouseYScrollOffset = 0;
	private int totalScrollableZoneHeight = 0;
	private double scrollPixelsPerSliderPixel = 0;
	private Button scrollBar;
	private SplashString splashLabel;
	private String splashLabelString;
	private boolean removingEntities = false;
	private Button removeEntitiesButton;
		// MENU PROPERTIES
	private static final String VIEWING_FROM_MENU_LABEL = "Entity Definitions";
	private static final String VIEWING_FROM_EDITOR_LABEL = "Select an entity to start placing";
	private static final String REMOVING_ENTITIES_LABEL = "Click an entity definition to delete it";
	private static final String CHAR_CHECK_BOX = Character.toString('\u2611');
	private static final String CHAR_REMOVE = "-";
	private static final int MENU_X = 0;
	private static final int MENU_Y = 0;
	private static final int MENU_WIDTH = 256;
	private static final int MENU_HEIGHT = UIConstants.SCREEN_HEIGHT-UIConstants.MENU_BORDER_THICKNESS/2;
	private static final int BUTTON_ID_CLOSE = -1;
	private static final int BUTTON_ID_ADD = -2;
	private static final int BUTTON_ID_REMOVE = -3;
	private static final int BUTTON_ID_SCROLL_BAR = -4;
	private static final int BUTTON_SCROLL_BAR_WIDTH = 10;
	private static final int BUTTON_SCROLL_BAR_SLIDER_HEIGHT = UIConstants.SCREEN_HEIGHT;
	private static final int BUTTON_SCROLL_BAR_START_Y = 0;
	private static final int BUTTONS_WIDTH = 40;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTONS_X = MENU_X+MENU_WIDTH;
	private static final int BUTTON_CLOSE_Y = MENU_Y+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_ADD_Y = BUTTON_CLOSE_Y+(int)(BUTTONS_HEIGHT*1.5)+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_REMOVE_Y = BUTTON_ADD_Y+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTONS_ENTITY_DEFINITION_WIDTH = MENU_WIDTH-BUTTON_SCROLL_BAR_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING*4;
	private static final int BUTTONS_ENTITY_DEFINITION_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_SCROLL_BAR_X = MENU_WIDTH-BUTTON_SCROLL_BAR_WIDTH-DEFAULT_BOX_HORIZONTAL_PADDING;
	
	// CONSTRUCTOR
	public MenuEntitySelect(Menu owner, MenuEditorTools editorTools) {
		super(owner);
		this.editorTools = editorTools;
		if (editorTools == null) splashLabelString = VIEWING_FROM_MENU_LABEL;
		else splashLabelString = VIEWING_FROM_EDITOR_LABEL;
		splashLabel = new SplashString(MENU_X+MENU_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, MENU_HEIGHT/2, Color.WHITE, splashLabelString, -1, HorizontalAlignment.RIGHT, true);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		addButton(BUTTON_ID_CLOSE, BUTTONS_X, BUTTON_CLOSE_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "X", false);
		addButtonWithMouseHoverTip(BUTTON_ID_ADD, BUTTONS_X, BUTTON_ADD_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, "+", true, "Add a new entity definition", HorizontalAlignment.RIGHT);
		removeEntitiesButton = addButtonWithMouseHoverTip(BUTTON_ID_REMOVE, BUTTONS_X, BUTTON_REMOVE_Y, BUTTONS_WIDTH, BUTTONS_HEIGHT, CHAR_REMOVE, true, "Toggle click to delete entity definitions", HorizontalAlignment.RIGHT);
		scrollBar = addButton(BUTTON_ID_SCROLL_BAR, BUTTON_SCROLL_BAR_X, BUTTON_SCROLL_BAR_START_Y, BUTTON_SCROLL_BAR_WIDTH, BUTTON_SCROLL_BAR_SLIDER_HEIGHT, "", false);
		scrollBar.setStickyMouse(true);
		updateEntityDefinitionButtons();
	}
	
	// REFRESH ENTITY DEFINITIONS
	protected void updateEntityDefinitionButtons() {
		// REMOVE OLD ENTITY DEFINTION BUTTONS
		for (int i = 0; i < entityDefinitionButtons.size(); i++) {
			Button b = entityDefinitionButtons.get(i);
			GameManager.removeUpdatable(b);
			GameManager.removeDrawable(b);
			buttons.remove(b);
		}
		entityDefinitionButtons.clear();
		// ADD ENTITY DEFINITION BUTTONS
		for (int i = 0; i < EntityDefinitionManager.getEntityDefinitionsSize(); i++) {
			EntityDefinition ed = EntityDefinitionManager.getEntityDefinition(i);
			if (ed != null) entityDefinitionButtons.add(addButton(ed.getID(), BUTTONS_ENTITY_DEFINITION_X, MENU_Y+(BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*i, BUTTONS_ENTITY_DEFINITION_WIDTH, BUTTONS_HEIGHT, ed.getStringNameWithID(), false));
		}
		// RESET SCROLL BAR
		int newScrollBarHeight = Math.min((int)(MENU_HEIGHT*((double)MENU_HEIGHT/((double)entityDefinitionButtons.size()*(BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)))), MENU_HEIGHT);
		scrollBar.setHeight(newScrollBarHeight);
		scrollBar.setY(0);
		totalScrollableZoneHeight = entityDefinitionButtons.size()*(BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)-MENU_HEIGHT;
		int sliderHeight = MENU_HEIGHT-scrollBar.getHeight();
		if (sliderHeight > 0 && totalScrollableZoneHeight > 0) scrollPixelsPerSliderPixel = (double)totalScrollableZoneHeight/(double)sliderHeight;
	}
	
	// UPDATE SCROLL BAR
	private void updateScrollBar() {
		int buttonEntireHeight = BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		if (scrollingWithBar) {
			if (Inputs.getMouseY() != scrollBar.getY()+mouseYScrollOffset) {
				scrollBar.setY(Math.max(Math.min(Inputs.getMouseY()-mouseYScrollOffset, MENU_HEIGHT-scrollBar.getHeight()), MENU_Y));
				for (int i = 0; i < entityDefinitionButtons.size(); i++) {
					Button b = entityDefinitionButtons.get(i);
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
		switch(buttonID) {
			case BUTTON_ID_SCROLL_BAR: startScrollBar();
		break;
		}
	}
	
	// ENABLE REMOVING ENTITIES
	private void removePressed() {
		if (!removingEntities) {
			removingEntities = true;
			splashLabel.remove();
			splashLabel = new SplashString(MENU_X+MENU_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, MENU_HEIGHT/2, Color.RED, REMOVING_ENTITIES_LABEL, -1, HorizontalAlignment.RIGHT, true);
			removeEntitiesButton.setBodyText(CHAR_CHECK_BOX);
		}
		else {
			removingEntities = false;
			splashLabel.remove();
			splashLabel = new SplashString(MENU_X+MENU_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, MENU_HEIGHT/2, Color.WHITE, splashLabelString, -1, HorizontalAlignment.RIGHT, true);
			removeEntitiesButton.setBodyText(CHAR_REMOVE);
		}
	}
	
	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
			case BUTTON_ID_ADD: {
				if (!scrollingWithBar) {
					if (removingEntities) removePressed();
					addSubMenu(new MenuAddEntityDefinition(this), false);
				}
			}
		break;
			case BUTTON_ID_CLOSE: if (!scrollingWithBar) close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
			case BUTTON_ID_REMOVE: if (!scrollingWithBar) removePressed();
		break;
			case BUTTON_ID_SCROLL_BAR: stopScrollBar();
		break;
			default: if (!scrollingWithBar) {
				if (editorTools != null && !removingEntities) {
					editorTools.setEntity(buttonID);
					close(AdditionalCloseOperation.ENABLE_OWNER);
				}
				if (removingEntities) {
					ArrayList<EntityDefinition> removalQueue = new ArrayList<EntityDefinition>();
					for (int i = 0; i < EntityDefinitionManager.getEntityDefinitionsSize(); i++) {
						EntityDefinition ed = EntityDefinitionManager.getEntityDefinition(i);
						if (buttonID == ed.getID()) removalQueue.add(ed);
					}
					for (int i = 0; i < removalQueue.size(); i++) {
						EntityDefinitionManager.removeEntityDefinition(removalQueue.get(i).getStringName());
					}
					updateEntityDefinitionButtons();
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