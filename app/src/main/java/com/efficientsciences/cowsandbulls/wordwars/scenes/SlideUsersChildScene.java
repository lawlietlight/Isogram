package com.efficientsciences.cowsandbulls.wordwars.scenes;

import java.util.Iterator;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.graphics.Point;
import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunkFactory;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;

public class SlideUsersChildScene extends Rectangle implements IScrollDetectorListener, IOnSceneTouchListener {

	private static final float FREQ_D = 60.0f;

	private static final byte STATE_WAIT = 0;
	private static final byte STATE_SCROLLING = 1;
	private static final byte STATE_MOMENTUM = 2;
	private static final byte STATE_DISABLE = 3;

	public static float WRAPPER_HEIGHT = 1260;
	private static final float MAX_ACCEL = 5500*ResourcesManager.resourceScaler;

	private static double FRICTION = 0.945f;


	private byte mState;
	private double accel, accel1, accel0;
	public static float mCurrentY;
	private IEntity mWrapper;
	private ScrollDetector mScrollDetector;
	private long t0;
	private Point windowDimensions;
	private boolean ScrollBarVerticalVisible = true;
	private boolean ScrollBarVisibleOnlyOnTouch = true;

	private Rectangle mRectVertical;

	private TimerHandler thandle;
	public Sprite playerButton;

	private float rectangleHeight;
	float lowerBottomLimits= 0;
	final Entity entity = this;
	MoveYModifier moveYForward;
	MoveYModifier moveYBack;

	public SlideUsersChildScene(float pX, float pY, float pWidth, float pHeight, Point windowDimensions,
			VertexBufferObjectManager pVertexBufferObjectManager) {

		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.windowDimensions = windowDimensions;

		createScene();
	}


