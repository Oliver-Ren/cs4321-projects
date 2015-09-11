package cs4321.project1;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

/**
 * The function of this class is to post traversal a expression tree,
 * and convert it to a List. At the end, getResult() function would return
 * the head of the list. 
 * 
 * @autho  Lucja Kot
 * @author Mingyuan Huang mh2239
 */
public class BuildPostfixExpressionTreeVisitor implements TreeVisitor {
	private ListNode dummy1;
	private ListNode dummy;
	/**
	 * constructor for the class, initialize two dummy nodes. 
	 * dummy node would continuously set its next node and change to 
	 * its next node.
	 * dummy1 node points to the precedence of the list head.
	 */
	public BuildPostfixExpressionTreeVisitor() {
		// TODO fill me in
		dummy = new AdditionListNode(); // this is just a dummy node
		dummy1 =dummy;
	}
	/**
	 * return the head of the list
	 * @return ListNode (the head of the list)
	 */
	public ListNode getResult() {
		// TODO fill me in
		
		return dummy1.getNext();
	}
	/**
	 * convert the leaf tree node to list node 
	 * add the value of the tree leaf node to the list
	 * @param LeafTreeNode
	 * 
	 */
	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
		NumberListNode n = new NumberListNode(node.getData());
		dummy.setNext(n);// 
		dummy =dummy.getNext();
		
	}
	/**
	 * convert the unary minus tree node to a unary minus list node
	 * and add it the list 
	 * @param unaryMinusTreeNode
	 */
	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		UnaryMinusListNode n = new UnaryMinusListNode();
		node.getChild().accept(this);		
		dummy.setNext(n);
		dummy = dummy.getNext();
	}
	/**
	 * convert the addition tree node to a addition list node
	 * first traverse its left child then its right child.
	 * then add it the list 
	 * @param addition tree node
	 */
	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
		AdditionListNode n = new AdditionListNode();
		node.getLeftChild().accept(this);;
		node.getRightChild().accept(this);
		dummy.setNext(n);
		dummy=dummy.getNext();
	}
	/**
	 * convert the multiplication tree node to a multiplication list node
	 * first traverse its left child then its right child.
	 * then add it the list 
	 * @param multiplication tree node
	 */
	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
		MultiplicationListNode n = new MultiplicationListNode();
		node.getLeftChild().accept(this);;
		node.getRightChild().accept(this);
		dummy.setNext(n);
		dummy=dummy.getNext();
		
	}
	/**
	 * convert the subtraction tree node to a subtraction list node
	 * first traverse its left child then its right child.
	 * then add it the list 
	 * @param subtraction tree node
	 */
	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
		SubtractionListNode n = new SubtractionListNode();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		dummy.setNext(n);
		dummy=dummy.getNext();
		
		
	}
	/**
	 * convert the division tree node to a division list node
	 * first traverse its left child then its right child.
	 * then add it the list 
	 * @param division tree node
	 */
	@Override
	public void visit(DivisionTreeNode node) {
		// TODO fill me in
		DivisionListNode n = new DivisionListNode();
		node.getLeftChild().accept(this);;
		node.getRightChild().accept(this);
		dummy.setNext(n);
		dummy=dummy.getNext();
		
	}

}
