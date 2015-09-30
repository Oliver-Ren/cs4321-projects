package cs4321.project1;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

/**
 * the function of this class is to pre traversal a expression tree,
 * and convert it to a List. At the end, getResult() function would return
 * the head of the list. 
 * @author Lucja Kot
 * @author Mingyuan Huang mh2239
 */
public class BuildPrefixExpressionTreeVisitor implements TreeVisitor {
	private ListNode dummy;
	private ListNode dummy1;
	/**
	 * constructor for the class, initialize two dummy nodes. 
	 * dummy node would continuously set its next node and change to 
	 * its next node.
	 * dummy1 node points to the precedence of the list head.
	 */
	public BuildPrefixExpressionTreeVisitor() {
		// TODO fill me in
		dummy = new AdditionListNode(); // this is a whatever node
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
	 */
	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
		NumberListNode n = new NumberListNode(node.getData());
		dummy.setNext(n);
		dummy = dummy.getNext();
		
	}
	
	/**
	 * convert the unary minus tree node to a unary minus list node
	 * first add it to the list, then traverse its child
	 * @param unary minus tree node
	 */
	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		UnaryMinusListNode n = new UnaryMinusListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getChild().accept(this);
		
	}
	/**
	 * convert the addition tree node to a addition list node
	 * first add it to the list, then traverse its left child, then right child
	 * @param addition tree node
	 */
	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
		AdditionListNode n = new AdditionListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}
	/**
	 * convert the multiplication tree node to a multiplication list node
	 * first add it to the list, then traverse its left child, then right child
	 * @param multiplication tree node
	 */
	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
		MultiplicationListNode n = new MultiplicationListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}
	/**
	 * convert the subtraction tree node to a subtraction list node
	 * first add it to the list, then traverse its left child, then right child
	 * @param addition tree node
	 */
	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
		SubtractionListNode n = new SubtractionListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}
	/**
	 * convert the division tree node to a division list node
	 * first add it to the list, then traverse its left child, then right child
	 * @param division  tree node
	 */
	@Override
	public void visit(DivisionTreeNode node) {
		// TODO fill me in
		DivisionListNode n = new DivisionListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}

}