	protected void onCreateResources() {
		thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				if(entity.isVisible()){
					doSetPos();
				}
			}
		});
	}

	public void createScene() {

		this.playerButton=attachPlayerButton();

		//this.setX(playerButton.getWidthScaled()/2);
		//this.playerButton.setScale(0.5f*ResourcesManager.resourceScaler);
		//this.playerButton.setAnchorCenter(0.5f,1);
		//this.playerTextFix.setColor(0.1f, 0.2f, 0.6f);
		rectangleHeight=(windowDimensions.y/1.5f)+50;
		lowerBottomLimits= windowDimensions.y-rectangleHeight;

		//this.setAlpha(0.2f);
		//this.setColor(0.35f,0.35f,0.35f);
		this.setColor(213/255f, 59/255f, 59/255f);
		this.setHeight(rectangleHeight);

		mWrapper = new Rectangle(0,0, 0, 0, this.getVertexBufferObjectManager());
		mWrapper.setAlpha(0);



		// TODO Auto-generated method stub
		//Startapp Ad call - start
		//commented

		ThemeManager.getInstance().themesKey=ThemeManager.getInstance().getThemePreferences().getThemeSelection();
		/*if(ThemeManager.getInstance().themesKey.equals(THEMES.BLUEMAGIC.toString())){
			createBackground();
		}*/
		//Entity entity = this;
		//this.setBackgroundColor();
		mScrollDetector = new SurfaceScrollDetector(this);


		final float userChunkHeight= ((windowDimensions.y/1.5f)/ResourcesManager.getInstance().maxNumberOfUsersPerPage) * ResourcesManager.resourceScaler;
		final float userChunkWidth= ResourcesManager.pageChunkInitialXOffset * ResourcesManager.resourceScaler;
		final float xPosition = 2*ResourcesManager.resourceScaler;
		int numberOfUsersJoined= 0;

		if(null!=ResourcesManager.getInstance().users){
			numberOfUsersJoined=ResourcesManager.getInstance().users.size(); 
			//Log.e("numberOfUsersJoined For Slider", "numberOfUsersJoined: "+numberOfUsersJoined);
			/*for (int j = 0; j < numberOfUsersJoined; j++) {

				UserChunk userChunkTemp = ResourcesManager.getInstance().users.get(j);
				UserDO userDO = null;
				if(null!= ResourcesManager.userProfiles){
					 userDO = ResourcesManager.userProfiles.get(userChunkTemp.getUsername());
				}
				if(null!=userDO){
					userChunkTemp  = UserChunkFactory.getInstance().next(userDO);
				}
				final UserChunk user = userChunkTemp;
				if(null!=user && null!=ResourcesManager.getInstance().users.get(j)){
					float textOffset = 10;
					if(null!=user.getDispNameText()){
						textOffset = user.getDispNameText().getHeight() * user.getDispNameText().getScaleY();
					}

					user.redMask.setWidth(user.getWidthScaled()+2);
					user.redMask.setHeight(user.getHeightScaled());
					user.redMask.setAnchorCenterX(0);
					user.redMask.setX(0);
					user.setScaleY(user.getScaleY());
					user.setHeight(user.getHeight()+textOffset);
					user.setWidth(userChunkWidth);
					user.setX(xPosition);
					user.setY((numberOfUsersJoined+1-j) * user.getHeight() - this.playerButton.getHeightScaled());
					Log.e("DrawerUsers",user.getY()+" "+user.getHeightScaled()+ " "+j);
					user.setAnchorCenter(0, 1);
					//this.registerTouchArea(user);
					ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

						@Override
						public void run() {
							if(user.hasParent()){
								user.getParent().detachChild(user);
							}

							mWrapper.attachChild(user);
						}
					});


				}
			}*/

			for (int j = 0; j < numberOfUsersJoined; j++) {
				final UserChunk user = ResourcesManager.getInstance().users.get(j);
				if(null!=user ){
					float textOffset = 55;
					if(null!=user.getDispNameText()){
						textOffset = textOffset * user.getDispNameText().getScaleY();
					}

					user.redMask.setWidth(user.getWidthScaled()+2);
					user.redMask.setHeight(user.getHeightScaled()+2);
					user.redMask.setAnchorCenterX(0);
					user.redMask.setScale(1);
					user.redMask.setAlpha(1);
					user.redMask.setX(-1);
					user.setHeight(user.getHeight()+textOffset);
					/*user.setWidth(userChunkWidth);*/
					user.setX(xPosition);
					final float yPos =(numberOfUsersJoined+1-j) * user.getHeight() - this.playerButton.getHeightScaled();
					Log.e("Players Position ", "Players Position > "+yPos);
					user.setY(yPos);
					user.setAnchorCenter(0, 1);
					user.dispNameTextOptions.setAutoWrapWidth(user.getWidthScaled()+50*ResourcesManager.resourceScaler);
					//this.registerTouchArea(user);

					Runnable run = new Runnable() {

						@Override
						public void run() {
							attachUser(user);
						}
					};
					ResourcesManager.getInstance().activity.runOnUpdateThread(run);


				}
			}
		}


		//mWrapper.setAnchorCenter(0.5f, 1f); // center on x-axis, upper top for y-axis
		if(ResourcesManager.getInstance().users!=null && !ResourcesManager.getInstance().users.isEmpty()){
			for (UserChunk user : ResourcesManager.getInstance().users) {
				if(user.getHeight()>ResourcesManager.getInstance().userChunkHeight)
					ResourcesManager.getInstance().userChunkHeight= user.getHeight();
				if(user.getWidth()>ResourcesManager.getInstance().userChunkWidth)
					ResourcesManager.getInstance().userChunkWidth= user.getWidth();
			}

		}

		mWrapper.setColor(0.21f, 0.9f, 0.29f);
		mWrapper.setHeight((ResourcesManager.getInstance().userChunkHeight)*numberOfUsersJoined);
		WRAPPER_HEIGHT = mWrapper.getHeight();
		mCurrentY =rectangleHeight  - 30-mWrapper.getHeight();
		mWrapper.setWidth(ResourcesManager.getInstance().userChunkWidth);
		this.setWidth(ResourcesManager.getInstance().userChunkWidth+ResourcesManager.getInstance().windowDimensions.x * .007f);


		//this.setAnchorCenter(0, 1);
		//this.setHeight(ResourcesManager.getInstance().userChunkHeight*ResourcesManager.getInstance().maxNumberOfUsersPerPage);
		//this.setWidth(userChunkWidth);
		mWrapper.setPosition(0, mCurrentY);
		mWrapper.setAnchorCenter(0, 0);
		this.setTag(EntityTagManager.slideWrapper);

		mState = STATE_WAIT;
		this.attachChild(mWrapper);

		//float heght = ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxusers;
		mRectVertical = new Rectangle(this.getWidthScaled(), rectangleHeight, (ResourcesManager.getInstance().windowDimensions.x * .007f), (20),this.getVertexBufferObjectManager());
		mRectVertical.setColor(0.0f, 0.0f, 0.0f,0.5f);
		mRectVertical.setAnchorCenter(1, 1);
		this.setAnchorCenter(1, 1);
		this.attachChild(mRectVertical);
		this.playerButton.setPosition(this.getWidthScaled()/2,0);
		//this.attachChild(this.playerButton);
		this.sortChildren();
		onCreateResources();
		mWrapper.registerUpdateHandler(thandle);


		if(moveYForward==null){
			moveYForward=new MoveYModifier(0.25f, entity.getY(), windowDimensions.y+rectangleHeight);
			moveYForward.setAutoUnregisterWhenFinished(true);
		}
		else{
			moveYForward.reset();
		}
		entity.registerEntityModifier(moveYForward);
		mWrapper.setVisible(false);
		entity.setAlpha(0);
		entity.setZIndex(100);


		/*if(!backButtonDisplayed){
			attachBackButton();
		}*/
		// return scene;
	}


	public void createSceneUpdate() {



		final float xPosition = 2*ResourcesManager.resourceScaler;
		int numberOfUsersJoined= 0;

		if(null!=ResourcesManager.getInstance().users){
			numberOfUsersJoined=ResourcesManager.getInstance().users.size(); 
			//Log.e("numberOfUsersJoined For Slider", "numberOfUsersJoined: "+numberOfUsersJoined);

			for (int j = 0; j < numberOfUsersJoined; j++) {
				final UserChunk user = ResourcesManager.getInstance().users.get(j);
				if(null!=user){
					float textOffset = 55;
					if(null!=user.getDispNameText()){
						textOffset = textOffset * user.getDispNameText().getScaleY();
					}

					if(!user.hasParent()){
						user.redMask.setWidth(user.getWidthScaled()+2);
						user.redMask.setHeight(user.getHeightScaled()+2);
					}
					user.redMask.setAnchorCenterX(0);
					user.redMask.setScale(1);
					user.redMask.setAlpha(1);
					user.redMask.setX(-1);
					user.setHeight(user.getHeight()+textOffset);
					/*user.setWidth(userChunkWidth);*/
					user.setX(xPosition);
					final float yPos =(numberOfUsersJoined+1-j) * user.getHeight() - this.playerButton.getHeightScaled();
					Log.e("Players Position ", "Players Position > "+yPos);
					user.setY(yPos);
					user.setAnchorCenter(0, 1);
					user.dispNameTextOptions.setAutoWrapWidth(user.getWidthScaled()+50*ResourcesManager.resourceScaler);
					//this.registerTouchArea(user);
					if(!user.hasParent()){

						attachUser(user);
					}
					/*Runnable run = new Runnable() {

						@Override
						public void run() {
							attachUser(user);
						}
					};
					ResourcesManager.getInstance().activity.runOnUpdateThread(run);*/


				}
			}
		}


		//mWrapper.setAnchorCenter(0.5f, 1f); // center on x-axis, upper top for y-axis
		if(ResourcesManager.getInstance().users!=null && !ResourcesManager.getInstance().users.isEmpty()){
			for (UserChunk user : ResourcesManager.getInstance().users) {
				if(user.getHeight()>ResourcesManager.getInstance().userChunkHeight)
					ResourcesManager.getInstance().userChunkHeight= user.getHeight();
				if(user.getWidth()>ResourcesManager.getInstance().userChunkWidth)
					ResourcesManager.getInstance().userChunkWidth= user.getWidth();
			}

		}

		mWrapper.setColor(0.21f, 0.9f, 0.29f);
		mWrapper.setHeight((ResourcesManager.getInstance().userChunkHeight)*numberOfUsersJoined);
		WRAPPER_HEIGHT = mWrapper.getHeight();
		mCurrentY =rectangleHeight  - 30-mWrapper.getHeight();
		mWrapper.setWidth(ResourcesManager.getInstance().userChunkWidth);
		this.setWidth(ResourcesManager.getInstance().userChunkWidth+ResourcesManager.getInstance().windowDimensions.x * .007f);

		mWrapper.setPosition(0, mCurrentY);
		mWrapper.setAnchorCenter(0, 0);

		mState = STATE_WAIT;

	}


	/**
	 * 
	 */
	protected void attachUser(UserChunk user) {
		mWrapper.attachChild(user);

	}


	protected Sprite attachPlayerButton(){
		playerButton = new Sprite(this.getWidthScaled()/2, 0, ResourcesManager.playersDrawerRegion, this.getVertexBufferObjectManager()) {
			boolean areaClickedDown;
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					areaClickedDown =true;
					return true;
				}
				else if((pSceneTouchEvent.isActionUp()) && areaClickedDown){
					Debug.e("Player Button Clicked");
					areaClickedDown =false;

					if(mWrapper.isVisible()){
						if(moveYForward==null){
							moveYForward=new MoveYModifier(0.25f, entity.getY(), windowDimensions.y+rectangleHeight);
							moveYForward.setAutoUnregisterWhenFinished(true);
						}
						else{
							moveYForward.reset();
						}
						entity.registerEntityModifier(moveYForward);
						mWrapper.setVisible(false);
						entity.setAlpha(0);
					}
					else{
						if(moveYBack==null){
							moveYBack=new MoveYModifier(0.25f, entity.getY(), windowDimensions.y);
							moveYBack.setAutoUnregisterWhenFinished(true);
						}
						else{
							moveYBack.reset();
						}
						entity.registerEntityModifier(moveYBack);
						mWrapper.setVisible(true);
						entity.setAlpha(1);
					}
					return true;
				}
				return false;
			}
		};
		playerButton.setAnchorCenter(0.5f, .85f);
		playerButton.setScaleX(0.4f * ResourcesManager.resourceScaler);
		playerButton.setScaleY(0.9f * ResourcesManager.resourceScaler);
		playerButton.setZIndex(25);
		playerButton.setTag(EntityTagManager.playerButton);

		if(!playerButton.hasParent()){
			this.attachChild(playerButton);
		}
		//this.getSregisterTouchArea(playerButton);
		return playerButton;
	}

	/*@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		//return onSceneTouchEvent(SceneManager.getInstance().getCurrentScene(), pSceneTouchEvent);
	}*/


	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mState == STATE_DISABLE){
			return true;
		}

		if (mState == STATE_MOMENTUM) {
			accel0 = accel1 = accel = 0;
			mState = STATE_WAIT;
		}
		if(playerButton.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())){
			return playerButton.onAreaTouched(pSceneTouchEvent, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		}
		if(pSceneTouchEvent.isActionDown()){
			for (Iterator iterator = ResourcesManager.getInstance().users.iterator(); iterator.hasNext();) {
				UserChunk user = (UserChunk) iterator.next();
				if(user.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())){
					return user.onAreaTouched(pSceneTouchEvent, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				}
			}
		}
		if (pSceneTouchEvent.isActionUp()) {
			if (ScrollBarVisibleOnlyOnTouch)
			{
				mRectVertical.setVisible(false);
			}
			mScrollDetector.onTouchEvent(pSceneTouchEvent);
			mWrapper.onAreaTouched(pSceneTouchEvent,pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
			return true;
		} else {
			if (ScrollBarVisibleOnlyOnTouch)
			{
				//Only display if they were displayed originally
				mRectVertical.setVisible(ScrollBarVerticalVisible);
				Log.e("scrollrectangle hori", mRectVertical.isVisible()+" : "+mRectVertical.getX()+" : "+mRectVertical.getY()+"    :::"   +mRectVertical.getWidth()+" : "+mRectVertical.getHeight());
			}
			mScrollDetector.setEnabled(true);
			mScrollDetector.onTouchEvent(pSceneTouchEvent);
			mWrapper.onAreaTouched(pSceneTouchEvent,pSceneTouchEvent.getX(),pSceneTouchEvent.getY());
			return true;
		}
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;
		ResourcesManager.usersScrolled=true;
	}

	@Override
	public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		long dt = System.currentTimeMillis() - t0;
		if (dt == 0)
			return;
		double s =  pDistanceY / (double)dt * 1000.0;  // pixel/second
		accel = (accel0 + accel1 + s) / 3;
		accel0 = accel1;
		accel1 = accel;
		/*Log.e("Slider users container acceleration"," accel: "+ accel);
		Log.e("Slider users container acceleration","  onScroll  pDistanceX:   "+pDistanceX+"  onScroll  pDistanceY:   "+pDistanceY);
		Log.e("Slider users container WRAPPER_HEIGHT","WRAPPER_HEIGHT"+WRAPPER_HEIGHT);
		Log.e("Slider users container this.getY()","this.getY()"+this.getY()+"");
		Log.e("Slider users container mWrapper","mWrapper"+mWrapper.getY());*/

		Log.e("mWrapper","getChildCount"+ mWrapper.getChildCount());

		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;
		//ResourcesManager.usersScrolled=true;


	}

	@Override
	public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		mState = STATE_MOMENTUM;
	}

	protected synchronized void doSetPos() {

		if (accel == 0) {
			return;
		}

		/*if (mCurrentY > mWrapper.getHeight()/2) {
			Log.e("mCurrentY > 0",mCurrentY+ " : " + mWrapper.getHeight());

			mCurrentY = mWrapper.getHeight()/2;
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
		}*/
		if(mWrapper.getHeight()+80<=rectangleHeight){
			//mWrapper.setY(30);
			return;
		}
		if (mCurrentY > 80 && accel<=0) {
			//Log.e("mCurrentY > windowDimensions.y-rectangleHeight","mCurrentY For Wrapper should Not be greater than lower limit" );
			MoveYModifier modifier = null;
			//mCurrentY = 0;
			if(mWrapper.getY() >78 && accel < 0 && accel>0.5){
				modifier = new MoveYModifier(0.5f, mWrapper.getY(),30);
				modifier.setAutoUnregisterWhenFinished(true);
				mWrapper.registerEntityModifier(modifier);

			}
			else if(null==modifier){
				mWrapper.clearEntityModifiers();
				mWrapper.setY(mCurrentY);
			}

			/*else if(null!=modifier && !modifier.isFinished()){
				modifier.
			}*/
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
		}

		if (mCurrentY < rectangleHeight - this.playerButton.getHeightScaled() -mWrapper.getHeight()) {
			//Log.e("mCurrentY < -mWrapper.getHeight()","should not be lesser than upperBound");
			mCurrentY = rectangleHeight  - this.playerButton.getHeightScaled()-mWrapper.getHeight();
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0.0;
		}
		if(accel==0){
			ResourcesManager.usersScrolled=false;
		}

		if (mWrapper.getY() > windowDimensions.y + mWrapper.getHeight()) {
			//Log.e("Slider users container mWrapper.getY() ", mWrapper.getY() +" : "+windowDimensions.y + mWrapper.getHeight() );
		}
		/*if (mWrapper.getY() > windowDimensions.y + mWrapper.getHeight()) {
			//mWrapper.setPosition(mWrapper.getX(), 0);
		} */
		if (mCurrentY<=80 && mCurrentY >= rectangleHeight - this.playerButton.getHeightScaled() -mWrapper.getHeight()) {
			mWrapper.setY(mCurrentY);
			//if((mRectVertical.getY()+(mCurrentY/mWrapper.getHeight())>0) && (mRectVertical.getY()+(mCurrentY/mWrapper.getHeight())< (rectangleHeight))){
			Log.e("Slider users container ", "  mRectVertical.getY()+(mCurrentY/windowDimensions.y):   "+ (mRectVertical.getY()+(mCurrentY/windowDimensions.y)));

			mRectVertical.setY((((-mWrapper.getY()+(rectangleHeight) - 80)/mWrapper.getHeight())*(rectangleHeight)));
			//}
			//Log.e("Slider users container mWrapper.getY() ", mWrapper.getY() +" : mCurrentY >> "+ mCurrentY);
		}


		if (accel < 0 && accel < -MAX_ACCEL)
			accel0 = accel1 = accel = - MAX_ACCEL;
		if (accel > 0 && accel > MAX_ACCEL)
			accel0 = accel1 = accel = MAX_ACCEL;

		double ny = accel / FREQ_D;
		if (ny >= -.05 && ny <= .05) {
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
			return;
		}
		if (! (Double.isNaN(ny) || Double.isInfinite(ny)))
			mCurrentY -= ny;
		if(mCurrentY>0 && mCurrentY<=80){
			FRICTION = 0.9f;
		}
		else{
			FRICTION = 0.945f;
		}
		accel = (accel * FRICTION);
		Log.e("mCurrentY", mCurrentY+"");
		Log.e("accel", accel+"");

		//mWrapper.setPosition(0, mCurrentY);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if(mState==STATE_WAIT){
			ResourcesManager.usersScrolled=false;
		}
		super.onManagedUpdate(pSecondsElapsed);
	}


}