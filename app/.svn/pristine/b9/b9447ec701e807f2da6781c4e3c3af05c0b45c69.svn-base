package com.efficientsciences.cowsandbulls.wordwars.scenes;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.util.debug.Debug;

import android.opengl.GLES20;
import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.domain.RoomProperties;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomStream;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.vungle.publisher.AdConfig;
/*import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.AdEventListener;*/

public class ScrollableRoomsScene extends BaseScene implements IScrollDetectorListener, IOnSceneTouchListener {

	private static final float FREQ_D = 120.0f;

	private static final byte STATE_WAIT = 0;
	private static final byte STATE_SCROLLING = 1;
	private static final byte STATE_MOMENTUM = 2;
	private static final byte STATE_DISABLE = 3;

	private static final int WRAPPER_HEIGHT = 1260;
	private static final float MAX_ACCEL = 5500*ResourcesManager.resourceScaler;

	private static final double FRICTION = 0.945f;


	private byte mState;
	private double accel, accel1, accel0;
	private float mCurrentY;
	private IEntity mWrapper;
	private ScrollDetector mScrollDetector;
	private long t0;

	private boolean ScrollBarVerticalVisible = true;
	private boolean ScrollBarVisibleOnlyOnTouch = true;

	private Rectangle mRectVertical;

	private TimerHandler thandle;



