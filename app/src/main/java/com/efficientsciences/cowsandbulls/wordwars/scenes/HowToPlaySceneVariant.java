package com.efficientsciences.cowsandbulls.wordwars.scenes;


import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.shape.Shape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.debug.Debug;

import android.opengl.GLES20;
import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.graphics.ShapeScrollContainer;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ShapeScrollContainer.IShapeScrollContainerTouchListener;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;

public class HowToPlaySceneVariant extends BaseScene {

	private static Sprite backButton;
	private static String creditsString;
	private static String creditsString2;
	private static String creditsString3;
	private static String creditsString4;
	private static String creditsString5;
	private static String creditsString6;
	private static String creditsString7;
	private static String creditsString8;
	private static String creditsString9;
	private static String creditsString10;
	private static String creditsString11;
	private static String creditsString12;
	private static String creditsString13;
	private static String creditsString14;
	protected Scene mScene;
	private static Text creditsText;
	private static Text creditsText2;
	private static Text creditsText3;
	private static Text creditsText4;
	private static Text creditsText5;
	private static Text creditsText6;
	private static Text creditsText7;
	private static Text creditsText8;
	private static Text creditsText9;
	private static Text creditsText10;
	private static Text creditsText11;
	private static Text creditsText12;
	private static Text creditsText13;
	private static Text creditsText14;
	private static Text titleText;
	private static float width;
	private static float height;
	private static ScrollDetector mScrollDetector;
	private static boolean manualScrolling;
	private static boolean creditsFinished;
	private static boolean backButtonDisplayed;


	@Override
	public void createScene() {
		height=windowDimensions.y;
		width=windowDimensions.x;
		mScene=this;
		//resourcesManager.engine.registerUpdateHandler(new FPSLogger());
		this.setBackground(new Background(254/255f, 210/255f, 59/255f));

		backButtonDisplayed = false;
		manualScrolling = false;
		creditsFinished = false;

		titleText = new Text(0, 0, ResourcesManager.getInstance().mBitmapHowToPlayLarge, "HOW TO PLAY", new TextOptions(HorizontalAlign.CENTER),
				vbom);
		titleText.setPosition(windowDimensions.x / 2, windowDimensions.y - HowToPlaySceneVariant.titleText.getHeight());
		titleText.setAlpha(0.68f);

		backButton = new Sprite(windowDimensions.x / 2, 50, ResourcesManager.mMenuBackTextureRegion, vbom) {
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				SceneManager.getInstance().setScene(SceneType.SCENE_MENU);
				Debug.e("How To Play Scene Back Key Pressed");
				mScene.back();
				return true;
			}
		};
		creditsString = "A person thinks of a word (An ISOGRAM - A word with all unique letters),\n & hosts the word for guessing. \n";
		creditsString2 = "Guessers will be provided with the number of letters in the word. \n\n" ;
		creditsString3 = "Everyone except the host in the same room will start making guesses,\n";
		creditsString3 =  "the number of Cows & Bulls in the word will be given for each guess.\n\n\n";
		creditsString4 = "Cows - The guessed word has letter/letters from the word hosted \n but at different positions.\n";
		creditsString5 = "Bulls - The guessed word has letters at the same position as that of the word hosted.\n\n\n";
		creditsString6 ="e.g. Say host thinks of a word \"BOAT\", hosts it \n and everyone else starts making their guesses.\n\n";
		creditsString7 ="You guess: \"ENVY\" \n\n";
		creditsString8 ="App will answer you as follows: \n COWS=0 (No similar letters at all) \n BULLS=0 (No letters in the same position)\n\n\n";
		creditsString9 ="You guess: \"TOAD\"\n\n";
		creditsString10 ="COWS = 1 (Since the letter 'T' is present in hosted word, \n but at different position than the word hosted, i.e. in the word \"BOAT\",\n\n";
		creditsString11 ="the letter \'T\' is at  4th spot whereas it\'s at 1st position in word guessed.)\n\n";
		creditsString12 ="BULLS = 2 (Since the letters \'O\' and \'A\' are present at the exact same positions \n as that of the word hosted)\n\n";
		creditsString13 ="Winner would be the one who gets it right (All BULLS). \n **BULLSEYE**\n\n\n\n";
		creditsString14 = "Inspired By old Classic Game";

