package nio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import util.Tuple;

/**
 * The <tt>NormalTupleReader</tt> class is a convenience class for reading
 * tuples from a file which contains normal characters representing the tuples.
 * <p>
 * The main purpose of this class is for the ease of <em>debugging</em> and 
 * serves as the scaffold code.
 * <p>
 * The tuple reader uses Java's standard I/O for input. the <tt>NIO<tt> is 
 * <em>NOT</em> used in this class. Operations on pages are <em>Not</em> 
 * supported.
 * <p>
 * The format of the input file is: every file contains zero or more tuples;
 * a tuple is a line in the file with fields separated by <em>commas</em>.
 * e.g: 88, 98, 67
 * 
 * @author Chengxiang Ren (cr486)
 *
 */
public final class NormalTupleReader implements TupleReader {
	private File file;	// the file reading from.
	private BufferedReader br = null;	// the buffered reader for the file.
	
	
	/**
	 * Creates a new <tt>NormalTupleReader</tt>, given the <tt>File</tt> to
	 * read from.
	 * 
	 * @param file the <tt>File</tt> to read from
	 * @throws FileNotFoundException if the file does not exist or cannot be 
	 * 		   opened for reading.
	 */
	public NormalTupleReader(File file) throws FileNotFoundException {
		this.file = file;
		br = new BufferedReader(new FileReader(this.file));
	}
	
	/**
	 * Creates a new <tt>NormalTupleReader</tt>, given the name of file to
	 * read from.
	 * 
	 * @param fileName the name of the file to read from 
	 * @throws FileNotFoundException if the file does not exist or cannot be 
	 * 		   opened for reading.
	 */
	public NormalTupleReader(String fileName) throws FileNotFoundException {
		this(new File(fileName));
	}

	/**
	 * Reads one tuple from the file.
	 * 
	 * @return Tuple that is at the current position of the file
	 * 		   <tt>null</tt> if reached the end of the file
	 * @throws IOException if I/O error occurs while calling this method
	 */
	@Override
	public Tuple read() throws IOException {
		String line = br.readLine();
		if (line == null) return null;
		String[] elems = line.split(",");
		int len = elems.length;
		int[] cols = new int[len];
		for (int i = 0; i < len; i++) {
			cols[i] = Integer.valueOf(elems[i]);
		}
		return new Tuple(cols);
	}

	/**
	 * This operation is not supported.
	 */
	@Override
	public void reset(int index) throws IOException {
		throw new UnsupportedOperationException("not supported in normal reader");
	}

	/**
	 * Resets the file reader to the start of the file.
	 */
	@Override
	public void reset() throws IOException {
		if (br != null) {
			br.close();
		}
		br = new BufferedReader(new FileReader(file));
	}

	/**
	 * Closes the tuple reader.
	 */
	@Override
	public void close() throws IOException {
		br.close();
	}

}
