package cs4321.project1;

import cs4321.project1.tree.DivisionTreeNode;
import cs4321.project1.tree.LeafTreeNode;
import cs4321.project1.tree.SubtractionTreeNode;
import cs4321.project1.tree.AdditionTreeNode;
import cs4321.project1.tree.MultiplicationTreeNode;
import cs4321.project1.tree.UnaryMinusTreeNode;

/**
 * Tree visitor interface
 * 
 * @author Lucja Kot
 */
public interface TreeVisitor {
	void visit(LeafTreeNode node);

	void visit(UnaryMinusTreeNode node);

	void visit(AdditionTreeNode node);

	void visit(MultiplicationTreeNode node);

	void visit(SubtractionTreeNode minusNode);

	void visit(DivisionTreeNode divisionNode);
}
