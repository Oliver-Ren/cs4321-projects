package cs4321.project1;

import cs4321.project1.list.*;

/**
 * The class prints out the each list node in the list in the form:
 * "1.0 2.0 +", "/ 2.0 1.0".
 * The purpose of this class is for debugging.
 * @author Lucja Kot
 * @author Mingyuan Huang
 */
public class PrintListVisitor implements ListVisitor {
	private String result;
	/**
	 * the constructor of the class, it initiates a string for the result.
	 */
	public PrintListVisitor() {
		// TODO fill me in
		result="";
	}
	/**
	 * It returns the list of nodes in a string format.
	 * @return String
	 */
	public String getResult() {
		// TODO fill me in
		
		return result;
	}
	/**
	 * it handles NumberListNode by adding the value of the node to the result 
	 * @param: NumberListNode node
	 */
	@Override
	public void visit(NumberListNode node) {
		// TODO fill me in
		if(node.getNext()!=null) {
			result+= node.getData() +" " ;
			node.getNext().accept(this);
			
		} else {
			result += node.getData();			
		}		
	}
	/**
	 * it handles AdditionListNode by adding the plus sign to the result 
	 * @param: AdditionListNode node
	 */
	@Override
	public void visit(AdditionListNode node) {
		// TODO fill me in
			
		if(node.getNext()!=null) {
			
			result+= "+ ";
			node.getNext().accept(this);
		} else {
			
			result+="+";			
		}				
	}
	/**
	 * it handles subtractionListNode by adding the minus sign to the result 
	 * @param: subtractionListNode node
	 */
	@Override
	public void visit(SubtractionListNode node) {
		// TODO fill me in		
		if(node.getNext()!=null) {			
			result+="- ";
			node.getNext().accept(this);
		} else {
			
			result+="-";			
		}
	}
	/**
	 * it handles multiplicationListNode by adding the plus sign to the result 
	 * @param: multiplicationListNode node
	 */
	@Override
	public void visit(MultiplicationListNode node) {
		// TODO fill me in		
		if(node.getNext()!=null) {		
			result+="* ";
			node.getNext().accept(this);
		} else {
			
			result+="*";			
		}		
	}
	/**
	 * it handles divisonListNode by adding the divison sign to the result 
	 * @param: divisionListNode node
	 */
	@Override
	public void visit(DivisionListNode node) {
		// TODO fill me in
		
		if(node.getNext()!=null) {
			
			result+="/ ";
			node.getNext().accept(this);
		} else {
			
			result+="/";
			
		}
	}
	/**
	 * it handlesUnaryMinusListNode by adding the ~ sign to the result 
	 * @param: UnaryMinusListNode node
	 */
	@Override
	public void visit(UnaryMinusListNode node) {
		// TODO fill me in
		
		if(node.getNext()!=null) {
			
			result+="~ ";
			node.getNext().accept(this);
		} else {
			
			result+="~";			
		}

	}

}
