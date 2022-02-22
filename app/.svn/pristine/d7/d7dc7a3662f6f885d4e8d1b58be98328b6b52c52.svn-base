package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.ArrayList;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.primitive.Gradient;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.ScreenGrabber;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBackOut;
import org.andengine.util.modifier.ease.EaseBounceIn;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseElasticOut;
import org.andengine.util.modifier.ease.EaseExponentialIn;
import org.andengine.util.modifier.ease.EaseExponentialOut;
import org.andengine.util.modifier.ease.IEaseFunction;

import android.content.Intent;
import android.graphics.Point;
import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.graphics.GameScreenCapture;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ParticleSwipeCreator;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.FacebookManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;
import com.efficientsciences.cowsandbulls.wordwars.managers.TimerManager;
import com.efficientsciences.cowsandbulls.wordwars.scenes.MainMenuScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.RealGameInterface;
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnectorPubSub;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

public class Notification extends Sprite{


	private String notificationText;
	private Text text;
	Sprite rectYes;
	Sprite rectNo;
	boolean actionYesRectDown=false;
	boolean actionNoRectDown=false;
	boolean flagIncrease = true;
	public CameraScene mPauseScene;

	public AnimatedSprite beeCaught;
	public Sprite ballCaught;
	protected Sprite backButton = null;
	private Sprite fbButton;


	//private Sprite tutorialBack;
	private Sprite tutorialTitle;
	private Sprite tutorialStep1;
	private Sprite tutorialStep2;
	private Sprite tutorialStep3;
	private Sprite tutorialStep4;
	private Sprite tutorialPrev;
	private Sprite tutorialNext;
	private Sprite tutorialFinish;
	public int currentStepVisible= 1;
	private IEntityModifierListener listenerStepIn;

	private IEntityModifierListener listenerStepOut;


	public MoveXModifier stepInMoveMod;
	private MoveXModifier stepOutMoveMod;


	public Notification(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, String notificationText) {

		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		//this.setScale(ResourcesManager.resourceScaler);
		Point windowDimensions=ResourcesManager.getInstance().windowDimensions;
		this.notificationText=notificationText;


		this.mPauseScene = new CameraScene(ResourcesManager.getInstance().camera);
		this.mPauseScene.setBackgroundEnabled(false);
		SceneManager.getInstance().getCurrentScene().setChildScene(mPauseScene, false, true, true);

		//this.setColor(.9f,.7f,.9f);
		this.setAlpha(.95f);

		text=new Text(40*ResourcesManager.resourceScaler, pHeight/2, ResourcesManager.mSmallFont, this.notificationText, pVertexBufferObjectManager);
		text.setAnchorCenter(0,0.5f);
		text.setColor(.90f,.92f,.90f,0.9f);
		text.setBlendFunctionSource(GLES20.GL_ALWAYS);
		text.setScale(ResourcesManager.resourceScaler);

		//this.setWidth(windowDimensions.x/2);
		this.setHeight(windowDimensions.y/2f);
		this.setAnchorCenter(0.5f,0.5f);


		beeCaught = new AnimatedSprite(this.getWidth()/2,
				this.getHeight()*3/4 ,ThemeManager.getInstance().beeTextureRegion, pVertexBufferObjectManager);
		beeCaught.setScale(0.4f*ResourcesManager.resourceScaler);
		beeCaught.animate(80);
		this.attachChild(beeCaught);

		this.attachChild(text);

		rectYes= new Sprite( pWidth/4,  pHeight *3/4, ResourcesManager.getInstance().leave_region, pVertexBufferObjectManager);
		//Text yesText = new Text(rectYes.getWidth()/2, rectYes.getHeight()/2, ResourcesManager.mSmallFont, "YES", new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		//yesText.setColor(.80f,.82f,.80f,0.9f);
		//rectYes.attachChild(yesText);
		rectYes.setScale(ResourcesManager.resourceScaler*0.4f);

		//rectYes.setColor(1f,1f,1f,0.7f);
		this.attachChild(rectYes);
		//Body circleBody = PhysicsFactory.createCircleBody(ResourcesManager.getInstance().mPhysWorld, rectYes, BodyType.StaticBody, MainActivity.mCellFixtureDef);


		rectNo= new Sprite( pWidth*3/4, pHeight *3/4, ResourcesManager.getInstance().stay_region, pVertexBufferObjectManager);
		//Text noText = new Text(rectNo.getWidth()/2, rectNo.getHeight()/2, ResourcesManager.mSmallFont, "NO", new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		//noText.setColor(.80f,.82f,.80f,0.9f);
		//rectNo.attachChild(noText);
		rectNo.setScale(ResourcesManager.resourceScaler*0.4f);
		//rectNo.setColor(1f,1f,1f,0.7f);
		this.attachChild(rectNo);
		/*this.setZIndex(1000);

		//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(false);
		if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){
			((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene().registerTouchArea(this);
			((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene().sortChildren();
		}*/
		this.mPauseScene.registerTouchArea(this);
		/*this.setIgnoreUpdate(false);
		this.mPauseScene.setIgnoreUpdate(false);*/
		if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
			this.setPosition(-pX, -pY);
			ResourcesManager.getInstance().notificationListener  = new IEntityModifierListener(){
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					//SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(true);
				} 
			};


