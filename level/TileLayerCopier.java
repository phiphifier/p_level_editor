package level;

public class TileLayerCopier {

	// VARS
	private static final int BLANK_TILE = 0;
	
	// MERGE LAYERS
	public static void copyLayerTo(TileLayer originLayer, TileLayer destinationLayer) {
		int width = originLayer.getWidthInTiles();
		int height = originLayer.getHeightInTiles();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int currentTileID = originLayer.getTileAt(x, y);
				if (currentTileID != BLANK_TILE) {
					destinationLayer.setTile(currentTileID, x, y);
				}
			}
		}
	}
	
	// CONSTRUCTOR
	private TileLayerCopier() {
		
	}
}