		creditsText = createTextScrolls(creditsString);
		creditsText2 = createTextScrolls(creditsString2);
		creditsText3 = createTextScrolls(creditsString3);
		creditsText4 = createTextScrolls(creditsString4);
		creditsText5 = createTextScrolls(creditsString5);
		creditsText6 = createTextScrolls(creditsString6);
		creditsText7 = createTextScrolls(creditsString7);
		creditsText8 = createTextScrolls(creditsString8);
		creditsText9 = createTextScrolls(creditsString9);
		creditsText10 = createTextScrolls(creditsString10);
		creditsText11 = createTextScrolls(creditsString11);
		creditsText12 = createTextScrolls(creditsString12);
		creditsText13 = createTextScrolls(creditsString13);
		creditsText14 = createTextScrolls(creditsString14);

		ShapeScrollContainer ScrollableArea = new ShapeScrollContainer(0, 0, width, height, new IShapeScrollContainerTouchListener() {
			
			@Override
			public void OnContentClicked(Shape pShape) {
				Log.e("How To Play Variant :", " ScrollComp clicked");
				
			}
		});
		this.registerTouchArea(ScrollableArea);
		this.attachChild(ScrollableArea);
		
		
		mScene.attachChild(creditsText);
		mScene.attachChild(creditsText2);
		mScene.attachChild(creditsText3);
		mScene.attachChild(creditsText4);
		mScene.attachChild(creditsText5);
		mScene.attachChild(creditsText6);
		mScene.attachChild(creditsText7);
		mScene.attachChild(creditsText8);
		mScene.attachChild(creditsText9);
		mScene.attachChild(creditsText10);
		mScene.attachChild(creditsText11);
		mScene.attachChild(creditsText12);
		mScene.attachChild(creditsText13);
		mScene.attachChild(creditsText14);
		
		
		ScrollableArea.Add(creditsText);
		ScrollableArea.Add(creditsText2);
		ScrollableArea.Add(creditsText3);
		ScrollableArea.Add(creditsText4);
		ScrollableArea.Add(creditsText5);
		ScrollableArea.Add(creditsText6);
		ScrollableArea.Add(creditsText7);
		ScrollableArea.Add(creditsText8);
		ScrollableArea.Add(creditsText9);
		ScrollableArea.Add(creditsText10);
		ScrollableArea.Add(creditsText11);
		ScrollableArea.Add(creditsText12);
		ScrollableArea.Add(creditsText13);
		ScrollableArea.Add(creditsText14);
		
		//this.attachChild(creditsText);

		this.attachChild(titleText);

		
		//Whether you want to allow user to scroll vertical/horizontal
		ScrollableArea.SetScrollableDirections(true, false);
		//Only allow the user to scroll in a direction to available content
		//(no more content in that direction - the user will be prevented from scrolling)
		ScrollableArea.SetScrollLock(false);
		//By how much over the last content in any direction the user is allowed to scroll (% of height/width)
		//ScrollableArea.SetAlphaPadding(15.0f, 0);
		//Allow ShapeScrollContainer to increase alpha of contents and by what distance it starts inside
		//the ShapeScrollContainer itself. (Fades content as it approaches the edges due to user scrolling)
		//ScrollableArea.SetScrollLockPadding(50.0f,0.0f);
		//Whether scroll bars will be visible, (horizontal/vertical)
		ScrollableArea.SetScrollBarVisibitlity(true,true);

	}


	private Text createTextScrolls(String creditsStringLocal) {
		Text local = new Text(0, 0, ResourcesManager.mSmallFont, creditsStringLocal, new TextOptions(HorizontalAlign.CENTER),
				vbom);
		local.setAnchorCenter(0.5f, 1f); // center on x-axis, bottom for y-axis
		//creditsText.setPosition(windowDimensions.x / 2, 0);
		local.setAlpha(0.7f);
		local.setVisible(true);
		local.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		return local;
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
