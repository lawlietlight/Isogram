/**
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.bullrun;

import android.util.Log;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.BaseMiniGameScene;
import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.ResourceManagerForMiniGame;
import com.efficientsciences.cowsandbulls.wordwars.managers.Constants;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.util.Iterator;
import java.util.LinkedList;

public class MiniGameSceneBull extends BaseMiniGameScene implements IOnSceneTouchListener,
ContactListener {

	private static final long TIME_TO_RESSURECTION = 200;
	PhysicsWorld physics;
	private static boolean isTouchedFlag = false;
	protected Sprite backButton = null;

	enum State {
		NEW, PAUSED, PLAY, DEAD, AFTERLIFE;
	}

	Text infoText;
	Text scoreText;
	Sprite ground;
	public AnimatedSprite  bull;
	Body bullBody;
	ParallaxBackground pb;

	State state = State.NEW;
	State lastState = state;
	long timestamp = 0;

	private int score = 0;
	private boolean scored;

	LinkedList<Stick> sticks = new LinkedList<Stick>();

	protected ResourceManagerForMiniGame res = ResourceManagerForMiniGame
			.getInstance();
	protected VertexBufferObjectManager vbom = ResourceManagerForMiniGame
			.getInstance().vbom;
	private boolean backButtonDisplayed;
	private State PREVSTATE;
	protected boolean isBullAnimationInProgress;
	private boolean isTouchJumpReadyForNextJump = true;

	public MiniGameSceneBull() {
		physics = new PhysicsWorld(new Vector2(0, 0), true);
		physics.setContactListener(this);
		StickFactoryForBullRun.getInstance().create(physics);

		createBackground();

		createBounds();
		createActor();
		createText();

		res.camera.setChaseEntity(bull);


		sortChildren();
		setOnSceneTouchListener(this);

		registerUpdateHandler(physics);
		/*DebugRenderer debug = new DebugRenderer(physics, vbom);
		this.attachChild(debug);*/
	}

	private void createText() {
		HUD hud = new HUD();
		res.camera.setHUD(hud);
		infoText = new Text(Constants.CW / 2, (Constants.CH / 2) - 75
				* ResourcesManager.resourceScaler, res.font,
				"12345678901234567890", vbom);
		hud.attachChild(infoText);

		scoreText = new Text(Constants.CW / 2, (Constants.CH / 2) + 100
				* ResourcesManager.resourceScaler, res.font,
				"12345678901234567890", vbom);
		hud.attachChild(scoreText);

		attachBackButton(hud);
		/*
		 * Sprite banner = new Sprite(0, Constants.CH, res.bannerRegion, vbom) {
		 * 
		 * @Override public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
		 * float pTouchAreaLocalX, float pTouchAreaLocalY) { if
		 * (pSceneTouchEvent.isActionDown()) { res.activity.gotoPlayStore(); }
		 * return true; }
		 * 
		 * }; banner.setAnchorCenter(0, 1); hud.registerTouchArea(banner);
		 */
		// hud.attachChild(banner);

	}

	private void createBounds() {
		float bigNumber = 999999; // i dunno, just a big number
		res.repeatingGroundRegion.setTextureWidth(bigNumber);

		int groundPos = -450;
		String groundFixtureData= Constants.BODY_WALL;
		if(!ResourceManagerForMiniGame.isBeeFly){
			groundPos = -150;
			groundFixtureData= Constants.BODY_GROUND_BULL;
		}

		ground = new Sprite(0, groundPos, res.repeatingGroundRegion, vbom);
		ground.setAnchorCenter(0, 0);
		ground.setZIndex(10);
		attachChild(ground);

		Body groundBody = PhysicsFactory.createBoxBody(physics, ground,
				BodyType.StaticBody, Constants.WALL_FIXTURE);

		groundBody.setUserData(groundFixtureData);

		// just to limit the movement at the top
		@SuppressWarnings("unused")
		Body ceillingBody = PhysicsFactory.createBoxBody(physics,
				bigNumber / 2, 820, bigNumber, 20, BodyType.StaticBody,
				Constants.CEILLING_FIXTURE);
	}

	private void createActor() {
		bull = new AnimatedSprite(200, ground.getY(), res.beeOrBullRegion, vbom);
		//bull.setAnchorCenter(0.5f, 0.5f);
		bull.setScale(0.2f);
		bull.setFlippedHorizontal(true);
		bull.setScale(0.2f);
		bull.animate(150);
		bull.setScale(0.2f);
		isBullAnimationInProgress=true;

		bull.setZIndex(999);
		bull.setAnchorCenter(0.5f, 0.544f);
		bull.setScaleX(0.2f);
		Log.e("ground.getY()", "ground.getY()" + ground.getY());
		Log.e("bull.getY()", "bull.getY()" + bull.getY());
		bull.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				bull.setScale(0.2f);
				if (bull.getY() > ground.getY()+ground.getHeight()+50) {
					bull.stopAnimation(2);
					isBullAnimationInProgress=false;
				} else {
					if(!isBullAnimationInProgress){
						isBullAnimationInProgress =true;
						bull.animate(150);
					}
				}
			}

			@Override
			public void reset() {
			}
		});
		bullBody = PhysicsFactory.createCircleBody(physics, bull,
				BodyType.DynamicBody, Constants.DANDELION_FIXTURE);
		bullBody.setFixedRotation(true);
		bullBody.setUserData(Constants.BODY_ACTOR);
		physics.registerPhysicsConnector(new PhysicsConnector(bull,
				bullBody,true,true));
		attachChild(bull);
	}

	private void createBackground() {
		pb = new ParallaxBackground(255 / 255f, 180 / 255f, 59 / 255f);
		Entity clouds = new Rectangle(0, 0, 500, 300, vbom);
		clouds.setAnchorCenter(0, 0);
		clouds.setAlpha(0f);
		clouds.attachChild(new Sprite(175, 350, res.cloudRegion, vbom));
		clouds.attachChild(new Sprite(300, 200, res.cloudRegion, vbom));

		clouds.attachChild(new Sprite(10, 450, res.cloudRegion, vbom));
		clouds.attachChild(new Sprite(500, 480, res.cloudRegion, vbom));

		ParallaxEntity pe = new ParallaxEntity(-0.2f, clouds);
		pb.attachParallaxEntity(pe);
		setBackground(pb);
	}

	@Override
	public void reset() {
		super.reset();
		ResourceManagerForMiniGame.SPEED_X = 8.0f;
		ResourceManagerForMiniGame.SPEED_Y = 9.8f;
		physics.setGravity(new Vector2(0, 0));

		Iterator<Stick> pi = sticks.iterator();
		while (pi.hasNext()) {
			Stick p = pi.next();
			float temp = p.rand.nextFloat();
			if (temp > 0.9) {
				p.shift = 90 * temp;
			}
			StickFactoryForBullRun.getInstance().recycle(p);
			pi.remove();
		}

		StickFactoryForBullRun.getInstance().reset();

		bullBody.setTransform(
				400 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				200 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

		addStick();
		addStick();
		addStick();

		score = 0;

		infoText.setText("Try Your Luck With Bull");
		infoText.setVisible(true);
		String plural = "";
		if (res.activity.getHighScore() > 1) {
			plural = "s";
		}
		scoreText.setText("Last Earned Coin" + plural + ": "
				+ res.activity.getHighScore());
		infoText.setVisible(true);

		sortChildren();

		unregisterUpdateHandler(physics);
		physics.onUpdate(0);

		state = State.NEW;
	}

	private void addStick() {
		Stick p = StickFactoryForBullRun.getInstance().next();
		sticks.add(p);
		attachIfNotAttached(p);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		Debug.e("IS RUN:" + res.engine.isRunning() + " ev: "
				+ pSceneTouchEvent.getAction());
		if (backButton.contains(pSceneTouchEvent.getX(),
				pSceneTouchEvent.getY())) {
			backButton.onAreaTouched(pSceneTouchEvent, pSceneTouchEvent.getX(),
					pSceneTouchEvent.getY());
		}
		if (pSceneTouchEvent.isActionUp()) {
			Vector2 v = bullBody.getLinearVelocity();
			v.x = ResourceManagerForMiniGame.SPEED_X*1.20f;
			v.y = -7;
			bullBody.setLinearVelocity(v);
			isTouchedFlag = false;
		}
		else if ((pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionDown()) && !isTouchedFlag) {
			if (state == State.PAUSED) {
				if (lastState != State.NEW) {
					registerUpdateHandler(physics);
				}
				state = lastState;
				Debug.d("->" + state);
			} else if (state == State.NEW) {
				registerUpdateHandler(physics);
				PREVSTATE = State.AFTERLIFE;
				state = State.PLAY;
				Debug.d("->PLAY");
				physics.setGravity(new Vector2(0, Constants.GRAVITY));
				bullBody.setLinearVelocity(new Vector2(ResourceManagerForMiniGame.SPEED_X*1.20f,
						0));
				scoreText.setText("0");
				infoText.setVisible(false);
			} else if (state == State.DEAD) {
				// don't touch the dead!

			} else if (state == State.AFTERLIFE) {
				reset();
				state = State.NEW;
				Debug.d("->NEW");
			} else {
				Vector2 v = bullBody.getLinearVelocity();
				v.x = ResourceManagerForMiniGame.SPEED_X*1.20f;
				isTouchedFlag = true;
				// v.y = ResourceManagerForMiniGame.SPEED_Y;
				bullBody.setLinearVelocity(v);
				Debug.d("TAP!");
				res.sndFly.play();
			}
		}
		/*else{
			Vector2 v = bullBody.getLinearVelocity();
			v.x = ResourceManagerForMiniGame.SPEED_X;
			// v.y = ResourceManagerForMiniGame.SPEED_Y;
			bullBody.setLinearVelocity(v);
			//isTouchedFlag = false;
			//isTouchedFlagForMove =true; //Should Already be in True state
		}*/

		return false;
	}

	@Override
	public void resume() {
		Debug.d("Game resumed");
	}

	@Override
	public void pause() {
		unregisterUpdateHandler(physics);
		lastState = state;
		state = State.PAUSED;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if (state == State.DEAD || state == State.AFTERLIFE) {
			Vector2 v = bullBody.getLinearVelocity();
			v.y = Constants.GRAVITY;
			v.x = 0;
			bullBody.setLinearVelocity(v);
		} else if (isTouchedFlag && isTouchJumpReadyForNextJump) {
			Vector2 v = bullBody.getLinearVelocity();
			// v.y=7;
			v.x = ResourceManagerForMiniGame.SPEED_X*1.5f;
			v.y = ResourceManagerForMiniGame.SPEED_Y*1.62f;
			isTouchJumpReadyForNextJump = false;
			bullBody.setLinearVelocity(v);
		}
		else if (isTouchedFlag) {
			Vector2 v = bullBody.getLinearVelocity();
			// v.y=7;
			v.x = ResourceManagerForMiniGame.SPEED_X*1.35f;
			bullBody.setLinearVelocity(v);
		}
		else{
			Vector2 v = bullBody.getLinearVelocity();
			// v.y=7;
			v.x = 7;
			bullBody.setLinearVelocity(v);
		}

		pb.setParallaxValue(res.camera.getCenterX());
		if (scored) {
			addStick();
			sortChildren();
			scored = false;
			score++;
			switch(score){
			case 0:
				ResourceManagerForMiniGame.SPEED_X = 8.0f;
				break;
			case 25:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.5f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.25f;

				break;
			case 50:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.5f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.25f;
				break;
			case 75:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.5f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.25f;
				break;
			case 100:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.75f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.25f;
				break;
			case 125:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.75f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.25f;
				break;
			default:
				if(score>130){
					float offSet= (0.001f * score);

					if((ResourceManagerForMiniGame.SPEED_X%10)<5){
						offSet = -offSet;
					}
					else{
						offSet = offSet + 0.001f;
					}
					ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + offSet;

					ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + offSet;
				}
				break;
			}
			scoreText.setText(String.valueOf(score));
		}

		// if first stick is out of the screen, delete it
		if (!sticks.isEmpty()) {
			Stick fp = sticks.getFirst();
			if (fp.getX() + fp.getWidth() < res.camera.getXMin()) {
				StickFactoryForBullRun.getInstance().recycle(fp);
				sticks.remove();
			}
		}

		if (state == State.DEAD
				&& timestamp + TIME_TO_RESSURECTION < System
				.currentTimeMillis()) {
			state = State.AFTERLIFE;
			Debug.d("->AFTERLIFE");
		}
	}

	private void attachIfNotAttached(Stick p) {
		if (!p.hasParent()) {
			attachChild(p);
		}

	}

	@Override
	public void beginContact(Contact contact) {
		if (Constants.BODY_WALL.equals(contact.getFixtureA().getBody()
				.getUserData())
				|| Constants.BODY_WALL.equals(contact.getFixtureB().getBody()
						.getUserData())) {
			isTouchJumpReadyForNextJump = true;
			state = State.AFTERLIFE;
			PREVSTATE = State.DEAD;
			if (StorageManager.getInstance().getUser() != null
					&& state == State.AFTERLIFE) {
				if (PREVSTATE == State.DEAD) {
					StorageManager
					.getInstance()
					.getUser()
					.setNumOfCoins(
							StorageManager.getInstance().getUser()
							.getNumOfCoins()
							+ score);
					StorageManager.getInstance().saveUserBeesBullsAndCoins(
							StorageManager.getInstance().getUser());
					if (score > 1) {
						SoundManager.getInstance().createAddCoinsForShopSound();
					} else if (score == 1) {
						SoundManager.getInstance().createAddCoinMediumSound();
					}
				} else {
					PREVSTATE = State.AFTERLIFE;
				}
				ResourceManagerForMiniGame.getInstance().activity.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						infoText.setText("Tap To Start Again");
						infoText.setVisible(true);
						String plural = "";
						if (res.activity.getHighScore() > 1) {
							plural = "s";
						}
						scoreText.setText("You Just Earned "
								+ res.activity.getHighScore() + "  Coin" + plural);
						infoText.setVisible(true);

						sortChildren();
					}
				});

			} else {
				PREVSTATE = State.DEAD;
			}
			state = State.DEAD;
			Debug.d("->DEAD");
			res.sndFail.play();

			// if (score > res.activity.getHighScore()) {
			res.activity.setHighScore(score);
			// }

			timestamp = System.currentTimeMillis();
			bullBody.setLinearVelocity(0, 0);
			for (Stick p : sticks) {
				p.getStickDownBody().setActive(false);
			}
		}
		else if(Constants.BODY_GROUND_BULL.equals(contact.getFixtureA().getBody()
				.getUserData())
				|| Constants.BODY_GROUND_BULL.equals(contact.getFixtureB().getBody()
						.getUserData())){
			isTouchedFlag = false;
			isTouchJumpReadyForNextJump = true;
			Vector2 v = bullBody.getLinearVelocity();
			v.x = ResourceManagerForMiniGame.SPEED_X*1.20f;
			v.y = 0;
			bullBody.setLinearVelocity(v);
		}
		else if(PREVSTATE != State.DEAD &&  state != State.AFTERLIFE){
			//isTouchJumpReadyForNextJump = true;
			Vector2 v = bullBody.getLinearVelocity();
			v.x = ResourceManagerForMiniGame.SPEED_X*1.20f;
			v.y = 0;
			bullBody.setLinearVelocity(v);
		}

	}

	@Override
	public void endContact(Contact contact) {
		if (Constants.BODY_SENSOR.equals(contact.getFixtureA().getBody()
				.getUserData())
				|| Constants.BODY_SENSOR.equals(contact.getFixtureB().getBody()
						.getUserData())) {
			scored = true;
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	protected Sprite attachBackButton(HUD hud) {
		backButton = new Sprite(5 * ResourcesManager.resourceScaler,
				5 * ResourcesManager.resourceScaler,
				ResourceManagerForMiniGame.chocobackbuttonregion, vbom) {
			boolean areaClickedDown;

			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent,
					final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					areaClickedDown = true;
					return true;
				} else if ((pSceneTouchEvent.isActionUp() || pSceneTouchEvent
						.isActionMove()) && areaClickedDown) {
					Debug.e("How To Play Scene Back Key Pressed");
					areaClickedDown = false;

					ResourceManagerForMiniGame.getInstance().activity.finish();
					if (!ResourcesManager.getInstance().showing
							&& null != MusicService.mPlayer
							&& MusicService.mPlayer.isPlaying()) {
						SoundManager.getInstance().changeToMenuMusic();
						// SoundManager.getInstance().doUnbindService(this);
					}
					return true;
				}
				return false;
			}
		};
		backButton.setAnchorCenter(0, 0);
		backButton.setScale(0.5f);
		backButton.setTag(EntityTagManager.backButton);
		backButtonDisplayed = true;

		if (!backButton.hasParent()) {
			hud.attachChild(backButton);
		}
		this.registerTouchArea(hud);
		hud.registerTouchArea(backButton);
		return backButton;
	}

}
