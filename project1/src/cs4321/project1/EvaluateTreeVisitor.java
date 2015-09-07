package cs4321.project1;

import java.util.Stack;

import cs4321.project1.tree.DivisionTreeNode;
import cs4321.project1.tree.LeafTreeNode;
import cs4321.project1.tree.SubtractionTreeNode;
import cs4321.project1.tree.AdditionTreeNode;
import cs4321.project1.tree.MultiplicationTreeNode;
import cs4321.project1.tree.TreeNode;
import cs4321.project1.tree.UnaryMinusTreeNode;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */

public class EvaluateTreeVisitor implements TreeVisitor {
	Stack<Double> stack ;
	public EvaluateTreeVisitor() {
		// TODO fill me in
		 stack = new Stack<Double>();
	}

	public double getResult() {
		// TODO fill me in
		
		return stack.pop(); // so that skeleton code compiles
	}

	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
		this.stack.push(node.getData());
	}

	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		node.getChild().accept(this);
		double num = stack.pop();
		stack.push(-num);
		
		
	}

	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double num2 = stack.pop();
		double num1 = stack.pop();
		num1 = num1 + num2;
		stack.push(num1);
		
		
		
	}

	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double num1 = stack.pop();
		double num2 = stack.pop();
		num1 = num1 * num2;
		stack.push(num1);
		
		
	}

	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double num2 = stack.pop();
		double num1 = stack.pop();
		num1 = num1 - num2;
		stack.push(num1);
	}

	@Override
	public void visit(DivisionTreeNode node) {
		// TODO fill me in
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		double num2 = stack.pop();
		double num1 = stack.pop();
		num1 = num1 / num2;
		stack.push(num1);	
	}
}
