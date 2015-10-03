package client;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import operators.Operator;
import util.DBCat;
import util.Helpers;
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
	public void execute(String inPath, String outPath, boolean isMute) {
		DBCat.getInstance();
		DBCat.resetDirs(inPath, outPath);
		try {
			CCJSqlParser parser 
				= new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement;
			int counter = 1;
			while ((statement = parser.Statement()) != null) {
				File file = new File(DBCat.outputDir 
						+ File.separator + "query" + counter);
				PrintStream ps = new PrintStream(new BufferedOutputStream(
					new FileOutputStream(file)));
				SelState selState = new SelState(statement);
				if (!isMute) {
					System.out.println("Parsing: " + statement);
				}
				Operator root = selState.root;
				root.dump(ps);
				// root.dump(System.out.);
				ps.close();
				counter++;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		if (args.length != 2) {
			throw new IllegalArgumentException("Number of arguments not right");
		}
		String inPath = args[0];
		String outPath = args[1];
		SQLInterpreter itpr = new SQLInterpreter();
		itpr.execute(inPath, outPath, true);
	}
}
