//ISABELLA NIKOLAIDIS
//260684656

import java.util.*;
import java.io.*;

// This class implements a google-like search engine
public class SearchEngine {

    public HashMap<String, LinkedList<String> > wordIndex;                  // this will contain a set of pairs (String, LinkedList of Strings)	
    public DirectedGraph internet;             // this is our internet graph
    
    
    
    // Constructor initializes everything to empty data structures
    // It also sets the location of the internet files
    SearchEngine() {
	// Below is the directory that contains all the internet files
	HtmlParsing.internetFilesLocation = "internetFiles";
	wordIndex = new HashMap<String, LinkedList<String> > ();		
	internet = new DirectedGraph();				
    } // end of constructor//2017
    
    
    // Returns a String description of a searchEngine
    public String toString () {
	return "wordIndex:\n" + wordIndex + "\ninternet:\n" + internet;
    }
    
    
    //helper method
    //pass word, pass link
    public void addToList(String word, String link) {
        //says links = the linkedlist at word
    	LinkedList<String> wordlinks = wordIndex.get(word);
        //if links is empty, new linked list at this empty place
    	if (wordlinks == null) {
    	    //puts new empty linkedlist there
    		wordlinks = new LinkedList<String> ();
    		wordIndex.put(word, wordlinks);
    	}
    	//adds link to linkedlist
    	wordlinks.add(link);
    }
    
    void traverseInternet(String url) throws Exception {
    	//adds given url as vertex on graph
    	internet.addVertex(url);    	
    	//deals with content
    	LinkedList<String> content = HtmlParsing.getContent(url);
    	//processes content of webpage
    	Iterator<String> j = content.iterator();
    	while (j.hasNext()) {
    		String s = (j.next());
    		//for each word s in content, addToList finds s in wordIndex and adds url
    		addToList(s, url);
    	}
    	//set url visited to true
    	internet.setVisited(url, true);
    	
    	//deals with links
    	LinkedList<String> links = HtmlParsing.getLinks(url);
    	//creates iterator to iterate through all places url links
    	Iterator<String> i = links.iterator();
    	//for each link, adds edge and then recursively calls traverseInternet on link obtained
    	while (i.hasNext()) {
    		String s = i.next();
    		internet.addEdge(url, s);
    		//if statement for if the neighboring url is not visited
    		if (!internet.getVisited(s)) {
    			//recursive call on the new url
    			traverseInternet(s);
    		}
    	}
	
    } 
    
    void computePageRanks() {
    	//we get this urlList to originally populate internet.pageRank
    	LinkedList<String> urlList = internet.getVertices();

    	//iterates through entire urlList
    	for (int i = 0; i < urlList.size(); i++) {
    		//sets url equal to urlstring at index i of urlList
    		String url = urlList.get(i);
    		//for each url, initializes their page rank to 1
    		internet.setPageRank(url, 1);
    	}
    	
    	//for each key (which is referred to as urlExamined)
    	for (int i = 0; i<100;i++) {
    	for (String urlExamined : urlList) {
    		//get edges
    		LinkedList<String> citers = internet.getEdgesInto(urlExamined);

			double updatedPageRank = 0;
			//method to find page rank of every citer of urlExamined
    		for (String citer : citers) {
    			//get pageRank of that citer, as well as outdegree of that citer
    			double citerPageRank = internet.getPageRank(citer);
    			int citerOutDegree = internet.getOutDegree(citer);
    			//create updated page rank which takes into account the citer page rank
    			updatedPageRank = updatedPageRank + (citerPageRank/citerOutDegree);
    		}
    		//calculate the updated page rank of urlExamined
    		updatedPageRank = (0.5) + (0.5*updatedPageRank);
    		internet.setPageRank(urlExamined, updatedPageRank);
    	}
    	}
    		
    } 
    
	
    String getBestURL(String query) {
    	//if single word query found in word index
    	if (wordIndex.containsKey(query)) {
    		//get all sites associated with word
    		LinkedList<String> sites = wordIndex.get(query);
    		//temporarily initialize best page result to first site in list of sites
    		String bestPage = sites.getFirst();
    		//for every site associated with query
    		for (String site : sites) {
    			//if page rank of site is better than current best page, update bestPage to site with better page rank
    			if (internet.pageRank.get(site) > internet.pageRank.get(bestPage)) {
    				bestPage = site;
    			}
    		}
    		return (bestPage+", p.r. = "+ internet.getPageRank(bestPage));
    		
    	} else {
    		//if query not found in index, return empty string
    		return "";
    	}
    } // end of getBestURL
    
    
	
    public static void main(String args[]) throws Exception{		
	SearchEngine mySearchEngine = new SearchEngine();
	// to debug your program, start with.
	mySearchEngine.traverseInternet("http://www.cs.mcgill.ca");

	mySearchEngine.computePageRanks();

	
	BufferedReader stndin = new BufferedReader(new InputStreamReader(System.in));
	String query;
	do {
	    System.out.print("Enter query: ");
	    query = stndin.readLine();
	    if ( query != null && query.length() > 0 ) {
		System.out.println("Best site = " + mySearchEngine.getBestURL(query));
	    }
	} while (query!=null && query.length()>0);				
    } // end of main
}
