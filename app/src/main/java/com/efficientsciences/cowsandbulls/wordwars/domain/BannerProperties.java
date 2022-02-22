/**
 * BannerProperties.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import android.opengl.GLES20;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;

/**
 * @author SATHISH
 *
 *
 */
public class BannerProperties extends Rectangle {




	private int index;
	private Sprite multiplierBanner1;
	private Sprite multiplierBanner2;

	private Sprite backgroundColumn1;
	private Sprite backgroundColumn2;

	private Sprite buyButtonColumn1;
	private Sprite buyButtonColumn2;

	private Sprite buyCoinColumn1;
	private Sprite buyCoinColumn2;
	Text textLeftCoin;
	Text textRightCoin;
	Text textLeftHeading;
	Text textRightHeading;
	Text textDescColumn1;
	Text textDescColumn2;
	boolean leftBannerLock1;
	boolean leftBannerLock2;
	
	private int leftColumnCoinsCount=500;
	private int rightColumnCoinsCount=500;

	private int leftColumnPackCount=500;
	private int rightColumnPackCount=500;

	AnimatedSprite leftColumnAnimation;
	AnimatedSprite rightColumnAnimation;

	public int getLeftColumnCoinsCount() {
		return leftColumnCoinsCount;
	}

	public void setLeftColumnCoinsCount(int leftColumnCoinsCount) {
		this.leftColumnCoinsCount = leftColumnCoinsCount;
	}

	public int getRightColumnCoinsCount() {
		return rightColumnCoinsCount;
	}

	public void setRightColumnCoinsCount(int rightColumnCoinsCount) {
		this.rightColumnCoinsCount = rightColumnCoinsCount;
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pVertexBufferObjectManager
	 */
	public BannerProperties(float pX, float pY, float pWidth, float pHeight, int index,ITextureRegion pTextureBackground,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.index=index;
		createBackground(pTextureBackground, pVertexBufferObjectManager);
		this.setBlendFunctionDestination(GLES20.GL_ALPHA_BITS);
		this.setAlpha(0);
		this.setAnchorCenter(0.5f, 1);


		this.actionClicked = false;


		//this.setCullingEnabled(true);
	}

	public void createBackground(ITextureRegion pTextureBackground, VertexBufferObjectManager pVertexBufferObjectManager) {
		backgroundColumn1 = new Sprite(ResourcesManager.getInstance().windowDimensions.x/2-50*ResourcesManager.resourceScaler, this.getHeight()/2 , pTextureBackground, pVertexBufferObjectManager);
		backgroundColumn1.setAnchorCenterX(1);
		backgroundColumn1.setAlpha(1f);
		//float scale = (this.getHeight())/backgroundColumn1.getHeight();
		backgroundColumn1.setWidth(this.getWidth()/2.5f);
		backgroundColumn1.setHeight(this.getHeight());

		//backgroundColumn1.setScale(scale);
		//background.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);


		backgroundColumn2 = new Sprite((ResourcesManager.getInstance().windowDimensions.x/2)+50*ResourcesManager.resourceScaler,  this.getHeight()/2  , pTextureBackground, pVertexBufferObjectManager);
		backgroundColumn2.setAnchorCenterX(0);

		backgroundColumn2.setAlpha(1f);
		backgroundColumn2.setWidth(this.getWidth()/2.5f);
		backgroundColumn2.setHeight(this.getHeight());
		//background.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		buyButtonColumn1 = new Sprite(backgroundColumn1.getWidthScaled()/2,  0 , ResourcesManager.getInstance().shopBuyButtonRegion, pVertexBufferObjectManager){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {

				coinTouchAction((byte) 0);

				return true;
			}
		};
		buyButtonColumn1.setAlpha(1f);
		buyButtonColumn1.setScale(ResourcesManager.resourceScaler*0.7f);
		buyButtonColumn1.setAnchorCenter(0.5f,0f);



