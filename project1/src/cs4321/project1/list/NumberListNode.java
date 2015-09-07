package cs4321.project1.list;

import cs4321.project1.ListVisitor;

/**
 * Class for a node to represent a numeric value
 * 
 * @author Lucja Kot
 */

public class NumberListNode extends ListNode {

	protected double data; // value stored in node

	public NumberListNode(double data) {
		this.data = data; // set in constructor as will never change
	}

	/**
	 * Getter for value
	 * 
	 * @return numeric value in node
	 */
	public double getData() {
		return data;
	}

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
