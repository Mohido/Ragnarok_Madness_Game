package ragmad.scenes.gamescene;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;


/**
 * Stores a grid of pixels which identifies our Map. Our world basically.
 * @author Mohido
 *
 */
public class Map {
	private int[] map;
	private int width,height;
	private String path;
	
	
	public Map(String path) {
		this.path = path;
		try {
			BufferedImage mapImage = ImageIO.read(new File(path)); 	// Loading an image to the memory
			this.width = mapImage.getWidth();
			this.height = mapImage.getHeight();
			map = new int [this.width*this.height];
			
			mapImage.getRGB(0, 0, this.width, this.height, map, 0, this.width); 	//Copies the loaded image pixels to our pixels-grid
			
		} catch (Exception e) {
			System.out.println("Error with loading the image from the path: " + path );
			e.printStackTrace();
		}
	}
	
	public Map() {
		this.width = 0;
		this.height = 0;
		map = new int[0];
	}
	
	
	
	/// ________________________________ METHDOS AREA ___________________________
	
	
	
	/**
	 * Get the exact tile from raster coordinate system.
	 * Pre-requisites: Tiles map.
	 * Ported From:
	 * 		Source: https://github.com/jorgt/perlin-landscape/pull/1/commits/9a8504043d2c02df507a6b1b3be794aee801b5ad
	 * 		Source: https://stackoverflow.com/questions/21842814/mouse-position-to-isometric-tile-including-height
	
	
	* Function:
	*/
	
	
	
	/// ________________________________ GETTERS AREA ___________________________
	
	public int[] getMap() {return this.map;}
	public int getWidth() {return this.width;}
	public int getHeight() {return this.height;}
	
}
