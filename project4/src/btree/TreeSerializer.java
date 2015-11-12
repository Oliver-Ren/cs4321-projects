package btree;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * The <tt>TreeSerializer</tt> class represents the component
 * which serializes the B+ tree in the memory into a file representing
 * this tree.
 * <p>
 * The process the serialization is done node by node where each node
 * represents a page in the index file.
 * <p>
 * The serializer uses the iterator model which mean every time the 
 * serailize method 
 * 
 * @author Chengxiang Ren (cr486)
 *
 */
public class TreeSerializer {
	private File indexFile;						// the file reading
	private static final int B_SIZE = 4096;	// the size of the buffer page
	private static final int INT_LEN = 4;	// the bytes a integer occupies
	private static final int LEAF_FLAG = 0; // the flag indicating this is leaf
	private static final int IDX_FLAG = 1;  // the flag indicating this is index.
	private static final int HEADER_ID = 0; // the the page id of header page.
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int pageNum;					// The current page number.
	private int numOfLeaves;				// The number of leaf nodes.

	/**
	 * The constructor of the serializer.
	 * @param indexFile
	 * @throws FileNotFoundException
	 */
	public TreeSerializer(File indexFile) throws FileNotFoundException {
		this.indexFile = indexFile;
		fc = new FileOutputStream(indexFile).getChannel();
		// allocate the buffer size for output page
		buffer = buffer.allocate(B_SIZE);
		pageNum = 1;	// we want to firstly write the leaf node.
		numOfLeaves = 0;
	}

	/**
	 * Serialzes the the node into the index file.
	 * @param node
	 * @return the address of the node to be serialzed in the index file.
	 * @throws IOException
	 */
	public int serialize(TreeNode node) throws IOException {
		long position = B_SIZE * (long) pageNum;
		
		// initialize the buffer.
		fc.position(position);
		eraseBuffer();
		
		
		// searilize the leaf node.
		if (node instanceof LeafNode) {
			numOfLeaves++;
			LeafNode curr = (LeafNode) node;
			int numOfEntries = curr.dataEntries.size();
			
			// start writing.
			buffer.putInt(LEAF_FLAG);
			buffer.putInt(numOfEntries);
			for (DataEntry data : curr.dataEntries) {
				buffer.putInt(data.key);
				buffer.putInt(data.rids.size());
				for (Rid rid : data.rids) {
					buffer.putInt(rid.pageId);
					buffer.putInt(rid.tupleId);
				}
			}
		} else if (node instanceof IndexNode) {
			IndexNode curr = (IndexNode) node;
			int numOfKeys = curr.keys.size();
			buffer.putInt(IDX_FLAG);
			buffer.putInt(numOfKeys);
			for (Integer key : curr.keys) {
				buffer.putInt(key);
			}
			for (Integer addr : curr.address) {
				buffer.putInt(addr);
			}
		}
		
		// finally padding zeros at the end.
		while(buffer.hasRemaining()){
			buffer.putInt(0);
		}
		
		buffer.flip();
		fc.write(buffer);
		return pageNum++;
	}
	
	/**
	 * set the current page as the root page,
	 * and write the head page.
	 * 
	 * @param order the order of the tree.
	 */
	public void finishSerialization(int order) {
		long position = B_SIZE * (long) HEADER_ID;
		
		try {
			// initialize the buffer.
			fc.position(position);
			eraseBuffer();
			
			buffer.putInt(pageNum);	// The address of the root.
			buffer.putInt(numOfLeaves);	// The number of leaves in the tree.
			buffer.putInt(order);	// The order of the tree.
			
			// finally padding zeros at the end.
			while(buffer.hasRemaining()){
				buffer.putInt(0);
			}
			buffer.flip();
			fc.write(buffer);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * closes the target
	 * 
	 * @throws IOException If an I/O error occurs while calling the underlying
	 * 					 	reader's close method
	 */
	public void close() throws IOException {
		fc.close();
	}
	
	// Helper method that erases the buffer by filling zeros.
	private void eraseBuffer() {
		buffer.clear();
		// fill with 0 to clear the buffer
		buffer.put(new byte[B_SIZE]);
		buffer.clear();
	}
}
