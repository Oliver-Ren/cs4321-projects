package cs4321.project1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import cs4321.project1.list.*;

import org.junit.Test;

public class EvaluatePostfixListVisitorTest {

	private static final double DELTA = 1e-15;

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(1.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(3.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new AdditionListNode();
		n5.setNext(n4);
		n4.setNext(n6);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n5.accept(v2);
		assertEquals(3.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionMultipleInstances() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		ListNode n4 = new NumberListNode(3.0);
		ListNode n5 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5); //expression is 1 2 + 3 + 
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(6.0, v1.getResult(), DELTA);
	}

	/**
	 * simple Subtraction.
	 */
	@Test
	public void testSubtractionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new SubtractionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(-1.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new SubtractionListNode();
		n5.setNext(n4);
		n4.setNext(n6);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n5.accept(v2);
		assertEquals(1.0, v2.getResult(), DELTA);
	}

	/**
	 * simple Multiplication.
	 */
	@Test
	public void testMultiplicationSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new MultiplicationListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(2.0, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new MultiplicationListNode();
		n5.setNext(n4);
		n4.setNext(n6);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n5.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}


	/**
	 * simple Division.
	 */
	@Test
	public void testDivisionSimple() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new DivisionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(0.5, v1.getResult(), DELTA);
		
		ListNode n4 = new NumberListNode(1.0);
		ListNode n5 = new NumberListNode(2.0);
		ListNode n6 = new DivisionListNode();
		n5.setNext(n4);
		n4.setNext(n6);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n5.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}

	/**
	 * simple negative number.
	 */
	@Test
	public void testSimpleNegNumber() {
		ListNode n1 = new NumberListNode(2.0);
		ListNode n2 = new UnaryMinusListNode();
		n1.setNext(n2);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(-2.0, v1.getResult(), DELTA);
	}

	
	/** Simple Addition with unary minus. 
	 *  infix: -(A + (-B)), -((-A) + B))
	 *  postfix: A B ~ + ~, A ~ B + ~
	 */
	@Test
	public void testAdditionSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new NumberListNode(num1);
		ListNode n2 = new NumberListNode(num2);
		ListNode n3 = new UnaryMinusListNode(); 
		ListNode n4 = new AdditionListNode();
		ListNode n5 = new UnaryMinusListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 + (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new NumberListNode(num1);
		ListNode n7 = new UnaryMinusListNode();
		ListNode n8 = new NumberListNode(num2);
		ListNode n9 = new AdditionListNode();
		ListNode n10 = new UnaryMinusListNode();
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n6.accept(v2);
		assertEquals(-(-num1 + num2), v2.getResult(), DELTA);
	}

	/** Simple Subtraction with unary minus. 
	 *  -(A - (-B)), -((=A) - B))
	 */
	@Test
	public void testSubtractionSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new NumberListNode(num1);
		ListNode n2 = new NumberListNode(num2);
		ListNode n3 = new UnaryMinusListNode(); 
		ListNode n4 = new SubtractionListNode();
		ListNode n5 = new UnaryMinusListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 - (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new NumberListNode(num1);
		ListNode n7 = new UnaryMinusListNode();
		ListNode n8 = new NumberListNode(num2);
		ListNode n9 = new SubtractionListNode();
		ListNode n10 = new UnaryMinusListNode();
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n6.accept(v2);
		assertEquals(-(-num1 - num2), v2.getResult(), DELTA);
	}

	/** Simple Multiplication with unary minus. 
	 *  -(A * (-B)), -((-A) * B))
	 */
	@Test
	public void testMultiplicationSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new NumberListNode(num1);
		ListNode n2 = new NumberListNode(num2);
		ListNode n3 = new UnaryMinusListNode(); 
		ListNode n4 = new MultiplicationListNode();
		ListNode n5 = new UnaryMinusListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 * (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new NumberListNode(num1);
		ListNode n7 = new UnaryMinusListNode();
		ListNode n8 = new NumberListNode(num2);
		ListNode n9 = new MultiplicationListNode();
		ListNode n10 = new UnaryMinusListNode();
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n6.accept(v2);
		assertEquals(-(-num1 * num2), v2.getResult(), DELTA);
	}

