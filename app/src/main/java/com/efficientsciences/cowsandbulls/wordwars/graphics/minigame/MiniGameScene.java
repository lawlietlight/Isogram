/**
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics.minigame;

import java.util.Iterator;
import java.util.LinkedList;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.entity.text.Text;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.content.Intent;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.efficientsciences.cowsandbulls.wordwars.MainActivity;
import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.managers.Constants;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;


public class MiniGameScene extends BaseMiniGameScene implements IOnSceneTouchListener, ContactListener {

	private static final long TIME_TO_RESSURECTION = 200;
	PhysicsWorld physics;
	private static boolean isTouchedFlag=false;
	protected Sprite backButton = null;
	enum State {
		NEW, PAUSED, PLAY, DEAD, AFTERLIFE;
	}

	Text infoText;
	Text scoreText;

	TiledSprite dandelion;
	Body dandelionBody;
	ParallaxBackground pb;

	State state = State.NEW;
	State lastState = state;
	long timestamp = 0;

	private int score = 0;
	private boolean scored;

	LinkedList<Pillar> pillars = new LinkedList<Pillar>();

	protected ResourceManagerForMiniGame res = ResourceManagerForMiniGame.getInstance();
	protected VertexBufferObjectManager vbom = ResourceManagerForMiniGame.getInstance().vbom;
	private boolean backButtonDisplayed;
	private State PREVSTATE;



	public MiniGameScene() {
		physics = new PhysicsWorld(new Vector2(0, 0), true);
		physics.setContactListener(this);
		PillarFactory.getInstance().create(physics);

		createBackground();
		createActor();
		createBounds();

		createText();

		res.camera.setChaseEntity(dandelion);

		sortChildren();
		setOnSceneTouchListener(this);

		registerUpdateHandler(physics);

	}

	private void createText() {
		HUD hud = new HUD();
		res.camera.setHUD(hud);
		infoText = new Text(Constants.CW / 2 , (Constants.CH / 2) - 75 * ResourcesManager.resourceScaler, res.font, "12345678901234567890", vbom);
		hud.attachChild(infoText);

		scoreText = new Text(Constants.CW / 2 , (Constants.CH / 2) + 100* ResourcesManager.resourceScaler, res.font, "12345678901234567890", vbom);
		hud.attachChild(scoreText);

		attachBackButton(hud);
		/*Sprite banner = new Sprite(0, Constants.CH, res.bannerRegion, vbom) {

			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					res.activity.gotoPlayStore();
				}
				return true;
			}

		};
		banner.setAnchorCenter(0, 1);
		hud.registerTouchArea(banner);*/
		//hud.attachChild(banner);

	}

	private void createBounds() {
		float bigNumber = 999999; // i dunno, just a big number
		res.repeatingGroundRegion.setTextureWidth(bigNumber);
		Sprite ground = new Sprite(0, -450, res.repeatingGroundRegion, vbom);
		ground.setAnchorCenter(0, 0);
		ground.setZIndex(10);
		attachChild(ground);

		Body groundBody = PhysicsFactory.createBoxBody(
				physics, ground, BodyType.StaticBody, Constants.WALL_FIXTURE);
		groundBody.setUserData(Constants.BODY_WALL);

		// just to limit the movement at the top
		@SuppressWarnings("unused")
		Body ceillingBody = PhysicsFactory.createBoxBody(
				physics, bigNumber / 2, 820, bigNumber, 20, BodyType.StaticBody, Constants.CEILLING_FIXTURE);
	}

	private void createActor() {
		dandelion = new TiledSprite(200, 400, res.beeOrBullRegion, vbom);
		dandelion.setZIndex(999);
		dandelion.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (dandelionBody.getLinearVelocity().y > -0.01) {
					dandelion.setCurrentTileIndex(0);
				} else {
					dandelion.setCurrentTileIndex(1);
				}
			}

			@Override
			public void reset() { }
		});
		dandelionBody = PhysicsFactory.createCircleBody(
				physics, dandelion, BodyType.DynamicBody, Constants.DANDELION_FIXTURE);
		dandelionBody.setFixedRotation(true);
		dandelionBody.setUserData(Constants.BODY_ACTOR);
		physics.registerPhysicsConnector(new PhysicsConnector(dandelion, dandelionBody));
		attachChild(dandelion);
	}

	private void createBackground() {
		pb = new ParallaxBackground(253/255f, 196/255f, 59/255f);
		Entity clouds = new Rectangle(0, 0, 500, 300, vbom);
		clouds.setAnchorCenter(0, 0);
		clouds.setAlpha(0f);
		clouds.attachChild(new Sprite(100, 300, res.cloudRegion, vbom));
		clouds.attachChild(new Sprite(200, 400, res.cloudRegion, vbom));

		clouds.attachChild(new Sprite(300, 450, res.cloudRegion, vbom));
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

		Iterator<Pillar> pi = pillars.iterator();
		while (pi.hasNext()) {
			Pillar p = pi.next();
			float temp=p.rand.nextFloat();
			if(temp>0.9){
				p.shift= 90 * temp;
			}
			PillarFactory.getInstance().recycle(p);
			pi.remove();
		}


		PillarFactory.getInstance().reset();

		dandelionBody.setTransform(400 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT,
				200 / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

		addPillar();
		addPillar();
		addPillar();

		score = 0;

		infoText.setText("Fly And Earn");
		infoText.setVisible(true);
		String plural="";
		if(res.activity.getHighScore()>1){
			plural="s";
		}
		scoreText.setText("Last Earned Coin"+plural+": " + res.activity.getHighScore());
		infoText.setVisible(true);

		sortChildren();	

		unregisterUpdateHandler(physics);
		physics.onUpdate(0);

		state = State.NEW;
	}

	private void addPillar() {
		Pillar p = PillarFactory.getInstance().next();
		pillars.add(p);
		attachIfNotAttached(p);
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		Debug.e("IS RUN:" + res.engine.isRunning() + " ev: " + pSceneTouchEvent.getAction());
		if(backButton.contains(pSceneTouchEvent.getX(), pSceneTouchEvent.getY())){
			backButton.onAreaTouched(pSceneTouchEvent,pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
		}
		if (pSceneTouchEvent.isActionDown()) {

			if (state == State.PAUSED) {
				if (lastState != State.NEW) {
					registerUpdateHandler(physics);
				}
				state = lastState;
				Debug.d("->" + state);
			} else if (state == State.NEW) {
				registerUpdateHandler(physics);
				PREVSTATE= State.AFTERLIFE;
				state = State.PLAY;
				Debug.d("->PLAY");
				physics.setGravity(new Vector2(0, Constants.GRAVITY));
				dandelionBody.setLinearVelocity(new Vector2(ResourceManagerForMiniGame.SPEED_X, 0 ));
				scoreText.setText("0");
				infoText.setVisible(false);
			} else if (state == State.DEAD) {
				// don't touch the dead!


			} else if (state == State.AFTERLIFE) {
				reset();
				state = State.NEW;
				Debug.d("->NEW");
			} else {
				Vector2 v = dandelionBody.getLinearVelocity();
				v.x = ResourceManagerForMiniGame.SPEED_X;
				isTouchedFlag = true;
				//v.y = Constants.SPEED_Y;
				dandelionBody.setLinearVelocity(v);
				Debug.d("TAP!");
				res.sndFly.play();
			}
		}

		else if(pSceneTouchEvent.isActionUp())
		{
			Vector2 v = dandelionBody.getLinearVelocity();
			v.x = ResourceManagerForMiniGame.SPEED_X;
			//v.y = Constants.SPEED_Y;
			dandelionBody.setLinearVelocity(v);
			isTouchedFlag = false;
		}

		return false;
	}

	public void resume() {
		Debug.d("Game resumed");
	}

	public void pause() {
		unregisterUpdateHandler(physics);
		lastState = state;
		state = State.PAUSED;
	}



	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		super.onManagedUpdate(pSecondsElapsed);

		if(state == State.DEAD || state == State.AFTERLIFE){
			Vector2 v = dandelionBody.getLinearVelocity();
			v.y=Constants.GRAVITY;
			v.x = 0;
			dandelionBody.setLinearVelocity(v);
		}
		else if (isTouchedFlag)
		{
			Vector2 v = dandelionBody.getLinearVelocity();
			//v.y=7;
			//v.x = 13;
			v.y=ResourceManagerForMiniGame.SPEED_Y;
			dandelionBody.setLinearVelocity(v);
		}



		pb.setParallaxValue(res.camera.getCenterX());
		if (scored) {
			addPillar();
			sortChildren();
			scored = false;
			score++;
			switch(score){
			case 25:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.15f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.10f;
				break;
			case 50:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.15f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.10f;
				break;
			case 75:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.15f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.10f;
				break;
			case 100:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.15f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.10f;
				break;
			case 125:
				ResourceManagerForMiniGame.SPEED_X = ResourceManagerForMiniGame.SPEED_X + 0.15f;
				ResourceManagerForMiniGame.SPEED_Y = ResourceManagerForMiniGame.SPEED_Y + 0.10f;
				break;
			default:
				if(score>130){
					float offSet= (0.0001f * score);

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

		// if first pillar is out of the screen, delete it
		if (!pillars.isEmpty()) {
			Pillar fp = pillars.getFirst();
			if (fp.getX() + fp.getWidth() < res.camera.getXMin()) {
				PillarFactory.getInstance().recycle(fp);
				pillars.remove();
			}
		}

		if (state == State.DEAD && timestamp + TIME_TO_RESSURECTION < System.currentTimeMillis()) {
			state = State.AFTERLIFE;
			Debug.d("->AFTERLIFE");
		}
	}

	private void attachIfNotAttached(Pillar p) {
		if (!p.hasParent()) {
			attachChild(p);
		}

	}

	@Override
	public void beginContact(Contact contact) {
		if (Constants.BODY_WALL.equals(contact.getFixtureA().getBody().getUserData()) ||
				Constants.BODY_WALL.equals(contact.getFixtureB().getBody().getUserData())) {
			if(StorageManager.getInstance().getUser()!=null && state == State.AFTERLIFE){
				if(PREVSTATE== State.DEAD){
					StorageManager.getInstance().getUser().setNumOfCoins(StorageManager.getInstance().getUser().getNumOfCoins()+score);
					StorageManager.getInstance().saveUserBeesBullsAndCoins(StorageManager.getInstance().getUser());
					if(score>1){
					SoundManager.getInstance().createAddCoinsForShopSound();
					}
					else if(score==1){
						SoundManager.getInstance().createAddCoinMediumSound();
					}
				}
				else{
					PREVSTATE= State.AFTERLIFE;
				}

				infoText.setText("Tap To Start Again");
				infoText.setVisible(true);
				String plural="";
				if(res.activity.getHighScore()>1){
					plural="s";
				}
				scoreText.setText("You Just Earned " + res.activity.getHighScore() +"  Coin"+plural);
				infoText.setVisible(true);

				sortChildren();	
			}
			else{
				PREVSTATE= State.DEAD;
			}
			state = State.DEAD;
			Debug.d("->DEAD");
			res.sndFail.play();

			//if (score > res.activity.getHighScore()) {
			res.activity.setHighScore(score);
			//}

			timestamp = System.currentTimeMillis();
			dandelionBody.setLinearVelocity(0, 0);
			for (Pillar p : pillars) {
				p.getPillarUpBody().setActive(false);
				p.getPillarDownBody().setActive(false);
			}			
		}

	}

	@Override
	public void endContact(Contact contact) {
		if (Constants.BODY_SENSOR.equals(contact.getFixtureA().getBody().getUserData()) ||
				Constants.BODY_SENSOR.equals(contact.getFixtureB().getBody().getUserData())) {
			scored = true;
		}

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
	}

	protected Sprite attachBackButton(HUD hud){
		backButton = new Sprite(5*ResourcesManager.resourceScaler, 5 * ResourcesManager.resourceScaler, ResourceManagerForMiniGame.chocobackbuttonregion, vbom) {
			boolean areaClickedDown;
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX,
					final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					areaClickedDown =true;
					return true;
				}
				else if((pSceneTouchEvent.isActionUp() || pSceneTouchEvent.isActionMove()) && areaClickedDown){
					Debug.e("How To Play Scene Back Key Pressed");
					areaClickedDown =false;


					ResourceManagerForMiniGame.getInstance().activity.finish();
					if(!ResourcesManager.getInstance().showing && null!= MusicService.mPlayer && MusicService.mPlayer.isPlaying()){
						SoundManager.getInstance().changeToMenuMusic();
						//SoundManager.getInstance().doUnbindService(this);
					}
					return true;
				}
				return false;
			}
		};
		backButton.setAnchorCenter(0, 0);
		backButton.setScale(0.30f * ResourcesManager.resourceScaler);
		backButton.setTag(EntityTagManager.backButton);
		backButtonDisplayed = true;

		if(!backButton.hasParent()){
			hud.attachChild(backButton);
		}
		this.registerTouchArea(hud);
		hud.registerTouchArea(backButton);
		return backButton;
	}

}
