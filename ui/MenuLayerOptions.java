package ui;

import java.awt.Graphics2D;
import level.Level;
import level.LevelLayerDeleter;
import level.LevelLayerShift;
import level.LevelTileInserter;
import level.TileLayer;
import level.TileLayerCopier;
import main.Inputs;

public class MenuLayerOptions extends Menu {

	// VARS
		// MENU PROPERTIES
	private static final int BUTTONS_SMALL_SIZE = 32;
	private static final int BUTTONS_WIDTH = 172;
	private static final int BUTTONS_HEIGHT = 32;
	private static final int BUTTON_ID_VISIBLE = 0;
	private static final int BUTTON_ID_SHIFT_UP = 1;
	private static final int BUTTON_ID_SHIFT_DOWN = 2;
	private static final int BUTTON_ID_INSERT_ABOVE = 3;
	private static final int BUTTON_ID_INSERT_BELOW = 4;
	private static final int BUTTON_ID_MERGE = 5;
	private static final int BUTTON_ID_DELETE = 6;
	private static final int BUTTON_ID_CLOSE = 7;
	private static final int MENU_WIDTH = BUTTONS_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING*2;
	private static final int MENU_HEIGHT = BUTTONS_HEIGHT*4+BUTTONS_SMALL_SIZE*3+DEFAULT_BOX_VERTICAL_PADDING*8;
	private static final char CHAR_DESELECTED = '\u2610';
	private static final char CHAR_SELECTED = '\u2611';
	private static final char CHAR_UP_ARROW = '\u2191';
	private static final char CHAR_DOWN_ARROW = '\u2193';
		// INSTANCE FIELDS
	private SplashString visibleLabel;
	private SplashString shiftsLabel;
	private MenuEditorTools editorTools;
	private int menuX;
	private int menuY;
	private int visibleY;
	private int shiftPositionY;
	private int insertAboveY;
	private int insertBelowY;
	private int mergeY;
	private int deleteY;
	private int closeY;
	private int buttonsX;
	private int layerDepth;
	private Level currentLevel;
	private TileLayer currentLayer;
	private Button visibleButton;
	private Button shiftUpButton;
	private Button shiftDownButton;
	
