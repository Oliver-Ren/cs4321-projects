package operators.logic;

public abstract class LogicUnaryOp extends LogicOperator {

	public LogicOperator child = null;
	
	public LogicUnaryOp(LogicOperator child) {
		this.child = child;
	}
	
}
