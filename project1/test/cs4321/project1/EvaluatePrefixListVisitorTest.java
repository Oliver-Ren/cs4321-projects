package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.list.*;

public class EvaluatePrefixListVisitorTest {
	
	private static final double DELTA = 1e-15;

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(1.0, v1.getResult(), DELTA);
	}

	@Test
	public void testAdditionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n3.accept(v1);
		assertEquals(3.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new AdditionListNode();
		n6.setNext(n5);
		n5.setNext(n4);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(3.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionMultipleInstances() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		ListNode n4 = new NumberListNode(3.0);
		ListNode n5 = new AdditionListNode();
		n5.setNext(n4);
		n4.setNext(n3);
		n3.setNext(n2);
		n2.setNext(n1);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n5.accept(v1);
		assertEquals(6.0, v1.getResult(), DELTA);
	}

	/**
	 * a very simple test case for Subtraction.
	 */
	@Test
	public void testSubtractionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new SubtractionListNode();
		n3.setNext(n1);
		n1.setNext(n2);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n3.accept(v1);
		assertEquals(-1.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new SubtractionListNode();
		n6.setNext(n5);
		n5.setNext(n4);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(1.0, v2.getResult(), DELTA);
	}

	/**
	 * a very simple test case for Multiplication.
	 */
	@Test
	public void testMultiplicationSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new MultiplicationListNode();
		n3.setNext(n1);
		n1.setNext(n2);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n3.accept(v1);
		assertEquals(2.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new MultiplicationListNode();
		n6.setNext(n5);
		n5.setNext(n4);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}

	/**
	 * a very simple test case for Division.
	 */
	@Test
	public void testDivisionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new DivisionListNode();
		n3.setNext(n1);
		n1.setNext(n2);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n3.accept(v1);
		assertEquals(0.5, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new DivisionListNode();
		n6.setNext(n5);
		n5.setNext(n4);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}


	/**
	 * simple negative number.
	 */
	@Test
	public void testSimpleNegNumber() {
		ListNode n1 = new NumberListNode(2.0);
		ListNode n2 = new UnaryMinusListNode();
		n2.setNext(n1);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n2.accept(v1);
		assertEquals(-2.0, v1.getResult(), DELTA);
	}

	/**
	 * Test case which contains multiple negative signs.
	 */
	@Test
	public void testUnaryMultipleMinusNode() {
		ListNode n1 = new NumberListNode(29431.22);
        ListNode[] list = new ListNode[10];
        list[9] = new UnaryMinusListNode();
        list[9].setNext(n1);
        for (int i = 8; i >= 0; i--) {
            list[i] = new UnaryMinusListNode();
            list[i].setNext(list[i + 1]);
        }

		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(29431.22, v1.getResult(), DELTA);
	}

	/** Simple Addition with unary minus. 
	 *  infix: -(A + (-B)), -((-A) + B))
	 *  prefix: ~ + A ~ B, ~ + ~ A B
	 */
	@Test
	public void testAdditionSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new UnaryMinusListNode(); 
		ListNode n2 = new AdditionListNode(); 
		ListNode n3 = new NumberListNode(num1); 
		ListNode n4 = new UnaryMinusListNode();
		ListNode n5 = new NumberListNode(num2); 
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 + (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new UnaryMinusListNode(); 
		ListNode n7 = new AdditionListNode(); 
		ListNode n8 = new UnaryMinusListNode();
		ListNode n9 = new NumberListNode(num1); 
		ListNode n10 = new NumberListNode(num2); 
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(-(-num1 + num2), v2.getResult(), DELTA);
	}

}
