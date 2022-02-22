/**
 * Swipe.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * @author SATHISH
 *
 */
public class Swipe extends Sprite{

	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pTextureRegion
	 * @param vbom
	 */
	public Swipe(float pX, float pY,
			ITextureRegion pTextureRegion,
			VertexBufferObjectManager vbom) {
		super(pX, pY, pTextureRegion, vbom);
		// TODO Auto-generated constructor stub
	}

}
