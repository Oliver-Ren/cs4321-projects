package operators;

import java.util.List;

import util.Tuple;

public class JoinOperator extends BinaryOperator {

	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> schema() {
		// TODO Auto-generated method stub
		return null;
	}

	public JoinOperator(Operator left, Operator right) {
		super(left, right);
		// TODO Auto-generated constructor stub
	}
	
}
