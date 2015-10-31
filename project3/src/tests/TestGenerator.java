package tests;

import java.io.File;
import java.io.IOException;

import client.SQLInterpreter;
import nio.FormatConverter;
import util.ConfigGen;
import util.RandomDataGenerator;
import util.SortTuple;

/** 
 * Utility Class for generating test cases.
 * 
 * @author Chenxiang Ren
 *
 */
public class TestGenerator {
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
	private String dataPath;
//	private File in;
//	private File out;
//	private File expected;
//	private File temp;
//	private File expectedHuman;
//	private File outHuman;
//	private File expSortedHuman;
//	private File outSortedHuman;
	
	public String Sailors;
	public String Boats;
	public String Reserves;
	
	
	public TestGenerator(String part) {
		this.testPart = "benchmarking" + File.separator + part;
		inPath = testPart + File.separator + "input";
		outPath = testPart + File.separator + "output";
		expectedPath = testPart + File.separator + "expected";
		tempPath = testPart + File.separator + "temp";
		expectedHumanPath = testPart + File.separator + "expected_humanreadable";
		outHumanPath = testPart + File.separator + "output_humanreadable";
		expSortedHumanPath = testPart + File.separator + "exp_sorted_human";
		outSortedHumanPath = testPart + File.separator + "out_sorted_human";
		dataPath = inPath + File.separator + "db" + File.separator + "data";
		Sailors = dataPath + File.separator + "Sailors";
		Boats = dataPath + File.separator + "Boats";
		Reserves = dataPath + File.separator + "Reserves";

		
//		outPath = testPart + File.separator + "output";
//		
//		in = new File(inPath);
//		out = new File(outPath);
//		expected = new File(expectedPath);
//		temp = new File(tempPath);
//		expectedHuman = new File(expectedHumanPath);
//		outHuman = new File(outHumanPath);
//		expSortedHuman = new File(expSortedHumanPath);
//		outSortedHuman = new File(outSortedHumanPath);
	}
	
	
	public void genSailors(int numTup, int range) {
		try {
			RandomDataGenerator.tuplesGenerator(Sailors + "_humanreadable", 
					numTup, range, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void genBoats(int numTup, int range) {
		try {
			RandomDataGenerator.tuplesGenerator(Boats + "_humanreadable", 
					numTup, range, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void genReserves(int numTup, int range) {
		try {
			RandomDataGenerator.tuplesGenerator(Reserves + "_humanreadable", 
					numTup, range, 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void genBinaryInput() {
		try {
			FormatConverter.normalToBin(Boats + "_humanreadable", Boats);
			FormatConverter.normalToBin(Sailors + "_humanreadable", Sailors);
			FormatConverter.normalToBin(Reserves + "_humanreadable", Reserves);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void genExpected() {
		ConfigGen configGen = new ConfigGen(inPath);
		Diff.cleanFolder(expectedPath);
		configGen.setJoinMethod(configGen.TNLJ, 0);
		configGen.setSortMethod(configGen.MEM_SORT, 0);
		configGen.gen();
		SQLInterpreter itpr = new SQLInterpreter();
		itpr.execute(inPath, expectedPath, tempPath, false);
	}
	
	public void convertExpHuman() {
		String[] binaryResults = Diff.dirList(expectedPath);
		Diff.cleanFolder(expectedHumanPath);
		for (String s : binaryResults) {
			String input = expectedPath + File.separator + s;
			String output = expectedHumanPath + File.separator + s + "_humanreadable";	
			try {
				FormatConverter.binToNormal(input, output);
			} catch (IOException e) {
				e.printStackTrace();
			}
	
		}	
	}
	
	public void convertExpSortedHuman() {
		String[] readableResults = Diff.dirList(expectedHumanPath);
		Diff.cleanFolder(expSortedHumanPath);
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
	
	
	public static void main(String[] args) {
		TestGenerator gen = new TestGenerator("small");
		gen.genBoats(5, 10);
		gen.genSailors(10, 20);
		gen.genReserves(5, 10);
		gen.genBinaryInput();
		gen.genExpected();
		gen.convertExpHuman();
		gen.convertExpSortedHuman();
	}
	
}
