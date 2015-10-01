package operators;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItem;
import util.Helpers;
import util.Tuple;

public class ProjectOperator extends UnaryOperator {
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tp = child.getNextTuple();
		if (tp == null) return null;
		
		int[] cols = new int[schema.size()];
		int i = 0;
		for (String attr : schema)
			cols[i++] = (int) Helpers.getAttr(tp, attr, child.schema);
		
		return new Tuple(cols);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		child.reset();
	}

	public ProjectOperator(Operator child, List<SelectItem> sis) {
		this.child = child;
		
		List<String> chdScm = child.schema();
		List<String> tmpScm = new ArrayList<String>();
		HashSet<String> allTabCols = new HashSet<String>();
		
		for (SelectItem si : sis) {
			if (si instanceof AllColumns) {
				tmpScm = chdScm;
				break;
			}
			
			if (si instanceof AllTableColumns)
				allTabCols.add(si.toString().split(".")[0]);
			else {
				Column col = (Column) ((SelectExpressionItem) si).getExpression();
				String tab = col.getTable().toString();
				if (tab != null && !allTabCols.contains(tab)) {
					tmpScm.add(si.toString());
					continue;
				}
				
				String colName = col.getColumnName();
				for (String tabCol : chdScm) {
					if (tabCol.split(".")[2].equals(colName)) {
						tmpScm.add(tabCol);
						continue;
					}
				}
			}
		}
		
		if (allTabCols.isEmpty())
			schema = tmpScm;
		else {
			for (String tabCol : chdScm) {
				String tab = tabCol.split(".")[0];
				if (allTabCols.contains(tab) || tmpScm.contains(tabCol));
					schema.add(tabCol);
			}
		}
	}
	
}
