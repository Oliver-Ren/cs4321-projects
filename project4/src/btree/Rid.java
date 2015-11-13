package btree;
/**
 * This method wraps the page id and the tuple id for
 * a specifc record id in the leafnode
 * @author Mingyuanh
 */
public class Rid {
	int pageId;
	int tupleId;
	
	public Rid(int pageId, int tupleId){
		this.pageId = pageId;
		this.tupleId  = tupleId;
	}
	
	/**
	 * Returns the string representation of this record id.
	 */
	@Override
	public String toString() {
		return "(" + pageId + "," + tupleId + ")";
	}
	
	public int getPageId() {
		return pageId;
	}
	
	public int getTupleid() {
		return tupleId;
	}
}	
