package level;

import main.Inputs;
import main.Panel;
import main.Resources;
import main.Updatable;

public class Camera implements Updatable {

	// BOUNDS ENUM
	public enum Bounds {
		OVER_SCROLL,
		REGULAR,
		NONE
	}
	//VARS
	private int x = 0, y = 0;
	private int cameraSpeed = 1;
	private int viewWidth = Panel.SCREEN_SIZE.width;
	private int viewHeight = Panel.SCREEN_SIZE.height;
	private Bounds bounds = Bounds.NONE;
	private int maxX;
	private int maxY;
	private int minX;
	private int minY;
	
	// GET X
	public int getX() {
		return x;
	}
	
	// GET Y
	public int getY() {
		return y;
	}
	
	// GET SCREEN X
	public int getScreenX(int subjectX) {
		return subjectX-x;
	}

	// GET SCREEN Y
	public int getScreenY(int subjectY) {
		return subjectY-y;
	}
	
	// GET VIEW WIDTH
	public int getViewWidth() {
		return viewWidth;
	}
	
	// GET VIEW HEIGHT
	public int getViewHeight() {
		return viewHeight;
	}
	
	// ENABLE BOUNDS
	public void enableBounds(Bounds bounds, int topBound, int leftBound, int bottomBound, int rightBound) {
		minY = topBound;
		minX = leftBound;
		maxY = bottomBound;
		maxX = rightBound;
		this.bounds = bounds;
	}
	
	// DISABLE BOUNDS
	public void disableBounds(int topBound, int leftBound, int bottomBound, int rightBound) {
		bounds = Bounds.NONE;
	}
	
	// UPDATE
	@Override
	public void update() {
		switch(bounds) {
			case NONE: {
				if (Inputs.isUpPressed()) y -= cameraSpeed*Resources.TILE_SIZE;
				if (Inputs.isLeftPressed()) x -= cameraSpeed*Resources.TILE_SIZE;
				if (Inputs.isDownPressed()) y += cameraSpeed*Resources.TILE_SIZE;
				if (Inputs.isRightPressed()) x += cameraSpeed*Resources.TILE_SIZE;
			}
		break;
			case REGULAR: {
				if (Inputs.isUpPressed()) y = Math.max(y-cameraSpeed*Resources.TILE_SIZE, minY);
				if (Inputs.isLeftPressed()) x = Math.max(x-cameraSpeed*Resources.TILE_SIZE, minX);
				if (Inputs.isDownPressed()) y = Math.min(y+cameraSpeed*Resources.TILE_SIZE, maxY-viewHeight);
				if (Inputs.isRightPressed()) x = Math.min(x+cameraSpeed*Resources.TILE_SIZE, maxX-viewWidth);
			}
		break;
			case OVER_SCROLL: {
				if (Inputs.isUpPressed()) y = Math.max(y-cameraSpeed*Resources.TILE_SIZE, minY-viewHeight+Resources.TILE_SIZE);
				if (Inputs.isLeftPressed()) x = Math.max(x-cameraSpeed*Resources.TILE_SIZE, minX-viewWidth+Resources.TILE_SIZE);
				if (Inputs.isDownPressed()) y = Math.min(y+cameraSpeed*Resources.TILE_SIZE, maxY-Resources.TILE_SIZE);
				if (Inputs.isRightPressed()) x = Math.min(x+cameraSpeed*Resources.TILE_SIZE, maxX-Resources.TILE_SIZE);
			}
		}
	}
}