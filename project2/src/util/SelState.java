package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import operators.*;

/**
 * A class which parses the select statement 
 * and builds the operator tree.
 * @author Guantian Zheng (gz94)
 *
 */
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
	
	/**
	 * Return a table according to its index in the FROM clause.
	 * @param idx the index
	 * @return the table
	 */
	private Table getTable(int idx) {
		return DBCat.getTable(froms.get(idx));
	}
	
	/**
	 * The max index of a list of tables in FROM.
	 * @param tabs the list of tables
	 * @return the last index
	 */
	private int lastIdx(List<String> tabs) {
		if (tabs == null) return froms.size() - 1;
		int idx = 0;
		for (String tab : tabs) {
			idx = Math.max(idx, froms.indexOf(tab));
		}
		return idx;
	}

	/**
	 * Get the select condition of the idx'th
	 * table.
	 * @param idx the index
	 * @return the final select condition
	 */
	private Expression getSelCond(int idx) {
		return fnSelCond.get(froms.get(idx));
	}
	
	/**
	 * Get the join condition of the idx'th
	 * table with its precedents in FROM.
	 * @param idx the index
	 * @return the join condition
	 */
	private Expression getJoinCond(int idx) {
		return fnJoinCond.get(froms.get(idx));
	}
	
	/**
	 * Build the operator tree according to conditions in fnSelCond 
	 * and fnJoinCond.
	 */
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
			if (orders != null)
				curRoot = new SortOperator(curRoot, orders);
			curRoot = new DuplicateEliminationOperator(curRoot);
		}
		
		root = curRoot;
	}
	
	/**
	 * Constructor. It extracts all the binary expressions and 
	 * analyze the relevant ones at each joining stage.
	 * @param st the SQL statement
	 */
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

