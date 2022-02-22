package com.efficientsciences.cowsandbulls.wordwars.scenes;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationByModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseLinear;

import android.graphics.Point;
import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.domain.KeyboardChar;
import com.efficientsciences.cowsandbulls.wordwars.domain.Notification;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunkFactory;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ParticleSwipeCreator;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ShapeScrollContainer;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ShapeScrollContainer.IShapeScrollContainerTouchListener;
import com.efficientsciences.cowsandbulls.wordwars.helper.AppRater;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.NotificationManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;
import com.efficientsciences.cowsandbulls.wordwars.managers.TimerManager;
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnectorPubSub;

public class GuessScene extends BaseScene implements RealGameInterface {

	private   int CAMERA_WIDTH = windowDimensions.x;
	private   int CAMERA_HEIGHT = windowDimensions.y;
	private static final float FREQ_D = (1f/20f);
	private   float AUTOWRAP_WIDTH = windowDimensions.x-50;

	private float keyBoardPosX;
	private float keyBoardPosY;
	public ShapeScrollContainer scrollableArea;
	private IEntityModifierListener listener;
	private IEntityModifierListener modListenerWinAlpha;
	private Task taskTracker;
	public static boolean isTaskcompleted=false;
	private TimerHandler thandle;
	private Notification notifier;
	SlideUsersChildScene childSlide;
	double timePassed=0;
	Sprite rotator;
	Sprite rotator2;
	Sprite rotator3;

	// ===========================================================
	// Fields
	// ===========================================================


