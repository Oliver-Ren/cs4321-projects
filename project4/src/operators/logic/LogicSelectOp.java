package operators.logic;

import net.sf.jsqlparser.expression.Expression;
import visitors.PhysicalPlanBuilder;

public class LogicSelectOp extends LogicUnaryOp {

	public Expression exp = null;
	
	public LogicSelectOp(LogicOperator child, Expression exp) {
		super(child);
		this.exp = exp;
	}
	
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		ppb.visit(this);
	}
	
}
