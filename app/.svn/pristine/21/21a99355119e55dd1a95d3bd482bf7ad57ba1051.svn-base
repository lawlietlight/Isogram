/**
 * ToastHelper.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.helper;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.MainActivity;
import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.SplashActivity;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;

/**
 * @author SATHISH
 *
 */
public class ToastHelper {


	public static void makeToastOnUI(final String text,final int duration){
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(ResourcesManager.getInstance().activity, text, duration).show();
			}
		});
	}

	public static void makeToast(final String text,final int duration){
		Toast.makeText(ResourcesManager.getInstance().activity, text, duration).show();
	}

	public static void makeCustomToast(final String text,final int duration){
		if(null==ResourcesManager.layout ){
			ResourcesManager.inflater = ResourcesManager.getInstance().activity.getLayoutInflater();
			ResourcesManager.layout = ResourcesManager.inflater.inflate(R.layout.custom_toast, (ViewGroup) ResourcesManager.getInstance().activity.findViewById(R.id.toast_layout));
			ResourcesManager.textShown=((TextView) ResourcesManager.layout.findViewById(R.id.toast_text_1));

			ResourcesManager.toast = new Toast(ResourcesManager.getInstance().activity.getBaseContext());

			ResourcesManager.toast.setView(ResourcesManager.layout);
		}
		ResourcesManager.textShown.setText(text);
		ResourcesManager.toast.setDuration(duration);
		ResourcesManager.toast.show();
	}

	public static void makeCustomToastOnUI(final String text,final int duration){
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(null==ResourcesManager.layout ){
					ResourcesManager.inflater = ResourcesManager.getInstance().activity.getLayoutInflater();
					ResourcesManager.layout = ResourcesManager.inflater.inflate(R.layout.custom_toast, (ViewGroup) ResourcesManager.getInstance().activity.findViewById(R.id.toast_layout));

					ResourcesManager.textShown=((TextView) ResourcesManager.layout.findViewById(R.id.toast_text_1));

					ResourcesManager.toast = new Toast(ResourcesManager.getInstance().activity.getBaseContext());
					ResourcesManager.toast.setGravity(Gravity.TOP, 0, 0);
					ResourcesManager.toast.setView(ResourcesManager.layout);
				}
				ResourcesManager.textShown.setText(text);
				ResourcesManager.toast.setDuration(duration);

				ResourcesManager.toast.show();
			}
		});
	}
	public static void makeCustomToastOnUI(final String text,final int duration, final Activity activity){
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(null==ResourcesManager.layout ){
					ResourcesManager.inflater = activity.getLayoutInflater();
					ResourcesManager.layout = ResourcesManager.inflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_layout));

					ResourcesManager.textShown=((TextView) ResourcesManager.layout.findViewById(R.id.toast_text_1));

					ResourcesManager.toast = new Toast(activity.getBaseContext());
					ResourcesManager.toast.setGravity(Gravity.TOP, 0, 0);
					ResourcesManager.toast.setView(ResourcesManager.layout);
				}
				ResourcesManager.textShown.setText(text);
				ResourcesManager.toast.setDuration(duration);

				ResourcesManager.toast.show();
			}
		});
	}
	public static void makeCustomToastOnUIDefinedColor(final String text,final int duration){
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				LayoutInflater inflater;
				Toast toast;
				View layout ;
				TextView textShown;
				/*int[] colors1 = {ResourcesManager.getInstance().activity.getResources().getColor(R.color.reconnectColor), ResourcesManager.getInstance().activity.getResources().getColor(R.color.AccentColor)};

				GradientDrawable red = new GradientDrawable(Orientation.TOP_BOTTOM, colors1);

				ResourcesManager.getInstance().activity.findViewById(R.id.toast_color_gradient);
				GradientDrawable shape = (GradientDrawable)ResourcesManager.getInstance().activity.getResources().getDrawable(R.drawable.rounded_rect_colored);
				 */
				//shadow.setBounds(0,98, 0, 0);
				inflater = ResourcesManager.getInstance().activity.getLayoutInflater();
				layout = inflater.inflate(R.layout.custom_toast_red, (ViewGroup) ResourcesManager.getInstance().activity.findViewById(R.id.toast_layout_red));

				textShown=((TextView) layout.findViewById(R.id.toast_text_red));

				toast = new Toast(ResourcesManager.getInstance().activity.getBaseContext());
				toast.setGravity(Gravity.TOP, 0, 0);
				toast.setView(layout);
				textShown.setText(text);
				toast.setDuration(duration);

				toast.show();
			}
		});
	}

	public static void makeCustomToastOnUIFBColor(final String text,final int duration, final MainActivity activity){
		if(ResourcesManager.getInstance().activity==null){
			ResourcesManager.getInstance().activity = activity;
		}
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				LayoutInflater inflater;
				Toast toast;
				View layout ;
				TextView textShown;
				inflater = ResourcesManager.getInstance().activity.getLayoutInflater();
				layout = inflater.inflate(R.layout.custom_toast_fb, (ViewGroup) ResourcesManager.getInstance().activity.findViewById(R.id.toast_layout_fb));

				textShown=((TextView) layout.findViewById(R.id.toast_text_fb));

				toast = new Toast(ResourcesManager.getInstance().activity.getBaseContext());
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.setView(layout);
				textShown.setText(text);
				toast.setDuration(duration);

				toast.show();
			}
		});
	}

	public static void makeCustomToastForCoin(final String text,final int duration){
		LayoutInflater inflater;
		Toast toast;
		View layout ;
		TextView textShown;
		/*int[] colors1 = {ResourcesManager.getInstance().activity.getResources().getColor(R.color.reconnectColor), ResourcesManager.getInstance().activity.getResources().getColor(R.color.AccentColor)};

				GradientDrawable red = new GradientDrawable(Orientation.TOP_BOTTOM, colors1);

				ResourcesManager.getInstance().activity.findViewById(R.id.toast_color_gradient);
				GradientDrawable shape = (GradientDrawable)ResourcesManager.getInstance().activity.getResources().getDrawable(R.drawable.rounded_rect_colored);
		 */
		//shadow.setBounds(0,98, 0, 0);
		inflater = ResourcesManager.getInstance().activity.getLayoutInflater();
		layout = inflater.inflate(R.layout.custom_toast_image, (ViewGroup) ResourcesManager.getInstance().activity.findViewById(R.id.toast_layout_image));

		textShown=((TextView) layout.findViewById(R.id.toast_text_image));

		toast = new Toast(ResourcesManager.getInstance().activity.getBaseContext());
		toast.setGravity(Gravity.TOP, 0, 0);
		toast.setView(layout);
		textShown.setText(text);
		toast.setDuration(duration);

		toast.show();

	}
	/**
	 * @param splashActivity
	 * @param string
	 * @param lengthLong
	 */
	public static void makeCustomToast(Activity splashActivity,
			String string, int lengthLong) {
		LayoutInflater inflater;
		Toast toast;
		View layout ;
		TextView textShown;

		inflater = splashActivity.getLayoutInflater();
		layout = inflater.inflate(R.layout.custom_toast_white, (ViewGroup) splashActivity.findViewById(R.id.toast_layout_white));
		textShown=((TextView) layout.findViewById(R.id.toast_text_1_white));

		toast = new Toast(splashActivity.getBaseContext());

		toast.setView(layout);
		textShown.setText(string);
		toast.setDuration(lengthLong);
		toast.show();

	}
}
