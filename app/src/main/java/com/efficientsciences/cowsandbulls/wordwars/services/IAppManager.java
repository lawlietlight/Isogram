package com.efficientsciences.cowsandbulls.wordwars.services;

import java.io.UnsupportedEncodingException;

import com.efficientsciences.cowsandbulls.wordwars.domain.RoomStream;


public interface IAppManager {
	
	public String getUsername();
	public String sendMessage(String username,String tousername, String message) throws UnsupportedEncodingException;
	public String authenticateUser(String usernameText, String passwordText) throws UnsupportedEncodingException; 
	public void messageReceived(String username, final int roomNumber, RoomStream roomDetailsFromServer); 
//	public void setUserKey(String value);
	public boolean isNetworkConnected();
	public boolean isUserAuthenticated();
	public String getLastRawFriendList();
	public void exit();
	//public String signUpUser(String usernameText, String passwordText, String email);
	public String addNewFriendRequest(String friendUsername);
	public String sendFriendsReqsResponse(String approvedFriendNames,
			String discardedFriendNames);

	
}
