/**
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics.minigame;


import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePack;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackLoader;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackTextureRegionLibrary;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.TexturePackerTextureRegion;
import org.andengine.extension.texturepacker.opengl.texture.util.texturepacker.exception.TexturePackParseException;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.bitmap.BitmapTextureFormat;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.adt.color.ColorUtils;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;

import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;

public class ResourceManagerForMiniGame {
	private static final ResourceManagerForMiniGame INSTANCE = new ResourceManagerForMiniGame();

	//font
	public Font font;
	public static float SPEED_X = 8; 
	public static float SPEED_Y = 10f;

	//common objects
	public EarnCoinsMiniGame activity;
	public Engine engine;
	public Camera camera;
	public VertexBufferObjectManager vbom;

	//gfx
	private BitmapTextureAtlas repeatingGroundAtlas;

	public TextureRegion repeatingGroundRegion;

	private BuildableBitmapTextureAtlas gameObjectsAtlas;

	public TextureRegion cloudRegion;
	public ITiledTextureRegion beeOrBullRegion;
	public TextureRegion pillarRegion;
	public static ITextureRegion chocobackbuttonregion;
	//public TextureRegion bannerRegion;
	private TexturePackTextureRegionLibrary texturePackForBullrunLibrary;
	private TexturePack texturePackForBullRun;
	public static boolean isBeeFly;

	//sfx
	public Sound sndFly;
	public Sound sndFail;


	private ResourceManagerForMiniGame() {}

	public static ResourceManagerForMiniGame getInstance() {
		return INSTANCE;
	}

	public void create(EarnCoinsMiniGame activity, Engine engine, Camera camera, VertexBufferObjectManager vbom) {
		this.activity = activity;
		this.engine = engine;
		this.camera = camera;
		this.vbom = vbom;
	}

	public void loadFont() {
		font = FontFactory.createStroke(activity.getFontManager(), activity.getTextureManager(), 256, 256, Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD), 35,
				true, ColorUtils.convertRGBAToABGRPackedInt(123/255, 179/255, 26/255, 1), 2, Color.WHITE_ABGR_PACKED_INT);
		font.load();
	}

	public void unloadFont() {
		font.unload();
	}

	//splash
	public void loadGameResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/minigame/");	

		repeatingGroundAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		repeatingGroundRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(repeatingGroundAtlas, activity, "ground.png", 0, 0);
		repeatingGroundAtlas.load();

		gameObjectsAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 
				1024, 1024, BitmapTextureFormat.RGBA_8888, TextureOptions.BILINEAR);

		cloudRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameObjectsAtlas, activity.getAssets(), "cloud.png");

		if(ResourceManagerForMiniGame.isBeeFly){
			pillarRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
					gameObjectsAtlas, activity.getAssets(), "pillar.png");


			beeOrBullRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
					gameObjectsAtlas, activity.getAssets(), "dandelion.png", 2, 1);
		}
		else{
			try{
				texturePackForBullRun = new TexturePackLoader(activity.getTextureManager(), "gfx/gametheme/").loadFromAsset(activity.getAssets(), "bull.xml");
				texturePackForBullRun.loadTexture();
				texturePackForBullrunLibrary = texturePackForBullRun.getTexturePackTextureRegionLibrary();

			} 
			catch (final TexturePackParseException e) 
			{
				Debug.e(e);
			}
			//Bull Run
			TexturePackerTextureRegion[] objBullRun = new TexturePackerTextureRegion[this.texturePackForBullrunLibrary.getIDMapping().size()];

			for (int i = 0; i < this.texturePackForBullrunLibrary.getIDMapping().size(); i++) {
				objBullRun[i] = this.texturePackForBullrunLibrary.get(i);
			}

			TiledTextureRegion bullTextureAnim = new TiledTextureRegion(texturePackForBullRun.getTexture(),
					objBullRun);

			beeOrBullRegion = bullTextureAnim;
			texturePackForBullRun.getTexture().load();

		}

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/menu/");

		chocobackbuttonregion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameObjectsAtlas, activity, EntityTagManager.chocoBackButton);


		/*bannerRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				gameObjectsAtlas, activity.getAssets(), "banner.png");*/

		try {
			gameObjectsAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(2, 0, 2));
			gameObjectsAtlas.load();

		} catch (final TextureAtlasBuilderException e) {
			throw new RuntimeException("Error while loading Splash textures", e);
		}		

		try {
			sndFly = SoundFactory.createSoundFromAsset(activity.getEngine().getSoundManager(), activity, "sfx/fly.wav");
			sndFail = SoundFactory.createSoundFromAsset(activity.getEngine().getSoundManager(), activity, "sfx/fail.wav");
		} catch (Exception e) {
			throw new RuntimeException("Error while loading sounds", e);
		} 
	}		
}
