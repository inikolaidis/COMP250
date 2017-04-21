import java.lang.Math;

/*********************************************************/
/* NAME:           ISABELLA NIKOLAIDIS                   */
/* STUDENT ID:     260684656                             */
/*********************************************************/

/* This class stores and manipulates very large non-negative integer numbers 
   The digits of the number are stored in an array of bytes. */
class LargeInteger {

    /* The digits of the number are stored in an array of bytes. 
       Each element of the array contains a value between 0 and 9. 
       By convention, digits[digits.length-1] correspond to units, 
       digits[digits.length-2] corresponds to tens, digits[digits.length-3] 
       corresponds to hundreds, etc. */

    byte digits[];


    
    /* Constructor that creates a new LargeInteger with n digits */
    public LargeInteger (int n) {
        digits= new byte[n];
    }

        
    /* Constructor that creates a new LargeInteger whose digits are those of the string provided */
    public LargeInteger (String s) {        
        digits = new byte[s.length()]; /* Note on "length" of arrays and strings: Arrays can be seen 
                                          as a class having a member called length. Thus we can access 
                                          the length of digits by writing digits.length
                                          However, in the class String, length is a method, so to access 
                                          it we need to write s.length() */

        for (int i=0;i<s.length();i++) digits[i] = (byte)Character.digit(s.charAt(i),10);
        /* Here, we are using a static method of the Character class, called digit, which 
           translates a character into an integer (in base 10). This integer needs to be 
           cast into a byte. ****/
    }


    /* Constructor that creates a LargeInteger from an array of bytes. Only the bytes  
       between start and up to but not including stop are copied. */
    public LargeInteger (byte[] array, int start, int stop) {
        digits = new byte[stop-start];
        for (int i=0;i<stop-start;i++) digits[i] = array[i+start];
    }


    /* This method returns a LargeInteger where eventual leading zeros are removed. 
       For example, it turns 000123 into 123. Special case: it turns 0000 into 0. */
    public LargeInteger removeLeadingZeros() {
        if (digits[0]!=0) return this;
        int i = 1;
        while (i<digits.length && digits[i]==0) i++;
        if (i==digits.length) return new LargeInteger("0");
        else return new LargeInteger(digits,i,digits.length);
    } // end of removeLeadingZeros
   

    /* This methods multiplies a given LargeInteger by 10^nbDigits, simply by shifting 
       the digits to the left and adding nbDigits zeros at the end */
    public LargeInteger shiftLeft(int nbDigits) {
        LargeInteger ret = new LargeInteger( digits.length + nbDigits );
        for (int i = 0 ; i < digits.length ; i++) ret.digits[ i ] = digits[ i ];
        for (int i = 0; i <  nbDigits; i++) ret.digits[ digits.length + i ] = 0;
        return ret;
    } // end of shiftLeft


      /* Returns true if the value of this is the same as the value of other */
    public boolean equals (LargeInteger other) {
        if ( digits.length != other.digits.length ) return false;
        for (int i = 0 ; i < digits.length ;i++ ) {
            if ( digits[i] != other.digits[i] ) return false;
        }
        return true;
    } // end of equals


      /* Returns true if the value of this is less than the value of other ****/
    public boolean isSmaller (LargeInteger other) {
        if ( digits.length > other.digits.length ) return false;
        if ( digits.length < other.digits.length ) return true;
        for (int i = 0 ; i < digits.length ; i++ ) {
            if ( digits[i] < other.digits[i] ) return true;
            if ( digits[i] > other.digits[i] ) return false;
        }
        return false;
    } // end of isSmaller
    


    /* This method adds two LargeIntegers: the one on which the method is 
       called and the one given as argument. The sum is returned. The algorithms 
       implemented is the normal digit-by-digit addition with carry. */
    /*B: 'other' will be added to the LargeInteger on which the method add is called, so in truth a.add(b) is a.add(other)*/
    LargeInteger add(LargeInteger other) {
    	/*B: int size declared, Math.max returns the largest of two numbers - here we are using it to optimize
    	 * */
        int size = Math.max( digits.length,other.digits.length );

        /* The sum can have at most one more digit than the two operands */
        LargeInteger sum = new LargeInteger( size + 1 ); 
        byte carry = 0;

        for (int i = 0; i < size + 1 ;i++) {
            // sumColumn will contain the sum of the two digits at position i plus the carry
            byte sumColumn = carry; 
            if ( digits.length - i  - 1 >= 0) sumColumn += digits[ digits.length - i - 1 ];
            if (other.digits.length - i - 1  >= 0) sumColumn += other.digits[ other.digits.length - i - 1 ];
            sum.digits[ sum.digits.length - 1 - i ] = (byte)( sumColumn % 10 ); // The i-th digit in the sum is sumColumn mod 10
            carry = (byte)( sumColumn / 10 );          // The carry for the next iteration is sumColumn/10
        }        
        return sum.removeLeadingZeros();
    } // end of add



