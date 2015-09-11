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
	Stack<wrapper> stack2; // for operand
	
	private class wrapper {
		private ListNode node;
		private int num ;
		
		public wrapper(ListNode n,int i) {
			node = n;
			num = i;
		}
		public void decreVal() {
			num--;
		}
	}
	
	public EvaluatePrefixListVisitor() {
		// TODO fill me in
		stack1 = new Stack<Double>();
		stack2 = new Stack<wrapper>();
		
	}

	public double getResult() {
		// TODO fill me in
		return stack1.pop(); // so that skeleton code compiles
	}

	@Override
	public void visit(NumberListNode node) {
		// TODO fill me in
		//System.out.println(node.getData());
		stack1.push(node.getData());
		if(!stack2.isEmpty()) {
			wrapper w = stack2.peek();
			//System.out.println(stack2.peek().num);
			w.decreVal();  // whether value in stack also change? 
			
			if(w.num!=0) {
				//stack2.push(w);
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
	private void helper() {
		if(stack2.isEmpty()) {
			return;
		}
		
		double second = stack1.pop();
		double first =0;  
		if(!stack1.isEmpty()) {  // in case of unary list node 
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

	@Override
	public void visit(AdditionListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
		
	}

	@Override
	public void visit(SubtractionListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
	}

	@Override
	public void visit(MultiplicationListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
	}

	@Override
	public void visit(DivisionListNode node) {
		// TODO fill me in
		wrapper w = new wrapper(node,2);
		stack2.push(w);
		if(node.getNext()!=null) {
			node.getNext().accept(this);
		}
	}

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
