package btree;

import java.util.ArrayList;

public class LeafNode extends TreeNode {
	
	ArrayList<DataEntry> dataEntries;
	
	public LeafNode(int order, ArrayList<Integer> keys,
			ArrayList<DataEntry> dataEntries) {
		super(order, keys);
		// TODO Auto-generated constructor stub
		this.dataEntries = new ArrayList<DataEntry>(dataEntries);
	}

}
