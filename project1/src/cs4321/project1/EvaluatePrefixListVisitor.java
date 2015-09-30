package cs4321.project1;

import java.util.HashMap;
import java.util.Stack;

import cs4321.project1.list.*;

/**
 *This class evaluates the prefix list expression, and then output the final
 *value of the expression. In order to evalue the expression from left to right, two
 *stacks are needed stack1 for operands and stack2 for operators. A inner wrapper class is 
 *also implemented to group the operator and a double value together, in order to keep
 *track the operands being pushed to the stack1. 
 * @author Lucja Kot
 * @author Mingyuan Huang
 */
public class EvaluatePrefixListVisitor implements ListVisitor {
	private Stack<Double> stack1; // for numerbers
	private Stack<wrapper> stack2; // for operand
	/**
	 * The wapper class to wrapper the opperator and an int value together, so 
	 * that we can tell whether we need to evaluate the top operator in stack2 
	 * @param ListNode, double 
	 */
	private class wrapper {
		private ListNode node;
		private int num ;
		/**
		 * constructor
		 * @param ListNode n
		 * @param int i
		 */
		public wrapper(ListNode n,int i) {
			node = n;
			num = i;
		}
		/**
		 * decrease the value of num by 1 every time it's called
		 */
		public void decreVal() {
			num--;
		}
	}
	/**
	 * the constructor of the outer class, it initialize two stacks, one 
	 * is to store opperand, stack2 is to store operator and int pair 
	 */
	public EvaluatePrefixListVisitor() {
		// TODO fill me in
		stack1 = new Stack<Double>();
		stack2 = new Stack<wrapper>();
		
	}
	/**
	 * return the value of the list expression
	 * @return double 
	 */
	public double getResult() {
		// TODO fill me in
		return stack1.pop(); // so that skeleton code compiles
	}
	/**
	 * The function is used to evaluate a numberListNode.First it push the 
	 * node value into stack2, second if there is operator inside stack2,
	 * decrement it's int value by one. Then check if the int value reaches 0 or not.
	 * if it reaches 0, call helper function to evaluate, if not go to next node. 
	 * @param NUmberListNode
	 */
	@Override
	public void visit(NumberListNode node) {
		// TODO fill me in
		stack1.push(node.getData());
		if(!stack2.isEmpty()) {
			wrapper w = stack2.peek();
			//System.out.println(stack2.peek().num);
			w.decreVal();  // whether value in stack also change? 
			
			if(w.num!=0) {				
				if(node.getNext()!=null) {
					node.getNext().accept(this);
				}								
			} else { // this time we need to evaluate the operator
				
				//System.out.println(stack2.peek().num);
				helper();
				if(node.getNext()!=null) {
					node.getNext().accept(this);					
				}				
			}			
		}		
	}
	/**
	 * The helper function takes care of the evaluation of operators.
	 * The helper function would stop recursively call itself only when there
	 * is no operator or the operator pair's num value does not equal zero.
	 * 
	 * First, it pops two elements from stack1, if the operator is not unaryMinusNode, 
	 * otherwise it pops one element from stack1.
	 * Second, we check the operator type and perform the corresponding evaluation,say 
	 * addition of two numbers. 
	 * Third, we push the result back to stack1, and decrease num value of the top operator 
	 * pair in stack2 if stack2 is not empty. Then we recursively call helper()  
	 */
	private void helper() {
		if(stack2.isEmpty()) {
			return;
		}
		if(stack2.peek().num!=0) {
			return;
		}
		
		double second = stack1.pop();
		double first =0;  
		// in case of unary list node 
		if(!(stack2.peek().node instanceof UnaryMinusListNode)) {  
			first = stack1.pop();
		}		
		wrapper w= stack2.pop();
		if(w.node instanceof AdditionListNode) {
			first = first + second;
			stack1.push(first);
			if(!stack2.isEmpty()) {
				w = stack2.peek();
				w.decreVal();
			}
		} else if (w.node instanceof DivisionListNode) {
			first = first/second;
			stack1.push(first);
			if(!stack2.isEmpty()) {
				w =stack2.peek();
				w.decreVal();				
			}
		} else if (w.node instanceof MultiplicationListNode) {
			first = first*second;
			stack1.push(first);
			if(!stack2.isEmpty()) {
				w =stack2.peek();
				w.decreVal();
			}
		} else if (w.node instanceof SubtractionListNode) {
			first = first - second;
			stack1.push(first);
			if(!stack2.isEmpty()) {
				w =stack2.peek();
				w.decreVal();
			}
		} else if (w.node instanceof UnaryMinusListNode) {
			second = -second;
			stack1.push(second);
			if(!stack2.isEmpty()) {
				w=stack2.peek();
				w.decreVal();
			}
		}
		helper();		
	}
	/**
	 * The function handles additionlistNode case. It wrappers the addition list node
	 * with a integer 2, and push it to stack2. Then visit its next node.
	 * @param AdditionListNode
	 */
	@Override
	public void visit(AdditionListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
		
	}
	/**
	 * The function handles subtractionlistNode case. It wrappers the subtractionListNode
	 * with a integer 2, and push it to stack2. Then visit its next node.
	 * @param SubtractionListNode
	 */
	@Override
	public void visit(SubtractionListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
	}
	/**
	 * The function handles multiplicationlistNode case. It wrappers the 
	 * multiplicationListNode with a integer 2, and push it to stack2. 
	 * Then visit its next node.
	 * @param MultiplicationListNode
	 */
	@Override
	public void visit(MultiplicationListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
	}
	/**
	 * The function handles divisionlistNode case. It wrappers the 
	 * divisionListNode with a integer 2, and push it to stack2. 
	 * Then visit its next node.
	 * @param divisionListNode
	 */
	@Override
	public void visit(DivisionListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
	}
	/**
	 * The function handles UnaryMinuslistNode case. It wrappers the 
	 * UnaryMinusListNode with a integer 1, and push it to stack2. 
	 * Then visit its next node.
	 * @param UnaryMinusListNode
	 */
	@Override
	public void visit(UnaryMinusListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,1);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
	}
}
