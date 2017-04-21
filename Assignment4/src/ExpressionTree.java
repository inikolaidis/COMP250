import java.lang.Math.*;

class ExpressionTree {
    private String value;
    private ExpressionTree leftChild, rightChild, parent;
    
    ExpressionTree() {
        value = null; 
        leftChild = rightChild = parent = null;
    }
    
    // Constructor
    /* Arguments: String s: Value to be stored in the node
                  ExpressionTree l, r, p: the left child, right child, and parent of the node to created      
       Returns: the newly created ExpressionTree               
    */
    ExpressionTree(String s, ExpressionTree l, ExpressionTree r, ExpressionTree p) {
        value = s; 
        leftChild = l; 
        rightChild = r;
        parent = p;
    }
    
    /* Basic access methods */
    String getValue() { return value; }

    ExpressionTree getLeftChild() { return leftChild; }

    ExpressionTree getRightChild() { return rightChild; }

    ExpressionTree getParent() { return parent; }


    /* Basic setting methods */ 
    void setValue(String o) { value = o; }
    
    // sets the left child of this node to n
    void setLeftChild(ExpressionTree n) { 
        leftChild = n; 
        n.parent = this; 
    }
    
    // sets the right child of this node to n
    void setRightChild(ExpressionTree n) { 
        rightChild = n; 
        n.parent=this; 
    }
    

    // Returns the root of the tree describing the expression s
    // Watch out: it makes no validity checks whatsoever!
    ExpressionTree(String s) {
        // check if s contains parentheses. If it doesn't, then it's a leaf
        if (s.indexOf("(")==-1) setValue(s);
        else {  // it's not a leaf

            /* break the string into three parts: the operator, the left operand,
               and the right operand. ***/
            setValue( s.substring( 0 , s.indexOf( "(" ) ) );
            // delimit the left operand 2008
            int left = s.indexOf("(")+1;
            int i = left;
            int parCount = 0;
            // find the comma separating the two operands
            while (parCount>=0 && !(s.charAt(i)==',' && parCount==0)) {
                if ( s.charAt(i) == '(' ) parCount++;
                if ( s.charAt(i) == ')' ) parCount--;
                i++;
            }
            int mid=i;
            if (parCount<0) mid--;

        // recursively build the left subtree
            setLeftChild(new ExpressionTree(s.substring(left,mid)));
    
            if (parCount==0) {
                // it is a binary operator
                // find the end of the second operand.F13
                while ( ! (s.charAt(i) == ')' && parCount == 0 ) )  {
                    if ( s.charAt(i) == '(' ) parCount++;
                    if ( s.charAt(i) == ')' ) parCount--;
                    i++;
                }
                int right=i;
                setRightChild( new ExpressionTree( s.substring( mid + 1, right)));
        }
    }
    }


    // Returns a copy of the subtree rooted at this node... 2014
    ExpressionTree deepCopy() {
        ExpressionTree n = new ExpressionTree();
        n.setValue( getValue() );
        if ( getLeftChild()!=null ) n.setLeftChild( getLeftChild().deepCopy() );
        if ( getRightChild()!=null ) n.setRightChild( getRightChild().deepCopy() );
        return n;
    }
    
    // Returns a String describing the subtree rooted at a certain node.
    public String toString() {
        String ret = value;
        if ( getLeftChild() == null ) return ret;
        else ret = ret + "(" + getLeftChild().toString();
        if ( getRightChild() == null ) return ret + ")";
        else ret = ret + "," + getRightChild().toString();
        ret = ret + ")";
        return ret;
    } 


