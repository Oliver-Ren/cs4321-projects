package cs4321.project1;

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

	public EvaluatePostfixListVisitor() {
		// TODO fill me in
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
