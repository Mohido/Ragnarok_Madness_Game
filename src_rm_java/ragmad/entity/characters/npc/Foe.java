package ragmad.entity.characters.npc;

import java.util.ArrayList;
import java.util.HashMap;
import ragmad.GameEngine;
import ragmad.entity.characters.Characters;
import ragmad.entity.characters.Direction;
import ragmad.entity.item.Item;
import ragmad.graphics.sprite.Sprite;
import ragmad.scenes.gamescene.GameScene;


public class Foe extends Characters{
	Sprite[] animationSprites;			// Animations: Row are directions, columns are animations sequence for that direction
	int animationRows, animationCols;
	HashMap<Direction, Integer> spriteMap; // Direction to animationRow mapping.
	Sprite curSprite;
	private ArrayList<Item> inventory;		// Foe can have Items and its own inventory.
	int anim, currentAnimationCol; 			// Helps in rendering the current sprite.
	double xDir, yDir; // = (2 * random - 1); // = (2 * (1 - random) - 1);
	
	
	/**
	 * Creates a Foe type of NPC. Foes can have inventory and attack the player whenever he is in range.
	 * Foes type of NPCs are attackable by the player. After creating a Foe, make sure to attach it to a map.
	 * 
	 * @param x_cord - The x Isometric coordinate of the Foe (Relates to the attached map)
	 * @param y_cord - The y Isometric Coordinate of the Foe (Relates to the attached map)
	 * @param animationSprites - Matrix where the rows contains the directions and the columns corrosponds to the sequence of the animation
	 * @param animationTypes - How many rows the animation matrix holds (Animation types)
	 * @param animationsPerType - Number of sprites in each row/
	 * @param spriteMap - Direction to Sprite Row map.
	 */
	public Foe(int x_cord, int y_cord, Sprite[] animationSprites, int animationTypes, int animationsPerType,  HashMap<Direction, Integer> spriteMap) {
		this.isMoving = false;
		this.speed = 0.01;  // This is a virtual unit.
		this.anim = 0;
		this.currentAnimationCol = 0;
		this.animationSprites = animationSprites;
		this.animationRows = animationTypes;
		this.animationCols = animationsPerType;
		this.spriteMap = spriteMap;
		this.curSprite = animationSprites[0];
		
		/*Foe Isometric coordinates*/
		this.xCord = x_cord;
		this.yCord = y_cord;

		setRasterPosFromCord(this.curSprite.getWidth()/2, this.curSprite.getHeight()/2);
		this.xDir = Math.random()*2 - 1;
		this.yDir = Math.random()*2 - 1;
		
	}
	
	
	
	
	
	/**
	 * A methode which updates the players object (It is thread)
	 * @param frameMovement the movement of the character by pixels
	 * @param map the world map
	 * */
	public void update() {
		setRasterPosFromCord(this.curSprite.getWidth()/2, this.curSprite.getHeight()/2);
		/*Clock for updating the sprite animations.*/
		anim = (anim+1) & 7; 	// same as anim % 32. But much faster. This here is just an update counter that resets when it reaches 32.
		if(anim == 0) {
			this.currentAnimationCol = (currentAnimationCol + 1) % this.animationCols ;
		}
		
		/*Moving the Foe*/
		if((int)(Math.random() * 100 ) == 0) {
			xDir = Math.random()*2 - 1;
			yDir = Math.random()*2 - 1;
		}
		
		
		if(xDir == 0 && yDir == 0)
			this.isMoving = false;
		
		/*Update Movement...*/
		if(xDir != 0 || yDir != 0) {
			double len = Math.sqrt(xDir*xDir + yDir*yDir);
			this.move(xDir/len, yDir/len);
		}
		
		
		/*Updating the sprite.*/
		if(this.isMoving) {
			int a_r = this.spriteMap.get(direction); // Get the row of the animation sprite we will be animating. 
			int a_c = this.currentAnimationCol;
			this.curSprite = this.animationSprites[a_c + a_r * this.animationCols];
		}
		
	}
	
	
	
	
	
	/**
	 * Method that renders the player on the screen
	 * 
	 * @param SCALING the scaling rate of the player
	 * */
	public void render() {
		int SCALING = 1;
		int[] outputPixels = GameEngine.GetPixels();
		int[] tilePixels = curSprite.getPixels();
		
		int s_height = (curSprite.getHeight()*SCALING);
		int s_width =  (curSprite.getWidth()*SCALING);

		int xPixel = (int)(x - GameScene.xOffset);
		int yPixel = (int)(y - GameScene.yOffset);

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
				
				if(col != 0xffd6e7ea) outputPixels[xx + yy * GameEngine.GetWidth()] = col + 0xfff00f0f;
			}
		}
	}
	
	
	/**
	 * Add an item to the inventory.
	 * @param it - The item wanted to be added to the inventory (Picked up Item)
	 */
	public void addItem(Item it) {
		System.out.println("Added Item: " + it.toString());
		this.inventory.add(it);
	}
}
