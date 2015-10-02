package operators;

import java.util.ArrayList;
import java.util.List;

/**
 * The <tt>BinaryOperator</tt> abstract class represents operators in the query
 * plan which have left and right child.
 * 
 * @author Chengxiang Ren
 *
 */
public abstract class BinaryOperator extends Operator {
	public Operator left, right; // the binary operator has left and right child.
	
	public List<String> schema() {
		return schema;
	}
	
	public BinaryOperator(Operator left, Operator right) {
		schema = new ArrayList<String>(left.schema());
		schema.addAll(right.schema());
	}
}
