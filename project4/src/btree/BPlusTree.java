package btree;

import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
import java.util.List;
//import java.util.Map;
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
	List<TreeNode> leafLayer; // leaflayer that stores all the leaf nodes
	TreeNode root;
	TreeSerializer ts;
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
	public BPlusTree(File file, int position, int order,File indexFile) throws IOException{
		this.file = file;
		tr = new BinaryTupleReader(file);
		leafLayer = new ArrayList<TreeNode>();
		this.position = position;
		this.indexFile = indexFile;
		this.order = order;
		this.capacity = 2 * order;
		this.ts = new TreeSerializer(indexFile);
		genUnclustDataEntries();
		
		createLeafLayer();

		List<TreeNode> layer = new ArrayList<TreeNode>(leafLayer);
		// debug 
		//PrintWriter writer = new PrintWriter("myIndexLayer.txt","UTF-8");
		//======
		while(layer.size()!=1){
			//System.out.println("I am in Bplus tree constructor");
			layer = createIndexLayer(layer, ts);
			// ==== debug ====			
//			for (TreeNode node : layer){
//				writer.println(node.toString());
//			}	
		}
		// debug 
	   //writer.close();
		//System.out.println(layer.size());
		//degub finish =============
		root = layer.get(0);
		ts.serialize(root);
		
		ts.finishSerialization(order);
		ts.close();
		tr.close();
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
				//System.out.println("Current min is " + node.getMin());
				
				leafLayer.add(node);
				nodeEntries.clear();
				cnt = 0;
			} 
				nodeEntries.add(dataEntries.get(i));
				cnt++;
		
		}
		if(nodeEntries.size()!=0){
			//System.out.println(" i am in create leaf layer,check underflow case");
			//check if the last node is underflow
			if(nodeEntries.size()>=order){
				//System.out.println("not under flow" );
				LeafNode node = new LeafNode(order,nodeEntries);
				leafLayer.add(node);
//				cnt=0;
//				nodeEntries.clear();
			} else { //underflow case
				//System.out.println("under flow" );
				if(leafLayer.size() == 0){ // only one node
					leafLayer.add(new LeafNode(order,nodeEntries));					
				} else {
					//System.out.println("needs to redistrubute");
					LeafNode secondLast = (LeafNode)leafLayer.remove(leafLayer.size()-1);
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
		
		// print leaflayer  debug 
//		try {
//			print();
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		
	}
	
	public void print() throws IOException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter ("mytestleaflayer.text","UTF-8");
		for (TreeNode tr: leafLayer) {
			 writer.println(tr.toString());
		}
		System.out.println("file created");
		writer.close();
	}
	/**
	 * generate the index layer according to the previous layer 
	 * @throws IOException 
	 */
	public List<TreeNode> createIndexLayer(List<TreeNode> preLayer,TreeSerializer ts) throws IOException{
		List<TreeNode> newLayer = new ArrayList<TreeNode>();		
			int cnt = 0;
			List<Integer> keys = new ArrayList<Integer>();
			List<TreeNode> children = new ArrayList<TreeNode>();
			List<Integer> address = new ArrayList<Integer>(); // list of address
			if(preLayer.size() <=capacity){ // only one node to construct
				for (int i = 0; i < preLayer.size(); i++){
					int ads = ts.serialize(preLayer.get(i));
					address.add(ads);
				}
				children.addAll(preLayer);
				for (int i = 1; i < preLayer.size(); i++){
					keys.add(preLayer.get(i).getMin());
				}
				newLayer.add(new IndexNode (order, keys,children , address));
				
			} else {
				
				for(int i = 0; i < preLayer.size(); i++){
					if (cnt == capacity){
						children.add(preLayer.get(i));
						int ads = ts.serialize(preLayer.get(i));
						address.add(ads);
						//add last key
						keys.add(preLayer.get(i).getMin());
						//create a index node
						IndexNode node = new IndexNode(order,keys,children,address);
						newLayer.add(node);
						//reset
						cnt = 0;
						keys.clear();
						children.clear();
						address.clear();
						// debug ======== 增加
						
						int remainNum = preLayer.size() - i - 1;
						//System.out.println("the remaining node num is " + remainNum);
						if(remainNum > (2 * order + 1) && remainNum < (3 * order + 2)) {
							int secLastNodeNum = remainNum/2;
							int LastNodeNum = remainNum - secLastNodeNum;
							
							// fill secLastNode
							for (int j = i + 1; j < i + 1 + secLastNodeNum; j++){
								ads = ts.serialize(preLayer.get(j));
								//System.out.println("my address is " + ads);
								address.add(ads);							
							}
							children.addAll(preLayer.subList(i+1, i + 1 + secLastNodeNum));
							for(TreeNode cd : children) {
								//System.out.println("my children is " + cd.getMin());
							}
							// add keys
							for (int j = i + 2; j < i+1+secLastNodeNum;j++){
								keys.add(preLayer.get(j).getMin());
								//System.out.println("my key " + preLayer.get(j).getMin() );
							}
							newLayer.add(new IndexNode (order,keys,children,address));
							keys.clear();
							children.clear();
							address.clear();
							//  fill LastNode
							for(int j = i + 1 + secLastNodeNum; j < preLayer.size(); j++) {
								ads = ts.serialize(preLayer.get(j));
								address.add(ads);
							}
							children.addAll(preLayer.subList(i+1+secLastNodeNum, preLayer.size()));
							//add keys
							for (int j = i+2+secLastNodeNum; j < preLayer.size();j++){
								keys.add(preLayer.get(j).getMin());
								//System.out.println("my last node key " + preLayer.get(j).getMin() );
							}
							newLayer.add(new IndexNode ( order, keys,children,address));
							//System.out.println("Curretnly new layer size is " + newLayer.size());
							keys.clear();
							children.clear();
							address.clear();
							break;
						}
						// debug =========
						
						continue;
					}
					
					if(cnt == 0){
						children.add(preLayer.get(i));	
						int ads = ts.serialize(preLayer.get(i));
						address.add(ads);
						cnt++;
					} else if (cnt < capacity ){
						//add key
						keys.add(preLayer.get(i).getMin());
						children.add(preLayer.get(i));
						int ads = ts.serialize(preLayer.get(i));
						address.add(ads);
						cnt++;
					} 
					
				}
				// check if there is a node left 
				if(keys.size()!=0) {
					newLayer.add(new IndexNode(order, keys,children,address));
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
