package btree;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Mingyuanh
 *
 */
public class LeafNode extends TreeNode {
	
	List<DataEntry> dataEntries;
	int min;
	public LeafNode(int order,
			List<DataEntry> dataEntries) {
		super(order);
		// TODO Auto-generated constructor stub
		this.dataEntries = new ArrayList<DataEntry>(dataEntries);
		min = dataEntries.get(0).key;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("LeafNode[\n");
		for (DataEntry data : dataEntries) {
			sb.append(data.toString() + "\n");
		}
		sb.append("]");
		return sb.toString();
	}

	@Override
	public int getMin() {
		// TODO Auto-generated method stub		
		return min;
	}
	
	

}
