package com.efficientsciences.cowsandbulls.wordwars;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.managers.FacebookManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.services.IMService;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.Builder;
import com.facebook.Session.OpenRequest;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.nineoldandroids.animation.Animator;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class LoginSign extends MainActivity
implements View.OnClickListener {



	/*
	 * API INTEGRATION SECTION. This section contains the code that integrates
	 * the game with the Google Play game services API.
	 */

	// Debug tag
	final static boolean ENABLE_DEBUG = true;
	final static String TAG = "WordWarsTM";
	// private boolean isFinishCalled=false;

	// Request codes for the UIs that we show with startActivityForResult:
	/*final static int RC_SELECT_PLAYERS = 10000;
	final static int RC_INVITATION_INBOX = 10001;
	final static int RC_WAITING_ROOM = 10002;*/
	static int numberOfAccounts = 1 ;
	private YoYo.YoYoString rope;

	private static int continueButton;
	private static int signInButton;
	private static int signOutButton;

	private static int guestButton;
	private static int fbLoginButton;
	private static int relativelayout;
	// Room ID where the currently active game is taking place; null if we're
	// not playing.
	//String mRoomId = null;

	// Are we playing in multiplayer mode?
	//boolean mMultiplayer = false;

	// The participants in the currently active game
	//ArrayList<Participant> mParticipants = null;

	// My participant ID in the currently active game
	String mMyId = null;

	// If non-null, this is the id of the invitation we received via the
	// invitation listener
	//String mIncomingInvitationId = null;

	// Message buffer for sending messages
	//byte[] mMsgBuf = new byte[2];

	// This array lists all the individual screens our game has.
	final static int[] SCREENS = {
		R.layout.sign_in
	};
	public static final int PICK_ACCOUNT_REQUEST = 6;
	//int mCurScreen = -1;


	protected void displayMemoryUsage(String message) {
		int usedKBytes = (int) (Debug.getNativeHeapAllocatedSize() / 1024L);
		String usedMegsString = String.format("%s - usedMemory = Memory Used: %d KB", message, usedKBytes);
		Log.d(TAG, usedMegsString);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		enableDebugLog(ENABLE_DEBUG, TAG);
		ResourcesManager.getInstance().activity=this;
		super.onCreate(savedInstanceState);
		int maxHeapInDevice= (int)(Runtime.getRuntime().maxMemory() /(1024L* 1024L));
		Log.d(" MAX SPACE ", " maxHeapInDevice " + maxHeapInDevice);
		int sdk = android.os.Build.VERSION.SDK_INT;

		/*LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		View view = inflater.inflate(R.layout.sign_in, null);
		LinearLayout layout =(LinearLayout)view.findViewById(R.id.screen_sign_in);
		 */

		if(maxHeapInDevice>20){
			setContentView(R.layout.sign_in_grandbg);
			signInButton = R.id.button_sign_in_bg;
			continueButton = R.id.button_continue_bg;
			signOutButton = R.id.button_sign_out_bg;
			fbLoginButton = R.id.authButton_bg;
			guestButton = R.id.button_anonymous_bg;
			Log.d(" HEAP SPACE ", " NORMAL ");
		}
		else{
			setContentView(R.layout.sign_in);
			signInButton = R.id.button_sign_in;
			continueButton = R.id.button_continue;
			signOutButton = R.id.button_sign_out;
			fbLoginButton = R.id.authButton;
			guestButton = R.id.button_anonymous;
			Log.d(" HEAP SPACE ", " Less VM SPACE ");
		}
		((Button)findViewById(continueButton)).setVisibility(View.GONE);
		((Button)findViewById(signOutButton)).setVisibility(View.GONE);
		//findViewById(R.id.button_sign_in).setVisibility(View.GONE);
		/*        ((TextView)findViewById(R.id.howtoplaytext)).setMovementMethod(new ScrollingMovementMethod());
		 */        // set up a click listener for everything we care about
		long duration = 400;
		if(mHelper.mConnecting){
			duration = 2400;
		}

		numberOfAccounts =  getAccountNames().length;

		//((Button)findViewById(R.id.button_sign_in)).setVisibility(View.GONE);
		playAnimationInitial(signInButton,Techniques.FadeIn, duration, View.VISIBLE);

		playAnimationInitial(guestButton,Techniques.FadeIn, duration, View.VISIBLE);
		playAnimationInitial(fbLoginButton,Techniques.FadeIn, duration, View.VISIBLE);

		if(maxHeapInDevice>20){
			for (int id : CLICKABLES_BG) {
				View tempView = findViewById(id);
				tempView.setOnClickListener(this);

			}
		}
		else{
			for (int id : CLICKABLES) {
				View tempView = findViewById(id);
				tempView.setOnClickListener(this);

			}
		}
	}


	/**
	 * @param viewId
	 */
	private void playAnimation(int viewId, Techniques technique, long duration, final int visible) {
		final View tempView = findViewById(viewId);
		rope =  YoYo.with(technique)
				.duration(duration)
				.interpolate(new AccelerateDecelerateInterpolator())
				.withListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {

					}

					@Override
					public void onAnimationEnd(Animator animation) {
						tempView.setVisibility(visible);
					}

					@Override
					public void onAnimationCancel(Animator animation) {
						//Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}
				})
				.playOn(tempView);
	}

	/**
	 * @param viewId
	 */
	private void playAnimationInitial(int viewId, Techniques technique, long duration, final int visible) {
		final View tempView = findViewById(viewId);
		rope =  YoYo.with(technique)
				.duration(duration)
				.interpolate(new AccelerateDecelerateInterpolator())
				.withListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animation) {
						tempView.setVisibility(visible);
					}

					@Override
					public void onAnimationEnd(Animator animation) {

					}

					@Override
					public void onAnimationCancel(Animator animation) {
						//Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onAnimationRepeat(Animator animation) {

					}
				})
				.playOn(tempView);
	}

	private String[] getAccountNames() {
		AccountManager mAccountManager = AccountManager.get(this);
		Account[] accounts = mAccountManager.getAccountsByType(
				GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
		String[] names = new String[1];
		if(null!=accounts && accounts.length>0){
			names = new String[accounts.length];
			for (int i = 0; i < names.length; i++) {
				names[i] = accounts[i].name;
				Log.e(TAG+"Accounts ", names[i]);
			}
		}
		return names;
	}

	/**
	 * Called by the base class (BaseGameActivityGApi) when sign-in has failed. For
	 * example, because the user hasn't authenticated yet. We react to this by
	 * showing the sign-in button.
	 */
	@Override
	public void onSignInFailed() {
		Log.d(TAG, "Sign-in failed.");
		playAnimation(signInButton,Techniques.Shake, 200, View.GONE);

		if(null!=uiHelper && Session.getActiveSession()!=null && ((LoginButton)findViewById(fbLoginButton)).getText().toString().equals(getString(R.string.com_facebook_loginview_log_out_button))){
			Session session=Session.getActiveSession();
			if(session.getState()==SessionState.OPENED){
				((Button)(findViewById(guestButton))).setText("Continue");
				((Button)(findViewById(guestButton))).setTextColor(getResources().getColor(R.color.com_facebook_loginview_text_color));
				((Button)(findViewById(guestButton))).setBackgroundResource(R.drawable.com_facebook_button_blue);
			}
		}

		playAnimation(guestButton,Techniques.FadeIn, 200, View.VISIBLE);
		playAnimation(fbLoginButton,Techniques.FadeIn, 200, View.VISIBLE);
	}


	/**
	 * Called by the base class (BaseGameActivityGApi) when sign-in succeeded. We
	 * react by going to our main screen.
	 */
	@Override
	public void onSignInSucceeded() {
		Log.d(TAG, "Sign-in succeeded.");

		if (rope != null) {
			rope.stop(true);
		}

			final UserDO user=StorageManager.getInstance().getUser();
					Player currentPerson = Games.Players.getCurrentPlayer(mHelper.mGoogleApiClient);
					if (currentPerson != null) {


						String userName=currentPerson.getDisplayName();

						if(null==userName || userName.isEmpty()){
							userName=currentPerson.getPlayerId();
						}

						String personName=currentPerson.getName();

						if(null==personName || personName.isEmpty()){
							personName= currentPerson.getDisplayName();
						}

						if(null!=userName && (null==user.getUserName() || -1!=user.getUserName().indexOf("WordPlayer#") ||  !user.getUserName().equals(userName))){
							user.setUserName(userName);
						}
						if(null!=userName){
							IMService.setHostUserName(userName);
						}

						if(null==user.getCurrentLocation() || user.getCurrentLocation().trim().equals("")){
							String location = "";
							if(null!=mHelper.mLastLocation){
								location = getCountryName(ResourcesManager.getInstance().activity, mHelper.mLastLocation.getLatitude(),  mHelper.mLastLocation.getLatitude());
							}
							user.setCurrentLocation(location);
						}

						if(null!=personName){
							int nameLength=personName.length();
							personName = personName.replaceAll("[^a-zA-Z0-9 ]", "");
							if(nameLength>6){
								user.setDisplayName(personName.substring(0,7));
							}
							else {
								user.setDisplayName(personName+ResourcesManager.appendSpace(7-nameLength));
							}
						}
						else{
							String nameToDisplay = user.getDisplayName();
							if(null!=nameToDisplay){
								int nameLength = nameToDisplay.length();
								if(nameLength>6){
									if(-1!=nameToDisplay.indexOf("WordPlayer") && nameToDisplay.length()>13){
										nameToDisplay = "Guest"+nameToDisplay.substring(10,14);
									}
									else{
										nameToDisplay = nameToDisplay.substring(0,7);
									}
									user.setDisplayName(nameToDisplay);
								}
								else {
									nameToDisplay = nameToDisplay+ResourcesManager.appendSpace(7-nameLength);
								}
							}
						}

						if(currentPerson.hasIconImage() && null!=currentPerson.getIconImageUrl()){
							user.setUserProfilePicUrl(currentPerson.getIconImageUrl());
							Log.d(TAG, "person.getCover():" +currentPerson.getIconImageUrl());

						}
						else if(currentPerson.hasHiResImage()){
							user.setUserProfilePicUrl(currentPerson.getHiResImageUrl());
							Log.d(TAG, "person.getCover():" + currentPerson.getHiResImageUrl());
						}
						StorageManager.getInstance().saveUser(user);
					}

		/*if ( mHelper.acct != null) {
			String personName = mHelper.acct.getDisplayName();
			String personGivenName = mHelper.acct.getGivenName();
			String personFamilyName = mHelper.acct.getFamilyName();
			String userName = mHelper.acct.getEmail();
			String personId = mHelper.acct.getId();
			Uri personPhoto = mHelper.acct.getPhotoUrl();

			if(null!=userName && (null==user.getUserName() || -1!=user.getUserName().indexOf("WordPlayer#") ||  !user.getUserName().equals(userName))){
				user.setUserName(userName);
			}
			if(null!=userName){
				IMService.setHostUserName(userName);
			}

			if(null==user.getCurrentLocation() || user.getCurrentLocation().trim().equals("")){
				String location = "";
				if(null!=mHelper.mLastLocation){
					location = getCountryName(ResourcesManager.getInstance().activity, mHelper.mLastLocation.getLatitude(),  mHelper.mLastLocation.getLatitude());
				}
						*//*if((null==location || location.trim().equals("")) && (null!=currentPerson && null!=currentPerson.get)){
							for(PlacesLived place : Plus.PeopleApi.getCurrentPerson(mHelper.mGoogleApiClient).getPlacesLived()){
								//If the place is primary, record it and break
								if(place.isPrimary()){
									location = place.getValue();

									break;
								}
								//If it isn't and there isn't any location yet, record the most recent location and wait to see if another is primary
								if(null==location || location.trim().equals("")){
									location = place.getValue();
								}
							}
						}*//*


				user.setCurrentLocation(location);
			}

			if(null!=personName){
				int nameLength=personName.length();
				personName = personName.replaceAll("[^a-zA-Z0-9 ]", "");
				if(nameLength>6){
					user.setDisplayName(personName.substring(0,7));
				}
				else {
					user.setDisplayName(personName+ResourcesManager.appendSpace(7-nameLength));
				}
			}
			else{
				String nameToDisplay = user.getDisplayName();
				if(null!=nameToDisplay){
					int nameLength = nameToDisplay.length();
					if(nameLength>6){
						if(-1!=nameToDisplay.indexOf("WordPlayer") && nameToDisplay.length()>13){
							nameToDisplay = "Guest"+nameToDisplay.substring(10,14);
						}
						else{
							nameToDisplay = nameToDisplay.substring(0,7);
						}
						user.setDisplayName(nameToDisplay);
					}
					else {
						nameToDisplay = nameToDisplay+ResourcesManager.appendSpace(7-nameLength);
					}
				}
			}

			if(null!=personPhoto){
				user.setUserProfilePicUrl(personPhoto.toString());
				Log.d(TAG, "person.getCover():" +personPhoto.toString());

			}
			StorageManager.getInstance().saveUser(user);
		}*/

		// register listener so we are notified if we receive an invitation to play
		// while we are in the game
		//Games.Invitations.registerInvitationListener(getApiClient(), this);

		// if we received an invite via notification, accept it; otherwise, go to main screen
		/*if (getInvitationId() != null) {
			return;
		}*/

		playAnimation(signInButton,Techniques.SlideOutDown, 1000, View.INVISIBLE);
		//((SignInButton)findViewById(signInButton)).setVisibility(View.INVISIBLE);
		playAnimation(guestButton,Techniques.SlideOutDown, 1000, View.INVISIBLE);
		playAnimation(fbLoginButton,Techniques.SlideOutDown, 1000, View.INVISIBLE);

		((Button)findViewById(continueButton)).setVisibility(View.VISIBLE);
		playAnimation(continueButton,Techniques.BounceInLeft, 1200, View.VISIBLE);


		((Button)findViewById(signOutButton)).setVisibility(View.VISIBLE);
		playAnimation(signOutButton,Techniques.BounceInRight, 1200, View.VISIBLE);

		switchToMainScreen();
	}

	private void copyDataFromFBToLocal(){
		final Session session=Session.getActiveSession();
		if(null!=session && session.isOpened())
			Request.newMeRequest(session, new Request.GraphUserCallback() {
				@Override
				public void onCompleted(GraphUser currentPerson, Response response) {
					if (currentPerson != null) {
						ResourcesManager.googleFbOrGuest = 1;
						if(null!=StorageManager.getInstance().getUser()){
							final UserDO storedUser=StorageManager.getInstance().getUser();
							/*if(null==StorageManager.getInstance().getUser().getUserName() || StorageManager.getInstance().getUser().getUserName().isEmpty()){
							user.setUserName(Plus.AccountApi.getAccountName(mHelper.mGoogleApiClient));
						}*/
							String userName = (String) currentPerson.getProperty("email");
							if(null!=userName){
								storedUser.setUserName(userName);
								IMService.setHostUserName(userName);
							}
							String personName = currentPerson.getFirstName();
							if(null!=personName)
								storedUser.setDisplayName(personName);

							if(null!=currentPerson.getLocation())
								storedUser.setCurrentLocation(currentPerson.getLocation().getName());
							String age= currentPerson.getBirthday();

							if(null!=age){
								SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd");
								try {

									Date dateOfBirth = formatter.parse(age);
									storedUser.setAge(getAge(dateOfBirth));

								} catch (ParseException e) {
									e.printStackTrace();
								}
							}

							if(null!=personName){
								int nameLength=personName.length();
								personName = personName.replaceAll("[^a-zA-Z0-9 ]", "");
								if(nameLength>6){
									storedUser.setDisplayName(personName.substring(0,7));
								}
								else {
									storedUser.setDisplayName(personName+ResourcesManager.appendSpace(7-nameLength));
								}
							}
							else{
								String nameToDisplay = storedUser.getDisplayName();
								if(null!=nameToDisplay){
									int nameLength = nameToDisplay.length();
									if(nameLength>6){
										if(-1!=nameToDisplay.indexOf("WordPlayer") && nameToDisplay.length()>13){
											nameToDisplay = "Guest"+nameToDisplay.substring(10,14);
										}
										else{
											nameToDisplay = nameToDisplay.substring(0,7);
										}
										storedUser.setDisplayName(nameToDisplay);
									}
									else {
										nameToDisplay = nameToDisplay+ResourcesManager.appendSpace(7-nameLength);
									}
								}
							}
							if(null!=userName){
								Bundle params = new Bundle();
								params.putBoolean("redirect", false);
								params.putInt("height", 160);
								params.putInt("width", 160);
								params.putString("type", "normal");
								/* make the API call */
								new Request(
								    session,
								    "/me/picture",
								    params,
								    HttpMethod.GET,
								    new Request.Callback() {
								        public void onCompleted(Response response) {
								        	JSONObject data = null;
											try {
												data = response.getGraphObject().getInnerJSONObject().getJSONObject("data");
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

						                    String url = null;
											try {
												url = (String) data.getString("url");
											} catch (JSONException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											if(null!=url)
								        	storedUser.setUserProfilePicUrl(url);
								        }
								    }
								).executeAsync();
							}
							StorageManager.getInstance().saveUser(storedUser);

							FacebookManager.sUserLoggedIn = true;
						} else {
							FacebookManager.sUserLoggedIn = false;
						}
					}
				}
			}).executeAsync();
	}

	public void checkUserLoggedIn() {
		Session session=Session.getActiveSession();
		if(null!=session && session.isOpened() && ((LoginButton)findViewById(fbLoginButton)).getText().toString().equals(getString(R.string.com_facebook_loginview_log_out_button))){
			((Button)(findViewById(guestButton))).setText("Guest");
			((Button)(findViewById(guestButton))).setTextColor(getResources().getColor(R.color.ButtonTextColor));
			((Button)(findViewById(guestButton))).setBackgroundColor(getResources().getColor(R.color.guest_color));

			((SignInButton)findViewById(signInButton)).setVisibility(View.VISIBLE);

			playAnimation(signInButton,Techniques.DropOut, 500, View.VISIBLE);
		}
		else if(session==null || !session.isOpened()){
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					openActiveSession(LoginSign.this, true, new Session.StatusCallback() {
						@Override
						public void call(Session session, SessionState state, Exception exception) {
							copyDataFromFBToLocal();

						}
					}, FacebookManager.PERMISSIONS);
				}
			});
		}
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

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void dimSoftButtonsIfPossible() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}


	@Override
	public void onClick(View v) {
		//Intent intent;

		switch (v.getId()) {

		case R.id.button_sign_in:
		case R.id.button_sign_in_bg:
			Intent intent = new Intent(this, MainActivity.class);

			startActivity(intent);
			overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);


			if(!mHelper.isSignedIn())
				beginUserInitiatedSignIn();

			// isFinishCalled=true;
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
			break;
		case R.id.button_anonymous:
		case R.id.button_anonymous_bg:
			Intent intent1 = new Intent(this, MainActivity.class);

			startActivity(intent1);
			overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
			ResourcesManager.googleFbOrGuest = 2;
			if(((Button)findViewById(guestButton)).getText().toString().equals("Continue")){
				copyDataFromFBToLocal();
			}

			// isFinishCalled=true;
			finish();
			overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
			break;
		case R.id.authButton:
		case R.id.authButton_bg:
			//Intent intent2 = new Intent(this, MainActivity.class);

			//startActivity(intent2);
			//overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);
			//ResourcesManager.googleFbOrGuest = 1;
			checkUserLoggedIn();
			// isFinishCalled=true;
			//finish();
			//overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
			break;
		case R.id.button_continue:
		case R.id.button_continue_bg:
			continueButtonClick();
			break;
		case  R.id.button_sign_out:
		case  R.id.button_sign_out_bg:
			if(ResourcesManager.googleFbOrGuest==0){
				if(null!=mHelper){
					mHelper.signOut();
				}
			}
			else if(ResourcesManager.googleFbOrGuest==1){
				FacebookManager.callFacebookLogout(this);
			}
			((SignInButton)findViewById(signInButton)).setVisibility(View.VISIBLE);
			((LoginButton)findViewById(fbLoginButton)).setVisibility(View.VISIBLE);
			(findViewById(guestButton)).setVisibility(View.VISIBLE);

			playAnimation(signInButton,Techniques.DropOut, 800, View.VISIBLE);

			playAnimation(fbLoginButton,Techniques.DropOut, 800, View.VISIBLE);
			playAnimation(guestButton,Techniques.DropOut, 800, View.VISIBLE);

			playAnimation(signOutButton,Techniques.TakingOff, 1200, View.INVISIBLE);
			playAnimation(continueButton,Techniques.TakingOff, 1200, View.INVISIBLE);
			//((Button)findViewById(continueButton)).setVisibility(View.GONE);
			//((Button)findViewById(signOutButton)).setVisibility(View.GONE);
			if(numberOfAccounts>1 && null!=mHelper){
				mHelper.beginUserInitiatedSignIn();
			}
			else{
				showGoogleAccountPicker();
			}
			break;
		}
	}

	private void continueButtonClick() {
		if(ResourcesManager.googleFbOrGuest == 0 && null!=mHelper){
			mHelper.mSignInFailureReason = null;
			mHelper.mConnectOnStart = true;
			mHelper.mUserInitiatedSignIn = false;
			mHelper.mConnecting = false;
		}
		else{
			if(null!=mHelper){
				mHelper.mConnectOnStart = false;
			}

			ResourcesManager.googleFbOrGuest = 1;
		}
		//onSignInSucceeded();
		Intent intent2 = new Intent(this, MainActivity.class);
		//intent2.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(intent2);

		overridePendingTransition(R.anim.activity_open_translate,R.anim.activity_close_scale);

		// isFinishCalled=true;
		finish();
		overridePendingTransition(R.anim.activity_open_scale,R.anim.activity_close_translate);
	}

	private void showGoogleAccountPicker() {
		Intent googlePicker = AccountPicker.newChooseAccountIntent(null, null,
				new String[] { GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE }, true, null, null, null, null);
		startActivityForResult(googlePicker, PICK_ACCOUNT_REQUEST);
	}



	@Override
	protected void onPause() {
		super.onPause();
		if(null!= MusicService.mPlayer && MusicService.mPlayer.isPlaying() && !isFinishing() && !mHelper.isConnecting()){
			MusicService.pauseMusic();
		}

	}

	/*void startQuickGame() {
		// quick-start a game with 1 randomly selected opponent
		final int MIN_OPPONENTS = 1, MAX_OPPONENTS = 1;
		Bundle autoMatchCriteria = RoomConfig.createAutoMatchCriteria(MIN_OPPONENTS,
				MAX_OPPONENTS, 0);
		RoomConfig.Builder rtmConfigBuilder = RoomConfig.builder(this);
		rtmConfigBuilder.setMessageReceivedListener(this);
		rtmConfigBuilder.setRoomStatusUpdateListener(this);
		rtmConfigBuilder.setAutoMatchCriteria(autoMatchCriteria);

		resetGameVars();
		Games.RealTimeMultiplayer.create(getApiClient(), rtmConfigBuilder.build());
	}*/

	@Override
	public void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);



	}



	/*
	 * CALLBACKS SECTION. This section shows how we implement the several games
	 * API callbacks.
	 */

	// Called when we are connected to the room. We're not ready to play yet! (maybe not everybody
	// is connected yet).
	/*@Override
	public void onConnectedToRoom(Room room) {
		Log.d(TAG, "onConnectedToRoom.");

		// get room ID, participants and my ID:
		mRoomId = room.getRoomId();
		mParticipants = room.getParticipants();
		mMyId = room.getParticipantId(Games.Players.getCurrentPlayerId(getApiClient()));

		// print out the list of participants (for debug purposes)
		Log.d(TAG, "Room ID: " + mRoomId);
		Log.d(TAG, "My ID " + mMyId);
		Log.d(TAG, "<< CONNECTED TO ROOM>>");
	}*/

	// Called when we've successfully left the room (this happens a result of voluntarily leaving
	// via a call to leaveRoom(). If we get disconnected, we get onDisconnectedFromRoom()).
	/*@Override
	public void onLeftRoom(int statusCode, String roomId) {
		// we have left the room; return to main screen.
		Log.d(TAG, "onLeftRoom, code " + statusCode);
		switchToMainScreen();
	}*/

	// Called when we get disconnected from the room. We return to the main screen.
	/*@Override
	public void onDisconnectedFromRoom(Room room) {
		mRoomId = null;

	}*/



	/*@Override
	public void onP2PDisconnected(String participant) {
	}

	@Override
	public void onP2PConnected(String participant) {
	}

	@Override
	public void onPeerJoined(Room room, List<String> arg1) {
	}

	@Override
	public void onPeerLeft(Room room, List<String> peersWhoLeft) {
	}

	@Override
	public void onRoomAutoMatching(Room room) {
	}

	@Override
	public void onRoomConnecting(Room room) {
	}

	@Override
	public void onPeersConnected(Room room, List<String> peers) {
	}

	@Override
	public void onPeersDisconnected(Room room, List<String> peers) {
	}*/

	/*
	 * GAME LOGIC SECTION. Methods that implement the game's rules.
	 */

	// Current state of the game:
	int mSecondsLeft = -1; // how long until the game ends (seconds)
	final static int GAME_DURATION = 20; // game duration, seconds.
	int mScore = 0; // user's current score

	// Reset game variables in preparation for a new game.
	void resetGameVars() {
		mSecondsLeft = GAME_DURATION;
		mScore = 0;
		mParticipantScore.clear();
		mFinishedParticipants.clear();
	}

	/*
	 * COMMUNICATIONS SECTION. Methods that implement the game's network
	 * protocol.
	 */

	// Score of other participants. We update this as we receive their scores
	// from the network.
	Map<String, Integer> mParticipantScore = new HashMap<String, Integer>();

	// Participants who sent us their final score.
	Set<String> mFinishedParticipants = new HashSet<String>();

	// Called when we receive a real-time message from the network.
	// Messages in our game are made up of 2 bytes: the first one is 'F' or 'U'
	// indicating
	// whether it's a final or interim score. The second byte is the score.
	// There is also the
	// 'S' message, which indicates that the game should start.
	/*@Override
	public void onRealTimeMessageReceived(RealTimeMessage rtm) {
		byte[] buf = rtm.getMessageData();
		String sender = rtm.getSenderParticipantId();
		Log.d(TAG, "Message received: " + (char) buf[0] + "/" + (int) buf[1]);

		if (buf[0] == 'F' || buf[0] == 'U') {
			// score update.
			int existingScore = mParticipantScore.containsKey(sender) ?
					mParticipantScore.get(sender) : 0;
					int thisScore = (int) buf[1];
					if (thisScore > existingScore) {
						// this check is necessary because packets may arrive out of
						// order, so we
						// should only ever consider the highest score we received, as
						// we know in our
						// game there is no way to lose points. If there was a way to
						// lose points,
						// we'd have to add a "serial number" to the packet.
						mParticipantScore.put(sender, thisScore);
					}

					// update the scores on the screen

					// if it's a final score, mark this participant as having finished
					// the game
					if ((char) buf[0] == 'F') {
						mFinishedParticipants.add(rtm.getSenderParticipantId());
					}
		}
	}*/

	/*
	 * UI SECTION. Methods that implement the game's UI.
	 */

	// This array lists everything that's clickable, so we can install click
	// event handlers.
	final static int[] CLICKABLES = {
		R.id.button_sign_in,R.id.button_continue, R.id.button_sign_out, R.id.authButton, R.id.button_anonymous

	};

	final static int[] CLICKABLES_BG = {
		R.id.button_sign_in_bg,R.id.button_continue_bg, R.id.button_sign_out_bg, R.id.authButton_bg, R.id.button_anonymous_bg

	};

	void switchToScreen() {
		continueButtonClick();
		//finish();

	}

	void switchToScreen(int screenId) {
		// make the requested screen visible; hide all others.
		for (int id : SCREENS) {
			findViewById(id).setVisibility(screenId == id ? View.VISIBLE : View.GONE);
		}
		/*mCurScreen = screenId;

		// should we show the invitation popup?
		boolean showInvPopup;
		if (mIncomingInvitationId == null) {
			// no invitation, so no popup
			showInvPopup = false;
		} else if (mMultiplayer) {
			// if in multiplayer, only show invitation on main screen
			showInvPopup = (mCurScreen == R.layout.sign_in);
		} */

		setContentView(screenId);
		//findViewById(R.id.invitation_popup).setVisibility(showInvPopup ? View.VISIBLE : View.GONE);
	}

	void switchToMainScreen() {
		//if(isSignedIn()){
		switchToScreen();
		/*}else{
    	switchToScreen(R.layout.sign_in);
    	}*/
	}

	/*@Override
	public void onInvitationReceived(Invitation invitation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInvitationRemoved(String invitationId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onJoinedRoom(int statusCode, Room room) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomConnected(int statusCode, Room room) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomCreated(int statusCode, Room room) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPeerDeclined(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}*/

	/*@Override
	protected void onStop() {
		 Log.d(TAG, "**** got onStop");

	        // if we're in a room, leave it.
	        leaveRoom();

	        // stop trying to keep the screen on
	        stopKeepingScreenOn();

	        super.onStop();

	};*/

	// Leave the room.
	/*void leaveRoom() {
		Log.d(TAG, "Leaving room.");
		mSecondsLeft = 0;
		stopKeepingScreenOn();
		if (mRoomId != null) {
			Games.RealTimeMultiplayer.leave(getApiClient(), this, mRoomId);
			mRoomId = null;
			switchToScreen(R.layout.screen_wait);
		} 
	}*/

	// Clears the flag that keeps the screen on.
	void stopKeepingScreenOn() {
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	/*@Override
	public void onPeerInvitedToRoom(Room arg0, List<String> arg1) {
		// TODO Auto-generated method stub

	}*/


}
