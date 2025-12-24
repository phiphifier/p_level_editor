package level;

import java.io.FileReader;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import config.Config;

public class LevelLoader {

	/*
	 * - This class is meant for loading custom .plvl formatted files which are are 2D tile maps with layers, entity data, and background data, all
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

	// DATA SET ENUM
	private enum dataSet {
		DIMENSIONS,
		TILES,
		ENTITIES,
		BACKGROUNDS
	}
	
	// CONSTANTS
		// FILE STUFF
	private static final String LEVEL_FORMAT = ".plvl";
	private static final int LEVEL_SIZE_DECIMAL_PLACES_TO_SHOW = 2;
		// DELIMITERS
	private static final char DELIMITER_NEXT_VALUE = ':';
	private static final char DELIMITER_NEXT_DATA_SET = '^';
		// DIMENSION INDECES
	private static final int INDECES_DIMENSIONS_AMOUNT = 3;
	private static final int INDEX_DIMENSION_WIDTH = 0;
	private static final int INDEX_DIMENSION_HEIGHT = 1;
	private static final int INDEX_DIMENSION_LAYERS = 2;
		// ENTITY INDECES
	private static final int INDECES_ENTITY_AMOUNT = 4;
	private static final int INDEX_ENTITY_ID = 0;
	private static final int INDEX_ENTITY_X = 1;
	private static final int INDEX_ENTITY_Y = 2;
	private static final int INDEX_ENTITY_EXTRA = 3;
		// BACKGROUND INDECES
	private static final int INDECES_BACKGROUND_AMOUNT = 6;
	private static final int INDEX_BACKGROUND_ID = 0;
	private static final int INDEX_BACKGROUND_X = 1;
	private static final int INDEX_BACKGROUND_Y = 2;
	private static final int INDEX_BACKGROUND_X_OFFSET_PER_SCREEN_WRAP = 3;
	private static final int INDEX_BACKGROUND_Y_OFFSET_PER_SCREEN_WRAP = 4;
	private static final int INDEX_BACKGROUND_IS_TILED = 5;
	
	// GET LEVEL IN FILE NAMES
	public static String[] getLevelInFileNames(String folderPath) {
		File fileAtPath = new File(folderPath);
		String[] allItems = fileAtPath.list();
		ArrayList<String> filteredItems = new ArrayList<String>();
		for(int i = 0; i < allItems.length; i++) {
			if (allItems[i].endsWith(LEVEL_FORMAT)) filteredItems.add(allItems[i]);
		}
		Collections.sort(filteredItems);
		return filteredItems.toArray(new String[0]);
	}
	
	// GET LEVEL SIZE
	public static String getLevelFileSizeInString(String folderPath, String levelName) {
		try {
			File fileAtPath = new File(Path.of(folderPath, levelName).toString());
			long fileSizeInBytes = fileAtPath.length();
			double fileSizeInKB = fileSizeInBytes/(double)1000;
			
			if (fileSizeInKB < 1000) {
				String wholeNumberKBs = ""+(int)fileSizeInKB;
				String decimalNumberKBs = (""+(fileSizeInKB-(int)fileSizeInKB)).substring(2, Math.min(2+LEVEL_SIZE_DECIMAL_PLACES_TO_SHOW, (""+(fileSizeInKB-(int)fileSizeInKB)).length()));
				String fileSizeStringInKB = wholeNumberKBs+"."+decimalNumberKBs+"kB";
				return fileSizeStringInKB;
			}
			else {
				double fileSizeInMB = fileSizeInKB/(double)1000;
				String wholeNumberMBs = ""+(int)fileSizeInMB;
				String decimalNumberMBs = (""+(fileSizeInMB-(int)fileSizeInMB)).substring(2, Math.min(2+LEVEL_SIZE_DECIMAL_PLACES_TO_SHOW, (""+(fileSizeInMB-(int)fileSizeInMB)).length()));
				String fileSizeStringInMB = wholeNumberMBs+"."+decimalNumberMBs+"mB";
				return fileSizeStringInMB;
			}
		}
		catch(Exception e) {
			return "not found";
		}
	}
	
	// LOAD LEVEL
	public static Level loadLevel(String name) {
		try(BufferedReader br = new BufferedReader(new FileReader(new File(Path.of(Config.getProperty(Config.PROPERTY_LEVEL_STORAGE_PATH), name).toString())))) {
			String allLevelData = br.readLine();
			StringBuilder currentValueString = new StringBuilder();
			ArrayList<EntityDataHolder> finalEntities = new ArrayList<EntityDataHolder>();
			ArrayList<BackgroundDataHolder> finalBackgrounds = new ArrayList<BackgroundDataHolder>();
			int[] dimensions = new int[INDECES_DIMENSIONS_AMOUNT];
			int[][][] layerData = new int[0][0][0];
			int[] currentEntity = new int[INDECES_ENTITY_AMOUNT];
			int[] currentBackground = new int[INDECES_BACKGROUND_AMOUNT];
			ArrayList<Integer> tileIDs = new ArrayList<Integer>();
			dataSet currentDataSet = dataSet.DIMENSIONS;
			int currentIndex = 0;
			for(int i = 0; i < allLevelData.length(); i++) {
				char currentChar = allLevelData.charAt(i);
				if (currentChar != DELIMITER_NEXT_VALUE && currentChar != DELIMITER_NEXT_DATA_SET) {
					currentValueString.append(currentChar);
					continue;
				}
				else {
					switch(currentDataSet) {
						case dataSet.DIMENSIONS: if (currentValueString.length() > 0) {
							dimensions[currentIndex] = Integer.valueOf(currentValueString.toString());
							currentIndex++;
						}
					break;
						case dataSet.TILES: if (currentValueString.length() > 0) {
							tileIDs.add(Integer.valueOf(currentValueString.toString()));
						}
					break;
						case dataSet.ENTITIES: if (currentValueString.length() > 0) {
							currentEntity[currentIndex] = Integer.valueOf(currentValueString.toString());
							currentIndex++;
							if (currentIndex >= INDECES_ENTITY_AMOUNT) {
								finalEntities.add(new EntityDataHolder(currentEntity[INDEX_ENTITY_ID], currentEntity[INDEX_ENTITY_X],
										currentEntity[INDEX_ENTITY_Y], currentEntity[INDEX_ENTITY_EXTRA]));
								currentEntity = new int[INDECES_ENTITY_AMOUNT];
								currentIndex = 0;
							}
						}
					break;
						case dataSet.BACKGROUNDS: if (currentValueString.length() > 0) {
							currentBackground[currentIndex] = Integer.valueOf(currentValueString.toString());
							currentIndex++;
							if (currentIndex >= INDECES_BACKGROUND_AMOUNT) {
								finalBackgrounds.add(new BackgroundDataHolder(currentBackground[INDEX_BACKGROUND_ID], currentBackground[INDEX_BACKGROUND_X], currentBackground[INDEX_BACKGROUND_Y],
										currentBackground[INDEX_BACKGROUND_X_OFFSET_PER_SCREEN_WRAP], currentBackground[INDEX_BACKGROUND_Y_OFFSET_PER_SCREEN_WRAP], currentBackground[INDEX_BACKGROUND_IS_TILED]));
								currentBackground = new int[INDECES_BACKGROUND_AMOUNT];
								currentIndex = 0;
							}
						}
					break;
					}
					currentValueString.setLength(0);
					if (currentChar == DELIMITER_NEXT_DATA_SET) {
						currentIndex = 0;
						switch(currentDataSet) {
							case dataSet.DIMENSIONS: {
								layerData = new int[dimensions[INDEX_DIMENSION_WIDTH]][dimensions[INDEX_DIMENSION_HEIGHT]][dimensions[INDEX_DIMENSION_LAYERS]];
								currentDataSet = dataSet.TILES;
							}
						break;
							case dataSet.TILES: {
								int tileIndex = 0;
								for (int layer = 0; layer < dimensions[INDEX_DIMENSION_LAYERS]; layer++) {
									for (int y = 0; y < dimensions[INDEX_DIMENSION_HEIGHT]; y++) {
										for (int x = 0; x < dimensions[INDEX_DIMENSION_WIDTH]; x++) {
											layerData[x][y][layer] = tileIDs.get(tileIndex);
											tileIndex++;
										}
									}
								}
								currentDataSet = dataSet.ENTITIES;
							}
						break;
							case dataSet.ENTITIES: {
								currentDataSet = dataSet.BACKGROUNDS;
							}
						break;
							case dataSet.BACKGROUNDS: ;
						break;
						}
					}
				}
			}
			return new Level(layerData, finalEntities, finalBackgrounds);
		}
		catch(Exception e) {
			e.printStackTrace();
			return new Level(1,1,1);
		}
	}

	// CONSTRUCTOR
	private LevelLoader() {
		
	}
}