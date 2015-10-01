package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;

import net.sf.jsqlparser.statement.select.OrderByElement;
import util.Tuple;

public class SortOperator extends UnaryOperator {
	
	List<Integer> orders = new ArrayList<Integer>();
	public List<Tuple> tps = new ArrayList<Tuple>();
	private int curIdx = 0;
	
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		if (curIdx >= tps.size()) return null;
		return tps.get(curIdx++);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		curIdx = 0;
	}

	public void SortOpeartor(Operator child, List<OrderByElement> orders) {
		this.child = child;
		tbs = child.tbs;
		schema = child.schema;
		
		Tuple tp = null;
		while ((tp = child.getNextTuple()) != null) {
			tps.add(tp);
		}
		
		for (OrderByElement obe : orders)
			this.orders.add(schema.indexOf(obe.toString()));
		
		Collections.sort(tps, new tupleComp(this.orders));
	}
	
	public class tupleComp implements Comparator<Tuple> {
		List<Integer> orders = null;
		HashSet<Integer> set = null;
		
		@Override
		public int compare(Tuple tp1, Tuple tp2) {
			for (int idx : orders) {
				int val1 = tp1.cols[idx];
				int val2 = tp2.cols[idx];
				int cmp = Integer.compare(val1, val2);
				if (cmp != 0) return cmp;
			}
			
			for (int i = 0; i < tp1.cols.length; i++) {
				if (set.contains(i)) continue;
				int val1 = tp1.cols[i];
				int val2 = tp2.cols[i];
				int cmp = Integer.compare(val1, val2);
				if (cmp != 0) return cmp;
			}
			
			return 0;
		}
		
		public tupleComp(List<Integer> orders) {
			this.orders = orders;
			this.set = new HashSet<Integer>(orders);
		}
	}
	
}
