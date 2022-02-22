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

public class HowToPlayScene extends BaseScene {

	private static Sprite backButton;
	private static Sprite illustrateButton;
	private static String creditsString;
	protected Scene mScene;
	private Sprite gradientOverlay;
	private static Rectangle mWrapper;
	private static Text creditsText;
	private static Text titleText;
	
	private static Text illustrationText;
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
		gradientOverlay = new Sprite((windowDimensions.x/2), (windowDimensions.y/2) , resourcesManager.gradientOverlay, vbom);
		gradientOverlay.setSize(windowDimensions.x, windowDimensions.y);
		//gradientOverlay.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		/*this.setBackground(new Background(203/255f, 35/255f, 35/255f));*/
		this.setBackground(new Background(254/255f, 210/255f, 59/255f));

		backButtonDisplayed = false;
		manualScrolling = false;
		creditsFinished = false;

		titleText = new Text(0, 0, ResourcesManager.getInstance().mBitmapHowToPlayLarge, "how to play", new TextOptions(HorizontalAlign.CENTER),
				vbom);
		titleText.setPosition(windowDimensions.x / 2, windowDimensions.y - HowToPlayScene.titleText.getHeight());
		titleText.setColor(0/255f, 0/255f, 0/255f);
		titleText.setScaleY(1.2f);
		titleText.setAlpha(0.70f);
		
		illustrateButton= new Sprite(windowDimensions.x-10*ResourcesManager.resourceScaler, windowDimensions.y-10*ResourcesManager.resourceScaler, ResourcesManager.getInstance().howToPlayIllustrateGreenRegion, vbom) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
				SceneManager.getInstance().howToPlayImageScene();
				Debug.e("How To Play Scene Back Key Pressed");
				}
				return true;
			}
		};
		illustrateButton.setAnchorCenter(1, 1);
		
		
		illustrationText = new Text(illustrateButton.getWidth()/2, illustrateButton.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlayLarge, "how\nto ?\nillUstrAtioN", new TextOptions(HorizontalAlign.CENTER),
				vbom);
		//illustrationText.setPosition(illustrateButton.getWidth()/2, illustrateButton.getHeight()/2);
		illustrationText.setColor(1f, 1f, 1f);
		illustrationText.setScaleX(0.70f);
		illustrationText.setScaleY(0.85f);
		illustrationText.setRotation(45);
		illustrationText.setAnchorCenter(0.5f,0.35f);
		//illustrationText.setAlpha(0.70f);
		illustrateButton.attachChild(illustrationText);
		illustrateButton.setScale(ResourcesManager.resourceScaler/2f);


		backButton = new Sprite(20*ResourcesManager.resourceScaler, 20*ResourcesManager.resourceScaler, ResourcesManager.mMenuBackTextureRegion, vbom) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
				Debug.e("How To Play Scene Back Key Pressed");
				mScene.back();
				return true;
			}
		};
		backButton.setAnchorCenter(0, 0);
		backButton.setScale(ResourcesManager.resourceScaler);
		creditsString = "A person thinks of a word (ISOGRAM - A word with all unique letters),\n & hosts the word (secret code word) for guessing. \n\n"
				+ "Guessers will be provided with the number of letters in the word. \n" 
				+ "Everyone except the host in the same room will start making guesses,\n\n" 
				+  "Easy Mode - Daily Challenge And First 25 Rooms Have Color Codes To Learn The Game.\n\n"
				+ "Green - Letter Is Present In Secret Word At Same Spot\n"
				+ "Orange - Letter Is Present In Secret Word But At Different Spot\n\n"
				+  "The number of Cows & Bulls in the guessed word will be given for each guess.\n\n\n"
