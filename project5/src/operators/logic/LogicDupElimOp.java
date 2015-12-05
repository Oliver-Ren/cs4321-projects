package operators.logic;

import java.io.PrintStream;

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

	@Override
	public String print() {
		return "DupElim";
	}
	
}
