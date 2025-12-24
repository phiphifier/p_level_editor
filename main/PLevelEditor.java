package main;

public class PLevelEditor {

	// INITIAL SETUP
	public static void main(String[] args) {
		// LOAD RESOURCES
		Resources.loadResources();
		
		// CREATE INPUT LISTENER
		Inputs inputs = new Inputs();
		
		// CREATE JPANEL AND PASS IT THE INPUT LISTENER SO IT CAN RECIEVE INPUTS
		Panel drawingPanel = new Panel(inputs);
		
		// CREATE JFRAME AND PASS IT THE DRAWING PANEL
		new Frame(drawingPanel);
		
		// CREATE GAME MANAGER,TO HANDLE GAME LOOP, PASS IT DRAWING PANEL SO IT CAN TELL IT WHICH LIST TO DRAW
		new GameManager(drawingPanel);
	}
}