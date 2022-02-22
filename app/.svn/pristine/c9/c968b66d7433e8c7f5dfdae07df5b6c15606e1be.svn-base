package com.efficientsciences.cowsandbulls.wordwars.managers;


import java.security.acl.LastOwnerException;
import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.domain.Level;
import com.efficientsciences.cowsandbulls.wordwars.domain.ThemePreference;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.domain.storage.UserDOColumns;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;

public class StorageManager {


	private StorageManager(){

	}
	private static final StorageManager INSTANCE = new StorageManager();

	public static StorageManager getInstance() {
		return INSTANCE;
	}


	private static final String PREFS_NAME = "WORDWARSTMSAVEDDATAEFFICIENTSCIENCES";

	private SharedPreferences mSettings;

	private static String mHostname;
	//private static String mPort;
	private static String mPath;
	private static final UserDO user = new UserDO();
	public static final UserDO tempUser = new UserDO();

	public static String referrer;
	private static String sourceContacted;
	private static boolean toastInProgress;
	private boolean loadAttemptMade;
	public void loadPrefs() {

		if(null!=ResourcesManager.getInstance().activity){
			mSettings = ResourcesManager.getInstance().activity.getSharedPreferences(PREFS_NAME, 0);

			mHostname=mSettings.getString("hostname", ConnectionManager.mHostname);
			//mPort=mSettings.getString("port", ConnectionManager.mPort);
			referrer=mSettings.getString("referrer", "");
			sourceContacted=mSettings.getString("sourceContacted", "");
			mPath=mSettings.getString("path", ConnectionManager.mPath+ConnectionManager.mPathRoomNumberSuffix);

			String refferedInstalls =getReferreredInstalls();
			if(null!=refferedInstalls && !refferedInstalls.isEmpty() && refferedInstalls.equals("R")){
				ResourcesManager.isAdsEnabledRefBased =false;
			}
			tempUser.setMusicMuted(mSettings.getBoolean(UserDOColumns.isMusicMutedColumn, false));
			tempUser.setSoundMuted(mSettings.getBoolean(UserDOColumns.isSoundMutedColumn, true));

			tempUser.setStartUpAnimationRequired(mSettings.getBoolean(UserDOColumns.startUpAnimationRequiredColumn, false));
			String randomUserName="WordPlayer#"+new Random().nextInt();
			tempUser.setUserName(mSettings.getString(UserDOColumns.userNameColumn, randomUserName));
			tempUser.setDisplayName(mSettings.getString(UserDOColumns.firstNameColumn, randomUserName));
			tempUser.setScore(mSettings.getLong(UserDOColumns.score, 0));

			ResourcesManager.lastLogin= mSettings.getLong("LASTLOGIN",  0);

			ResourcesManager.getInstance().level = new Level(tempUser.getScore());

			//ResourcesManager.numberOfCoinsTotal = mSettings.getLong(UserDOColumns.numberOfCoinsTotal, 0);
			tempUser.setNumOfCoins(mSettings.getLong(UserDOColumns.numberOfCoinsTotal, 0));
			tempUser.setNumOfGamesPlayed(mSettings.getInt(UserDOColumns.numberOfGamesPlayed, 0));
			tempUser.setNumOfGamesWon(mSettings.getInt(UserDOColumns.numberOfGamesWon, 0));
			//TimerManager.getInstance().beesShoppedCount = mSettings.getInt(UserDOColumns.beesCount, 0);
			tempUser.setNumOfBees(mSettings.getInt(UserDOColumns.beesCount, 0));
			tempUser.setNumOfHoneyCombs(mSettings.getInt(UserDOColumns.honeycombCount, 0));

			tempUser.setNumOfHoneyCombsSmall(mSettings.getInt(UserDOColumns.honeycombSmallCount, 0));
			tempUser.setNumOfBullRuns(mSettings.getInt(UserDOColumns.bullsCount, 0));
			tempUser.setNumOfRedCape(mSettings.getInt(UserDOColumns.redCapeCount, 0));
			if(tempUser.getUserName().equals(randomUserName)){
				saveUser(tempUser);
			}
			tempUser.toString();
			ThemeManager.themePreferences.setThemeSelection(mSettings.getString(ThemePreference.themeSelectionColumn, THEMES.YELLOWFANTASY.toString()));
			AudioManager audio = (AudioManager) ResourcesManager.getInstance().activity.getSystemService(Context.AUDIO_SERVICE);
			switch( audio.getRingerMode() ){
			case AudioManager.RINGER_MODE_NORMAL:
				break;
			case AudioManager.RINGER_MODE_SILENT:
				tempUser.setMusicMuted(true);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				tempUser.setMusicMuted(true);
				break;
			}
			tempUser.toString();
		}
	}

	public void savePrefs() {
		//TODO

		SharedPreferences.Editor editor = mSettings.edit();
		editor.putString("hostname", mHostname);
		//editor.putString("port", mPort);
		editor.putString("path", mPath);
		editor.commit();
	}


