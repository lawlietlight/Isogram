package com.efficientsciences.cowsandbulls.wordwars.socketCon;
/*
 * @author Sathish
 */
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.codehaus.jackson.type.TypeReference;

import de.tavendo.autobahn.WebSocket;
import wei.mark.standout.StandOutWindow;
import wei.mark.standout.ui.Window;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.chat.Message;
import com.efficientsciences.cowsandbulls.wordwars.chat.WidgetsWindow;
import com.efficientsciences.cowsandbulls.wordwars.domain.KeyboardChar;
import com.efficientsciences.cowsandbulls.wordwars.domain.Level;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomProperties;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomStream;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.graphics.LineMoveModifier;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ChatManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.Constants;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.FacebookManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.TimerManager;
import com.efficientsciences.cowsandbulls.wordwars.scenes.BaseScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.GuessScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.RealGameInterface;
import com.efficientsciences.cowsandbulls.wordwars.scenes.WaitingScene;
import com.efficientsciences.cowsandbulls.wordwars.services.IMService;
import com.efficientsciences.cowsandbulls.wordwars.textureMap.Chat;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.Achievements;
import com.google.android.gms.games.achievement.Achievements.LoadAchievementsResult;

import de.tavendo.autobahn.Wamp;
import de.tavendo.autobahn.Wamp.ConnectionHandler;
import de.tavendo.autobahn.WampConnection;
import de.tavendo.autobahn.WampCra;

public class AutoBahnConnectorPubSub {

	//final String wsuri = "ws://127.0.0.1:8080/chat";  //localhost
	//final static String prefixHostURI = "http://127.0.0.1/";  //localhost
	//final String wsuri = "ws://picshare.sytes.net/chat"; //local
	//final static String prefixHostURI = "http://picshare.sytes.net/"; //local
	static String wsuri = "ws://"+ConnectionManager.mHostname+"/chat"; //local To Change
	public static String prefixHostURI = "http://"+ConnectionManager.mHost+"/"; //local To Change
	//final String wsuri = "ws://www.wordwithworld.com:8080/chat"; //Remote Server
	//final static String prefixHostURI = "http://www.wordwithworld.com/"; //Remote Server
	//final String wsuri = "ws://74.208.164.102/chat"; //To Change
	//final static String prefixHostURI = "http://74.208.164.102/"; //To Change


	private ConnectionHandler connectionHandler;
	static final String TAG = "efficientsciences";
	private static final String PREFS_NAME = "WORDWARSSAVEDDATA";
	//Queue those publishes And Republish If No Answer
	public final ConcurrentLinkedQueue<Object> mPubs = new ConcurrentLinkedQueue<Object>();
	public static KeyboardChar keyClear;
	static String topicSelected = "chat";
	private PublishResponseMonitoringTask  queueTaskTracker;
	private ParallelEntityModifier parallelModifier;
	private IEntityModifierListener parallelModifierListener;

	//Order Of Call 1
	public void establishConnectionAuthenticateSubscribeToRooms() {

		final String wsuri = "ws://" + ConnectionManager.mHostname +  "/" + ConnectionManager.mPath+ConnectionManager.mPathRoomNumberSuffix;

		ConnectionManager.mStatusline = "Connecting to\n" + wsuri + " ..";

		Log.e(TAG, ConnectionManager.mStatusline);



		// we establish a connection by giving the WebSockets URL of the server
		// and the handler for open/close events
		ConnectionManager.getInstance().mConnection.connect(wsuri, new WampCra.ConnectionHandler() {

			@Override
			public void onOpen() {
				ConnectionManager.noConnectionAttempts=0;
				ResourcesManager.foundAttempts = 0;
				// The connection was successfully established. we set the
				// status
				// and save the host/port as Android application preference for
				// next time.
				ConnectionManager.mStatusline ="Connected to\n" + wsuri;

				//Create Prefix
				ConnectionManager.getInstance().mConnection.prefix(ConnectionManager.prefix, prefixHostURI+"subscribedTopics/");
				connectionHandler = this;
				//Authenticate
				ConnectionManager.getInstance().mConnection.authenticate(new WampCra.AuthHandler() {

					@Override
					public void onAuthSuccess(Object permissions) {

						ConnectionManager.mStatusline="Authenticated";
						Log.e("mStatusline", ConnectionManager.mStatusline);
						ConnectionManager.myId = permissions.toString();
						//testRpc();
						//testPubSub();

						//This Tracks weather we remain Connected to server or not 
						// TaskResponse class tracks and keeps us connected
						ResourcesManager.isResponseGot=true;

						//Initial Call To Fetch ALl Room States Once Logged in
						fetchAllRoomsState();

						//Subscribe To Random Host Guess Broadcasts For Individual Room 
						sendRoomHostedBroadcastSubscription();

						if(ResourcesManager.isFaceBookRequestUnAddressed && ResourcesManager.chocobackbuttonregion!=null){

							ResourcesManager.getInstance().joinRandomDailyChallengeRoom(ConnectionManager.mPathRoomNumberSuffix);
							/*ResourcesManager.getInstance().activity.onDeleteListener=new OnDeleteListener() {

								 (non-Javadoc)
							 * @see com.sromku.simple.fb.listeners.OnActionListener#onComplete(java.lang.Object)

								@Override
								public void onComplete(Void response) {
									// TODO Auto-generated method stub
									super.onComplete(response);
									Log.e("Deleted ", "APP REQUEST");
								}

								 (non-Javadoc)
							 * @see com.sromku.simple.fb.listeners.OnActionListener#onException(java.lang.Throwable)

								@Override
								public void onException(Throwable throwable) {
									// TODO Auto-generated method stub
									super.onException(throwable);
									Log.e("onException ", "APP onException"+throwable);
								}

								 (non-Javadoc)
							 * @see com.sromku.simple.fb.listeners.OnActionListener#onFail(java.lang.String)

								@Override
								public void onFail(String reason) {
									super.onFail(reason);
									Log.e("onFail ", "APP onFail"+reason);
								}
							};
							ResourcesManager.getInstance().activity.mSimpleFacebook.deleteRequest(FacebookManager.requestId, ResourcesManager.getInstance().activity.onDeleteListener);*/
							FacebookManager.deleteRequest(FacebookManager.requestId);
						}
					}

					@Override
					public void onAuthError(String errorUri, String errorDesc) {

					}

				}, "foobar", "secret");
			}

			@Override
			public void onClose(int code, String reason) {

				if( (ConnectionManager.mHost.equals(ConnectionManager.mHost2) && WebSocket.ConnectionHandler.CLOSE_RECONNECT==code)  || (null!=reason && reason.contains("already connected"))){
					return;
				}
				ConnectionManager.noConnectionAttempts++;
				Log.e("ConnectionAtt:autoRetry","noConnectionAttempts: "+ ConnectionManager.noConnectionAttempts + " : "+ ResourcesManager.autoRetryInProgress);


				if(ConnectionManager.noConnectionAttempts<6){
					// The connection was closed. Set the status line, show a
					// message box,
					// and set the button to allow to connect again.
					ConnectionManager.mStatusline="Connection closed." + reason;
					Log.e("mStatusline", ConnectionManager.mStatusline);
					if(ResourcesManager.getInstance().haveNetworkConnection()){
						if(ConnectionManager.noConnectionAttempts<2){
							Log.e("mStatusline", "ConnectionHandler.CLOSE_RECONNECT");

							return;
						}
						else if(ConnectionManager.noConnectionAttempts<6){
							ResourcesManager.autoRetryInProgress=true;
							ToastHelper.makeCustomToastOnUI("Sorry, Server Couldn't Be Reached. \n\nWORD Is Getting Reconnected With Rest Of The WORLD! ", Toast.LENGTH_SHORT);

							toggleConnection();
							//ConnectionManager.getInstance().mConnection.failConnection(0, "explicit reconnect");
						}
						else{
							ResourcesManager.isResponseGot=false;
							ToastHelper.makeCustomToastOnUI("Sorry, Server Couldn't Be Reached. \n\nWORD Is Getting Reconnected With Rest Of The WORLD! ", Toast.LENGTH_SHORT);
						}
					}
					else{
						ToastHelper.makeCustomToastOnUI("You Are Not Connected To Internet, \nFYI: This Is An Online Multiplayer Game! ", Toast.LENGTH_LONG);
					}
				}
				else if(ConnectionManager.noConnectionAttempts<9){
					ResourcesManager.autoRetryInProgress=true;
					toggleConnection();
					//ConnectionManager.getInstance().mConnection.failConnection(0, "explicit reconnect");
				}
				else if(ConnectionManager.noConnectionAttempts<17){
					toggleConnection();
					if(!ResourcesManager.autoRetryInProgress && !ResourcesManager.getInstance().isGamePaused){
						ToastHelper.makeCustomToastOnUIDefinedColor("\n Sorry, Server Couldn't Be Reached \n ", Toast.LENGTH_SHORT);
					}
				}
				else{
					ConnectionManager.noConnectionAttempts=2;
				}
			}
		});
	}

	private synchronized void toggleConnection() {
		if(ConnectionManager.mHost.equals(ConnectionManager.mHost1)){
			ConnectionManager.mHost = ConnectionManager.mHost2;
			ConnectionManager.mPort = ConnectionManager.mPort2;
			ConnectionManager.prefix = ConnectionManager.prefix2;
		}
		else {
			ConnectionManager.mHost = ConnectionManager.mHost1; //Remote server Single POC Host
			ConnectionManager.mPort = ConnectionManager.mPort1; //Remote server Single POC Host port
			ConnectionManager.prefix = ConnectionManager.prefix1;
		}
		ConnectionManager.mHostname = ConnectionManager.mHost+":"+ConnectionManager.mPort; //To Change
		AutoBahnConnectorPubSub.wsuri = "ws://"+ConnectionManager.mHostname+"/chat"; //local To Change
		AutoBahnConnectorPubSub.prefixHostURI = "http://"+ConnectionManager.mHost+"/"; //local To Change
		IMService.prefixHostURI = AutoBahnConnectorPubSub.prefixHostURI;
		ConnectionManager.getInstance().autobahnConnectorPubSub.establishConnectionAuthenticateSubscribeToRooms();
	}


