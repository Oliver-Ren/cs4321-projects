
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import visitors.JoinExpVisitor;
import visitors.SelExpVisitor;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
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
	public static Long getAttrVal(Tuple tp, String attr, List<String> schema) {
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
	
	public static boolean isSelect(Expression exp) {
		List<String> tmp = getExpTabs(exp);
		return (tmp != null && tmp.size() == 1);
	}
	
	public static boolean isJoin(Expression exp) {
		List<String> tmp = getExpTabs(exp);
		return (tmp != null && tmp.size() == 2);
	}

	private static void updateRange(Integer[] range, int val, 
			boolean isLower, boolean inclusive, boolean oppo) {
		if (oppo) {
			updateRange(range, val, !isLower, inclusive, false);
			return;
		}
		
		if (!inclusive)
			val = (isLower) ? val + 1 : val - 1;
		
		if (isLower)
			range[0] = (range[0] == null) ? val : 
				Math.max(range[0], val);
		else
			range[1] = (range[1] == null) ? val :
				Math.min(range[1], val);
	}
	
	public static Integer[] getSelRange(Expression exp, String[] attr) {
		if (!isSelect(exp))
			throw new IllegalArgumentException();
		
		Expression left = 
				((BinaryExpression) exp).getLeftExpression();
		Expression right = 
				((BinaryExpression) exp).getRightExpression();
		
		Integer val = null;
		if (left instanceof Column) {
			attr[0] = left.toString();
			val = Integer.parseInt(right.toString());
		}
		else {
			attr[0] = right.toString();
			val = Integer.parseInt(left.toString());
		}
		
		boolean oppo = !(left instanceof Column);
		boolean inclusive = !(exp instanceof MinorThan) && 
				!(exp instanceof GreaterThan);
		boolean isLower = (exp instanceof MinorThan ||
				exp instanceof MinorThanEquals || 
				exp instanceof EqualsTo);
		boolean isUpper = (exp instanceof GreaterThan ||
				exp instanceof GreaterThan || 
				exp instanceof EqualsTo);
		
		if (!isLower && !isUpper)
			throw new IllegalArgumentException();
		
		Integer[] ret = new Integer[2];
		
		if (isLower)
			updateRange(ret, val, true, inclusive, oppo);
		if (isUpper)
			updateRange(ret, val, false, inclusive, oppo);
		
		return ret;
	}
	
	public static Expression createCondition(String tab, String col, 
			int val, boolean isEq, boolean isGE) {
		Table t = new Table(null, tab);
		Column c = new Column(t, col);
		LongValue v = new LongValue(String.valueOf(val));
		
		if (isEq)
			return new EqualsTo(c, v);
		if (isGE)
			return new GreaterThanEquals(c, v);
		return new MinorThanEquals(c, v);
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
	
	public static Expression procJoinConds(Expression exp, 
			List<String> outScm, List<String> inScm, 
			List<Integer> outIdxs, List<Integer> inIdxs) {
		outIdxs.clear(); inIdxs.clear();
		if (exp == null) return null;
		
		List<Expression> exps = decompAnds(exp);
		List<Expression> eqs = new ArrayList<Expression>();
		List<Expression> others = new ArrayList<Expression>();
		
		for (Expression e : exps) {
			if (!(e instanceof EqualsTo)) {
				others.add(e);
				continue;
			}
			
			EqualsTo et = (EqualsTo) e;
			String left = et.getLeftExpression().toString();
			String right = et.getRightExpression().toString();
			
			boolean ol = outScm.contains(left), 
					or = outScm.contains(right), 
					il = inScm.contains(left),
					ir = inScm.contains(right);
			
			if (ol && or || il && ir || 
					!(ol && ir || or && il)) {
				others.add(e);
				continue;
			}
			
			System.out.println("Equals: " + left + ' ' + right);
			
			if (or) {
				et = new EqualsTo(et.getRightExpression(), 
						et.getLeftExpression());
				String tmp = left; left = right; right = tmp;
			}
			
			int outIdx = getAttrIdx(left, outScm);
			int inIdx = getAttrIdx(right, inScm);
			
			if (outIdx == -1 || inIdx == -1)
				throw new IllegalArgumentException();
			
			outIdxs.add(outIdx);
			inIdxs.add(inIdx);
			eqs.add(et);
		}
		
		eqs.addAll(others);
		return genAnds(eqs);
	}
	
	public static boolean hasIdxAttr(String tab, Expression selCond) {
		IndexInfo ii = DBCat.getIndexInfo(tab);
		if (selCond == null || ii == null) return false;
		List<Expression> conds = decompAnds(selCond);
		
		for (Expression expr : conds) {
			Expression left = 
					((BinaryExpression) expr).getLeftExpression();
			Expression right = 
					((BinaryExpression) expr).getRightExpression();
			
			String str = null;
			
			if (left instanceof Column && right instanceof LongValue
					|| left instanceof LongValue && right instanceof Column) {
				if (   expr instanceof EqualsTo
					|| expr instanceof GreaterThan
					|| expr instanceof GreaterThanEquals
					|| expr instanceof MinorThan
					|| expr instanceof MinorThanEquals ) {
					
					str = (left instanceof Column) ? left.toString() : 
						right.toString();
					if (str.indexOf('.') != -1)
						str = str.split("\\.")[1];
					if (ii.attr.equals(str)) return true; 
				}
					
			}
//			//handle corner case mingyuan
//			if(left instanceof Column && right instanceof Column){
//				return false;
//			}
//			if(expr instanceof NotEqualsTo){
//				return false;
//			}
			str = (left instanceof Column) ? left.toString() : 
				right.toString();
			if (str.indexOf('.') != -1)
				str = str.split("\\.")[1];
			
			if (ii.attr.equals(str)) return true;
		}
		
		return false;
	}
	
	public static Integer[] bPlusKeys(String indexAttr, Expression selCond) {
		if (selCond == null) return null;
		List<Expression> conds = decompAnds(selCond);
		
		Integer[] ret = new Integer[2]; // low and high
		
		for (Expression expr : conds) {
			Expression left = 
					((BinaryExpression) expr).getLeftExpression();
			Expression right = 
					((BinaryExpression) expr).getRightExpression();
			
			String attr = null;
			Integer val = null;
			if (left instanceof Column) {
				attr = left.toString();
				val = Integer.parseInt(right.toString());
			}
			else {
				attr = right.toString();
				val = Integer.parseInt(left.toString());
			}
			if (attr.indexOf('.') != -1)
				attr = attr.split("\\.")[1];
			if (!indexAttr.equals(attr)) continue;			
			// TODO
			// update low key and high key
			//update low key
			if(expr instanceof GreaterThan){ // inclusive low key
				if(ret[0] == null){
					ret[0] = val+1;
				} else{
					ret[0] = Math.max(ret[0], val+1);
				}
							
			} else if (expr instanceof GreaterThanEquals) {
				if(ret[0] == null){
					ret[0] = val;
				} else{
					ret[0] = Math.max(ret[0], val);
				}
			}else if(expr instanceof MinorThan){
				if(ret[1] == null){
					ret[1] = val;
				} else {
					ret[1] = Math.min(ret[1],val);
				}	
			} else if(expr instanceof MinorThanEquals){ // exclusive high key
				if(ret[1] == null){
					ret[1] = val+1;
				} else {
					ret[1] = Math.min(ret[1], val+1);
				}
			} else if (expr instanceof EqualsTo){
				ret[0] = val;
				ret[1] = val;
			}
		}		
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

