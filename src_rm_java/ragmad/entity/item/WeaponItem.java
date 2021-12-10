package ragmad.entity.item;

import java.util.ArrayList;

import ragmad.GameEngine;
import ragmad.entity.Entity;
import ragmad.graphics.sprite.Sprite;
import ragmad.scenes.gamescene.GameScene;

public class WeaponItem extends Item{
	
	private int strength;	// Strength of the item. Defines the damage dealt to the oponent.
	private Sprite sprite;
	private ArrayList<Bullet> firedBullets;
	private double bulletSpeed;
	private double range; // isometric range of shooting.
	
	
	/**
	 * Creates a weapon item. A weapon item is an item that can be equiped by the player as a main weapon. This kind of weapon is capable of shooting bullets.
	 * @param itemName - The item name
	 * @param strength - Strength of the item (Damage dealt by the item)
	 * @param shape - Item shape (Sprite/Image that defines the item bullets)
	 * @param speed - Speed of the item bullets.
	 */
	public WeaponItem(String itemName, int strength, Sprite shape, double speed , double range) {
		this.itemName = itemName;
		this.strength = strength;
		this.sprite = shape;
		this.range = range;
		this.bulletSpeed = speed;
		this.firedBullets = new ArrayList();
	}
	
	/**
	 * Defines the usage of an item over an Entity (Scene objects).
	 * @param en - entity.
	 * @return - If the item was successfully used.
	 */
	public boolean usage(Entity en) {return false;}
	 
	/**
	 * Shoots a bullet toward the given angle.
	 * @param angle_r - Angle of the shooting.
	 * @param xStart - x raster starting point.
	 * @param yStart - y raster starting point.
	 */
	public void shoot(double angle_r, double xCordStart, double yCordStart) {
		this.firedBullets.add(new Bullet(xCordStart, yCordStart, angle_r, this.sprite, bulletSpeed, this.range));
	}
	 
	
	/**
	 * Update all the bullets that were fired by this gun.
	 */
	public void update() {
		for(int i = 0; i < firedBullets.size(); i++) {
			this.firedBullets.get(i).update();			
		}
		
		int i = 0;
		while(i < firedBullets.size()) {
			if(firedBullets.get(i).isOutRange()) {
				this.firedBullets.remove(i);
			}else {
				i++;
			}	
		}
		
		
	}
	
	/**
	 * Renders all the bullets that were fired by this gun.
	 */
	public void render() {
		for(int i = 0; i < firedBullets.size(); i++) {
			this.firedBullets.get(i).render();
		}
	}

	
	
	
}
