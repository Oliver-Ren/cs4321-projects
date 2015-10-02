package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import operators.*;

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
	public HashMap<String, List<Expression>> selConds = null, joinConds = null;
	public HashMap<String, Expression> fnSelCond = null, fnJoinCond = null;
	
	public Operator root = null;
	
	private Table getTable(int idx) {
		return DBCat.getTable(froms.get(idx));
	}
	
	private int lastIdx(List<String> tabs) {
		if (tabs == null) return froms.size() - 1;
		int idx = 0;
		for (String tab : tabs) {
			idx = Math.max(idx, froms.indexOf(tab));
		}
		return idx;
	}
	
<<<<<<< HEAD
	private void genWhereConds() {
		if (ands.isEmpty()) return;
		
		int i = 0, j = 0;
		while (j < ands.size()) {
			List<String> tabs = Helpers.getTabs(ands.get(j));
			if (lastIdx(tabs) != 0 || tabs.size() > 1)	//QUESTION
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
=======
	private Expression getSelCond(int idx) {
		return fnSelCond.get(froms.get(idx));
>>>>>>> 41f3c694d49e9c2798b7113d9ea5c299cd0e86d0
	}
	
	private Expression getJoinCond(int idx) {
		return fnJoinCond.get(froms.get(idx));
	}
	
	private void buildOpTree() {
		Operator curRoot = new ScanOperator(getTable(0));
		if (getSelCond(0) != null)
			curRoot = new SelectOperator((ScanOperator) curRoot, getSelCond(0));
		
		for (int i = 1; i < froms.size(); i++) {
			Operator newOp = new ScanOperator(getTable(i));
			if (getSelCond(i) != null)
				newOp = new SelectOperator((ScanOperator) newOp, getSelCond(i));
			curRoot = new JoinOperator(curRoot, newOp, getJoinCond(i));
		}
		
		if (orders != null)
			curRoot = new SortOperator(curRoot, orders);
		if (sels != null)
			curRoot = new ProjectOperator(curRoot, sels);
		if (dist != null) {
			curRoot = new SortOperator(curRoot, new ArrayList<OrderByElement>());
			curRoot = new DuplicateEliminationOperator((SortOperator) curRoot);
		}
		
		root = curRoot;
	}
	
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
			DBCat.aliases.put(from.getAlias(), Helpers.getFromTab(from));
			froms.add(from.getAlias());
		}
		else
			froms.add(from.toString());
		
		if (joins != null) {
			for (Join join : joins) {
				FromItem ri = join.getRightItem();
				if (ri.getAlias() != null) {
					DBCat.aliases.put(ri.getAlias(), Helpers.getFromTab(ri));
					froms.add(ri.getAlias());
				}
				else
					froms.add(ri.toString());
			}
		}
		
		selConds = new HashMap<String, List<Expression>>();
		joinConds = new HashMap<String, List<Expression>>();
		for (String tab : froms) {
			selConds.put(tab, new ArrayList<Expression>());
			joinConds.put(tab, new ArrayList<Expression>());
		}
		
		ands = Helpers.decompAnds(where);
		for (Expression exp : ands) {
			List<String> tabs = Helpers.getExpTabs(exp);
			int idx = lastIdx(tabs);
			if (tabs == null)
				joinConds.get(froms.get(froms.size() - 1)).add(exp);
			else if (tabs.size() <= 1)
				selConds.get(froms.get(idx)).add(exp);
			else
				joinConds.get(froms.get(idx)).add(exp);
		}

		fnSelCond = new HashMap<String, Expression>();
		fnJoinCond = new HashMap<String, Expression>();
		for (String tab : froms) {
			fnSelCond.put(tab, Helpers.genAnds(selConds.get(tab)));
			fnJoinCond.put(tab, Helpers.genAnds(joinConds.get(tab)));
		}
		
		buildOpTree();
		
		selConds.clear(); joinConds.clear();
		fnSelCond.clear(); fnJoinCond.clear();
	}
	
}

