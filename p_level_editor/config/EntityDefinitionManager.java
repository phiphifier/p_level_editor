package config;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

public class EntityDefinitionManager {

	// VARS
	private static Properties p;
	private static ArrayList<EntityDefinition> entityDefinitions = new ArrayList<EntityDefinition>();
	
	// GET ENTITY DEFINITION
	public static EntityDefinition getEntityDefinition(int index) {
		return entityDefinitions.get(index);
	}
	
	// ADD NEW ENTITY DEFINITION
	public static boolean addNewEntityDefinition(String name, int id) {
		for (int i = 0; i < entityDefinitions.size(); i++) {
			EntityDefinition ed = entityDefinitions.get(i);
			if (id == ed.getID()) return false;
		}
		try(OutputStream os = new FileOutputStream(new File(Config.getEntityDefinitionsPath()))) {
			p.setProperty(name, String.valueOf(id));
			p.store(os, null);
			loadEntityDefinitions();
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	// REMOVE ENTITY DEFINITION
	public static void removeEntityDefinition(String value) {
		try(OutputStream os = new FileOutputStream(new File(Config.getEntityDefinitionsPath()))) {
			p.remove(value);
			p.store(os, null);
			loadEntityDefinitions();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// GET ENTITY DEFINITIONS SIZE
	public static int getEntityDefinitionsSize() {
		return entityDefinitions.size();
	}
	
	// LOAD ENTITY DEFINITIONS
	public static void loadEntityDefinitions() {
		entityDefinitions = new ArrayList<EntityDefinition>();
		try(InputStream is = new FileInputStream(new File(Config.getEntityDefinitionsPath()))) {
			p = new Properties();
			p.load(is);
			processEntityDefinitions();
		}
		catch(Exception e) {
			createNewEntityDefinitionsFile();
		}
	}
	
	// CREATE NEW BLANK ENTITY DEFINITIONS
	private static void createNewEntityDefinitionsFile() {
		try(OutputStream os = new FileOutputStream(new File(Config.getEntityDefinitionsPath()))) {
			loadEntityDefinitions();
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// PARSE ENTITY DEFINITIONS
	private static void processEntityDefinitions() {
		Set<String> entNames = p.stringPropertyNames();
		for (String name : entNames) {
			String idString = p.getProperty(name);
			entityDefinitions.add(new EntityDefinition(name, Integer.valueOf(idString)));
		}
		sortEntityDefinitionsByID();
	}
	
	// SORT ENTITY DEFINITIONS BY ID
	private static void sortEntityDefinitionsByID() {
		int entityDefinitionsToBeSorted =  entityDefinitions.size();
		ArrayList<EntityDefinition> sortedEntityDefinitions = new ArrayList<EntityDefinition>();
		int entityDefinitionsSorted = 0;
		EntityDefinition lowestEntDefID = null;
		while (entityDefinitionsSorted < entityDefinitionsToBeSorted) {
			for (int i = 0; i < entityDefinitions.size(); i++) {
				EntityDefinition ed = entityDefinitions.get(i);
				if (lowestEntDefID == null || ed.getID() < lowestEntDefID.getID()) lowestEntDefID = ed;
			}
			sortedEntityDefinitions.add(lowestEntDefID);
			entityDefinitions.remove(lowestEntDefID);
			lowestEntDefID = null;
			entityDefinitionsSorted++;
		}
		entityDefinitions = sortedEntityDefinitions;
	}
	
	// CONSTRUCTOR
	private EntityDefinitionManager() {}
}