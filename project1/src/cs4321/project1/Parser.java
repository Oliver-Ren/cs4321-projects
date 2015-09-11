package cs4321.project1;

import org.hamcrest.core.IsInstanceOf;

import cs4321.project1.list.DivisionListNode;
import cs4321.project1.tree.*;

/**
 * Class for a parser that can parse a string and produce an expression tree. To
 * keep the code simple, this does no input checking whatsoever so it only works
 * on correct input.
 * 
 * An expression is one or more terms separated by + or - signs. A term is one
 * or more factors separated by * or / signs. A factor is an expression in
 * parentheses (), a factor with a unary - before it, or a number.
 * 
 * @author Lucja Kot
 * @author Guantian Zheng (gz94)
 */
public class Parser {

	private String[] tokens;
	private int currentToken; // pointer to next input token to be processed

	/**
	 * Check if we have reached the end of tokens. 
	 * 
	 * @author Guantian Zheng (gz94)
	 */	
	public boolean isEnd() {
		return (currentToken >= tokens.length);
	}
	
	/**
	 * Check if the string is a number. In the context of our project if it starts 
	 * with a number than it is a double.
	 * 
	 * @author Guantian Zheng (gz94)
	 */	
	public boolean isNum(String s) {
		if (s.isEmpty()) return false;
		return (s.charAt(0) >= '0' && s.charAt(0) <= '9');
	}
	
	/**
	 * Get the current token and increment the pointer.
	 * 
	 * @author Guantian Zheng (gz94)
	 */	
	public String getCurTk() {
		if (isEnd()) return null;
		return tokens[currentToken++];
	}
	
	/**
	 * @precondition input represents a valid expression with all tokens
	 *               separated by spaces, e.g. "3.0 - ( 1.0 + 2.0 ) / - 5.0. All
	 *               tokens must be either numbers that parse to Double, or one
	 *               of the symbols +, -, /, *, ( or ), and all parentheses must
	 *               be matched and properly nested.
	 */
	public Parser(String input) {
		this.tokens = input.split("\\s+");
		currentToken = 0;
	}

	/**
	 * Parse the input and build the expression tree
	 * 
	 * @return the (root node of) the resulting tree
	 */
	public TreeNode parse() {
		return expression();
	}

	/**
	 * Parse the remaining input as far as needed to get the next factor
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode factor() {

		// TODO fill me in
		if (isEnd()) return null;
		
		String tk = getCurTk();
		
		if (tk.equals("(")) {
			return expression();
		}
		else if (tk.equals("-")) {
			return new UnaryMinusTreeNode(factor());
		}
		else if (isNum(tk)){
			return new LeafTreeNode(Double.parseDouble(tk));
		}
		
		return null;
	}

	/**
	 * Parse the remaining input as far as needed to get the next term
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode term() {

		// TODO fill me in
		if (isEnd()) return null;
		
		TreeNode left = factor(), right = null;
		
		while (!isEnd()) {
			String tk = getCurTk();
			if (!tk.equals("*") && !tk.equals("/")) {
				currentToken--;
				return left;
			}
			
			right = factor();
			
			if (tk.equals("*"))
				left = new MultiplicationTreeNode(left, right);
			else
				left = new DivisionTreeNode(left, right);
		}
		
		return left;
	}

	/**
	 * Parse the remaining input as far as needed to get the next expression
	 * 
	 * @return the (root node of) the resulting subtree
	 */
	private TreeNode expression() {

		// TODO fill me in
		if (isEnd()) return null;
		
		TreeNode left = term(), right = null;
		
		while (!isEnd()) {
			String tk = getCurTk();
			if (!tk.equals("+") && !tk.equals("-")) {
				// currentToken--;
				return left;
			}
			
			right = term();
			
			if (tk.equals("+"))
				left = new AdditionTreeNode(left, right);
			else
				left = new SubtractionTreeNode(left, right);
		}
		
		return left;
	}
}
