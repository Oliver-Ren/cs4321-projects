package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import nio.FormatConverter;

import org.junit.Test;

import util.SortTuple;
import client.SQLInterpreter;
/**
 * The test cases for the project 4.
 * The main test item is benchmarking test
 * @author mingyuan huang mh2239
 */
public class Project4Test {

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
			
			private Harness(String part) {
				this.testPart = "benchmarking" + File.separator + part;
				inPath = testPart + File.separator + "input";
				outPath = testPart + File.separator + "output";
				//expectedPath = testPart + File.separator + "expected";
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
	
	//@Test
	public void test() {
		// generate the full scan test result 
		Harness  harness = new Harness("fullScan");
		try{
			harness.executeAllQueries();
		} catch(Exception e){
			e.printStackTrace();
		}
		harness.convertToHumanReadable();
		harness.convertToSortedHumanReadable();
		harness.verifySortedReadable();
		
		//generate the clustered index scan test result 
		Harness harness1 = new Harness("clustered");
		try{
			harness1.executeAllQueries();
		} catch(Exception e){
			e.printStackTrace();
		}
		harness1.convertToHumanReadable();
		harness1.convertToSortedHumanReadable();
		
		// generate the unclustered index scan test result
//		Harness harness2 = new Harness("unclustered");
//		try{
//			harness2 = new Harness("clustered");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		harness2.convertToHumanReadable();
//		harness2.convertToSortedHumanReadable();
//		
//		
	}
	//@Test 
	public void largeDataTest(){
		//TestGenerator gen = new TestGenerator("largeData");

		// test full scan speed and its accuracy based on 
		//the expected input we generated by p3 test generator
		Harness h = new Harness("largeData");
		try{
			h.executeAllQueries();
		} catch(Exception e){
			e.printStackTrace();
		}
		h.convertToHumanReadable();
		h.convertToSortedHumanReadable();
		h.verifySortedReadable();
		
	}
	@Test
	public void largeData1Test(){
		//TestGenerator gen = new TestGenerator("largeData1");

		// test full scan speed and its accuracy based on 
		//the expected input we generated by p3 test generator
		Harness h = new Harness("largeData1");
		try{
			h.executeAllQueries();
		} catch(Exception e){
			e.printStackTrace();
		}
		h.convertToHumanReadable();
		h.convertToSortedHumanReadable();
		h.verifySortedReadable();
		
	}
	@Test
	public void largeData2Test(){
		TestGenerator gen = new TestGenerator("largeData2");
		gen.genBoats(5000, 200);
		gen.genSailors(5000, 200);
		gen.genReserves(5000, 200);
		gen.genBinaryInput();
		gen.genExpected();
		gen.convertExpHuman();
		gen.convertExpSortedHuman();
		// check full scan
		System.out.println("Start execution");
		Harness h = new Harness("largeData2");
		try{
			h.executeAllQueries();
		} catch(Exception e){
			e.printStackTrace();
		}
		h.convertToHumanReadable();
		h.convertToSortedHumanReadable();
		h.verifySortedReadable();
	}
}
