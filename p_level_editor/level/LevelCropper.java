package level;

import main.Resources;

public class LevelCropper {

	// CONSTANTS
	private static final int EMPTY_TILE_ID = 0;
	
	// CROP LEVEL
	public static Level cropLevel(Level levelToBeCropped) {
		// CHECK LEVEL BOUNDS BASED ON TILES
		int topTile = levelToBeCropped.getHeightInTiles();
		int leftTile = levelToBeCropped.getWidthInTiles();
		int bottomTile = 0;
		int rightTile = 0;
		for (int layer = 0; layer < levelToBeCropped.getLayerCount(); layer++) {
			for (int y = 0; y < levelToBeCropped.getHeightInTiles(); y++) {
				for (int x = 0; x < levelToBeCropped.getWidthInTiles(); x++) {
					int currentID = levelToBeCropped.getLayer(layer).getTileAt(x, y);
					if (currentID != EMPTY_TILE_ID) {
						if (y < topTile) topTile = y;
						if (x < leftTile) leftTile = x;
						if (y > bottomTile) bottomTile = y;
						if (x > rightTile) rightTile = x;
					}
				}
			}
		}
		// CREATE AND POPULATE CROPPED LEVEL
		int croppedWidth = rightTile-leftTile+1;
		int croppedHeight = bottomTile-topTile+1;
		if (croppedWidth <= 0 || croppedHeight <= 0 || croppedWidth == levelToBeCropped.getWidthInTiles() && croppedHeight == levelToBeCropped.getHeightInTiles()) return null;
		int[][][] croppedLevelData = new int[croppedWidth][croppedHeight][levelToBeCropped.getLayerCount()];
		final int originalLevelXStart = leftTile;
		final int originalLevelYStart = topTile;
		for (int layer = 0; layer < levelToBeCropped.getLayerCount(); layer++) {
			for (int y = 0; y < croppedHeight; y++) {
				for (int x = 0; x < croppedWidth; x++) {
					croppedLevelData[x][y][layer] = levelToBeCropped.getLayer(layer).getTileAt(originalLevelXStart+x, originalLevelYStart+y);
				}
			}
		}
		// SHIFT ENTITY POSITIONS
		if (topTile > 0 || leftTile > 0) for (int i = 0; i < levelToBeCropped.getEntities().size(); i++) {
			EntityDataHolder e = levelToBeCropped.getEntities().get(i);
			e.setX(e.getX()-leftTile*Resources.TILE_SIZE);
			e.setY(e.getY()-topTile*Resources.TILE_SIZE);
			
		}
		Level croppedLevel = new Level(croppedLevelData, levelToBeCropped.getEntities(), levelToBeCropped.getBackgrounds());
		return croppedLevel;
	}
	
	// CONSTRUCTOR
	private LevelCropper() {
		
	}
}