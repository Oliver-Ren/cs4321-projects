package operators;

import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import util.Tuple;

public class SortOperator extends Operator {

	public List<OrderByElement> orders;
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

}
