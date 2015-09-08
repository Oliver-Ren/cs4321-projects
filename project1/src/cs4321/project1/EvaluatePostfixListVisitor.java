package cs4321.project1;

import java.util.Stack;

import cs4321.project1.list.DivisionListNode;
import cs4321.project1.list.SubtractionListNode;
import cs4321.project1.list.NumberListNode;
import cs4321.project1.list.AdditionListNode;
import cs4321.project1.list.MultiplicationListNode;
import cs4321.project1.list.UnaryMinusListNode;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */
public class EvaluatePostfixListVisitor implements ListVisitor {
	Stack<Double> stack;
	public EvaluatePostfixListVisitor() {
		// TODO fill me in
		stack = new Stack<Double>();
		
	}

	public double getResult() {
		// TODO fill me in
		
		return stack.pop(); // so that skeleton code compiles
	}

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
