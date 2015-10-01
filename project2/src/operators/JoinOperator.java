package operators;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import util.Tuple;

public class JoinOperator extends BinaryOperator {

	Expression joinConds = null;
	Tuple curLeft = null, curRight = null;
	
	@Override
	public Tuple getNextTuple() {
		while (curLeft != null && curRight != null) {
			// visit the expression with curLeft, curRight,
			// left.schema(), right.schema() as parameters?
			// if true return curLeft joining curRight
			next();
		}
		
		return null;
	}

	@Override
	public void reset() {
		left.reset();
		right.reset();
	}

	public void next() {
		if (curLeft != null && curRight != null)
			return;
		if (curLeft == null) return;
		
		curLeft = left.getNextTuple();
		right.reset();
		curRight = right.getNextTuple();
	}
	
	public JoinOperator(Operator left, Operator right) {
		super(left, right);
		curLeft = left.getNextTuple();
		curRight = right.getNextTuple();
	}
	
}
