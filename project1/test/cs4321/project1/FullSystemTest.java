package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.tree.*;
import cs4321.project1.list.*;

public class FullSystemTest {
	
	
	private static final double DELTA = 1e-15;
	
	/**
	 * Helper method for setting up the code that 
	 * will be used at each test case.
	 * @param s -- input string.
	 * @param ans -- expected result.
	 * @author Chengxiang Ren (cr486).
	 */
	private static void test(String s, double ans) {
		Parser p1 = new Parser(s);
		TreeNode parseResult1 = p1.parse();

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		parseResult1.accept(v1);
		double treeEvaluationResult = v1.getResult();
		
		BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		parseResult1.accept(v2);
		ListNode prefixRepresentation = v2.getResult();
		EvaluatePrefixListVisitor v3 = new EvaluatePrefixListVisitor();
		prefixRepresentation.accept(v3);
		double prefixEvaluationResult = v3.getResult();
		
		BuildPostfixExpressionTreeVisitor v4 = new BuildPostfixExpressionTreeVisitor();
		parseResult1.accept(v4);
		ListNode postfixRepresentation = v4.getResult();
		EvaluatePostfixListVisitor v5 = new EvaluatePostfixListVisitor();
		postfixRepresentation.accept(v5);
		double postfixEvaluationResult = v5.getResult();
		
		assertEquals(ans, treeEvaluationResult, DELTA);
		assertEquals(ans, prefixEvaluationResult, DELTA);
		assertEquals(ans, postfixEvaluationResult, DELTA);
	}
	
	@Test
	public void testSingleNumber() {
		Parser p1 = new Parser("1.0");
		TreeNode parseResult1 = p1.parse();

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		parseResult1.accept(v1);
		double treeEvaluationResult = v1.getResult();
		
		BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		parseResult1.accept(v2);
		ListNode prefixRepresentation = v2.getResult();
		EvaluatePrefixListVisitor v3 = new EvaluatePrefixListVisitor();
		prefixRepresentation.accept(v3);
		double prefixEvaluationResult = v3.getResult();
		
		BuildPostfixExpressionTreeVisitor v4 = new BuildPostfixExpressionTreeVisitor();
		parseResult1.accept(v4);
		ListNode postfixRepresentation = v4.getResult();
		EvaluatePostfixListVisitor v5 = new EvaluatePostfixListVisitor();
		postfixRepresentation.accept(v5);
		double postfixEvaluationResult = v5.getResult();
		
		assertEquals(1.0, treeEvaluationResult, DELTA);
		assertEquals(1.0, prefixEvaluationResult, DELTA);
		assertEquals(1.0, postfixEvaluationResult, DELTA);

	}
	
	@Test
	public void testAdditionSimple() {
		Parser p1 = new Parser("1.0 + 3.0");
		TreeNode parseResult1 = p1.parse();

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		parseResult1.accept(v1);
		double treeEvaluationResult = v1.getResult();
		
		BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		parseResult1.accept(v2);
		ListNode prefixRepresentation = v2.getResult();
		EvaluatePrefixListVisitor v3 = new EvaluatePrefixListVisitor();
		prefixRepresentation.accept(v3);
		double prefixEvaluationResult = v3.getResult();
		
		BuildPostfixExpressionTreeVisitor v4 = new BuildPostfixExpressionTreeVisitor();
		parseResult1.accept(v4);
		ListNode postfixRepresentation = v4.getResult();
		EvaluatePostfixListVisitor v5 = new EvaluatePostfixListVisitor();
		postfixRepresentation.accept(v5);
		double postfixEvaluationResult = v5.getResult();
		
		assertEquals(4.0, treeEvaluationResult, DELTA);
		assertEquals(4.0, prefixEvaluationResult, DELTA);
		assertEquals(4.0, postfixEvaluationResult, DELTA);

	}

	
	@Test
	public void testComplexExpression1() {
		Parser p1 = new Parser("4.0 * 2.0 + 3.0");
		TreeNode parseResult1 = p1.parse();

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		parseResult1.accept(v1);
		double treeEvaluationResult = v1.getResult();
		
		BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		parseResult1.accept(v2);
		ListNode prefixRepresentation = v2.getResult();
		EvaluatePrefixListVisitor v3 = new EvaluatePrefixListVisitor();
		prefixRepresentation.accept(v3);
		double prefixEvaluationResult = v3.getResult();
		
		BuildPostfixExpressionTreeVisitor v4 = new BuildPostfixExpressionTreeVisitor();
		parseResult1.accept(v4);
		ListNode postfixRepresentation = v4.getResult();
		EvaluatePostfixListVisitor v5 = new EvaluatePostfixListVisitor();
		postfixRepresentation.accept(v5);
		double postfixEvaluationResult = v5.getResult();
		
		assertEquals(11.0, treeEvaluationResult, DELTA);
		assertEquals(11.0, prefixEvaluationResult, DELTA);
		assertEquals(11.0, postfixEvaluationResult, DELTA);

	}
	
