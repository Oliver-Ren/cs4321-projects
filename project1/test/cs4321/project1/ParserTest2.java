package cs4321.project1;

import static org.junit.Assert.*;

import org.junit.Test;

import cs4321.project1.tree.TreeNode;

public class ParserTest2 {

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
			assert(ans.equals(res));
	}
	
	@Test
	public void parserTests() {
		test("123.123", "123.123");
		test("( 123.123 )", "123.123");
		test("1.0 + 2.1", "(1.0+2.1)");
		test("1.0 + 2.0 + 3.0", "((1.0+2.0)+3.0)");
		test("( ( ( 1.0 + 2.0 ) ) )", "(1.0+2.0)");
		test("( ( ( 333 - 222 ) ) )", "(333.0-222.0)");
		test("( ( 111 * 999 ) )", "(111.0*999.0)");
		test("( 234 / 17.99 )", "(234.0/17.99)");
		test("1 * ( 2 / 3 ) - 4", "((1.0*(2.0/3.0))-4.0)");
		test("2 / ( ( ( 1.0 + 2.0 ) ) - 100.132 ) * 909 ", "(2.0/(((1.0+2.0)-100.132)*909.0))");
		test("1 * ( ( 2 / ( ( 3 + 4 ) - 5 ) ) * 6 ) + 7", "((1.0*((2.0/((3.0+4.0)-5.0))*6.0))+7.0)");
		test("1 * ( ( 2 / ( ( 3 + 4 + 5 - 99 ) ) * 6 ) + 7", null);
	}
	
}
