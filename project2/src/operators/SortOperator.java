package operators;

import java.util.Comparator;
import java.util.HashSet;
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

	public class tupleComp implements Comparator<Tuple> {
		List<Integer> orders = null;
		HashSet<Integer> set = null;
		
		@Override
		public int compare(Tuple tp1, Tuple tp2) {
			for (int idx : orders) {
				int val1 = tp1.cols[idx];
				int val2 = tp2.cols[idx];
				if (val1 != val2) return Integer.compare(val1, val2);
			}
			
			for (int i = 0; i < tp1.cols.length && !set.contains(i); i++) {
				int val1 = tp1.cols[i];
				int val2 = tp2.cols[i];
				if (val1 != val2) return Integer.compare(val1, val2);
			}
			
			return 0;
		}
		
		public tupleComp(List<Integer> orders) {
			this.orders = orders;
			this.set = new HashSet<Integer>(orders);
		}
	}
	
}
