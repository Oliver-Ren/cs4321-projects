package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import nio.BinaryFileWriter;
import nio.NormalTupleReader;
import nio.TupleReader;

import org.junit.Test;

import util.Tuple;

public class BinaryTupleWriterTest {

	@Test
	public void test() {
		try{
			TupleReader reader = new
					NormalTupleReader("sandbox/Boats_humanreadable");
			BinaryFileWriter writer = new BinaryFileWriter("sandBox/HMY");		
//		for(int i =0 ; i< 1000; i++){
//			Tuple t = reader.read();
//			writer.write(t);
//		}
//		writer.close();
		Tuple t;	
			while( ( t = reader.read())!=null){
					writer.write(t);
				
				
			}
			writer.close();
			//System.out.println(Diff.areSame("sandBox/Boats", "sandBox/HMY"));
			//System.out.println(t.toString());
			
			
		
		}catch(IOException e){
			e.printStackTrace();
		}
	
		
	}
}
