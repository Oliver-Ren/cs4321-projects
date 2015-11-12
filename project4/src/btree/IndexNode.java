package btree;

import java.util.ArrayList;

public class IndexNode extends TreeNode {
	
	ArrayList<TreeNode> children;
	ArrayList<Integer> keys;
	int min; //the minimum key value for its parent
	
	public IndexNode(int order, ArrayList<Integer> keys,
			ArrayList<TreeNode> children) {
		
		super(order);
		this.children = new ArrayList<TreeNode>(children);
		this.keys = new ArrayList<Integer>(keys);
		min = children.get(0).getMin();
	}

	@Override
	public int getMin() {
		// TODO Auto-generated method stub
		return min;
	}
	
	
	
	
	
	
}