	/**
	 * test case for simple Subtraction.
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testSubtractionSimple() {
		Parser p1 = new Parser("1.0 - 3.0");
		TreeNode parseResult1 = p1.parse();

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		parseResult1.accept(v1);
		double treeEvaluationResult = v1.getResult();
		
		BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		parseResult1.accept(v2);
		ListNode prefixRepresentation = v2.getResult();
		EvaluatePrefixListVisitor v3 = new EvaluatePrefixListVisitor();
		prefixRepresentation.accept(v3);
		double prefixEvaluationResult = v3.getResult();
		
		BuildPostfixExpressionTreeVisitor v4 = new BuildPostfixExpressionTreeVisitor();
		parseResult1.accept(v4);
		ListNode postfixRepresentation = v4.getResult();
		EvaluatePostfixListVisitor v5 = new EvaluatePostfixListVisitor();
		postfixRepresentation.accept(v5);
		double postfixEvaluationResult = v5.getResult();
		
		assertEquals(-2.0, treeEvaluationResult, DELTA);
		assertEquals(-2.0, prefixEvaluationResult, DELTA);
		assertEquals(-2.0, postfixEvaluationResult, DELTA);

	}

	/**
	 * test case for simple Multiplication.
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testMultiplicationSimple() {
		Parser p1 = new Parser("1.0 * 3.0");
		TreeNode parseResult1 = p1.parse();

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		parseResult1.accept(v1);
		double treeEvaluationResult = v1.getResult();
		
		BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		parseResult1.accept(v2);
		ListNode prefixRepresentation = v2.getResult();
		EvaluatePrefixListVisitor v3 = new EvaluatePrefixListVisitor();
		prefixRepresentation.accept(v3);
		double prefixEvaluationResult = v3.getResult();
		
		BuildPostfixExpressionTreeVisitor v4 = new BuildPostfixExpressionTreeVisitor();
		parseResult1.accept(v4);
		ListNode postfixRepresentation = v4.getResult();
		EvaluatePostfixListVisitor v5 = new EvaluatePostfixListVisitor();
		postfixRepresentation.accept(v5);
		double postfixEvaluationResult = v5.getResult();
		
		assertEquals(3.0, treeEvaluationResult, DELTA);
		assertEquals(3.0, prefixEvaluationResult, DELTA);
		assertEquals(3.0, postfixEvaluationResult, DELTA);

	}

	/**
	 * test case for simple Division.
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testDivisionSimple() {
		Parser p1 = new Parser("6.6 / 3.0");
		TreeNode parseResult1 = p1.parse();

		EvaluateTreeVisitor v1 = new EvaluateTreeVisitor();
		parseResult1.accept(v1);
		double treeEvaluationResult = v1.getResult();
		
		BuildPrefixExpressionTreeVisitor v2 = new BuildPrefixExpressionTreeVisitor();
		parseResult1.accept(v2);
		ListNode prefixRepresentation = v2.getResult();
		EvaluatePrefixListVisitor v3 = new EvaluatePrefixListVisitor();
		prefixRepresentation.accept(v3);
		double prefixEvaluationResult = v3.getResult();
		
		BuildPostfixExpressionTreeVisitor v4 = new BuildPostfixExpressionTreeVisitor();
		parseResult1.accept(v4);
		ListNode postfixRepresentation = v4.getResult();
		EvaluatePostfixListVisitor v5 = new EvaluatePostfixListVisitor();
		postfixRepresentation.accept(v5);
		double postfixEvaluationResult = v5.getResult();
		
		assertEquals(2.2, treeEvaluationResult, DELTA);
		assertEquals(2.2, prefixEvaluationResult, DELTA);
		assertEquals(2.2, postfixEvaluationResult, DELTA);

	}
	
	/**
	 * Test cases for single operator only
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testSingleOperator() {
		test("2.56 * 2.56", 2.56 * 2.56);
		test("2.56 + 2.56", (2.56+2.56));
		test("2.56 - 2.56", (2.56-2.56));
		test("2.56 / 2.56", (2.56/2.56));
	}
	
	/**
	 * Test cases for combination of operators
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testCombination() {
		test("1.28 + 1.28 + 1.28 + 1.28", (((1.28+1.28)+1.28)+1.28));
		test("1.28 + 1.28 - 1.28 * 1.28 / 1.28", ((1.28+1.28)-((1.28*1.28)/1.28)));
		test("- - - - - 2.0", (-(-(-(-(-2.0))))));
		test("- - 2.8 - - - 2.9", ((-(-2.8))-(-(-2.9))));
		test("- ( 4.3 )", (-4.3));
		test("- ( - ( ( ( - ( 5.9 ) ) ) ) )", (-(-(-5.9))));
	}

	/**
	 * Test cases for various cases.
	 * @author Guantian Zheng (gz94), Chengxiang Ren (cr486).
	 */
	@Test
	public void testMisc() {
		test("123.123", 123.123);
		test("( 321.321 )", 321.321);
		test("( ( ( ( ( 321.321 ) ) ) ) )", 321.321);
		test("- 2", (-2.0));
		test("- 2 - - 1 + 3", (((-2.0)-(-1.0))+3.0));
		test("- ( - 2 - - 1 + 3 ) + - ( 1 + 2 ) - - ( 3 - 2 - 1 )", (((-(((-2.0)-(-1.0))+3.0))+(-(1.0+2.0)))-(-((3.0-2.0)-1.0))));
		test("1.0 + 2.1", (1.0+2.1));
		test("1.0 + 2.0 + 3.0", ((1.0+2.0)+3.0));
		test("1.0 + 2.0 + 3.0 + 4.1", (((1.0+2.0)+3.0)+4.1));
		test("( ( ( 1.0 + 2.0 ) ) )", (1.0+2.0));
		test("( ( ( 333 - 222 ) ) )", (333.0-222.0));
		test("( ( 111 * 999 ) )", (111.0*999.0));
		test("( 234 / 17.99 )", (234.0/17.99));
		test("1 * ( 2 / 3 ) - 4", ((1.0*(2.0/3.0))-4.0));
		test("2 / ( ( ( 1.0 + 2.0 ) ) - 100.132 ) * 909 ", ((2.0/((1.0+2.0)-100.132))*909.0));
		test("1 * ( ( 2 / ( ( 3 + 4 ) - 5 ) ) * 6 ) + 7", ((1.0*((2.0/((3.0+4.0)-5.0))*6.0))+7.0));
		test("1 * ( ( 2 / ( ( 3 + 4 + 5 - 99 ) ) * 6 ) ) + 7 - 8", (((1.0*((2.0/(((3.0+4.0)+5.0)-99.0))*6.0))+7.0)-8.0));
	}


}
