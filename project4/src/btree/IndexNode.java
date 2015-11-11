package btree;

import java.util.ArrayList;

public class IndexNode extends TreeNode {
	
	ArrayList<TreeNode> children;
	
	public IndexNode(int order, ArrayList<Integer> keys,
			ArrayList<TreeNode> children) {
		super(order, keys);
		this.children = new ArrayList<TreeNode>(children);
		
	}
	
	
}
