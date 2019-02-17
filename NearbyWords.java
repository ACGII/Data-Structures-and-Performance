/**
 * 
 */
package spelling;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;


/**
 * @author UC San Diego Intermediate MOOC team
 *
 */
public class NearbyWords implements SpellingSuggest {
	// THRESHOLD to determine how many words to look through when looking
	// for spelling suggestions (stops prohibitively long searching)
	// For use in the Optional Optimization in Part 2.
	private static final int THRESHOLD = 50000; 

	Dictionary dict;

	public NearbyWords (Dictionary dict) 
	{
		this.dict = dict;
	}

	/** Return the list of Strings that are one modification away
	 * from the input string.  
	 * @param s The original String
	 * @param wordsOnly controls whether to return only words or any String
	 * @return list of Strings which are nearby the original string
	 */
	public List<String> distanceOne(String s, boolean wordsOnly )  {
		   List<String> retList = new ArrayList<String>();
		   insertions(s, retList, wordsOnly);
		   substitution(s, retList, wordsOnly);
		   deletions(s, retList, wordsOnly);
		   return retList;
	}

	
	/** Add to the currentList Strings that are one character mutation away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void substitution(String s, List<String> currentList, boolean wordsOnly) {
		// for each letter in the s and for all possible replacement characters
		for(int index = 0; index < s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				StringBuffer sb = new StringBuffer(s);
				sb.setCharAt(index, (char)charCode);
				

				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}
	
	/** Add to the currentList Strings that are one character insertion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void insertions(String s, List<String> currentList, boolean wordsOnly ) {
		// TODO: Implement this method  
		for(int index = 0; index <= s.length(); index++){
			for(int charCode = (int)'a'; charCode <= (int)'z'; charCode++) {
				// use StringBuffer for an easy interface to permuting the 
				// letters in the String
				
				StringBuffer sb = new StringBuffer(s);
				sb.insert(index, (char)charCode);
				//sb.setCharAt(index, (char)charCode);
				//System.out.println("DBG "+sb.toString()+" indx "+index);
				// if the item isn't in the list, isn't the original string, and
				// (if wordsOnly is true) is a real word, add to the list
				if(!currentList.contains(sb.toString()) && 
						(!wordsOnly||dict.isWord(sb.toString())) &&
						!s.equals(sb.toString())) {
					currentList.add(sb.toString());
				}
			}
		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param s The original String
	 * @param currentList is the list of words to append modified words 
	 * @param wordsOnly controls whether to return only words or any String
	 * @return
	 */
	public void deletions(String s, List<String> currentList, boolean wordsOnly ) {
		// TODO: Implement this method
		StringBuffer sba = new StringBuffer(s);
		for(int index = 0; index < s.length(); index++){
			StringBuffer sb = new StringBuffer();
			int k = 0;
			for(int j=0;j<s.length();j++) {
				if(j != index) {
					sb.insert(k, sba.charAt(j));
					k++;
				}
			}
			
			if(!currentList.contains(sb.toString()) && 
				(!wordsOnly||dict.isWord(sb.toString())) &&
				!s.equals(sb.toString())) {
					currentList.add(sb.toString());
			}

		}
	}

	/** Add to the currentList Strings that are one character deletion away
	 * from the input string.  
	 * @param word The misspelled word
	 * @param numSuggestions is the maximum number of suggestions to return 
	 * @return the list of spelling suggestions
	 */
	@Override
	public List<String> suggestions(String word, int numSuggestions) {

		// initial variables
		List<String> queue = new LinkedList<String>();     // String to explore
		HashSet<String> visited = new HashSet<String>();   // to avoid exploring the same  
														   // string multiple times
		List<String> retList = new LinkedList<String>();   // words to return
		 
		
		// insert first node
		queue.add(word);
		visited.add(word);
		int numWords = 0;
		while(!queue.isEmpty()) {
			String wrd = queue.remove(0);
			List<String> l = distanceOne(wrd, false);
			for(String s : l) {
				visited.add(s);
				queue.add(s);
				numWords++;
				if(dict.isWord(s)) {
					if(!retList.contains(s)) {
						retList.add(s);
					}
					
				}
				if(retList.size()>= numSuggestions) {
					break;
				}
			}
			if(retList.size()>= numSuggestions) {
				break;
			}
			if(numWords > THRESHOLD) {
				System.out.println("Threshold reached "+numWords);
				break;
			}
			
		}
					
		// TODO: Implement the remainder of this method, see assignment for algorithm
		
		return retList;

	}	

   public static void main(String[] args) {
	   // basic testing code to get started
	   String word = "train";
	   // Pass NearbyWords any Dictionary implementation you prefer
	   Dictionary d = new DictionaryHashSet();
	   DictionaryLoader.loadDictionary(d, "data/dict.txt");
	   NearbyWords w = new NearbyWords(d);
	   String nw = "a";
	   List<String> scurlist = new ArrayList<String>();
	   w.substitution(nw,  scurlist, true);
	   System.out.println("substitutions for "+nw+" "+scurlist);
	   
	   List<String> icurlist = new ArrayList<String>();
	   w.insertions(nw,  icurlist, true);
	   System.out.println("insertions for "+nw+" "+icurlist);
	   
	   List<String> dcurlist = new ArrayList<String>();
	   w.deletions(nw,  dcurlist, true);
	   System.out.println("deletions for "+nw+" "+dcurlist);
	   
	   List<String> l = w.distanceOne(word, true);
	   System.out.println("One away word Strings for for \""+word+"\" are:");
	   System.out.println(l+"\n");

	   //word = "tailo";
	   word = "kangaro";
	   List<String> suggest = w.suggestions(word, 10);
	   System.out.println("Spelling Suggestions for \""+word+"\" are:");
	   System.out.println(suggest);
	  /* */
   }

}
