package ui;

import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Color;
import java.util.ArrayList;

public final class UIUtils {
	
	// CONSTANTS
	private static final String STRING_TOO_LONG_LEADING = "...";
	private static final char CHAR_NEW_LINE = '\n';
	private static final char CHAR_INPUT_CURSOR = '|';
	
	// GET UPDATED ALPHA
	public static float getUpdatedAlpha(float currentAlpha, boolean isEnabled) {
		float updatedAlpha;
		if (isEnabled) {
			updatedAlpha = Math.min(currentAlpha+UIConstants.ALPHA_DECAY_RATE, UIConstants.ALPHA_ACTIVE);
		}
		else {
			updatedAlpha = Math.max(currentAlpha-UIConstants.ALPHA_DECAY_RATE, UIConstants.ALPHA_DISABLED);
		}
		return updatedAlpha;
	}
	
	// DRAW STRING WITH SHADOW
	public static void drawStringWithShadow(Graphics2D g2, Color shadowColor, String stringToDraw, int x, int y) {
		Color textColor = g2.getColor();
		if (shadowColor != null) g2.setColor(shadowColor);
		g2.drawString(stringToDraw, x, y+UIConstants.UI_TEXT_SHADOW_DEPTH);
		g2.setColor(textColor);
		g2.drawString(stringToDraw, x, y);
	}
	
	// DRAW LIMITED WIDTH STRING WITH SHADOW
	public static void drawLimitedWidthStringWithShadow(Graphics2D g2, Color shadowColor, String stringToDraw, int x, int y, int maxLengthInPixels, boolean appendInputCursor) {
		Color textColor = g2.getColor();
		FontMetrics fm = g2.getFontMetrics();
		if (fm.stringWidth(stringToDraw) > maxLengthInPixels-g2.getFont().getSize()/4 && maxLengthInPixels >= 0) {
			StringBuilder croppedString = new StringBuilder();
			for (int i = stringToDraw.length()-1; fm.stringWidth(croppedString.toString()) <= maxLengthInPixels-g2.getFont().getSize()/4; i--) {
				croppedString.append(stringToDraw.charAt(i));
			}
			croppedString.setLength(Math.max(croppedString.length()-STRING_TOO_LONG_LEADING.length(), 0));
			croppedString.reverse();
			stringToDraw = STRING_TOO_LONG_LEADING+croppedString.toString();
		}
		if (appendInputCursor && System.currentTimeMillis()%1000 > 500) stringToDraw = stringToDraw+CHAR_INPUT_CURSOR;
		if (shadowColor != null) g2.setColor(shadowColor);
		g2.drawString(stringToDraw, x, y+UIConstants.UI_TEXT_SHADOW_DEPTH);
		g2.setColor(textColor);
		g2.drawString(stringToDraw, x, y);
	}

	// DRAW WRAPPED STRING WITH SHADOW - I hate string manipulation omg
	public static void drawWrappedString(Graphics2D g2, Color shadowColor, String stringToDraw, int nextLineThresholdInPixels, int x, int y) {
		FontMetrics fm = g2.getFontMetrics();
		int textHeight = fm.getHeight();
		int textCenterToBaselineOffset = (fm.getAscent()+fm.getDescent())/2;
		ArrayList<String> lines = new ArrayList<String>();
		StringBuilder currentLine = new StringBuilder();
		int index = 0;
		int lastSpaceIndex = 0;
		boolean finished = false;
		// PROCESSING LOOP
		while (!finished) {
			if (index < stringToDraw.length()) {
				char currentChar = stringToDraw.charAt(index);
				if (currentChar == ' ') lastSpaceIndex = index;
				currentLine.append(currentChar);
				String currentLineString = currentLine.toString();
				int currentStringWidth = fm.stringWidth(currentLineString);
				// NEW LINE SPLIT IF LENGTH IS AT OR ABOVE THRESHOLD, OR A NEW LINE CHAR IS ENCOUNTERED
				if (currentStringWidth >= nextLineThresholdInPixels || currentChar == CHAR_NEW_LINE) {
					// SPLIT HAPPENS ON A SPACE, ON A LONG LINE WITH NO SPACES, OR A NEW LINE CHAR IS FOUND
					if (currentChar == ' ' || currentLine.toString().indexOf(' ') == -1 || currentChar == CHAR_NEW_LINE) {
						lines.add(currentLineString);
						currentLine.setLength(0);
					}
					// SPLIT HAPPENS IN THE MIDDLE OF A WORD, BACKTRACK AND WRAP WHOLE WORD
					else {
						int indecesSinceLastSpace = index-lastSpaceIndex;
						currentLine.setLength(currentLine.length()-indecesSinceLastSpace);
						currentLineString = currentLine.toString();
						lines.add(currentLineString);
						currentLine.setLength(0);
						index -= indecesSinceLastSpace;
					}
				}
				index++;
			}
			else {
				// ADD THE FINAL LINE AFTER PROCESSING HAS ENDED, ENSURING THE FINAL LINE GETS ADDED
				lines.add(currentLine.toString());
				finished = true;
			}
		}
		// SIMPLE LOOP FOR DRAWING THE LINES
		for (int i = 0; i < lines.size(); i++) {
			if (shadowColor != null) drawStringWithShadow(g2, shadowColor, lines.get(i), x, y+i*textHeight+textCenterToBaselineOffset);
			else g2.drawString(lines.get(i), x, y);
		}
	}
	
	// CONSTRUCTOR
	private UIUtils() {
		
	}
}