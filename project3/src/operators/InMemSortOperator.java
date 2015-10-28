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
public class InMemSortOperator extends SortOperator {
	
	List<Tuple> tps = new ArrayList<Tuple>();
	private long curIdx = 0;
	
	/**
	 * Since the whole table is buffered in memory, 
	 * we can keep track of the next index to be read.
	 */
	@Override
	public Tuple getNextTuple() {
		// TODO Auto-generated method stub
		if (curIdx >= tps.size()) return null;
		return tps.get((int) curIdx++);
	}

	/**
	 * Zero the current index.
	 */
	@Override
	public void reset() {
		curIdx = 0;
	}

	@Override
	public void reset(long idx) {
		if (idx < 0 || idx >= tps.size())
			return;
		curIdx = idx;
	}
	
	/**
	 * Construct a sort operator.
	 * @param child its child
	 * @param orders the list of attributes to be ordered
	 */
	public InMemSortOperator(Operator child, List<OrderByElement> orders) {
		super(child, orders);
		Tuple tp = null;
		while ((tp = child.getNextTuple()) != null)
			tps.add(tp);
		Collections.sort(tps, tpCmp);
	}
	
}

