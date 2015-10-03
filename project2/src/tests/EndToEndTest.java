package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import client.SQLInterpreter;

/**
 * This is the end-to-end test class which tests 
 * every differect functions of the interpretor in througb out 
 * the whole process.
 * 
 * @author Mingyuan Huang (MH2239)
 *
 */
public class EndToEndTest {
	// The test harness for efficient tests.
	private static class Harness {
		private String testPart;
		private String inPath;
		private String outPath;
		private String expectedPath;
		
		private Harness(String part) {
			this.testPart = "sqltests" + File.separator + part;
			inPath = testPart + File.separator + "input";
			outPath = testPart + File.separator + "output";
			expectedPath = testPart + File.separator + "expected_output";
		}
		
		private void testFunction() {
			SQLInterpreter itpr = new SQLInterpreter();
			itpr.execute(inPath, outPath, false);
			String[] results = Diff.dirList(outPath);
			for (String s : results) {
				if (!Diff.areSame(outPath + File.separator + s, 
					expectedPath + File.separator + s)) {
					fail( "The " + s + " is not same as expected." );
				}
				
			}
		}
		
		private void testFunctionNoOrder() {
			SQLInterpreter itpr = new SQLInterpreter();
			itpr.execute(inPath, outPath, true);
			String[] results = Diff.dirList(outPath);
			for (String s : results) {
				if (!Diff.containSameTuples(outPath + File.separator + s, 
					expectedPath + File.separator + s)) {
					fail( "The " + s + " is not same as expected." );
				}
				
			}
		}
		
		
	}
	
	@Test
	public void testSimpleScan() {
		Harness harness = new Harness("scan");
		harness.testFunction();		
	}
	
	/**
	 * Test case for queries of constant selection. 
	 * "and" expression is allowed.
	 * e.g. "select * from Sailors where 1 = 1 and 0 <> 2;"
	 */
	@Test
	public void testConstSelect() {
		Harness harness = new Harness("constselect");
		harness.testFunction();
	}

	/**
	 * Test case for queries of simple pure selection.
	 * "and" expression is allowed, no joins or alias allowed.
	 * e.g. "SELECT * FROM Sailors WHERE Sailors.A > 1;"
	 */
	@Test
	public void testSimplePureSelect() {
		Harness harness = new Harness("simple-pure-select");
		harness.testFunction();
	}
	
	/**
	 * Test case for queries of cross-product.
	 */
	@Test
	public void testCrossProduct() {
		Harness harness = new Harness("cross-product");
		harness.testFunctionNoOrder();
	}
	
	/**
	 * Test case for queries of join.
	 */
	@Test
	public void testJoin() {
		Harness harness = new Harness("join");
		harness.testFunctionNoOrder();
	}
	
	/**
	 * Test case for queries of projection.
	 */
	@Test
	public void testProjection() {
		Harness harness = new Harness("projection");
		harness.testFunctionNoOrder();
	}
	
	/**
	 * Test case for queries of order-by.
	 */
	@Test
	public void testOrderBy() {
		Harness harness = new Harness("order-by");
		harness.testFunction();
	}
	
	/**
	 * Test case for queries of distinct.
	 */
	@Test
	public void testDistinct() {
		Harness harness = new Harness("distinct");
		harness.testFunctionNoOrder();
	}
	
	/**
	 * Test case for queries of distinct with order by.
	 */
	@Test
	public void testDistinctOrdered() {
		Harness harness = new Harness("distinct-ordered");
		harness.testFunction();
	}
	
	/**
	 * Test case for sample queries.
	 */
	@Test
	public void testSamples() {
		Harness harness = new Harness("samples");
		harness.testFunction();
	}
	
	

}
