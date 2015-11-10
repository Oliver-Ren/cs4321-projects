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
public class BinaryTupleWriter implements TupleWriter {
	private static final int B_SIZE = 4096;	// the size of the buffer page
	private static final int INT_LEN = 4;	// the bytes a integer occupies
	private FileChannel fc;					// The file channel for reader
	private ByteBuffer buffer;				// The buffer page.
	private int numOfAttr;					// Number of attributes in a tuple
	private int numOfTuples;				// Number of tuples in the page
	private boolean firstCallWrite;			// flag to check if the write method
											// is called the first time
	private int TUPLE_LIMIT; 				// the maximum num of tuples that
											// can fit in on page
	private FileOutputStream fos;

	/**
	 *Constructor of BinaryFileWriter
	 *@param file path where the output should be written to
	 */
	public BinaryTupleWriter(String filePath) throws FileNotFoundException{	
		File file = new File(filePath);
		fos = new FileOutputStream(file);
		fc = fos.getChannel(); // assign file channel object
		// allocate the buffer size for output page
		buffer = buffer.allocate(B_SIZE);	
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
		if(numOfTuples<TUPLE_LIMIT){
			// write the tuple into the buffer
			for(int i = 0; i < tuple.length(); i++){
				buffer.putInt(tuple.get(i));
			}
			numOfTuples++;								
		} else {
			//paddle the buffer if necessary
			paddle(buffer);
			//update the numOfTuples
			buffer.putInt(4, numOfTuples);		
			buffer.clear();			
			fc.write(buffer);
			// rellocate a new buffer page
			numOfTuples = 0;
			firstCallWrite = true;
			buffer.clear();
			buffer  = buffer.put(new byte[B_SIZE]);
			buffer.clear();
			write(tuple);
		}		
	}
	/**
	 * paddel zero to the end of the page
	 * if there are still some spaces left
	 * @param bf
	 */
	public void paddle(ByteBuffer bf){
		while(bf.hasRemaining()){
			bf.putInt(0);
		}
	}
	
	@Override
	public void close() throws IOException {
		// close the when finish output tuples		
		//paddel the buffer
		paddle(buffer);
		//update the num of tuple
		buffer.putInt(4, numOfTuples);
		buffer.clear();
		fc.write(buffer);
		fc.close();
		fos.close();
		
		
	}
	
}
