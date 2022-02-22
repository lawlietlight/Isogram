package com.efficientsciences.cowsandbulls.wordwars.managers;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;

import android.graphics.Point;
import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.domain.KeyboardChar;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomProperties;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomStream;
import com.efficientsciences.cowsandbulls.wordwars.domain.ThemePreference;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.ResourceManagerForMiniGame;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager.MusicPlayed;
import com.efficientsciences.cowsandbulls.wordwars.scenes.BaseScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.GuessScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.RealGameInterface;
import com.efficientsciences.cowsandbulls.wordwars.scenes.WaitingScene;

public class ThemeManager {


	private ThemeManager(){

	}
	private static final ThemeManager INSTANCE = new ThemeManager();

	public static ThemeManager getInstance() {
		return INSTANCE;
	}


	private static final String PREFS_NAME = "WORDWARSTMSAVEDDATAEFFICIENTSCIENCES";


	public enum THEMES{
		BLUEMAGIC, YELLOWFANTASY
	}

	public boolean changeColor=true;
	//public InputStream inputStream;
	//keyColor set to transculent grey
	public String themesKey;

	public BuildableBitmapTextureAtlas optionTextureAtlas;

	public ITextureRegion themeThumbnailOneCreative;
	public ITextureRegion themeThumbnailTwoYellow;

	public ITiledTextureRegion beeTextureRegion;
	public ITiledTextureRegion honeycombTextureRegion;
	public ITiledTextureRegion honeycombSmallTextureRegion;
	public ITiledTextureRegion coinTextureRegion;
	public ITiledTextureRegion bullTextureRegion;
	public ITiledTextureRegion redCapeTextureRegion;

	public AnimatedSprite  beeAnim;
	public AnimatedSprite  honeycombAnim;
	public AnimatedSprite  honeycombSmallAnim;
	public AnimatedSprite  bullAnim;
	public AnimatedSprite  bullRedCapeAnim;


	public MoveModifier beeFlyModifier;
	public MoveXModifier bullModifier;
	public ScaleModifier scaleModifier;
	public ScaleModifier scaleModifierForHoney;
	public ScaleModifier scaleModifierForCoin;
	public MoveModifier coinFlyAwayModifier;
	public AlphaModifier coinAlphaModifier;

	//Screen Options
	public Text themeSelection;

	public static final ThemePreference themePreferences = new ThemePreference(THEMES.BLUEMAGIC.toString());
	public Text musicOnOffText;
	public Text tutorialOnOffText;
	public Text startUpAnimationOnOffText;
	public ITextureRegion howToPlay;
	private IEntityModifierListener moveModifierCompleteListener;
	private IEntityModifierListener scaleModifierlistener;
	private IEntityModifierListener scaleModifierlistenerForHoney;

	private IEntityModifierListener scaleModifierListenerForCoin;
	private IEntityModifierListener coinFlyAwayModifierListener;
	private IEntityModifierListener coinAlphaModifierListener;

	ScaleModifier scaleModifierForCoinLocal1;
	MoveModifier coinFlyAwayModifierLocal1;
	AlphaModifier coinAlphaModifierLocal1;

	IEntityModifierListener scaleModifierListenerForCoinLocal1;
	IEntityModifierListener coinFlyAwayModifierListenerLocal1;
	IEntityModifierListener coinAlphaModifierListenerLocal1;

	ScaleModifier scaleModifierForCoinLocal2;
	MoveModifier coinFlyAwayModifierLocal2;
	AlphaModifier coinAlphaModifierLocal2;

	IEntityModifierListener scaleModifierListenerForCoinLocal2;
	IEntityModifierListener coinFlyAwayModifierListenerLocal2;
	IEntityModifierListener coinAlphaModifierListenerLocal2;

	private boolean isBeeOnScene;
	public boolean touchDownHandled=true;
	public boolean coinTouchDownHandled=true;

	private boolean isHoneyCombOnScene;
	private boolean isMoneyBagScene;
	private boolean isDailyChallengeOnScene;
	float honeycombScale= 0.50f;
	float coinScale= 0.45f;

	float honeycombShopScale= 0.30f;
	float honeycombSmallShopScale= 0.22f;
	float beeShopScale= 0.40f;
	float bullShopScale= 0.35f;
	float redCapeShopScale= 0.40f;

	private AnimatedSprite coinAnim;
	private AnimatedSprite coinAnimLocal1;
	private AnimatedSprite coinAnimLocal2;
	private Sprite moneyBag;
	private float moneyBagScale= 0.25f;
	private float bullChallengeScale= 0.62f;
	private AnimatedSprite dailyChallengeAnim;
	private AnimatedSprite lightsAnim;
	private Sprite lightsBg;

	private boolean isBullOnScene;