    /* This method subtracts the LargeInteger other from that from where the method is called.
       Assumption: the argument other contains a number that is not larger than the current number. 
       The algorithm is quite interesting as it makes use of the addition code.
       Suppose numbers X and Y have six digits each. Then X - Y = X + (999999 - Y) - 1000000 + 1.
       It turns out that computing 999999 - Y is easy as each digit d is simply changed to 9-d. 
       Moreover, subtracting 1000000 is easy too, because we just have to ignore the '1' at the 
       first position of X + (999999 - Y). Finally, adding one can be done with the add code we already have.
       This tricks is the equivalent of the method used by most computers to do subtractions on binary numbers. ***/

    public LargeInteger subtract( LargeInteger other ) {
        // if other is larger than this number, simply return 0;
        if (this.isSmaller( other ) || this.equals( other ) ) return new LargeInteger( "0" );

        LargeInteger complement = new LargeInteger( digits.length ); /* complement will be 99999999 - other.digits */
        for (int i = 0; i < digits.length; i++) complement.digits[ i ] = 9;
        for (int i = 0; i < other.digits.length; i++) 
            complement.digits[ digits.length - i - 1 ] -= other.digits[other.digits.length - i -  1];

        LargeInteger temp = this.add( complement );     // add (999999- other.digits) to this
        temp = temp.add(new LargeInteger( "1" ));       // add one

        // return the value of temp, but skipping the first digit (i.e. subtracting 1000000)
        // also making sure to remove leading zeros that might have appeared.
        return new LargeInteger(temp.digits,1,temp.digits.length).removeLeadingZeros();
    } // end of subtract


    /* Returns a randomly generated LargeInteger of n digits */
    public static LargeInteger getRandom( int n ) {
        LargeInteger ret = new LargeInteger( n );
        for (int i = 0 ; i < n ; i++) {
            // Math.random() return a random number x such that 0<= x <1
            ret.digits[ i ]=(byte)( Math.floor( Math.random() * 10) );
            // if we generated a zero for first digit, regenerate a draw
            if ( i==0 && ret.digits[ i ] == 0 ) i--;
        }
        return ret;
    } // end of getRandom



    /* Returns a string describing a LargeInteger 17*/
    public String toString () {        

        /* We first write the digits to an array of characters ****/
        char[] out = new char[digits.length];
        for (int i = 0 ; i < digits.length; i++) out[ i ]= (char) ('0' + digits[i]);

        /* We then call a String constructor that takes an array of characters to create the string */
        return new String(out);
    } // end of toString




    /* This function returns the product of this and other by iterative addition */
	/*a.iterativeAddition(b) will multiply a x b through this process*/
    public LargeInteger iterativeAddition(LargeInteger other) {
    	LargeInteger counter = new LargeInteger("1");
    	LargeInteger counterIncreaser = new LargeInteger("1");
    	LargeInteger result = other;
    	/*this LargeInteger used as multiplier,
    	 * other LargeInteger used as multiplicand*/
    	while (this.equals(counter) != true) {
    		result = result.add(other);
    		/*the counter increases by one each time, as made possible by counterIncreaser,
    		 *  until it matches the LargeInteger fed to the method, 
    		 *  and then it breaks out of the while loop*/
    		counter = counter.add(counterIncreaser);
    	}
    	 
    	
    	 
        return result; // Remove this from your code.
    } // end of iterativeAddition



    /* This function returns the product of this and other by using the standard multiplication algorithm */
    public LargeInteger standardMultiplication(LargeInteger other) {
    	/*FOR REFERENCE
    	 * k = digits.length
    	 * n = other.digits.length
    	 * a = this
    	 * b = other
    	 * */
    	/*initialize variable total which will return the result*/
    	LargeInteger total = new LargeInteger("0");
    	
    	/*for loop which looks at the digits in LargeInteger other, 
    	 * starting from ones place digit and moving left with every iteration*/
        for (int i = other.digits.length - 1; i >= 0; i--) {
        	byte carry = 0;
        	/*int array created one digit larger than this.digits.length*/
        	int[] tempAdd = new int[this.digits.length+1];
        	/*for loop which looks at the digits in LargeInteger this
        	 * starting from ones place digit and moving left with each iteration*/
        	for (int j = this.digits.length - 1; j >= 0; j--) {
        		/*operation which carries out the multiplication of digits being
        		 * examined in this and other, as well as adding the carry value*/
        		int c = other.digits[i] * this.digits[j] + carry;
        		/*the remainder of this result (as obtained by modulo),
        		 * then added to int array*/
        		tempAdd[j+1] = (byte) (c % 10);
        		/*carry value updated*/
        		carry = (byte) (c/10);
        	}
        	/*last carryover digit added to the leftmost position of tempAdd*/
        	tempAdd[0] = carry;
        	/*for loop which iterates through tempAdd, converting it to String*/
        	String tempAddConverted = "";
        	for (int k = 0; k < tempAdd.length; k++) {
        		tempAddConverted = tempAddConverted + tempAdd[k];
        	}
        	/*passes string to constructor which creates LargeInteger*/
        	LargeInteger tempAddFinal = new LargeInteger(tempAddConverted);
        	
        	tempAddFinal = tempAddFinal.shiftLeft(other.digits.length - i -1);
        	total = total.add(tempAddFinal);
        	

        }

    	
    		
        return total; 
    } // end of standardMultiplication
                


