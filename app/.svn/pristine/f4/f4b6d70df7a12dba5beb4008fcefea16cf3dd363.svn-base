/**
 * FetchImageFromServer.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.io.InputStream;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.opengl.GLES20;
import android.os.AsyncTask;
import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.graphics.BitmapTextureAtlasSource;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;

/**
 * @author SATHISH
 *
 */
public class FetchAdsImageFromServerTask extends AsyncTask<RoomProperties, Void, TextureRegion> {

	private Exception exception;
	private RoomProperties room;
	TextureRegion MyImageFromWeb;
	public AndroidHttpClient client;
	
	protected TextureRegion doInBackground(RoomProperties... rooms) {
		try {
			this.room=rooms[0];

			final String urlString=this.room.getAdsDetail().getImageUrl();
			//URL url = new URL(urlString);
			Bitmap pBitmap= downloadBitmap(urlString);
			// Usage:
			BitmapTextureAtlasSource source = new BitmapTextureAtlasSource(pBitmap);
			BitmapTextureAtlas mTexture = new BitmapTextureAtlas(ResourcesManager.getInstance().activity.getTextureManager(), 160, 160);
			mTexture.addTextureAtlasSource(source, 0, 0);
			
			if(null!=mTexture){
				/*mTexture.load();
				MyImageFromWeb = TextureRegionFactory.extractFromTexture(mTexture);*/
				
				mTexture.load();
				MyImageFromWeb = (TextureRegion) TextureRegionFactory.createFromSource(mTexture, source, 0, 0);
			}
			loadProfilePic(MyImageFromWeb, room, ResourcesManager.getInstance().vbom);
			return MyImageFromWeb;
		} catch (Exception e) {
			this.exception = e;
			return null;
		}
	}
	
	Bitmap downloadBitmap(String url) {
		final HttpGet getRequest = new HttpGet(url);
		try {
			client = AndroidHttpClient.newInstance("Android");
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}

	/**
	 * @param pVertexBufferObjectManager 
	 * @param innerRects2 
	 * 
	 */
	private void loadProfilePic(final TextureRegion MyImageFromWeb,final RoomProperties roomSelected, final VertexBufferObjectManager pVertexBufferObjectManager) {
		if(null!=roomSelected && null!=MyImageFromWeb){
			//MyImageFromWeb.setTextureHeight(innerRectFirstChunk.getHeight());
			//MyImageFromWeb.setTextureWidth(innerRectFirstChunk.getWidth());
			//innerRectFirstChunk.setAlpha(0);

			final Sprite profileImage = new Sprite(roomSelected.getWidth()- ((ResourcesManager.roomJoinButtonOffset * ResourcesManager.resourceScaler)/2) ,roomSelected.getHeight()/2,MyImageFromWeb,pVertexBufferObjectManager){
				/* (non-Javadoc)
				 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
				 */
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					Runnable run = new Runnable() {
						@Override
						public void run() {
							roomSelected.getAdsDetail().sendClick(ResourcesManager.getInstance().activity);
						}
					};
					ResourcesManager.getInstance().activity.runOnUiThread(run);

					return true;
				}
			};
			Runnable runAttacherForUpdate = new Runnable() {
				@Override
				public void run() {
					profileImage.setWidth((ResourcesManager.roomJoinButtonOffset * ResourcesManager.resourceScaler) - 10);
					profileImage.setHeight(roomSelected.getHeight() - 10);
					profileImage.setAnchorCenter(0.5f, 0.5f);
					//profileImage.setAlpha(0.9f);
					profileImage.registerEntityModifier(new AlphaModifier(1, 0.1f, 0.9f));
					profileImage.setZIndex(5);
					profileImage.setTag(EntityTagManager.ADSIMAGE);
					profileImage.setBlendFunctionDestination(GLES20.GL_ALPHA);
					if(SceneManager.getInstance().getCurrentSceneType().equals(SceneType.SCENE_ROOMS) ) 
						SceneManager.getInstance().getCurrentScene().registerTouchArea(profileImage);
					//profileImage.setScale(ResourcesManager.resourceScaler);

					roomSelected.attachChild(profileImage);

				}
			};
			ResourcesManager.getInstance().activity.runOnUpdateThread(runAttacherForUpdate);
			Runnable run = new Runnable() {
				@Override
				public void run() {
					roomSelected.getAdsDetail().sendImpression(ResourcesManager.getInstance().activity);
				}
			};
			ResourcesManager.getInstance().activity.runOnUiThread(run);
			roomSelected.sortChildren();
			//roomSelected.setAlpha(0.8f);
		}
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(TextureRegion result) {

		//loadProfilePic(result, room, ResourcesManager.getInstance().vbom);
		if(null!=client){
			client.close();
		}
	}
	
}





	

