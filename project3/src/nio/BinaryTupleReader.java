package nio;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jdk.management.resource.internal.TotalResourceContext;
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
	private static final int INT_LEN = 4;	// the bytes a integer occupies
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int numOfAttr;					// Number of attributes in a tuple
	private int numOfTuples;				// Number of tuples in the page
	private boolean needNewPage;			// flag for read new page to buffer
	private boolean endOfFile;				// flag for end of file
	private long numTuplesBuffered;			// the total number of tuples buffered
	private List<Long> offsets;				// the list stores the maximum
											// tuple index in the page at 
											// current index.
	/**
	 * Creates a new <tt>BinaryTupleReader</tt>, given the <tt>File</tt> to
	 * read from.
	 * 
	 * @param file the <tt>File</tt> to read from
	 * @throws FileNotFoundException if the file does not exist or cannot be 
	 * 		   opened for reading.
	 */
	@SuppressWarnings("resource")
	public BinaryTupleReader(File file) throws FileNotFoundException {
		fc = new FileInputStream(file).getChannel();
		buffer = ByteBuffer.allocate(B_SIZE);
		needNewPage = true;
		offsets = new ArrayList<Long>();
		offsets.add(new Long(0));
		numTuplesBuffered = 0;
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
		while (!endOfFile) {
			// read a new page into the buffer and set the metadata accordingly
			if (needNewPage) {
				try {
					fetchPage();
				} catch (EOFException e) {
					break;
				}
				System.out.println("============ page " + offsets.size() 
						+ "=== tuple " + numOfTuples +" =======");
			}
			
			if (buffer.hasRemaining()) {
				int [] cols = new int[numOfAttr];
				for (int i = 0; i < numOfAttr; i++) {
					cols[i] = buffer.getInt();
				}
				return new Tuple(cols);
			}
			
			// does not has remaining
			buffer.clear();
			// fill with 0 to clear the buffer
			buffer.put(new byte[B_SIZE]);
			buffer.clear();
			needNewPage = true;		
		}
		
		return null;	// if reached the end of the file, return null
	}

	@Override
	public void reset(long index) throws IOException, IndexOutOfBoundsException {
		// precondition: the index should not exceed the number of tuples buffered
		if (index >= numTuplesBuffered || index < 0) {
			throw new IndexOutOfBoundsException("The index is too large");
		}
		int pageIdx = Collections.binarySearch(offsets, new Long(index + 1));
		pageIdx = pageIdx >= 0 ? pageIdx : -(pageIdx + 1);
		System.out.println("the pageIdx is: " + pageIdx);
		fc.position((long) (pageIdx - 1) * B_SIZE);
		
		// reset the page containing the tuple
		needNewPage = true;
		endOfFile = false;
		offsets = offsets.subList(0, pageIdx);
		numTuplesBuffered = offsets.get(offsets.size() - 1);
		
		// go to the exact position
		int newTupleOffset = (int) (index - numTuplesBuffered);
		int newPos = (newTupleOffset * numOfAttr + 2) * INT_LEN;
		
		try {
			fetchPage();
		} catch (EOFException e) {
			e.printStackTrace();
		}
		
		buffer.position(newPos);
		for (Long l : offsets) {
			System.out.println("offest" + l);
		}
		System.out.println("tup buffered" + (offsets.get(offsets.size() - 1)));
	}

	@Override
	public void reset() throws IOException {
		
		// TODO Auto-generated method stub

	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		for (Long l : offsets) {
			System.out.println("offest" + l);
		}
		fc.close();
	}
	
	// Helper method for reading a new page into the buffer
	private void fetchPage() throws IOException {
		endOfFile = (fc.read(buffer) < 0);
		needNewPage = false;
		
		if (endOfFile) throw new EOFException();
		
		buffer.flip();
		numOfAttr = buffer.getInt();	// metadata 0
		numOfTuples = buffer.getInt();	// metadata 1
		numTuplesBuffered += numOfTuples;
		offsets.add(numTuplesBuffered);
		// set the limit according to the number of tuples and
		// attributes actually in the page.
		buffer.limit((numOfAttr * numOfTuples + 2) * INT_LEN);
	}
	
	// Helper method that dumps the file to the System.out for ease of debugging
	private void dump() throws IOException {
		while (fc.read(buffer) > 0) {
		
			
			buffer.flip();
			
			int attr = buffer.getInt();
			int tuples = buffer.getInt();
			//System.out.println("============ page " + count 
			// + "=== tuple " + tuples +" =======");
			int col = 0;
			buffer.limit((attr * tuples + 2) * INT_LEN);
			while (buffer.hasRemaining()) {
				col++;
				System.out.print(buffer.getInt());
				if (col == attr) {
					System.out.println();
					col = 0;
				} else {
					System.out.print(",");
				}
			}
			
			buffer.clear();
			// fill with 0 to clear the buffer
			buffer.put(new byte[B_SIZE]);
			buffer.clear();
			System.out.println();
	
		}
		
	}
	
	

}
