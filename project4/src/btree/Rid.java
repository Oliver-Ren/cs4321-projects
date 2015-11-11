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
	
}	
