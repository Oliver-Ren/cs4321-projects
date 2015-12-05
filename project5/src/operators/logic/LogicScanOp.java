package operators.logic;

import java.io.PrintStream;

import util.DBCat;
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

	@Override
	public String print() {
		return String.format("Leaf[%s]", DBCat.origName(tab.name));
	}

	@Override
	public void printTree(PrintStream ps, int lv) {
		printIndent(ps, lv);
		ps.println(print());
	}
	
}
