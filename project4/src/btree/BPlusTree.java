package btree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import util.Tuple;
import nio.BinaryTupleReader;

/**
 * build a b+tree using the input sorted file
 * @author Mingyuan Huang (mh2239), Chengxiang Ren (cr486)
 *
 */

public class BPlusTree {
	
	File file; // input file 
	BinaryTupleReader tr;
	String name;
	int order;
	int capacity = 2 * order;// the total entries in each node
	int position;
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
		this.isClust = isClust;
		if(isClust){
			genClustDataEntries();
		} else {
			genUnclustDataEntries();
		}
		
		createLeafLayer();
	}
	/**
	 * method to create all the data entries which would store in leaf nodes later
	 * @throws IOException 
	 * 
	 */
	public void genClustDataEntries() throws IOException{
		int currPageId = 0;
		int currTupleId = 0;
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
			throw new NullPointerException();
		}
		int cnt = 0;		
		// for each sub dataentry list in each node
		ArrayList<DataEntry> nodeEntries = new ArrayList<DataEntry>();
		for(int i = 0; i< dataEntries.size(); i++){
			if(dataEntries.size()-i)
			
			if(cnt == capacity){
				LeafNode node = new LeafNode(order,nodeEntries);
				leafLayer.add(node);
				nodeEntries.clear();
				cnt = 0;
			} else {
				nodeEntries.add(dataEntries.get(i));
				cnt++;
			}
		}
		
	}
	
	/**
	 * Generates the list of data entries from an unclustered relation.
	 */
	public void genUnclustDataEntries() {
		SortedMap<Integer, DataEntry> entryMap = new TreeMap<Integer, DataEntry>();
		try {
			// tuples in this page.
			ArrayList<Tuple> tps;
			
			while ((tps = tr.getNextPage()) != null) {
				System.out.println("i am building" + currPageId);
				for (int currTupleId = 0; currTupleId < tps.size(); currTupleId++) {
					Tuple currTuple = tps.get(currTupleId);
					int key = currTuple.cols[position];
					if (entryMap.containsKey(key)) {
						DataEntry target = entryMap.get(key);
						target.rids.add(new Rid(currPageId, currTupleId));
					} else {
						DataEntry newEntry = new DataEntry(key, new ArrayList<Rid>());
						newEntry.rids.add(new Rid(currPageId, currTupleId));
						entryMap.put(key, newEntry);
					}
				}
				
				//finished one page, increment the page id.
				currPageId++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Now all the record ids are in the Map, the next step is to sort
		// all the data entries in the map, and partition the entries to 
		// different leaf pages.
		Collection<DataEntry> dataEntries = entryMap.values();
		
		for (DataEntry d : dataEntries) {
			System.out.println(d);
		}
		
		
		
	}
}
