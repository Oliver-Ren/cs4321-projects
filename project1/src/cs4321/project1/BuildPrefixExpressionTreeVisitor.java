package cs4321.project1;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */
public class BuildPrefixExpressionTreeVisitor implements TreeVisitor {
	ListNode dummy;
	ListNode dummy1;
	public BuildPrefixExpressionTreeVisitor() {
		// TODO fill me in
		dummy = new AdditionListNode(); // this is a whatever node
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
		dummy.setNext(n);
		dummy = dummy.getNext();
		
	}

	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
		UnaryMinusListNode n = new UnaryMinusListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getChild().accept(this);
		
	}

	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
		AdditionListNode n = new AdditionListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}

	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
		MultiplicationListNode n = new MultiplicationListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}

	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
		SubtractionListNode n = new SubtractionListNode();
		dummy.setNext(n);
		dummy = dummy.getNext();
		node.getLeftChild().accept(this);
		node.getRightChild().accept(this);
		
	}

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
