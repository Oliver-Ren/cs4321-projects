package util;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
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
		if (from.getAlias() != null)
			DBCat.aliases.put(from.getAlias(), Helpers.getTabName(from));
		for (Join join : joins) {
			FromItem ri = join.getRightItem();
			DBCat.aliases.put(ri.getAlias(), Helpers.getTabName(ri));
		}
	}
	
}
