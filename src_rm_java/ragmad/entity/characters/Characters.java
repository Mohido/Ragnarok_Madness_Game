package ragmad.entity.characters;


import java.util.HashMap;

import ragmad.GameEngine;
import ragmad.entity.Entity;
import ragmad.graphics.sprite.Sprite;
import ragmad.scenes.gamescene.GameScene;
import ragmad.scenes.gamescene.Map;
import ragmad.scenes.gamescene.Tile;

public abstract class Characters extends Entity {
	public Sprite sprites;
	protected Direction direction;
	protected boolean isMoving = false;	
	
	
	

	
	/**
	 * Takes care if the movement of the character.
	 * @param dirX the offset which will update the x motion
	 * @param dirY the offset which will update the x motion
	 * @param colorsMap A hash map of colors which returns tile
	 * @param sprites sprites object which update the sprite of the player
	 * */
	public void move(int dirX, int dirY, Map map, HashMap<Integer, Tile> colorsMap, Sprite sprites) {
		this.sprites = sprites;
		
		if(dirX > 0) direction = Direction.RIGHT;
		if(dirY > 0) direction = Direction.DOWN;
		if(dirX < 0) direction = Direction.LEFT;
		if(dirY < 0) direction = Direction.UP;
		if(dirX > 0 && dirY > 0) direction = Direction.DOWN_RIGHT;
		if(dirX < 0 && dirY > 0) direction = Direction.DOWN_LEFT;
		if(dirX < 0 && dirY < 0) direction = Direction.UP_LEFT;
		if(dirX > 0 && dirY < 0) direction = Direction.UP_RIGHT;
		
 
		double temp = Math.sqrt(dirX*dirX + dirY*dirY);
		
		double modifiedDirX = (3 * dirX/temp);
		
		double modifiedDirY = (3* dirY/temp);
		
		if(!collision( 0,  modifiedDirY, map, colorsMap)) {
			
			GameScene.yOffset += modifiedDirY ;
		}
		
		if(!collision( 2 * modifiedDirX,  0, map, colorsMap)) {
			GameScene.xOffset += modifiedDirX;
		}
	}
	
	
	
	public void update() {}
	
	
	/**
	 * Method which returns if a tile is collidable or not
	 * @param dirX the pixels which need to be checked before moving on x-axis
	 * @param dirY the pixels which need to be checked before moving on y-axis
	 * @param map it stores the worlds map
	 * @param colorsMap A hash map of colors which returns tile
	 * */
	public boolean collision(double dirX, double dirY, Map map, HashMap<Integer, Tile> colorsMap) {
		boolean solid = false;		
		int playerWidth = sprites.getWidth() ;
		int playerHeight = sprites.getHeight();

		int[] n = map.getTileAt(-(int)x ,-(int)y , (int)(GameScene.xOffset - (playerWidth/2) + dirX),(int) (GameScene.yOffset -  (playerHeight - 14)  + dirY));
		if(n != null) {
			if( map.tileExists(n[0], n[1])) {
				solid = colorsMap.get(map.getMap()[(n[0])+ (n[1])*map.getWidth()]).isSolid();
				}
			}
		return solid;
	}
	
	
	public Direction getDirection() {
		return direction;
	}
}
