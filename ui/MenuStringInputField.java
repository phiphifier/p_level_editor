package ui;

import main.Inputs;
import main.TypedInputListener;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;

public class MenuStringInputField extends Menu implements TypedInputListener {

	// VARS
		// CHAR CONSTANTS
	private static final char[] FILE_PATH_CHARS = new char[] {'/','\\',':','*','?','"','<','>'};
	public static final char CHAR_BACKSPACE = '\b';
	public static final char CHAR_ENTER = '\n';
		// MENU INSTANCE VARS
	private boolean allowFilePathChars;
	private boolean numbersOnly;
	private int charLimit;
	private StringInputFieldListener listener;
	private StringBuilder currentInputBuilder = new StringBuilder();
	private String currentInput = "";
		// MENU PROPERTIES
	public static final int MENU_HEIGHT = 28;
	private static final String COMPLETE_INPUT_HINT = "Press [ENTER] to proceed...";
	private static final int FONT_HINT_SIZE = 14;
	private static final Font FONT_HINT = UIConstants.DEFAULT_FONT.deriveFont(Font.PLAIN, FONT_HINT_SIZE);
	private static final Font FONT_BODY = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, UIConstants.MENU_FONT_BODY_SIZE);
	private static final int INPUT_TEXT_HORIZONTAL_PADDING = 2;
	private static final int TEXT_INPUT_TITLE_SIZE = 21;
	
	// CONSTRUCTOR
	public MenuStringInputField(Menu owner, String expectedInput, int x, int y, int width, int charLimit, boolean numbersOnly, boolean allowFilePathChars, StringInputFieldListener listener) {
		super(owner);
		setPosition(x, y);
		setSize(width, MENU_HEIGHT);
		setTitle(expectedInput, HorizontalAlignment.LEFT);
		this.allowFilePathChars = allowFilePathChars;
		this.numbersOnly = numbersOnly;
		this.charLimit = charLimit;
		this.listener = listener;
		setEnabled(false);
		titleFont = UIConstants.DEFAULT_FONT.deriveFont(Font.BOLD, TEXT_INPUT_TITLE_SIZE);
		bodyColor = UIConstants.MENU_COLOR_BORDER;
		borderColor = UIConstants.BUTTON_COLOR_PRESSED;
	}

	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		String stringToBeDrawn = currentInput;
		drawMenuBody(g2);
		if (Inputs.getTypedInputListener() == this) {
			g2.setFont(FONT_HINT);
			FontMetrics fmHint = g2.getFontMetrics();
			int hintHeight = fmHint.getHeight();
			UIUtils.drawWrappedString(g2, shadowColor, COMPLETE_INPUT_HINT, width, x, y+MENU_HEIGHT+hintHeight/4);
		}
		g2.setFont(FONT_BODY);
		FontMetrics fm = g2.getFontMetrics();
		int inputCenterToBaselineOffset = (fm.getAscent()-fm.getDescent())/2;
		boolean drawInputCursor = Inputs.getTypedInputListener() == this;
		UIUtils.drawLimitedWidthStringWithShadow(g2, shadowColor, stringToBeDrawn, x+INPUT_TEXT_HORIZONTAL_PADDING, y+height/2+inputCenterToBaselineOffset, width, drawInputCursor);
	}

	// BACKSPACE PRESSED
	private String backspacePressed() {
		currentInputBuilder.setLength(Math.max(0, currentInputBuilder.length()-1));
		String inputBuilderString = currentInputBuilder.toString();
		return inputBuilderString;
	}
	
	// ENTER PRESSED
	private String enterPressed() {
		String inputBuilderString = currentInputBuilder.toString();
		if (currentInputBuilder.length() > 0) listener.stringFinished(inputBuilderString);
		else listener.attemptInvalidInput(StringInputFieldListener.ERROR_EMPTY);
		return inputBuilderString;
	}
	
	// FILTER NUMS
	private boolean filterDigits(char c) {
		if (Character.isDigit(c)) return true;
		else return false;
	}
	
	// FILTER PATH SYMBOLS
	private boolean filterFilePathSymbols(char c) {
		for (int i = 0; i < FILE_PATH_CHARS.length; i++) {
			if (c == FILE_PATH_CHARS[i]) return false;
		}
		return true;
	}
	
	// ADD CHAR
	private String addChar(char c) {
		if (charLimit < 0 || currentInputBuilder.length() < charLimit) {
			boolean canAppend = true;
			if (!allowFilePathChars && !filterFilePathSymbols(c)) {
				canAppend = false;
				listener.attemptInvalidInput(StringInputFieldListener.ERROR_FILE_PATH_CHAR);
			}
			if (numbersOnly && !filterDigits(c)) {
				canAppend = false;
				listener.attemptInvalidInput(StringInputFieldListener.ERROR_LETTER);
			}
			if (canAppend) currentInputBuilder.append(c);
		}
		else listener.attemptInvalidInput(StringInputFieldListener.ERROR_FULL);
		String currentBuilderString = currentInputBuilder.toString();
		return currentBuilderString;
	}
	
	// GET INPUTS
	@Override
	public void charTyped(char c) {
		switch(c) {
		case CHAR_BACKSPACE: currentInput = backspacePressed();
		break;
		case CHAR_ENTER: currentInput = enterPressed();
		break;
		default: currentInput = addChar(c);
		}
	}
	
	// SET FIELD TEXT
	public void appendText(String text) {
		boolean canAppend = true;
		for (int i = 0; i < text.length(); i++) {
			if (numbersOnly && !filterDigits(text.charAt(i))) canAppend = false;
			if (!allowFilePathChars && !filterFilePathSymbols(text.charAt(i))) canAppend = false;
			if (canAppend) currentInput = addChar(text.charAt(i));
			canAppend = true;
		}
	}
	
	// CLOSE
	@Override
	public void close(AdditionalCloseOperation doWhat) {
		super.close(doWhat);
		if (Inputs.getTypedInputListener() == this) Inputs.setTypedInputListener(null);
	}
	
	// UNUSED
	@Override
	public void buttonMoused(int buttonID) {}
	@Override
	public void buttonUnMoused(int buttonID) {}
	@Override
	public void buttonLeftPressed(int buttonID) {}
	@Override
	public void buttonLeftReleased(int buttonID) {}
	@Override
	public void buttonRightPressed(int buttonID) {}
	@Override
	public void buttonRightReleased(int buttonID) {}
}