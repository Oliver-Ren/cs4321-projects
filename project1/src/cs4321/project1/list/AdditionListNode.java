package cs4321.project1.list;

import cs4321.project1.ListVisitor;

/**
 * Class for a node to represent an addition operator
 * 
 * @author Lucja Kot
 */

public class AdditionListNode extends ListNode {

	/**
	 * Method for accepting visitor; just calls back visitor, logic of traversal
	 * will be handled in visitor method (to maintain consistency with the
	 * cs4321.project.tree package)
	 * 
	 * @param visitor
	 *            visitor to be accepted
	 */
	@Override
	public void accept(ListVisitor visitor) {
		visitor.visit(this);

	}

}
