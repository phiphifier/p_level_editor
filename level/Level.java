package level;

import main.Drawable;
import main.GameManager;
import main.Resources;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.util.ArrayList;

public class Level implements Drawable {

	// VARS
	private TileLayer[] layers;
	private ArrayList<EntityDataHolder> entities = new ArrayList<EntityDataHolder>();
	private ArrayList<BackgroundDataHolder> backgrounds = new ArrayList<BackgroundDataHolder>();
	private static final int TILE_ID_BOUNDS_BACKGROUND = 23;
	private static final int TILE_ID_GRID = 22;
	public static final int BLANK_TILE_ID = 0;
	private Camera camera;
	private int widthInTiles;
	private int heightInTiles;
	private int widthInPixels;
	private int heightInPixels;
	private int layerCount;
	private boolean isGridEnabled = true;
	private static final float LEVEL_BOUNDS_BACKGROUND_ALPHA = 0.75f;
	private TileLayer boundsLayer;
	private TileLayer gridLayer;
	
	// BLANK LEVEL CONSTRUCTOR
	public Level(int widthInTiles, int heightInTiles, int layerCount) {
		this.widthInTiles = widthInTiles;
		this.heightInTiles = heightInTiles;
		widthInPixels = widthInTiles*Resources.TILE_SIZE;
		heightInPixels = heightInTiles*Resources.TILE_SIZE;
		camera = new Camera();
		camera.enableBounds(Camera.Bounds.OVER_SCROLL, 0, 0, heightInPixels, widthInPixels);
		this.layerCount = layerCount;
		layers = new TileLayer[layerCount];
		for (int i = 0; i < layerCount; i++) {
			layers[i] = new TileLayer(new int[widthInTiles][heightInTiles], this);
		}
		createBoundsLayer();
		createGridLayer();
	}
	
	// POPULATED LEVEL CONSTRUCTOR
	public Level(int[][][] layerData, ArrayList<EntityDataHolder> entities, ArrayList<BackgroundDataHolder> backgrounds) {
		// INITIAL SETUP
		layers = new TileLayer[layerData[0][0].length];
		camera = new Camera();
		// ADD LAYERS
		for (int layer = 0; layer < layers.length; layer++) {
			int[][] tileLayerData = new int[layerData.length][layerData[0].length];
			for (int y = 0; y < layerData[0].length; y++) {
				for (int x = 0; x < layerData.length; x++) {
					tileLayerData[x][y] = layerData[x][y][layer];
				}
			}
			layers[layer] = new TileLayer(tileLayerData, this);
		}
		// ADD ENTS
		for (int i = 0; i < entities.size(); i++) addEntity(entities.get(i));
		// ADD BACKGROUNDS
		for (int i = 0; i < backgrounds.size(); i++) addBackground(backgrounds.get(i));
		// FINISH INITIAL SETUP
		this.widthInTiles = layers[0].getWidthInTiles();
		this.heightInTiles = layers[0].getHeightInTiles();
		widthInPixels = widthInTiles*Resources.TILE_SIZE;
		heightInPixels = heightInTiles*Resources.TILE_SIZE;
		this.layerCount = layers.length;
		camera.enableBounds(Camera.Bounds.OVER_SCROLL, 0, 0, heightInPixels, widthInPixels);
		createBoundsLayer();
		createGridLayer();
	}
	
	// GET CAMERA
	public Camera getCamera() {
		return camera;
	}
	
	// GET LEVEL WIDTH IN TILES
	public int getWidthInTiles() {
		return widthInTiles;
	}
	
	// GET LEVEL HEIGHT IN TILES
	public int getHeightInTiles() {
		return heightInTiles;
	}
	
	// GET LEVEL LAYER COUNT
	public int getLayerCount() {
		return layerCount;
	}
	
	// GET LAYER
	public TileLayer getLayer(int layerDepth) {
		if (layerDepth >= 0 && layerDepth < layers.length) return layers[layerDepth];
		else return null;
	}
	
	// GET LAYERS
	public TileLayer[] getLayers() {
		return layers;
	}
	
	// SET LAYERS
	public void setLayers(TileLayer[] newLayers) {
		layers = newLayers;
		layerCount = newLayers.length;
	}
	
	// GET ENTITIES
	public ArrayList<EntityDataHolder> getEntities() {
		return entities;
	}
	
	// GET BACKGROUNDS
	public ArrayList<BackgroundDataHolder> getBackgrounds() {
		return backgrounds;
	}
	
