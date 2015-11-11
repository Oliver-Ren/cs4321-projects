package btree;

import java.util.ArrayList;

public class IndexNode extends TreeNode {
	
	ArrayList<TreeNode> children;
	ArrayList<Integer> keys;
	
	public IndexNode(int order, ArrayList<Integer> keys,
			ArrayList<TreeNode> children) {
		
		super(order);
		this.children = new ArrayList<TreeNode>(children);
		this.keys = new ArrayList<Integer>(keys);
	}
	
	
}