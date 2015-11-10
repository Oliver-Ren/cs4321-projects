package operators.logic;

import util.Table;
import visitors.PhysicalPlanBuilder;

public class LogicScanOp extends LogicOperator {

	public Table tab = null;
	
	public LogicScanOp(Table tab) {
		this.tab = tab;
	}
	
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		ppb.visit(this);
	}
	
}
