package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.list.*;
import cs4321.project1.tree.*;

public class BuildPostfixExpressionTreeVisitorTest {

	private static final double DELTA = 1e-15;

	@Test
	public void testSingleLeafNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		n1.accept(v1);
		ListNode result = v1.getResult();
		assertNull(result.getNext());
		assertTrue(result instanceof NumberListNode);
	}

	@Test
	public void testAdditionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new AdditionTreeNode(n1, n2);
		TreeNode n4 = new AdditionTreeNode(n2, n1);

		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		n3.accept(v1);
		ListNode result = v1.getResult();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		result = result.getNext();
		assertTrue(result instanceof AdditionListNode);
		assertNull(result.getNext());

		BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		n4.accept(v2);
		result = v2.getResult();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		result = result.getNext();
		assertTrue(result instanceof AdditionListNode);
		assertNull(result.getNext());
	}


    @Test
	public void testUnaryMinusNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new UnaryMinusTreeNode(n1);

		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		n2.accept(v1);
		ListNode result = v1.getResult();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);
		assertNull(result.getNext());

	}

	/**
	 * Simple test case for Subtraction
	 */
	@Test
	public void testSubtractionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new SubtractionTreeNode(n1, n2);
		TreeNode n4 = new SubtractionTreeNode(n2, n1);
		
        BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		n3.accept(v1);
		ListNode result = v1.getResult();
		
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof SubtractionListNode);
		
		
        BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		n4.accept(v2);
		result = v2.getResult();
		
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof SubtractionListNode);
	}

	/**
	 * Simple test case for Multiplication
	 */
	@Test
	public void testMultiplicationNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new MultiplicationTreeNode(n1, n2);
		TreeNode n4 = new MultiplicationTreeNode(n2, n1);
		
        BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		n3.accept(v1);
		ListNode result = v1.getResult();
		
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof MultiplicationListNode);
		
		
        BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		n4.accept(v2);
		result = v2.getResult();
		
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof MultiplicationListNode);
	}


	/**
	 * Simple test case for Division
	 */
	@Test
	public void testDivisionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new DivisionTreeNode(n1, n2);
		TreeNode n4 = new DivisionTreeNode(n2, n1);
		
        BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		n3.accept(v1);
		ListNode result = v1.getResult();
		
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof DivisionListNode);
		
		
        BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		n4.accept(v2);
		result = v2.getResult();
		
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 2.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), 1.0, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof DivisionListNode);
	}
    
	/**
	 * Test case for two level expression tree started with
	 * a unary minus node and Addition operator at level 2.
	 * "~, +, #, num1, num2"
	 */
	@Test
	public void testTwoLevelWithUnaryMinusRootAndAddition() {
		double num1 = 293.32;
		double num2 = 38.29;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);
		TreeNode opAdd1 = new AdditionTreeNode(leaf1, leaf2);
		TreeNode opAdd2 = new AdditionTreeNode(leaf2, leaf1);
		TreeNode root;
		
		// Addition 1
		root = new UnaryMinusTreeNode(opAdd1);
		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v1);
		
		ListNode result = v1.getResult();

		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof AdditionListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);

		// Addition 2
		root = new UnaryMinusTreeNode(opAdd2);
		
		BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v2);
		
		result = v2.getResult();
        
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof AdditionListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);
		
	}


	/**
	 * Test case for two level expression tree started with
	 * a unary minus node and Subtraction operator at level 2.
	 * "~, /, #, num1, num2"
	 */
	@Test
	public void testTwoLevelWithUnaryMinusRootAndSubtraction() {
		double num1 = 293.32;
		double num2 = 38.29;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);
		TreeNode opAdd1 = new SubtractionTreeNode(leaf1, leaf2);
		TreeNode opAdd2 = new SubtractionTreeNode(leaf2, leaf1);
		TreeNode root;
		
		// Subtraction 1
		root = new UnaryMinusTreeNode(opAdd1);
		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v1);
		
		ListNode result = v1.getResult();

		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof SubtractionListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);

		// Subtraction 2
		root = new UnaryMinusTreeNode(opAdd2);
		
		BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v2);
		
		result = v2.getResult();
        
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof SubtractionListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);
		
	}


	/**
	 * Test case for two level expression tree started with
	 * a unary minus node and Multiplication operator at level 2.
	 * "~, *, #, num1, num2"
	 */
	@Test
	public void testTwoLevelWithUnaryMinusRootAndMultiplication() {
		double num1 = 293.32;
		double num2 = 38.29;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);
		TreeNode opAdd1 = new MultiplicationTreeNode(leaf1, leaf2);
		TreeNode opAdd2 = new MultiplicationTreeNode(leaf2, leaf1);
		TreeNode root;
		
		// Multiplication 1
		root = new UnaryMinusTreeNode(opAdd1);
		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v1);
		
		ListNode result = v1.getResult();

		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof MultiplicationListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);

		// Multiplication 2
		root = new UnaryMinusTreeNode(opAdd2);
		
		BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v2);
		
		result = v2.getResult();
        
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof MultiplicationListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);
		
	}

	/**
	 * Test case for two level expression tree started with
	 * a unary minus node and Division operator at level 2.
	 * "~, /, #, num1, num2"
	 */
	@Test
	public void testTwoLevelWithUnaryMinusRootAndDivision() {
		double num1 = 293.32;
		double num2 = 38.29;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);
		TreeNode opAdd1 = new DivisionTreeNode(leaf1, leaf2);
		TreeNode opAdd2 = new DivisionTreeNode(leaf2, leaf1);
		TreeNode root;
		
		// Division 1
		root = new UnaryMinusTreeNode(opAdd1);
		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v1);
		
		ListNode result = v1.getResult();

		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);
		
		result = result.getNext();
		assertTrue(result instanceof DivisionListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);

		// Division 2
		root = new UnaryMinusTreeNode(opAdd2);
		
		BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
		root.accept(v2);
		
		result = v2.getResult();
        
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num2, DELTA);

		result = result.getNext();
		assertTrue(result instanceof NumberListNode);
		assertEquals(((NumberListNode) result).getData(), num1, DELTA);

		result = result.getNext();
		assertTrue(result instanceof DivisionListNode);

		result = result.getNext();
		assertTrue(result instanceof UnaryMinusListNode);
		
	}


    /**
     * Test cases of two levels starts with an Addition.
     * the one of the level 2 is unary minus.
     * "+, ~, num1, num2, #"
     * "+, num1, ~, #, #, num2"
     */
	@Test
    public void testTwoLevelWithAdditionRoot() {
		double num1 = 2938.13;
		double num2 = 23.10;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);

        TreeNode uMinus1 = new UnaryMinusTreeNode(leaf1);
        TreeNode uMinus2 = new UnaryMinusTreeNode(leaf2);
        
        TreeNode root;
        ListNode result;

        // Addition 1
        root = new AdditionTreeNode(uMinus1, leaf2);
        BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v1);

        result = v1.getResult();

		assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);

        result = result.getNext();
        assertTrue(result instanceof AdditionListNode);
        
        // Addition 2
        root = new AdditionTreeNode(leaf1, uMinus2);
        BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v2);

        result = v2.getResult();

        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);

        result = result.getNext();
        assertTrue(result instanceof AdditionListNode);

    }


    /**
     * Test cases of two levels starts with an Subtraction.
     * the one of the level 2 is unary minus.
     * "-, ~, num1, num2, #"
     * "-, num1, ~, #, #, num2"
     */
	@Test
    public void testTwoLevelWithSubtractionRoot() {
		double num1 = 2938.13;
		double num2 = 23.10;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);

        TreeNode uMinus1 = new UnaryMinusTreeNode(leaf1);
        TreeNode uMinus2 = new UnaryMinusTreeNode(leaf2);
        
        TreeNode root;
        ListNode result;

        // Subtraction 1
        root = new SubtractionTreeNode(uMinus1, leaf2);
        BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v1);

        result = v1.getResult();

		assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);

        result = result.getNext();
        assertTrue(result instanceof SubtractionListNode);
        
        // Subtraction 2
        root = new SubtractionTreeNode(leaf1, uMinus2);
        BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v2);

        result = v2.getResult();

        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);

        result = result.getNext();
        assertTrue(result instanceof SubtractionListNode);

    }

    
    /**
     * Test cases of two levels starts with an Multiplication.
     * the one of the level 2 is unary minus.
     * "*, ~, num1, num2, #"
     * "*, num1, ~, #, #, num2"
     */
	@Test
    public void testTwoLevelWithMultiplicationRoot() {
		double num1 = 2938.13;
		double num2 = 23.10;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);

        TreeNode uMinus1 = new UnaryMinusTreeNode(leaf1);
        TreeNode uMinus2 = new UnaryMinusTreeNode(leaf2);
        
        TreeNode root;
        ListNode result;

        // Multiplication 1
        root = new MultiplicationTreeNode(uMinus1, leaf2);
        BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v1);

        result = v1.getResult();

		assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);

        result = result.getNext();
        assertTrue(result instanceof MultiplicationListNode);
        
        // Multiplication 2
        root = new MultiplicationTreeNode(leaf1, uMinus2);
        BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v2);

        result = v2.getResult();

        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);

        result = result.getNext();
        assertTrue(result instanceof MultiplicationListNode);

    }

    /**
     * Test cases of two levels starts with an Division.
     * the one of the level 2 is unary minus.
     * "/, ~, num1, num2, #"
     * "/, num1, ~, #, #, num2"
     */
	@Test
    public void testTwoLevelWithDivisionRoot() {
		double num1 = 2938.13;
		double num2 = 23.10;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);

        TreeNode uMinus1 = new UnaryMinusTreeNode(leaf1);
        TreeNode uMinus2 = new UnaryMinusTreeNode(leaf2);
        
        TreeNode root;
        ListNode result;

        // Division 1
        root = new DivisionTreeNode(uMinus1, leaf2);
        BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v1);

        result = v1.getResult();

		assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);

        result = result.getNext();
        assertTrue(result instanceof DivisionListNode);
        
        // Division 2
        root = new DivisionTreeNode(leaf1, uMinus2);
        BuildPostfixExpressionTreeVisitor v2 = new BuildPostfixExpressionTreeVisitor();
        root.accept(v2);

        result = v2.getResult();

        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num1, DELTA);

        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), num2, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof UnaryMinusListNode);

        result = result.getNext();
        assertTrue(result instanceof DivisionListNode);

    }

	/**
	 * Test with a three-level tree:
	 * "/, *, D, A, +, #, #, #, #, B, C"
	 */
	@Test
    public void testThreeLevel1() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;
		
		LeafTreeNode na = new LeafTreeNode(a);
		LeafTreeNode nb = new LeafTreeNode(b);
		LeafTreeNode nc = new LeafTreeNode(c);
		LeafTreeNode nd = new LeafTreeNode(d);
		
		AdditionTreeNode oAdd = new AdditionTreeNode(nb, nc);
		MultiplicationTreeNode oMul = new MultiplicationTreeNode(na, oAdd);
		DivisionTreeNode oDiv = new DivisionTreeNode(oMul, nd);
		
		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
        oDiv.accept(v1);
        
        ListNode result = v1.getResult();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), a, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), b, DELTA);

        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), c, DELTA);

        result = result.getNext();
        assertTrue(result instanceof AdditionListNode);

        result = result.getNext();
        assertTrue(result instanceof MultiplicationListNode);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), d, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof DivisionListNode);
        
	}

	/**
	 * Test with a three-level tree:
	 * "+, *, /, a, b, c, d"
	 */
	@Test
    public void testThreeLevel2() {
		double a = 2.35;
		double b = 293.00;
		double c = 13.23;
		double d = 20.00;
		
		LeafTreeNode na = new LeafTreeNode(a);
		LeafTreeNode nb = new LeafTreeNode(b);
		LeafTreeNode nc = new LeafTreeNode(c);
		LeafTreeNode nd = new LeafTreeNode(d);
		
		MultiplicationTreeNode oMul = new MultiplicationTreeNode(na, nb);
		DivisionTreeNode oDiv = new DivisionTreeNode(nc, nd);
		AdditionTreeNode oAdd = new AdditionTreeNode(oMul, oDiv);
		
		BuildPostfixExpressionTreeVisitor v1 = new BuildPostfixExpressionTreeVisitor();
        oAdd.accept(v1);
        
        ListNode result = v1.getResult();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), a, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), b, DELTA);

        result = result.getNext();
        assertTrue(result instanceof MultiplicationListNode);

        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), c, DELTA);
        
        result = result.getNext();
        assertTrue(result instanceof NumberListNode);
        assertEquals(((NumberListNode) result).getData(), d, DELTA);

        result = result.getNext();
        assertTrue(result instanceof DivisionListNode);

        result = result.getNext();
        assertTrue(result instanceof AdditionListNode);
        
	}
}
