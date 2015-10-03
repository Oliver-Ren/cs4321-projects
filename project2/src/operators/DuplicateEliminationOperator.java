package operators;

import util.Tuple;

/**
 * Operator dealing with DISTINCT.
 * @author Guantian Zheng (gz94)
 *
 */
public class DuplicateEliminationOperator extends UnaryOperator {

	Tuple last = null;
	
	/**
	 * The current tuple is checked against the last 
	 * one returned. If equal skip it.
	 * @return the next nun-duplicate tuple
	 */
	@Override
	public Tuple getNextTuple() {
		if (last == null) {
			last = child.getNextTuple();
			return last;
		}
		else {
			Tuple tp = null;
			while ((tp = child.getNextTuple()) != null)
				if (!tp.equals(last)) break;
			last = tp;
			return tp;
		}
	}

	/**
	 * The child needs to be a sort operator.
	 * @param sop a sort operator
	 */
	public DuplicateEliminationOperator(Operator sop) {
		child = sop;
	}
	
}

