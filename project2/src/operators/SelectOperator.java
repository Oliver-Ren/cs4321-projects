package operators;

import net.sf.jsqlparser.expression.Expression;
import util.Helpers;
import util.Tuple;
import visitors.SelExpVisitor;

/**
 * The select operator which filters tuples from 
 * its child according to the expression.
 * @author Guantian Zheng (gz94)
 *
 */
public class SelectOperator extends UnaryOperator {

	Expression exp = null;
	SelExpVisitor sv = null;
	
	/**
	 * Keep skipping until the tuple 
	 * satisfies the expression.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		Tuple tp = null;
		while ((tp = child.getNextTuple()) != null) {
			if (exp == null) return tp;
			if (Helpers.getSelRes(tp, exp, sv))
				return tp;
		}
		return null;
	}

	/**
	 * Construct a select operator.
	 * @param sop a scan operator as its child
	 * @param exp select conditions
	 */
	public SelectOperator(ScanOperator sop, Expression exp) {
		child = sop;
		this.exp = exp;
		sv = new SelExpVisitor(child.schema());
	}
	
}

