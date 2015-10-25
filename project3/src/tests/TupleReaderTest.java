package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import nio.BinaryTupleReader;
import nio.NormalTupleReader;
import nio.TupleReader;

import org.junit.Test;

public class TupleReaderTest {

	//@Test
	public void test() {
		try {
			TupleReader reader = new NormalTupleReader("sandbox/Boats_humanreadable");
			System.out.println(reader.read());
			System.out.println(reader.read());
			System.out.println(reader.read());
			System.out.println(reader.read());
			reader.reset();
			System.out.println(reader.read());
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testBinaryTupleRead() {
		try {
			TupleReader reader = new BinaryTupleReader("sandbox/Boats");
			reader.reset();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
