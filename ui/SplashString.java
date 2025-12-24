package ui;

import main.Updatable;
import main.Drawable;
import main.GameManager;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class SplashString implements Updatable, Drawable {

	// VARS
	private int x, y;
	private Color splashColor;
	private Color splashShadowColor;
	private Color backgroundColor;
	private String splashString;
	private Font splashFont;
	private HorizontalAlignment horizontalAlignment;
	private int lifeFramesRemaining;
	private float alpha = 1;
	private boolean drawBackground = false;
	private static final float ALPHA_DECAY_RATE = 0.1f;
	private static final int SHADOW_COLOR_DARKENING_AMOUNT = 220; // 0 - 255
	
	// CONSTRUCTOR
	public SplashString(int x, int y, Color color, String splashString, float lifeInSeconds, HorizontalAlignment horizontalAlignment, boolean  drawBackground) {
		this.x = x;
		this.y = y;
		splashColor = color;
		splashShadowColor = calculateShadowColor(splashColor);
		backgroundColor = UIConstants.UI_COLOR_SHADOW;
		this.splashString = splashString;
		splashFont = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.SPLASH_FONT_SIZE);
		this.horizontalAlignment = horizontalAlignment;
		lifeFramesRemaining = convertSecondsToFrames(lifeInSeconds);
		this.drawBackground = drawBackground;
		GameManager.addUpdatable(this);
		GameManager.addDrawable(this);
	}
	
	// CALCULATE SHADOW COLOR
	private Color calculateShadowColor(Color baseColor) {
		int red = Math.max(0, baseColor.getRed()-SHADOW_COLOR_DARKENING_AMOUNT);
		int green = Math.max(0, baseColor.getGreen()-SHADOW_COLOR_DARKENING_AMOUNT);
		int blue = Math.max(0, baseColor.getBlue()-SHADOW_COLOR_DARKENING_AMOUNT);
		return new Color(red, green, blue);
	}
	
	// CONVERT SECONDS TO FRAMES
	private int convertSecondsToFrames(float lifeInSeconds) {
		int lifeInFrames;
		lifeInFrames = Math.round(GameManager.UPDATES_PER_SECOND*lifeInSeconds);
		return lifeInFrames;
	}

	// UPDATE
	@Override
	public void update() {
		if (lifeFramesRemaining > 0) lifeFramesRemaining--;
		if (alpha > 0 && lifeFramesRemaining == 0) alpha = Math.max(0, alpha-ALPHA_DECAY_RATE);
		if (alpha  == 0) remove();
	}

	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		g2.setFont(splashFont);
		FontMetrics fm = g2.getFontMetrics();
		int stringWidth = fm.stringWidth(splashString);
		int yCenterToBaselineOffset = (fm.getAscent()+fm.getDescent())/2;
		int backgroundHeight = fm.getHeight();
		int textDrawY = y+yCenterToBaselineOffset/2;
		int backgroundDrawY = textDrawY-yCenterToBaselineOffset/2-backgroundHeight/2;
		int drawX;
		switch(horizontalAlignment) {
		case HorizontalAlignment.LEFT: drawX = x-stringWidth;
		break;
		case HorizontalAlignment.MIDDLE: drawX = x-stringWidth/2;
		break;
		case HorizontalAlignment.RIGHT: drawX = x;
		break;
		default: drawX = x;
		}
		if (drawBackground) {
			g2.setColor(backgroundColor);
			g2.fillRect(drawX-1, backgroundDrawY, stringWidth+2, backgroundHeight);
		}
		g2.setColor(splashColor);
		UIUtils.drawStringWithShadow(g2, splashShadowColor, splashString, drawX, textDrawY);
	}
	
	// REMOVE
	public void remove() {
		GameManager.removeUpdatable(this);
		GameManager.removeDrawable(this);
	}
	
	// SET ALPHA
	public void setAlpha(float newAlpha) {
		alpha = newAlpha;
	}
}