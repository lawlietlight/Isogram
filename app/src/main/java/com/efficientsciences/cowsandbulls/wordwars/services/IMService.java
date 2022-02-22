/* 
 * Copyright (C) 2007 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.efficientsciences.cowsandbulls.wordwars.services;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.http.AndroidHttpClient;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;

import com.efficientsciences.cowsandbulls.wordwars.AutoBootService;
import com.efficientsciences.cowsandbulls.wordwars.CancelNotifyReceiver;
import com.efficientsciences.cowsandbulls.wordwars.MainActivity;
import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.domain.FriendInfo;
import com.efficientsciences.cowsandbulls.wordwars.domain.MessageInfo;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomStream;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.domain.storage.UserDOColumns;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnectorPubSub;

import de.tavendo.autobahn.Wamp;
import de.tavendo.autobahn.WampCra;
import de.tavendo.autobahn.WampCraConnection;


/**
 * This is an application service that runs locally
 * in the same process as the application.  The {@link LocalServiceController}
 * and {@link LocalServiceBinding} classes show how to interact with the
 * service.
 *
 * <p>Notice the use of the {@link NotificationManager} when interesting things
 * happen in the service.  This is generally how background services should
 * interact with the user, rather than doing something more disruptive such as
 * calling startActivity().
 */
public class IMService extends Service implements IAppManager, IUpdateData {
	//	private NotificationManager mNM;

	public static String USERNAME;
	public static final String TAKE_MESSAGE = "Take_Message";
	public static final String FRIEND_LIST_UPDATED = "Take Friend List";
	public static final String MESSAGE_LIST_UPDATED = "Take Message List";
	public ConnectivityManager conManager = null; 
	private final int UPDATE_TIME_PERIOD = 15000;
	//	private static final INT LISTENING_PORT_NO = 8956;
	private String rawFriendList = new String();
	private String rawMessageList = new String();
	//
	private final String TAG = "WORD NOTIFY SERVICE ";
	public static int CONNECTED_VIA_SERVICE = 0;
	protected static final int FILL_BOTH_USERNAME_AND_PASSWORD = 1;
	public static final String AUTHENTICATION_FAILED = "0";
	public static final String FRIEND_LIST = "FRIEND_LIST";
	protected static final int MAKE_SURE_USERNAME_AND_PASSWORD_CORRECT = 2 ;
	protected static final int NOT_CONNECTED_TO_NETWORK = 3;
	public static final int SIGN_UP_ID = Menu.FIRST;
	public static final int EXIT_APP_ID = Menu.FIRST + 1;
	//
	private static final String PREFS_NAME = "WORDWARSTMSAVEDDATAEFFICIENTSCIENCES";

	private final IBinder mBinder = new IMBinder();
	private String username;
	private String password;
	private boolean authenticatedUser = false;
	// timer to take the updated data from server
	public AndroidHttpClient client;

	//private LocalStorageHandler localstoragehandler; 

	private NotificationManager mNM;
	public static boolean isAppRunning;
	public static boolean isGameFromServiceNotify;
	private static String hostUserName;
	public static WampCra mConnection;
	private SharedPreferences mSettings;



	public static void setHostUserName(String hostUserName) {
		IMService.hostUserName = hostUserName;
	}

	public class IMBinder extends Binder {
		public IAppManager getService() {
			return IMService.this;
		}

	}
	/*public boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}*/

	public boolean haveNetworkConnection() {
		return isNetworkConnected();
	}

