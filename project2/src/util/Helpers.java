package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Helpers {

	public static String getTabName(FromItem from) {
		return from.toString().split(" ")[0];
	}
	
	public static String getSelCol(SelectItem sel) {
		String[] strs = sel.toString().split(".");
		if (strs.length < 2)
			return strs[0];
		else
			return strs[1];
	}
	
	public static long getColVal(Tuple tp, String attr, String tabName) {
		return tp.cols[DBCat.schemas.get(tabName).indexOf(attr)];
	}
	
	public static long getAttr(Tuple tp, String attr, List<String> schema) {
		return tp.cols[schema.indexOf(attr)];
	}
	
	public static int getNumTabs(Expression exp) {
		if (!(exp instanceof BinaryExpression))
			return 0;
		
		BinaryExpression be = (BinaryExpression) exp;
		Expression left = be.getLeftExpression();
		Expression right = be.getRightExpression();
		
		int cnt = 0;
		String tab1 = null, tab2 = null;
		if (left instanceof Column) {
			cnt++;
			Column col = (Column) left;
			tab1 = col.getTable().toString();
			if (tab1 == null) return cnt;
		}
		if (right instanceof Column) {
			Column col = (Column) right;
			tab2 = col.getTable().toString();
			if (!tab1.equals(tab2))
				cnt++;
		}
		
		return cnt;
	}
	
	public static List<String> getTabs(Expression exp) {
		List<String> ret = new ArrayList<String>();
		if (!(exp instanceof BinaryExpression))
			return ret;
		
		BinaryExpression be = (BinaryExpression) exp;
		Expression left = be.getLeftExpression();
		Expression right = be.getRightExpression();
		
		if (left instanceof Column)
			ret.add(((Column) left).getTable().toString());
		if (right instanceof Column)
			ret.add(((Column) right).getTable().toString());
		
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
	
}
