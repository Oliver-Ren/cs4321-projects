package tests;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import net.sf.jsqlparser.parser.CCJSqlParser;

import org.junit.Test;

import util.DBCat;
import util.SelState;
import util.Tuple;
import visitors.ConcreteExpVisitor;
import visitors.SelExpVisitor;

/**
 * Test of concrete expression visitor.
 * @author Chengxiang Ren (cr486)
 *
 */
public class ConcreteExpVisitorTest {

	/**
	 * Test the visitor with constant conditions.
	 * @param query
	 * @return true / false
	 */
	private boolean testVisitor(String query) {
		ConcreteExpVisitor vi = new SelExpVisitor(null);
		try {
			CCJSqlParser parser = new CCJSqlParser(new StringReader(query));
			SelState ss = new SelState(parser.Statement());
			ss.where.accept(vi);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vi.getFinalCondition();
	}
	
	/**
	 * Test the visitor with a tuple with specified schema. 
	 * @param query
	 * @param cols the columns of the tuple
	 * @return true / false
	 */
	private boolean testVisitorWithTuple(String query, int[] cols) {
		DBCat.getInstance();
		Tuple tuple = new Tuple(cols);
		SelExpVisitor vi = new SelExpVisitor(DBCat.getSchema("Sailors"));
		try {
			CCJSqlParser parser = new CCJSqlParser(new StringReader(query));
			SelState ss = new SelState(parser.Statement());
			vi.setTuple(tuple);
			ss.where.accept(vi);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vi.getFinalCondition();
	}
	
	/**
	 * Test = condition.
	 */
	@Test
	public void testVisitEqualsTo() {
		String query = "select * from aa where 1=2";
		assertEquals(false, testVisitor(query));
		
		query = "select a.b from a where 0 = 0";
		assertEquals(true, testVisitor(query));
		
		query = "select a.b from a where 99999999 = 99999999";
		assertEquals(true, testVisitor(query));
	}
	
	/**
	 * Test != condition.
	 */
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
	
	/**
	 * Test > condition.
	 */
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
	
	/**
	 * Test >= condition.
	 */
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
	
	/**
	 * Test < condition.
	 */
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
	
	/**
	 * Test <= condition.
	 */
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
	
	/**
	 * Test AND expression.
	 */
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
	
	/**
	 * Test column expression.
	 */
	@Test
	public void testColumn() {
		String query = "select * from Sailors where Sailors.A = 1";
		int[] cols = {1, 200, 50};
		assertEquals(true, testVisitorWithTuple(query, cols));
		
		int[] cols2 = {2, 200, 50};
		assertEquals(false, testVisitorWithTuple(query, cols2));
		
		query = "select * from Sailors where Sailors.A = 1 and 1 > -9999";
		assertEquals(true, testVisitorWithTuple(query, cols));
		assertEquals(false, testVisitorWithTuple(query, cols2));
		
		query = "select * from Sailors where Sailors.A = 1 and 1 <= -9999";
		assertEquals(false, testVisitorWithTuple(query, cols));
		assertEquals(false, testVisitorWithTuple(query, cols2));
		
		query = "select * from Sailors where Sailors.A = 2 and Sailors.B < 300";
		assertEquals(false, testVisitorWithTuple(query, cols));
		assertEquals(true, testVisitorWithTuple(query, cols2));
	}

}