	/** Simple Division with unary minus. 
	 *  -(A / (-B)), -((-A) / B))
	 */
	@Test
	public void testDivisionSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new NumberListNode(num1);
		ListNode n2 = new NumberListNode(num2);
		ListNode n3 = new UnaryMinusListNode(); 
		ListNode n4 = new DivisionListNode();
		ListNode n5 = new UnaryMinusListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 / (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new NumberListNode(num1);
		ListNode n7 = new UnaryMinusListNode();
		ListNode n8 = new NumberListNode(num2);
		ListNode n9 = new DivisionListNode();
		ListNode n10 = new UnaryMinusListNode();
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePostfixListVisitor v2 = new EvaluatePostfixListVisitor();
		n6.accept(v2);
		assertEquals(-(-num1 / num2), v2.getResult(), DELTA);
	}

	/**
	 * Test case which contains multiple negative signs.
	 */
	@Test
	public void testUnaryMultipleMinusNode() {
		ListNode n1 = new NumberListNode(29431.22);
        ListNode[] list = new ListNode[10];
        list[9] = new UnaryMinusListNode();
        n1.setNext(list[0]);
        for (int i = 8; i >= 0; i--) {
            list[i] = new UnaryMinusListNode();
            list[i].setNext(list[i + 1]);
        }

		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		n1.accept(v1);
		assertEquals(29431.22, v1.getResult(), DELTA);
	}

	/**
	 * A complex Postfix Test
	 */
	@Test
	public void testComplexPostfix1() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;
		
		ListNode na = new NumberListNode(a);
		ListNode nb = new NumberListNode(b);
		ListNode nc = new NumberListNode(c);
		ListNode nd = new NumberListNode(d);

        ListNode opAdd = new AdditionListNode();
        ListNode opMul = new MultiplicationListNode();
        ListNode opDiv = new DivisionListNode();

        na.setNext(nb);
        nb.setNext(opMul);
        opMul.setNext(nc);
        nc.setNext(nd);
        nd.setNext(opDiv);
        opDiv.setNext(opAdd);

        
		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		na.accept(v1);
		assertEquals((a * b) + (c / d), v1.getResult(), DELTA);

	}

	/**
	 * Another complex Postfix Test
	 * infix: ((A * (B + C)) / D)
	 * postfix: A B C + * D /
	 */
	@Test
	public void testComplexPostfix2() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;
		List<ListNode> list = new ArrayList<ListNode>();
		list.add(new NumberListNode(a));
		list.add(new NumberListNode(b));
		list.add(new NumberListNode(c));
		list.add(new AdditionListNode());
		list.add(new MultiplicationListNode());
		list.add(new NumberListNode(d));
		list.add(new DivisionListNode());

		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).setNext(list.get(i + 1));
		}

		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		list.get(0).accept(v1);
		assertEquals(((a * (b + c)) / d), v1.getResult(), DELTA);
	}

	/**
	 * Complex Postfix Test
	 * infix: (a / b) * ((-(c * d)) - (-(e + a)))
	 * postfix: a b / c d * ~ e a + ~ - *
	 */
	@Test
	public void testComplexPostfix3() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;
		double e = 293.12;
		List<ListNode> list = new ArrayList<ListNode>();
		list.add(new NumberListNode(a));
		list.add(new NumberListNode(b));
		list.add(new DivisionListNode());
		list.add(new NumberListNode(c));
		list.add(new NumberListNode(d));
		list.add(new MultiplicationListNode());
		list.add(new UnaryMinusListNode());
		list.add(new NumberListNode(e));
		list.add(new NumberListNode(a));
		list.add(new AdditionListNode());
		list.add(new UnaryMinusListNode());
		list.add(new SubtractionListNode());
		list.add(new MultiplicationListNode());

		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).setNext(list.get(i + 1));
		}

		EvaluatePostfixListVisitor v1 = new EvaluatePostfixListVisitor();
		list.get(0).accept(v1);
		assertEquals((a / b) * ((-(c * d)) - (-(e + a))), 
				v1.getResult(), DELTA);
	}


		


}
