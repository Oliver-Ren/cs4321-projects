package util;

/**
 * This is the index information for a certain table.
 * @author chengxiang
 *
 */
public class IndexInfo {

	public String relt = "";
	public String attr = "";
	public boolean clust = false;
	public int order = 1;
	private int numOfLeaveNodes = -1;
	
	public IndexInfo(String r, String a, boolean c, int o) {
		relt = r;
		attr = a;
		clust = c;
		order = o;
	}	
	
	/**
	 * get the number of leaf nodes.
	 * @return num of leaf nodes.
	 */
	public int getNumOfLeafNodes() {
		return numOfLeaveNodes;
	}
	
	/**
	 * set the number of leaf nodes.
	 * @param num of leaf nodes.
	 */
	public void setNumOfLeafNodes(int num) {
		numOfLeaveNodes = num;
	}
	}
}
