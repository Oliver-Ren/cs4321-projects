package tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import nio.NormalTupleReader;
import nio.NormalTupleWriter;
import nio.TupleReader;
import nio.TupleWriter;

import org.junit.Test;

import util.Tuple;

public class TupleReaderWriterTest {
	private static class Harness {
		private String testPart;
		private String inPath;
		private String outPath;
		private String fileName;

		
		private Harness(String fileName, String inDir, String outDir) {
			this.fileName = fileName;
			this.testPart = "iotests";
			inPath = testPart + File.separator + inDir + File.separator + fileName;
			outPath = testPart + File.separator + outDir + File.separator + fileName;
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
			return Diff.areSame(inPath, outPath);
		}
	}

	/**
	 * Test case for test the normal tuple write and the normal tuple read.
	 */
	@Test
	public void testNormalReadWrite() {
		Harness h = new Harness("query1", "normal-input", "normal-output");
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
