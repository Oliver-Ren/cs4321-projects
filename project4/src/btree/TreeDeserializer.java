package btree;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int rootId;						// the address of the root
	private int numOfLeaves;				// The number of leaves in the tree
	private int order;						// The order of the tree.
	
	/**
	 * Constructs the the deserializer using the given indexFile.
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
