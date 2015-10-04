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

/**
 * The project operator selecting certain attributes 
 * from each tuple.
 * @author Guantian Zheng (gz94)
 *
 */
public class ProjectOperator extends UnaryOperator {
	
	/**
	 * Generate a projected tuple from the schema.
	 * @return the projected tuple
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple tp = child.getNextTuple();
		if (tp == null) return null;
		
		int[] cols = new int[schema.size()];
		int i = 0;
		for (String attr : schema) {
			Long val = Helpers.getAttr(tp, attr, child.schema());
			cols[i++] = val.intValue();
		}
		
		return new Tuple(cols);
	}

	/**
	 * Construct a project operator. Both AllColumns, AllTableColumns 
	 * and SelectExpressionItem are considered.
	 * @param child the child
	 * @param sis the list of selected columns
	 */
	public ProjectOperator(Operator child, List<SelectItem> sis) {
		super(child);
		
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
				if (col.toString().indexOf('.') != -1) {// if (col.getTable() != null) {
					String tab = col.toString().split("\\.")[0];
					if (allTabCols.contains(tab)) continue;
					tmpScm.add(col.toString());
				}
				else {
					String colName = col.getColumnName();
					for (String tabCol : chdScm) {
						if (Helpers.getColName(tabCol).equals(colName)) {
							tmpScm.add(tabCol);
							break;
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

