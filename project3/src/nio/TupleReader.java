package nio;

import util.Tuple;

public interface TupleReader {

	
	
	
	/**
	 * read the next tuple from the table
	 * author:hmy
	 */
	public Tuple readTuple();
	
	/**
	 * reset the current buffer, 
	 * read the file from start
	 */
	
}
