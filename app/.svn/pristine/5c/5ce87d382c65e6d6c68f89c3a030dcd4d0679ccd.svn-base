package com.efficientsciences.cowsandbulls.wordwars.scenes;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Shader.TileMode;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.chat.WidgetsWindow;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunkFactory;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomProperties;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunkFactory;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ParticleSwipeCreator;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ChatManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.FacebookManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager.MusicPlayed;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;
import com.efficientsciences.cowsandbulls.wordwars.managers.TimerManager;
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnectorPubSub;
import com.efficientsciences.cowsandbulls.wordwars.textureMap.Askhelp;
import com.efficientsciences.cowsandbulls.wordwars.textureMap.Chat;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.ScreenGrabber;
import org.andengine.entity.util.ScreenGrabber.IScreenGrabberCallback;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.source.EmptyBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.decorator.BaseBitmapTextureAtlasSourceDecorator;
import org.andengine.opengl.util.GLState;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException;
import org.andengine.util.modifier.ease.EaseStrongIn;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import wei.mark.standout.StandOutWindow;

public class WaitingScene extends BaseScene {

	public static final String FILEDIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Word";
	public short  minimumNumOfPlayers= 2;
	private Scene thisScene;

	//Room Objects WaitimeElapsed will be ideally 0 for room that is available for hosting or Hosting in progress and will be negative For Not Available for guessers after hosting
	public static int waitTimeElapsed;
	private static int WAITTIME=31;
	private String PREFIX;
	private boolean reclaimInProgress;
	private int timeoutInMinutes = 10;
	private boolean userChunkAdded; 
	public static String nameOfImageShared= "WordGameSharedToWhatsAppImage.png";


	// ===========================================================
	// Fields
	// ===========================================================