+ "Cows (C) - The guessed word has letter/letters from the word hosted \n but at different positions.\n"
+ "Bulls (B) - The guessed word has letter/letters at the same position \n from the word hosted.\n\n\n"
+" Gameplay e.g. Host thinks of a word \"BOAT\", hosts it \n and everyone else starts making their guesses.\n\n"
+"You guess: \"ENVY\" \n\n"
+"You\'ll Get C0B0 : \n COWS (C) = 0 (No similar letters at all) \n BULLS (B) = 0 (No letters in the same position)\n\n\n"
+"You guess: \"TOAD\"\n\n"
+"You'll get C1B2\n Which means there are 3 letters in secret word hosted,\nwith 1 letter in different postion\nand 2 letters in same postion\n\ni.e.\n"
+"COWS (C1) = 1 (1 Letter \'T\' is present in hosted word, \n but at different position than in the word guessed. In the hosted word \"BOAT\",\n"
+"the letter \'T\' is at  4th spot whereas it\'s at 1st position in word guessed)\n\n"
+"BULLS (B2) = 2 (2 Letters \'O\' and \'A\' are present at the exact same positions \n as that of the word hosted)\n\n"
+"Winner would be the one who gets it all right (All BULLS)\n **BULLSEYE**\n\n\n\n"
+ "Inspired By old Classic Game"
+ "\n"
+ "That\'s All Folks";

		creditsText = new Text(0, 0, ResourcesManager.getInstance().mBitmapHowToPlaySmall, creditsString,new TextOptions(AutoWrap.NONE, windowDimensions.x-150f, HorizontalAlign.CENTER, Text.LEADING_DEFAULT),
				vbom);
		creditsText.setAnchorCenter(0.5f, 1f); // center on x-axis, upper top for y-axis
		creditsText.setPosition(windowDimensions.x / 2, 0);
		creditsText.setScale(0.5f*ResourcesManager.resourceScaler);
		creditsText.setColor(0/255f, 0/255f, 0/255f);
		//creditsText.setColor(1, 1, 1);

		creditsText.setAlpha(0.55f);
		
		
		this.registerTouchArea(illustrateButton);
		//creditsText.setAutoWrap(pAutoWrap);
		this.attachChild(creditsText);
		this.attachChild(gradientOverlay);
		this.attachChild(illustrateButton);
/*		this.attachChild(lowerbar);*/
		this.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (manualScrolling == false) {
					if (creditsText.getY() > windowDimensions.y + 0.5f*creditsText.getHeight()*ResourcesManager.resourceScaler-50*ResourcesManager.resourceScaler) {
						creditsFinished = true;
						creditsText.setPosition(creditsText.getX(), 0);
					}
					else {
						int dropRateReducer=36;
						if(creditsText.getY()<windowDimensions.y/2){
							dropRateReducer=9;
						}
						creditsText.setPosition(creditsText.getX(),
								creditsText.getY() + (float) getDropDistance(dropRateReducer, pSecondsElapsed));
					}
					if (creditsFinished == true && backButtonDisplayed == false) {
						backButtonDisplayed = true;
						mScene.attachChild(backButton);
						mScene.registerTouchArea(backButton);
					}
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

		HowToPlayScene.mScrollDetector = new SurfaceScrollDetector(new IScrollDetectorListener() {

			@Override
			public void onScrollStarted(ScrollDetector pScollDetector, int pPointerID, float pDistanceX,
					float pDistanceY) {
			}

			@Override
			public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {
				if (creditsText.getY() > windowDimensions.y + 0.5f*creditsText.getHeight()*ResourcesManager.resourceScaler-50*ResourcesManager.resourceScaler) {
					creditsFinished = true;
				}
				if (creditsText.getY() > windowDimensions.y+ 0.5f*creditsText.getHeight()*ResourcesManager.resourceScaler) {
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
		HowToPlayScene.mScrollDetector.setEnabled(true);

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
		SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
		Debug.e("How To Play Scene Back Key Pressed");
		this.back();

	}

	@Override
	public SceneType getSceneType() {

		return SceneType.SCENE_HOWTOPLAY;
	}

	@Override
	public void disposeScene()
	{
	}



}
