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
			File dir = new File(outHumanPath);
			if (!dir.exists()) {
				dir.mkdir();
			} else {
				Diff.cleanFolder(outHumanPath);
			}
			
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
		
		
		private void convertToSortedHumanReadable() {
			String[] readableResults = Diff.dirList(outHumanPath);
			File dir = new File(outSortedHumanPath);
			if (!dir.exists()) {
				dir.mkdir();
			} else {
				Diff.cleanFolder(outSortedHumanPath);
			}
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
				if (!Diff.areTotallySame(exp_humanreadable, output_humanreadable)) {
					fail( "The " + s + " is not same as expected." );
				}
			}
		}
		
		private void verifyHumanReadable(int start, int end) {
			for (int i = start; i <= end; i++) {
				String s = "query" + i + "_humanreadable";
				String exp_humanreadable = expectedHumanPath 
						+ File.separator + s;
				String output_humanreadable = outHumanPath 
						+ File.separator + s;
				if (!Diff.areTotallySame(exp_humanreadable, output_humanreadable)) {
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
				if (!Diff.areTotallySame(exp_humanreadable, output_humanreadable)) {
					fail( "The " + s + " is not same as expected." );
				}
			}
		}
		
		private void verifySortedReadable(int start, int end) {
			for (int i = start; i <= end; i++) {
				String s = "query" + i + "_humanreadable";
				String exp_humanreadable = expSortedHumanPath 
						+ File.separator + s;
				String output_humanreadable = outSortedHumanPath 
						+ File.separator + s;
				if (!Diff.areTotallySame(exp_humanreadable, output_humanreadable)) {
					fail( "The " + s + " is not same as expected." );
				}
			}
		}
		
	}
	
	/**
	 * Test case for tuple nested loop join with the given grading test cases.
	 */
	//@Test
	public void testTNLJGrading() {
		Harness harness = new Harness("grading_test_cases");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.TNLJ, 0);
			configGen.setSortMethod(configGen.MEM_SORT, 0);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for block nested loop join and in memory sort
	 * with the given grading test cases
	 */
	@Test
	public void test_BNLJ_MemSort_Grading() {
		Harness harness = new Harness("grading_test_cases");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.BNLJ, 1);
			configGen.setSortMethod(configGen.MEM_SORT, 0);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for block nested loop join and external merge sort
	 * with the given grading test cases
	 */
	@Test
	public void test_BNLJ_EMSort_Grading() {
		Harness harness = new Harness("grading_test_cases");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.BNLJ, 1);
			configGen.setSortMethod(configGen.EM_SORT, 3);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for sort merge join and external merge sort
	 * with the given grading test cases
	 */
	@Test
	public void test_SMJ_EMSort_Grading() {
		Harness harness = new Harness("grading_test_cases");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.SMJ, 0);
			configGen.setSortMethod(configGen.EM_SORT, 3);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for block nested loop join and in memory sort
	 * with the given grading test cases
	 */
	@Test
	public void test_SMJ_MemSort_Grading() {
		Harness harness = new Harness("grading_test_cases");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.SMJ, 0);
			configGen.setSortMethod(configGen.MEM_SORT, 0);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	
	/**
	 * Test case for tuple nested loop join with the given grading test cases.
	 * and with random generated large size table.
	 */
	//@Test
	public void testTNLJGradingLarge() {
		Harness harness = new Harness("grading_test_cases_large");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.TNLJ, 0);
			configGen.setSortMethod(configGen.MEM_SORT, 0);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for block nested loop join and in memory sort
	 * with the given grading test cases and with random generated large size table.
	 */
	@Test
	public void test_BNLJ_MemSort_Grading_Large() {
		Harness harness = new Harness("grading_test_cases_large");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.BNLJ, 1);
			configGen.setSortMethod(configGen.MEM_SORT, 0);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for block nested loop join and external merge sort
	 * with the given grading test cases and with random 
	 * generated large size table.
	 */
	@Test
	public void test_BNLJ_EMSort_Grading_Large() {
		Harness harness = new Harness("grading_test_cases_large");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.BNLJ, 1);
			configGen.setSortMethod(configGen.EM_SORT, 3);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for sort merge join and external merge sort
	 * with the given grading test cases and with 
	 * random generated large size table.
	 */
	@Test
	public void test_SMJ_EMSort_Grading_Large() {
		Harness harness = new Harness("grading_test_cases_large");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.SMJ, 0);
			configGen.setSortMethod(configGen.EM_SORT, 3);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}
	
	/**
	 * Test case for block nested loop join and in memory sort
	 * with the given grading test cases and with 
	 * random generated large size table.
	 */
	@Test
	public void test_SMJ_MemSort_Grading_Large() {
		Harness harness = new Harness("grading_test_cases_large");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.SMJ, 0);
			configGen.setSortMethod(configGen.MEM_SORT, 0);
			configGen.gen();
			harness.clearOutputFolder();
			harness.clearTempFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			// The order does not matter.
			harness.verifySortedReadable(1, 31);
			// The order should be correct.
			harness.verifyHumanReadable(32, 40);
			finished = true;
		} catch (Exception e) {
			finished = false;
			e.printStackTrace();
		}
		assertEquals(true, finished);
	}

	//@Test
	public void testBNLJ() {
		Harness harness = new Harness("large2");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		try {
			configGen.setJoinMethod(configGen.BNLJ, 1);
			configGen.gen();
			harness.clearOutputFolder();
			harness.executeAllQueries();
			harness.convertToHumanReadable();
			harness.convertToSortedHumanReadable();
			harness.verifySortedReadable();
		} catch (Exception e) {
			fail( "exception happend" );
			e.printStackTrace();
		}
	}
	
	/**
	 * Test case for sort merge join.
	 */
	//@Test
	public void testSMJ() {
		Harness harness = new Harness("large1");
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
		Harness harness = new Harness("small1");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		boolean finished = false;
		try {
			configGen.setJoinMethod(configGen.TNLJ, 0);
			configGen.setSortMethod(configGen.MEM_SORT, 0);
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
