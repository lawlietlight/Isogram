/**
 * Level.java @author Sathish Kumar
 */
package com.efficientsciences.cowsandbulls.wordwars.domain;

/**
 * @author SATHISH
 *
 */
public class Level {


	private long score;
	private long numberOfCoinsPerGame;
	private int levelDifficulty;
	
	private int experience;
	
	//calculated score For each game
	private int intelligentQuotientForCurrentgame;
	
	private String iqDesc;
	
	private IQ wordIqEnum;
	private USERLEVELEXPERIENCE experienceEnum;

	
	/**
	 * @param numberOfGamesWon 
	 * 
	 */
	public Level(long l,int levelId, int numberOfGamesPlayed, int numberOfGamesWon) {
		this.numberOfCoinsPerGame=l;
		this.levelDifficulty=levelId;
		this.experience = numberOfGamesPlayed;
		calculateIntelligentQuotientForCurrentgame(numberOfGamesWon);
	}


	
	/**
	 * @param score2
	 */
	public Level(long score) {
		setScore(score);
		//seIntelligentQuotientForCurrentgame((int)score);
	}

	/**
	 * @param score2
	 */
	public IQ getWordIq(long score) {
		setScore(score);
		setIntelligentQuotientForCurrentgame((int)score);
		return getWordIq();
	}



	/**
	 * 
	 */
	private void calculateIntelligentQuotientForCurrentgame(int numberOfGamesWon) {
		float percentageWins= numberOfGamesWon/experience;
		int normalisedScore = (int)(numberOfCoinsPerGame*percentageWins);
		if(normalisedScore<1){
			normalisedScore=1;
		}
		if(levelDifficulty<1){
			levelDifficulty=1;
		}
		setIntelligentQuotientForCurrentgame(normalisedScore*levelDifficulty);
	}



	//based on total gameplay experience and accumulated score
	public enum IQ{
		PRECOCIOUS("PRECOCIOUS STAR"),
		SYNAPTIC_THRESHOLD("STAR OF SYNAPSES"),
		SUPERNOVA("SUPERNOVA"),
		SUPERIOR_INTELLECT("SUPERIOR STAR"),
		INTELLECT("INTELLECTUAL STAR"),
		RED_GIANT("RED GIANT"),
		AVERAGE_STAR("AVERAGE STAR"),
		PROTOSTAR("PROTOSTAR");
		

	    private final String iqString;       

	    private IQ(String s) {
	    	iqString = s;
	    }

	    public boolean equalsName(String pIqString){
	        return (pIqString == null)? false:iqString.equals(pIqString);
	    }

	    public String toString(){
	       return iqString;
	    }
	}
	

	
	private enum USERLEVELEXPERIENCE{
		USERLEVEL5 (5),
		USERLEVEL4 (4),
		USERLEVEL3 (3),
		USERLEVEL2 (2),
		USERLEVEL1 (1);
		
		private int experienceLevel;       

	    private USERLEVELEXPERIENCE(int s) {
	    	experienceLevel = s;
	    }

	    public boolean equals(int otherName){
	        return experienceLevel==otherName;
	    }

	    public String toString(){
	       return experienceLevel+"";
	    }
	}

	/**
	 * @return the iq
	 */
	public IQ getWordIq() {
		if(getScore()>750000){
			wordIqEnum=IQ.PRECOCIOUS;
		}
		else if(getScore()>440490){
			wordIqEnum=IQ.SYNAPTIC_THRESHOLD;
		}
		else if(getScore()>200240){
			wordIqEnum=IQ.SUPERNOVA;
		}
		else if(getScore()>55140){
			wordIqEnum=IQ.SUPERIOR_INTELLECT;
		}
		else if(getScore()>20700){
			wordIqEnum=IQ.INTELLECT;
		}
		else if(getScore()>10000){
			wordIqEnum=IQ.RED_GIANT;
		}
		else if(getScore()>5000){
			wordIqEnum=IQ.AVERAGE_STAR;
		}
		else{
			wordIqEnum=IQ.PROTOSTAR;
		}

		return wordIqEnum;
	}
	

	

	public int geIntelligentQuotientForCurrentgame() {
		return intelligentQuotientForCurrentgame;
	}


	public void setIntelligentQuotientForCurrentgame(int intelligentQuotientForCurrentgame) {
		this.intelligentQuotientForCurrentgame = intelligentQuotientForCurrentgame;
	}



	/**
	 * 
	 */
	public long getNewScore(long presentScore) {
		setScore(this.intelligentQuotientForCurrentgame+presentScore);
		return getScore();
	}


	/**
	 * 
	 */
	public int isNewScoreCrossingLevels(long presentScore) {
		long scoreCrossed= this.intelligentQuotientForCurrentgame+presentScore;
		int wordIqEnumTemp = 1;
		if(scoreCrossed>750000 && presentScore<=750000){
			wordIqEnumTemp=8;
		}
		else if(scoreCrossed>440490  && presentScore<=440490 ){
			wordIqEnumTemp=7;
		}
		else if(scoreCrossed>200240  && presentScore<=200240){
			wordIqEnumTemp=6;
		}
		else if(scoreCrossed>55140  && presentScore<=55140){
			wordIqEnumTemp=5;
		}
		else if(scoreCrossed>20700  && presentScore<=20700){
			wordIqEnumTemp=4;
		}
		else if(scoreCrossed>10000  && presentScore<=10000){
			wordIqEnumTemp=3;
		}
		else if(scoreCrossed>5000  && presentScore<=5000){
			wordIqEnumTemp=2;
		}
		else if(scoreCrossed>1  && presentScore<=1){
			wordIqEnumTemp=1;
		}
		return wordIqEnumTemp;
	}

	public long getScore() {
		return score;
	}



	public void setScore(long score) {
		this.score = score;
	}
	
	
}
