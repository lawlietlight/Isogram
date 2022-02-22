/**
 * FacebookManager.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.managers;

import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.MainActivity;
import com.efficientsciences.cowsandbulls.wordwars.R;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebookConfiguration;
import com.sromku.simple.fb.listeners.OnInviteListener;
import com.sromku.simple.fb.listeners.OnLoginListener;
import com.sromku.simple.fb.listeners.OnLogoutListener;

/**
 * @author SATHISH
 *
 */
public class FacebookManager {
	public static final List<String> PERMISSIONS = Arrays.asList("public_profile");
	private static final List<String> PERMISSIONSFORPOST = Arrays.asList("publish_actions");
	public static String sFirstName;
	public static boolean sUserLoggedIn;
	private static OnCompleteListener listener;
	public static int reasonForLogout;
	public static String requestId;
	private static Session.StatusCallback statusCallback = new SessionStatusCallback();
	private static WebDialog dialog;
	private static Bundle dialogParams;
	private static String dialogAction;
	public static Permission[] permissions = new Permission[] {
		Permission.PUBLIC_PROFILE,
		Permission.EMAIL
		//,Permission.PUBLISH_ACTION
	};

	public static SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
	.setAppId("1571223996439841")
	.setPermissions(permissions)
	.build();
	public static String TAG="WORDWARSFB";

	//From Facebok SDK Friend Smash use below two methods together
	private static void sendBasicRequestForGame(){
		Bundle params = new Bundle();
		params.putString("message", "I just smashed " + StorageManager.getInstance().getUser().getScore() +
				" friends! Can you beat it?");
		showDialogWithoutNotificationBar("apprequests", params);
	}

	public static void deleteRequest(String inRequestId) {
		// Create a new request for an HTTP delete with the
		// request ID as the Graph path.
		Request request = new Request(Session.getActiveSession(), 
				inRequestId, null, HttpMethod.DELETE, new Request.Callback() {

			@Override
			public void onCompleted(Response response) {
				// Show a confirmation of the deletion
				// when the API call completes successfully.
				//ToastHelper.makeCustomToastOnUI("FB Request Deleted", Toast.LENGTH_LONG);
			}
		});
		// Execute the request asynchronously.
		Request.executeBatchAsync(request);
	}