	/*
	 * Tags 
	 * Keyboard Rectangle 		 1
	 * Line drawn			 	 2
	 * Host Button 				 3
	 * Host Scene tag			 12345
	 * WelcomeText 				 99
	 *  (non-Javadoc)
	 * @see com.efficientsciences.cowsandbulls.wordwars.scenes.BaseScene#createScene()
	 */
	@Override
	public void createScene() {


		if(ThemeManager.getInstance().themesKey.equals(THEMES.BLUEMAGIC.toString())){
			createBackground();

		}
		//Set Tag(name) for Entire scene object as 12345 and give  Mango yellow color, divide RGB values by 255 for andengine color system
		this.setTag(EntityTagManager.hostGuessScene);
		this.getBackground().setColor(253/255f, 196/255f, 59/255f);

		//Create a Keyboard Rectangle to show Hide Keyboard and set Tag(name) as 1 
		resourcesManager.keyBoard = new Rectangle(0, 0, windowDimensions.x, resourcesManager.yTextStart-5, vbom, DrawType.STATIC);
		resourcesManager.keyBoard.setTag(EntityTagManager.keyboardRectangle);
		resourcesManager.keyBoard.setAnchorCenter(0, 0);
		//keyBoard.setColor(253/255f, 196/255f, 59/255f);
		resourcesManager.keyBoard.setAlpha(0);
		keyBoardPosX=resourcesManager.keyBoard.getX();
		keyBoardPosY=resourcesManager.keyBoard.getY();
		ResourcesManager.numberOfCoinsPerGame =0;
		//this.mText = new Text(50, 40, this.mFont, "", 1000, new TextOptions(AutoWrap.LETTERS, AUTOWRAP_WIDTH, HorizontalAlign.CENTER), vertexBufferObjectManager);
		String defaultText = "Start Guessing";

		if(SceneManager.getInstance().getCurrentSceneType().equals(SceneType.SCENE_HOST)){
			defaultText = "Host Word Here";
		}


		//Create Line to create illusion of holdin the letters and set relative reference for it
		resourcesManager.line = new Line(50, resourcesManager.yTextStart, windowDimensions.x-50,  resourcesManager.yTextStart, vbom);
		resourcesManager.line.setTag(EntityTagManager.lineInScene);
		resourcesManager.line.setColor(0.0f,0.0f, 0.0f);



		this.attachChild(resourcesManager.line);

		//this.attachChild(resourcesManager.keyboard);
		//Set the Keyboard
		//Iterate over keys and register touch area for each
		KeyboardChar[] keyChars=resourcesManager.keyboardString.getKeyCharArray();
		for (int i = 0; i < keyChars.length; i++) {
			KeyboardChar key = keyChars[i];
			/*			key.setAlpha(0.3f);
			key.getText().setAlpha(3);
			 */			this.registerTouchArea(key);

			 //key.setColor(0.09804f, 0.6274f, 0.0f);
			 resourcesManager.keyBoard.attachChild(key);

		}

		//Host Key For Host scene
		Text done= new Text(0, 0, resourcesManager.mBitmapFont, resourcesManager.guessText, vbom);
		//done.setColor(0.25f, 0.25f, 0.30f);
		//done.setColor((203f/255f), (35f/255f), (53f/255f));
		done.setColor((15f/255f), (12f/255f), (9f/255f));
		/*	done.setHeight(done.getHeight()*ResourcesManager.resourceScaler);
		done.setWidth(done.getWidth()*ResourcesManager.resourceScaler);
		 */done.setScale(ResourcesManager.resourceScaler);
		 //define base pX and pY as defined while creating individual keys
		 float pX=(resourcesManager.windowDimensions.x) - (resourcesManager.letterWidth*1f*ResourcesManager.resourceScaler) ;
		 float pY=160*ResourcesManager.resourceScaler;
		 KeyboardChar hostConnect=new KeyboardChar(pX, pY-((done.getHeightScaled()/2)+10*ResourcesManager.resourceScaler), done.getWidthScaled()+20*ResourcesManager.resourceScaler, done.getHeightScaled()+10*ResourcesManager.resourceScaler, resourcesManager.guessText,done, vbom);
		 //hostConnect.setAnchorCenter(0,0);
		 done.setPosition(hostConnect.getWidth()*.5f, hostConnect.getHeight()*.5f);
		 hostConnect.setTag(EntityTagManager.hostConnectButton);
		 //hostConnect.setColor(0.8431372549019608f, 0.8431372549019608f, 0.0274509803921569f);
		 hostConnect.setColor(0.901f, 0.901f, 0.820f,0.9f);
		 hostConnect.setAnchorCenterX(1);
		 hostConnect.attachChild(done);
		 this.registerTouchArea(hostConnect);
		 resourcesManager.keyBoard.attachChild(hostConnect);
		 this.attachChild(resourcesManager.keyBoard);
		 this.attachChild(ResourcesManager.getInstance().particleSystem);
		 this.attachChild(ResourcesManager.getInstance().particleSystemb);
		 this.attachChild(ResourcesManager.getInstance().particleSystemh);
		 this.attachChild(ResourcesManager.getInstance().particleSystemp);
		 this.attachChild(ResourcesManager.getInstance().particleSystemi);


		 PageChunkFactory.getInstance().create();
		 ResourcesManager.getInstance().welcome=new Text(ResourcesManager.getInstance().windowDimensions.x*0.5f,(ResourcesManager.getInstance().windowDimensions.y*0.5f)+100, ResourcesManager.getInstance().mBitmapFontForGame, ResourcesManager.getInstance().payloadFromServer,150,ResourcesManager.getInstance().vbom){
			 @Override
			 protected void onManagedUpdate(float pSecondsElapsed) {
				 if(ResourcesManager.getInstance().payloadFromServer.equals("Welcome")){
					 this.setText(ResourcesManager.getInstance().payloadFromServer);
					 super.onManagedUpdate(pSecondsElapsed);
				 }
				 else{
					 if(this.hasParent()){
						 ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
							 @Override
							 public void run() {
								 SceneManager.getInstance().getCurrentScene().detachChild(ResourcesManager.getInstance().welcome);
							 }
						 });
					 }
				 }
			 }
		 };

		 //welcome.setAnchorCenter(0, 0);
		 ResourcesManager.getInstance().welcome.setColor(.1f,.4f,.1f);
		 ResourcesManager.getInstance().welcome.setTag(EntityTagManager.welcomeText);
		 ResourcesManager.getInstance().welcome.setScale(.5f*resourcesManager.resourceScaler);

		 //21-05-2014
		 scrollableArea = new ShapeScrollContainer(0, 0, windowDimensions.x, windowDimensions.y, new IShapeScrollContainerTouchListener() {

			 @Override
			 public void OnContentClicked(Shape pShape) {
				 Log.e("How To Play Variant :", " ScrollComp clicked");

			 }
		 });

		 scrollableArea.SetScrollBarVisibleOnlyOnTouch(true);
		 scrollableArea.SetScrollBarVisibitlity(true, false);
		 scrollableArea.SetAlphaVisiblitiyControl(false);
		 //scrollableArea.setChildrenIgnoreUpdate(true);
		 scrollableArea.SetScrollableDirections(true, false);
		 scrollableArea.SetScrollLock(true);
		 /*scrollableArea.SetAlphaVisiblitiyControl(false);*/

		 this.registerTouchArea(scrollableArea);
		 this.attachChild(scrollableArea);
		 //21-05-2014
		 /*Text entity= (Text) SceneManager.getInstance().getCurrentScene().getChildByTag(99);
			if(null!= entity){
				this.detachChild(entity);
			}*/
		 this.attachChild(ResourcesManager.getInstance().welcome);

		 timerManager.beeAppearanceTimer= new Timer();
		 timerManager.randomiser=new Random(new Date().getTime());
		 Log.e("ATTACHBEEFLYBY: YRand", (timerManager.randomiser.nextFloat()*windowDimensions.y)+"");

		 onCreateResources();
		 taskTracker=new Task();
		 taskTracker.run();
		 this.registerUpdateHandler(thandle);

		 createDrawerChild();

		 if(!backButtonDisplayed){
			 attachBackButton();
		 }
		 /*//SceneManager.getInstance().getCurrentScene().attachChild(ResourcesManager.getInstance().particleSystem);
		 if(null!=childSlide){
			 Entity wrapper = (Entity) childSlide.getChildByTag(EntityTagManager.slideWrapper);
			 if(null!=wrapper){
				 if(null!=this.getChildByTag(EntityTagManager.shapeScrollContainerTag)){
					 int zIndex=this.getChildByTag(EntityTagManager.shapeScrollContainerTag).getZIndex();
					 //Log.e("Scrollable view Z Index", zIndex+"");
					 childSlide.setZIndex(zIndex+5);
					 wrapper.setZIndex(zIndex+5);
				 }
			 }


		 }*/
		 
	}

	/**
	 * 
	 */
	public void createDrawerChild() {
		childSlide = new SlideUsersChildScene(10* ResourcesManager.resourceScaler,windowDimensions.y,ResourcesManager.pageChunkInitialXOffset * ResourcesManager.resourceScaler,0,windowDimensions,vbom);
		childSlide.setAnchorCenter(0, 1);
		this.registerTouchArea(childSlide);
		this.attachChild(childSlide);
	}

	/**
	 * @param background 
	 * 
	 */
	private void attachRotors(Sprite background) {
		rotator= new Sprite((windowDimensions.x)/4, (int)(Math.random()*(windowDimensions.y)) , resourcesManager.rotatorRegion, vbom);
		rotator.setAlpha(0.5f);
		background.attachChild(rotator);

		rotator2= new Sprite((int)(windowDimensions.x*0.75f), (int)(Math.random()*(windowDimensions.y)) , resourcesManager.rotatorRegion, vbom);
		rotator2.setAlpha(0.4f);
		rotator2.setColor(0.7f, 0.5f, 0.5f);
		background.attachChild(rotator2);

		rotator3= new Sprite((int)(Math.random()*windowDimensions.x)/2, (int)(Math.random()*(windowDimensions.y)) , resourcesManager.rotatorRegion, vbom);
		rotator3.setAlpha(0.25f);
		rotator3.setColor(0.6f, 0.6f, 0.9f);
		background.attachChild(rotator3);

	}

	protected void onCreateResources() {

		thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						timePassed= timePassed+(double)(1.0d / FREQ_D)/60d;

						if(!timerManager.bullAppeared &&timePassed>1 && isTaskcompleted){
							UserDO localUserRef=StorageManager.getInstance().getUser();
							if(null!=localUserRef && localUserRef.getNumOfBullRuns()>0){
								doBullRuns();
							}
							else{
								timerManager.bullAppeared=true;
							}
						}
						else{
							doFlyBys();
						}


					}
				});
				ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(null!=rotator){
							rotator.clearEntityModifiers();
							rotator.registerEntityModifier(new RotationByModifier(50, 1000f));

							rotator2.clearEntityModifiers();
							rotator2.registerEntityModifier(new RotationByModifier(50, 1400f));

							rotator3.clearEntityModifiers();
							rotator3.registerEntityModifier(new RotationByModifier(30, -600f));
						}
					}
				});
			}
		});
	}

	private void doFlyBys() {
		UserDO localTempUser=StorageManager.getInstance().getUser();
		int numOfBees=0;
		if(null!=localTempUser){
			numOfBees =localTempUser.getNumOfBees();
		}
		else{
			numOfBees=resourcesManager.numberOfLettersHosted/2;
		}

		if((taskTracker!=null && isTaskcompleted)  && timerManager.beeAppearedCount<numOfBees){
			Log.e("OnManage doFlyBys", "Entered");
			taskTracker.run();
		}
	}

	private void doBullRuns() {
		isTaskcompleted=false;
		final UserDO localTempUser=StorageManager.getInstance().getUser();
		if(null!=TimerManager.getInstance().randomiser){
			int delay = (10 + TimerManager.getInstance().randomiser.nextInt(5)) * TimerManager.getInstance().howOftenShouldAppear;
			Log.e("delay", delay+"");
			/*if(delay<100){
				delay=100;
			}*/
			Log.e("OnManage doBullRuns", "Entered");
			TimerManager.getInstance().beeAppearanceTimer.schedule(new Task(){
				@Override
				public void run() {
					Log.e("Timer Task", "Scheduled execution For Bull: "+localTempUser.getNumOfBullRuns());
					ThemeManager.getInstance().attachBullRunBy();
					isTaskcompleted=true;
				}
			}, 0);
		}
	}

	static class Task extends TimerTask {
		@Override
		public void run() {
			isTaskcompleted=false;
			Log.e("Timer Task", "once");
			if(null!=TimerManager.getInstance().randomiser){
				int delay = (10 + TimerManager.getInstance().randomiser.nextInt(5)) * TimerManager.getInstance().howOftenShouldAppear;
				Log.e("delay", delay+"");
				if(delay<200){
					delay=300;
				}
				TimerManager.getInstance().beeAppearanceTimer.schedule(new Task(){
					@Override
					public void run() {
						Log.e("Timer Task", "Scheduled execution: "+TimerManager.getInstance().beeAppearedCount);
						ThemeManager.getInstance().attachBeeFlyBy();
						isTaskcompleted=true;
					}
				}, delay);
			}
		}

	}



	@Override
	public void onBackKeyPressed() {
		/*if(null!=this.getChildByTag(EntityTagManager.letterA))
			this.getChildByTag(EntityTagManager.letterA).setIgnoreUpdate(true);
		if(null!=this.getChildByTag(EntityTagManager.letterB))
			this.getChildByTag(EntityTagManager.letterB).setIgnoreUpdate(true);
		if(null!=this.getChildByTag(EntityTagManager.letterH))
			this.getChildByTag(EntityTagManager.letterH).setIgnoreUpdate(true);
		if(null!=this.getChildByTag(EntityTagManager.letterI))
			this.getChildByTag(EntityTagManager.letterI).setIgnoreUpdate(true);
		if(null!=this.getChildByTag(EntityTagManager.letterP))
			this.getChildByTag(EntityTagManager.letterP).setIgnoreUpdate(true);*/
		ResourcesManager.getInstance().closeChatWindow();
		if(ResourcesManager.getInstance().overlayRect!=null && ResourcesManager.getInstance().overlayRect.hasParent()){
			ResourcesManager.getInstance().detachInUpdateThread(ResourcesManager.getInstance().overlayRect);
		}
		if(!ResourcesManager.getInstance().deviceBackButtonToGameScene){
			if(notifier==null || (notifier!=null && !notifier.hasParent())){
				SoundManager.getInstance().createBlurpSound();
				notifier = new Notification(windowDimensions.x/2f, windowDimensions.y/2f, windowDimensions.x/2f, (float)(windowDimensions.y/2), ResourcesManager.getInstance().greyHeart_region, vbom, "   Giving Up Already?");
				notifier.setTag(EntityTagManager.backButtonNotifier);
				//this.registerTouchArea(notifier);
				ResourcesManager.getInstance().hosted=false;
				//this.attachChild(notifier);
				notifier.setIgnoreUpdate(false);
				//System.gc();
			}
		}
		else{
			ResourcesManager.getInstance().deviceBackButtonToGameScene = false;
			ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					confirmBackKeyPressed();
				}
			});
		}
	}

	@Override
	public void confirmBackKeyPressed(){
		//clear the char set
		if(null!=ResourcesManager.getInstance().host && null!= ResourcesManager.getInstance().host.getCharSet()){
			ResourcesManager.getInstance().host.getCharSet().clear();
		}
		if(null!=ResourcesManager.getInstance().addedPositions){
			ResourcesManager.getInstance().addedPositions.clear();
		}
		if(null!=ResourcesManager.getInstance().users){
			ResourcesManager.getInstance().users.clear();
		}
		ResourcesManager.getInstance().users= new ArrayList<UserChunk>();
		ResourcesManager.fbHelpShared=0;
		isTaskcompleted=true;
		timePassed=0;
		timerManager.beeAppearedCount=0;
		timerManager.bullAppeared=false;
		if(null!=AutoBahnConnectorPubSub.keyClear && AutoBahnConnectorPubSub.keyClear.isVisible()){
			AutoBahnConnectorPubSub.keyClear.setVisible(false);
			AutoBahnConnectorPubSub.keyClear.detachSelf();
			AutoBahnConnectorPubSub.keyClear.clearEntityModifiers();
		}
		if(thememanager.beeAnim!=null && thememanager.beeAnim.hasParent()){
			ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
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

		disposeScene();
		StorageManager.getInstance().getUser().setHostedGuessedRoomIndex(0);
		if(ConnectionManager.suffixEventMarker.equals(ConnectionManager.USEREVENTNORMAL) && null!=SceneManager.getInstance().hostGuessGameScene){
			SceneManager.getInstance().setScene(SceneManager.getInstance().hostGuessGameScene);
		}
		else{
			ResourcesManager.isFaceBookRequestUnAddressed = false;
			if(null!=SceneManager.getInstance().menuScene)
				SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
			else
				SceneManager.getInstance().createMenuScene();
		}
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AppRater.app_launched(ResourcesManager.getInstance().activity,true);
			}
		});
		Debug.e("Host Scene Back Key Pressed");
		this.back();
	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_GUESS;
	}

	@Override
	public void disposeScene() {
		this.detachChildren();
		this.detachSelf();
		this.dispose();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);
	}

	public void createBackground() {
		Sprite background  = new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.game_guess_background_region, vbom){

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};

		background.setSize(windowDimensions.x, windowDimensions.y);
		attachRotors(background);
		background.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		attachChild(background);

	}

	@Override
	public synchronized void addPageChunk(UserDO user) {

		if(!ResourcesManager.getInstance().pages.hasPagechunk(new PageChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, null==user.getWordGuessed()?"": user.getWordGuessed()))){
			if(!user.isHoster()){
				PageChunk p = PageChunkFactory.getInstance().next(user);
				attachIfNotAttached(p);
				if(ResourcesManager.getInstance().gameWon){
					try{
						final UserDO userFinal =user;
						if(null!=ResourcesManager.getInstance().gameWonBy && ResourcesManager.getInstance().gameWonBy.equalsIgnoreCase("You")
								&& ((null!=StorageManager.getInstance().getUser().getDisplayName() && userFinal.getDisplayName()!=null && StorageManager.getInstance().getUser().getDisplayName().equals(userFinal.getDisplayName())) ||
										(null!=StorageManager.getInstance().getUser().getUserName() && userFinal.getUserName()!=null && StorageManager.getInstance().getUser().getUserName().equals(userFinal.getUserName())))){


							Point windowDimensions=ResourcesManager.getInstance().windowDimensions;
							final Rectangle rectScreenHighlight = new Rectangle(windowDimensions.x/2f, windowDimensions.y/2f, windowDimensions.x, windowDimensions.y, ResourcesManager.getInstance().vbom);
							rectScreenHighlight.setAlpha(1);
							SoundManager.getInstance().createCameraCaptureSound();
							rectScreenHighlight.setColor(Color.WHITE);
							modListenerWinAlpha = new IEntityModifierListener(){
								@Override
								public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {


								}
								@Override
								public void onModifierFinished(IModifier<IEntity> pModifier,
										IEntity pItem) {
									rectScreenHighlight.setVisible(false);
									NotificationManager.getInstance().createUserWonNotificationForWinner(userFinal);
									ResourcesManager.getInstance().closeChatWindow();
								}
							}; 


							AlphaModifier alphaMod1= new AlphaModifier(0.1f, 0, 1);
							AlphaModifier alphaMod2= new AlphaModifier(0.1f, 1, 0,modListenerWinAlpha);

							rectScreenHighlight.registerEntityModifier(new SequenceEntityModifier(alphaMod1,new ColorModifier(0.12f, 1f, 1f, 1f, 1, 1, 1f),alphaMod2));

							SceneManager.getInstance().getCurrentScene().attachChild(rectScreenHighlight);
						}
						else if((null!=ResourcesManager.getInstance().gameWonBy && userFinal.getDisplayName()!=null && ResourcesManager.getInstance().gameWonBy.equalsIgnoreCase(userFinal.getDisplayName())) ||
								(null!=ResourcesManager.getInstance().gameWonBy && userFinal.getUserName()!=null && ResourcesManager.getInstance().gameWonBy.equalsIgnoreCase(userFinal.getUserName()))){
							NotificationManager.getInstance().createUserWonNotificationForWinner(userFinal);
							ResourcesManager.getInstance().closeChatWindow();
						}
					}
					catch(Exception e){
						if(null!=e)
							Log.e("Word Exception", e.getMessage());
					}
				}
			}
		}
		else if(user.getClueWord()!=null){
			PageChunk p = PageChunkFactory.getInstance().next(user);
			attachIfNotAttached(p);
		}
		else if(null!=user.getUserName() && user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
			Runnable run = new Runnable() {

				@Override
				public void run() {

					ToastHelper.makeCustomToast("Word Already Guessed", Toast.LENGTH_SHORT);
				}
			};
			ResourcesManager.getInstance().activity.runOnUiThread(run); 

		}
	}

	private void attachIfNotAttached(PageChunk p) {
		if (!p.hasParent()) {
			moveModifierForPageChunk(p);
			attachChild(p);
			this.sortChildren();
			ResourcesManager.getInstance().wordsListChanged = true;
			SoundManager.getInstance().createSound();

			//scrollableArea.Add(p);
		}
	}

	private void moveModifierForPageChunk(final PageChunk p) {
		listener =  new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				scrollableArea.Add(p);
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {

			}
		};
		float pagechunkAlpha=p.getAlpha();
		p.registerEntityModifier(new ParallelEntityModifier(new FadeInModifier(0.3f,listener,EaseLinear.getInstance()),new AlphaModifier(2f,0.1f,0.8f)));
	}


}
