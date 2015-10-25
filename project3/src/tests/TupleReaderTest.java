package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import nio.BinaryTupleReader;
import nio.NormalTupleReader;
import nio.TupleReader;

import org.junit.Test;

import util.Tuple;

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
	
	//@Test
	public void testBinaryTupleRead() {
		try {
			TupleReader reader = new BinaryTupleReader("sandbox/Boats");
			Tuple tup;
			while ((tup = reader.read()) != null) {
				//System.out.println(tup);
			}
			reader.reset(99);
			System.out.println(reader.read());
//			while ((tup = reader.read()) != null) {
//				System.out.println(tup);
//			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * The test cases for TupleReader's reset(long index) method.
	 */
	@Test
	public void testBinaryTupleReadResetByIndex() {
		try {
			TupleReader reader = new BinaryTupleReader("sandbox/Boats");
			Tuple tup;
			while ((tup = reader.read()) != null) {
				//System.out.println(tup);
			}
			
			reader.reset(1000);
			System.out.println(reader.read());
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
