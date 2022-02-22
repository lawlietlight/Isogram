package com.efficientsciences.cowsandbulls.wordwars.managers;

import java.util.Calendar;

import org.andengine.engine.Engine;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.HowToPlayImageActivity;
import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.EarnCoinsMiniGame;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.scenes.BaseScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.CreditsScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.GameScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.GuessScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.HostScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.HowToPlayScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.LoadingScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.MainMenuScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.OptionScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.ScrollableRoomsScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.ShopBannersScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.WaitingScene;
//import com.startapp.android.publish.StartAppAd;

public class SceneManager {

	//---------------------------------------------
	// SCENES
	//---------------------------------------------

	public BaseScene splashScene;
	public BaseScene menuScene;
	public BaseScene optionGameScene;
	public BaseScene loadingScene;
	public BaseScene hostGameScene;
	public BaseScene guessGameScene;
	public BaseScene hostGuessGameScene;
	public BaseScene clientGameScene;
	public BaseScene howToPlayScene;
	public BaseScene scrollableRoomScene;
	public BaseScene waitingScene;
	public BaseScene creditsScene;
	public BaseScene shopBannerScene;
	//---------------------------------------------
	// VARIABLES
	//---------------------------------------------

	private static final SceneManager INSTANCE = new SceneManager();


	private SceneType currentSceneType = SceneType.SCENE_SPLASH;

	private BaseScene currentScene;

	private Engine engine = ResourcesManager.getInstance().getEngine();



	public enum SceneType
	{
		SCENE_SPLASH,
		SCENE_MENU,
		SCENE_GAME,
		SCENE_HOST,
		SCENE_GUESS,
		SCENE_CLIENT,
		SCENE_LOADING,
		SCENE_OPTION,
		SCENE_HOWTOPLAY, 
		SCENE_ROOMS,
		SCENE_WAITING,
		SCENE_CREDITS,
		SCENE_BANNERS
	}

	public static SceneManager getInstance() {
		return INSTANCE;
	}

	public void setScene(BaseScene scene) {
		engine.setScene(scene);
		currentScene=scene;
		currentSceneType=currentScene.getSceneType();
	}

	public void setScene(SceneType sceneType)
	{
		switch (sceneType)
		{
		case SCENE_MENU:
			ResourcesManager.getInstance().activity.loadLeadBoltAd();
			setScene(menuScene);
			break;
		case SCENE_CLIENT:
			setScene(clientGameScene);
			break;
		case SCENE_GAME:
			setScene(hostGuessGameScene);
			break;
		case SCENE_HOST:
			setScene(hostGameScene);
			break;
		case SCENE_GUESS:
			setScene(guessGameScene);
			break;
		case SCENE_SPLASH:
			setScene(splashScene);
			break;
		case SCENE_LOADING:
			setScene(loadingScene);
			break;
		case SCENE_OPTION:
			setScene(optionGameScene);
			break;
		case SCENE_HOWTOPLAY:
			setScene(howToPlayScene);
			break;
		case SCENE_ROOMS:
			setScene(scrollableRoomScene);
			break;
		case SCENE_WAITING:
			setScene(waitingScene);
			break;
		case SCENE_CREDITS:
			setScene(creditsScene);
			break;
		case SCENE_BANNERS:
			setScene(shopBannerScene);
			break;
		default:
			break;
		}
	}

	public SceneType getCurrentSceneType() {
		return currentSceneType;
	}

	public BaseScene getCurrentScene() {
		return currentScene;
	}
	
	public BaseScene createSplashScene()
	{
	    ResourcesManager.getInstance().loadSplashScreen();
	    splashScene = new LoadingScene();
	    while(null==engine || null==splashScene){
			Log.e("Word Engine Not Initialised", "Word Game Engine Not Initialised" + Calendar.getInstance().getTimeInMillis());
		}
	    setScene(splashScene);
	    
	    return splashScene;
	}
	
