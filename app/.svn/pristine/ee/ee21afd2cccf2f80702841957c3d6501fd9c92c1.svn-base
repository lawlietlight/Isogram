package com.efficientsciences.cowsandbulls.wordwars.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.View;

import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.TimerManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;

public abstract class BaseScene extends Scene {
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	protected Engine engine;
	protected Activity activity;
	protected ResourcesManager resourcesManager;
	protected ThemeManager thememanager;
	protected TimerManager timerManager;
	public VertexBufferObjectManager vbom;
	protected Camera camera;
	protected Point windowDimensions;
	protected float centerX;
	protected float centerY;
	protected boolean backButtonDisplayed = false;
	protected Sprite backButton = null;
	Runnable dimSoftButtons = new Runnable() {

		@Override
		public void run() {
			dimSoftButtonsIfPossible();
		}
	};
	
	//---------------------------------------------
	// CONSTRUCTOR
	//---------------------------------------------

	public BaseScene()
	{
		this.resourcesManager = ResourcesManager.getInstance();
		this.thememanager=ThemeManager.getInstance();
		this.timerManager=TimerManager.getInstance();
		this.engine = resourcesManager.getEngine();
		this.activity = resourcesManager.activity;
		this.vbom = resourcesManager.vbom;
		this.camera = resourcesManager.camera;
		this.windowDimensions=resourcesManager.windowDimensions;
		this.centerX= this.windowDimensions.x/2;
		this.centerY= this.windowDimensions.y/2;


		createScene();

	}

	//---------------------------------------------
	// ABSTRACTION
	//---------------------------------------------

	public abstract void createScene();

	public abstract void onBackKeyPressed();

	public abstract SceneType getSceneType();

	public abstract void disposeScene();

	/*@Override
	protected synchronized void onManagedUpdate(float pSecondsElapsed) {
		synchronized (this) {
			activity.runOnUiThread(dimSoftButtons);
			super.onManagedUpdate(pSecondsElapsed);
		}
	}

	@Override
	protected synchronized void onManagedDraw(GLState pGLState, Camera pCamera) {
		synchronized (this) {
			super.onManagedDraw(pGLState, pCamera);
		}
	}*/
	@Override
	protected  void onManagedUpdate(float pSecondsElapsed) {
			activity.runOnUiThread(dimSoftButtons);
			super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	protected void onManagedDraw(GLState pGLState, Camera pCamera) {
			super.onManagedDraw(pGLState, pCamera);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	protected void dimSoftButtonsIfPossible() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}

	protected Sprite attachBackButton(){
		backButton = new Sprite(5*ResourcesManager.resourceScaler, 5 * ResourcesManager.resourceScaler, ResourcesManager.chocobackbuttonregion, vbom) {
			boolean areaClickedDown;
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					areaClickedDown =true;
					return true;
				}
				else if((pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionMove()) && areaClickedDown){
					Debug.e("How To Play Scene Back Key Pressed");
					areaClickedDown =false;
					SceneManager.getInstance().getCurrentScene().onBackKeyPressed();
					return true;
				}
				return false;
			}
		};
		backButton.setAnchorCenter(0, 0);
		backButton.setScale(0.7f * ResourcesManager.resourceScaler);
		backButton.setZIndex(10);
		backButton.setTag(EntityTagManager.backButton);
		backButtonDisplayed = true;

		if(!backButton.hasParent()){
			this.attachChild(backButton);
		}
		this.registerTouchArea(backButton);
		return backButton;
	}

}
