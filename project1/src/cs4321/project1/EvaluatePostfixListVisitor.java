package cs4321.project1;

import java.util.Stack;

import cs4321.project1.list.DivisionListNode;
import cs4321.project1.list.SubtractionListNode;
import cs4321.project1.list.NumberListNode;
import cs4321.project1.list.AdditionListNode;
import cs4321.project1.list.MultiplicationListNode;
import cs4321.project1.list.UnaryMinusListNode;

/**
 *This class evaluates the postfix list expression, and then output the final
 *value of the expression.
 * @author Lucja Kot
 * @author Mingyuan Huang
 */
public class EvaluatePostfixListVisitor implements ListVisitor {
	private Stack<Double> stack;
	/**
	 * constructor of the calss, it initiates a new of type double 
	 * for the object
	 */
	public EvaluatePostfixListVisitor() {
		// TODO fill me in
		stack = new Stack<Double>();
		
	}
	/**
	 * it returns the final result of the expression 
	 * @return double (the result of the expression)
	 */
	public double getResult() {
		// TODO fill me in
		
		return stack.pop(); // so that skeleton code compiles
	}
	/**
	 * it evaluate a numberListNode, push the value to the stack
	 * if it has a next node, then visit it otherwise return
	 * @param NumberListNode
	 */
	@Override
	public void visit(NumberListNode node) {
		// TODO fill me in
		if(node.getNext()!=null) {
			stack.push(node.getData());
			node.getNext().accept(this);
		} else {
			stack.push(node.getData());
		}  // A: maybe not needed, since the last node should always be an operand
		
	}
	/**
	 * it evaluate a AdditionListNode, pop two values from the stack
	 * and sum them up, then push it back to the stack
	 * if it has a next node, go visit it otherwise return
	 * @param AdditionListNode
	 */
	@Override
	public void visit(AdditionListNode node) {
		// TODO fill me in
		if(node.getNext()!=null) {
			double second = stack.pop();
			double first = stack.pop();
			first = first + second;
			stack.push(first);
			node.getNext().accept(this);
		} else {
			double second = stack.pop();
			double first = stack.pop();
			first = first + second;
			stack.push(first);
		}
	}
	/**
	 * it evaluate a subtractionNode, pop two values from the stack
	 * and do the subtraction, then push the result back to the stack
	 * if it has a next node, go visit it otherwise return
	 * @param SubtractionListNode
	 */
	@Override
	public void visit(SubtractionListNode node) {
		// TODO fill me in
		if(node.getNext()!=null) {
			double second = stack.pop();
			double first = stack.pop();
			first = first - second;
			stack.push(first);
			node.getNext().accept(this);
		} else {
			double second = stack.pop();
			double first = stack.pop();
			first = first - second;
			stack.push(first);
		}
	}
	/**
	 * it evaluate a MultiplicationListnNode, pop two values from the stack
	 * and do the multiplication of two valus, then push result back to the stack
	 * if it has a next node, go visit it otherwise return
	 * @param MultiplicationListNode
	 */
	@Override
	public void visit(MultiplicationListNode node) {
		// TODO fill me in
		if(node.getNext()!=null) {
			double second = stack.pop();
			double first = stack.pop();
			first = first * second;
			stack.push(first);
			node.getNext().accept(this);
		} else {
			double second = stack.pop();
			double first = stack.pop();
			first = first * second;
			stack.push(first);
		}
	}
	/**
	 * it evaluate a divisionListnNode, pop two values from the stack
	 * and do the division of two valus, then push result back to the stack
	 * if it has a next node, go visit it otherwise return
	 * @param divisonListNode
	 */
	@Override
	public void visit(DivisionListNode node) {
		// TODO fill me in
		if(node.getNext()!=null) {
			double second = stack.pop();
			double first = stack.pop();
			first = first / second;
			stack.push(first);
			node.getNext().accept(this);
		} else {
			double second = stack.pop();
			double first = stack.pop();
			first = first / second;
			stack.push(first);
		}
	}
	/**
	 * it evaluate a UnaryMinusListnNode, pop one value from the stack
	 * and add minus sign to it, then push result back to the stack
	 * if it has a next node, go visit it otherwise return
	 * @param UnaryMinusListNode
	 */
	@Override
	public void visit(UnaryMinusListNode node) {
		// TODO fill me in
		if(node.getNext()!=null) {
			double first = stack.pop();
			first = -first ;
			stack.push(first);
			node.getNext().accept(this);
		} else {
			double first = stack.pop();
			first = -first;
			stack.push(first);
		}
	}

}
