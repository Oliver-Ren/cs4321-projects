package operators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import util.Helpers;
import util.Tuple;

/**
 * The sort operator which acts according to 
 * the order by elements.
 * @author Guantian Zheng (gz94)
 *
 */
public class SortOperator extends UnaryOperator {
	
	List<Tuple> tps = new ArrayList<Tuple>();
	List<Integer> orders = new ArrayList<Integer>();
	private int curIdx = 0;
	
	/**
	 * Since the whole table is buffered in memory, 
	 * we can keep track of the next index to be read.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		if (curIdx >= tps.size()) return null;
		return tps.get(curIdx++);
	}

	/**
	 * Zero the current index.
	 */
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		curIdx = 0;
	}

	/**
	 * Construct a sort operator.
	 * @param child its child
	 * @param orders the list of attributes to be ordered
	 */
	public SortOperator(Operator child, List<OrderByElement> orders) {
		this.child = child;
		
		Tuple tp = null;
		while ((tp = child.getNextTuple()) != null) {
			tps.add(tp);
		}
		
		for (OrderByElement obe : orders)
			this.orders.add(Helpers.getAttrIdx(tp, obe.toString(), child.schema()));
		
		Collections.sort(tps, new tupleComp(this.orders));
	}
	
	/**
	 * A comparator for tuples. It compares the tupes in 
	 * the specified order.
	 * @author Guantian Zheng (gz94)
	 *
	 */
	public class tupleComp implements Comparator<Tuple> {
		List<Integer> orders = null;
		HashSet<Integer> set = null;
		
		@Override
		public int compare(Tuple tp1, Tuple tp2) {
			if (tp1.length() != tp2.length())
				try {
					throw new Exception("Comparing tuples of different lengths");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return 0;
				}
			
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

