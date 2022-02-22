/**
 * CancelNotifyReceiver.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.efficientsciences.cowsandbulls.wordwars.services.IMService;

/**
 * @author SATHISH
 *
 */
public class CancelNotifyReceiver extends BroadcastReceiver{
	 
	@Override
	 public void onReceive(Context context, Intent intent) {
		if(null!=IMService.mConnection && IMService.mConnection.isConnected())
		IMService.mConnection.disconnect();
	   NotificationManager mNM = (NotificationManager) context.getSystemService(IMService.NOTIFICATION_SERVICE);
	   mNM.cancelAll();
	   //context.stopService(service);
	 }
	 
}
