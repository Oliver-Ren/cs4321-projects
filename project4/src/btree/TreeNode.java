package btree;

import java.util.ArrayList;
import java.util.List;

public abstract class TreeNode {
	ArrayList<Integer> keys;
	int order;
	
	public TreeNode(int order,ArrayList<Integer> keys){
		this.order = order;
		this.keys = new ArrayList<Integer>(keys);
		
	}
}
