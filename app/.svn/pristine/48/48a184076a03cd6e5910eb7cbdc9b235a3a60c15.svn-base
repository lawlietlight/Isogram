/**
 * RoomProperties.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.ArrayList;
import java.util.List;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.opengl.GLES20;
import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SoundManager.MusicPlayed;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;
import com.efficientsciences.cowsandbulls.wordwars.scenes.ScrollableRoomsScene;
import com.startapp.android.publish.nativead.NativeAdDetails;

/**
 * @author SATHISH
 *
 * State Machine
 * Room State Diagram
 * Hosting Flow Possible state 
 * STATE_HOSTING_AVAILABLE --> STATE_HOSTING_IN_PROGRESS ---> STATE_HOSTED
 * STATE_HOSTED -->  STATE_WAITING
 * 
 * Guessing Flow
 * STATE_WAITING --> STATE_GUESSING_DISABLED
 * ***********************************************************************************
 * getRoomSize().getValue()/3 is the minNumOfPlayers This Room should Hold
 * ***********************************************************************************
 *
 */
public class RoomProperties extends Rectangle {

	//For Hosting Flow
	public static final byte STATE_HOSTED = 1;
	public static final byte STATE_HOSTING_AVAILABLE = 4;

	public static Color colorJoinTouchDown=new Color(0.65f, 0.96f, 0.13f);
	public static Color colorRoomTouchDown=new Color(0.95f, 0.76f, 0.23f);
	public static Color colorRoomNormalUpdate=new Color(0.45f, 0.8f, 0.15f);
	public static Color colorGuessingDisabled=new Color(0.8f,0.1f,0.2f);
	public static Color colorTextInRoom = new Color(0.15f,0.15f,0.15f,0.9f);
	//public static Color colorTextInRoom = new Color(1f,0.99f,0.99f, 1f);
	
	//For Guessing Flow
	public static final byte STATE_WAITING = 2;
	public static final byte STATE_GUESSING_DISABLED = 8;
	public static final byte STATE_HOSTING_IN_PROGRESS = 16;

	private byte roomState = STATE_HOSTING_AVAILABLE;

	Text textMinNumberOfPlayers;
	private Text textNumberOfPlayersJoined;

	String attachedRoomDisabledString= "AVAILABLE";

	public byte getRoomState() {
		return roomState;
	}


	public void setRoomState(byte roomState) {
		this.roomState = roomState;
	}

	public boolean isRoomHosted() {
		return (0 != (roomState & (STATE_HOSTED | STATE_HOSTING_IN_PROGRESS)));
	}

	private boolean isRoomAvailableForHosting() {
		return !isRoomDisabledForHosting();
	}

	public boolean isRoomDisabledForHostingGuessing() {
		return (isRoomDisabledForGuessing() && ResourcesManager.getInstance().guessImageclicked) ||  (isRoomDisabledForHosting() && ResourcesManager.getInstance().hostImageclicked) ;
	}

	public boolean isRoomDisabledForGuessing() {
		return (0 != (roomState & (STATE_GUESSING_DISABLED | STATE_HOSTING_AVAILABLE | STATE_HOSTED | STATE_HOSTING_IN_PROGRESS)));
	}

	public boolean isRoomDisabledForHosting() {
		return (0 != (roomState & (STATE_HOSTED | STATE_WAITING | STATE_GUESSING_DISABLED | STATE_HOSTING_IN_PROGRESS)));
	}

