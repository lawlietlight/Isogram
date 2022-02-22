package com.efficientsciences.cowsandbulls.wordwars.services;


import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.entity.IEntity;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.scenes.MainMenuScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.RealGameInterface;

/**
 * Created by SATHISH on 26/1/14.
 */

public class MusicService implements MediaPlayer.OnErrorListener, OnPreparedListener{

	public static MediaPlayer mPlayer;

	public static MediaPlayer mPlayerHifi;
	public static MediaPlayer mPlayerRock;
	public static MediaPlayer mPlayerBeemusic;
	public static MediaPlayer mPlayerFastMarch;
	public static MediaPlayer mPlayerBeemusicJingle;
	public static MediaPlayer mPlayerFastMarchJingle;
	public static MediaPlayer mPlayerLost;

	private static int length = 0;
	public static boolean isRunning = false;

	private static final MusicService INSTANCE=new MusicService();

	public static MusicService getInstance() {
		return INSTANCE;
	}
	public MusicService() { }


	public void onCreate (Context context){

		mPlayerHifi = MediaPlayer.create(context, R.raw.hifi_02);
		mPlayer=mPlayerHifi;

		if(mPlayer!= null)
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);

			mPlayer.setLooping(true);
			mPlayer.setVolume(100,100);

			mPlayer.setOnErrorListener(new OnErrorListener() {

				public boolean onError(MediaPlayer mp, int what, int
						extra){

					onError(mPlayer, what, extra);
					return true;
				}
			});
		}
	}


	public static void pauseMusic()
	{
		if(null!=mPlayer && mPlayer.isPlaying())
		{
			mPlayer.pause();
			length=mPlayer.getCurrentPosition();
		}
	}

	public static void resumeMusic()
	{
		if(null!=mPlayer && mPlayer.isPlaying()==false)
		{	
			if(!StorageManager.getInstance().getUser().isMusicMuted()){
				mPlayer.seekTo(length);
				mPlayer.start();
			}
		}
	}

	public static void stopMusic()
	{
		if(null!=mPlayer){
			if(mPlayer.isPlaying())
			{
				mPlayer.stop();
				//mPlayer.release();
			}
			//mPlayer.reset();
		}
	}

	public void onDestroy ()
	{
		if(mPlayer != null)
		{
			try{
				mPlayer.stop();
				mPlayer.release();
			}finally {
				mPlayer.stop();
			}
		}
	}

	public boolean onError(MediaPlayer mp, int what, int extra) {

		ToastHelper.makeCustomToast("music player failed", Toast.LENGTH_SHORT);
		if(mPlayer != null && mPlayer.isPlaying())
		{
			try{

				mPlayer.stop();
				//mPlayer.release();
			}finally {

				mPlayer.stop();
			}
		}
		return false;
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		if(!StorageManager.getInstance().getUser().isMusicMuted()){
			if(length==0)
				mPlayer.start();
		}
	}

	public void changeToInGameTrack(){
		pauseMusic();

		mPlayer=mPlayerBeemusic;

		
		if(mPlayer!= null)
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setLooping(true);
			mPlayer.setVolume(100,100);
			
			if(!mPlayer.isPlaying()){
				try {
					length=0;
					resumeMusic();
				} catch (IllegalStateException e) {
					return;
				}
			}
		}


		/*mPlayer.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int
					extra){

				onError(mPlayer, what, extra);
				return true;
			}
		});*/
		

	}

	public void changeBackToMenuTrack(){
		pauseMusic();

		mPlayer=mPlayerHifi;


		if(mPlayer!= null)
		{	
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setLooping(true);
			mPlayer.setVolume(100,100);
			
			if(!mPlayer.isPlaying()){
				try {
					length=0;
					resumeMusic();
				} catch (IllegalStateException e) {
					return;
				}
			}
		}


		/*mPlayer.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int
					extra){

				onError(mPlayer, what, extra);
				return true;
			}
		});*/
		
	}

	/**
	 * 
	 */
	public void changeToRockMusicTrack() {
		pauseMusic();

		mPlayer=mPlayerRock;

		
		if(mPlayer!= null)
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setLooping(true);
			mPlayer.setVolume(100,100);
			
			if(!mPlayer.isPlaying()){
				try {
					length=0;
					resumeMusic();
				} catch (IllegalStateException e) {
					return;
				}
			}
		}
	}


	public void changeToInGameFastMarchTrack(){
		pauseMusic();

		mPlayer=mPlayerFastMarch;
		
		if(mPlayer!= null)
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setLooping(true);
			mPlayer.setVolume(50,50);
			
			if(!mPlayer.isPlaying()){
				try {
					length=0;
					resumeMusic();
				} catch (IllegalStateException e) {
					return;
				}
			}
		}
	}

	/**
	 * 
	 */
	public void stopMusicAndRelease() {
		stopMusic();
		mPlayer.release();
	}
	/**
	 * 
	 */
	public void changeToInGameBeeJingleTrack() {
		pauseMusic();

		mPlayer=mPlayerBeemusicJingle;

		
		if(mPlayer!= null)
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setLooping(true);
			mPlayer.setVolume(100,100);
			if(!mPlayer.isPlaying()){
				try {
					//length=0;
					if(mPlayer.getDuration()<=length){
						length=0;
					}
					resumeMusic();
				} catch (IllegalStateException e) {
					return;
				}
			}
		}
	}

	/**
	 * 
	 */
	public void changeToInGameFastJingleTrack() {
		pauseMusic();

		mPlayer=mPlayerFastMarchJingle;

		
		if(mPlayer!= null)
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setLooping(true);
			mPlayer.setVolume(100,100);
			
			if(!mPlayer.isPlaying()){
				try {
					if(mPlayer.getDuration()<=length){
						length=0;
					}
					resumeMusic();
				} catch (IllegalStateException e) {
					return;
				}
			}
		}

	}
	/**
	 * 
	 */
	public void changeToInGameLostTrack() {
		pauseMusic();
		mPlayerLost.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer mp) {
				ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						//SceneManager.getInstance().getCurrentScene().setBackgroundEnabled(true);
						if(SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_GUESS) || SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_HOST)){

							SceneManager.getInstance().getCurrentScene().clearChildScene();
							SceneManager.getInstance().getCurrentScene().setIgnoreUpdate(false);

							//enableUpdates();
							((RealGameInterface)(SceneManager.getInstance().getCurrentScene())).confirmBackKeyPressed();
						}
					}
				});
			}
		});
		mPlayer=mPlayerLost;

		
		if(mPlayer!= null)
		{
			mPlayer.setOnErrorListener(this);
			mPlayer.setOnPreparedListener(this);
			mPlayer.setLooping(false);
			mPlayer.setVolume(100,100);
			
			if(!mPlayer.isPlaying()){
				try {
					length=0;
					resumeMusic();
				} catch (IllegalStateException e) {
					return;
				}
			}
		}

		
	}
}