	public void disposeSplashScene()
	{
	    ResourcesManager.getInstance().unloadSplashScreen();
	    splashScene.disposeScene();
	    splashScene = null;
	}
	
	public void disposeWaitingScene()
	{
	    waitingScene.disposeScene();
	}


	public BaseScene createMenuScene()
	{
		
		ResourcesManager.getInstance().loadHostGuessGameSceneResources(); // movedcode
		ResourcesManager.getInstance().loadWaitingRoomResources(); //Moved Code
		
		ResourcesManager.getInstance().loadMenuResources();
		menuScene = new MainMenuScene();
		setScene(menuScene);
		//disposeSplashScene();
		return menuScene;
		
	}


	public BaseScene createHostScene(){
		ResourcesManager.getInstance().loadHostResources();

		hostGameScene =  new HostScene();

		setScene(hostGameScene);
		ResourcesManager.getInstance().clearAndUnloadRooms();
		return hostGameScene;
	}
	
	public BaseScene createGuessScene(){
		ResourcesManager.getInstance().loadGuessResources();

		guessGameScene =  new GuessScene();

		setScene(guessGameScene);
		ResourcesManager.getInstance().clearAndUnloadRooms();
		return guessGameScene;
	}
	
	public BaseScene createHostGuessGameScene(){

		hostGuessGameScene =  new GameScene();

		setScene(hostGuessGameScene);
		return hostGuessGameScene;
	}

	public BaseScene createOptionScene() {
		ResourcesManager.getInstance().loadOptionResources();

		optionGameScene =  new OptionScene();

		setScene(optionGameScene);
		return optionGameScene;
		
	}
	
	public BaseScene createHowToPlayScene() {
		ResourcesManager.getInstance().loadHowToPlayScene();
		howToPlayScene =  new HowToPlayScene();
		setScene(howToPlayScene);
		return howToPlayScene;
	}
	
	public BaseScene createScrollableRoomsScene() {
		ResourcesManager.getInstance().loadScrollableRoomsScene();
		scrollableRoomScene =  new ScrollableRoomsScene();
		setScene(scrollableRoomScene);
		return scrollableRoomScene;
	}
	
	public BaseScene createShopBannersScene() {
		ResourcesManager.getInstance().loadShopBannerScene();
		shopBannerScene =  new ShopBannersScene();
		setScene(shopBannerScene);
		return shopBannerScene;
	}

	/**
	 * 
	 */
	public BaseScene createWaitingScene() {
		if(ResourcesManager.getInstance().users!=null){
			ResourcesManager.getInstance().users.clear();
		}
		waitingScene =  new WaitingScene();
		setScene(waitingScene);
		return waitingScene;
	}

	/**
	 * @return 
	 * 
	 */
	public BaseScene createCreditsScene() {
		ResourcesManager.getInstance().loadCreditsScene();
		creditsScene =  new CreditsScene();
		setScene(creditsScene);
		return creditsScene;
	}
	
	public void earnCoinsMiniGameScene() {
		Intent earnCoinsMiniGameIntent = new Intent(ResourcesManager.getInstance().activity, EarnCoinsMiniGame.class);

		try {
			ResourcesManager.getInstance().activity.startActivity(earnCoinsMiniGameIntent);
		} catch (Exception ex) {
			ToastHelper.makeCustomToast("MiniGame mode Not Successful.",Toast.LENGTH_SHORT);
		}
	}
	
	public void howToPlayImageScene() {
		Intent howToPlayImageIntent = new Intent(ResourcesManager.getInstance().activity, HowToPlayImageActivity.class);

		try {
			ResourcesManager.getInstance().activity.startActivity(howToPlayImageIntent);
		} catch (Exception ex) {
			ToastHelper.makeCustomToast("howToPlayImage Activity Launch Not Successful.",Toast.LENGTH_SHORT);
		}
	}

}
