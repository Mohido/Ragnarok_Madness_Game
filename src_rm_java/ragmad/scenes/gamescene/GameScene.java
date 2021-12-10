package ragmad.scenes.gamescene;

import ragmad.GameEngine;
import ragmad.entity.characters.Player;
import ragmad.entity.characters.npc.Foe;
import ragmad.entity.item.Item;
import ragmad.entity.item.ItemCapsule;
import ragmad.graphics.sprite.Sprite;
import ragmad.io.Keyboard;
import ragmad.scenes.Scene;
import java.util.ArrayList;
import java.util.HashMap;


public class GameScene implements Scene{

	HashMap<Integer, Tile> hashmap;
	public static double xOffset, yOffset;
	public static int SCALING = 1;  // Change it if you want to see different scalings. 
	int xCord;
	int yCord;
	int frameMovement;
	int m_width, m_height;
	private Map map;
	private Player player; 	
	private ArrayList<ItemCapsule> itemCapsules; // Stores the items on the scene.
	private final static int interactionRadiusSqr = 2;
	private ArrayList<Foe> foes;
	
	
	/// _________________________ Constructor Area_________________________________
	/**
	 * Creates a game scene with the given parameters
	 * @param width - visibility width
	 * @param height - visibility height
	 * @param map - map that defines the world
	 * @param player - player to play in the scene.
	 */
	public GameScene(int width, int height, Map map, Player player) {
		this.m_height = height;
		this.m_width = width;
		this.player = player;
		xOffset = GameEngine.GetWidth()/2 - this.player.getX(); 		//For testing change all offset variables to player.y
		yOffset = GameEngine.GetHeight()/2 - this.player.getY();		//For testing change all offset variables to player.y
		this.map = map;
		
		this.foes = new ArrayList();
		this.itemCapsules = new ArrayList<>(); 
	}
	
	
	
	///___________________________ GameEngine component methods area _________________________________
	
	/**
	 * Used to add a predefined item capsule directly to the scene. 
	 * @param x_cord - Isometric X coordinate of the map.
	 * @param y_cord - Isometric Y coordinate of the map.
	 * @param item - the item we want to encapsulate
	 * @param capsuleSprite - 
	 */
	public void addItemCapsule(int x_cord, int y_cord, Item item, Sprite capsuleSprite) {
		/*Check if the item is in the correct range.*/
		ItemCapsule it = new ItemCapsule(x_cord, y_cord,  capsuleSprite, item);
		if( 0 <= it.getXCord() &&  it.getXCord() < this.map.getWidth() && 0 <= it.getYCord() &&  it.getYCord() < this.map.getHeight()) {
			this.itemCapsules.add(it);
		}
	}
	
	
	/**
	 * Used to add a predefined item capsule directly to the scene. 
	 * @param it - Item capsule to be added to the scene.
	 */
	public void addItemCapsule(ItemCapsule it) {
		/*Check if the item is in the correct range.*/
		if( 0 <= it.getXCord() &&  it.getXCord() < this.map.getWidth() && 0 <= it.getYCord() &&  it.getYCord() < this.map.getHeight()) {
			this.itemCapsules.add(it);
		}
	}
	
	
	
