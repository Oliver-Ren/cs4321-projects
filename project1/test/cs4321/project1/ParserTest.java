package cs4321.project1;

import static org.junit.Assert.*;

import java.util.Scanner;

import org.junit.Test;

import cs4321.project1.tree.TreeNode;

public class ParserTest {

	/*
	 * This class depends on the correct functioning of PrintTreeVisitor(), which is provided for you.
	 */
			
	@Test
	public void testSingleNumber() {
		Parser p1 = new Parser("1.0");
		TreeNode parseResult1 = p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("1.0", v1.getResult());

	}
	
	@Test
	public void testUnaryMinusSimple() {
		Parser p1 = new Parser("- 1.0");
		TreeNode parseResult1 = p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("(-1.0)", v1.getResult());
	}
	
	@Test
	public void testUnaryMinusComplex() {
		Parser p1 = new Parser("- - 1.0");
		TreeNode parseResult1 =  p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		assertEquals("(-(-1.0))", v1.getResult());

	}

	/**
	 * Test the parsed result of a string against a given answer.
	 * 
	 * @author Guantian Zheng (gz94)
	 * @param s: the string
	 * @param ans: the answer, will be checked if not null
	 */
	public static void test(String s, String ans) {
		Parser p1 = new Parser(s);
		TreeNode parseResult1 =  p1.parse();
		PrintTreeVisitor v1 = new PrintTreeVisitor();
		parseResult1.accept(v1);
		String res = v1.getResult();
		
		System.out.println("str: " + s);
		System.out.println("ans: " + ans);
		System.out.println("res: " + res + "\n");
		
		if (ans != null)
			assertEquals(ans, res);
	}
	
	/*
	 * Run a group of test cases for the parser.
	 * 
	 * @author Guantian Zheng (gz94)
	 */
	@Test
	public void parserTests() {
		test("123.123", "123.123");
		test("( 321.321 )", "321.321");
		test("( ( ( ( ( 321.321 ) ) ) ) )", "321.321");
		test("- 2", "(-2.0)");
		test("- 2 - - 1 + 3", "(((-2.0)-(-1.0))+3.0)");
		test("- ( - 2 - - 1 + 3 ) + - ( 1 + 2 ) - - ( 3 - 2 - 1 )", "(((-(((-2.0)-(-1.0))+3.0))+(-(1.0+2.0)))-(-((3.0-2.0)-1.0)))");
		test("1.0 + 2.1", "(1.0+2.1)");
		test("1.0 + 2.0 + 3.0", "((1.0+2.0)+3.0)");
		test("1.0 + 2.0 + 3.0 + 4.1", "(((1.0+2.0)+3.0)+4.1)");
		test("( ( ( 1.0 + 2.0 ) ) )", "(1.0+2.0)");
		test("( ( ( 333 - 222 ) ) )", "(333.0-222.0)");
		test("( ( 111 * 999 ) )", "(111.0*999.0)");
		test("( 234 / 17.99 )", "(234.0/17.99)");
		test("1 * ( 2 / 3 ) - 4", "((1.0*(2.0/3.0))-4.0)");
		test("2 / ( ( ( 1.0 + 2.0 ) ) - 100.132 ) * 909 ", "((2.0/((1.0+2.0)-100.132))*909.0)");
		test("1 * ( ( 2 / ( ( 3 + 4 ) - 5 ) ) * 6 ) + 7", "((1.0*((2.0/((3.0+4.0)-5.0))*6.0))+7.0)");
		test("1 * ( ( 2 / ( ( 3 + 4 + 5 - 99 ) ) * 6 ) ) + 7 - 8", "(((1.0*((2.0/(((3.0+4.0)+5.0)-99.0))*6.0))+7.0)-8.0)");
		test("1 + 3 + ( ( 3 + 4 * 5 / ( 6 + 61 - 62 + 63 ) / 7 - 8 ) ) + 555 + ( 66 + 77 + 88 )", 
				"((((1.0+3.0)+((3.0+(((4.0*5.0)/(((6.0+61.0)-62.0)+63.0))/7.0))-8.0))+555.0)+((66.0+77.0)+88.0))");
		
//		while (true) {
//			Scanner sc = new Scanner(System.in);
//			String exp = sc.nextLine();
//			try {
//				test(exp, null);
//			} catch (Exception e) {
//				System.out.println(e.getClass().getName());
//			}
//			
//			if (exp.isEmpty())
//				break;
//		}
	}

	/**
	 * Test cases for single operator only
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testSingleOperator() {
		test("2.56 * 2.56", "(2.56*2.56)");
		test("2.56 + 2.56", "(2.56+2.56)");
		test("2.56 - 2.56", "(2.56-2.56)");
		test("2.56 / 2.56", "(2.56/2.56)");
	}

	/**
	 * Test cases for combination of operators
	 * @author Chengxiang Ren (cr486).
	 */
	@Test
	public void testCombination() {
		test("1.28 + 1.28 + 1.28 + 1.28", "(((1.28+1.28)+1.28)+1.28)");
		test("1.28 + 1.28 - 1.28 * 1.28 / 1.28", "((1.28+1.28)-((1.28*1.28)/1.28))");
		test("- - - - - 2.0","(-(-(-(-(-2.0)))))" );
		test("- - 2.8 - - - 2.9", "((-(-2.8))-(-(-2.9)))");
		test("- ( 4.3 )", "(-4.3)");
		test("- ( - ( ( ( - ( 5.9 ) ) ) ) )", "(-(-(-5.9)))");
	}
}
