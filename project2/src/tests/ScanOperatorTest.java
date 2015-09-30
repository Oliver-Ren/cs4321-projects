package tests;

import static org.junit.Assert.*;

import java.io.FileReader;

import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import operators.ScanOperator;

import org.junit.Test;

import util.DBCat;

public class ScanOperatorTest {
	private ScanOperator getScanOperator(int queryId) {
		ScanOperator sop = null;
		DBCat.getInstance();
		try {
			CCJSqlParser parser = new CCJSqlParser(new FileReader(DBCat.qryPath));
			Statement statement = null;
			for (int i = 0; i <= queryId; i++) {
				statement = parser.Statement();
				if (statement == null) {
					throw new NullPointerException();
				}		
			}
			Select select = (Select) statement;
			PlainSelect ps = (PlainSelect) select.getSelectBody();
			FromItem from = ps.getFromItem();
			String tabName = from.toString().split(" ")[0];
			sop = new ScanOperator(DBCat.getTable(tabName));
		} catch (NullPointerException e) {
			System.err.println("queryId out of bound.");
		} catch (Exception e) {
			System.err.println("Exception occurred during parsing");
			e.printStackTrace();
		}
		return sop;
	}
	
	
	@Test
	public void testGetNextTuple() {
		// SELECT * FROM Sailors;
		ScanOperator scanOptr = getScanOperator(0);
		assertEquals("1,200,50", scanOptr.getNextTuple().toString());
		assertEquals("2,200,200", scanOptr.getNextTuple().toString());
		assertEquals("3,100,105", scanOptr.getNextTuple().toString());
		assertEquals("4,100,50", scanOptr.getNextTuple().toString());
		assertEquals("5,100,500", scanOptr.getNextTuple().toString());
		assertEquals("6,300,400", scanOptr.getNextTuple().toString());
		assertEquals(null, scanOptr.getNextTuple());
	}

	@Test
	public void testReset() {
		ScanOperator scanOptr = getScanOperator(0);
		//scanOptr.reset();
		assertEquals("1,200,50", scanOptr.getNextTuple().toString());
		scanOptr.reset();
		assertEquals("1,200,50", scanOptr.getNextTuple().toString());
		scanOptr.reset();
		scanOptr.reset();
		assertEquals("1,200,50", scanOptr.getNextTuple().toString());
		assertEquals("2,200,200", scanOptr.getNextTuple().toString());
		assertEquals("3,100,105", scanOptr.getNextTuple().toString());
		assertEquals("4,100,50", scanOptr.getNextTuple().toString());
		scanOptr.reset();
		assertEquals("1,200,50", scanOptr.getNextTuple().toString());
		assertEquals("2,200,200", scanOptr.getNextTuple().toString());
		assertEquals("3,100,105", scanOptr.getNextTuple().toString());
		assertEquals("4,100,50", scanOptr.getNextTuple().toString());
		assertEquals("5,100,500", scanOptr.getNextTuple().toString());
		assertEquals("6,300,400", scanOptr.getNextTuple().toString());
		assertEquals(null, scanOptr.getNextTuple());
		scanOptr.reset();
		scanOptr.reset();
		scanOptr.reset();
		assertEquals("1,200,50", scanOptr.getNextTuple().toString());
		assertEquals("2,200,200", scanOptr.getNextTuple().toString());
		assertEquals("3,100,105", scanOptr.getNextTuple().toString());
		assertEquals("4,100,50", scanOptr.getNextTuple().toString());
		assertEquals("5,100,500", scanOptr.getNextTuple().toString());
		assertEquals("6,300,400", scanOptr.getNextTuple().toString());
		assertEquals(null, scanOptr.getNextTuple());
	}

}
