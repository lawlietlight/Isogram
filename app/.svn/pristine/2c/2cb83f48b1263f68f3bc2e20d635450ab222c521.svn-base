/**
 * FetchImageFromServer.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.io.InputStream;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
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

/**
 * @author SATHISH
 *
 */
public class FetchImageFromServerTask extends AsyncTask<Rectangle, Void, TextureRegion> {

	private Exception exception;
	private Rectangle rect;
	TextureRegion MyImageFromWeb;
	public AndroidHttpClient client;

	protected TextureRegion doInBackground(Rectangle... rect) {
		try {
			this.rect=rect[0];
			String guessedBy= (String) this.rect.getUserData();

			if(null!=guessedBy && !guessedBy.isEmpty() ){
				//String s = null!=ResourcesManager.userProfiles.get(guessedBy)? ResourcesManager.userProfiles.get(guessedBy).getUserProfilePicUrl():"";
				//TextureRegion t = ResourcesManager.userProfilePicSprites.get(guessedBy);
				if(ResourcesManager.userProfilePicSprites.get(guessedBy)==null){
					UserDO user= ResourcesManager.userProfiles.get(guessedBy);
					String urlStringForReplace=user.getUserProfilePicUrl();
					if(null!=urlStringForReplace && !urlStringForReplace.isEmpty() && urlStringForReplace.indexOf("s96-ns")!=-1){
						urlStringForReplace=urlStringForReplace.replace("s96-ns", "s120-ns");
					}
					else if(null!=urlStringForReplace && !urlStringForReplace.isEmpty() && urlStringForReplace.indexOf("fbcdn-profile")!=-1){
					}
					else {
						//urlStringForReplace ="https://lh3.googleusercontent.com/-XdUIqdMkCWA/AAAAAAAAAAI/AAAAAAAAAAA/4252rscbv5M/photo.jpg?sz=120";
						urlStringForReplace ="http://i.imgur.com/SnpwGnF.png";
					}
					final String urlString = urlStringForReplace;
					
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
						ResourcesManager.userProfilePicSprites.put(guessedBy, MyImageFromWeb);
					}
					/*ITexture mTexture = new BitmapTexture(ResourcesManager.getInstance().getEngine().getTextureManager(), new IInputStreamOpener() {
						@Override
						public InputStream open() throws IOException {

							URL url = new URL(urlString);

							HttpURLConnection connection = (HttpURLConnection) url.openConnection();
							connection.setDoInput(true);
							connection.connect();
							InputStream input = connection.getInputStream();
							BufferedInputStream in = new BufferedInputStream(input);    
							return in;
						}
					});

					if(null!=mTexture){
						mTexture.load();
						MyImageFromWeb = TextureRegionFactory.extractFromTexture(mTexture);
						ResourcesManager.userProfilePicSprites.put(guessedBy, MyImageFromWeb);
					}*/
				}
				else{
					MyImageFromWeb = ResourcesManager.userProfilePicSprites.get(guessedBy);
				}
			}

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
	private void loadProfilePic(TextureRegion MyImageFromWeb,Rectangle innerRectFirstChunk, VertexBufferObjectManager pVertexBufferObjectManager) {

		if(null!=innerRectFirstChunk && null!=MyImageFromWeb){
			//MyImageFromWeb.setTextureHeight(innerRectFirstChunk.getHeight());
			//MyImageFromWeb.setTextureWidth(innerRectFirstChunk.getWidth());
			//innerRectFirstChunk.setAlpha(0);
			Sprite profileImage = new Sprite(innerRectFirstChunk.getWidthScaled()/2,innerRectFirstChunk.getHeightScaled()/2,MyImageFromWeb,pVertexBufferObjectManager);
			profileImage.setWidth(innerRectFirstChunk.getWidthScaled());
			profileImage.setHeight(innerRectFirstChunk.getHeightScaled());
			profileImage.setAnchorCenter(0.5f, 0.5f);
			//profileImage.setAlpha(0.9f);
			profileImage.registerEntityModifier(new AlphaModifier(1, 0.1f, 0.9f));
			profileImage.setZIndex(1);
			//profileImage.setScale(0.9f);
			profileImage.setBlendFunctionDestination(GLES20.GL_ALPHA);
			profileImage.setTag(EntityTagManager.profileImage);
			//profileImage.setScale(ResourcesManager.resourceScaler);
			innerRectFirstChunk.attachChild(profileImage);
			innerRectFirstChunk.sortChildren();
			innerRectFirstChunk.setAlpha(0f);
			profileImage.setAlpha(0.9f);
		}
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(TextureRegion result) {
		loadProfilePic(result, rect, ResourcesManager.getInstance().vbom);
		if(null!=client){
			client.close();
		}
	}
	/*


	public void loadImageFromServer() {

	    final IAsyncCallback callback = new IAsyncCallback() {

	        @Override
	        public void workToDo() {
	            Log.e("BaseActivity", "OnWhat to do working");
	            if (isInternetOn()) {
	                try {
	                    ITexture mTexture = new BitmapTexture(
	                            mEngine.getTextureManager(),
	                            new IInputStreamOpener() {
	                                @Override
	                                public InputStream open()
	                                        throws IOException {

	                                    URL url = new URL(
	                                            "http://tenlogix.com/cupcakemania/Ads/burgermaker.png");

	                                    HttpURLConnection connection = (HttpURLConnection) url
	                                            .openConnection();
	                                    connection.setDoInput(true);
	                                    connection.connect();
	                                    InputStream input = connection
	                                            .getInputStream();
	                                    BufferedInputStream in = new BufferedInputStream(
	                                            input);
	                                    return in;
	                                }
	                            });
	                    if (mTexture != null) {
	                        mTexture.load();

	                        MyImageFromWeb = TextureRegionFactory
	                                .extractFromTexture(mTexture);
	                    }

	                } catch (IOException e) {
	                    Log.d("TenlogixAds", " " + e);
	                }

	            } else {
	                Log.d("TenlogixAds", " No Internet Connection Detected.. ");
	                AdSprite = null;
	            }

	        }

	        @Override
	        public void onComplete() {
	            // can create after Initializing MyImageFromWeb

	            if (MyImageFromWeb != null) {

	                AdSprite = new Sprite(0, 0, MyImageFromWeb,
	                		ResourcesManager.getInstance().vbom);
	            }

	        }

	    };

	    ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	            new AsyncTaskLoader().execute(callback);
	        }
	    });

	}*/

}
