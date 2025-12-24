package ui;

public interface ButtonListener {

	// BUTTON MOUSED
	public void buttonMoused(int buttonID);
	
	// BUTTON UNMOUSED
	public void buttonUnMoused(int buttonID);
	
	// BUTTON LEFT PRESSED
	public void buttonLeftPressed(int buttonID);
	
	// BUTTON LEFT RELEASED
	public void buttonLeftReleased(int buttonID);
	
	// BUTTON RIGHT PRESSED
	public void buttonRightPressed(int buttonID);
	
	// BUTTON LEFT RELEASED
	public void buttonRightReleased(int buttonID);
}