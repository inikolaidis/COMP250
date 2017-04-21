// STUDENT_NAME:Isabella Nikolaidis
// STUDENT_ID: 260684656

import java.util.*;
import java.io.*;



public class Scrabble{

    static HashSet<String> myDictionary; // this is where the words of the dictionary are stored

    // DO NOT CHANGE THIS METHOD
    // Reads dictionary from file
    public static void readDictionaryFromFile(String fileName) throws Exception {
        myDictionary=new HashSet<String>();

        BufferedReader myFileReader = new BufferedReader(new FileReader(fileName) );

        String word;
        while ((word=myFileReader.readLine())!=null) myDictionary.add(word);

	myFileReader.close();
    }



    /* Arguments: 
        char availableLetters[] : array of characters containing the letters that remain available
        String prefix : Word assembled to date
        Returns: String corresponding to the longest English word starting with prefix, completed with zero or more letters from availableLetters. 
	    If no such word exists, it returns the String ""
     */
     public static String longestWord(char availableLetters[], String prefix) {
	 String longest = "";
	 int currentLongestLength = 0;
	 
	 //base case = when there are is only one available letter
	 if (availableLetters.length == 1) {
		 //checks if dictionary has prefix+one available letter
		 if(myDictionary.contains(prefix+availableLetters[0])) {
			 //returns prefix + last letter because it was found in dictionary
			 return prefix+availableLetters[0];
		 } else {
			 //if not found in dictionary, returns empty string
			 return "";
		 }
	 }
	 else {
		 //create tempArray of length availableLetters -1 
		 char[] tempArray = new char[availableLetters.length -1];
		 //this for loop iterates as many times as there are available letters
		 for (int i = 0; i < availableLetters.length; i++) {
			 //i needs to iterate through all the possible availableLetters
			 //this for loop iterates for as many availableLetters as there are -1
			 for (int j = 0; j < tempArray.length; j++) {
				 //if statement which skips i, the excluded element
				 if (j < i) {
					 tempArray[j] = availableLetters[j];
				 } else {
					 //notice the j+1 means that j = i is skipped
					 tempArray[j] = availableLetters[j + 1];
				 }
			 }
			//checks if contained in dictionary
			if (myDictionary.contains(prefix+availableLetters[i])) {
				//if statement which updates best word if a longer one is found
				//also updates length of best word (to keep track of longest)
				if (prefix.length() + 1 > currentLongestLength) {
					currentLongestLength = prefix.length() + 1;
					longest = prefix+availableLetters[i];
				 } 
			}
			//recursive step which calls upon longestWord method from within longestWord method with string temp
			String temp = longestWord(tempArray, prefix+availableLetters[i]);
			if (temp.length() > currentLongestLength) {
				currentLongestLength = temp.length();
				longest = temp;
			}	
		 }
		 //return longest is here because it must come after the for loop, which explores every possible permutation
		 return longest;
	 }
	 
    }

    
    
    /* main method
        You should not need to change anything here.
     */
    public static void main (String args[]) throws Exception {
       
	// First, read the dictionary
	try {
	    readDictionaryFromFile("englishDictionary.txt");
        }
        catch(Exception e) {
            System.out.println("Error reading the dictionary: "+e);
        }
        
        
        // Ask user to type in letters
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in) );
        char letters[]; 
        do {
            System.out.println("Enter your letters (no spaces or commas):");
            
            letters = keyboard.readLine().toCharArray();

	    // now, enumerate the words that can be formed
            String longest = longestWord(letters, "");
	    System.out.println("The longest word is "+longest);
        } while (letters.length!=0);

        keyboard.close();
        
    }
}