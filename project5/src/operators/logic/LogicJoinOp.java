package operators.logic;

import java.io.PrintStream;

import net.sf.jsqlparser.expression.Expression;
import operators.Operator;
import visitors.PhysicalPlanBuilder;

public class LogicJoinOp extends LogicBinaryOp {

	public Expression exp = null;
	
	public LogicJoinOp(LogicOperator left, LogicOperator right, 
			Expression exp) {
		super(left, right);
		this.exp = exp;
	}
	
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		ppb.visit(this);
	}

	@Override
	public String print() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void printTree(PrintStream ps, int lv) {
		throw new UnsupportedOperationException();
	}
	
}