	public void saveUser(UserDO user) {
		if(null!=mSettings){
			SharedPreferences.Editor editor = mSettings.edit();
			editor.putBoolean(UserDOColumns.isMusicMutedColumn, user.isMusicMuted());
			editor.putBoolean(UserDOColumns.isSoundMutedColumn, user.isSoundMuted());

			if(null!=user.getUserName() && !user.getUserName().isEmpty()){
				editor.putString(UserDOColumns.userNameColumn, user.getUserName());
				editor.putString(UserDOColumns.firstNameColumn, user.getDisplayName());
				editor.putString(UserDOColumns.userImageUrl, user.getUserProfilePicUrl());
				//editor.putLong("LASTLOGIN", Calendar.getInstance().getTimeInMillis());
			}
			editor.commit();
		}
	}

	public void saveLastLogin() {
		if(null==mSettings && null!=ResourcesManager.getInstance().activity){
			mSettings = ResourcesManager.getInstance().activity.getSharedPreferences(PREFS_NAME, 0);
		}
		if(null!=mSettings){
			SharedPreferences.Editor editor = mSettings.edit();
			if(null!=tempUser.getUserName() && !tempUser.getUserName().isEmpty()){
				ResourcesManager.lastLogin=Calendar.getInstance().getTimeInMillis();
				editor.putLong("LASTLOGIN", ResourcesManager.lastLogin);
				
			}
			editor.commit();
		}
	}
	
	public void saveUserScore(UserDO user) {

		if(null!=ResourcesManager.getInstance().activity){
			getUser().setStObject(false);
			mSettings = ResourcesManager.getInstance().activity.getSharedPreferences(PREFS_NAME, 0);
			String s= mSettings.getString(UserDOColumns.userNameColumn, "");
			if(tempUser.getUserName().equals(s) && user.getUserName().equals(s)){
				if(null!=mSettings){
					SharedPreferences.Editor editor = mSettings.edit();
					if(null!=user.getUserName() && !user.getUserName().isEmpty() ){
						editor.putLong(UserDOColumns.score, user.getScore());
						editor.putLong(UserDOColumns.numberOfCoinsTotal, user.getNumOfCoins());
						editor.putInt(UserDOColumns.numberOfGamesPlayed, user.getNumOfGamesPlayed());
						editor.putInt(UserDOColumns.numberOfGamesWon, user.getNumOfGamesWon());

					}
					editor.commit();
				}
			}
		}
	}

	public void saveThemePreferences(ThemePreference selectedTheme){
		if(null!=mSettings){
			SharedPreferences.Editor editor = mSettings.edit();
			editor.putString(ThemePreference.themeSelectionColumn, selectedTheme.getThemeSelection());
			editor.commit();
		}
	}

