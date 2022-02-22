package com.efficientsciences.cowsandbulls.wordwars.domain;

import org.andengine.entity.IEntity;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import wei.mark.standout.StandOutWindow;
import android.opengl.GLES20;
import android.os.AsyncTask;

import com.efficientsciences.cowsandbulls.wordwars.chat.WidgetsWindow;
import com.efficientsciences.cowsandbulls.wordwars.managers.ChatManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.SceneManager.SceneType;
import com.efficientsciences.cowsandbulls.wordwars.scenes.SlideUsersChildScene;
import com.efficientsciences.cowsandbulls.wordwars.textureMap.Chat;

public class UserChunk extends Rectangle implements Comparable<UserChunk>{


	private int index;
	private String username;
	private int score;
	private Text dispNameText;
	private Level level;
	private Rectangle[] innerRects=new Rectangle[1];
	private int columnIndex;
	private float offset;
	private boolean isHost;
	private short HEIGHT=128;
	float textHeight = 10;
	public Sprite redMask;
	public TextOptions dispNameTextOptions;
	public UserChunk(float pX, float pY, float pWidth, float pHeight,
			final VertexBufferObjectManager pVertexBufferObjectManager, int index , UserDO user) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.setAlpha(0);
		this.index=index;
		this.username=user.getUserName();
		this.isHost = user.isHoster() && user.getUserName().equals(ResourcesManager.getInstance().roomHostedBy);
		HEIGHT=(short)(ResourcesManager.getInstance().windowDimensions.y/8);
		int initialOffset=15;

