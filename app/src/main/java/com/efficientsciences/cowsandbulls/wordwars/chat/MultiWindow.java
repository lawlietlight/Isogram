package com.efficientsciences.cowsandbulls.wordwars.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import wei.mark.standout.StandOutWindow;
import wei.mark.standout.constants.StandOutFlags;
import wei.mark.standout.ui.Window;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.managers.ChatManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.textureMap.Chat;

/**
 * This implementation provides multiple windows. You may extend this class or
 * use it as a reference for a basic foundation for your own windows.
 * 
 * <p>
 * Functionality includes system window decorators, moveable, resizeable,
 * hideable, closeable, and bring-to-frontable.
 * 
 * <p>
 * The persistent notification creates new windows. The hidden notifications
 * restores previously hidden windows.
 * 
 * @author Mark Wei <markwei@gmail.com>
 * 
 */
public class MultiWindow extends StandOutWindow {

	@Override
	public String getAppName() {
		return "WORD";
	}

	@Override
	public int getAppIcon() {
		return R.drawable.ic_launcher_small;
	}
	
	@Override
	public boolean onClose(int id, Window window) {
		super.onClose(id, window);
		ChatManager.chatState=0;
		if(null!=ResourcesManager.getInstance().chat){
			ResourcesManager.getInstance().chat.setCurrentTileIndex(Chat.CHAT_INV_ID);
			ResourcesManager.getInstance().chat.setAlpha(1f);
		}
		return false;
	}

	@Override
	public String getTitle(int id) {
		return getAppName() + " ";
	}

	@Override
	public void createAndAttachView(int id, FrameLayout frame) {
		// create a new layout from body.xml
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.body, frame, true);

		TextView idText = (TextView) view.findViewById(R.id.idText);
		idText.setText(String.valueOf(id));
	}

	// every window is initially same size
	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		return new StandOutLayoutParams(id, 400, 300,
				StandOutLayoutParams.AUTO_POSITION,
				StandOutLayoutParams.AUTO_POSITION, 100, 100);
	}

	// we want the system window decorations, we want to drag the body, we want
	// the ability to hide windows, and we want to tap the window to bring to
	// front
	@Override
	public int getFlags(int id) {
		return StandOutFlags.FLAG_DECORATION_SYSTEM
				| StandOutFlags.FLAG_WINDOW_FOCUS_INDICATOR_DISABLE
				| StandOutFlags.FLAG_WINDOW_HIDE_ENABLE
				| StandOutFlags.FLAG_WINDOW_BRING_TO_FRONT_ON_TAP
				| StandOutFlags.FLAG_WINDOW_EDGE_LIMITS_ENABLE
				| StandOutFlags.FLAG_WINDOW_PINCH_RESIZE_ENABLE;
	}

	@Override
	public String getPersistentNotificationTitle(int id) {
		return getAppName() + " Window Open";
	}

	@Override
	public String getPersistentNotificationMessage(int id) {
		return " " + getAppName();
	}

	// return an Intent that creates a new MultiWindow
	@Override
	public Intent getPersistentNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), 0);
	}

	@Override
	public int getHiddenIcon() {
		return android.R.drawable.presence_online;
	}

	@Override
	public String getHiddenNotificationTitle(int id) {
		return getAppName() + " Hidden";
	}

	@Override
	public String getHiddenNotificationMessage(int id) {
		return "Click to restore";
	}

	// return an Intent that restores the MultiWindow
	@Override
	public Intent getHiddenNotificationIntent(int id) {
		return StandOutWindow.getShowIntent(this, getClass(), id);
	}

	@Override
	public Animation getShowAnimation(int id) {
		if (isExistingId(id)) {
			// restore
			return AnimationUtils.loadAnimation(this,
					android.R.anim.slide_in_left);
		} else {
			// show
			return super.getShowAnimation(id);
		}
	}

	@Override
	public Animation getHideAnimation(int id) {
		return AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right);
	}

	@Override
	public List<DropDownListItem> getDropDownItems(int id) {
		List<DropDownListItem> items = new ArrayList<DropDownListItem>();
		items.add(new DropDownListItem(android.R.drawable.ic_menu_help,
				"Add Friend", new Runnable() {

					@Override
					public void run() {
						Toast.makeText(
								MultiWindow.this,
								" Add Friends Feature In Progress",
								Toast.LENGTH_SHORT).show();
					}
				}));
		return items;
	}

	@Override
	public void onReceiveData(int id, int requestCode, Bundle data,
			Class<? extends StandOutWindow> fromCls, int fromId) {
		// receive data from WidgetsWindow's button press
		// to show off the data sending framework
		switch (requestCode) {
			case WidgetsWindow.DATA_CHANGED_TEXT:
				Window window = getWindow(id);
				if (window == null) {
					String errorText = String.format(Locale.US,
							"%s received data but Window id: %d is not open.",
							getAppName(), id);
					//Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
					return;
				}
				String changedText = data.getString("changedText");
				TextView status = (TextView) window.findViewById(R.id.idText);
				status.setTextSize(20);
				status.setText("Received data from WidgetsWindow: "
						+ changedText);
				break;
			default:
				Log.d("MultiWindow", "Unexpected data received.");
				break;
		}
	}
}