	// ADD ENTITY
	public EntityDataHolder addEntity(EntityDataHolder entity) {
		entity.setCamera(camera);
		entities.add(entity);
		return entity;
	}
	
	// ADD BACKGROUND
	public BackgroundDataHolder addBackground(BackgroundDataHolder background) {
		backgrounds.add(background);
		return background;
	}
	
	// REMOVE ENTITY
	public ArrayList<EntityDataHolder> removeEntitiesAt(int atX, int atY) {
		ArrayList<EntityDataHolder> entityRemovalQueue = new ArrayList<EntityDataHolder>();
		for (int i = 0; i < entities.size(); i++) {
			EntityDataHolder e = entities.get(i);
			if (e.getX() == atX && e.getY() == atY) {
				entityRemovalQueue.add(e);
			}
		}
		entities.removeAll(entityRemovalQueue);
		return entityRemovalQueue;
	}
	
	// SET GRID ENABLED
	public void setGridEnabled(boolean newState) {
		isGridEnabled = newState;
	}
	
	// CREATE BOUNDS LAYER
	private void createBoundsLayer() {
		boundsLayer = new TileLayer(new int[widthInTiles][heightInTiles], this);
		for (int y = 0; y < heightInTiles; y++) {
			for (int x = 0; x < widthInTiles; x++) {
				boundsLayer.setTile(TILE_ID_BOUNDS_BACKGROUND, x, y);
			}
		}
	}
	
	// CREATE GRID LAYER
	private void createGridLayer() {
		gridLayer = new TileLayer(new int[widthInTiles][heightInTiles], this);
		for (int y = 0; y < heightInTiles; y++) {
			for (int x = 0; x < widthInTiles; x++) {
				gridLayer.setTile(TILE_ID_GRID, x, y);
			}
		}
	}
	
	// CLOSE
	public void close() {
		GameManager.removeDrawable(this);
	}
	
	// DRAW
	@Override
	public void draw(Graphics2D g2) {
		// DRAW LEVEL BOUNDS LAYER
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, LEVEL_BOUNDS_BACKGROUND_ALPHA));
		for (int y = 0; y < heightInTiles; y++) {
			for (int x = 0; x < widthInTiles; x++) {
				g2.drawImage(Resources.getTile(boundsLayer.getLayerData()[x][y]), camera.getScreenX(x*Resources.TILE_SIZE), camera.getScreenY(y*Resources.TILE_SIZE), Resources.TILE_SIZE, Resources.TILE_SIZE, null);
			}
		}
		// DRAW GRID LAYER
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		if (isGridEnabled) for (int y = 0; y < heightInTiles; y++) {
			for (int x = 0; x < widthInTiles; x++) {
				g2.drawImage(Resources.getTile(gridLayer.getLayerData()[x][y]), camera.getScreenX(x*Resources.TILE_SIZE), camera.getScreenY(y*Resources.TILE_SIZE), Resources.TILE_SIZE, Resources.TILE_SIZE, null);
			}
		}
		// DRAW ALL OTHER LAYERS
		for (int layer = 0; layer < layers.length; layer++) {
			TileLayer tl = layers[layer];
			if (tl.getIsVisible()) {
				int cTopBoundTile = camera.getY()/Resources.TILE_SIZE;
				int cLeftBoundTile = camera.getX()/Resources.TILE_SIZE;
				int cBottomBoundTile = cTopBoundTile+camera.getViewHeight()/Resources.TILE_SIZE+1;
				int cRightBoundTile = cLeftBoundTile+camera.getViewWidth()/Resources.TILE_SIZE+1;
				for (int y = Math.max(0, cTopBoundTile); y < Math.min(heightInTiles, cBottomBoundTile); y++) {
					for (int x = Math.max(0, cLeftBoundTile); x < Math.min(widthInTiles, cRightBoundTile); x++) {
						if (tl.getLayerData()[x][y] != BLANK_TILE_ID) {
							g2.drawImage(Resources.getTile(tl.getLayerData()[x][y]), camera.getScreenX(x*Resources.TILE_SIZE), camera.getScreenY(y*Resources.TILE_SIZE), Resources.TILE_SIZE, Resources.TILE_SIZE, null);
						}
					}
				}
			}
		}
		// DRAW ENTS
		for (int i = 0; i < entities.size(); i++) {
			EntityDataHolder e = entities.get(i);
			e.draw(g2);
		}
	}
}