package btree;

import java.util.ArrayList;
import java.util.List;

public class IndexNode extends TreeNode {
	
	List<TreeNode> children;
	List<Integer> keys;
	int min; //the minimum key value for its parent
	
	public IndexNode(int order, List<Integer> keys,
			List<TreeNode> children) {
		
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
