package level;

public class BackgroundDataHolder {

	// VARS
	private int id, x, y, xOffsetPerScreenWrap, yOffsetPerScreenWrap, isTiled;
	
	// CONSTRUCTOR
	public BackgroundDataHolder(int id, int x, int y, int xOffsetPerScreenWrap, int yOffsetPerScreenWrap, int isTiled) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.xOffsetPerScreenWrap = xOffsetPerScreenWrap;
		this.yOffsetPerScreenWrap = yOffsetPerScreenWrap;
		this.isTiled = isTiled;
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
	
	// GET X OFFSET PER SCREEN WRAP
	public int getXOffsetPerScreenWrap() {
		return xOffsetPerScreenWrap;
	}
	
	// GET Y OFFSET PER SCREEN WRAP
	public int getYOffsetPerScreenWrap() {
		return yOffsetPerScreenWrap;
	}
	
	// GET IS TILED
	public int getIsTiled() {
		return isTiled;
	}
	
	// SET X
	public void setX(int x) {
		this.x = x;
	}
	
	// SET Y
	public void setY(int y) {
		this.y = y;
	}
	
	// SET X OFFSET PER SCREEN WRAP
	public void setXOffsetPerScreenWrap(int xOffsetPerScreenWrap) {
		this.xOffsetPerScreenWrap = xOffsetPerScreenWrap;
	}
	
	// SET Y OFFSET PER SCREEN WRAP
	public void setYOffsetPerScreenWrap(int yOffsetPerScreenWrap) {
		this.yOffsetPerScreenWrap = yOffsetPerScreenWrap;
	}
	
	// SET IS TILED
	public void setIsTiled(int isTiled) {
		this.isTiled = isTiled;
	}
}