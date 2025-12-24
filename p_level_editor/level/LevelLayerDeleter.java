package level;

public class LevelLayerDeleter {

	// DELETE LAYER AT
	public static void deleteLayerAt(Level currentLevel, int layerDepth) {
		TileLayer[] layers = currentLevel.getLayers();
		TileLayer[] newLayers = new TileLayer[layers.length-1];
		int oldLayersIndex = 0;
		for (int i = 0; i < newLayers.length; i++) {
			if (i == layerDepth) oldLayersIndex++;
			newLayers[i] = layers[oldLayersIndex];
			oldLayersIndex++;
		}
		currentLevel.setLayers(newLayers);
	}
	
	// CONSTRUCTOR
	private LevelLayerDeleter() {
		
	}
}