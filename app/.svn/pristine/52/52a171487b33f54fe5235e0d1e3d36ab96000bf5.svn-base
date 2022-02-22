/**
 * 
 */
package com.efficientsciences.cowsandbulls.wordwars.managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.particle.BatchedPseudoSpriteParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ExpireParticleInitializer;
import org.andengine.entity.particle.initializer.RotationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ColorParticleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.text.TickerText;
import org.andengine.entity.text.TickerText.TickerTextOptions;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackerTextureRegion;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.font.BitmapFont;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.AssetBitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;
import org.andengine.util.debug.Debug;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.ui.Window;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.MainActivity;
import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.chat.Message;
import com.efficientsciences.cowsandbulls.wordwars.chat.WidgetsWindow;
import com.efficientsciences.cowsandbulls.wordwars.domain.FetchAdsImageFromServerTask;
import com.efficientsciences.cowsandbulls.wordwars.domain.KeyboardChar;
import com.efficientsciences.cowsandbulls.wordwars.domain.KeyboardString;
import com.efficientsciences.cowsandbulls.wordwars.domain.Level;
import com.efficientsciences.cowsandbulls.wordwars.domain.PageChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.Pages;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomProperties;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomStream;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunk;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserChunkFactory;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.graphics.ParticleSwipeCreator;
import com.efficientsciences.cowsandbulls.wordwars.graphics.RegisterXSwingEntityModifierInitializer;
import com.efficientsciences.cowsandbulls.wordwars.graphics.RegisterXSwingEntityModifierInitializerSnow;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager.MusicPlayed;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;
import com.efficientsciences.cowsandbulls.wordwars.scenes.RealGameInterface;
import com.efficientsciences.cowsandbulls.wordwars.scenes.SlideUsersChildScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.WaitingScene;
import com.efficientsciences.cowsandbulls.wordwars.services.IMService;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnector;
import com.efficientsciences.cowsandbulls.wordwars.textureMap.Chat;

/**
 * @author SATHISH
 *
 */
public class ResourcesManager {

	private ThemeManager themeManager;
	public static LayoutInflater inflater;
	public static Toast toast;
	public static  View layout ;
	public static  TextView textShown;
	final static int numOfAds = 6;
	private ResourcesManager(){
		themeManager = ThemeManager.getInstance();
	}
	private static final ResourcesManager INSTANCE = new ResourcesManager();

	public static ResourcesManager getInstance() {
		return INSTANCE;
	}


	//For Easy And Tutorial mode
	public static boolean isTutorialMode;

	private Map<Integer,Boolean> revealedBits;


	public Map<Integer, Boolean> getRevealedBits() {
		return revealedBits;
	}

	@SuppressLint("UseSparseArrays")
	public void setRevealedBits(Map<Integer, Boolean> revealedBits) {
		if(null!=revealedBits){
			this.revealedBits = new HashMap<Integer, Boolean>(revealedBits);
		}
		else{
			this.revealedBits = new HashMap<Integer, Boolean>();
		}
	}

	private Engine engine;
	public PhysicsWorld mPhysWorld;
	public MainActivity activity;
	public Camera camera;
	public VertexBufferObjectManager vbom;
	public Point windowDimensions;

	//PageChunk text Dimensions based on text
	public float pageChunkWidth;
	public float pageChunkHeight;

	//Scaling
	public static float resourceScaler=1;

	//Splash Text 
	public Text splash_region;

	//Fonts
	public Font font;

	/*public static Font mLargeFont;
	 */
	public static Font mSmallFont;

	//Bitmap Fonts
	public BitmapFont mBitmapFont;
	public BitmapFont mBitmapFontOption; //previously New
	public BitmapFont mBitmapFontForGame; //previously pencil
	public BitmapFont mBitmapFontNight;
	public BitmapFont mBitmapHowToPlayLarge;
	public BitmapFont mBitmapHowToPlaySmall;
	public BitmapFont mBitmapBabyKrufy;
	public BitmapFont mBitmapFontRound;


	//Shop
	public BitmapFont mBitmapFontWooden; //For Shop
	public BitmapFont mBitmapFontOutline; //For Shop
	//Bitmap texture atlas, should give explicit x and why on where to place the regions in atlas
	//How To Play
	public static BitmapTextureAtlas mLargeFontTexture;
	public static BitmapTextureAtlas mSmallFontTexture;
	public static BitmapTextureAtlas mMenuTexture;

	//Buildable Texture Atlas to build bitmap texture regions
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	private BuildableBitmapTextureAtlas menuExtraButtonsTextureAtlas;
	private BuildableBitmapTextureAtlas gameHostTextureAtlas;
	private BuildableBitmapTextureAtlas gameGuessTextureAtlas;
	private BuildableBitmapTextureAtlas notificationTextureAtlas;
	private BuildableBitmapTextureAtlas hostGuessGameBackgroundTextureAtlas;
	private BuildableBitmapTextureAtlas shareTextureAtlas;

	private BuildableBitmapTextureAtlas hostHighlightTextureAtlas;
	private BuildableBitmapTextureAtlas guessHighlightTextureAtlas;
	private BuildableBitmapTextureAtlas hostTransparentTextureAtlas;
	private BuildableBitmapTextureAtlas guessTransparentTextureAtlas;
	private BuildableBitmapTextureAtlas gradientTextureAtlas;

	private BuildableBitmapTextureAtlas hostRoomsBackgroundTextureAtlas;
	private BuildableBitmapTextureAtlas guessRoomsBackgroundTextureAtlas;


	//Texture and texture Regions

	public static TextureRegion mMenuCreditsTextureRegion;
	public static TextureRegion mMenuBackTextureRegion;

	public static ITextureRegion chocobackbuttonregion;
	public static ITextureRegion help_region;
	public static ITextureRegion howToPlay_region;

	//How To Play
	//Gradient overlay for fading effect
	public ITextureRegion gradientOverlay;

	//Game Screen Backgrounds
	public ITextureRegion menu_background_region;
	public ITextureRegion game_host_background_region;
	public ITextureRegion game_guess_background_region;
	//public ITextureRegion play_region;
	public ITextureRegion options_region;

	public ITextureRegion whatsapp_region;
	public ITextureRegion gmail_region;
	public ITextureRegion fb_region;

	public ITextureRegion fbLogin_region;

	public ITextureRegion fbPost_region;

	public ITextureRegion shareIcon_region;

	private ITexture mGreyHeartTexture;
	public ITextureRegion greyHeart_region;

	private ITexture mGreenGrassTexture;
	public ITextureRegion greenGrass_region;

	private ITexture mGreenHeartTexture;
	public ITextureRegion greenHeart_region;

	private ITexture mBlueHeartTexture;
	public ITextureRegion blueHeart_region;

	private ITexture mYellowHoneyTexture;
	public ITextureRegion yellowHoney_region;

	private ITexture mBlueHoneyTexture;
	public ITextureRegion blueHoney_region;

	private ITexture mFBLoginTexture;
	public ITextureRegion fbloginBG_region;

	public ITextureRegion blueMenu_region;
	public ITextureRegion blueTryAgain_region;
	//public ITextureRegion greenWorld_region;

	public ITextureRegion redWaitingSceneUserChunk_region;

	//Rooms Scene background
	/*	public ITextureRegion host_room_background_region;
	public ITextureRegion guess_room_background_region;*/

	//HostGuess Game Scene

	public ITextureRegion host_guess_background_region;
	//Click Highlight images
	public ITextureRegion hostHighlightRegion;
	public ITextureRegion guessHighlightRegion;
	public ITextureRegion hostTransparentRegion;
	public ITextureRegion guessTransparentRegion;

	//Snow Textures
	private ITexture mSnowParticle;
	public ITextureRegion mSnowParticleRegion;
	public BatchedPseudoSpriteParticleSystem particleSystem;

	public ITexture touchSprite;
	public ITextureRegion touchSpriteRegion;

	//Snow Textures
	private ITexture mSnowParticleb;
	private ITextureRegion mSnowParticleRegionb;
	public BatchedPseudoSpriteParticleSystem particleSystemb;

	//Snow Textures
	public ITexture mSnowParticleh;
	public ITextureRegion mSnowParticleRegionh;
	public BatchedPseudoSpriteParticleSystem particleSystemh;
	//Snow Textures
	private ITexture mSnowParticlep;
	private ITextureRegion mSnowParticleRegionp;
	public BatchedPseudoSpriteParticleSystem particleSystemp;
	//Snow Textures
	private ITexture mSnowParticlei;
	private ITextureRegion mSnowParticleRegioni;
	public BatchedPseudoSpriteParticleSystem particleSystemi;
	//TexturePacker

	private ITexture mSnowParticleStars;
	private ITextureRegion mSnowParticleRegionStars;
	public BatchedPseudoSpriteParticleSystem particleSystemStars;

	private ITexture mSnowParticleHearts;
	private ITextureRegion mSnowParticleRegionHearts;
	public BatchedPseudoSpriteParticleSystem particleSystemHearts;
	//TexturePacker
	//public ITextureRegion swipeBlueStripRegion;
	public ITextureRegion rotatorRegion;
	public static ITextureRegion playersDrawerRegion;

	private TexturePackTextureRegionLibrary texturePackLibrary;
	private TexturePack texturePack;


	private TexturePackTextureRegionLibrary texturePackForLightsLibrary;
	private TexturePack texturePackForLights;

	private TexturePackTextureRegionLibrary texturePackForBeeLibrary;
	private TexturePack texturePackForBee;

	private TexturePackTextureRegionLibrary texturePackForHoneycombLibrary;
	private TexturePack texturePackForHoneycomb;

	private TexturePackTextureRegionLibrary texturePackForDailyChallengeLibrary;
	private TexturePack texturePackForDailyChallenge;


	private TexturePackTextureRegionLibrary texturePackForCoinLibrary;
	private TexturePack texturePackForCoin;

	private TexturePackTextureRegionLibrary texturePackForBullrunLibrary;
	private TexturePack texturePackForBullRun;

	private TexturePackTextureRegionLibrary texturePackForChatLibrary;
	private TexturePack texturePackForChat;


	private TexturePackTextureRegionLibrary texturePackForAskHelpLibrary;
	private TexturePack texturePackForAskHelp;

	private TexturePackTextureRegionLibrary texturePackForHoneycombSmallLibrary;
	private TexturePack texturePackForHoneycombSmall;

	private TexturePackTextureRegionLibrary texturePackForRedCapeLibrary;
	private TexturePack texturePackForRedCape;

	private TexturePackTextureRegionLibrary texturePackForFlagsLibrary;
	private TexturePack texturePackForFlags;

	public TexturePackerTextureRegion[] flagsTextureAnimRegions;
	/*	private ITextureRegion textureRegion_0;
	private ITextureRegion textureRegion_1;
	private ITextureRegion textureRegion_2;
	private ITextureRegion textureRegion_3;
	private ITextureRegion textureRegion_4;
	private ITextureRegion textureRegion_5;*/
	/*	private ITextureRegion textureRegion_7;
	private ITextureRegion textureRegion_8;
	private ITextureRegion textureRegion_9;*/

	public ITiledTextureRegion playRegionPack;

	//Music icon
	public ITiledTextureRegion musicOnOff;

	public ITiledTextureRegion tutorialOnOff;


	public KeyboardString keyboardString=new KeyboardString();

	public float xTextStart=0;
	public float yTextStart=0;
	public float yKeyBoardStartOffsetReference =210;
	public float letterWidth = 0;
	public float letterheight= 0;
	public int maxNoOfLetter = 20;
	public List<Integer> addedPositions=new ArrayList<Integer>();
	public List<Integer> removedPositions=new ArrayList<Integer>();

	public int currentPageIndex;