	protected void onCreateResources() {
		thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {
			@Override
			public void onTimePassed(final TimerHandler pTimerHandler) {
				doSetPos();
			}
		});
	}

	@Override
	public void createScene() {

		mWrapper = new Entity(0, 0);
		// TODO Auto-generated method stub
		//Startapp Ad call - start
		//commented
		if(ResourcesManager.getInstance().guessImageclicked){
			//final AdConfig overrideConfig = new AdConfig();

			// set any configuration options you like. 
			// For a full description of available options, see the 'Configuration Options' section.
			//overrideConfig.setIncentivized(true);
			if(StorageManager.getInstance().getUser().isMusicMuted()){
				//overrideConfig.setSoundEnabled(false);
			}
			//overrideConfig.setIncentivizedUserId(StorageManager.getInstance().getUser().getUserName());
			//overrideConfig.setIncentivizedCancelDialogBodyText("Closing this video early will disqualify you from earning your surprise reward we are planning to offer in forthcoming releases. Are you sure?");
			if(ResourcesManager.getInstance().activity.vunglePub.isAdPlayable() && !ResourcesManager.vungleAdShown  && ResourcesManager.getInstance().numberOfVungleAdsSkipped>=3  && ResourcesManager.isAdsEnabled && ResourcesManager.isAdsEnabledRefBased){
				Log.e("Vungle cached", "nnnnn");
				ResourcesManager.getInstance().numberOfVungleAdsSkipped = 0;

				ResourcesManager.getInstance().activity.vunglePub.playAd();
				//ResourcesManager.getInstance().activity.vunglePub.playAd(adConfig);
			}
			//ResourcesManager.getInstance().activity.vunglePub.playAd();
			else if(!ResourcesManager.vungleAdShown && ResourcesManager.getInstance().numberOfVungleAdsSkipped>=3  && ResourcesManager.isAdsEnabled && ResourcesManager.isAdsEnabledRefBased){
				ResourcesManager.getInstance().numberOfVungleAdsSkipped = 0;
				ResourcesManager.getInstance().activity.vunglePub.playAd();
			}
			else{
				ResourcesManager.getInstance().numberOfVungleAdsSkipped++;
				ResourcesManager.vungleAdShown=false;
			}
		}
		//Startapp Ad call - End
		else{
			final AdConfig overrideConfig = new AdConfig();

			// set any configuration options you like. 
			// For a full description of available options, see the 'Configuration Options' section.
			if(StorageManager.getInstance().getUser().isMusicMuted()){
				overrideConfig.setSoundEnabled(false);
			}
			
			if(!ResourcesManager.vungleAdShown && ResourcesManager.getInstance().numberOfVungleAdsSkipped>=3  && ResourcesManager.isAdsEnabled && ResourcesManager.isAdsEnabledRefBased){
				ResourcesManager.getInstance().numberOfVungleAdsSkipped = 0;
				ResourcesManager.getInstance().activity.vunglePub.playAd(overrideConfig);
			}
			else{
				ResourcesManager.getInstance().numberOfVungleAdsSkipped++;
				ResourcesManager.vungleAdShown=false;
			}
		}

		ThemeManager.getInstance().themesKey=ThemeManager.getInstance().getThemePreferences().getThemeSelection();
		/*if(ThemeManager.getInstance().themesKey.equals(THEMES.BLUEMAGIC.toString())){
			createBackground();
		}*/
		Scene scene = this;
		this.setBackgroundColor();
		mScrollDetector = new SurfaceScrollDetector(this);
		scene.setOnSceneTouchListener(this);

		
		//ResourcesManager.getInstance().maxRooms = 100;
		ResourcesManager.getInstance().maxRoomsPerPage=6;
		for (int i = 1; i <= ResourcesManager.getInstance().maxRooms; i++) {
			RoomProperties room = new RoomProperties(ResourcesManager.getInstance().windowDimensions.x/2f, (ResourcesManager.getInstance().maxRooms+1-i) * ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxRoomsPerPage, ResourcesManager.getInstance().windowDimensions.x, (ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxRoomsPerPage)+1, i, ResourcesManager.getInstance().vbom);
			createTextAndAddToRect(room,i);
			this.registerTouchArea(room);
			mWrapper.attachChild(room);
		}
		mWrapper.setHeight((ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxRoomsPerPage)*ResourcesManager.getInstance().maxRooms);
		mWrapper.setAnchorCenter(0.5f, 1f); // center on x-axis, upper top for y-axis
		if(ResourcesManager.rooms!=null && ResourcesManager.rooms.get(1)!=null){
			ResourcesManager.roomHeight=ResourcesManager.rooms.get(1).getHeight();
		}

		mWrapper.setPosition(0,  0+windowDimensions.y-ResourcesManager.roomHeight);
		mState = STATE_WAIT;
		scene.attachChild(mWrapper);

		//float heght = ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxRooms;
		mRectVertical = new Rectangle(ResourcesManager.getInstance().windowDimensions.x, ResourcesManager.getInstance().windowDimensions.y, (ResourcesManager.getInstance().windowDimensions.x * .007f), (40),ResourcesManager.getInstance().vbom);
		mRectVertical.setColor(0.0f, 0.0f, 0.0f,0.5f);
		mRectVertical.setAnchorCenter(1, 1);
		this.attachChild(mRectVertical);

		onCreateResources();
		scene.registerUpdateHandler(thandle);

		if(!backButtonDisplayed){
			attachBackButton();
		}
		//try{
		//ResourcesManager.getInstance().createStartNativeAds();
		//}
		//catch(Exception e){
			//if(null!=e)
			//	Log.e("Word Exception", "Unable To Create AD"+ e.getMessage());
		//}
		// return scene;
	}

	private void createTextAndAddToRect(RoomProperties r,int scrollIndex) {
		//Add rooms to Map
		ResourcesManager.rooms.put(scrollIndex, r);
		r.setIndex(scrollIndex);
		r.setAnchorCenter(0.5f, 0);

		/*Set<Entry<String, RoomStream>> entrySet = ResourcesManager.roomsState.entrySet();
		if(null != entrySet)
		for (Entry<String, RoomStream> entrySetValue : entrySet) {
			Integer roomIndex=Integer.parseInt(entrySetValue.getKey());
			RoomStream room = entrySetValue.getValue();
		}*/
		//Copy data Fetched from server to Local Room r
		RoomStream roomFromServer= ResourcesManager.roomsState.get(scrollIndex);
		if(null!=roomFromServer){
			r.setRoomState(roomFromServer.getRoomState());
			r.setRoomHostedBy(roomFromServer.getRoomHostedBy());
			r.setNoOfParticipants(roomFromServer.getNoOfParticipants());
		}
		ThemeManager.getInstance().resetRoomTheme(r,scrollIndex);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		if (mState == STATE_DISABLE){
			return true;
		}

		if (mState == STATE_MOMENTUM) {
			accel0 = accel1 = accel = 0;
			mState = STATE_WAIT;
		}
		if (pSceneTouchEvent.isActionUp()) {
			if (ScrollBarVisibleOnlyOnTouch)
			{
				mRectVertical.setVisible(false);
			}
			mWrapper.onAreaTouched(pSceneTouchEvent,0,0);
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
			mWrapper.onAreaTouched(pSceneTouchEvent,0,0);
			return true;
		}
	}

	@Override
	public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;
		ResourcesManager.roomsScrolled=true;
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
		Log.e("Scrollable rooms container acceleration s", "pixels"+"  s:   "+s);

		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;
		//ResourcesManager.roomsScrolled=true;


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
		if (mCurrentY > 0-windowDimensions.y+ResourcesManager.roomHeight) {
			//Log.e("mCurrentY > 0",ResourcesManager.roomHeight+ " : " + ResourcesManager.roomHeight);

			mCurrentY = 0-windowDimensions.y+ResourcesManager.roomHeight;
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
		}
		if (mCurrentY < -mWrapper.getHeight()+ResourcesManager.roomHeight) {
			//Log.e("mCurrentY < -mWrapper.getHeight()",mCurrentY+ " : " + -mWrapper.getHeight());
			mCurrentY = -mWrapper.getHeight()+ResourcesManager.roomHeight;
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0.0;
		}
		if(accel==0){
			ResourcesManager.roomsScrolled=false;
		}

		/*if (mWrapper.getY() > windowDimensions.y + mWrapper.getHeight()) {
			Log.e("Scrollable rooms container mWrapper.getY() ", mWrapper.getY() +" : "+windowDimensions.y + mWrapper.getHeight() );
		}*/
		/*if (mWrapper.getY() > windowDimensions.y + mWrapper.getHeight()) {
			//mWrapper.setPosition(mWrapper.getX(), 0);
		} */
		else {
			mWrapper.setPosition(mWrapper.getX(), -mCurrentY);
			if((mRectVertical.getY()+(mCurrentY/mWrapper.getHeight())>0) && (mRectVertical.getY()+(mCurrentY/mWrapper.getHeight())< windowDimensions.y)){
				//Log.e("Scrollable rooms container ", "  mRectVertical.getY()+(mCurrentY/windowDimensions.y):   "+ (mRectVertical.getY()+(mCurrentY/windowDimensions.y)));

				mRectVertical.setY((windowDimensions.y-((mWrapper.getY()-windowDimensions.y+ResourcesManager.roomHeight)/mWrapper.getHeight())* windowDimensions.y));
			}
			//Log.e("Scrollable rooms container mWrapper.getY() ", mWrapper.getY() +" : mCurrentY >> "+ mCurrentY);
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
			mCurrentY += ny;
		accel = (accel * FRICTION);
		//Log.e("mCurrentY", mCurrentY+"");
		//Log.e("accel", accel+"");

		//mWrapper.setPosition(0, mCurrentY);
	}


	public void createBackground() {
		ITextureRegion backgroundRegion =  null;
		if(ResourcesManager.getInstance().hostImageclicked){
			//backgroundRegion=resourcesManager.host_room_background_region; //Uncomment For Background
		}
		else{
			//backgroundRegion=resourcesManager.guess_room_background_region; //Uncomment For Background
		}
		Sprite background = new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , backgroundRegion, vbom){

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

	public void setBackgroundColor() {
		if(ResourcesManager.getInstance().hostImageclicked){
			this.getBackground().setColor(0.21f, 0.9f, 0.29f);
		}
		else{
			this.getBackground().setColor(0.9f, 0.21564f, 0.29f);
		}

	}
	public void onBackKeyPressed() {
		//clear the char set
		if(SceneManager.getInstance().scrollableRoomScene.hasParent()){
			SceneManager.getInstance().scrollableRoomScene.detachSelf();
		}
		mWrapper = null;
		
		
		SceneManager.getInstance().setScene(SceneManager.getInstance().hostGuessGameScene);
		
		this.back();
		
		Debug.e("Rooms Scene Back Key Pressed");
		
		ResourcesManager.getInstance().cancelAsyncAdLoadTasks();
		
		if(null!=SceneManager.getInstance().scrollableRoomScene){
			Runnable runOnUpdate=new Runnable() {
				
				@Override
				public void run() {
					SceneManager.getInstance().scrollableRoomScene.detachChildren();
					SceneManager.getInstance().scrollableRoomScene = null;
				}
			};
			ResourcesManager.getInstance().activity.runOnUpdateThread(runOnUpdate);
			
		}
	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_ROOMS;
	}

	@Override
	public void disposeScene()
	{

		this.clearUpdateHandlers();
		this.clearTouchAreas();
		this.clearEntityModifiers();
		this.detachChildren();
		this.dispose();
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if(mState==STATE_WAIT){
			ResourcesManager.roomsScrolled=false;
		}
		super.onManagedUpdate(pSecondsElapsed);
	}


}