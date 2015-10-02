package tests;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import client.SQLInterpreter;

public class EndToEndTest {
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
	}
	
	@Test
	public void testSimpleScan() {
		SQLInterpreter itpr = new SQLInterpreter();
		Harness harness = new Harness("scan");
		itpr.execute(harness.inPath, harness.outPath);
		String[] results = Diff.dirList(harness.outPath);
		for (String s : results) {
			if (!Diff.areTotallySame(harness.outPath + File.separator + s, 
				harness.expectedPath + File.separator + s)) {
				fail( "The " + s + " is not same as expected." );
			}
			
		}
	}

	//@Test
	public void testPlainSelect() {
		SQLInterpreter itpr = new SQLInterpreter();
		Harness harness = new Harness("plainselect");
		itpr.execute(harness.inPath, harness.outPath);
		String[] results = Diff.dirList(harness.outPath);
		for (String s : results) {
			if (!Diff.areTotallySame(harness.outPath + File.separator + s, 
				harness.expectedPath + File.separator + s)) {
				fail( "The " + s + " is not same as expected." );
			}
			
		}
	}

}
