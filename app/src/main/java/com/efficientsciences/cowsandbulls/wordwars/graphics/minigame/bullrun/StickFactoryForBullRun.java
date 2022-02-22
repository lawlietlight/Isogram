/**
 */
package com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.bullrun;

import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.util.adt.pool.GenericPool;

import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.graphics.minigame.ResourceManagerForMiniGame;

public class StickFactoryForBullRun {
	private static final StickFactoryForBullRun INSTANCE = new StickFactoryForBullRun();
	private StickFactoryForBullRun() {	
	}

	GenericPool<Stick> pool;

	float nextX;
	float nextY;
	float dy;

	final float dx = 200;

	final int maxY = 260;
	final int minY = 210;

	public static final StickFactoryForBullRun getInstance() {
		return INSTANCE;
	}

	public void create(final PhysicsWorld physics) {
		reset();
		pool = new GenericPool<Stick>(3) {

			@Override
			protected Stick onAllocatePoolItem() {
				Stick p = new Stick(0, 0, 
						ResourceManagerForMiniGame.getInstance().vbom, 
						physics);
				return p;
			}
		};

	}

	public Stick next() {
		Stick p = pool.obtainPoolItem();
		p.setPosition(nextX, nextY);
		float temp=p.rand.nextFloat();
		if(temp>0.95){
			p.shift= p.shift * temp;
		}
		p.stickDown.setY(-p.shift);
		p.stickDown.setX(+(p.shift/4f));

		p.getScoreSensor().setTransform((nextX + (p.getStickShiftX()/4f))/ PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);


		p.getStickDownBody().setTransform((nextX + (p.getStickShiftX()/4f)) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(nextY - p.getStickShiftY()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);	
		Log.e("Sticksss Y", (nextY)+"");
		Log.e("Sticksss NextY", (nextY - p.getStickShiftY())+"");
		p.getScoreSensor().setActive(true);
		p.getStickDownBody().setActive(true);


		if(temp>0.3 && temp<0.8 && (nextY < maxY && nextY > minY)){
			dy = -dy;
		}

		//dy = dy+(int)temp;
		nextX += (p.shift/50f)*dx;
		nextY += dy;

		if (nextY >= maxY || nextY <= minY) {
			/*if(dy<0){
				dy=50;
			}
			else{
				dy=-50;
			}*/
			nextY= 240;
			dy = -dy;
		}


		return p;
	}

	public void recycle(Stick p) {
		p.detachSelf();
		p.getScoreSensor().setActive(false);
		p.getStickDownBody().setActive(false);		
		p.getScoreSensor().setTransform(-1000, -1000, 0);
		p.getStickDownBody().setTransform(-1000, -1000, 0);
		pool.recyclePoolItem(p);
	}

	public void reset() {
		nextX = 650;
		nextY = 200;
		dy = 27;
	}

}
