package operators;

import java.io.PrintStream;
import java.util.List;

/**
 * The <tt>UnaryOperator</tt> abstract class represents operators in the query
 * plan which only have one child.
 * 
 * @author Chengxiang Ren
 *
 */
public abstract class UnaryOperator extends Operator {
	public Operator child;	// the unary operator only has one child.
	
	/**
	 * For unary operator reset means 
	 * resetting its child.
	 */
	public void reset() {
		child.reset();
	}
	
	/**
	 * Return if the instance's own schema exists;
	 * else its child must provide the schema.
	 */
	public List<String> schema() {
		if (this.schema != null)
			return this.schema;
		return child.schema();
	}
	
	/**
	 * Constructor.
	 * @param child the child
	 */
	public UnaryOperator(Operator child) {
		this.child = child;
	}
	
	@Override
	public void printTree(PrintStream ps, int lv) {
		printIndent(ps, lv);
		ps.println(print());
		child.printTree(ps, lv + 1);
	}
}
