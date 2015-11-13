package client;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import util.DBCat;

public class SQLInterpreterTest {

	/**
	 * Tests if the configuration file is configured.
	 */
	//@Test
	public void testConfig() {
		SQLInterpreter itpr = new SQLInterpreter();
		try {
			itpr.execute("tests/unit/interpreter/samples/interpreter_config_file.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 *test for test index build and query evaluation
	 *@author mingyuan
	 */
	@Test
	public void testQuery(){
		System.out.println("Build index on unclustered file and evaluate queries");
		SQLInterpreter itpr = new SQLInterpreter();
		try {
			itpr.execute("tests/unit/interpreter/queryEvaluation/unclustered/interpreter_config_file.txt");
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Unclustered file index built, queries evaluation done");
		System.out.println("Build index on clustered file and evaluate queries");
		try {
			itpr.execute("tests/unit/interpreter/queryEvaluation/clustered/interpreter_config_file.txt");
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Clustered file index built, queries evalution done");
		System.out.println("Evaluate queries using full scan");
		try {
			itpr.execute("tests/unit/interpreter/queryEvaluation/fullScan/interpreter_config_file.txt");
		} catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("queries evalution done");
		
	}
	// evalute exsiting queries using three different methods (full scan, index, )
	
}
