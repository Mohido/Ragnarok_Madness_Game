package ragmad.entity.item;

import ragmad.GameEngine;
import ragmad.entity.Entity;
import ragmad.graphics.sprite.Sprite;
import ragmad.scenes.gamescene.GameScene;

/**
 * An encapsulation of the Item. This class represents the item that is shown on the map.
 * 		A player can collide with it. Then, press a button, such as 'E',  to equip it.
 * 
 * @author Mohido
 *
 */
public class ItemCapsule extends Entity {
	private Sprite sprite;		// Shape of the Item.
	private int xCord, yCord; 	// Map coordinates where the item resides at
	private Item item;			// Encapsulated item within the item capsule
	
	/**
	 * Create an Item capsule to be displayed on the scene. 
	 * @param x_cord - The X coordinate in the Isometric space related to the map. 
	 * @param y_cord - The y Coordinate in the Isometric space related to the map.
	 * @param sprite - The shape of the Capsule. It can be a treasure for example.
	 * @param item - The item that is within the capsule.
	 * 
	 * Note: Size of hte itemCapsule is Sprite related. It also accounts for the scaling. 
	 */
	public ItemCapsule(int x_cord, int y_cord, Sprite sprite, Item item){
		this.xCord = x_cord;
		this.yCord = y_cord;
		this.item = item;
		this.sprite = sprite;
	}
	
	
	/**
	 * Renders the capsule in the world.
	 */
	public void render() {
		int[] outputPixels = GameEngine.GetPixels();
		int scale = GameScene.SCALING;
		
		for(int y = 0; y < this.sprite.getHeight()*scale ; y++) {
			int yy = (int)this.y + y;
			for(int x = 0 ; x < this.sprite.getWidth()*scale ;x++) {
				int xx = (int)this.x + x;
				int col = this.sprite.getPixels()[x/scale + (y/scale) * this.sprite.getWidth()];
				if(0 <= xx && xx < GameEngine.GetWidth() && 0 <= yy && yy < GameEngine.GetHeight() && (col & 0xff000000) != 0 )
					outputPixels[xx + yy*GameEngine.GetWidth()] = col;
			}
		}
	}
	
	
	/*=----------------------------Getters area---------------------------==*/
	public int getXCord () {return this.xCord;}
	public int getYCord () {return this.yCord;}
	
	
	/**
	 * This is used for updating the raster position of the item capsule. It is better called whenever the Offset is changed in the gamescene. Or before rendering to set the raster coordinates
	 * 
	 * @param xOffset - X Camera Offset (It is also GameScene offset).
	 * @param yOffset - Y Camera Offset (It is also GameScene offset).
	 * @param tileWidth - normal tile width defined in the map.
	 * @param tileHeight - normal tile height defined in the map.
	 */
	public void updateXY(int xOffset, int yOffset, int tileWidth, int tileHeight) {
		int scale = GameScene.SCALING;
		int n_width_half = (tileWidth*scale) >> 1;
		int n_height_half = (tileHeight*scale) >> 1;

		this.x = yCord * n_width_half + xCord * n_width_half + xOffset ; 
		this.y = yCord * n_height_half - xCord * n_height_half + yOffset - n_height_half/2;
	}
	
	

	
}
