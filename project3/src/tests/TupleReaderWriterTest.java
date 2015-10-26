package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import nio.BinaryTupleReader;
import nio.NormalTupleReader;
import nio.NormalTupleWriter;
import nio.TupleReader;
import nio.TupleWriter;

import org.junit.Test;

import util.Tuple;

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
				assertTrue(h.verify());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Test case for test the normal tuple write and the binary tuple read.
	 */
	@Test
	public void testBinReadNormalWrite() {
		String inDir = "bin-input";
		String outDir = "normal-output";
		String expectedDir = "normal-input";
		String inPath = Harness.testPart + File.separator + inDir;
		String expPath = Harness.testPart + File.separator + expectedDir;
		for (String fileName : Diff.dirList(expPath)) {
			Harness h = new Harness(fileName, inDir, outDir, expectedDir);
			try {
				TupleReader r = new BinaryTupleReader(h.inPath);
				TupleWriter w = new NormalTupleWriter(h.outPath);
				h.testIO(r, w);
				assertTrue(h.verify());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
