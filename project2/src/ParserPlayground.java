import java.io.FileReader;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

/**
 * The ParserPlayground class is a sand box for us to
 * experiment and get familiar with the jsqlparser.
 * 
 * @author Chengxiang Ren
 *
 */

public class ParserPlayground {
	private static final String queriesFile = "samples/input/queries.sql";

	public static void print(Object obj) {
		System.out.println(obj);
	}
	
	public static void main(String[] args) {
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(queriesFile));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				System.out.println("Select body is " + select.getSelectBody());
				PlainSelect ps = (PlainSelect) select.getSelectBody();
				print(ps.getFromItem());
				print("Distinct:" + ps.getDistinct());
				if (ps.getWhere() != null && ps.getWhere().getClass() != null)
					print(ps.getWhere().getClass().getName());
			}
			
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}

}
