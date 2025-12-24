package level;

import java.io.File;
import java.nio.file.Path;
import config.Config;

public class LevelDeleter {

	// REMOVE
	public static void remove(String name) {
		if (name.length() > 0) {
			String path = Path.of(Config.getProperty(Config.PROPERTY_LEVEL_STORAGE_PATH), name).toString();
			File level = new File(path);
			level.delete();
		}
	}
	
	// CONSTRUCTOR
	private LevelDeleter() {
		
	}
}