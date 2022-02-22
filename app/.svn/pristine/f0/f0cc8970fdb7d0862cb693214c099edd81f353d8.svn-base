package com.efficientsciences.cowsandbulls.wordwars.graphics.minigame;

import java.io.IOException;

import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.engine.options.resolutionpolicy.IResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;

import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.bullrun.MiniGameSceneBull;
import com.efficientsciences.cowsandbulls.wordwars.managers.Constants;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager.MusicPlayed;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;

public class EarnCoinsMiniGame extends BaseGameActivity {

	private Camera camera;
	
	private BaseMiniGameScene miniGameScene;

	
	SharedPreferences prefs;

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		Engine engine = new LimitedFPSEngine(pEngineOptions, Constants.FPS_LIMIT);
		return engine;
	}

	public void setHighScore(int score) {
    	SharedPreferences.Editor settingsEditor = prefs.edit();
    	settingsEditor.putInt(Constants.KEY_HISCORE, score);
    	settingsEditor.commit();
	}
	
	public int getHighScore() {
		return prefs.getInt(Constants.KEY_HISCORE, 0);		
	}	
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		camera = new FollowCamera(0, 0, Constants.CW, Constants.CH);
		IResolutionPolicy resolutionPolicy = new FillResolutionPolicy();
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_SENSOR, resolutionPolicy, camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		ResourceManagerForMiniGame.getInstance().create(this, getEngine(), camera, getVertexBufferObjectManager());
		ResourceManagerForMiniGame.getInstance().loadFont();
		ResourceManagerForMiniGame.getInstance().loadGameResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {
		if(ResourceManagerForMiniGame.isBeeFly){
		miniGameScene = new MiniGameScene();
		}
		else{
			miniGameScene = new MiniGameSceneBull();
		}
		pOnCreateSceneCallback.onCreateSceneFinished(miniGameScene);

	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback)
			throws IOException {
		pScene.reset();
		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
		miniGameScene.pause();
		if(!ResourcesManager.getInstance().showing && null!= MusicService.mPlayer && MusicService.mPlayer.isPlaying()){
			MusicService.pauseMusic();
		}
	}

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
		miniGameScene.resume();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dimSoftButtonsIfPossible();
			}
		});
		/*if(null!=SoundManager.getInstance().mServ)
			SoundManager.getInstance().mServ.resumeMusic();	
		else */
		if(null!= MusicService.mPlayer && !MusicService.mPlayer.isPlaying()){
			SoundManager.changeMusic(MusicPlayed.FASTMARCH);
		}
		
	}
	/*public void gotoPlayStore() {
		try {
			Intent i = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("market://details?id=" + getString(R.string.google_play_app_id)));
			ResourceManagerForMiniGame.getInstance().activity.startActivity(i);

		} catch (Exception ex) {
			Debug.w("Google Play Store not installed");
			Intent i = new Intent(Intent.ACTION_VIEW, 
					Uri.parse("http://play.google.com/store/apps/details?id=com.efficientsciences.cowsandbulls.wordwars"));
			ResourceManagerForMiniGame.getInstance().activity.startActivity(i);
		}
		
	}*/
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void dimSoftButtonsIfPossible() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		} 
	}
	
}
