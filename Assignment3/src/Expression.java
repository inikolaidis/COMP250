import java.io.*;
import java.util.*;


public class Expression {
	static String delimiters="+-*%()";
	//string created to specify the operators (to uninclude parentheses which are a special case)
	static String ops="+-*%";
	
    public static Stack<Integer> OperandStack = new Stack<Integer>();
    
    public static Stack<Character> OperatorStack = new Stack<Character>();
    
    public static int newvalue = 0;
    
    //boolean method allows for the 'prioritization' of operations - required otherwise we run into issues when multiple operations are within parentheses
    public static boolean charLessThanTop(String newOp, char topOp) {
    	if ((newOp.charAt(0) == '+') && (topOp == '(')) { return true; }
    	if ((newOp.charAt(0) == '-') && (topOp == '(')) { return true; }
    	if ((newOp.charAt(0) == '*') && (topOp == '(')) { return true; }
    	if ((newOp.charAt(0) == '%') && (topOp == '(')) { return true; }
    	else { return false; }
    }
	
    //processing method performs operation
	public static void processing() {
		
		int newvalue = 0;
		int value1 = OperandStack.pop();
		char operator = OperatorStack.pop();
		int value2 = OperandStack.pop();
		//series of if statements perform the operator function required 
		if (operator == '+') {
			newvalue = value1 + value2;
		}
		else if (operator == '-') {
			newvalue = value2 - value1;
		}
		else if (operator == '*') {
			newvalue = value1 * value2;
		}
		else if (operator == '%') {
			newvalue = value2 / value1;
		}
		
		OperandStack.push(newvalue);
	}
	
	
	// Returns the value of the arithmetic Expression described by expr
	// Throws an exception if the Expression is malformed
	static Integer evaluate(String expr) throws Exception {
	    /* The code below gives you an example of utilization of the
	     * StringTokenizer class to break the Expression string into
	     * its components */
		
		//start from empty newvalue, operand, and operator stacks
		int newvalue = 0;
		OperandStack.clear();
		OperatorStack.clear();

	    
	    StringTokenizer st = new StringTokenizer( expr , delimiters , true );    
        
	    /* This is just an example of how to use the StringTokenizer */
	    while ( st.hasMoreTokens() ) {
	    	String element = st.nextToken();
		
		
	    	//pushes operand onto OperandStackck.
	    	if (delimiters.contains(element) != true) {
	    		int convertedOperand = Integer.parseInt(element);
	    		OperandStack.push(convertedOperand);
	    	}
	    	
	    	//pushes operator onto an empty OperatorStack
	    	else if (ops.contains(element) && OperatorStack.empty()) {
	    		char convertedOperator = element.charAt(0);
	    		OperatorStack.push(convertedOperator);
			
	    	}
	    	
	    	//pushes operator onto a non-empty OperatorStack (by calling charLessThanTop method)
	    	else if (ops.contains(element) && charLessThanTop(element, OperatorStack.peek())) {
	    		char convertedOperator = element.charAt(0);
	    		OperatorStack.push(convertedOperator);
	    	}
	    	
	    	//pushes ( onto OperatorStack
	    	else if (element.charAt(0) == '(') {
	    		char convertedOperator = element.charAt(0);
	    		OperatorStack.push(convertedOperator);
	    	}

	    	//if the element is ), calls upon processing method until ( is encountered
	    	//then pops OperatorStack
	    	else if (element.charAt(0) == ')') {
	    		
	    		//int newvalue = 0;
	    		while (OperatorStack.peek() != '(') {
	    			processing();
	    		}
	    		OperatorStack.pop();
			
	    	}
	    	
	    	//otherwise, calls upon processing method and puts updated operators/results of calculations
	    	//into the OperatorStack
	    	else {
	    		processing();
	    		char convertedOperator = element.charAt(0);
	    		OperatorStack.push(convertedOperator);
	    		
	    	}
		}
	    
	    //while OperatorStack is not empty, continues processing
    	while (OperatorStack.empty() != true) {
    		processing();
    	}
    
    return OperandStack.peek();
	    //return null;
	}	
		
	public static void main(String args[]) throws Exception {
		String line;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
                                      	                        
		do {
			line=stdin.readLine();
			if (line.length()>0) {
				try {
					Integer x=evaluate(line);
					System.out.println(" = " + x);
				}
				catch (Exception e) {
					System.out.println("Malformed Expression: "+e);
				}
			}
		} while (line.length()>0);
	}
}