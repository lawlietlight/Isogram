package com.efficientsciences.cowsandbulls.wordwars;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.GLES20;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.efficientsciences.cowsandbulls.wordwars.chat.WidgetsWindow;
import com.efficientsciences.cowsandbulls.wordwars.domain.RoomProperties;
import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;
import com.efficientsciences.cowsandbulls.wordwars.helper.AppRater;
import com.efficientsciences.cowsandbulls.wordwars.helper.GameHelper;
import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.FacebookManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.TimerManager;
import com.efficientsciences.cowsandbulls.wordwars.scenes.LoadingScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.MainMenuScene;
import com.efficientsciences.cowsandbulls.wordwars.scenes.WaitingScene;
import com.efficientsciences.cowsandbulls.wordwars.services.IAppManager;
import com.efficientsciences.cowsandbulls.wordwars.services.IMService;
import com.efficientsciences.cowsandbulls.wordwars.services.MusicService;
import com.efficientsciences.cowsandbulls.wordwars.socketCon.AutoBahnConnectorPubSub;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.ruucdprrwp.AdController;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnDeleteListener;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

import org.andengine.AndEngine;
import org.andengine.engine.Engine;
import org.andengine.engine.LimitedFPSEngine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.debug.Debug;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import eu.inmite.android.lib.dialogs.ISimpleDialogListener;
import wei.mark.standout.StandOutWindow;

//commented



