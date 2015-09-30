package Operators;

import java.io.PrintStream;

import util.Tuple;

public abstract class Operator {

	public final static int numChd = 0;
	public Operator[] children = null;
	
	public abstract Tuple getNextTuple();
	
	public abstract void reset();
	
	public void dump(PrintStream ps) {
		Tuple tp = null;
		while ((tp = getNextTuple()) != null) {
			tp.print(ps);
		}
	}
	
}
