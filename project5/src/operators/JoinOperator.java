package operators;

import net.sf.jsqlparser.expression.Expression;
import util.Helpers;
import util.Tuple;
import visitors.JoinExpVisitor;

public abstract class JoinOperator extends BinaryOperator {

	protected Tuple curLeft = null, curRight = null;
	
	protected Expression exp = null;
	protected JoinExpVisitor jv = null;
	
	/**
	 * Concatenate two tuples.
	 * @param tp1 the first tuple
	 * @param tp2 the second tuple
	 * @return the joined tuple
	 */
	protected Tuple joinTp(Tuple tp1, Tuple tp2) {
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
			if (exp == null || 
					Helpers.getJoinRes(curLeft, curRight, exp, jv))
				ret = joinTp(curLeft, curRight);
			next();
			if (ret != null) return ret;
		}
		
		return null;
	}
	
	protected abstract void next();
	
	public JoinOperator(Operator left, Operator right, Expression exp) {
		super(left, right);
		this.exp = exp;
		jv = new JoinExpVisitor(left.schema(), right.schema());
	}
	
	

}
