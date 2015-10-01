package operators;

/**
 * The <tt>UnaryOperator</tt> abstract class represents operators in the query
 * plan which only have one child.
 * 
 * @author Chengxiang Ren
 *
 */
public abstract class UnaryOperator extends Operator {
	public Operator child;	// the unary operator only has one child.
}
