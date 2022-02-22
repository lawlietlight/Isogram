package com.efficientsciences.cowsandbulls.wordwars.scenes;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;

import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.domain.BannerProperties;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunkFactory;
import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.ResourceManagerForMiniGame;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;
/*import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.AdEventListener;*/

public class ShopBannersScene extends BaseScene implements IScrollDetectorListener, IOnSceneTouchListener {

	private static final float FREQ_D = 120.0f;

	private static final byte STATE_WAIT = 0;
	private static final byte STATE_SCROLLING = 1;
	private static final byte STATE_MOMENTUM = 2;
	private static final byte STATE_DISABLE = 3;

	private static final int WRAPPER_HEIGHT = 1260;
	private static final float MAX_ACCEL = 5500*ResourcesManager.resourceScaler;

	private static final double FRICTION = 0.99f;

	private static final float resourceScalerLocal = 1.25f;
	private byte mState;
	private double accel, accel1, accel0;
	private float mCurrentY;
	private IEntity mWrapper;
	private ScrollDetector mScrollDetector;
	private long t0;

	private boolean ScrollBarVerticalVisible = true;
	private boolean ScrollBarVisibleOnlyOnTouch = true;

	private Rectangle mRectVertical;
	private Sprite earnCoin;
	private TimerHandler thandle;

	private boolean puzzleScreenHidden;
	Sprite puzzlePaint;


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

		resourcesManager.createAndLoadParticlesystem("gfx/snowParticle/touchSpriteBrightStar.png",6);

		ThemeManager.getInstance().themesKey=ThemeManager.getInstance().getThemePreferences().getThemeSelection();
		//createBackground();

		//	Scene scene = this;
		ITextureRegion textureAlternative = null;

		if(ThemeManager.getInstance().themesKey.equals(THEMES.YELLOWFANTASY.toString())){
			this.setBackgroundColor(true);
			textureAlternative = ResourcesManager.getInstance().shopBeesGreenRegion;
		}
		else{
			this.setBackgroundColor(false);

			textureAlternative = ResourcesManager.getInstance().shopBeesBlueRegion;
		}

		mScrollDetector = new SurfaceScrollDetector(this);
		this.setOnSceneTouchListener(this);


		ResourcesManager.getInstance().maxBanners = 6;
		ResourcesManager.getInstance().maxBannersPerPage=2;
		for (int i = 1; i <= ResourcesManager.getInstance().maxBanners; i++) {

			BannerProperties banner = new BannerProperties(ResourcesManager.getInstance().windowDimensions.x/2f, (ResourcesManager.getInstance().maxBanners+1-i) * ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxBannersPerPage, ResourcesManager.getInstance().windowDimensions.x, (ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxBannersPerPage)+1, i, textureAlternative, ResourcesManager.getInstance().vbom);
			ResourcesManager.bannerHeight=banner.getHeight();
			/*if(i==6){
				banner.setAlpha(0.3f);
			}*/
			createTextAndAddToRect(banner,i);
			this.registerTouchArea(banner);
			mWrapper.attachChild(banner);
		}
		mWrapper.setHeight((ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxBannersPerPage)*ResourcesManager.getInstance().maxBanners);
		mWrapper.setAnchorCenter(0.5f, 1f); // center on x-axis, upper top for y-axis

		mWrapper.setPosition(0,  0+windowDimensions.y-ResourcesManager.bannerHeight);
		mState = STATE_WAIT;
		this.attachChild(ResourcesManager.getInstance().particleSystemStars);
		this.attachChild(mWrapper);
		puzzlePaint= new Sprite(ResourcesManager.getInstance().windowDimensions.x/2, ResourcesManager.getInstance().windowDimensions.y, ResourcesManager.getInstance().shopPaintCurtainRegion, vbom);
		//puzzlePaint.setIgnoreUpdate(true);
		//puzzlePaint.setZIndex(3);
		puzzlePaint.setAnchorCenter(0.5f, 0.85f);
		float scale = ResourcesManager.getInstance().windowDimensions.x/puzzlePaint.getWidth();
		puzzlePaint.setScale(scale);

