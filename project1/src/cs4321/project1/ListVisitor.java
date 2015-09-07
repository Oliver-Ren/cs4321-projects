package cs4321.project1;

import cs4321.project1.list.*;

/**
 * List visitor interface
 * 
 * @author Lucja Kot
 */
public interface ListVisitor {
	void visit(NumberListNode node);

	void visit(AdditionListNode plusListNode);

	void visit(SubtractionListNode minusListNode);

	void visit(MultiplicationListNode timesListNode);

	void visit(DivisionListNode divisionListNode);

	void visit(UnaryMinusListNode unaryMinusListNode);
}