	private boolean isRoomAvailableForGuessing() {
		return !isRoomDisabledForGuessing();
	}

	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pVertexBufferObjectManager
	 */
	@Deprecated
	public RoomProperties(float pX, float pY, float pWidth, float pHeight,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		joinRoom=new Rectangle(0, pHeight/2, 0, pHeight-20, pVertexBufferObjectManager);
		joinRoom.setAnchorCenter(0, 0.5f);
		joinRoom.setColor(0.65f,0.85f,0.75f);

		Text textJoin=new Text(0, joinRoom.getHeight()/2, ResourcesManager.getInstance().font, "JOIN", pVertexBufferObjectManager);
		textJoin.setAnchorCenter(0.5f, 0.5f);
		textJoin.setScale(0.6f);
		joinRoom.setWidth(textJoin.getWidth()+20);
		textJoin.setX(joinRoom.getWidth()/2);
		joinRoom.setX(pWidth-joinRoom.getWidth()-20);
		joinRoom.attachChild(textJoin);
		this.attachChild(joinRoom);
	}


	/**
	 * @param pX
	 * @param pY
	 * @param pWidth
	 * @param pHeight
	 * @param pVertexBufferObjectManager
	 */
	public RoomProperties(float pX, float pY, float pWidth, float pHeight, int index,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.setBlendFunctionDestination(GLES20.GL_ALPHA_BITS);
		this.index=index;
		setRoomSize();
		this.actionClicked = false;
		joinRoom=new Rectangle(0, pHeight/2, 0, pHeight-20, pVertexBufferObjectManager);
		joinRoom.setAnchorCenter(0, 0.5f);
		joinRoom.setColor(0.8f,0.8f,0.8f);
		joinRoom.setAlpha(0.7f);

		String buttonTextJoin="";
		if(ResourcesManager.getInstance().hostImageclicked){
			buttonTextJoin =  "HOST";
		}
		else{
			buttonTextJoin =  "JOIN";
		}
		Text textJoin=new Text(0, joinRoom.getHeight()/2, ResourcesManager.getInstance().font, buttonTextJoin, pVertexBufferObjectManager);
		textJoin.setAnchorCenter(0.5f, 0.5f);
		textJoin.setScale(0.4f * ResourcesManager.resourceScaler);
		textJoin.setColor(.3f,.3f,.3f);
		joinRoom.setWidth(textJoin.getWidth()*ResourcesManager.resourceScaler+20);
		textJoin.setX(joinRoom.getWidth()/2);

		Text textRoom=new Text(0, pHeight/2, ResourcesManager.getInstance().mBitmapFontForGame, "Room #", pVertexBufferObjectManager);
		textRoom.setAnchorCenter(0.5f, 0.5f);
		textRoom.setColor(0.95f,.9f,1f);
		textRoom.setX(100*ResourcesManager.resourceScaler);
		textRoom.setScale(ResourcesManager.resourceScaler);

		Text textRoomNumber=new Text(0, pHeight/2, ResourcesManager.getInstance().mBitmapFontOption, ""+this.index, pVertexBufferObjectManager);
		textRoomNumber.setAnchorCenter(0f, 0.4f);
		textRoomNumber.setColor(1f,1f,1f);
		textRoomNumber.setX(textRoom.getX()+textRoom.getWidthScaled());
		textRoomNumber.setScale(ResourcesManager.resourceScaler);

		Text textRoomNumberLargest=new Text(0, pHeight/2, ResourcesManager.getInstance().mBitmapFontOption, " "+ResourcesManager.getInstance().maxRooms, pVertexBufferObjectManager);
		textRoomNumberLargest.setX(textRoom.getX()+textRoom.getWidthScaled());
		textRoomNumberLargest.setScale(ResourcesManager.resourceScaler);

		Text textRoomSize=new Text(0, pHeight-5*ResourcesManager.resourceScaler, ResourcesManager.getInstance().mBitmapFontForGame, "Room Size: ", pVertexBufferObjectManager);
		textRoomSize.setScale(0.5f*ResourcesManager.resourceScaler);
		textRoomSize.setColor(colorTextInRoom);
		//textRoomSize.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		textRoomSize.setAnchorCenter(0f, 1f);
		textRoomSize.setX(textRoomNumberLargest.getX()+textRoomNumberLargest.getWidthScaled()+20*ResourcesManager.resourceScaler);

		Text textRoomSizeNumber=new Text(0, pHeight-5*ResourcesManager.resourceScaler, ResourcesManager.getInstance().mBitmapFontForGame, this.getRoomSize().getValue()+" ("+this.getRoomSize().toString()+") ", pVertexBufferObjectManager);
		textRoomSizeNumber.setAnchorCenter(0f, 1f);
		textRoomSizeNumber.setScale(0.5f*ResourcesManager.resourceScaler);
		textRoomSizeNumber.setColor(colorTextInRoom);
		//textRoomSizeNumber.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		//textRoomSize.setAnchorCenter(0f, 1f);
		textRoomSizeNumber.setX(textRoomSize.getX()+textRoomSize.getWidthScaled());

		textMinNumberOfPlayers=new Text(0,5*ResourcesManager.resourceScaler,ResourcesManager.getInstance().mBitmapFontForGame, "Min Players: ", pVertexBufferObjectManager);
		textMinNumberOfPlayers.setAnchorCenter(0f, 0f);
		textMinNumberOfPlayers.setScale(0.5f*ResourcesManager.resourceScaler);
		textMinNumberOfPlayers.setColor(colorTextInRoom);
		//textMinNumberOfPlayers.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		textMinNumberOfPlayers.setX(textRoomNumberLargest.getX()+textRoomNumberLargest.getWidthScaled()+20*ResourcesManager.resourceScaler);


		Text textMinNumberOfPlayersNumber=new Text(0, 5*ResourcesManager.resourceScaler,ResourcesManager.getInstance().mBitmapFontForGame,  ((this.getRoomSize().getValue()/3)+1)+"", pVertexBufferObjectManager);
		textMinNumberOfPlayersNumber.setAnchorCenter(0f, 0f);
		textMinNumberOfPlayersNumber.setScale(0.5f*ResourcesManager.resourceScaler);
		textMinNumberOfPlayersNumber.setColor(colorTextInRoom);
		//textMinNumberOfNumber.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		textMinNumberOfPlayersNumber.setX(textMinNumberOfPlayers.getX()+textMinNumberOfPlayers.getWidthScaled());

		Text textMaxLetters=new Text(0, pHeight-5*ResourcesManager.resourceScaler, ResourcesManager.getInstance().mBitmapFontForGame, "Max No. Of Letters: ", pVertexBufferObjectManager);
		textMaxLetters.setScale(0.5f*ResourcesManager.resourceScaler);
		textMaxLetters.setColor(colorTextInRoom);
		//textRoomSize.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		textMaxLetters.setAnchorCenter(0f, 1f);
		textMaxLetters.setX(textMinNumberOfPlayersNumber.getX()+textMinNumberOfPlayersNumber.getWidthScaled()+40*ResourcesManager.resourceScaler);

		Text textMaxLettersNumber=new Text(0, pHeight-5*ResourcesManager.resourceScaler, ResourcesManager.getInstance().mBitmapFontForGame, this.getMaxNoOfLetters()+"",3, pVertexBufferObjectManager);
		textMaxLettersNumber.setAnchorCenter(0f, 1f);
		textMaxLettersNumber.setScale(0.5f*ResourcesManager.resourceScaler);
		textMaxLettersNumber.setColor(colorTextInRoom);
		//textRoomSizeNumber.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		textMaxLettersNumber.setX(textMaxLetters.getX()+textMaxLetters.getWidthScaled());
		
		Text textNumberOfPlayers=new Text(0,5*ResourcesManager.resourceScaler,ResourcesManager.getInstance().mBitmapFontForGame, "No. Players Joined: ", pVertexBufferObjectManager);
		textNumberOfPlayers.setAnchorCenter(0f, 0f);
		textNumberOfPlayers.setScale(0.5f*ResourcesManager.resourceScaler);
		textNumberOfPlayers.setColor(colorTextInRoom);
		//textMinNumberOfPlayers.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		textNumberOfPlayers.setX(textMinNumberOfPlayersNumber.getX()+textMinNumberOfPlayersNumber.getWidthScaled()+40*ResourcesManager.resourceScaler);


		setTextNumberOfPlayersJoined(new Text(0, 5*ResourcesManager.resourceScaler,ResourcesManager.getInstance().mBitmapFontForGame,  getNoOfParticipants()+" ",5, pVertexBufferObjectManager));
		getTextNumberOfPlayersJoined().setAnchorCenter(0f, 0f);
		getTextNumberOfPlayersJoined().setScale(0.5f*ResourcesManager.resourceScaler);
		getTextNumberOfPlayersJoined().setColor(colorTextInRoom);
		//textMinNumberOfNumber.setHeight(((pHeight/4)-5*ResourcesManager.resourceScaler)*0.8f);
		getTextNumberOfPlayersJoined().setX(textNumberOfPlayers.getX()+textNumberOfPlayers.getWidthScaled());
		
		
		joinRoom.setX(pWidth-joinRoom.getWidth()-ResourcesManager.roomJoinButtonOffset*ResourcesManager.resourceScaler);


		joinRoom.attachChild(textJoin);
		this.attachChild(textRoom);
		this.attachChild(textRoomNumber);
		this.attachChild(joinRoom);
		this.attachChild(textRoomSize);
		this.attachChild(textRoomSizeNumber);
		this.attachChild(textMinNumberOfPlayers);
		this.attachChild(textMinNumberOfPlayersNumber);
		this.attachChild(textMaxLetters);
		this.attachChild(textMaxLettersNumber);
		this.attachChild(textNumberOfPlayers);
		this.attachChild(getTextNumberOfPlayersJoined());
		this.setCullingEnabled(true);
	}

