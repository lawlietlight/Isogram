/**
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.bullrun;

import java.util.Random;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.ResourceManagerForMiniGame;
import com.efficientsciences.cowsandbulls.wordwars.managers.Constants;

public class Stick extends Entity {
	public Rectangle stickDown;
	
	private Body stickDownBody;
	private Body scoreSensor;
	static Random rand=new Random();
	float shift = 65;
	
	public Stick(float x, float y, VertexBufferObjectManager vbom, PhysicsWorld physics) {
		super(x, y);
		float temp=rand.nextFloat();
		if(temp>0.75){
		shift= shift * temp;
		}
		
		stickDown = new Rectangle(0, 0, Constants.CW/12, Constants.CW/2, ResourceManagerForMiniGame.getInstance().vbom);

		stickDown.setAnchorCenterX(0.4f);
		stickDown.setAnchorCenterY(1f);
		attachChild(stickDown);	

		stickDownBody = PhysicsFactory.createBoxBody(physics, stickDown, BodyType.StaticBody, Constants.WALL_FIXTURE);
		stickDownBody.setUserData(Constants.BODY_WALL);

		Rectangle r = new Rectangle(0, 0, 1, 9999, vbom); // just to make sure it's big
		r.setColor(Color.GREEN);
		r.setAlpha(0f);
		attachChild(r);
		
		scoreSensor = PhysicsFactory.createBoxBody(physics, r, BodyType.StaticBody, Constants.SENSOR_FIXTURE);
		scoreSensor.setUserData(Constants.BODY_SENSOR);

	}

	@Override
	public boolean collidesWith(IEntity pOtherEntity) {
		return stickDown.collidesWith(pOtherEntity);
	}


	public Body getStickDownBody() {
		return stickDownBody;
	}

	public Body getScoreSensor() {
		return scoreSensor;
	}
	
	public float getStickShiftY() {
		return stickDown.getHeight() / 2f + (shift);
	}
	public float getStickShiftX() {
		return stickDown.getWidth() / 2f + (shift);
	}
	@Override
	public float getWidth() {
		return stickDown.getWidth();
	}
	
	
}
