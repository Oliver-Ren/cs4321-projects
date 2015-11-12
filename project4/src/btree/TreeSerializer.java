package btree;

import java.io.File;
import java.io.FileOutputStream;
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
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private long position;					// The writing position.
	
	public TreeSerializer(File indexFile) {
		this.file = file;
		fc = new FileOutputStream(file).getChannel();
		// allocate the buffer size for output page
		buffer = buffer.allocate(B_SIZE);	
		firstCallWrite = true;
		position = B_SIZE;	// firstly assign the initial position to be 
							// the second page (index 1).
		
		
	}

	public int serialize(TreeNode node) {
		if (node instanceof LeafNode) {
			LeafNode curr = (LeafNode) node;
			
		}
	}
}
