package btree;

import java.util.ArrayList;
import java.util.List;

public class IndexNode extends TreeNode {
	
	List<TreeNode> children;
	List<Integer> keys;
	List<Integer> address; // the address the children
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
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("IndexNode with keys [");
		for (Integer key : keys) {
			sb.append(key + ", ");
		}
		sb.setLength(sb.length() - 2);
		sb.append("] and child addresses [");
//		for (Integer address : address) {
//			sb.append(key + ", ");
//		}
		sb.append("]\n");
		return sb.toString();
	}
	
//	@Override
//	public String toString() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("IndexNode with keys [");
//		for (Integer key : keys) {
//			sb.append(key + ", ");
//		}
//		sb.setLength(sb.length() - 2);
//		sb.append("] and child addresses [");
////		for (Integer address : address) {
////			sb.append(key + ", ");
////		}
//		sb.append("]\n");
//		return sb.toString();
//	}
	
	
	
}
