/**
 * ReferrerBroadcastReceiver.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.analytics.CampaignTrackingReceiver;

/**
 * @author SATHISH
 *
 */
public class ReferrerBroadcastReceiver  extends BroadcastReceiver {



	  @Override
	  public void onReceive(Context context, Intent intent) {

	    // Pass the intent to other receivers.

	    // When you're done, pass the intent to the Google Analytics receiver.
	    new CampaignTrackingReceiver().onReceive(context, intent);
	    Log.e("referrer campaign received ", ""+intent.getExtras());
	  }
	
}

