package com.efficientsciences.cowsandbulls.wordwars.scenes;

import com.efficientsciences.cowsandbulls.wordwars.domain.UserDO;

public interface RealGameInterface {
	
	//public void addPageChunk();

	/**
	 * @param user
	 */
	void addPageChunk(UserDO user);

	/**
	 * 
	 */
	void confirmBackKeyPressed();
	
	void createDrawerChild();
}
