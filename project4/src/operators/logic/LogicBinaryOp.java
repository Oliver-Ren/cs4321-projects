package operators.logic;

public abstract class LogicBinaryOp extends LogicOperator {

	public LogicOperator left = null, right = null;
	
	public LogicBinaryOp(LogicOperator left, LogicOperator right) {
		this.left = left;
		this.right = right;
	}
	
}
