package com.efficientsciences.cowsandbulls.wordwars.services;


import java.io.IOException;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;

/**
 * Created by SATHISH on 26/1/14.
 */

public class OriginalMusicService extends Service  implements MediaPlayer.OnErrorListener, OnPreparedListener{

	private final IBinder mBinder = new ServiceBinder();
	public static MediaPlayer mPlayer;
	private static int length = 0;
	public static boolean isRunning = false;
	/*
	private static final MusicService INSTANCE = MusicService.this;

	public static MusicService getInstance() {
		return INSTANCE;
	}*/
	public OriginalMusicService() { }

	public class ServiceBinder extends Binder {
		public OriginalMusicService getService()
		{
			return OriginalMusicService.this;
		}
	}

	@Override
	public IBinder onBind(Intent arg0){return mBinder;}

	@Override
	public void onCreate (){
		if(SoundManager.getInstance().mIsBoundForServiceStart){
			super.onCreate();
			mPlayer = MediaPlayer.create(this, R.raw.hifi_02);
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
	}

	@Override
	public int onStartCommand (Intent intent, int flags, int startId)
	{
		return START_NOT_STICKY;
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
			}
			//mPlayer.release();
		}
	}

	@Override
	public void onDestroy ()
	{
		super.onDestroy();
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

		Toast.makeText(this, "music player failed", Toast.LENGTH_SHORT).show();
		if(mPlayer != null && mPlayer.isPlaying())
		{
			try{

				mPlayer.stop();
				mPlayer.release();
			}finally {

				mPlayer.stop();
				this.stopSelf();
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
		stopMusic();
		mPlayer = MediaPlayer.create(this, R.raw.hifi_02);
		mPlayer.setOnErrorListener(this);
		mPlayer.setOnPreparedListener(this);
		if(mPlayer!= null)
		{
			mPlayer.setLooping(true);
			mPlayer.setVolume(100,100);
		}


		mPlayer.setOnErrorListener(new OnErrorListener() {

			public boolean onError(MediaPlayer mp, int what, int
					extra){

				onError(mPlayer, what, extra);
				return true;
			}
		});
		if(!mPlayer.isPlaying()){
			try {
				try {
					length=0;
					mPlayer.prepare();
				} catch (IOException e) {
					e.printStackTrace();
				}

			} catch (IllegalStateException e) {
				return;
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
}