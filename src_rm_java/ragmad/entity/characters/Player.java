package ragmad.entity.characters;

import ragmad.scenes.gamescene.Map;

import java.util.ArrayList;
import java.util.HashMap;

import ragmad.GameEngine;
import ragmad.entity.item.Item;
import ragmad.entity.item.WeaponItem;
import ragmad.graphics.sprite.Sprite;
import ragmad.io.Keyboard;
import ragmad.io.Mouse;
import ragmad.scenes.gamescene.GameScene;
import ragmad.scenes.gamescene.Tile;

/**
 * Player in the game
 */
public class Player extends Characters {
	 
	
	private Sprite curSprite;
	private int anim = 0;
	private boolean isWalking = false;
	private double xCord, yCord;
	int animationRows, animationCols;
	private Sprite[] animationSprites;
	private HashMap<Direction, Integer> spriteMap;
	private int currentAnimationCol;
	
	private ArrayList<Item> inventory; /// Inventory is a bag. It stores different kind of items. However, for the simplicity, we are taking first picked item as a weaponItem .
	
	/**
	 * A testing implementation for creating a spriteless player with no animation. Note that this will not work! It is only for testing purposes.
	 * @param x - raster position of the player.
	 * @param y - raster position of the player.
	 */
	public Player(int x, int y) {
		this.x = -x;
		this.y = -y;
		curSprite = null;
	}
	
	
	
	/**
	 * Constructs and initializes a Player. It can create a player and defines its animation sprites, and directions/animation mapping.
	 * @param x - This is the X position of the player.	Positive X means move the player Right from the top-left of the screen.
	 * @param y - This is the Y position of the player. Positive Y means move the player Downwards from the top-left of the screen
	 * @param animationSprites - This is an array that contains all the Sprites that the player going to use. Define the Sprites in a contigoues order. E.g: Up sprites, then Down direction sprites and so on. Note: Sprites number per direction must be same. 
	 * @param animationTypes - This is the row counts of the animation sprites. In other words, how many animations types do we have. E.g: Up,Right,Left,Down are 4 animation types.
	 * @param animationsPerType - This is the count of animations we do have per each direction. E.G: 'animationsPerType = 4' means that we have 4 different animations for walking on a specific direction. Note that all directions will have the same count of animations.
	 * @param spriteMap - This is a map that maps a Direction to a sprite Row (Animation Type). Note that it relies that the animationSprites contiguous sprites.
	 */
	public Player(int x, int y, Sprite[] animationSprites, int animationTypes, int animationsPerType,  HashMap<ragmad.entity.characters.Direction, Integer> spriteMap) {
		this.x = -x; 
		this.y = -y;
		this.currentAnimationCol = 0;
		this.spriteMap = spriteMap;
		this.animationSprites = animationSprites;
		this.animationCols = animationsPerType;
		this.animationRows = animationTypes;
		this.curSprite = animationSprites[0];
		this.inventory = new ArrayList();
	}
	
	
	/**
	 * A methode which updates the players object (It is thread)
	 * @param frameMovement the movement of the character by pixels
	 * @param map the world map
	 * */
	public void update(int frameMovement, Map map) {
		anim = (anim+1) & 7; 	// same as anim % 32. But much faster. This here is just an update counter that resets when it reaches 32.
		if(anim == 0) {
			this.currentAnimationCol = (currentAnimationCol + 1) % this.animationCols ;
		}

		int xOffset = 0 ,  yOffset = 0;
		if(Keyboard.isUp()) yOffset+=frameMovement;
		if(Keyboard.isDown()) yOffset-=frameMovement;
		if(Keyboard.isRight()) xOffset-=frameMovement;
		if(Keyboard.isLeft()) xOffset+=frameMovement;
		double modifiedDirX = 0;
		double modifiedDirY = 0;
		
		if(xOffset != 0 || yOffset != 0) {
			double temp = Math.sqrt(xOffset*xOffset + yOffset*yOffset);
			modifiedDirX = (3 * xOffset/temp);
			modifiedDirY = (3* yOffset/temp);
			
			move(modifiedDirX, modifiedDirY, map, map.getColorMap(), curSprite);
			isWalking = true;
			int a_r = this.spriteMap.get(direction); // Get the row of the animation sprite we will be animating. 
			int a_c = this.currentAnimationCol;
			this.curSprite = this.animationSprites[a_c + a_r * this.animationCols];
		}else {
			isWalking = false;
		}
		
		/*Shoot if mouse is pressed*/ 
		if(Mouse.buttonNum == 1 && this.inventory.size() > 0 && inventory.get(0) instanceof WeaponItem) {
			double angle_r = Math.atan2(Mouse.y - (GameEngine.GetHeight()>>1), Mouse.x - (GameEngine.GetWidth() >> 1) );
			((WeaponItem)inventory.get(0)).shoot(angle_r, -(int)this.x, -(int)this.y);
		} 
		
		/*update projectiles*/
		if( this.inventory.size() > 0 && inventory.get(0) instanceof WeaponItem) {
			((WeaponItem)this.inventory.get(0)).offsetChange(modifiedDirX, modifiedDirY); // if player moves, offset the projectiles
			((WeaponItem)this.inventory.get(0)).update();  // update projectiles movement.
		}
	}
	
	
	
	
	
	
	
	/**
	 * Method that renders the player on the screen
	 * 
	 * @param SCALING the scaling rate of the player
	 * */
	public void render(int SCALING) {
		int[] outputPixels = GameEngine.GetPixels();
		int[] tilePixels = curSprite.getPixels();
		
		int s_height =( curSprite.getHeight()*SCALING);
		int s_width = (curSprite.getWidth()*SCALING);

		int xPixel = (int)x;
		int yPixel = (int)y;

		for(int y = 0 ; y < s_height; y++) {
			int yy = y - yPixel;   //Mapping coordinates space to the GameEngine pixel Space (Raster space) //yOffset for vertical movement
			if( yy >= GameEngine.GetHeight()) break;
			if(yy < -s_height) break;
			if(yy < 0) continue;  
			for(int x = 0 ; x < s_width; x++) {
				int xx = x - xPixel;
				int col = tilePixels[x/SCALING + (y/SCALING) * curSprite.getWidth()]; // getting the pixel colour of the tile
				
				if ( xx >= GameEngine.GetWidth() ) // break if the renderer pointer has exited screen right side
					break;
				if( xx < 0 || (col & 0xff000000) == 0 )  //don't do anything if the xx is out of bounds or pixel is transparent 
					continue;
				
				if(col != 0xffd6e7ea) outputPixels[xx + yy * GameEngine.GetWidth()] = col;
			}
		}
		
		/*Render projectiles*/
		if( this.inventory.size() > 0 && inventory.get(0) instanceof WeaponItem) {
			((WeaponItem)this.inventory.get(0)).render();
		}
	}


	
//	private void cordToRaster() {
//		int normal_height =  Tile.TILE_HEIGHT*GameScene.SCALING;
//		int normal_width = Tile.TILE_WIDTH*GameScene.SCALING;
//		int n_width_half = normal_width >> 1;
//		int n_height_half = normal_height >> 1;
//
//		/*Foe pixel coordinates*/
//		this.x = (-yCord * n_width_half - xCord * n_width_half - GameScene.xOffset);
//		this.y = (-yCord * n_height_half + xCord * n_height_half -  GameScene.yOffset);
//	}
	
	
	
	
	/**
	 * Add an item to the inventory.
	 * @param it - The item wanted to be added to the inventory (Picked up Item)
	 */
	public void addItem(Item it) {
		System.out.println("Added Item: " + it.toString());
		this.inventory.add(it);
	}
	
}
