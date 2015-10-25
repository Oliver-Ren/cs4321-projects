package nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;

import util.Tuple;

/**
 * The <tt>BinaryTupleReader</tt> class is a convenience class for reading
 * tuples from a file of binary format representing the tuples.
 * <p>
 * The tuple reader uses Java's <em>NIO</em> which reads file in blocks to speed
 * up the operation of I/O.
 * <p>
 * The format of the input file is: data file is a sequence of pages of 4096
 * bytes in size. Every page contains two pieces of metadata. tuple themselves
 * are stored as (four-byte) integers.
 * 
 * @author Chengxiang Ren (cr486)
 *
 */
public final class BinaryTupleReader implements TupleReader {
	private static final int B_SIZE = 4096;	// the size of the buffer page
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int numOfAttr;					// Number of attributes in a tuple
	private int numOfTuples;				// Number of tuples in the page
	
	/**
	 * Creates a new <tt>BinaryTupleReader</tt>, given the <tt>File</tt> to
	 * read from.
	 * 
	 * @param file the <tt>File</tt> to read from
	 * @throws FileNotFoundException if the file does not exist or cannot be 
	 * 		   opened for reading.
	 */
	public BinaryTupleReader(File file) throws FileNotFoundException {
		fc = new FileInputStream(file).getChannel();
		buffer = ByteBuffer.allocate(B_SIZE);
	}
	
	/**
	 * Creates a new <tt>BinaryTupleReader</tt>, given the name of file to
	 * read from.
	 * 
	 * @param fileName the name of the file to read from 
	 * @throws FileNotFoundException if the file does not exist or cannot be 
	 * 		   opened for reading.
	 */
	public BinaryTupleReader(String fileName) throws FileNotFoundException {
		this(new File(fileName));
	}

	@Override
	public Tuple read() throws IOException {
		
		return null;
	}

	@Override
	public void reset(int index) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset() throws IOException {
		int count = 1;
		while (fc.read(buffer) > 0) {
		
			
			buffer.flip();
			
			int attr = buffer.getInt();
			int tuples = buffer.getInt();
			System.out.println("============ page " + count + "=== tuple " + tuples +" =======");
			int col = 0;
			while (buffer.hasRemaining()) {
				col++;
				System.out.print(" " + buffer.getInt());
				if (col == attr) {
					System.out.println();
					col = 0;
				}
			}
			
			buffer.clear();
			buffer.put(new byte[B_SIZE]);
			
				// fill with 0 to clear the buffer
			buffer.clear();
			System.out.println();
			count++;
		}
		
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		fc.close();
	}

}
