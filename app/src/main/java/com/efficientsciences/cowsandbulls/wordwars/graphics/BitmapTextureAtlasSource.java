/**
 * BitmapTextureAtlasSource.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;


/**
 * @author SATHISH
 *
 */
public class BitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource 
{
    private final int[] mColors;
 
    public BitmapTextureAtlasSource(Bitmap pBitmap)
    {
    	super(0,0, pBitmap.getWidth(), pBitmap.getHeight());
        
        mColors = new int[mTextureWidth * mTextureHeight];
        
        for(int y = 0; y < mTextureHeight; ++y)
        {
        	for( int x = 0; x < mTextureWidth; ++x)
        	{
        		mColors[x + y * mTextureWidth] = pBitmap.getPixel(x, y);
        	}
        }
    }

	@Override
	public Bitmap onLoadBitmap(Config pBitmapConfig)
	{
		return Bitmap.createBitmap(mColors, mTextureWidth, mTextureHeight, Bitmap.Config.ARGB_8888);
	}

	@Override
	public IBitmapTextureAtlasSource deepCopy()
	{
		return new BitmapTextureAtlasSource(Bitmap.createBitmap(mColors, mTextureWidth, mTextureHeight, Bitmap.Config.ARGB_8888));
	}

	/* (non-Javadoc)
	 * @see org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource#onLoadBitmap(android.graphics.Bitmap.Config, boolean)
	 */
	@Override
	public Bitmap onLoadBitmap(Config pBitmapConfig, boolean pMutable) {
		// TODO Auto-generated method stub
		return null;
	}
}