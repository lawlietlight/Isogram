/**
 * PauseScene.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.scenes;

import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseActivity;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;

import android.util.Log;

/**
 * @author SATHISH
 *
 */
public class PauseScene{
	CameraScene mPauseScene;
	boolean isPaused;
	private void initPauseScene() {
		this.mPauseScene = new CameraScene(ResourcesManager.getInstance().camera);


	/*	final Sprite pausedSprite = createPauseSprite(ResourcesManager.getInstance().windowDimensions.x/2, ResourcesManager.getInstance().windowDimensions.y/2, BaseActivity.mPauseButton);
		this.mPauseScene.registerTouchArea(pausedSprite);
		this.mPauseScene.attachChild(pausedSprite);*/

		/* Makes the paused Game look through. */
		this.mPauseScene.setBackgroundEnabled(false);
	}

	/**
	 * Creates a Sprite that manages Pausing
	 * @param pX X Position to be created at
	 * @param pY Y Position to be created at
	 * @return
	 */
	private Sprite createPauseSprite(float pX, float pY, TextureRegion button) {
		final Sprite pauseButton = new Sprite(pX, pY,
				button,
				ResourcesManager.getInstance().vbom) {

			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {				
				// Toggle pause or not				
				switch(pSceneTouchEvent.getAction()) {
				case TouchEvent.ACTION_DOWN:
					if(isPaused) {
						isPaused = false;
						SceneManager.getInstance().getCurrentScene().clearChildScene();
						SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
					}
					else {
						isPaused = true;
						SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(true);
						SceneManager.getInstance().getCurrentScene().setChildScene(mPauseScene, false, true, true);
						Log.d("paused", "done");
					}
					break;
				case TouchEvent.ACTION_MOVE:
					break;
				case TouchEvent.ACTION_UP:
					break;
				}
				return true;
			}
		};
		return pauseButton;
	}
}
