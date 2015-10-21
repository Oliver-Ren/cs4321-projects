package tests;

import java.io.FileReader;
import java.io.PrintStream;
import java.io.StringReader;

import operators.ScanOperator;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;
import util.DBCat;

/**
 * Preliminary tests of the JSqlParser interface.
 * @author Guantian Zheng (gz94)
 *
 */
public class tests {
	
	public static final PrintStream so = System.out;
	
	/**
	 * Some random tests and printing.
	 * @param args argument list
	 */
	public static void tests(String[] args) {
		DBCat.getInstance();
		
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement;
			while ((statement = parser.Statement()) != null) {
				System.out.println("Read statement: " + statement);
				Select select = (Select) statement;
				System.out.println("Select body is " + select.getSelectBody());
				
				PlainSelect ps = (PlainSelect) select.getSelectBody();
				
				so.println(ps.getDistinct());
				
				so.print("Select items are ");
				for (Object obj : ps.getSelectItems()) {
					SelectItem si = (SelectItem) obj;
					so.print(si);
					so.print(" ");
				}
				so.print('\n');
				
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
			
			parser = new CCJSqlParser(new StringReader("select distinct a.x, b.y, c.z, aa, * from a, b, c as asdjf "
					+ "where a.x = b.y and a.x = c.z and b.y = c.z and 1 < 2"));
			PlainSelect ps = (PlainSelect) ((Select) parser.Statement()).getSelectBody();
			so.println(ps.getDistinct());
			so.println(ps.getSelectItems());
			so.println(ps.getFromItem());
			so.println(ps.getFromItem().getAlias());
			so.println(ps.getJoins().get(0));
			so.println(ps.getJoins().get(1));
			so.println(((Join) ps.getJoins().get(1)).getOnExpression());
			so.println(((Join) ps.getJoins().get(1)).getRightItem().getAlias());
			so.println(((Join) ps.getJoins().get(1)).getUsingColumns());			
			
			AndExpression and = (AndExpression) ps.getWhere();
			so.println(and.getLeftExpression());
			so.println(and.getRightExpression());
			
			so.print("Select items are ");
			for (Object obj : ps.getSelectItems()) {
				SelectItem si = (SelectItem) obj;
				so.print(si);
				so.print(" ");
			}
			so.print('\n');
			
			parser = new CCJSqlParser(new StringReader("select X.*, X.a, b from X"));
			ps = (PlainSelect) ((Select) parser.Statement()).getSelectBody();
			so.println(ps.getSelectItems().get(0).getClass().getName());
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
	}

}


