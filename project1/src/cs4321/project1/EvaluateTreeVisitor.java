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
 * This function pretraverses a tree, and evalues the numbers and operators
 * in each of the tree node, and then return the final result.
 * 
 * @author Lucja Kot
 * @author Mingyuan Huang mh2239
 */
public class EvaluateTreeVisitor implements TreeVisitor {
	private Stack<Double> stack ;
	/**
	 * constructor of the class, it initiate a stack of type double.
	 */
	public EvaluateTreeVisitor() {
		// TODO fill me in
		 stack = new Stack<Double>();
	}
	/**
	 * returns the final result of the tree expression
	 * @return double
	 */
	public double getResult() {
		// TODO fill me in
		
		return stack.pop(); // so that skeleton code compiles
	}
	/**
	 * It handles leafTreeNode case. It pushes the value of the node
	 * to the stack.
	 * @param LeafTreeNode 
	 */
	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
		this.stack.push(node.getData());
	}
	/**
	 * It handles UnaryMinusTreeNode case. It traverse its child, until gets to the leaf
	 * node, then it pops the value from the stack, and add minus sign to it.
	 * Finally push it back to the stack.
	 * @param UnaryMinusTreeNode 
	 */
	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		node.getChild().accept(this);
		double num = stack.pop();
		stack.push(-num);
		
		
	}
	/**
	 * It handles AdditionsTreeNode case. It first traverses its left child, then 
	 * traverses its right child until gets to the leaf  node, then it pops two elements
	 * from the stack,and do the addition. 
	 * Finally push it back to the stack.
	 * @param UnaryMinusTreeNode 
	 */
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
	/**
	 * It handles MultiplicationTreeNode case. It first traverses its left child, then 
	 * traverses its right child until gets to the leaf  node, then it pops two elements
	 * from the stack,and do the multiplication. 
	 * Finally push it back to the stack.
	 * @param MultiplicationTreeNode 
	 */
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
	/**
	 * It handles SubtractionTreeNode case. It first traverses its left child, then 
	 * traverses its right child until gets to the leaf  node, then it pops two elements
	 * from the stack,and do the subtraction. 
	 * Finally push it back to the stack.
	 * @param subtractionTreeNode 
	 */
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
	/**
	 * It handles divisionTreeNode case. It first traverses its left child, then 
	 * traverses its right child until gets to the leaf  node, then it pops two elements
	 * from the stack,and do the division. 
	 * Finally push it back to the stack.
	 * @param divisionTreeNode 
	 */
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
