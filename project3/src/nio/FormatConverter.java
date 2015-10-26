package nio;

import java.io.IOException;

import util.Tuple;

/**
 * The <tt>BinaryToNormalConverter</tt> class is a utility class which provides 
 * methods for converting a database file of binary format into human readable
 * format as well as the methods converting from human readable to binary.
 * <p>
 * The implementation is based on the TupleReader and the TupleWriter.
 * 
 * @author Chengxiang Ren (cr486)
 *
 */
public class FormatConverter {
	
	/**
	 * The util method which converts binary database file to human readable.
	 * 
	 * @param inPath	the input file path
	 * @param outPath	the output file path
	 * @throws IOException	if I/O error occurs
	 */
	public static void binToNormal(String inPath, String outPath) throws IOException {
		TupleReader reader = new BinaryTupleReader(inPath);
		TupleWriter writer = new NormalTupleWriter(outPath);
		Tuple t;
		while ((t = reader.read()) != null) {
			writer.write(t);
		}
		reader.close();
		writer.close();
	}
	
	public static void 
}
