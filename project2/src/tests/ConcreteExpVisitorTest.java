package tests;

import static org.junit.Assert.*;

import java.io.StringReader;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.parser.CCJSqlParser;

import org.junit.Test;

import util.SelState;
import visitors.ConcreteExpVisitor;

public class ConcreteExpVisitorTest {

	private boolean testVisitor(String query) {
		ConcreteExpVisitor vi = new ConcreteExpVisitor();
		try {
			CCJSqlParser parser = new CCJSqlParser(new StringReader(query));
			SelState ss = new SelState(parser.Statement());
			ss.where.accept(vi);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vi.getFinalCondition();
	}
	
	@Test
	public void testVisitEqualsTo() {
		String query = "select * from aa where 1=2";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 0 = 0";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 99999999 = 99999999";
		assertEquals(true, testVisitor(query));
	}
	
	@Test
	public void testVisitNotEqualsTo() {
		String query = "select * from aa where 1 <> 2";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 0 <> 0";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 999999999 <> 999999999";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 999999999 != 99999999";
		assertEquals(true, testVisitor(query));
	}
	
	@Test
	public void testVisitGreaterThan() {
		String query = "select * from aa where 1 > 2";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 0 > 0";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 999999999 > 999999999";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 999999999 > 99999999";
		assertEquals(true, testVisitor(query));
	}
	
	@Test
	public void testVisitGreaterThanEquals() {
		String query = "select * from aa where 1 >= 2";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 0 >= 0";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 999999999 >= 999999999";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 999999999 >= 99999999";
		assertEquals(true, testVisitor(query));
	}
	
	@Test
	public void testVisitMinorThan() {
		String query = "select * from aa where 1 < 2";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 0 < 0";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 999999999 < 999999999";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 999999999 < 99999999";
		assertEquals(false, testVisitor(query));
	}
	
	@Test
	public void testVisitMinorThanEquals() {
		String query = "select * from aa where 1 <= 2";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 0 <= 0";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 999999999 <= 999999999";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 999999999 <= 99999999";
		assertEquals(false, testVisitor(query));
	}
	
	@Test
	public void testAndExpression() {
		String query = "select * from aa where 1 <= 2 and 1 = 1";
		assertEquals(true, testVisitor(query));
		
		query = "select * from aa where 1 = 2 and 1 = 1";
		assertEquals(false, testVisitor(query));
		
		query = "select * from aa where 1 > -1 and 1 < 999";
		assertEquals(true, testVisitor(query));
		
		query = "select * from aa where 1 > -1 and 1 < 999 and 2 <= 3";
		assertEquals(true, testVisitor(query));
	}
	

}
