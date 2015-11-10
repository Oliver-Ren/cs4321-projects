package nio;

import java.io.IOException;

import util.Tuple;

/**
 * the <tt>TupleReader</tt> interface represents a reader that reads tuple 
 * from a database table file.
 * 
 * @author Mingyuan Huang (mh2239)
 * @author Chengxiang Ren (cr486)
 *
 */
public interface TupleReader {
	/**
	 * get the index of the current tuple  
	 * @return
	 * @throws IOException
	 */
	public Long getIndex() throws IOException;
	
	/**
	 * read the next tuple from the table.
	 * 
	 * @return the current tuple the reader is at
	 * @throws IOException If an I/O error occurs while calling the underlying
	 * 					 	reader's read method
	 *
	 */
	public Tuple read() throws IOException;
	
	/**
	 * resets the target to the specified position.
	 * 
	 * @throws IOException If an I/O error occurs while calling the underlying
	 * 					 	reader's reset method
	 */
	public void reset(long index) throws IOException;
	
	
	/**
	 * resets the target to the start.
	 * 
	 * @throws IOException If an I/O error occurs while calling the underlying
	 * 					 	reader's reset method
	 */
	public void reset() throws IOException;
	
	/**
	 * closes the target
	 * 
	 * @throws IOException If an I/O error occurs while calling the underlying
	 * 					 	reader's close method
	 */
	public void close() throws IOException;
	
	
}
