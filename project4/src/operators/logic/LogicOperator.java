package operators.logic;

import operators.Operator;
import visitors.PhysicalPlanBuilder;

public abstract class LogicOperator {

	public abstract void accept(PhysicalPlanBuilder ppb);
	
}
