/**
 * HowToPlayImageActivity.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars;

import org.andengine.util.ActivityUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.efficientsciences.cowsandbulls.wordwars.graphics.TouchImageView;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;

/**
 * @author SATHISH
 *
 */
public class HowToPlayImageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        ActivityUtils.keepScreenOn(this);
        TouchImageView img = new TouchImageView(this);
        img.setImageResource(R.drawable.hostguessinstructable843);
        img.setMaxZoom(4f);
        setContentView(img);
    }

    

	@Override
	public synchronized void onPause() {
		super.onPause();
		if(null!= MusicService.mPlayer && MusicService.mPlayer.isPlaying()){
			MusicService.pauseMusic();
		}
	}

	@Override
	public synchronized void onResume() {
		super.onResume();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dimSoftButtonsIfPossible();
			}
		});
		/*if(null!=SoundManager.getInstance().mServ)
			SoundManager.getInstance().mServ.resumeMusic();	
		else */
		if(null!= MusicService.mPlayer && !MusicService.mPlayer.isPlaying()){
			MusicService.resumeMusic();
		}
		
	}
	
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void dimSoftButtonsIfPossible() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		} 
	}
}
