package config;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

public class BackgroundDefinitionManager {

	// VARS
	private static Properties p;
	private static ArrayList<BackgroundDefinition> backgroundDefinitions = new ArrayList<BackgroundDefinition>();
	
	// GET BACKGROUND DEFINITION
	public static BackgroundDefinition getBackgroundDefinitionByID(int id) {
		BackgroundDefinition finalBD = null;
		for (int i = 0; i < backgroundDefinitions.size(); i++) {
			BackgroundDefinition bd = backgroundDefinitions.get(i);
			if (bd.getID() == id) finalBD = bd;
		}
		return finalBD;
	}
	
	// GET BACKGROUND DEFINITION AT
	public static BackgroundDefinition getBackgroundDefinitionByIndex(int index) {
		return backgroundDefinitions.get(index);
	}
	
	// ADD NEW BACKGROUND DEFINITION
	public static boolean addNewBackgroundDefinition(String name, int id) {
		for (int i = 0; i < backgroundDefinitions.size(); i++) {
			BackgroundDefinition ed = backgroundDefinitions.get(i);
			if (id == ed.getID()) return false;
		}
		try(OutputStream os = new FileOutputStream(new File(Config.getBackgroundDefinitionsPath()))) {
			p.setProperty(name, String.valueOf(id));
			p.store(os, null);
			loadBackgroundDefinitions();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// REMOVE BACKGROUND DEFINITION
	public static void removeBackgroundDefinition(String value) {
		try(OutputStream os = new FileOutputStream(new File(Config.getBackgroundDefinitionsPath()))) {
			p.remove(value);
			p.store(os, null);
			loadBackgroundDefinitions();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// GET BACKGROUND DEFINITIONS SIZE
	public static int getBackgroundDefinitionsSize() {
		return backgroundDefinitions.size();
	}
	
	// LOAD BACKGROUND DEFINITIONS
	public static void loadBackgroundDefinitions() {
		backgroundDefinitions = new ArrayList<BackgroundDefinition>();
		try(InputStream is = new FileInputStream(new File(Config.getBackgroundDefinitionsPath()))) {
			p = new Properties();
			p.load(is);
			processBackgroundDefinitions();
		}
		catch(Exception e) {
			createNewBackgroundDefinitionsFile();
		}
	}
	
	// CREATE NEW BLANK BACKGROUND DEFINITIONS
	private static void createNewBackgroundDefinitionsFile() {
		try(OutputStream os = new FileOutputStream(new File(Config.getBackgroundDefinitionsPath()))) {
			loadBackgroundDefinitions();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// PARSE BACKGROUND DEFINITIONS
	private static void processBackgroundDefinitions() {
		Set<String> entNames = p.stringPropertyNames();
		for (String name : entNames) {
			String idString = p.getProperty(name);
			backgroundDefinitions.add(new BackgroundDefinition(name, Integer.valueOf(idString)));
		}
		sortBackgroundDefinitionsByID();
	}
	
	// SORT BACKGROUND DEFINITIONS BY ID
	private static void sortBackgroundDefinitionsByID() {
		int backgroundDefinitionsToBeSorted =  backgroundDefinitions.size();
		ArrayList<BackgroundDefinition> sortedBackgroundDefinitions = new ArrayList<BackgroundDefinition>();
		int backgroundDefinitionsSorted = 0;
		BackgroundDefinition lowestEntDefID = null;
		while (backgroundDefinitionsSorted < backgroundDefinitionsToBeSorted) {
			for (int i = 0; i < backgroundDefinitions.size(); i++) {
				BackgroundDefinition ed = backgroundDefinitions.get(i);
				if (lowestEntDefID == null || ed.getID() < lowestEntDefID.getID()) lowestEntDefID = ed;
			}
			sortedBackgroundDefinitions.add(lowestEntDefID);
			backgroundDefinitions.remove(lowestEntDefID);
			lowestEntDefID = null;
			backgroundDefinitionsSorted++;
		}
		backgroundDefinitions = sortedBackgroundDefinitions;
	}
	
	// CONSTRUCTOR
	private BackgroundDefinitionManager() {}
}