package operators;

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

	public ScanOperator(Table tab) {
		this.tab = tab;
		if (!tab.alias.isEmpty())
			tbs.add(tab.alias);
		else
			tbs.add(tab.name);
		schema = tab.schema;
	}
	
}
