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
	private File file;						// the file reading
	private static final int B_SIZE = 4096;	// the size of the buffer page
	private static final int INT_LEN = 4;	// the bytes a integer occupies
	private static final int LEAF_FLAG = 0; // the flag indicating this is leaf
	private static final int IDX_FLAG = 1;  // the flag indicating this is index.
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int pageNum;					// The current page number.

	
	public TreeSerializer(File indexFile) throws FileNotFoundException {
		this.file = file;
		fc = new FileOutputStream(file).getChannel();
		// allocate the buffer size for output page
		buffer = buffer.allocate(B_SIZE);
		pageNum = 1;	// we want to firstly write the leaf node.
		
	}

	public int serialize(TreeNode node) throws IOException {
		long position = B_SIZE * (long) pageNum;
		
		// initialize the buffer.
		fc.position(position);
		eraseBuffer();
		
		
		// searilize the leaf node.
		if (node instanceof LeafNode) {
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
		
		}
		
		// finally padding zeros at the end.
		while(buffer.hasRemaining()){
			buffer.putInt(0);
		}
		
		return pageNum++;
	}
	
	// Helper method that erases the buffer by filling zeros.
	private void eraseBuffer() {
		buffer.clear();
		// fill with 0 to clear the buffer
		buffer.put(new byte[B_SIZE]);
		buffer.clear();
	}
}
