package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import nio.FormatConverter;
import nio.TupleReader;
import nio.TupleWriter;

import org.junit.Test;

import client.SQLInterpreter;
import util.ConfigGen;
import util.SortTuple;
import util.Tuple;

/**
 * The test cases for the project 3.
 * The main test items includes the BNLJ, External Sort and the SMJ
 * @author Chengxiang Ren (cr486)
 *
 */
public class Project3Test {
	
	// The test harness for efficient tests.
	private static class Harness {
		private String testPart;
		private String inPath;
		private String outPath;
		private String expectedPath;
		private String expectedHumanPath;
		private String outHumanPath;
		private String expSortedHumanPath;
		private String outSortedHumanPath;
		private String resultPath;
		private String tempPath;
		
		private Harness(String part) {
			this.testPart = "benchmarking" + File.separator + part;
			inPath = testPart + File.separator + "input";
			outPath = testPart + File.separator + "output";
			expectedPath = testPart + File.separator + "expected";
			tempPath = testPart + File.separator + "temp";
			expectedHumanPath = testPart + File.separator + "expected_humanreadable";
			outHumanPath = testPart + File.separator + "output_humanreadable";
			expSortedHumanPath = testPart + File.separator + "exp_sorted_human";
			outSortedHumanPath = testPart + File.separator + "out_sorted_human";
		}
		
		private void executeAllQueries() {
			SQLInterpreter itpr = new SQLInterpreter();
			itpr.execute(inPath, outPath, tempPath, false);
		}
		
		private void convertToHumanReadable() {
			String[] binaryResults = Diff.dirList(outPath);
			for (String s : binaryResults) {
				String output = outPath + File.separator + s;
				String output_humanreadable = outHumanPath 
						+ File.separator + s + "_humanreadable";
				try {
					FormatConverter.binToNormal(output, output_humanreadable);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
		}
		
		private void genExpSortedHumanReadable() {
			String[] readableResults = Diff.dirList(expectedHumanPath);
			for (String s : readableResults) {
				String input = expectedHumanPath + File.separator + s;
				String output = expSortedHumanPath + File.separator + s;
				try {
					SortTuple.sortTuples(input, output);
				} catch (IOException e) {
					e.printStackTrace();
				}
		
			}	
		}
		
		private void convertToSortedHumanReadable() {
			String[] readableResults = Diff.dirList(outHumanPath);
			for (String s : readableResults) {
				String input = outHumanPath + File.separator + s;
				String output = outSortedHumanPath + File.separator + s;
				try {
					SortTuple.sortTuples(input, output);
				} catch (IOException e) {
					e.printStackTrace();
				}
		
			}		
		}
		
		private void verifyHumanReadable() {
			String[] readableResults = Diff.dirList(outHumanPath);
			for (String s : readableResults) {
				String exp_humanreadable = expectedHumanPath 
						+ File.separator + s;
				String output_humanreadable = outHumanPath 
						+ File.separator + s;
				if (!Diff.containSameTuples(exp_humanreadable, output_humanreadable)) {
					fail( "The " + s + " is not same as expected." );
				}
			}
		}
		
		private void clearOutputFolder() {
			Diff.cleanFolder(outPath);
			Diff.cleanFolder(outHumanPath);
		}
		
		private void clearTempFolder() {
			Diff.cleanFolder(tempPath);
		}
		
		
		private void verifySortedReadable() {
			String[] readableResults = Diff.dirList(outHumanPath);
			for (String s : readableResults) {
				String exp_humanreadable = expSortedHumanPath 
						+ File.separator + s;
				String output_humanreadable = outSortedHumanPath 
						+ File.separator + s;
				if (!Diff.containSameTuples(exp_humanreadable, output_humanreadable)) {
					fail( "The " + s + " is not same as expected." );
				}
			}
		}
	}

	//@Test
	public void testBNLJ() {
		Harness harness = new Harness("samples");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		try {
			configGen.setJoinMethod(configGen.BNLJ, 1);
			configGen.gen();
			harness.clearOutputFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.verifyHumanReadable();
		} catch (Exception e) {
			fail( "exception happend" );
			e.printStackTrace();
		}
	}
	
	/**
	 * Test case for sort merge join.
	 */
	@Test
	public void testSMJ() {
		Harness harness = new Harness("samples");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.SMJ, 0);
			configGen.setSortMethod(configGen.EM_SORT, 3);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			//harness.genExpSortedHumanReadable();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			harness.verifySortedReadable();
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for tuple nexted loop join.
	 */
	//@Test
	public void testTNLJ() {
		Harness harness = new Harness("samples");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.TNLJ, 0);
			configGen.setSortMethod(configGen.MEM_SORT, 3);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			//harness.genExpSortedHumanReadable();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			harness.verifyHumanReadable();
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}

}