	public KeyboardString host=new KeyboardString();
	//public KeyboardString client=new KeyboardString();

	public String payloadFromServer = "Welcome" ;
	public Text welcome;

	public String waitTimeString = "" ;
	public Text waitingText;
	public Text numberOfLettersHostedText;
	public String waitTextString = "";

	public Pages pages=new Pages();
	public int maxPageChunksInPage = 9;
	public int hostedWordLength;
	public PageChunk referenceLatestPage;
	//TexturePacker


	//For Host
	public Text[] keyBoardBitmapText=new Text[26];
	//	private Font mFont;
	public Text mText;
	public Line mRight;
	public Line mLeft;

	public boolean showToast=true;
	//For Host
	public boolean showToast2=true;
	public boolean hosted=false;

	//Animations On click of host
	public IEntityModifierListener moveAndHideLineListener;
	public IEntityModifier moveAndHideLine;
	public IEntityModifierListener moveAndHideRectangleListener;
	public IEntityModifier moveAndHideRectangle;

	public IEntityModifierListener hostGuessKeyCharAlphaModifierListener;
	public IEntityModifier hostGuessKeyCharAlphaModifier;

	public IEntityModifierListener hostGuessKeyCharAlphaModifierNormalListener;
	public IEntityModifier hostGuessKeyCharAlphaNormalModifier;

	public IEntityModifierListener notificationListener;
	public IEntityModifier notificationModifier;

	public Line line;
	public Rectangle keyBoard;

	//MiniGameSceneBull button
	public boolean hostImageclicked;
	public boolean guessImageclicked;

	//Host Scene Button
	public boolean hostButtonclicked;
	//Guess Scene Button 
	public boolean guessButtonclicked;

	public String hostText = "Host";
	public String guessText = "Guess";
	/*
	 * Refer to 
	 *  public AutoBahnConnectorPubSub autobahnConnectorPubSub in ConnectionManager
	 */
	@Deprecated
	public AutoBahnConnector autobahnConnector;

	public int maxNoOfPageChunkColumnsPerPage;
	private BuildableBitmapTextureAtlas animationTextureAtlas;
	public static float scalingFactorX;
	public static float scalingFactorY;

	public int numberOfLettersHosted;
	public int minimumNumberOfLetters;
	public String wordHosted;
	public boolean showing;
	public static int vungleAdShownCount;

	public float userChunkWidth;
	public float userChunkHeight;
	public volatile List<UserChunk> users;
	public List<UserChunk> usersForHostAndGuessScene;
	public int maxUserChunksInRoom;
	public boolean gameWon;
	public String gameWonBy;
	public ITextureRegion leave_region;
	public ITextureRegion stay_region;
	public List<FetchAdsImageFromServerTask> tasks=new ArrayList<FetchAdsImageFromServerTask>();
	public int maxNumberOfUsersPossible = 18;
	public int maxNumberOfUsersPerPage = 5;

	//this will be set only when someone wins enabling HardBackbutton to goto MiniGameSceneBull
	public boolean deviceBackButtonToGameScene;
	public Text roomNumber;

	public TiledSprite chat;
	public TiledSprite askHelp;
	public TiledSprite qrcode;
	public Sprite fbButton;
	public Rectangle hudRect;
	public AnimatedSprite beeHudAnim;
	public AnimatedSprite honeyCombHudAnim;
	public AnimatedSprite honeyCombSmallHudAnim;
	public AnimatedSprite bullRunHudAnim;
	public AnimatedSprite redCapeHudAnim;

	public Text numberOfBeeFliesText;
	public Text numberOfBullRunsText;
	public Text numberOfHoneyCombsText;
	public Text numberOfHoneyCombsSmallText;
	public Text numberOfRedCapesText;

	public Level level;
	public Text wordIqText;
	public Text numberOfCoinsText;
	public AnimatedSprite hudCoin;


	public static byte numberOfAdsSkipped;
	public static byte numberOfVungleAdsSkipped;	

	//	public static int numberOfGamesPlayed;
	//	public static int numberOfGamesWon;
	//	public static long numberOfCoinsTotal;
	public static int numberOfCoinsPerGame;

	public ITextureRegion moneybagRegion;
	public ITextureRegion achievementLevelRegion;	
	public ITextureRegion leaderBoardRegion;
	public ITextureRegion earnCoinsRegion;
	public ITextureRegion shopBeesGreenRegion;
	public ITextureRegion shopBeesBlueRegion;
	public ITextureRegion shopWordCoinRegion;
	public ITextureRegion shopMultiplierBackgroundRegion;
	public ITextureRegion shopPaintCurtainRegion;
	//private ITexture shopBackgroundTexture;
	//public ITextureRegion shopBackgroundRegion;
	public ITextureRegion howToPlayIllustrateGreenRegion;
	public ITextureRegion shopBuyButtonRegion;
	public int maxBanners;
	public int maxBannersPerPage;
	private BuildableBitmapTextureAtlas shopTextureAtlas;
	public boolean isGamePaused;
	public TiledTextureRegion dailyChallengeRegion;
	public TiledTextureRegion lightsRegion;
	public ITextureRegion lights_BG_Region;

	public ITiledTextureRegion chat_Region; 
	public ITiledTextureRegion askHelp_Region; 
	public ITextureRegion qrcode_Region; 

	public ITextureRegion fbFriend_region;
	public ITextureRegion shopLockRegion;
	public String changedText = " ";
	private BuildableBitmapTextureAtlas notifyTextureAtlas;
	public TextureRegion coolers_region;
	public TextureRegion drama_region;
	public TextureRegion confetti_region;
	public TextureRegion sheryl_region;
	public TextureRegion inveye_region;
	public TextureRegion hostAgainButton;
	public String roomHostedBy;
	public TextureRegion hostContinue_region;
	public TextureRegion guessAgain_region;
	private ITexture mTutorialGradientTexture;
	private ITexture mTutorialFinishTexture;
	private ITexture mTutorialStep1Texture;
	private ITexture mTutorialStep2Texture;
	private ITexture mTutorialStep3Texture;
	private ITexture mTutorialStep4Texture;
	public TextureRegion tutorialname_region;
	public TextureRegion tutorialnext_region;
	public TextureRegion tutorialprev_region;
	public TextureRegion tutorialStep4_region;
	public TextureRegion tutorialStep3_region;
	public TextureRegion tutorialStep2_region;
	public TextureRegion tutorialStep1_region;
	public TextureRegion tutorialFinish_region;
	public TextureRegion tutorialGradient_region;

	public Rectangle overlayRect;
	public Text helpTextRow1;
	public Text helpTextRow2;
	public Text helpTextRow3;
	public Text helpTextRow4;
	public Text helpTextRow5;
	public Sprite sherlockSprite;
	public Text helpTextRow6;
	public boolean wordsListChanged= true;
	public Sprite qrcodeSprite;

	public static int numberOfPlaysToSkipAd;
	public static boolean isAdsEnabled = true;
	public static boolean isAdsEnabledRefBased = true;
	public static byte googleFbOrGuest; //0, 1 and 2 respectively
	//Offsets
	//create Offset For First character Over The Line
	public static float lineMovedTo = 50;
	public static float offset=80;
	public static float roomJoinButtonOffset=90;

	public static final int maxRooms = 100;
	public static final int WHATSAPPRESPONSECODE = 12;
	public static int maxRoomsPerPage = 6;
	public static float roomHeight=70;
	//Local Rooms
	public static SparseArray<RoomProperties> rooms=new SparseArray<RoomProperties>();
	//Rooms From Server
	public static HashMap<Integer, RoomStream> roomsState=new HashMap<Integer, RoomStream>();
	public static boolean roomsScrolled;

	public static HashMap<String, UserDO> userProfiles=new HashMap<String, UserDO>();
	public static HashMap<String, TextureRegion> userProfilePicSprites=new HashMap<String, TextureRegion>();

	public static float density;
	public static boolean vungleAdShown = true;
	public static boolean isResponseGot =  true;
	public static boolean autoRetryInProgress=false;
	public static float pageChunkInitialXOffset = 100;
	public static boolean usersScrolled;
	public static int foundAttempts = 0;
	public static boolean bannersScrolled;
	public static float bannerHeight;
	public static Uri uriData;
	public static boolean isFaceBookRequestUnAddressed;
	public static boolean roomHostedBroadCastSubscriptionSent;
	public static long lastLogin;
	public static boolean isChatEnabled = true;
	public static boolean isValentine;
	//public static boolean displayTutorial = true;
	public static boolean fileShared;
	public static byte fbHelpShared;



	public void loadMenuResources()
	{
		loadMenuGraphics();
		loadMenuAudio();
	}

	public void loadGameResources()
	{
		loadGameGraphics();
		loadGameFonts();
		loadGameAudio();
	}

	private void loadMenuGraphics()
	{
		try 
		{
			texturePackForHoneycomb = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "honedripping.xml");
			texturePackForHoneycomb.loadTexture();
			texturePackForHoneycombLibrary = texturePackForHoneycomb.getTexturePackTextureRegionLibrary();

			texturePackForHoneycombSmall = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "honeycomb.xml");
			texturePackForHoneycombSmall.loadTexture();
			texturePackForHoneycombSmallLibrary = texturePackForHoneycombSmall.getTexturePackTextureRegionLibrary();

