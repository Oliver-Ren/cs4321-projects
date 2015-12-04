package btree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.print.attribute.standard.Finishings;


/**
 * The <tt>TreeDeserializer</tt> class represents the component
 * which deserializes the B+ tree in the index file into the memory.
 * 
 * @author Chengxiang Ren (cr486)
 *
 */
public class TreeDeserializer {
	private File file;						// the file reading
	private static final int B_SIZE = 4096;	// the size of the buffer page
	private static final int INT_LEN = 4;	// the bytes a integer occupies
	private static final int LEAF_FLAG = 0; // the flag indicating this is leaf
	private static final int IDX_FLAG = 1;  // the flag indicating this is index.
	private static final int HEADER_ID = 0; // the the page id of header page.
	private static final int FIRST_LEAF = 1;// the id of the first leaf page.
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int rootId;						// the address of the root
	private int numOfLeaves;				// The number of leaves in the tree
	private int order;						// The order of the tree.
	private Integer lowKey;					// The low key of the search range.
	private Integer highKey;				// The high key of the search range.
	private LeafNode currLeaf;				// Current leaf node.
	private int currLeafPageAddr;			// Current 
	private int dEntryPtr;				    // the pointer for dataEntry;
	private int	ridPtr;						// The pointer for record id.
	private boolean finished;				// is finished for get next rid.
	
