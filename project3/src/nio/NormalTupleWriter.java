package nio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import util.Tuple;

public final class NormalTupleWriter implements TupleWriter {
	private File file;					// the file writing to.
	private BufferedWriter bufferW = null;	// the buffered writer for the file.
	
	/**
	 * Creates a new <tt>NormalTupleReader</tt>, given the <tt>File</tt> 
	 * to write to.
	 * 
	 * @param file the <tt>File</tt> to write to
	 * @throws IOException  if the file exists but is a directory 
	 * 					 	rather than a regular file, does not 
	 * 						exist but cannot be created, or cannot 
	 * 						be opened for any other reason
	 */
	public NormalTupleWriter(File file) throws IOException {
		this.file = file;
		bufferW = new BufferedWriter(new FileWriter(file));
	}
	
	/**
	 * Creates a new <tt>NormalTupleReader</tt>, given the <tt>fileName</tt> 
	 * to write to.
	 * 
	 * @param fileName the <tt>fileName</tt> to write to
	 * @throws IOException  if the file exists but is a directory 
	 * 					 	rather than a regular file, does not 
	 * 						exist but cannot be created, or cannot 
	 * 						be opened for any other reason
	 */
	public NormalTupleWriter(String fileName) throws IOException {
		this(new File(fileName));
	}

	/**
	 * Writes the tuple to the file.
	 * 
	 * @param tuple the tuple to be written
	 * @throws IOException  if an I/O error occurs.
	 */
	@Override
	public void write(Tuple tuple) throws IOException {
		bufferW.write(tuple.toString());
		bufferW.newLine();
	}

	/** 
	 * Closes the writer.
	 * 
	 * @throws IOException if an I/O error occurs.
	 */
	@Override
	public void close() throws IOException {
		bufferW.close();
	}

}