			texturePackForRedCape = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "bullwithredcape.xml");
			texturePackForRedCape.loadTexture();
			texturePackForRedCapeLibrary = texturePackForRedCape.getTexturePackTextureRegionLibrary();

			texturePackForFlags = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "flags.xml");
			texturePackForFlags.loadTexture();
			texturePackForFlagsLibrary = texturePackForFlags.getTexturePackTextureRegionLibrary();

			texturePackForDailyChallenge = new TexturePackLoader(activity.getTextureManager(), "gfx/menu/").loadFromAsset(activity.getAssets(), "dailychallenge.xml");
			texturePackForDailyChallenge.loadTexture();
			texturePackForDailyChallengeLibrary = texturePackForDailyChallenge.getTexturePackTextureRegionLibrary();

			texturePackForLights = new TexturePackLoader(activity.getTextureManager(), "gfx/menu/").loadFromAsset(activity.getAssets(), "lights.xml");
			texturePackForLights.loadTexture();
			texturePackForLightsLibrary = texturePackForLights.getTexturePackTextureRegionLibrary();

			texturePackForCoin = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "coin.xml");
			texturePackForCoin.loadTexture();
			texturePackForCoinLibrary = texturePackForCoin.getTexturePackTextureRegionLibrary();
		} 
		catch (final TexturePackParseException e) 
		{
			Debug.e(e);
		}

		//HoneyComb
		TexturePackerTextureRegion[] honeycombTextureAnimRegions = new TexturePackerTextureRegion[this.texturePackForHoneycombLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForHoneycombLibrary.getIDMapping().size(); i++) {
			honeycombTextureAnimRegions[i] = this.texturePackForHoneycombLibrary.get(i);
		}

		TiledTextureRegion honeycombTextureAnim = new TiledTextureRegion(texturePackForHoneycomb.getTexture(),
				honeycombTextureAnimRegions);

		themeManager.honeycombTextureRegion=honeycombTextureAnim;
		texturePackForHoneycomb.getTexture().load();

		//HoneyComb Small
		TexturePackerTextureRegion[] honeycombSmallTextureAnimRegions = new TexturePackerTextureRegion[this.texturePackForHoneycombSmallLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForHoneycombSmallLibrary.getIDMapping().size(); i++) {
			honeycombSmallTextureAnimRegions[i] = this.texturePackForHoneycombSmallLibrary.get(i);
		}

		TiledTextureRegion honeycombSmallTextureAnim = new TiledTextureRegion(texturePackForHoneycombSmall.getTexture(),
				honeycombSmallTextureAnimRegions);

		themeManager.honeycombSmallTextureRegion=honeycombSmallTextureAnim;
		texturePackForHoneycombSmall.getTexture().load();

		//Red Cape
		TexturePackerTextureRegion[] redCapeTextureAnimRegions = new TexturePackerTextureRegion[this.texturePackForRedCapeLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForRedCapeLibrary.getIDMapping().size(); i++) {
			redCapeTextureAnimRegions[i] = this.texturePackForRedCapeLibrary.get(i);
		}

		TiledTextureRegion redCapeTextureAnim = new TiledTextureRegion(texturePackForRedCape.getTexture(),
				redCapeTextureAnimRegions);

		themeManager.redCapeTextureRegion=redCapeTextureAnim;
		texturePackForRedCape.getTexture().load();

		//Flag
		flagsTextureAnimRegions = new TexturePackerTextureRegion[this.texturePackForFlagsLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForFlagsLibrary.getIDMapping().size(); i++) {
			flagsTextureAnimRegions[i] = this.texturePackForFlagsLibrary.get(i);
		}

		texturePackForFlags.getTexture().load();
		//Lights
		TexturePackerTextureRegion[] lightsAnimRegions = new TexturePackerTextureRegion[this.texturePackForLightsLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForLightsLibrary.getIDMapping().size(); i++) {
			lightsAnimRegions[i] = this.texturePackForLightsLibrary.get(i);
		}

		TiledTextureRegion lightsAnim = new TiledTextureRegion(texturePackForLights.getTexture(),
				lightsAnimRegions);

		this.lightsRegion=lightsAnim;
		texturePackForLights.getTexture().load();

		//Daily Challenge
		TexturePackerTextureRegion[] dailyChallengeAnimRegions = new TexturePackerTextureRegion[this.texturePackForDailyChallengeLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForDailyChallengeLibrary.getIDMapping().size(); i++) {
			dailyChallengeAnimRegions[i] = this.texturePackForDailyChallengeLibrary.get(i);
		}

		TiledTextureRegion dailyChallengeAnim = new TiledTextureRegion(texturePackForDailyChallenge.getTexture(),
				dailyChallengeAnimRegions);

		this.dailyChallengeRegion=dailyChallengeAnim;
		texturePackForDailyChallenge.getTexture().load();



		//COINS
		TexturePackerTextureRegion[] coinTextureAnimRegions = new TexturePackerTextureRegion[this.texturePackForCoinLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForCoinLibrary.getIDMapping().size(); i++) {
			coinTextureAnimRegions[i] = this.texturePackForCoinLibrary.get(i);
		}

		TiledTextureRegion coinTextureAnim = new TiledTextureRegion(texturePackForCoin.getTexture(),
				coinTextureAnimRegions);

		themeManager.coinTextureRegion=coinTextureAnim;
		texturePackForCoin.getTexture().load();

		//Menu Resources
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		menuExtraButtonsTextureAtlas  = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		howToPlay_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.howToPlayButton);
		help_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.helpButton);
		chocobackbuttonregion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.chocoBackButton);
		moneybagRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.moneybag2tone);
		achievementLevelRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.achievementLevel);
		leaderBoardRegion  = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.leaderboard);
		earnCoinsRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.earnCoins);
		lights_BG_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuExtraButtonsTextureAtlas, activity, EntityTagManager.lightBGRegion);

		menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, EntityTagManager.menuBackground);
		//play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
		options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, EntityTagManager.optionsButton);

		//BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/gametheme/");
		gameHostTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		gameGuessTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);


		//TODO create bitmap atlas for new theme
		//game_host_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameHostTextureAtlas, activity, "game_cry.png");
		game_host_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameHostTextureAtlas, activity, EntityTagManager.hostSceneBackground);

		//game_guess_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameGuessTextureAtlas, activity, "game_cry6.png");
		game_guess_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameGuessTextureAtlas, activity, EntityTagManager.guessSceneBackground);
		//TexturePacker

		shareTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		whatsapp_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity, EntityTagManager.whatsappShareButton);
		gmail_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity,  EntityTagManager.gmailShareButton);
		fb_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity,  EntityTagManager.fbShareButton);
		fbLogin_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity,  EntityTagManager.fbLoginButton);
		fbPost_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity,  EntityTagManager.fbPostButton);
		shareIcon_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity,  EntityTagManager.shareAndroidIcon);
		fbFriend_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shareTextureAtlas, activity,  EntityTagManager.inviteFriend); 


		notifyTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1000, 1000, TextureOptions.BILINEAR);

		coolers_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity, EntityTagManager.coolersEntity);
		drama_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.dramaEntity);
		confetti_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.confettiEntity);
		sheryl_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.sherlockEntity);
		inveye_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.smallEyesEntity);
		tutorialname_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.tutorialNameEntity);
		tutorialnext_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.tutorialNextEntity);
		tutorialprev_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.tutorialPrevEntity);


		hostContinue_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.hostContinuetyEntity);
		guessAgain_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notifyTextureAtlas, activity,  EntityTagManager.guessAgainEntity);
		try {

			mGreenGrassTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), "gfx/notification/pinkheart.png", TextureOptions.BILINEAR);

			mGreyHeartTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), "gfx/notification/greyheart4.png", TextureOptions.BILINEAR);


			mBlueHeartTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), "gfx/notification/blueheart.png", TextureOptions.BILINEAR);
			mGreenHeartTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), "gfx/notification/greenheart.png", TextureOptions.BILINEAR);

			mYellowHoneyTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), "gfx/notification/actualgreenheart.png", TextureOptions.BILINEAR);
			mBlueHoneyTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), "gfx/notification/actualblueheart.png", TextureOptions.BILINEAR);

			mTutorialGradientTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), EntityTagManager.tutorialGradientEntity, TextureOptions.BILINEAR);
			mTutorialFinishTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), EntityTagManager.tutorialFinishEntity, TextureOptions.BILINEAR);


			mTutorialStep1Texture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(),EntityTagManager.tutorialstep1Entity, TextureOptions.BILINEAR);

			mTutorialStep2Texture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), EntityTagManager.tutorialstep2Entity, TextureOptions.BILINEAR);


			mTutorialStep3Texture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), EntityTagManager.tutorialstep3Entity, TextureOptions.BILINEAR);
			mTutorialStep4Texture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(),EntityTagManager.tutorialstep4Entity, TextureOptions.BILINEAR);


			mFBLoginTexture = new AssetBitmapTexture(activity.getTextureManager(), 
					activity.getAssets(), "gfx/notification/fb.png", TextureOptions.BILINEAR);
		} catch (IOException e) {
			Debug.e(e);
		}



		greenGrass_region = TextureRegionFactory.extractFromTexture(
				mGreenGrassTexture);
		mGreenGrassTexture.load();

		greyHeart_region = TextureRegionFactory.extractFromTexture(
				mGreyHeartTexture);
		mGreyHeartTexture.load();

		blueHeart_region = TextureRegionFactory.extractFromTexture(
				mBlueHeartTexture);
		mBlueHeartTexture.load();


		greenHeart_region = TextureRegionFactory.extractFromTexture(
				mGreenHeartTexture);
		mGreenHeartTexture.load();

		yellowHoney_region = TextureRegionFactory.extractFromTexture(
				mYellowHoneyTexture);
		mYellowHoneyTexture.load();


		blueHoney_region = TextureRegionFactory.extractFromTexture(
				mBlueHoneyTexture);
		mBlueHoneyTexture.load();

		//

		tutorialGradient_region = TextureRegionFactory.extractFromTexture(
				mTutorialGradientTexture);
		mTutorialGradientTexture.load();

		tutorialFinish_region = TextureRegionFactory.extractFromTexture(
				mTutorialFinishTexture);
		mTutorialFinishTexture.load();

		tutorialStep1_region = TextureRegionFactory.extractFromTexture(
				mTutorialStep1Texture);
		mTutorialStep1Texture.load();


		tutorialStep2_region = TextureRegionFactory.extractFromTexture(
				mTutorialStep2Texture);
		mTutorialStep2Texture.load();

		tutorialStep3_region = TextureRegionFactory.extractFromTexture(
				mTutorialStep3Texture);
		mTutorialStep3Texture.load();


		tutorialStep4_region = TextureRegionFactory.extractFromTexture(
				mTutorialStep4Texture);
		mTutorialStep4Texture.load();



		fbloginBG_region = TextureRegionFactory.extractFromTexture(
				mFBLoginTexture);
		mFBLoginTexture.load();

		try 
		{
			texturePack = new TexturePackLoader(activity.getTextureManager(), "gfx/menu/").loadFromAsset(activity.getAssets(), "play_icon.xml");
			texturePack.loadTexture();
			texturePackLibrary = texturePack.getTexturePackTextureRegionLibrary();
		} 
		catch (final TexturePackParseException e) 
		{
			Debug.e(e);
		}

		/*textureRegion_0 = texturePackLibrary.get(PlayIcon.PLAY1_ID);
        textureRegion_1 = texturePackLibrary.get(PlayIcon.PLAY2_ID);
        textureRegion_2 = texturePackLibrary.get(PlayIcon.PLAY3_ID);
        textureRegion_3 = texturePackLibrary.get(PlayIcon.PLAY4_ID);
        textureRegion_4 = texturePackLibrary.get(PlayIcon.PLAY5_ID);
        textureRegion_5 = texturePackLibrary.get(PlayIcon.PLAY6_ID);
        textureRegion_7 = texturePackLibrary.get(PlayIcon.PLAY8_ID);
        textureRegion_8 = texturePackLibrary.get(PlayIcon.PLAY9_ID);
        textureRegion_9 = texturePackLibrary.get(PlayIcon.PLAY10_ID);
		 */
		TexturePackerTextureRegion[] obj = new TexturePackerTextureRegion[this.texturePackLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackLibrary.getIDMapping().size(); i++) {
			obj[i] = this.texturePackLibrary.get(i);
		}

		TiledTextureRegion playTextureAnim = new TiledTextureRegion(texturePack.getTexture(),
				obj);

		playRegionPack=playTextureAnim;
		texturePack.getTexture().load();
		//TexturePacker

		try 
		{
			this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.menuTextureAtlas.load();


			this.shareTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.shareTextureAtlas.load();

			this.menuExtraButtonsTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
			this.menuExtraButtonsTextureAtlas.load();

			this.notifyTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
			this.notifyTextureAtlas.load();

		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	public void loadShopBannerScene(){
		this.shopTextureAtlas  = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		//For ShopBanners
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");
		shopBeesGreenRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shopTextureAtlas, activity, EntityTagManager.shopBeesGreen);
		shopBeesBlueRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shopTextureAtlas, activity, EntityTagManager.shopBeesBlue);
		shopMultiplierBackgroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shopTextureAtlas, activity, EntityTagManager.multiplierbg);
		shopWordCoinRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shopTextureAtlas, activity, EntityTagManager.coinForShop);
		shopPaintCurtainRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shopTextureAtlas, activity, EntityTagManager.shopPaintCurtain);
		shopBuyButtonRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shopTextureAtlas, activity, EntityTagManager.shopBuyButton);
		shopLockRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(shopTextureAtlas, activity, EntityTagManager.shopLock);

		//Oil painted bg
		/*try{
			shopBackgroundTexture=new AssetBitmapTexture(activity.getTextureManager(),
					activity.getAssets(), EntityTagManager.shopBg, TextureOptions.BILINEAR);
		} catch (IOException e) {
			Debug.e(e);

		}
		shopBackgroundRegion = TextureRegionFactory.extractFromTexture(
				shopBackgroundTexture);
		shopBackgroundTexture.load();*/ //uncomment For shop_puzzle_bg.png  
		try{
			this.shopTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
			this.shopTextureAtlas.load();

		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}

		//For ShopBanners
	}

	private void loadMenuAudio()
	{
		SoundManager.getInstance().loadInGameSounds();
	}

	private void loadGameGraphics()
	{

	}

	private void loadGameFonts()
	{
		this.font = FontFactory.create(activity.getFontManager(), activity.getTextureManager(), 512, 512, TextureOptions.BILINEAR, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 64);
		this.font.load();	

		this.mBitmapFontOption = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontOptionStyle);
		this.mBitmapFontOption.load();

		this.mBitmapFontForGame = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontForGameStyle);
		this.mBitmapFontForGame.load();

		/*mLargeFontTexture = new BitmapTextureAtlas(activity.getTextureManager(),512, 512, TextureOptions.BILINEAR);
		mLargeFont = FontFactory.createStroke(activity.getFontManager(), mLargeFontTexture, 
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 50, true, 
				new Color(0.3f,0.34f,0.3f).getABGRPackedInt(), 3, new Color(0.3f,0.35f,0.3f).getABGRPackedInt());

		mSmallFontTexture = new BitmapTextureAtlas(activity.getTextureManager(),256, 256, TextureOptions.BILINEAR);
		mSmallFont = new Font(activity.getFontManager(),mSmallFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, new Color(0.2f,0.2f,0.2f).getABGRPackedInt());

		engine.getTextureManager().loadTexture(mLargeFontTexture);
		engine.getTextureManager().loadTexture(mSmallFontTexture);

		engine.getFontManager().loadFonts(mLargeFont, mSmallFont);
		 */
		this.mBitmapFontWooden = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontWooden);
		this.mBitmapFontWooden.load();

		this.mBitmapBabyKrufy = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontBabyKrufy);
		this.mBitmapBabyKrufy.load();

		this.mBitmapFontRound = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontRound);
		this.mBitmapFontRound.load();


		this.mBitmapFontOutline = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontOutline);
		this.mBitmapFontOutline.load();

		this.mBitmapHowToPlayLarge = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapHowToPlayLargeStyle);
		this.mBitmapHowToPlayLarge.load();

		this.mBitmapHowToPlaySmall = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapHowToPlaySmall);
		this.mBitmapHowToPlaySmall.load();


		mSmallFontTexture = new BitmapTextureAtlas(activity.getTextureManager(),256, 256, TextureOptions.BILINEAR);
		mSmallFont = new Font(activity.getFontManager(),mSmallFontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 24, true, new Color(0.2f,0.2f,0.2f).getABGRPackedInt());
		engine.getTextureManager().loadTexture(mSmallFontTexture);

		engine.getFontManager().loadFonts(mSmallFont);

	}

	private void loadGameAudio()
	{
		//MusicService.mPlayerHifi = MediaPlayer.create(activity, R.raw.hifi_02);
		MusicService.mPlayerRock = MediaPlayer.create(activity, R.raw.ingamerock_ballad);
		MusicService.mPlayerBeemusic = MediaPlayer.create(activity, R.raw.gamemusic);
		MusicService.mPlayerFastMarch = MediaPlayer.create(activity, R.raw.jingleslow);
		MusicService.mPlayerBeemusicJingle = MediaPlayer.create(activity, R.raw.beejinglecrop);
		MusicService.mPlayerFastMarchJingle = MediaPlayer.create(activity, R.raw.jinglevolumeredu);
		MusicService.mPlayerLost = MediaPlayer.create(activity, R.raw.lostcomp);
	}

	public void loadSplashScreen()
	{
		//sampleText to get Dimensions
		loadGameFonts();

		splash_region = new TickerText(windowDimensions.x*.5f, windowDimensions.y*.5f, this.font, "Loading Game........!", new TickerTextOptions(HorizontalAlign.CENTER, 6), activity.getVertexBufferObjectManager());
		splash_region.registerEntityModifier(
				new SequenceEntityModifier(
						new ParallelEntityModifier(
								new AlphaModifier(10, 0.1f, 0.9f),
								new ScaleModifier(10, 0.5f*resourceScaler, 1.0f*resourceScaler)
								),
								new RotationModifier(10, 0, 360)
						)
				);
		//splash_region.setScale(1.5f);


	}

	public void unloadSplashScreen()
	{

	}

	public static void prepareManager(Engine engine, MainActivity activity, Camera camera, VertexBufferObjectManager vbom, Point dimensions, PhysicsWorld mPhysWorld)
	{
		getInstance().engine = engine;
		getInstance().activity = activity;
		getInstance().camera = camera;
		getInstance().vbom = vbom;
		/*		Log.d("Unscaled", "Unscaled Width: " + dimensions.x + "Unscaled Height: "+dimensions.y);
		dimensions.set((int)(dimensions.x/resourceScaler), (int)(dimensions.y/resourceScaler));
		Log.d("Scaled", "Scaled Width: " + dimensions.x + "Scaled Height: "+dimensions.y);*/
		getInstance().windowDimensions = dimensions;
		getInstance().mPhysWorld=mPhysWorld;
	}

	public void loadWaitingRoomResources(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/notification/");

		notificationTextureAtlas  = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);

		blueMenu_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notificationTextureAtlas, activity, EntityTagManager.notificationMenuButton);
		//blueTryAgain_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notificationTextureAtlas, activity, EntityTagManager.notificationTryAgainButton);
		//greenWorld_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notificationTextureAtlas, activity, EntityTagManager.greenWorld);
		leave_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notificationTextureAtlas, activity, EntityTagManager.leaveButton);
		stay_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notificationTextureAtlas, activity, EntityTagManager.stayButton);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/waitingScene/");
		redWaitingSceneUserChunk_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(notificationTextureAtlas, activity, EntityTagManager.waitingSceneUserChunkMask);

		try 
		{
			this.notificationTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.notificationTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}
	}

	public void loadHostResources() {

		try {
			this.gameHostTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.gameHostTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//Create and load particleSystem
		createAndLoadParticlesystem("gfx/snowParticle/snow-particlea.png",1);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particleb.png",2);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particleh.png",3);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particlep.png",4);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particlei.png",5);

		this.mBitmapFont = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontStyle1);
		this.mBitmapFont.load();

		//Keys with Codes For KeyBoard
		KeyboardChar[] keys=new KeyboardChar[26];
		String[] codes= {"Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M"};

		//sampleText to get Dimensions
		Text dimensionText= new Text( 20, 20, mBitmapFont, "Q", vbom);
		dimensionText.setScale(0.8f*resourceScaler);


		letterWidth = (dimensionText.getWidth()*resourceScaler)-3*resourceScaler;
		xTextStart = (windowDimensions.x*0.5f) - ((dimensionText.getWidth()*4.85f) *resourceScaler);
		yTextStart = yKeyBoardStartOffsetReference*resourceScaler;

		float pX= xTextStart;
		float pY=(yKeyBoardStartOffsetReference-45)*resourceScaler;

		//KeyboardCharRectangle width n Height
		float pWidth=windowDimensions.x/26;
		float pHeight=dimensionText.getHeightScaled();
		letterheight = pHeight;

		List<String> vowels=new ArrayList<String>();
		vowels.add("A");
		vowels.add("E");
		vowels.add("I");
		vowels.add("O");
		vowels.add("U");

		for (int i = 0; i < keys.length; i++) {

			keyBoardBitmapText[i]= new Text( ((pWidth+5)/2), 25*resourceScaler, mBitmapFont, codes[i], new TextOptions(AutoWrap.LETTERS,pWidth,HorizontalAlign.CENTER), vbom);

			if(!vowels.contains(codes[i])){
				keyBoardBitmapText[i].setScale(0.8f*resourceScaler);
			}
			else{
				keyBoardBitmapText[i].setScale(0.9f*resourceScaler);
			}

			//Keyboard layout design depends on the x and y position of each keys and the spacing between them
			//from 11th letter move x and Y to next line as in Keyboard
			if(i==10){
				pX=(windowDimensions.x*0.5f) - ((dimensionText.getWidth()*4.30f)*resourceScaler);
				pY=(yKeyBoardStartOffsetReference-105)*resourceScaler;
			}
			else if(i==19){
				pX=(windowDimensions.x*0.5f) - ((dimensionText.getWidth()*3.29f)*resourceScaler);
				pY=(yKeyBoardStartOffsetReference-165)*resourceScaler;
			}

			KeyboardChar keyboardChar;
			Log.e(" Host pHeight", pHeight+"");
			keyboardChar=new KeyboardChar(pX, pY, pWidth+5, pHeight, codes[i],keyBoardBitmapText[i], vbom);
			/*keyboardChar.setKeyboardPositionX(pX);
			keyboardChar.setKeyboardPositionY(pY);
			 */
			pX=pX+pWidth+8;

			themeManager.setKeyBoardCharTheme(keyboardChar);

			keyboardChar.setBlendFunctionDestination(GLES20.GL_ALWAYS);
			keyboardChar.attachChild(keyBoardBitmapText[i]);

			//keyboard.attachChild(keyboardChar);
			keys[i]=keyboardChar;
		}

		keyboardString.setKeyCharArray(keys);


	}


	public void loadGuessResources() {



		try {
			this.gameGuessTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.gameGuessTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		//Create and load particleSystem
		createAndLoadParticlesystem("gfx/snowParticle/snow-particlea.png",1);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particleb.png",2);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particleh.png",3);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particlep.png",4);

		createAndLoadParticlesystem("gfx/snowParticle/snow-particlei.png",5);

		this.mBitmapFont = new BitmapFont(activity.getTextureManager(), activity.getAssets(), EntityTagManager.mBitmapFontStyle2);
		this.mBitmapFont.load();

		//Keys with Codes For KeyBoard
		KeyboardChar[] keys=new KeyboardChar[26];
		String[] codes= {"Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M"};

		//sampleText to get Dimensions
		Text dimensionText= new Text( 20, 20, mBitmapFont, "Q", vbom);
		dimensionText.setScale(0.8f*resourceScaler);

		letterWidth = dimensionText.getWidth()*resourceScaler-3*resourceScaler;
		xTextStart = (windowDimensions.x*0.5f) - ((dimensionText.getWidth()*4.85f) *resourceScaler);
		yTextStart = yKeyBoardStartOffsetReference*resourceScaler;

		float pX= xTextStart;
		float pY=(yKeyBoardStartOffsetReference-45)*resourceScaler;

		//KeyboardCharRectangle width n Height
		float pWidth=windowDimensions.x/26;
		float pHeight=dimensionText.getHeightScaled();
		letterheight = pHeight;
		List<String> vowels=new ArrayList<String>();
		vowels.add("A");
		vowels.add("E");
		vowels.add("I");
		vowels.add("O");
		vowels.add("U");

		for (int i = 0; i < keys.length; i++) {

			keyBoardBitmapText[i]= new Text( (pWidth+5)/2, 25*resourceScaler, mBitmapFont, codes[i], new TextOptions(AutoWrap.LETTERS,pWidth,HorizontalAlign.CENTER), vbom);

			if(!vowels.contains(codes[i])){
				keyBoardBitmapText[i].setScale(0.8f*resourceScaler);
			}
			else{
				keyBoardBitmapText[i].setScale(0.9f*resourceScaler);
			}

			//Keyboard layout design depends on the x and y position of each keys and the spacing between them
			//from 11th letter move x and Y to next line as in Keyboard
			if(i==10){
				pX=(windowDimensions.x*0.5f) - ((dimensionText.getWidth()*4.30f)*resourceScaler);
				pY=(yKeyBoardStartOffsetReference-105)*resourceScaler;
			}
			else if(i==19){
				pX=(windowDimensions.x*0.5f) - ((dimensionText.getWidth()*3.29f)*resourceScaler);
				pY=(yKeyBoardStartOffsetReference-165)*resourceScaler;
			}

			KeyboardChar keyboardChar;
			Log.e(" Host pHeight", pHeight+"");
			keyboardChar=new KeyboardChar(pX, pY, pWidth+5, pHeight, codes[i],keyBoardBitmapText[i], vbom);
			/*keyboardChar.setKeyboardPositionX(pX);
			keyboardChar.setKeyboardPositionY(pY);
			 */
			pX=pX+pWidth+8;

			themeManager.setKeyBoardCharTheme(keyboardChar);

			keyboardChar.setBlendFunctionDestination(GLES20.GL_ALWAYS);
			keyboardChar.attachChild(keyBoardBitmapText[i]);

			//keyboard.attachChild(keyboardChar);
			keys[i]=keyboardChar;
		}

		keyboardString.setKeyCharArray(keys);
	}


	public void loadOptionResources() {

		//Create and load thumbnails
		themeManager.themeSelection= new Text(50*resourceScaler, windowDimensions.y-50*resourceScaler, this.mBitmapFontOption, "Select Theme", vbom);
		themeManager.themeSelection.setColor(.1f,.1f,.1f,.8f);
		themeManager.themeSelection.setAnchorCenterX(0);

		themeManager.musicOnOffText= new Text(50*resourceScaler, windowDimensions.y-200*resourceScaler, this.mBitmapFontOption, "Music On/Off", vbom);
		themeManager.musicOnOffText.setColor(.1f,.1f,.1f,.8f);
		themeManager.musicOnOffText.setAnchorCenterX(0);

		themeManager.tutorialOnOffText= new Text(50*resourceScaler, windowDimensions.y-350*resourceScaler, this.mBitmapFontOption, "Tutorial", vbom);
		themeManager.tutorialOnOffText.setColor(.1f,.1f,.1f,.8f);
		themeManager.tutorialOnOffText.setAnchorCenterX(0);

		themeManager.startUpAnimationOnOffText= new Text(50*resourceScaler, windowDimensions.y-425 *resourceScaler, this.mBitmapFontWooden, "STARTUP ANIMATION OFF \nClick To Toggle", vbom){
			public boolean onAreaTouched(org.andengine.input.touch.TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					if(StorageManager.getInstance().getUser().isStartUpAnimationRequired()){
						StorageManager.getInstance().getUser().setStartUpAnimationRequired(false);
						themeManager.startUpAnimationOnOffText.setColor(.9f,.1f,.1f,.9f);
						StorageManager.getInstance().saveStartUpAnimPref(false);
						themeManager.startUpAnimationOnOffText.setText("STARTUP ANIMATION OFF \nClick To Toggle");
					}
					else{
						StorageManager.getInstance().getUser().setStartUpAnimationRequired(true);
						themeManager.startUpAnimationOnOffText.setColor(.1f,.1f,.1f,.9f);
						StorageManager.getInstance().saveStartUpAnimPref(true);
						themeManager.startUpAnimationOnOffText.setText("STARTUP ANIMATION ON \nClick To Toggle");
					}
					return true;
				}
				return false;
			};
		};
		if(StorageManager.getInstance().getUser().isStartUpAnimationRequired()){
			themeManager.startUpAnimationOnOffText.setColor(.1f,.1f,.1f,.9f);
			themeManager.startUpAnimationOnOffText.setText("STARTUP ANIMATION ON \nClick To Toggle");
		}
		else
		{
			themeManager.startUpAnimationOnOffText.setColor(.9f,.1f,.1f,.9f);
			themeManager.startUpAnimationOnOffText.setText("STARTUP ANIMATION OFF \nClick To Toggle");
		}
		themeManager.startUpAnimationOnOffText.setAnchorCenterX(0);

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/option/");
		themeManager.optionTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 800, 800, TextureOptions.BILINEAR);

		themeManager.themeThumbnailOneCreative = BitmapTextureAtlasTextureRegionFactory.createFromAsset(themeManager.optionTextureAtlas, activity, EntityTagManager.themeThumbnailOneCreativeImage);
		themeManager.themeThumbnailTwoYellow =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(themeManager.optionTextureAtlas, activity, EntityTagManager.themeThumbnailTwoYellowImage);
		themeManager.howToPlay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(themeManager.optionTextureAtlas, activity, EntityTagManager.howToPlayImage );


		this.musicOnOff = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(themeManager.optionTextureAtlas, activity, EntityTagManager.musicOnOffImage, 2, 1);
		this.tutorialOnOff = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(themeManager.optionTextureAtlas, activity, EntityTagManager.tutorialOnOffImage, 2, 1);

		try 
		{
			themeManager.optionTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(1, 1, 1));
			themeManager.optionTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}

	}

	public void loadHostGuessGameSceneResources() {
		loadGameAudio();
		//Create and load host and Guess images
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/gametheme/");
		this.hostGuessGameBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.hostHighlightTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 570, TextureOptions.BILINEAR);
		this.hostTransparentTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 570, TextureOptions.NEAREST);
		this.guessHighlightTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 570, TextureOptions.BILINEAR);
		this.guessTransparentTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 570, TextureOptions.NEAREST);

		this.animationTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);


		this.host_guess_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hostGuessGameBackgroundTextureAtlas, activity, EntityTagManager.hostGuessBackground);

		this.hostHighlightRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.hostHighlightTextureAtlas, activity, EntityTagManager.hostHighlightImage);
		this.hostTransparentRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.hostTransparentTextureAtlas, activity, EntityTagManager.hostTransparentImage);

		this.guessHighlightRegion =  BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.guessHighlightTextureAtlas, activity, EntityTagManager.guessHighlightImage);
		this.guessTransparentRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.guessTransparentTextureAtlas, activity, EntityTagManager.guessTransparentImage);

		//this.swipeBlueStripRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTextureAtlas, activity, EntityTagManager.swipeBlueStripImage);
		this.rotatorRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTextureAtlas, activity, EntityTagManager.rotatorImage);
		playersDrawerRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTextureAtlas, activity, EntityTagManager.chocoPlayersDrawer);
		qrcode_Region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTextureAtlas, activity,  EntityTagManager.qrcodewithbarcode);

		//Bee Animation
		//themeManager.ballTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(animationTextureAtlas, activity, "colours_of_joy.png");
		try 
		{
			texturePackForBee = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "happybee.xml");
			texturePackForBee.loadTexture();
			texturePackForBeeLibrary = texturePackForBee.getTexturePackTextureRegionLibrary();

			texturePackForBullRun = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "bull.xml");
			texturePackForBullRun.loadTexture();
			texturePackForBullrunLibrary = texturePackForBullRun.getTexturePackTextureRegionLibrary();

			texturePackForChat = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "chat.xml");
			texturePackForChat.loadTexture();
			texturePackForChatLibrary = texturePackForChat.getTexturePackTextureRegionLibrary();


			texturePackForAskHelp = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "askhelp.xml");
			texturePackForAskHelp.loadTexture();
			texturePackForAskHelpLibrary = texturePackForAskHelp.getTexturePackTextureRegionLibrary();


		} 
		catch (final TexturePackParseException e) 
		{
			Debug.e(e);
		}

		TexturePackerTextureRegion[] obj = new TexturePackerTextureRegion[this.texturePackForBeeLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForBeeLibrary.getIDMapping().size(); i++) {
			obj[i] = this.texturePackForBeeLibrary.get(i);
		}

		TiledTextureRegion beeTextureAnim = new TiledTextureRegion(texturePackForBee.getTexture(),
				obj);

		themeManager.beeTextureRegion=beeTextureAnim;
		texturePackForBee.getTexture().load();

		//Bull Run
		TexturePackerTextureRegion[] objBullRun = new TexturePackerTextureRegion[this.texturePackForBullrunLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForBullrunLibrary.getIDMapping().size(); i++) {
			objBullRun[i] = this.texturePackForBullrunLibrary.get(i);
		}

		TiledTextureRegion bullTextureAnim = new TiledTextureRegion(texturePackForBullRun.getTexture(),
				objBullRun);

		themeManager.bullTextureRegion=bullTextureAnim;
		texturePackForBullRun.getTexture().load();


		//Chat
		TexturePackerTextureRegion[] objChat = new TexturePackerTextureRegion[this.texturePackForChatLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForChatLibrary.getIDMapping().size(); i++) {
			objChat[i] = this.texturePackForChatLibrary.get(i);
		}

		TiledTextureRegion chatTextureAnim = new TiledTextureRegion(texturePackForChat.getTexture(),
				objChat);

		chat_Region=chatTextureAnim;
		texturePackForChat.getTexture().load();

		//Ask Help
		TexturePackerTextureRegion[] objAskHelp = new TexturePackerTextureRegion[this.texturePackForAskHelpLibrary.getIDMapping().size()];

		for (int i = 0; i < this.texturePackForAskHelpLibrary.getIDMapping().size(); i++) {
			objAskHelp[i] = this.texturePackForAskHelpLibrary.get(i);
		}

		TiledTextureRegion askHelpTextureAnim = new TiledTextureRegion(texturePackForAskHelp.getTexture(),
				objAskHelp);

		askHelp_Region=askHelpTextureAnim;
		texturePackForAskHelp.getTexture().load();

		//build atlas
		try 
		{
			this.hostGuessGameBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.hostGuessGameBackgroundTextureAtlas.load();
			this.hostHighlightTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.hostHighlightTextureAtlas.load();
			this.hostTransparentTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.hostTransparentTextureAtlas.load();
			this.guessHighlightTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.guessHighlightTextureAtlas.load();
			this.guessTransparentTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.guessTransparentTextureAtlas.load();
			this.animationTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.animationTextureAtlas.load();
		} 
		catch (final TextureAtlasBuilderException e)
		{
			Debug.e(e);
		}

	}

	public void loadHowToPlayScene(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/option/");


		mMenuTexture = new BitmapTextureAtlas(activity.getTextureManager(),256, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mMenuBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuTexture, activity, "go-back-button.png", 0, 250);

		gradientTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		this.gradientOverlay = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gradientTextureAtlas, activity, "gradientoverlay.png");
		howToPlayIllustrateGreenRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gradientTextureAtlas, activity, EntityTagManager.shoppedGreenCard);

		try {
			this.gradientTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			gradientTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}



		engine.getTextureManager().loadTexture(mMenuTexture);

	}

	public void loadCreditsScene(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/option/");

		mMenuTexture = new BitmapTextureAtlas(activity.getTextureManager(),256, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		mMenuBackTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuTexture, activity, "go-back-button.png", 0, 250);

		engine.getTextureManager().loadTexture(mMenuTexture);

	}


	public void loadScrollableRoomsScene(){
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");


		hostRoomsBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		guessRoomsBackgroundTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(),1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		//this.host_room_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(hostRoomsBackgroundTextureAtlas, activity, "gameHostRoomSceneThemeCool.png");
		//this.guess_room_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(guessRoomsBackgroundTextureAtlas, activity, "gameGuessRoomSceneThemeCool.png");

		try {
			this.hostRoomsBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			this.guessRoomsBackgroundTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
			hostRoomsBackgroundTextureAtlas.load();
			guessRoomsBackgroundTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}

	}

	public void createAndLoadParticlesystem(String particlePath,int letter) {

		switch (letter){
		case 1:
			//A
			//Create Snow Particle
			try {
				mSnowParticle = new AssetBitmapTexture(activity.getTextureManager(), 
						activity.getAssets(), particlePath, TextureOptions.BILINEAR);
			} catch (IOException e) {
				Debug.e(e);
			}
			mSnowParticleRegion = TextureRegionFactory.extractFromTexture(
					mSnowParticle);
			mSnowParticle.load();
			mSnowParticle.getTextureOptions().apply();
			//Create Snow Particle
			//load Particle system
			//mSnowParticleRegion.setTextureSize(100, 100);
			particleSystem = new BatchedPseudoSpriteParticleSystem(
					new RectangleParticleEmitter(windowDimensions.x / 2, windowDimensions.y, windowDimensions.x, 1),
					5, 8, 50, mSnowParticleRegion, 
					vbom);
			particleSystem.setCullingEnabled(true);
			particleSystem.setTag(EntityTagManager.letterA);
			particleSystem.setBlendFunction(GLES20.GL_ALPHA_BITS, GLES20.GL_ALPHA_BITS);
			particleSystem.addParticleInitializer(new VelocityParticleInitializer<Entity>(-3, 3, -40, -70));
			particleSystem.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, -3, -5));
			particleSystem.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystem.addParticleInitializer(new ExpireParticleInitializer<Entity>(10f));
			particleSystem.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f*resourceScaler, 0.5f*resourceScaler));
			particleSystem.addParticleInitializer(new RegisterXSwingEntityModifierInitializer<Entity>(10f, 0f, (float) Math.PI * 8, 3f, 25f, true));
			particleSystem.addParticleInitializer(new ColorParticleModifier<Entity>(0, 2, 0.0f, 0.0f, 1f , 1f, 1f, 1f ));
			particleSystem.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));
			/*particleSystem.addParticleInitializer(new IParticleInitializer<Entity>() {

				public void onInitializeParticle(Particle<Entity> pParticle) {
					Body body = PhysicsFactory.createCircleBody(activity.mPhysWorld, pParticle.getEntity(), BodyType.DynamicBody, activity.mCellFixtureDef);
					activity.mPhysWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body, true, true));
				}

			});*/
			particleSystem.setAlpha(1f);
			break;
		case 2:
			//B
			//Create Snow Particle
			try {
				mSnowParticleb = new AssetBitmapTexture(activity.getTextureManager(), 
						activity.getAssets(), particlePath, TextureOptions.BILINEAR);
			} catch (IOException e) {
				Debug.e(e);
			}
			mSnowParticleRegionb = TextureRegionFactory.extractFromTexture(
					mSnowParticleb);
			mSnowParticleb.load();
			mSnowParticleb.getTextureOptions().apply();
			//Create Snow Particle
			//load Particle system
			//mSnowParticleRegion.setTextureSize(100, 100);
			particleSystemb = new BatchedPseudoSpriteParticleSystem(
					new RectangleParticleEmitter(windowDimensions.x / 2, windowDimensions.y, windowDimensions.x, 1),
					2, 5, 20, mSnowParticleRegionb, 
					vbom);
			particleSystemb.setCullingEnabled(true);
			particleSystemb.setTag(EntityTagManager.letterB);
			particleSystemb.setBlendFunction(GLES20.GL_ALPHA_BITS, GLES20.GL_ALPHA_BITS);
			particleSystemb.addParticleInitializer(new VelocityParticleInitializer<Entity>(-6, 9, -20, -70));
			particleSystemb.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-6, 3, -1, -5));
			particleSystemb.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystemb.addParticleInitializer(new ExpireParticleInitializer<Entity>(10f));
			particleSystemb.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f*resourceScaler, 0.4f*resourceScaler));
			particleSystemb.addParticleInitializer(new RegisterXSwingEntityModifierInitializer<Entity>(10f, 0f, (float) Math.PI * 8, 3f, 25f, true));
			particleSystemb.addParticleInitializer(new ColorParticleModifier<Entity>(0, 10, 0.0f, 0.0f, 0.0f , 0.8f, 0.4f, 0.4f ));
			particleSystemb.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));
			/*particleSystemb.addParticleInitializer(new IParticleInitializer<Entity>() {

				public void onInitializeParticle(Particle<Entity> pParticle) {
					Body body = PhysicsFactory.createCircleBody(activity.mPhysWorld, pParticle.getEntity(), BodyType.DynamicBody, activity.mCellFixtureDef);
					activity.mPhysWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body, true, true));
				}

			});*/
			particleSystemb.setAlpha(1f);
			break;
		case 3:
			//H
			//Create Snow Particle
			try {
				mSnowParticleh = new AssetBitmapTexture(activity.getTextureManager(), 
						activity.getAssets(), particlePath, TextureOptions.BILINEAR);
			} catch (IOException e) {
				Debug.e(e);
			}
			mSnowParticleRegionh = TextureRegionFactory.extractFromTexture(
					mSnowParticleh);
			mSnowParticleh.load();
			mSnowParticleh.getTextureOptions().apply();
			//Create Snow Particle
			//load Particle system
			//mSnowParticleRegion.setTextureSize(100, 100);
			particleSystemh = new BatchedPseudoSpriteParticleSystem(
					new RectangleParticleEmitter(windowDimensions.x / 2, windowDimensions.y, windowDimensions.x, 1),
					2, 10, 15, mSnowParticleRegionh, 
					vbom);
			particleSystemh.setCullingEnabled(true);
			particleSystemh.setTag(EntityTagManager.letterH);
			particleSystemh.setBlendFunction(GLES20.GL_ALPHA_BITS, GLES20.GL_ALPHA_BITS);
			particleSystemh.addParticleInitializer(new VelocityParticleInitializer<Entity>(-3, 8, -40, -50));
			particleSystemh.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, -3, -5));
			particleSystemh.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystemh.addParticleInitializer(new ExpireParticleInitializer<Entity>(10f));
			particleSystemh.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.3f*resourceScaler, 0.7f*resourceScaler));
			particleSystemh.addParticleInitializer(new RegisterXSwingEntityModifierInitializer<Entity>(10f, 0f, (float) Math.PI * 8, 3f, 25f, true));
			particleSystemh.addParticleInitializer(new ColorParticleModifier<Entity>(0, 10, 0.0f, 0.0f, 0.0f , 0.8f, 0.4f, 0.4f ));
			particleSystemh.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));
			/*particleSystemh.addParticleInitializer(new IParticleInitializer<Entity>() {

				public void onInitializeParticle(Particle<Entity> pParticle) {
					Body body = PhysicsFactory.createCircleBody(activity.mPhysWorld, pParticle.getEntity(), BodyType.DynamicBody, activity.mCellFixtureDef);
					activity.mPhysWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body, true, true));
				}

			});*/
			particleSystemh.setAlpha(1f);
			break;
		case 4:
			//p
			//Create Snow Particle
			try {
				mSnowParticlep = new AssetBitmapTexture(activity.getTextureManager(), 
						activity.getAssets(), particlePath, TextureOptions.BILINEAR);
			} catch (IOException e) {
				Debug.e(e);
			}
			mSnowParticleRegionp = TextureRegionFactory.extractFromTexture(
					mSnowParticlep);
			mSnowParticlep.load();
			mSnowParticlep.getTextureOptions().apply();
			//Create Snow Particle
			//load Particle system
			//mSnowParticleRegion.setTextureSize(100, 100);
			particleSystemp = new BatchedPseudoSpriteParticleSystem(
					new RectangleParticleEmitter(windowDimensions.x / 2, windowDimensions.y, windowDimensions.x, 1),
					2, 5, 30, mSnowParticleRegionp, 
					vbom);
			particleSystemp.setCullingEnabled(true);
			particleSystemp.setTag(EntityTagManager.letterP);
			particleSystemp.setBlendFunction(GLES20.GL_ALPHA_BITS, GLES20.GL_ALPHA_BITS);
			particleSystemp.addParticleInitializer(new VelocityParticleInitializer<Entity>(-7, 3, -10, -40));
			particleSystemp.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, -15, -30));
			particleSystemp.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystemp.addParticleInitializer(new ExpireParticleInitializer<Entity>(10f));
			particleSystemp.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.3f*resourceScaler, .5f*resourceScaler));
			particleSystemp.addParticleInitializer(new RegisterXSwingEntityModifierInitializer<Entity>(10f, 0f, (float) Math.PI * 8, 3f, 25f, true));
			particleSystemp.addParticleInitializer(new ColorParticleModifier<Entity>(0, 10, 0.0f, 0.0f, 0.0f , 0.8f, 0.4f, 0.4f ));
			particleSystemp.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));
			/*particleSystemp.addParticleInitializer(new IParticleInitializer<Entity>() {

				public void onInitializeParticle(Particle<Entity> pParticle) {
					Body body = PhysicsFactory.createCircleBody(activity.mPhysWorld, pParticle.getEntity(), BodyType.DynamicBody, activity.mCellFixtureDef);
					activity.mPhysWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body, true, true));
				}

			});*/
			particleSystemp.setAlpha(1f);
			break;
		case 5:
			//A
			//Create Snow Particle
			try {
				mSnowParticlei = new AssetBitmapTexture(activity.getTextureManager(), 
						activity.getAssets(), particlePath, TextureOptions.BILINEAR);
			} catch (IOException e) {
				Debug.e(e);
			}
			mSnowParticleRegioni = TextureRegionFactory.extractFromTexture(
					mSnowParticlei);
			mSnowParticlei.load();
			mSnowParticlei.getTextureOptions().apply();
			//Create Snow Particle
			//load Particle system
			//mSnowParticleRegion.setTextureSize(100, 100);
			particleSystemi = new BatchedPseudoSpriteParticleSystem(
					new RectangleParticleEmitter(windowDimensions.x / 2, windowDimensions.y, windowDimensions.x, 1),
					2, 5, 50, mSnowParticleRegioni, 
					vbom);
			particleSystemi.setCullingEnabled(true);
			particleSystemi.setTag(EntityTagManager.letterI);
			particleSystemi.setBlendFunction(GLES20.GL_ALPHA_BITS, GLES20.GL_ALPHA_BITS);
			particleSystemi.addParticleInitializer(new VelocityParticleInitializer<Entity>(-3, 5, -30, -70));
			particleSystemi.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, -3, -5));
			particleSystemi.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystemi.addParticleInitializer(new ExpireParticleInitializer<Entity>(10f));
			particleSystemi.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f*resourceScaler, 0.4f*resourceScaler));
			particleSystemi.addParticleInitializer(new RegisterXSwingEntityModifierInitializer<Entity>(10f, 0f, (float) Math.PI * 8, 3f, 25f, true));
			particleSystemi.addParticleInitializer(new ColorParticleModifier<Entity>(0, 10, 0.0f, 0.0f, 0.0f , 0.8f, 0.4f, 0.4f ));
			particleSystemi.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));
			/*particleSystemi.addParticleInitializer(new IParticleInitializer<Entity>() {

				public void onInitializeParticle(Particle<Entity> pParticle) {
					Body body = PhysicsFactory.createCircleBody(activity.mPhysWorld, pParticle.getEntity(), BodyType.DynamicBody, activity.mCellFixtureDef);
					activity.mPhysWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body, true, true));
				}

			});*/
			particleSystemi.setAlpha(1f);
			break;
		case 6:
			//A
			//Create Snow Particle
			try {
				mSnowParticleStars = new AssetBitmapTexture(activity.getTextureManager(), 
						activity.getAssets(), particlePath, TextureOptions.BILINEAR);
			} catch (IOException e) {
				Debug.e(e);
			}
			mSnowParticleRegionStars = TextureRegionFactory.extractFromTexture(
					mSnowParticleStars);
			mSnowParticleStars.load();
			mSnowParticleStars.getTextureOptions().apply();
			//Create Snow Particle
			//load Particle system
			//mSnowParticleRegion.setTextureSize(100, 100);
			particleSystemStars = new BatchedPseudoSpriteParticleSystem(
					new RectangleParticleEmitter(windowDimensions.x / 2, windowDimensions.y*0.9f, windowDimensions.x, 1),
					2, 7, 80, mSnowParticleRegionStars, 
					vbom);
			particleSystemStars.setCullingEnabled(true);
			particleSystemStars.setTag(EntityTagManager.brightStar);
			particleSystemStars.setBlendFunction(GLES20.GL_ALPHA_BITS, GLES20.GL_ALWAYS);
			particleSystemStars.addParticleInitializer(new VelocityParticleInitializer<Entity>(-1, 1, -30, -70));
			particleSystemStars.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-3, 3, -3, -5));
			particleSystemStars.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			particleSystemStars.addParticleInitializer(new ExpireParticleInitializer<Entity>(20f));
			particleSystemStars.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f*resourceScaler, 0.65f*resourceScaler));
			particleSystemStars.addParticleInitializer(new RegisterXSwingEntityModifierInitializer<Entity>(10f, 0f, (float) Math.PI * 4, 3f, 30f, true));
			particleSystemStars.addParticleInitializer(new ColorParticleModifier<Entity>(0, 10, 1f, 1f, 1f , 0.8f, 0.4f, 0.9f ));
			particleSystemStars.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 1.0f, 0.0f));
			/*particleSystemi.addParticleInitializer(new IParticleInitializer<Entity>() {

				public void onInitializeParticle(Particle<Entity> pParticle) {
					Body body = PhysicsFactory.createCircleBody(activity.mPhysWorld, pParticle.getEntity(), BodyType.DynamicBody, activity.mCellFixtureDef);
					activity.mPhysWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body, true, true));
				}

			});*/
			particleSystemStars.setAlpha(1f);
			break;

		case 7:
			//A
			//Create Snow Particle
			try {
				mSnowParticleHearts = new AssetBitmapTexture(activity.getTextureManager(), 
						activity.getAssets(), particlePath, TextureOptions.BILINEAR);
			} catch (IOException e) {
				Debug.e(e);
			}
			mSnowParticleRegionHearts = TextureRegionFactory.extractFromTexture(
					mSnowParticleHearts);
			mSnowParticleHearts.load();
			mSnowParticleHearts.getTextureOptions().apply();
			//Create Snow Particle
			//load Particle system
			//mSnowParticleRegion.setTextureSize(100, 100);
			particleSystemHearts = new BatchedPseudoSpriteParticleSystem(
					new RectangleParticleEmitter(windowDimensions.x / 2, windowDimensions.y*1.1f, windowDimensions.x, 1),
					2, 7, 80, mSnowParticleRegionHearts, 
					vbom);
			particleSystemHearts.setCullingEnabled(true);
			particleSystemHearts.setTag(EntityTagManager.brightHeart);
			particleSystemHearts.setBlendFunction(GLES20.GL_ALPHA_BITS, GLES20.GL_ALWAYS);
			particleSystemHearts.addParticleInitializer(new VelocityParticleInitializer<Entity>(-2, 3, -30, -70));
			particleSystemHearts.addParticleInitializer(new AccelerationParticleInitializer<Entity>(-5, 6, -3, -5));
			if(isValentine){
				particleSystemHearts.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 360.0f));
			}
			else{
				particleSystemHearts.addParticleInitializer(new RotationParticleInitializer<Entity>(0.0f, 45.0f));
			}
			particleSystemHearts.addParticleInitializer(new ExpireParticleInitializer<Entity>(20f));
			particleSystemHearts.addParticleInitializer(new ScaleParticleInitializer<Entity>(0.2f*resourceScaler, 0.9f*resourceScaler));
			particleSystemHearts.addParticleInitializer(new RegisterXSwingEntityModifierInitializerSnow<Entity>(10f, 0f, (float) Math.PI * 4, 3f, 30f, true));
			if(!isValentine){
				float red = TimerManager.getInstance().randomiser.nextFloat();
				float green = TimerManager.getInstance().randomiser.nextFloat();
				float blue =  TimerManager.getInstance().randomiser.nextFloat();
				particleSystemHearts.addParticleModifier(new ColorParticleModifier<Entity>(0, 10, 1, 1, 1 , red, green, blue));
			}
			else
				particleSystemHearts.addParticleModifier(new AlphaParticleModifier<Entity>(6f, 10f, 0.95f, 0.5f));
			/*particleSystemi.addParticleInitializer(new IParticleInitializer<Entity>() {

				public void onInitializeParticle(Particle<Entity> pParticle) {
					Body body = PhysicsFactory.createCircleBody(activity.mPhysWorld, pParticle.getEntity(), BodyType.DynamicBody, activity.mCellFixtureDef);
					activity.mPhysWorld.registerPhysicsConnector(new PhysicsConnector(pParticle.getEntity(), body, true, true));
				}

			});*/
			particleSystemHearts.setAlpha(1f);
			break;

		}

		//particleSystemi.setScale(3f);
		//load Particle system

	}

	public Engine getEngine() {
		return engine;
	}

	/**
	 * 
	 */
	public void removeTouchedSpritePSInstances() {
		if (ParticleSwipeCreator.getInstance().particleSystem != null && ParticleSwipeCreator.getInstance().particleSystem.hasParent()) {
			ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					SceneManager.getInstance().getCurrentScene().detachChild(ParticleSwipeCreator.getInstance().particleSystem);
					ParticleSwipeCreator.getInstance().particleSystem = null;
				}
			});
		}
	}

	/**
	 * 
	 */
	public void unsubscribeRoom() {
		if(haveNetworkConnection()){
			Runnable run5 = new Runnable() {

				@Override
				public void run() {
					ConnectionManager.getInstance().autobahnConnectorPubSub.unSubscribeFromRoom();
					//Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
				}
			};
			//Though we run on UI thread , we spawn a new thread local n works in background else it will throw cannot use handlers error
			this.activity.runOnUiThread(run5); 
		}
	}

	public void unloadSceneParticleSystems(){
		particleSystem.clearEntityModifiers();
		particleSystem.clearUpdateHandlers();
		mSnowParticle.unload();
		particleSystem=null;

		particleSystemi.clearEntityModifiers();
		particleSystemi.clearUpdateHandlers();
		mSnowParticlei.unload();
		particleSystemi=null;

		particleSystemh.clearEntityModifiers();
		particleSystemh.clearUpdateHandlers();
		mSnowParticleh.unload();
		particleSystemh=null;

		particleSystemb.clearEntityModifiers();
		particleSystemb.clearUpdateHandlers();
		mSnowParticleb.unload();
		particleSystemb=null;

		particleSystemp.clearEntityModifiers();
		particleSystemp.clearUpdateHandlers();
		mSnowParticlep.unload();
		particleSystemp=null;
	}

	public void unloadShopSceneParticleSystems(){
		particleSystemStars.clearEntityModifiers();
		particleSystemStars.clearUpdateHandlers();
		mSnowParticleStars.unload();
		particleSystemStars=null;
	}

	public void unloadMenuSceneParticleSystems(){
		particleSystemHearts.clearEntityModifiers();
		particleSystemHearts.clearUpdateHandlers();
		mSnowParticleHearts.unload();
		particleSystemHearts=null;
	}


	//commented
