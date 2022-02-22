package com.efficientsciences.cowsandbulls.wordwars.graphics;

import java.util.Random;

import org.andengine.entity.IEntity;
import org.andengine.entity.particle.Particle;
import org.andengine.entity.particle.initializer.IParticleInitializer;

public class RegisterXSwingEntityModifierInitializerSnow<T extends IEntity> implements
		IParticleInitializer<T> {
	
	private float mDuration;
	private float mFromValue;
	private float mToValue;
	private float mFromMagnitude;
	private float mToMagnitude;
	private boolean mRandomize;
	
	private static final Random RANDOM = new Random(System.currentTimeMillis());

	public RegisterXSwingEntityModifierInitializerSnow(float pDuration, float pFromValue, float pToValue,
			float pFromMagnitude, float pToMagnitude,
			boolean pRandomize) {
		mDuration = pDuration;
		mFromValue = pFromValue;
		mToValue = pToValue;
		mFromMagnitude = pFromMagnitude;
		mToMagnitude = pToMagnitude;
		mRandomize = pRandomize;
	}

	@Override
	public void onInitializeParticle(Particle<T> pParticle) {
		if (mRandomize) {
			pParticle.getEntity().registerEntityModifier(
					new PositionXSwingModifierSnow(mDuration, 
							mFromValue, mFromValue + RANDOM.nextFloat() * (mToValue - mFromValue),
							mFromMagnitude, mFromMagnitude + RANDOM.nextFloat() * (mToMagnitude - mFromMagnitude) ));
		} else {
			pParticle.getEntity().registerEntityModifier(
					new PositionXSwingModifierSnow(mDuration, 
							mFromValue, mToValue, 
							mFromMagnitude, mToMagnitude));
		}
	}

}
