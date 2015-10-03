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
public class JoinOperator extends BinaryOperator {

	Expression exp = null;
	Tuple curLeft = null, curRight = null;
	JoinExpVisitor jv = null;
	
	/**
	 * Concatenate two tuples.
	 * @param tp1 the first tuple
	 * @param tp2 the second tuple
	 * @return the joined tuple
	 */
	private Tuple joinTp(Tuple tp1, Tuple tp2) {
		int[] cols = new int[tp1.length() + tp2.length()];
		int i = 0;
		for (int val : tp1.cols)
			cols[i++] = val;
		for (int val : tp2.cols)
			cols[i++] = val;
		
		return new Tuple(cols);
	}
	
	/**
	 * Keep the tuple nested loop until a valid pair 
	 * is found.
	 */
	@Override
	public Tuple getNextTuple() {
		Tuple ret = null;
		
		while (curLeft != null && curRight != null) {
			if (exp == null)
				ret = joinTp(curLeft, curRight);
			else
				if (Helpers.getJoinRes(curLeft, curRight, exp, jv))
					ret = joinTp(curLeft, curRight);			
			next();
			if (ret != null) return ret;
		}
		
		return null;
	}

	/**
	 * Fix the left tuple and move on to the next right tuple.
	 * If the end of right is reached, reset it and get the 
	 * next left tuple.
	 */
	public void next() {
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
	public JoinOperator(Operator left, Operator right, Expression exp) {
		super(left, right);
		this.exp = exp;
		curLeft = left.getNextTuple();
		curRight = right.getNextTuple();
		jv = new JoinExpVisitor(null, left.schema(), null, right.schema());
	}
	
}

