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
		for (String attr : schema) {
			Long val = Helpers.getAttr(tp, attr, child.schema);
			cols[i++] = val.intValue();
		}
		
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
				schema = chdScm;
				return;
			}
			
			if (si instanceof AllTableColumns)
				allTabCols.add(si.toString().split(".")[0]);
			else {
				Column col = (Column) ((SelectExpressionItem) si).getExpression();
				if (col.getTable() != null) {
					String tab = col.getTable().toString();
					if (allTabCols.contains(tab)) continue;
					tmpScm.add(si.toString());
				}
				else {
					String colName = col.getColumnName();
					for (String tabCol : chdScm) {
						if (tabCol.split(".")[1].equals(colName)) {
							tmpScm.add(tabCol);
							continue;
						}
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

