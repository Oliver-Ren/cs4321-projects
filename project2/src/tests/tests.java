package tests;

import java.io.FileReader;
import java.io.PrintStream;
import java.io.StringReader;

import Operators.ScanOperator;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;
import util.DBCat;
import util.Table;

public class tests {
	
	public static final PrintStream so = System.out;
	
	public static void main(String[] args) {
		DBCat.getInstance();
		
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				System.out.println("Select body is " + select.getSelectBody());
				PlainSelect ps = (PlainSelect) select.getSelectBody();
				so.println("From item is " + ps.getFromItem());
				FromItem from = ps.getFromItem();
				so.println("Alias is " + from.getAlias());
				String tabName = from.toString().split(" ")[0];
				ScanOperator sop = new ScanOperator(DBCat.getTable(tabName));
				sop.dump(so);
//				print(ps.getJoins());
//				print(ps.getWhere());
//				if (ps.getWhere() != null && ps.getWhere().getClass() != null)
//					print(ps.getWhere().getClass().getName());
			}
			
			parser = new CCJSqlParser(new StringReader("select * from a, b, c as asdjf "
					+ "where a.x = b.y and a.x = c.z and b.y = c.z"));
			PlainSelect ps = (PlainSelect) ((Select) parser.Statement()).getSelectBody();
			so.println(ps.getFromItem());
			so.println(ps.getFromItem().getAlias());
			so.println(ps.getJoins().get(0));
			so.println(ps.getJoins().get(1));
			so.println(((Join) ps.getJoins().get(1)).getOnExpression());
			so.println(((Join) ps.getJoins().get(1)).getRightItem().getAlias());
			so.println(((Join) ps.getJoins().get(1)).getUsingColumns());
			
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}

}
