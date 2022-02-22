/**
 * Copyright (C) 2014 Sathish Kumar <bsathish.in@gmail.com>
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;

public class PageChunkFactory {
	private static final PageChunkFactory INSTANCE = new PageChunkFactory();

	private PageChunkFactory() {	
		pool  = null;
	}

	List<PageChunk> pool;

	float nextX;
	float nextY;
	boolean nextColumnEnabled;
	int xOffsetBetweenPageChunks=20;
	int yOffsetBetweenPageChunks=10;
	int columnIndex=1;
	public static int pageChunkIndex = 0;

	public static int pageChunkYOffset = 60;

	//Distance between two page chunks
	float dy;

	float dx = 20;

	//final float maxY = ResourcesManager.getInstance().windowDimensions.y;
	float minY = 50;


	public static final PageChunkFactory getInstance() {
		return INSTANCE;
	}

	public void create() {
		reset();
	}

	protected PageChunk onAllocatePoolItem(UserDO user) {
		//PageChunk p = new PageChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, 0, user.getDisplayName(), ResourcesManager.getInstance().payloadFromServer, user.getCows(), user.getBulls());
		ResourcesManager.userProfiles.put(user.getUserName(), user); // For retrieving user data from client end asynchronously when needed based on key
		pageChunkIndex++;
		PageChunk p = new PageChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, pageChunkIndex, user);

		ResourcesManager.getInstance().pages.addPageChunk(p,ResourcesManager.getInstance().currentPageIndex);
		return p;
	}

	protected PageChunk onAllocatePoolItemForClue(UserDO user) {
		PageChunk p = new PageChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, 0, user.getDisplayName(), user.getClueWord(), 0, user.getBulls(),user.getUserName());
		ResourcesManager.userProfiles.put(user.getDisplayName()!=null?user.getDisplayName():user.getUserName(), user); // For retrieving user data from client end asynchronously when needed based on key
		pageChunkIndex++;

		ResourcesManager.getInstance().pages.addPageChunk(p,ResourcesManager.getInstance().currentPageIndex);
		return p;
	}

	public synchronized PageChunk next(UserDO user) {

		PageChunk p = null;
		if(null!=user.getWordGuessed() || null!=user.getWordHosted()){
			p = onAllocatePoolItem(user);
		}
		else{
			p =	onAllocatePoolItemForClue(user);
		}
		p.setCullingEnabled(true);
		ResourcesManager.getInstance().maxNoOfPageChunkColumnsPerPage= (int) (ResourcesManager.getInstance().windowDimensions.x/ (ResourcesManager.getInstance().pageChunkWidth+xOffsetBetweenPageChunks));
			if(null!=ResourcesManager.getInstance().referenceLatestPage && !nextColumnEnabled){
				nextX= ResourcesManager.getInstance().referenceLatestPage.getX();
			}
			else if(nextColumnEnabled){
				nextX= ResourcesManager.getInstance().referenceLatestPage.getX();
				dx = xOffsetBetweenPageChunks+ResourcesManager.getInstance().pageChunkWidth;
				nextX += dx;
				nextColumnEnabled = false;
				if(columnIndex>ResourcesManager.getInstance().maxNoOfPageChunkColumnsPerPage){
					p.setColumnIndex(columnIndex);
				}
			}
			if(ResourcesManager.getInstance().guessImageclicked){
				minY= ResourcesManager.getInstance().pageChunkHeight/2+ResourcesManager.getInstance().yTextStart; //Should have been letter height but page chunk height does better job at spacing
			}
			else{
				minY = ResourcesManager.lineMovedTo+ ResourcesManager.getInstance().pageChunkHeight;
			}
			p.setPosition(nextX, nextY-ResourcesManager.getInstance().pageChunkHeight); //since Y anchor center 0  -- minus page chunk height is needed while setting maximum Y
			p.setAnchorCenter(0, 0);


			dy = ResourcesManager.getInstance().pageChunkHeight+yOffsetBetweenPageChunks; //10 is distance between page chunks
			//p.setWidth(ResourcesManager.getInstance().pageChunkWidth);


			/*	p.getScoreSensor().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

		p.getPageChunkUpBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(nextY + p.getPageChunkShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

		p.getPageChunkDownBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(nextY - p.getPageChunkShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);	

		p.getScoreSensor().setActive(true);
		p.getPageChunkUpBody().setActive(true);
		p.getPageChunkDownBody().setActive(true);
			 */


			nextY -= dy; //decrease next Page Chunk Y position by (height of previous and space needed)
			Log.e("PageChunkFactory", nextY-ResourcesManager.getInstance().pageChunkHeight+ "  "+ minY );
			if (nextY-ResourcesManager.getInstance().pageChunkHeight <= minY) { //since Y anchor center 0 -- minus page chunk height is needed while checking for minimum height reached
				//dy = -dy;
				nextY = ResourcesManager.getInstance().windowDimensions.y - pageChunkYOffset * ResourcesManager.resourceScaler; //Initial Page Chunk Position
				nextColumnEnabled = true;
				columnIndex++;
				/*dx = dx+ResourcesManager.getInstance().pageChunkWidth;
			nextX += dx;*/
			}
			ResourcesManager.getInstance().referenceLatestPage = p;
		
		return p;
	}

	public void recycle(final PageChunk p) {
		//p.detachSelf();
		if(p.hasParent()){
			ResourcesManager.getInstance().activity.runOnUpdateThread(new Runnable() {
				@Override
				public void run() {
					p.getParent().detachChild(p);
				}
			});
		}
		/*p.getScoreSensor().setActive(false);
		p.getPageChunkUpBody().setActive(false);
		p.getPageChunkDownBody().setActive(false);		
		p.getScoreSensor().setTransform(-1000, -1000, 0);
		p.getPageChunkUpBody().setTransform(-1000, -1000, 0);
		p.getPageChunkDownBody().setTransform(-1000, -1000, 0);*/
		//pool.recyclePoolItem(p);
	}

	public void reset() {
		if(null!=ResourcesManager.getInstance().pages){
			ResourcesManager.getInstance().pages.pages=new ArrayList<Page>();
		}
		pool = new ArrayList<PageChunk>(ResourcesManager.getInstance().maxPageChunksInPage);
		nextX = xOffsetBetweenPageChunks + (ResourcesManager.pageChunkInitialXOffset*ResourcesManager.resourceScaler);
		ResourcesManager.getInstance().referenceLatestPage = null;
		nextColumnEnabled=false;
		dx=xOffsetBetweenPageChunks;
		minY=50;
		columnIndex=1;
		nextY = ResourcesManager.getInstance().windowDimensions.y - pageChunkYOffset * ResourcesManager.resourceScaler;
		dy = yOffsetBetweenPageChunks+(ResourcesManager.getInstance().welcome==null?0:ResourcesManager.getInstance().welcome.getHeight()* ResourcesManager.resourceScaler);
		pageChunkIndex=0;
	}

}