public  class MainActivity extends BaseGameActivity implements
		GameHelper.GameHelperListener, ContactListener, ISimpleDialogListener   {
	//private AdView adView;
	//StartApp Ads - Start
	//commented
	//public StartAppAd startAppAd = new StartAppAd(this);
	//public StartAppNativeAd startAppNativeAd = new StartAppNativeAd(this);
	//StartApp Ads -End

	//commented
	// get the VunglePub instance
	public final VunglePub vunglePub = VunglePub.getInstance();
	//commented
	private AdController leadBoltBanner;

	public PhysicsWorld mPhysWorld;
	public static FixtureDef mCellFixtureDef = PhysicsFactory.createFixtureDef(1f, 0f, 1f,false);


	private Camera camera;

	private ResourcesManager resourcesManager;

	private TaskResponse  taskTracker;

	MainMenuScene m;
	LoadingScene l;

	public UiLifecycleHelper uiHelper;

	public IAppManager imService;

	private ServiceConnection mConnection = new ServiceConnection() {



		public void onServiceConnected(ComponentName className, IBinder service) {
			imService = ((IMService.IMBinder)service).getService();
		}
		public void onServiceDisconnected(ComponentName className) {
			imService = null;
			Toast.makeText(MainActivity.this, "Word Disconnected From World",
					Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	protected void onSetContentView() {

		if(!AndEngine.isDeviceSupported(this) && !Build.FINGERPRINT.startsWith("generic")) {
			//this device is not supported, create a toast to tell the user
			//then kill the activity

			Thread thread = new Thread() {
				@Override
				public void run() {
					try{
						Thread.sleep(3500);
						android.os.Process.killProcess(android.os.Process.myPid());
					}
					catch (InterruptedException e) {}
				}
			};
			ToastHelper.makeCustomToastOnUI("Your device does not support 3D graphics pipeline GLES 2.0, this game will not work. Sorry.",Toast.LENGTH_LONG,this);
			finish();
			thread.start();
			System.exit(0);

		}
		else {
			//this device is supported
			//if(!Build.FINGERPRINT.startsWith("generic")){
			//mRenderSurfaceView.setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
			//}


			this.mRenderSurfaceView = new RenderSurfaceView(this);

			this.mRenderSurfaceView.setRenderer(this.mEngine, this);
			preserveEglContext();
			//StartApp Ads - Start
			//user id , app id

			if(null!=StorageManager.getInstance().getUser()){
				/*SDKAdPreferences sdkAdPref= new SDKAdPreferences();
				sdkAdPref.setAge(StorageManager.getInstance().getUser().getAge());
				Gender gender= null;
				if(StorageManager.getInstance().getUser().getGender()==1){
					gender= Gender.FEMALE;
				}
				else{
					gender= Gender.MALE;
				}
				sdkAdPref.setGender(gender);*/
				//StartAppSDK.init(this, "107658503", "207642702",sdkAdPref, true);
			}
			else{
				//commented
				//StartAppSDK.init(this, "107658503", "207642702", true);
				//StartApp Ads -End
			}



			if(this instanceof LoginSign){
				final Activity thisRef=this;
				final Runnable runAds = new Runnable() {
					@Override
					public void run() {
						//StartApp Slider Ads - Start
						//StartAppAd.showSlider(thisRef);
						//StartApp Ads - End

					}
				};
				//
				this.runOnUiThread(runAds);
			}

			//Uncomment Below Line for Adless Gameplay and comment  lines within having  StartApp Ads - Start
			this.setContentView(this.mRenderSurfaceView, BaseGameActivity.createSurfaceViewLayoutParams());

			//StartApp Ads - Start
			// SETUP THE LAYOUT FOR THE AD
			// THE AD WILL APPEAR ON TOP OF EVERYTHING
			/*		 final FrameLayout frameLayout = new FrameLayout(this);
	 	        final FrameLayout.LayoutParams frameLayoutLayoutParams =
	 	                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
	 	                                             FrameLayout.LayoutParams.MATCH_PARENT,
	 	                                             Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM);

	 	        //frameLayout.setBackgroundColor(Color.rgb(255, 210, 59));
	 	      //For Ad  getSizeApiIndependent();
	 		    //DISPLAY ADD AT BOTTOM
	 	     //For Ad   FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams((int)(90*ResourcesManager.resourceScaler), (int)(130*ResourcesManager.resourceScaler), Gravity.CENTER_VERTICAL|Gravity.RIGHT);

	 	        final android.widget.FrameLayout.LayoutParams surfaceViewLayoutParams =
	 	                new FrameLayout.LayoutParams(BaseGameActivity.createSurfaceViewLayoutParams());
	 	        //mRenderSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
	 	        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);

	 	        //CHOOSE TYPE OF STARTAPP AD
	 	        //Banner startAppBanner = new Banner(this);//INCLUDES 3D ROTATING BANNER
	 	      //commented
	          //For Ad   BannerStandard startAppBanner = new BannerStandard(this); //TRADITIONAL BANNER ONLY
	 	      //commented
	 	      //For Ad  frameLayout.addView(startAppBanner, adViewLayoutParams);


	 	        this.setContentView(frameLayout, frameLayoutLayoutParams);


	 	       // mRenderSurfaceView.setZOrderOnTop(false);
	 		//StartApp Ads - End
			 */

		}


	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onPositiveButtonClicked(int requestCode) {
		if(requestCode == 2){
			this.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + AppRater.APP_PNAME)));
			if (AppRater.editor != null) {
				AppRater.editor.putBoolean("dontshowagain", true);
				AppRater.editor.commit();
			}
			AppRater.mBuildDialog1.dismiss();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onNegativeButtonClicked(int requestCode) {
		if(requestCode == 2){
			if (AppRater.editor != null) {
				AppRater.editor.putBoolean("dontshowagain", true);
				AppRater.editor.commit();
			}
			AppRater.mBuildDialog1.dismiss();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onNeutralButtonClicked(int requestCode) {
		if(requestCode == 2){
			AppRater.mBuildDialog1.dismiss();
		}
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void preserveEglContext(){
		ResourcesManager.uriData=null;
		// Check for an incoming notification. Save the info
		getFacebookRequestId(this.getIntent());
		// Uri intentUri = this.getIntent().getData();
		// if(null==intentUri && ResourcesManager.requestId==null ){
		this.mRenderSurfaceView.setBackgroundColor(Color.rgb(255, 210, 59));
		// }
		/*else{
	    	if(!(this instanceof LoginSign)){
	    		this.mRenderSurfaceView = new RenderSurfaceView(this);
	    	this.mRenderSurfaceView.setZOrderOnTop(true);
	    	this.mRenderSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
	    	this.mRenderSurfaceView.getHolder().setFormat(PixelFormat.RGBA_8888);
	 		this.mRenderSurfaceView.setRenderer(this.mEngine, this);

	    		if(null!=this.getIntent().getExtras() && !this.getIntent().getExtras().getBoolean(FACEBOOKLOGINKILLPREVIOUSACTIVITY)){
	    			ResourcesManager.uriData=intentUri;
	    			Intent in =new Intent(MainActivity.this, com.efficientsciences.cowsandbulls.wordwars.MainActivity.class);
	    			if(null!=ResourcesManager.uriData){
	    				in.setData(ResourcesManager.uriData);
	    				in.putExtra(FACEBOOKLOGINKILLPREVIOUSACTIVITY, true);
	    			}
	    			startActivity(in);
	    			finish();
	    			android.os.Process.killProcess(android.os.Process.myPid());
	    			System.exit(0);
	    			//yellowcleared=false;
	    		}
	    		else{
	    			ResourcesManager.uriData=null;
	    		}
	    	}
	    }*/
		if(android.os.Build.VERSION.SDK_INT >= 11){
			this.mRenderSurfaceView.setPreserveEGLContextOnPause(true);
		}
	}

	/**
	 * @param intent
	 */
	private void getFacebookRequestId(Intent intent) {
		Uri intentUri = intent.getData();
		if (intentUri != null) {
			String requestIdParam = intentUri.getQueryParameter("request_ids");
			if (requestIdParam != null) {
				String array[] = requestIdParam.split(",");
				int index=0;
				if(null!=array && array.length>0){
					index=array.length-1;
				}
				FacebookManager.requestId = array[index];
				Log.i(TAG, "Request id: "+FacebookManager.requestId);
				FacebookManager.getRequestData(FacebookManager.requestId,this);
			}
		}
		else{
			onReceive(intent);
		}
	}
	public void onReceive(Intent intent)
	{
		Bundle extra = intent.getExtras();
		if(null!=extra){
			String username = extra.getString("username");
			int roomNumber = extra.getInt("room_number");
			intent.removeExtra("username");
			intent.removeExtra("room_number");
			if (username != null)
			{
				IMService.isGameFromServiceNotify = true;
				ConnectionManager.mPathRoomNumberSuffix= roomNumber;
				ResourcesManager.setTutorialModeEnabled(ConnectionManager.mPathRoomNumberSuffix);

				ResourcesManager.isFaceBookRequestUnAddressed=true;

				String message = "\n" +
						"Joining "+username+"'s Room , Please Wait";
				if(!message.isEmpty()){
					ToastHelper.makeCustomToastOnUI(
							message,
							Toast.LENGTH_LONG,this);
					if(ResourcesManager.isFaceBookRequestUnAddressed && ResourcesManager.roomHostedBroadCastSubscriptionSent && ResourcesManager.chocobackbuttonregion!=null){

						ResourcesManager.getInstance().joinRandomDailyChallengeRoom(ConnectionManager.mPathRoomNumberSuffix);
						FacebookManager.deleteRequest(FacebookManager.requestId);
					}
				}

				//ResourcesManager.getInstance().joinRandomDailyChallengeRoom(ConnectionManager.mPathRoomNumberSuffix);

			}
		}
	}
	private final EventListener vungleListener = new EventListener(){

		@Override
		public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
			if(isCompletedView){
				ResourcesManager.vungleAdShownCount++;
				ResourcesManager.vungleAdShown = true;
			}
			else{
				ToastHelper.makeCustomToastOnUIDefinedColor(" Invite Your Facebook Friends To Game To Suppress Ads", Toast.LENGTH_LONG);
			}
			Log.e("Vungle onVideoView:", " Count "+ResourcesManager.vungleAdShownCount + ", isCompletedView "+ isCompletedView);
		}

		@Override
		public void onAdStart() {
			// Called before playing an ad
			Log.e("Vungle", " onAdStart: vungleAdShownCount"+ResourcesManager.vungleAdShownCount);
		}



		@Override
		public void onAdUnavailable(String arg0) {
			ResourcesManager.vungleAdShown = false;
			ResourcesManager.getInstance().numberOfVungleAdsSkipped = 3;
			Log.e("Vungle" , "onAdUnavailable: vungleAdShownCount"+ResourcesManager.vungleAdShownCount);
		}

		@Override
		public void onAdEnd(boolean arg0) {

		}

		@Override
		public void onAdPlayableChanged(boolean arg0) {

		}

		/*@Override
		public void onAdEnd() {
			// TODO Auto-generated method stub
			Log.e("Vungle", " onAdEnd: vungleAdShownCount"+ResourcesManager.vungleAdShownCount);

		}

		@Override
		public void onCachedAdAvailable() {
			// Called when a cached ad is downloaded and ready to be played
			Log.e("Vungle", "onCachedAdAvailable: vungleAdShownCount"+ResourcesManager.vungleAdShownCount);

		}*/

	};
	private boolean yellowcleared;


	private void initializeLeadBolt() {
		final Runnable runAds = new Runnable() {

			@Override
			public void run() {
				Point sceneDimensions=getSizeApiIndependent();

				if((sceneDimensions.y/resourcesManager.density)<=250)
					leadBoltBanner = new AdController(ResourcesManager.getInstance().activity, "373630019");
				else if((sceneDimensions.y/resourcesManager.density)<=350)
					leadBoltBanner = new AdController(ResourcesManager.getInstance().activity, "903042122");
				else
					leadBoltBanner = new AdController(ResourcesManager.getInstance().activity, "947197899");
				//leadBoltBanner = new AdController(ResourcesManager.getInstance().activity, "562377295");
				//leadBoltBanner = new AdController(ResourcesManager.getInstance().activity, "373630019");
				//leadBoltBanner.hideElements();
				leadBoltBanner.loadAdToCache();

			}
		};
		//
		this.runOnUiThread(runAds);

	}

	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


	    // Create an ad.
	    adView = new AdView(this, AdSize.BANNER, "a152f914e841b45"); //Admob

	    // Add the AdView to the view hierarchy. The view will have no size
	    // until the ad is loaded.
	    RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearLayout);
	    layout.addView(adView);

	    // Create an ad request. Check logcat output for the hashed device ID to
	    // get test ads on a physical device.
	    AdRequest adRequest = new AdRequest();
	    adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
	    adRequest.addTestDevice("672FA49C86F12B99DE9AD1F917FF51EF");

	    // Start loading the ad in the background.
	    adView.loadAd(adRequest);
	  }
	 */

	@Override
	public EngineOptions onCreateEngineOptions(){
		int camWidth,camHeight;
		Point camDimensions=getSizeApiIndependent();
		camWidth=camDimensions.x;
		camHeight=camDimensions.y;
		Log.i(MainActivity.class.getName()," camWidth: "+ camWidth + " camHeight: "+camHeight);
		camera = new Camera(0, 0, camWidth, camHeight);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new RatioResolutionPolicy(camWidth, camHeight), this.camera);
		engineOptions.getAudioOptions().setNeedsMusic(true).setNeedsSound(true);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return engineOptions;
	}

	/*@Override
	public void onCreateResources()
			throws IOException {




	}*/

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws IOException {
		while(null==mEngine){
			//Log.e("Activity Game Engine Not Initialised", "Activity Game Engine Not Initialised" + Calendar.getInstance().getTimeInMillis());
		}
		ResourcesManager.prepareManager(mEngine, this, camera, getVertexBufferObjectManager(), getSizeApiIndependent(), mPhysWorld);

		resourcesManager = ResourcesManager.getInstance();

		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws IOException {

		Runnable run = new Runnable() {

			@Override
			public void run() {

				ConnectionManager.getInstance().prepareConnection();

				StorageManager.getInstance().loadPrefs();
				if(null!=StorageManager.tempUser && null!=StorageManager.tempUser.getUserName() && null!=StorageManager.tempUser.getDisplayName()){
					if(null!=StorageManager.getInstance().getUser()){
						if(null!=StorageManager.getInstance().getUser().getUserName() && !StorageManager.getInstance().getUser().getUserName().isEmpty() && !StorageManager.getInstance().getUser().getUserName().contains("WordPlayer#")){
							UserDO user=StorageManager.getInstance().getUser();
							if(null!=imService){
								((IMService)imService).saveUserName(user.getUserName());
							}
							StorageManager.getInstance().saveUser(user);
						}
					}
				}
				else{
					StorageManager.getInstance().loadPrefs(MainActivity.this, "", "");
					if(null!=StorageManager.getInstance().getUser()){
						if(null!=StorageManager.getInstance().getUser().getUserName() && !StorageManager.getInstance().getUser().getUserName().isEmpty() && !StorageManager.getInstance().getUser().getUserName().contains("WordPlayer#")){
							/*UserDO user=StorageManager.getInstance().getUser();
							StorageManager.getInstance().saveUser(user);*/
							if(null!=imService){
								((IMService)imService).saveUserName(StorageManager.getInstance().getUser().getUserName());
							}
						}
					}
				}
			}
		};
		runOnUiThread(run);



		//mRenderSurfaceView.setZOrderMediaOverlay(true);
		//this.mRenderSurfaceView.setBackgroundResource(0);

		l=(LoadingScene)SceneManager.getInstance().createSplashScene();


		ResourcesManager.getInstance().mPhysWorld = new PhysicsWorld(new Vector2(0, 0), true);
		ResourcesManager.getInstance().mPhysWorld .setContactListener(this);
		mPhysWorld=ResourcesManager.getInstance().mPhysWorld ;
		//SceneManager.getInstance().disposeSplashScene();
		//LeadBolt Ads - Start
		//commented
		initializeLeadBolt();
		//LeadBolt Ads - End

		//Vungle Ads - Start
		//commented
		vunglePub.init(this, "53d523e90acc0f294200002d"); //Current prod
		//vunglePub.init(this, "53d5241b0acc0f2942000032"); //Previous Prod 
		vunglePub.setEventListeners(vungleListener);
		//Vungle Ads - End

		pOnCreateSceneCallback.onCreateSceneFinished(l);
		if(!yellowcleared)
			ResourcesManager.getInstance().activity.clearYellowColorDrawable();
	}

	/**
	 *
	 */
	public void clearYellowColorDrawable() {
		yellowcleared = true;
		Runnable runOnUiThread= new Runnable() {

			@Override
			public void run() {
				//this.mRenderSurfaceView.setBackground(null);
				//MainActivity.this.mRenderSurfaceView.setBackgroundResource(0);
				//MainActivity.this.mRenderSurfaceView.setBackground(null);
				//MainActivity.this.mRenderSurfaceView.refreshDrawableState();
				MainActivity.this.mRenderSurfaceView.setBackgroundResource(0);
				MainActivity.this.mRenderSurfaceView.refreshDrawableState();
			}
		};
		runOnUiThread(runOnUiThread);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private Point getSize(){
		Display display = getWindowManager().getDefaultDisplay();
		Point point=new Point();
		display.getSize(point);

		return point;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void getSize(Point p){
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		p.x = metrics.widthPixels;
		p.y = metrics.heightPixels;
		if(metrics.density!=0){
			ResourcesManager.density= metrics.density;
			ResourcesManager.resourceScaler= ((float)metrics.widthPixels)/1024f;
			//ResourcesManager.resourceScaler=(float) Math.sqrt(metrics.density);
			ResourcesManager.lineMovedTo = 50 * ResourcesManager.resourceScaler;
		}
		ResourcesManager.scalingFactorX=metrics.xdpi;
		ResourcesManager.scalingFactorY=metrics.ydpi;
		printSecreenInfo(metrics);
	}

	void printSecreenInfo( DisplayMetrics metrics){


		Log.i(TAG, "density :" +  metrics.density);

		// density interms of dpi
		Log.i(TAG, "D density :" +  metrics.densityDpi);

		// horizontal pixel resolution
		Log.i(TAG, "width pix :" +  metrics.widthPixels);

		// horizontal pixel resolution
		Log.i(TAG, "height pix :" +  metrics.heightPixels);

		// actual horizontal dpi
		Log.i(TAG, "xdpi :" +  metrics.xdpi);

		// actual vertical dpi
		Log.i(TAG, "ydpi :" +  metrics.ydpi);

		// actual Scaled dpi
		Log.i(TAG, "Scaled density :" +  metrics.scaledDensity);

		Log.i(TAG, "ResourcesManager.resourceScaler:" + ResourcesManager.resourceScaler);

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void dimSoftButtonsIfPossible() {
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		}
	}

	static class TaskResponse extends TimerTask {
		@Override
		public void run() {

			Log.e("Response Timer Task", "Repeat");

			Runnable runTimerTaskOnUIThread= new Runnable() {

				@Override
				public void run() {
					if(null!=ConnectionManager.getInstance().mConnection && ConnectionManager.getInstance().mConnection.isConnected()){
						if(!ResourcesManager.isResponseGot && !ConnectionManager.getInstance().mConnection.isConnectionClosed()){
							//ResourcesManager.isResponseGot=true;
							if(ConnectionManager.noConnectionAttempts>12){
								ConnectionManager.noConnectionAttempts=0;
							}
							if(!ConnectionManager.getInstance().prepareConnection()){
								ResourcesManager.isResponseGot=false;
								AutoBahnConnectorPubSub.sendHeartBeat();
							}
						}
						else{
							ResourcesManager.isResponseGot=false;
							AutoBahnConnectorPubSub.sendHeartBeat();
						}
					}
				}
			};

			ResourcesManager.getInstance().activity.runOnUiThread(runTimerTaskOnUIThread);
		}

	}

	public static final int WRITE_EXTERNAL_STORAGE = 122;

	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case WRITE_EXTERNAL_STORAGE: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.

				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws IOException
	{
		mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {

			@Override
			public void onTimePassed(TimerHandler pTimerHandler) {

				mEngine.unregisterUpdateHandler(pTimerHandler);
				if(haveNetworkConnection()){
					TimerManager.responseTimer= new Timer();
					Runnable runpingpong= new Runnable() {

						@Override
						public void run() {
							taskTracker=new TaskResponse();
							//Initial Delay Of 2 Minutes And twice the delay in-between pingpong beats
							int delay = 120000;
							Log.e("delay", delay+"");
							TimerManager.responseTimer.scheduleAtFixedRate(taskTracker, delay, delay);
						}
					};

					ResourcesManager.getInstance().activity.runOnUiThread(runpingpong);
				}
				m=(MainMenuScene)SceneManager.getInstance().createMenuScene();

				SceneManager.getInstance().disposeSplashScene();
				if(ResourcesManager.isFaceBookRequestUnAddressed && ResourcesManager.roomHostedBroadCastSubscriptionSent){

					ResourcesManager.getInstance().joinRandomDailyChallengeRoom(ConnectionManager.mPathRoomNumberSuffix);
					/*onDeleteListener=new OnDeleteListener() {

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
							// TODO Auto-generated method stub
							super.onFail(reason);
							Log.e("onFail ", "APP onFail"+reason);
						}
					};
					mSimpleFacebook.deleteRequest(FacebookManager.requestId, onDeleteListener);*/
					FacebookManager.deleteRequest(FacebookManager.requestId);
				}
				if(!yellowcleared)
					ResourcesManager.getInstance().activity.clearYellowColorDrawable();
				//m.setBackground(new Background(0.09804f, 0.6274f, 0.8784f));
				MainActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						AppRater.app_launched(MainActivity.this,false);
					}
				});

			}
		}
		));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}

	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		if(null!=netInfo)
			for (NetworkInfo ni : netInfo) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
					if (ni.isConnected())
						haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
					if (ni.isConnected())
						haveConnectedMobile = true;
			}
		return haveConnectedWifi || haveConnectedMobile;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions)
	{
		return new LimitedFPSEngine(pEngineOptions, 60);
	}


	public Point getSizeApiIndependent(){
		Point point=new Point();
		if(android.os.Build.VERSION.SDK_INT >= 13){
			point=getSize();
			getSize(point);

		}
		else{
			DisplayMetrics metrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(metrics);
			point.x=metrics.widthPixels;
			point.y=metrics.heightPixels;
			if(metrics.density!=0){
				ResourcesManager.density= metrics.density;
				ResourcesManager.resourceScaler= ((float)metrics.widthPixels)/1024f;

				//ResourcesManager.resourceScaler= (point.x/1024)*(float) Math.sqrt(metrics.density);
				//ResourcesManager.resourceScaler=(float) Math.sqrt(metrics.density);
				ResourcesManager.lineMovedTo = 50 * ResourcesManager.resourceScaler;
			}
		}
		return point;
	}

	@Override
	public void beginContact(Contact contact) {


	}

	@Override
	public void endContact(Contact contact) {


	}



	/*@Override
	public Scene onCreateScene() {

		Runnable run = new Runnable() {

			@Override
			public void run() {
				ConnectionManager.getInstance().prepareConnection();
			}
		};
		runOnUiThread(run);


		m=(MainMenuScene)SceneManager.getInstance().createMenuScene();
		SceneManager.getInstance().disposeSplashScene();
		return m;
	}*/




	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent) {

		if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) {
			if(null==this.m){
				if(null!=mHelper)
					mHelper.signOut();
				ConnectionManager.getInstance().mConnection.disconnect();
				IMService.isAppRunning=false;
				System.exit(0);
			}
			if(this.m.hasChildScene()) {
				/* Remove the menu and reset it. */
				if(this.m.getSceneType().equals(SceneType.SCENE_MENU)){
					if(null!=mHelper)
						mHelper.signOut();
				}
				this.m.back();
			} else {
				/* Attach the menu. */
				this.m.setChildScene(m.getMenuChildScene(), false, true, true);
			}
			return true;
		}
		else  if (pKeyCode == KeyEvent.KEYCODE_BACK){
			if(null==SceneManager.getInstance().getCurrentScene()){
				if(null!=mHelper)
					mHelper.signOut();
				ConnectionManager.getInstance().mConnection.disconnect();
				IMService.isAppRunning=false;
				System.exit(0);
			}

			SceneManager.getInstance().getCurrentScene().onBackKeyPressed();

			return true;
		}
		else {
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}

	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
		//if(haveNetworkConnection())

		/*if(null!=SoundManager.getInstance().mServ)
			SoundManager.getInstance().mServ.pauseMusic();

		else */
		if(!ResourcesManager.getInstance().showing && null!= MusicService.mPlayer && MusicService.mPlayer.isPlaying()){
			MusicService.pauseMusic();
			//SoundManager.getInstance().doUnbindService(this);
		}
		ResourcesManager.getInstance().isGamePaused = true;
	}

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();

		IMService.isAppRunning = true;

		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dimSoftButtonsIfPossible();
			}
		});
		/*if(null!=SoundManager.getInstance().mServ)
			SoundManager.getInstance().mServ.resumeMusic();	
		else */
		if(null!= MusicService.mPlayer && !MusicService.mPlayer.isPlaying()){
			MusicService.resumeMusic();
		}
		ResourcesManager.getInstance().isGamePaused = false;
	}

	//Google games client


	// The game helper object. This class is mainly a wrapper around this object.
	protected GameHelper mHelper;

	// We expose these constants here because we don't want users of this class
	// to have to know about GameHelper at all.
	public static final int CLIENT_GAMES = GameHelper.CLIENT_GAMES;
	public static final int CLIENT_APPSTATE = GameHelper.CLIENT_SNAPSHOT;
	public static final int CLIENT_PLUS = GameHelper.CLIENT_PLUS;
	public static final int CLIENT_ALL = GameHelper.CLIENT_ALL;

	// Requested clients. By default, that's just the games client.
	protected int mRequestedClients = CLIENT_GAMES;

	private final static String TAG = "MainActivity";
	//private static final String FACEBOOKLOGINKILLPREVIOUSACTIVITY = "facebookError";
	protected boolean mDebugLog = false;
	public SimpleFacebook mSimpleFacebook;
	public OnDeleteListener onDeleteListener;


	/** Constructs a MainActivity with default client (GamesClient). */
	public  MainActivity() {
		super();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dimSoftButtonsIfPossible();
			}
		});
	}

	/**
	 * Constructs a MainActivity with the requested clients.
	 * @param requestedClients The requested clients (a combination of CLIENT_GAMES,
	 *         CLIENT_PLUS and CLIENT_APPSTATE).
	 */
	public MainActivity(int requestedClients) {
		super();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				dimSoftButtonsIfPossible();
			}
		});
		setRequestedClients(requestedClients);
	}

	/**
	 * Sets the requested clients. The preferred way to set the requested clients is
	 * via the constructor, but this method is available if for some reason your code
	 * cannot do this in the constructor. This must be called before onCreate or getGameHelper()
	 * in order to have any effect. If called after onCreate()/getGameHelper(), this method
	 * is a no-op.
	 *
	 * @param requestedClients A combination of the flags CLIENT_GAMES, CLIENT_PLUS
	 *         and CLIENT_APPSTATE, or CLIENT_ALL to request all available clients.
	 */
	protected void setRequestedClients(int requestedClients) {
		mRequestedClients = requestedClients;
	}

	public GameHelper getGameHelper() {
		if (mHelper == null) {
			mHelper = new GameHelper(this, mRequestedClients);
			mHelper.enableDebugLog(mDebugLog);
		}
		return mHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SimpleFacebook.setConfiguration(FacebookManager.configuration);
		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);


		if(ResourcesManager.googleFbOrGuest==0){
			if (mHelper == null) {
				getGameHelper();
			}

			mHelper.setup(this);
		}

		if(IMService.CONNECTED_VIA_SERVICE == 0 && haveNetworkConnection()){
			startService(new Intent(MainActivity.this,  IMService.class));
		}


		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			@Override
			public void uncaughtException(Thread paramThread, Throwable paramThrowable) {
				handleApplicationErrors(paramThread, paramThrowable);
			}
		});
	}

	public void handleApplicationErrors(Thread paramThread, Throwable paramThrowable){
		//if(null==ResourcesManager.uriData){
		Log.e("Alert","Lets See if it Works !!!" +"paramThread:::" +paramThread +"paramThrowable:::" +paramThrowable + Log.getStackTraceString(paramThrowable));

		//ToastHelper.makeCustomToastOnUI("We are trying to decipher the issue, \n Please restart the application if you are experiencing Problems",Toast.LENGTH_LONG);
		Intent in =new Intent(MainActivity.this, com.efficientsciences.cowsandbulls.wordwars.MainActivity.class);
		/* if(null!=ResourcesManager.uriData){
        in.setData(ResourcesManager.uriData);
        in.putExtra(FACEBOOKLOGINKILLPREVIOUSACTIVITY, true);
        }*/
		//in.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		//Intent restartIntent = this.getPackageManager().getLaunchIntentForPackage(this.getPackageName());
		//in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent intent = PendingIntent.getActivity(
				this, 0,
				in, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager mgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, intent);
		System.exit(2);
		//startActivity(in);

		//finish();
		//android.os.Process.killProcess(android.os.Process.myPid()); 
		//}
	}

	@Override
	protected void onStart() {
		super.onStart();
		//ResourcesManager.getInstance().loadMenuResources();
		if(null!=mHelper)
			mHelper.onStart(this);
	}

	@Override
	protected void onStop() {

		super.onStop();
		if(null!=mHelper)
			mHelper.onStop();
		try{
			StandOutWindow.closeAll(this, WidgetsWindow.class);
		}
		catch(Exception e){

		}		//SoundManager.getInstance().doUnbindService(this);
		/*	if(null!=SoundManager.getInstance().mServ){
			//SoundManager.getInstance().mServ.unbindService(SoundManager.getInstance().Scon);
			Log.e("On Stop: ", SoundManager.getInstance().mIsBound+"");
			//SoundManager.getInstance().mServ.stopMusicAndRelease();
			//SoundManager.getInstance().mServ.stopSelf();

		}*/
	}

	@Override
	protected void onActivityResult(int request, int response, Intent data) {
		if(null!=mSimpleFacebook)
			mSimpleFacebook.onActivityResult(this, request, response, data);
		//FB
		if(null!=uiHelper)
			uiHelper.onActivityResult(request, response, data, new FacebookDialog.Callback() {
				@Override
				public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
					Log.e("Activity", String.format("Error: %s", error.toString()));
				}

				@Override
				public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
					Log.i("Activity", "Success!");
					ResourcesManager.numberOfPlaysToSkipAd=1;
					ResourcesManager.isAdsEnabled=false;
					// boolean didCancel = FacebookDialog.getNativeDialogDidComplete(data);
					// String completionGesture = FacebookDialog.getNativeDialogCompletionGesture(data);
					String postId = FacebookDialog.getNativeDialogPostId(data);
					if(null!=postId){

					}

				}
			});
		super.onActivityResult(request, response, data);


		if(null!=mHelper)
			mHelper.onActivityResult(request, response, data);

		if (request == MainMenuScene.MESSAGE_RESULT) {
			// Make sure the request was successful
			if (response ==  MainMenuScene.PICK_CONTACT_REQUEST) {
				ToastHelper.makeCustomToast("E-Mail sent successfully", Toast.LENGTH_LONG);
			}
		}
		else if(request ==  MainMenuScene.REQUEST_ACHIEVEMENTS){
			ResourcesManager.getInstance().showing = false;
		}
		else if(request ==  ResourcesManager.WHATSAPPRESPONSECODE || ResourcesManager.fileShared){
			ResourcesManager.fileShared = false;
			this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					File myFile = new File(WaitingScene.FILEDIR+"/"+WaitingScene.nameOfImageShared);
					if(myFile.exists())
						myFile.delete();
				}
			});
			ResourcesManager.getInstance().detachInUpdateThread(ResourcesManager.getInstance().overlayRect);

		}
		/*if(null!=SceneManager.getInstance() && null!=SceneManager.getInstance().getCurrentSceneType() && SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_MENU){
			((MainMenuScene)(SceneManager.getInstance().getCurrentScene())).onActivityResult(request, response, data);
		}*/
		/*if (Session.getActiveSession() != null) {
            Session.getActiveSession().onActivityResult(this, request, response, data);
		}*/
	}


	public GoogleApiClient getApiClient() {
		if(ResourcesManager.googleFbOrGuest!=0 && null==mHelper){
			this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					mHelper=getGameHelper();
					mHelper.setup(MainActivity.this);
					mHelper.onStart(MainActivity.this);
				}
			});

		}
		return mHelper.getApiClient();
	}

	protected boolean isSignedIn() {
		if(null!=mHelper)
			return mHelper.isSignedIn();

		return false;
	}

	protected void beginUserInitiatedSignIn() {
		if(null!=mHelper)
			mHelper.beginUserInitiatedSignIn();
	}

	protected void signOut() {
		if(null!=mHelper)
			mHelper.signOut();
	}

	protected void showAlert(String message) {
		if(null!=mHelper)
			mHelper.makeSimpleDialog(message).show();
	}

	protected void showAlert(String title, String message) {
		if(null!=mHelper)
			mHelper.makeSimpleDialog(title, message).show();
	}

	protected void enableDebugLog(boolean enabled) {
		mDebugLog = true;
		if (mHelper != null) {
			mHelper.enableDebugLog(enabled);
		}
	}

	@Deprecated
	protected void enableDebugLog(boolean enabled, String tag) {
		Log.w(TAG, "MainActivity.enabledDebugLog(bool,String) is " +
				"deprecated. Use enableDebugLog(boolean)");
		enableDebugLog(enabled);
	}

	/*protected String getInvitationId() {
		return mHelper.getInvitationId();
	}*/

	protected void reconnectClient() {
		if(null!=mHelper)
			mHelper.reconnectClient();
	}

	protected boolean hasSignInError() {
		if(null!=mHelper)
			return mHelper.hasSignInError();

		return false;
	}

	protected GameHelper.SignInFailureReason getSignInError() {
		if(null!=mHelper)
			return mHelper.getSignInError();

		return null;
	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		if(null!=StorageManager.getInstance().getUser()){
			final UserDO user=StorageManager.getInstance().getUser();
			/*if(null==StorageManager.getInstance().getUser().getUserName() || StorageManager.getInstance().getUser().getUserName().isEmpty()){
				user.setUserName(Plus.AccountApi.getAccountName(mHelper.mGoogleApiClient));
			}*/

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
		}
	}


	public static int getAge(Date dateOfBirth) {

		Calendar today = Calendar.getInstance();
		Calendar birthDate = Calendar.getInstance();

		int age = 0;

		birthDate.setTime(dateOfBirth);
		if (birthDate.after(today)) {
			throw new IllegalArgumentException("Can't be born in the future");
		}

		age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

		// If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
		if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
				(birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
			age--;

			// If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
		}else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
				(birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
			age--;
		}

		return age;
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();
		uiHelper.onDestroy();
		if(null!=mHelper)
			mHelper.onStop();
		doUnbindService();

		Log.e("onDestroy Word Game", "unbind service called");
		if(null!=ConnectionManager.getInstance().mConnection && ConnectionManager.getInstance().mConnection.isConnected()){
			ConnectionManager.getInstance().mConnection.disconnect();
		}
		/*SoundManager.getInstance().doUnbindService(this);
		Log.e("onDestroy Word Game", "unbind service called");
		if(null!=SoundManager.getInstance().mServ){
			Log.e("onDestroy Word Game", "mserv not null and stop music called");
			//SoundManager.getInstance().mServ.stopMusic();
			SoundManager.getInstance().mServ.stopMusicAndRelease();
			SoundManager.getInstance().mServ.stopService(new Intent(MainActivity.this, MusicService.class));
			doUnbindService();
		}*/
		/*if (localstoragehandler != null) {
			localstoragehandler.close();
		}
		if (dbCursor != null) {
			dbCursor.close();
		}
		 */
	}

	void doUnbindService()
	{
		if(SoundManager.getInstance().mIsBound)
		{
			//unbindService(SoundManager.getInstance().Scon);
			SoundManager.getInstance().mIsBound = false;
		}
	}

	@Override
	protected void onPause() {

		super.onPause();
		uiHelper.onPause();
		unbindService(mConnection);
		//StartApp Ads - Start
		//commented
		if(ResourcesManager.isAdsEnabled){

			//startAppAd.onPause();
			//StartApp Ads - End

			//commented
			if(!(this instanceof LoginSign))
				vunglePub.onPause();
		}
		try{
			StandOutWindow.closeAll(this, WidgetsWindow.class);
		}
		catch(Exception e){

		}
		//unregisterReceiver(messageReceiver);

		//Facebook
		// Logs 'app deactivate' App Event.
		// AppEventsLogger.deactivateApp(this);
		/*if(null!=SoundManager.getInstance().mServ)
			SoundManager.getInstance().mServ.pauseMusic();*/
	}
	@Override
	protected void onNewIntent(Intent intent) {
		if (intent != null){

			setIntent(intent);
		}
	}
	@Override
	protected synchronized void onResume() {
		/*if(null!=SoundManager.getInstance().mServ)
			SoundManager.getInstance().mServ.resumeMusic();
		else */
		super.onResume();

		if(null!= MusicService.mPlayer && !MusicService.mPlayer.isPlaying()){
			MusicService.resumeMusic();
		}

		if(ResourcesManager.isAdsEnabled){

			//StartApp Ads - Start
			//commented
			//startAppAd.onResume();
			//StartApp Ads - End

			//Vungle Ads Start
			if(!(this instanceof LoginSign))
				vunglePub.onResume();
			//Vungle Ads End
		}
		mSimpleFacebook = SimpleFacebook.getInstance(this);
		uiHelper.onResume();
		bindService(new Intent(MainActivity.this, IMService.class), mConnection , Context.BIND_AUTO_CREATE);

		getFacebookRequestId(this.getIntent());

		if(ResourcesManager.isFaceBookRequestUnAddressed && ResourcesManager.roomHostedBroadCastSubscriptionSent && ResourcesManager.chocobackbuttonregion!=null){

			ResourcesManager.getInstance().joinRandomDailyChallengeRoom(ConnectionManager.mPathRoomNumberSuffix);
			/*onDeleteListener=new OnDeleteListener() {
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
					// TODO Auto-generated method stub
					super.onFail(reason);
					Log.e("onFail ", "APP onFail"+reason);
				}
			};
			mSimpleFacebook.deleteRequest(FacebookManager.requestId, onDeleteListener);*/
			FacebookManager.deleteRequest(FacebookManager.requestId);
		}

		/*IntentFilter i = new IntentFilter();
		i.addAction(IMService.TAKE_MESSAGE);

		registerReceiver(messageReceiver, i);*/

		//FriendController.setActiveFriend(friend.userName);		


		//Facebook
		//AppEventsLogger.activateApp(this);
	}



	/*public class  MessageReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) 
		{		
			Bundle extra = intent.getExtras();
			String username = extra.getString("user_name");			
			int roomNumber = extra.getInt("room_number");
			if (username != null)
			{
				ConnectionManager.mPathRoomNumberSuffix= roomNumber;

				ResourcesManager.getInstance().joinRandomDailyChallengeRoom(ConnectionManager.mPathRoomNumberSuffix);

				if (friend.userName.equals(username)) {
					//appendToMessageHistory(username, message);
					localstoragehandler.insert(username,imService.getUsername(), message);

				}
				else {

				}
			}			
		}

	};
	private MessageReceiver messageReceiver = new MessageReceiver();*/
	/*
	public  void appendToMessageHistory(String username, String message) {
		if (username != null && message != null) {
			messageHistoryText.append(username + ":\n");								
			messageHistoryText.append(message + "\n");
		}
	}
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	@Deprecated
	public boolean submitResults(final RoomProperties room) {
		/*ResourcesManager.getInstance().activity.doAsync(R.string.dialog_benchmark_submit_title, R.string.dialog_benchmark_submit_message, new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {*/
		try{
			TextureRegion myImageFromWeb;
			final String urlString=room.getAdsDetail().getImageUrl();

			ITexture mTexture=null;
			try {
				mTexture = new BitmapTexture(ResourcesManager.getInstance().getEngine().getTextureManager(), new IInputStreamOpener() {
					@Override
					public InputStream open() throws IOException {
						//TODO Async
						URL url = new URL(urlString);

						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setDoInput(true);
						connection.connect();
						InputStream input = connection.getInputStream();
						BufferedInputStream in = new BufferedInputStream(input);
						return in;
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null!=mTexture){
				mTexture.load();
				myImageFromWeb = TextureRegionFactory.extractFromTexture(mTexture);
				loadAdsPic(myImageFromWeb, room, ResourcesManager.getInstance().vbom);
			}

			return true;
		}catch(IllegalStateException e) {
			Debug.e(e);
		}

		try{
		}catch(IllegalStateException e) {
			Debug.e(e);
		}
		return false;

		/*}
		}, new Callback<Boolean>() {
			@Override
			public void onCallback(final Boolean pCallbackValue) {
				ToastHelper.makeCustomToastOnUI("Ads Loaded", Toast.LENGTH_SHORT);
			}
		}, new Callback<Exception>() {
			@Override
			public void onCallback(final Exception pException) {
				Debug.e(pException);
				ToastHelper.makeCustomToastOnUI("Exception While loading Ads", Toast.LENGTH_SHORT);

			}
		});*/
	}

	/**
	 * @param pVertexBufferObjectManager
	 * @param roomSelected
	 *
	 */
	private void loadAdsPic(final TextureRegion MyImageFromWeb,final RoomProperties roomSelected, final VertexBufferObjectManager pVertexBufferObjectManager) {
		if(null!=roomSelected && null!=MyImageFromWeb){
			//MyImageFromWeb.setTextureHeight(innerRectFirstChunk.getHeight());
			//MyImageFromWeb.setTextureWidth(innerRectFirstChunk.getWidth());
			//innerRectFirstChunk.setAlpha(0);

			final Sprite profileImage = new Sprite(roomSelected.getWidth()- ((ResourcesManager.roomJoinButtonOffset * ResourcesManager.resourceScaler)/2) ,roomSelected.getHeight()/2,MyImageFromWeb,pVertexBufferObjectManager){
				/* (non-Javadoc)
				 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
				 */
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
											 float pTouchAreaLocalX, float pTouchAreaLocalY) {
					Runnable run = new Runnable() {
						@Override
						public void run() {
							roomSelected.getAdsDetail().sendClick(ResourcesManager.getInstance().activity);
						}
					};
					ResourcesManager.getInstance().activity.runOnUiThread(run);

					return true;
				}
			};
			Runnable runAttacherForUpdate = new Runnable() {
				@Override
				public void run() {
					profileImage.setWidth((ResourcesManager.roomJoinButtonOffset * ResourcesManager.resourceScaler) - 10);
					profileImage.setHeight(roomSelected.getHeight() - 10);
					profileImage.setAnchorCenter(0.5f, 0.5f);
					//profileImage.setAlpha(0.9f);
					profileImage.registerEntityModifier(new AlphaModifier(1, 0.1f, 0.9f));
					profileImage.setZIndex(5);
					profileImage.setBlendFunctionDestination(GLES20.GL_ALPHA);
					if(SceneManager.getInstance().getCurrentSceneType().equals(SceneType.SCENE_ROOMS) )
						SceneManager.getInstance().getCurrentScene().registerTouchArea(profileImage);
					//profileImage.setScale(ResourcesManager.resourceScaler);

					roomSelected.attachChild(profileImage);

				}
			};
			ResourcesManager.getInstance().activity.runOnUpdateThread(runAttacherForUpdate);
			Runnable run = new Runnable() {
				@Override
				public void run() {
					roomSelected.getAdsDetail().sendImpression(ResourcesManager.getInstance().activity);
				}
			};
			ResourcesManager.getInstance().activity.runOnUiThread(run);
			roomSelected.sortChildren();
			//roomSelected.setAlpha(0.8f);
		}
	}

	/**
	 *
	 */
	public void destroyLeadBoltAd() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				leadBoltBanner.destroyAd();
			}
		});
	}

	/**
	 *
	 */
	public void loadLeadBoltAd() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(ResourcesManager.isAdsEnabled  && ResourcesManager.isAdsEnabledRefBased){
					leadBoltBanner.loadAd();
				}
			}
		});
	}

	public static String getCountryName(Context context, double latitude, double longitude) {
		Geocoder geocoder = new Geocoder(context, Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(latitude, longitude, 1);
			Address result;

			if (addresses != null && !addresses.isEmpty()) {
				return addresses.get(0).getCountryName();
			}
			return null;
		} catch (IOException ignored) {
			//do something
		}
		return null;
	}

}
