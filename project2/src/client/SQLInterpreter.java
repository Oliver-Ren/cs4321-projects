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
	public void execute(String inPath, String outPath) {
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
				Operator root = Helpers.generatePlan(selState);
				root.dump(ps);
				ps.close();
				counter++;
				// SelectOperator slctOp 
					// = new SelectOperator(DBCat.getTable(tabName), ps.getWhere());	
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
