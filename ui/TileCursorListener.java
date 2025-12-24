package ui;

public interface TileCursorListener {

	// LEFT CLICKED
	public void leftClicked(int x, int y);
	
	// RIGHT CLICKED
	public void rightClicked(int x, int y);

	// LEFT RELEASED
	public void leftReleased(int x, int y);
	
	// RIGHT RELEASED
	public void rightReleased(int x, int y);
}