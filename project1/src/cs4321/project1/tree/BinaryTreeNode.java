package cs4321.project1.tree;

/**
 * Second-level abstract class for a node in an expression tree. Contains logic
 * to handle the left and right child pointers.
 * 
 * @author Lucja Kot
 */
public abstract class BinaryTreeNode extends TreeNode {

	private TreeNode leftChild;
	private TreeNode rightChild;

	public BinaryTreeNode(TreeNode left, TreeNode right) {
		// both set in constructor as won't change
		this.leftChild = left;
		this.rightChild = right;
	}

	/**
	 * Getter for left child
	 * 
	 * @return left child node
	 */
	public TreeNode getLeftChild() {
		return leftChild;
	}

	/**
	 * Getter for right child
	 * 
	 * @return right child node
	 */
	public TreeNode getRightChild() {
		return rightChild;
	}
}
