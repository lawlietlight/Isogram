package com.efficientsciences.cowsandbulls.wordwars.scenes;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.ease.EaseElasticIn;
import org.andengine.util.modifier.ease.EaseExponentialIn;
import org.andengine.util.modifier.ease.EaseQuadIn;
import org.andengine.util.modifier.ease.EaseQuadOut;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.domain.ThemePreference;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;

public class OptionScene extends BaseScene {

	public Sprite thumbnailOne;
	public Sprite thumbnailTwo;
	public Sprite howToPlay;
	public TiledSprite musicIcon;
	public  TiledSprite tutorialSwitch;
	public Rectangle rect;
	@Override
	public void createScene() {
		createBackground();
		final ThemePreference themeSelectionObj= ThemeManager.getInstance().getThemePreferences();
		ThemeManager.getInstance().themeSelection.setScale(ResourcesManager.resourceScaler);
		ThemeManager.getInstance().musicOnOffText.setScale(ResourcesManager.resourceScaler);
		ThemeManager.getInstance().tutorialOnOffText.setScale(ResourcesManager.resourceScaler);

		ThemeManager.getInstance().startUpAnimationOnOffText.setScale(ResourcesManager.resourceScaler*0.65f);


		thumbnailOne= new Sprite(ThemeManager.getInstance().themeSelection.getWidth()*ResourcesManager.resourceScaler+ThemeManager.getInstance().themeSelection.getX()+50*ResourcesManager.resourceScaler,
				windowDimensions.y-ThemeManager.getInstance().themeThumbnailOneCreative.getHeight()*ResourcesManager.resourceScaler-20*ResourcesManager.resourceScaler, ThemeManager.getInstance().themeThumbnailOneCreative, vbom){
			/*
			 * returns true if the touch event is handled here
			 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
			 */
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					Log.e("Option Scene", "BlueMagic ChildOneClicked");
					themeSelectionObj.setThemeSelection(THEMES.BLUEMAGIC.toString());
					Runnable run = new Runnable() {
						@Override
						public void run() {
							if(Engine.OUTOFMEMORYOCCURED){
								themeSelectionObj.setThemeSelection(THEMES.YELLOWFANTASY.toString());
								ToastHelper.makeCustomToast("Your Device Doesn't Support This Theme", Toast.LENGTH_LONG);
							}
							else{
								StorageManager.getInstance().saveThemePreferences(themeSelectionObj);
								ToastHelper.makeCustomToast("Colours Of Joy theme selected", Toast.LENGTH_LONG);
							}
						}
					};
					resourcesManager.activity.runOnUiThread(run);
					return true;
				}
				return false;

			}

		};
		thumbnailOne.setScale(ResourcesManager.resourceScaler*0.75f);


		thumbnailTwo= new Sprite(thumbnailOne.getX()+thumbnailOne.getWidth()*ResourcesManager.resourceScaler,
				windowDimensions.y-ThemeManager.getInstance().themeThumbnailOneCreative.getHeight()*ResourcesManager.resourceScaler-20*ResourcesManager.resourceScaler,  ThemeManager.getInstance().themeThumbnailTwoYellow, vbom){
			/*
			 * returns true if the touch event is handled here
			 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
			 */
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					Log.e("Option Scene", "YellowFantasy ChildTwo Clicked");

					themeSelectionObj.setThemeSelection(THEMES.YELLOWFANTASY.toString());
					Runnable run = new Runnable() {
						@Override
						public void run() {
							StorageManager.getInstance().saveThemePreferences(themeSelectionObj);
							ToastHelper.makeCustomToast( "Yellow Fantasy theme selected", Toast.LENGTH_LONG);

						}
					};
					resourcesManager.activity.runOnUiThread(run);					
					return true;
				}
				return false;
			}

		};
		thumbnailTwo.setScale(ResourcesManager.resourceScaler*0.75f);

		ThemeManager.getInstance().themeSelection.setY(windowDimensions.y-((ThemeManager.getInstance().themeThumbnailOneCreative.getHeight()*ResourcesManager.resourceScaler)/2));
		ThemeManager.getInstance().themeSelection.setAnchorCenterY(1f);
		musicIcon = new TiledSprite(ThemeManager.getInstance().themeSelection.getWidth()*ResourcesManager.resourceScaler+ThemeManager.getInstance().themeSelection.getX()+70*ResourcesManager.resourceScaler, thumbnailOne.getY()-50*ResourcesManager.resourceScaler, resourcesManager.musicOnOff, vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.e("Option Scene", "musicIcon Clicked");
				if(pSceneTouchEvent.isActionUp()){
					if(null!=StorageManager.getInstance().getUser()){
						Log.e("Option Scene", "user not null music icon");
						if(!StorageManager.getInstance().getUser().isMusicMuted()){
							StorageManager.getInstance().getUser().setMusicMuted(true);
							MusicService.pauseMusic();
						}
						else{
							StorageManager.getInstance().getUser().setMusicMuted(false);
							MusicService.resumeMusic();
						}
						StorageManager.getInstance().saveUser(StorageManager.getInstance().getUser());
					}
					return true;
				}
				return false;
			}
		};
		//musicIcon.setZIndex(999);
		musicIcon.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (null!=StorageManager.getInstance().getUser() && StorageManager.getInstance().getUser().isMusicMuted()) {
					musicIcon.setCurrentTileIndex(0);
				} else {
					musicIcon.setCurrentTileIndex(1);
				}
			}

			@Override
			public void reset() { }
		});

		
		tutorialSwitch = new TiledSprite(ThemeManager.getInstance().themeSelection.getWidth()*ResourcesManager.resourceScaler+ThemeManager.getInstance().themeSelection.getX()+70*ResourcesManager.resourceScaler, musicIcon.getY()-100*ResourcesManager.resourceScaler, resourcesManager.tutorialOnOff, vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Log.e("Option Scene", "tutorial On Off Clicked");
				if(pSceneTouchEvent.isActionUp()){
					if(null!=StorageManager.getInstance().getUser()){
						Log.e("Option Scene", "user not null music icon");
						if(!StorageManager.getInstance().getUser().isSoundMuted()){
							StorageManager.getInstance().getUser().setSoundMuted(true);
						}
						else{
							StorageManager.getInstance().getUser().setSoundMuted(false);
						}
						StorageManager.getInstance().saveUser(StorageManager.getInstance().getUser());
					}
					return true;
				}
				return false;
			}
		};
		//musicIcon.setZIndex(999);
		tutorialSwitch.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (null!=StorageManager.getInstance().getUser() && StorageManager.getInstance().getUser().isSoundMuted()) {
					tutorialSwitch.setCurrentTileIndex(0);
				} else {
					tutorialSwitch.setCurrentTileIndex(1);
				}
			}

			@Override
			public void reset() { }
		});

		ThemeManager.getInstance().musicOnOffText.setY(musicIcon.getY()-10*resourcesManager.resourceScaler);
		ThemeManager.getInstance().musicOnOffText.setAnchorCenterY(0.7f);
		
		ThemeManager.getInstance().tutorialOnOffText.setY(tutorialSwitch.getY()-25*resourcesManager.resourceScaler);
		ThemeManager.getInstance().tutorialOnOffText.setAnchorCenterY(0.7f);
		thumbnailOne.setAnchorCenter(0,0f);
		thumbnailTwo.setAnchorCenter(0,0f);
		musicIcon.setAlpha(.7f);
		musicIcon.setScale(0.5f*ResourcesManager.resourceScaler);
		musicIcon.setAnchorCenter(0f, 0.9f);

		tutorialSwitch.setAlpha(.8f);
		tutorialSwitch.setScale(1f*ResourcesManager.resourceScaler);
		tutorialSwitch.setAnchorCenter(0f, 1f);

		
		ThemeManager.getInstance().startUpAnimationOnOffText.setY(tutorialSwitch.getY()-tutorialSwitch.getHeight()*ResourcesManager.resourceScaler-50*ResourcesManager.resourceScaler);

		howToPlay= new Sprite(windowDimensions.x,
				ThemeManager.getInstance().startUpAnimationOnOffText.getY(),  ThemeManager.getInstance().howToPlay, vbom){
			/*
			 * returns true if the touch event is handled here
			 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
			 */
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					Log.e("Option Scene", "Credits Clicked");
					SceneManager.getInstance().createCreditsScene();
					return true;
				}
				return false;
			}

		};
		howToPlay.setAnchorCenter(1f, 0.5f);
		howToPlay.setColor(0.12f,0.1f,0.05f);
		howToPlay.setAlpha(0.9f);
		howToPlay.setScale(ResourcesManager.resourceScaler*0.65f);

		this.registerTouchArea(musicIcon);
		this.registerTouchArea(tutorialSwitch);
		this.registerTouchArea(thumbnailOne);
		this.registerTouchArea(thumbnailTwo);
		this.registerTouchArea(howToPlay);
		this.registerTouchArea(ThemeManager.getInstance().startUpAnimationOnOffText);
		rect = new Rectangle(windowDimensions.x*0.5f, windowDimensions.y*0.5f, windowDimensions.x-20, windowDimensions.y-20, vbom);
		rect.setColor(120/255f, 130/255f, 130/255f);
		rect.registerEntityModifier((new ParallelEntityModifier(new AlphaModifier(.2f, 0.2f, 0.4f), new MoveXModifier(.3f, 0, windowDimensions.x*0.5f,EaseQuadIn.getInstance()))));


		rect.attachChild(ThemeManager.getInstance().themeSelection);
		rect.attachChild(thumbnailOne);
		rect.attachChild(thumbnailTwo);
		rect.attachChild(howToPlay);

		rect.attachChild(ThemeManager.getInstance().musicOnOffText);
		rect.attachChild(musicIcon);

		rect.attachChild(ThemeManager.getInstance().tutorialOnOffText);
		rect.attachChild(tutorialSwitch);
		
		rect.attachChild(ThemeManager.getInstance().startUpAnimationOnOffText);
		this.attachChild(rect);

		if(!backButtonDisplayed){
			attachBackButton();
		}
		if(null!=backButton){
			float xOffset=backButton.getX()+backButton.getWidthScaled()+40*ResourcesManager.resourceScaler;
			ThemeManager.getInstance().startUpAnimationOnOffText.setX(xOffset);
		}
	}

	public Sprite attachBackButton(){
		backButton = new Sprite(20*ResourcesManager.resourceScaler, 10 * ResourcesManager.resourceScaler, ResourcesManager.chocobackbuttonregion, vbom) {
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
		backButtonDisplayed = true;

		if(!backButton.hasParent()){
			this.attachChild(backButton);
		}
		this.registerTouchArea(backButton);
		return backButton;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_OPTION;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}


	public void createBackground() {
		Sprite s = new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.menu_background_region, vbom){

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		s.setSize(windowDimensions.x, windowDimensions.y);
		attachChild(s);

	}

	@Override
	public void onBackKeyPressed() {
		rect.registerEntityModifier((new ParallelEntityModifier(new AlphaModifier(.2f, 0.4f, 0.2f), new MoveXModifier(.3f, windowDimensions.x*0.5f, windowDimensions.x,EaseQuadOut.getInstance()))));
		//rect.detachSelf();
		ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				/* Now it is save to remove the entity! */
				SceneManager.getInstance().optionGameScene.detachChild(rect);
			}
		});
		SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
		Debug.e("Option Scene Back Key Pressed");
		this.back();
	}

}
