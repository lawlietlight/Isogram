/**
 * AppRater.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.helper;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import eu.inmite.android.lib.dialogs.SimpleDialogFragment;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.text.AndroidCharacter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author SATHISH
 *
 */
public class AppRater{
    private final static String APP_TITLE = "WORD";
    public final static String APP_PNAME = "com.efficientsciences.cowsandbulls.wordwars";
    
    private final static int DAYS_UNTIL_PROMPT = 3;
    private final static int LAUNCHES_UNTIL_PROMPT = 3;
    public static SharedPreferences.Editor editor;
    public static DialogFragment mBuildDialog1;
    public static void app_launched(Context mContext, boolean gameWon) {
        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) { return ; }
        
        editor = prefs.edit();
        
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }
        
        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT || (launch_count >= LAUNCHES_UNTIL_PROMPT+2 &&  launch_count%5 ==0)) {
        	if(gameWon){
        		if(android.os.Build.VERSION.SDK_INT >= 11){
        			runOnHoneyCombPlus();
        		}
        		else
        		showRateDialog(mContext, editor);
            }
            if (System.currentTimeMillis() >= date_firstLaunch + 
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
            	if(android.os.Build.VERSION.SDK_INT >= 11){
        			runOnHoneyCombPlus();
        		}
        		else
                showRateDialog(mContext, editor);
            }
            
        }
        
        editor.commit();
    }   
    
    public static void showRateDialog(final Context mContext, final SharedPreferences.Editor editor) {
        final Dialog dialog = new Dialog(mContext);
        dialog.setTitle("Rate " + APP_TITLE);

        LinearLayout ll = new LinearLayout(mContext);
        ll.setOrientation(LinearLayout.VERTICAL);
        
        TextView tv = new TextView(mContext);
        tv.setText("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support!");
        tv.setWidth(240);
        tv.setPadding(4, 0, 4, 10);
        ll.addView(tv);
        
        Button b1 = new Button(mContext);
        b1.setText("Rate " + APP_TITLE);
        b1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME)));
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });        
        ll.addView(b1);

        Button b2 = new Button(mContext);
        b2.setText("Remind me later");
        b2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ll.addView(b2);

        Button b3 = new Button(mContext);
        b3.setText("No, thanks");
        b3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (editor != null) {
                    editor.putBoolean("dontshowagain", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
        ll.addView(b3);

        dialog.setContentView(ll);        
        dialog.show();        
    }
    

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static void runOnHoneyCombPlus(){
		mBuildDialog1 =  SimpleDialogFragment.createBuilder(ResourcesManager.getInstance().activity, ResourcesManager.getInstance().activity.getFragmentManager())
				.setTitle("RATE US")
				.setMessage("If you enjoy using " + APP_TITLE + ", please take a moment to rate it. Thanks for your support! \n").setCancelable(true).setPositiveButtonText("Rate " + APP_TITLE).setNeutralButtonText("Remind me later").setNegativeButtonText("No thanks").setRequestCode(2).setTag("Rate-Us").show();

	}


}
// see http://androidsnippets.com/prompt-engaged-users-to-rate-your-app-in-the-android-market-appirater