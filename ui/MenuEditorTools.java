package ui;

import level.*;
import main.GameManager;
import main.Resources;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class MenuEditorTools extends Menu implements TileCursorListener {

	// VARS
		// EDITOR MODE ENUM
	private enum EditorMode {
		EDIT_TILES,
		EDIT_ENTITIES
	}
	
		// LEVEL EDITOR FIELDS
	private EditorMode currentMode = EditorMode.EDIT_TILES;
	private Camera camera;
	private Level currentLevel;
	private TileCursor cursor;
	private int currentEntityID = 0;
	private int currentLayer;
	private int currentTile = 24;
	private boolean isGridEnabled = true;
	private Button buttonGrid;
	private boolean isEraseAllEnabled = true;
	private Button buttonEraseAll;
	private SplashString toggleGridLabel;
	private SplashString toggleEraseAllLabel;
	private boolean buttonHoveredThisFrame = false;
	private boolean buttonUnHoveredThisFrame = false;
	private MenuLayerSelect layerSelect;
	private String levelName;
		// GENERAL MENU PROPERTIES
	private static final int RIGHT_MENU_ELEMENTS_WIDTH = 160;
	private static final int RIGHT_MENU_ELEMENTS_X = UIConstants.SCREEN_WIDTH-DEFAULT_BOX_VERTICAL_PADDING-RIGHT_MENU_ELEMENTS_WIDTH;
		// UI PROPERTIES
	private static final int BUTTONS_STANDARD_HEIGHT = 40;
	private static final int BUTTON_ID_TILES = -1;
	private static final int BUTTON_ID_ENTITIES = -2;
	private static final int BUTTON_ID_BACKGROUNDS = -3;
	private static final int BUTTON_ID_TOGGLE_GRID = -4;
	private static final int BUTTON_ID_TOGGLE_ERASE = -5;
	private static final int BUTTON_ID_CROP = -6;
	private static final int BUTTON_ID_ENLARGE = -7;
	private static final int BUTTON_ID_SAVE = -8;
	private static final int BUTTON_ID_SAVE_AS = -9;
	private static final int BUTTON_ID_LOAD = -10;
	private static final int BUTTON_ID_MAIN_MENU = -11;
	private static final int MENU_Y = DEFAULT_BOX_VERTICAL_PADDING+UIConstants.MENU_FONT_TITLE_SIZE;
	private static final int MENU_WIDTH = RIGHT_MENU_ELEMENTS_WIDTH+RIGHT_MENU_ELEMENTS_WIDTH/4;
	private static final int MENU_HEIGHT = 108;
	private static final int MENU_X = RIGHT_MENU_ELEMENTS_X-(MENU_WIDTH-RIGHT_MENU_ELEMENTS_WIDTH);
	private static final int BUTTON_TILES_Y = MENU_Y+MENU_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_ENTITIES_Y = BUTTON_TILES_Y+BUTTONS_STANDARD_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_BACKGROUNDS_Y = BUTTON_ENTITIES_Y+BUTTONS_STANDARD_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTONS_TOGGLE_SIZE = BUTTONS_STANDARD_HEIGHT;
	private static final int BUTTONS_TOGGLE_X = RIGHT_MENU_ELEMENTS_X+RIGHT_MENU_ELEMENTS_WIDTH-BUTTONS_TOGGLE_SIZE;
	private static final int BUTTON_TOGGLE_GRID_Y = BUTTON_BACKGROUNDS_Y+BUTTONS_STANDARD_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_TOGGLE_ERASE_Y = BUTTON_TOGGLE_GRID_Y+BUTTONS_TOGGLE_SIZE+DEFAULT_BOX_VERTICAL_PADDING;
	private static final char BUTTON_TOGGLED_ON = '\u2611';
	private static final char BUTTON_TOGGLED_OFF = '\u2610';
	private static final Font BODY_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.PLAIN, (int)(UIConstants.MENU_FONT_BODY_SIZE*0.9));
	private static final String MENU_DISPLAY_CAMERA_X = "Camera X: ";
	private static final String MENU_DISPLAY_CAMERA_Y = "Camera Y: ";
	private static final String MENU_DISPLAY_CURRENT_LAYER = "Current Layer: ";
	private static final String MENU_DISPLAY_CURRENT_TILE = "Current Tile ID: ";
	private static final int BUTTONS_STANDARD_HALF_SIZE_WIDTH = RIGHT_MENU_ELEMENTS_WIDTH/2-DEFAULT_BOX_VERTICAL_PADDING/4;
	private static final int BUTTON_STANDARD_HALF_SIZE_RIGHT_X = RIGHT_MENU_ELEMENTS_X+BUTTONS_STANDARD_HALF_SIZE_WIDTH+DEFAULT_BOX_VERTICAL_PADDING/2;
	private static final int BUTTONS_RESIZE_TOOLS_Y = BUTTON_TOGGLE_ERASE_Y+BUTTONS_STANDARD_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTONS_SAVE_Y = BUTTONS_RESIZE_TOOLS_Y+BUTTONS_STANDARD_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_LOAD_Y = BUTTONS_SAVE_Y+BUTTONS_STANDARD_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	
	// CONSTRUCTOR
	public MenuEditorTools(Menu owner, Level currentLevel, String levelName) {
		super(owner);
		this.currentLevel = currentLevel;
		this.levelName = levelName;
		setLayer(currentLevel.getLayerCount()-1);
		camera = currentLevel.getCamera();
		GameManager.addUpdatable(camera);
		GameManager.addDrawableToBackground(currentLevel);
		cursor = new TileCursor(this, Resources.TILE_SIZE, 0, 0);
		GameManager.addUpdatable(cursor);
		GameManager.addDrawableToBackground(cursor);
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle("Information", HorizontalAlignment.LEFT);
		addButton(BUTTON_ID_TILES, RIGHT_MENU_ELEMENTS_X, BUTTON_TILES_Y, RIGHT_MENU_ELEMENTS_WIDTH, BUTTONS_STANDARD_HEIGHT, "Tiles", false);
		addButton(BUTTON_ID_ENTITIES, RIGHT_MENU_ELEMENTS_X, BUTTON_ENTITIES_Y, RIGHT_MENU_ELEMENTS_WIDTH, BUTTONS_STANDARD_HEIGHT, "Entities", false);
		addButton(BUTTON_ID_BACKGROUNDS, RIGHT_MENU_ELEMENTS_X, BUTTON_BACKGROUNDS_Y, RIGHT_MENU_ELEMENTS_WIDTH, BUTTONS_STANDARD_HEIGHT, "Backgrounds", false);
		addButtonWithMouseHoverTip(BUTTON_ID_CROP, RIGHT_MENU_ELEMENTS_X, BUTTONS_RESIZE_TOOLS_Y, BUTTONS_STANDARD_HALF_SIZE_WIDTH, BUTTONS_STANDARD_HEIGHT, "Crop", false, "Crop this level size to perfectly fit placed tiles", HorizontalAlignment.LEFT);
		addButtonWithMouseHoverTip(BUTTON_ID_ENLARGE, BUTTON_STANDARD_HALF_SIZE_RIGHT_X, BUTTONS_RESIZE_TOOLS_Y, BUTTONS_STANDARD_HALF_SIZE_WIDTH, BUTTONS_STANDARD_HEIGHT, "Enlarge", false, "Specify an amount to enlarge this level by", HorizontalAlignment.LEFT);
		addButton(BUTTON_ID_SAVE, RIGHT_MENU_ELEMENTS_X, BUTTONS_SAVE_Y, BUTTONS_STANDARD_HALF_SIZE_WIDTH, BUTTONS_STANDARD_HEIGHT, "Save", false);
		addButton(BUTTON_ID_SAVE_AS, RIGHT_MENU_ELEMENTS_X+BUTTONS_STANDARD_HALF_SIZE_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, BUTTONS_SAVE_Y, BUTTONS_STANDARD_HALF_SIZE_WIDTH, BUTTONS_STANDARD_HEIGHT, "Save As", false);
		addButton(BUTTON_ID_LOAD, RIGHT_MENU_ELEMENTS_X, BUTTON_LOAD_Y, BUTTONS_STANDARD_HALF_SIZE_WIDTH, BUTTONS_STANDARD_HEIGHT, "Load", false);
		addButtonWithMouseHoverTip(BUTTON_ID_MAIN_MENU, RIGHT_MENU_ELEMENTS_X+BUTTONS_STANDARD_HALF_SIZE_WIDTH+DEFAULT_BOX_HORIZONTAL_PADDING, BUTTON_LOAD_Y, BUTTONS_STANDARD_HALF_SIZE_WIDTH, BUTTONS_STANDARD_HEIGHT, "Exit", false, "Exit the editor, and return to the main menu", HorizontalAlignment.LEFT);
		toggleGridLabel = new SplashString(BUTTONS_TOGGLE_X-DEFAULT_BOX_VERTICAL_PADDING/2, BUTTON_TOGGLE_GRID_Y+BUTTONS_TOGGLE_SIZE/2, Color.WHITE, "Toggle Grid", -1f,HorizontalAlignment.LEFT, true);
		buttonGrid = addButtonWithMouseHoverTip(BUTTON_ID_TOGGLE_GRID, BUTTONS_TOGGLE_X, BUTTON_TOGGLE_GRID_Y, BUTTONS_TOGGLE_SIZE, BUTTONS_TOGGLE_SIZE, ""+BUTTON_TOGGLED_ON, true, "Toggle whether or not the tile grid is visible", HorizontalAlignment.LEFT);
		toggleEraseAllLabel = new SplashString(BUTTONS_TOGGLE_X-DEFAULT_BOX_VERTICAL_PADDING/2, BUTTON_TOGGLE_ERASE_Y+BUTTONS_TOGGLE_SIZE/2, Color.WHITE, "Toggle Erase-All", -1f,HorizontalAlignment.LEFT, true);
		buttonEraseAll = addButtonWithMouseHoverTip(BUTTON_ID_TOGGLE_ERASE, BUTTONS_TOGGLE_X, BUTTON_TOGGLE_ERASE_Y, BUTTONS_TOGGLE_SIZE, BUTTONS_TOGGLE_SIZE, ""+BUTTON_TOGGLED_ON, true, "Toggle whether or not right click erases the selected tile on ALL layers", HorizontalAlignment.LEFT);
		layerSelect = new MenuLayerSelect(this, currentLevel, this);
		addSubMenu(layerSelect, false);
	}

	// CHECK CURSOR POSITION
	private void checkCursorPosition() {
		if (currentMode == EditorMode.EDIT_TILES) {
			int tileX = (cursor.getX()+camera.getX())/Resources.TILE_SIZE;
			int tileY = (cursor.getY()+camera.getY())/Resources.TILE_SIZE;
			if (tileX >= 0 && tileX < currentLevel.getWidthInTiles() && tileY >= 0 && tileY < currentLevel.getHeightInTiles()) cursor.changeColor(TileCursor.CURSOR_COLOR_NORMAL);
			else cursor.changeColor(TileCursor.CURSOR_COLOR_INVALID);
		}
		else cursor.changeColor(TileCursor.CURSOR_COLOR_NORMAL);
	}
	
	// SET BUTTON HOVERED THIS FRAME
	protected void setButtonHovered() {
		buttonHoveredThisFrame = true;
	}
	
	// SET BUTTON UN HOVERED THIS FRAME
	protected void setButtonUnHovered() {
		buttonUnHoveredThisFrame = true;
	}
	
	// CHECK IF BUTTON IS HOVERED
	private void checkButtonHover() {
		if (buttonHoveredThisFrame) cursor.setEnabled(false);
		else if (buttonUnHoveredThisFrame) cursor.setEnabled(true);
		buttonHoveredThisFrame = false;
		buttonUnHoveredThisFrame = false;
	}
	
	// UPDATE
	@Override
	public void update() {
		super.update();
		checkCursorPosition();
		checkButtonHover();
		toggleGridLabel.setAlpha(alpha);
		toggleEraseAllLabel.setAlpha(alpha);
	}

	// PRODUCE HUD STRING
	private String produceHudString() {
		StringBuilder hudStringBuilder = new StringBuilder();
		hudStringBuilder.append(MENU_DISPLAY_CAMERA_X+camera.getX()/Resources.TILE_SIZE+" / "+currentLevel.getWidthInTiles()+'\n');
		hudStringBuilder.append(MENU_DISPLAY_CAMERA_Y+camera.getY()/Resources.TILE_SIZE+" / "+currentLevel.getHeightInTiles()+'\n');
		hudStringBuilder.append(MENU_DISPLAY_CURRENT_LAYER+currentLayer+" / "+(currentLevel.getLayerCount()-1)+'\n');
		hudStringBuilder.append(MENU_DISPLAY_CURRENT_TILE+currentTile);
		return hudStringBuilder.toString();
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
		g2.setFont(BODY_FONT);
		UIUtils.drawWrappedString(g2, shadowColor, produceHudString(), width, MENU_X+BODY_TEXT_HORIZONTAL_PADDING, MENU_Y+BODY_TEXT_VERTICAL_PADDING);
	}
	
	// SET ENABLED
	@Override
	protected void setEnabled(boolean isEnabled) {
		super.setEnabled(isEnabled);
		cursor.setEnabled(isEnabled);
		layerSelect.setEnabled(isEnabled);
	}
	
	// CLOSE
	@Override
	protected void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		currentLevel.close();
		GameManager.removeDrawable(currentLevel);
		GameManager.removeUpdatable(cursor);
		GameManager.removeDrawable(cursor);
		GameManager.removeUpdatable(camera);
		toggleGridLabel.remove();
		toggleEraseAllLabel.remove();
	}
	
	// SET LAYER
	protected void setLayer(int layer) {
		currentLayer = layer;
	}
	
	// GET LAYER
	protected int getCurrentLayer() {
		return currentLayer;
	}
	
	// SET TILE
	protected void setTile(int tileID) {
		currentTile = tileID;
	}
	
	// SET ENTITY
	protected void setEntity(int entityID) {
		currentEntityID = entityID;
		currentMode = EditorMode.EDIT_ENTITIES;
	}
	
	// ENLARGE LEVEL
	protected void enlargeLevel(int extraWidth, int extraHeight, LevelEnlarger.ResizeVertAlign vertAlign, LevelEnlarger.ResizeHoriAlign horiAlign) {
		new LoadingOverlay("Enlarging level...");
		Level enlargedLevel = LevelEnlarger.enlargeLevel(currentLevel, extraWidth, extraHeight, vertAlign, horiAlign);
		MenuEditorTools newEditor = new MenuEditorTools(null, enlargedLevel, levelName);
		if (!isGridEnabled) newEditor.toggleGrid();
		if (!isEraseAllEnabled) newEditor.toggleEraseAll();
		close(AdditionalCloseOperation.CLOSE_ENTIRE_HIERARCHY);
	}
	
	// TOGGLE GRID
	private void toggleGrid() {
		isGridEnabled = !isGridEnabled;
		currentLevel.setGridEnabled(isGridEnabled);
		if (isGridEnabled) buttonGrid.setBodyText(""+BUTTON_TOGGLED_ON);
		else buttonGrid.setBodyText(""+BUTTON_TOGGLED_OFF);
	}

	// TOGGLE ERASE ALL
	private void toggleEraseAll() {
		isEraseAllEnabled = !isEraseAllEnabled;
		if (isEraseAllEnabled) buttonEraseAll.setBodyText(""+BUTTON_TOGGLED_ON);
		else buttonEraseAll.setBodyText(""+BUTTON_TOGGLED_OFF);
	}
	
	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
			case BUTTON_ID_TILES: {
				addSubMenu(new MenuTileSelect(this, this), true);
				currentMode = EditorMode.EDIT_TILES;
			}
		break;
			case BUTTON_ID_ENTITIES: addSubMenu(new MenuEntitySelect(this, this), true);
		break;
			case BUTTON_ID_BACKGROUNDS: addSubMenu(new MenuBackgroundManager(this, currentLevel), true);
		break;
			case BUTTON_ID_CROP: {
				new LoadingOverlay("Cropping level...");
				Level croppedLevel = LevelCropper.cropLevel(currentLevel);
				if (croppedLevel != null) {
					MenuEditorTools newEditor = new MenuEditorTools(null, croppedLevel, levelName);
					if (!isGridEnabled) newEditor.toggleGrid();
					if (!isEraseAllEnabled) newEditor.toggleEraseAll();
					close(AdditionalCloseOperation.CLOSE_ENTIRE_HIERARCHY);
				}
				else new SplashString(UIConstants.SCREEN_WIDTH/2, UIConstants.SCREEN_HEIGHT-UIConstants.SCREEN_HEIGHT/6, Color.RED, "Can't crop levels that have no leading or trailing empty space, or are empty!", 3f, HorizontalAlignment.MIDDLE, true);
			}
		break;
			case BUTTON_ID_ENLARGE: addSubMenu(new MenuEnlargeLevel(this, this), true);
		break;
			case BUTTON_ID_TOGGLE_GRID: toggleGrid();
		break;
			case BUTTON_ID_TOGGLE_ERASE: toggleEraseAll();
		break;
			case BUTTON_ID_SAVE: if (levelName != null) {
				new LoadingOverlay("Saving level...");
				LevelSaver.saveLevelToFile(currentLevel, levelName);
			}
			else {
				addSubMenu(new MenuLevelSaveAs(this, currentLevel), true);
			}
		break;
			case BUTTON_ID_SAVE_AS: addSubMenu(new MenuLevelSaveAs(this, currentLevel), true);
		break;
			case BUTTON_ID_LOAD: addSubMenu(new MenuLevelSelect(this, this), true);
		break;
			case BUTTON_ID_MAIN_MENU: addSubMenu(new MenuWarningMainMenuUnsaved(this), true);
		break;
		}
	}
	
	// BUTTON MOUSED
	@Override
	public void buttonMoused(int buttonID) {
		setButtonHovered();
	}

	// BUTTON UN MOUSED
	@Override
	public void buttonUnMoused(int buttonID) {
		setButtonUnHovered();
	}
	
	// TILE CURSOR LEFT CLICK
	@Override
	public void leftClicked(int x, int y) {
		int tileX = (x+camera.getX())/Resources.TILE_SIZE;
		int tileY = (y+camera.getY())/Resources.TILE_SIZE;
		if (cursor.getIsEnabled()) {
			if (currentMode == EditorMode.EDIT_TILES) {
				if (tileX >= 0 && tileX < currentLevel.getWidthInTiles() && tileY >= 0 && tileY < currentLevel.getHeightInTiles()) {
					currentLevel.getLayer(currentLayer).setTile(currentTile, tileX, tileY);
				}
			}
			if (currentMode == EditorMode.EDIT_ENTITIES) {
				currentLevel.removeEntitiesAt(tileX*Resources.TILE_SIZE, tileY*Resources.TILE_SIZE);
				currentLevel.addEntity(new EntityDataHolder(currentEntityID, tileX*Resources.TILE_SIZE, tileY*Resources.TILE_SIZE, 0));
			}
		}
	}

	// TILE CURSOR RIGHT CLICK
	@Override
	public void rightClicked(int x, int y) {
		if (cursor.getIsEnabled()) {
			int tileX = (x+camera.getX())/Resources.TILE_SIZE;
			int tileY = (y+camera.getY())/Resources.TILE_SIZE;
			if (currentMode == EditorMode.EDIT_TILES) {
				if (tileX >= 0 && tileX < currentLevel.getWidthInTiles() && tileY >= 0 && tileY < currentLevel.getHeightInTiles()) {
					if (!isEraseAllEnabled) currentLevel.getLayer(currentLayer).setTile(Level.BLANK_TILE_ID, tileX, tileY);
					else for (int i = 0; i < currentLevel.getLayerCount(); i++) {
						currentLevel.getLayer(i).setTile(Level.BLANK_TILE_ID, tileX, tileY);
					}
				}
			}
			if (currentMode == EditorMode.EDIT_ENTITIES) {
				currentLevel.removeEntitiesAt(tileX*Resources.TILE_SIZE, tileY*Resources.TILE_SIZE);
			}
		}
	}
	
	// SET LEVEL NAME
	protected void setLevelName(String newName) {
		levelName = newName;
	}
	
	//UNUSED
	@Override
	public void buttonLeftPressed(int buttonID) {}
	@Override
	public void leftReleased(int x, int y) {}
	@Override
	public void rightReleased(int x, int y) {}
	@Override
	public void buttonRightPressed(int buttonID) {}
	@Override
	public void buttonRightReleased(int buttonID) {}
}