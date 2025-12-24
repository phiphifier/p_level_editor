package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import main.Updatable;
import main.Drawable;
import main.GameManager;

public class LoadingOverlay implements Updatable, Drawable {

	// VARS
		// CONSTANTS
	private static final float LIFE_IN_SECONDS = 0.35f;
	private static final Color OVERLAY_COLOR = Color.BLACK;
	private static final Color TEXT_COLOR = UIConstants.UI_COLOR_TEXT;
	private static final Font DISPLAY_FONT = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.MENU_FONT_TITLE_SIZE);
		// INSTANCE FIELDS
	private String text;
	private int lifeInFrames = (int)(GameManager.UPDATES_PER_SECOND*LIFE_IN_SECONDS);
	
	// CONSTRUCTOR
	public LoadingOverlay(String displayText) {
		text = displayText;
		GameManager.addUpdatable(this);
		GameManager.setOverlayDrawable(this);
		GameManager.forceDraw();
	}

	// UPDATE
	@Override
	public void update() {
		lifeInFrames--;
		if (lifeInFrames <= 0) {
			GameManager.removeUpdatable(this);
			GameManager.setOverlayDrawable(null);
			GameManager.resetUpdateThreshold();
		}
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		g2.setColor(OVERLAY_COLOR);
		g2.fillRect(0, 0, UIConstants.SCREEN_WIDTH, UIConstants.SCREEN_HEIGHT);
		g2.setFont(DISPLAY_FONT);
		g2.setColor(TEXT_COLOR);
		FontMetrics fm = g2.getFontMetrics();
		int stringWidth = fm.stringWidth(text);
		int stringCenterToBaslineOffset = (fm.getAscent()-fm.getDescent())/2;
		UIUtils.drawStringWithShadow(g2, UIConstants.UI_COLOR_SHADOW, text, UIConstants.SCREEN_WIDTH/2-stringWidth/2, UIConstants.SCREEN_HEIGHT/2+stringCenterToBaslineOffset);
	}
}