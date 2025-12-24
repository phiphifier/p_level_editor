package level;

public class LevelLayerShift {

	// SHIFT LEVEL UP
	public static void shiftLayerUp(Level currentLevel, int layerIndex) {
		TileLayer l1 = currentLevel.getLayer(layerIndex);
		TileLayer l2 = currentLevel.getLayer(layerIndex-1);
		currentLevel.getLayers()[layerIndex-1] = l1;
		currentLevel.getLayers()[layerIndex] = l2;
	}

	// SHIFT LEVEL DOWN
	public static void shiftLayerDown(Level currentLevel, int layerIndex) {
		TileLayer l1 = currentLevel.getLayer(layerIndex);
		TileLayer l2 = currentLevel.getLayer(layerIndex+1);
		currentLevel.getLayers()[layerIndex+1] = l1;
		currentLevel.getLayers()[layerIndex] = l2;
	}
	
	// CONSTRUCTOR
	private LevelLayerShift() {
		
	}
}