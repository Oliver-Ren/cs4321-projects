package operators;

import java.util.ArrayList;
import java.util.List;

import util.Table;
import util.Tuple;

public class ScanOperator extends Operator {

	Table tab = null;
	
	@Override
	public Tuple getNextTuple() {
		return tab.nextTuple();
	}

	@Override
	public void reset() {
		tab.reset();
	}

	@Override
	public List<String> schema() {
		return schema;
	}
	
	public ScanOperator(Table tab) {
		this.tab = tab;
		schema = new ArrayList<String>();
		for (String col : tab.schema) {
			schema.add(tab.name + '.' + col);
		}
	}
	
}
