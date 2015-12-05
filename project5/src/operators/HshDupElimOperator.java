package operators;

import java.util.HashSet;

import util.Tuple;

/**
 * Duplicate eliminator using hashset. This is only invoked 
 * when the ordered attributes are not projected.
 * @author Guantian Zheng (gz94)
 *
 */
public class HshDupElimOperator extends UnaryOperator {

	private HashSet<Tuple> set = new HashSet<Tuple>();
	
	@Override
	public Tuple getNextTuple() {
		Tuple tp = null;
		while ((tp = child.getNextTuple()) != null) {
			if (set.contains(tp)) continue;
			set.add(tp);
			return tp;
		}
		
		return null;
	}

	public HshDupElimOperator(Operator child) {
		super(child);
	}

	@Override
	public String print() {
		// TODO Auto-generated method stub
		return "HashDupElim";
	}
	
}
