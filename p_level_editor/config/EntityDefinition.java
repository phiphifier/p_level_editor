package config;

public class EntityDefinition {

	// VARS
		// INSTANCE VARS
	private String stringName;
	private int id;
	
	// CONSTRUCTOR
	public EntityDefinition(String stringName, int id) {
		this.stringName = stringName;
		this.id = id;
	}
	
	// GET STRING NAME
	public String getStringName() {
		return stringName;
	}
	
	// GET STRING NAME WITH ID
	public String getStringNameWithID() {
		return stringName+" ID: " + id;
	}
	
	// GET ID
	public int getID() {
		return id;
	}
}