		buyButtonColumn2 = new Sprite(backgroundColumn2.getWidthScaled()/2,  0 , ResourcesManager.getInstance().shopBuyButtonRegion, pVertexBufferObjectManager){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				coinTouchAction((byte) 1);
				return true;
			}
		};
		buyButtonColumn2.setAlpha(1f);
		buyButtonColumn2.setScale(ResourcesManager.resourceScaler*0.7f);
		buyButtonColumn2.setAnchorCenter(0.5f,0f);




		buyCoinColumn1 =  new Sprite(2, 0.5f, ResourcesManager.getInstance().shopWordCoinRegion, pVertexBufferObjectManager);
		buyCoinColumn2 =  new Sprite(2,  0.5f , ResourcesManager.getInstance().shopWordCoinRegion, pVertexBufferObjectManager);
		buyCoinColumn1.setScale(0.5f);
		buyCoinColumn2.setScale(0.5f);
		buyCoinColumn1.setAnchorCenter(0,0.5f);
		buyCoinColumn2.setAnchorCenter(0,0.5f);
		String buttonTextJoin1=  "You Hold\nX " ;
		String buttonTextJoin2=  "You Hold\nX " ;
		Text multiplierText1=new Text(0,0, ResourcesManager.getInstance().mBitmapFontWooden, "012345789abcdef", pVertexBufferObjectManager);
		multiplierText1.setAnchorCenter(0f,0.5f);
		multiplierText1.setScale(0.40f * ResourcesManager.resourceScaler);
		multiplierText1.setColor(0.2f,0.1f,0.068f);


		Text multiplierText2=new Text(0, 0, ResourcesManager.getInstance().mBitmapFontWooden, "012345789abcdef", pVertexBufferObjectManager);
		multiplierText2.setAnchorCenter(0f,0.5f);
		multiplierText2.setScale(0.40f * ResourcesManager.resourceScaler);
		multiplierText2.setColor(0.2f,0.1f,0.068f);

		String s1;
		String s2;
		switch(index){
		case 1:
			leftColumnCoinsCount = 2000;
			rightColumnCoinsCount = 4000;
			leftColumnPackCount=3;
			rightColumnPackCount=5;
			s1= " "+leftColumnCoinsCount;
			s2= " "+rightColumnCoinsCount;

			textLeftCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler, 0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s1, ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s2, ResourcesManager.getInstance().vbom);

			leftColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn1,ThemeManager.getInstance().beeTextureRegion,index,1);
			rightColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn2,ThemeManager.getInstance().beeTextureRegion,index,2);

			textLeftHeading = new Text(leftColumnAnimation.getX()+50*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Bees", ResourcesManager.getInstance().vbom);
			textRightHeading = new Text(rightColumnAnimation.getX()+50*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Bees", ResourcesManager.getInstance().vbom);

			textDescColumn1 = new Text(leftColumnAnimation.getX()+leftColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler, backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nGives You Clues \nPack Of "+leftColumnPackCount, ResourcesManager.getInstance().vbom);
			textDescColumn2 = new Text(rightColumnAnimation.getX()+rightColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nGives You Clues \nPack Of "+rightColumnPackCount, ResourcesManager.getInstance().vbom);

			textLeftHeading.setScale(0.70f*ResourcesManager.resourceScaler);
			textRightHeading.setScale(0.70f*ResourcesManager.resourceScaler);

			buttonTextJoin1= buttonTextJoin1 + StorageManager.getInstance().getUser().getNumOfBees();
			buttonTextJoin2= buttonTextJoin2 + StorageManager.getInstance().getUser().getNumOfBees();

			break;
		case 2:
			leftColumnCoinsCount = 7000;
			rightColumnCoinsCount = 10000;
			leftColumnPackCount=10;
			rightColumnPackCount=15;
			s1= " "+leftColumnCoinsCount;
			s2= " "+rightColumnCoinsCount;
			textLeftCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s1, ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s2, ResourcesManager.getInstance().vbom);

			leftColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn1,ThemeManager.getInstance().beeTextureRegion,index,1);
			rightColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn2,ThemeManager.getInstance().beeTextureRegion,index,2);

			textLeftHeading = new Text(leftColumnAnimation.getX()+50*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Bees", ResourcesManager.getInstance().vbom);
			textRightHeading = new Text(rightColumnAnimation.getX()+50*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Bees", ResourcesManager.getInstance().vbom);

			textDescColumn1 = new Text(leftColumnAnimation.getX()+leftColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler, backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nGives You Clues \nPack Of "+leftColumnPackCount, ResourcesManager.getInstance().vbom);
			textDescColumn2 = new Text(rightColumnAnimation.getX()+rightColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nGives You Clues \nPack Of "+rightColumnPackCount, ResourcesManager.getInstance().vbom);

			textLeftHeading.setScale(0.70f*ResourcesManager.resourceScaler);
			textRightHeading.setScale(0.70f*ResourcesManager.resourceScaler);
			buttonTextJoin1= buttonTextJoin1 + StorageManager.getInstance().getUser().getNumOfBees();
			buttonTextJoin2= buttonTextJoin2 + StorageManager.getInstance().getUser().getNumOfBees();
			break;
		case 3:
			//ThemeManager.getInstance().attachHoneyComb(backgroundColumn1);
			leftBannerLock1=true;
			leftBannerLock2=true;
			leftColumnCoinsCount = 15000;
			rightColumnCoinsCount = 20000;
			leftColumnPackCount=5;
			rightColumnPackCount=8;
			s1= " "+leftColumnCoinsCount;
			s2= " "+rightColumnCoinsCount;
			textLeftCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s1, ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s2, ResourcesManager.getInstance().vbom);

			leftColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn1,ThemeManager.getInstance().honeycombSmallTextureRegion,index,1);
			rightColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn2,ThemeManager.getInstance().honeycombSmallTextureRegion,index,2);

			textLeftHeading = new Text(leftColumnAnimation.getX()+25*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Honeycombs (S)", ResourcesManager.getInstance().vbom);
			textRightHeading = new Text(rightColumnAnimation.getX()+25*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Honeycombs (S)", ResourcesManager.getInstance().vbom);

			textDescColumn1 = new Text(leftColumnAnimation.getX()+leftColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler, backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nAttract Bees On Demand\nFor Duration Of 15 Seconds\nPack Of "+leftColumnPackCount, ResourcesManager.getInstance().vbom);
			textDescColumn2 = new Text(rightColumnAnimation.getX()+rightColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nAttract Bees On Demand\nFor Duration Of 15 Seconds\nPack Of "+rightColumnPackCount, ResourcesManager.getInstance().vbom);


			textLeftHeading.setScale(0.47f*ResourcesManager.resourceScaler);
			textRightHeading.setScale(0.47f*ResourcesManager.resourceScaler);
			buttonTextJoin1= buttonTextJoin1 + StorageManager.getInstance().getUser().getNumOfHoneyCombsSmall();
			buttonTextJoin2= buttonTextJoin2 + StorageManager.getInstance().getUser().getNumOfHoneyCombsSmall();
			break;
		case 4:
			leftBannerLock1=true;
			leftBannerLock2=true;
			leftColumnCoinsCount = 25000;
			rightColumnCoinsCount = 30000;
			leftColumnPackCount=5;
			rightColumnPackCount=8;
			s1= " "+leftColumnCoinsCount;
			s2= " "+rightColumnCoinsCount;
			textLeftCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s1, ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s2, ResourcesManager.getInstance().vbom);

			leftColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn1,ThemeManager.getInstance().honeycombTextureRegion,index,1);
			rightColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn2,ThemeManager.getInstance().honeycombTextureRegion,index,2);

			textLeftHeading = new Text(leftColumnAnimation.getX()+25*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Honeycombs (L)", ResourcesManager.getInstance().vbom);
			textRightHeading = new Text(rightColumnAnimation.getX()+25*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Honeycombs (L)", ResourcesManager.getInstance().vbom);

			textDescColumn1 = new Text(leftColumnAnimation.getX()+leftColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nAttract Bees On Demand\nFor Duration Of 30 Seconds\nPack Of "+leftColumnPackCount, ResourcesManager.getInstance().vbom);
			textDescColumn2 = new Text(rightColumnAnimation.getX()+rightColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nAttract Bees On Demand\nFor Duration Of 30 Seconds\nPack Of "+rightColumnPackCount, ResourcesManager.getInstance().vbom);
			textLeftHeading.setScale(0.47f*ResourcesManager.resourceScaler);
			textRightHeading.setScale(0.47f*ResourcesManager.resourceScaler);
			buttonTextJoin1= buttonTextJoin1 + StorageManager.getInstance().getUser().getNumOfHoneyCombs();
			buttonTextJoin2= buttonTextJoin2 + StorageManager.getInstance().getUser().getNumOfHoneyCombs();
			break;
		case 5:
			leftColumnCoinsCount = 30000;
			rightColumnCoinsCount = 40000;
			leftColumnPackCount=5;
			rightColumnPackCount=10;
			s1= " "+leftColumnCoinsCount;
			s2= " "+rightColumnCoinsCount;
			textLeftCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s1, ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s2, ResourcesManager.getInstance().vbom);

			leftColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn1,ThemeManager.getInstance().bullTextureRegion,index,1);
			rightColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn2,ThemeManager.getInstance().bullTextureRegion,index,2);

			textLeftHeading = new Text(leftColumnAnimation.getX()+50*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Bulls", ResourcesManager.getInstance().vbom);
			textRightHeading = new Text(rightColumnAnimation.getX()+50*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Bulls", ResourcesManager.getInstance().vbom);

			textDescColumn1 = new Text(leftColumnAnimation.getX()+leftColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nGives Half the Bulls Out \nUsage: Once Per Game \nPack Of "+leftColumnPackCount, ResourcesManager.getInstance().vbom);
			textDescColumn2 = new Text(rightColumnAnimation.getX()+rightColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nGives Half the Bulls Out \nUsage: Once Per Game  \nPack Of "+rightColumnPackCount, ResourcesManager.getInstance().vbom);

			textLeftHeading.setScale(0.70f*ResourcesManager.resourceScaler);
			textRightHeading.setScale(0.70f*ResourcesManager.resourceScaler);
			buttonTextJoin1= buttonTextJoin1 + StorageManager.getInstance().getUser().getNumOfBullRuns();
			buttonTextJoin2= buttonTextJoin2 + StorageManager.getInstance().getUser().getNumOfBullRuns();
			break;
		case 6:
			leftBannerLock2=true;
			
			leftColumnCoinsCount = 50000;
			rightColumnCoinsCount = 50000;
			leftColumnPackCount=15;
			rightColumnPackCount=10;
			s1= " "+leftColumnCoinsCount;
			s2= " "+rightColumnCoinsCount;
			textLeftCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s1, ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler,0.5f, ResourcesManager.getInstance().mBitmapFontOutline, s2, ResourcesManager.getInstance().vbom);

			leftColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn1,ThemeManager.getInstance().bullTextureRegion,index,1);
			rightColumnAnimation=ThemeManager.getInstance().attachAnimSpritesForShop(backgroundColumn2,ThemeManager.getInstance().redCapeTextureRegion,index,2);
			
			textLeftHeading = new Text(leftColumnAnimation.getX()+50*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Bulls", ResourcesManager.getInstance().vbom);
			textRightHeading = new Text(rightColumnAnimation.getX()+25*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4), ResourcesManager.getInstance().mBitmapFontWooden, "Red Capes", ResourcesManager.getInstance().vbom);

			textDescColumn1 = new Text(leftColumnAnimation.getX()+leftColumnAnimation.getWidthScaled()+20*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nGives Half the Bulls Out \nUsage: Once Per Game \nPack Of "+leftColumnPackCount, ResourcesManager.getInstance().vbom);
			textDescColumn2 = new Text(rightColumnAnimation.getX()+rightColumnAnimation.getWidthScaled()+2*ResourcesManager.resourceScaler,backgroundColumn1.getHeight()/2, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "\nAttract Bulls On Demand   \nUsage: Once Per Game\nPack Of "+rightColumnPackCount, ResourcesManager.getInstance().vbom);

			textLeftHeading.setScale(0.70f*ResourcesManager.resourceScaler);
			textRightHeading.setScale(0.65f*ResourcesManager.resourceScaler);
			buttonTextJoin1= buttonTextJoin1 + StorageManager.getInstance().getUser().getNumOfBullRuns();
			buttonTextJoin2= buttonTextJoin2 + StorageManager.getInstance().getUser().getNumOfRedCape();
			break;
			/*case 7:
			textLeftCoin =new Text(getWidthScaled()/2, getHeightScaled()/2, ResourcesManager.getInstance().mBitmapFontOutline, "X 2000", ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(getWidthScaled()/2, getHeightScaled()/2, ResourcesManager.getInstance().mBitmapFontOutline, "X 250", ResourcesManager.getInstance().vbom);
			break;
		case 8:
			textLeftCoin =new Text(getWidthScaled()/2, getHeightScaled()/2, ResourcesManager.getInstance().mBitmapFontOutline, "X 2000", ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(getWidthScaled()/2, getHeightScaled()/2, ResourcesManager.getInstance().mBitmapFontOutline, "X 250", ResourcesManager.getInstance().vbom);
			break;*/
		default:
			leftColumnCoinsCount = 100000;
			rightColumnCoinsCount = 100000;
			s1= " "+leftColumnCoinsCount;
			s2= " "+rightColumnCoinsCount;
			textLeftCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+1*ResourcesManager.resourceScaler, buyButtonColumn1.getHeightScaled()/2, ResourcesManager.getInstance().mBitmapFontOutline, s1, ResourcesManager.getInstance().vbom);
			textRightCoin =new Text(buyCoinColumn1.getX()+buyCoinColumn1.getWidthScaled()+1*ResourcesManager.resourceScaler, buyButtonColumn2.getHeightScaled()/2, ResourcesManager.getInstance().mBitmapFontOutline, s2, ResourcesManager.getInstance().vbom);
			break;

		}
		multiplierText1.setPosition(leftColumnAnimation.getX()+212*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4)+10*ResourcesManager.resourceScaler);
		multiplierText2.setPosition(rightColumnAnimation.getX()+212*ResourcesManager.resourceScaler,(backgroundColumn1.getHeight()*3f/4)+10*ResourcesManager.resourceScaler);
		multiplierText1.setText(buttonTextJoin1);
		multiplierText2.setText(buttonTextJoin2);

		textLeftCoin.setScale(0.85f*ResourcesManager.resourceScaler);
		textRightCoin.setScale(0.85f*ResourcesManager.resourceScaler);

		textLeftCoin.setAnchorCenter(0f,0.5f);
		textRightCoin.setAnchorCenter(0f,0.5f);


		textLeftCoin.setColor(0.95f,0.6f,0.1f);
		textRightCoin.setColor(0.95f,0.6f,0.1f);


		TextOptions options = new TextOptions();
		options.setHorizontalAlign(HorizontalAlign.CENTER);
		options.setAutoWrap(AutoWrap.WORDS);


		textDescColumn1.setScale(0.30f*ResourcesManager.resourceScaler);
		textDescColumn2.setScale(0.30f*ResourcesManager.resourceScaler);
		textDescColumn1.setAnchorCenter(0f,0.5f);
		textDescColumn2.setAnchorCenter(0f,0.5f);
		textDescColumn1.setTextOptions(options);
		textDescColumn2.setTextOptions(options);

		textLeftHeading.setAnchorCenter(0f,0f);
		textRightHeading.setAnchorCenter(0f,0f);
		textLeftHeading.setColor(0.2f,0.1f,0.068f);
		textRightHeading.setColor(0.2f,0.1f,0.068f);
		if(THEMES.BLUEMAGIC.toString().equals(ThemeManager.getInstance().getThemePreferences().getThemeSelection())){
			textDescColumn1.setColor(.1f,.1f,.1f);
			textDescColumn2.setColor(.1f,.1f,.1f);

		}
		else{
			textDescColumn1.setColor(.3f,.3f,.3f);
			textDescColumn2.setColor(.3f,.3f,.3f);
		}


		Rectangle rect1 = new Rectangle(buyButtonColumn1.getWidth()/2, buyButtonColumn1.getHeight()/2, buyCoinColumn1.getWidthScaled()+4*ResourcesManager.resourceScaler+textLeftCoin.getWidthScaled(), 1, pVertexBufferObjectManager);
		Rectangle rect2 = new Rectangle(buyButtonColumn2.getWidth()/2, buyButtonColumn2.getHeight()/2, buyCoinColumn2.getWidthScaled()+4*ResourcesManager.resourceScaler+textRightCoin.getWidthScaled(), 1, pVertexBufferObjectManager);
		rect1.setAnchorCenter(0.5f, 0.5f);
		rect2.setAnchorCenter(0.5f, 0.5f);
		rect1.setAlpha(0f);
		rect1.attachChild(buyCoinColumn1);
		rect1.attachChild(textLeftCoin);
		rect2.setAlpha(0);
		rect2.attachChild(buyCoinColumn2);
		rect2.attachChild(textRightCoin);
		buyButtonColumn1.attachChild(rect1);
		buyButtonColumn2.attachChild(rect2);
		/*buyButtonColumn1.attachChild(textLeftCoin);
		buyButtonColumn2.attachChild(textRightCoin);*/
		backgroundColumn1.attachChild(buyButtonColumn1);
		backgroundColumn2.attachChild(buyButtonColumn2);
		backgroundColumn1.attachChild(textLeftHeading);
		backgroundColumn2.attachChild(textRightHeading);
		backgroundColumn1.attachChild(textDescColumn1);
		backgroundColumn2.attachChild(textDescColumn2);

		multiplierBanner1=new Sprite(ResourcesManager.getInstance().windowDimensions.x/2-50*ResourcesManager.resourceScaler, this.getHeight()/2 , ResourcesManager.getInstance().shopMultiplierBackgroundRegion, pVertexBufferObjectManager);
		multiplierBanner1.setAnchorCenterX(1f);
		//multiplierBanner1.setColor(0.8f,0.8f,0.8f);
		multiplierBanner1.setAlpha(1f);
		multiplierBanner1.setWidth(this.getWidth()/2.5f);
		multiplierBanner1.setHeight(this.getHeight());

		multiplierBanner2=new Sprite((ResourcesManager.getInstance().windowDimensions.x/2)+50*ResourcesManager.resourceScaler, this.getHeight()/2 , ResourcesManager.getInstance().shopMultiplierBackgroundRegion, pVertexBufferObjectManager);
		multiplierBanner2.setAnchorCenterX(0f);
		//multiplierBanner2.setColor(0.8f,0.8f,0.8f);
		multiplierBanner2.setAlpha(1f);
		multiplierBanner2.setWidth(this.getWidth()/2.5f);
		multiplierBanner2.setHeight(this.getHeight());
		//multiplierBanner2.setScale(ResourcesManager.resourceScaler);

		backgroundColumn1.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		multiplierBanner1.attachChild(multiplierText1);
		multiplierBanner2.attachChild(multiplierText2);

		if(leftBannerLock1){
			backgroundColumn1.setAlpha(0.4f);
			multiplierBanner1.setAlpha(0.2f);
			backgroundColumn1.setColor(0.2f,0f,1f);
			
			Sprite lock=new Sprite(50*ResourcesManager.resourceScaler, 50*ResourcesManager.resourceScaler, ResourcesManager.getInstance().shopLockRegion, pVertexBufferObjectManager);
			backgroundColumn1.attachChild(lock);
		}
		if(leftBannerLock2){
			backgroundColumn2.setAlpha(0.4f);
			multiplierBanner2.setAlpha(0.2f);
			backgroundColumn2.setColor(0.2f,0f,1f);
			Sprite lock=new Sprite(50*ResourcesManager.resourceScaler,50*ResourcesManager.resourceScaler, ResourcesManager.getInstance().shopLockRegion, pVertexBufferObjectManager);

			backgroundColumn2.attachChild(lock);


		}
		attachChild(backgroundColumn1);
		attachChild(backgroundColumn2);
		attachChild(multiplierBanner1);
		attachChild(multiplierBanner2);
	}



	/**
	 * 
	 */
	protected void coinTouchAction(final byte leftRight) {

		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				int coinCount = leftColumnCoinsCount;
				int packCount = leftColumnPackCount;
				if(leftRight==1){
					coinCount = rightColumnCoinsCount;
					packCount = rightColumnPackCount;
				}


				//++TimerManager.getInstance().beesShoppedCount;
				if((StorageManager.getInstance().getUser().getNumOfCoins() - coinCount) < 0){
					ToastHelper.makeCustomToastForCoin("You Do Not Have Enough Coins", Toast.LENGTH_SHORT);
				}
				else if((0==leftRight && !leftBannerLock1) || (1==leftRight && !leftBannerLock2)){
					SoundManager.getInstance().createAddCoinsForShopSound();
					switch(index){
					case 1:
						StorageManager.getInstance().getUser().setNumOfBees(StorageManager.getInstance().getUser().getNumOfBees()+packCount);
						SoundManager.getInstance().createBeeWooshSound();
						ResourcesManager.getInstance().numberOfBeeFliesText.setText(" X "+StorageManager.getInstance().getUser().getNumOfBees());
						break;
					case 2:
						StorageManager.getInstance().getUser().setNumOfBees(StorageManager.getInstance().getUser().getNumOfBees()+packCount);
						SoundManager.getInstance().createBeeWooshSound();
						ResourcesManager.getInstance().numberOfBeeFliesText.setText(" X "+StorageManager.getInstance().getUser().getNumOfBees());
						break;
					case 3:
						StorageManager.getInstance().getUser().setNumOfHoneyCombsSmall(StorageManager.getInstance().getUser().getNumOfHoneyCombsSmall()+packCount);
						SoundManager.getInstance().createBeeWooshSound();
						ResourcesManager.getInstance().numberOfHoneyCombsSmallText.setText(" X "+StorageManager.getInstance().getUser().getNumOfHoneyCombsSmall());

						break;
					case 4:
						StorageManager.getInstance().getUser().setNumOfHoneyCombs(StorageManager.getInstance().getUser().getNumOfHoneyCombs()+packCount);
						SoundManager.getInstance().createBeeWooshSound();
						ResourcesManager.getInstance().numberOfHoneyCombsText.setText(" X "+StorageManager.getInstance().getUser().getNumOfHoneyCombs());
						break;
					case 5:
						StorageManager.getInstance().getUser().setNumOfBullRuns(StorageManager.getInstance().getUser().getNumOfBullRuns()+packCount);
						SoundManager.getInstance().createBullRunSound();
						ResourcesManager.getInstance().numberOfBullRunsText.setText(" X "+StorageManager.getInstance().getUser().getNumOfBullRuns());

						break;
					case 6:

						if(0==leftRight){
							StorageManager.getInstance().getUser().setNumOfBullRuns(StorageManager.getInstance().getUser().getNumOfBullRuns()+packCount);
							ResourcesManager.getInstance().numberOfBullRunsText.setText(" X "+StorageManager.getInstance().getUser().getNumOfBullRuns());
							SoundManager.getInstance().createBullRunSound();
						}
						else{
							StorageManager.getInstance().getUser().setNumOfRedCape(StorageManager.getInstance().getUser().getNumOfRedCape()+packCount);
							ResourcesManager.getInstance().numberOfRedCapesText.setText(" X "+StorageManager.getInstance().getUser().getNumOfRedCape());
							SoundManager.getInstance().createBullRunSound();
						}
						
						break;
					default:
						break;

					}
					//ResourcesManager.numberOfCoinsTotal= StorageManager.getInstance().getUser().getNumOfCoins() - 50;
					StorageManager.getInstance().getUser().setNumOfCoins(StorageManager.getInstance().getUser().getNumOfCoins() - coinCount);
					ResourcesManager.getInstance().numberOfCoinsText.setText(" X "+StorageManager.getInstance().getUser().getNumOfCoins());
					StorageManager.getInstance().saveUserBeesBullsAndCoins(StorageManager.getInstance().getUser());
					SoundManager.getInstance().createAddCoinsForShopSound();
				}
				else{
					ToastHelper.makeCustomToast("This Item is Currently Locked. \n Will Be Made Availabe In Pro Version", Toast.LENGTH_SHORT);
				}
			}
		});
	}



	public boolean actionClicked;
	private CharSequence attachedBannerDisabledString;

	/* (non-Javadoc)
	 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if(buyButtonColumn1.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY()) || buyButtonColumn2.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())){
			if(pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()){
				/*	if(buyButtonColumn1.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())){
					//buyButtonColumn1.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
				else{
					//buyButtonColumn2.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}*/
				actionClicked=true;
				return true;
			}
			else if(actionClicked){
				actionClicked= false;
				if(buyButtonColumn1.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())){
					buyButtonColumn1.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
				else{
					buyButtonColumn2.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
				return true;
			}
		}
		else{
			actionClicked = false;
			//backgroundColumn1.setAlpha(0.8f);
			//backgroundColumn2.setAlpha(0.8f);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.Entity#onManagedUpdate(float)
	 */
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		//Disable accidental click of join while scrolling
		if(ResourcesManager.bannersScrolled){
			actionClicked=false;
			//ThemeManager.getInstance().resetRoomTheme(this, index);
		}

		super.onManagedUpdate(pSecondsElapsed);
	}
	/**
	 * @param BannerProperties 
	 * 
	 */
	public void attachBannerHostedText(BannerProperties BannerProperties) {
		Text textBannerTaken=new Text(getWidthScaled()/2, getHeightScaled()/2, ResourcesManager.getInstance().font, attachedBannerDisabledString, ResourcesManager.getInstance().vbom){
			protected void onManagedUpdate(float pSecondsElapsed) {
				this.setText(attachedBannerDisabledString);
				this.setX(ResourcesManager.getInstance().windowDimensions.x/2-80*ResourcesManager.resourceScaler);
			}
		};
		textBannerTaken.setAnchorCenter(0f, 0.5f);
		textBannerTaken.setScale(0.3f * ResourcesManager.resourceScaler);
		textBannerTaken.setColor(1f,0.2f,0.2f,0.9f);
		//textBannerTaken.setColor(colorTextInBanner);
		this.attachChild(textBannerTaken);
	}


}
