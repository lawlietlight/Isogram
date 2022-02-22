/**
 * NotificationManager.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.managers;

import org.andengine.entity.primitive.Gradient;
import org.andengine.util.adt.color.Color;

import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.domain.Notification;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager.MusicPlayed;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;


/**
 * @author SATHISH
 *
 */
public class NotificationManager {

	public static final NotificationManager INSTANCE = new NotificationManager();
	Notification hostNotification;
	public Notification notificationCreatedForFB;
	public Notification notificationCreatedForTutorial;

	public static NotificationManager getInstance() {
		return INSTANCE;
	}

	public void createUserWonNotificationForHost(UserDO user){
		notificationCreatedForFB=null;
		notificationCreatedForTutorial=null;
		//calculateAdsSkippingForFBPosts();
		if(null!=ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs){
			synchronized (ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs) {
				ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs.clear();
			}
		}
		ResourcesManager.getInstance().numberOfLettersHosted = 0;
		SoundManager.getInstance().createBlurpSound();
		if(ThemeManager.getInstance().themesKey.equals(THEMES.YELLOWFANTASY.toString()))
			hostNotification = new Notification(ResourcesManager.getInstance().windowDimensions.x/2f, ResourcesManager.getInstance().windowDimensions.y/2f, ResourcesManager.getInstance().windowDimensions.x/2f, (float)(ResourcesManager.getInstance().windowDimensions.y/2), ResourcesManager.getInstance().blueHeart_region, ResourcesManager.getInstance().vbom, "Player "+user.getDisplayName()+" Guessed Your Word Right",user);
		else
			hostNotification = new Notification(ResourcesManager.getInstance().windowDimensions.x/2f, ResourcesManager.getInstance().windowDimensions.y/2f, ResourcesManager.getInstance().windowDimensions.x/2f, (float)(ResourcesManager.getInstance().windowDimensions.y/2), ResourcesManager.getInstance().blueHoney_region, ResourcesManager.getInstance().vbom, "Player "+user.getDisplayName()+" Guessed Your Word Right",user);

		hostNotification.setTag(EntityTagManager.backButtonNotifier);
		//ResourcesManager.numberOfGamesPlayed++;
		ResourcesManager.numberOfAdsSkipped++;

		StorageManager.getInstance().getUser().setNumOfGamesPlayed(StorageManager.getInstance().getUser().getNumOfGamesPlayed()+1);
		//commented
		/*if((ResourcesManager.numberOfAdsSkipped>=3 || !ConnectionManager.isNormalFlow()) && ResourcesManager.isAdsEnabled && ResourcesManager.isAdsEnabledRefBased){
			ResourcesManager.numberOfAdsSkipped=0;
			//commented
			ResourcesManager.getInstance().createStartAppInterstials();
		}*/

		//this.registerTouchArea(notifier);
		ResourcesManager.getInstance().gameWon = false;
		//this.getMenuChildScene().attachChild(notifier);
		hostNotification.setIgnoreUpdate(false);
		calculateAdsSkippingForFBPosts();
	}

	public void createFBLoginRequiredNotification(byte inviteOrPlay){
		SoundManager.getInstance().createBlurpSound();
		notificationCreatedForFB = new Notification(ResourcesManager.getInstance().windowDimensions.x/2f, ResourcesManager.getInstance().windowDimensions.y/2f, ResourcesManager.getInstance().windowDimensions.x/2f, (float)(ResourcesManager.getInstance().windowDimensions.y/2), ResourcesManager.getInstance().fbloginBG_region,inviteOrPlay, ResourcesManager.getInstance().vbom);
		notificationCreatedForFB.setTag(EntityTagManager.backButtonNotifier);
		//this.getMenuChildScene().attachChild(notifier);
		notificationCreatedForFB.setIgnoreUpdate(false);
	}

	public void createTutorial(){
		SoundManager.getInstance().createBlurpSound();
		notificationCreatedForTutorial = new Notification((float)(ResourcesManager.getInstance().windowDimensions.x/2f), ResourcesManager.getInstance().windowDimensions.y/2f, (float)(ResourcesManager.getInstance().windowDimensions.x/1.25f), (float)(ResourcesManager.getInstance().windowDimensions.y/1.25f), ResourcesManager.getInstance().vbom);
		notificationCreatedForTutorial.setTag(EntityTagManager.backButtonNotifier);
		//this.getMenuChildScene().attachChild(notifier);
		notificationCreatedForTutorial.setIgnoreUpdate(false);
	}

