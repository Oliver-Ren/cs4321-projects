package visitors;

import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;

public class ConcreteExpVisitor extends AbstractExpVisitor {
	// the current numeric value holding in the visitor.
	private long currNumericValue = 0;
	// the current condition holding in the visitor.
	private boolean currCondition = false;
	
	/**
	 * Returns the final condition of the visitor.
	 * @return the current condition.
	 */
	public boolean getFinalCondition() {
		return currCondition;
	}
	
	/**
	 * Visit the expression of type LongValue.
	 * As LongValue is a leaf expression, set
	 * the the value of expression to current numeric value.
	 * 
	 * @param arg0 the expression of type LongValue.
	 */
	@Override
	public void visit(LongValue arg0) {
		currNumericValue = arg0.getValue();
	}

	/**
	 * Visit the expression of type AndExpression.
	 * After evaluating its left child, there should be a valid condition
	 * value produced by the left child expression. And so for the right child.
	 * The condition the visitor holds after evaluating this expression should 
	 * be (left condition == true && right condition == true).
	 * 
	 * @param arg0 the expression of type AndExpression.
	 */
	@Override
	public void visit(AndExpression arg0) {
		arg0.getLeftExpression().accept(this);
		boolean leftCondition = currCondition;
		arg0.getRightExpression().accept(this);
		boolean rightCondition = currCondition;
		// update the condition.
		currCondition = leftCondition && rightCondition;
	}

	@Override
	public void visit(Column arg0) {
		System.out.print(arg0.getColumnName());
		
	}

	/**
	 * Visit the expression of type EqualsTo.
	 * After evaluating its left child, there should be a valid numeric
	 * value produced by the left child expression. And so for the right child.
	 * The condition the visitor holds after evaluating this expression should 
	 * be (leftValue == rightValue).
	 * 
	 * @param arg0 the expression of type EqualsTo.
	 */
	@Override
	public void visit(EqualsTo arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = currNumericValue;
		arg0.getRightExpression().accept(this);
		long rightValue = currNumericValue;
		// update the condition.
		currCondition = (leftValue == rightValue);		
	}

	/**
	 * Visit the expression of type NotEqualsTo.
	 * After evaluating its left child, there should be a valid numeric
	 * value produced by the left child expression. And so for the right child.
	 * The condition the visitor holds after evaluating this expression should 
	 * be (leftValue != rightValue).
	 * 
	 * @param arg0 the expression of type NotEqualsTo.
	 */
	@Override
	public void visit(NotEqualsTo arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = currNumericValue;
		arg0.getRightExpression().accept(this);
		long rightValue = currNumericValue;
		// update the condition.
		currCondition = (leftValue != rightValue);	
	}

	/**
	 * Visit the expression of type GreaterThan.
	 * After evaluating its left child, there should be a valid numeric
	 * value produced by the left child expression. And so for the right child.
	 * The condition the visitor holds after evaluating this expression should 
	 * be (leftValue > rightValue).
	 * 
	 * @param arg0 the expression of type GreaterThan.
	 */
	@Override
	public void visit(GreaterThan arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = currNumericValue;
		arg0.getRightExpression().accept(this);
		long rightValue = currNumericValue;
		// update the condition.
		currCondition = (leftValue > rightValue);
	}

	/**
	 * Visit the expression of type GreaterThanEquals.
	 * After evaluating its left child, there should be a valid numeric
	 * value produced by the left child expression. And so for the right child.
	 * The condition the visitor holds after evaluating this expression should 
	 * be (leftValue >= rightValue).
	 * 
	 * @param arg0 the expression of type GreaterThanEquals.
	 */
	@Override
	public void visit(GreaterThanEquals arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = currNumericValue;
		arg0.getRightExpression().accept(this);
		long rightValue = currNumericValue;
		// update the condition.
		currCondition = (leftValue >= rightValue);
		
	}

	/**
	 * Visit the expression of type MinorThan.
	 * After evaluating its left child, there should be a valid numeric
	 * value produced by the left child expression. And so for the right child.
	 * The condition the visitor holds after evaluating this expression should 
	 * be (leftValue >= rightValue).
	 * 
	 * @param arg0 the expression of type MinorThan.
	 */
	@Override
	public void visit(MinorThan arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = currNumericValue;
		arg0.getRightExpression().accept(this);
		long rightValue = currNumericValue;
		// update the condition.
		currCondition = (leftValue < rightValue);
	}

	/**
	 * Visit the expression of type MinorThanEquals.
	 * After evaluating its left child, there should be a valid numeric
	 * value produced by the left child expression. And so for the right child.
	 * The condition the visitor holds after evaluating this expression should 
	 * be (leftValue >= rightValue).
	 * 
	 * @param arg0 the expression of type MinorThanEquals.
	 */
	@Override
	public void visit(MinorThanEquals arg0) {
		arg0.getLeftExpression().accept(this);
		long leftValue = currNumericValue;
		arg0.getRightExpression().accept(this);
		long rightValue = currNumericValue;
		// update the condition.
		currCondition = (leftValue <= rightValue);
	}

}
