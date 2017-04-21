import java.util.*;

class CardDeck {
    LinkedList<Integer> deck;

    // constructor, creates a deck with n cards, placed in increasing order
    CardDeck(int n) {
	deck = new LinkedList<Integer> ();
	for (int i=1;i<=n;i++) deck.addLast(new Integer(i));
    }

    // executes the card trick
    public void runTrick() {

	while (!deck.isEmpty()) {
	    // remove the first card and remove it
	    Integer topCard = deck.removeFirst();
	    System.out.println("Showing card "+topCard);

	    // if there's nothing left, we are done
	    if (deck.isEmpty()) break;
	    
	    // otherwise, remove the top card and place it at the back.
	    Integer secondCard = deck.removeFirst();
	    deck.addLast(secondCard);

	    System.out.println("Remaining deck: "+deck);

	}
    }



    public void setupDeck(int n) {
    	//creates a new deck of size 0
    	CardDeck i = new CardDeck(0);
    	//makes the initial first element the highest number (n) in the deck
    	i.deck.addFirst(n);
    	//for loop that iterates for n-1 times
    	for(int j=n-1; j>=1; j--) {
    		//adds the last element of the deck to the front
    		//(on the first iteration, will not change anything as n is both head and tail)
    		i.deck.addFirst(i.deck.getLast());
    		//removes the last element from deck (so as to get rid of repeats)
    		i.deck.remove(i.deck.size()-1);
    		//adds element reflective of count number to front of deck
    		i.deck.addFirst(j);
    	}
    	//allows deck from main to refer to created deck
    	this.deck = i.deck;
    }



    public static void main(String args[]) {
	// this is just creating a deck with cards in increasing order, and running the trick. 
	CardDeck d = new CardDeck(10);
	d.runTrick();

	// this is calling the method you are supposed to write, and executing the trick.
	CardDeck e = new CardDeck(0);
	e.setupDeck(10);
	e.runTrick();
    }
}

    