	public void disconnectAndReconnectAgain(){
		if(ResourcesManager.getInstance().haveNetworkConnection() && null!=connectionHandler){
			if(ConnectionManager.noConnectionAttempts<5){
				ConnectionManager.getInstance().mConnection.disconnect();
				ConnectionManager.getInstance().mConnection.connect(wsuri, connectionHandler);
				//ConnectionManager.getInstance().mConnection.failConnection(0, "explicit reconnect");

			}
			else{
				ToastHelper.makeCustomToastOnUI("Sorry, Server Couldn't Be Reached. \n\nPlease rejoin if you are trying to access a room!!!", Toast.LENGTH_LONG);
			}
		}
		else{
			ToastHelper.makeCustomToastOnUI("You Are Not Connected To Internet, \nFYI: This Is An Online Multiplayer Game!!!", Toast.LENGTH_LONG);
		}
	}

	//Order Of Call 2
	//Scrollable Rooms Scene fetch All The Rooms states
	//Latest Used Call getting all rooms state
	public void fetchAllRoomsState() {

		ConnectionManager.getInstance().mConnection.call(prefixHostURI+"roomsRpcRequest", new TypeReference<ConcurrentHashMap<Integer, RoomStream>>(){}, new Wamp.CallHandler() {

			@Override
			public void onResult(Object result) {
				ResourcesManager.isResponseGot=true;
				ConnectionManager.noConnectionAttempts=0;
				ResourcesManager.foundAttempts = 0;
				// when we get an event, we  can safely cast to the type we
				// specified previously
				final ConcurrentHashMap<Integer, RoomStream> roomDetailsMap = (ConcurrentHashMap<Integer, RoomStream>) result;
				//ConnectionManager.sentText = roomIndex;
				ResourcesManager.getInstance().roomsState = new HashMap<Integer, RoomStream>(roomDetailsMap);
				if(null!=ResourcesManager.getInstance().rooms && ResourcesManager.roomsState!=null){
					for (Iterator iterator = ResourcesManager.roomsState.keySet().iterator(); iterator
							.hasNext();) {
						Integer index = (Integer) iterator.next();
						RoomProperties roomRectSelected = ResourcesManager.getInstance().rooms.get(index);
						RoomStream roomStreamed = ResourcesManager.roomsState.get(index);
						if(null!=roomRectSelected && null!=roomStreamed){
							roomRectSelected.setNoOfParticipants(roomStreamed.getNoOfParticipants());
							roomRectSelected.getTextNumberOfPlayersJoined().setText(roomStreamed.getNoOfParticipants()+"");
							/*if(roomStreamed.getRoomHostedBy()==null){
								roomStreamed.setRoomState(RoomProperties.STATE_HOSTING_AVAILABLE);
								roomStreamed.setParticipants(null);
								roomRectSelected.setRoomState(RoomProperties.STATE_HOSTING_AVAILABLE);
								continue;
							}*/
							if(ResourcesManager.getInstance().hostImageclicked && (roomStreamed.getRoomState()==RoomProperties.STATE_HOSTED || roomStreamed.getRoomState()==RoomProperties.STATE_HOSTING_IN_PROGRESS || roomStreamed.getRoomState()==RoomProperties.STATE_WAITING)  ){
								roomStreamed.setRoomState(RoomProperties.STATE_HOSTED);
								roomRectSelected.setRoomState(RoomProperties.STATE_HOSTED);
							}
							else if(ResourcesManager.getInstance().guessImageclicked){
								//Room Objects WaitimeElapsed will be negative depicting  Non-Availablity for guessers after hosting if number of participants is greater than minimum required

								if(roomStreamed.getWaitTimeElapsed()<0 && (roomStreamed.getNoOfParticipants()>=(roomRectSelected.getRoomSize().getValue()/3)+1)){
									roomRectSelected.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
									roomStreamed.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
								}
								//if the number of participants is less than min players required or if waiting time has not expired yet
								else if((roomStreamed.getRoomState()==RoomProperties.STATE_WAITING || roomStreamed.getRoomState()==RoomProperties.STATE_HOSTED) && roomStreamed.getRoomHostedBy()!=null){
									roomStreamed.setRoomState(RoomProperties.STATE_WAITING);
									roomRectSelected.setRoomState(RoomProperties.STATE_WAITING);
								}
							}

						}

					}
				}

			}


			@Override
			public void onError(String errorUri, String errorDesc) {
				// TODO Auto-generated method stub

			}
		});

		//ConnectionManager.getInstance().mConnection.publish(prefixHostURI+"subscribedTopics/chat" + ConnectionManager.mPathRoomNumberSuffix,"Hello, world!" + ConnectionManager.myId);
	}