	/**
	 * @param roomProperties
	 * @param scrollIndex 
	 */
	public void setRoomSize() {
		//themesKey=StorageManager.getInstance().getThemePreferences().getThemeSelection();

		if(index<20){
			this.roomSize = RoomSize.S;
			this.setMaxNoOfLetters(5);
		}
		else if(index<40){
			this.roomSize = RoomSize.M;
			this.setMaxNoOfLetters(9);
		}
		else if(index<60){
			this.roomSize = RoomSize.L;
			this.setMaxNoOfLetters(12);
		}
		else if(index<80){
			this.roomSize = RoomSize.XL;
			this.setMaxNoOfLetters(15);
		}
		else if(index<=100){
			this.roomSize = RoomSize.XXL;
			this.setMaxNoOfLetters(18);
		}
		else if(index<ResourcesManager.getInstance().maxRooms){
			this.roomSize = RoomSize.XXXL;
		}

		/*if(this.themesKey.equals(THEMES.BLUEMAGIC.toString())){
			roomProperties.setAlpha(.4f);
		}*/
		else{
			this.roomSize = RoomSize.XXXL;
		}
	}



	private int index;
	public enum RoomSize{
		S(3),
		M(6),
		L(9),
		XL(12),
		XXL(15),
		XXXL(18);

