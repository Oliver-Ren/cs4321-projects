package client;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import util.DBCat;
import util.SelState;

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
	
	/**
	 * Execute the parser with the given input / output directory.
	 * @param inPath input directory
	 * @param outPath output directory
	 * @param isMute whether to print debugging info
	 */
	public void execute(String inPath, String outPath, boolean isMute) {
		DBCat.resetDirs(inPath, outPath);
		DBCat.getInstance();

		try {
			CCJSqlParser parser 
				= new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement;
			int counter = 1;
			while ((statement = parser.Statement()) != null) {
				try {
					File file = new File(DBCat.outputDir 
							+ File.separator + "query" + counter);
					PrintStream ps = new PrintStream(new BufferedOutputStream(
						new FileOutputStream(file)));
					// if (!isMute) {
						System.out.println("Parsing: " + statement);
					// }
					SelState selState = new SelState(statement);
					selState.root.dump(ps);
					ps.close();
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
		if (args.length != 2) {
			throw new IllegalArgumentException("Number of arguments not right");
		}
		
		SQLInterpreter itpr = new SQLInterpreter();
		itpr.execute(args[0], args[1], true);
	}
}
