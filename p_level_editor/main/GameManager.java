package main;


import config.BackgroundDefinitionManager;
import config.Config;
import config.EntityDefinitionManager;
import ui.MenuMain;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameManager implements Runnable {

	// VARS
		// DRAWING STUFF
	private static Panel drawingPanel;
	private static BufferedImage globalBackground = Resources.getBackground(Resources.ID_BACKGROUND_SUNSET_SKY);
	private static BufferedImage backBuffer = new BufferedImage(Panel.SCREEN_SIZE.width, Panel.SCREEN_SIZE.height, BufferedImage.TYPE_4BYTE_ABGR);
	private static BufferedImage frontBuffer = new BufferedImage(Panel.SCREEN_SIZE.width, Panel.SCREEN_SIZE.height, BufferedImage.TYPE_4BYTE_ABGR);
		// LOOP SPEED VARS
	public static final int UPDATES_PER_SECOND = 60;
	private static final double NANO_SECONDS_PER_SECOND = 1000000000;
	private static final double NANOS_PER_UPDATE = NANO_SECONDS_PER_SECOND/UPDATES_PER_SECOND;
	private static final double NANO_SECONDS_PER_MILLISECOND = 0.000001;
	private static long lastUpdateCheckTime = System.nanoTime();
	private static double updateThreshold = 0;
		// UPDATABLE & DRAWABLE LISTS
	private static ArrayList<Updatable> updatables = new ArrayList<Updatable>();
	private static CopyOnWriteArrayList<Drawable> drawables = new CopyOnWriteArrayList<Drawable>();
	private static Drawable overlayDrawable;
	private static ArrayList<Updatable> updatableAdditionQueue = new ArrayList<Updatable>();
	private static ArrayList<Updatable> updatableRemovalQueue = new ArrayList<Updatable>();
	private static ArrayList<Drawable> drawableAdditionQueue = new ArrayList<Drawable>();
	private static ArrayList<Drawable> backgroundDrawableAdditionQueue = new ArrayList<Drawable>();
	private static ArrayList<Drawable> drawableRemovalQueue = new ArrayList<Drawable>();
	
	// CONSTRUCTOR
	public GameManager(Panel panel) {
		drawingPanel = panel;
		startGame();
		Thread mainLoop = new Thread(this);
		mainLoop.setName("Game-Thread");
		mainLoop.start();
	}

	// START GAME
	private static void startGame() {
		Config.loadConfig();
		EntityDefinitionManager.loadEntityDefinitions();
		BackgroundDefinitionManager.loadBackgroundDefinitions();
		new MenuMain(null);
	}
	
	// CREATE BUFFER IMAGE
	private static void redrawScreenBuffer() {
		Graphics2D g2 = (Graphics2D)backBuffer.getGraphics();
		// DRAW GLOBAL BACKGROUND
		g2.drawImage(globalBackground, 0, 0, Panel.SCREEN_SIZE.width, Panel.SCREEN_SIZE.height, null);
		// DRAW DRAWABLES
		for(int i = 0; i < drawables.size(); i++) {
			Drawable d = drawables.get(i);
			Graphics2D dg2 = (Graphics2D)g2.create();
			d.draw(dg2);
			dg2.dispose();
		}
		if (overlayDrawable != null) overlayDrawable.draw(g2);
		g2.dispose();
		BufferedImage temp = frontBuffer;
	    frontBuffer = backBuffer;
	    backBuffer = temp;
	    drawingPanel.setScreenBuffer(frontBuffer);
	}
	
	// GAME LOOP
	@Override
	public void run() {
		while(true) {
			long loopStartTimeNanos = System.nanoTime();
			// UPDATES
			updateThreshold += (loopStartTimeNanos-lastUpdateCheckTime)/NANOS_PER_UPDATE;
			lastUpdateCheckTime = loopStartTimeNanos;
			if (updateThreshold >= 1) {
				updateLists();
				updateUpdatables();
				redrawScreenBuffer();
				drawingPanel.repaint();
				updateThreshold--;
			}
			//THREAD SLEEP STUFF
			long logicEndTime = System.nanoTime();
			long timeToExecuteLoopInNanos = logicEndTime-loopStartTimeNanos;
			try {
				long timeLeftInMillis = (long)((timeToExecuteLoopInNanos+NANOS_PER_UPDATE-updateThreshold*NANOS_PER_UPDATE)*NANO_SECONDS_PER_MILLISECOND);
				if (timeLeftInMillis > 0) Thread.sleep(timeLeftInMillis);
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
	// RESET UPDATE THRESHOLD
	public static void resetUpdateThreshold() {
		updateThreshold = 0;
		lastUpdateCheckTime = System.nanoTime();
	}
	
	// FORCE DRAW
	public static void forceDraw() {
		redrawScreenBuffer();
		drawingPanel.repaint();
	}
	
	// UPDATE UPDATABLES
	private static void updateUpdatables() {
		for(int i = 0; i < updatables.size(); i++) {
			Updatable u = updatables.get(i);
			u.update();
		}
	}
	
	// UPDATE LISTS
	public static void updateLists() {
		updatables.removeAll(updatableRemovalQueue);
		updatableRemovalQueue.clear();
		updatables.addAll(updatableAdditionQueue);
		updatableAdditionQueue.clear();
		drawables.removeAll(drawableRemovalQueue);
		drawableRemovalQueue.clear();
		drawables.addAll(0, backgroundDrawableAdditionQueue);
		backgroundDrawableAdditionQueue.clear();
		drawables.addAll(drawableAdditionQueue);
		drawableAdditionQueue.clear();
	}
	
	// SET OVERLAY DRAWABLE
	public static void setOverlayDrawable(Drawable d) {
		overlayDrawable = d;
	}
	
	// ADD DRAWABLE IN BACKGROUND
	public static void addDrawableToBackground(Drawable d) {
		backgroundDrawableAdditionQueue.add(d);
	}
	
	// ADD UPDATABLE
	public static void addUpdatable(Updatable u) {
		updatableAdditionQueue.add(u);
	}
	
	// ADD DRAWABLE
	public static void addDrawable(Drawable d) {
		drawableAdditionQueue.add(d);
	}
	
	// REMOVE UPDATABLE
	public static void removeUpdatable(Updatable u) {
		updatableRemovalQueue.add(u);
	}
	
	// REMOVE DRAWABLE
	public static void removeDrawable(Drawable d) {
		drawableRemovalQueue.add(d);
	}
}