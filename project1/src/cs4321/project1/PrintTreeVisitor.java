package cs4321.project1;

import cs4321.project1.tree.*;

/**
 * Visitor to pretty-print a tree expression, fully parenthesized.
 * 
 * @author Lucja Kot
 */
public class PrintTreeVisitor implements TreeVisitor {

	private String result; // running string representation

	public PrintTreeVisitor() {
		result = "";
	}

	/**
	 * Method to get the finished string representation when visitor is done
	 * 
	 * @return string representation of the visited tree
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Visit method for leaf node; just concatenates the numeric value to the
	 * running string
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(LeafTreeNode node) {
		result += node.getData();

	}

	/**
	 * Visit method for unary minus node; recursively visit subtree and wraps
	 * result in parens with unary -
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(UnaryMinusTreeNode node) {
		result += "(-";
		node.getChild().accept(this);
		result += ")";

	}

	/**
	 * Visit method for addition node based on inorder tree traversal
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(AdditionTreeNode node) {
		result += "(";
		node.getLeftChild().accept(this);
		result += "+";
		node.getRightChild().accept(this);
		result += ")";
	}

	/**
	 * Visit method for multiplication node based on inorder tree traversal
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(MultiplicationTreeNode node) {
		result += "(";
		node.getLeftChild().accept(this);
		result += "*";
		node.getRightChild().accept(this);
		result += ")";

	}

	/**
	 * Visit method for subtraction node based on inorder tree traversal
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(SubtractionTreeNode node) {
		result += "(";
		node.getLeftChild().accept(this);
		result += "-";
		node.getRightChild().accept(this);
		result += ")";

	}

	/**
	 * Visit method for division node based on inorder tree traversal
	 * 
	 * @param node
	 *            the node to be visited
	 */
	@Override
	public void visit(DivisionTreeNode node) {
		result += "(";
		node.getLeftChild().accept(this);
		result += "/";
		node.getRightChild().accept(this);
		result += ")";

	}

}
