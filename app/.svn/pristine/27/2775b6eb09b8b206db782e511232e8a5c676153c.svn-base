/**
 * GameScreenCapture.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.ScreenGrabber;
import org.andengine.entity.util.ScreenGrabber.IScreenGrabberCallback;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.opengl.GLES20;
import android.os.Environment;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.scenes.BaseScene;

/**
 * @author SATHISH
 *
 */
public class GameScreenCapture {




	public static void captureScreen(ScreenGrabber screenCapture, final IEntity scene) {
		final int viewWidth = ResourcesManager.getInstance().camera.getSurfaceWidth();
		final int viewHeight = ResourcesManager.getInstance().camera.getSurfaceHeight();

		final int hWidth = Math.round(viewWidth / 2f);
		final int hHeight = Math.round(viewHeight / 2f);

		screenCapture.grab(0, 0, viewWidth, viewHeight, new IScreenGrabberCallback() {

			@Override
			public void onScreenGrabbed(Bitmap pBitmap) {
				// TODO Auto-generated method stub
				Bitmap b2 = getResizedBitmap(pBitmap, hHeight, hWidth);


				//createFile(b2);

				BitmapTextureAtlas mBitmapTextureAtlasn = new BitmapTextureAtlas(ResourcesManager.getInstance().activity.getTextureManager(), hWidth, hHeight, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
				ITextureRegion SegmentTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromSource(mBitmapTextureAtlasn, getAtlasBitmap(hWidth, hHeight, b2), 0, 0);
				mBitmapTextureAtlasn.load();

				// display Screenshot

				Sprite capturedImage = new Sprite(0,0, SegmentTextureRegion, ResourcesManager.getInstance().activity.getVertexBufferObjectManager());
				capturedImage.setScale(0.4f);
				Rectangle rect = new Rectangle(ResourcesManager.getInstance().windowDimensions.x/2, 150*ResourcesManager.resourceScaler,(capturedImage.getWidthScaled())+30*ResourcesManager.resourceScaler,(capturedImage.getHeightScaled())+20*ResourcesManager.resourceScaler,  ResourcesManager.getInstance().activity.getVertexBufferObjectManager());
				rect.setAnchorCenter(0.5f, 0);
				rect.setColor(0.95f,0.91f,0.87f,1f);
				//rect.setBlendFunctionDestination(GLES20.GL_ONE);
				capturedImage.setAnchorCenter(0.5f, 0.5f);
				capturedImage.setPosition(rect.getWidthScaled()/2,rect.getHeightScaled()/2);
				rect.attachChild(capturedImage);
				rect.registerEntityModifier(new RotationModifier(2, 0, 1110));
				scene.attachChild(rect);
			}

			@Override
			public void onScreenGrabFailed(Exception pException) {
				// TODO Auto-generated method stub
				ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ResourcesManager.getInstance().activity, "not taken", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});

	}

	/**
	 * @param b2
	 */
	protected static void createFile(Bitmap b2) {
		try{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			b2.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/Word");
			dir.mkdirs();
			File f = new File(dir, "wordGame"+ System.currentTimeMillis()+".jpg");
			f.createNewFile();

			FileOutputStream fo = new FileOutputStream(f);
			fo.write(bytes.toByteArray());
			fo.close();

		} catch (final Exception e) {
			ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ResourcesManager.getInstance().activity, e.toString(), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	private static IBitmapTextureAtlasSource getAtlasBitmap(final int width, final int height, final Bitmap pBitmap) {

		final IBitmapTextureAtlasSource baseTexture = new EmptyBitmapTextureAtlasSource(width, height);
		final IBitmapTextureAtlasSource decoratedTexture = new BaseBitmapTextureAtlasSourceDecorator(baseTexture) {
			@Override
			protected void onDecorateBitmap(Canvas pCanvas) throws Exception {

				mPaint.setAntiAlias(true);
				mPaint.setDither(true);

				final BitmapShader b = new BitmapShader(pBitmap, TileMode.REPEAT, TileMode.REPEAT);
				mPaint.setShader(b);
				pCanvas.drawRect(0, 0, width, height, mPaint);
			}

			@Override
			public BaseBitmapTextureAtlasSourceDecorator deepCopy() {
				throw new DeepCopyNotSupportedException();
			}
		};
		return decoratedTexture;
	}

	public static Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

		int width = bm.getWidth();
		int height = bm.getHeight();
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

		return resizedBitmap;

	}
}
