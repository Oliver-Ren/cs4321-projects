package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import nio.BinaryTupleReader;
import nio.BinaryTupleWriter;
import nio.FormatConverter;
import nio.NormalTupleReader;
import nio.NormalTupleWriter;
import nio.TupleReader;
import nio.TupleWriter;

import org.junit.Test;

import util.Tuple;

/**
 * Unit tests for the tuple writer and tuple reader.
 * 
 * @author Chengxiang Ren
 *
 */
public class TupleReaderWriterTest {
	private static class Harness {
		private static final String testPart = "iotests";
		private String inPath;
		private String outPath;
		private String expectedPath;
		private Harness(String fileName, String inDir, String outDir, String expectedDir) {
			inPath = testPart + File.separator + inDir + File.separator + fileName;
			outPath = testPart + File.separator + outDir + File.separator + fileName;
			expectedPath = testPart + File.separator + expectedDir + File.separator + fileName;
		}
		
		private void testIO(TupleReader reader, TupleWriter writer) throws IOException {
			Tuple t;
			while ((t = reader.read()) != null) {
				writer.write(t);
			}
			reader.close();
			writer.close();
		}
		
		private boolean verify() {
			return Diff.areTotallySame(expectedPath, outPath);
		}
	}

	/**
	 * Test case for test the normal tuple write and the normal tuple read.
	 */
	@Test
	public void testNormalReadWrite() {
		String inDir = "normal-input";
		String outDir = "normal-output";
		String expectedDir = "normal-input";
		String inPath = Harness.testPart + File.separator + inDir;
		for (String fileName : Diff.dirList(inPath)) {
			Harness h = new Harness(fileName, inDir, outDir, expectedDir);
			try {
				TupleReader r = new NormalTupleReader(h.inPath);
				TupleWriter w = new NormalTupleWriter(h.outPath);
				h.testIO(r, w);
				assertEquals(true, h.verify());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Test case for test the normal tuple write and the binary tuple read.
	 */
	//@Test
	public void testBinReadNormalWrite() {
		String inDir = "bin-input";
		String outDir = "normal-output";
		String expectedDir = "normal-input";
		String inPath = Harness.testPart + File.separator + inDir;
		String outPath = Harness.testPart + File.separator + outDir;
		String expPath = Harness.testPart + File.separator + expectedDir;
		Diff.cleanFolder(outPath);
		for (String fileName : Diff.dirList(expPath)) {
			Harness h = new Harness(fileName, inDir, outDir, expectedDir);
			try {
				TupleReader r = new BinaryTupleReader(h.inPath);
				TupleWriter w = new NormalTupleWriter(h.outPath);
				h.testIO(r, w);
				assertEquals(true, h.verify());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//Diff.cleanFolder(outPath);
	}
	
	/**
	 * Test case for normal tuple read and binary tuple write, a converter is used.
	 */
	@Test
	public void testNormalReadBinWrite() {
		
		String inDir = "normal-input";
		String outDir = "bin-output";
		String expectedDir = "normal-input";
		String resultDir = "normal-output";
		String inPath = Harness.testPart + File.separator + inDir;
		String outPath = Harness.testPart + File.separator + outDir;
		String resultPath = Harness.testPart + File.separator + resultDir;
		String expPath = Harness.testPart + File.separator + expectedDir;
		Diff.cleanFolder(outPath);
		Diff.cleanFolder(resultPath);
		for (String fileName : Diff.dirList(expPath)) {
			Harness h = new Harness(fileName, inDir, outDir, expectedDir);
			Harness c = new Harness(fileName, outDir, resultDir, expectedDir);
			try {
				TupleReader r = new NormalTupleReader(h.inPath);
				TupleWriter w = new BinaryTupleWriter(h.outPath);
				h.testIO(r, w);
				FormatConverter.binToNormal(outPath + File.separator + fileName
						, resultPath + File.separator + fileName);
				assertEquals(true, c.verify());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
