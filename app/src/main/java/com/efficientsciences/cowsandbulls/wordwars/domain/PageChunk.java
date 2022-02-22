package com.efficientsciences.cowsandbulls.wordwars.domain;

import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;

import android.os.AsyncTask;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager;
import com.efficientsciences.cowsandbulls.wordwars.managers.ThemeManager.THEMES;

public class PageChunk extends Rectangle implements Comparable<PageChunk>{


	private int index;
	private String guessedBy;
	private String wordGuessed;
	private Text dispNameText;
	private Text wordText;
	private Text cowsBullsText;
	private Rectangle[] innerRects=new Rectangle[3];
	private int cows;
	private int bulls;
	private int columnIndex;
	private float offset;

	public PageChunk(float pX, float pY, float pWidth, float pHeight,
			final VertexBufferObjectManager pVertexBufferObjectManager, String wordGuessed){
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.wordGuessed=wordGuessed;
	}

	/**
	 * @param nameLength
	 * @return
	 */
	private String appendSpace(int appendLength) {
		StringBuffer space=new StringBuffer(appendLength);
		for (int i = 0; i < appendLength; i++) {
			space.append(" ");
		}
		return space.toString();
	}

	public PageChunk(float pX, float pY, float pWidth, float pHeight,
			final VertexBufferObjectManager pVertexBufferObjectManager, int index, String guessedBy, String wordGuessed, int i, int j, String userName) {
		super(pX, pY, pWidth, pHeight, pVertexBufferObjectManager);
		this.index=index;
		if(null!=guessedBy){
			int nameLength=guessedBy.length();
			if(nameLength>6){
				guessedBy=guessedBy.substring(0,7);
			}
			else {
				guessedBy=guessedBy+appendSpace(7-nameLength);
			}
		}
		this.guessedBy=guessedBy;
		this.wordGuessed=wordGuessed;
		this.cows=i;
		this.bulls=j;

		int initialOffset=15;
		
		
		this.dispNameText=new Text(0, 0, ResourcesManager.getInstance().mBitmapFontForGame, this.guessedBy+":", new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		this.dispNameText.setScale(0.5f*ResourcesManager.resourceScaler);
		this.dispNameText.setAnchorCenter(0.5f,0);
		

		this.wordText=new Text( 0 , 0, ResourcesManager.getInstance().mBitmapFontForGame, this.wordGuessed, new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		this.wordText.setScale(0.8f*ResourcesManager.resourceScaler);
		this.wordText.setAnchorCenter(0.5f,0.5f);
		
		this.cowsBullsText=new Text(0 , 0, ResourcesManager.getInstance().mBitmapFontOption, "C"+this.cows+":"+"B"+this.bulls, new TextOptions(HorizontalAlign.CENTER), pVertexBufferObjectManager);
		this.cowsBullsText.setAnchorCenter(0.5f,0.5f);
		this.cowsBullsText.setScale(ResourcesManager.resourceScaler);
		ThemeManager.getInstance().setPageChunkTheme(this);
		
		this.setHeight(this.wordText.getHeight()*ResourcesManager.resourceScaler+10);
		
		this.innerRects[0]=new Rectangle(0, 0, (dispNameText.getWidthScaled())+5, getHeight(), pVertexBufferObjectManager);
		this.innerRects[0].setUserData(userName);
		
		Runnable run = new Runnable() {
			@Override
			public void run() {
				AsyncTask<Rectangle, Void, TextureRegion> task=new FetchImageFromServerTask();
				task.execute(innerRects);
			}
		};
		ResourcesManager.getInstance().activity.runOnUiThread(run); 
		
		
		this.innerRects[0].setColor(1,1,1,0.1f);
		this.innerRects[0].setAnchorCenter(0, 0);
		
		this.innerRects[1]=new Rectangle(this.innerRects[0].getX() + this.innerRects[0].getWidth(), 0, (wordText.getWidthScaled()) + 5, getHeight(), pVertexBufferObjectManager);
		if(ThemeManager.getInstance().getThemePreferences().getThemeSelection().equals(THEMES.BLUEMAGIC.toString()) && ResourcesManager.getInstance().hostImageclicked){
			this.innerRects[1].setColor(245/255f,250/255f,235/255f,0.5f);
		}
		else{
			this.innerRects[1].setColor(1,1,0.5f,0.5f);	
		}

		this.innerRects[1].setAnchorCenter(0, 0);
		this.innerRects[2]=new Rectangle(this.innerRects[1].getX() + this.innerRects[1].getWidth(), 0, cowsBullsText.getWidthScaled()+ 5, getHeight(), pVertexBufferObjectManager);
		
		if(ThemeManager.getInstance().getThemePreferences().getThemeSelection().equals(THEMES.BLUEMAGIC.toString()) && ResourcesManager.getInstance().hostImageclicked){
			this.innerRects[2].setColor(255/255f,10/255f,50/255f,0.5f);
		}
		else{
			this.innerRects[2].setColor(0.7f,1,0.5f,0.5f);
		}
		this.innerRects[2].setAnchorCenter(0, 0);

		
		
		this.setWidth(this.innerRects[0].getWidth() + this.innerRects[1].getWidth()  + this.innerRects[2].getWidth());
		
		if(0==ResourcesManager.getInstance().pageChunkWidth || ResourcesManager.getInstance().pageChunkWidth<this.getWidth()){
			ResourcesManager.getInstance().pageChunkWidth = this.getWidth();
		}
		ResourcesManager.getInstance().pageChunkHeight= this.getHeight();
		offset= 0.4f*ResourcesManager.getInstance().pageChunkHeight;

		this.dispNameText.setPosition(this.innerRects[0].getWidth()/2,0);
		this.wordText.setPosition(this.innerRects[1].getWidth()/2,this.getHeight()/2);
		this.cowsBullsText.setPosition(this.innerRects[2].getWidth()/2,(this.getHeight()/2)+10);
		
		this.dispNameText.setZIndex(2);
		this.innerRects[0].attachChild(this.dispNameText);
		this.innerRects[1].attachChild(this.wordText);
		this.innerRects[2].attachChild(this.cowsBullsText);
		
		this.attachChild(this.innerRects[0]);
		this.attachChild(this.innerRects[1]);
		this.attachChild(this.innerRects[2]);
	}
	
	public PageChunk(float pX, float pY, float pWidth, float pHeight,
			final VertexBufferObjectManager pVertexBufferObjectManager, int index, UserDO user){
		this(pX, pY,  pWidth,  pHeight,
				  pVertexBufferObjectManager,  index, user.getDisplayName()!=null?user.getDisplayName():user.getUserName(), 
						  (user.getWordGuessed()!=null && !user.getWordGuessed().isEmpty())? user.getWordGuessed(): user.getWordHosted() ,
								  user.getCows(), user.getBulls(), user.getUserName());
		
	}



	public Text getCowsBullsText() {
		return cowsBullsText;
	}




	public void setCowsBullsText(Text cowsBullsText) {
		this.cowsBullsText = cowsBullsText;
	}




	public Text getDispNameText() {
		return dispNameText;
	}



	public void setDispNameText(Text dispNameText) {
		this.dispNameText = dispNameText;
	}



	public int getCows() {
		return cows;
	}



	public int getBulls() {
		return bulls;
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



	public String getGuessedBy() {
		return guessedBy;
	}



	public void setGuessedBy(String guessedBy) {
		this.guessedBy = guessedBy;
	}



	public String getWordGuessed() {
		return wordGuessed;
	}



	public void setWordGuessed(String wordGuessed) {
		this.wordGuessed = wordGuessed;
	}

	

	public Text getWordText() {
		return wordText;
	}



	public void setWordText(Text wordText) {
		this.wordText = wordText;
	}



	@Override
	public boolean equals(Object o) {
		if(o instanceof PageChunk){
			return this.getWordGuessed().equalsIgnoreCase(((PageChunk)o).getWordGuessed());
		}
		return false;
	};

	@Override
	public int compareTo(PageChunk another) {
		if(this.getWordGuessed().equals(((PageChunk)another).getWordGuessed())){
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
		if(null!=ResourcesManager.getInstance().keyBoard && !(ResourcesManager.getInstance().keyBoard.getY()<0) && this.getScaleX()!=0.8f){
			this.setScale(0.8f);
			//this.setY(getY()+offset);
			this.setOffsetCenterY(-0.5f);
		}
		else if(null!=ResourcesManager.getInstance().keyBoard && (ResourcesManager.getInstance().keyBoard.getY()<0) && this.getScaleX()==0.8f){
			this.setScale(1f);
			//this.setY(getY()-offset);
			this.setOffsetCenterY(0);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}



}

