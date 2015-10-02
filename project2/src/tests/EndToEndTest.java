package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import client.SQLInterpreter;

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
			itpr.execute(inPath, outPath);
			String[] results = Diff.dirList(outPath);
			for (String s : results) {
				if (!Diff.areTotallySame(outPath + File.separator + s, 
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
	//@Test
	public void testConstSelect() {
		Harness harness = new Harness("constselect");
		harness.testFunction();
	}

	/**
	 * Test case for queries of simple pure selection.
	 * "and" expression is allowed, no joins or alias allowed.
	 * e.g. "SELECT * FROM Sailors WHERE Sailors.A > 1;"
	 */
	//@Test
	public void testSimplePureSelect() {
		Harness harness = new Harness("simple-pure-select");
		harness.testFunction();
	}

}