		private final int value;

		private RoomSize(final int newValue) {
			value = newValue;
		}

		public int getValue() { return value; }
	}
	private RoomSize roomSize;
	private volatile int noOfParticipants;
	private int difficultyLevel;
	private String roomHostedBy;
	private List<UserDO> participants;
	private String hostedRoomURI;
	private Rectangle joinRoom;
	private int maxNoOfLetters;


	public int getMaxNoOfLetters() {
		return maxNoOfLetters;
	}


	public void setMaxNoOfLetters(int maxNoOfLetters) {
		this.maxNoOfLetters = maxNoOfLetters;
	}


	public RoomSize getRoomSize() {
		return roomSize;
	}


	public void setRoomSize(RoomSize roomSize) {
		this.roomSize = roomSize;
	}


	public String getHostedRoomURI() {
		return hostedRoomURI;
	}
	public void setHostedRoomURI(String hostedRoomURI) {
		this.hostedRoomURI = hostedRoomURI;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	

	public int getNoOfParticipants() {
		return noOfParticipants;
	}
	public void setNoOfParticipants(int noOfParticipants) {
		this.noOfParticipants = noOfParticipants;
	}
	public int getDifficultyLevel() {
		return difficultyLevel;
	}
	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}
	public String getRoomHostedBy() {
		return roomHostedBy;
	}
	public void setRoomHostedBy(String roomHostedBy) {
		this.roomHostedBy = roomHostedBy;
	}
	public List<UserDO> getParticipants() {
		return participants;
	}
	public void setParticipants(List<UserDO> participants) {
		this.participants = participants;
	}