    // Returns the value of the expression rooted at a given node
    // when x has a certain value
    double evaluate(double x) {
    	//this = node we are examining, starts at root
    	
    	double outcome = 0;
    	
    	//if node is null, returns 0
    	if (this.equals(null)) {
    		return 0;
    	}
    	
    	//there are a bunch of things we need to check for --> is a value x?
    	//if statement encapsulating binary operators mult, add, minus
    	if (this.value.equals("add") || this.value.equals("mult") || this.value.equals("minus")) {
    		//we know there are two children
    		double tempVal1 = 0;
    		double tempVal2 = 0;
    		//recursive call to evaluate children of node
    		tempVal1 = this.getLeftChild().evaluate(x);
    		tempVal2 = this.getRightChild().evaluate(x);
    		//if statement to get sum of children
    		if (this.value.equals("add")) {
        		outcome = tempVal1 + tempVal2;
        	}
    		//if statement to get product of children
        	else if (this.value.equals("mult")) {
        		outcome = tempVal1 * tempVal2;
        	}
    		//if statement to get the difference of children
        	else if (this.value.equals("minus")) {
        		outcome = tempVal1 - tempVal2;
        	}
    		//returns result from one of the above if statements
    		return outcome;
    	}
    	//if statement encapsulating unary operators cos, sin, exp
    	else if (this.value.equals("cos") || this.value.equals("sin") || this.value.equals("exp")) {
    		//we know there is one child
    		double tempVal1 = 0;
    		//it will be a left child (no right children with no sibling on trees)
    		//recursive call to evaluate child of node
    		tempVal1 = this.getLeftChild().evaluate(x);
    		//if statement to get cos value of child
    		if (this.value.equals("cos")) {
    			outcome = Math.cos(tempVal1);
    		}
    		//if statement to get sin value of child
        	else if (this.value.equals("sin")) {
        		outcome = Math.sin(tempVal1);
        	}
    		//if statement to get exp value of child
        	else if (this.value.equals("exp")) {
        		outcome = Math.exp(tempVal1);
        	}
    		//returns result from one of the above if statements
    		return outcome;
    	}
    	//if statement encapsulating 'x' variable
    	else if (this.value.equals("x")) {
    		//replaces x with the value inputted for x, converts it to double
    		outcome = Double.parseDouble(String.valueOf(x));
    		return outcome;
    	}
    	//if statement encapsulating 'X' variable
    	else if (this.value.endsWith("X")) {
    		//replaces X with the value inputted for X, converts it to double
    		outcome = Double.parseDouble(String.valueOf(x));
    		return outcome;
    	}
    	//if statement accounting for numerical values
    	else if (isNumeric(this.value)) {
    		//converts value to double
    		outcome = Double.parseDouble(this.value);
    		return outcome;
    	}
    	return outcome;
    	
    	
    }
    
    //helper boolean method which checks if value at node is numeric
    public static boolean isNumeric(String str) {  
      try {  
        double d = Double.parseDouble(str);  
      }  
      //returns false if value is not valid numerical entity
      catch(NumberFormatException nfe) {  
        return false;  
      }  
      return true;  
    }
    
