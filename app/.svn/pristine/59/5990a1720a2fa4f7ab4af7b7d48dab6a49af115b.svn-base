/**
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics.minigame;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.pool.GenericPool;

public class PillarFactory {
	private static final PillarFactory INSTANCE = new PillarFactory();
	private PillarFactory() {	
	}
	
	GenericPool<Pillar> pool;

	int nextX;
	int nextY;
	int dy;
	
	final int dx = 300;
	
	final int maxY = 400;
	final int minY = 200;
	
	public static final PillarFactory getInstance() {
		return INSTANCE;
	}

	public void create(final PhysicsWorld physics) {
		reset();
		pool = new GenericPool<Pillar>(3) {
				
				@Override
				protected Pillar onAllocatePoolItem() {
					Pillar p = new Pillar(0, 0, 
							ResourceManagerForMiniGame.getInstance().pillarRegion, 
							ResourceManagerForMiniGame.getInstance().vbom, 
							physics);
					return p;
				}
			};
		
	}
	
	public Pillar next() {
		Pillar p = pool.obtainPoolItem();
		p.setPosition(nextX, nextY);
		float temp=p.rand.nextFloat();
		if(temp>0.95){
			p.shift= p.shift * temp;
		}
		p.pillarUp.setY(p.shift);
		p.pillarDown.setY(-p.shift);
		p.getScoreSensor().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
		
		p.getPillarUpBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(nextY + p.getPillarShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);
		
		p.getPillarDownBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(nextY - p.getPillarShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);	
		
		p.getScoreSensor().setActive(true);
		p.getPillarUpBody().setActive(true);
		p.getPillarDownBody().setActive(true);
		
		
		if(temp>0.3 && (nextY != maxY && nextY != minY)){
			dy = -dy;
		}
		
		//dy = dy+(int)temp;
		nextX += dx;
		nextY += dy;
		
		if (nextY >= maxY || nextY <= minY) {
			/*if(dy<0){
				dy=50;
			}
			else{
				dy=-50;
			}*/
			dy = -dy;
		}

		
		return p;
	}
	
	public void recycle(Pillar p) {
		p.detachSelf();
		p.getScoreSensor().setActive(false);
		p.getPillarUpBody().setActive(false);
		p.getPillarDownBody().setActive(false);		
		p.getScoreSensor().setTransform(-1000, -1000, 0);
		p.getPillarUpBody().setTransform(-1000, -1000, 0);
		p.getPillarDownBody().setTransform(-1000, -1000, 0);
		pool.recyclePoolItem(p);
	}
	
	public void reset() {
		nextX = 650;
		nextY = 200;
		dy = 50;
	}

}