	/**
	 * @return the textNumberOfPlayersJoined
	 */
	public Text getTextNumberOfPlayersJoined() {
		return textNumberOfPlayersJoined;
	}


	/**
	 * @param textNumberOfPlayersJoined the textNumberOfPlayersJoined to set
	 */
	public void setTextNumberOfPlayersJoined(Text textNumberOfPlayersJoined) {
		this.textNumberOfPlayersJoined = textNumberOfPlayersJoined;
	}



	public boolean actionClicked;
	private NativeAdDetails adsDetail;
	private boolean isRoomDisabledTextAttached;



	/* (non-Javadoc)
	 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if(joinRoom.contains(pSceneTouchEvent.getX(),pSceneTouchEvent.getY())){
			if(pSceneTouchEvent.isActionDown() || (pSceneTouchEvent.isActionMove() && !actionClicked)){
				actionClicked=true;
				this.setColor(colorJoinTouchDown);
				this.setAlpha(1f);
			}
			else if((pSceneTouchEvent.isActionUp() && actionClicked) || (pSceneTouchEvent.isActionMove() && actionClicked)){

				StorageManager.getInstance().getUser().setHostedGuessedRoomIndex(index);
				ConnectionManager.mPathRoomNumberSuffix=index;
				ResourcesManager.setTutorialModeEnabled(ConnectionManager.mPathRoomNumberSuffix);

				ResourcesManager.getInstance().maxNoOfLetter=getMaxNoOfLetters();

				//Host
				if(ResourcesManager.getInstance().hostImageclicked){
					if(isRoomAvailableForHosting()){
						if(SceneManager.getInstance().getCurrentSceneType().equals(SceneType.SCENE_ROOMS)){
							((ScrollableRoomsScene)SceneManager.getInstance().getCurrentScene()).disposeScene();
						}
						StorageManager.getInstance().getUser().setHoster(true);

						Runnable run5 = new Runnable() {

							@Override
							public void run() {
								try {
									RoomStream hostRoomDetails= new RoomStream();
									hostRoomDetails.setRoomHostedBy(StorageManager.getInstance().getUser().getUserName());
									hostRoomDetails.setIndex(index);
									hostRoomDetails.setRoomState(STATE_HOSTING_IN_PROGRESS);
									//hostRoomDetails.setParticipants(null);
									UserDO participant = StorageManager.getInstance().getUser();
									if(null==hostRoomDetails.getParticipants()){
										hostRoomDetails.setParticipants(new ArrayList<UserDO>());
									}

									if(!hostRoomDetails.getParticipants().contains(participant)){
										hostRoomDetails.getParticipants().add(participant);
									}
									if(ConnectionManager.getInstance().mConnection.isConnected()){
										actionClicked=false;
										//SoundManager.getInstance().createWhipSound();

										ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(hostRoomDetails);
										Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
									}
									else{
										actionClicked=false;
										ConnectionManager.getInstance().prepareConnection();
										ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(hostRoomDetails);
									}
								} 
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
						ResourcesManager.getInstance().activity.runOnUiThread(run5); 

						if(THEMES.BLUEMAGIC.toString().equals(ThemeManager.getInstance().getThemePreferences().getThemeSelection())){
							SoundManager.changeMusic(MusicPlayed.BEEMUSIC);
						}
						else{
							SoundManager.changeMusic(MusicPlayed.FASTMARCH);
						}
						this.noOfParticipants=1;
						//
						if(null!=SceneManager.getInstance().scrollableRoomScene){
							SceneManager.getInstance().scrollableRoomScene.detachChildren();
						}
						SceneManager.getInstance().scrollableRoomScene = null;
						SceneManager.getInstance().createHostScene();
						ResourcesManager.getInstance().cancelAsyncAdLoadTasks();
						unloadAds();
						return true;
					}
					else{
						actionClicked=false;
						makeToastRoomTaken();
						return true;
					}

				} //Guess
				else 
				{
					if(isRoomAvailableForGuessing()){
						if(SceneManager.getInstance().getCurrentSceneType().equals(SceneType.SCENE_ROOMS)){
							((ScrollableRoomsScene)SceneManager.getInstance().getCurrentScene()).disposeScene();
						}
						StorageManager.getInstance().getUser().setHoster(false);
						Runnable run5 = new Runnable() {

							@Override
							public void run() {
								try {
									RoomStream hostRoomDetails= new RoomStream();
									UserDO participant = StorageManager.getInstance().getUser();
									if(null==hostRoomDetails.getParticipants()){
										hostRoomDetails.setParticipants(new ArrayList<UserDO>());
									}

									if(!hostRoomDetails.getParticipants().contains(participant)){
										hostRoomDetails.getParticipants().add(participant);
									}

									hostRoomDetails.setIndex(index);
									hostRoomDetails.setRoomState(STATE_WAITING);

									if(ConnectionManager.getInstance().mConnection.isConnected()){
										actionClicked=false;
										//SoundManager.getInstance().createWhipSound();

										ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(hostRoomDetails);
										Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
									}
									else{
										actionClicked=false;
										ConnectionManager.getInstance().prepareConnection();
										ConnectionManager.getInstance().autobahnConnectorPubSub.sendRoomHostedBroadcastPublish(hostRoomDetails);
									}
								} 
								catch (Exception e) {
									e.printStackTrace();
								}
							}
						};
						//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
						ResourcesManager.getInstance().activity.runOnUiThread(run5); 

						
						if(THEMES.BLUEMAGIC.toString().equals(ThemeManager.getInstance().getThemePreferences().getThemeSelection())){
							SoundManager.changeMusic(MusicPlayed.BEEMUSIC);
						}
						else{
							SoundManager.changeMusic(MusicPlayed.FASTMARCH);
						}
						this.noOfParticipants=0;
						
						if(SceneManager.getInstance().scrollableRoomScene.hasParent()){
							SceneManager.getInstance().scrollableRoomScene.detachSelf();
						}
						SceneManager.getInstance().scrollableRoomScene = null;
						SceneManager.getInstance().createWaitingScene();
						ResourcesManager.getInstance().cancelAsyncAdLoadTasks();
						unloadAds();
						//SceneManager.getInstance().createGuessScene();
						return true;
					}
					else{
						actionClicked=false;
						makeToastRoomTaken();
						return true;
					}
				}
				/*else{
					actionClicked=false;
					Log.i("Unknown state",  "Room state not known");
					return true;
				}*/
			}
		}
		else if(pSceneTouchEvent.isActionDown() || pSceneTouchEvent.isActionMove()){
			actionClicked = true;
			//this.setAlpha(0.1f);
			
			this.setColor(colorRoomTouchDown);
		}
		else{
			actionClicked = false;
			//ThemeManager.resetRoomAlpha(this);
			ThemeManager.resetRoomHighlight(this);
		}
		return false;
	}


	/**
	 * 
	 */
	public void unloadAds() {
		Sprite sp = (Sprite) this.getChildByTag(EntityTagManager.ADSIMAGE);
		if(null!=sp){
			ITextureRegion unloader = sp.getTextureRegion();
			if(null!=unloader && null!=unloader.getTexture()){
				unloader.getTexture().unload();
			}
		}
	}


	private boolean makeToastRoomTaken() {
		Runnable run = new Runnable() {

			@Override
			public void run() {

				if(ResourcesManager.getInstance().hostImageclicked){
					ToastHelper.makeCustomToast( "OOPS, Room Already Taken, Please Try Another Room For Hosting ", Toast.LENGTH_LONG);
				}
				else if(ResourcesManager.getInstance().guessImageclicked){
					ToastHelper.makeCustomToast(attachedRoomDisabledString+", Please Select A Room Available For Guessing ", Toast.LENGTH_SHORT);
				}


			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run); 
		return true;
	}

	/* (non-Javadoc)
	 * @see org.andengine.entity.Entity#onManagedUpdate(float)
	 */
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		if(!actionClicked){
			if(!this.isRoomDisabledTextAttached){
				ThemeManager.setRoomHostedTheme(this);
				isRoomDisabledTextAttached=true;
			}
			else
				if(this.isRoomDisabledForGuessing() && ResourcesManager.getInstance().guessImageclicked){
					ThemeManager.setRoomGuessingDisabledTheme(colorGuessingDisabled,this);
				}
				else if(this.isRoomDisabledForHosting() && ResourcesManager.getInstance().hostImageclicked){
					ThemeManager.setRoomGuessingDisabledTheme(colorGuessingDisabled, this);
					if(!attachedRoomDisabledString.equals("Room Already Hosted    ")){
						attachedRoomDisabledString = "Room Taken      ";
					}
				}
				else{
					//ThemeManager.getInstance().resetRoomTheme(this, index);
					
					ThemeManager.setRoomColor(colorRoomNormalUpdate,this);
					//this.setColor(0.99f, 0.76f, 0.23f);
					//this.setAlpha(0.9f);
					if(ResourcesManager.getInstance().hostImageclicked)
					attachedRoomDisabledString = "Available";
					else{
						attachedRoomDisabledString = "Live";

					}
				}
		}

		//Disable accidental click of join while scrolling
		if(ResourcesManager.roomsScrolled){
			actionClicked=false;
			//ThemeManager.getInstance().resetRoomTheme(this, index);
		}

		super.onManagedUpdate(pSecondsElapsed);
	}



	/**
	 * 
	 */
	public void addNumOfParticipants() {
		this.noOfParticipants++;
	}


	public NativeAdDetails getAdsDetail() {
		return adsDetail;
	}


	public void setAdsDetail(NativeAdDetails adsDetail) {
		this.adsDetail = adsDetail;
	}


	/**
	 * @param roomProperties 
	 * 
	 */
	public void attachRoomHostedText(RoomProperties roomProperties) {
		if(roomProperties.isRoomDisabledForGuessing()  && ResourcesManager.getInstance().guessImageclicked){
			if(isRoomAvailableForHosting()){
				attachedRoomDisabledString = "Room Not Yet Hosted";
			}
			else{
				attachedRoomDisabledString = "Game In Progress";
			}
		}
		else if(roomProperties.isRoomDisabledForHosting()  && ResourcesManager.getInstance().hostImageclicked){
			attachedRoomDisabledString = "Room Already Hosted    ";
		}
		Text textRoomTaken=new Text(getWidthScaled()/2, getHeightScaled()/2, ResourcesManager.getInstance().font, attachedRoomDisabledString, ResourcesManager.getInstance().vbom){
			protected void onManagedUpdate(float pSecondsElapsed) {
				this.setText(attachedRoomDisabledString);
				this.setX(textMinNumberOfPlayers.getX());
			}
		};
		textRoomTaken.setAnchorCenter(0f, 0.5f);
		textRoomTaken.setScale(0.3f * ResourcesManager.resourceScaler);
		textRoomTaken.setColor(1f,0.2f,0.2f,0.9f);
		//textRoomTaken.setColor(colorTextInRoom);
		textRoomTaken.setTag(EntityTagManager.textRoomTaken);
		this.attachChild(textRoomTaken);
	}


}