	/**
	 * Constructs the the deserializer using the given indexFile. 
	 * This constructor is used for deserialiing the full tree.
	 * 
	 * @param indexFile which stores the serialized tree index.
	 * @throws FileNotFoundException 
	 */
	public TreeDeserializer(File indexFile) throws FileNotFoundException {
		this.file = indexFile;
		fc = new FileInputStream(file).getChannel();
		buffer = ByteBuffer.allocate(B_SIZE);
		try {
			readHead();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructs the the deserializer using the given indexFile and the given
	 * range specified by the lowKey and highKey.
	 * 
	 * @param indexFile which stores the serialized tree index.
	 * @param lowKey the lower bound (inclusive), no limit if set to null.
	 * @param highKey the higher bound (exclusive), no limit if set to null.
	 * @throws FileNotFoundException 
	 */
	public TreeDeserializer(File indexFile, Integer lowKey, Integer highKey) throws FileNotFoundException {
		this(indexFile);
		this.lowKey = lowKey;
		this.highKey = highKey;
		dEntryPtr = 0;
		ridPtr = 0;
		currLeafPageAddr = FIRST_LEAF;
		finished = false;
		moveToStartLeafPage();
	}
	
	// locate to the starting leaf page using the given low key.
	private void moveToStartLeafPage() {
		try {
			if (lowKey == null) {
				currLeaf = (LeafNode) dsNode(currLeafPageAddr);
			} else {
				currLeaf = traverseToStartLeaf();
				while (currLeaf != null 
						&& currLeaf.dataEntries.get(dEntryPtr).key < lowKey) {
					dEntryPtr++;
					if (dEntryPtr >= currLeaf.dataEntries.size()) {
						currLeaf = getNextLeafNode();
						dEntryPtr = 0;
					}
				}
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private LeafNode traverseToStartLeaf() throws IOException {
		// precondition: lowKey is not null
		if (lowKey == null) {
			throw new IllegalArgumentException();
		}
		fetchPage(rootId);
		IndexNode root = dsINode();
		TreeNode curr = root;
		int pageAddr = FIRST_LEAF;
		while (curr instanceof IndexNode) {
			IndexNode currIndexNode = (IndexNode) curr;
			int pageIdx = Collections.binarySearch(currIndexNode.keys, lowKey + 1);
			pageIdx = pageIdx >= 0 ? pageIdx : -(pageIdx + 1);
			pageAddr = currIndexNode.address.get(pageIdx);
			System.out.println("pageaddr: " + pageAddr);
			curr = dsNode(pageAddr);
		}
		currLeafPageAddr = pageAddr;
		return (LeafNode) curr;
	}
	
	/**
	 * Returns the next record id in the specified range, null if no more 
	 * record id to return.
	 * @return Rid the record id.
	 * 				null if reached the end of the range.
	 * @throws IOException 
	 */
	public Rid getNextRid() throws IOException {
		if (finished || currLeaf == null) return null;
		// Need to go to the next data entry.
		if (ridPtr >= currLeaf.dataEntries.get(dEntryPtr).rids.size()) {
			dEntryPtr++;
			ridPtr = 0;
		}
		
		// Need to go to the next leaf page.
		if (dEntryPtr >= currLeaf.dataEntries.size()) {
			currLeaf = getNextLeafNode();
			// at the end of the leaf page.
			if (currLeaf == null) {
				finished = true;
				return null;
			}
			dEntryPtr = 0;
		}
		
		// now dEntryPtr is valid and ridPtr is valid.
		// check if it is within the right bound.
		if (highKey != null && currLeaf.dataEntries.get(dEntryPtr).key >= highKey) {
			finished = true;
			return null;
		}
		
		// good to go.
		return currLeaf.dataEntries.get(dEntryPtr).rids.get(ridPtr++);
	}
	
	
	private LeafNode getNextLeafNode() throws IOException {
		currLeafPageAddr++;
		if (currLeafPageAddr > 0 && currLeafPageAddr <= numOfLeaves) {
			return (LeafNode) dsNode(currLeafPageAddr);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the number of leaf nodes.
	 * 
	 * @return number of leaf nodes.
	 */
	public int getNumOfLeaves() {
		return numOfLeaves;
	}
	
	/**
	 * Dump the index file into human readable format.
	 * 
	 * @param printer The printstream specified.
	 */
	public void dump(PrintStream printer) {
		Queue<TreeNode> nodeQueue = new LinkedList<TreeNode>();
		try {
			fetchPage(rootId);
			IndexNode root = dsINode();
			printer.println("Header Page info: tree has order "
					+ order +", a root at address " + rootId + " and "
					+ numOfLeaves + " leaf nodes ");
			printer.println();
			printer.print("Root node is: ");
			printer.println(root);
			
			for (Integer addr : root.address) {
				nodeQueue.offer(dsNode(addr));
			}
			
			while (!nodeQueue.isEmpty()) {
				int size = nodeQueue.size();
				if (nodeQueue.peek() instanceof IndexNode) {
					printer.println("---------Next layer is index nodes---------");
				} else {
					printer.println("---------Next layer is leaf nodes---------");
				}
				for (int i = 0; i < size; i++) {
					TreeNode curr = nodeQueue.poll();			
					printer.println(curr);
					if (curr instanceof IndexNode) {
						for (Integer addr : ((IndexNode) curr).address) {
							nodeQueue.offer(dsNode(addr));
						}
					} 
				    //  ==== dubug====
//					else {
//						
//						LeafNode lf  = (LeafNode)curr;
//						if(lf.getMin() >= 9177) {
//							System.out.println("Curr node is " + lf.toString());
//						}
//						
//					}
					//=========================================
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	// deserilize the node of the given address.
	private TreeNode dsNode(int addr) throws IOException {
		fetchPage(addr);
		// condition of leaf node.
		if (addr > 0 && addr <= numOfLeaves) {
			return dsLNode();
		} else {
			return dsINode();
		}
	}
	
	// deserialize the leaf node.
	private LeafNode dsLNode() {
		int flag = buffer.getInt();
		int numOfDEntries = buffer.getInt();
		List<DataEntry> dataEntries = new ArrayList<DataEntry>();
		for (int i = 0; i < numOfDEntries; i++) {
			int key = buffer.getInt();
			int ridSize = buffer.getInt();
			List<Rid> rids = new ArrayList<Rid>();
			for (int j = 0; j < ridSize; j++) {
				int pageId = buffer.getInt();
				int tupleId = buffer.getInt();
				rids.add(new Rid(pageId, tupleId));
			}
			dataEntries.add(new DataEntry(key, rids));
		}
		return new LeafNode(order, dataEntries);
	}

	// deserialize the index node.
	private IndexNode dsINode() {
		int flag = buffer.getInt();
		int numOfKeys = buffer.getInt();
		List<Integer> keys = new ArrayList<Integer>();
		for (int i = 0; i < numOfKeys; i++) {
			keys.add(buffer.getInt());
		}
		List<Integer> addresses = new ArrayList<Integer>();
		for (int i = 0; i < numOfKeys + 1; i++) {
			addresses.add(buffer.getInt());
		}
		return new IndexNode(order, keys, addresses);
	}
		
	private void fetchPage(int pageId) throws IOException {
		eraseBuffer();
		long position = B_SIZE * (long) pageId;
		fc.position(position);
		fc.read(buffer);
		buffer.flip();
	}

	
	// read and store information in the head.
	private void readHead() throws IOException {
		fetchPage(HEADER_ID);
		rootId = buffer.getInt();
		numOfLeaves = buffer.getInt();
		order = buffer.getInt();
		eraseBuffer();
	}
	
	// Helper method that erases the buffer by filling zeros.
	private void eraseBuffer() {
		buffer.clear();
		// fill with 0 to clear the buffer
		buffer.put(new byte[B_SIZE]);
		buffer.clear();
	}
	
	

}
