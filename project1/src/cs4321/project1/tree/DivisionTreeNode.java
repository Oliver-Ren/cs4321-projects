package cs4321.project1.tree;

import cs4321.project1.TreeVisitor;

/**
 * Class for a binary node in the tree representing a division operation
 * 
 * @author Lucja Kot
 */
public class DivisionTreeNode extends BinaryTreeNode {

	public DivisionTreeNode(TreeNode left, TreeNode right) {
		super(left, right);

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
