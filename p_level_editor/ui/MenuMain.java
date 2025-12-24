package ui;

import main.Resources;
import config.Config;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MenuMain extends Menu {

	// VARS
		// BUTTON PROPERTIES
	private static final Font INFO_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.BUTTON_FONT_SIZE_SMALL);
	private static final int BUTTON_ID_NEW = 0;
	private static final int BUTTON_ID_LOAD = 1;
	private static final int BUTTON_ID_SETTINGS = 2;
	private static final int BUTTON_ID_EXIT = 3;
	private static final int BUTTON_ID_ABOUT = 4;
	private static final int BUTTONS_STANDARD_WIDTH = 224;
	private static final int BUTTONS_SMALL_WIDTH = BUTTONS_STANDARD_WIDTH/2-DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTONS_HEIGHT = 48;
	private static final int BUTTONS_STANDARD_X = UIConstants.SCREEN_WIDTH/2-BUTTONS_STANDARD_WIDTH/2;
	private static final int BUTTON_EXIT_X = UIConstants.SCREEN_WIDTH/2+DEFAULT_BOX_HORIZONTAL_PADDING;
	private static final int BUTTON_NEW_LEVEL_Y = UIConstants.SCREEN_HEIGHT-(BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING)*4;
	private static final int BUTTON_LOAD_LEVEL_Y = BUTTON_NEW_LEVEL_Y+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_SETTINGS_Y = BUTTON_LOAD_LEVEL_Y+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_ABOUT_Y = BUTTON_SETTINGS_Y+BUTTONS_HEIGHT+DEFAULT_BOX_VERTICAL_PADDING;
	private static final int BUTTON_EXIT_Y = BUTTON_ABOUT_Y;
		// MAIN MENU TITLE IMAGE VATS
	private static final double ANGLE_UPDATE_RATE_IN_DEGREES = 0.0305;
	private static final double ANGLE_DIRECTION_THRESHOLD_IN_DEGREES = 3.15;
	private static final double SCALE_UPDATE_RATE = 0.0009;
	private static final double SCALE_UPPER_THRESHOLD = 1.1;
	private static final double SCALE_LOWER_THRESHOLD = 0.95;
	private BufferedImage titleImage;
	private static final int TITLE_VERTICAL_PADDING = 14;
	private static final int TITLE_ICON_IMAGE_PADDING = 4;
	private static final String TITLE_IMAGE_TEXT = "PLevel Editor";
	private static final int TITLE_FONT_SIZE = 88;
	private static final Font TITLE_IMAGE_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.PLAIN, TITLE_FONT_SIZE);
	private double titleImageScale = 1;
	private double titleAngleInDegrees = ANGLE_DIRECTION_THRESHOLD_IN_DEGREES/2;
	private boolean isRotatingRight = 1==(int)(Math.random()*2);
	private boolean isScaleGrowing = 1==(int)(Math.random()*2);
	
	// CONSTRUCTOR
	public MenuMain(Menu owner) {
		super(owner);
		// CREATE BUTTONS
		addButtonWithMouseHoverTip(BUTTON_ID_NEW, BUTTONS_STANDARD_X, BUTTON_NEW_LEVEL_Y, BUTTONS_STANDARD_WIDTH, BUTTONS_HEIGHT, "New Level", true, "Create an empty level with specified dimensions", HorizontalAlignment.RIGHT);
		addButtonWithMouseHoverTip(BUTTON_ID_LOAD, BUTTONS_STANDARD_X, BUTTON_LOAD_LEVEL_Y, BUTTONS_STANDARD_WIDTH, BUTTONS_HEIGHT, "Browse Levels", true, "Load or delete an existing level", HorizontalAlignment.RIGHT);
		addButtonWithMouseHoverTip(BUTTON_ID_SETTINGS, BUTTONS_STANDARD_X, BUTTON_SETTINGS_Y, BUTTONS_STANDARD_WIDTH, BUTTONS_HEIGHT, "Settings", true, "Change the settings", HorizontalAlignment.RIGHT);
		addButtonWithMouseHoverTip(BUTTON_ID_EXIT, BUTTON_EXIT_X, BUTTON_EXIT_Y, BUTTONS_SMALL_WIDTH, BUTTONS_HEIGHT, "Exit", true, "Guess, lol", HorizontalAlignment.RIGHT);
		addButtonWithMouseHoverTip(BUTTON_ID_ABOUT, BUTTONS_STANDARD_X, BUTTON_ABOUT_Y, BUTTONS_SMALL_WIDTH, BUTTONS_HEIGHT, "About", true, "About this program", HorizontalAlignment.RIGHT);
		// CREATE TITLE IMAGE
		titleImage = createTitleImage();
		// FIRST RUN PROMPT
		if (!Boolean.valueOf(Config.getProperty(Config.PROPERTY_FIRST_PROMPT_SHOWN))) addSubMenu(new MenuChangeLevelPath(this, false), true);
	}

	// CREATE TITLE IMAGE
	private BufferedImage createTitleImage() {
		BufferedImage iconImage = Resources.getIcon();
		BufferedImage finalImage = new BufferedImage(UIConstants.SCREEN_WIDTH, UIConstants.SCREEN_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = (Graphics2D)finalImage.getGraphics();
		g2.setFont(TITLE_IMAGE_FONT);
		FontMetrics fm = g2.getFontMetrics();
		int titleTextWidth = fm.stringWidth(TITLE_IMAGE_TEXT);
		int titleHeight = fm.getHeight();
		UIUtils.drawStringWithShadow(g2, shadowColor, TITLE_IMAGE_TEXT, UIConstants.SCREEN_WIDTH/2-titleTextWidth/2-titleHeight/2, TITLE_VERTICAL_PADDING+titleHeight);
		g2.drawImage(iconImage, UIConstants.SCREEN_WIDTH/2+titleTextWidth/2-titleHeight/2+TITLE_ICON_IMAGE_PADDING, TITLE_VERTICAL_PADDING+fm.getDescent()/2, titleHeight, titleHeight, null);
		return finalImage;
	}
	
	// GROW/SHRINK SCALE
	private void updateTitleImageAnimation() {
		// PULSATE(titleScale)
		if (isScaleGrowing) {
			titleImageScale = Math.min(titleImageScale+SCALE_UPDATE_RATE, SCALE_UPPER_THRESHOLD);
		}
		else {
			titleImageScale = Math.max(titleImageScale-SCALE_UPDATE_RATE, SCALE_LOWER_THRESHOLD);
		}
		if (titleImageScale == SCALE_UPPER_THRESHOLD || titleImageScale == SCALE_LOWER_THRESHOLD) isScaleGrowing = !isScaleGrowing;
		// ANGLE(titleAngle)
		if (isRotatingRight) {
			titleAngleInDegrees = Math.min(titleAngleInDegrees+ANGLE_UPDATE_RATE_IN_DEGREES, ANGLE_DIRECTION_THRESHOLD_IN_DEGREES);
		}
		else {
			titleAngleInDegrees = Math.max(titleAngleInDegrees-ANGLE_UPDATE_RATE_IN_DEGREES, -ANGLE_DIRECTION_THRESHOLD_IN_DEGREES);
		}
		if (titleAngleInDegrees == ANGLE_DIRECTION_THRESHOLD_IN_DEGREES || titleAngleInDegrees == -ANGLE_DIRECTION_THRESHOLD_IN_DEGREES) isRotatingRight = !isRotatingRight;
	}
	
	// UPDATE
	@Override
	public void update() {
		super.update();
		updateTitleImageAnimation();
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		Graphics2D g2t = (Graphics2D)g2.create();
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2.scale(titleImageScale, titleImageScale);
		g2.rotate(Math.toRadians(titleAngleInDegrees), UIConstants.SCREEN_WIDTH/2, TITLE_VERTICAL_PADDING);
		g2.drawImage(titleImage, (int)(UIConstants.SCREEN_WIDTH-(UIConstants.SCREEN_WIDTH*titleImageScale))/2, (int)(UIConstants.SCREEN_HEIGHT-(UIConstants.SCREEN_HEIGHT*titleImageScale))/4, UIConstants.SCREEN_WIDTH, UIConstants.SCREEN_HEIGHT, null);
		g2t.setFont(INFO_FONT);
		g2t.setColor(Color.WHITE);
		String levelPathDisplayPrefix = "Level storage path:\n";
		String levelPathDisplayString = Config.getProperty(Config.PROPERTY_LEVEL_STORAGE_PATH);
		FontMetrics fm = g2t.getFontMetrics();
		int textWrapLength = BUTTONS_STANDARD_X-DEFAULT_BOX_HORIZONTAL_PADDING*4;
		int textHeight = fm.getHeight();
		int stringLinesRequired = 2+fm.stringWidth(levelPathDisplayString)/textWrapLength;
		int textDrawY = UIConstants.SCREEN_HEIGHT-textHeight*stringLinesRequired;
		UIUtils.drawWrappedString(g2t, Color.BLACK, levelPathDisplayPrefix+levelPathDisplayString, textWrapLength, DEFAULT_BOX_HORIZONTAL_PADDING, textDrawY);
	}

	// BUTTON RELEASED
	@Override
	public void buttonLeftReleased(int buttonID) {
		switch(buttonID) {
		case BUTTON_ID_NEW: addSubMenu(new MenuNewLevel(this), true);
		break;
		case BUTTON_ID_LOAD: ; addSubMenu(new MenuLevelSelect(this, null), true);
		break;
		case BUTTON_ID_SETTINGS: addSubMenu(new MenuSettings(this), true);
		break;
		case BUTTON_ID_ABOUT: addSubMenu(new MenuAbout(this), true);
		break;
		case BUTTON_ID_EXIT: System.exit(0);
		}
	}

	// UNUSED
	@Override
	public void buttonMoused(int buttonID) {}
	@Override
	public void buttonUnMoused(int buttonID) {}
	@Override
	public void buttonLeftPressed(int buttonID) {}
	@Override
	public void buttonRightPressed(int buttonID) {}
	@Override
	public void buttonRightReleased(int buttonID) {}
}