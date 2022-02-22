package com.efficientsciences.cowsandbulls.wordwars.managers;

import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnectorPubSub;

import de.tavendo.autobahn.WampCra;
import de.tavendo.autobahn.WampCraConnection;


public class ConnectionManager {

	private static final ConnectionManager INSTANCE = new ConnectionManager();
	public static String prefix="userprefix";
	public final static String prefix1="userprefix";
	public final static String prefix2="userprefix2";

	public static ConnectionManager getInstance() {
		return INSTANCE;
	}


	public AutoBahnConnectorPubSub autobahnConnectorPubSub;
	public final WampCra mConnection = new WampCraConnection();
	public SharedPreferences mSettings;

	//public static String mHostname = "127.0.0.1:8080"; //localhost
	//public static String mHostname = "picshare.sytes.net"; //For Local
	//public static final String mHostname = "www.wordwithworld.com:8080"; //Remote server Host
	//public static final String mHostname = "74.208.164.102:8080"; //To Change

	//public static final String mHost1 = "www.wordwithworld.com"; //Remote server Single POC Host
	//public static final String mPort1 = "8080"; //Remote server Single POC Host port

	public static final String mHost1 ="192.168.0.131";
	public static String mPort1 = "8085";

	//public static final String mHost ="192.168.0.6";
	//public static String mPort = "8083";
	public static final String mHost2 ="192.168.0.131";
	public static String mPort2 = "8085";

	public static String mHost = mHost2; //Remote server Single POC Host
	public static String mPort = mPort2; //Remote server Single POC Host port
	public static String mHostname = mHost+":"+mPort; //To Change

	public static final String mHostname1 = mHost1+":"+mPort1; //To Change
	public static final String mHostname2 = mHost2+":"+mPort2; //To Change

	public static String mPath = "chat";
	public static String suffixEventMarker = "#usereventword";
	public static int liveUpdateBroadcastIndex = 50000;
	public static int mPathRoomNumberSuffix = 1;
	public static String mStatusline = "Welcome";
	public static String myId;
	public static String sentText = "";
	public static int noConnectionAttempts;
	public static long LastConnectedTimestamp;
	public static String LastPublishedWord = "";
	public static final long TIME_TO_REISSUEPUBLISH = 4500;
	//public final static AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
	public static final String USEREVENTNORMAL = "#usereventword";
	public static final String USEREVENTCHALLENGE = "#usereventchallenge";
	
	public boolean prepareConnection(){
		if(ResourcesManager.getInstance().haveNetworkConnection()){
			if(autobahnConnectorPubSub == null){
				autobahnConnectorPubSub = new AutoBahnConnectorPubSub();
				autobahnConnectorPubSub.establishConnectionAuthenticateSubscribeToRooms();
				Log.i("Server Connected 1", mConnection.isConnected()+" Initial Call");
				return true;
			}
			else if(!ResourcesManager.isResponseGot && mConnection.isConnected()){
				autobahnConnectorPubSub.disconnectAndReconnectAgain();
				//autobahnConnectorPubSub.test();
				Log.e("Server Connected 2", mConnection.isConnected()+" Disconnected call");
				return true;
			}
			else if(!mConnection.isConnected()){
				autobahnConnectorPubSub.establishConnectionAuthenticateSubscribeToRooms();
				Log.e("Server Connected 3", mConnection.isConnected()+" Connection Closed call");
				return true;
			}
			else{
				Log.e("Server Connected 4", mConnection.isConnected()+" Server state unknown");
				return false;
			}
			
		}
		else{
			ToastHelper.makeCustomToastOnUI("You Are Not Connected To Internet, \n FYI: This Is An Online Multiplayer Game! ", Toast.LENGTH_LONG);
			return true;
		}

	}

	/**
	 * @return
	 */
	public static boolean isNormalFlow() {
		return USEREVENTNORMAL.equals(suffixEventMarker);
	}
}