	//Order Of Call 3
	//Scrollable Rooms Scene adhoc joins to rooms and fills
	//Latest Used Subscription for Room hosting/Guessing User Joined broadcast called initially (Live Updation in Scrollable Rooms Scene)
	public void sendRoomHostedBroadcastSubscription() {

		ConnectionManager.getInstance().mConnection.subscribe(prefixHostURI+"subscribedTopics/chat" + ConnectionManager.liveUpdateBroadcastIndex, RoomStream.class, new Wamp.EventHandler() {

			@Override
			public void onEvent(String topicUri, Object event) {

				ConnectionManager.noConnectionAttempts=0;
				ResourcesManager.isResponseGot=true;

				// when we get an event, we  can safely cast to the type we
				// specified previously
				final RoomStream roomDetailsFromServer = (RoomStream) event;
				if((roomDetailsFromServer.getRoomHostedBy()==null || roomDetailsFromServer.getRoomHostedBy().trim().isEmpty()) && roomDetailsFromServer.getRoomState()==RoomProperties.STATE_WAITING){
					roomDetailsFromServer.setRoomHostedBy(Constants.ANONYMOUS);
				}
				//ConnectionManager.sentText = roomIndex;
				Log.e(TAG, "roomIndex: "+roomDetailsFromServer.getIndex());
				if(null!=roomDetailsFromServer && null!=roomDetailsFromServer.getRoomHostedBy() && !roomDetailsFromServer.getRoomHostedBy().isEmpty()){
					//	String parts[]=roomDetailsFromServer.split(":");
					final int roomIndex = roomDetailsFromServer.getIndex();
					final String hostdBy= roomDetailsFromServer.getRoomHostedBy();
					ResourcesManager.getInstance().roomHostedBy = hostdBy;
					if(0!=roomIndex){
						if(StorageManager.getInstance().getUser().getUserName().equalsIgnoreCase(hostdBy)){

						}

						//RoomStreamHosted By Someone Client Copy
						RoomStream roomFromServersLocalCopy=ResourcesManager.roomsState.get(roomIndex);
						//Actual Rooms
						final RoomProperties roomRectSelected = ResourcesManager.rooms.get(roomIndex);

						//Decide The State Of Local rooms
						if(null!=roomFromServersLocalCopy){
							if(ResourcesManager.getInstance().hostImageclicked && (roomDetailsFromServer.getRoomState()==RoomProperties.STATE_HOSTED || roomDetailsFromServer.getRoomState()==RoomProperties.STATE_HOSTING_IN_PROGRESS)){
								roomFromServersLocalCopy.setRoomState(RoomProperties.STATE_HOSTED);
								if(null!=roomRectSelected)
									roomRectSelected.setRoomState(RoomProperties.STATE_HOSTED);
							}
							else if(ResourcesManager.getInstance().guessImageclicked){
								if(roomDetailsFromServer.getRoomState()==RoomProperties.STATE_HOSTING_IN_PROGRESS){
									roomFromServersLocalCopy.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
									if(null!=roomRectSelected)
										roomRectSelected.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
								}
								else if(roomDetailsFromServer.getRoomState()==RoomProperties.STATE_WAITING){
									roomFromServersLocalCopy.setRoomState(RoomProperties.STATE_WAITING);
									if(null!=roomRectSelected)
										roomRectSelected.setRoomState(RoomProperties.STATE_WAITING);
								}
							}
						}
						else{
							ResourcesManager.roomsState.put(roomIndex, roomDetailsFromServer);
							roomFromServersLocalCopy=roomDetailsFromServer;
							if(null!=roomRectSelected)
								roomRectSelected.setRoomState(roomDetailsFromServer.getRoomState());
						}


						//For Adding User Chunks If in Waiting Room
						if(null!=roomDetailsFromServer.getParticipants() && !roomDetailsFromServer.getParticipants().isEmpty()){
							final RoomStream tempRoomStream=roomFromServersLocalCopy;
							Runnable run6 = new Runnable() {
								@Override
								public void run() {
									if(null!=SceneManager.getInstance().getCurrentScene()  && SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_WAITING)){ // If Waiting Scene Add users
										if(roomDetailsFromServer.getWaitTimeElapsed()>0)
											WaitingScene.waitTimeElapsed=roomDetailsFromServer.getWaitTimeElapsed();

										//Set The Number Of Letters Hosted In Room
										if(0!=roomDetailsFromServer.getNumOfLettersHosted()){
											ResourcesManager.getInstance().numberOfLettersHosted=roomDetailsFromServer.getNumOfLettersHosted();
											if(null!=ResourcesManager.getInstance().numberOfLettersHostedText && null!=ResourcesManager.getInstance().numberOfLettersHostedText.getText()
													&& !ResourcesManager.getInstance().numberOfLettersHostedText.getText().toString().isEmpty()
													&& ResourcesManager.getInstance().numberOfLettersHostedText.getText().toString().indexOf("Letters:")==-1){
												ResourcesManager.getInstance().numberOfLettersHostedText.setText("No. Of Letters In Word Hosted: "+ResourcesManager.getInstance().numberOfLettersHosted);
											}
										}
										else if(!ConnectionManager.isNormalFlow()){
											if(null!=ResourcesManager.getInstance().numberOfLettersHostedText && null!=ResourcesManager.getInstance().numberOfLettersHostedText.getText()
													&& !ResourcesManager.getInstance().numberOfLettersHostedText.getText().toString().isEmpty()
													&& ResourcesManager.getInstance().numberOfLettersHostedText.getText().toString().indexOf("Letters:")==-1){
												ResourcesManager.getInstance().numberOfLettersHostedText.setText("No. Of Letters: Thinking...");
											}
										}
										try {
											int numOfPlayers= roomDetailsFromServer.getParticipants().size();
											addUserChunks(numOfPlayers,roomDetailsFromServer,roomIndex,hostdBy);
											numOfPlayers= roomDetailsFromServer.getParticipants().size();

											if(tempRoomStream.getIndex()==roomDetailsFromServer.getIndex()){
												tempRoomStream.setNoOfParticipants(numOfPlayers);
											}

											if(null!=roomRectSelected){
												roomRectSelected.setNoOfParticipants(numOfPlayers);
												roomRectSelected.getTextNumberOfPlayersJoined().setText(numOfPlayers+"");
											}
										}
										catch (Exception e) {
											e.printStackTrace();
										}
									}
								}


							};
							//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
							ResourcesManager.getInstance().activity.runOnUpdateThread(run6);
						}
						//ConnectionManager.mPathRoomNumberSuffix = Integer.parseInt(roomIndex);
						subscribeUserToRoom();
					}
				}
				else if(null!=roomDetailsFromServer  && (null==roomDetailsFromServer.getRoomHostedBy()  || roomDetailsFromServer.getRoomHostedBy().isEmpty())){
					int roomIndex= roomDetailsFromServer.getIndex();
					if(roomIndex!=0){
						//unSubscribeFromRoom();
						//RoomStreamHosted By Someone Client Copy
						RoomStream roomFromServersLocalCopy=ResourcesManager.roomsState.get(roomIndex);
						//Actual Rooms
						final RoomProperties roomRectSelected = ResourcesManager.rooms.get(roomIndex);
						ToastHelper.makeCustomToast( "Room Under Review", Toast.LENGTH_SHORT);

						//Decide The State Of Local rooms
						if(null!=roomFromServersLocalCopy){
							if(ResourcesManager.getInstance().hostImageclicked && (roomDetailsFromServer.getRoomState()==RoomProperties.STATE_HOSTED || roomDetailsFromServer.getRoomState()==RoomProperties.STATE_HOSTING_IN_PROGRESS)){
								roomFromServersLocalCopy.setRoomState(RoomProperties.STATE_HOSTING_AVAILABLE);
								if(null!=roomRectSelected)
									roomRectSelected.setRoomState(RoomProperties.STATE_HOSTING_AVAILABLE);
							}
							else if(ResourcesManager.getInstance().guessImageclicked){
								if(roomDetailsFromServer.getRoomState()==RoomProperties.STATE_HOSTING_IN_PROGRESS){
									roomFromServersLocalCopy.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
									if(null!=roomRectSelected)
										roomRectSelected.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
								}
								else if(roomDetailsFromServer.getRoomState()==RoomProperties.STATE_WAITING){
									roomFromServersLocalCopy.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
									if(null!=roomRectSelected)
										roomRectSelected.setRoomState(RoomProperties.STATE_GUESSING_DISABLED);
								}
							}
						}
						else{
							ResourcesManager.roomsState.put(roomIndex, roomDetailsFromServer);
							roomFromServersLocalCopy=roomDetailsFromServer;
							if(null!=roomRectSelected)
								roomRectSelected.setRoomState(roomDetailsFromServer.getRoomState());
						}
					}
				}
			}
		});
		ResourcesManager.roomHostedBroadCastSubscriptionSent=true;
		//ConnectionManager.getInstance().mConnection.publish(prefixHostURI+"subscribedTopics/chat" + ConnectionManager.mPathRoomNumberSuffix,"Hello, world!" + ConnectionManager.myId);
	}

	//Latest Used publish for Room hosting broadcast
	public void sendRoomHostedBroadcastPublish(RoomStream roomDetails){

		String topicSelected = "chat";
		ConnectionManager.getInstance().mConnection.publish(prefixHostURI+"subscribedTopics/"+topicSelected+ ConnectionManager.liveUpdateBroadcastIndex,  roomDetails);

	}

	private void addUserChunks(int numOfPlayers,
							   RoomStream roomDetailsFromServer, int roomIndex, String hostdBy) {
		if(roomIndex == roomDetailsFromServer.getIndex() && ConnectionManager.mPathRoomNumberSuffix==roomIndex){
			for (int i = 0; i < numOfPlayers; i++) {
				UserDO user = roomDetailsFromServer.getParticipants().get(i);
				if(null!=user){
					if(user.getUserProfilePicUrl()==null){
						user.setUserProfilePicUrl("");
					}

					if(user.isHoster() && ResourcesManager.getInstance().hostImageclicked){
						if(null!=hostdBy && hostdBy.equals(StorageManager.getInstance().getUser().getUserName())){
							((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);
						}
						else{
							if(numOfPlayers>1)
							{
								roomDetailsFromServer.getParticipants().remove(i);
								//numOfPlayers--;
							}
						}
					}
					else {
						((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);
					}
					//localHardCoding
					/*	user.setUserName(user.getUserName()+1);
					((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);*/
					/*user.setUserName(user.getUserName()+2);
					((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);
					user.setUserName(user.getUserName()+3);
					((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);
					user.setUserName(user.getUserName()+4);
					((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);
					user.setUserName(user.getUserName()+5);
					((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);*/
				}

			}
		}
	}

	//Subscribe to a specific Room for user events
	//Latest used subscription call for subscribing to Room
	private void subscribeUserToRoom() {

		String topicSelected = "chat";
		ConnectionManager.getInstance().mConnection.subscribe(ConnectionManager.prefix+":"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix+ConnectionManager.suffixEventMarker,
				UserDO.class,
				new WampCra.EventHandler() {



					//This onEvent will be called on Result of publish calls
					@Override
					public void onEvent(String topic, Object event) {
						ResourcesManager.isResponseGot=true;
						ResourcesManager.foundAttempts = 0;
						ConnectionManager.noConnectionAttempts=0;

						final UserDO user = (UserDO) event;
						if(user.getHostedGuessedRoomIndex() == StorageManager.getInstance().getUser().getHostedGuessedRoomIndex()){
							//copy server details from DB to Client for initial sync -> ST object is db object
							if(!StorageManager.getInstance().getUser().isStObject() && user.isStObject() && StorageManager.getInstance().getUser().getUserName().equals(user.getUserName())){
								copyServerObjectToStorage(StorageManager.getInstance().getUser(), user);
							}
							String evt = null;
							if(mPubs.contains(user)){
								for (Iterator iterator = mPubs.iterator(); iterator
										.hasNext();) {
									UserDO queuedUser = (UserDO) iterator.next();
									if((queuedUser.getWordGuessed()!=null && user.getWordGuessed()!=null && user.getWordGuessed().equals(queuedUser.getWordGuessed())) || (queuedUser.getWordHosted()!=null  && user.getWordHosted()!=null && user.getWordHosted().equals(queuedUser.getWordHosted()))
											|| ((queuedUser.getWordHosted()==null  && user.getWordHosted()==null) &&  (queuedUser.getWordGuessed()==null && user.getWordGuessed()==null))){
										mPubs.remove(queuedUser); //TODO locally committed to do queueing and reproduce load performance issue //TODO performance testing
										Log.e(TAG, "user Removed From Publish Queue "+queuedUser.getWordGuessed()+"");
									}

								}
							}
							//Not a hoster's dummy call response condition i.e set hosted word to guesser only for initial dummy publish call
							if(!user.isHoster() && (user.getWordGuessed()==null || user.getWordGuessed().isEmpty()) && user.getWordHosted()!=null && !user.getWordHosted().isEmpty() ){
								ResourcesManager.getInstance().wordHosted = user.getWordHosted();
								user.setWordHosted(null);
							}
							//Valid Word or not toast
							if(null!=user && user.isHoster() && null!=user.getWordHosted()  && !user.getWordHosted().isEmpty()){ //For Host
								if(user.isWordValid()){
									evt =  user.getWordHosted();
									ResourcesManager.getInstance().numberOfLettersHosted= user.getWordHosted().length();
									if(null!=ResourcesManager.getInstance().numberOfLettersHostedText && null!=ResourcesManager.getInstance().numberOfLettersHostedText.getText()
											&& !ResourcesManager.getInstance().numberOfLettersHostedText.getText().toString().isEmpty()
											&& ResourcesManager.getInstance().numberOfLettersHostedText.getText().toString().indexOf("Letters:")==-1){
										ResourcesManager.getInstance().numberOfLettersHostedText.setText("No. Of Letters In Word Hosted: "+ResourcesManager.getInstance().numberOfLettersHosted);
									}
								}
								else if(null!=user.getClueWord() && !user.getClueWord().isEmpty() && StorageManager.getInstance().getUser().getHostedGuessedRoomIndex()!=0){
									if(null!=ResourcesManager.getInstance().chat && (ChatManager.chatState==0 || !ResourcesManager.isChatEnabled)){
										ResourcesManager.getInstance().chat.setCurrentTileIndex(Chat.CHAT_INVZ_ID);
										ResourcesManager.getInstance().chat.setAlpha(1f);
									}
									else if (user.getClueWord().contains("::") && ResourcesManager.isChatEnabled) {
										String[] strarr=user.getClueWord().split("::");
										if(strarr.length>=1){
											//ResourcesManager.getInstance().closeChatWindow();
									/*for (int i = 0; i < strarr.length; i++) {
										ResourcesManager.getInstance().changedText= ResourcesManager.getInstance().changedText + "\n" +strarr[i];
									}
									 */
											StandOutWindow.show(ResourcesManager.getInstance().activity, WidgetsWindow.class, 0);
											ChatManager.chatState=1;
											Window window = getWindow(0);
											if (window == null) {
												String errorText = String.format(Locale.US,
														"%s received data but Chat Window  is not open.",
														"WORD Chat");
												//Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
												return;
											}

											for (int i = 0; i < strarr.length; i++) {
												//ResourcesManager.getInstance().changedText= ResourcesManager.getInstance().changedText + "\n" +strarr[i];
												if(strarr[i]!=null && !strarr[i].isEmpty()){
													Message m=new Message(strarr[i], StorageManager.getInstance().getUser().getUserName().equals(user.getUserName()));
													int indexOfJunk = strarr[i].indexOf("1u*#z");
													if(user.isAutoLoginRequired() || indexOfJunk!=-1){
														if(indexOfJunk!=-1){
															m.setStatusMessage(true);
															m.setMessage(strarr[i].replace("1u*#z", ""));
														}
														else{
															m.setStatusMessage(false);
														}

														if(user.isAutoLoginRequired()  && indexOfJunk!=-1 && StorageManager.getInstance().getUser().getUserName().equals(user.getUserName())){
															m.setMine(true);
															m.setStatusMessage(true);
															StorageManager.getInstance().getUser().setAutoLoginRequired(false);
															//continue;
														}
														else if(i==strarr.length-1 && indexOfJunk!=-1 && strarr[i].contains(user.getDisplayName())){
															m.setMine(true);
															m.setStatusMessage(true);
															StorageManager.getInstance().getUser().setAutoLoginRequired(false);
														}
														else{
															StorageManager.getInstance().getUser().setAutoLoginRequired(false);
															//m.setStatusMessage(true);
															if(!StorageManager.getInstance().getUser().getUserName().equals(user.getUserName())){
																continue;
															}

															if(strarr[i].contains(user.getDisplayName())){
																m.setMine(true);
															}
															else{
																m.setMine(false);
															}

														}
													}
													else{
														m.setStatusMessage(false);
													}
													ChatManager.getInstance().addNewMessage(m);
												}
											}
										}
									}
								}
								else if(user.getUserName().equals(StorageManager.getInstance().getUser().getUserName()) && SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_HOST){
									ToastHelper.makeCustomToast( "The Word Hosted " + "\""+user.getWordHosted()+"\"" + " Is Not A Valid English Word", Toast.LENGTH_SHORT);
								}
							}
							else{ //For Guessers for each guess
								if(null!=user.getWordGuessed() && !user.getWordGuessed().isEmpty() && !user.isHoster()){


									if(user.isWordValid()){
										evt =  user.getWordGuessed();
										StorageManager.getInstance().getUser().setRevealedBits(user.getRevealedBits());

										//Calculate Coins for each of guesses
										if(user.getUserName().equals(StorageManager.getInstance().getUser().getUserName()) && SceneManager.getInstance().getCurrentSceneType() == SceneType.SCENE_GUESS){
											final int numberOfCoinsPrev= ResourcesManager.numberOfCoinsPerGame;
											ResourcesManager.numberOfCoinsPerGame= ResourcesManager.numberOfCoinsPerGame+user.getCows();
											ResourcesManager.numberOfCoinsPerGame= ResourcesManager.numberOfCoinsPerGame+ 4 * user.getBulls();

											final int changedCoinCount= ResourcesManager.numberOfCoinsPerGame - numberOfCoinsPrev;
											float currentTextY=ResourcesManager.getInstance().windowDimensions.y/4+80*ResourcesManager.resourceScaler;
											final Text tempText =new Text(ResourcesManager.getInstance().windowDimensions.x/4,currentTextY, ResourcesManager.getInstance().mBitmapHowToPlaySmall, "+"+changedCoinCount, ResourcesManager.getInstance().vbom);
											tempText.setColor(0.15f, 0.15f, 0.15f);
											MoveModifier coinCountFlyAwayModifier = new MoveModifier(2.5f, ResourcesManager.getInstance().windowDimensions.x/4,currentTextY,tempText.getX(),currentTextY+100*ResourcesManager.resourceScaler);

											AlphaModifier coinCountAlphaModifier = new AlphaModifier(2.5f, 1, 0.0f);
											parallelModifierListener=new IEntityModifierListener() {

												@Override
												public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {

												}

												@Override
												public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
													ResourcesManager.getInstance().detachInUpdateThread(tempText);
												}
											};

											ScaleModifier scaleModifierForCoinCount = new ScaleModifier(2.5f, 1f*ResourcesManager.resourceScaler, 0.6f*ResourcesManager.resourceScaler, parallelModifierListener);
											scaleModifierForCoinCount.setAutoUnregisterWhenFinished(true);
											coinCountAlphaModifier.setAutoUnregisterWhenFinished(true);
											coinCountFlyAwayModifier.setAutoUnregisterWhenFinished(true);

											parallelModifier = new ParallelEntityModifier(coinCountFlyAwayModifier,scaleModifierForCoinCount, coinCountAlphaModifier);
											tempText.registerEntityModifier(parallelModifier);
											((GuessScene)SceneManager.getInstance().getCurrentScene()).attachChild(tempText);

											if(changedCoinCount>0){
												ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {

													@Override
													public void run() {
														boolean shopSoundPlayed = true;
														for (int i = 1; i <= changedCoinCount; i++) {
															long coins = StorageManager.getInstance().getUser().getNumOfCoins()+ResourcesManager.numberOfCoinsPerGame;
															ResourcesManager.getInstance().numberOfCoinsText.setText( " X "+ coins);
															if(changedCoinCount>4){
																if(i<=2){
																	SoundManager.getInstance().createAddCoinLongSound();
																}
																else if(i<4){
																	SoundManager.getInstance().createAddCoinMediumSound();
																}
																else if(i<6){
																	SoundManager.getInstance().createAddCoinSound();
																}
																else if(shopSoundPlayed){
																	shopSoundPlayed = false;
																	SoundManager.getInstance().createAddCoinsForShopSound();
																}
															}
															else{
																if(i<2){
																	SoundManager.getInstance().createAddCoinSound();
																}
																else if(shopSoundPlayed){
																	shopSoundPlayed = false;
																	SoundManager.getInstance().createAddCoinsForShopSound();
																}
															}
															ThemeManager.getInstance().attachCoins((GuessScene)SceneManager.getInstance().getCurrentScene());
														}
													}
												});
											}
										}

									}
									else if(user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
										ToastHelper.makeCustomToast("The Word Guessed " + "\""+user.getWordGuessed()+"\"" + "Is Not A Valid English Word", Toast.LENGTH_SHORT);
									}
									if(user.getBulls()!=0 && user.getBulls()==ResourcesManager.getInstance().numberOfLettersHosted){
										ResourcesManager.getInstance().gameWon=true;
										ResourcesManager.getInstance().deviceBackButtonToGameScene = true;
										RoomStream roomStreamAvailable = ResourcesManager.roomsState.get(user.getHostedGuessedRoomIndex());
										RoomProperties roomPropertiesAvailable = ResourcesManager.rooms.get(user.getHostedGuessedRoomIndex());



										roomStreamAvailable.setRoomState(RoomProperties.STATE_HOSTING_AVAILABLE);
										roomPropertiesAvailable.setRoomState(RoomProperties.STATE_HOSTING_AVAILABLE);

										int difficultySetting= 2;
										if(roomPropertiesAvailable.getNoOfParticipants()!=0){
											difficultySetting = ResourcesManager.getInstance().numberOfLettersHosted/roomPropertiesAvailable.getNoOfParticipants();
											if(difficultySetting<2){
												difficultySetting = 2;
											}
										}

										if(user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
											ResourcesManager.numberOfCoinsPerGame = ResourcesManager.numberOfCoinsPerGame * 2;
											roomPropertiesAvailable.setDifficultyLevel((int)difficultySetting*3/2); //For Winner

											StorageManager.getInstance().getUser().setNumOfCoins(StorageManager.getInstance().getUser().getNumOfCoins() + ResourcesManager.numberOfCoinsPerGame );
											StorageManager.getInstance().getUser().setNumOfGamesPlayed(StorageManager.getInstance().getUser().getNumOfGamesPlayed()+1);
											StorageManager.getInstance().getUser().setNumOfGamesWon(StorageManager.getInstance().getUser().getNumOfGamesWon()+1);
											//ResourcesManager.numberOfCoinsTotal=  ResourcesManager.numberOfCoinsTotal + ResourcesManager.numberOfCoinsPerGame ;
											ResourcesManager.getInstance().level=new Level(ResourcesManager.numberOfCoinsPerGame, roomPropertiesAvailable.getDifficultyLevel(), StorageManager.getInstance().getUser().getNumOfGamesPlayed(), StorageManager.getInstance().getUser().getNumOfGamesWon());
											ResourcesManager.numberOfCoinsPerGame=0;

											checkNewLevelUnlocked();

											StorageManager.getInstance().getUser().setScore(ResourcesManager.getInstance().level.getNewScore(StorageManager.getInstance().getUser().getScore()));
											StorageManager.getInstance().saveUserScore(StorageManager.getInstance().getUser());
											ResourcesManager.getInstance().gameWonBy="You";
											ResourcesManager.getInstance().numberOfLettersHosted=0;
										}
										else{
											roomPropertiesAvailable.setDifficultyLevel((int)difficultySetting/2);  //For Loser Experience Bonus
											StorageManager.getInstance().getUser().setNumOfCoins(StorageManager.getInstance().getUser().getNumOfCoins() + ResourcesManager.numberOfCoinsPerGame );
											StorageManager.getInstance().getUser().setNumOfGamesPlayed(StorageManager.getInstance().getUser().getNumOfGamesPlayed()+1);

											ResourcesManager.getInstance().level=new Level(ResourcesManager.numberOfCoinsPerGame, roomPropertiesAvailable.getDifficultyLevel(),StorageManager.getInstance().getUser().getNumOfGamesPlayed(), StorageManager.getInstance().getUser().getNumOfGamesWon());
											ResourcesManager.numberOfCoinsPerGame=0;
											checkNewLevelUnlocked();
											StorageManager.getInstance().getUser().setScore(ResourcesManager.getInstance().level.getNewScore(StorageManager.getInstance().getUser().getScore()));
											StorageManager.getInstance().saveUserScore(StorageManager.getInstance().getUser());
											ResourcesManager.getInstance().gameWonBy=null!=user.getDisplayName()?user.getDisplayName().toUpperCase():user.getUserName();
										}
									}
								}//For Clue
								 if(null!=user.getClueWord() && !user.getClueWord().isEmpty()  && StorageManager.getInstance().getUser().getHostedGuessedRoomIndex()!=0){
									if(null!=ResourcesManager.getInstance().chat && (ChatManager.chatState==0 || !ResourcesManager.isChatEnabled) && user.getClueWord().contains("::")){
										ResourcesManager.getInstance().chat.setCurrentTileIndex(Chat.CHAT_INVZ_ID);
										ResourcesManager.getInstance().chat.setAlpha(1f);
									}
									else if (user.getClueWord().contains("::")) {
										String[] strarr=user.getClueWord().split("::");
										if(strarr.length>=1){
											//ResourcesManager.getInstance().closeChatWindow();


											StandOutWindow.show(ResourcesManager.getInstance().activity, WidgetsWindow.class, 0);
											Window window = getWindow(0);
											ChatManager.chatState=1;
											if (window == null) {
												String errorText = String.format(Locale.US,
														"%s received data but Chat Window  is not open.",
														"WORD Chat");
												//Toast.makeText(this, errorText, Toast.LENGTH_SHORT).show();
												return;
											}

											if(null!=ChatManager.getInstance().messages && ChatManager.getInstance().messages.isEmpty()){
												Message init = new Message(true,"<b>Start Guessing "+ ResourcesManager.getInstance().numberOfLettersHosted + " Letter Words</b>");
												init.setMine(false);
												ChatManager.getInstance().addNewMessage(init);
											}
											for (int i = 0; i < strarr.length; i++) {
												//ResourcesManager.getInstance().changedText= ResourcesManager.getInstance().changedText + "\n" +strarr[i];
												if(strarr[i]!=null && !strarr[i].isEmpty()){
													Message m=new Message(strarr[i], StorageManager.getInstance().getUser().getUserName().equals(user.getUserName()));
													int indexOfJunk = strarr[i].indexOf("1u*#z");
													if(user.isAutoLoginRequired() || indexOfJunk!=-1){
														if(indexOfJunk!=-1){
															m.setStatusMessage(true);
															m.setMessage(strarr[i].replace("1u*#z", ""));
														}
														else{
															m.setStatusMessage(false);
														}

														if(user.isAutoLoginRequired()  && indexOfJunk!=-1 && StorageManager.getInstance().getUser().getUserName().equals(user.getUserName())){
															m.setMine(true);
															m.setStatusMessage(true);
															StorageManager.getInstance().getUser().setAutoLoginRequired(false);
															//continue;
														}
														else if(i==strarr.length-1 && indexOfJunk!=-1 && strarr[i].contains(user.getDisplayName())){
															m.setMine(true);
															m.setStatusMessage(true);
															StorageManager.getInstance().getUser().setAutoLoginRequired(false);
														}
														else{
															StorageManager.getInstance().getUser().setAutoLoginRequired(false);
															//m.setStatusMessage(true);
															if(!StorageManager.getInstance().getUser().getUserName().equals(user.getUserName())){
																continue;
															}

															if(strarr[i].contains(user.getDisplayName())){
																m.setMine(true);
															}
															else{
																m.setMine(false);
															}

														}
													}
													else{
														m.setStatusMessage(false);
													}
													ChatManager.getInstance().addNewMessage(m);
												}
											}

										}
									}
									else if(user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
										evt =  user.getClueWord();
										ResourcesManager.getInstance().setRevealedBits(user.getRevealedBits());
										StorageManager.getInstance().getUser().setRevealedBits(user.getRevealedBits());
									}
								}
							}

							if(evt!=null && !evt.isEmpty()){
								ConnectionManager.sentText = evt;
							}
							Log.d(TAG, "Text From Server"+ConnectionManager.sentText);
							ResourcesManager.getInstance().payloadFromServer = ConnectionManager.sentText;

							if(!user.isHoster() && !StorageManager.getInstance().getUser().isHoster() && (null==user.getClueWord() || user.getClueWord().isEmpty()) && 0!=user.getNumberOfLettersHosted()){ // get Number Of letters in hosted word for Guessers
								ResourcesManager.getInstance().numberOfLettersHosted= user.getNumberOfLettersHosted();
								ResourcesManager.getInstance().maxNoOfLetter= user.getNumberOfLettersHosted();
								if(null!=ResourcesManager.getInstance().numberOfLettersHostedText)
									ResourcesManager.getInstance().numberOfLettersHostedText.setText("Letters: "+ResourcesManager.getInstance().numberOfLettersHosted);

							}
							if(null!=SceneManager.getInstance().getCurrentScene()  && (SceneManager.getInstance().getCurrentScene() instanceof RealGameInterface)){

								//Adding Page Chunks To Scene
								if(evt!=null && !evt.isEmpty()){
									Runnable run5 = new Runnable() {
										@Override
										public void run() {
											try {
												((RealGameInterface)SceneManager.getInstance().getCurrentScene()).addPageChunk(user);
												final RoomProperties roomSelected = ResourcesManager.rooms.get(user.getHostedGuessedRoomIndex());
												short minimumNumOfPlayers=(short)((roomSelected.getRoomSize().getValue()/3)+1);
												if(!((!ResourcesManager.getInstance().waitTimeString.equals("Play Time") && roomSelected.getRoomSize().getValue()>=roomSelected.getNoOfParticipants()) || (ResourcesManager.getInstance().waitTimeString.equals("Play Time")  && roomSelected.getNoOfParticipants()<minimumNumOfPlayers))){
													ResourcesManager.getInstance().addUserChunk(user);
												}
											}
											catch (Exception e) {
												e.printStackTrace();
											}
										}
									};
									//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
									ResourcesManager.getInstance().activity.runOnUpdateThread(run5);
									if(null!= user && null!=user.getUserName() && user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
										moveLineAndsetTransparency(SceneManager.getInstance().getCurrentScene());
										moveKeyboardAndsetTransparency(SceneManager.getInstance().getCurrentScene());
									}
								}
							}
					/*else if(null!=SceneManager.getInstance().getCurrentScene()  && SceneManager.getInstance().getCurrentScene().getSceneType().equals(SceneType.SCENE_WAITING)){ // If Waiting Scene Add users
					Runnable run6 = new Runnable() {
						@Override
						public void run() {
							try {
								((WaitingScene)(SceneManager.getInstance().getCurrentScene())).addUserChunk(user);
							} 
							catch (Exception e) {
								e.printStackTrace();
							}
						}
					};
					//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
					ResourcesManager.getInstance().activity.runOnUpdateThread(run6); 

					RoomProperties roomHostedGuessed=ResourcesManager.rooms.get(user.getHostedGuessedRoomIndex());
					if( (user.isHoster()==null || !user.isHoster()) && !user.getUserName().equals(StorageManager.getInstance().getUser().getUserName())){
						roomHostedGuessed.addNumOfParticipants();
					}
				}*/
							ResourcesManager.getInstance().hosted=false;
						}
					}
				}
		);


		//Initial Call for users Joining
		Log.e("Is Room Connected", ConnectionManager.getInstance().mConnection.isConnected()+"");
		ResourcesManager.getInstance().wordHosted=StorageManager.getInstance().getUser().getWordHosted();
		StorageManager.getInstance().getUser().setWordGuessed("");
		StorageManager.getInstance().getUser().setWordHosted("");
		StorageManager.getInstance().getUser().setClueWord("");
		StorageManager.getInstance().getUser().setRevealedBits(null);
		ConnectionManager.getInstance().mConnection.publish(ConnectionManager.prefix+":"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix+ConnectionManager.suffixEventMarker,StorageManager.getInstance().getUser());
		if(!mPubs.contains(StorageManager.getInstance().getUser())){
			mPubs.offer(StorageManager.getInstance().getUser());
		}
		createQueuingTimer();
	}

	/**
	 * Return the window corresponding to the id, if it exists in cache. The
	 * window will not be created with
	 * {@link #createAndAttachView(int, ViewGroup)}. This means the returned
	 * value will be null if the window is not shown or hidden.
	 *
	 * @param id
	 *            The id of the window.
	 * @return The window if it is shown/hidden, or null if it is closed.
	 */
	public final Window getWindow(int id) {
		if(null!=StandOutWindow.sWindowCache){
			return StandOutWindow.sWindowCache.getCache(id, WidgetsWindow.class);
		}
		return null;
	}

	/**
	 *
	 */
	protected void checkNewLevelUnlocked() {
		int tempIqId= ResourcesManager.getInstance().level.isNewScoreCrossingLevels(StorageManager.getInstance().getUser().getScore());
		Activity activity = ResourcesManager.getInstance().activity ;

		if(null!=ResourcesManager.getInstance().activity.getApiClient() && ResourcesManager.getInstance().activity.getApiClient().isConnected()){
			Games.Leaderboards.submitScore(ResourcesManager.getInstance().activity.getApiClient(), activity.getBaseContext().getString(R.string.leaderboard_word_iq_leaderboard), StorageManager.getInstance().getUser().getScore());
			Games.Achievements.load(ResourcesManager.getInstance().activity.getApiClient(), true).setResultCallback(new AchievementClass(tempIqId));
		}
		else if(null!=StorageManager.getInstance().getUser().getUserName() && -1==StorageManager.getInstance().getUser().getUserName().indexOf("WordPlayer#")){
			Log.e(TAG, "disconnected From google Game Play Services connecting again");
			ToastHelper.makeCustomToastOnUI("Please Wait While We Are Reconnecting You To Google Play For Unlocking Achievement", Toast.LENGTH_SHORT);
			ResourcesManager.getInstance().activity.getApiClient().connect();
		}

	}

	/**
	 * @param tempIqId
	 * @return
	 */
	private String getAchievementId(int tempIqId) {
		String achievementId= "" ;
		Activity activity = ResourcesManager.getInstance().activity ;

		switch (tempIqId) {
			case 1:

				achievementId= activity.getBaseContext().getString(R.string.achievement_protostar);
				break;
			case 2:

				achievementId= activity.getBaseContext().getString(R.string.achievement_average_star);
				break;
			case 3:
				achievementId= activity.getBaseContext().getString(R.string.achievement_red_giant);

				break;
			case 4:
				achievementId= activity.getBaseContext().getString(R.string.achievement_intellectual_star);

				break;
			case 5:
				achievementId= activity.getBaseContext().getString(R.string.achievement_superior_star);

				break;
			case 6:
				achievementId= activity.getBaseContext().getString(R.string.achievement_supernova);

				break;
			case 7:
				achievementId= activity.getBaseContext().getString(R.string.achievement_star_of_synapses);

				break;
			case 8:
				achievementId= activity.getBaseContext().getString(R.string.achievement_precocious_star);

				break;
			default:
				achievementId= activity.getBaseContext().getString(R.string.achievement_protostar);

				break;
		}
		return achievementId;
	}

	class AchievementClass implements ResultCallback<Achievements.LoadAchievementsResult> {
		int tempIqId = 0;
		AchievementClass(int tempIqId){
			this.tempIqId = tempIqId;
		}
		@Override
		public void onResult(LoadAchievementsResult arg0) {
			Achievement ach;
			AchievementBuffer aBuffer = arg0.getAchievements();
			Iterator<Achievement> aIterator = aBuffer.iterator();
			if(tempIqId<9 && tempIqId>0){
				for (int i = tempIqId; i > 0; i--) {
					String achievementId= "" ;
					achievementId = getAchievementId(i);
					while (aIterator.hasNext()) {
						ach = aIterator.next();
						if (achievementId.equals(ach.getAchievementId())) {
							if (ach.getState() == Achievement.STATE_UNLOCKED) {
								Log.e("Achievement", "Achievement " + achievementId+" ALready Unlocked");
								continue;
							} else {
								Games.Achievements.unlock(ResourcesManager.getInstance().activity.getApiClient(), achievementId); //To Unlock Achievement
							}
							//aBuffer.close();
							//break;
						}
					}
					aBuffer.close();
				}

			}
		}
	}
	/**
	 * @param userFromServer
	 * @param userStored
	 *
	 */
	protected void copyServerObjectToStorage(UserDO userStored, UserDO userFromServer) {
		//userStored.setNumOfBees(userFromServer.getNumOfBees());
		//userStored.setNumOfHoneyCombs(userFromServer.getNumOfHoneyCombs());
		userStored.setStObject(userFromServer.isStObject());
		if(userStored.getNumOfGamesPlayed()<userFromServer.getNumOfGamesPlayed()){
			userStored.setNumOfGamesPlayed(userFromServer.getNumOfGamesPlayed());
		}
		if(userStored.getNumOfGamesWon()<userFromServer.getNumOfGamesWon()){
			userStored.setNumOfGamesWon(userFromServer.getNumOfGamesWon());
		}
		if(userStored.getScore()<userFromServer.getScore()){
			userStored.setScore(userFromServer.getScore());
			userStored.setNumOfBees(userFromServer.getNumOfBees());
			userStored.setNumOfHoneyCombs(userFromServer.getNumOfHoneyCombs());
			userStored.setNumOfCoins(userFromServer.getNumOfCoins());
			userStored.setNumOfBullRuns(userFromServer.getNumOfBullRuns());
		}
		if(userFromServer.getReferrer()!=null){
			if(userFromServer.getReferrer().equals("R") && ResourcesManager.isAdsEnabledRefBased){
				//userStored.setReferrer(userFromServer.getReferrer());
				StorageManager.getInstance().saveUserReferredInstalls(userFromServer.getReferrer());
				ResourcesManager.isAdsEnabledRefBased =false;
				ToastHelper.makeCustomToastOnUIDefinedColor("\n Congratulations, We Are Disabling Your Ads Now \n ", Toast.LENGTH_LONG);
			}
			else if(userFromServer.getReferrer().equals("D")  && !ResourcesManager.isAdsEnabledRefBased  && userFromServer.getId()>100){
				//userStored.setReferrer(userFromServer.getReferrer());
				StorageManager.getInstance().saveUserReferredInstalls(userFromServer.getReferrer());
				ResourcesManager.isAdsEnabledRefBased =true;
			}
		}
		if(userFromServer.getId()>0 && userFromServer.getId()<=100  && ResourcesManager.isAdsEnabledRefBased){
			StorageManager.getInstance().saveUserReferredInstalls("R");
			ResourcesManager.isAdsEnabledRefBased =false;
			ToastHelper.makeCustomToastOnUIDefinedColor("\n Congratulations, You Are One Of Our First 100 Players \nEnjoy Ads Free Gameplay Now ", Toast.LENGTH_LONG);
		}
	}


	//Latest publish call for each guess Prefix only using curie (corresponding to testPubSubCustomEvent call)
	public void publishUserWordToRoomPrefixOnly(UserDO user){
		String topicSelected = "chat";

		if(mPubs.contains(user)){
			boolean found=false;
			for (Iterator iterator = mPubs.iterator(); iterator
					.hasNext();) {
				UserDO queuedUser = (UserDO) iterator.next();
				if((queuedUser.getWordGuessed()!=null && user.getWordGuessed()!=null && user.getWordGuessed().equals(queuedUser.getWordGuessed())) || (queuedUser.getWordHosted()!=null  && user.getWordHosted()!=null && user.getWordHosted().equals(queuedUser.getWordHosted()))){
					found=true;
					break;
				}
			}
			if(found){
				ResourcesManager.foundAttempts++;

			}
			else{
				mPubs.offer(user);
			}
		}
		else{
			mPubs.offer(user);
		}

		if(ConnectionManager.getInstance().mConnection instanceof WampConnection && ((WampConnection)ConnectionManager.getInstance().mConnection).mSubs.containsKey(prefixHostURI+"subscribedTopics/"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix +ConnectionManager.suffixEventMarker))
		{
			if(ResourcesManager.foundAttempts>4){
				ToastHelper.makeCustomToastOnUI("Sorry, Server Couldn't Be Reached. \n\nWORD Is Getting Reconnected With Rest Of The WORLD! ", Toast.LENGTH_LONG);
				ResourcesManager.isResponseGot = false;
				subscribeUserToRoom();
			}
			ConnectionManager.getInstance().mConnection.publish(ConnectionManager.prefix+":"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix +ConnectionManager.suffixEventMarker,  user);
		}
		else{
			subscribeUserToRoom();
			ConnectionManager.getInstance().mConnection.publish(ConnectionManager.prefix+":"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix +ConnectionManager.suffixEventMarker,  user);

		}

	}

	private void createQueuingTimer(){
		if(null!=TimerManager.queueResponseTimer){
			TimerManager.queueResponseTimer.cancel();
			TimerManager.queueResponseTimer.purge();
		}
		TimerManager.queueResponseTimer = new Timer();
		Runnable runQueue= new Runnable() {

			@Override
			public void run() {
				queueTaskTracker=new PublishResponseMonitoringTask();
				int delay = 2500;//TODO Performance test change from 2500
				Log.e("delay", delay+"");
				TimerManager.queueResponseTimer.scheduleAtFixedRate(queueTaskTracker, delay, delay);
			}
		};

		ResourcesManager.getInstance().activity.runOnUiThread(runQueue);
	}

	class PublishResponseMonitoringTask extends TimerTask {
		@Override
		public void run() {



			Runnable runTimerTaskOnUIThread= new Runnable() {

				@Override
				public void run() {
					if(null!=ConnectionManager.getInstance().mConnection && ConnectionManager.getInstance().mConnection.isConnected()){
						if(!mPubs.isEmpty()){
							if(!ConnectionManager.getInstance().mConnection.isConnectionClosed() && ConnectionManager.LastConnectedTimestamp + ConnectionManager.TIME_TO_REISSUEPUBLISH < System.currentTimeMillis()){
								Log.e(TAG, "PublishResponseMonitoringTask Cycle");
								ResourcesManager.autoRetryInProgress= true;
								ConnectionManager.getInstance().mConnection.publish(ConnectionManager.prefix+":"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix +ConnectionManager.suffixEventMarker,  mPubs.peek());
							}

						}
						else{
							ResourcesManager.autoRetryInProgress= false;
						}
					}
				}
			};

			ResourcesManager.getInstance().activity.runOnUiThread(runTimerTaskOnUIThread);
		}

	}

	//Latest publish call for each bee catch clue
	public void publishClueToRoom(UserDO user){

		String topicSelected = "chat";
		ConnectionManager.getInstance().mConnection.publish(prefixHostURI+"subscribedTopics/"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix +ConnectionManager.suffixEventMarker,  user);

	}



	public void unSubscribeFromRoom(){

		String topicSelected = "chat";
		ConnectionManager.getInstance().mConnection.unsubscribe(prefixHostURI+"subscribedTopics/"+topicSelected+ ConnectionManager.mPathRoomNumberSuffix +ConnectionManager.suffixEventMarker);

	}

	public synchronized void moveLineAndsetTransparency(final BaseScene scene) {

		final Line line= (Line) scene.getChildByTag(EntityTagManager.lineInScene);

		ResourcesManager.getInstance().moveAndHideLineListener = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				if(ResourcesManager.getInstance().guessImageclicked){
					createClearButtonAndAddAboveLine(scene, line);
				}
			}
		};

		ResourcesManager.getInstance().moveAndHideLine = new ParallelEntityModifier(new AlphaModifier(.8f, 0.2f, 1f),new LineMoveModifier(.3f, line.getX1(), ResourcesManager.getInstance().yTextStart,line.getX2(), ResourcesManager.lineMovedTo, ResourcesManager.getInstance().moveAndHideLineListener));

		line.registerEntityModifier(ResourcesManager.getInstance().moveAndHideLine);
	}

	private synchronized void createClearButtonAndAddAboveLine(final BaseScene scene, final Line line) {
		if(null!=ResourcesManager.getInstance().host && null!=ResourcesManager.getInstance().host.getCharSet()){
			float clearXStart = (ResourcesManager.getInstance().host.getCharSet().size()+1)*ResourcesManager.getInstance().letterWidth;
			float clearWidth = ResourcesManager.getInstance().windowDimensions.x - clearXStart - 20;
			float clearWidthAlt = line.getX2() - line.getX1() - clearXStart - 20;
			float clearYStart = ResourcesManager.lineMovedTo;

			Text clear=new Text(0, 0, ResourcesManager.getInstance().mBitmapFont, "Expand", scene.vbom);
			clear.setColor(0.15f, 0.15f, 0.20f);
			clear.setScale(ResourcesManager.resourceScaler);
			//clear.setAnchorCenter(0.5f, 0.5f);

			if(null==keyClear || !keyClear.hasParent() || !keyClear.isVisible()){
				Log.e("KEY Expand","KeyExpand Not Visible");
				keyClear = new KeyboardChar(clearXStart, clearYStart, clearWidth, clear.getHeightScaled()+10*ResourcesManager.resourceScaler, "Expand", clear, scene.vbom){
					@Override
					public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
						if(pSceneTouchEvent.isActionUp()){
							if(null!=keyClear && keyClear.isVisible()){
								keyClear.setVisible(false);
								keyClear.detachSelf();
								keyClear.clearEntityModifiers();
							}
							if(this.hasParent()){
								this.detachSelf();
								this.reset();
								this.resetEntityModifiers();
							}
							if(this.hasParent()){
								this.getParent().detachChild(this);
							}
							moveLineAndsetTransparencyBackToNormal(scene);
							moveKeyboardAndsetTransparencyBackToNormal(SceneManager.getInstance().getCurrentScene());
							return true;
						}
						else if(pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()){
							return true;
						}
						return false;
					};
				};

				keyClear.setAnchorCenter(0, 0);
				keyClear.setAlpha(0.2f);
				keyClear.setTag(EntityTagManager.keyClearExpand);
				clear.setAlpha(0.8f);
				//Decide of the three available widths and start position
				if(clear.getWidthScaled()+20<clearWidth){
					keyClear.setWidth((clear.getWidth()+20)*ResourcesManager.resourceScaler);
					if(clearXStart<ResourcesManager.getInstance().windowDimensions.x*0.75){
						clearXStart = line.getX2() - (clear.getWidth()+20)*ResourcesManager.resourceScaler;
						keyClear.setX(clearXStart);
					}
				}
				clear.setPosition(keyClear.getWidth()*.5f, keyClear.getHeight()*.5f);
				clear.setAnchorCenter(0.5f, 0.5f);
				keyClear.attachChild(clear);
				scene.registerTouchArea(keyClear);
				scene.getChildByTag(EntityTagManager.shapeScrollContainerTag).attachChild(keyClear);
				Log.e("KEY Expand","KeyExpand Should be Visible at this point");
			}
			else{
				Log.e("KEY Expand","KeyExpand Already Visible since keyClear.hasParent() is " + keyClear.hasParent() + "Visible " +  keyClear.isVisible());
			}
		}
	}


	public synchronized void moveKeyboardAndsetTransparency(BaseScene scene) {

		Rectangle keyBoard= (Rectangle) scene.getChildByTag(EntityTagManager.keyboardRectangle);

		float toY = ResourcesManager.lineMovedTo+(ResourcesManager.getInstance().letterheight/2); //20 should be ResourcesManager.getInstance().line.getY2() ----> 20 = Lines collapsed Y position

		ResourcesManager.getInstance().moveAndHideRectangleListener = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			}
		};

		ResourcesManager.getInstance().moveAndHideRectangle = new MoveYModifier(.3f,  keyBoard.getY(),  keyBoard.getY()-keyBoard.getHeight(), ResourcesManager.getInstance().moveAndHideRectangleListener);

		ResourcesManager.getInstance().hostGuessKeyCharAlphaModifierListener  = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			}
		};

		//Move Keys above line to follow the line
		if(null!=ResourcesManager.getInstance().host && null!=ResourcesManager.getInstance().host.getCharSet()){
			float individualcharMoveSpeed = 0f;
			UserDO localScopedUser = StorageManager.getInstance().getUser();
			for (Iterator iterator = ResourcesManager.getInstance().host.getCharSet().iterator(); iterator.hasNext();) {
				KeyboardChar charToMoveAndHide = (KeyboardChar) iterator.next();
				float toX = ResourcesManager.getInstance().line.getX1()+(ResourcesManager.offset*ResourcesManager.resourceScaler)+ResourcesManager.getInstance().letterWidth*charToMoveAndHide.getPosition();
				individualcharMoveSpeed = individualcharMoveSpeed+0.1f;


				if(ResourcesManager.isTutorialMode && null!=localScopedUser && null!=localScopedUser.getRevealedBits() && null==localScopedUser.getClueWord()){
					ResourcesManager.getInstance().hostGuessKeyCharAlphaModifier = new ParallelEntityModifier(new AlphaModifier(.3f+individualcharMoveSpeed, 0.2f, 1f,ResourcesManager.getInstance().hostGuessKeyCharAlphaModifierListener),new MoveModifier(0.3f+individualcharMoveSpeed, charToMoveAndHide.getX(), charToMoveAndHide.getY(), toX, toY, EaseBounceOut.getInstance()));

					Map<Integer, Boolean> revealedBits= localScopedUser.getRevealedBits();
					Set<Integer> keySet=localScopedUser.getRevealedBits().keySet();
					for (Iterator<Integer> iteratorInner = keySet.iterator(); iteratorInner.hasNext();) {
						Integer key =  iteratorInner.next();
						if(key+1==charToMoveAndHide.getPosition()){
							//charToMoveAndHide.getText().setColor(0,0,0);

							Boolean value = revealedBits.get(key);
							if(null==value){
								charToMoveAndHide.setAlpha(1f);
								charToMoveAndHide.setColor(1f, .27f, 0f);

							}
							else if(value){
								charToMoveAndHide.setAlpha(0f);
								charToMoveAndHide.setColor(0.298f, .733f, 0.091f);
							}
							else{
								charToMoveAndHide.setAlpha(0.5f);
								//charToMoveAndHide.getText().setColor(0,0,0);
							}
							break;
						}
					}
				}
				else{
					ResourcesManager.getInstance().hostGuessKeyCharAlphaModifier = new ParallelEntityModifier(new AlphaModifier(.3f+individualcharMoveSpeed, 0.2f, .07f,ResourcesManager.getInstance().hostGuessKeyCharAlphaModifierListener),new MoveModifier(0.3f+individualcharMoveSpeed, charToMoveAndHide.getX(), charToMoveAndHide.getY(), toX, toY, EaseBounceOut.getInstance()));

				}
				charToMoveAndHide.registerEntityModifier(ResourcesManager.getInstance().hostGuessKeyCharAlphaModifier);

			}
		}
		Sprite backButton= (Sprite) scene.getChildByTag(EntityTagManager.backButton);
		if(null!=backButton){
			backButton.setZIndex(50);
		}
		keyBoard.registerEntityModifier(ResourcesManager.getInstance().moveAndHideRectangle);
	}


	private synchronized void moveLineAndsetTransparencyBackToNormal(BaseScene scene) {

		Line line= (Line) scene.getChildByTag(EntityTagManager.lineInScene);

		ResourcesManager.getInstance().moveAndHideLineListener = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			}
		};

		ResourcesManager.getInstance().moveAndHideLine = new ParallelEntityModifier(new AlphaModifier(.8f, 0.2f, 1f),new LineMoveModifier(.3f, line.getX1(), line.getY1(), 50, ResourcesManager.getInstance().yTextStart, ResourcesManager.getInstance().moveAndHideLineListener));
		line.registerEntityModifier(ResourcesManager.getInstance().moveAndHideLine);
	}

	private synchronized void moveKeyboardAndsetTransparencyBackToNormal(final BaseScene scene) {

		Rectangle keyBoard= (Rectangle)scene.getChildByTag(EntityTagManager.keyboardRectangle);
		//Line line= (Line) scene.getChildByTag(2);
		float toY = ResourcesManager.getInstance().yTextStart+(ResourcesManager.getInstance().letterheight/2);

		ResourcesManager.getInstance().moveAndHideRectangleListener = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {

			}
		};

		ResourcesManager.getInstance().moveAndHideRectangle = new MoveYModifier(.3f,  keyBoard.getY(),  0, ResourcesManager.getInstance().moveAndHideRectangleListener);

		ResourcesManager.getInstance().hostGuessKeyCharAlphaModifierNormalListener  = new IEntityModifierListener(){
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				/*if(ResourcesManager.getInstance().){
					//moveCharsBackToKeyboard((KeyboardChar)pItem,scene);
				}*/
			}
		};
		if(null!=ResourcesManager.getInstance().host && null!=ResourcesManager.getInstance().host.getCharSet()){
			float individualcharMoveSpeed = 0f;



			for (KeyboardChar charToMoveToKeyboard: Collections.synchronizedSet(ResourcesManager.getInstance().host.getCharSet())) {
				//KeyboardChar charToMoveToKeyboard = (KeyboardChar) iterator.next();
				float toX = ResourcesManager.getInstance().line.getX1()+(ResourcesManager.offset*ResourcesManager.resourceScaler)+ResourcesManager.getInstance().letterWidth*charToMoveToKeyboard.getPosition();
				individualcharMoveSpeed = individualcharMoveSpeed+0.1f;
				ResourcesManager.getInstance().hostGuessKeyCharAlphaNormalModifier = new MoveModifier(0.3f+individualcharMoveSpeed, charToMoveToKeyboard.getX(), charToMoveToKeyboard.getY(), toX, toY,ResourcesManager.getInstance().hostGuessKeyCharAlphaModifierNormalListener, EaseBounceOut.getInstance());
				ThemeManager.getInstance().setKeyBoardCharTheme(charToMoveToKeyboard);
				charToMoveToKeyboard.registerEntityModifier(ResourcesManager.getInstance().hostGuessKeyCharAlphaNormalModifier);
				//moveCharsBackToKeyboard(charToMoveToKeyboard,scene);
			}

		}

		keyBoard.registerEntityModifier(ResourcesManager.getInstance().moveAndHideRectangle);
	}







	private synchronized void moveCharsBackToKeyboard(KeyboardChar charToMoveToKeyboard,BaseScene scene) {
		float toX = 0;
		float toY = 0;
		toX = charToMoveToKeyboard.getKeyboardPositionX();
		toY = charToMoveToKeyboard.getKeyboardPositionY();
		charToMoveToKeyboard.mod=null;
		/*Rectangle keyboard = (Rectangle)scene.getChildByTag(1);
		keyboard.attachChild(charToMoveToKeyboard);
		ResourcesManager.getInstance().showToast=true;
		if(ResourcesManager.getInstance().host.getCharSet().size()!=0){
			if(!ResourcesManager.getInstance().addedPositions.contains(charToMoveToKeyboard.getPosition()) && charToMoveToKeyboard.getPosition()!=0){
				boolean b=ResourcesManager.getInstance().addedPositions.add((Integer)charToMoveToKeyboard.getPosition());

				Debug.e("Push Positions>>> "+ charToMoveToKeyboard.getPosition() + b);
			}
		}*/
		/*Collections.synchronizedSet(ResourcesManager.getInstance().host.getCharSet()).remove(charToMoveToKeyboard);*/
		ThemeManager.getInstance().setKeyBoardCharTheme(charToMoveToKeyboard);
		charToMoveToKeyboard.onAreaTouched(TouchEvent.obtain(charToMoveToKeyboard.getX(), charToMoveToKeyboard.getY(), MotionEvent.ACTION_DOWN, 129, null), toX, toY);
		//charToMoveToKeyboard.mod= new MoveModifier(0.5f, charToMoveToKeyboard.getX(), charToMoveToKeyboard.getY(), toX, toY );
		//charToMoveToKeyboard.registerEntityModifier(charToMoveToKeyboard.mod);

	}


	@Deprecated
	private void test2() {


		// we establish a connection by giving the WebSockets URL of the server
		// and the handler for open/close events
		ConnectionManager.getInstance().mConnection.connect(wsuri, new Wamp.ConnectionHandler() {

			@Override
			public void onOpen() {

				// The connection was successfully established. we set the status
				// and save the host/port as Android application preference for next time.

				// We establish a prefix to use for writing URIs using shorthand CURIE notation.
				ConnectionManager.getInstance().mConnection.prefix("calc", "http://example.com/simple/calc#");

				testRpc2();
			}

			@Override
			public void onClose(int code, String reason) {

				// The connection was closed. Set the status line, show a message box,
				// and set the button to allow to connect again.
			}
		});
	}

	@Deprecated
	private void testRpc2() {

		ArrayList<Integer> nums = new ArrayList<Integer>();
		for (int i = 0; i < 10; ++i) nums.add(i);

		ConnectionManager.getInstance().mConnection.call("calc:asum", Integer.class, new Wamp.CallHandler() {

			@Override
			public void onResult(Object result) {
				int res = (Integer) result;
			}

			@Override
			public void onError(String errorId, String errorInfo) {
			}
		}, nums);

		ConnectionManager.getInstance().mConnection.call("calc:add", Integer.class, new Wamp.CallHandler() {

			@Override
			public void onResult(Object result) {
				int res = (Integer) result;
			}

			@Override
			public void onError(String errorId, String errorInfo) {
			}
		}, 23, 55);
	}



	@Deprecated
	//Latest Used publish for Room hosting broadcast
	public void sendAllRoomsBroadcastPublish(Map roomDetails){

		String topicSelected = "chat";
		ConnectionManager.getInstance().mConnection.publish(prefixHostURI+"roomsRpcRequest",  roomDetails);

	}

	//String as event rpc call Echo client call (ping Pong implementation replacement)
	@Deprecated
	public static void testRpcArray() {

		//final String echoUri = "http://192.168.1.9:8090/echo";
		//final String echoUri = "http://api.wamp.ws/procedure#echo";
		Log.e("Ping Pong", ConnectionManager.mStatusline);

		final String echoUri = prefixHostURI+"chat/echo";
		final String[] ping = {"ping"};
		ConnectionManager.getInstance().mConnection.call(echoUri, Array.class, new WampCra.CallHandler() {

			@Override
			public void onResult(Object result) {
				String res = null;
				if(result instanceof String[]){
					String[] strArr= (String[]) result;
					if(strArr.length>0){
						res = strArr[0];
					}
				}
				if(null!=res && res.equals("pong")){
					ConnectionManager.mStatusline = "Connected";
					ResourcesManager.isResponseGot =true;
				}
			}

			@Override
			public void onError(String errorId, String errorInfo) {
				ConnectionManager.mStatusline = "Connection Lost";
				Log.e(TAG, "ConnectionManager.mStatusline " +ConnectionManager.mStatusline);
				ResourcesManager.isResponseGot=false;
			}
		}, (Object[]) ping);
	}

	//String as event rpc call Echo client call (ping Pong implementation replacement)
	public static void sendHeartBeat() {

		//final String echoUri = "http://192.168.1.9:8090/echo";
		//final String echoUri = "http://api.wamp.ws/procedure#echo";
		Log.e("Ping Pong", "Ping Sent");

		final String echoUri = prefixHostURI+"chat/echo";


		final String ping = "ping";

		//final ArrayList<String> pingArray = new ArrayList<String>();
		//pingArray.add("ping");
		//new TypeReference<ArrayList<String>>(){}

		ConnectionManager.getInstance().mConnection.call(echoUri, String.class, new WampCra.CallHandler() {

			@Override
			public void onResult(Object result) {
				String pong = (String) result;

				/*String pong = null;
				if(null!=res && res instanceof ArrayList<?> && ((ArrayList<String>)res).size()>0){
					pong = ((ArrayList<String>)res).get(0);
				}
				else if(res instanceof String){
					pong =  (String) res;
				}*/
				if(null!=pong && pong.equals("pong")){
					Log.e("Ping Pong", "Pong Received");
					ConnectionManager.mStatusline = "Connected";
					ResourcesManager.isResponseGot =true;
				}
			}

			@Override
			public void onError(String errorId, String errorInfo) {
				ConnectionManager.mStatusline = "Connection Lost";
				Log.e(TAG, "ConnectionManager.mStatusline: "+ConnectionManager.mStatusline);
				ResourcesManager.isResponseGot=false;
			}
		}, "ping");
	}

	//String as event
	@Deprecated
	private void testPubSub() {
		/*ConnectionManager.getInstance().mConnection.subscribe(prefixHostURI+"subscribedTopics/chat" + ConnectionManager.mPathRoomNumberSuffix, UserDO.class, new Wamp.EventHandler() {

				@Override
				public void onEvent(String topicUri, Object event) {

					// when we get an event, we safely can cast to the type we
					// specified previously
					UserDO evt = (UserDO) event;
					ConnectionManager.sentText = evt.getWordGuessed();
					Log.d(TAG, ConnectionManager.sentText);
					ResourcesManager.getInstance().payloadFromServer = ConnectionManager.sentText;
					ResourcesManager.getInstance().hosted=false;
				}
			});*/

		ConnectionManager.getInstance().mConnection.subscribe(prefixHostURI+"subscribedTopics/chat" + ConnectionManager.mPathRoomNumberSuffix, String.class, new Wamp.EventHandler() {

			@Override
			public void onEvent(String topicUri, Object event) {

				// when we get an event, we safely can cast to the type we
				// specified previously
				String evt = (String) event;
				ConnectionManager.sentText = evt;
				Log.d(TAG, ConnectionManager.sentText);
				ResourcesManager.getInstance().payloadFromServer = ConnectionManager.sentText;
				if(null!=SceneManager.getInstance().getCurrentScene()  && (SceneManager.getInstance().getCurrentScene() instanceof RealGameInterface)){
					((RealGameInterface)SceneManager.getInstance().getCurrentScene()).addPageChunk(new UserDO());
					moveLineAndsetTransparency(SceneManager.getInstance().getCurrentScene());
					moveKeyboardAndsetTransparency(SceneManager.getInstance().getCurrentScene());
				}
				ResourcesManager.getInstance().hosted=false;
			}
		});

		ConnectionManager.getInstance().mConnection.publish(prefixHostURI+"subscribedTopics/chat" + ConnectionManager.mPathRoomNumberSuffix,"Hello, world!" + ConnectionManager.myId);
	}

	//String as event
	@Deprecated
	public void sendRand(String textToBeSent){

		String topicSelected = "chat";
		ConnectionManager.getInstance().mConnection.publish(prefixHostURI+"subscribedTopics/"+topicSelected+ + ConnectionManager.mPathRoomNumberSuffix,  textToBeSent);
	}

}