	public void saveStartUpAnimPref(final boolean bool) {
		Runnable run = new Runnable() {
			@Override
			public void run() {
				if(null!=mSettings){
					SharedPreferences.Editor editor = mSettings.edit();
					editor.putBoolean(UserDOColumns.startUpAnimationRequiredColumn, bool);
					editor.commit();
				}
			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run);

	}

	public UserDO getUser() {
		if(null!=tempUser && null!=tempUser.getUserName() && null!=tempUser.getDisplayName()){
			return tempUser;
		}
		else if(null!=ResourcesManager.getInstance().activity){
			Runnable run = new Runnable() {



				@Override
				public void run() {
					if(!loadAttemptMade){
						loadPrefs();
						loadAttemptMade=true;
					}
				}};
				ResourcesManager.getInstance().activity.runOnUiThread(run);
		}

		while(tempUser!=null && null==tempUser.getUserName()){
			if(null==ResourcesManager.getInstance().activity){
				break;
			}
			if(!toastInProgress){		
				ToastHelper.makeCustomToastOnUI("You Are Not Signed In,  Please Logout And Login Again", Toast.LENGTH_SHORT);
			}
			toastInProgress = true;
		}
		return tempUser;
	}

	public void saveUserReferencePreferences(String pReferrer, String pSourceContacted){
		if(null!=mSettings){
			SharedPreferences.Editor editor = mSettings.edit();
			editor.putString("referrer", pReferrer);
			editor.putString("sourceContacted",pSourceContacted);
			editor.commit();
		}
	}

	private String getReferrer() {
		return mSettings.getString("referrer", null);
	}

	public void saveUserReferredInstalls(String pReferrerInstalls){
		if(null!=mSettings){
			SharedPreferences.Editor editor = mSettings.edit();
			editor.putString("referrerInstalls", pReferrerInstalls);
			editor.commit();
		}
	}

	private String getReferreredInstalls() {
		return mSettings.getString("referrerInstalls", null);
	}

	public void loadPrefs(Activity splashActivity, String pReferrer, String pSourceContacted) {
		if(null!=splashActivity){
			mSettings = splashActivity.getSharedPreferences(PREFS_NAME, 0);

			//
			if(null!=pReferrer && !pReferrer.isEmpty()){
				saveUserReferencePreferences(pReferrer, pSourceContacted);
			}

			mHostname=mSettings.getString("hostname", ConnectionManager.mHostname);

			/*				referrer=mSettings.getString("referrer", "");
				sourceContacted=mSettings.getString("sourceContacted", "");*/
			mPath=mSettings.getString("path", ConnectionManager.mPath+ConnectionManager.mPathRoomNumberSuffix);

			//Campaigns
			if(null!=pReferrer && !pReferrer.isEmpty()){
				referrer=pReferrer;
			}
			else{
				referrer=getReferrer();
			}
			sourceContacted=pSourceContacted;
			
			String refferedInstalls =getReferreredInstalls();
			if(null!=refferedInstalls && !refferedInstalls.isEmpty() && refferedInstalls.equals("R")){
				ResourcesManager.isAdsEnabledRefBased =false;
			}
			tempUser.setReferrer(referrer);
			tempUser.setSourceContacted(sourceContacted);

			tempUser.setMusicMuted(mSettings.getBoolean(UserDOColumns.isMusicMutedColumn, false));
			tempUser.setSoundMuted(mSettings.getBoolean(UserDOColumns.isSoundMutedColumn, true));

			tempUser.setStartUpAnimationRequired(mSettings.getBoolean(UserDOColumns.startUpAnimationRequiredColumn, false));

			String randomUserName="WordPlayer#"+new Random().nextInt();
			tempUser.setUserName(mSettings.getString(UserDOColumns.userNameColumn, randomUserName));
			tempUser.setDisplayName(mSettings.getString(UserDOColumns.firstNameColumn, randomUserName));
			tempUser.setScore(mSettings.getLong(UserDOColumns.score, 0));
			tempUser.setUserProfilePicUrl(mSettings.getString(UserDOColumns.userImageUrl, ""));
			ResourcesManager.lastLogin= mSettings.getLong("LASTLOGIN", 0);

			//ResourcesManager.numberOfCoinsTotal = mSettings.getLong(UserDOColumns.numberOfCoinsTotal, 0);
			tempUser.setNumOfCoins(mSettings.getLong(UserDOColumns.numberOfCoinsTotal, 0));
			tempUser.setNumOfGamesPlayed(mSettings.getInt(UserDOColumns.numberOfGamesPlayed, 0));
			tempUser.setNumOfGamesWon(mSettings.getInt(UserDOColumns.numberOfGamesWon, 0));
			tempUser.setNumOfBees(mSettings.getInt(UserDOColumns.beesCount, 0));
			tempUser.setNumOfHoneyCombs(mSettings.getInt(UserDOColumns.honeycombCount, 0));
			tempUser.setNumOfHoneyCombsSmall(mSettings.getInt(UserDOColumns.honeycombSmallCount, 0));
			tempUser.setNumOfBullRuns(mSettings.getInt(UserDOColumns.bullsCount, 0));
			tempUser.setNumOfRedCape(mSettings.getInt(UserDOColumns.redCapeCount, 0));
			tempUser.toString();
			if(tempUser.getUserName().equals(randomUserName)){
				saveUser(tempUser);
			}
			ThemeManager.themePreferences.setThemeSelection(mSettings.getString(ThemePreference.themeSelectionColumn, THEMES.YELLOWFANTASY.toString()));

			AudioManager audio = (AudioManager) splashActivity.getSystemService(Context.AUDIO_SERVICE);
			switch( audio.getRingerMode() ){
			case AudioManager.RINGER_MODE_NORMAL:
				break;
			case AudioManager.RINGER_MODE_SILENT:
				tempUser.setMusicMuted(true);
				ToastHelper.makeCustomToast(splashActivity, "System Volume Is Muted, Please Enable Music From Within Game Options", Toast.LENGTH_LONG);
				break;
			case AudioManager.RINGER_MODE_VIBRATE:
				tempUser.setMusicMuted(true);
				ToastHelper.makeCustomToast(splashActivity, "System Volume Is Muted, Please Enable Music From Within Game Options", Toast.LENGTH_LONG);
				break;
			}
			tempUser.toString();
		}
	}

	/**
	 * @param i
	 */
	public void saveUserBeesBullsAndCoins(UserDO user) {
		if(null!=ResourcesManager.getInstance().activity){
			getUser().setStObject(false);
			mSettings = ResourcesManager.getInstance().activity.getSharedPreferences(PREFS_NAME, 0);
			String s= mSettings.getString(UserDOColumns.userNameColumn, "");
			if(tempUser.getUserName().equals(s) && user.getUserName().equals(s)){
				if(null!=mSettings){
					SharedPreferences.Editor editor = mSettings.edit();
					if(null!=user.getUserName() && !user.getUserName().isEmpty() ){
						editor.putInt(UserDOColumns.beesCount, user.getNumOfBees());
						editor.putInt(UserDOColumns.honeycombCount, user.getNumOfHoneyCombs());
						editor.putInt(UserDOColumns.honeycombSmallCount, user.getNumOfHoneyCombsSmall());
						editor.putInt(UserDOColumns.bullsCount, user.getNumOfBullRuns());
						editor.putInt(UserDOColumns.redCapeCount, user.getNumOfRedCape());
						editor.putLong(UserDOColumns.numberOfCoinsTotal, user.getNumOfCoins());
					}
					editor.commit();
				}
			}
		}
	}

}