	private boolean ACTION_SCREEN_ON_FLAG;
	private final BroadcastReceiver internetStateReceiverForService = new BroadcastReceiver() {


		@Override
		public void onReceive(Context context, Intent intent) {

			if(null!=intent && null!=intent.getAction()) {
				String action = intent.getAction();
				if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
					if (isNetworkConnected()) {
						CONNECTED_VIA_SERVICE = 1;
						establishConnectionAuthenticateSubscribeToRooms();
					}
				} else if (action.equals(android.net.ConnectivityManager.CONNECTIVITY_ACTION)) {
					//action for phone state changed
					if (isNetworkConnected()) {
						establishConnectionAuthenticateSubscribeToRooms();
					}
				} else if (action.equals(Intent.ACTION_SCREEN_ON)) {
					if (isNetworkConnected()) {
						ACTION_SCREEN_ON_FLAG = true;
						establishConnectionAuthenticateSubscribeToRooms();
					}
				} else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
					ACTION_SCREEN_ON_FLAG = false;
					if (isNetworkConnected() && null != mConnection) {
						mConnection.disconnect();
					}
				} else if (action.equals(Intent.ACTION_USER_PRESENT) && !ACTION_SCREEN_ON_FLAG) {
					if (isNetworkConnected()) {
						establishConnectionAuthenticateSubscribeToRooms();
					}
				}
			}
		}
	};
	@Override
	public void onCreate() 
	{   	
		IntentFilter filter = new IntentFilter();
		//	filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
		//	filter.addAction("android.intent.action.USER_PRESENT");
		filter.addAction(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_USER_PRESENT);

		registerReceiver(internetStateReceiverForService, filter);

		getStoredUserName();
		conManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		if(haveNetworkConnection()){
			if(CONNECTED_VIA_SERVICE==0 ){
				CONNECTED_VIA_SERVICE = 1;
				establishConnectionAuthenticateSubscribeToRooms();
			}
		}
		else{
			CONNECTED_VIA_SERVICE=0;
		}

		mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

		//localstoragehandler = new LocalStorageHandler(this);
		// Display a notification about us starting.  We put an icon in the status bar.
		//   showNotification();

		//new LocalStorageHandler(this);

		// Timer is used to take the friendList info every UPDATE_TIME_PERIOD;

		/*Thread thread = new Thread()
		{
			@Override
			public void run() {			

				//socketOperator.startListening(LISTENING_PORT_NO);
				Random random = new Random();
				int tryCount = 0;
			}
		};		
		thread.start();*/

	}

	/*
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(R.string.local_service_started);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }
	 */	

	@Override
	public IBinder onBind(Intent intent) 
	{
		isAppRunning=true;
		/*if(null!=mConnection && mConnection.isConnected()){
			mConnection.disconnect();
		}*/
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		isAppRunning=false;
		/*if(null!=mConnection && !mConnection.isConnected()){
			establishConnectionAuthenticateSubscribeToRooms();
		}*/
		return super.onUnbind(intent);
	};


	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if(haveNetworkConnection()){
			if(CONNECTED_VIA_SERVICE==0 ){
				CONNECTED_VIA_SERVICE = 1;
				establishConnectionAuthenticateSubscribeToRooms();
			}
		}
		else{
			CONNECTED_VIA_SERVICE=0;
		}

		return START_STICKY;
	};
	/*
	@Override
	public void onLowMemory() {
		this.stopSelf();
	};
	 */
	/**
	 * Show a notification while this service is running.
	 * @param roomDetailsFromServer 
	 **/
	private void showNotification(String username, final int roomNumber, RoomStream roomDetailsFromServer) 
	{       
		// Set the icon, scrolling text and TIMESTAMP

		String title = "New Challenge";

		String appendText = " challenged you to play in room # ";
		//boolean isChanged = false;


		if(username.equalsIgnoreCase(hostUserName) && !roomDetailsFromServer.isTimeSyncPulseFlag()){
			title = "Challenge Accepted";
			if(null!=roomDetailsFromServer.getParticipants() && !roomDetailsFromServer.getParticipants().isEmpty()){
				UserDO player= roomDetailsFromServer.getParticipants().get(roomDetailsFromServer.getParticipants().size()-1);
				if(null!=player && null!=player.getUserName() && !player.getUserName().isEmpty()){
					username= player.getDisplayName();

					/*String urlStringForReplace=player.getUserProfilePicUrl();
					if(null!=urlStringForReplace && !urlStringForReplace.isEmpty() && urlStringForReplace.indexOf("sz=50")!=-1){
						urlStringForReplace=urlStringForReplace.replace("sz=50", "sz=70");
						icon= downloadBitmap(urlStringForReplace);
					}*/

					//URL url = new URL(urlString);
					// Usage:
				}
			}
			//isChanged=true;
			appendText = " accepted your challenge in room # ";
		}
		else if(username.equalsIgnoreCase(hostUserName) && roomDetailsFromServer.isTimeSyncPulseFlag()){
			title = "Continue Play Here";
			appendText = " Click to spectate your room # ";
		}

		/*if(!isChanged){
			if(!username.equalsIgnoreCase(hostUserName)){
				if(null!=roomDetailsFromServer.getParticipants() && !roomDetailsFromServer.getParticipants().isEmpty()){
					for (UserDO player : roomDetailsFromServer.getParticipants()) {
						if(null!=player && null!=player.getUserName() && null!=player.getDisplayName() && !player.getUserName().isEmpty() && player.getUserName().equals(username)){
							username= player.getDisplayName();
						}
					}
				}
			}
		}*/
		if(null!=username){
			if(username.length()>5){
				username = username.substring(0, 5);
			}
			username=username.replaceAll("[^a-zA-Z0-9]", " ");
		}
		String text = username + appendText + roomNumber+" ";

		Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.icon_google_play_notify);
		icon = getResizedBitmap(icon, 70);

		Intent deleteIntent = new Intent(this, CancelNotifyReceiver.class);
		PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, deleteIntent, PendingIntent.FLAG_CANCEL_CURRENT);


		//NotificationCompat.Builder notification = new NotificationCompat.Builder(R.drawable.stat_sample, title,System.currentTimeMillis());
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher_small)
		.setLargeIcon(icon)
		.setContentTitle(title)
		.setContentText(text)
		.addAction(android.R.drawable.ic_menu_close_clear_cancel, "Mute notifications until next game", pendingIntentCancel);



		Intent in = new Intent(this, MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		in.putExtra(FriendInfo.USERNAME, username);
		in.putExtra(MessageInfo.ROOMNUMBER, roomNumber);	

		// The PendingIntent to launch our activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				in, PendingIntent.FLAG_CANCEL_CURRENT);

		// Set the info for the views that show in the notification panel.
		// msg.length()>15 ? MSG : msg.substring(0, 15);
		mBuilder.setContentIntent(contentIntent); 
		mBuilder.setSmallIcon(R.drawable.icon_google_play_notify);
		mBuilder.setContentText(text);
		mBuilder.setAutoCancel(true);
		mBuilder.setWhen(0);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			mBuilder.setPriority(Notification.PRIORITY_HIGH);
		}
		mBuilder.setDefaults(Notification.DEFAULT_LIGHTS) // requires VIBRATE permission

		/*
		 * Sets the big view "big text" style and supplies the
		 * text (the user's reminder message) that will be displayed
		 * in the detail area of the expanded notification.
		 * These calls are ignored by the support library for
		 * pre-4.1 devices.
		 */
		.setStyle(new NotificationCompat.BigTextStyle()
		.bigText(text));
		//TODO: it can be improved, for instance message coming from same user may be concatenated 
		// next version

		// Send the notification.
		// We use a layout id because it is a unique number.  We use it later to cancel.
		mNM.notify(0, mBuilder.build());
	}

	public String getUsername() {
		return this.username;
	}


	public String sendMessage(String  username, String  tousername, String message) throws UnsupportedEncodingException 
	{			
		String params = "username="+ URLEncoder.encode(this.username,"UTF-8") +
				"&password="+ URLEncoder.encode(this.password,"UTF-8") +
				"&to=" + URLEncoder.encode(tousername,"UTF-8") +
				"&message="+ URLEncoder.encode(message,"UTF-8") +
				"&action="  + URLEncoder.encode("sendMessage","UTF-8")+
				"&";		
		Log.i("PARAMS", params);
		return params;		
	}


	/*private String getFriendList() throws UnsupportedEncodingException 	{		
		// after authentication, server replies with friendList xml

		//rawFriendList = socketOperator.sendHttpRequest(getAuthenticateUserParams(username, password));
		if (rawFriendList != null) {
			this.parseFriendInfo(rawFriendList);
		}
		return rawFriendList;
	}*/

	/*private String getMessageList() throws UnsupportedEncodingException 	{		
		// after authentication, server replies with friendList xml

		//rawMessageList = socketOperator.sendHttpRequest(getAuthenticateUserParams(username, password));
		if (rawMessageList != null) {
			this.parseMessageInfo(rawMessageList);
		}
		return rawMessageList;
	}
	 */


	/**
	 * authenticateUser: it authenticates the user and if succesful
	 * it returns the friend list or if authentication is failed 
	 * it returns the "0" in string type
	 * @throws UnsupportedEncodingException 
	 * */
	public String authenticateUser(String usernameText, String passwordText) throws UnsupportedEncodingException 
	{
		this.username = usernameText;
		this.password = passwordText;	

		this.authenticatedUser = false;

		//String result = this.getFriendList(); //socketOperator.sendHttpRequest(getAuthenticateUserParams(username, password));
		/*if (result != null && !result.equals(AUTHENTICATION_FAILED)) 
		{			
			// if user is authenticated then return string from server is not equal to AUTHENTICATION_FAILED
			this.authenticatedUser = true;
			rawFriendList = result;
			USERNAME = this.username;
			Intent i = new Intent(FRIEND_LIST_UPDATED);					
			i.putExtra(FriendInfo.FRIEND_LIST, rawFriendList);
			sendBroadcast(i);

			timer.schedule(new TimerTask()
			{			
				public void run() 
				{
					try {					
						//rawFriendList = IMService.this.getFriendList();
						// sending friend list 
						Intent i = new Intent(FRIEND_LIST_UPDATED);
						Intent i2 = new Intent(MESSAGE_LIST_UPDATED);
						String tmp = IMService.this.getFriendList();
						String tmp2 = IMService.this.getMessageList();
						if (tmp != null) {
							i.putExtra(FriendInfo.FRIEND_LIST, tmp);
							sendBroadcast(i);	
							Log.i("friend list broadcast sent ", "");

							if (tmp2 != null) {
								i2.putExtra(MessageInfo.MESSAGE_LIST, tmp2);
								sendBroadcast(i2);	
								Log.i("friend list broadcast sent ", "");
							}
						}
						else {
							Log.i("friend list returned null", "");
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}					
				}			
			}, UPDATE_TIME_PERIOD, UPDATE_TIME_PERIOD);
		} */
		return "";		
	}

	public void messageReceived(String username, final int roomNumber, RoomStream roomDetailsFromServer) 
	{				

		//FriendInfo friend = FriendController.getFriendInfo(username);
		//MessageInfo msg = MessageController.checkMessage(username);
		if ( roomNumber != 0)
		{			
			/*Intent i = new Intent(TAKE_MESSAGE);

			i.putExtra(MessageInfo.USERNAME, username);			
			i.putExtra(MessageInfo.ROOMNUMBER, message);		
			sendBroadcast(i);*/
			//String activeFriend = FriendController.getActiveFriend();
			if (!isAppRunning && null!=username) 
			{
				//localstoragehandler.insert(username,this.getUsername(), message.toString());
				showNotification(username, roomNumber,roomDetailsFromServer);
			}

			Log.i("TAKE_MESSAGE broadcast", "");
		}	

	}  

	private String getAuthenticateUserParams(String usernameText, String passwordText) throws UnsupportedEncodingException 
	{			
		String params = "username=" + URLEncoder.encode(usernameText,"UTF-8") +
				"&password="+ URLEncoder.encode(passwordText,"UTF-8") +
				"&action="  + URLEncoder.encode("authenticateUser","UTF-8")+
				"&port="    + URLEncoder.encode("8080","UTF-8") +
				"&";		

		return params;		
	}

	public void setUserKey(String value) 
	{		
	}

	public boolean isNetworkConnected() {
		if(null!=conManager && null!=conManager.getActiveNetworkInfo()){
			return conManager.getActiveNetworkInfo().isConnected();
		}
		return false;
	}

	public boolean isUserAuthenticated(){
		return authenticatedUser;
	}

	public String getLastRawFriendList() {		
		return this.rawFriendList;
	}

	@Override
	public void onDestroy() {
		Log.i("IMService is  destroyed", "...");
		unregisterReceiver(internetStateReceiverForService);
		super.onDestroy();
	}

	public void exit() 
	{
		//timer.cancel();
		//socketOperator.exit(); 
		//socketOperator = null;
		this.stopSelf();
	}

	/*public String signUpUser(String usernameText, String passwordText,
			String emailText) 
	{
		String params = "username=" + usernameText +
				"&password=" + passwordText +
				"&action=" + "signUpUser"+
				"&email=" + emailText+
				"&";

		String result = socketOperator.sendHttpRequest(params);		

		return result;
	}*/

	public String addNewFriendRequest(String friendUsername) 
	{
		String params = "username=" + this.username +
				"&password=" + this.password +
				"&action=" + "addNewFriend" +
				"&friendUserName=" + friendUsername +
				"&";

		//String result = socketOperator.sendHttpRequest(params);		

		return params;
	}

	public String sendFriendsReqsResponse(String approvedFriendNames,
			String discardedFriendNames) 
	{
		String params = "username=" + this.username +
				"&password=" + this.password +
				"&action=" + "responseOfFriendReqs"+
				"&approvedFriends=" + approvedFriendNames +
				"&discardedFriends=" +discardedFriendNames +
				"&";

		//String result = socketOperator.sendHttpRequest(params);		

		return params;

	} 

	/*private void parseFriendInfo(String xml)
	{			
		try 
		{
			SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
			sp.parse(new ByteArrayInputStream(xml.getBytes()), new XMLHandler(IMService.this));		
		} 
		catch (ParserConfigurationException e) {			
			e.printStackTrace();
		}
		catch (SAXException e) {			
			e.printStackTrace();
		} 
		catch (IOException e) {			
			e.printStackTrace();
		}	
	}*/

	/*public void updateData(MessageInfo[] messages,FriendInfo[] friends,
			FriendInfo[] unApprovedFriends, String userKey) 
	{
		this.setUserKey(userKey);
		//FriendController.	
		//S.setMessagesInfo(messages);
		//Log.i("MESSAGEIMSERVICE","messages.length="+messages.length);

		int i = 0;
		while (i < messages.length){
			messageReceived(messages[i].userid,messages[i].messagetext);
			//appManager.messageReceived(messages[i].userid,messages[i].messagetext);
			i++;
		}


		FriendController.setFriendsInfo(friends);
		FriendController.setUnapprovedFriendsInfo(unApprovedFriends);

	}*/
	public static String prefixHostURI = AutoBahnConnectorPubSub.prefixHostURI; //Remote Server

	public void subscribeToNotifications(){
		mConnection.subscribe(prefixHostURI+"subscribedTopics/chat" + ConnectionManager.liveUpdateBroadcastIndex, RoomStream.class, new Wamp.EventHandler() {

			@Override
			public void onEvent(String topicUri, Object event) {
				try {
					if(!isAppRunning){
						final RoomStream roomDetailsFromServer = (RoomStream) event;

						//ConnectionManager.sentText = roomIndex;
						if(null!=roomDetailsFromServer){
							final int roomIndex = roomDetailsFromServer.getIndex();
							if(roomIndex!=0){
								Log.d(TAG, "roomIndex: "+roomIndex);
								if(null!=roomDetailsFromServer.getRoomHostedBy() && !roomDetailsFromServer.getRoomHostedBy().isEmpty() && ((hostUserName!=null && roomDetailsFromServer.getRoomHostedBy().equals(hostUserName)) || roomDetailsFromServer.isTimeSyncPulseFlag())){
									//	String parts[]=roomDetailsFromServer.split(":");
									/*if(isAppRunning){
										CONNECTED_VIA_SERVICE=0;
										if(null!=mConnection){
											mConnection.disconnect();
										}
									}*/
									String hostdBy= roomDetailsFromServer.getRoomHostedBy();


									// when we get an event, we  can safely cast to the type we
									// specified previously

									IMService.this.messageReceived(hostdBy, roomIndex,roomDetailsFromServer);
									return;
								}
							}
						}
					}
				}
				catch(Throwable t){
					CONNECTED_VIA_SERVICE = 0;
				}
			}
		});
	}

	public void establishConnectionAuthenticateSubscribeToRooms() {
		try{
			mConnection=new WampCraConnection();
			final String wsuri = "ws://" + ConnectionManager.mHostname +  "/" + ConnectionManager.mPath+ConnectionManager.mPathRoomNumberSuffix;

			ConnectionManager.mStatusline = "Connecting to\n" + wsuri + " ..";

			Log.e(TAG, "OPTIMISED");



			// we establish a connection by giving the WebSockets URL of the server
			// and the handler for open/close events
			mConnection.connect(wsuri, new WampCra.ConnectionHandler() {

				@Override
				public void onOpen() {
					try{
						// The connection was successfully established. we set the
						// status
						// and save the host/port as Android application preference for
						// next time.

						//Create Prefix
						mConnection.prefix(ConnectionManager.prefix+":", prefixHostURI+"subscribedTopics/");
						//Authenticate
						mConnection.authenticate(new WampCra.AuthHandler() {

							@Override
							public void onAuthSuccess(Object permissions) {

								try{
									ConnectionManager.mStatusline="Authenticated";

									//Subscribe To Random Host Guess Broadcasts For Individual Room 
									subscribeToNotifications();
								}
								catch(Throwable t){
									CONNECTED_VIA_SERVICE = 0;
								}

							}

							@Override
							public void onAuthError(String errorUri, String errorDesc) {

							}

						}, "foobar", "secret");
					}
					catch(Throwable t){
						CONNECTED_VIA_SERVICE = 0;
					}
				}

				@Override
				public void onClose(int code, String reason) {
					CONNECTED_VIA_SERVICE=0;
					if(null!=mConnection){
						mConnection.disconnect();
					}

				}
			});
		}
		catch(Throwable t){
			CONNECTED_VIA_SERVICE = 0;
		}
	}


	public void saveUserName(String userName) {
		try{
			if(null==mSettings){
				mSettings = this.getSharedPreferences(PREFS_NAME, 0);
			}
			SharedPreferences.Editor editor = mSettings.edit();
			if(null!=userName && !userName.isEmpty()){
				editor.putString(UserDOColumns.userNameColumn, userName);
				//editor.putLong("LASTLOGIN", Calendar.getInstance().getTimeInMillis());
			}
			editor.commit();
		}
		catch(Throwable t){
			CONNECTED_VIA_SERVICE = 0;
		}
	}

	public void getStoredUserName(){
		if(null==mSettings){
			mSettings = this.getSharedPreferences(PREFS_NAME, 0);
		}
		setHostUserName(mSettings.getString(UserDOColumns.userNameColumn, "temp"));
	}

	public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
		int width = image.getWidth();
		int height = image.getHeight();

		float bitmapRatio = (float)width / (float) height;
		if (bitmapRatio > 0) {
			width = maxSize;
			height = (int) (width / bitmapRatio);
		} else {
			height = maxSize;
			width = (int) (height * bitmapRatio);
		}
		return Bitmap.createScaledBitmap(image, width, height, true);
	}

	/*Bitmap downloadBitmap(String url) {
		final HttpGet getRequest = new HttpGet(url);
		try {
			client = AndroidHttpClient.newInstance("Android");
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("ImageDownloader", "Error " + statusCode
						+ " while retrieving bitmap from " + url);
				return null;
			}

			final HttpEntity entity = response.getEntity();
			if (entity != null) {
				InputStream inputStream = null;
				try {
					inputStream = entity.getContent();
					final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
					return bitmap;
				} finally {
					if (inputStream != null) {
						inputStream.close();
					}
					entity.consumeContent();
				}
			}
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("ImageDownloader", "Error while retrieving bitmap from " + url);
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	}*/

}