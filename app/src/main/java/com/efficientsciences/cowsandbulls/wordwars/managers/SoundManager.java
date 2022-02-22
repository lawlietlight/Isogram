package com.efficientsciences.cowsandbulls.wordwars.managers;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.util.debug.Debug;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;

public class SoundManager {

	public static final SoundManager INSTANCE = new SoundManager();

	public static MusicPlayed musicBeingPlayed=MusicPlayed.HIFI;
	public static MusicPlayed previouslyPlayedMusic=MusicPlayed.HIFI;
	public static SoundManager getInstance() {
		return INSTANCE;
	}

	public enum MusicPlayed{
		HIFI(1),BEEMUSIC(2),FASTMARCH(3),ROCKMUSIC(4),BEEMUSICJINGLE(5),FASTMARCHJINGLE(6),LOST(7);

		private int value;

		private MusicPlayed(final int newValue) {
			value = newValue;
		}

		public int getValue() { return value; }

	}
	public boolean mIsBound = false;
	public static MusicService mServ;
	public Sound mAddPageChunkSound;
	private Sound beeWoosh;
	private Sound addCoin;
	private Sound addCoinMedium;
	private Sound addCoinLong;
	private Sound addCoinsShop;
	private Sound bullRun;

	public boolean mIsBoundForServiceStart=true;

	public Sound blurp;
	public Sound cameraCapture;
	private Sound whipEffect;



	/*public void doBindService(Context c){
		SoundManager.getInstance().mIsBound=c.bindService(new Intent(c,MusicService.class),
				SoundManager.getInstance().Scon, Context.BIND_AUTO_CREATE);
		//SoundManager.getInstance().mIsBound = true;
	}

	public  void doUnbindService(Context c)
	{
		if(SoundManager.getInstance().mIsBound)
		{
			c.unbindService(SoundManager.getInstance().Scon);
			//SoundManager.getInstance().mIsBound = false;
		}
	}*/


	/*public ServiceConnection Scon =new ServiceConnection(){

		public void onServiceConnected(ComponentName name, IBinder
				binder) {
			mServ = ((MusicService.ServiceBinder)binder).getService();
			if(null!=SoundManager.getInstance().mServ){
				SoundManager.getInstance().mServ.changeToInGameTrack();
			}
			SoundManager.getInstance().mIsBound=true;
			mIsBoundForServiceStart=false;
			SoundManager.getInstance().doUnbindService(ResourcesManager.getInstance().activity);
		}

		public void onServiceDisconnected(ComponentName name) {
			mServ = null;
			SoundManager.getInstance().mIsBound=false;
		}
	};*/


	public void loadInGameSounds(){
		SoundFactory.setAssetBasePath("sfx/");
		try {
			mServ=MusicService.getInstance();
			this.mAddPageChunkSound = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "gamedrop.mp3");
			this.beeWoosh = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "beewooshsfx.mp3");
			this.bullRun = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "bullrun.mp3");
			this.blurp = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "watersplashfun.mp3");
			this.cameraCapture = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "wincapture.mp3");
			this.whipEffect = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "gamewhoosh.mp3");
			this.addCoin = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "addcoin.mp3");
			this.addCoinMedium = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "addcoinmedium.mp3");
			this.addCoinLong = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "addcoinlong.mp3");
			this.addCoinsShop = SoundFactory.createSoundFromAsset(ResourcesManager.getInstance().getEngine().getSoundManager(), ResourcesManager.getInstance().activity, "coinsinhand.mp3");

		} catch (final IOException e) {
			Debug.e(e);
		}
	}


	public void createSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.mAddPageChunkSound.play();
		}
	}

	public void createBeeWooshSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.beeWoosh.play();
		}
	}
	
	public void createBullRunSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.bullRun.play();
		}
	}

	public void createBlurpSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.blurp.play();
		}
	}
	
	public void createCameraCaptureSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.cameraCapture.play();
		}
	}

	public void createWhipSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.whipEffect.play();
		}
	}

	public void createAddCoinSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.addCoin.play();
		}
	}
	public void createAddCoinMediumSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.addCoinMedium.play();
		}
	}
	public void createAddCoinLongSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.addCoinLong.play();
		}
	}

	public void createAddCoinsForShopSound() {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			this.addCoinsShop.play();
		}
	}
	/**
	 * 
	 */
	public static void changeMusic(MusicPlayed musicNeeded) {
		if(musicNeeded.equals(MusicPlayed.BEEMUSIC.toString())||musicNeeded.equals(MusicPlayed.FASTMARCH)
				||musicNeeded.equals(MusicPlayed.BEEMUSICJINGLE.toString())||musicNeeded.equals(MusicPlayed.FASTMARCHJINGLE)){
			previouslyPlayedMusic=musicBeingPlayed;
		}
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			//SoundManager.getInstance().doBindService(ResourcesManager.getInstance().activity);
			if(null!=mServ){
				musicBeingPlayed=musicNeeded;
				switch (musicNeeded) {
				case HIFI:
					mServ.changeBackToMenuTrack();
					break;
				case BEEMUSIC:
					mServ.changeToInGameTrack();
					break;
				case FASTMARCH:
					mServ.changeToInGameFastMarchTrack();
					break;
				case ROCKMUSIC:
					mServ.changeToRockMusicTrack();
					break;
				case BEEMUSICJINGLE:
					mServ.changeToInGameBeeJingleTrack();
					break;
				case FASTMARCHJINGLE:
					mServ.changeToInGameFastJingleTrack();
					break;
				case LOST:
					mServ.changeToInGameLostTrack();
					break;
				default:
					mServ.changeToInGameTrack();
					break;
				}

			}

		}
	}


	/**
	 * 
	 */
	public void changeToMenuMusic() {
		if(previouslyPlayedMusic.equals(MusicPlayed.HIFI)){
			changeMusic(MusicPlayed.ROCKMUSIC);
		}
		else{
			changeMusic(MusicPlayed.HIFI);
		}

	}



}