/*
	public void createStartAppInterstials(){

		final Runnable run = new Runnable() {

			@Override
			public void run() {
				//resourcesManager.activity.startAppAd.showAd();
				activity.startAppAd.loadAd(new AdEventListener() {
					@Override
					public void onReceiveAd(Ad ad) {
						Log.d("STARTAPP", "Ad received "+ad.getErrorMessage());

						activity.startAppAd.showAd(new AdDisplayListener() {

							@Override
							public void adHidden(Ad ad) {
								Log.d("STARTAPP", "Ad hidden "+ad.getErrorMessage());
								showing = false;
							}

							@Override
							public void adDisplayed(Ad ad) {
								Log.d("STARTAPP", "Ad displayed "+ad.getErrorMessage());
								showing = true;
								//MusicService.resumeMusic();
							}

							@Override
							public void adClicked(Ad ad) {
								Log.d("STARTAPP", "Ad Clicked "+ad.getErrorMessage());
								//resourcesManager.showing = true;
							}


						});
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

					}
					@Override
					public void onFailedToReceiveAd(Ad ad) {
						Log.d("STARTAPP", "Ad not received "+ad.getErrorMessage());
					}
				});
			}
		};			//

		activity.runOnUiThread(run);
	}


	public void createStartAppSlider(){
		final Runnable runAds = new Runnable() {
			@Override
			public void run() {
				//StartApp Slider Ads - Start
				StartAppAd.showSlider(activity);
				//StartApp Ads - End

			}
		};
		//
		activity.runOnUiThread(runAds);
	}

	public void createStartNativeAds(){
		if(ResourcesManager.isAdsEnabled  && ResourcesManager.isAdsEnabledRefBased){
			final Runnable run = new Runnable() {

				private final int numberOfRoomsPerAd=6;

				private final int maxRoomsForAds= maxRooms/(100/36);



				@Override
				public void run() {

					try{


						// Declare Native Ad Preferences
						NativeAdPreferences nativePrefs = new NativeAdPreferences()
						.setAdsNumber(numOfAds)                // Load 3 Native Ads
						.setAutoBitmapDownload(false);   // Retrieve Images object


						// Declare Ad Callbacks Listener
						AdEventListener adListener = new AdEventListener() {     // Callback Listener
							@Override
							public void onReceiveAd(Ad arg0) {   
								try{
									// Native Ad received
									ArrayList<NativeAdDetails> ads = activity.startAppNativeAd.getNativeAds();    // get NativeAds list

									for (int i = 0; i < ads.size(); i++) {
										NativeAdDetails nativeAdDetails =  ads.get(i);
										//Log.e("Rooms set with Native Ads", nativeAdDetails.toString()+ " :"+i+ " No Of Rooms: "+rooms.size());
										if((i+1)*numberOfRoomsPerAd<=maxRoomsForAds){
											Log.e("Room Numbers For Ads", ""+(i+1)*numberOfRoomsPerAd);
											RoomProperties roomForAd=rooms.get((i+1)*numberOfRoomsPerAd);
											if(null!=roomForAd){
												Log.e("Room Ads Not Null", "Ads Not Null "+(i+1)*numberOfRoomsPerAd);

												setRoomsWithAdImageUrl(nativeAdDetails,roomForAd);
											}
										}
									}
								}
								catch(Exception e){
									Log.e("MyApplication onReceiveAd", "Error while Retriving Ads " + e.getMessage());

								}
							}



							@Override
							public void onFailedToReceiveAd(Ad arg0) {
								// Native Ad failed to receive
								Log.e("MyApplication", "Error while loading Ad ");
								try{
									Log.e("StartApp", arg0.getErrorMessage());
								}
								catch(Exception e){
									Log.e("MyApplication on Failed To Receive Ad", "Error while Retriving error " + e.getMessage());

								}
							}
						};

						// Load Native Ads
						activity.startAppNativeAd.loadAd(nativePrefs, adListener);
					}
					catch(IllegalArgumentException e){
						Log.e("Room Ads Error", "Illegal Argument Exception Outer Catch");
					}
					catch(Exception e){
						Log.e("Room Ads Error", "Some exception  Outer Catch"+e.getMessage());
					}
				}

			};			//
			try{
				activity.runOnUiThread(run);
			}
			catch(Exception e){
				Log.e("MyApplication", "Error while loading Ad Outer Catch");

			}
		}
	}

	private void setRoomsWithAdImageUrl(
			NativeAdDetails nativeAdDetails,final RoomProperties roomProperties) {
		roomProperties.setAdsDetail(nativeAdDetails);

		AsyncTask<RoomProperties, Void, TextureRegion> task=new FetchAdsImageFromServerTask();
		tasks.add((FetchAdsImageFromServerTask)task);
		task.execute(roomProperties);



	}*/

	public boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	/**
	 * 
	 */
	public void cancelAsyncAdLoadTasks() {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(!tasks.isEmpty()){
					for (FetchAdsImageFromServerTask  task: ResourcesManager.getInstance().tasks) {
						if(null!=task && task.getStatus()==AsyncTask.Status.RUNNING || task.getStatus()==AsyncTask.Status.PENDING ){
							task.cancel(true);
							if(null!=task.client){
								try {
									task.client.close();
								}
								catch(Throwable e){
									e.printStackTrace();
								}
							}
						}
					}
					tasks.clear();
				}
			}
		});

	}

	/**
	 * 
	 */
	public void unloadHostSceneResources() {
		// TODO Auto-generated method stub
		unloadSceneParticleSystems();
		this.mBitmapFont.unload();
		this.gameHostTextureAtlas.unload();
	}

	/**
	 * 
	 */
	public void unloadShopSceneResources() {
		// TODO Auto-generated method stub
		unloadShopSceneParticleSystems();
		this.shopTextureAtlas.unload();
	}



	/**
	 * 
	 */
	public void unloadOptionResources() {

		if(null!=themeManager.optionTextureAtlas){
			themeManager.optionTextureAtlas.unload();
		}
		//unload thumbnails
		themeManager.themeSelection= null;

		themeManager.musicOnOffText=null;
		themeManager.tutorialOnOffText=null;
		themeManager.startUpAnimationOnOffText = null;

		themeManager.optionTextureAtlas = null;

		themeManager.themeThumbnailOneCreative = null;
		themeManager.themeThumbnailTwoYellow =  null;
		themeManager.howToPlay = null;


		this.musicOnOff = null;
		tutorialOnOff = null;

	}

	/**
	 * 
	 */
	public void unloadGuessSceneResources() {
		// TODO Auto-generated method stub
		unloadSceneParticleSystems();
		this.mBitmapFont.unload();
		this.gameGuessTextureAtlas.unload();
	}

	/**
	 * @param entity honeycombAnim
	 */
	public void detachInUpdateThread(final IEntity entity) {
		// TODO Auto-generated method stub
		activity.runOnUpdateThread(new Runnable() {

			@Override
			public void run() {
				entity.detachSelf();
			}
		});
	}

	//For FB
	public void joinRandomDailyChallengeRoom(final int randomRoomIndex) {
		ResourcesManager.isFaceBookRequestUnAddressed=false;
		ResourcesManager.getInstance().activity.destroyLeadBoltAd();
		if(null!=ResourcesManager.getInstance().host && null!= ResourcesManager.getInstance().host.getCharSet()){
			ResourcesManager.getInstance().host.getCharSet().clear();
		}
		if(null!=ResourcesManager.getInstance().addedPositions){
			ResourcesManager.getInstance().addedPositions.clear();
		}
		//Play byte 1
		if(randomRoomIndex>6000)
			ConnectionManager.suffixEventMarker=ConnectionManager.USEREVENTCHALLENGE;
		else{
			ConnectionManager.suffixEventMarker=ConnectionManager.USEREVENTNORMAL;
		}
		final TimerManager timerManager=TimerManager.getInstance();
		timerManager.randomiser=new Random(new Date().getTime());

		final RoomStream randomGuessRoomDetails= new RoomStream();
		UserDO participant = StorageManager.getInstance().getUser();
		if(null==randomGuessRoomDetails.getParticipants()){
			randomGuessRoomDetails.setParticipants(new ArrayList<UserDO>());
		}

		if(!randomGuessRoomDetails.getParticipants().contains(participant)){
			randomGuessRoomDetails.getParticipants().add(participant);
		}
		//GETS A ROOM in closed interval [6001 to 12000]

		StorageManager.getInstance().getUser().setHostedGuessedRoomIndex(randomRoomIndex);
		ConnectionManager.mPathRoomNumberSuffix=randomRoomIndex;
		ResourcesManager.setTutorialModeEnabled(ConnectionManager.mPathRoomNumberSuffix);

		RoomProperties room = new RoomProperties(0, 0, 0, 0, randomRoomIndex, ResourcesManager.getInstance().vbom);
		createRoomForRandomPlay(randomGuessRoomDetails,room,randomRoomIndex);

		Runnable run5 = new Runnable() {

			@Override
			public void run() {
				try {

					ResourcesManager.getInstance().guessImageclicked =true;
					ResourcesManager.getInstance().hostImageclicked =false;
					randomGuessRoomDetails.setIndex(randomRoomIndex);
					randomGuessRoomDetails.setRoomState(RoomProperties.STATE_WAITING);

					if(ConnectionManager.getInstance().mConnection.isConnected()){
						//SoundManager.getInstance().createWhipSound();

						ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(randomGuessRoomDetails);
						Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
					}
					else{
						ConnectionManager.getInstance().prepareConnection();
						ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(randomGuessRoomDetails);
					}
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
		ResourcesManager.getInstance().activity.runOnUiThread(run5); 

		StorageManager.getInstance().getUser().setHoster(false);
		if(THEMES.BLUEMAGIC.toString().equals(ThemeManager.getInstance().getThemePreferences().getThemeSelection())){
			SoundManager.changeMusic(MusicPlayed.BEEMUSIC);
		}
		else{
			SoundManager.changeMusic(MusicPlayed.FASTMARCH);
		}
		SceneManager.getInstance().createWaitingScene();
		if(!IMService.isGameFromServiceNotify){
			WaitingScene.waitTimeElapsed=25;
		}
		else{
			IMService.isGameFromServiceNotify = false;
		}
		((WaitingScene)SceneManager.getInstance().waitingScene).minimumNumOfPlayers =1;
		//SceneManager.getInstance().createGuessScene();
	}
	private void createRoomForRandomPlay(RoomStream roomFromServer, RoomProperties r,int scrollIndex) {
		//Add rooms to Map
		ResourcesManager.rooms.put(scrollIndex, r);
		r.setIndex(scrollIndex);
		r.setNoOfParticipants(1);
		r.setRoomHostedBy("#WORDCHALLENGE#");
		/*Set<Entry<String, RoomStream>> entrySet = ResourcesManager.roomsState.entrySet();
		if(null != entrySet)
		for (Entry<String, RoomStream> entrySetValue : entrySet) {
			Integer roomIndex=Integer.parseInt(entrySetValue.getKey());
			RoomStream room = entrySetValue.getValue();
		}*/
		//Copy data Fetched from server to Local Room r
		if(null!=roomFromServer){
			r.setRoomState(roomFromServer.getRoomState());
			r.setRoomHostedBy(roomFromServer.getRoomHostedBy());
			r.setNoOfParticipants(roomFromServer.getNoOfParticipants());
		}
	}


	/**
	 * Return the window corresponding to the id, if it exists in cache. The
	 * window will not be created with
	 * {@link #(int, )}. This means the returned
	 * value will be null if the window is not shown or hidden.
	 * 
	 * @param id
	 *            The id of the window.
	 * @return The window if it is shown/hidden, or null if it is closed.
	 */
	public final Window getWindow(int id) {
		if(null!=StandOutWindow.sWindowCache){
			return StandOutWindow.sWindowCache.getCache(id, WidgetsWindow.class);
		}
		return null;
	}


	//For Adding To Player's Slider in Game
	public synchronized void addUserChunk(final UserDO user) {
		synchronized (ResourcesManager.getInstance().users) {

			if(!hasUser(user)){
				StandOutWindow.show(ResourcesManager.getInstance().activity, WidgetsWindow.class, 0);
				ChatManager.chatState=1;
				ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						Message mUserAdded = new Message(true, "<b>"+user.getDisplayName()+"</b>"+ " joined the room");
						ChatManager.getInstance().addNewMessage(mUserAdded);
						//Window window = getWindow(0);

						/*if (window == null) {
							String errorText = String.format(Locale.US,
									"%s received data but Chat Window  is not open.",
									"WORD Chat");
							//Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
							return;
						}*/

						//StandOutWindow.hide(ResourcesManager.getInstance().activity, WidgetsWindow.class, 0);
					}
				});

				UserChunk p = UserChunkFactory.getInstance().next(user);
				if(SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.slideWrapper)!=null){
					Log.d(" WRAPPER POSITION BEFORE: ", " WRAPPER POSITION BEFORE Position X: "+SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.slideWrapper).getX() +  "Position Y: "+SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.slideWrapper).getY());

					//SceneManager.getInstance().getCurrentScene().detachChild(EntityTagManager.slideWrapper);
					for (Iterator iterator = ResourcesManager.getInstance().users.iterator(); iterator
							.hasNext();) {
						final UserChunk userChunk = (UserChunk) iterator.next();
						/*if(userChunk.hasParent()){
									userChunk.detachSelf();
						}*/
						Log.d("userChunk Old", "userChunk Old Position X: " + userChunk.getX() + " Position Y: "+ userChunk.getY()  );
					}
					Log.d("Slider Creation", "numberOfUsersJoined: "+ResourcesManager.getInstance().users);
					((SlideUsersChildScene)(SceneManager.getInstance().getCurrentScene().getChildByTag(EntityTagManager.slideWrapper))).createSceneUpdate();
					/*for (Iterator iterator = ResourcesManager.getInstance().users.iterator(); iterator
							.hasNext();) {
						UserChunk userChunk = (UserChunk) iterator.next();
						Log.d("userChunk Old", "userChunk New Position X: " + userChunk.getX() + " Position Y: "+ userChunk.getY()  );
					}*/
				}
			}
		}	
	}

	//For Adding To Player's Slider in Game
	/**
	 * @param user
	 * @return
	 */
	private static synchronized boolean hasUser(UserDO user) {
		List<UserChunk> userChunks=ResourcesManager.getInstance().users;
		if(null==userChunks){
			ResourcesManager.getInstance().users = new ArrayList<UserChunk>();
			userChunks=ResourcesManager.getInstance().users;
		}
		for (Iterator<UserChunk> iterator = userChunks.iterator(); iterator.hasNext();) {
			UserChunk chunk = iterator.next();
			if(null!=chunk){
				if(null!=chunk.getUsername() && chunk.getUsername().equalsIgnoreCase(user.getUserName())){
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isDailyLogin(){
		long milliseconds= Calendar.getInstance().getTimeInMillis()-lastLogin;
		long days =(int) (milliseconds / (1000*60*60*24));
		if(days>1){
			return true;

		}
		return false;
	}


	/**
	 * Chat Specific Info follows 
	 */
	public void closeChatWindow() {
		try{
			StandOutWindow.closeAll(activity, WidgetsWindow.class);
			ChatManager.chatState=0;
			if(null!=chat){
				chat.setCurrentTileIndex(Chat.CHAT_INV_ID);
				chat.setAlpha(1f);
			}
		}
		catch(Exception e){

		}

	}
	/**
	 * @param mPathRoomNumberSuffix
	 */
	public static void setTutorialModeEnabled(int mPathRoomNumberSuffix) {
		if(mPathRoomNumberSuffix<25 || mPathRoomNumberSuffix>6000){
			isTutorialMode = true;
		}
		else{
			isTutorialMode = false;
		}
	}

	/**
	 * @param appendLength
	 * @return
	 */
	public static String appendSpace(int appendLength) {
		StringBuffer space=new StringBuffer(appendLength);
		for (int i = 0; i < appendLength; i++) {
			space.append(" ");
		}
		return space.toString();
	}

	/**
	 * 
	 */
	public void clearAndUnloadRooms() {
		if(null!=rooms){
			for (int i = 0; i < rooms.size(); i++) {
				RoomProperties room = rooms.get(i);
				if(null!=room)
					room.unloadAds();
			}
			//rooms.clear();
		}
	}



}
