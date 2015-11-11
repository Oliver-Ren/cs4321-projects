package btree;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author Mingyuanh
 *
 */
public class LeafNode extends TreeNode {
	
	ArrayList<DataEntry> dataEntries;
	
	public LeafNode(int order,
			ArrayList<DataEntry> dataEntries) {
		super(order);
		// TODO Auto-generated constructor stub
		this.dataEntries = new ArrayList<DataEntry>(dataEntries);
	}
	
	

}
