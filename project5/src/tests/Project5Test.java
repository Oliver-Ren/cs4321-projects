package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import nio.FormatConverter;

import org.junit.Test;

import util.SortTuple;
import client.SQLInterpreter;

public class Project5Test {
	
	// The test harness for efficient tests.
	private static class Harness {
		private String testPart;
		private String inPath;
		private String outPath;
		//private String expectedPath;
		private String expectedHumanPath;
		private String outHumanPath;
		private String expSortedHumanPath;
		private String outSortedHumanPath;
		private String resultPath;
		private String tempPath;
		private String configPath;

		private Harness(String part) {
			this.testPart = "tests" + File.separator + part;
			inPath = testPart + File.separator + "input";
			outPath = testPart + File.separator + "output";
			//expectedPath = testPart + File.separator + "expected";
			tempPath = testPart + File.separator + "temp";
			expectedHumanPath = testPart + File.separator + "expected_humanreadable";
			outHumanPath = testPart + File.separator + "output_humanreadable";
			expSortedHumanPath = testPart + File.separator + "exp_sorted_human";
			outSortedHumanPath = testPart + File.separator + "out_sorted_human";
			configPath = testPart + File.separator + "interpreter_config_file.txt";
		}

		private void executeAllQueries() {
			SQLInterpreter itpr = new SQLInterpreter();
			//itpr.execute(inPath, outPath, tempPath, false);
			try {
				itpr.execute(configPath);
			} catch (Exception e){
				e.printStackTrace();
			}
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
	 * Test case for index generation.
	 */
	//@Test
	public void testIndexGen() {
		Harness h = new Harness("index-gen");
		try{
			h.executeAllQueries();
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Test case for logical plan
	 */
	@Test
	public void testLogicalPlan() {
		Harness h = new Harness("logical-plan");
		try{
			h.executeAllQueries();
			h.convertToHumanReadable();
			h.convertToSortedHumanReadable();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

}
