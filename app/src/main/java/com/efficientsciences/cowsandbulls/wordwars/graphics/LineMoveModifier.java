package com.efficientsciences.cowsandbulls.wordwars.graphics;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.util.modifier.ease.IEaseFunction;

/*
 * @Author Sathish Kumar Linear Line Movement Modifier 
 */
public class LineMoveModifier extends MoveModifier{

	public LineMoveModifier(float pDuration, float pFromX, float pToX,
			float pFromY, float pToY, IEntityModifierListener moveAndHideLineListener) {
		super(pDuration, pFromX, pToX, pFromY, pToY, moveAndHideLineListener);
	}
	public LineMoveModifier(final float pDuration, final float pFromX, final float pToX, final float pFromY, final float pToY, final IEntityModifierListener pEntityModifierListener, final IEaseFunction pEaseFunction) {
		super(pDuration, pFromX, pToX, pFromY, pToY, pEntityModifierListener, pEaseFunction);
	}

	@Override
	protected void onSetValues(final IEntity pEntity, final float pPercentageDone, final float pX, final float pY) {
		if(pEntity instanceof Line){
			Line line = (Line)pEntity;
			line.setPosition(line.getX1(), pY, line.getX2(), pY);
		}
	}

}