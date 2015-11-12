package btree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
	int order;
	int capacity;// the total entries in each node
	int position;
	File indexFile; // output idex file 
	List<DataEntry> dataEntries; // dataentries for creating leaf nodes
	List<LeafNode> leafLayer; // leaflayer that stores all the leaf nodes
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
	public BPlusTree(File file,int position, int order,File indexFile) throws IOException{
		this.file = file;
		tr = new BinaryTupleReader(file);
		leafLayer = new ArrayList<LeafNode>();
		this.position = position;
		this.indexFile = indexFile;
		this.order = order;
		this.capacity = 2 * order;
		
		genUnclustDataEntries();
		
		createLeafLayer();
	}
	/**
	 * method to create all the data entries which would store in leaf nodes later
	 * @throws IOException 
	 * 
	 */
//	public void genClustDataEntries() throws IOException{
//		int currPageId = 0;
//		int currTupleId = 0;
//		// generate all the data entries
//		dataEntries = new ArrayList<DataEntry>();
//		ArrayList<Tuple> tps = tr.getNextPage();	
//		int key = tps.get(0).cols[position];
//		ArrayList<Rid> rids = new ArrayList<Rid>();
//		rids.add(new Rid(currPageId,currTupleId));
//		while(tps != null){
//			
//			currTupleId++;
//			if(key!=tps.get(currTupleId).cols[position]){
//				dataEntries.add(new DataEntry(key,rids));
//				key = tps.get(currTupleId).cols[position];
//				rids = new ArrayList<Rid>();
//				rids.add(new Rid(currPageId,currTupleId));
//			}
//			if(currTupleId >= tps.size()){
//				tps = tr.getNextPage();
//				currPageId++;
//				currTupleId = -1;
//			}
//		}
//		dataEntries.add(new DataEntry(key,rids));
//	}
	
	public void createLeafLayer(){
		if(dataEntries == null){
			throw new NullPointerException();
		}
		int cnt = 0;		
		// each sub dataentry list in each node
		List<DataEntry> nodeEntries = new ArrayList<DataEntry>();
		
		for(int i = 0; i< dataEntries.size(); i++){
			if(cnt == capacity){
				LeafNode node = new LeafNode(order,nodeEntries);
				leafLayer.add(node);
				nodeEntries.clear();
				cnt = 0;
			} 
				nodeEntries.add(dataEntries.get(i));
				cnt++;
		
		}
		if(nodeEntries.size()!=0){
			//check if the last node is underflow
			if(nodeEntries.size()>=order){
				LeafNode node = new LeafNode(order,nodeEntries);
				leafLayer.add(node);
//				cnt=0;
//				nodeEntries.clear();
			} else { //underflow case
				if(leafLayer.size() == 0){ // only one node
					leafLayer.add(new LeafNode(order,nodeEntries));					
				} else {
					LeafNode secondLast = leafLayer.remove(leafLayer.size()-1);
					//num of entries should put in seond last node
					int numOfEntry = (2*order + nodeEntries.size())/2;
					
					List<DataEntry> secondNodeEntries = 
							secondLast.dataEntries;
					
					List<DataEntry> lastNodeEntries = 
							secondNodeEntries.subList(numOfEntry,secondNodeEntries.size());
					lastNodeEntries.addAll(nodeEntries);
					
					secondNodeEntries = secondNodeEntries.subList(0, numOfEntry);
					
					secondLast = new LeafNode(order, secondNodeEntries);
					//add the second last
					leafLayer.add(secondLast);
					// add the last node
					leafLayer.add(new LeafNode(order,lastNodeEntries));				
				}
			}
		}
		
		
	}
	/**
	 * generate the index layer according to the previous layer 
	 */
	public List<TreeNode> genIndexLayer(List<TreeNode> preLayer){
		
		List<TreeNode> newLayer = new ArrayList<TreeNode>();
		int cnt = 0;
		List<Integer> keys = new ArrayList<Integer>();
		List<TreeNode> children = new ArrayList<TreeNode>();
		for(int i = 0; i < preLayer.size(); i++){
			if (cnt == capacity){
				children.add(preLayer.get(i));
				//add last key
				keys.add(preLayer.get(i).getMin());
				//create a index node
				IndexNode node = new IndexNode(order,keys,children);
				newLayer.add(node);
				//reset
				cnt = 0;
				keys.clear();
				children.clear();
			}
			
			if(cnt == 0){
				children.add(preLayer.get(i));	
				cnt++;
			} else if (cnt < capacity ){
				//add key
				keys.add(preLayer.get(i).getMin());
				children.add(preLayer.get(i));
				cnt++;
			} 
			
		}
		//check the last node
		if(keys.size() > order){
			IndexNode node = new IndexNode(order, keys, children);
			newLayer.add(node);
		} else {
			//the current lay only has one node
			if(keys.size() != 0){
				if(newLayer.size() == 0){
					IndexNode node = new IndexNode (order, keys, children);
					newLayer.add(node);
				}
			} else { // need redistribution
				IndexNode secondLast =(IndexNode)newLayer.remove(newLayer.size()-1);
				List<TreeNode>secLastChildren = secondLast.children;
				List<Integer> secLastKeys = secondLast.keys;
				int numOfKeys =(secLastChildren.size() + children.size())/2-1;
				// build keys for last node
				List<Integer> lastNodeKeys = secLastKeys.subList(numOfKeys, secLastKeys.size());
				lastNodeKeys.addAll(keys);
				// build children for last node
				
				
			}
		}
		
		
		
		return newLayer;
	}
	/**
	 * Generates the list of data entries from an unclustered relation.
	 */
	public void genUnclustDataEntries() {
		SortedMap<Integer, DataEntry> entryMap = new TreeMap<Integer, DataEntry>();
		try {
			// tuples in this page.
			ArrayList<Tuple> tps;
			int currPageId = 0;
			while ((tps = tr.getNextPage()) != null) {
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
		// all the data entries in the map. Since the TreeMap already have 
		// the entries in sorted order, we only can directly create the 
		// arraylist.
		dataEntries = new ArrayList<DataEntry>(entryMap.values());
	}
}
