package com.efficientsciences.cowsandbulls.wordwars.scenes;


import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;

public class CreditsScene extends BaseScene {

	private static Sprite backButton;
	private static String creditsString;
	protected Scene mScene;
	private static Rectangle mWrapper;
	private static Text creditsText;
	private static Text titleText;
	private static int height;
	private static float width;
	private static ScrollDetector mScrollDetector;
	private static boolean manualScrolling;
	private static boolean creditsFinished;
	private static boolean backButtonDisplayed;

	/*private Rectangle upperbar;
	private Rectangle lowerbar;*/


	@Override
	public void createScene() {
		height=windowDimensions.y;
		width=windowDimensions.x;
		mScene=this;
		/*Gradient g = new Gradient(0, 0, width, height,ResourcesManager.getInstance().vbom);

		g.setGradient(Color.RED, Color.BLUE, 1, 0);

		upperbar =new Rectangle(0.0f, height, (windowDimensions.x), (height * .20f),ResourcesManager.getInstance().vbom);
		upperbar.setColor(254/255f, 210/255f, 59/255f, 0.5f);
		upperbar.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		upperbar.setAnchorCenter(0f, 1f);
		lowerbar =new Rectangle(0.0f, 0.0f, (windowDimensions.x), (height * .20f),ResourcesManager.getInstance().vbom);
		lowerbar.setColor(254/255f, 210/255f, 59/255f, 0.5f);
		lowerbar.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		lowerbar.setAnchorCenter(0, 0);*/
		//gradientOverlay.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		/*this.setBackground(new Background(203/255f, 35/255f, 35/255f));*/
		this.setBackground(new Background(255/255f, 255/255f, 250/255f));

		backButtonDisplayed = false;
		manualScrolling = false;
		creditsFinished = false;

		titleText = new Text(0, 0, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "Credits", new TextOptions(HorizontalAlign.CENTER),
				vbom);
		titleText.setPosition(windowDimensions.x / 2, windowDimensions.y - CreditsScene.titleText.getHeight());
		titleText.setColor(0/255f, 0/255f, 0/255f);
		titleText.setScaleY(1.2f);
		titleText.setAlpha(0.30f);

		backButton = new Sprite(20*ResourcesManager.resourceScaler, 20*ResourcesManager.resourceScaler, ResourcesManager.mMenuBackTextureRegion, vbom) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
				Debug.e("CreditsScene Back Key Pressed");
				mScene.back();
				return true;
			}
		};
		backButton.setAnchorCenter(0, 0);
		backButton.setScale(0.8f*ResourcesManager.resourceScaler);
		backButtonDisplayed = true;
		mScene.attachChild(backButton);
		mScene.registerTouchArea(backButton);
		creditsString = "\n"
				+ "WORD\nIs a code breaking mind-game.\n It has No Candies, Just Colors\n It has No Sugar, Just Honey\n\n"
				+ " Thanks for playing and being part of the huge player base.\n"
				+ " Hope you enjoy it!!!\n\n"
				+ "Design,\nArtWork,\nMusic, And \n Programming By\n Sathish (\"Lawliet, thegreekgeek\" An Indie developer). \n"
				+ "  \n\n"
				+ "\n FONTS USED\n" 
				+ "Comicrelief - Jeff Davis [ info@loudifier.com ]\n"
				+ "Papercut - Created by Eoin [ smulvi@iol.ie ]\n"
				+ "Benwood - BenChalit Sagiamsak\n"
				+ "Titillium \n"
				+ " Neige \n\n" 
				+ "All fonts used are licensed under SIL Open Font License.\n\n"
				+ "I should say thanks to those big hearted font designers\nfor making them available for commercial use.\n\n"
				+ "Icon And Login screen graphics inspired from Freepik.com (Not Derivative Work)\n\n\n"
				+ "DISCLAIMER\n"
				+ "All the in-game graphics and music are worked from ground up \nException Christmas Lights - Designed by Freepik.com\n\n"
				+ "Copying/Ripping of the content for commercial use is strictly prohibited.\n\n"
				+ "Do not cheat with puzzle solvers,\n"
				+ "We play it for fun &\n"
				+ "Of course, Cheating is no fun.\n\n"
				+ "Do not use this game for any other purpose\n"
				+ "other than the way it was intended to be played/used.\n\n"
				+ "This game uses your contact's email addresses\n"
				+ "only for game invitation purposes\n\n\n"
				+"SPECIAL THANKS\n"
				+ "A Special thanks to my family members for bearing with my working hours\n\n\n"
				+ "TO YOU\n"
				+ "We don't judge you by your score,\n"
				+ "And yes you are special\n"
				+ "Wish you the Goodness of all colours from this game,\n"
				+ " Have Fun!!!";

		creditsText = new Text(0, windowDimensions.y-100*ResourcesManager.resourceScaler, ResourcesManager.getInstance().mBitmapHowToPlaySmall, creditsString,new TextOptions(AutoWrap.NONE, windowDimensions.x-(150f*ResourcesManager.resourceScaler), HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
				vbom);
		creditsText.setAnchorCenter(0.5f, 1f); // center on x-axis, upper top for y-axis
		creditsText.setPosition(windowDimensions.x / 2, 0);
		creditsText.setScale(0.4f*ResourcesManager.resourceScaler);
		creditsText.setColor(15/255f, 15/255f, 15/255f);
		//creditsText.setColor(1, 1, 1);

		creditsText.setAlpha(0.8f);

		//creditsText.setAutoWrap(pAutoWrap);
		this.attachChild(creditsText);
		/*		this.attachChild(lowerbar);*/
		this.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (manualScrolling == false) {
					if (creditsText.getY() > windowDimensions.y/2 + 0.5f*creditsText.getHeight()*ResourcesManager.resourceScaler-50*ResourcesManager.resourceScaler) {
						creditsFinished = true;
						creditsText.setPosition(creditsText.getX(), 0);
					}
					else {
						int dropRateReducer=52;
						if(creditsText.getY()<windowDimensions.y/2){
							dropRateReducer=9;
						}
						creditsText.setPosition(creditsText.getX(),
								creditsText.getY() + (float) getDropDistance(dropRateReducer, pSecondsElapsed));
					}
					/*if (creditsFinished == true && backButtonDisplayed == false) {
						
					}*/
				}
			}

			@Override
			public void reset() {
			}
		});

		this.setOnSceneTouchListener(new IOnSceneTouchListener() {
			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionUp()) {
					manualScrolling = false;
					return true;
				} else {
					manualScrolling = true;
					mScrollDetector.onTouchEvent(pSceneTouchEvent);
					return true;
				}
			}

		});

		CreditsScene.mScrollDetector = new SurfaceScrollDetector(new IScrollDetectorListener() {

			@Override
			public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX,
					float pDistanceY) {
			}

			@Override
			public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
				if (creditsText.getY() > windowDimensions.y/2 + 0.5f*creditsText.getHeight()*ResourcesManager.resourceScaler-50*ResourcesManager.resourceScaler) {
					creditsFinished = true;
					creditsText.setPosition(creditsText.getX(), 0);
				} else {
					creditsText.setPosition(creditsText.getX(), creditsText.getY() - pDistanceY);
				}
			}

			@Override
			public void onScrollFinished(ScrollDetector pScollDetector, int pPointerID, float pDistanceX,
					float pDistanceY) {
			}

		});
		CreditsScene.mScrollDetector.setEnabled(true);

		this.attachChild(titleText);

		this.setOnSceneTouchListener(this.getOnSceneTouchListener());
		this.setOnSceneTouchListenerBindingOnActionDownEnabled(true);

	}

	public static double getDropDistance(double dropRate, float mSecondsElapsed) {
		// dropRate is how fast in seconds it should travel the screen
		return (height * mSecondsElapsed) /  dropRate;
	}


	@Override
	public void onBackKeyPressed() {
		SceneManager.getInstance().setScene(SceneType.SCENE_OPTION);
		Debug.e("CreditsScene Back Key Pressed");
		this.back();

	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_CREDITS;
	}

	@Override
	public void disposeScene()
	{
	}



}
