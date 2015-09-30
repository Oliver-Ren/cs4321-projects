package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.tree.*;

public class EvaluateTreeVisitorTest {

	private static final double DELTA = 1e-15;

	@Test
	public void testSingleLeafNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n1.accept(v1);
		assertEquals(1.0, v1.getResult(), DELTA);
	}
	
	@Test
	public void testAdditionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new AdditionTreeNode(n1, n2);
		TreeNode n4 = new AdditionTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(3.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(3.0, v2.getResult(), DELTA);
	}
	
	@Test
	public void testMultiplicationNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new LeafTreeNode(2.0);
		TreeNode n3 = new MultiplicationTreeNode(n1, n2);
		TreeNode n4 = new MultiplicationTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(2.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}
	
	
	/**
	 * Test cases for the simple subtraction tree.
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testSubtractionNode() {
        TreeNode n1 = new LeafTreeNode(1.0);
        TreeNode n2 = new LeafTreeNode(2.0);
        TreeNode n3 = new SubtractionTreeNode(n1, n2);
        TreeNode n4 = new SubtractionTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(-1.0, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(1.0, v2.getResult(), DELTA);
	}
	
	/**
	 * Test cases for the simple unary minus node followed
	 * with a number.
	 * "-,1.0,#"
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
    public void testUnaryMinusNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
		TreeNode n2 = new UnaryMinusTreeNode(n1);
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n2.accept(v1);
		assertEquals(-1.0, v1.getResult(), DELTA);
	}
	
	/**
	 * Test cases for the simple division
	 * "/, 1.0, 2.0" "/, 2.0, 1.0"
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testDivsionNode() {
		TreeNode n1 = new LeafTreeNode(1.0);
        TreeNode n2 = new LeafTreeNode(2.0);
        TreeNode n3 = new DivisionTreeNode(n1, n2);
        TreeNode n4 = new DivisionTreeNode(n2, n1);
        EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		n3.accept(v1);
		assertEquals(0.5, v1.getResult(), DELTA);
        EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		n4.accept(v2);
		assertEquals(2.0, v2.getResult(), DELTA);
	}
	
	/**
	 * Test cases for two level expression tree started with
	 * a unary minus node.
	 * "~, {+|-|*|/}, #, num1, num2"
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testTwoLevelWithUnaryMinusRoot() {
		double num1 = 293.32;
		double num2 = 38.29;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);
		TreeNode opAdd1 = new AdditionTreeNode(leaf1, leaf2);
		TreeNode opAdd2 = new AdditionTreeNode(leaf2, leaf1);
		TreeNode root = new UnaryMinusTreeNode(opAdd1);
		
		// Addition
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		root.accept(v1);
		assertEquals(-(num1 + num2), v1.getResult(), DELTA);
		
		root = new UnaryMinusTreeNode(opAdd2);
		
		EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		root.accept(v2);
		assertEquals(-(num2 + num1), v2.getResult(), DELTA);
		
		
		// Subtraction
		TreeNode opSub1 = new SubtractionTreeNode(leaf1, leaf2);
		TreeNode opSub2 = new SubtractionTreeNode(leaf2, leaf1);
		
		root = new UnaryMinusTreeNode(opSub1);
		
		EvaluateTreeVisitor v3 = new EvaluateTreeVisitor();
		root.accept(v3);
		assertEquals(-(num1 - num2), v3.getResult(), DELTA);
		
		root = new UnaryMinusTreeNode(opSub2);
		
		EvaluateTreeVisitor v4 = new EvaluateTreeVisitor();
		root.accept(v4);
		assertEquals(-(num2 - num1), v4.getResult(), DELTA);
		
		
		// Multiplication
		TreeNode opMul1 = new MultiplicationTreeNode(leaf1, leaf2);
		TreeNode opMul2 = new MultiplicationTreeNode(leaf2, leaf1);
		
		root = new UnaryMinusTreeNode(opMul1);
		
		EvaluateTreeVisitor v5 = new EvaluateTreeVisitor();
		root.accept(v5);
		assertEquals(-(num1 * num2), v5.getResult(), DELTA);
		
		root = new UnaryMinusTreeNode(opMul2);
		
		EvaluateTreeVisitor v6 = new EvaluateTreeVisitor();
		root.accept(v6);
		assertEquals(-(num2 * num1), v6.getResult(), DELTA);


		// Division
		TreeNode opDiv1 = new MultiplicationTreeNode(leaf1, leaf2);
		TreeNode opDiv2 = new MultiplicationTreeNode(leaf2, leaf1);
		
		root = new UnaryMinusTreeNode(opDiv1);
		
		EvaluateTreeVisitor v7 = new EvaluateTreeVisitor();
		root.accept(v7);
		assertEquals(-(num1 * num2), v7.getResult(), DELTA);
		
		root = new UnaryMinusTreeNode(opDiv2);
		
		EvaluateTreeVisitor v8 = new EvaluateTreeVisitor();
		root.accept(v8);
		assertEquals(-(num2 * num1), v8.getResult(), DELTA);
		
	}

    /**
     * Test cases of two levels starts with an operator.
     * "{+|-|*|/}, ~, num1, num2, #"
	 * @author Chengxiang Ren (cr486).
     */
	@Test
    public void testTwoLevelWithOperatorRoot1() {
		double num1 = 2938.13;
		double num2 = 23.10;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);

        TreeNode uMinus1 = new UnaryMinusTreeNode(leaf1);
        TreeNode uMinus2 = new UnaryMinusTreeNode(leaf2);
        
        TreeNode root;

        // Addition
		root = new AdditionTreeNode(uMinus1, leaf2);
		
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		root.accept(v1);
		assertEquals((-num1) + num2, v1.getResult(), DELTA);
		
		root = new AdditionTreeNode(uMinus2, leaf1);
		
		EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		root.accept(v2);
		assertEquals((-num2) + num1, v2.getResult(), DELTA);

        
        // Subtraction
		root = new SubtractionTreeNode(uMinus1, leaf2);
		
		EvaluateTreeVisitor v3 = new EvaluateTreeVisitor();
		root.accept(v3);
		assertEquals((-num1) - num2, v3.getResult(), DELTA);
		
		root = new SubtractionTreeNode(uMinus2, leaf1);
		
		EvaluateTreeVisitor v4 = new EvaluateTreeVisitor();
		root.accept(v4);
		assertEquals((-num2) - num1, v4.getResult(), DELTA);

        
        // Multiplication
		root = new MultiplicationTreeNode(uMinus1, leaf2);
		
		EvaluateTreeVisitor v5 = new EvaluateTreeVisitor();
		root.accept(v5);
		assertEquals((-num1) * num2, v5.getResult(), DELTA);
		
		root = new MultiplicationTreeNode(uMinus2, leaf1);
		
		EvaluateTreeVisitor v6 = new EvaluateTreeVisitor();
		root.accept(v6);
		assertEquals((-num2) * num1, v6.getResult(), DELTA);


        // Division
		root = new DivisionTreeNode(uMinus1, leaf2);
		
		EvaluateTreeVisitor v7 = new EvaluateTreeVisitor();
		root.accept(v7);
		assertEquals((-num1) / num2, v7.getResult(), DELTA);
		
		root = new DivisionTreeNode(uMinus2, leaf1);
		
		EvaluateTreeVisitor v8 = new EvaluateTreeVisitor();
		root.accept(v8);
		assertEquals((-num2) / num1, v8.getResult(), DELTA);
    }
	
	/**
     * Test cases of two levels starts with an operator.
     * "{+|-|*|/}, num1,  ~, num2, #"
	 * @author Chengxiang Ren (cr486).
     */
	@Test
    public void testTwoLevelWithOperatorRoot2() {
		double num1 = 35.13;
		double num2 = 0.10;
		TreeNode leaf1 = new LeafTreeNode(num1);
		TreeNode leaf2 = new LeafTreeNode(num2);

        TreeNode uMinus1 = new UnaryMinusTreeNode(leaf1);
        TreeNode uMinus2 = new UnaryMinusTreeNode(leaf2);
        
        TreeNode root;

        // Addition
		root = new AdditionTreeNode(leaf2, uMinus1);
		
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		root.accept(v1);
		assertEquals(num2 + (-num1), v1.getResult(), DELTA);
		
		root = new AdditionTreeNode(leaf1, uMinus2);
		
		EvaluateTreeVisitor v2 = new EvaluateTreeVisitor();
		root.accept(v2);
		assertEquals(num1 + (-num2), v2.getResult(), DELTA);

        
        // Subtraction
		root = new SubtractionTreeNode(leaf2, uMinus1);
		
		EvaluateTreeVisitor v3 = new EvaluateTreeVisitor();
		root.accept(v3);
		assertEquals(num2 - (-num1), v3.getResult(), DELTA);
		
		root = new SubtractionTreeNode(leaf1, uMinus2);
		
		EvaluateTreeVisitor v4 = new EvaluateTreeVisitor();
		root.accept(v4);
		assertEquals(num1 - (-num2), v4.getResult(), DELTA);

        
        // Multiplication
		root = new MultiplicationTreeNode(leaf2, uMinus1);
		
		EvaluateTreeVisitor v5 = new EvaluateTreeVisitor();
		root.accept(v5);
		assertEquals(num2 * (-num1), v5.getResult(), DELTA);
		
		root = new MultiplicationTreeNode(leaf1, uMinus2);
		
		EvaluateTreeVisitor v6 = new EvaluateTreeVisitor();
		root.accept(v6);
		assertEquals(num1 * (-num2), v6.getResult(), DELTA);


        // Division
		root = new DivisionTreeNode(leaf2, uMinus1);
		
		EvaluateTreeVisitor v7 = new EvaluateTreeVisitor();
		root.accept(v7);
		assertEquals(num2 / (-num1), v7.getResult(), DELTA);
		
		root = new DivisionTreeNode(leaf1, uMinus2);
		
		EvaluateTreeVisitor v8 = new EvaluateTreeVisitor();
		root.accept(v8);
		assertEquals(num1 / (-num2), v8.getResult(), DELTA);
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
		
		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		oDiv.accept(v1);
		assertEquals(((a * (b + c) / d)), v1.getResult(), DELTA);
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

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		oAdd.accept(v1);
		assertEquals((a * b) + (c / d), v1.getResult(), DELTA);

    }

	/**
	 * Test with a three-level tree:
	 * "*, a, +, #, #, b, /, #, #, c, d"
	 */
    @Test
    public void testThreeLevel3() {
        double a = 29.72;
        double b = 282.33;
        double c = 23.23;
        double d = 934.12;

		LeafTreeNode na = new LeafTreeNode(a);
		LeafTreeNode nb = new LeafTreeNode(b);
		LeafTreeNode nc = new LeafTreeNode(c);
		LeafTreeNode nd = new LeafTreeNode(d);
		
		DivisionTreeNode oDiv = new DivisionTreeNode(nc, nd);
		AdditionTreeNode oAdd = new AdditionTreeNode(nb, oDiv);
		MultiplicationTreeNode oMul = new MultiplicationTreeNode(na, oAdd);

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		oMul.accept(v1);
		assertEquals((a * (b + (c / d))), v1.getResult(), DELTA);
    }

		
}
