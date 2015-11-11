package btree;

import java.util.ArrayList;
/**
 * Data entry is one element inside the leaf node,
 * and it contains a key and a list of record ids.
 * its format: <key, [<pid,tid>, <pid,tid>, ...]> 
 * @author Mingyuanh
 *
 */
public class DataEntry {
	int key;
	ArrayList<Rid> rids;
/*
 * constructor;	
 */
	public DataEntry(int key, ArrayList<Rid> rids){
		this.key = key;
		this.rids = new ArrayList<Rid>(rids);
	}
	
	
}
