package cs4321.project1;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */
public class BuildPostfixExpressionTreeVisitor implements TreeVisitor {
	ListNode dummy1;
	ListNode dummy;
	public BuildPostfixExpressionTreeVisitor() {
		// TODO fill me in
		dummy = new AdditionListNode(); // this is just a dummy node
		dummy1 =dummy;
	}

	public ListNode getResult() {
		// TODO fill me in
		
		return dummy1.getNext();
	}

	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
		NumberListNode n = new NumberListNode(node.getData());
		dummy.setNext(n);// 
		dummy =dummy.getNext();
		
	}

	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		UnaryMinusListNode n = new UnaryMinusListNode();
		node.getChild().accept(this);		
		dummy.setNext(n);
		dummy = dummy.getNext();
	}

	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
		AdditionListNode n = new AdditionListNode();
		node.getLeftChild().accept(this);;
		node.getRightChild().accept(this);
		dummy.setNext(n);
		dummy=dummy.getNext();
	}

	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
		MultiplicationListNode n = new MultiplicationListNode();
		node.getLeftChild().accept(this);;
		node.getRightChild().accept(this);
		dummy.setNext(n);
		dummy=dummy.getNext();
		
	}

	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
		SubtractionListNode n = new SubtractionListNode();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		dummy.setNext(n);
		dummy=dummy.getNext();
		
		
	}

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
