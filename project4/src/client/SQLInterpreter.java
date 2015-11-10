package client;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import nio.BinaryTupleWriter;
import nio.NormalTupleWriter;
import nio.TupleWriter;
import tests.Diff;
import util.DBCat;
import util.SelState;
import util.SortTuple;

/**
 * The <tt>SqlInterpreter</tt> class provides a client for accepting
 * SQL query requests and output the results.
 * <p>
 * This is the top-level entry for the program.
 * 
 * @author Chengxiang Ren
 *
 */
public class SQLInterpreter {
	
	public void execute(String inPath, String outPath, boolean isMute) {
		execute(inPath, outPath, "", isMute);
	}
	
	/**
	 * Execute the parser with the given input / output directory.
	 * @param inPath input directory
	 * @param outPath output directory
	 * @param isMute whether to print debugging info
	 */
	public void execute(String inPath, String outPath, String tempPath, boolean isMute) {
		DBCat.resetDirs(inPath, outPath, tempPath);
		DBCat.getInstance();

		try {
			
			Diff.cleanFolder(outPath);
			Diff.cleanFolder(tempPath);
			
			CCJSqlParser parser 
				= new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement;
			int counter = 1;
			while ((statement = parser.Statement()) != null) {
				try {
					File file = new File(DBCat.outputDir 
							+ File.separator + "query" + counter);
					
					// if (!isMute) {
						System.out.println("Parsing: " + statement);
					// }
					SelState selState = new SelState(statement);
					
					TupleWriter writer = new BinaryTupleWriter(file.getAbsolutePath());
					
					// begin time
					long beginTime = System.currentTimeMillis();
					selState.root.dump(writer);
					// end time
					long endTime = System.currentTimeMillis();
					System.out.println("The running time for query " 
					+ counter + " is " + (endTime - beginTime) + " milliseconds");
					
					writer.close();
					counter++;
				} catch (Exception e) {
					System.out.println("Exception when parsing query" + counter);
					e.printStackTrace();
					continue;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The main function invoked by jar.
	 * @param args argument list
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			throw new IllegalArgumentException("Number of arguments not right");
		}
		
		SQLInterpreter itpr = new SQLInterpreter();
		itpr.execute(args[0], args[1], args[2], true);
	}
}
