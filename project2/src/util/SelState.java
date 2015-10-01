package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

public class SelState {

	public Select sel;
	public Distinct dist;
	public PlainSelect ps;
	public List<SelectItem> sels;
	public FromItem from;
	public List<Join> joins;
	public Expression where;
	public List<OrderByElement> orders;
	
	public List<String> froms = new ArrayList<String>();
	public List<Expression> ands = null;
	
	public SelState(Statement st) {
		sel = (Select) st;
		ps = (PlainSelect) sel.getSelectBody();
		
		dist = ps.getDistinct();
		sels = ps.getSelectItems();
		from = ps.getFromItem();
		joins = ps.getJoins();
		where = ps.getWhere();
		orders = ps.getOrderByElements();
		
		DBCat.aliases.clear();
		if (from.getAlias() != null) {
			DBCat.aliases.put(from.getAlias(), Helpers.getTabName(from));
			froms.add(from.getAlias());
		}
		else
			froms.add(from.toString());
		
		if (joins == null) return;
		for (Join join : joins) {
			FromItem ri = join.getRightItem();
			if (ri.getAlias() != null) {
				DBCat.aliases.put(ri.getAlias(), Helpers.getTabName(ri));
				froms.add(ri.getAlias());
			}
			else
				froms.add(ri.toString());
		}
		
		ands = Helpers.decompAnds(where);
		Collections.sort(ands, new expComp(froms));
	}
	
	public class expComp implements Comparator<Expression> {
		private List<String> froms;
		
		private int lastIdx(List<String> tabs) {
			int idx = -1;
			for (String tab : tabs) {
				idx = Math.max(idx, froms.indexOf(tab));
			}
			return idx;
		}
		
		@Override
		public int compare(Expression exp1, Expression exp2) {
			List<String> tabs1 = Helpers.getTabs(exp1);
			List<String> tabs2 = Helpers.getTabs(exp2);
			
			int idx1 = lastIdx(tabs1);
			int idx2 = lastIdx(tabs2);
			
			int cmp = Integer.compare(idx1, idx2);
			if (cmp != 0) return cmp;
			return Integer.compare(tabs1.size(), tabs2.size());
		}
		
		public expComp(List<String> froms) {
			this.froms = froms;
		}
	}
	
}
