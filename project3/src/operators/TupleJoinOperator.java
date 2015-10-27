package operators;

import net.sf.jsqlparser.expression.Expression;
import util.Helpers;
import util.Tuple;
import visitors.JoinExpVisitor;

/**
 * Join operator is a subclass of binary operator.
 * @author Guantian Zheng (gz94)
 *
 */
public class TupleJoinOperator extends JoinOperator {

	/**
	 * Fix the left tuple and move on to the next right tuple.
	 * If the end of right is reached, reset it and get the 
	 * next left tuple.
	 */
	@Override
	protected void next() {
		if (curLeft == null) return;
		
		if (curRight != null)
			curRight = right.getNextTuple();
		
		if (curRight == null) {
			curLeft = left.getNextTuple();
			right.reset();
			curRight = right.getNextTuple();
		}
	}
	
	/**
	 * Construct a join operator.
	 * @param left the left operator
	 * @param right the right operator
	 * @param exp the join condition
	 */
	public TupleJoinOperator(Operator left, Operator right, Expression exp) {
		super(left, right, exp);
		curLeft = left.getNextTuple();
		curRight = right.getNextTuple();
	}
	
}

