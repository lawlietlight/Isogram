package com.efficientsciences.cowsandbulls.wordwars.scenes;

import java.util.ArrayList;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.DrawType;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseLinear;

import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.domain.KeyboardChar;
import com.efficientsciences.cowsandbulls.wordwars.domain.Notification;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunkFactory;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomStream;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ParticleSwipeCreator;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ShapeScrollContainer;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ShapeScrollContainer.IShapeScrollContainerTouchListener;
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
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnectorPubSub;

public class HostScene extends BaseScene implements RealGameInterface  {

	private   int CAMERA_WIDTH = windowDimensions.x;
	private   int CAMERA_HEIGHT = windowDimensions.y;

	private   float AUTOWRAP_WIDTH = windowDimensions.x-50;

	private float keyBoardPosX;
	private float keyBoardPosY;
	ShapeScrollContainer scrollableArea;
	private IEntityModifierListener listener;
	private Notification notifier;
	SlideUsersChildScene childSlide;

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

		//this.mText = new Text(50, 40, this.mFont, "", 1000, new TextOptions(AutoWrap.LETTERS, AUTOWRAP_WIDTH, HorizontalAlign.CENTER), vertexBufferObjectManager);

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

			this.registerTouchArea(key);

			//key.setColor(0.09804f, 0.6274f, 0.0f);
			resourcesManager.keyBoard.attachChild(key);

		}

		//Host Key For Host scene
		Text done= new Text(0, 0, resourcesManager.mBitmapFont, resourcesManager.hostText, vbom);
		done.setColor(0.25f, 0.25f, 0.30f);
		done.setScale(ResourcesManager.resourceScaler);
		//define base pX and pY as defined while creating individual keys
		float pX=(resourcesManager.windowDimensions.x) - (resourcesManager.letterWidth*1f*ResourcesManager.resourceScaler) ;
		float pY=160*ResourcesManager.resourceScaler;
		KeyboardChar hostConnect=new KeyboardChar(pX, pY-((done.getHeightScaled()/2)+10*ResourcesManager.resourceScaler), done.getWidthScaled()+20*ResourcesManager.resourceScaler, done.getHeightScaled()+10*ResourcesManager.resourceScaler, resourcesManager.guessText,done, vbom);
		//hostConnect.setAnchorCenter(0,0);
		done.setPosition(hostConnect.getWidth()*.5f, hostConnect.getHeight()*.5f);
		hostConnect.setAnchorCenterX(1);
		hostConnect.setTag(EntityTagManager.hostConnectButton);
		hostConnect.setColor(0.8431372549019608f, 0.8431372549019608f, 0.0274509803921569f);
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
		ResourcesManager.getInstance().welcome.setScale(.5f);


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
		scrollableArea.SetScrollableDirections(true, false);
		scrollableArea.SetScrollLock(true);

		this.registerTouchArea(scrollableArea);
		this.attachChild(scrollableArea);
		//21-05-2014

		/*Text entity= (Text) SceneManager.getInstance().getCurrentScene().getChildByTag(99);
			if(null!= entity){
				this.detachChild(entity);
			}*/
		this.attachChild(ResourcesManager.getInstance().welcome);

		if(!backButtonDisplayed){
			attachBackButton();
		}
		//SceneManager.getInstance().getCurrentScene().attachChild(ResourcesManager.getInstance().particleSystem);
	}







	@Override
	public void onBackKeyPressed() {
		ResourcesManager.getInstance().closeChatWindow();
		if(!ResourcesManager.getInstance().deviceBackButtonToGameScene){
			if((notifier==null || (notifier!=null && !notifier.hasParent()))){
				SoundManager.getInstance().createBlurpSound();
				notifier = new Notification(windowDimensions.x/2f, windowDimensions.y/2f, windowDimensions.x/2f, (float)(windowDimensions.y/2), ResourcesManager.getInstance().greyHeart_region, vbom, "   Stop Spectating?");
				notifier.setTag(EntityTagManager.backButtonNotifier);
				//notifier.setScale(resourcesManager.resourceScaler);
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
		/*else{
		//clear the char set
		if(null!=ResourcesManager.getInstance().host && null!= ResourcesManager.getInstance().host.getCharSet()){
			ResourcesManager.getInstance().host.getCharSet().clear();
		}
		if(null!=ResourcesManager.getInstance().addedPositions && null!= ResourcesManager.getInstance().addedPositions){
			ResourcesManager.getInstance().addedPositions.clear();
		}
		PageChunkFactory.getInstance().reset();
		if(null!=AutoBahnConnectorPubSub.keyClear && AutoBahnConnectorPubSub.keyClear.isVisible()){
			AutoBahnConnectorPubSub.keyClear.setVisible(false);
			AutoBahnConnectorPubSub.keyClear.detachSelf();
			AutoBahnConnectorPubSub.keyClear.clearEntityModifiers();
		}
		SoundManager.changeMusic(MusicPlayed.ROCKMUSIC);
		SceneManager.getInstance().setScene(SceneManager.getInstance().hostGuessGameScene);
		Debug.e("Host Scene Back Key Pressed");
		this.back();
		}*/
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
		if(null!=ResourcesManager.getInstance().addedPositions){
			ResourcesManager.getInstance().addedPositions.clear();
		}
		ResourcesManager.getInstance().users= new ArrayList<UserChunk>();

		timerManager.beeAppearedCount=0;
		timerManager.bullAppeared=false;
		if(null!=AutoBahnConnectorPubSub.keyClear && AutoBahnConnectorPubSub.keyClear.isVisible()){
			AutoBahnConnectorPubSub.keyClear.setVisible(false);
			AutoBahnConnectorPubSub.keyClear.detachSelf();
			AutoBahnConnectorPubSub.keyClear.clearEntityModifiers();
		}

		PageChunkFactory.getInstance().reset();
		ParticleSwipeCreator.getInstance().reset();

		SoundManager.getInstance().changeToMenuMusic();
		resourcesManager.unsubscribeRoom();

		resourcesManager.unloadHostSceneResources();

		disposeScene();
		StorageManager.getInstance().getUser().setHostedGuessedRoomIndex(0);
		if(null==SceneManager.getInstance().hostGuessGameScene){
			SceneManager.getInstance().createHostGuessGameScene();
		}
		else
		SceneManager.getInstance().setScene(SceneManager.getInstance().hostGuessGameScene);
		Debug.e("Host Scene Back Key Pressed");

		this.back();
	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_HOST;
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

	@Override
	public synchronized void addPageChunk(UserDO user) {
		if(!ResourcesManager.getInstance().pages.hasPagechunk(new PageChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, ResourcesManager.getInstance().payloadFromServer))){
			PageChunk p = PageChunkFactory.getInstance().next(user);
			if(null!=user && user.isHoster()){
				p.setAlpha(.6f);
				p.getWordText().setAlpha(1);
				p.setColor(.25f,.6f,.21f);
			}
			attachIfNotAttached(p,user);
		}
		if((ResourcesManager.getInstance().payloadFromServer.equals(ResourcesManager.getInstance().wordHosted) || ResourcesManager.getInstance().gameWon) && null!=user && !user.isHoster()){
			NotificationManager.getInstance().createUserWonNotificationForHost(user);
			ResourcesManager.getInstance().closeChatWindow();
			StorageManager.getInstance().getUser().setWordHosted("");
			StorageManager.getInstance().getUser().setHoster(false);
		}
	}

	private synchronized void attachIfNotAttached(PageChunk p, UserDO user) {
		if (!p.hasParent()) {
			moveModifierForPageChunk(p,user);
			attachChild(p);
			this.sortChildren();
			SoundManager.getInstance().createSound();
			/*scrollableArea.SetAlphaVisiblitiyControl(false);*/

		}
	}



	//Set the Time to sync once the word is actually hosted and once we step into Waiting scene for hoster
	private void moveModifierForPageChunk(final PageChunk p,final UserDO user) {
		listener =  new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				scrollableArea.Add(p);
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier,
					IEntity pItem) {
				if(user.isHoster()){

					if(user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
						SceneManager.getInstance().createWaitingScene();

						Runnable run6 = new Runnable() {
							@Override
							public void run() {
								try {

									((WaitingScene)SceneManager.getInstance().getCurrentScene()).addUserChunk(StorageManager.getInstance().getUser());
								} 
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						//Though we run on UI thread , AutoBahnSocket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
						ResourcesManager.getInstance().activity.runOnUpdateThread(run6); 
						
						Runnable run5 = new Runnable() {

							@Override
							public void run() {
								try {
									RoomStream hostRoomDetails= ResourcesManager.getInstance().roomsState.get(StorageManager.getInstance().getUser().getHostedGuessedRoomIndex());
									if(null==hostRoomDetails){
										hostRoomDetails=new RoomStream();
									}
									if(0==hostRoomDetails.getIndex()){
										hostRoomDetails.setIndex(StorageManager.getInstance().getUser().getHostedGuessedRoomIndex());
									}
									hostRoomDetails.setTimeSyncPulseFlag(true);
									hostRoomDetails.setWaitTimeElapsed(WaitingScene.waitTimeElapsed);
									if(ConnectionManager.getInstance().mConnection.isConnected()){
										//SoundManager.getInstance().createWhipSound();

										ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(hostRoomDetails);
										Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
									}
									else{

										ConnectionManager.getInstance().prepareConnection();
										ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(hostRoomDetails);
									}
								} 
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
						ResourcesManager.getInstance().activity.runOnUiThread(run5); 
					}
					else{
						ToastHelper.makeCustomToastOnUI("Room Simultaneously Hosted By Another Player \n Try Hosting Another Room", Toast.LENGTH_LONG);
						if(null!=ResourcesManager.getInstance().host && null!= ResourcesManager.getInstance().host.getCharSet()){
							ResourcesManager.getInstance().host.getCharSet().clear();
						}
						if(null!=ResourcesManager.getInstance().addedPositions && null!= ResourcesManager.getInstance().addedPositions){
							ResourcesManager.getInstance().addedPositions.clear();
						}

						ResourcesManager.getInstance().users= new ArrayList<UserChunk>();
						StorageManager.getInstance().getUser().setHoster(false);
						timerManager.beeAppearedCount=0;
						timerManager.bullAppeared=false;



						PageChunkFactory.getInstance().reset();
						ParticleSwipeCreator.getInstance().reset();

						SoundManager.getInstance().changeToMenuMusic();
						resourcesManager.unsubscribeRoom();
						resourcesManager.unloadSceneParticleSystems();

						ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

							@Override
							public void run() {
								if(null!=AutoBahnConnectorPubSub.keyClear && AutoBahnConnectorPubSub.keyClear.isVisible()){
									AutoBahnConnectorPubSub.keyClear.setVisible(false);
									AutoBahnConnectorPubSub.keyClear.detachSelf();
									AutoBahnConnectorPubSub.keyClear.clearEntityModifiers();
									disposeScene();
								}
							}
						});

						SceneManager.getInstance().createScrollableRoomsScene();
					}
				}
				/*//Latest Change && user.getWordHosted()!=null && user.getUserName().equals(StorageManager.getInstance().getUser().getUserName()) Condition
				if(user.isHoster() && user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
					
				}*/
			}
		};
		float pagechunkAlpha=p.getAlpha();
		p.registerEntityModifier(new ParallelEntityModifier(new FadeInModifier(0.3f,listener,EaseLinear.getInstance()),new AlphaModifier(2f,0.1f,0.8f)));
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

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



}
