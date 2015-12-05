package operators;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import nio.TupleWriter;
import util.Tuple;

/**
 * The abstract class of all operators.
 * @author Guantian Zheng (gz94)
 *
 */
public abstract class Operator {
	
	/**
	 * Return the next valid tuple.
	 * @return the next tuple
	 */
	public abstract Tuple getNextTuple();
	
	/**
	 * Reset the operator to the start.
	 */
	public abstract void reset();
	
	/**
	 * Return the schema of the current table.
	 * @return the schema as a list of strings
	 */
	public abstract List<String> schema();
	
	protected List<String> schema = null;
	
	/**
	 * Dump every tuple to the print stream.
	 * @param ps the print stream
	 */
	public void dump(PrintStream ps) {
		Tuple tp = null;
		while ((tp = getNextTuple()) != null)
			tp.dump(ps);
	}
	
	public void dump(TupleWriter tw) {
		Tuple tp = null;
		while ((tp = getNextTuple()) != null)
			try {
				tw.write(tp);
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
	}

	// print out the indentation.
	protected static void printIndent(PrintStream ps, int lv) {
		while (lv-- > 0)
			ps.print('-');
	}
	
	/**
	 * Prints this operator
	 * @return
	 */
	public abstract String print();
	
	/**
	 * Print the plan
	 * @param ps
	 * @param lv
	 */
	public abstract void printTree(PrintStream ps, int lv);
}
