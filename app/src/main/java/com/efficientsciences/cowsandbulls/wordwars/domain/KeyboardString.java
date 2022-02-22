package com.efficientsciences.cowsandbulls.wordwars.domain;

import java.util.HashSet;
import java.util.Set;

public class KeyboardString {

	
	private StringBuffer keyString=new StringBuffer();
	private KeyboardChar[] keyCharArray;
	private Set<KeyboardChar> charSet = new HashSet<KeyboardChar>();
	
	
	public  Set<KeyboardChar> getCharSet() {
		return charSet;
	}


	public KeyboardChar[] getKeyCharArray() {
		return keyCharArray;
	}



	public void setKeyCharArray(KeyboardChar[] keyCharArray) {
		this.keyCharArray = keyCharArray;
	}



	@Override
	public String toString() {
		if(null!=keyCharArray){
			
			for (int i = 0; i < keyCharArray.length; i++) {
				KeyboardChar keyChar = keyCharArray[i];
				keyString.append(keyChar.getCharCode());
			}
		}
		return keyString.toString();
	}
}
