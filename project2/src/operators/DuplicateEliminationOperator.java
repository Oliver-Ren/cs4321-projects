package operators;

import util.Tuple;

public class DuplicateEliminationOperator extends Operator {

	SortOperator child = null;
	Tuple last = null;
	
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
			Tuple ret = last;
			last = tp;
			return ret;
		}
	}

	@Override
	public void reset() {
		child.reset();
	}

	public DuplicateEliminationOperator(SortOperator sop) {
		child = sop;
	}
	
}
