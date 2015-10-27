package operators;

import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import util.Tuple;

public class ExternSortOperator extends SortOperator {

	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void reset() {
		//TODO
	}
	
	public ExternSortOperator(Operator child, List<OrderByElement> orders) {
		super(child, orders);
		// TODO Auto-generated constructor stub
	}

}
