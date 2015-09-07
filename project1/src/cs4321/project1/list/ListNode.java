package cs4321.project1.list;

import cs4321.project1.ListVisitor;

/**
 * Abstract class for a node in a list (queue). The list will represent an
 * arithmetic expression in either prefix or postfix format.
 * 
 * @author Lucja Kot
 */
public abstract class ListNode {

	private ListNode next;

	public ListNode() {
		next = null;
	}

	/**
	 * List manipulation -- set successor
	 * 
	 * @param next
	 *            successor node
	 */
	public void setNext(ListNode next) {
		this.next = next;
	}

	/**
	 * List manipulation -- get successor
	 * 
	 * @return successor node
	 */
	public ListNode getNext() {
		return next;
	}

	/**
	 * Abstract method for accepting visitor
	 * 
	 * @param visitor
	 *            visitor to be accepted
	 */
	public abstract void accept(ListVisitor visitor);

}
