package level;

import main.Resources;

public class LevelEnlarger {
	
	public enum ResizeHoriAlign {
		LEFT,
		MIDDLE,
		RIGHT
	}
	
	public enum ResizeVertAlign {
		TOP,
		MIDDLE,
		BOTTOM
	}
	
	// ENLARGE LEVEL
	public static Level enlargeLevel(Level levelToBeEnlarged, int extraWidth, int extraHeight, ResizeVertAlign vertAlignment, ResizeHoriAlign horiAlignment) {
		// SET OFFSETS
		int yOffset;
		int xOffset;
		switch(vertAlignment) {
		case TOP: yOffset = 0;
		break;
		case MIDDLE: yOffset = extraHeight/2;
		break;
		case BOTTOM: yOffset = extraHeight;
		break;
		default: yOffset = 0;
		}
		switch(horiAlignment) {
		case LEFT: xOffset = 0;
		break;
		case MIDDLE: xOffset = extraWidth/2;
		break;
		case RIGHT: xOffset = extraWidth;
		break;
		default: xOffset = 0;
		}
		// CREATE AND POPULATE RESIZED LEVEL
		int[][][] enlargedLevelData = new int[levelToBeEnlarged.getWidthInTiles()+extraWidth][levelToBeEnlarged.getHeightInTiles()+extraHeight][levelToBeEnlarged.getLayerCount()];
		for (int layer = 0; layer < levelToBeEnlarged.getLayerCount(); layer++) {
			for (int y = yOffset; y < levelToBeEnlarged.getHeightInTiles()+yOffset; y++) {
				for (int x = xOffset; x < levelToBeEnlarged.getWidthInTiles()+xOffset; x++) {
					enlargedLevelData[x][y][layer] = levelToBeEnlarged.getLayer(layer).getTileAt(x-xOffset, y-yOffset);
				}
			}
		}
		// SHIFT ENTITY POSITIONS
		if (xOffset > 0 || yOffset > 0) for (int i = 0; i < levelToBeEnlarged.getEntities().size(); i++) {
			EntityDataHolder e = levelToBeEnlarged.getEntities().get(i);
			e.setX(e.getX()+xOffset*Resources.TILE_SIZE);
			e.setY(e.getY()+yOffset*Resources.TILE_SIZE);
		}
		return new Level(enlargedLevelData, levelToBeEnlarged.getEntities(), levelToBeEnlarged.getBackgrounds());
	}
	
	// CONSTRUCTOR
	private LevelEnlarger() {
		
	}
}