	public void createUserWonNotificationForWinner(UserDO user){
		notificationCreatedForFB=null;
		notificationCreatedForTutorial=null;
		if(null!=ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs){
			synchronized (ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs) {
				ConnectionManager.getInstance().autobahnConnectorPubSub.mPubs.clear();
			}
		}

		ResourcesManager.getInstance().numberOfLettersHosted = 0;
		if(null!=ResourcesManager.getInstance().gameWonBy){
			if(!ResourcesManager.getInstance().gameWonBy.equalsIgnoreCase("you")){
				SoundManager.changeMusic(MusicPlayed.LOST);
			}
			else{
				ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(ResourcesManager.getInstance().activity, "WOW, YOU WON!", Toast.LENGTH_SHORT).show();

					}
				});
				SoundManager.getInstance().createBlurpSound();
			}
		}

		if(ThemeManager.getInstance().themesKey.equals(THEMES.YELLOWFANTASY.toString()))
			hostNotification = new Notification(ResourcesManager.getInstance().windowDimensions.x/2f, ResourcesManager.getInstance().windowDimensions.y/2f, ResourcesManager.getInstance().windowDimensions.x/2f, (float)(ResourcesManager.getInstance().windowDimensions.y/2), ResourcesManager.getInstance().greenHeart_region, ResourcesManager.getInstance().vbom, ResourcesManager.getInstance().gameWonBy+" "+"Guessed It Right \n The Hosted word was "+user.getWordGuessed() ,user);
		else{
			hostNotification = new Notification(ResourcesManager.getInstance().windowDimensions.x/2f, ResourcesManager.getInstance().windowDimensions.y/2f, ResourcesManager.getInstance().windowDimensions.x/2f, (float)(ResourcesManager.getInstance().windowDimensions.y/2), ResourcesManager.getInstance().yellowHoney_region, ResourcesManager.getInstance().vbom, ResourcesManager.getInstance().gameWonBy+" "+"Guessed It Right \n The Hosted word was "+user.getWordGuessed() ,user);

		}
		
		
		hostNotification.setTag(EntityTagManager.backButtonNotifier);
		//this.registerTouchArea(notifier);
		//ResourcesManager.numberOfGamesPlayed++;
		//ResourcesManager.numberOfGamesWon++;
		ResourcesManager.numberOfAdsSkipped++;
		StorageManager.getInstance().getUser().setNumOfGamesPlayed(StorageManager.getInstance().getUser().getNumOfGamesPlayed()+1);
		StorageManager.getInstance().getUser().setNumOfGamesWon(StorageManager.getInstance().getUser().getNumOfGamesWon()+1);

		if(null!=ResourcesManager.getInstance().gameWonBy && !ResourcesManager.getInstance().gameWonBy.equalsIgnoreCase("you")){
			//commented
			if((ResourcesManager.numberOfAdsSkipped>=2  || !ConnectionManager.isNormalFlow()) && ResourcesManager.isAdsEnabled && ResourcesManager.isAdsEnabledRefBased){
				ResourcesManager.numberOfAdsSkipped=0;
				//commented
				//ResourcesManager.getInstance().createStartAppInterstials();
			}
			else if(StorageManager.getInstance().getUser().getNumOfGamesPlayed()>6){
				ResourcesManager.numberOfAdsSkipped=0;
			}
		}
		else{
			StorageManager.getInstance().getUser().setScore(StorageManager.getInstance().getUser().getScore()+1);
		}

		ResourcesManager.getInstance().gameWon = false;
		ResourcesManager.getInstance().gameWonBy = "";
		//this.getMenuChildScene().attachChild(notifier);
		hostNotification.setIgnoreUpdate(false);
		calculateAdsSkippingForFBPosts();
	}

	/**
	 * 
	 */
	private void calculateAdsSkippingForFBPosts() {
		if(ResourcesManager.numberOfPlaysToSkipAd>0){
			ResourcesManager.numberOfPlaysToSkipAd--;
		}
		if(ResourcesManager.numberOfPlaysToSkipAd==0){
			ResourcesManager.isAdsEnabled = true;
		}
	}
}
