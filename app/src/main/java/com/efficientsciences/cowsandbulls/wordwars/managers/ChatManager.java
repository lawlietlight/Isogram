package com.efficientsciences.cowsandbulls.wordwars.managers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.chat.AwesomeAdapter;
import com.efficientsciences.cowsandbulls.wordwars.chat.Message;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;



public class ChatManager {

	private static final ChatManager INSTANCE = new ChatManager();
	public static int roomIndex= -1;
	public static byte chatState;
	public static ChatManager getInstance() {
		return INSTANCE;
	}

	public final ArrayList<Message> messages = new ArrayList<Message>();

	/**
	 * This field should be made private, so it is hidden from the SDK.
	 * {@hide}
	 */
	/**
	 * This field should be made private, so it is hidden from the SDK.
	 * {@hide}
	 */
	public ListView mList;


	/*	chat specifics
	 */
	//ArrayList<Message> messages;
	AwesomeAdapter adapter;
	public EditText text;
	public TextView empty;
	public View view;
	public Button buttonShout; 
	/*	chat specifics
	 */

	public void resetMessages(){
		//	setContentView(R.layout.main);
		if(null!=view){
			if(null==text)
				text = (EditText) view.findViewById(R.id.text);

			if(null==empty)
				empty= (TextView) view.findViewById(R.id.empty);

		}

		if(empty!=null)
			empty.setText(R.string.main_empty_list);

		//mList = (ListView) ResourcesManager.getInstance().activity.findViewById(R.id.listNew);
		messages.clear();
		/*
		messages.add(new Message("Hello", false));
		messages.add(new Message("Hi!", true));
		messages.add(new Message("Wassup??", false));
		messages.add(new Message("nothing much, working on speech bubbles.", true));
		messages.add(new Message("you say!", true));
		messages.add(new Message("oh thats great. how are you showing them", false));
		 */
		
		adapter = new AwesomeAdapter(ResourcesManager.getInstance().activity.getBaseContext(), messages);
		synchronized (ResourcesManager.getInstance().activity) {
			ensureList();
			mList.setAdapter(adapter);
		}

	}

	/*public void sendMessage()
	{
		String newMessage = text.getText().toString().trim(); 
		empty.setText("");
		if(newMessage.length() > 0)
		{
			text.setText("");
			addNewMessage(new Message(newMessage, true));
			//new SendMessage().execute();
		}
	}*/
	/*private class SendMessage extends AsyncTask<Void, String, String>
	{
		@Override
		protected String doInBackground(Void... params) {
			try {
				Thread.sleep(2000); //simulate a network call
			}catch (InterruptedException e) {
				e.printStackTrace();
			}

			this.publishProgress(String.format("%s started writing", sender));
			try {
				Thread.sleep(2000); //simulate a network call
			}catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.publishProgress(String.format("%s has entered text", sender));
			try {
				Thread.sleep(3000);//simulate a network call
			}catch (InterruptedException e) {
				e.printStackTrace();
			}

			return " ";


		}
		@Override
		public void onProgressUpdate(String... v) {

			if(null!=messages)//check wether we have already added a status message
			{	
				Message tempM=messages.poll();
				if(tempM.isStatusMessage()){ //Status update disabled 
						tempM.setMessage(v[0]); //update the status for that
					adapter.notifyDataSetChanged(); 
					getListView().setSelection(messages.size()-1);
				}
				else{
					addNewMessage(tempM); //add new message, if there is no existing status message
				}
			}

		}
		@Override
		protected void onPostExecute(String text) {
			if(messages.peek().isStatusMessage())//check if there is any status message, now remove it.
			{
				messages.remove(messages.size()-1);
			}

			addNewMessage(new Message(text, false)); // add the orignal message from server.
		}


	}*/

	public void addNewMessage(Message m)
	{	
		if(!messages.contains(m)){
			messages.add(m);
		}
		else{
			for (Message message : messages) {
				if(m.equals(message)){
					message.setMessage(message.getMessage()+ " \u2713");
					break;
				}
			}
		}
		if(null!=adapter)
		adapter.notifyDataSetChanged();
		getListView().setSelection(messages.size()-1);
	}

	/**
	 * Get the activity's list view widget.
	 */
	public ListView getListView() {
		ensureList();
		return mList;
	}

	/**
	 * Get the ListAdapter associated with this activity's ListView.
	 */
	public ListAdapter getListAdapter() {
		return adapter;
	}

	private void ensureList() {
		if (mList != null) {
			return;
		}
	}

}