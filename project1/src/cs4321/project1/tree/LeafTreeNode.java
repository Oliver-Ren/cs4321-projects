package cs4321.project1.tree;

import cs4321.project1.TreeVisitor;

/**
 * Class for a leaf node in the tree, i.e. one that holds a numeric value
 * 
 * @author Lucja Kot
 */

public class LeafTreeNode extends TreeNode {

	protected double data; // value stored in node

	public LeafTreeNode(double data) {
		this.data = data; // set in constructor as will never change
	}

	/**
	 * Getter for value
	 * 
	 * @return numeric value in node
	 */
	public double getData() {
		return data;
	}

	/**
	 * Method for accepting visitor; just calls back visitor, logic of traversal
	 * will be handled in visitor method (to allow for different traversals, e.g
	 * preorder, postorder, inorder etc.)
	 * 
	 * @param visitor
	 *            visitor to be accepted
	 */
	@Override
	public void accept(TreeVisitor visitor) {
		visitor.visit(this);

	}
}
