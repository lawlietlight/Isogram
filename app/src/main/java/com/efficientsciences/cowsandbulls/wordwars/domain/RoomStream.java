/**
 * RoomProperties.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.List;

/**
 * @author SATHISH
 *
 */
public class RoomStream{


	private byte roomState = RoomProperties.STATE_HOSTING_AVAILABLE;


	public byte getRoomState() {
		return roomState;
	}

	public void setRoomState(byte roomState) {
		this.roomState = roomState;
	}



	private int index;
	private int noOfParticipants;
	private int difficultyLevel;
	private String roomHostedBy;
	private List<UserDO> participants;
	private String hostedRoomSubTopic = "chat";
	private int maxNoOfLetters;
	private int numOfLettersHosted;
	private int waitTimeElapsed;
	private boolean timeSyncPulseFlag; 
	
	
	
	public boolean isTimeSyncPulseFlag() {
		return timeSyncPulseFlag;
	}

	public void setTimeSyncPulseFlag(boolean timeSyncPulseFlag) {
		this.timeSyncPulseFlag = timeSyncPulseFlag;
	}

	public int getWaitTimeElapsed() {
		return waitTimeElapsed;
	}

	public void setWaitTimeElapsed(int waitTimeElapsed) {
		this.waitTimeElapsed = waitTimeElapsed;
	}

	public int getNumOfLettersHosted() {
		return numOfLettersHosted;
	}

	public void setNumOfLettersHosted(int numOfLettersHosted) {
		this.numOfLettersHosted = numOfLettersHosted;
	}

	public int getMaxNoOfLetters() {
		return maxNoOfLetters;
	}


	public void setMaxNoOfLetters(int maxNoOfLetters) {
		this.maxNoOfLetters = maxNoOfLetters;
	}

	
	
	public String getHostedRoomSubTopic() {
		return hostedRoomSubTopic;
	}

	public void setHostedRoomSubTopic(String hostedRoomSubTopic) {
		this.hostedRoomSubTopic = hostedRoomSubTopic;
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
	 * 
	 */
	public void addNumOfParticipants() {
		this.noOfParticipants++;
	}

}