	@Override
	public void createScene() {
		if(ResourcesManager.getInstance().hostImageclicked && null!=ResourcesManager.toast){
			ResourcesManager.toast.cancel();
		}
		WAITTIME=31;
		waitTimeElapsed=0;

		ResourcesManager.getInstance().waitTimeString= WAITTIME+"";
		this.thisScene= this;
		ResourcesManager.getInstance().waitTimeString = "";
		timerManager.waitingRoomTimer= new TimerHandler(1.0f, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				waitTimeElapsed++;
				if(waitTimeElapsed>WAITTIME){
					if(!reclaimInProgress){
						ResourcesManager.getInstance().waitTimeString="Play Time";
						final RoomProperties roomSelected = ResourcesManager.rooms.get(StorageManager.getInstance().getUser().getHostedGuessedRoomIndex());
						if(!(roomSelected.getNoOfParticipants()<minimumNumOfPlayers)){
							thisScene.unregisterUpdateHandler(pTimerHandler);
						}
					}
				}
				else{
					int remainingTime = WAITTIME-waitTimeElapsed;
					ResourcesManager.getInstance().waitTimeString = remainingTime+"";
					if(waitTimeElapsed > 10 && !userChunkAdded && ConnectionManager.isNormalFlow()){
						ToastHelper.makeCustomToastOnUIDefinedColor("***OOPS, Joining Room is Taking Longer Than Usual, Please Try Another Room***", Toast.LENGTH_LONG);
						onBackKeyPressed();
					}
				}

			}
		});
		this.registerUpdateHandler(timerManager.waitingRoomTimer);
		if(ResourcesManager.getInstance().hostImageclicked){
			PREFIX="Successfully Hosted Word \""+ConnectionManager.sentText+"\" \n";
		}
		else{
			PREFIX="";
		}
		/*if(ThemeManager.getInstance().themesKey.equals(THEMES.BLUEMAGIC.toString())){
			createBackground();
		}*/
		//Set Tag(name) for Entire scene object as 12345 and give  Red yellow color, divide RGB values by 255 for andengine color system
		this.setTag(EntityTagManager.waitingScene);
		this.getBackground().setColor(213/255f, 59/255f, 59/255f);
		final RoomProperties roomSelected = ResourcesManager.rooms.get(StorageManager.getInstance().getUser().getHostedGuessedRoomIndex());
		if(null!=roomSelected){
			minimumNumOfPlayers=(short)((roomSelected.getRoomSize().getValue()/3)+1);
		}
		ResourcesManager.getInstance().waitingText=new Text(ResourcesManager.getInstance().windowDimensions.x*0.5f,(ResourcesManager.getInstance().windowDimensions.y*0.5f)+100, ResourcesManager.getInstance().mBitmapFontForGame, ResourcesManager.getInstance().waitTextString,300,ResourcesManager.getInstance().vbom){
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if( null!=roomSelected && ((!ResourcesManager.getInstance().waitTimeString.equals("Play Time") && roomSelected.getRoomSize().getValue()>=roomSelected.getNoOfParticipants()) || (ResourcesManager.getInstance().waitTimeString.equals("Play Time")  && roomSelected.getNoOfParticipants()<minimumNumOfPlayers))){

					if(((ResourcesManager.getInstance().waitTimeString.equals("Play Time") || reclaimInProgress) && roomSelected.getNoOfParticipants()<minimumNumOfPlayers)){
						if(!reclaimInProgress){
							this.setText(PREFIX+"Waiting For Minimum " + minimumNumOfPlayers +  " Players To Join The Room");
						}

						if(waitTimeElapsed==timeoutInMinutes*60){
							reclaimInProgress = true;
							WAITTIME = waitTimeElapsed + 8;
						}
						else if(reclaimInProgress){
							if(WAITTIME-waitTimeElapsed != 0){
								ResourcesManager.getInstance().waitTimeString = (WAITTIME-waitTimeElapsed)+" Room Reclaim In Progress";
								this.setScale(0.5f*ResourcesManager.resourceScaler);
								this.setText("Not Enough Players Available To Continue Playing This Room, \nYou Will Be Automatically Redirected To Menu In " + ResourcesManager.getInstance().waitTimeString);
							}
							else{
								onBackKeyPressed();
							}
							//WAITTIME = waitTimeElapsed + 5;
						}
					}
					else{
						this.setText(PREFIX+"Waiting For Players To Join " + ResourcesManager.getInstance().waitTimeString);
					}
					Log.d("Wait Timer", "Wait Timer Handler Seconds" + ResourcesManager.getInstance().waitTimeString);
					Log.d("roomSelected", "roomSelected.getNoOfParticipants()" + roomSelected.getNoOfParticipants());
					super.onManagedUpdate(pSecondsElapsed);
				}
				else{ //Start The GAME
					if(this.hasParent()){
						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								/* Now it is safe to remove the entity! */
								SceneManager.getInstance().waitingScene.detachChild(ResourcesManager.getInstance().waitingText);
								//SceneManager.getInstance().disposeWaitingScene();
							}
						});

					}

					if(THEMES.BLUEMAGIC.toString().equals(ThemeManager.getInstance().getThemePreferences().getThemeSelection())){
						SoundManager.changeMusic(MusicPlayed.BEEMUSICJINGLE);
					}
					else{
						SoundManager.changeMusic(MusicPlayed.FASTMARCHJINGLE);
					}


					if(ResourcesManager.getInstance().hostImageclicked){
						//disposeScene();
						((HostScene)(SceneManager.getInstance().hostGameScene)).createDrawerChild();
						SceneManager.getInstance().setScene(SceneType.SCENE_HOST);
						ToastHelper.makeCustomToastOnUI("***Start Whistle Blows***", Toast.LENGTH_SHORT);

					}
					else{
						//disposeScene();

						SceneManager.getInstance().createGuessScene();
						ToastHelper.makeCustomToastOnUI("***Start Whistle Blows***", Toast.LENGTH_SHORT);

						if(StorageManager.getInstance().getUser().isSoundMuted()){
							com.efficientsciences.cowsandbulls.wordwars.managers.NotificationManager.getInstance().createTutorial();
						}
						else{
							ToastHelper.makeCustomToastOnUI("Start Guessing "+ ResourcesManager.getInstance().numberOfLettersHosted + " Letter Words", Toast.LENGTH_LONG);
						}

						if(ResourcesManager.isDailyLogin()){
							activity.runOnUiThread(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									ToastHelper.makeCustomToastForCoin("100 Bonus Coins For Daily Login Reward Collected", Toast.LENGTH_SHORT);
									StorageManager.getInstance().getUser().setNumOfCoins(StorageManager.getInstance().getUser().getNumOfCoins() + 100);
									StorageManager.getInstance().saveUserBeesBullsAndCoins(StorageManager.getInstance().getUser());
									StorageManager.getInstance().saveLastLogin();
									SoundManager.getInstance().createAddCoinsForShopSound();
								}
							});


						}
					}
					createHudInGame();
				}
			}
		};

		ResourcesManager.getInstance().numberOfLettersHostedText=new Text(ResourcesManager.getInstance().windowDimensions.x*0.2f,(ResourcesManager.getInstance().windowDimensions.y*0.5f)+100, ResourcesManager.getInstance().mBitmapFontForGame, "No. Of Letters In Word Hosted: "+ResourcesManager.getInstance().numberOfLettersHosted,150,ResourcesManager.getInstance().vbom);

		//welcome.setAnchorCenter(0, 0);
		ResourcesManager.getInstance().waitingText.setColor(1f,1f,1f);
		ResourcesManager.getInstance().waitingText.setTag(EntityTagManager.waitingText);
		ResourcesManager.getInstance().waitingText.setScale(.75f*ResourcesManager.resourceScaler);
		ResourcesManager.getInstance().waitingText.setAnchorCenter(1, 1);
		IEntityModifier moveMod = new MoveModifier(1, ResourcesManager.getInstance().waitingText.getX(), ResourcesManager.getInstance().waitingText.getY()-100, windowDimensions.x- ResourcesManager.getInstance().waitingText.getWidthScaled() - 40*resourcesManager.resourceScaler  , windowDimensions.y  - 20*ResourcesManager.resourceScaler,EaseStrongIn.getInstance());
		moveMod.setAutoUnregisterWhenFinished(true);
		ResourcesManager.getInstance().waitingText.registerEntityModifier(moveMod);
		this.attachChild(ResourcesManager.getInstance().waitingText);

		ResourcesManager.getInstance().numberOfLettersHostedText.setColor(1f,1f,1f);
		ResourcesManager.getInstance().numberOfLettersHostedText.setTag(EntityTagManager.waitingRoomLettersHostedText);
		ResourcesManager.getInstance().numberOfLettersHostedText.setScale(.65f*ResourcesManager.resourceScaler);
		//ResourcesManager.getInstance().numberOfLettersHostedText.setAnchorCenter(0.5f, 0.5f);
		ResourcesManager.getInstance().numberOfLettersHostedText.setAnchorCenter(0f, 0f);
		MoveModifier moveMod2 = new MoveModifier(1, ResourcesManager.getInstance().numberOfLettersHostedText.getX(), ResourcesManager.getInstance().numberOfLettersHostedText.getY(), 130*ResourcesManager.resourceScaler   , 30*ResourcesManager.resourceScaler,EaseStrongIn.getInstance());
		moveMod2.setAutoUnregisterWhenFinished(true);

		ResourcesManager.getInstance().roomNumber=new Text(50*ResourcesManager.resourceScaler,ResourcesManager.getInstance().windowDimensions.y, ResourcesManager.getInstance().mBitmapFontForGame, "Room # "+roomSelected.getIndex(),150,ResourcesManager.getInstance().vbom);
		ResourcesManager.getInstance().roomNumber.setColor(1f,1f,1f);
		ResourcesManager.getInstance().roomNumber.setTag(EntityTagManager.waitingRoomNumberText);
		ResourcesManager.getInstance().roomNumber.setScale(.65f*ResourcesManager.resourceScaler);
		//ResourcesManager.getInstance().numberOfLettersHostedText.setAnchorCenter(0.5f, 0.5f);
		ResourcesManager.getInstance().roomNumber.setAnchorCenter(0f, 1f);

		this.attachChild(ResourcesManager.getInstance().roomNumber);
		this.attachChild(ResourcesManager.getInstance().numberOfLettersHostedText);
		ResourcesManager.getInstance().numberOfLettersHostedText.registerEntityModifier(moveMod2);

		if(!backButtonDisplayed){
			attachBackButton();
		}

	}


	private void createHudInGame() {
		ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				/* Now it is safe to remove the entity from Waiting Scene! */
				if(null!=ResourcesManager.getInstance().roomNumber && null!=ResourcesManager.getInstance().numberOfLettersHostedText && SceneManager.getInstance().getCurrentScene() instanceof RealGameInterface){
					ResourcesManager.getInstance().roomNumber.detachSelf();
					ResourcesManager.getInstance().numberOfLettersHostedText.detachSelf();
					if(ResourcesManager.getInstance().users!=null && !ResourcesManager.getInstance().users.isEmpty()){
						for (UserChunk user : ResourcesManager.getInstance().users) {
							user.redMask.setScale(1f);
							if(user.getHeight()>ResourcesManager.getInstance().userChunkHeight)
								ResourcesManager.getInstance().userChunkHeight= user.getHeight();
							if(user.getWidth()>ResourcesManager.getInstance().userChunkWidth)
								ResourcesManager.getInstance().userChunkWidth= user.getWidth();
						}
					}
					float sliderEndXPosition=10*ResourcesManager.resourceScaler+ ResourcesManager.getInstance().userChunkWidth;
					float interElementalHUDOffset = 30*ResourcesManager.resourceScaler;
					float hudOffset = 10*ResourcesManager.resourceScaler;
					ResourcesManager.getInstance().hudRect = new Rectangle(0, windowDimensions.y, windowDimensions.x, (PageChunkFactory.pageChunkYOffset-38)*ResourcesManager.resourceScaler, vbom);
					ResourcesManager.getInstance().hudRect.setColor(1f,1f,1f,0f);
					ResourcesManager.getInstance().hudRect.setAnchorCenter(0, 1);

					ResourcesManager.getInstance().roomNumber.setPosition(sliderEndXPosition+hudOffset+interElementalHUDOffset, ResourcesManager.getInstance().hudRect.getHeightScaled());
					ResourcesManager.getInstance().roomNumber.setScale(0.6f*ResourcesManager.resourceScaler);
					ResourcesManager.getInstance().roomNumber.setColor(0.15f,0.15f,0.15f);

					ResourcesManager.getInstance().numberOfLettersHostedText.setPosition(ResourcesManager.getInstance().roomNumber.getX()+ ResourcesManager.getInstance().roomNumber.getWidthScaled() + interElementalHUDOffset ,  ResourcesManager.getInstance().hudRect.getHeightScaled());
					ResourcesManager.getInstance().numberOfLettersHostedText.setIgnoreUpdate(true);
					ResourcesManager.getInstance().numberOfLettersHostedText.setText("Letters: "+ResourcesManager.getInstance().numberOfLettersHosted);
					ResourcesManager.getInstance().numberOfLettersHostedText.setScale(0.6f*ResourcesManager.resourceScaler);
					ResourcesManager.getInstance().numberOfLettersHostedText.setAnchorCenter(0, 1);
					ResourcesManager.getInstance().numberOfLettersHostedText.setColor(0.15f,0.15f,0.15f);

					if(ResourcesManager.getInstance().guessImageclicked){
						ResourcesManager.getInstance().beeHudAnim = new AnimatedSprite(ResourcesManager.getInstance().numberOfLettersHostedText.getX()+ ResourcesManager.getInstance().numberOfLettersHostedText.getWidthScaled() + interElementalHUDOffset ,
								ResourcesManager.getInstance().hudRect.getHeightScaled() ,ThemeManager.getInstance().beeTextureRegion, vbom);
						ResourcesManager.getInstance().beeHudAnim.setScale(0.23f*ResourcesManager.resourceScaler);
						ResourcesManager.getInstance().beeHudAnim.animate(100);
						ResourcesManager.getInstance().beeHudAnim.setAnchorCenter(0, 1);
						if(StorageManager.getInstance().getUser().getNumOfBees()>0){
							ResourcesManager.getInstance().beeHudAnim.setAlpha(1);
						}
						else{
							ResourcesManager.getInstance().beeHudAnim.setAlpha(0.6f);
						}
						ResourcesManager.getInstance().numberOfBeeFliesText=new Text( ResourcesManager.getInstance().beeHudAnim.getX()+ ResourcesManager.getInstance().beeHudAnim.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontForGame, " X "+StorageManager.getInstance().getUser().getNumOfBees(),150,vbom);
						ResourcesManager.getInstance().numberOfBeeFliesText.setScale(0.6f*ResourcesManager.resourceScaler);
						ResourcesManager.getInstance().numberOfBeeFliesText.setAnchorCenter(0, 1);
						ResourcesManager.getInstance().numberOfBeeFliesText.setColor(0.15f,0.15f,0.15f);

						ResourcesManager.getInstance().wordIqText=new Text( ResourcesManager.getInstance().numberOfBeeFliesText.getX()+ ResourcesManager.getInstance().numberOfBeeFliesText.getWidthScaled() + interElementalHUDOffset , 
								ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontForGame,
								"WIQ: "+ResourcesManager.getInstance().level.getWordIq().toString(),150, vbom);
						ResourcesManager.getInstance().wordIqText.setIgnoreUpdate(true);
						ResourcesManager.getInstance().wordIqText.setScale(0.6f*ResourcesManager.resourceScaler);
						ResourcesManager.getInstance().wordIqText.setAnchorCenter(0, 1);
						ResourcesManager.getInstance().wordIqText.setColor(0.15f,0.15f,0.15f);

						ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().beeHudAnim);
						ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().numberOfBeeFliesText);
					}
					else{
						ResourcesManager.getInstance().wordIqText=new Text( ResourcesManager.getInstance().numberOfLettersHostedText.getX()+ ResourcesManager.getInstance().numberOfLettersHostedText.getWidthScaled() + interElementalHUDOffset , 
								ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontForGame,
								"WORD IQ: "+ResourcesManager.getInstance().level.getWordIq().toString(),150, vbom);
						ResourcesManager.getInstance().wordIqText.setIgnoreUpdate(true);
						ResourcesManager.getInstance().wordIqText.setScale(0.6f*ResourcesManager.resourceScaler);
						ResourcesManager.getInstance().wordIqText.setAnchorCenter(0, 1);
						ResourcesManager.getInstance().wordIqText.setColor(0.15f,0.15f,0.15f);
					}

					thememanager.getCoinToHud();
					//ResourcesManager.getInstance().hudCoin.setAnchorCenterY(0.75f);
					ResourcesManager.getInstance().hudCoin.setPosition(ResourcesManager.getInstance().wordIqText.getX()+ ResourcesManager.getInstance().wordIqText.getWidthScaled()+interElementalHUDOffset ,  ResourcesManager.getInstance().hudRect.getHeightScaled());
					ResourcesManager.getInstance().numberOfCoinsText=new Text(ResourcesManager.getInstance().hudCoin.getX()+ ResourcesManager.getInstance().hudCoin.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontForGame, " X "+ StorageManager.getInstance().getUser().getNumOfCoins(),150,vbom);
					ResourcesManager.getInstance().numberOfCoinsText.setScale(0.6f*ResourcesManager.resourceScaler);
					ResourcesManager.getInstance().numberOfCoinsText.setAnchorCenter(0, 1);
					ResourcesManager.getInstance().numberOfCoinsText.setColor(0.15f,0.15f,0.15f);


					ResourcesManager.getInstance().fbButton = new Sprite(ResourcesManager.getInstance().numberOfCoinsText.getX()+ResourcesManager.getInstance().numberOfCoinsText.getWidthScaled()+interElementalHUDOffset,
							ResourcesManager.getInstance().hudRect.getHeightScaled() ,ResourcesManager.getInstance().fbFriend_region, vbom){
						public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
							if(pSceneTouchEvent.isActionUp()){
								//Commented Temporarily
								com.efficientsciences.cowsandbulls.wordwars.managers.NotificationManager.getInstance().createFBLoginRequiredNotification((byte) 1);
								return true;
							}
							return true;
						}
					};
					ResourcesManager.getInstance().fbButton.setScale(0.2f*ResourcesManager.resourceScaler);
					ResourcesManager.getInstance().fbButton.setAnchorCenter(0, 1);


					ResourcesManager.getInstance().bullRunHudAnim = new AnimatedSprite(ResourcesManager.getInstance().fbButton.getX()+ ResourcesManager.getInstance().fbButton.getWidthScaled() + interElementalHUDOffset ,
							ResourcesManager.getInstance().hudRect.getHeightScaled() ,ThemeManager.getInstance().bullTextureRegion, vbom);
					ResourcesManager.getInstance().bullRunHudAnim.setScale(0.15f*ResourcesManager.resourceScaler);
					ResourcesManager.getInstance().bullRunHudAnim.animate(100);
					ResourcesManager.getInstance().bullRunHudAnim.setAnchorCenter(0, 1);
					String bullText=TimerManager.getInstance().bullAppeared?"Used":"Not Used";
					if(StorageManager.getInstance().getUser().getNumOfBullRuns()>0){
						ResourcesManager.getInstance().bullRunHudAnim.setAlpha(1);
					}
					else{
						bullText= "N/A";
						ResourcesManager.getInstance().bullRunHudAnim.setAlpha(0.6f);
					}

					ResourcesManager.getInstance().numberOfBullRunsText=new Text( ResourcesManager.getInstance().bullRunHudAnim.getX()+ ResourcesManager.getInstance().bullRunHudAnim.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontForGame,bullText,150,vbom);
					ResourcesManager.getInstance().numberOfBullRunsText.setScale(0.6f*ResourcesManager.resourceScaler);
					ResourcesManager.getInstance().numberOfBullRunsText.setAnchorCenter(0, 1);
					ResourcesManager.getInstance().numberOfBullRunsText.setColor(0.15f,0.15f,0.15f);

					ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().roomNumber);
					ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().numberOfLettersHostedText);
					ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().wordIqText);
					ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().hudCoin);
					ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().numberOfCoinsText);
					ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().fbButton);
					if(ResourcesManager.getInstance().guessImageclicked){
						ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().bullRunHudAnim);
						ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().numberOfBullRunsText);
					}
					ResourcesManager.getInstance().hudRect.setZIndex(24);

					ResourcesManager.getInstance().chat = new TiledSprite(windowDimensions.x, windowDimensions.y/2 ,ResourcesManager.getInstance().chat_Region, vbom){
						@Override
						public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
							if(pSceneTouchEvent.isActionDown()){
								ResourcesManager.getInstance().chat.setCurrentTileIndex(Chat.CHAT_INVY_ID);
								return true;
							}
							else if(pSceneTouchEvent.isActionUp()){
								//Commented Temporarily

								StandOutWindow.show(ResourcesManager.getInstance().activity, WidgetsWindow.class, 0);
								ResourcesManager.getInstance().chat.setCurrentTileIndex(Chat.CHAT_INVZ_ID);
								ResourcesManager.getInstance().chat.setAlpha(0.5f);
								ChatManager.chatState=1;
								return true;
							}
							return true;

						}
					};
					ResourcesManager.getInstance().chat.setScale(0.35f*ResourcesManager.resourceScaler);
					ResourcesManager.getInstance().chat.setCurrentTileIndex(Chat.CHAT_INV_ID);
					ResourcesManager.getInstance().chat.setAnchorCenter(1, 1);
					if(ChatManager.chatState==0){ //Not open
						ResourcesManager.getInstance().chat.setAlpha(1);
					}
					else{
						ResourcesManager.getInstance().chat.setAlpha(0.5f);
					}



					SceneManager.getInstance().getCurrentScene().attachChild(ResourcesManager.getInstance().chat);
					SceneManager.getInstance().getCurrentScene().registerTouchArea(ResourcesManager.getInstance().chat);


					//Attach Ask Help
					if(ResourcesManager.getInstance().guessImageclicked){
						ResourcesManager.getInstance().askHelp = new TiledSprite(0, windowDimensions.y/2 ,ResourcesManager.getInstance().askHelp_Region, vbom){
							@Override
							public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
								if(pSceneTouchEvent.isActionDown()){
									ResourcesManager.getInstance().askHelp.setCurrentTileIndex(Askhelp.ASKHELP3_ID);
									return true;
								}
								else if(pSceneTouchEvent.isActionUp()){
									//Commented Temporarily

									ResourcesManager.getInstance().askHelp.setCurrentTileIndex(Askhelp.ASKHELP4_ID);
									ResourcesManager.getInstance().askHelp.setAlpha(0.5f);


									attachcontentCaptureAndShare();

									return true;
								}
								return true;

							}

						};
						ResourcesManager.getInstance().askHelp.setScale(0.45f*ResourcesManager.resourceScaler);
						ResourcesManager.getInstance().askHelp.setCurrentTileIndex(Askhelp.ASKHELP2_ID);
						ResourcesManager.getInstance().askHelp.setAnchorCenter(0.2f, 0);
						ResourcesManager.getInstance().askHelp.setAlpha(1);



						SceneManager.getInstance().getCurrentScene().attachChild(ResourcesManager.getInstance().askHelp);
						SceneManager.getInstance().getCurrentScene().registerTouchArea(ResourcesManager.getInstance().askHelp);
					}


					SceneManager.getInstance().getCurrentScene().attachChild(ResourcesManager.getInstance().hudRect);
					SceneManager.getInstance().getCurrentScene().registerTouchArea(ResourcesManager.getInstance().fbButton);
					SceneManager.getInstance().getCurrentScene().sortChildren();
				}
				SceneManager.getInstance().disposeWaitingScene();
			}
		});
	}


	private void attachcontentCaptureAndShare() {
		ScreenGrabber screenCapture = new ScreenGrabber();
		attachContent();
		SceneManager.getInstance().getCurrentScene().attachChild(screenCapture); // As the last Entity in the scene
		captureScreenAndShare(screenCapture, SceneManager.getInstance().getCurrentScene());
	}

	public void captureScreenAndShare(ScreenGrabber screenCapture, final IEntity scene) {
		final int viewWidth = ResourcesManager.getInstance().camera.getSurfaceWidth();
		final int viewHeight = ResourcesManager.getInstance().camera.getSurfaceHeight();

		final int hWidth = Math.round(viewWidth / 2f);
		final int hHeight = Math.round(viewHeight / 2f);

		screenCapture.grab(0, 0, viewWidth, viewHeight, new IScreenGrabberCallback() {

			@Override
			public void onScreenGrabbed(Bitmap pBitmap) {
				// TODO Auto-generated method stub
				final Bitmap b2 = getResizedBitmap(pBitmap, hHeight, hWidth);

				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						SoundManager.getInstance().createCameraCaptureSound();
						createFile(b2);						
					}
				});


				/*BitmapTextureAtlas mBitmapTextureAtlasn = new BitmapTextureAtlas(ResourcesManager.getInstance().activity.getTextureManager(), hWidth, hHeight, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);
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
				scene.attachChild(rect);*/
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

	private void attachContent() {

		//Help Text Row 1 to 4
		ResourcesManager.getInstance().overlayRect= new Rectangle(windowDimensions.x/2, windowDimensions.y/2, windowDimensions.x, windowDimensions.y, vbom);
		ResourcesManager.getInstance().overlayRect.setColor(0.0039f,0.6549f,0.5450f,1f);
		ResourcesManager.getInstance().sherlockSprite =new Sprite(0, 0.65f*windowDimensions.y, ResourcesManager.getInstance().sheryl_region, ResourcesManager.getInstance().vbom);
		ResourcesManager.getInstance().sherlockSprite.setAnchorCenter(0, 0.45f);
		ResourcesManager.getInstance().sherlockSprite.setColor(0.1f,0.1f,0.1f,0.9f);
		ResourcesManager.getInstance().sherlockSprite.setScale(1.2f*ResourcesManager.resourceScaler);
		ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().sherlockSprite);

		ResourcesManager.getInstance().qrcodeSprite =new Sprite(windowDimensions.x, windowDimensions.y, ResourcesManager.getInstance().qrcode_Region, ResourcesManager.getInstance().vbom);
		ResourcesManager.getInstance().qrcodeSprite.setAnchorCenter(1, 1);
		ResourcesManager.getInstance().qrcodeSprite.setColor(0.1f,0.1f,0.1f,0.9f);
		ResourcesManager.getInstance().qrcodeSprite.setScale(0.7f*ResourcesManager.resourceScaler);
		ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().qrcodeSprite);
		
		//Step 1
		float yPOsitions= (windowDimensions.y/2)+190*ResourcesManager.resourceScaler;
		ResourcesManager.getInstance().helpTextRow1= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().mBitmapFontRound,"I am a "+ResourcesManager.getInstance().numberOfLettersHosted+" Letter Word", vbom);
		ResourcesManager.getInstance().helpTextRow1.setColor(1f,0.152f,0.219f,1f);
		ResourcesManager.getInstance().helpTextRow1.setScale(1f*ResourcesManager.resourceScaler);

		ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow1);

		Set<Character> sortedLettersSet= new TreeSet<Character>();

		List<PageChunk> usablePageChunks =  ResourcesManager.getInstance().pages.getUsablePageChunksTotal();
		List<PageChunk> usablePageChunksBulls =  ResourcesManager.getInstance().pages.getUsablePageChunksBull();

		TextOptions opts = new TextOptions(AutoWrap.WORDS,  windowDimensions.x, HorizontalAlign.CENTER);

		//Step 2
		if((ResourcesManager.getInstance().wordHosted==null || ResourcesManager.getInstance().wordHosted.isEmpty() || ResourcesManager.getInstance().wordHosted.length()<=4) && usablePageChunks!=null && usablePageChunks.size()>3){
			yPOsitions=yPOsitions-90*ResourcesManager.resourceScaler;
			ResourcesManager.getInstance().helpTextRow2= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font,  "I Have All Unique Letters", vbom);
			ResourcesManager.getInstance().helpTextRow2.setColor(0.1f,0.1f,0.1f,0.9f);
			ResourcesManager.getInstance().helpTextRow2.setScale(0.5f*ResourcesManager.resourceScaler);
			ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow2);
		}
		else if(ResourcesManager.getInstance().wordHosted!=null && !ResourcesManager.getInstance().wordHosted.isEmpty() && (ResourcesManager.getInstance().wordHosted.length()>6 || (ResourcesManager.getInstance().wordHosted.length()<=6 && (null==usablePageChunksBulls || usablePageChunksBulls.isEmpty())))){
			StringBuffer sb= new StringBuffer("I have Letters ");

			char[] anagram = ResourcesManager.getInstance().wordHosted.toCharArray();
			Set<Character> charSet=new HashSet<Character>();
			Random rand = new Random();
			for (int i = 0; i < anagram.length;) {
				int randInt = rand.nextInt(anagram.length);
				char c = anagram[randInt];
				boolean b = charSet.add(c);
				if(b){
					i++;
					sb.append(c+ " ");
				}
			}
			sb.append(" Jumbled\nFind Me If You Are A Genius ");

			yPOsitions=yPOsitions-100*ResourcesManager.resourceScaler;
			ResourcesManager.getInstance().helpTextRow2= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font, sb.toString(), vbom);
			ResourcesManager.getInstance().helpTextRow2.setColor(0.1f,0.1f,0.1f,0.9f);
			ResourcesManager.getInstance().helpTextRow2.setScale(0.48f*ResourcesManager.resourceScaler);
			ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow2);
		}
		else if(ResourcesManager.getInstance().wordHosted!=null && !ResourcesManager.getInstance().wordHosted.isEmpty() && ResourcesManager.getInstance().wordHosted.length()<=6){
			StringBuffer sb= new StringBuffer("I have Letters ");

			char[] anagram = ResourcesManager.getInstance().wordHosted.toCharArray();
			Set<Character> charSet=new HashSet<Character>();
			Random rand = new Random();
			for (int i = 0; i < anagram.length;) {
				
				int randInt = rand.nextInt(anagram.length);
				while(randInt==i){
				randInt= rand.nextInt(anagram.length);
				}
				char c = anagram[randInt];
				boolean b = charSet.add(c);
				if(b && i<=(anagram.length/2)){
					i++;
					sb.append(c+ " ");
				}
				else if(b){
					i++;
					sb.append("*");
				}
			}
			sb.append(" Jumbled And Masked\nFind Masked Letters &\nMake Perfect English Word\nIf You Are A Genius ");

			yPOsitions=yPOsitions-100*ResourcesManager.resourceScaler;
			ResourcesManager.getInstance().helpTextRow2= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font, sb.toString(),opts, vbom);
			ResourcesManager.getInstance().helpTextRow2.setColor(0.1f,0.1f,0.1f,0.9f);
			ResourcesManager.getInstance().helpTextRow2.setScale(0.44f*ResourcesManager.resourceScaler);
			ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow2);
		}

		ResourcesManager.getInstance().helpTextRow3 = null;
		//Step 3 optional
		if(ResourcesManager.getInstance().wordHosted!=null && !ResourcesManager.getInstance().wordHosted.isEmpty() && ResourcesManager.getInstance().wordHosted.length()<=6){
			if(usablePageChunks!=null && !usablePageChunks.isEmpty()){
				StringBuffer sb= new StringBuffer("I have ");
				byte b=0;
				for (Iterator iterator = usablePageChunks
						.iterator(); iterator.hasNext();) {
					PageChunk pageChunk = (PageChunk) iterator
							.next();
					if(null!=pageChunk && null!= pageChunk.getWordGuessed()){
						if((0!=pageChunk.getCows() || 0!=pageChunk.getBulls()) && b<=4){
							int charCount=pageChunk.getCows()+pageChunk.getBulls();
							b++;
							if(charCount==1){
								sb.append(charCount+ " Letter in Word " +  pageChunk.getWordGuessed());
							}
							else{
								sb.append(charCount+ " Letters in Word " +  pageChunk.getWordGuessed());
							}
							if(iterator.hasNext()){
								sb.append(",\n");
							}
						}
						else if(null!=pageChunk.getWordGuessed()){
							char c[]= pageChunk.getWordGuessed().toCharArray();
							for (int i = 0; i < c.length; i++) {
								char eachLetter = c[i];
								sortedLettersSet.add(eachLetter);
							}

						}
					}
				}

				yPOsitions=yPOsitions-110*ResourcesManager.resourceScaler;
				ResourcesManager.getInstance().helpTextRow3= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font, sb.toString(), vbom);
				ResourcesManager.getInstance().helpTextRow3.setColor(0.1f,0.1f,0.1f,0.9f);
				ResourcesManager.getInstance().helpTextRow3.setAnchorCenterY(0.6f);
				ResourcesManager.getInstance().helpTextRow3.setScale(0.44f*ResourcesManager.resourceScaler);
				ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow3);
			}
			else if(ResourcesManager.getInstance().wordHosted.length()>3){
				String clueWord = getClueWord(timerManager.randomiser.nextInt(ResourcesManager.getInstance().wordHosted.length()),  ResourcesManager.getInstance().wordHosted);
				StringBuffer sb= new StringBuffer("Here is your clue for\none of the letters from word\nwith exact position ");
				sb.append(clueWord);
				yPOsitions=yPOsitions-110*ResourcesManager.resourceScaler;
				ResourcesManager.getInstance().helpTextRow3= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font, sb.toString(), vbom);
				ResourcesManager.getInstance().helpTextRow3.setColor(0.1f,0.1f,0.1f,0.9f);
				ResourcesManager.getInstance().helpTextRow3.setAnchorCenterY(0.6f);
				ResourcesManager.getInstance().helpTextRow3.setScale(0.44f*ResourcesManager.resourceScaler);
				ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow3);
			}
		}
		else if(usablePageChunksBulls!=null && !usablePageChunksBulls.isEmpty()){
			StringBuffer sb= new StringBuffer("I have ");
			byte b=0;
			for (Iterator iterator = usablePageChunksBulls
					.iterator(); iterator.hasNext();) {
				PageChunk pageChunk = (PageChunk) iterator
						.next();
				if(null!=pageChunk && null!= pageChunk.getWordGuessed()){
					if((0!=pageChunk.getBulls()) && b<=2){
						int charCount=pageChunk.getBulls();
						b++;
						if(charCount==1){
							continue;
						}
						else{
							sb.append(charCount+ " Letters in Word " +  pageChunk.getWordGuessed()+" at exact position");
						}
						if(iterator.hasNext()){
							sb.append(",\n");
						}
					}
					else if(null!=pageChunk.getWordGuessed()){
						char c[]= pageChunk.getWordGuessed().toCharArray();
						for (int i = 0; i < c.length; i++) {
							char eachLetter = c[i];
							sortedLettersSet.add(eachLetter);
						}

					}
				}
			}
		

		yPOsitions=yPOsitions-110*ResourcesManager.resourceScaler;
		ResourcesManager.getInstance().helpTextRow3= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font, sb.toString(), vbom);
		ResourcesManager.getInstance().helpTextRow3.setColor(0.1f,0.1f,0.1f,0.9f);
		ResourcesManager.getInstance().helpTextRow3.setAnchorCenterY(0.6f);
		ResourcesManager.getInstance().helpTextRow3.setScale(0.43f*ResourcesManager.resourceScaler);
		ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow3);
		}
		else if(usablePageChunks!=null && !usablePageChunks.isEmpty()){
			StringBuffer sb= new StringBuffer("I have ");
			byte b=0;
			for (Iterator iterator = usablePageChunks
					.iterator(); iterator.hasNext();) {
				PageChunk pageChunk = (PageChunk) iterator
						.next();
				if(null!=pageChunk && null!= pageChunk.getWordGuessed()){
					if((0!=pageChunk.getCows() || 0!=pageChunk.getBulls()) && b<=4){
						int charCount=pageChunk.getCows()+pageChunk.getBulls();
						b++;
						if(charCount==1){
							sb.append(charCount+ " Letter in Word " +  pageChunk.getWordGuessed());
						}
						else{
						sb.append(charCount+ " Letters in Word " +  pageChunk.getWordGuessed());
						}
						if(iterator.hasNext()){
							sb.append(",\n");
						}
					}
					else if(null!=pageChunk.getWordGuessed()){
						char c[]= pageChunk.getWordGuessed().toCharArray();
						for (int i = 0; i < c.length; i++) {
							char eachLetter = c[i];
							sortedLettersSet.add(eachLetter);
						}
						
					}
				}
			}

			yPOsitions=yPOsitions-110*ResourcesManager.resourceScaler;
			ResourcesManager.getInstance().helpTextRow3= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font, sb.toString(), vbom);
			ResourcesManager.getInstance().helpTextRow3.setColor(0.1f,0.1f,0.1f,0.9f);
			ResourcesManager.getInstance().helpTextRow3.setAnchorCenterY(0.6f);
			ResourcesManager.getInstance().helpTextRow3.setScale(0.4f*ResourcesManager.resourceScaler);
			ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow3);
		}
		
		
		//Step 4 based on step 3 pagechunks in total
		if(!sortedLettersSet.isEmpty()){
			StringBuffer notPresentHelpString= new StringBuffer("I don't have letters ");
			for (Iterator iterator = sortedLettersSet
					.iterator(); iterator.hasNext();) {
				Character letter = (Character) iterator
						.next();
				notPresentHelpString.append(Character.toUpperCase(letter));
				if(iterator.hasNext()){
					notPresentHelpString.append(", ");
				}
				else{
					notPresentHelpString.append(".");
				}

			}

			yPOsitions=yPOsitions-ResourcesManager.getInstance().helpTextRow3.getHeightScaled()-2*ResourcesManager.resourceScaler;
			ResourcesManager.getInstance().helpTextRow4= new Text(windowDimensions.x/2, yPOsitions, ResourcesManager.getInstance().font, notPresentHelpString.toString(), vbom);
			ResourcesManager.getInstance().helpTextRow4.setColor(0.1f,0.1f,0.1f,0.9f);
			ResourcesManager.getInstance().helpTextRow4.setScale(0.6f*ResourcesManager.resourceScaler);
			ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow4);

			/*if((sortedLettersSet.size()<26-ResourcesManager.getInstance().numberOfLettersHosted-2) && ResourcesManager.getInstance().numberOfLettersHosted>4){

			}*/
		}//Step 4 No Step 4
		else{
			ResourcesManager.getInstance().helpTextRow4=null;
		}

		String s = "";
		

		//yPOsitions= yPOsitions-80*ResourcesManager.resourceScaler;
		s= "Puzzle created By Word - A Multiplayer Lingo Game \n #wordwithworld #isogram #hackthebrain";
		opts = new TextOptions(AutoWrap.WORDS, windowDimensions.x-20*ResourcesManager.resourceScaler, HorizontalAlign.RIGHT);
		ResourcesManager.getInstance().helpTextRow6= new Text((windowDimensions.x-20*ResourcesManager.resourceScaler), 20*ResourcesManager.resourceScaler, ResourcesManager.getInstance().font, s, opts,vbom);
		ResourcesManager.getInstance().helpTextRow6.setColor(0.25f,0.25f,0.25f,0.9f);
		ResourcesManager.getInstance().helpTextRow6.setAnchorCenter(1,0);
		ResourcesManager.getInstance().helpTextRow6.setScale(0.40f*ResourcesManager.resourceScaler);
		ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow6);
		
		if(ConnectionManager.isNormalFlow()){
			s= "Help "+ StorageManager.getInstance().getUser().getDisplayName() +" By Joining League Room "+StorageManager.getInstance().getUser().getHostedGuessedRoomIndex();
		}else{
			s= "Join "+ StorageManager.getInstance().getUser().getDisplayName() +" By Accepting This Daily Challenge In Game";

		}
		ResourcesManager.getInstance().helpTextRow5= new Text(windowDimensions.x/2, 20*ResourcesManager.resourceScaler+(ResourcesManager.getInstance().helpTextRow6.getHeightScaled()), ResourcesManager.getInstance().font, s,vbom);
		ResourcesManager.getInstance().helpTextRow5.setColor(0.1f,0.1f,0.1f,0.9f);
		ResourcesManager.getInstance().helpTextRow5.setAnchorCenterY(0);
		ResourcesManager.getInstance().helpTextRow5.setScale(0.55f*ResourcesManager.resourceScaler);
		ResourcesManager.getInstance().overlayRect.attachChild(ResourcesManager.getInstance().helpTextRow5);


		SceneManager.getInstance().getCurrentScene().attachChild(ResourcesManager.getInstance().overlayRect);
	}

	public String getClueWord(int index,  String wordHosted) {

		StringBuilder sb = new StringBuilder(wordHosted.length());
		for(int i = 0; i < wordHosted.length(); i++) {
			char c = i==index ? wordHosted.charAt(index) : '*';
			sb.append(c);
			sb.append(" ");
		}
		return sb.toString();
	}


	
	/**
	 * @param b2
	 */
	protected void createFile(Bitmap b2) {
		try{
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			b2.compress(Bitmap.CompressFormat.PNG, 100, bytes);

			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(ResourcesManager.getInstance().activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_DENIED){
				if(ResourcesManager.getInstance().activity.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
					ToastHelper.makeCustomToastOnUI("Asking Help Requires Creating A File In Phone/SD-Card Temporarily",Toast.LENGTH_LONG);
				}
				ActivityCompat.requestPermissions(ResourcesManager.getInstance().activity,
						new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
						ResourcesManager.getInstance().activity.WRITE_EXTERNAL_STORAGE);
			}
			if(ContextCompat.checkSelfPermission(ResourcesManager.getInstance().activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
				File dir = new File(FILEDIR);
				dir.mkdirs();
				File f = new File(dir, nameOfImageShared);
				f.createNewFile();

				FileOutputStream fo = new FileOutputStream(f);
				fo.write(bytes.toByteArray());
				fo.close();
			}
			ResourcesManager.fileShared = true;
			try{
				if(!FacebookManager.sUserLoggedIn && ResourcesManager.fbHelpShared==0){
					loginFBAndPost(b2);
					ResourcesManager.fbHelpShared=1;
				}
				else{
					if(ResourcesManager.fbHelpShared==0 && ResourcesManager.getInstance().wordsListChanged){
						postImageToFacebook(b2);
						ResourcesManager.getInstance().wordsListChanged = false;
					}
					else{
						shareToWhatsapp("Find This "+ResourcesManager.getInstance().numberOfLettersHosted+" Letter Word If You Are A Genius  \nHelp Me By Downloading This Brainteaser Puzzle Game Following This Link https://goo.gl/WpCXbk",b2);
						ResourcesManager.fbHelpShared=0;
					}
				}
			}
			catch(Exception e){
				ToastHelper.makeCustomToast("Facebook Not Properly Configured",Toast.LENGTH_LONG);
				shareToWhatsapp("Find This "+ResourcesManager.getInstance().numberOfLettersHosted+" Letter Word If You Are A Genius  \nHelp Me By Downloading This Brainteaser Game Following This Link https://goo.gl/WpCXbk",b2);

			}
		} catch (final Exception e) {
			ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(ResourcesManager.getInstance().activity, e.toString(), Toast.LENGTH_SHORT).show();
				}
			});
		}
	}

	/**
	 * 
	 */
	public void loginFBAndPost(final Bitmap b2) {
		OnLoginListener onLoginListener = new OnLoginListener() {
			@Override
			public void onLogin() {
				FacebookManager.sUserLoggedIn = true;
				postImageToFacebook(b2);

			}

			@Override
			public void onNotAcceptingPermissions(Permission.Type type) {
				// user didn't accept READ or WRITE permission
				FacebookManager.sUserLoggedIn = false;
				Log.w(FacebookManager.TAG, String.format("You didn't accept %s permissions", type.name()));
			}

			@Override
			public void onThinking() {
				FacebookManager.sUserLoggedIn = false;
				// TODO Auto-generated method stub

			}

			@Override
			public void onException(Throwable throwable) {
				FacebookManager.sUserLoggedIn = false;
				OnLogoutListener onLogoutListener = new OnLogoutListener() {



					@Override
					public void onThinking() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onException(Throwable throwable) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFail(String reason) {

					}

					@Override
					public void onLogout() {
						// TODO Auto-generated method stub
						FacebookManager.sUserLoggedIn = false;
						if(FacebookManager.reasonForLogout==1){
							FacebookManager.reasonForLogout=0;
							postImageToFacebook(b2);
						}
					}
				};

				FacebookManager.reasonForLogout=1;
				ResourcesManager.getInstance().activity.mSimpleFacebook.logout(onLogoutListener);
				// TODO Auto-generated method stub
			}

			@Override
			public void onFail(String reason) {
				FacebookManager.sUserLoggedIn = false;
				// TODO Auto-generated method stub

			}

			/* 
			 * You can override other methods here: 
			 * onThinking(), onFail(String reason), onException(Throwable throwable)
			 */ 
		};

		ResourcesManager.getInstance().activity.mSimpleFacebook.login(onLoginListener);

	}


	private Session openActiveSession(final Activity pActivity, final boolean pAllowLoginUI,
			final StatusCallback pCallback, final List<String> pPermissions) {
		final OpenRequest openRequest = new OpenRequest(pActivity).setPermissions(pPermissions)
				.setCallback(pCallback);
		openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
		final Session session = new Builder(pActivity.getBaseContext()).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || pAllowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
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

	public void shareToWhatsapp(String content,Bitmap bitmap) {
		try{
		Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
		whatsappIntent.setType("image/png");
		whatsappIntent.putExtra(Intent.EXTRA_TEXT,content);
		whatsappIntent.setPackage("com.whatsapp");
		whatsappIntent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(new File(FILEDIR,nameOfImageShared)));
		activity.startActivityForResult(whatsappIntent, ResourcesManager.WHATSAPPRESPONSECODE);
		}
		catch(Exception e){
			ToastHelper.makeCustomToast("Whatsapp Not Properly Configured",Toast.LENGTH_LONG);
			postImageToFacebook(bitmap);

		}
	}


	//private Bundle mExtras;
	private void postImageToFacebook(Bitmap bitmap) {
		Session session = Session.getActiveSession();
		//final Uri uri = (Uri) mExtras.get(Intent.EXTRA_STREAM);
		final String extraText = "Solve If You Are A Genius \n \nSave Me By Downloading This Brainteaser Game Following This Link https://goo.gl/WpCXbk";
		if (session.isPermissionGranted("publish_actions"))
		{
			Bundle param = new Bundle();

			// Add the image
			try {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArrayData = stream.toByteArray();
				param.putByteArray("picture", byteArrayData);
			} catch (Exception ioe) {
				// The image that was send through is now not there?
			}

			// Add the caption
			param.putString("message", extraText);
			Request request = new Request(session,"me/photos", param, HttpMethod.POST, new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					if(ResourcesManager.fileShared){
						ResourcesManager.fileShared=false;
						activity.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								File myFile = new File(WaitingScene.FILEDIR+"/"+WaitingScene.nameOfImageShared);
								if(myFile.exists())
									myFile.delete();
							}
						});
						if(ResourcesManager.getInstance().overlayRect.hasParent())
						ResourcesManager.getInstance().detachInUpdateThread(ResourcesManager.getInstance().overlayRect);
						ToastHelper.makeCustomToast("Check Your Facebook Wall For Clue Or Click Again For Help Via WhatsApp",Toast.LENGTH_LONG);
						ResourcesManager.fbHelpShared = 1;

					}
					if(android.os.Build.VERSION.SDK_INT >= 16){
						FacebookManager.addNotification(response.getGraphObject(), response.getError());
					}
				}
			}, null);
			RequestAsyncTask asyncTask = new RequestAsyncTask(request);
			asyncTask.execute();
		}
	}

	


	@Override
	public void onBackKeyPressed() {
		WAITTIME=31;
		ResourcesManager.getInstance().closeChatWindow();

		if(null!=timerManager.waitingRoomTimer){
			this.unregisterUpdateHandler(timerManager.waitingRoomTimer);
		}
		PageChunkFactory.getInstance().reset();
		ParticleSwipeCreator.getInstance().reset();
		if(null!=ResourcesManager.getInstance().host && null!= ResourcesManager.getInstance().host.getCharSet()){
			ResourcesManager.getInstance().host.getCharSet().clear();
		}
		if(null!=ResourcesManager.getInstance().addedPositions && null!= ResourcesManager.getInstance().addedPositions){
			ResourcesManager.getInstance().addedPositions.clear();
		}

		timerManager.beeAppearedCount=0;
		timerManager.bullAppeared=false;
		if(null!=AutoBahnConnectorPubSub.keyClear && AutoBahnConnectorPubSub.keyClear.isVisible()){
			AutoBahnConnectorPubSub.keyClear.setVisible(false);
			AutoBahnConnectorPubSub.keyClear.detachSelf();
			AutoBahnConnectorPubSub.keyClear.clearEntityModifiers();
		}
		SoundManager.changeMusic(MusicPlayed.ROCKMUSIC);

		ResourcesManager.getInstance().users= new ArrayList<UserChunk>();
		if(resourcesManager.hostImageclicked){
			//if(!ConnectionManager.isNormalFlow()){
			resourcesManager.unsubscribeRoom(); //Comment This in Prod
			//}
			resourcesManager.unloadHostSceneResources();
		}
		else{
			resourcesManager.unsubscribeRoom(); 
		}
		if(ConnectionManager.suffixEventMarker.equals(ConnectionManager.USEREVENTNORMAL)){
			ResourcesManager.getInstance().clearAndUnloadRooms();
			if(null==SceneManager.getInstance().hostGuessGameScene){
				SceneManager.getInstance().createHostGuessGameScene();
			}
			else
				SceneManager.getInstance().setScene(SceneManager.getInstance().hostGuessGameScene);

		}
		else{
			ResourcesManager.isFaceBookRequestUnAddressed = false;
			SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
			ResourcesManager.getInstance().clearAndUnloadRooms();
			if(null!=ResourcesManager.rooms){
				ResourcesManager.rooms.clear();
			}
		}
		Debug.e("Waiting Scene Back Key Pressed");

		this.back();
	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_WAITING;
	}

	@Override
	public void disposeScene() {
		this.detachChildren();
		this.detachSelf();
		this.dispose();
	}


	public void createBackground() {
		Sprite background = new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.game_host_background_region, vbom){

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		background.setSize(windowDimensions.x, windowDimensions.y);
		background.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		attachChild(background);

	}

	public void addUserChunk(UserDO user) {
		if(!hasUser(user)){
			UserChunk p = UserChunkFactory.getInstance().next(user);
			//if(null!=user && user.isHoster()){
			//p.setAlpha(.6f);
			//p.getDispNameText().setAlpha(1);
			//p.setBlendFunctionSource(GLES20.GL_ONE);
			//}
			attachIfNotAttached(p);
		}
	}

	/**
	 * @param user
	 * @return
	 */
	private boolean hasUser(UserDO user) {
		List<UserChunk> userChunks=ResourcesManager.getInstance().users;
		if(null==userChunks){
			ResourcesManager.getInstance().users = new ArrayList<UserChunk>();
			userChunks=ResourcesManager.getInstance().users;
		}
		for (Iterator<UserChunk> iterator = userChunks.iterator(); iterator.hasNext();) {
			UserChunk chunk = iterator.next();
			if(null!=chunk){
				if(null!=chunk.getUsername() && chunk.getUsername().equalsIgnoreCase(user.getUserName())){
					return true;
				}
			}
		}
		if( user.isHoster() && null!=user.getUserName() && null!=StorageManager.getInstance().getUser().getUserName() && user.getUserName().equals(StorageManager.getInstance().getUser().getUserName()) && ResourcesManager.getInstance().guessImageclicked   && (null==ResourcesManager.getInstance().roomHostedBy || user.getUserName().equals(ResourcesManager.getInstance().roomHostedBy))){
			if(waitTimeElapsed>25){
				waitTimeElapsed = 21;
			}
			ResourcesManager.getInstance().hostImageclicked = true;
			ResourcesManager.getInstance().guessImageclicked = false;
			ResourcesManager.getInstance().loadHostResources();
			SceneManager.getInstance().hostGameScene=  new HostScene();
			ToastHelper.makeCustomToastOnUIDefinedColor("You Have Hosted This Room,\nYou Can Just Spectate", Toast.LENGTH_LONG);
			ConnectionManager.getInstance().autobahnConnectorPubSub.moveLineAndsetTransparency(SceneManager.getInstance().hostGameScene);
			ConnectionManager.getInstance().autobahnConnectorPubSub.moveKeyboardAndsetTransparency(SceneManager.getInstance().hostGameScene);
			return false;
		}
		return false;
	}







	private void attachIfNotAttached(final UserChunk userChunk) {
		if(userChunk.hasParent() && !(userChunk.getParent() instanceof WaitingScene)){
			userChunk.getParent().detachChild(userChunk);
		}
		if (!userChunk.hasParent()) {
			//moveModifierForUserChunk(p);
			attachChild(userChunk);
			userChunkAdded= true;
			SoundManager.getInstance().createSound();
			/*scrollableArea.SetAlphaVisiblitiyControl(false);*/

		}
	}



	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

	}
}
