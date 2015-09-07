package cs4321.project1.tree;

import cs4321.project1.TreeVisitor;

/**
 * Top-level abstract class for a node in an expression tree.
 * 
 * @author Lucja Kot
 */

public abstract class TreeNode {

	/**
	 * Abstract method for accepting visitor
	 * 
	 * @param visitor
	 *            visitor to be accepted
	 */
	public abstract void accept(TreeVisitor visitor);
}