    ExpressionTree differentiate() {
    	//if node is null, returns null
    	if (this.equals(null)) {
    		return null;
    	}
    	//if node is 'x', result of differentiation is 1
    	if (this.value.equals("x")) {
    		//returns new ExpressionTree with 1 at node examined
    		return new ExpressionTree("1", null, null, new ExpressionTree()); 
    	}
    	//if node is 'X', result of differentiation is 1
    	else if (this.value.equals("X")) {
    		//returns new ExpressionTree with 1 at node examined
    		return new ExpressionTree("1", null, null, new ExpressionTree());
    	}
    	//if node is numeric, result of differentiation is 0
    	else if (isNumeric(this.value)) {
    		//returns new ExpressionTree with 0 at node examined
    		return new ExpressionTree("0", null, null, new ExpressionTree());
    	}
    	//if node is 'add'
    	else if(this.value.equals("add")) {
    		//recursively call differentiate method on children of node being examined
    		ExpressionTree tempVal1 = this.getLeftChild().differentiate();
    		ExpressionTree tempVal2 = this.getRightChild().differentiate();
    		//return new ExpressionTree with 'add' at main value, differentiated values of children at children positions
    		return new ExpressionTree ("add", tempVal1, tempVal2, new ExpressionTree());
    	}
    	//if node is 'minus'
    	else if(this.value.equals("minus")) {
    		//recursively call differentiate method on children of node being examined
    		ExpressionTree tempVal1 = this.getLeftChild().differentiate();
    		ExpressionTree tempVal2 = this.getRightChild().differentiate();
    		//return new ExpressionTree with 'minus' at main value, differentiated values of children at children positions
    		return new ExpressionTree ("minus", tempVal1, tempVal2, new ExpressionTree());
    	}
    	//if node is 'mult'
    	else if(this.value.equals("mult")) {
    		//recursively call differentiate method on children of node being examined
    	    ExpressionTree diffLeft = this.getLeftChild().differentiate();
    	    ExpressionTree diffRight = this.getRightChild().differentiate();
    	    //however, must then deepCopy the subtrees of both children to retain structure/values farther down the tree
    	    //deepCopy allows us to continue to differentiate subtrees without altering original tree (which would cause problems later)
    	    ExpressionTree cpyLeft = this.getLeftChild().deepCopy();
    	    ExpressionTree cpyRight = this.getRightChild().deepCopy();
    	    //returns new ExpressionTree of following structure:
    	    //'add' at main value 
    	    //with subtrees which have 'mult' at main node, recursively differentiated values of children at left child of subtree
    	    //and the deepCopy result as right child of subtree
    	    return new ExpressionTree ("add", 
                                         new ExpressionTree ("mult", diffLeft, cpyRight, new ExpressionTree()),
    	                                 new ExpressionTree ("mult", diffRight, cpyLeft, new ExpressionTree()),
    	                                 new ExpressionTree());
    	     }
    	//if node is 'sin'
    	else if(this.value.equals("sin")) {
    		//recursively call differentiate method on child of node being examined
    		ExpressionTree diffLeft = this.getLeftChild().differentiate();
    		//also deepCopy left child to retain structures/values farther down tree
    		ExpressionTree cpyLeft = this.getLeftChild().deepCopy();
    		//returns new ExpressionTree of following structure: 
    		//'mult' at main value 
    		//with subtree at left child with 'cos' at main node and deepCopy at left child
    		//differentiation of left child as right child
    		return new ExpressionTree ("mult", 
    										new ExpressionTree("cos", cpyLeft, null, new ExpressionTree()),
    										diffLeft,
    										new ExpressionTree());
    	}
    	//if node is 'cos'
    	else if(this.value.equals("cos")) {
    		//recursively call differentiate method on child of node being examined
    		ExpressionTree diffLeft = this.getLeftChild().differentiate();
    		//also deepCopy left child to retain structures/values farther down tree
    		ExpressionTree cpyLeft = this.getLeftChild().deepCopy();
    		//also require additional storedSin value for the differentiation equation
    		//sin at main node of expressionTree, with deepCopy of left as left child
    		ExpressionTree storedSin = new ExpressionTree("sin", cpyLeft, null, new ExpressionTree());
    		//returns new ExpressionTree of following structure:
    		//'minus' at main value
    		//'0' is main value of left subtree
    		//'mult' is main value of right subtree, with storedSin and differentiated left as children
    		return new ExpressionTree ("minus",
    										new ExpressionTree("0", null, null, new ExpressionTree()),
    										new ExpressionTree("mult", storedSin, diffLeft, new ExpressionTree()),
    										new ExpressionTree());
    	}
    	//if node is 'exp'
    	else if(this.value.equals("exp")) {
    		//recursively call differentiate method on child of node being examined
    		ExpressionTree diffLeft = this.getLeftChild().differentiate();
    		//deepCopy left child
    		ExpressionTree cpyLeft = this.getLeftChild().deepCopy();
    		//returns new ExpressionTree of following structure:
    		//'mult' at main value
    		//left subtree with 'exp' as main value, deepCopy of left child as child of exp
    		//right subtree with differentiated left child as value
    		return new ExpressionTree ("mult",
											new ExpressionTree("exp", cpyLeft, null, new ExpressionTree()),
											diffLeft,
											new ExpressionTree());
    	}
    	return new ExpressionTree();
    }
        
    
    public static void main(String args[]) {

        ExpressionTree e = new ExpressionTree("mult(x,add(add(2,x),cos(minus(x,4))))");
        System.out.println(e);                
        System.out.println(e.evaluate(1));                
        System.out.println(e.differentiate());       
        System.out.println(e.differentiate().evaluate(1));    
        System.out.println(e.evaluate(1));                 

 }
}
