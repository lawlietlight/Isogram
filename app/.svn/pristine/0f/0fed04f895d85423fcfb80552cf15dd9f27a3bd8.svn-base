/**
 * AutoBootService.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars;

import com.efficientsciences.cowsandbulls.wordwars.services.IMService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author SATHISH
 *
 */
public class AutoBootService extends BroadcastReceiver
	{
	    public void onReceive(Context arg0, Intent arg1) 
	    {
	        Intent intent = new Intent(arg0,IMService.class);
	        arg0.startService(intent);
	        Log.i("Autostart", "started");
	    }
	}