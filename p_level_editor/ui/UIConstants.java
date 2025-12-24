package ui;

import main.Panel;
import main.Resources;
import java.awt.Color;
import java.awt.Font;

public final class UIConstants {
	
	// GENERAL CONSTANTS
	public static final int SCREEN_WIDTH = Panel.SCREEN_SIZE.width;
	public static final int SCREEN_HEIGHT = Panel.SCREEN_SIZE.height;
	public static final Font DEFAULT_FONT = Resources.getGlobalFont();
	public static final Color UI_COLOR_TEXT = new Color(255, 255, 255);
	public static final Color UI_COLOR_SHADOW = new Color(45, 45, 75);
	public static final int UI_BOX_SHADOW_DEPTH = 4;
	public static final int UI_TEXT_SHADOW_DEPTH = 2;
	public static final float ALPHA_ACTIVE = 1f;
	public static final float ALPHA_DISABLED = 0.3f;
	public static final float ALPHA_DECAY_RATE = 0.15f;
	public static final int SPLASH_FONT_SIZE = 21;
	
	// MENUS
	public static final Color MENU_COLOR_BODY = new Color(70, 70, 160);
	public static final Color MENU_COLOR_BORDER = new Color(130, 130, 240);
	public static final int MENU_BORDER_THICKNESS = 3;
	public static final int MENU_FONT_TITLE_SIZE = 26;
	public static final int MENU_FONT_BODY_SIZE = 18;
	
	// BUTTONS
	public static final Color BUTTON_COLOR_NORMAL = new Color(110, 110, 200);
	public static final Color BUTTON_COLOR_HIGHLIGHTED = new Color(150, 150, 235);
	public static final Color BUTTON_COLOR_PRESSED = new Color(190, 190, 255);
	public static final int BUTTON_FONT_SIZE_LARGE = 31;
	public static final int BUTTON_FONT_SIZE_SMALL = 20;
	public static final int BUTTON_FONT_SIZE_TIP = 20;
	public static final Color BUTTON_COLOR_HOVER_TIP_BACKGROUND = new Color(0, 0, 0);
	public static final int BUTTON_TIP_XOFFSET = 9;
	public static final int BUTTON_TIP_BACKGROUND_HEIGHT = 22;
	
	// CONSTRUCTOR
	private UIConstants() {
		
	}
}