		String hostPrefix = isHost? " H: ":" ";
		dispNameTextOptions=new TextOptions(AutoWrap.LETTERS,(HEIGHT+60)*ResourcesManager.resourceScaler,HorizontalAlign.LEFT);
		String nameToDisplay = user.getDisplayName()!=null?user.getDisplayName():user.getUserName()+"";
		if(null!=nameToDisplay){
			int nameLength=nameToDisplay.length();
			nameToDisplay = nameToDisplay.replaceAll("[^a-zA-Z0-9 ]", "");
			if(nameLength>6){
				if(-1!=nameToDisplay.indexOf("WordPlayer")){
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

		this.dispNameText=new Text(0, 0, ResourcesManager.getInstance().mBitmapFontForGame, hostPrefix+nameToDisplay,dispNameTextOptions , pVertexBufferObjectManager);
		this.dispNameText.setScale(0.45f*ResourcesManager.resourceScaler);
		this.dispNameText.setAnchorCenter(0,1);



		this.innerRects[0]=new Rectangle(0, 0, HEIGHT, HEIGHT, pVertexBufferObjectManager);
		this.innerRects[0].setUserData(user.getUserName());

		Runnable run = new Runnable() {
			@Override
			public void run() {
				AsyncTask<Rectangle, Void, TextureRegion> task=new FetchImageFromServerTask();
				task.execute(innerRects);
			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run); 


		this.innerRects[0].setAnchorCenter(0, 0);

		this.setHeight(this.innerRects[0].getHeightScaled());
		this.setWidth(this.innerRects[0].getWidthScaled());

		/*if(0==ResourcesManager.getInstance().userChunkWidth || ResourcesManager.getInstance().userChunkWidth<this.getWidth()){
			ResourcesManager.getInstance().userChunkWidth = this.getWidth();
		}
		ResourcesManager.getInstance().userChunkHeight= this.getHeight();*/

		this.dispNameText.setPosition(0,0);

		//this.dispNameText.setZIndex(2);
		if(ResourcesManager.resourceScaler<=1){
			offset=5*ResourcesManager.resourceScaler;
		}
		else{
			offset=0;
		}

		/*redMask=new Sprite((this.innerRects[0].getWidthScaled()/2)-1*ResourcesManager.resourceScaler, this.innerRects[0].getHeightScaled()/2, ResourcesManager.getInstance().redWaitingSceneUserChunk_region, ResourcesManager.getInstance().vbom);
		redMask.setWidth(this.innerRects[0].getWidthScaled()+2*ResourcesManager.resourceScaler);
		redMask.setHeight(this.innerRects[0].getHeightScaled()+3*ResourcesManager.resourceScaler);*/
		redMask=new Sprite((this.innerRects[0].getWidthScaled()/2), this.innerRects[0].getHeightScaled()/2, ResourcesManager.getInstance().redWaitingSceneUserChunk_region, ResourcesManager.getInstance().vbom);
		redMask.setWidth(this.innerRects[0].getWidthScaled()+offset);
		redMask.setHeight(this.innerRects[0].getHeightScaled()+offset);
		redMask.setAnchorCenter(0.5f, 0.5f);
		redMask.attachChild(this.dispNameText);
		redMask.setZIndex(2);
		redMask.setBlendFunctionSource(GLES20.GL_FRONT);
		redMask.setBlendFunctionDestination(GLES20.GL_BLEND_COLOR);
		redMask.setScale(1.05f);
		this.innerRects[0].attachChild(redMask);
		this.sortChildren();
		this.attachChild(this.innerRects[0]);
		textHeight =dispNameText.getHeight() * dispNameText.getScaleY();
		//this.setAnchorCenter(0.5f, 0.5f);
	}




	public String getUsername() {
		return username;
	}




	public void setUsername(String username) {
		this.username = username;
	}




	public int getScore() {
		return score;
	}




	public void setScore(int score) {
		this.score = score;
	}




	public Level getLevel() {
		return level;
	}




	public void setLevel(Level level) {
		this.level = level;
	}




	public Text getDispNameText() {
		return dispNameText;
	}



	public void setDispNameText(Text dispNameText) {
		this.dispNameText = dispNameText;
	}



	public int getColumnIndex() {
		return columnIndex;
	}



	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}



	public int getIndex() {
		return index;
	}



	public void setIndex(int index) {
		this.index = index;
	}



	@Override
	public boolean equals(Object o) {
		if(o instanceof UserChunk){
			return this.getUsername().equalsIgnoreCase(((UserChunk)o).getUsername());
		}
		return false;
	};

	@Override
	public int compareTo(UserChunk another) {
		if(this.getUsername().equals(((UserChunk)another).getUsername())){
			return 0;
		}
		else{
			if(this.index<another.index){
				return -1;
			}
			else if(this.index > another.index){
				return 1;
			}
			else if(0!=this.index && this.index == another.index){
				return 0;
			}
		}
		return 1;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		//this.setAlpha(0);
		if(!(SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_WAITING)){
			//this.setX(2*ResourcesManager.resourceScaler);
			if(SlideUsersChildScene.mCurrentY!=0 && this.getY() < -SlideUsersChildScene.mCurrentY+ResourcesManager.getInstance().userChunkHeight + textHeight){
				this.setAlpha(0);
				if(null!=innerRects && null!=innerRects[0]){
					innerRects[0].setAlpha(0);
					dispNameText.setAlpha(0);
					for (int i = 0; i < innerRects[0].getChildCount(); i++) {
						IEntity entity = innerRects[0].getChildByIndex(i);
						entity.setAlpha(0);
					}
				}
			}
			else{
				//this.setAlpha(1);
				if(null!=innerRects && null!=innerRects[0]){
					innerRects[0].setAlpha(1);
					dispNameText.setAlpha(1);
					for (int i = 0; i < innerRects[0].getChildCount(); i++) {
						IEntity entity = innerRects[0].getChildByIndex(i);
						entity.setAlpha(1f);
					}
				}
			}
		}
		super.onManagedUpdate(pSecondsElapsed);
	}


	/* (non-Javadoc)
	 * @see org.andengine.entity.Entity#onAreaTouched(org.andengine.input.touch.TouchEvent, float, float)
	 */
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if(pSceneTouchEvent.isActionDown()){
			StandOutWindow.show(ResourcesManager.getInstance().activity, WidgetsWindow.class, 0);
			if(null!=ResourcesManager.getInstance().chat){
				ResourcesManager.getInstance().chat.setCurrentTileIndex(Chat.CHAT_INVZ_ID);
				ResourcesManager.getInstance().chat.setAlpha(0.5f);
			}
			ChatManager.chatState=1;
			return true;
		}
		return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);

	}

}