    /* This function returns the product of this and other by using the basic recursive approach described 
       in the homework. Only use the built-in "*" operator to multiply single-digit numbers */
    public LargeInteger recursiveMultiplication( LargeInteger other ) {

        // left and right halves of this and number2                                                                                        
        LargeInteger leftThis, rightThis, leftOther, rightOther;
        LargeInteger term1,  term2,  term3,  term4, sum; // temporary terms                                                                      

        if ( digits.length==1 && other.digits.length==1 ) {
            int product = digits[0] * other.digits[0];
            return new LargeInteger( String.valueOf( product ) );
        }

        int k = digits.length;
        int n = other.digits.length;
        leftThis = new LargeInteger( digits, 0, k - k/2 );
        rightThis = new LargeInteger( digits, k - k/2, k );
        leftOther = new LargeInteger( other.digits, 0, n - n/2 );
        rightOther = new LargeInteger( other.digits, n - n/2, n );

        /* now recursively call recursiveMultiplication to compute the                    
           four products with smaller operands  */

        if ( n > 1 && k > 1 )  term1 = rightThis.recursiveMultiplication(rightOther );
        else term1 = new LargeInteger( "0" );

        if ( k>1 ) term2 = ( rightThis.recursiveMultiplication( leftOther ) ).shiftLeft( n/2 );
        else term2 = new LargeInteger( "0" );

        if ( n>1 ) term3 = ( leftThis.recursiveMultiplication( rightOther ) ).shiftLeft( k/2 );
        else term3 = new LargeInteger( "0" );

        term4 = ( leftThis.recursiveMultiplication( leftOther ) ).shiftLeft( k/2 + n/2 );

        sum = new LargeInteger( "0" );
        sum = sum.add( term1 );
        sum = sum.add( term2 );
        sum = sum.add( term3 );
        sum = sum.add( term4 );

        return sum;
    } // end of recursiveMultiplication             


    /* This method returns the product of this and other by using the faster recursive approach 
       described in the homework. It only uses the built-in "*" operator to multiply single-digit numbers */
    public LargeInteger recursiveFastMultiplication(LargeInteger other) {
    	/*FOR REFERENCE!
    	 * n = other.digits.length
    	 * k = digits.length
    	 * a = this
    	 * b = other*/
    	LargeInteger leftThis, rightThis, leftOther, rightOther;
    	LargeInteger term1, term2, term3, sum;
    	
    	if (other.digits.length < digits.length) {
    		return (other.recursiveFastMultiplication(this));
    		
    	}
    	if (digits.length == 1) {
    		return (this.standardMultiplication(other));
    	}
    	
    	int k = digits.length;
    	int n = other.digits.length;
    	/*FOR REFERENCE!
    	 * la = leftThis
    	 * ra = rightThis
    	 * lb = leftOther
    	 * rb = rightOther
    	 * */
        leftThis = new LargeInteger( digits, 0, k - k/2 );
        rightThis = new LargeInteger( digits, k - k/2, k );
        leftOther = new LargeInteger( other.digits, 0, n - n/2 );
        rightOther = new LargeInteger( other.digits, n - n/2, n );
        
        /*recursively calls recursiveFastMultiplication for sub-operations*/
        term1 = rightThis.recursiveFastMultiplication(rightOther);
        term2 = leftThis.recursiveFastMultiplication(leftOther);
        
        term3 = leftOther.shiftLeft(n/2 - k/2);
        /*computation of sum*/
        term3 = term3.add(rightOther);
        term3 = (leftThis.add(rightThis)).recursiveFastMultiplication(term3);
        LargeInteger temp = term2.shiftLeft(n/2 - k/2);
        term3 = term3.subtract(temp);
        term3 = term3.subtract(term1);
        sum = new LargeInteger("0");
        sum = term2.shiftLeft(k/2 + n/2);
        sum = sum.add(term3.shiftLeft(k/2));
        sum = sum.add(term1);
        
        return sum;
        
    }


}  // end of the LargeInteger class




// This class contains code to test the methods of the LargeInteger class. 
// Modify it as you wish to thorougly test each of the multiplication methods
// and to measure their running time.
// THE CODE IN THIS CLASS WILL NOT BE EVALUATED OR TESTED BY THE TAS.

public class TestLargeInteger {
    public static void main( String args[] ) {

        long startTime = System.nanoTime();
         for (int i = 0; i <= 10; i++) { 
        	LargeInteger multiplicand, multiplier;
        	multiplicand = LargeInteger.getRandom(8);
        	multiplier = LargeInteger.getRandom(8);
        	LargeInteger result = multiplicand.iterativeAddition(multiplier);
        	System.out.println(result);
         } 
        
        long currentTime = System.nanoTime();
        long totalTime = (currentTime - startTime)/10;
        
        System.out.println(totalTime);
        
        
    }
}
                
        
