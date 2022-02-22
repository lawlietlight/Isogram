package com.efficientsciences.cowsandbulls.wordwars;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by SATHISH on 26/1/14.
 */
public class SplashActivity extends Activity implements MediaPlayer.OnCompletionListener,OnPreparedListener, OnErrorListener, ReceiveObserver{
	//private boolean isFinishcalled=false;
	private VideoView video = null;

	private static final String GA_PROPERTY_ID = "UA-XXXX-Y";
	private static final String CAMPAIGN_SOURCE_PARAM = "utm_source";

	Tracker mTracker;

	private void jump() {
		//it is safe to use this code even if you
		//do not intend to allow users to skip the splash
		if(isFinishing())
			return;
		startActivity(new Intent(this, LoginSign.class));
		finish();
	}

	public static String printKeyHash(Activity context) {
		PackageInfo packageInfo;
		String key = null;
		try {

			//getting application package name, as defined in manifest
			String packageName = context.getApplicationContext().getPackageName();

			//Retriving package info
			packageInfo = context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_SIGNATURES);
			
			Log.e("Package Name=", context.getApplicationContext().getPackageName());
			
			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));
			
				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key Hash= ", key);

			}
		} catch (NameNotFoundException e1) {
			Log.e("Name not found", e1.toString());
		}

		catch (NoSuchAlgorithmException e) {
			Log.e("No such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return key;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		//printKeyHash(this);
		dimSoftButtonsIfPossible();
		super.onCreate(savedInstanceState);
		
		//Campaign Tracking - Start
		// Get tracker.
		// Tracker t = (this.getApplication()).getTracker(TrackerName.APP_TRACKER);
		Intent startIntent = this.getIntent();
		Uri uri = startIntent.getData();
		String referrer="";
		String contactSource="";
		if (uri != null) {
			if(uri.getQueryParameter("utm_source") != null) {    // Use campaign parameters if avaialble.
				referrer=uri.getQueryParameter("utm_source");
			}
			if (uri.getQueryParameter("referrer") != null) {    // Otherwise, try to find a referrer parameter.
				contactSource=uri.getQueryParameter("referrer");
			}
		}
		//Campaign Tracking - End

		StorageManager.getInstance().loadPrefs(this, referrer, contactSource);
		if(StorageManager.getInstance().getUser()!=null && StorageManager.getInstance().getUser().getUserName()!=null  && !StorageManager.getInstance().getUser().getUserName().startsWith("WordPlayer#") &&!StorageManager.getInstance().getUser().isStartUpAnimationRequired()){
			MusicService.getInstance().onCreate(this);
			if(StorageManager.getInstance().getUser().isMusicMuted() && null!=MusicService.mPlayer){
				MusicService.pauseMusic();
			}
			jump();
		}
		else{
			if(!MusicService.isRunning){
				setContentView(R.layout.splash);
				VideoView video = (VideoView) findViewById(R.id.videoView1);

				MusicService.isRunning=true;
				video.setVideoPath("android.resource://" + getPackageName() + "/raw/splashvideo");

				video.setOnPreparedListener(this);

				//startService(new Intent(SplashActivity.this, MusicService.class));
				video.setOnErrorListener(this);

				if(StorageManager.getInstance().getUser().isMusicMuted() && null!=MusicService.mPlayer){
					MusicService.pauseMusic();
				}

				video.setOnCompletionListener(this);

			}
			else{
				Intent intent = new Intent(this, LoginSign.class);

				//startService(new Intent(SplashActivity.this, MusicService.class));

				startActivity(intent);
				// isFinishcalled=true;
				finish();
			}
		}
	}

	

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void dimSoftButtonsIfPossible() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		} 
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra)   
	{  
		Log.e(getPackageName(), String.format("Error(%s%s)", what, extra));


		// Deal with any other errors you need to. 
		if(null!=video){
		video.setOnErrorListener(this);
		video.setOnCompletionListener(this);
		video.setOnPreparedListener(this);

		// and handle onPrepare, start(), etc with this function

		try {
			//	mp.release();
			mp.reset();
			video.setVideoPath("android.resource://" + getPackageName() + "/raw/splashvideo_hero_3gp");

			//mp.prepare();
			return true;  
		} catch (IllegalStateException e) {
			try {
				//mp.reset();
				if(what == MediaPlayer.MEDIA_ERROR_SERVER_DIED)
					mp.reset();

				else if(extra == MediaPlayer.MEDIA_ERROR_UNKNOWN)
					mp.reset();
				video.setVideoPath("android.resource://" + getPackageName() + "/raw/splashvideo_hero_3gp");
				//mp.prepare();

			} catch (IllegalArgumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jump();
			} catch (SecurityException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jump();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				jump();
			} 
			e.printStackTrace();
		}
		}
		else{
			jump();
		}
			/*catch (IOException e) {
		}
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		return true;  
	}  

	@Override
	public void onCompletion(MediaPlayer mp)
	{

		Intent intent = new Intent(this, LoginSign.class);
		startActivity(intent);
		if(null!= MusicService.mPlayer){
			if(StorageManager.getInstance().getUser().isMusicMuted()){
				MusicService.pauseMusic();
			}
			else if(SoundManager.getInstance().mIsBoundForServiceStart){

				MusicService.resumeMusic();
			}
		}
		//  isFinishcalled=true;
		finish();
	}



	@Override
	protected void onPause() {
		super.onPause();
		if(null!= MusicService.mPlayer && MusicService.mPlayer.isPlaying() && !isFinishing()){
			MusicService.pauseMusic();
		}

	}



	@Override
	protected void onResume() {
		super.onResume();
		/*Intent intent = new Intent(this, LoginSign.class);
        startActivity(intent);*/
		if(null!=video){
			video.setOnErrorListener(this);
			video.setOnCompletionListener(this);
			video.setOnPreparedListener(this);
		}
		if(null!= MusicService.mPlayer && !MusicService.mPlayer.isPlaying()){
			if(StorageManager.getInstance().getUser().isMusicMuted()){
				MusicService.pauseMusic();
			}
			else{
				MusicService.resumeMusic();
			}
			jump();
		}
		else{
			/*if(SoundManager.getInstance().Scon==null){
            	SoundManager.getInstance().Scon =new ServiceConnection(){

                public void onServiceConnected(ComponentName name, IBinder
                        binder) {
                	SoundManager.getInstance().mServ = ((MusicService.ServiceBinder)binder).getService();
                }

                public void onServiceDisconnected(ComponentName name) {
                	SoundManager.getInstance().mServ = null;
                }
            };
            }*/
		}
	}


	@Override
	protected void onStop() {
		super.onStop();

		/*if(null!=SoundManager.getInstance().mServ){
			MusicService.stopMusic();
		}
		else{*/
		if( null!=MusicService.mPlayer &&  MusicService.mPlayer.isPlaying()&& !isFinishing()){
			MusicService.pauseMusic();
		}
		/*}*/
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		video = (VideoView) findViewById(R.id.videoView1);
		video.start();	

		MusicService.getInstance().onCreate(this);
	}
	
	

	/* (non-Javadoc)
	 * @see com.efficientsciences.cowsandbulls.wordwars.ReceiveObserver#onReceived()
	 */
	@Override
	public void onReceived() {
		Log.i("Campaign ", "onReceived");
        doSomeThing();
    }

    private void doSomeThing() {
        Log.i("Campaign ", "doSomeThing");
        String referrer = StorageManager.getInstance().referrer;
    }
	
	
}

interface ReceiveObserver {
    void onReceived();
}

