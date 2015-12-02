package operators.logic;

import operators.DuplicateEliminationOperator;
import operators.Operator;
import visitors.PhysicalPlanBuilder;

public class LogicDupElimOp extends LogicUnaryOp {

	public LogicDupElimOp(LogicOperator child) {
		super(child);
	}
	
	@Override
	public void accept(PhysicalPlanBuilder ppb) {
		ppb.visit(this);
	}
	
}
