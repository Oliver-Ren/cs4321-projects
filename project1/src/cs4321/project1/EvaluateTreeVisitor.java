package cs4321.project1;

import cs4321.project1.tree.DivisionTreeNode;
import cs4321.project1.tree.LeafTreeNode;
import cs4321.project1.tree.SubtractionTreeNode;
import cs4321.project1.tree.AdditionTreeNode;
import cs4321.project1.tree.MultiplicationTreeNode;
import cs4321.project1.tree.UnaryMinusTreeNode;

/**
 * Provide a comment about what your class does and the overall logic
 * 
 * @author Your names and netids go here
 */

public class EvaluateTreeVisitor implements TreeVisitor {

	public EvaluateTreeVisitor() {
		// TODO fill me in
	}

	public double getResult() {
		// TODO fill me in
		return 42; // so that skeleton code compiles
	}

	@Override
	public void visit(LeafTreeNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(UnaryMinusTreeNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(AdditionTreeNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(MultiplicationTreeNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(SubtractionTreeNode node) {
		// TODO fill me in
	}

	@Override
	public void visit(DivisionTreeNode node) {
		// TODO fill me in
	}
}
