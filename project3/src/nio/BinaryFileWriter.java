package nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import util.Tuple;
/**
 * this method converts the output tuple files to binary format.
 * This method uses java NIO to write a block of data into a buffer
 * then save to the targeted destination file
 * @author Mingyuanh
 *
 */
public class BinaryFileWriter implements TupleWriter {
	private static final int B_SIZE = 4096;	// the size of the buffer page
	private static final int INT_LEN = 4;	// the bytes a integer occupies
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int numOfAttr;					// Number of attributes in a tuple
	private int numOfTuples;				// Number of tuples in the page
	private boolean fullBuffer;			    // flag if buffer is full
	private boolean endOfFile;				// flag for end of file
	private boolean firstCallWrite;			// flag to check if the write method
											// is called the first time
	private int TUPLE_LIMIT; 				// the maximum num of tuples that
											// can fit in on page

	/**
	 *Constructor of BinaryFileWriter
	 *@param file path where the output should be written to
	 */
	public BinaryFileWriter(String filePath) throws FileNotFoundException{	
		File file = new File(filePath);
		FileOutputStream fos = new FileOutputStream(file);
		fc = fos.getChannel(); // assign file channel object
		// allocate the buffer size for output page
		buffer = buffer.allocate(B_SIZE);	
		fullBuffer = false;
		firstCallWrite = true;
		numOfTuples = 0;
		
	}
	
	@Override
	public void write(Tuple tuple) throws IOException {
		if(firstCallWrite){
			numOfAttr = tuple.length(); // assign nums of Attrs	
			buffer.putInt(numOfAttr);
			 // a defualt number for tuple number,will update in close();
			buffer.putInt(0);		
			firstCallWrite = false;
			// calculate the maximum size of tuple 
			//can be fit in one page not include the metadata
			TUPLE_LIMIT = (B_SIZE - 8)/(INT_LEN * numOfAttr);			
		}
		if(buffer.position()<=(B_SIZE-(INT_LEN*numOfAttr))){
			// write the tuple into the buffer
			for(int i = 0; i < tuple.length(); i++){
				buffer.putInt(tuple.get(i));
			}
			numOfTuples++;								
		} else {
			buffer.flip();
			fc.write(buffer);
			// rellocate a new buffer page
			buffer.clear();
			buffer  = buffer.put(new byte[B_SIZE]);
			buffer.clear();
			write(tuple);
		}		
	}
	
	
	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}
	
}
