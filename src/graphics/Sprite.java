package graphics;

import java.io.IOException;

import org.lwjgl.examples.spaceinvaders.Texture;
import org.lwjgl.opengl.GL11;

/**
 * Implementation of sprite that uses an OpenGL quad and a texture
 * to render a given image to the screen.
 * 
 * @author Kevin Glass
 * @author Brian Matzon
 */
public class Sprite{
	/** The texture that stores the image for this sprite */
	private Texture texture;
  
	/** The width in pixels of this sprite */
	private int width;
  
	/** The height in pixels of this sprite */
	private int height;
	
	/**
	 * Create a new sprite from a specified image.
	 * 
	 * @param window The window in which the sprite will be displayed
	 * @param ref A reference to the image on which this sprite should be based
	 */
	public Sprite(TextureLoader loader,String ref) {
		try {
			texture = loader.getTexture(ref);
			
			width = texture.getImageWidth();
			height = texture.getImageHeight();
		} catch (IOException e) {
			// a tad abrupt, but our purposes if you can't find a 
			// sprite's image you might as well give up.
			System.err.println("Unable to load texture: "+ref);
			System.exit(42);
		}
	}
	
	/**
	 * Get the width of this sprite in pixels
	 * 
	 * @return The width of this sprite in pixels
	 */
	public int getWidth() {
		return texture.getImageWidth();
	}

	/**
	 * Get the height of this sprite in pixels
	 * 
	 * @return The height of this sprite in pixels
	 */
	public int getHeight() {
		return texture.getImageHeight();
	}

	/**
	 * Draw the sprite at the specified location
	 * 
	 * @param x The x location at which to draw this sprite
	 * @param y The y location at which to draw this sprite
	 * @param angle l'angle
	 */
	public void draw(int x, int y) {
		draw(x,y,0,1);
	}
	
	public void draw(int x, int y, float angle) {
		draw(x,y,angle,1);
	}
	
	public void draw(int x, int y, float angle, float zoom) {
		draw(x, y, angle, zoom, 1, 1, 1);
	}
	
	public void draw(int x, int y, float angle, float zoom, float r, float g, float b) {
		draw(x,y,angle,zoom,r,g,b,1);
	}
	
	public void draw(int x, int y, float angle, float zoom, float r, float g, float b, float a) {
		// store the current model matrix
		GL11.glPushMatrix();
		
		// bind to the appropriate texture for this sprite
		texture.bind();
    
		// translate to the right location and prepare to draw
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(angle, 0, 0, 1);
		GL11.glScalef(zoom, zoom, 1);
		GL11.glTranslatef(-width/2, -height/2, 0);
    	GL11.glColor4f(r,g,b,a);
		
		// draw a quad textured to match the sprite
    	GL11.glBegin(GL11.GL_QUADS);
		{
	      GL11.glTexCoord2f(0, 0);
	      GL11.glVertex2f(0, 0);
	      GL11.glTexCoord2f(0, texture.getHeight());
	      GL11.glVertex2f(0, height);
	      GL11.glTexCoord2f(texture.getWidth(), texture.getHeight());
	      GL11.glVertex2f(width,height);
	      GL11.glTexCoord2f(texture.getWidth(), 0);
	      GL11.glVertex2f(width,0);
		}
		GL11.glEnd();
		
		// restore the model view matrix to prevent contamination
		GL11.glPopMatrix();
	}
	
}