package com.efficientsciences.cowsandbulls.wordwars.graphics;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.scenes.BaseScene;

public class MomentumScrollerUnMod extends BaseScene implements IScrollDetectorListener, IOnSceneTouchListener {
	 
	 private static final float FREQ_D = 120.0f;
	 
	 private static final int STATE_WAIT = 0;
	 private static final int STATE_SCROLLING = 1;
	 private static final int STATE_MOMENTUM = 2;
	 private static final int STATE_DISABLE = 3;
	 
	 private static final int WRAPPER_HEIGHT = 1260;
	 private static final float MAX_ACCEL = 5000;

	 private static final double FRICTION = 0.96f;
	 
	 private TimerHandler thandle;
	 private int mState = STATE_DISABLE;
	 private double accel, accel1, accel0;
	 private float mCurrentY;
	 private IEntity mWrapper;
	 private SurfaceScrollDetector mScrollDetector;
	 private long t0;
	 

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
			// TODO Auto-generated method stub
			
	  Scene scene = this;
	  scene.setBackground(new Background(0.1f, 0.1f, 0.1f));
	  
	  mScrollDetector = new SurfaceScrollDetector(this);
	  scene.setOnSceneTouchListener(this);
	  
	  mWrapper = new Entity(0, 0);
	  for (int i = 0; i < 50; i++) {
	   Rectangle r = new Rectangle(ResourcesManager.getInstance().windowDimensions.x/2f, i * 100 + 20, ResourcesManager.getInstance().windowDimensions.x-100, ResourcesManager.getInstance().windowDimensions.y/5f, ResourcesManager.getInstance().vbom);
	   r.setAnchorCenter(0.5f, 0);
	   createTextAndAddToRect(r);
	   mWrapper.attachChild(r);
	  }
	  scene.attachChild(mWrapper);
	  scene.registerUpdateHandler(thandle);
	  mState = STATE_WAIT;
	 // return scene;
	 }

	 private void createTextAndAddToRect(Rectangle r) {
		// TODO Auto-generated method stub
		
	}

	@Override
	 public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
	  if (mState == STATE_DISABLE)
	   return true;
	  
	  if (mState == STATE_MOMENTUM) {
	   accel0 = accel1 = accel = 0;
	   mState = STATE_WAIT;
	  }

	  mScrollDetector.onTouchEvent(pSceneTouchEvent);
	  return true;
	 }

	 @Override
	 public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
	  t0 = System.currentTimeMillis();
	  mState = STATE_SCROLLING;
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
	  
	  t0 = System.currentTimeMillis();
	  mState = STATE_SCROLLING;
	  
	 }

	 @Override
	 public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
	  mState = STATE_MOMENTUM;
	 }

	 protected synchronized void doSetPos() {
	  
	  if (accel == 0) {
	   return;
	  }
	  
	  if (mCurrentY > 0) {
	   mCurrentY = 0;
	   mState = STATE_WAIT;
	   accel0 = accel1 = accel = 0;
	  }
	  if (mCurrentY < -WRAPPER_HEIGHT) {
	   mCurrentY = -WRAPPER_HEIGHT;
	   mState = STATE_WAIT;
	   accel0 = accel1 = accel = 0;
	  }
	  mWrapper.setPosition(0, mCurrentY);
	  

	  
	  if (accel < 0 && accel < -MAX_ACCEL)
	   accel0 = accel1 = accel = - MAX_ACCEL;
	  if (accel > 0 && accel > MAX_ACCEL)
	   accel0 = accel1 = accel = MAX_ACCEL;
	  
	  double ny = accel / FREQ_D;
	  if (ny >= -1 && ny <= 1) {
	   mState = STATE_WAIT;
	   accel0 = accel1 = accel = 0;
	   return;
	  }
	  if (! (Double.isNaN(ny) || Double.isInfinite(ny)))
	   mCurrentY += ny;
	  accel = (accel * FRICTION);
	 }

	
	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SceneType getSceneType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disposeScene() {
		// TODO Auto-generated method stub
		
	}


	}