package cs4321.project1;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
	 * @author Chengxiang Ren (cr486).
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
	 * @author Chengxiang Ren (cr486).
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
	 * @author Chengxiang Ren (cr486).
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
	 * @author Chengxiang Ren (cr486).
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
	 * @author Chengxiang Ren (cr486).
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
	 * @author Chengxiang Ren (cr486).
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

	/** Simple Subtraction with unary minus. 
	 *  infix: -(A - (-B)), -((-A) - B))
	 *  prefix: ~ - A ~ B, ~ - ~ A B
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testSubtractionSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new UnaryMinusListNode(); 
		ListNode n2 = new SubtractionListNode(); 
		ListNode n3 = new NumberListNode(num1); 
		ListNode n4 = new UnaryMinusListNode();
		ListNode n5 = new NumberListNode(num2); 
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 - (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new UnaryMinusListNode(); 
		ListNode n7 = new SubtractionListNode(); 
		ListNode n8 = new UnaryMinusListNode();
		ListNode n9 = new NumberListNode(num1); 
		ListNode n10 = new NumberListNode(num2); 
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(-(-num1 - num2), v2.getResult(), DELTA);
	}

	/** Simple Multiplication with unary minus. 
	 *  infix: -(A * (-B)), -((-A) * B))
	 *  prefix: ~ * A ~ B, ~ * ~ A B
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testMultiplicationSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new UnaryMinusListNode(); 
		ListNode n2 = new MultiplicationListNode(); 
		ListNode n3 = new NumberListNode(num1); 
		ListNode n4 = new UnaryMinusListNode();
		ListNode n5 = new NumberListNode(num2); 
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 * (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new UnaryMinusListNode(); 
		ListNode n7 = new MultiplicationListNode(); 
		ListNode n8 = new UnaryMinusListNode();
		ListNode n9 = new NumberListNode(num1); 
		ListNode n10 = new NumberListNode(num2); 
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(-((-num1) * num2), v2.getResult(), DELTA);
	}

	/** Simple Division with unary minus. 
	 *  infix: -(A / (-B)), -((-A) / B))
	 *  prefix: ~ / A ~ B, ~ / ~ A B
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testDivisionSimpleWithUnaryMinus() {
		double num1 = 23.18;
		double num2 = 382.93;
		ListNode n1 = new UnaryMinusListNode(); 
		ListNode n2 = new DivisionListNode(); 
		ListNode n3 = new NumberListNode(num1); 
		ListNode n4 = new UnaryMinusListNode();
		ListNode n5 = new NumberListNode(num2); 
		n1.setNext(n2);
		n2.setNext(n3);
		n3.setNext(n4);
		n4.setNext(n5);
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		n1.accept(v1);
		assertEquals(-(num1 / (-num2)), v1.getResult(), DELTA);
		
		ListNode n6 = new UnaryMinusListNode(); 
		ListNode n7 = new DivisionListNode(); 
		ListNode n8 = new UnaryMinusListNode();
		ListNode n9 = new NumberListNode(num1); 
		ListNode n10 = new NumberListNode(num2); 
		n6.setNext(n7);
		n7.setNext(n8);
		n8.setNext(n9);
		n9.setNext(n10);
		EvaluatePrefixListVisitor v2 = new EvaluatePrefixListVisitor();
		n6.accept(v2);
		assertEquals(-((-num1) / num2), v2.getResult(), DELTA);
	}

	/**
	 * A complex Prefix Test
	 * infix: (a * b) + (c / d) 
	 * Prefix: + * a b / c d 
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testComplexPostfix1() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;

		List<ListNode> list = new ArrayList<ListNode>();
		list.add(new AdditionListNode());
		list.add(new MultiplicationListNode());
		list.add(new NumberListNode(a));
		list.add(new NumberListNode(b));
		list.add(new DivisionListNode());
		list.add(new NumberListNode(c));
		list.add(new NumberListNode(d));

		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).setNext(list.get(i + 1));
		}
		
		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		list.get(0).accept(v1);
		assertEquals((a * b) + (c / d), v1.getResult(), DELTA);

	}

	/**
	 * Another complex Prefix Test
	 * infix: ((A * (B + C)) / D)
	 * prefix: / * A + B C D
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testComplexPrefix2() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;
		List<ListNode> list = new ArrayList<ListNode>();
		list.add(new DivisionListNode());
		list.add(new MultiplicationListNode());
		list.add(new NumberListNode(a));
		list.add(new AdditionListNode());
		list.add(new NumberListNode(b));
		list.add(new NumberListNode(c));
		list.add(new NumberListNode(d));

		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).setNext(list.get(i + 1));
		}

		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		list.get(0).accept(v1);
		assertEquals(((a * (b + c)) / d), v1.getResult(), DELTA);
	}

	/**
	 * Complex Prefix Test
	 * infix: (a / b) * ((-(c * d)) - (-(e + a)))
	 * Prefix: * / a b - ~ * c d ~ + e a
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testComplexPrefix3() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;
		double e = 293.12;
		List<ListNode> list = new ArrayList<ListNode>();
		list.add(new MultiplicationListNode());
		list.add(new DivisionListNode());
		list.add(new NumberListNode(a));
		list.add(new NumberListNode(b));
		list.add(new SubtractionListNode());
		list.add(new UnaryMinusListNode());
		list.add(new MultiplicationListNode());
		list.add(new NumberListNode(c));
		list.add(new NumberListNode(d));
		list.add(new UnaryMinusListNode());
		list.add(new AdditionListNode());
		list.add(new NumberListNode(e));
		list.add(new NumberListNode(a));

		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).setNext(list.get(i + 1));
		}

		EvaluatePrefixListVisitor v1 = new EvaluatePrefixListVisitor();
		list.get(0).accept(v1);
		assertEquals((a / b) * ((-(c * d)) - (-(e + a))), 
				v1.getResult(), DELTA);
	}
}
