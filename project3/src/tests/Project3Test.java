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
		private String resultPath;
		private String tempPath;
		
		private Harness(String part) {
			this.testPart = "benchmarking" + File.separator + part;
			inPath = testPart + File.separator + "input";
			outPath = testPart + File.separator + "output";
			expectedPath = testPart + File.separator + "expected_output";
			tempPath = testPart + File.separator + "temp";
			expectedHumanPath = testPart + File.separator + "expected_humanreadable";
			outHumanPath = testPart + File.separator + "output_humanreadable";
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
	
	@Test
	public void testSMJ() {
		Harness harness = new Harness("samples");
		ConfigGen configGen = new ConfigGen(harness.inPath);
		try {
			configGen.setJoinMethod(configGen.SMJ, 0);
			configGen.setSortMethod(configGen.EM_SORT, 3);
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
	

}
