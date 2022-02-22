package com.efficientsciences.cowsandbulls.wordwars.chat;

import wei.mark.standout.ui.Window;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ChatManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;

public class WidgetsWindow extends MultiWindow {
	public static final int DATA_CHANGED_TEXT = 0;

	@Override
	public void createAndAttachView(final int id, FrameLayout frame) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.main, frame, true);
		ChatManager.getInstance().view = view;
		/*final TextView status = (TextView) view.findViewById(R.id.status);
		final EditText edit = (EditText) view.findViewById(R.id.edit);
		final TextView chatHistory = (TextView) view.findViewById(R.id.chatHistory);*/

		//final TextView status = (TextView) view.findViewById(R.id.status);
		final EditText edit = (EditText) view.findViewById(R.id.text);
		ChatManager.getInstance().text = edit;
		ChatManager.getInstance().empty= (TextView) view.findViewById(R.id.empty);
		ChatManager.getInstance().mList = (ListView) view.findViewById(R.id.listNew);
		//final TextView chatHistory = (TextView) view.findViewById(R.id.chatHistory);
		if(ChatManager.getInstance().roomIndex != StorageManager.getInstance().getUser().getHostedGuessedRoomIndex()){
			ChatManager.getInstance().resetMessages();
		}
		//chatHistory.setText(Html.fromHtml(ResourcesManager.getInstance().changedText));
		Button button = (Button) view.findViewById(R.id.shout);
		ChatManager.getInstance().buttonShout = button;
		final UserDO user = StorageManager.getInstance().getUser();
		if(ChatManager.getInstance().messages==null || ChatManager.getInstance().messages.isEmpty()){
			user.setAutoLoginRequired(true);
		}
		else{
			user.setAutoLoginRequired(false);
		}
		sendMessageToServer("CHAT:: \n"+"1u*#z"+"<i>"+user.getDisplayName()
				+ " entered chat" +"</i> ");
		ChatManager.getInstance().empty.setText("");
		
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = edit.getText().toString();

				String changedText = "CHAT:: \n<b>"+StorageManager.getInstance().getUser().getDisplayName()+":</b> "
						+ text;

				//	status.setText("Shout Message Received");
				edit.setText("");
				if(ChatManager.getInstance().messages==null || ChatManager.getInstance().messages.isEmpty()){
					user.setAutoLoginRequired(true);
				}
				else{
					user.setAutoLoginRequired(false);
				}
				sendMessageToServer(changedText);
				ChatManager.getInstance().addNewMessage(new Message(text, true));
				// update MultiWindow:0 when button is pressed
				// to show off the data sending framework
				/*Bundle data = new Bundle();
				data.putString("changedText", changedText);
				sendData(id, MultiWindow.class, DEFAULT_ID, DATA_CHANGED_TEXT,
						data);*/
			}
		});
	}

	private void sendMessageToServer(final String msg) {
		Runnable run5 = new Runnable() {

			@Override
			public void run() {
				UserDO user = StorageManager.getInstance().getUser();
				if(null!=user){
					user.setWordGuessed(null);
					user.setWordHosted(null);
					user.setClueWord(msg);
					
				}
				if(ResourcesManager.getInstance().haveNetworkConnection()){
					if(ConnectionManager.getInstance().mConnection.isConnected()){
						ConnectionManager.getInstance().autobahnConnectorPubSub.publishClueToRoom(user);
						//Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
					}
					else{
						ConnectionManager.getInstance().prepareConnection();
						//	ConnectionManager.getInstance().autobahnConnectorPubSub.publishClueToRoom(user);
					}
				}
				else{
					ToastHelper.makeCustomToastOnUI("You Are Not Connected To Internet, \n FYI: This Is An Online Multiplayer Game! ", Toast.LENGTH_LONG);
				}
				//user.setAutoLoginRequired(false);
			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run5);
	}

	@Override
	public StandOutLayoutParams getParams(int id, Window window) {
		return new StandOutLayoutParams(id, (int)(400*ResourcesManager.resourceScaler), (int)(275*ResourcesManager.resourceScaler),
				StandOutLayoutParams.RIGHT, (int)(ResourcesManager.getInstance().windowDimensions.y-ResourcesManager.getInstance().yTextStart-275*ResourcesManager.resourceScaler));
	}

	@Override
	public String getAppName() {
		return "Chat" + " Room #: "+ConnectionManager.mPathRoomNumberSuffix;
	}

	@Override
	public int getThemeStyle() {
		return android.R.style.Theme_Holo_NoActionBar;
	}
}