/**
 * NotificationManager.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.managers;

import java.util.Date;
import java.util.Random;
import java.util.Timer;

import org.andengine.engine.handler.timer.TimerHandler;

/**
 * @author SATHISH
 *
 */
public class TimerManager {
	private TimerManager(){
	}
	private static final TimerManager INSTANCE=new TimerManager();
	
	public static TimerManager getInstance(){
		return INSTANCE;
	}
	
	
	public Timer beeAppearanceTimer;
	public int beeAppearedCount;
	public Random randomiser=new Random(new Date().getTime());
	//public int howOftenShouldAppear=10000;
	public int howOftenShouldAppear=200;
	
	public static Timer responseTimer;
	public TimerHandler waitingRoomTimer;
	public boolean bullAppeared;
	//public int beesShoppedCount;
	public static Timer queueResponseTimer;
}
