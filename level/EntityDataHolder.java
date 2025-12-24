package level;

import main.Drawable;
import main.Resources;
import ui.UIConstants;
import ui.UIUtils;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;

public class EntityDataHolder implements Drawable {

	// VARS
	private Camera camera;
	private String displayString;
	private int id, x, y, additionalEntityData;
	private int drawX, drawY, width, height;
	private static final Color ENTITY_COLOR = new Color(0,0,150);
	private static final int ENTITY_FONT_SIZE = 13;
	private static final Font ENTITY_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, ENTITY_FONT_SIZE);
	private static final int DRAW_X_OFFSET = 3;
	private static final int DRAW_Y_OFFSET = 3;
	
	//CONSTRUCTOR
	public EntityDataHolder(int id, int x, int y, int extraData) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.additionalEntityData = extraData;
		drawX = x+DRAW_X_OFFSET;
		drawY = y+DRAW_Y_OFFSET;
		width = Resources.TILE_SIZE-DRAW_X_OFFSET*2;
		height = Resources.TILE_SIZE-DRAW_Y_OFFSET*2;
		setDisplayString();
	}
	
	// SET CAMERA
	public void setCamera(Camera c) {
		camera = c;
	}
	
	// SET DISPLAY STRING
	private void setDisplayString() {
		if (additionalEntityData != 0) this.displayString = "ID " + id + "\nAD " + additionalEntityData;
		else this.displayString = "id:" + id;
	}
	
	// GET ID
	public int getID() {
		return id;
	}
	
	// GET X
	public int getX() {
		return x;
	}
	
	// GET Y
	public int getY() {
		return y;
	}
	
	// SET X
	public void setX(int x) {
		this.x = x;
		drawX = x+DRAW_X_OFFSET;
		width = Resources.TILE_SIZE-DRAW_X_OFFSET*2;
	}
	
	// SET Y
	public void setY(int y) {
		this.y = y;
		drawY = y+DRAW_Y_OFFSET;
		height = Resources.TILE_SIZE-DRAW_Y_OFFSET*2;
	}
	
	// GET ADDITIONAL ENTITY DATA
	public int getAdditionalEntityData() {
		return additionalEntityData;
	}
	
	// SET ADDITIONAL ENTITY DATA
	public void setAdditionalEntityData(int additionalData) {
		additionalEntityData = additionalData;
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(ENTITY_COLOR);
		g2.fillRect(camera.getScreenX(drawX), camera.getScreenY(drawY), width, height);
		g2.setFont(ENTITY_FONT);
		g2.setColor(UIConstants.UI_COLOR_TEXT);
		UIUtils.drawWrappedString(g2, Color.BLACK, displayString, width*2, camera.getScreenX(x+3), camera.getScreenY(y+ENTITY_FONT_SIZE/3));
	}
}