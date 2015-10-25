package nio;

import java.io.IOException;

import util.Tuple;

/**
 * the <tt>TupleWriter</tt> interface represents a writer that writes tuple 
 * to a database table file.
 * 
 * @author Mingyuan Huang (mh2239)
 * @author Chengxiang Ren (cr486)
 *
 */

public interface TupleWriter {


	/**
	 * write the next tuple from the table to bytebuffer.
	 * @throws IOException If an I/O error occurs while calling the underlying
	 * 					 	reader's read method
	 *
	 */
	public void write(Tuple tuple) throws IOException;
	
	/**
	 * closes the target
	 * 
	 * @throws IOException If an I/O error occurs while calling the underlying
	 * 					 	reader's close method
	 */
	public void close() throws IOException;
	
	
	
}
