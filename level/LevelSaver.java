package level;

import config.Config;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.ArrayList;


public class LevelSaver {
	
	/*
	 * - This class is meant for saving custom .plvl formatted files which are are 2D tile maps with layers, entity data, and background data, all
	 * encoded with integers. 
	 * 
	 * - The next value delimiter(:) signals that a number value has ended.
	 * 
	 * - The next data set delimiter(^) signals the end of a specific data type.
	 * 
	 * - Properly encoded .nlvl files should be a single line, and contain three sets of data: level dimensions in tiles, entity data,
	 * and background data.
	 * 
	 * - Tile data is simply just a list of integers in a specific order.
	 * 
	 * - Entity data is expected to come in chunks of four: ent id, raw x, raw y, and an additional integer flag.
	 * 
	 * - Background data is expected to come in chunks of six: bckg id, screen x, screen y, x offset per screen wrap, y offset per screen wrap,
	 * and a integer represented boolean for is tiled.
	 * 
	 */
	
	// DELIMITERS
	private static final char DELIMITER_NEXT_VALUE = ':';
	private static final char DELIMITER_NEXT_DATA_SET = '^';
	
	// SAVE LEVEL
	public static boolean saveLevelToFile(Level level, String name) {
		if (level == null || name == null) return false;
		int width = level.getWidthInTiles();
		int height = level.getHeightInTiles();
		int layerCount = level.getLayerCount();
		TileLayer[] layers = new TileLayer[layerCount];
		for (int i = 0; i < layerCount; i++) {
			layers[i] = level.getLayer(i);
		}
		ArrayList<EntityDataHolder> entities = level.getEntities();
		ArrayList<BackgroundDataHolder> backgrounds = level.getBackgrounds();
		
		try(BufferedWriter bw = new BufferedWriter(new FileWriter(new File(Path.of(Config.getProperty(Config.PROPERTY_LEVEL_STORAGE_PATH), name).toString())))) {
			StringBuilder levelData = new StringBuilder();
			levelData.append(width);
			levelData.append(DELIMITER_NEXT_VALUE);
			levelData.append(height);
			levelData.append(DELIMITER_NEXT_VALUE);
			levelData.append(layerCount);
			levelData.append(DELIMITER_NEXT_DATA_SET);
			for (int layer = 0; layer < layers.length; layer++) {
				for (int y = 0; y < layers[0].getHeightInTiles(); y++) {
					for (int x = 0; x < layers[0].getWidthInTiles(); x++) {
						levelData.append(layers[layer].getTileAt(x, y));
						levelData.append(DELIMITER_NEXT_VALUE);
					}
				}
			}
			levelData.setLength(levelData.length()-1);
			levelData.append(DELIMITER_NEXT_DATA_SET);
			for (int i = 0; i < entities.size(); i++) {
				EntityDataHolder e = entities.get(i);
				levelData.append(e.getID());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(e.getX());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(e.getY());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(e.getAdditionalEntityData());
				levelData.append(DELIMITER_NEXT_VALUE);
			}
			if (!entities.isEmpty()) levelData.setLength(levelData.length()-1);
			levelData.append(DELIMITER_NEXT_DATA_SET);
			for (int i = 0; i < backgrounds.size(); i++) {
				BackgroundDataHolder b = backgrounds.get(i);
				levelData.append(b.getID());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(b.getX());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(b.getY());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(b.getXOffsetPerScreenWrap());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(b.getYOffsetPerScreenWrap());
				levelData.append(DELIMITER_NEXT_VALUE);
				levelData.append(b.getIsTiled());
				levelData.append(DELIMITER_NEXT_VALUE);
			}
			if (!backgrounds.isEmpty()) levelData.setLength(levelData.length()-1);
			levelData.append(DELIMITER_NEXT_DATA_SET);
			bw.write(levelData.toString());
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// CONSTRUCTOR
	private LevelSaver() {
		
	}
}