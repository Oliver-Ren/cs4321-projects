package client;
import java.io.FileReader;

import operators.SelectOperator;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import util.DBCat;

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
	public void executeSelect(String inPath, String outPath) {
		DBCat.getInstance();
		DBCat.resetDirs(inPath, outPath);
		try {
			CCJSqlParser parser 
				= new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				Select select = (Select) statement;
				PlainSelect ps = (PlainSelect) select.getSelectBody();
				FromItem from = ps.getFromItem();
				String tabName = from.toString().split(" ")[0];
				
				// SelectOperator slctOp 
					// = new SelectOperator(DBCat.getTable(tabName), ps.getWhere());
				
			}
		} catch (NullPointerException e) {
			System.err.println("null pointer");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
}