		//puzzlePaint.setWidth(ResourcesManager.getInstance().windowDimensions.x);
		this.attachChild(puzzlePaint);
		createHud();
		//float heght = ResourcesManager.getInstance().windowDimensions.y/ResourcesManager.getInstance().maxBanners;
		mRectVertical = new Rectangle(ResourcesManager.getInstance().windowDimensions.x, ResourcesManager.getInstance().windowDimensions.y, (ResourcesManager.getInstance().windowDimensions.x * .007f), (40),ResourcesManager.getInstance().vbom);
		mRectVertical.setColor(0.0f, 0.0f, 0.0f,0.5f);
		mRectVertical.setAnchorCenter(1, 1);
		this.attachChild(mRectVertical);

		onCreateResources();
		this.registerUpdateHandler(thandle);

		if(!backButtonDisplayed){
			attachBackButton();
		}


		
		// return scene;
	}

	/**
	 * @param puzzlePaint
	 */
	private void createHud() {
		//float initialOffset=30*ResourcesManager.resourceScaler;
		float interElementalHUDOffset = 50*ResourcesManager.resourceScaler;
		float initialHudOffset = 45*ResourcesManager.resourceScaler;
		ResourcesManager.getInstance().hudRect = new Rectangle(0, windowDimensions.y, windowDimensions.x, (PageChunkFactory.pageChunkYOffset-38)*ResourcesManager.resourceScaler, vbom);
		ResourcesManager.getInstance().hudRect.setColor(1f,1f,1f,0.4f);
		ResourcesManager.getInstance().hudRect.setAnchorCenter(0, 1);


		ResourcesManager.getInstance().beeHudAnim = new AnimatedSprite(initialHudOffset,
				ResourcesManager.getInstance().hudRect.getHeightScaled()/2 ,ThemeManager.getInstance().beeTextureRegion, vbom);
		ResourcesManager.getInstance().beeHudAnim.setScale((0.33f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().beeHudAnim.animate(100);
		ResourcesManager.getInstance().beeHudAnim.setAnchorCenter(0, 0.65f);

		ResourcesManager.getInstance().numberOfBeeFliesText=new Text( ResourcesManager.getInstance().beeHudAnim.getX()+ ResourcesManager.getInstance().beeHudAnim.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontWooden, " X "+StorageManager.getInstance().getUser().getNumOfBees(),150,vbom);
		ResourcesManager.getInstance().numberOfBeeFliesText.setScale((0.8f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().numberOfBeeFliesText.setAnchorCenter(0, 1);
		ResourcesManager.getInstance().numberOfBeeFliesText.setColor(0.4f,0.2f,0.068f);

		ResourcesManager.getInstance().wordIqText=new Text( ResourcesManager.getInstance().numberOfBeeFliesText.getX()+ ResourcesManager.getInstance().numberOfBeeFliesText.getWidthScaled() + interElementalHUDOffset , 
				ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontWooden,
				"WORD IQ: "+ResourcesManager.getInstance().level.getWordIq().toString(),150, vbom);
		ResourcesManager.getInstance().wordIqText.setIgnoreUpdate(true);
		ResourcesManager.getInstance().wordIqText.setScale((0.6f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().wordIqText.setAnchorCenter(0, 1);
		ResourcesManager.getInstance().wordIqText.setColor(0.4f,0.2f,0.068f);

		ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().beeHudAnim);
		ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().numberOfBeeFliesText);

		thememanager.getCoinToHud();
		ResourcesManager.getInstance().hudCoin.setScale(((0.6f)*ResourcesManager.resourceScaler/2)/resourceScalerLocal);
		ResourcesManager.getInstance().hudCoin.setAnchorCenter(0f, 0.65f);
		//ResourcesManager.getInstance().hudCoin.setAnchorCenterY(0.75f);
		ResourcesManager.getInstance().hudCoin.setPosition(ResourcesManager.getInstance().wordIqText.getX()+ ResourcesManager.getInstance().wordIqText.getWidthScaled()+interElementalHUDOffset ,  ResourcesManager.getInstance().hudRect.getHeightScaled()/2);
		ResourcesManager.getInstance().numberOfCoinsText=new Text(ResourcesManager.getInstance().hudCoin.getX()+ ResourcesManager.getInstance().hudCoin.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontWooden, " X "+ StorageManager.getInstance().getUser().getNumOfCoins(),150,vbom);
		ResourcesManager.getInstance().numberOfCoinsText.setScale((0.8f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().numberOfCoinsText.setAnchorCenter(0, 1);
		ResourcesManager.getInstance().numberOfCoinsText.setColor(0.4f,0.2f,0.068f);

		ResourcesManager.getInstance().bullRunHudAnim = new AnimatedSprite(ResourcesManager.getInstance().numberOfCoinsText.getX()+ ResourcesManager.getInstance().numberOfCoinsText.getWidthScaled() + interElementalHUDOffset ,
				ResourcesManager.getInstance().hudRect.getHeightScaled() ,ThemeManager.getInstance().bullTextureRegion, vbom);
		ResourcesManager.getInstance().bullRunHudAnim.setScale((0.25f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().bullRunHudAnim.animate(100);
		ResourcesManager.getInstance().bullRunHudAnim.setAnchorCenter(0, 0.65f);

		ResourcesManager.getInstance().numberOfBullRunsText=new Text( ResourcesManager.getInstance().bullRunHudAnim.getX()+ ResourcesManager.getInstance().bullRunHudAnim.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontWooden,"X "+StorageManager.getInstance().getUser().getNumOfBullRuns(),150,vbom);
		ResourcesManager.getInstance().numberOfBullRunsText.setScale((0.8f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().numberOfBullRunsText.setAnchorCenter(0, 1);
		ResourcesManager.getInstance().numberOfBullRunsText.setColor(0.4f,0.2f,0.068f);



		ResourcesManager.getInstance().numberOfHoneyCombsSmallText=new Text( ResourcesManager.getInstance().beeHudAnim.getX()+ ResourcesManager.getInstance().beeHudAnim.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontWooden, " X "+StorageManager.getInstance().getUser().getNumOfBees(),150,vbom);
		ResourcesManager.getInstance().numberOfHoneyCombsSmallText.setScale((0.8f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().numberOfHoneyCombsSmallText.setAnchorCenter(0, 1);
		ResourcesManager.getInstance().numberOfHoneyCombsSmallText.setColor(0.4f,0.2f,0.068f);


		ResourcesManager.getInstance().numberOfHoneyCombsText=new Text( ResourcesManager.getInstance().beeHudAnim.getX()+ ResourcesManager.getInstance().beeHudAnim.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontWooden, " X "+StorageManager.getInstance().getUser().getNumOfBees(),150,vbom);
		ResourcesManager.getInstance().numberOfHoneyCombsText.setScale((0.8f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().numberOfHoneyCombsText.setAnchorCenter(0, 1);
		ResourcesManager.getInstance().numberOfHoneyCombsText.setColor(0.4f,0.2f,0.068f);


		ResourcesManager.getInstance().numberOfRedCapesText=new Text( ResourcesManager.getInstance().beeHudAnim.getX()+ ResourcesManager.getInstance().beeHudAnim.getWidthScaled() ,  ResourcesManager.getInstance().hudRect.getHeightScaled(), ResourcesManager.getInstance().mBitmapFontWooden, " X "+StorageManager.getInstance().getUser().getNumOfBees(),150,vbom);
		ResourcesManager.getInstance().numberOfRedCapesText.setScale((0.8f*ResourcesManager.resourceScaler)/resourceScalerLocal);
		ResourcesManager.getInstance().numberOfRedCapesText.setAnchorCenter(0, 1);
		ResourcesManager.getInstance().numberOfRedCapesText.setColor(0.4f,0.2f,0.068f);


		earnCoin = new Sprite(windowDimensions.x,windowDimensions.y/2, resourcesManager.earnCoinsRegion, vbom){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()  || pSceneTouchEvent.isActionOutside()){
					ResourceManagerForMiniGame.isBeeFly= true;
					//ResourceManagerForMiniGame.isBeeFly= false;
					SceneManager.getInstance().earnCoinsMiniGameScene();

					return true;
				}
				return false;
			};
		};
		earnCoin.setScale(0.35f*ResourcesManager.resourceScaler);
		earnCoin.setAnchorCenter(1f, 0.5f);
		this.registerTouchArea(earnCoin);	
		this.attachChild(earnCoin);

		ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().bullRunHudAnim);
		ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().numberOfBullRunsText);
		ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().wordIqText);
		ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().hudCoin);
		ResourcesManager.getInstance().hudRect.attachChild(ResourcesManager.getInstance().numberOfCoinsText);
		ResourcesManager.getInstance().hudRect.setHeight(ResourcesManager.getInstance().wordIqText.getHeightScaled()*(1.75f));
		ResourcesManager.getInstance().beeHudAnim.setY(ResourcesManager.getInstance().hudRect.getHeightScaled()/2);
		ResourcesManager.getInstance().numberOfBeeFliesText.setY(ResourcesManager.getInstance().hudRect.getHeightScaled());
		ResourcesManager.getInstance().numberOfBullRunsText.setY(ResourcesManager.getInstance().hudRect.getHeightScaled());
		ResourcesManager.getInstance().wordIqText.setY(ResourcesManager.getInstance().hudRect.getHeightScaled());
		ResourcesManager.getInstance().hudCoin.setY(ResourcesManager.getInstance().hudRect.getHeightScaled()/2);
		ResourcesManager.getInstance().numberOfCoinsText.setY(ResourcesManager.getInstance().hudRect.getHeightScaled());
		ResourcesManager.getInstance().hudRect.setZIndex(24);
		this.attachChild(ResourcesManager.getInstance().hudRect);
		this.sortChildren();
	}

	/**
	 * 
	 */
	public void createBackground() {
		/*Sprite background = new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.shopBackgroundRegion, vbom){

			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) 
			{
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		background.setSize(windowDimensions.x, windowDimensions.y);
		background.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		attachChild(background);*/ //uncomment For shop_puzzle_bg.png 
	}

	private void createTextAndAddToRect(BannerProperties r,int scrollIndex) {
		//Add Banners to Map

		/*Set<Entry<String, BannerStream>> entrySet = ResourcesManager.BannersState.entrySet();
		if(null != entrySet)
		for (Entry<String, BannerStream> entrySetValue : entrySet) {
			Integer BannerIndex=Integer.parseInt(entrySetValue.getKey());
			BannerStream Banner = entrySetValue.getValue();
		}*/
		//Copy data Fetched from server to Local Banner r
		if(scrollIndex%2==0){
			r.setColor(80/255f, 80/255f, 80/255f);
		}
		else{
			r.setColor(5/255f, 10/255f, 15/255f);
		}
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
		ResourcesManager.bannersScrolled=true;
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
		//Log.e("Scrollable Banners container acceleration", accel+"  pDistanceY:   "+pDistanceY);

		t0 = System.currentTimeMillis();
		mState = STATE_SCROLLING;
		//ResourcesManager.bannersScrolled=true;


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
		if (mCurrentY > 0-windowDimensions.y+ResourcesManager.bannerHeight) {
			//Log.e("mCurrentY > 0",ResourcesManager.bannerHeight+ " : " + ResourcesManager.bannerHeight);

			mCurrentY = 0-windowDimensions.y+ResourcesManager.bannerHeight;
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0;
		}
		if (mCurrentY < -mWrapper.getHeight()) {
			//Log.e("mCurrentY < -mWrapper.getHeight()",mCurrentY+ " : " + -mWrapper.getHeight());
			mCurrentY = -mWrapper.getHeight();
			mState = STATE_WAIT;
			accel0 = accel1 = accel = 0.0;
		}
		if(accel==0){
			ResourcesManager.bannersScrolled=false;
		}

		/*if (mWrapper.getY() > windowDimensions.y + mWrapper.getHeight()) {
			Log.e("Scrollable Banners container mWrapper.getY() ", mWrapper.getY() +" : "+windowDimensions.y + mWrapper.getHeight() );
		}*/
		/*if (mWrapper.getY() > windowDimensions.y + mWrapper.getHeight()) {
			//mWrapper.setPosition(mWrapper.getX(), 0);
		} */
		else {
			mWrapper.setPosition(mWrapper.getX(), -mCurrentY);
			if(!puzzleScreenHidden && -mCurrentY>windowDimensions.y-(ResourcesManager.bannerHeight/2)){
				//Log.e("Scrollable Banners container mWrapper.getY() ", mWrapper.getY() +" : mCurrentY >> "+ mCurrentY);
				//Log.e("threshold ", ""+(-windowDimensions.y+2*ResourcesManager.bannerHeight));
				puzzleScreenHidden=true;
				double t= 1;
				if(accel>100 && mCurrentY>0){
					t=Math.sqrt(mCurrentY/accel);
				}
				MoveYModifier moveMod=new MoveYModifier((float) t, puzzlePaint.getY(), ResourcesManager.getInstance().windowDimensions.y+puzzlePaint.getHeightScaled()+10*ResourcesManager.resourceScaler);
				moveMod.setAutoUnregisterWhenFinished(true);
				puzzlePaint.registerEntityModifier(moveMod);
			}
			else if(puzzleScreenHidden && -mCurrentY<windowDimensions.y-(ResourcesManager.bannerHeight/2) ){
				puzzleScreenHidden=false;
				double t= 1;
				if(accel>100 && mCurrentY>0){
					t=Math.sqrt(mCurrentY/accel);
				}
				MoveYModifier moveMod=new MoveYModifier((float) t, puzzlePaint.getY(), ResourcesManager.getInstance().windowDimensions.y);
				moveMod.setAutoUnregisterWhenFinished(true);
				puzzlePaint.registerEntityModifier(moveMod);
			}
			if((mRectVertical.getY()+(mCurrentY/mWrapper.getHeight())>0) && (mRectVertical.getY()+(mCurrentY/mWrapper.getHeight())< windowDimensions.y)){
				//Log.e("Scrollable Banners container ", "  mRectVertical.getY()+(mCurrentY/windowDimensions.y):   "+ (mRectVertical.getY()+(mCurrentY/windowDimensions.y)));

				mRectVertical.setY((windowDimensions.y-((mWrapper.getY()-windowDimensions.y+ResourcesManager.bannerHeight)/mWrapper.getHeight())* windowDimensions.y));
			}
			//Log.e("Scrollable Banners container mWrapper.getY() ", mWrapper.getY() +" : mCurrentY >> "+ mCurrentY);
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


	public void setBackgroundColor(boolean b) {
		if(b)
			this.getBackground().setColor(87/255f, 145/255f, 0/255f);
		else{
			this.getBackground().setColor(87/255f, 145/255f, 0/255f);
		}
		//this.setAlpha(0.4f);
	}
	public void onBackKeyPressed() {
		//clear the char set
		if(ThemeManager.getInstance().honeycombAnim!=null && ThemeManager.getInstance().honeycombAnim.hasParent()){
			ResourcesManager.getInstance().detachInUpdateThread(ThemeManager.getInstance().honeycombAnim);
		}
		if(SceneManager.getInstance().shopBannerScene.hasParent()){
			SceneManager.getInstance().shopBannerScene.detachSelf();
		}
		mWrapper = null;


		SceneManager.getInstance().setScene(SceneType.SCENE_MENU);

		this.back();

		Debug.e("Banners Scene Back Key Pressed");

		if(null!=SceneManager.getInstance().shopBannerScene){
			Runnable runOnUpdate=new Runnable() {

				@Override
				public void run() {
					SceneManager.getInstance().shopBannerScene.detachChildren();
					SceneManager.getInstance().shopBannerScene = null;
				}
			};
			ResourcesManager.getInstance().activity.runOnUpdateThread(runOnUpdate);
		}
		ResourcesManager.getInstance().unloadShopSceneResources();
	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_BANNERS;
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
			ResourcesManager.bannersScrolled=false;
		}
		super.onManagedUpdate(pSecondsElapsed);
	}


}