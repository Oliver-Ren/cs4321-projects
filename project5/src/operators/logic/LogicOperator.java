package operators.logic;

import java.io.PrintStream;

import operators.Operator;
import visitors.PhysicalPlanBuilder;

public abstract class LogicOperator {

	protected static void printIndent(PrintStream ps, int lv) {
		while (lv-- > 0)
			ps.print('-');
	}
	
	public abstract String print();
	public abstract void printTree(PrintStream ps, int lv);
	
	public abstract void accept(PhysicalPlanBuilder ppb);
	
}
