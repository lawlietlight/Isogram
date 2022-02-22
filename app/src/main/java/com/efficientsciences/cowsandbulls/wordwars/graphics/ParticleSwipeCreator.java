/**
 * ParticleSwipeCreator.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.CircleOutlineParticleEmitter;
import org.andengine.entity.particle.emitter.PointParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.ColorParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.particle.modifier.ScaleParticleModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;
import org.andengine.util.math.MathUtils;

import android.graphics.Point;
import android.opengl.GLES20;
import android.util.FloatMath;
import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;

/**
 * @author SATHISH
 *
 */
public class ParticleSwipeCreator {

	private static final ParticleSwipeCreator INSTANCE = new ParticleSwipeCreator();


	public static ParticleSwipeCreator getInstance() {
		return INSTANCE;
	}

	//private Swipe swipe;
	float mTouchDownX;
	float mTouchDownY;
	float mAngle;
	private CircleOutlineParticleEmitter particleEmitter;
	public SpriteParticleSystem particleSystem;
	private int previousX;
	private int previousY;
	private static final float FREQ_D = 120.0f;
	Queue<Point> myQ=new LinkedList<Point>();

	TimerHandler thandle;

	@Deprecated
	protected void onCreateResources() {
		if(null==thandle){
			thandle = new TimerHandler(1.0f / FREQ_D, true, new ITimerCallback() {
				@Override
				public void onTimePassed(final TimerHandler pTimerHandler) {
					Point prevPoints=myQ.poll();
					if(null!=prevPoints){
						/*if(mTouchDownX<0){
							mTouchDownX=0;
						}
						if(mTouchDownY<0){
							mTouchDownY=0;
						}

						if(mTouchDownX<prevPoints.x){
							mTouchDownX++;
						}
						else if(mTouchDownX>prevPoints.x){
							mTouchDownX--;
						}

						if(mTouchDownY<prevPoints.y){
							mTouchDownY++;
						}
						else if(mTouchDownX>prevPoints.y){
							mTouchDownY--;
						}

						moveTrail(mTouchDownX, mTouchDownY, 0,SceneManager.getInstance().getCurrentScene());
						Log.e("Points", "mTouchDownX: "+mTouchDownX + "   mTouchDownY: "+mTouchDownY);*/
						drawLine(prevPoints.x, prevPoints.y);
					}
				}
			});
		}
	}

	public boolean onSceneTouchEvent(TouchEvent pSceneTouchEvent) {
		Scene pScene=SceneManager.getInstance().getCurrentScene();
		if (pSceneTouchEvent.isActionDown()) {
			mTouchDownX = pSceneTouchEvent.getX();
			mTouchDownY = pSceneTouchEvent.getY();

			if (particleSystem == null) {
				initTrail(mTouchDownX, mTouchDownY,pScene);
				/*swipe = new Swipe(mTouchDownX, mTouchDownY,
						ResourcesManager.getInstance().swipeRegion, ResourcesManager.getInstance().vbom);
				swipe.setAnchorCenter(0, 0);
				swipe.setScaleCenter(1f, 1f);
				swipe.setScaleX(0f);
				pScene.attachChild(swipe);*/
			}
			return true;
		} else if (pSceneTouchEvent.isActionMove()) {

			if (particleSystem != null) {
				float pX = pSceneTouchEvent.getX();
				float pY = pSceneTouchEvent.getY();
				//Log.e("Points", "pX: "+pX + "   pY: "+pY);
				//myQ.add(new Point((int)pX,(int)pY));
				moveTrail(pX, pY, 0,pScene);
				/*
				swipe.setRotationCenter(0f, 0f);
				float distance = getDistance(pX, pY);
				float scale = distance / swipe.getWidth();
				scale = (scale > 1.5f) ? 0f : scale;

				swipe.setScaleX(scale*1.5f);
				swipe.setBlendFunction(GL10.GL_SRC_ALPHA,
						GL10.GL_ONE_MINUS_SRC_ALPHA);
				drawLine(pX,pY);
				if (mAngle == 0f)
					mAngle = MathUtils.radToDeg(getAngle(pX, pY));
				swipe.setRotation(mAngle);
				Log.e("Swipe Angle", mAngle+"");*/
			}
			return true;
		} else{
			//mAngle = 0f;
			ResourcesManager.getInstance().removeTouchedSpritePSInstances();
			Log.e("Touched particle", "Bam Bam");
			/*pScene.detachChild(swipe);
				swipe = null;*/
			return true;
		}

		//return false;
	}

	private float getAngle(float pX, float pY) {

		float dx = mTouchDownX - pX;
		float dy = mTouchDownY - pY;
		Log.e("dx", dx+"");
		Log.e("dy", dy+"");
		return MathUtils.atan2(dy, dx);
	}

	private double getDistance(float pX, float pY) {
		float dx = mTouchDownX - pX;
		float dy = mTouchDownY - pY;
		return Math.sqrt(dx * dx + dy * dy);
	}




