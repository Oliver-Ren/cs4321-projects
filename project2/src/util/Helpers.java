
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import visitors.JoinExpVisitor;
import visitors.SelExpVisitor;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * The helper functions useful througout the project.
 * @author Guantian Zheng (gz94)
 *
 */
public class Helpers {

	/**
	 * Obtain the selection result.
	 * @param tp the tuple to be examined
	 * @param exp the selection expression
	 * @param sv the selection condition visitor
	 * @return the result (true / false)
	 */
	public static boolean getSelRes(Tuple tp, Expression exp, SelExpVisitor sv) {
		sv.setTuple(tp);
		exp.accept(sv);
		return sv.getFinalCondition();
	}
	
	/**
	 * Obtain the join result.
	 * @param tp1 the left tuple
	 * @param tp2 the right tuple
	 * @param exp the join condition
	 * @param jv the join expression visitor
	 * @return the result (true / false)
	 */
	public static boolean getJoinRes(Tuple tp1, Tuple tp2, Expression exp, JoinExpVisitor jv) {
		jv.setTuple(tp1, tp2);
		exp.accept(jv);
		return jv.getFinalCondition();
	}
	
	/**
	 * Get the original table name of an FromItem.
	 * @param from a FromItem
	 * @return the original table name
	 */
	public static String getFromTab(FromItem from) {
		return from.toString().split(" ")[0];
	}
	
	/**
	 * Get the column name in a "Table.Column" string
	 * @param tabCol the string
	 * @return the column name
	 */
	public static String getColName(String tabCol) {
		return tabCol.split("\\.")[1];
	}
	
	/**
	 * Get the index of a tp's attribute.
	 * @param tp the tuple
	 * @param attr the attribute
	 * @param schema the schema
	 * @return the position of the attribute
	 */
	public static int getAttrIdx(String attr, List<String> schema) {
		int idx = schema.indexOf(attr);
		if (idx != -1) return idx;
		
		for(int i = 0; i < schema.size(); i++) {
			String colName = getColName(schema.get(i));
			if (colName.equals(attr))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * Obtain the tuple's attribute.
	 * @param tp the tuple
	 * @param attr the attribute
	 * @param schema the tuple's schema
	 * @return the long value of the attribute
	 */
	public static Long getAttr(Tuple tp, String attr, List<String> schema) {
		int idx = getAttrIdx(attr, schema);
		if (idx != -1) return (long) tp.get(idx);
		return null;
	}
	
	/**
	 * Analyze the tables mentioned in a binary expression.
	 * @param exp the binary expression
	 * @return the list of tables mentioned; is null if 
	 * a table is referenced anonymously
	 */
	public static List<String> getExpTabs(Expression exp) {
		List<String> ret = new ArrayList<String>();
		if (!(exp instanceof BinaryExpression))
			return ret;
		
		BinaryExpression be = (BinaryExpression) exp;
		Expression left = be.getLeftExpression();
		Expression right = be.getRightExpression();
		
		Column col;
		if (left instanceof Column) {
			col = (Column) left;
			if (col.getTable() == null) return null;
			ret.add(col.getTable().toString());
		}
		if (right instanceof Column) {
			col = (Column) right;
			if (col.getTable() == null) return null;
			ret.add(col.getTable().toString());
		}
		
		if (ret.size() == 2 && ret.get(0).equals(ret.get(1)))
			ret.remove(1);
		
		return ret;
	}
	
	/**
	 * Decompose an AND expression recursively into a list of 
	 * binary expressions.
	 * @param exp the expression
	 * @return a list of basic expressions
	 */
	public static List<Expression> decompAnds(Expression exp) {
		List<Expression> ret = new ArrayList<Expression>();
		while (exp instanceof AndExpression) {
			AndExpression and = (AndExpression) exp;
			ret.add(and.getRightExpression());
			exp = and.getLeftExpression();
		}
		ret.add(exp);
		
		Collections.reverse(ret);
		return ret;
	}
	
	/**
	 * Concatenate a group of expressions into a 
	 * long AND expression.
	 * @param exps the list of binary expressions
	 * @return the final AND expression
	 */
	public static Expression genAnds(List<Expression> exps) {
		if (exps.isEmpty()) return null;
		Expression ret = exps.get(0);
		for (int i = 1; i < exps.size(); i++)
			ret = new AndExpression(ret, exps.get(i));
		return ret;
	}
	
	/**
	 * Check if the ordered elements are not selected.
	 * @param sels the selected items
	 * @param orders the ordered elements
	 * @return true / false
	 */
	public static boolean projLossy(List<SelectItem> sels, List<OrderByElement> orders) {
		if (sels.get(0) instanceof AllColumns)
			return false;
		
		if (orders == null || orders.isEmpty())
			return false;
		
		HashSet<String> selCols = new HashSet<String>();
		HashSet<String> ordCols = new HashSet<String>();
		
		for (SelectItem si : sels)
			selCols.add(si.toString());
		for (OrderByElement obe : orders)
			ordCols.add(obe.toString());
		
		ordCols.removeAll(selCols);
		return !ordCols.isEmpty();
	}
	
}

