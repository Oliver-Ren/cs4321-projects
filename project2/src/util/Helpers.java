
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import operators.Operator;
import operators.ScanOperator;
import operators.SelectOperator;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import operators.*;

public class Helpers {

	public static String getFromTab(FromItem from) {
		return from.toString().split(" ")[0];
	}
	
	public static String getColName(String tabCol) {
		return tabCol.split("\\.")[1];
	}
	
	public static Long getAttr(Tuple tp, String attr, List<String> schema) {
		int idx = schema.indexOf(attr);
		if (idx != -1) return (long) tp.get(idx);
		
		for(int i = 0; i < schema.size(); i++) {
			String colName = getColName(schema.get(i));
			if (colName.equals(attr))
				return (long) tp.get(i);
		}
		
		return null;
	}
	
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
			if (col.getTable() == null) return null;  //QUESTION: is invalid condition?
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
	
	public static Expression genAnds(List<Expression> exps) {
		if (exps.isEmpty()) return null;
		Expression ret = exps.get(0);
		for (int i = 1; i < exps.size(); i++)
			ret = new AndExpression(ret, exps.get(i));
		return ret;
	}
	
	/**
	 * This is only an experimental method.
	 * Now it supports the scan.
	 * @param selState
	 * @return
	 */
	public static Operator generatePlan(SelState selState) {
		Operator curr = null;
		Operator leaf = new ScanOperator(DBCat.getTable(getFromTab(selState.from)));
		curr = leaf;
		if (selState.where != null) {
			curr = new SelectOperator((ScanOperator)leaf, selState.where);
		}
		return curr;
	}
	
}

