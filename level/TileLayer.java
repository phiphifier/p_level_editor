package level;

public class TileLayer {

	// VARS
	private int[][] layerData;
	private int widthInTiles;
	private int heightInTiles;
	private boolean isVisible = true;
	
	// CONSTRUCTOR
	public TileLayer(int [][] layerData, Level owner) {
		this.layerData = layerData;
		widthInTiles = layerData.length;
		heightInTiles = layerData[0].length;
	}
	
	// GET WIDTH IN TILES
	public int getWidthInTiles() {
		return widthInTiles;
	}
	
	// GET HEIGHT IN TILES
	public int getHeightInTiles() {
		return heightInTiles;
	}
	
	// SET TILE
	public void setTile(int tileID, int tileX, int tileY) {
		if (tileX >= 0 && tileX < widthInTiles && tileY >= 0 && tileY < heightInTiles) layerData[tileX][tileY] = tileID;
	}
	
	// GET TILE AT
	public int getTileAt(int tileX, int tileY) {
		if (tileX >= 0 && tileX < widthInTiles && tileY >= 0 && tileY < heightInTiles) return layerData[tileX][tileY];
		else return 0;
	}
	
	// SET VISIBLE
	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
	
	// GET IS VISIBILE
	public boolean getIsVisible() {
		return isVisible;
	}
	
	// GET LAYER DATE
	public int[][] getLayerData() {
		return layerData;
	}
}