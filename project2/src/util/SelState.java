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
	public Expression[][] whereConds = null;
	
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
		
		if (froms.size() == 1) {
			whereConds = new Expression[1][1];
			whereConds[0][0] = where;
			return;
		}
		
		ands = Helpers.decompAnds(where);
		Collections.sort(ands, new expComp());
		
		whereConds = new Expression[froms.size()][2];
		genWhereConds();
	}
	
	private int lastIdx(List<String> tabs) {
		if (tabs == null) return froms.size() - 1;
		int idx = 0;
		for (String tab : tabs) {
			idx = Math.max(idx, froms.indexOf(tab));
		}
		return idx;
	}
	
	private void genWhereConds() {
		if (ands.isEmpty()) return;
		
		int i = 0, j = 0;
		while (j < ands.size()) {
			List<String> tabs = Helpers.getTabs(ands.get(j));
			if (lastIdx(tabs) != 0 || tabs.size() > 1)
				break;
			j++;
		}
		
		whereConds[0][0] = Helpers.genAnds(ands, i, j);
		i = j;
		
		while (i < ands.size()) {
			List<String> tabs = Helpers.getTabs(ands.get(i));
			if (tabs == null) {
				whereConds[froms.size() - 1][1] = Helpers.genAnds(ands, i, ands.size());
				return;
			}
			
			int curSize = tabs.size();
			int curIdx = lastIdx(tabs);
			
			j = i + 1;
			while (j < ands.size()) {
				List<String> tmpTabs = Helpers.getTabs(ands.get(j));
				if (tmpTabs == null && curIdx == froms.size() - 1) {
					whereConds[froms.size() - 1][1] = Helpers.genAnds(ands, i, ands.size());
					return;
				}
				
				if (tmpTabs == null || tmpTabs.size() != curSize || lastIdx(tmpTabs) != curIdx)
					break;
				j++;
			}
			
			whereConds[curIdx][curSize - 1] = Helpers.genAnds(ands, i, j);
			i = j;
		}
	}
	
	public class expComp implements Comparator<Expression> {
		@Override
		public int compare(Expression exp1, Expression exp2) {
			List<String> tabs1 = Helpers.getTabs(exp1);
			List<String> tabs2 = Helpers.getTabs(exp2);
			
			if (tabs1 == null) return 1;
			if (tabs2 == null) return -1;
			
			int idx1 = lastIdx(tabs1);
			int idx2 = lastIdx(tabs2);
			
			int cmp = Integer.compare(idx1, idx2);
			if (cmp != 0) return cmp;
			return Integer.compare(tabs1.size(), tabs2.size());
		}
	}
	
}
