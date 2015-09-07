package cs4321.project1.tree;

/**
 * Second-level abstract class for a node in an expression tree. Contains logic
 * to handle the (unique) child.
 * 
 * @author Lucja Kot
 */
public abstract class UnaryTreeNode extends TreeNode {

	private TreeNode child; // child node

	public UnaryTreeNode(TreeNode child) {
		this.child = child; // set in constructor as won't change
	}

	/**
	 * Getter for child
	 * 
	 * @return child node
	 */
	public TreeNode getChild() {
		return child;
	}

}
