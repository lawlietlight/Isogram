package com.efficientsciences.cowsandbulls.wordwars.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;
import org.andengine.util.modifier.IModifier;

import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;

public class GameScene extends BaseScene {

	public Sprite hostLeftImage;
	public Sprite guessRightImage;

	public Sprite hostLeftImageTransparent;
	public Sprite guessRightImageTransparent;

	public Rectangle rect;
	private boolean isTouchInProgressLeftClick;
	private boolean isTouchInProgressRightClick;	
	private boolean isTouchInProgressLeftClickDown;
	private boolean isTouchInProgressRightClickDown;

	IEntityModifierListener listenerHost;
	IEntityModifier modifierHost;
	IEntityModifierListener listenerAlphaHost;
	IEntityModifier modifierAlphaHost;
	IEntityModifierListener listenerHostTrans;
	IEntityModifier modifierHostTrans;

	IEntityModifierListener listenerGuess;
	IEntityModifier modifierGuess;
	IEntityModifierListener listenerAlphaGuess;
	IEntityModifier modifierAlphaGuess;
	IEntityModifierListener listenerGuessTrans;
	IEntityModifier modifierGuessTrans;

	public GameScene(){
		//Tag 11 used for Game scene

		this.setTag(EntityTagManager.gameScene);
	}

	@Override
	public void createScene() {
		createBackground();

		hostLeftImage= new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.hostHighlightRegion, vbom);
		hostLeftImage.setSize(windowDimensions.x, windowDimensions.y);