	/**
	 * 
	 */
	public void attachBeeFlyBy() {

		float beeAnimSta = 1f*ResourcesManager.resourceScaler;
		float beeAnimFin = beeShopScale*ResourcesManager.resourceScaler;
		if(ResourcesManager.resourceScaler>1.5){
			beeAnimSta = 1f;
			beeAnimFin = 0.6f;
		}
		final float beeanimScaleStart = beeAnimSta;
		final float beeanimScaleFinal = beeAnimFin;
		final TimerManager timerManager=TimerManager.getInstance();
		BaseScene scene=SceneManager.getInstance().getCurrentScene();
		if(scene instanceof RealGameInterface){
			final Point windowDimensions= ResourcesManager.getInstance().windowDimensions;
			if(!isBeeOnScene){
				SoundManager.getInstance().createBeeWooshSound();
			}
			if(this.beeAnim==null){
				// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
				this.beeAnim=new AnimatedSprite(-windowDimensions.x/2,-windowDimensions.y/2,ThemeManager.getInstance().beeTextureRegion, ResourcesManager.getInstance().vbom){
					private byte valueSet=0;


					public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
						isBeeOnScene=true;
						if(valueSet==0){
							if(pSceneTouchEvent.isActionDown()){

								ThemeManager.getInstance().beeAnim.clearEntityModifiers();
								ThemeManager.getInstance().beeAnim.unregisterEntityModifier(beeFlyModifier);
								if(touchDownHandled){
									ThemeManager.getInstance().beeAnim.setScale(beeanimScaleStart);
									touchDownHandled=false;
								}
								/*else{
									ThemeManager.getInstance().beeAnim.setScale(0.5f);
								}*/
								this.setPosition(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
								valueSet=1;

								return true;
							}
						}
						else{
							if(pSceneTouchEvent.isActionUp()){
								scaleModifierlistener=new IEntityModifierListener() {

									@Override
									public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
										touchDownHandled=true;
										if(null!=ThemeManager.getInstance().beeAnim  && ThemeManager.getInstance().beeAnim.hasParent()){


											ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
												@Override
												public void run() {
													/* Now it is save to remove the entity! */
													ThemeManager.getInstance().beeAnim.setScale(beeanimScaleFinal);
													ThemeManager.getInstance().beeAnim.setPosition(-windowDimensions.x/2,-windowDimensions.y/2);
													SceneManager.getInstance().guessGameScene.detachChild(ThemeManager.getInstance().beeAnim);
													isBeeOnScene=false;
													//System.gc();
													getClueFromServer(0);
													timerManager.beeAppearedCount++;

													int beeAppearancesRemaining = 0;
													UserDO localUserRef=StorageManager.getInstance().getUser();
													if(null!=localUserRef && localUserRef.getNumOfBees()>timerManager.beeAppearedCount){
														beeAppearancesRemaining=localUserRef.getNumOfBees()-1;

														localUserRef.setNumOfBees(beeAppearancesRemaining);
														StorageManager.getInstance().saveUserBeesBullsAndCoins(localUserRef);
													}
													ResourcesManager.getInstance().numberOfBeeFliesText.setText(" X "+beeAppearancesRemaining);
													//ThemeManager.getInstance().beeAnim.clearEntityModifiers();
												}

											});
										}
										else if(null!=ThemeManager.getInstance().beeAnim){
											ThemeManager.getInstance().beeAnim.setPosition(-windowDimensions.x/2,-windowDimensions.y/2);
											ThemeManager.getInstance().beeAnim.setScale(beeanimScaleFinal);
										}
									}

								};
								valueSet=0;
								scaleModifier=new ScaleModifier(beeanimScaleStart, this.getScaleX(), 0, scaleModifierlistener);
								scaleModifier.setAutoUnregisterWhenFinished(true);
								this.registerEntityModifier(scaleModifier);
								//SceneManager.getInstance().guessGameScene.unregisterTouchArea(ThemeManager.getInstance().beeAnim);

								return true;
							}
						}
						return false;
					};
				};
				this.beeAnim.setCullingEnabled(true);
				this.beeAnim.setAnchorCenter(0.5f, 0f);
				this.beeAnim.animate(75);
				this.beeAnim.setScale(beeanimScaleFinal);
			}
			if(!ThemeManager.getInstance().beeAnim.hasParent()){
				this.beeAnim.setIgnoreUpdate(false);
				scene.registerTouchArea(this.beeAnim);
				ThemeManager.getInstance().beeAnim.setScale(beeanimScaleFinal);
				moveModifierCompleteListener=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						if(null!=ThemeManager.getInstance().beeAnim  && ThemeManager.getInstance().beeAnim.hasParent())
							ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									/* Now it is save to remove the entity! */
									ThemeManager.getInstance().beeAnim.clearEntityModifiers();
									SceneManager.getInstance().guessGameScene.unregisterTouchArea(ThemeManager.getInstance().beeAnim);
									SceneManager.getInstance().guessGameScene.detachChild(ThemeManager.getInstance().beeAnim);
									isBeeOnScene=false;
								}
							});
					}
				};

				float randomTime= timerManager.randomiser.nextFloat();

				if(randomTime<0.5f){
					randomTime=randomTime+0.25f;
				}
				randomTime=randomTime*2.8f;

				float randomY1WithinLimits=timerManager.randomiser.nextFloat()*windowDimensions.y;
				if(randomY1WithinLimits<ResourcesManager.getInstance().yTextStart){
					randomY1WithinLimits= ResourcesManager.getInstance().yTextStart+(ResourcesManager.getInstance().yTextStart-randomY1WithinLimits);
				}


				float randomY2WithinLimits=timerManager.randomiser.nextInt(windowDimensions.y);
				if(randomY2WithinLimits<ResourcesManager.getInstance().yTextStart){
					randomY2WithinLimits= ResourcesManager.getInstance().yTextStart+(ResourcesManager.getInstance().yTextStart-randomY2WithinLimits);
				}

				Log.e("ATTACHBEEFLYBY: YRand", timerManager.randomiser.nextFloat()+"");
				this.beeFlyModifier = new MoveModifier(randomTime, windowDimensions.x+100, randomY1WithinLimits, -100, randomY2WithinLimits,moveModifierCompleteListener);
				this.beeFlyModifier.setAutoUnregisterWhenFinished(true);
				this.beeAnim.registerEntityModifier(this.beeFlyModifier);
				if(null!=scene.getChildByTag(EntityTagManager.shapeScrollContainerTag)){
					int zIndex=scene.getChildByTag(EntityTagManager.shapeScrollContainerTag).getZIndex();
					//Log.e("Scrollable view Z Index", zIndex+"");
					this.beeAnim.setZIndex(zIndex+5);
				}
				scene.attachChild(this.beeAnim);
			}
		}
	}

	/**
	 * 
	 */
	public void attachBullRunBy() {

		float bullAnimSta = 1f*ResourcesManager.resourceScaler;
		float bullAnimFin = bullShopScale*ResourcesManager.resourceScaler;
		if(ResourcesManager.resourceScaler>1.5){
			bullAnimSta = 1f;
			bullAnimFin = 0.5f;
		}
		final float bullanimScaleStart = bullAnimSta;
		final float bullanimScaleFinal = bullAnimFin;


		final TimerManager timerManager=TimerManager.getInstance();
		BaseScene scene=SceneManager.getInstance().getCurrentScene();
		if(scene instanceof RealGameInterface){
			final Point windowDimensions= ResourcesManager.getInstance().windowDimensions;
			if(!isBullOnScene){
				SoundManager.getInstance().createBullRunSound();
			}
			if(this.bullAnim==null){
				// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
				this.bullAnim=new AnimatedSprite(-windowDimensions.x/2,-windowDimensions.y/2,ThemeManager.getInstance().bullTextureRegion, ResourcesManager.getInstance().vbom){
					private byte valueSet=0;


					public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
						isBullOnScene=true;
						if(valueSet==0){
							if(pSceneTouchEvent.isActionDown()){

								ThemeManager.getInstance().bullAnim.clearEntityModifiers();
								ThemeManager.getInstance().bullAnim.unregisterEntityModifier(bullModifier);
								if(touchDownHandled){
									ThemeManager.getInstance().bullAnim.setScale(bullanimScaleStart);
									touchDownHandled=false;
								}
								/*else{
									ThemeManager.getInstance().bullAnim.setScale(0.5f);
								}*/
								this.setPosition(pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
								valueSet=1;

								return true;
							}
						}
						else{
							if(pSceneTouchEvent.isActionUp()){
								scaleModifierlistener=new IEntityModifierListener() {

									@Override
									public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
										// TODO Auto-generated method stub

									}

									@Override
									public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
										touchDownHandled=true;
										if(null!=ThemeManager.getInstance().bullAnim  && ThemeManager.getInstance().bullAnim.hasParent()){


											ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
												@Override
												public void run() {
													/* Now it is save to remove the entity! */
													ThemeManager.getInstance().bullAnim.setScale(bullanimScaleFinal);
													ThemeManager.getInstance().bullAnim.setPosition(-windowDimensions.x/2,-windowDimensions.y/2);
													SceneManager.getInstance().guessGameScene.detachChild(ThemeManager.getInstance().bullAnim);
													isBullOnScene=false;
													//System.gc();
													getClueFromServer(1);
													//timerManager.beeAppearedCount++;
													timerManager.bullAppeared=true;
													int bullRunsRemaining = 0;
													UserDO localUserRef=StorageManager.getInstance().getUser();
													if(null!=localUserRef && localUserRef.getNumOfBullRuns()>0){
														bullRunsRemaining=localUserRef.getNumOfBullRuns()-1;

														if(null!=ResourcesManager.getInstance().numberOfBullRunsText && null!=ResourcesManager.getInstance().bullRunHudAnim){
															ResourcesManager.getInstance().numberOfBullRunsText.setText("Used");
															ResourcesManager.getInstance().bullRunHudAnim.setAlpha(0.6f);
														}
														localUserRef.setNumOfBullRuns(bullRunsRemaining);
														StorageManager.getInstance().saveUserBeesBullsAndCoins(localUserRef);
													}
													//ResourcesManager.getInstance().numberOfBullRunsText.setText(" X "+bullRunsRemaining);
													//ThemeManager.getInstance().bullAnim.clearEntityModifiers();
												}

											});
										}
										else if(null!=ThemeManager.getInstance().bullAnim){
											ThemeManager.getInstance().bullAnim.setPosition(-windowDimensions.x/2,-windowDimensions.y/2);
											ThemeManager.getInstance().bullAnim.setScale(bullanimScaleFinal);
										}
									}

								};
								valueSet=0;
								scaleModifier=new ScaleModifier(bullanimScaleStart, this.getScaleX(), 0, scaleModifierlistener);
								scaleModifier.setAutoUnregisterWhenFinished(true);
								this.registerEntityModifier(scaleModifier);
								//SceneManager.getInstance().guessGameScene.unregisterTouchArea(ThemeManager.getInstance().beeAnim);

								return true;
							}
						}
						return false;
					};
				};
				this.bullAnim.setCullingEnabled(true);
				this.bullAnim.setAnchorCenter(0f, 0f);
				this.bullAnim.animate(75);
				this.bullAnim.setScale(bullanimScaleFinal);
			}
			if(!ThemeManager.getInstance().bullAnim.hasParent()){
				this.bullAnim.setIgnoreUpdate(false);
				scene.registerTouchArea(this.bullAnim);
				ThemeManager.getInstance().bullAnim.setScale(bullanimScaleFinal);
				moveModifierCompleteListener=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
						// TODO Auto-generated method stub
						bullAnim.setY(ResourcesManager.getInstance().yTextStart);
					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						if(null!=ThemeManager.getInstance().bullAnim  && ThemeManager.getInstance().bullAnim.hasParent())
							ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
								@Override
								public void run() {
									/* Now it is save to remove the entity! */
									ThemeManager.getInstance().bullAnim.clearEntityModifiers();
									SceneManager.getInstance().guessGameScene.unregisterTouchArea(ThemeManager.getInstance().bullAnim);
									SceneManager.getInstance().guessGameScene.detachChild(ThemeManager.getInstance().bullAnim);
									isBeeOnScene=false;
								}
							});
					}
				};

				float randomTime= timerManager.randomiser.nextFloat();

				if(randomTime<0.5f){
					randomTime=randomTime+0.25f;
				}
				randomTime=randomTime*2.8f;

				/*float randomY1WithinLimits=timerManager.randomiser.nextFloat()*windowDimensions.y;
				if(randomY1WithinLimits<ResourcesManager.getInstance().yTextStart){
					randomY1WithinLimits= ResourcesManager.getInstance().yTextStart+(ResourcesManager.getInstance().yTextStart-randomY1WithinLimits);
				}


				float randomY2WithinLimits=timerManager.randomiser.nextInt(windowDimensions.y);
				if(randomY2WithinLimits<ResourcesManager.getInstance().yTextStart){
					randomY2WithinLimits= ResourcesManager.getInstance().yTextStart+(ResourcesManager.getInstance().yTextStart-randomY2WithinLimits);
				}*/

				Log.e("ATTACHBULLRUNBY: YRand", timerManager.randomiser.nextFloat()+"");
				this.bullModifier = new MoveXModifier(randomTime, (float)(windowDimensions.x+100),  (float)-100, moveModifierCompleteListener);
				this.bullModifier.setAutoUnregisterWhenFinished(true);
				this.bullAnim.registerEntityModifier(this.bullModifier);
				if(null!=scene.getChildByTag(EntityTagManager.shapeScrollContainerTag)){
					int zIndex=scene.getChildByTag(EntityTagManager.shapeScrollContainerTag).getZIndex();
					//Log.e("Scrollable view Z Index", zIndex+"");
					this.bullAnim.setZIndex(zIndex+5);
				}
				scene.attachChild(this.bullAnim);
			}
		}
	}


	private void getClueFromServer(final int clueSelector) {

		Runnable run5 = new Runnable() {

			@Override
			public void run() {
				UserDO user = StorageManager.getInstance().getUser();
				if(null!=user){
					if(ResourcesManager.getInstance().guessImageclicked){
						user.setWordGuessed(null);
						user.setWordHosted(null);
						if(clueSelector==0){
							user.setClueWord("C");
						}
						else{
							user.setClueWord("B");
						}
					}
				}
				if(ConnectionManager.getInstance().mConnection.isConnected()){
					user.setRevealedBits(ResourcesManager.getInstance().getRevealedBits());
					ConnectionManager.getInstance().autobahnConnectorPubSub.publishClueToRoom(user);
					//Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
				}
				else{
					ConnectionManager.getInstance().prepareConnection();
					user.setRevealedBits(ResourcesManager.getInstance().getRevealedBits());
					ConnectionManager.getInstance().autobahnConnectorPubSub.publishClueToRoom(user);
				}
			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run5);
	}

	public void setKeyBoardCharTheme(KeyboardChar keyboardChar){
		themesKey=getThemePreferences().getThemeSelection();
		if(this.themesKey.equals(THEMES.BLUEMAGIC.toString())){
			keyboardChar.setColor(120/255f, 120/255f, 120/255f);
			if(ResourcesManager.getInstance().guessImageclicked){
				keyboardChar.setAlpha(0.5f);
				keyboardChar.getText().setColor(0,0,0);
			}
			else{
				//keyboardChar.setColor(0.5607843137254902f,.52745f,0.5941176470588235f);
				keyboardChar.setAlpha(0.5f);
				/*				keyboardChar.getText().setColor(0.09f,0.08f,0.07f);
				keyboardChar.getText().setAlpha(0.9f);*/
				keyboardChar.getText().setColor(0,0,0);
			}
		}
		else{
			keyboardChar.setColor(0,0,0);
			keyboardChar.setAlpha(1);
		}
	}

	public void setKeyBoardCharThemeForWin(KeyboardChar keyboardChar){
		keyboardChar.setColor(0f, 0f, 0f);
		if(ResourcesManager.getInstance().guessImageclicked){
			keyboardChar.getText().setColor(0.8f,0,0);
		}
		else{
			keyboardChar.getText().setColor(0.4f,0.7f,0);
		}
	}

	/**
	 * @param roomProperties
	 * @param scrollIndex 
	 */
	public static void resetRoomTheme(RoomProperties roomProperties, int scrollIndex) {
		//themesKey=StorageManager.getInstance().getThemePreferences().getThemeSelection();

		if(scrollIndex<20){
			//r.setColor((10*scrollIndex+50)/255f,(10*scrollIndex+10)/255f,(10*scrollIndex+5)/255f);
			if(scrollIndex%2==0){
				roomProperties.setColor(80/255f, 80/255f, 80/255f);
			}
			else{
				roomProperties.setColor(5/255f, 10/255f, 15/255f);
			}

		}
		else if(scrollIndex<40){
			//r.setColor((4.5f*scrollIndex+10)/255f,(4.5f*scrollIndex+40)/255f,(4.5f*scrollIndex+10)/255f);

			if(scrollIndex%2==0){
				roomProperties.setColor((80+15)/255f, (80+15)/255f, (80+15)/255f);
			}
			else{
				roomProperties.setColor((10)/255f, (15)/255f, (20)/255f);
			}
		}
		else if(scrollIndex<60){
			//r.setColor((2*scrollIndex+10)/255f,(2*scrollIndex+10)/255f,(2*scrollIndex+30)/255f);

			if(scrollIndex%2==0){
				roomProperties.setColor((80+30)/255f, (80+30)/255f, (80+30)/255f);
			}
			else{
				roomProperties.setColor((15)/255f, (20)/255f, (25)/255f);
			}
		}
		else if(scrollIndex<80){
			//r.setColor((1f*scrollIndex+20)/255f,(1f*scrollIndex+10)/255f,(1f*scrollIndex+10)/255f);

			if(scrollIndex%2==0){
				roomProperties.setColor((80+45)/255f, (80+45)/255f, (80+45)/255f);
			}
			else{
				roomProperties.setColor((20)/255f, (25)/255f, (30)/255f);
			}
		}
		else if(scrollIndex<=100){
			//r.setColor((0.75f*scrollIndex+10)/255f,(0.75f*scrollIndex+10)/255f,(0.75f*scrollIndex+10)/255f);

			if(scrollIndex%2==0){
				roomProperties.setColor((80+60)/255f, (80+60)/255f, (80+60)/255f);
			}
			else{
				roomProperties.setColor((25)/255f, (30)/255f, (35)/255f);
			}
		}
		else if(scrollIndex<ResourcesManager.getInstance().maxRooms){

		}
		resetRoomAlpha(roomProperties);

		/*if(this.themesKey.equals(THEMES.BLUEMAGIC.toString())){
			roomProperties.setAlpha(.4f);
		}
		else{
			roomProperties.setAlpha(.4f);
		}*/
	}


	/**
	 * @param roomProperties
	 */
	public static void setRoomHostedTheme(RoomProperties roomProperties) {
		if(roomProperties.isRoomDisabledForHostingGuessing()){
			roomProperties.setColor(0.7f,0.5f,0.5f);
			//roomProperties.setAlpha(0.9f);
		}
		roomProperties.attachRoomHostedText(roomProperties);
	}


	/**
	 * @param roomProperties
	 */
	public static void setRoomGuessingDisabledTheme(Color disabledGuessingRoomColor, RoomProperties roomProperties) {

		setRoomColor(disabledGuessingRoomColor,roomProperties);
		//roomProperties.setAlpha(0.9f);
	}

	public static void setRoomColor(Color color,RoomProperties roomProperties){
		//themesKey=StorageManager.getInstance().getThemePreferences().getThemeSelection();

		//r.setColor((10*scrollIndex+50)/255f,(10*scrollIndex+10)/255f,(10*scrollIndex+5)/255f);
		if(roomProperties.getIndex()%2==0){
			roomProperties.setColor(color.getRed(), color.getGreen()-0.1f, color.getBlue()-0.15f);
			roomProperties.setAlpha(0.9f);
		}
		else{
			roomProperties.setColor(color.getRed()+0.05f, color.getGreen()-0.05f, color.getBlue()-0.05f);
			//this.setAlpha(0.85f);
		}

	}

	/**
	 * @param roomProperties
	 */
	public static void resetRoomAlpha(RoomProperties roomProperties) {
		roomProperties.setAlpha(1f);
	}

	/**
	 * @param roomProperties
	 */
	public static void resetRoomHighlight(RoomProperties roomProperties) {
		//+4 Rooms And -4 Rooms
		int noOfRoomsToResetBelow=4;
		int start = roomProperties.getIndex() - noOfRoomsToResetBelow;
		int noOfRoomsToResetAbove = (noOfRoomsToResetBelow*2);
		for(int i = start; i<start+noOfRoomsToResetAbove; i++){
			if(i<0){
				continue;
			}
			if(i>=100){
				break;
			}
			RoomProperties roomToResetHighlight = ResourcesManager.rooms.get(i);
			if(null!=roomToResetHighlight)
				roomToResetHighlight.actionClicked = false;
		}
	}

	public static int randomInRange(int min, int max) {
		return min + (int)(Math.random() * (max - min + 1));
	}

	/**
	 * @param pageChunk
	 */
	public void setPageChunkTheme(PageChunk pageChunk) {
		if(this.themesKey.equals(THEMES.BLUEMAGIC.toString())){
			if(ResourcesManager.getInstance().guessImageclicked){
				pageChunk.setColor(.4f,.4f,.4f);
				pageChunk.setAlpha(.4f);

				pageChunk.getDispNameText().setColor(.25f,.5f,.5f);
				pageChunk.getWordText().setColor(.05f,.07f,.09f);
				pageChunk.getCowsBullsText().setColor(0.3f,.3f,0.1f);
			}
			else{
				pageChunk.setColor(.45f,.40f,.45f);
				pageChunk.setAlpha(.3f);

				pageChunk.getDispNameText().setColor(.25f,.5f,.5f);
				pageChunk.getWordText().setColor(.20f,.22f,.20f);
				pageChunk.getCowsBullsText().setColor(0.2f,.4f,0.3f);
			}
		}
		else{
			pageChunk.setColor(.40f,.35f,.40f);
			pageChunk.setAlpha(.2f);

			pageChunk.getDispNameText().setColor(.25f,.5f,.5f);
			pageChunk.getWordText().setColor(.20f,.22f,.20f);
			pageChunk.getCowsBullsText().setColor(0.5f,.5f,0.1f);
		}
	}

	public ThemePreference getThemePreferences(){
		return themePreferences;
	}

	public void attachHoneyComb(Sprite backgroundColumn1) {
		if(!isHoneyCombOnScene){
			SoundManager.getInstance().createBeeWooshSound();
		}
		if(this.honeycombAnim==null){
			// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
			this.honeycombAnim=new AnimatedSprite(50 *ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2,ThemeManager.getInstance().honeycombTextureRegion, ResourcesManager.getInstance().vbom){
				private byte valueSet=0;


				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					isHoneyCombOnScene=true;
					if(valueSet==0){
						if(pSceneTouchEvent.isActionDown()){
							ThemeManager.getInstance().honeycombAnim.setAlpha(0.7f);
							valueSet=1;

							return true;
						}
					}
					else{
						if(pSceneTouchEvent.isActionUp()){
							scaleModifierlistenerForHoney=new IEntityModifierListener() {

								@Override
								public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
									touchDownHandled=true;
									ThemeManager.getInstance().honeycombAnim.setAlpha(1f);
									if(null!=ThemeManager.getInstance().honeycombAnim  && ThemeManager.getInstance().honeycombAnim.hasParent()){

									}
									else if(null!=ThemeManager.getInstance().honeycombAnim){
									}
								}

							};
							valueSet=0;
							scaleModifierForHoney=new ScaleModifier(1f, honeycombScale*ResourcesManager.resourceScaler, honeycombScale*ResourcesManager.resourceScaler*1.1f, scaleModifierlistenerForHoney);
							scaleModifierForHoney.setAutoUnregisterWhenFinished(true);
							this.registerEntityModifier(scaleModifierForHoney);

							return true;
						}
					}
					return false;
				};
			};
			this.honeycombAnim.setCullingEnabled(true);
			this.honeycombAnim.setAnchorCenter(0f, 0.5f);
			//long[] pFrameDurations={260,250,180,10,20,25,30,50};
			long[] pFrameDurations={260,250,180,10,20,45,190,210};
			this.honeycombAnim.animate(pFrameDurations);
			this.honeycombAnim.setTag(EntityTagManager.honeycombAnim);
			this.honeycombAnim.setScale(honeycombScale*ResourcesManager.resourceScaler);
		}
		if(!ThemeManager.getInstance().honeycombAnim.hasParent()){
			this.honeycombAnim.setIgnoreUpdate(false);
			//banner.registerTouchArea(this.honeycombAnim);
			ThemeManager.getInstance().honeycombAnim.setScale(honeycombScale*ResourcesManager.resourceScaler);
			backgroundColumn1.attachChild(this.honeycombAnim);
		}

	}

	public AnimatedSprite attachAnimSpritesForShop(Sprite backgroundColumn,ITiledTextureRegion textureRegion,int selector, int columnIndex) {
		AnimatedSprite  localAnim=new AnimatedSprite(50 *ResourcesManager.resourceScaler,backgroundColumn.getHeight()/2,textureRegion, ResourcesManager.getInstance().vbom);
		localAnim.setCullingEnabled(true);
		localAnim.setAnchorCenter(0f, 0.5f);
		//long[] pFrameDurations={260,250,180,10,20,25,30,50};
		//long[] pFrameDurations={260,250,180,10,20,45,190,210};
		localAnim.animate(150);
		//localAnim.setTag(EntityTagManager.honeycombAnim);

		switch (selector) {
		case 1:
			if(columnIndex==1){
				localAnim.setScale(beeShopScale*ResourcesManager.resourceScaler);
			}
			else{
				localAnim.setScale(beeShopScale*ResourcesManager.resourceScaler);
			}
			break;
		case 2:
			if(columnIndex==1){
				localAnim.setScale(beeShopScale*ResourcesManager.resourceScaler);
			}
			else{
				localAnim.setScale(beeShopScale*ResourcesManager.resourceScaler);
			}
			break;
		case 3:
			if(columnIndex==1){
				localAnim.setScale(honeycombSmallShopScale*ResourcesManager.resourceScaler);
			}
			else{
				localAnim.setScale(honeycombSmallShopScale*ResourcesManager.resourceScaler);
			}
			break;
		case 4:
			if(columnIndex==1){
				localAnim.setScale(honeycombShopScale*ResourcesManager.resourceScaler);
			}
			else{
				localAnim.setScale(honeycombShopScale*ResourcesManager.resourceScaler);
			}
			break;
		case 5:
			if(columnIndex==1){
				localAnim.setScale(bullShopScale*ResourcesManager.resourceScaler);
			}
			else{
				localAnim.setScale(bullShopScale*ResourcesManager.resourceScaler);
			}
			break;
		case 6:
			if(columnIndex==1){
				localAnim.setScale(bullShopScale*ResourcesManager.resourceScaler);
			}
			else{
				localAnim.setScale(redCapeShopScale*ResourcesManager.resourceScaler);
			}
			break;
			/*case 7:
			if(columnIndex==1){

			}
			else{

			}
			break;
		case 8:
			if(columnIndex==1){

			}
			else{

			}
			break;*/
		default:
			break;
		}
		if(!localAnim.hasParent()){
			localAnim.setIgnoreUpdate(false);
			//banner.registerTouchArea(localAnim);
			//localAnim.setScale(honeycombScale*ResourcesManager.resourceScaler);
			backgroundColumn.attachChild(localAnim);
		}
		return localAnim;
	}

	public void attachMoneyBag(MenuScene scene) {
		final Point windowDimensions= ResourcesManager.getInstance().windowDimensions;
		if(!isMoneyBagScene){
			SoundManager.getInstance().createBeeWooshSound();
		}
		// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
		this.moneyBag=new Sprite(12 *ResourcesManager.resourceScaler,windowDimensions.y*3/4,ResourcesManager.getInstance().moneybagRegion, ResourcesManager.getInstance().vbom){
			private byte valueSet=0;


			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				isMoneyBagScene=true;
				if(valueSet==0){
					if(pSceneTouchEvent.isActionDown()){
						ThemeManager.getInstance().moneyBag.setAlpha(0.7f);
						valueSet=1;

						return true;
					}
				}
				else{
					if(pSceneTouchEvent.isActionUp()){
						ThemeManager.getInstance().moneyBag.setAlpha(1f);
						SceneManager.getInstance().createShopBannersScene();
						valueSet=0;
						return true;
					}
				}
				return false;
			};
		};
		this.moneyBag.setCullingEnabled(true);
		this.moneyBag.setAnchorCenter(0f, 1f);
		this.moneyBag.setScale(moneyBagScale*ResourcesManager.resourceScaler);
		if(!ThemeManager.getInstance().moneyBag.hasParent()){
			this.moneyBag.setIgnoreUpdate(false);
			scene.registerTouchArea(this.moneyBag);
			ThemeManager.getInstance().moneyBag.setScale(moneyBagScale*ResourcesManager.resourceScaler);
			scene.attachChild(this.moneyBag);
		}

	}

	public void attachDailyChallenge(MenuScene scene) {
		final Point windowDimensions= ResourcesManager.getInstance().windowDimensions;
		if(!isDailyChallengeOnScene){
			SoundManager.getInstance().createAddCoinMediumSound();
		}
		// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
		this.dailyChallengeAnim=new AnimatedSprite(windowDimensions.x,windowDimensions.y/2,ResourcesManager.getInstance().dailyChallengeRegion, ResourcesManager.getInstance().vbom){
			private byte valueSet=0;


			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				isDailyChallengeOnScene=true;
				if(valueSet==0){
					if(pSceneTouchEvent.isActionDown()){
						dailyChallengeAnim.setAlpha(0.7f);
						valueSet=1;

						return true;
					}
				}
				else{
					if(pSceneTouchEvent.isActionUp()){
						dailyChallengeAnim.setAlpha(1f);
						ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								ResourcesManager.getInstance().activity.destroyLeadBoltAd();
								//Play byte 1
								ConnectionManager.suffixEventMarker=ConnectionManager.USEREVENTCHALLENGE;
								joinRandomDailyChallengeRoom();



								/*if(FacebookManager.sUserLoggedIn){
									FacebookManager.inviteFriends();
								}
								else {
									FacebookManager.loginFBAndInvite();
								}*/
								/*FacebookManager.checkUserLoggedIn();
								if(FacebookManager.sUserLoggedIn){
									FacebookManager.requestForPublishPermissions(Session.getActiveSession());
									FacebookManager.post(FacebookManager.sFirstName,"Play With ");
								}
								else {
									FacebookManager.loginAndPost("test");
								}*/
							}

						});

						valueSet=0;
						return true;
					}
				}
				return false;
			};
		};
		this.dailyChallengeAnim.setCullingEnabled(true);
		this.dailyChallengeAnim.setAnchorCenter(1f, 0.5f);
		this.dailyChallengeAnim.animate(150);
		this.dailyChallengeAnim.setScale(bullChallengeScale*ResourcesManager.resourceScaler);
		if(!dailyChallengeAnim.hasParent()){
			this.dailyChallengeAnim.setIgnoreUpdate(false);
			scene.registerTouchArea(this.dailyChallengeAnim);
			dailyChallengeAnim.setScale(bullChallengeScale*ResourcesManager.resourceScaler);
			scene.attachChild(this.dailyChallengeAnim);
		}

	}

	public void attachLightsAnim(MenuScene scene) {
		final Point windowDimensions= ResourcesManager.getInstance().windowDimensions;
		// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
		this.lightsAnim=new AnimatedSprite(0,0,ResourcesManager.getInstance().lightsRegion, ResourcesManager.getInstance().vbom);
		this.lightsAnim.setCullingEnabled(true);
		this.lightsAnim.setAnchorCenter(0.5f, 0.5f);
		this.lightsAnim.animate(300);
		if(!lightsAnim.hasParent()){
			this.lightsAnim.setIgnoreUpdate(false);
			lightsAnim.setScale(1.1f);
			this.lightsBg =new Sprite((windowDimensions.x/2)-4*ResourcesManager.resourceScaler,windowDimensions.y,ResourcesManager.getInstance().lights_BG_Region, ResourcesManager.getInstance().vbom){
				private byte valueSet=0;


				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionDown()){
						valueSet=1;

						return true;
					}
					else if(pSceneTouchEvent.isActionUp() && valueSet==1){
						ResourceManagerForMiniGame.isBeeFly= TimerManager.getInstance().randomiser.nextBoolean();
						//ResourceManagerForMiniGame.isBeeFly= false;
						SceneManager.getInstance().earnCoinsMiniGameScene();
						valueSet=0;
						return true;
					}

					return false;
				};
			};

			this.lightsBg.attachChild(this.lightsAnim);
			//this.lightsAnim.setScale(bullChallengeScale*ResourcesManager.resourceScaler);
			this.lightsBg.setScale((bullChallengeScale+0.1f)*ResourcesManager.resourceScaler);
			this.lightsBg.setScaleX((bullChallengeScale+0.1f)*ResourcesManager.resourceScaler);
			this.lightsBg.setAnchorCenter(0.5f, 0.5f);
			this.lightsAnim.setPosition(this.lightsBg.getWidth()/2, this.lightsBg.getHeight()/2);

			scene.registerTouchArea(this.lightsBg);

			scene.attachChild(this.lightsBg);
		}

	}

	//For Daily Challenge
	private void joinRandomDailyChallengeRoom() {

		final TimerManager timerManager=TimerManager.getInstance();
		timerManager.randomiser=new Random(new Date().getTime());

		final RoomStream randomGuessRoomDetails= new RoomStream();
		UserDO participant = StorageManager.getInstance().getUser();
		if(null==randomGuessRoomDetails.getParticipants()){
			randomGuessRoomDetails.setParticipants(new ArrayList<UserDO>());
		}

		if(!randomGuessRoomDetails.getParticipants().contains(participant)){
			randomGuessRoomDetails.getParticipants().add(participant);
		}
		int OPENINTERVALRANGE =5;
		//GETS A ROOM in closed interval [6001 to 6005]
		//ResourcesManager.isTutorialMode = true;
		final int randomRoomIndex=6001+timerManager.randomiser.nextInt(OPENINTERVALRANGE);
		StorageManager.getInstance().getUser().setHostedGuessedRoomIndex(randomRoomIndex);
		ConnectionManager.mPathRoomNumberSuffix=randomRoomIndex;
		ResourcesManager.setTutorialModeEnabled(ConnectionManager.mPathRoomNumberSuffix);

		RoomProperties room = new RoomProperties(0, 0, 0, 0, randomRoomIndex, ResourcesManager.getInstance().vbom);
		createRoomForRandomPlay(randomGuessRoomDetails,room,randomRoomIndex);

		Runnable run5 = new Runnable() {

			@Override
			public void run() {
				try {

					ResourcesManager.getInstance().guessImageclicked =true;
					ResourcesManager.getInstance().hostImageclicked =false;
					randomGuessRoomDetails.setIndex(randomRoomIndex);
					randomGuessRoomDetails.setRoomState(RoomProperties.STATE_WAITING);

					if(ConnectionManager.getInstance().mConnection.isConnected()){
						//SoundManager.getInstance().createWhipSound();
						ResourcesManager.isResponseGot = false;
						ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(randomGuessRoomDetails);
						Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
					}
					else{
						ConnectionManager.getInstance().prepareConnection();
						ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(randomGuessRoomDetails);
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
		ResourcesManager.getInstance().activity.runOnUiThread(run5); 

		StorageManager.getInstance().getUser().setHoster(false);
		if(THEMES.BLUEMAGIC.toString().equals(ThemeManager.getInstance().getThemePreferences().getThemeSelection())){
			SoundManager.changeMusic(MusicPlayed.BEEMUSIC);
		}
		else{
			SoundManager.changeMusic(MusicPlayed.FASTMARCH);
		}
		SceneManager.getInstance().createWaitingScene();
		WaitingScene.waitTimeElapsed=25;
		((WaitingScene)SceneManager.getInstance().waitingScene).minimumNumOfPlayers =1;
		//SceneManager.getInstance().createGuessScene();
	}

	public void attachCoins(GuessScene scene) {
		final Point windowDimensions= ResourcesManager.getInstance().windowDimensions;
		if(this.coinAnim==null){
			// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
			this.coinAnim=new AnimatedSprite(windowDimensions.x/4,windowDimensions.y/2+80*ResourcesManager.resourceScaler,ThemeManager.getInstance().coinTextureRegion, ResourcesManager.getInstance().vbom){
				private byte valueSet=0;

				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(valueSet==0){
						if(pSceneTouchEvent.isActionDown()){

							if(coinTouchDownHandled){
								coinTouchDownHandled=false;
								this.setScale(0.8f*ResourcesManager.resourceScaler);
							}
							valueSet=1;

							return true;
						}
					}
					else{
						this.setScale(coinScale*ResourcesManager.resourceScaler);
						if(pSceneTouchEvent.isActionUp()){
							valueSet=0;
							return true;
						}
					}
					return false;
				};
			};
			this.coinAnim.setCullingEnabled(true);
			this.coinAnim.setAnchorCenter(0f, 1f);
			long[] pFrameDurations={60,80,80,80,80,65,60,50};
			this.coinAnim.animate(pFrameDurations);
			this.coinAnim.setScale(coinScale*ResourcesManager.resourceScaler);

			this.coinAnimLocal1=new AnimatedSprite(windowDimensions.x/4,windowDimensions.y/2+80*ResourcesManager.resourceScaler,ThemeManager.getInstance().coinTextureRegion, ResourcesManager.getInstance().vbom);
			this.coinAnimLocal1.setCullingEnabled(true);
			this.coinAnimLocal1.setAnchorCenter(0f, 1f);
			this.coinAnimLocal1.animate(pFrameDurations);
			this.coinAnimLocal1.setScale(coinScale*ResourcesManager.resourceScaler);

			this.coinAnimLocal2=new AnimatedSprite(windowDimensions.x/4,windowDimensions.y/2+80*ResourcesManager.resourceScaler,ThemeManager.getInstance().coinTextureRegion, ResourcesManager.getInstance().vbom);
			this.coinAnimLocal2.setCullingEnabled(true);
			this.coinAnimLocal2.setAnchorCenter(0f, 1f);
			this.coinAnimLocal2.animate(pFrameDurations);
			this.coinAnimLocal2.setScale(coinScale*ResourcesManager.resourceScaler);
		}

		if(!ThemeManager.getInstance().coinAnim.hasParent()){
			this.coinAnim.setIgnoreUpdate(false);

			if(null!=SceneManager.getInstance().getCurrentSceneType() && SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_GUESS){
				scaleModifierListenerForCoin=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
						ThemeManager.getInstance().coinAnim.setAlpha(1f);
						if(null!=ThemeManager.getInstance().coinAnim  && ThemeManager.getInstance().coinAnim.hasParent()){
						}
						else if(null!=ThemeManager.getInstance().coinAnim){
						}
					}
				};

				coinAlphaModifierListener=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								coinAnim.detachSelf();
							}
						});
					}
				};

				coinFlyAwayModifierListener=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
					}
				};

				coinFlyAwayModifier = new MoveModifier(0.8f, windowDimensions.x/4,windowDimensions.y/2+80*ResourcesManager.resourceScaler,ResourcesManager.getInstance().hudCoin.getX(),windowDimensions.y,coinFlyAwayModifierListener);

				coinAlphaModifier = new AlphaModifier(1.1f, 1, 0.5f,coinAlphaModifierListener);
				scaleModifierForCoin=new ScaleModifier(0.8f, coinScale*ResourcesManager.resourceScaler, coinScale*ResourcesManager.resourceScaler/2, scaleModifierListenerForCoin);
				scaleModifierForCoin.setAutoUnregisterWhenFinished(true);
				coinAlphaModifier.setAutoUnregisterWhenFinished(true);
				coinFlyAwayModifier.setAutoUnregisterWhenFinished(true);
				coinAnim.registerEntityModifier(new ParallelEntityModifier(coinFlyAwayModifier,scaleModifierForCoin, coinAlphaModifier));
			}
			ThemeManager.getInstance().coinAnim.setScale(coinScale*ResourcesManager.resourceScaler);
			scene.attachChild(this.coinAnim);
		}
		else if(!ThemeManager.getInstance().coinAnimLocal1.hasParent()){


			if(null!=SceneManager.getInstance().getCurrentSceneType() && SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_GUESS){


				scaleModifierListenerForCoinLocal1=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
						coinAnimLocal1.setAlpha(1f);
					}
				};

				coinAlphaModifierListenerLocal1=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								coinAnimLocal1.detachSelf();
							}
						});
					}
				};

				coinFlyAwayModifierListenerLocal1=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
					}
				};

				coinFlyAwayModifierLocal1 = new MoveModifier(0.6f, windowDimensions.x/4,windowDimensions.y/2+80*ResourcesManager.resourceScaler,ResourcesManager.getInstance().hudCoin.getX(),windowDimensions.y,coinFlyAwayModifierListenerLocal1);

				coinAlphaModifierLocal1 = new AlphaModifier(0.8f, 1, 0.5f,coinAlphaModifierListenerLocal1);
				scaleModifierForCoinLocal1=new ScaleModifier(0.6f, coinScale*ResourcesManager.resourceScaler, coinScale*ResourcesManager.resourceScaler/2, scaleModifierListenerForCoinLocal1);
				scaleModifierForCoinLocal1.setAutoUnregisterWhenFinished(true);
				coinAlphaModifierLocal1.setAutoUnregisterWhenFinished(true);
				coinFlyAwayModifierLocal1.setAutoUnregisterWhenFinished(true);
				coinAnimLocal1.registerEntityModifier(new ParallelEntityModifier(coinFlyAwayModifierLocal1,scaleModifierForCoinLocal1, coinAlphaModifierLocal1));
			}
			this.coinAnimLocal1.setScale(coinScale*ResourcesManager.resourceScaler);
			scene.attachChild(this.coinAnimLocal1);
		}
		else if(!ThemeManager.getInstance().coinAnimLocal2.hasParent()){


			if(null!=SceneManager.getInstance().getCurrentSceneType() && SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_GUESS){


				scaleModifierListenerForCoinLocal2=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
						coinAnimLocal2.setAlpha(1f);
					}
				};

				coinAlphaModifierListenerLocal2=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								coinAnimLocal2.detachSelf();
							}
						});
					}
				};

				coinFlyAwayModifierListenerLocal2=new IEntityModifierListener() {

					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

					}

					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						coinTouchDownHandled=true;
					}
				};

				coinFlyAwayModifierLocal2 = new MoveModifier(0.6f, windowDimensions.x/4,windowDimensions.y/2+80*ResourcesManager.resourceScaler,ResourcesManager.getInstance().hudCoin.getX(),windowDimensions.y,coinFlyAwayModifierListenerLocal2);

				coinAlphaModifierLocal2 = new AlphaModifier(0.8f, 1, 0.5f,coinAlphaModifierListenerLocal2);
				scaleModifierForCoinLocal2=new ScaleModifier(0.6f, coinScale*ResourcesManager.resourceScaler, coinScale*ResourcesManager.resourceScaler/2, scaleModifierListenerForCoinLocal2);
				scaleModifierForCoinLocal2.setAutoUnregisterWhenFinished(true);
				coinAlphaModifierLocal2.setAutoUnregisterWhenFinished(true);
				coinFlyAwayModifierLocal2.setAutoUnregisterWhenFinished(true);
				coinAnimLocal2.registerEntityModifier(new ParallelEntityModifier(coinFlyAwayModifierLocal2,scaleModifierForCoinLocal2, coinAlphaModifierLocal2));
			}
			this.coinAnimLocal2.setScale(coinScale*ResourcesManager.resourceScaler);
			scene.attachChild(this.coinAnimLocal2);
		}

	}


	public AnimatedSprite getCoinToHud() {
		// Negative dimensions set to avoid glitch of bee appearing before getting detached from scene for milli seconds after the scale modifier finishes
		ResourcesManager.getInstance().hudCoin=new AnimatedSprite(0,0,ThemeManager.getInstance().coinTextureRegion, ResourcesManager.getInstance().vbom);
		ResourcesManager.getInstance().hudCoin.setAnchorCenter(0f, 1f);
		ResourcesManager.getInstance().hudCoin.setAlpha(1);
		long[] pFrameDurations={58,60,60,60,60,50,50,45};
		ResourcesManager.getInstance().hudCoin.animate(pFrameDurations);
		ResourcesManager.getInstance().hudCoin.setIgnoreUpdate(false);
		ResourcesManager.getInstance().hudCoin.setScale((coinScale)*ResourcesManager.resourceScaler/2);
		return ResourcesManager.getInstance().hudCoin;
	}


	private void createRoomForRandomPlay(RoomStream roomFromServer, RoomProperties r,int scrollIndex) {
		//Add rooms to Map
		ResourcesManager.rooms.put(scrollIndex, r);
		r.setIndex(scrollIndex);
		r.setNoOfParticipants(1);
		r.setRoomHostedBy("#WORDCHALLENGE#");
		/*Set<Entry<String, RoomStream>> entrySet = ResourcesManager.roomsState.entrySet();
		if(null != entrySet)
		for (Entry<String, RoomStream> entrySetValue : entrySet) {
			Integer roomIndex=Integer.parseInt(entrySetValue.getKey());
			RoomStream room = entrySetValue.getValue();
		}*/
		//Copy data Fetched from server to Local Room r
		if(null!=roomFromServer){
			r.setRoomState(roomFromServer.getRoomState());
			r.setRoomHostedBy(roomFromServer.getRoomHostedBy());
			r.setNoOfParticipants(roomFromServer.getNoOfParticipants());
		}
	}
}
