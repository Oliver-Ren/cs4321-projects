package operators;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import util.Tuple;

public abstract class Operator {
	
	public abstract Tuple getNextTuple();
	
	public abstract void reset();
	
	public abstract List<String> schema();
	
	public HashSet<String> tbs = new HashSet<String>();
	public List<String> schema = null;
	
	public void dump(PrintStream ps) {
		Tuple tp = null;
		while ((tp = getNextTuple()) != null) {
			tp.dump(ps);
		}
	}
	
}
