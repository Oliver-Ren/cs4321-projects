package operators.logic;

import java.io.PrintStream;

public abstract class LogicUnaryOp extends LogicOperator {

	public LogicOperator child = null;
	
	public LogicUnaryOp(LogicOperator child) {
		this.child = child;
	}
	
	@Override
	public void printTree(PrintStream ps, int lv) {
		printIndent(ps, lv);
		ps.println(print());
		child.printTree(ps, lv + 1);
	}
	
}