	private void initTrail(float mX, float mY, Scene pScene) {
		this.particleEmitter = new CircleOutlineParticleEmitter(mX, mY,2f);
		String particlePath= "gfx/snowParticle/touchSprite.png";
		if(THEMES.BLUEMAGIC.toString().equals(ThemeManager.getInstance().themesKey)){
			particlePath= "gfx/snowParticle/touchspritelatest.png";
		}
		try {
			ResourcesManager.getInstance().touchSprite = new AssetBitmapTexture(ResourcesManager.getInstance().activity.getTextureManager(), 
					ResourcesManager.getInstance().activity.getAssets(), particlePath, TextureOptions.BILINEAR);
		} catch (IOException e) {
			Debug.e(e);
		}
		ResourcesManager.getInstance().touchSpriteRegion = TextureRegionFactory.extractFromTexture(
				ResourcesManager.getInstance().touchSprite);
		ResourcesManager.getInstance().touchSprite.load();
		ResourcesManager.getInstance().touchSprite.getTextureOptions().apply();

		this.particleSystem = new SpriteParticleSystem(particleEmitter, 20, 200, 300, ResourcesManager.getInstance().touchSpriteRegion,ResourcesManager.getInstance().vbom);

		//GL_BLEND
		//GL_BLEND_DST_ALPHA
		//GL_BLEND_DST_RGB
		//GL_ALPHA_BITS
		//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SAMPLE_COVERAGE, GLES20.GL_SUBPIXEL_BITS));
		//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_BLEND_DST_RGB));
		if(ThemeManager.getInstance().themesKey.equals(THEMES.BLUEMAGIC.toString())){
			particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_BLEND_DST_ALPHA));
			//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_STATIC_DRAW, GLES20.GL_ONE));
			//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_COLOR, GLES20.GL_DST_ALPHA));
		}
		else{
		
			//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_BLEND_DST_ALPHA));
			//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_INCR));
			particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_COLOR, GLES20.GL_DST_ALPHA));
			//particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_BLEND_DST_RGB));
			particleSystem.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ALPHA_BITS));

		}

		particleSystem.addParticleInitializer(new ExpireParticleInitializer<Sprite>(2f));
		particleSystem.addParticleInitializer(new ColorParticleInitializer<Sprite>(1f, 0f, 0.2784313725490196f));
		particleSystem.addParticleInitializer(new ScaleParticleInitializer<Sprite>(0.8f*ResourcesManager.resourceScaler));
		particleSystem.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0.5f));

		particleSystem.addParticleModifier(new ScaleParticleModifier<Sprite>(0, 1.5f, 0.6f*ResourcesManager.resourceScaler, 0f));
		particleSystem.addParticleModifier(new AlphaParticleModifier<Sprite>(0, 1.5f, 0.6f, 0));
		Color pFromColor= new Color(1f, 0f, 0.2784313725490196f);
		Color pToColor= new Color(0.2f,0.6f,0.3f);
		particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0f,0.3f, pFromColor, pToColor));
		//pFromColor= new Color(0.5f,0.9f,0.5f);
		//pFromColor= new Color(0.1294117647058824f,0.607843137254902f,0.1f);
		pFromColor= new Color(0.2f,0.7f,0.3f);
		pToColor= new Color(0.196078431372549f,0.5627450980392157f,0.9627450980392157f);
		//pToColor= new Color(0.3f,0.5f,1f);
		particleSystem.addParticleModifier(new ColorParticleModifier<Sprite>(0.3f,1f, pFromColor, pToColor));
		pScene.attachChild(particleSystem);
	}


	private void moveTrail(float trailX, float trailY, int count, Scene pScene) {
		if (particleEmitter == null) {
			initTrail(trailX, trailY,pScene);
		}

		particleEmitter.setCenter(trailX, trailY);
	}

	public void drawLine(float currentX, float currentY) {
		int x;
		float y, m;

		previousX = (int) mTouchDownX;
		previousY = (int) mTouchDownY;
		x = (int) previousX;
		y = (int) previousY;

		m = (currentY - previousY) / (currentX - previousX);

		if (Math.round(previousX) == Math.round(currentX)) {
			if (previousY < currentY) {
				for (y = (int) previousY; y < currentY; ++y)
					particleEmitter.setCenter(previousX, y);
			} else {
				for (y = (int) previousY; y > currentY; --y)
					particleEmitter.setCenter(previousX, y);
			}
		} else {
			if (previousX < currentX) {
				for (x = (int) previousX; x < currentX; ++x) {
					y = m * (x - previousX) + previousY;
					particleEmitter.setCenter(x, y);
				}
			} else if (previousX > currentX) {
				for (x = (int) previousX; x > currentX; --x) {
					y = m * (x - currentX) + currentY;
					particleEmitter.setCenter(x, y);
				}
			}
		}
	}

	/**
	 * 
	 */
	public void reset() {
		particleEmitter=null;

	}
}