	/**
	 * @param string
	 * @param params
	 */
	private static void showDialogWithoutNotificationBar(String action,
			Bundle params) {
		dialog = new WebDialog.Builder(ResourcesManager.getInstance().activity, Session.getActiveSession(), action, params).
				setOnCompleteListener(new WebDialog.OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error) {
						if (error != null && !(error instanceof FacebookOperationCanceledException)) {

						}
						dialog = null;
						dialogAction = null;
						dialogParams = null;
						if (error != null) {
							if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(ResourcesManager.getInstance().activity.getApplicationContext(), 
										"Request cancelled", 
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(ResourcesManager.getInstance().activity.getApplicationContext(), 
										"Network Error", 
										Toast.LENGTH_SHORT).show();
							}
						} else {
							final String requestId = values.getString("request");
							if (requestId != null) {
								ToastHelper.makeCustomToastOnUI("Request sent",  
										Toast.LENGTH_SHORT);
							} else {
								Toast.makeText(ResourcesManager.getInstance().activity.getApplicationContext(), 
										"Request cancelled", 
										Toast.LENGTH_SHORT).show();
							}
						}   
					}
				}).build();

		Window dialog_window = dialog.getWindow();
		dialog_window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		dialogAction = action;
		dialogParams = params;

		dialog.show();
	}
	//From Facebok SDK Friend Smash



	static void sendRequestDialog(final String pFirstName, final String pData) {

		Bundle params = new Bundle();
		params.putString("message", pFirstName+" has challenged you to break the code");
		params.putString("data", pFirstName + pData);
		params.putString("title", "Friend Invite");
		//params.putString("description", "Click on Get button to get it!");
		// params.putString("link", "https://play.google.com/store/apps/details?id=com.efficientsciences.cowsandbulls.wordwars");
		//params.putString("picture", "https://lh5.ggpht.com/UdHZ-gX1N-uAyKzONCVCiDfSlf8xjHYGRHanCx7TOpxEo9nzSIGqKihajL73-OZ5tIA");
		/*	JSONObject actions = new JSONObject();
		try {
			actions.put("name", "Get");
			actions.put("link", "https://play.google.com/store/apps/details?id=com.efficientsciences.cowsandbulls.wordwars");
		} catch (Exception e) {};*/
		params.putString("actions", "apprequests");

		WebDialog requestsDialog = (
				new WebDialog.RequestsDialogBuilder(ResourcesManager.getInstance().activity,
						Session.getActiveSession(),
						params))
						.setOnCompleteListener(new OnCompleteListener() {

							@Override
							public void onComplete(Bundle values,
									FacebookException error) {
								if (error != null) {
									if (error instanceof FacebookOperationCanceledException) {
										Toast.makeText(ResourcesManager.getInstance().activity.getApplicationContext(), 
												"Request cancelled", 
												Toast.LENGTH_SHORT).show();
									} else {
										Toast.makeText(ResourcesManager.getInstance().activity.getApplicationContext(), 
												"Network Error", 
												Toast.LENGTH_SHORT).show();
									}
								} else {
									final String requestId = values.getString("request");
									if (requestId != null) {
										ToastHelper.makeCustomToastOnUI("Request sent",  
												Toast.LENGTH_SHORT);
									} else {
										Toast.makeText(ResourcesManager.getInstance().activity.getApplicationContext(), 
												"Request cancelled", 
												Toast.LENGTH_SHORT).show();
									}
								}   
							}

						})
						.build();
		requestsDialog.show();
	}

	//For FaceBook
	//http://www.andengine.org/forums/tutorials/facebook-sdk-t10854.html?hilit=Facebook
	//https://developers.facebook.com/docs/android/send-requests#step2

	public static void sendFacebookRequestDialog() {
		Bundle params = new Bundle();
		params.putString("message", "Break this code word with the world");
		params.putString("data",
				"{\"room_number\":\""+StorageManager.getInstance().getUser().getHostedGuessedRoomIndex()+"\"," +
						"\"word_iq_score\":\""+StorageManager.getInstance().getUser().getScore()+"\"}");
		listener = new OnCompleteListener() {

			@Override
			public void onComplete(Bundle values, FacebookException error) {
				if (error != null) {
					if (error instanceof FacebookOperationCanceledException) {     
						// Request cancelled
						ToastHelper.makeCustomToastOnUIDefinedColor("Request cancelled",  
								Toast.LENGTH_SHORT);
					} else {
						// Network Error

						ToastHelper.makeCustomToastOnUIDefinedColor(" Network Error",  
								Toast.LENGTH_SHORT);
					}
				} else {
					final String requestId = values.getString("request");
					if (requestId != null) {                     
						ToastHelper.makeCustomToastOnUIDefinedColor("Request Sent",  
								Toast.LENGTH_SHORT);
						// Request sent
					} else {               
						// Request cancelled
						ToastHelper.makeCustomToastOnUIDefinedColor(" Request cancelled",  
								Toast.LENGTH_SHORT);
					}
				}   
			}
		};

		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(ResourcesManager.getInstance().activity, Session.getActiveSession(), params)).setOnCompleteListener(listener).build();

		requestsDialog.show();
	}

	public void isUserAlreadyLoggedInToFaceBook() {
		Session.openActiveSession(ResourcesManager.getInstance().activity, false, new Session.StatusCallback() {
			@SuppressWarnings("deprecation")
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								String fbUsername = user.getFirstName();
								boolean useFacebook = true;
							}
						}
					});
				}
			}
		});
	}

	public void facebookLogout() {
		Session activeSession = Session.getActiveSession();
		if (activeSession != null) {
			activeSession.closeAndClearTokenInformation();
		}
	}

	public static void getRequestData(final String inRequestId, MainActivity mainActivity) {
		if(Session.getActiveSession()==null){
			FacebookManager.checkUserLoggedIn(inRequestId,mainActivity);
			if(sFirstName ==null){
				sFirstName = StorageManager.getInstance().getUser().getDisplayName();
			}

		}
		else
			getRequestedData(inRequestId,mainActivity);
	}



	/**
	 * @param inRequestId
	 */
	private static void getRequestedData(final String inRequestId,final MainActivity activity) {
		// Create a new request for an HTTP GET with the
		// request ID as the Graph path.
		Request request = new Request(Session.getActiveSession(), 
				inRequestId, null, HttpMethod.GET, new Request.Callback() {

			@Override
			public void onCompleted(Response response) {
				// Process the returned response
				GraphObject graphObject = response.getGraphObject();
				FacebookRequestError error = response.getError();
				// Default message
				String message = "";
				if (graphObject != null) {
					// Check if there is extra data
					if (graphObject.getProperty("data") != null) {
						try {
							// Get the data, parse info to get the key/value info
							JSONObject dataObject = 
									new JSONObject((String)graphObject.getProperty("data"));
							// Get the value for the key - badge_of_awesomeness
							String roomNumber = 
									dataObject.getString("room_number");
							// Get the value for the key - social_karma
							String wordIqScoreSender = 
									dataObject.getString("word_iq_score");
							// Get the sender's name
							String sender = 
									dataObject.getString("user_name");
							/*JSONObject fromObject = 
											(JSONObject) graphObject.getProperty("from");*/
							//String sender = fromObject.getString("name");
							String title = "";
							// Create the text for the alert based on the sender
							// and the data
							if(null!=roomNumber && !roomNumber.isEmpty()){
								ConnectionManager.mPathRoomNumberSuffix=Integer.parseInt(roomNumber);
								ResourcesManager.setTutorialModeEnabled(ConnectionManager.mPathRoomNumberSuffix);

								ResourcesManager.isFaceBookRequestUnAddressed=true;
							}

							message = title + "\n" + 
									"Joining "+sender+"'s Room , Please Wait";

						} catch (JSONException e) {
							message = "Error getting request info";
						}
					} else if (error != null) {
						message = "Error getting request info";
					}
				}
				else{
					ToastHelper.makeCustomToastOnUIFBColor(
							"Either Your Friend's Room Invitation Expired\n\nOr Please Login To Facebook Again Clicking The Icon",
							Toast.LENGTH_LONG,activity);
					FacebookManager.deleteRequest(FacebookManager.requestId);
				}
				if(!message.isEmpty()){
					ToastHelper.makeCustomToastOnUIFBColor(
							message,
							Toast.LENGTH_LONG,activity);
					if(ResourcesManager.isFaceBookRequestUnAddressed && ResourcesManager.roomHostedBroadCastSubscriptionSent && ResourcesManager.chocobackbuttonregion!=null){

						ResourcesManager.getInstance().joinRandomDailyChallengeRoom(ConnectionManager.mPathRoomNumberSuffix);
						FacebookManager.deleteRequest(FacebookManager.requestId);
					}
				}
			}
		});
		// Execute the request asynchronously.
		Request.executeBatchAsync(request);
	}

	public void facebookLogin() {
		//Andengine way deprecated
		Session.openActiveSession(ResourcesManager.getInstance().activity, true, new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {
				if (session.isOpened()) {
					Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {


						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								sendFacebookRequestDialog();
							}
						}
					});
				}
			}
		}); 

		//The Facebook way
		/*Session session = Session.getActiveSession();
			if (null!=session && !session.isOpened() && !session.isClosed()) {
				session.openForRead(new Session.OpenRequest(ResourcesManager.getInstance().activity)
				.setPermissions(Arrays.asList("public_profile"))
				.setCallback(statusCallback));
			} else {
				Session.openActiveSession(ResourcesManager.getInstance().activity, true, statusCallback);
			}

			requestForPublishPermissions(session);*/
	}

	public static void requestForPublishPermissions(Session session){
		//PERMISSIONSFORPOST = Arrays.asList("publish_actions");
		if(null!=session && session.isOpened() && !session.isPermissionGranted(PERMISSIONSFORPOST.get(0))){
			Session.NewPermissionsRequest newPermissionsRequest = new Session
					.NewPermissionsRequest(ResourcesManager.getInstance().activity, PERMISSIONSFORPOST);
			session.requestNewPublishPermissions(newPermissionsRequest);
		}

	}

	//Used calls
	//Fatal
	public static void loginAndPost(final String pData) {
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				openActiveSession(ResourcesManager.getInstance().activity, true, statusCallback, PERMISSIONS);
			}
		});
	}



	public static void checkUserLoggedIn() {
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				openActiveSession(ResourcesManager.getInstance().activity, false, new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {
							Request.newMeRequest(session, new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user, Response response) {
									if (user != null) {
										sFirstName = user.getFirstName();
										FacebookManager.sUserLoggedIn = true;
									} else {
										FacebookManager.sUserLoggedIn = false;
									}
								}
							}).executeAsync();
						}
					}
				}, PERMISSIONS);
			}
		});
	}
	
	//To get Request data on first Login
	public static void checkUserLoggedIn(final String requestId, final MainActivity mainActivity) {
		mainActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				openActiveSession(mainActivity, true, new Session.StatusCallback() {
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						if (session.isOpened()) {
							Request.newMeRequest(session, new Request.GraphUserCallback() {
								@Override
								public void onCompleted(GraphUser user, Response response) {
									if (user != null) {
										sFirstName = user.getFirstName();
										FacebookManager.sUserLoggedIn = true;
										getRequestedData(requestId,mainActivity);

									} else {
										FacebookManager.sUserLoggedIn = false;
									}
								}
							}).executeAsync();
						}
					}
				}, PERMISSIONS);
			}
		});
	}

	private static Session openActiveSession(final Activity pActivity, final boolean pAllowLoginUI,
			final StatusCallback pCallback, final List<String> pPermissions) {
		final OpenRequest openRequest = new OpenRequest(pActivity).setPermissions(pPermissions)
				.setCallback(pCallback);
		openRequest.setLoginBehavior(SessionLoginBehavior.SSO_WITH_FALLBACK);
		final Session session = new Builder(pActivity.getBaseContext()).build();
		if (SessionState.CREATED_TOKEN_LOADED.equals(session.getState()) || pAllowLoginUI) {
			Session.setActiveSession(session);
			session.openForPublish(openRequest);
			return session;
		}
		return null;
	}

	/**
	 * Logout From Facebook 
	 */
	public static void callFacebookLogout(Context context) {
	    Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    } else {

	        session = new Session(context);
	        Session.setActiveSession(session);

	        session.closeAndClearTokenInformation();
	            //clear your preferences if saved

	    }

	}
	
	//Posts in my Timeline For Friends
	public static void postShare(final String pFirstName, final String pData) {
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				String localName = pFirstName;
				if(null==pFirstName){
					if(null!=StorageManager.getInstance().getUser() && StorageManager.getInstance().getUser().getDisplayName()!=null){
						localName= StorageManager.getInstance().getUser().getDisplayName();
					}
					else{
						localName= "Friend";
					}
				}
				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(ResourcesManager.getInstance().activity)
				.setLink("https://play.google.com/store/apps/details?id=com.efficientsciences.cowsandbulls.wordwars&referrer=utm_source%3D"+StorageManager.getInstance().getUser().getUserName()+"%26utm_medium%3Dfb%26utm_term%3DtestTerm%26utm_content%3DreferrerCampaign%26utm_campaign%3Dtestcampaign")
				.setDescription("Limited Time Offer For Free, Get This Game Now")
				.setApplicationName("WORD")
				.setCaption(pData+localName)
				.setName("Break The Code")
				.setPicture("http://i.imgur.com/TEQr3ze.png").build();
				ResourcesManager.getInstance().activity.uiHelper.trackPendingDialogCall(shareDialog.present());
			}


		});
	}

	//Posts in my Feed For Friends
	public static void publishFeedDialog(final String pFirstName, final String pData) {
	    Bundle params = new Bundle();
	    params.putString("name", "Break The Code");
		String localName = pFirstName;
		if(null==pFirstName){
			if(null!=StorageManager.getInstance().getUser() && StorageManager.getInstance().getUser().getDisplayName()!=null){
				localName= StorageManager.getInstance().getUser().getDisplayName();
			}
			else{
				localName= "Friend";
			}
		}
		params.putString("caption", pData+localName );
		params.putString("description", "Click on Get button to get the Game!");
		String link = "https://play.google.com/store/apps/details?id=com.efficientsciences.cowsandbulls.wordwars&referrer=utm_source%3D"+StorageManager.getInstance().getUser().getUserName()+"%26utm_medium%3Dfb%26utm_term%3DtestTerm%26utm_content%3DreferrerCampaign%26utm_campaign%3Dtestcampaign";
		params.putString("link", link);
		params.putString("picture", "http://i.imgur.com/q4Db2Pz.png");
		JSONObject actions = new JSONObject();
		try {
			actions.put("name", "Get Word");
			actions.put("link", link);
		} catch (Exception e) {};
		params.putString("actions", actions.toString());

	    WebDialog feedDialog = (
	        new WebDialog.FeedDialogBuilder(ResourcesManager.getInstance().activity,
	            Session.getActiveSession(),
	            params))
	        .setOnCompleteListener(new OnCompleteListener() {

	            @Override
	            public void onComplete(Bundle values,
	                FacebookException error) {
	                if (error == null) {
	                    // When the story is posted, echo the success
	                    // and the post Id.
	                    final String postId = values.getString("post_id");
	                    if (postId != null) {
	                    	
							String adsDisabled= "";
							if(ResourcesManager.isAdsEnabled  && ResourcesManager.isAdsEnabledRefBased){
								adsDisabled= "Now enjoy ads free gameplay for next 1 game";
							}
	                        ToastHelper.makeCustomToastOnUI(
	                            "Successfully Posted On Your Timeline\n"+adsDisabled,
	                            Toast.LENGTH_SHORT);
	                        ResourcesManager.numberOfPlaysToSkipAd=1;
							ResourcesManager.isAdsEnabled=false;
	                    } else {
	                        // User clicked the Cancel button
	                    	ToastHelper.makeCustomToastOnUI( 
	                            "Publish cancelled", 
	                            Toast.LENGTH_SHORT);
	                    }
	                } else if (error instanceof FacebookOperationCanceledException) {
	                    // User clicked the "x" button
	                	ToastHelper.makeCustomToastOnUI( 
	                        "Publish cancelled", 
	                        Toast.LENGTH_SHORT);
	                } else {
	                    // Generic, ex: network error
	                	ToastHelper.makeCustomToastOnUIDefinedColor(
	                        "Error posting story", 
	                        Toast.LENGTH_SHORT);
	                }
	            }

	        })
	        .build();
	    feedDialog.show();
	}
	
	//Posts in my News Feed For Friends Fallback, needs requestForNew Permission call before
	public static void postUsingGraph(final String pFirstName, final String pData) {
		
		ResourcesManager.getInstance().activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				requestForPublishPermissions(Session.getActiveSession());

				Bundle params = new Bundle();
				params.putString("name", "Break The Code");
				String localName = pFirstName;
				if(null==pFirstName){
					if(null!=StorageManager.getInstance().getUser() && StorageManager.getInstance().getUser().getDisplayName()!=null){
						localName= StorageManager.getInstance().getUser().getDisplayName();
					}
					else{
						localName= "Friend";
					}
				}
				params.putString("caption", pData+localName );
				params.putString("description", "Click on Get button to get the Game!");
				String link = "https://play.google.com/store/apps/details?id=com.efficientsciences.cowsandbulls.wordwars&referrer=utm_source%3D"+StorageManager.getInstance().getUser().getUserName()+"%26utm_medium%3Dfb%26utm_term%3DtestTerm%26utm_content%3DreferrerCampaign%26utm_campaign%3Dtestcampaign";
				params.putString("link", link);
				params.putString("picture", "https://lh5.ggpht.com/UdHZ-gX1N-uAyKzONCVCiDfSlf8xjHYGRHanCx7TOpxEo9nzSIGqKihajL73-OZ5tIA");
				JSONObject actions = new JSONObject();
				try {
					actions.put("name", "Get Word");
					actions.put("link", link);
				} catch (Exception e) {};
				params.putString("actions", actions.toString());

				Request.Callback callback = new Request.Callback() {
					@Override
					public void onCompleted(Response response) {
						try {
							JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
							String adsDisabled= "";
							if(ResourcesManager.isAdsEnabled  && ResourcesManager.isAdsEnabledRefBased){
								adsDisabled= "Now enjoy ads free gameplay for next 1 game";
							}
							ToastHelper.makeCustomToastOnUI("Successfully Posted On Your News Feed ,\n "+adsDisabled, Toast.LENGTH_LONG);
							ResourcesManager.numberOfPlaysToSkipAd=1;
							ResourcesManager.isAdsEnabled=false;
							@SuppressWarnings("unused")
							String postID = null;
							try {
								postID = graphResponse.getString("id");
							} catch (JSONException e) {}
						} catch (NullPointerException e) {
						}
					}
				};


				Request request = new Request(Session.getActiveSession(), "me/feed", params, HttpMethod.POST,
						callback);

				RequestAsyncTask task = new RequestAsyncTask(request);
				task.execute();
			}
		});
	}


	//For Post to wall/ feed status callback after login
	private static class SessionStatusCallback implements Session.StatusCallback {
		private SessionState sessionState;

		@Override
		public void call(final Session session, SessionState state, Exception exception) {
			// Respond to session state changes, ex: updating the view
			//sendFacebookRequestDialog();
			if (session.isOpened()) {
				sessionState= session.getState();
				Request.newMeRequest(session, new Request.GraphUserCallback() {
					private String pData;

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							sFirstName = user.getFirstName();
							//requestForPublishPermissions(session);
							sUserLoggedIn = true;
							pData ="Play With ";
							if(sessionState!=SessionState.OPENED_TOKEN_UPDATED){
								/*if(null!=NotificationManager.getInstance().notificationCreatedForFB){
									ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
										
										@Override
										public void run() {
											NotificationManager.getInstance().notificationCreatedForFB.performNoButtonClick();
											NotificationManager.getInstance().createFBLoginRequiredNotification((byte) 0);
										}
									});
									
								}*/
								publishFeedDialog(user.getFirstName(), pData);

								/*if (FacebookDialog.canPresentShareDialog(ResourcesManager.getInstance().activity.getApplicationContext(), 
                                        FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
									postShare(user.getFirstName(), pData);
								}
								else{
								post(user.getFirstName(), pData);
								}*/
								//sendBasicRequestForGame();

							}
						} else {
							sUserLoggedIn = false;
						}
					}
				}).executeAsync();
			}
		}
	}
	//Fatal


	/**
	 * 
	 */
	public static void loginFBAndInvite() {
		OnLoginListener onLoginListener = new OnLoginListener() {
			@Override
			public void onLogin() {
				FacebookManager.sUserLoggedIn = true;
				// change the state of the button or do whatever you want
				Log.i(FacebookManager.TAG, "Logged in");
				/*if(null!=NotificationManager.getInstance().notificationCreatedForFB){
					ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							NotificationManager.getInstance().notificationCreatedForFB.performNoButtonClick();
							NotificationManager.getInstance().createFBLoginRequiredNotification((byte) 1);
						}
					});
					
				}*/
				inviteFriends();

			}

			@Override
			public void onNotAcceptingPermissions(Permission.Type type) {
				// user didn't accept READ or WRITE permission
				FacebookManager.sUserLoggedIn = false;
				Log.w(FacebookManager.TAG, String.format("You didn't accept %s permissions", type.name()));
			}

			@Override
			public void onThinking() {
				FacebookManager.sUserLoggedIn = false;
				// TODO Auto-generated method stub

			}

			@Override
			public void onException(Throwable throwable) {
				FacebookManager.sUserLoggedIn = false;
				OnLogoutListener onLogoutListener = new OnLogoutListener() {



					@Override
					public void onThinking() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onException(Throwable throwable) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFail(String reason) {

					}

					@Override
					public void onLogout() {
						// TODO Auto-generated method stub
						FacebookManager.sUserLoggedIn = false;
						if(reasonForLogout==1){
							reasonForLogout=0;
							loginFBAndInvite();
						}
					}
				};

				reasonForLogout=1;
				ResourcesManager.getInstance().activity.mSimpleFacebook.logout(onLogoutListener);
				// TODO Auto-generated method stub
			}

			@Override
			public void onFail(String reason) {
				FacebookManager.sUserLoggedIn = false;
				// TODO Auto-generated method stub

			}

			/* 
			 * You can override other methods here: 
			 * onThinking(), onFail(String reason), onException(Throwable throwable)
			 */ 
		};

		ResourcesManager.getInstance().activity.mSimpleFacebook.login(onLoginListener);

	}
	public static void inviteFriends() {
		OnInviteListener onInviteListener = new OnInviteListener() {
			@Override
			public void onComplete(List<String> invitedFriends, String requestId) {
				int numOfFriends = invitedFriends.size();
				String plural= " friends";
				if(numOfFriends==1){
					plural =  " friend";
				}
				Log.i(FacebookManager.TAG, "Invitation was sent to " + numOfFriends + " users with request id " + requestId); 
				FacebookManager.sUserLoggedIn = true;
				String adsDisabled= "";
				if(ResourcesManager.isAdsEnabled  && ResourcesManager.isAdsEnabledRefBased){
					adsDisabled= "\nNow enjoy ads free gameplay for next 2 games";
					ResourcesManager.numberOfPlaysToSkipAd=2;
					if( ResourcesManager.isAdsEnabledRefBased){ //Comment After 500 installs
						if(numOfFriends>12 ){
						adsDisabled= "\nWe Are Unlocking Ads Free Version Of This Game\nSince You Have Referred More Facebook Friends\nThanks For Playing"; //Comment After 500 installs
						StorageManager.getInstance().saveUserReferredInstalls("R");
						ResourcesManager.isAdsEnabledRefBased =false;
						ToastHelper.makeCustomToastOnUIDefinedColor("\n Congratulations, You Are Now One Of Our Ads Free Priveledged Players\nEnjoy Ads Free Gameplay Now", Toast.LENGTH_LONG);
						}
						else if(numOfFriends>5){
							ResourcesManager.numberOfPlaysToSkipAd=8;
							adsDisabled= "\nNow enjoy ads free gameplay 8 games";
						}
					}//Comment After 500 installs
				}
				ToastHelper.makeCustomToastOnUI("Invitation was sent to " + numOfFriends + plural + adsDisabled, Toast.LENGTH_LONG);
				
				
				ResourcesManager.isAdsEnabled=false;
			}

			@Override
			public void onCancel() {
				Log.i(FacebookManager.TAG, "Canceled the dialog");
			}

			@Override
			public void onException(Throwable throwable) {
				FacebookManager.sUserLoggedIn = false;
				OnLogoutListener onLogoutListener = new OnLogoutListener() {



					@Override
					public void onThinking() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onException(Throwable throwable) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFail(String reason) {

					}

					@Override
					public void onLogout() {
						// TODO Auto-generated method stub
						FacebookManager.sUserLoggedIn = false;
						if(reasonForLogout==2){
							reasonForLogout=0;
							inviteFriends();
						}
					}
				};

				reasonForLogout=2;
				ResourcesManager.getInstance().activity.mSimpleFacebook.logout(onLogoutListener);
				// TODO Auto-generated method stub
				/*ResourcesManager.getInstance().activity.mSimpleFacebook.getSession().getActiveSession().refreshPermissions();
					ResourcesManager.getInstance().activity.mSimpleFacebook.getSession().closeAndClearTokenInformation();*/
			}

			@Override
			public void onFail(String reason) {
				// TODO Auto-generated method stub
				//FacebookManager.sUserLoggedIn = false;
			}


			/* 
			 * You can override other methods here: 
			 * onFail(String reason), onException(Throwable throwable)
			 */
		};
		ResourcesManager.getInstance().activity.mSimpleFacebook.invite("Break this code and challenge with me", onInviteListener, "{\"room_number\":\""+StorageManager.getInstance().getUser().getHostedGuessedRoomIndex()+"\"," +
				"\"word_iq_score\":\""+StorageManager.getInstance().getUser().getScore()+"\"," +
				"\"user_name\":\""+StorageManager.getInstance().getUser().getDisplayName()+"\"}");
	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}
	
	/**
	 * @param graphObject
	 * @param error
	 */@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
		public static void addNotification(GraphObject result, FacebookRequestError error)
		{
			String title = null;
			String alertMessage = null;
			if (error == null) {
				title = "Helping Hand";
				alertMessage = "We'll Send A Helper Bee Pass By";
				StorageManager.getInstance().getUser().setNumOfBees(StorageManager.getInstance().getUser().getNumOfBees()+1);;
			} else {
				title = "Error Posting To Facebook";
				alertMessage = error.getErrorMessage();
			}

			NotificationCompat.Builder mBuilder =
					new NotificationCompat.Builder(ResourcesManager.getInstance().activity)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(title)
			.setAutoCancel(true)
			.setContentText(alertMessage);
			// Creates an explicit intent for an Activity in your app
			Intent resultIntent = new Intent(ResourcesManager.getInstance().activity,MainActivity.class);

			// The stack builder object will contain an artificial back stack for the
			// started Activity.
			// This ensures that navigating backward from the Activity leads out of
			// your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(ResourcesManager.getInstance().activity);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(MainActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent =
					stackBuilder.getPendingIntent(
							0,
							PendingIntent.FLAG_UPDATE_CURRENT
							);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager =
					(NotificationManager) ResourcesManager.getInstance().activity.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.
			int mId = 0;
			mNotificationManager.notify(mId, mBuilder.build());
		}

	//For FaceBook
}
