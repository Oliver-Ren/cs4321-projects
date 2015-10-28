package operators;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import net.sf.jsqlparser.statement.select.OrderByElement;
import util.Helpers;
import util.Tuple;

public abstract class SortOperator extends UnaryOperator {

	List<Integer> orders = new ArrayList<Integer>();
	TupleComp tpCmp = null;
	
	public SortOperator(Operator child, List<OrderByElement> orders) {
		super(child);
		for (OrderByElement obe : orders) {
			this.orders.add(Helpers.getAttrIdx(
					obe.toString(), child.schema()));
		}
		tpCmp = new TupleComp(this.orders);
	}
	
	public abstract void reset(long idx);
	
	/**
	 * A comparator for tuples. It compares the tuples in 
	 * the specified order.
	 * @author Guantian Zheng (gz94)
	 *
	 */
	public class TupleComp implements Comparator<Tuple> {
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
		
		public TupleComp(List<Integer> orders) {
			this.orders = orders;
			this.set = new HashSet<Integer>(orders);
		}
	}

}
