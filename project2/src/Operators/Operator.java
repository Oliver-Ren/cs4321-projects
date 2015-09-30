package operators;

import java.io.PrintStream;

import util.Tuple;

public abstract class Operator {
	
	public abstract Tuple getNextTuple();
	
	public abstract void reset();
	
	public void dump(PrintStream ps) {
		Tuple tp = null;
		while ((tp = getNextTuple()) != null) {
			tp.dump(ps);
		}
	}
	
}
