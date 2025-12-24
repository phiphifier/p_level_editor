package config;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class Config {
	
	// VARS
	private static Properties p;
		// GENERAL CONSTANTS
	private static final String USER_HOME = System.getProperty("user.home");
	private static final String APP_FILE_NAME = ".plvleditor";
	private static final String ENTITY_DEFINITIONS_FILE_NAME = "entities.properties";
	private static final String BACKGROUND_DEFINITIONS_FILE_NAME = "backgrounds.properties";
	private static final String CONFIG_FILE_NAME = "config.properties";
	private static final Path APP_FILE_PATH = Path.of(USER_HOME, APP_FILE_NAME);
	private static final Path CONFIG_FILE_PATH = Path.of(APP_FILE_PATH.toString(), CONFIG_FILE_NAME);
	private static final Path ENTITY_DEFINITIONS_PATH = Path.of(APP_FILE_PATH.toString(), ENTITY_DEFINITIONS_FILE_NAME);
	private static final Path BACKGROUND_DEFINITIONS_PATH = Path.of(APP_FILE_PATH.toString(), BACKGROUND_DEFINITIONS_FILE_NAME);
		// PROPERTY NAMES
	public static final String PROPERTY_FIRST_PROMPT_SHOWN = "first_run_prompt_shown";
	public static final String PROPERTY_LEVEL_STORAGE_PATH = "path_level_storage";
		// DEFAULT PROPERTY VALUES
	private static final String PROPERTY_DEFAULT_FIRST_PROMPT_SHOWN = "false";
	private static final String PROPERTY_DEFAULT_LEVEL_STORAGE_PATH = "";
	
	// GET PROPERTY
	public static String getProperty(String key) {
		return p.getProperty(key);
	}
	
	// GET ENTITY DEFINITIONS PATH
	public static String getEntityDefinitionsPath() {
		return ENTITY_DEFINITIONS_PATH.toString();
	}
	
	// GET BACKGROUND DEFINITIONS PATH
	public static String getBackgroundDefinitionsPath() {
		return BACKGROUND_DEFINITIONS_PATH.toString();
	}
	
	// SET PROPERTY
	public static boolean setProperty(String key, String stringValue) {
		if (stringValue == null) return false;
		try(OutputStream os = new FileOutputStream(new File(CONFIG_FILE_PATH.toString()))) {
			if (!p.containsKey(key)) return false;
			else {
				if (key.equals(PROPERTY_FIRST_PROMPT_SHOWN)) {
					if (!stringValue.equals("true") && !stringValue.equals("false")) return false;
				}
				if (key.equals(PROPERTY_LEVEL_STORAGE_PATH)) {
					if (!validatePath(stringValue)) return false;
				}
			}
			p.setProperty(key, filterValue(stringValue));
			p.store(os, null);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// SET VALUE FILTER
	private static String filterValue(String string) {
		String finalString = string;
		if (string != "true" && string != "false") finalString = Path.of(USER_HOME.toString(), string).toString();
		return finalString;
	}
	
	// VALIDATE PATH
	public static boolean validatePath(String path) {
		path = Path.of(USER_HOME, path).toString();
		boolean validPath = false;
		try {
			boolean exists = Files.exists(Path.of(path));
			boolean isWritable = Files.isWritable(Path.of(path));
			if (exists && isWritable) validPath = true;
			return validPath;
		}
		catch(Exception e) {
			return validPath;
		}
	}
	
	// GET DEFAULT PROPERTIES
	private static Properties getDefaultProperties() {
		Properties p = new Properties();
		p.setProperty(PROPERTY_FIRST_PROMPT_SHOWN, PROPERTY_DEFAULT_FIRST_PROMPT_SHOWN);
		p.setProperty(PROPERTY_LEVEL_STORAGE_PATH, PROPERTY_DEFAULT_LEVEL_STORAGE_PATH);
		return p;
	}
	
	// LOAD CONFIG
	public static void loadConfig() {
		// CONFIG FILE
		try(InputStream is = new FileInputStream(CONFIG_FILE_PATH.toString())) {
			p = new Properties(getDefaultProperties());
			p.load(is);
		}
		catch (Exception e) {
			createNewConfigFile();
		}
	}
	
	// CREATE NEW CONFIG FILES
	private static void createNewConfigFile() {
		try {
			Files.createDirectories(Path.of(USER_HOME, APP_FILE_NAME));
			OutputStream os = new FileOutputStream(new File(CONFIG_FILE_PATH.toString()));
			p = new Properties(getDefaultProperties());
			p.putAll(getDefaultProperties());
			p.store(os, null);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// CONSTRUCTOR
	private Config() {}
}