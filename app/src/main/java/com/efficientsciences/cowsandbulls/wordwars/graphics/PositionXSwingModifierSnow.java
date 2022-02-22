package com.efficientsciences.cowsandbulls.wordwars.graphics;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;

public class PositionXSwingModifierSnow extends SingleValueSpanEntityModifier {
	
	private float mInitialX;
	
	private float mFromMagnitude;
	private float mToMagnitude;


	public PositionXSwingModifierSnow(float pDuration, float pFromValue, float pToValue,
			float pFromMagnitude, float pToMagnitude) {
		super(pDuration, pFromValue, pToValue);
		mFromMagnitude = pFromMagnitude;
		mToMagnitude = pToMagnitude;
	}

	@Override
	protected void onSetInitialValue(IEntity pItem, float pValue) {
		mInitialX = pItem.getX();

	}

	@Override
	protected void onSetValue(IEntity pItem, float pPercentageDone, float pValue) {
		float currentMagnitude = mFromMagnitude + (mToMagnitude - mFromMagnitude) * pPercentageDone;
		float currentSinValue = (float) Math.sin(pValue);
		pItem.setX(mInitialX + currentMagnitude * currentSinValue);
	}

	@Override
	public PositionXSwingModifierSnow deepCopy() throws DeepCopyNotSupportedException {
		throw new DeepCopyNotSupportedException();
	}

}