	/**
	 * Defines Game physics and scene logic
	 */
	@Override
	public void update() {
		frameMovement = 5;
		player.update(frameMovement, this.map);
		this.updateOffset();
		
		
		for(int i = 0; i < foes.size() ;i++) { this.foes.get(i).update();}
		
		
		if (Keyboard.esc()) { 
			GameEngine.ChangeScene("Menu");
		}
		
		 
		int i = 0;
		while(i < this.itemCapsules.size()) {
			//this.itemCapsules.get(i).updateXY((int)this.xOffset,(int)this.yOffset, this.map.getTileWidth(), this.map.getTileHeight());
			
			double xItemCenter = this.itemCapsules.get(i).getXCord();
			double yItemCenter = this.itemCapsules.get(i).getYCord();
			double xDist = this.player.getXCord() - xItemCenter;
			double yDist = this.player.getYCord() - yItemCenter;
			
			xDist = xDist*xDist;
			yDist = yDist*yDist;
			double distanceSqr = xDist + yDist; 
			boolean inItemRadius =  distanceSqr <= interactionRadiusSqr;
			
			if(Keyboard.equip() && inItemRadius) {
				/*remove Item from the array list and equip it to the player*/
				ItemCapsule itc = this.itemCapsules.remove(i);
				Item it = itc.getItem(); 
				this.player.addItem(it);
			}else if(inItemRadius) { // tell user that he can equip an item. 
				System.out.println("ItemCapsule in range, press 'E' to equip.");
				i++;
			}
			else {			
				i++;
			}
		}	
	}

	
	
	
	/**
	 * Takes care of rendering ot the GameEngine class
	 * 
	 * Developing Note: Speeding things up can be by getting the first XTile in the current raster space and first YTile.
	 * 			This can be done when working with mouse input. The sources are in the Map() class.
	 */
	@Override
	public void render() {
		for(int x = this.map.getWidth() - 1; x >= 0 ; x--) {
			for(int y = 0 ; y < this.map.getHeight(); y++) {
				int id = this.map.getMap()[x+y*map.getWidth()];
				Tile t = this.map.getTile(id);
				if(anchorExists(x, y, t, id)) {
					t.renderToRaster(x, y, (int)xOffset,(int) yOffset, SCALING);
				}
				if(Math.floor(player.getXCord()) == x && Math.floor(player.getYCord()) == y) {
					player.render(1);
				}
				for(int i = 0; i < this.itemCapsules.size(); i++) {
					if(Math.floor(itemCapsules.get(i).getXCord()) == x && Math.floor(itemCapsules.get(i).getYCord()) == y) {
						this.itemCapsules.get(i).render();
					}	
				}
				for(int i = 0; i < foes.size() ;i++) {
					if(Math.floor(foes.get(i).getXCord()) == x && Math.floor(foes.get(i).getYCord()) == y) {
						this.foes.get(i).blocked = true;
					}else {
						this.foes.get(i).blocked = false;
					}
				}
			}
		}
		
		for(int i = 0; i < foes.size() ;i++) {
			this.foes.get(i).render();
		}
		
	}
	
	
	
//	/**
//	 * Helper function that helps in encapsulating the rendering process ofo the item capsules.
//	 * */
//	private void renderCapsules() {
//		for(int i = 0; i < this.itemCapsules.size(); i++) {
//			/*Updating the render positioning regarding offset before rendering.*/
//			this.itemCapsules.get(i).updateXY((int)this.xOffset,(int)this.yOffset, this.map.getTileWidth(), this.map.getTileHeight());
//			/*Rendering phase*/
//			this.itemCapsules.get(i).render();
//		}
//	}
	
	
	
	/**
	 * This is a simple rendering algorithm that I created. We render big sprites from top down, but before we start rendering, we check if the base of the tile (meant to be rendered) exists.
	 * I call the base "Anchor" (Just trying to be cool :) )!
	 * @param x, y - Coordinates of the tile head on the map
	 * @param t - The tile that is meant to be rendered.
	 * @param id - The colour id of the tile (Base colour should be equal to head colour => Tile base and head exists => We can start rendering :))) )
	 * */
	private boolean anchorExists(int x, int y, Tile t, int id) {
		 return this.map.getMap()[ ( x - (t.getIsoWidth() - 1)) + (y + (t.getIsoHeight() - 1)) *map.getWidth()] == id;
	}
	
	
	/**
	 * Zooming in is used to scale the view. Zooming into the game.
	 */
	public static void zoomIn() { SCALING = (SCALING < 2 )? SCALING + 1 : 2 ;}
	
	/**
	 * Zooming out is used to scale out the view. Zooming out of the game.
	 */
	public static void zoomOut() { SCALING = (SCALING > 1 )? SCALING - 1 : 1 ;}

	
	/**
	 * Synchronize the camera with the player object.
	 */
	public void updateOffset() {
		xOffset = this.player.getX() + (GameEngine.GetWidth() >> 1); 		//+ GameEngine.GetWidth()/2 For testing change all offset variables to player.y
		yOffset = this.player.getY()  + (GameEngine.GetHeight() >> 1);		//+ GameEngine.GetHeight()/2  For testing change all offset variables to player.y
	}
	
	
	public void addNPC(Foe yugi) {this.foes.add(yugi);}
	
	
	
}
