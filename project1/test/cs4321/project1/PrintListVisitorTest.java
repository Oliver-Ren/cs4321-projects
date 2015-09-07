package cs4321.project1;

import static org.junit.Assert.*;
import cs4321.project1.list.*;
import cs4321.project1.tree.UnaryMinusTreeNode;

import org.junit.Test;

public class PrintListVisitorTest {

	@Test
	public void testSingleNumberNode() {
		ListNode n1 = new NumberListNode(1.0);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("+ 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testAdditionSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new AdditionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 +", pv1.getResult());
	}

    @Test
    public void testSubtractionSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new SubtractionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("- 2.0 1.0", pv1.getResult());
    }

	@Test
	public void testSubtractionSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new SubtractionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 -", pv1.getResult());
	}

	@Test
	public void testMultiplicationSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new MultiplicationListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("* 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testMultiplicationSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new MultiplicationListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 *", pv1.getResult());
	}

    
	@Test
	public void testDivisionSimplePrefix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new DivisionListNode();
		n3.setNext(n2);
		n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n3.accept(pv1);
		assertEquals("/ 2.0 1.0", pv1.getResult());
	}
	
	@Test
	public void testDivisionSimplePostfix() {
		ListNode n1 = new NumberListNode(1.0);
		ListNode n2 = new NumberListNode(2.0);
		ListNode n3 = new DivisionListNode();
		n1.setNext(n2);
		n2.setNext(n3);
		PrintListVisitor pv1 = new PrintListVisitor();
		n1.accept(pv1);
		assertEquals("1.0 2.0 /", pv1.getResult());
	}

	@Test
	public void testUnaryMinusNode() {
		ListNode n1 = new NumberListNode(1.0);
        ListNode n2 = new UnaryMinusListNode();
        n2.setNext(n1);
		PrintListVisitor pv1 = new PrintListVisitor();
		n2.accept(pv1);
		assertEquals("~ 1.0", pv1.getResult());
	}
    
	/**
	 * Test case which contains multiple negative signs.
	 */
	@Test
	public void testUnaryMultipleMinusNode() {
		ListNode n1 = new NumberListNode(29431.22);
        ListNode[] list = new ListNode[10];
        String s = "~ ";
        list[9] = new UnaryMinusListNode();
        list[9].setNext(n1);
        for (int i = 8; i >= 0; i--) {
            list[i] = new UnaryMinusListNode();
            list[i].setNext(list[i + 1]);
            s += "~ ";
        }

		PrintListVisitor pv1 = new PrintListVisitor();
		list[0].accept(pv1);
		assertEquals(s + 29431.22, pv1.getResult());
	}
	
	@Test
	public void testComplexPrefix() {
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

        opAdd.setNext(opMul);
        opMul.setNext(na);
        na.setNext(nb);
        nb.setNext(opDiv);
        opDiv.setNext(nc);
        nc.setNext(nd);

        
		PrintListVisitor pv1 = new PrintListVisitor();
        opAdd.accept(pv1);

        assertEquals("+ * " + a + " " + b + " / " + c + " " + d, 
                pv1.getResult()); 

	}


	@Test
	public void testComplexPostfix() {
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

        
		PrintListVisitor pv1 = new PrintListVisitor();
        na.accept(pv1);

        assertEquals("" + a + " " + b + " * " + c + " " + d + " / +", 
                pv1.getResult()); 

	}
}
