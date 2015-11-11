package btree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import util.Tuple;
import nio.BinaryTupleReader;

/**
 * build a b+tree using the input sorted file
 * @author Mingyuanh
 *
 */

public class BPlusTree {
	
	File file; // input file 
	BinaryTupleReader tr;
	String name;
	int order;
	int position;
	int currPageId;
	int currTupleId;
	boolean isClust;
	ArrayList<DataEntry> dataEntries; // dataentries for creating leaf nodes
	ArrayList<LeafNode> leafLayer; // leaflayer that stores all the leaf nodes
	/**
	 * constructor
	 * @param file: the input file used for construct the tree
	 * @param name: the current table name
	 * @param position: the column index for the index 
	 * @param order: the half capacity of entries in each node, 
	 * say d is the order, then capacity of each node is 2d
	 *   
	 * @throws IOException 
	 */
	public BPlusTree(File file,String name,int position, int order,boolean isClust) throws IOException{
		this.file = file;
		tr = new BinaryTupleReader(file);
		this.position = position;
		this.name = name;
		this.order = order;
		currPageId = 0;
		currTupleId = 0;
		this.isClust = isClust;
		if(isClust){
			genClustDataEntries();
		} else {
			////////////
		}
		
		createLeafLayer();
	}
	/**
	 * method to create all the data entries which would store in leaf nodes later
	 * @throws IOException 
	 * 
	 */
	public void genClustDataEntries() throws IOException{
		// generate all the data entries
		dataEntries = new ArrayList<DataEntry>();
		ArrayList<Tuple> tps = tr.getNextPage();	
		int key = tps.get(0).cols[position];
		ArrayList<Rid> rids = new ArrayList<Rid>();
		rids.add(new Rid(currPageId,currTupleId));
		while(tps != null){
			
			currTupleId++;
			if(key!=tps.get(currTupleId).cols[position]){
				dataEntries.add(new DataEntry(key,rids));
				key = tps.get(currTupleId).cols[position];
				rids = new ArrayList<Rid>();
				rids.add(new Rid(currPageId,currTupleId));
			}
			if(currTupleId >= tps.size()){
				tps = tr.getNextPage();
				currPageId++;
				currTupleId = -1;
			}
		}
		dataEntries.add(new DataEntry(key,rids));
	}
	public void createLeafLayer(){
		if(dataEntries == null){
			
		}
		int capacity = 2 * order;// the total entries in each node
		
	}
}
