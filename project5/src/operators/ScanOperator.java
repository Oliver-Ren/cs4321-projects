package operators;

import java.util.ArrayList;
import java.util.List;

import util.DBCat;
import util.Table;
import util.Tuple;

/**
 * The scan operator which talks directly to
 * a Table instance.
 * @author Guantian Zheng (gz94)
 *
 */
public class ScanOperator extends Operator {

	Table tab = null;
	
	/**
	 * Tell the table to deliver the next tuple.
	 */
	@Override
	public Tuple getNextTuple() {
		return tab.nextTuple();
	}

	/**
	 * Reset the operator is equivalent to resetting 
	 * its table.
	 */
	@Override
	public void reset() {
		tab.reset();
	}

	/**
	 * Every column of the schema is appended 
	 * with the table's name (or alias if used) 
	 * plus a dot
	 */
	@Override
	public List<String> schema() {
		return schema;
	}
	
	/**
	 * Construct a scan operator from a table.
	 * @param tab the relevant table
	 */
	public ScanOperator(Table tab) {
		this.tab = tab;
		schema = new ArrayList<String>();
		if (tab == null || tab.schema == null){
			System.out.println("table / schema empty!");
		}
		for (String col : tab.schema) {
			schema.add(tab.name + '.' + col);
		}
	}
	
	@Override
	public String print() {
		return "TableScan[" + DBCat.origName(tab.name) + "]";
	}
	
}
