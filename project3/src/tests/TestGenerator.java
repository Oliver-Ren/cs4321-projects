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
 * @author Chenxiang Ren (cr486).
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
	
	/**
	 * Construct the generator using the part name.
	 * @param part	the part to be tested
	 */
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
	
	/**
	 * Generates the Sailors table.
	 * @param numTup
	 * @param range
	 */
	public void genSailors(int numTup, int range) {
		try {
			RandomDataGenerator.tuplesGenerator(Sailors + "_humanreadable", 
					numTup, range, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Boats table.
	 * @param numTup
	 * @param range
	 */
	public void genBoats(int numTup, int range) {
		try {
			RandomDataGenerator.tuplesGenerator(Boats + "_humanreadable", 
					numTup, range, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Reserves Table.
	 * @param numTup
	 * @param range
	 */
	public void genReserves(int numTup, int range) {
		try {
			RandomDataGenerator.tuplesGenerator(Reserves + "_humanreadable", 
					numTup, range, 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Table 1.
	 * @param numTup
	 * @param range
	 */
	public void genTable1(int numTup, int range) {
		try {
			String file = dataPath + File.separator + "TestTable1";
			RandomDataGenerator.tuplesGenerator(file + "_humanreadable", 
					numTup, range, 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Table 2.
	 * @param numTup
	 * @param range
	 */
	public void genTable2(int numTup, int range) {
		try {
			String file = dataPath + File.separator + "TestTable2";
			RandomDataGenerator.tuplesGenerator(file + "_humanreadable", 
					numTup, range, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Table 3.
	 * @param numTup
	 * @param range
	 */
	public void genTable3(int numTup, int range) {
		try {
			String file = dataPath + File.separator + "TestTable3";
			RandomDataGenerator.tuplesGenerator(file + "_humanreadable", 
					numTup, range, 2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Generates the Table 4.
	 * @param numTup
	 * @param range
	 */
	public void genTable4(int numTup, int range) {
		try {
			String file = dataPath + File.separator + "TestTable4";
			RandomDataGenerator.tuplesGenerator(file + "_humanreadable", 
					numTup, range, 3);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Generates the binary version of the tables.
	 */
	public void genBinaryInput() {
		String[] normalData = Diff.dirList(dataPath);
		for (String s : normalData) {
			if (s.contains("_humanreadable")) {
				String input = dataPath + File.separator + s;
				String output = dataPath + File.separator 
						+ s.substring(0, s.lastIndexOf("_humanreadable"));	
				try {
					FormatConverter.normalToBin(input, output);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Generates the expected result using tuple nested loop join.
	 */
	public void genExpected() {
		ConfigGen configGen = new ConfigGen(inPath);
		File dir = new File(expectedPath);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			Diff.cleanFolder(expectedPath);
		}
		configGen.setJoinMethod(configGen.TNLJ, 0);
		configGen.setSortMethod(configGen.MEM_SORT, 0);
		configGen.gen();
		SQLInterpreter itpr = new SQLInterpreter();
		itpr.execute(inPath, expectedPath, tempPath, false);
	}
	
	/**
	 * Converts the Expected result into the human readable version.
	 */
	public void convertExpHuman() {
		String[] binaryResults = Diff.dirList(expectedPath);
		
		File dir = new File(expectedHumanPath);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			Diff.cleanFolder(expectedHumanPath);
		}
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
	
	/**
	 * Converts the human readable version of the expression into sorted.
	 */
	public void convertExpSortedHuman() {
		String[] readableResults = Diff.dirList(expectedHumanPath);
		File dir = new File(expSortedHumanPath);
		if (!dir.exists()) {
			dir.mkdir();
		} else {
			Diff.cleanFolder(expSortedHumanPath);
		}
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
//
		TestGenerator gen = new TestGenerator("project4");
//		gen.genBoats(1000, 5000);
//		gen.genSailors(400, 3000);
//		gen.genReserves(150, 100);
//		gen.genTable1(300, 60);
//		gen.genTable2(1000, 800);
//		gen.genTable3(200, 100);
//		gen.genTable4(500, 200);
//
//		gen.genBinaryInput();
		gen.genExpected();
		gen.convertExpHuman();
		gen.convertExpSortedHuman();
	}
	
}