	// CONSTRUCTOR
	public MenuLayerOptions(Menu owner, int layerDepth, Level currentLevel, MenuEditorTools editorTools) {
		super(owner);
		this.editorTools = editorTools;
		editorTools.setEnabled(false);
		this.layerDepth = layerDepth;
		this.currentLevel = currentLevel;
		this.currentLayer = currentLevel.getLayer(layerDepth);
		menuX = owner.x+owner.width+DEFAULT_BOX_HORIZONTAL_PADDING;
		menuY = Math.max(0, Math.min(UIConstants.SCREEN_HEIGHT-MENU_HEIGHT ,Inputs.getMouseY()));
		buttonsX = menuX+DEFAULT_BOX_HORIZONTAL_PADDING;
		visibleY = menuY+DEFAULT_BOX_VERTICAL_PADDING;
		shiftPositionY = visibleY+BUTTONS_SMALL_SIZE+DEFAULT_BOX_VERTICAL_PADDING;
		insertAboveY = shiftPositionY+BUTTONS_SMALL_SIZE+DEFAULT_BOX_VERTICAL_PADDING;
		insertBelowY = insertAboveY+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		mergeY = insertBelowY+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		deleteY = mergeY+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		closeY = deleteY+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
		setTitle("Layer " + layerDepth + " options", HorizontalAlignment.MIDDLE);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setPosition(menuX, menuY);
		visibleButton = addButton(BUTTON_ID_VISIBLE, buttonsX, visibleY, BUTTONS_SMALL_SIZE, BUTTONS_SMALL_SIZE, "", false);
		if (currentLayer.getIsVisible()) visibleButton.setBodyText(""+CHAR_SELECTED);
		else visibleButton.setBodyText(""+CHAR_DESELECTED);
		visibleLabel = new SplashString(buttonsX+BUTTONS_SMALL_SIZE, visibleY+BUTTONS_SMALL_SIZE/2, UIConstants.UI_COLOR_TEXT, "Toggle visibility", -1, HorizontalAlignment.RIGHT, true);
		shiftUpButton = addButton(BUTTON_ID_SHIFT_UP, buttonsX, shiftPositionY, BUTTONS_SMALL_SIZE, BUTTONS_SMALL_SIZE, ""+CHAR_UP_ARROW, false);
		shiftDownButton = addButton(BUTTON_ID_SHIFT_DOWN, buttonsX+BUTTONS_SMALL_SIZE+DEFAULT_BOX_HORIZONTAL_PADDING, shiftPositionY, BUTTONS_SMALL_SIZE, BUTTONS_SMALL_SIZE, ""+CHAR_DOWN_ARROW, false);
		shiftsLabel = new SplashString(buttonsX+(BUTTONS_SMALL_SIZE+DEFAULT_BOX_HORIZONTAL_PADDING)*2, shiftPositionY+BUTTONS_SMALL_SIZE/2, UIConstants.UI_COLOR_TEXT, "Move layer", -1, HorizontalAlignment.RIGHT, true);
		checkMoveResults();
		addButton(BUTTON_ID_INSERT_ABOVE, buttonsX, insertAboveY, BUTTONS_WIDTH, BUTTONS_HEIGHT, "New layer above", false);
		addButton(BUTTON_ID_INSERT_BELOW, buttonsX, insertBelowY, BUTTONS_WIDTH, BUTTONS_HEIGHT, "New layer below", false);
		Button merge = addButton(BUTTON_ID_MERGE, buttonsX, mergeY, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Merge layer down", false);
		if (currentLevel.getLayerCount() <= 1 || layerDepth < 1) merge.setEnabled(false);
		Button delete = addButton(BUTTON_ID_DELETE, buttonsX, deleteY, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Delete this layer", false);
		if (currentLevel.getLayerCount() <= 1) delete.setEnabled(false);
		addButton(BUTTON_ID_CLOSE, buttonsX, closeY, BUTTONS_WIDTH, BUTTONS_HEIGHT, "Close", false);
	}

	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
	}

	// CHECK MOVE RESULTS
	private void checkMoveResults() {
		if (layerDepth <= 0) shiftDownButton.setEnabled(false);
		if (layerDepth >= currentLevel.getLayerCount()-1) shiftUpButton.setEnabled(false);
	}
	
	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
			case BUTTON_ID_VISIBLE: {
				if (currentLayer.getIsVisible()) {
					currentLayer.setIsVisible(false);
					visibleButton.setBodyText(""+CHAR_DESELECTED);
					new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Layer " + layerDepth + " is now invisible!", 2f, HorizontalAlignment.MIDDLE, true);
				}
				else {
					currentLayer.setIsVisible(true);
					visibleButton.setBodyText(""+CHAR_SELECTED);
					new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Layer " + layerDepth + " is visible again!", 2f, HorizontalAlignment.MIDDLE, true);
				}
			}
		break;
			case BUTTON_ID_SHIFT_UP: {
				{
					LevelLayerShift.shiftLayerDown(currentLevel, layerDepth);
					close(AdditionalCloseOperation.ENABLE_OWNER);
					new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Layer " + layerDepth + " and " + (layerDepth+1) + " have been swapped!", 2f, HorizontalAlignment.MIDDLE, true);
				}
			}
		break;
			case BUTTON_ID_SHIFT_DOWN: {
				{
					LevelLayerShift.shiftLayerUp(currentLevel, layerDepth);
					close(AdditionalCloseOperation.ENABLE_OWNER);
					new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Layer " + layerDepth + " and " + (layerDepth-1) + " have been swapped!", 2f, HorizontalAlignment.MIDDLE, true);
				}
			}
		break;
			case BUTTON_ID_INSERT_ABOVE: {
				LevelTileInserter.insertNewLayer(currentLevel, layerDepth+1);
				editorTools.setLayer(layerDepth+1);
				owner.close(AdditionalCloseOperation.NOTHING);
				editorTools.addSubMenu(new MenuLayerSelect(editorTools, currentLevel, editorTools), false);
				close(AdditionalCloseOperation.ENABLE_OWNER);
				new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Inserted a new layer at index " + (layerDepth+1) + "!", 2f, HorizontalAlignment.MIDDLE, true);
			}
		break;
			case BUTTON_ID_INSERT_BELOW: {
				LevelTileInserter.insertNewLayer(currentLevel, layerDepth);
				editorTools.addSubMenu(new MenuLayerSelect(editorTools, currentLevel, editorTools), false);
				owner.close(AdditionalCloseOperation.NOTHING);
				close(AdditionalCloseOperation.ENABLE_OWNER);
				new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Inserted a new layer at index " + layerDepth + "!", 2f, HorizontalAlignment.MIDDLE, true);
			}
		break;
			case BUTTON_ID_MERGE: {
				TileLayerCopier.copyLayerTo(currentLayer, currentLevel.getLayer(layerDepth-1));
				LevelLayerDeleter.deleteLayerAt(currentLevel, layerDepth);
				editorTools.setLayer(layerDepth-1);
				editorTools.addSubMenu(new MenuLayerSelect(editorTools, currentLevel, editorTools), false);
				owner.close(AdditionalCloseOperation.NOTHING);
				close(AdditionalCloseOperation.ENABLE_OWNER);
				new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Merged layer " + layerDepth + " and layer " + (layerDepth+1), 2f, HorizontalAlignment.MIDDLE, true);
			}
		break;
			case BUTTON_ID_DELETE: {
				LevelLayerDeleter.deleteLayerAt(currentLevel, layerDepth);
				if (editorTools.getCurrentLayer() >= currentLevel.getLayerCount()) editorTools.setLayer(currentLevel.getLayerCount()-1);
				editorTools.addSubMenu(new MenuLayerSelect(editorTools, currentLevel, editorTools), false);
				owner.close(AdditionalCloseOperation.NOTHING);
				close(AdditionalCloseOperation.ENABLE_OWNER);
				new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, UIConstants.UI_COLOR_TEXT, "Deleted layer " + layerDepth, 2f, HorizontalAlignment.MIDDLE, true);
			}
		break;
			case BUTTON_ID_CLOSE: close(AdditionalCloseOperation.ENABLE_OWNER);
		break;
		}
	}

	// CLOSE
	@Override
	public void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		if (visibleLabel != null) visibleLabel.remove();
		if (shiftsLabel != null) shiftsLabel.remove();
		editorTools.setEnabled(true);
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