		guessRightImage= new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.guessHighlightRegion, vbom);
		guessRightImage.setSize(windowDimensions.x, windowDimensions.y);


		hostLeftImageTransparent= new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.hostTransparentRegion, vbom);
		hostLeftImageTransparent.setSize(windowDimensions.x, windowDimensions.y);

		guessRightImageTransparent= new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.guessTransparentRegion, vbom);
		guessRightImageTransparent.setSize(windowDimensions.x, windowDimensions.y);


		//

		listenerHost = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				resourcesManager.hostImageclicked = true;
				ResourcesManager.getInstance().hosted = false;
				resourcesManager.guessImageclicked = false;
				
				
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
				//hostLeftImage.detachSelf(); 20-06-2014

				//Added 20-06-2014
				ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						/* Now it is safe to remove the entity! */
						SceneManager.getInstance().hostGuessGameScene.detachChild(hostLeftImage);
					}
				});


				/*				pItem.unregisterEntityModifier((IEntityModifier) pModifier);
				 */				
				pModifier.reset();
				isTouchInProgressLeftClick=false;

				if(hostLeftImageTransparent.getParent()!=null){
					//hostLeftImageTransparent.detachSelf(); 20-06-2014
					ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							/* Now it is safe to remove the entity! */
							SceneManager.getInstance().hostGuessGameScene.detachChild(hostLeftImageTransparent);
						}
					});
				}

				if(guessRightImageTransparent.getParent()!=null){
					//guessRightImageTransparent.detachSelf();
					ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							/* Now it is save to remove the entity! */
							SceneManager.getInstance().hostGuessGameScene.detachChild(guessRightImageTransparent);
						}
					});
				}
				/*				pItem.unregisterEntityModifier((IEntityModifier) pModifier);
				 */				
				modifierHostTrans.reset();
				isTouchInProgressLeftClickDown=false;
			} 
		};

		listenerGuess = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				resourcesManager.guessImageclicked = true;
				ResourcesManager.getInstance().hosted = false;
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
				//System.gc();

				//guessRightImage.detachSelf(); 20-06-2014

				//Added 20-06-2014

				ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						/* Now it is save to remove the entity! */
						SceneManager.getInstance().hostGuessGameScene.detachChild(guessRightImage);
					}
				});
				/*				pItem.unregisterEntityModifier((IEntityModifier) pModifier);
				 */				
				pModifier.reset();
				isTouchInProgressRightClick=false;

				if(hostLeftImageTransparent.getParent()!=null){
					//hostLeftImageTransparent.detachSelf();  20-06-2014

					//Added 20-06-2014
					ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							/* Now it is save to remove the entity! */
							SceneManager.getInstance().hostGuessGameScene.detachChild(hostLeftImageTransparent);
						}
					});
				}

				if(guessRightImageTransparent.getParent()!=null){
					//guessRightImageTransparent.detachSelf(); 20-06-2014

					//Added 20-06-2014
					ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
						@Override
						public void run() {
							/* Now it is save to remove the entity! */
							SceneManager.getInstance().hostGuessGameScene.detachChild(guessRightImageTransparent);
						}
					});
				}
				/*				pItem.unregisterEntityModifier((IEntityModifier) pModifier);
				 */				
				modifierGuessTrans.reset();
				isTouchInProgressRightClickDown=false;
			} 
		};


		//

		listenerHostTrans = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			} 
		};

		listenerGuessTrans = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			} 
		};

		listenerAlphaHost = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				/*				pItem.unregisterEntityModifier((IEntityModifier) pModifier);
				 */				pModifier.reset();
			} 
		};

		listenerAlphaGuess = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				//SceneManager.getInstance().createClientScene();
				/*				pItem.unregisterEntityModifier((IEntityModifier) pModifier);
				 */				pModifier.reset();
			} 
		};

		modifierHost = new ScaleModifier(.3f, 1f,1.1f,listenerHost);
		modifierGuess  = new ScaleModifier(.3f, 1f,1.1f,listenerGuess);

		modifierAlphaHost = new AlphaModifier(.3f, 0.3f, 0.98f,listenerAlphaHost);
		modifierAlphaGuess  = new AlphaModifier(.3f, 0.3f, 0.98f,listenerAlphaGuess);

		modifierHostTrans =  new AlphaModifier(.05f, 0.3f, 0.95f,listenerHostTrans);
		modifierGuessTrans  = new AlphaModifier(.05f, 0.3f,0.95f,listenerGuessTrans);

		//

		if(!backButtonDisplayed){
			attachBackButton();
		}

	}



	public void createBackground() {
		Sprite s = new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.host_guess_background_region, vbom){

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
		SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
		Debug.e("Host Guess Game Scene Back Key Pressed");
		this.back();
	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_GAME;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		if(!backButton.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())){
			if(pSceneTouchEvent.isActionDown()){

				if(pSceneTouchEvent.getX()<(windowDimensions.x*0.5f)){
					Log.e("Host Guess Game Scene", "left Host Image Clicked");
					if(!isTouchInProgressLeftClickDown && !isTouchInProgressRightClickDown){
						isTouchInProgressLeftClickDown = true;
						if(!hostLeftImageTransparent.hasParent()){
							hostLeftImageTransparent.registerEntityModifier(modifierHostTrans);
							this.attachChild(hostLeftImageTransparent);
						}
						return true;
					}
				}
				else{
					Log.e("Host Guess Game Scene", "Right guess Image Clicked");
					if(!isTouchInProgressLeftClickDown && !isTouchInProgressRightClickDown){
						isTouchInProgressRightClickDown=true;
						if(!guessRightImageTransparent.hasParent()){
							guessRightImageTransparent.registerEntityModifier(modifierGuessTrans);
							this.attachChild(guessRightImageTransparent);
						}
						return true;
					}
				}


			}
			else if(pSceneTouchEvent.isActionUp()){


				if(pSceneTouchEvent.getX()<(windowDimensions.x*0.5f)){
					Log.e("Host Guess Game Scene", "left Host Image Clicked");
					if(!isTouchInProgressLeftClick && !isTouchInProgressRightClick){
						if(!isTouchInProgressRightClickDown){
							isTouchInProgressLeftClick = true;
							hostLeftImage.registerEntityModifier(new ParallelEntityModifier(modifierAlphaHost,modifierHost));
							SoundManager.getInstance().createWhipSound();
							this.attachChild(hostLeftImage);
							return true;
						}
						else{
							if(guessRightImageTransparent.getParent()!=null){
								isTouchInProgressRightClickDown=false;
								//guessRightImageTransparent.detachSelf();
								ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										/* Now it is save to remove the entity! */
										SceneManager.getInstance().hostGuessGameScene.detachChild(guessRightImageTransparent);
									}
								});
							}
							return true;
						}
					}
				}
				else{
					Log.e("Host Guess Game Scene", "Right guess Image Clicked");
					if(!isTouchInProgressLeftClick && !isTouchInProgressRightClick){
						if(!isTouchInProgressLeftClickDown){
							isTouchInProgressRightClick=true;
							guessRightImage.registerEntityModifier(new ParallelEntityModifier(modifierAlphaGuess,modifierGuess));
							SoundManager.getInstance().createWhipSound();
							this.attachChild(guessRightImage);
							return true;
						}
						else{
							if(hostLeftImageTransparent.getParent()!=null){
								isTouchInProgressLeftClickDown=false;
								//hostLeftImageTransparent.detachSelf(); 20-06-2014
								ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
									@Override
									public void run() {
										/* Now it is safe to remove the entity! */
										SceneManager.getInstance().hostGuessGameScene.detachChild(hostLeftImageTransparent);
									}
								});
							}
							return true;
						}
					}
				}


			}
		}
		return super.onSceneTouchEvent(pSceneTouchEvent);
	}



}
