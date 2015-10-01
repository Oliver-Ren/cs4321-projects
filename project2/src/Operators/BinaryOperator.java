package operators;


/**
 * The <tt>BinaryOperator</tt> abstract class represents operators in the query
 * plan which have left and right child.
 * 
 * @author Chengxiang Ren
 *
 */
public abstract class BinaryOperator extends Operator {
	public Operator left, right; // the binary operator has left and right child.
	
	public BinaryOperator(Operator left, Operator right) {
		
		// schema = left.schema.addAll(right.schema());
	}
}
