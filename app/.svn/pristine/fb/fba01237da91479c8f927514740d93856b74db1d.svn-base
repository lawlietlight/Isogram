/**
 * 
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.util.Log;
import android.widget.Toast;

import com.efficientsciences.cowsandbulls.wordwars.helper.ToastHelper;
import com.efficientsciences.cowsandbulls.wordwars.managers.ConnectionManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.EntityTagManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.NotificationManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.StorageManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.scenes.BaseScene;

/**
 * @author SATHISH
 *
 */
public class KeyboardChar extends Rectangle implements Comparable<KeyboardChar> {

	private String charCode;
	private Text text;
	private float keyboardPositionX;
	private float keyboardPositionY;
	private int position;

	public MoveModifier mod;

	public int getPosition() {
		return position;
	}


	/*
	 * returns true if the touch event is handled here
	 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		ResourcesManager.getInstance().removeTouchedSpritePSInstances();
		/*
		 * Check if move modifier is already called for this entity or not
		 */
		if(mod==null){

			/*Debug.e("Touch Me>>>>>> " + this.getCharCode());
			Debug.e("RootEntity " +this.getRootEntity().getTag());*/
			if(this.getTag()!=EntityTagManager.hostConnectButton){
				ResourcesManager.getInstance().host.getCharSet().add(this);
			}
			int numCharInWord= ResourcesManager.getInstance().host.getCharSet().size();
			BaseScene scene=(BaseScene)this.getRootEntity();


			//Tag = 3 for Host
			if(this.getTag()!=EntityTagManager.hostConnectButton){
				/*
				 * Check For Parent Keyboard Rectangle with Tag 1
				 * Move Characters if the characters are outside  rectangle i.e above the line within scene
				 * Or if the characters are within Rectangle and less than 13 characters i.e Allow only 12 characters to be entered
				 * 
				 */
				if(this.getParent().getTag()!=EntityTagManager.keyboardRectangle || (this.getParent().getTag()== EntityTagManager.keyboardRectangle && ((ResourcesManager.getInstance().hostImageclicked && numCharInWord<=ResourcesManager.getInstance().maxNoOfLetter)||(ResourcesManager.getInstance().guessImageclicked && numCharInWord<=ResourcesManager.getInstance().numberOfLettersHosted)))){

					return moveLettersToLineAndReturnTrue(numCharInWord, scene);
				}
				else{
					return maxLettersAlreadyEnteredValidation();

				}
			}
			else{
				/*if(null!=this.getCharCode() && this.getCharCode().equalsIgnoreCase("Host")){
					ResourcesManager.getInstance().hostButtonclicked=true;
					ResourcesManager.getInstance().guessButtonclicked
				}
				else if(null!=this.getCharCode() && this.getCharCode().equalsIgnoreCase("Guess")){
					ResourcesManager.getInstance().guessButtonclicked=true;
				}*/
				//if atleast 3 characters are entered send connect signal to websocket
				if( (ResourcesManager.getInstance().hostImageclicked && ResourcesManager.getInstance().minimumNumberOfLetters<=ResourcesManager.getInstance().host.getCharSet().size())
						||  (ResourcesManager.getInstance().guessImageclicked && ResourcesManager.getInstance().host.getCharSet().size()==ResourcesManager.getInstance().numberOfLettersHosted)){
					
					if(ResourcesManager.getInstance().hostImageclicked){
						ToastHelper.makeCustomToastOnUI( "HOSTING ...", Toast.LENGTH_LONG);

					}
					return postWordToSocketAndReturn(pSceneTouchEvent, scene);

				}
				else{
					return makeToastForMinimumCharacters();
				}
			}
		}
		else{
			//this.unregisterEntityModifier(mod);
			if(mod.isFinished()){
				mod=null;
			}
			return true;
		}

	}

	/**
	 * @return
	 */
	private boolean isWordAlreadyGuessed() {
		List<KeyboardChar> sortedList=new ArrayList<KeyboardChar>(ResourcesManager.getInstance().host.getCharSet());

		Collections.sort(sortedList);
		final StringBuffer sb=new StringBuffer();
		for (KeyboardChar hostChars : sortedList) {
			sb.append(hostChars.charCode);
		}

		if(null!=ResourcesManager.getInstance().pages && !ResourcesManager.getInstance().pages.hasPagechunk(new PageChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, sb.toString()))){
			return false;
		}
		else{
			Runnable run = new Runnable() {

				@Override
				public void run() {

					ToastHelper.makeCustomToast( "Word Already Guessed", Toast.LENGTH_SHORT);
				}
			};
			ResourcesManager.getInstance().activity.runOnUiThread(run); 
			return true;
		}

	}


	private boolean maxLettersAlreadyEnteredValidation() {
		final int numCharInWord= ResourcesManager.getInstance().host.getCharSet().size();
		ResourcesManager.getInstance().host.getCharSet().remove(this);

		Runnable run = new Runnable() {

			@Override
			public void run() {

				if(ResourcesManager.getInstance().showToast){
					if(ResourcesManager.getInstance().hostImageclicked){
						ToastHelper.makeCustomToast("Room Difficulty Does Not Allow More Than "+ ResourcesManager.getInstance().maxNoOfLetter + " Letters Be Hosted", Toast.LENGTH_SHORT);
					}
					else if(ResourcesManager.getInstance().guessImageclicked){

						ToastHelper.makeCustomToast( "Number Of Characters Guessed Cannot Be Greater Than That Of Word Hosted", Toast.LENGTH_SHORT);

					}
					ResourcesManager.getInstance().showToast=false;

					Timer timer = new Timer();
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							ResourcesManager.getInstance().showToast=true;								
						}
					}, 2500);
				}

			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run); 

		return true;
	}

	private boolean postWordToSocketAndReturn(TouchEvent pSceneTouchEvent, BaseScene scene) {
		List<KeyboardChar> sortedList=new ArrayList<KeyboardChar>(ResourcesManager.getInstance().host.getCharSet());
		Collections.sort(sortedList);
		final StringBuffer sb=new StringBuffer();
		for (KeyboardChar hostChars : sortedList) {
			sb.append(hostChars.charCode);
		}

		Log.d("hosted word:", sb.toString());
		//ThemeManager.getInstance().inputStream = new ByteArrayInputStream(sb.toString().getBytes());
		if(pSceneTouchEvent.isActionUp()){
			if(!isWordAlreadyGuessed()){
				final String word= sb.toString();
				
				if(null!=ConnectionManager.LastPublishedWord && ((!ConnectionManager.LastPublishedWord.equals(word)) || (ConnectionManager.LastPublishedWord.equals(word) && (ConnectionManager.LastConnectedTimestamp+2000)<System.currentTimeMillis()))){
					ConnectionManager.LastPublishedWord = word;
					ConnectionManager.LastConnectedTimestamp = System.currentTimeMillis();
					Runnable run5 = new Runnable() {

						@Override
						public void run() {
							try {
								/*ws2 = new WebSocketClientLocal();
			ws2.sendMessage((String)sb.toString());*/
								if(!ResourcesManager.getInstance().hosted){

									animateshrug();
									UserDO user = StorageManager.getInstance().getUser();
									if(null!=user){

										if(ResourcesManager.getInstance().hostImageclicked){
											user.setWordHosted(word);
											user.setWordGuessed(null);
											user.setClueWord(null);
											user.setSyncPulseFlag(true);
										}
										else if(ResourcesManager.getInstance().guessImageclicked){
											user.setWordGuessed(word);
											user.setWordHosted(null);
											user.setClueWord(null);
											user.setRevealedBits(null);
										}
									}


									if(ConnectionManager.getInstance().mConnection.isConnected()){

										ConnectionManager.getInstance().autobahnConnectorPubSub.publishUserWordToRoomPrefixOnly(user);
										//Log.i("Server Already Connected:", ConnectionManager.getInstance().mConnection.isConnected()+ " : No New connection needed");
									}
									else{
										ConnectionManager.getInstance().prepareConnection();
										ConnectionManager.getInstance().autobahnConnectorPubSub.publishUserWordToRoomPrefixOnly(user);
									}
									ResourcesManager.getInstance().hosted=true;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					};

					//Though we run on UI thread , AutoBahnSockket client internally spawns a new thread local n works in background else it will throw cannot use handlers error
					ResourcesManager.getInstance().activity.runOnUiThread(run5); 
				}
			}
		}
		else if(pSceneTouchEvent.isActionDown()){
			ResourcesManager.getInstance().hosted=false;
		}
		return true;
	}

	private boolean makeToastForMinimumCharacters() {
		Runnable run = new Runnable() {

			@Override
			public void run() {

				if(ResourcesManager.getInstance().showToast2){
					if(ResourcesManager.getInstance().hostImageclicked){
						ToastHelper.makeCustomToast( "Host an Isogram of at-least 3 letters minimum to have fun while playing", Toast.LENGTH_SHORT);
					}
					else if(ResourcesManager.getInstance().guessImageclicked){
						String textToast= "Could Not Post Guessed Word";
						if(ResourcesManager.getInstance().numberOfLettersHosted==0){
							textToast =  "Sorry, We Are Searching For The Word Hosted";
						}
						else{
							textToast= "Number Of Characters In Word Guessed IS Lesser Than Letters In Word Hosted "+ResourcesManager.getInstance().numberOfLettersHosted;
						}
						ToastHelper.makeCustomToast( textToast, Toast.LENGTH_SHORT);
					}

					ResourcesManager.getInstance().showToast2=false;
					ResourcesManager.getInstance().hosted=false;
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {

						@Override
						public void run() {
							ResourcesManager.getInstance().showToast2=true;								
						}
					}, 2500);
				}

			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run); 
		return true;

	}

	private boolean moveLettersToLineAndReturnTrue(int numCharInWord, BaseScene scene) {
		//final PhysicsHandler physicsHandler = new PhysicsHandler(this);
		//this.registerUpdateHandler(physicsHandler);

		Line line= (Line) scene.getChildByTag(EntityTagManager.lineInScene);



		/*if(numCharInWord==1){
		offset=50f;
	}
	else{
		numCharInWord= numCharInWord+1;
	}*/

		float fromX = 0;
		float fromY = 0;
		float toX = 0;
		float toY = 0;
		fromX = this.getX();
		fromY = this.getY();
		synchronized (ResourcesManager.getInstance().addedPositions) {

			this.detachSelf();

			if(this.getX()==this.getKeyboardPositionX() && this.getY()==this.getKeyboardPositionY()){
				this.detachSelf();

				numCharInWord=positionNotTaken(numCharInWord);
				this.position=numCharInWord;

				toX = line.getX1()+(ResourcesManager.offset*ResourcesManager.resourceScaler)+ResourcesManager.getInstance().letterWidth*numCharInWord;
				toY = line.getY1()+(ResourcesManager.getInstance().letterheight/2);
				/*Debug.e("Line X1---> "+ line.getX1());
			Debug.e("offset---> "+ offset);
			Debug.e("letterWidth---> "+ ResourcesManager.getInstance().letterWidth);
			Debug.e("numCharInWord---> "+ numCharInWord);
			Debug.e("toX ---> "+ toX);*/
				scene.attachChild(this);

			}
			else {
				toX = this.getKeyboardPositionX();
				toY = this.getKeyboardPositionY();
				ThemeManager.getInstance().setKeyBoardCharTheme(this);
				Rectangle keyboard = (Rectangle)scene.getChildByTag(EntityTagManager.keyboardRectangle);
				keyboard.attachChild(this);
				ResourcesManager.getInstance().showToast=true;
				if(ResourcesManager.getInstance().host.getCharSet().size()!=0){
					if(!ResourcesManager.getInstance().addedPositions.contains(this.position) && this.position!=0){
						boolean b=ResourcesManager.getInstance().addedPositions.add((Integer)this.position);

						Debug.e("Push Positions>>> "+ this.position + b);
					}
				}
				ResourcesManager.getInstance().host.getCharSet().remove(this);

			}
		}

		this.mod= new MoveModifier(0.5f, this.getX(), this.getY(), toX, toY );
		this.registerEntityModifier(mod);


		//physicsHandler.setVelocity(this.getX(), 100);
		return true;
	}

	private void animateshrug() {
		RotationModifier r1=new RotationModifier(.2f, 0, -30);
		r1.setAutoUnregisterWhenFinished(true);
		this.registerEntityModifier(r1);
		RotationModifier r2=new RotationModifier(.2f, r1.getToValue(), 80);
		r2.setAutoUnregisterWhenFinished(true);
		this.registerEntityModifier(r2);

		RotationModifier r3=new RotationModifier(.3f, r2.getToValue(), 0);
		r3.setAutoUnregisterWhenFinished(true);
		this.registerEntityModifier(r3);
	}



	private synchronized int positionNotTaken(int numCharInWord) {
		int finalPosition=1;

		if(!(ResourcesManager.getInstance().host.getCharSet().size()==0)){

			Debug.e("number checking" +numCharInWord);
			Debug.e("number checking" +ResourcesManager.getInstance().addedPositions.toString());

			if(!ResourcesManager.getInstance().addedPositions.isEmpty()){
				finalPosition = getLeastPossibleNumberNotTakenWithinLimits();
			}
			else{
				Debug.e("Does not contain in added position");
				finalPosition=numCharInWord;
			}

		}

		return finalPosition;
	}

	private synchronized int getLeastPossibleNumberNotTakenWithinLimits() {
		int position = 1;
		for (int i = 1; i < ResourcesManager.getInstance().maxNoOfLetter+1; i++) {
			synchronized(ResourcesManager.getInstance().addedPositions){
				if(ResourcesManager.getInstance().addedPositions.contains(i)){
					Debug.e("getLeastPossibleNumberNotTakenWithinLimits > Position Not Taken>> " + i);

					boolean b=ResourcesManager.getInstance().addedPositions.remove((Integer)i);
					//ResourcesManager.getInstance().host.getCharSet().remove(this);
					Debug.e("pop Positions>>> "+ i + b);
					return i;				
				}
			}

		}
		return position;

	}

	public float getKeyboardPositionX() {
		return keyboardPositionX;
	}


	/*public void setKeyboardPositionX(float keyboardPositionX) {
		this.keyboardPositionX = keyboardPositionX;
	}
	 */

	public float getKeyboardPositionY() {
		return keyboardPositionY;
	}


	/*public void setKeyboardPositionY(float keyboardPositionY) {
		this.keyboardPositionY = keyboardPositionY;
	}
	 */

	public Text getText() {
		return text;
	}


	public String getCharCode() {
		return charCode;
	}


	public KeyboardChar(float pX, float pY, float pWidth, float pHeight, String charCode,Text text,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);

		this.keyboardPositionX = pX;
		this.keyboardPositionY = pY;
		this.text=text;
		this.charCode=charCode;
		/*setUserData("KeyBoardLetters");*/
	}


	@Override
	public boolean equals(Object o) {
		if(o instanceof KeyboardChar){
			return this.getCharCode().equals(((KeyboardChar)o).getCharCode());
		}
		return super.equals(o);
	}

	@Override
	public int compareTo(KeyboardChar another) {
		if(this.position<another.position){
			return -1;
		}
		else if(this.position > another.position){
			return 1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return this.charCode+this.position;
	}

}