			ResourcesManager.getInstance().notificationModifier = new MoveModifier(0.5f, pX , pY-300, pX, pY,ResourcesManager.getInstance().notificationListener, EaseBounceOut.getInstance());
			this.registerEntityModifier(ResourcesManager.getInstance().notificationModifier);
		}
		else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){
			this.setPosition(pX, pY);
			ResourcesManager.getInstance().notificationListener  = new IEntityModifierListener(){
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(true);
				} 
			};


			ResourcesManager.getInstance().notificationModifier = new FadeInModifier(0.3f,  EaseBounceOut.getInstance());
			this.registerEntityModifier(ResourcesManager.getInstance().notificationModifier);
		}

		attachBackButton(pVertexBufferObjectManager, "backButtonPress");
		this.mPauseScene.attachChild(this);
		this.mPauseScene.setBackgroundEnabled(false);
		//Body circleBody2 = PhysicsFactory.createCircleBody(ResourcesManager.getInstance().mPhysWorld, rectNo, BodyType.StaticBody, MainActivity.mCellFixtureDef);
	}

	Color firstColor = new Color(0.8431f, 0, 0.2705f,0.80f);
	Color secondColor = new Color(0.9568f, 0, 0.3058f ,0.80f);

	Color firstColorForBlue = new Color(0.887f, 0.76f, 0f ,0.80f);
	Color secondColorForBlue = new Color(0.9568f, 0.5f, 0f ,0.80f);

	private KeyboardString createWonWordKey(UserDO user){
		//Keys with Codes For KeyBoard
		if(null!=user && null!=user.getWordGuessed() && !user.getWordGuessed().trim().isEmpty()){
			Point windowDimensions = ResourcesManager.getInstance().windowDimensions;
			int length =user.getWordGuessed().trim().length();
			KeyboardString keyboardString = new KeyboardString();
			KeyboardChar[] keys=new KeyboardChar[length];
			char[] codes = user.getWordGuessed().toCharArray();

			//sampleText to get Dimensions
			Text dimensionText= new Text( 20, 20, ResourcesManager.getInstance().mBitmapFont, "Q", ResourcesManager.getInstance().vbom);
			dimensionText.setScale(0.8f*ResourcesManager.resourceScaler);


			float centerX =windowDimensions.x/2;
			float pXLoc= 0;
			float pYLoc=0;

			//KeyboardCharRectangle width n Height
			float pWidth=windowDimensions.x/26;
			float pHeight=dimensionText.getHeightScaled();
			Text[] keyBoardBitmapText=new Text[length];
			float totalWidth = 0;
			for (int i = 0; i < keys.length; i++) {


				keyBoardBitmapText[i]= new Text( (pWidth+5)/2, 25*ResourcesManager.resourceScaler, ResourcesManager.getInstance().mBitmapFont, codes[i]+"", new TextOptions(AutoWrap.LETTERS,pWidth,HorizontalAlign.CENTER), ResourcesManager.getInstance().vbom);
				keyBoardBitmapText[i].setScale(0.9f*ResourcesManager.resourceScaler);


				KeyboardChar keyboardChar;
				Log.e(" Host pHeight", pHeight+"");
				keyboardChar=new KeyboardChar(pXLoc, pYLoc, pWidth+5, pHeight, codes[i]+"",keyBoardBitmapText[i], ResourcesManager.getInstance().vbom);
				/*keyboardChar.setKeyboardPositionX(pX);
					keyboardChar.setKeyboardPositionY(pY);
				 */
				pXLoc=pXLoc+keyboardChar.getWidthScaled();
				totalWidth = keyboardChar.getWidthScaled()*length;



				keyboardChar.setBlendFunctionDestination(GLES20.GL_ALWAYS);
				keyboardChar.attachChild(keyBoardBitmapText[i]);
				keyboardChar.setAnchorCenter(0, 0);
				//keyboard.attachChild(keyboardChar);
				keys[i]=keyboardChar;

			}

			keyboardString.setKeyCharArray(keys);
			return keyboardString;
		}
		return null;
	}

	Gradient createGardient(String themesKey){
		final float CameraHeight = this.getHeightScaled();
		final float CameraWidth = this.getWidthScaled();
		int directionX = 0;
		int directionY = 1;
		Gradient g = new Gradient(this.getX()/2,this.getY()/2,CameraWidth,CameraHeight,
				ResourcesManager.getInstance().vbom);
		g.setAnchorCenter(0.5f,0.5f);
		if(ThemeManager.getInstance().themesKey.equals(THEMES.YELLOWFANTASY.toString()))
			g.setGradient(firstColorForBlue,secondColorForBlue, directionX,directionY);
		else
			g.setGradient(firstColor,secondColor,directionX,directionY);


		animateAndAttachNotifiers(g);

		//g.setAlpha(0.9f);
		return g;
	}

	final int size= 5;
	Sprite sprites[]= new Sprite[size];
	MoveXModifier moveXs[]= new MoveXModifier[size];
	private IEaseFunction easingFunction = EaseExponentialIn.getInstance();
	private IEntityModifierListener listener[]= new IEntityModifierListener[size];
	/**
	 * @param g
	 */
	private void animateAndAttachNotifiers(final Gradient g) {
		float initialSpeedOffset= 0.15f;
		for (int i = 0; i < sprites.length; i++) {
			initialSpeedOffset = initialSpeedOffset+0.1f;
			switch (i) {

			case 0:
				sprites[i]=new Sprite(ResourcesManager.getInstance().windowDimensions.x+100, 0.75f*g.getHeight(), ResourcesManager.getInstance().coolers_region, ResourcesManager.getInstance().vbom);
				//sprites[i].setTag(1);
				moveXs[i] = new MoveXModifier(0.6f+initialSpeedOffset, ResourcesManager.getInstance().windowDimensions.x+20, 0.25f*g.getWidth(),easingFunction);

				break;
			case 1:
				sprites[i]=new Sprite(ResourcesManager.getInstance().windowDimensions.x+100, 0.25f*g.getHeight(), ResourcesManager.getInstance().drama_region, ResourcesManager.getInstance().vbom);
				//sprites[i].setTag(2);
				moveXs[i] = new MoveXModifier(0.65f+initialSpeedOffset, ResourcesManager.getInstance().windowDimensions.x+20,g.getX() - g.getWidth()/2,easingFunction);

				break;
			case 2:
				sprites[i]=new Sprite(ResourcesManager.getInstance().windowDimensions.x+100, 0.85f*g.getHeight(), ResourcesManager.getInstance().confetti_region, ResourcesManager.getInstance().vbom);
				//sprites[i].setTag(3);
				moveXs[i] = new MoveXModifier(0.7f+initialSpeedOffset, ResourcesManager.getInstance().windowDimensions.x+20, 0.10f*g.getWidth(),easingFunction);

				break;
			case 3:
				sprites[i]=new Sprite(ResourcesManager.getInstance().windowDimensions.x+100, 0.75f*g.getHeight(), ResourcesManager.getInstance().sheryl_region, ResourcesManager.getInstance().vbom);
				//sprites[i].setTag(4);
				moveXs[i] = new MoveXModifier(0.75f+initialSpeedOffset, ResourcesManager.getInstance().windowDimensions.x+20, 0.70f*g.getWidth(),easingFunction);

				break;
			case 4:
				sprites[i]=new Sprite(ResourcesManager.getInstance().windowDimensions.x+100, 0.10f*g.getHeight(), ResourcesManager.getInstance().inveye_region, ResourcesManager.getInstance().vbom);
				//sprites[i].setTag(5);
				moveXs[i] = new MoveXModifier(0.7f+initialSpeedOffset, ResourcesManager.getInstance().windowDimensions.x+20, 0.90f*g.getWidth(),easingFunction);

				break;
			default:
				break;
			} 
			sprites[i].setScale(0.7f*ResourcesManager.resourceScaler);


		}
		//sprites[0].registerEntityModifier(moveXs[0]);

	}
	Gradient g;
	private IUpdateHandler pUpdateHandler= null;
	Sprite hostContButton = null;
	Sprite guessAgainButton = null;
	//User Won Notification
	public Notification(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager, String notificationText, UserDO user) {

		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.setAlpha(0.6f);
		g=null;
		g=createGardient(ThemeManager.getInstance().themesKey);
		KeyboardString keysChar= createWonWordKey(user);
		float centerX = this.getWidthScaled()/2;
		Entity dummy = new Entity(centerX, pY, 0, 0);
		dummy.setAnchorCenter(0.5f, 0);
		Point windowDimensions=ResourcesManager.getInstance().windowDimensions;

		if(null!=keysChar && null!=keysChar.getKeyCharArray()){
			KeyboardChar[] keys= keysChar.getKeyCharArray();

			if(keys[0]!=null){
				dummy.setWidth(keys[0].getWidthScaled()*keys.length);
				g.attachChild(dummy);
				for (int i = 0; i < keys.length; i++) {
					float toX= keys[i].getX();
					float toY= keys[i].getY();
					keys[i].registerEntityModifier(new MoveModifier((i+1)/4f, centerX+100,windowDimensions.y, toX, toY));
					dummy.attachChild(keys[i]);
					ThemeManager.getInstance().setKeyBoardCharThemeForWin(keys[i]);
				}
			}

		}
		this.attachChild(g);


		ScreenGrabber screenCapture = new ScreenGrabber();
		SceneManager.getInstance().getCurrentScene().attachChild(screenCapture); // As the last Entity in the scene

		//this.setAlpha(0.5f);
		//this.setScale(ResourcesManager.resourceScaler);

		this.notificationText=notificationText;


		this.mPauseScene = new CameraScene(ResourcesManager.getInstance().camera);
		this.mPauseScene.setBackgroundEnabled(false);
		SceneManager.getInstance().getCurrentScene().setChildScene(mPauseScene, false, true, true);

		//this.setColor(.9f,.7f,.9f);
		//this.setAlpha(.95f);

		text=new Text(40*ResourcesManager.resourceScaler, pHeight/2, ResourcesManager.mSmallFont, this.notificationText, pVertexBufferObjectManager);
		text.setAnchorCenter(0,0.5f);
		text.setColor(.10f,.12f,.10f,0.9f);
		text.setBlendFunctionDestination(GLES20.GL_BLEND_DST_RGB);
		text.setScale(ResourcesManager.resourceScaler);

		//this.setWidth(windowDimensions.x/2);
		this.setHeight(windowDimensions.y/2f);
		this.setAnchorCenter(0.5f,0.5f);


		beeCaught = new AnimatedSprite(this.getWidth()/2,
				this.getHeight()*3/4 ,ThemeManager.getInstance().beeTextureRegion, pVertexBufferObjectManager);
		beeCaught.setScale(0.4f*ResourcesManager.resourceScaler);
		beeCaught.animate(80);
		this.attachChild(beeCaught);

		this.attachChild(text);

		final ResourcesManager resourcesManager = ResourcesManager.getInstance();
		final ThemeManager thememanager = ThemeManager.getInstance();
		final TimerManager timerManager =TimerManager.getInstance();
		for (int i = 0; i < sprites.length; i++) {
			g.attachChild(sprites[i]);
			sprites[i].registerEntityModifier(moveXs[i]);
		}

		/////////////////////////////////////////////////////////////////////////////new
		hostContButton=new Sprite(-50, 0.07f*g.getHeightScaled(), ResourcesManager.getInstance().hostContinue_region, ResourcesManager.getInstance().vbom){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					//clear the char set
					if(null!=ResourcesManager.getInstance().host && null!= ResourcesManager.getInstance().host.getCharSet()){
						ResourcesManager.getInstance().host.getCharSet().clear();
					}
					if(null!=ResourcesManager.getInstance().addedPositions){
						ResourcesManager.getInstance().addedPositions.clear();
					}
					if(null!=ResourcesManager.getInstance().addedPositions){
						ResourcesManager.getInstance().addedPositions.clear();
					}
					ResourcesManager.getInstance().users= new ArrayList<UserChunk>();

					timerManager.beeAppearedCount=0;
					timerManager.bullAppeared=false;

					resourcesManager.activity.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							if(null!=AutoBahnConnectorPubSub.keyClear && AutoBahnConnectorPubSub.keyClear.isVisible()){
								AutoBahnConnectorPubSub.keyClear.setVisible(false);
								AutoBahnConnectorPubSub.keyClear.detachSelf();
								AutoBahnConnectorPubSub.keyClear.clearEntityModifiers();
							}
						}
					});
					PageChunkFactory.getInstance().reset();
					ParticleSwipeCreator.getInstance().reset();

					SoundManager.getInstance().changeToMenuMusic();
					resourcesManager.unsubscribeRoom();

					resourcesManager.unloadHostSceneResources();

					ConnectionManager.suffixEventMarker=ConnectionManager.USEREVENTNORMAL;
					StorageManager.getInstance().getUser().setHostedGuessedRoomIndex(0);
					ResourcesManager.getInstance().hostImageclicked = true;
					ResourcesManager.getInstance().hosted = false;
					ResourcesManager.getInstance().guessImageclicked = false;


					Runnable run5 = new Runnable() {

						@Override
						public void run() {
							try {
								if(ConnectionManager.getInstance().mConnection.isConnected()){

									ConnectionManager.getInstance().autobahnConnectorPubSub.fetchAllRoomsState();
									//Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
								}
								else{
									ConnectionManager.getInstance().prepareConnection();
								}
								ResourcesManager.getInstance().hosted=true;
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
					ResourcesManager.getInstance().activity.runOnUiThread(run5); 



					SceneManager.getInstance().createScrollableRoomsScene();
					ResourcesManager.getInstance().minimumNumberOfLetters=3;

					return true;
				}
				return true;
			};
		};
		//sprites[i].setTag(4);
		hostContButton.setAnchorCenter(1,0);
		MoveXModifier hostFly= new MoveXModifier(0.75f, -20, 0.50f*g.getWidthScaled(),EaseBounceIn.getInstance());
		hostContButton.registerEntityModifier(hostFly);
		guessAgainButton=new Sprite(ResourcesManager.getInstance().windowDimensions.x+50, 0.07f*g.getHeightScaled(), ResourcesManager.getInstance().guessAgain_region, ResourcesManager.getInstance().vbom){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){

					//clear the char set
					if(null!=resourcesManager.host && null!= resourcesManager.host.getCharSet()){
						resourcesManager.host.getCharSet().clear();
					}
					if(null!=resourcesManager.addedPositions){
						resourcesManager.addedPositions.clear();
					}
					if(null!=resourcesManager.users){
						resourcesManager.users.clear();
					}
					resourcesManager.users= new ArrayList<UserChunk>();
					timerManager.beeAppearedCount=0;
					timerManager.bullAppeared=false;

					if(thememanager.beeAnim!=null && thememanager.beeAnim.hasParent()){
						resourcesManager.activity.runOnUpdateThread(new Runnable() {
							@Override
							public void run() {
								if(null!=AutoBahnConnectorPubSub.keyClear && AutoBahnConnectorPubSub.keyClear.isVisible()){
									AutoBahnConnectorPubSub.keyClear.setVisible(false);
									AutoBahnConnectorPubSub.keyClear.detachSelf();
									AutoBahnConnectorPubSub.keyClear.clearEntityModifiers();
								}
								SceneManager.getInstance().guessGameScene.detachChild(thememanager.beeAnim);
								thememanager.beeAnim.clearEntityModifiers();
								thememanager.beeAnim.clearUpdateHandlers();
								thememanager.beeAnim=null;
							}
						});
					}

					PageChunkFactory.getInstance().reset();
					ParticleSwipeCreator.getInstance().reset();

					SoundManager.getInstance().changeToMenuMusic();
					resourcesManager.unsubscribeRoom();
					resourcesManager.unloadGuessSceneResources();

					ConnectionManager.suffixEventMarker=ConnectionManager.USEREVENTNORMAL;
					StorageManager.getInstance().getUser().setHostedGuessedRoomIndex(0);
					resourcesManager.guessImageclicked = true;
					resourcesManager.hosted = false;
					resourcesManager.hostImageclicked = false;

					Runnable run5 = new Runnable() {

						@Override
						public void run() {
							try {
								if(ConnectionManager.getInstance().mConnection.isConnected()){

									ConnectionManager.getInstance().autobahnConnectorPubSub.fetchAllRoomsState();
								}
								else{
									ConnectionManager.getInstance().prepareConnection();
								}
								ResourcesManager.getInstance().hosted=true;
							}
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
					ResourcesManager.getInstance().activity.runOnUiThread(run5);

					SceneManager.getInstance().createScrollableRoomsScene();

					return true;
				}
				return true;
			};
		};
		//sprites[i].setTag(5);
		guessAgainButton.setAnchorCenter(0,0);
		MoveXModifier guessFly= new MoveXModifier(0.7f, ResourcesManager.getInstance().windowDimensions.x+20, 0.50f*g.getWidthScaled(),EaseBounceIn.getInstance());
		guessAgainButton.registerEntityModifier(guessFly);


		hostContButton.setScale(0.8f*ResourcesManager.resourceScaler);
		guessAgainButton.setScale(0.8f*ResourcesManager.resourceScaler);

		//this.mPauseScene.registerTouchArea(g);
		this.mPauseScene.registerTouchArea(hostContButton);
		g.attachChild(hostContButton);
		this.mPauseScene.registerTouchArea(guessAgainButton);
		g.attachChild(guessAgainButton);
		/*this.setZIndex(1000);
/////////////////////////////////////////////////////////////////////////////new
		//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(false);
		if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){
			((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene().registerTouchArea(this);
			((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene().sortChildren();
		}*/
		this.mPauseScene.registerTouchArea(this);
		flagIncrease = true;
		pUpdateHandler = new IUpdateHandler(){

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if(null!=ThemeManager.getInstance().themesKey && ThemeManager.getInstance().themesKey.equals(THEMES.YELLOWFANTASY.toString())){
					if(g.getRed()<0.99f && flagIncrease){
						g.setFromRed(g.getFromRed()+0.0001f);
						g.setToRed(g.getToRed()+0.0001f);
						g.setFromGreen(g.getFromGreen()+0.0001f);
						g.setToGreen(g.getToGreen()+0.0001f);
						if(g.getFromRed()>=0.99f || g.getFromGreen()>=0.99f   
								|| g.getToRed()>=0.99f || g.getToGreen()>=0.99f ){

							flagIncrease = false;
						}
					}
					else if(!flagIncrease){
						g.setFromRed(g.getFromRed()-0.0001f);
						g.setToRed(g.getToRed()-0.0001f);
						g.setFromGreen(g.getFromGreen()-0.0001f);
						g.setToGreen(g.getToGreen()-0.0001f);
						if(g.getFromRed()<=0.01f || g.getFromGreen()<=0.01f
								|| g.getToRed()<=0.01f || g.getToGreen()<=0.01f ){
							flagIncrease = true;
						}
					}
				}
				else{
					if(g.getRed()<0.99f && flagIncrease){
						g.setFromRed(g.getFromRed()+0.0001f);
						g.setToRed(g.getToRed()+0.0001f);
						g.setFromBlue(g.getFromBlue()+0.0001f);
						g.setToBlue(g.getToBlue()+0.0001f);
						if(g.getFromRed()>=0.99f  || g.getFromBlue()>=0.99f   
								|| g.getToBlue()>=0.99f || g.getToRed()>=0.99f ){
							flagIncrease = false;
						}
					}
					else if(!flagIncrease){
						g.setFromRed(g.getFromRed()-0.0001f);
						g.setToRed(g.getToRed()-0.0001f);
						g.setFromBlue(g.getFromBlue()-0.0001f);
						g.setToBlue(g.getToBlue()-0.0001f);
						if(g.getFromRed()<=0.01f || g.getToBlue()<=0.01f
								|| g.getFromBlue()<=0.01f || g.getToRed()<=0.01f ){
							flagIncrease = true;
						}
					}
				}
			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}
		};
		this.mPauseScene.registerUpdateHandler(pUpdateHandler);
		/*this.setIgnoreUpdate(false);
		this.mPauseScene.setIgnoreUpdate(false);*/
		if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) ){
			this.setPosition(-pX, -pY);
			ResourcesManager.getInstance().notificationListener  = new IEntityModifierListener(){
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					//SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(true);
				} 
			};

			ResourcesManager.getInstance().notificationModifier = new MoveModifier(0.5f, pX , pY-300, pX, pY,ResourcesManager.getInstance().notificationListener, EaseBounceOut.getInstance());
			this.registerEntityModifier(ResourcesManager.getInstance().notificationModifier);
		}
		else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
			this.setPosition(pX, pY);
			ResourcesManager.getInstance().notificationListener  = new IEntityModifierListener(){
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					//SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(true);
				} 
			};


			ResourcesManager.getInstance().notificationModifier = new FadeInModifier(0.3f,  EaseBounceOut.getInstance());
			this.registerEntityModifier(ResourcesManager.getInstance().notificationModifier);
		}
		attachBackButton(pVertexBufferObjectManager,  "userEventNotification");


		//this.mPauseScene.registerEntityModifier(new ColorModifier(1, 4, 1, 3, 1, 1, 1));
		this.mPauseScene.attachChild(this);
		//this.registerEntityModifier(new ColorModifier(1, 4, 1, 3, 1, 1, 1));
		GameScreenCapture.captureScreen(screenCapture, this);
		this.mPauseScene.setBackgroundEnabled(false);

		//Body circleBody2 = PhysicsFactory.createCircleBody(ResourcesManager.getInstance().mPhysWorld, rectNo, BodyType.StaticBody, MainActivity.mCellFixtureDef);
	}

	//Notification Screen For FB

	//invite By wall= 0 , invite to game app request = 1
	public Notification(float pX, float pY, float pWidth, float pHeight, ITextureRegion pTextureRegion, final byte inviteOrPlayRequest,
			VertexBufferObjectManager pVertexBufferObjectManager) {

		super(pX, pY, pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		this.setAlpha(0.5f);
		//this.setScale(ResourcesManager.resourceScaler);
		Point windowDimensions=ResourcesManager.getInstance().windowDimensions;
		ITextureRegion tempRegion= null;
		String adsFree="";
		if(inviteOrPlayRequest==0 && ResourcesManager.isAdsEnabled && ResourcesManager.isAdsEnabledRefBased){
			if(ResourcesManager.numberOfPlaysToSkipAd<1){
				adsFree="\n\nAlso enjoy ads free gameplay for next 1 game";
			}
			else{
				adsFree="";
			}
		}
		else if(inviteOrPlayRequest==1  && ResourcesManager.isAdsEnabled  && ResourcesManager.isAdsEnabledRefBased){
			if( ResourcesManager.numberOfPlaysToSkipAd<2){
				adsFree="\n\nAlso enjoy ads free gameplay for next 2 games";
			}
			else{
				adsFree="";
			}
		}
		if(!FacebookManager.sUserLoggedIn && Session.getActiveSession()!=null){
			Session session=Session.getActiveSession();
			if(session.getState()==SessionState.OPENED){
				FacebookManager.sUserLoggedIn =true;
				
			}
		}
		if(!FacebookManager.sUserLoggedIn){
			if(inviteOrPlayRequest==0){
				this.notificationText= "This Feature Needs You To Login Into Facebook.\nPlease Click On Button Below To Login And Share The Game"+adsFree;
				tempRegion=ResourcesManager.getInstance().fb_region;
			}
			else if(inviteOrPlayRequest==1){
				this.notificationText= "This Social Feature Needs You To Login Into Facebook.\nPlease Click On Button Below To Login And Play With Friends"+adsFree;
				tempRegion=ResourcesManager.getInstance().fb_region;
			}
		}
		else{
			if(inviteOrPlayRequest==0){
				this.notificationText= "Please Confirm With Button Below To Post On Your Feed"+adsFree;
				tempRegion=ResourcesManager.getInstance().fbPost_region;
			}
			else if(inviteOrPlayRequest==1){
				this.notificationText= "Please Click On Button Below To Invite Friends"+adsFree;
				tempRegion=ResourcesManager.getInstance().fbFriend_region;
			}
		}

		this.mPauseScene = new CameraScene(ResourcesManager.getInstance().camera);
		this.mPauseScene.setBackgroundEnabled(false);
		SceneManager.getInstance().getCurrentScene().setChildScene(mPauseScene, false, true, true);

		//this.setColor(.9f,.7f,.9f);
		this.setAlpha(.95f);

		text=new Text(this.getWidth()/2, this.getHeight()*3/4, ResourcesManager.getInstance().mBitmapFontOutline, this.notificationText, pVertexBufferObjectManager);
		TextOptions options = new TextOptions();
		options.setHorizontalAlign(HorizontalAlign.CENTER);
		options.setAutoWrap(AutoWrap.WORDS);
		text.setTextOptions(options);
		text.setAnchorCenter(0.5f,0.5f);
		text.setColor(.15f,.15f,.15f,0.8f);
		//text.setBlendFunctionDestination(GLES20.GL_BLEND_DST_RGB);
		text.setScale(0.5f*ResourcesManager.resourceScaler);

		//this.setWidth(windowDimensions.x/2);
		this.setHeight(windowDimensions.y/2f);
		this.setAnchorCenter(0.5f,0.5f);


		fbButton = new Sprite(this.getWidth()/2,
				this.getHeight()*1/4 ,tempRegion, pVertexBufferObjectManager){
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					/*IEntity removableEntity=null;
					//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(true);
					if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
						performBackButtonPressConfirmation(removableEntity, "fbNotifier");
					}
					else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){

						removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
						mPauseScene.detachChild(removableEntity);
						SceneManager.getInstance().getCurrentScene().clearChildScene();
						SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
						SceneManager.getInstance().getCurrentScene().setChildScene(((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene(),false,true,true);
					}*/

					ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
						public void run() {

							if(FacebookManager.sUserLoggedIn){
								if(inviteOrPlayRequest==0){
									if(FacebookManager.sFirstName==null){
										FacebookManager.checkUserLoggedIn();
									}
									//FacebookManager.requestForPublishPermissions(Session.getActiveSession());
									if (FacebookDialog.canPresentShareDialog(ResourcesManager.getInstance().activity.getApplicationContext(), 
											FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
										FacebookManager.postShare(FacebookManager.sFirstName, "Play With ");
									}
									else{
										//FacebookManager.post(FacebookManager.sFirstName, "Play With ");
										FacebookManager.publishFeedDialog(FacebookManager.sFirstName, "Play With ");
									}
								}
								else if(inviteOrPlayRequest==1){
									FacebookManager.inviteFriends();
								}
							}
							else{

								if(inviteOrPlayRequest==0){
									if(FacebookManager.sFirstName==null){
										FacebookManager.checkUserLoggedIn();
									}
									FacebookManager.loginAndPost("Play With ");
								}
								else if(inviteOrPlayRequest==1){
									FacebookManager.loginFBAndInvite();	
								}
							}
						}
					});
				}
				return true;
			}
		};


		if(!FacebookManager.sUserLoggedIn){
			if(inviteOrPlayRequest==0){
				fbButton.setScale(0.4f*ResourcesManager.resourceScaler);
			}
			else{
				fbButton.setScale(0.4f*ResourcesManager.resourceScaler);
			}
		}
		else{
			if(inviteOrPlayRequest==0){
				fbButton.setScale(0.7f*ResourcesManager.resourceScaler);
			}
			else{
				fbButton.setScale(0.4f*ResourcesManager.resourceScaler);
			}
		}

		this.attachChild(fbButton);

		this.attachChild(text);


		/*this.setZIndex(1000);

		//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(false);
		if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){
			((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene().registerTouchArea(this);
			((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene().sortChildren();
		}*/
		this.mPauseScene.registerTouchArea(this);
		/*this.setIgnoreUpdate(false);
		this.mPauseScene.setIgnoreUpdate(false);*/
		if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
			this.setPosition(-pX, -pY);
			ResourcesManager.getInstance().notificationListener  = new IEntityModifierListener(){
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					//SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(true);
				} 
			};

			ResourcesManager.getInstance().notificationModifier = new MoveModifier(0.5f, pX , pY-300, pX, pY,ResourcesManager.getInstance().notificationListener, EaseElasticOut.getInstance());
			this.registerEntityModifier(ResourcesManager.getInstance().notificationModifier);
		}
		else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){
			this.setPosition(pX, pY);
			ResourcesManager.getInstance().notificationListener  = new IEntityModifierListener(){
				@Override
				public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				}
				@Override
				public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
					//SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(true);
				} 
			};


			ResourcesManager.getInstance().notificationModifier = new FadeInModifier(0.3f,  EaseBackOut.getInstance());
			this.registerEntityModifier(ResourcesManager.getInstance().notificationModifier);
		}
		attachBackButton(pVertexBufferObjectManager,  "fbNotifier");

		this.mPauseScene.attachChild(this);
		this.mPauseScene.setBackgroundEnabled(false);

		//Body circleBody2 = PhysicsFactory.createCircleBody(ResourcesManager.getInstance().mPhysWorld, rectNo, BodyType.StaticBody, MainActivity.mCellFixtureDef);
	}

	public Notification(float pX, float pY, float pWidth, float pHeight,  
			VertexBufferObjectManager pVertexBufferObjectManager) {

		super(pX, pY, pWidth, pHeight,  ResourcesManager.getInstance().tutorialGradient_region, pVertexBufferObjectManager);
		this.setAnchorCenter(0.5f, 0.5f);
		final Point windowDimensions=ResourcesManager.getInstance().windowDimensions;
		this.setScale(0.5f*(ResourcesManager.resourceScaler));

		this.mPauseScene = new CameraScene(ResourcesManager.getInstance().camera);
		this.mPauseScene.setBackgroundEnabled(false);
		SceneManager.getInstance().getCurrentScene().setChildScene(mPauseScene, false, true, true);

		//this.setColor(.9f,.7f,.9f);

		//this.setWidth(windowDimensions.x/2);
		//this.setHeight(windowDimensions.y/2f);


		listenerStepIn = new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub

			}
		};

		listenerStepOut = new IEntityModifierListener() {

			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				// TODO Auto-generated method stub

			}
		};
		stepInMoveMod= new MoveXModifier(5f, 1.5f*windowDimensions.x, windowDimensions.x/2, listenerStepIn, EaseExponentialIn.getInstance());
		stepOutMoveMod= new MoveXModifier(5f, -1.5f* windowDimensions.x, windowDimensions.x/2, listenerStepOut, EaseExponentialIn.getInstance());

		/*	tutorialBack = new Sprite(windowDimensions.x/2, windowDimensions.y/2, ResourcesManager.getInstance().tutorialGradient_region, pVertexBufferObjectManager);
			tutorialBack.setAnchorCenter(0.5f, 0.5f);*/


		tutorialTitle = new Sprite(this.getWidth()/2, this.getHeight()*0.95f, ResourcesManager.getInstance().tutorialname_region, pVertexBufferObjectManager);
		tutorialTitle.setAnchorCenter(0.5f, 1f);
		tutorialTitle.setScale(0.95f*ResourcesManager.resourceScaler);
		tutorialStep1 = new Sprite(this.getWidth()/2, this.getHeight()/2, ResourcesManager.getInstance().tutorialStep1_region, pVertexBufferObjectManager);
		tutorialStep1.setAnchorCenter(0.5f, 0.5f);
		tutorialStep1.setScale(0.90f*ResourcesManager.resourceScaler);
		tutorialStep2 = new Sprite( 1.5f*windowDimensions.x, this.getHeight()/2, ResourcesManager.getInstance().tutorialStep2_region, pVertexBufferObjectManager);
		tutorialStep2.setAnchorCenter(0.5f, 0.5f);
		tutorialStep2.setScale(0.90f*ResourcesManager.resourceScaler);
		tutorialStep3 = new Sprite( 1.5f*windowDimensions.x, this.getHeight()/2, ResourcesManager.getInstance().tutorialStep3_region, pVertexBufferObjectManager);
		tutorialStep3.setAnchorCenter(0.5f, 0.5f);
		tutorialStep3.setScale(0.90f*ResourcesManager.resourceScaler);
		tutorialStep4 = new Sprite( 1.5f*windowDimensions.x, this.getHeight()/2, ResourcesManager.getInstance().tutorialStep4_region, pVertexBufferObjectManager);
		tutorialStep4.setAnchorCenter(0.5f, 0.5f);
		tutorialStep4.setScale(0.90f*ResourcesManager.resourceScaler);

		float lowHipPerct = 0.075f;
		if(ResourcesManager.resourceScaler<1.3f){
			lowHipPerct=0.025f;
		}

		tutorialFinish = new Sprite(this.getWidth()/2, lowHipPerct*this.getHeight(), ResourcesManager.getInstance().tutorialFinish_region, pVertexBufferObjectManager){
			/* (non-Javadoc)
			 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
			 */
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(Notification.this.hasParent()){
						ToastHelper.makeCustomToastOnUI("** You Can Turn On Tutorial From Menu Options **\nStart Guessing "+ ResourcesManager.getInstance().numberOfLettersHosted + " Letter Words", Toast.LENGTH_LONG);
						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								Notification.this.clearEntityModifiers();
								Notification.this.clearUpdateHandlers();
								Notification.this.detachChildren();
								Notification.this.detachSelf();
								IEntity removableEntity=null;
								if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
									mPauseScene.setBackgroundEnabled(true);
									removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
									mPauseScene.detachChild(removableEntity);
									SceneManager.getInstance().getCurrentScene().clearChildScene();
									SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
									//performNoButtonClick();
								}
								else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){

									removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
									mPauseScene.detachChild(removableEntity);
									SceneManager.getInstance().getCurrentScene().clearChildScene();
									SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
									SceneManager.getInstance().getCurrentScene().setChildScene(((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene(),false,true,true);
								}
							}
						});
					}
					return true;
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		tutorialFinish.setAnchorCenter(0.5f, 0f);

		tutorialPrev = new Sprite(tutorialFinish.getX()-(tutorialFinish.getWidth()/2)-20*ResourcesManager.resourceScaler,tutorialFinish.getY(), ResourcesManager.getInstance().tutorialprev_region, pVertexBufferObjectManager){
			/* (non-Javadoc)
			 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
			 */
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(Notification.this.hasParent()){
						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								switch (currentStepVisible) {
								case 1:
									break;
								case 2:
									currentStepVisible=1;
									stepInMoveMod= new MoveXModifier(.5f, -1.5f*windowDimensions.x, Notification.this.getWidth()/2, listenerStepIn, EaseExponentialIn.getInstance());
									stepOutMoveMod= new MoveXModifier(.5f,  Notification.this.getWidth()/2, 1.5f* windowDimensions.x, EaseExponentialOut.getInstance());

									tutorialStep1.registerEntityModifier(stepInMoveMod);
									tutorialStep2.registerEntityModifier(stepOutMoveMod);
									tutorialPrev.registerEntityModifier(new AlphaModifier(.2f, 1, 0));
									break;
								case 3:
									currentStepVisible=2;
									stepInMoveMod= new MoveXModifier(.5f, -1.5f*windowDimensions.x, Notification.this.getWidth()/2, listenerStepIn, EaseExponentialIn.getInstance());
									stepOutMoveMod= new MoveXModifier(.5f,  Notification.this.getWidth()/2, 1.5f* windowDimensions.x, EaseExponentialOut.getInstance());

									tutorialStep2.registerEntityModifier(stepInMoveMod);
									tutorialStep3.registerEntityModifier(stepOutMoveMod);
									break;
								case 4:
									currentStepVisible=3;
									stepInMoveMod= new MoveXModifier(.5f, -1.5f*windowDimensions.x, Notification.this.getWidth()/2, listenerStepIn, EaseExponentialIn.getInstance());
									stepOutMoveMod= new MoveXModifier(.5f,  Notification.this.getWidth()/2, 1.5f* windowDimensions.x, EaseExponentialOut.getInstance());

									tutorialStep3.registerEntityModifier(stepInMoveMod);
									tutorialStep4.registerEntityModifier(stepOutMoveMod);
									tutorialNext.registerEntityModifier(new AlphaModifier(.2f,0,1));
									tutorialFinish.registerEntityModifier(new AlphaModifier(.2f,0.7f,0));

									break;
								default:
									break;
								}

							}
						});
					}
					return true;
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		tutorialPrev.setAnchorCenter(1f, 0f);

		tutorialNext = new Sprite(tutorialFinish.getX()+(tutorialFinish.getWidth()/2)+20*ResourcesManager.resourceScaler, tutorialFinish.getY(), ResourcesManager.getInstance().tutorialnext_region, pVertexBufferObjectManager){
			/* (non-Javadoc)
			 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
			 */
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(Notification.this.hasParent()){
						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								switch (currentStepVisible) {
								case 1:
									currentStepVisible=2;
									stepInMoveMod= new MoveXModifier(.5f, 1.5f*windowDimensions.x, Notification.this.getWidth()/2, listenerStepIn, EaseExponentialIn.getInstance());
									stepOutMoveMod= new MoveXModifier(.5f,  Notification.this.getWidth()/2, -1.5f* windowDimensions.x, EaseExponentialIn.getInstance());

									tutorialStep2.registerEntityModifier(stepInMoveMod);
									tutorialStep1.registerEntityModifier(stepOutMoveMod);
									tutorialPrev.registerEntityModifier(new AlphaModifier(.2f, 0,1));
									break;
								case 2:
									currentStepVisible=3;
									stepInMoveMod= new MoveXModifier(.5f, 1.5f*windowDimensions.x, Notification.this.getWidth()/2, listenerStepIn, EaseExponentialIn.getInstance());
									stepOutMoveMod= new MoveXModifier(.5f,  Notification.this.getWidth()/2, -1.5f* windowDimensions.x, EaseExponentialIn.getInstance());

									tutorialStep3.registerEntityModifier(stepInMoveMod);
									tutorialStep2.registerEntityModifier(stepOutMoveMod);
									break;
								case 3:
									currentStepVisible=4;
									stepInMoveMod= new MoveXModifier(.5f, 1.5f*windowDimensions.x, Notification.this.getWidth()/2, listenerStepIn, EaseExponentialIn.getInstance());
									stepOutMoveMod= new MoveXModifier(.5f,  Notification.this.getWidth()/2, -1.5f* windowDimensions.x, EaseExponentialIn.getInstance());

									tutorialStep4.registerEntityModifier(stepInMoveMod);
									tutorialStep3.registerEntityModifier(stepOutMoveMod);
									tutorialNext.registerEntityModifier(new AlphaModifier(.2f, 1, 0));
									tutorialFinish.registerEntityModifier(new AlphaModifier(.2f,0,1));

									break;
								case 4:

									break;
								default:
									break;
								}

							}
						});
					}
					return true;
				}
				return super
						.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
			}
		};
		tutorialNext.setAnchorCenter(0f, 0f);

		tutorialFinish.setScale(0.75f);
		tutorialNext.setScale(0.75f);
		tutorialPrev.setScale(0.75f);

		//this.attachChild(Notification.this);
		Notification.this.attachChild(tutorialTitle);
		Notification.this.attachChild(tutorialStep1);
		Notification.this.attachChild(tutorialStep2);
		Notification.this.attachChild(tutorialStep3);
		Notification.this.attachChild(tutorialStep4);
		Notification.this.attachChild(tutorialFinish);
		Notification.this.attachChild(tutorialNext);
		Notification.this.attachChild(tutorialPrev);	
		tutorialPrev.setAlpha(0f);
		tutorialFinish.setAlpha(0f);

		ResourcesManager.getInstance().notificationModifier = new MoveModifier(0.5f, pX , pY-300, pX, pY,ResourcesManager.getInstance().notificationListener, EaseElasticOut.getInstance());
		this.registerEntityModifier(ResourcesManager.getInstance().notificationModifier);

		this.mPauseScene.registerTouchArea(this);
		attachBackButton(pVertexBufferObjectManager,  "fbNotifier");

		this.mPauseScene.attachChild(this);
		this.mPauseScene.setBackgroundEnabled(false);
		//this.mPauseScene.setAnchorCenter(0.5f,0.5f);
		//this.mPauseScene.setScale(1f*(ResourcesManager.resourceScaler/ResourcesManager.density));
		//this.mPauseScene.setScale(1f*ResourcesManager.resourceScaler);
		//Body circleBody2 = PhysicsFactory.createCircleBody(ResourcesManager.getInstance().mPhysWorld, rectNo, BodyType.StaticBody, MainActivity.mCellFixtureDef);
	}

	protected Sprite attachBackButton(VertexBufferObjectManager pVertexBufferObjectManager, final String notifierType){
		backButton = new Sprite(5f *ResourcesManager.resourceScaler, 5f * ResourcesManager.resourceScaler, ResourcesManager.chocobackbuttonregion, pVertexBufferObjectManager) {
			boolean areaClickedDown;
			private boolean gotoMainMenu=false;
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					areaClickedDown =true;
					return true;
				}
				else if((pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionMove()) && areaClickedDown){
					IEntity removableEntity=null;
					//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(true);
					if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
						/*	removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
						mPauseScene.detachChild(removableEntity);
						SceneManager.getInstance().getCurrentScene().clearChildScene();
						SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
						if(notifierType.equals("userEventNotification")){
							SceneManager.getInstance().setScene(SceneType.SCENE_GAME);
						}
						//enableUpdates();
						 */						
						performBackButtonPressConfirmation(removableEntity,notifierType);
					}
					else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){

						removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
						mPauseScene.detachChild(removableEntity);
						SceneManager.getInstance().getCurrentScene().clearChildScene();
						SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
						SceneManager.getInstance().getCurrentScene().setChildScene(((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene(),false,true,true);
						if(gotoMainMenu){
							gotoMainMenuAndroidHome();
						}
					}
				}
				return false;
			}

		};
		backButton.setAnchorCenter(0, 0);
		backButton.setScale(0.73f * ResourcesManager.resourceScaler);
		backButton.setZIndex(10);
		if(!backButton.hasParent()){
			this.mPauseScene.registerTouchArea(backButton);
			this.mPauseScene.attachChild(backButton);
		}
		return backButton;
	}

	public void gotoMainMenuAndroidHome(){
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		ResourcesManager.getInstance().activity.startActivity(startMain);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Notification){
			if(null==this.getnotificationText() && null==((Notification)o).getnotificationText()){
				return true;
			}
			return this.getnotificationText().equalsIgnoreCase(((Notification)o).getnotificationText());
		}
		return false;
	}

	private void removeNotification(){
		ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				IEntity removableEntity=null;
				//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(true);
				if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
					removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
					mPauseScene.unregisterTouchArea(removableEntity);
					if(null!=pUpdateHandler){
						mPauseScene.unregisterUpdateHandler(pUpdateHandler);
						pUpdateHandler=null;
					}
					mPauseScene.detachChild(removableEntity);

					SceneManager.getInstance().getCurrentScene().clearChildScene();
					SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);

					//enableUpdates();
					((RealGameInterface)(SceneManager.getInstance().getCurrentScene())).confirmBackKeyPressed();
				}
			}
		});
	}

	/**
	 * @return
	 */
	private String getnotificationText() {
		// TODO Auto-generated method stub
		return notificationText;
	};

	/* (non-Javadoc)
	 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		if(null != rectYes && rectYes.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){

			if(pSceneTouchEvent.isActionDown()){
				actionYesRectDown=true;
				this.setAlpha(0.9f);
			}
			else if(pSceneTouchEvent.isActionUp() && actionYesRectDown){
				actionYesRectDown=false;
				performYesButtonClick();
				return true;
			}
			return true;
		}
		else if(null!=rectNo && rectNo.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()))
		{
			if(pSceneTouchEvent.isActionDown()){
				actionNoRectDown=true;
				this.setAlpha(0.9f);
			}
			else if(pSceneTouchEvent.isActionUp() && actionNoRectDown){
				actionNoRectDown=false;
				performNoButtonClick();
				return true;
			}

			return true;
		}
		else if(null!=fbButton && fbButton.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
			if(pSceneTouchEvent.isActionDown()){
				actionNoRectDown=true;
				fbButton.setAlpha(0.7f);
			}
			else if(pSceneTouchEvent.isActionUp() && actionNoRectDown){
				actionNoRectDown=false;
				fbButton.setAlpha(1f);
				fbButton.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				//if(FacebookManager.sUserLoggedIn){
				performNoButtonClick();
				//}
				//else{

				//}
				return true;
			}

			return true;
		}
		else if(null!=tutorialPrev&& tutorialPrev.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
			if(pSceneTouchEvent.isActionDown()){
				actionNoRectDown=true;
				tutorialPrev.setAlpha(0.7f);
			}
			else if(pSceneTouchEvent.isActionUp() && actionNoRectDown){
				actionNoRectDown=false;
				tutorialPrev.setAlpha(1f);
				tutorialPrev.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				return true;
			}

			return true;
		}else if(null!=tutorialFinish&& tutorialFinish.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
			if(pSceneTouchEvent.isActionDown()){
				actionNoRectDown=true;
				tutorialFinish.setAlpha(0.7f);
			}
			else if(pSceneTouchEvent.isActionUp() && actionNoRectDown){
				actionNoRectDown=false;
				tutorialFinish.setAlpha(1f);
				tutorialFinish.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				return true;
			}

			return true;
		}else if(null!=tutorialNext&& tutorialNext.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
			if(pSceneTouchEvent.isActionDown()){
				actionNoRectDown=true;
				tutorialNext.setAlpha(0.7f);
			}
			else if(pSceneTouchEvent.isActionUp() && actionNoRectDown){
				actionNoRectDown=false;
				tutorialNext.setAlpha(1f);
				tutorialNext.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				return true;
			}

			return true;
		}
		return false;

	}

	/**
	 * 
	 */
	public void performNoButtonClick() {
		ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run(){
				IEntity removableEntity=null;
				//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(true);
				if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
					removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
					mPauseScene.detachChild(removableEntity);
					SceneManager.getInstance().getCurrentScene().clearChildScene();
					SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
					//enableUpdates();
				}
				else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){
					removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
					mPauseScene.detachChild(removableEntity);
					SceneManager.getInstance().getCurrentScene().clearChildScene();
					SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);
					SceneManager.getInstance().getCurrentScene().setChildScene(((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).getMenuChildScene(),false,true,true);
				}
			}
		});
	}

	/**
	 * 
	 */
	private void performYesButtonClick() {
		ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				IEntity removableEntity=null;
				//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(true);
				if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){
					performBackButtonPressConfirmation(removableEntity, "yesClick");
				}
				else if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_MENU)){
					((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).confirmBackKeyPressed();
				}
			}
		});
	}

	//For Yes Button Click And For User Events Win or Lose
	private void performBackButtonPressConfirmation(IEntity removableEntity, String notifierType) {
		removableEntity=mPauseScene.getChildByTag(EntityTagManager.backButtonNotifier);
		mPauseScene.unregisterTouchArea(removableEntity);
		if(null!=pUpdateHandler){
			mPauseScene.unregisterUpdateHandler(pUpdateHandler);
			pUpdateHandler=null;
		}
		mPauseScene.detachChild(removableEntity);

		SceneManager.getInstance().getCurrentScene().clearChildScene();
		SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);

		//enableUpdates();
		if(notifierType.equals("userEventNotification") || notifierType.equals("yesClick")){
			if(null!=ConnectionManager.getInstance().autobahnConnectorPubSub && null!=ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs){
				//synchronized (ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs) {
				if(!ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs.isEmpty())
					ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs.clear();
				//}
			}
			ResourcesManager.getInstance().numberOfLettersHosted = 0;
			ResourcesManager.getInstance().deviceBackButtonToGameScene = false;
			((RealGameInterface)(SceneManager.getInstance().getCurrentScene())).confirmBackKeyPressed();
		}
	}

	private void enableUpdates() {
		if(null!=SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterA))
			SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterA).setIgnoreUpdate(false);
		if(null!=SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterB))
			SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterB).setIgnoreUpdate(false);
		if(null!=SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterH))
			SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterH).setIgnoreUpdate(false);
		if(null!=SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterI))
			SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterI).setIgnoreUpdate(false);
		if(null!=SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterP))
			SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.letterP).setIgnoreUpdate(false);
	}
}



