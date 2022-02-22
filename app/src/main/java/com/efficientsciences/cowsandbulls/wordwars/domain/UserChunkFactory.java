/**
 * Copyright (C) 2014 Sathish Kumar <bsathish.in@gmail.com>
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.efficientsciences.cowsandbulls.wordwars.managers.ResourcesManager;

public class UserChunkFactory {
	private static final UserChunkFactory INSTANCE = new UserChunkFactory();

	private UserChunkFactory() {	
		pool  = null;
	}

	List<UserChunk> pool;

	float nextX;
	float nextY;
	boolean nextColumnEnabled;
	int xOffsetBetweenUserChunks=20;
	int yOffsetBetweenUserChunks=20;
	int columnIndex=1;
	public static int userChunkIndex = 0;
	private static int RADIUS=100;

	//Distance between two user chunks
	float dy;

	float dx = 20;

	//final float maxY = ResourcesManager.getInstance().windowDimensions.y;
	float minY = 50;

	public static final UserChunkFactory getInstance() {
		return INSTANCE;
	}

	public void create() {
		reset();
	}

	protected UserChunk onAllocatePoolItem(UserDO user) {
		//UserChunk p = new UserChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, 0, user.getDisplayName(), ResourcesManager.getInstance().payloadFromServer, user.getCows(), user.getBulls());
		if(null!=user.getUserName())
			ResourcesManager.userProfiles.put(user.getUserName(), user); // For retrieving user data from client end asynchronously when needed based on key
		userChunkIndex++;
		UserChunk p = new UserChunk(0, 0, 0, 0, ResourcesManager.getInstance().vbom, userChunkIndex, user);
		//if(SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_WAITING){
		ResourcesManager.getInstance().users.add(p);
		//}
		return p;
	}

	public UserChunk next(UserDO user) {
		xOffsetBetweenUserChunks=ResourcesManager.getInstance().windowDimensions.x/2;
		yOffsetBetweenUserChunks=ResourcesManager.getInstance().windowDimensions.y/2;

		UserChunk p = onAllocatePoolItem(user);
		/*//TODO Uncomment below
		ResourcesManager.getInstance().maxNoOfUserChunkColumnsPerUser= (int) (ResourcesManager.getInstance().windowDimensions.x/ (ResourcesManager.getInstance().userChunkWidth+xOffsetBetweenUserChunks));
		if(null!=ResourcesManager.getInstance().referenceLatestUser && !nextColumnEnabled){
			nextX= ResourcesManager.getInstance().referenceLatestUser.getX();
		}
		else if(nextColumnEnabled){
			nextX= ResourcesManager.getInstance().referenceLatestUser.getX();
			dx = xOffsetBetweenUserChunks+ResourcesManager.getInstance().userChunkWidth;
			nextX += dx;
			nextColumnEnabled = false;
			if(columnIndex>ResourcesManager.getInstance().maxNoOfUserChunkColumnsPerUser){
				p.setColumnIndex(columnIndex);
			}
		}
		minY= ResourcesManager.getInstance().userChunkHeight+ResourcesManager.getInstance().yTextStart; //Should have been letter height but user chunk height does better job at spacing
		p.setPosition(nextX, nextY-ResourcesManager.getInstance().userChunkHeight); //since Y anchor center 0  -- minus user chunk height is needed while setting maximum Y
		 */		
		//if(SceneManager.getInstance().getCurrentSceneType()==SceneType.SCENE_WAITING){
			 p.setAnchorCenter(0.5f, 0.5f);

			 int numberOfUsers=null!=ResourcesManager.getInstance().users?ResourcesManager.getInstance().users.size():0;
			 
			 if(numberOfUsers>12){
				 float lastX = 50;
				 float lastY = 0;
				 nextX = 0;
				 nextY = (float) ((ResourcesManager.getInstance().windowDimensions.y/2)+100*ResourcesManager.resourceScaler); 
				 lastY =nextY;
				 for(int i = 0; i < numberOfUsers; i++) {
					 
					 if(i !=0){
						 UserChunk lastUser =  ResourcesManager.getInstance().users.get(i-1);
						 
						 if(null!= lastUser){
							 lastX = lastUser.getX()+lastUser.getWidthScaled();
							 if(lastUser.getX()+(lastUser.getWidthScaled()*2)>ResourcesManager.getInstance().windowDimensions.x){
								 nextX = 50;
								 nextY = lastY - lastUser.getHeightScaled() -  60*ResourcesManager.resourceScaler;
								 lastX = nextX;
								 lastY = nextY;
								 Log.e("lastUser.getHeightScaled()", lastUser.getHeightScaled()+" ");

							 }
						 }
					 }
					 //lastX = nextX;
					 
					 nextX = (float) (lastX + 20*ResourcesManager.resourceScaler);
					 Log.e("User nextY In Factory", nextY+" ");
					 ResourcesManager.getInstance().users.get(i).setPosition(nextX, nextY);
				 }
			 }
			 
			 else{
				 if(numberOfUsers>8){
					 RADIUS=210;
				 }
				 else if(numberOfUsers>4){
					 RADIUS=150;
				 }
				 for(int i = 0; i < numberOfUsers; i++) {


					 nextX = (float) (xOffsetBetweenUserChunks + RADIUS * ResourcesManager.resourceScaler * Math.cos(2 * Math.PI * i / numberOfUsers));
					 nextY = (float) (yOffsetBetweenUserChunks + RADIUS * ResourcesManager.resourceScaler * Math.sin(2 * Math.PI * i / numberOfUsers)); 
					 ResourcesManager.getInstance().users.get(i).setPosition(nextX, nextY);
				 }
			 }
			 // }
		 //dy = ResourcesManager.getInstance().userChunkHeight+yOffsetBetweenUserChunks; //10 is distance between user chunks
		 //p.setWidth(ResourcesManager.getInstance().userChunkWidth);


		 /*	p.getScoreSensor().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				nextY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

		p.getUserChunkUpBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(nextY + p.getUserChunkShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);

		p.getUserChunkDownBody().setTransform(nextX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 
				(nextY - p.getUserChunkShift()) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT, 0);	

		p.getScoreSensor().setActive(true);
		p.getUserChunkUpBody().setActive(true);
		p.getUserChunkDownBody().setActive(true);
		  */


		 /*nextY -= dy; //decrease next User Chunk Y position by (height of previous and space needed)
		if (nextY-ResourcesManager.getInstance().userChunkHeight <= minY) { //since Y anchor center 0 -- minus user chunk height is needed while checking for minimum height reached
			//dy = -dy;
			nextY = ResourcesManager.getInstance().windowDimensions.y - 20 * ResourcesManager.resourceScaler; //Initial User Chunk Position
			nextColumnEnabled = true;
			columnIndex++;
			dx = dx+ResourcesManager.getInstance().userChunkWidth;
			nextX += dx;
		}
		  */
		 return p;
	}

	public void recycle(final UserChunk p) {
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
		p.getUserChunkUpBody().setActive(false);
		p.getUserChunkDownBody().setActive(false);		
		p.getScoreSensor().setTransform(-1000, -1000, 0);
		p.getUserChunkUpBody().setTransform(-1000, -1000, 0);
		p.getUserChunkDownBody().setTransform(-1000, -1000, 0);*/
		//pool.recyclePoolItem(p);
	}

	public void reset() {
		pool = new ArrayList<UserChunk>(ResourcesManager.getInstance().maxUserChunksInRoom);
		nextX = xOffsetBetweenUserChunks;
		nextColumnEnabled=false;
		dx=xOffsetBetweenUserChunks;
		minY=50;
		columnIndex=1;
		nextY = ResourcesManager.getInstance().windowDimensions.y - 20 * ResourcesManager.resourceScaler;
		//dy = yOffsetBetweenUserChunks+(ResourcesManager.getInstance().welcome==null?0:ResourcesManager.getInstance().welcome.getHeight()* ResourcesManager.resourceScaler);
		userChunkIndex=0;
	}

}
