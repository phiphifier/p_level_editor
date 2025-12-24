package main;

import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.awt.Font;
import javax.imageio.ImageIO;

public class Resources {

	// VARS
		// CACHED AND/OR STORED RESOURCE REFERENCES
	private static Font globalFont = null;
	private static BufferedImage icon = null;
	private static BufferedImage tilesetImage = null;
	private static ArrayList<BufferedImage> tiles = new ArrayList<BufferedImage>();
	private static ArrayList<BufferedImage> backgrounds = new ArrayList<BufferedImage>();
		// TILESET INFO CONSTANTS
	public static final int TILE_SIZE = 32;
	private static final int TILESET_WIDTH = 768;
	private static final int TILESET_HEIGHT = 384;
		// BACKGROUND ID/INDEX CONSTANTS
	public static final int ID_BACKGROUND_SUNSET_SKY = 0;
		// FILE PATH CONSTANTS
	private static final String PATH_SPRITE_ICON = "/images/spr_icon.png";
	private static final String PATH_SPRITE_TILES = "/images/spr_tiles.png";
	private static final String PATH_BACKGROUND_SKY_SUNSET = "/images/bckg_sky_sunset.png";
	private static final String PATH_GLOBAL_FONT = "/misc/fnt_handlee.ttf";
	
	// CONSTRUCTOR
	private Resources() {
		
	}
	
	// LOAD RES
	public static void loadResources() {
		try {
			// LOAD ICON
			icon = ImageIO.read(Resources.class.getResourceAsStream(PATH_SPRITE_ICON));
			// LOAD, THEN CACHE TILES
			tilesetImage = ImageIO.read(Resources.class.getResourceAsStream(PATH_SPRITE_TILES));
			cacheTiles(tilesetImage);
			// LOAD BACKGROUNDS
			backgrounds.add(ImageIO.read(Resources.class.getResourceAsStream(PATH_BACKGROUND_SKY_SUNSET)));
			// LOAD FONT
			globalFont = Font.createFont(Font.TRUETYPE_FONT, Resources.class.getResourceAsStream(PATH_GLOBAL_FONT));
		}
		catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// CACHE INDIVIDUAL TILES
	private static void cacheTiles(BufferedImage tileset) {
		for(int y = 0; y < TILESET_HEIGHT/TILE_SIZE; y++) {
			for(int x = 0; x < TILESET_WIDTH/TILE_SIZE; x++) {
				tiles.add(tileset.getSubimage(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE));
			}
		}
	}
	
	// GET ICON
	public static BufferedImage getIcon() {
		return icon;
	}
	
	// GET TILE
	public static BufferedImage getTile(int tileID) {
		return tiles.get(tileID);
	}
	
	// GET TILESET IMAGE
	public static BufferedImage getTilesetImage() {
		return tilesetImage;
	}
	
	// GET TILESET IMAGE WIDTH
	public static int getTilesetImageWidth() {
		return TILESET_WIDTH;
	}
	
	// GET TILESET IMAGE
	public static int getTilesetImageHeight() {
		return TILESET_HEIGHT;
	}
	
	// GET BACKGROUND
	public static BufferedImage getBackground(int backgroundID) {
		return backgrounds.get(backgroundID);
	}
	
	// GET GLOBAL FONT NAME
	public static Font getGlobalFont() {
		return globalFont;
	}
}