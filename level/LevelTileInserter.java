package level;

public class LevelTileInserter {

	// INSERT NEW LAYER
	public static void insertNewLayer(Level currentLevel, int atIndex) {
		TileLayer[] newLayers = new TileLayer[currentLevel.getLayerCount()+1];
		TileLayer tl = new TileLayer(new int[currentLevel.getWidthInTiles()][currentLevel.getHeightInTiles()], currentLevel);
		int oldLayersIndex = 0;
		for (int i = 0; i < currentLevel.getLayerCount()+1; i++) {
			if (i != atIndex) {
				newLayers[i] = currentLevel.getLayers()[oldLayersIndex];
				oldLayersIndex++;
			}
			else newLayers[i] = tl;
		}
		currentLevel.setLayers(newLayers);
	}
	
	// CONSTRUCTOR
	private LevelTileInserter() {
		
	}
}