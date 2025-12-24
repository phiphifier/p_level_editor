package ui;

import main.GameManager;
import main.Resources;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MenuTileSelect extends Menu implements TileCursorListener {

	// VARS
		// MENU PROPERTIES
	private static final int BUTTON_ID_CANCEL = 0;
	private static final int BUTTON_WIDTH = Resources.getTilesetImageWidth();
	private static final int BUTTON_HEIGHT = 32;
	private static final int MENU_WIDTH = Resources.getTilesetImageWidth()+DEFAULT_BOX_HORIZONTAL_PADDING*4;
	private static final int MENU_HEIGHT = Resources.getTilesetImageHeight()+BUTTON_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING*4;
	private static final int MENU_X = UIConstants.SCREEN_WIDTH/2-MENU_WIDTH/2;
	private static final int MENU_Y = UIConstants.SCREEN_HEIGHT/2-MENU_HEIGHT/2;
	private static final String MENU_TITLE = "Select a tile:";
	private static final int BUTTON_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_Y = MENU_Y+MENU_HEIGHT-BUTTON_HEIGHT-DEFAULT_BOX_VERTICAL_PADDING;
	private static final int TILESET_IMAGE_X = MENU_X+DEFAULT_BOX_HORIZONTAL_PADDING*2;
	private static final int TILESET_IMAGE_Y = MENU_Y+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int TILESET_IMAGE_WIDTH = Resources.getTilesetImageWidth();
	private static final int TILESET_IMAGE_HEIGHT = Resources.getTilesetImageHeight();
	private static final Color TILESET_IMAGE_BACKGROUND_COLOR = UIConstants.MENU_COLOR_BORDER;
	private static final Color TILESET_IMAGE_BACKGROUND_BORDER_COLOR = UIConstants.UI_COLOR_SHADOW;
	private static final Color TILESET_IMAGE_BACKGROUND_BORDER_COLOR2 = UIConstants.BUTTON_COLOR_PRESSED;
	private static final int TILESET_IMAGE_BACKGROUND_THICKNESS = 3;
		// INSTANCE FIELDS
	private MenuEditorTools editor;
	private BufferedImage tileImage;
	private TileCursor cursor;
	private int cursorOffsetX = TILESET_IMAGE_X%Resources.TILE_SIZE;
	private int cursorOffsetY = TILESET_IMAGE_Y%Resources.TILE_SIZE;
	
	// CONSTRUCTOR
	public MenuTileSelect(Menu owner, MenuEditorTools editor) {
		super(owner);
		this.editor = editor;
		setPosition(MENU_X, MENU_Y);
		setSize(MENU_WIDTH, MENU_HEIGHT);
		setTitle(MENU_TITLE, HorizontalAlignment.LEFT);
		addButton(BUTTON_ID_CANCEL, BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT, "Cancel", false);
		cursor = new TileCursor(this, Resources.TILE_SIZE, -cursorOffsetX, -cursorOffsetY);
		GameManager.addUpdatable(cursor);
		GameManager.addDrawable(cursor);
		tileImage = Resources.getTilesetImage();
	}
	
	// UPDATE
	@Override
	public void update() {
		super.update();
		checkCursorPosition();
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		drawMenuBody(g2);
		g2.setColor(TILESET_IMAGE_BACKGROUND_BORDER_COLOR2);
		g2.fillRect(TILESET_IMAGE_X-TILESET_IMAGE_BACKGROUND_THICKNESS, TILESET_IMAGE_Y-TILESET_IMAGE_BACKGROUND_THICKNESS, TILESET_IMAGE_WIDTH+TILESET_IMAGE_BACKGROUND_THICKNESS*2, TILESET_IMAGE_HEIGHT+TILESET_IMAGE_BACKGROUND_THICKNESS*2);
		g2.setColor(TILESET_IMAGE_BACKGROUND_BORDER_COLOR);
		g2.fillRect(TILESET_IMAGE_X, TILESET_IMAGE_Y, TILESET_IMAGE_WIDTH+TILESET_IMAGE_BACKGROUND_THICKNESS, TILESET_IMAGE_HEIGHT+TILESET_IMAGE_BACKGROUND_THICKNESS);
		g2.setColor(TILESET_IMAGE_BACKGROUND_COLOR);
		g2.fillRect(TILESET_IMAGE_X, TILESET_IMAGE_Y, TILESET_IMAGE_WIDTH, TILESET_IMAGE_HEIGHT);
		g2.drawImage(tileImage, TILESET_IMAGE_X, TILESET_IMAGE_Y, TILESET_IMAGE_WIDTH, TILESET_IMAGE_HEIGHT, null);
	}
	
	// LEFT RELEASED
	@Override
	public void leftReleased(int x, int y) {
		if (cursor.getIsEnabled()) {
			int tileX = (x+cursorOffsetX-TILESET_IMAGE_X)/Resources.TILE_SIZE;
			int tileY = (y+cursorOffsetY-TILESET_IMAGE_Y)/Resources.TILE_SIZE;
			int tileID = tileX+(tileY*(Resources.getTilesetImageWidth()/Resources.TILE_SIZE));
			editor.setTile(tileID);
			close(AdditionalCloseOperation.ENABLE_OWNER);
		}
	}

	// CHECK CURSOR POSITION
	private void checkCursorPosition() {
		if (cursor.getX() >= TILESET_IMAGE_X && cursor.getX() < TILESET_IMAGE_X+TILESET_IMAGE_WIDTH && cursor.getY() >= TILESET_IMAGE_Y && cursor.getY() < TILESET_IMAGE_Y+TILESET_IMAGE_HEIGHT) {
			cursor.setEnabled(true);
		}
		else {
			cursor.setEnabled(false);
		}
	}
	
	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		close(AdditionalCloseOperation.ENABLE_OWNER);
	}

	// CLOSE
	@Override
	public void close(AdditionalCloseOperation additionCloseOperation) {
		super.close(additionCloseOperation);
		GameManager.removeUpdatable(cursor);
		GameManager.removeDrawable(cursor);
	}
	
	// UNUSED
	@Override
	public void buttonLeftPressed(int buttonID) {}
	@Override
	public void leftClicked(int x, int y) {}
	@Override
	public void rightClicked(int x, int y) {}
	@Override
	public void rightReleased(int x, int y) {}
	@Override
	public void buttonMoused(int buttonID) {}
	@Override
	public void buttonUnMoused(int buttonID) {}
	@Override
	public void buttonRightPressed(int buttonID) {}
	@Override
	public void buttonRightReleased(int buttonID) {}
}