package cs4321.project1;

import java.util.HashMap;
import java.util.Stack;

import cs4321.project1.list.*;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */


public class EvaluatePrefixListVisitor implements ListVisitor {
	Stack<Double> stack1; // for numerbers
	//Stack<> stack2; // for operand
	HashMap<ListNode, Integer> map; // used to keep track 
	
	public EvaluatePrefixListVisitor() {
		// TODO fill me in
		stack1 = new Stack<Double>();
		map =new HashMap();
		//stack2 = new Stack();
		
	}

	public double getResult() {
		// TODO fill me in
		return 42; // so that skeleton code compiles
	}

	@Override
	public void visit(NumberListNode node) {
		// TODO fill me in
		
	}

	@Override
	public void visit(AdditionListNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(SubtractionListNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(MultiplicationListNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(DivisionListNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(UnaryMinusListNode node) {
		// TODO fill me in
	}
}
