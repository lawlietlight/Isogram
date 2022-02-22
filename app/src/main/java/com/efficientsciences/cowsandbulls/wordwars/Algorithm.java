package com.efficientsciences.cowsandbulls.wordwars;

/**
 * Created by SATHISH on 14/12/13.
 */


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Algorithm {

	static String strDirectoryFile = "F:/Cows n Bulls/algorithmTest.txt";

	public Algorithm(String hostActualWord) {
		this.actualWord = hostActualWord;

	}

	static List linesList = new ArrayList();
	static int numOfLetters = 0;
	static List<String> WordsList = new ArrayList<String>();
	static List<String> WordsListClone = new ArrayList<String>();
	static List<Integer> WordsListCows = new ArrayList<Integer>();
	static List<Integer> WordsListBulls = new ArrayList<Integer>();
	static Set<Character> ignoredCharacterSet = new HashSet();
	static List<Character> ignoredCharacterList = new ArrayList();
	static String actualWord;
	static Set<Character> mayBeCharacterSet = new HashSet<Character>();
	static List<Character> mayBeCharacterList = new ArrayList<Character>();


	static Set<Character> sureCharacterSet = new HashSet<Character>();
	static List<Character> sureCharacterList = new ArrayList<Character>();

	static char[] finalWord;
	static List<Character> finalLetterList;


	/**
	 * @param args
	 */
	 public static void main(String[] args) {
		 List sArray = null;
		 try {
			 sArray = readFromFileLineByLine();
		 } catch (IOException e) {

			 e.printStackTrace();
		 }
		 getIndividualLists(sArray);
		 printCowsandbullsForEachIndex();
		 //gettingLetterToBeIgnored();
		 //getMayBeLetters();
		 //	getBullsOut();


	 }

	 public static String printCowsandbullsForEachIndex() {
		 int countCows = 0;
		 int countBulls = 0;
		 for (Iterator iterator = WordsList.iterator(); iterator.hasNext(); ) {
			 String type = (String) iterator.next();
			 countCows = 0;
			 countBulls = 0;
			 for (int i = 0; i < actualWord.length(); i++) {
				 Character s = actualWord.charAt(i);

				 if (type.contains(s.toString())) {
					 int pplIndex = type.indexOf(s.toString());
					 if (i == pplIndex) {
						 countBulls++;
					 } else {
						 countCows++;
					 }
				 }
			 }
			 System.out.println(type + ":" + "C" + countCows + "B" + countBulls);

		 }
		 return "C" + countCows + " " + "B" + countBulls;

	 }


	 private static void getBullsOut() {
		 for (int i = 0; i < WordsListCows.size(); i++) {
			 Integer cow = WordsListCows.get(i);
			 Integer bull = WordsListBulls.get(i);

			 String s;
			 s = WordsList.get(i);
			 s = s.toUpperCase();
			 char[] mayBeCharSetArrayLocal;
			 mayBeCharSetArrayLocal = s.toCharArray();

			 //if cows and bulls togather contribute to the whole word
			 if (cow + bull == numOfLetters) {
				 System.out.println("Letters are just out of order 'm trying to structure it" + s);

			 }

			 //if cows and bulls togather contribute to the whole word after ignoring not need letter
			 int wordSize = mayBeCharSetArrayLocal.length;
			 for (int j = 0; j < wordSize; j++) {
				 char c = mayBeCharSetArrayLocal[j];
				 if (mayBeCharSetArrayLocal.length < numOfLetters && mayBeCharSetArrayLocal.length == cow + bull)
					 sureCharacterSet.add(Character.toUpperCase(c));


			 }


			 if (bull != 0) {
				 if (finalLetterList.isEmpty())
					 finalWord = mayBeCharSetArrayLocal;

				 else {
					 for (int j = 0; j < mayBeCharSetArrayLocal.length; j++) {
						 //if(mayBeCharSetArrayLocal)
					 }
				 }
			 }
		 }
		 System.out.println(sureCharacterSet);
	 }


	 private static void getMayBeLetters() {
		 for (int i = 0; i < WordsListCows.size(); i++) {
			 Integer cow = WordsListCows.get(i);
			 Integer bull = WordsListBulls.get(i);
			 String sNulledOut;
			 String sGapPreserve;
			 char[] mayBeCharSetArrayLocal;
			 //	if(cow!=0 || bull!=0){
			 sNulledOut = WordsList.get(i);
			 sNulledOut = sNulledOut.toUpperCase();
			 sGapPreserve = sNulledOut;
			 mayBeCharSetArrayLocal = sNulledOut.toCharArray();

			 int wordSize = mayBeCharSetArrayLocal.length;
			 for (int j = 0; j < wordSize; j++) {
				 char c = mayBeCharSetArrayLocal[j];

				 mayBeCharacterSet.add(Character.toUpperCase(c));
				 mayBeCharacterSet.removeAll(ignoredCharacterSet);

				 for (Iterator iterator = ignoredCharacterSet.iterator(); iterator
						 .hasNext(); ) {
					 Character charToBeRemoved = (Character) iterator.next();
					 //s=s.replace(charToBeRemoved, '\u0000');

					 if (sNulledOut != sNulledOut.replace(charToBeRemoved, ' ')) {

						 sNulledOut = sNulledOut.replace(charToBeRemoved, ' ');
						 sNulledOut = sNulledOut.replaceAll("\\s", "");
						 sGapPreserve = sGapPreserve.replace(charToBeRemoved, '\u0000');

						 //System.out.println(WordsList);
					 }

				 }
				 //System.out.println(s);
				 //mayBeCharacterList.add(c);
			 }


			 //}
			 WordsList.remove(i);
			 WordsList.add(i, sNulledOut);
			 WordsListClone.remove(i);
			 WordsListClone.add(i, sGapPreserve);
		 }
		 System.out.println(WordsList);
		 System.out.println(WordsListClone);
		 System.out.println(WordsListBulls);
		 System.out.println(WordsListCows);
		 Collections.sort(new ArrayList(mayBeCharacterSet));
		 System.out.println(mayBeCharacterSet);
		 //System.out.println(mayBeCharacterList);

	 }

	 public static void gettingLetterToBeIgnored() {

		 for (int i = 0; i < WordsListCows.size(); i++) {
			 Integer cow = WordsListCows.get(i);
			 Integer bull = WordsListBulls.get(i);
			 if (cow == 0 && 0 == bull) {
				 char[] ignoredCharSetArray = WordsList.get(i).toCharArray();

				 for (int j = 0; j < ignoredCharSetArray.length; j++) {
					 char c = ignoredCharSetArray[j];
					 ignoredCharacterSet.add(Character.toUpperCase(c));

				 }


			 }
		 }
		 ignoredCharacterList = new ArrayList(ignoredCharacterSet);
		 Collections.sort(ignoredCharacterList);
		 System.out.println(ignoredCharacterList);
	 }

	 public static void getIndividualLists(List linesList) {

		 numOfLetters = Integer.parseInt((String) linesList.remove(0));
		 actualWord = ((String) linesList.remove(0));

		 finalWord = new char[numOfLetters];
		 finalLetterList = new ArrayList(numOfLetters);
		 for (Iterator iterator = linesList.iterator(); iterator.hasNext(); ) {
			 String object = (String) iterator.next();
			 WordsList.add(object.substring(0, numOfLetters));
			 //WordsListCows.add(Integer.parseInt((String)object.substring(numOfLetters+2, numOfLetters+3)));
			 //WordsListBulls.add(Integer.parseInt((String)object.substring(numOfLetters+4, numOfLetters+5)));
		 }
		 WordsListClone.addAll(WordsList);
		 return;

	 }

	 static List readFromFileLineByLine() throws IOException {

		 FileInputStream fis = new FileInputStream(strDirectoryFile);
		 // Get the object of DataInputStream
		 DataInputStream in = new DataInputStream(fis);
		 BufferedReader br = new BufferedReader(new InputStreamReader(in));
		 String strLine;
		 //Read File Line By Line
		 while ((strLine = br.readLine()) != null) {
			 linesList.add(strLine);
		 }


		 return linesList;

	 }

}
