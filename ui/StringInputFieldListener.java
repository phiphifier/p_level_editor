package ui;

public interface StringInputFieldListener {

	// CONSTANTS
	public static final int ERROR_EMPTY = 0;
	public static final int ERROR_LETTER = 1;
	public static final int ERROR_FULL = 2;
	public static final int ERROR_FILE_PATH_CHAR = 3;
	
	// ATTEMPTED INVALID INPUT
	public void attemptInvalidInput(int errorID);
	
	// STRING FINISHED
	public void stringFinished(String finalString);
}