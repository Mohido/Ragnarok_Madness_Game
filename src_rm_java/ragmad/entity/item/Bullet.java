package ragmad.entity.item;

import ragmad.GameEngine;
import ragmad.entity.Entity;
import ragmad.graphics.sprite.Sprite;
import ragmad.scenes.gamescene.GameScene;

public class Bullet extends Entity{ 
	private double angle_r;
	private Sprite sprite;
	private double speed;
	
	/*These defines how much x and y the bullet is moving (it can be deduced from the angle)*/
	private double yUnit, xUnit;
	
	
	/**
	 * Creates a bullet object (A projectile).
	 * @param xStart - Raster coordinates of where the bullet should start.
	 * @param yStart - Raster coordinates of where the bullet should start.
	 * @param angle_r - Angle of the projection.
	 * @param sprite - Bullet shape
	 * @param speed - Bullet speed
	 */
	public Bullet(int xStart, int yStart, double angle_r, Sprite sprite, double speed) {
		this.x = xStart; 
		this.y = yStart;
		this.angle_r = angle_r;
		this.sprite = sprite;
		this.speed = speed;
		
		this.yUnit = Math.sin(angle_r);
		this.xUnit = Math.cos(angle_r);
	}
	
	
	/**
	 * Updates the projectile movement
	 */
	public void update() {
		this.x += speed*xUnit;
		this.y += speed*yUnit;
	}
	
	/**
	 * Renders the Projectile
	 */
	public void render() {
		int[] outputPixels = GameEngine.GetPixels();
		int scale = GameScene.SCALING;
		
		for(int y = 0; y < this.sprite.getHeight()*scale; y++) {
			int yy = (int)(this.y + y);
			for(int x = 0; x < this.sprite.getWidth()*scale; x++) {
				int xx = (int)(this.x + x);
				int col = this.sprite.getPixels()[ (x/scale) + (y/scale) * this.sprite.getWidth()];
				
				if(0 <= xx && xx < GameEngine.GetWidth() && 0 <= yy && yy < GameEngine.GetHeight() && (col & 0xff000000) != 0 ) {
					outputPixels[xx + yy*GameEngine.GetWidth()] = col;
				}
			}
		}
	}
}
