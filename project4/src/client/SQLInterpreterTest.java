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
	 * mingyuan test for test index build and query evaluation
	 */
	@Test
	public void testQuery(){
		SQLInterpreter itpr = new SQLInterpreter();
		try {
			itpr.execute("tests/unit/interpreter/samples/interpreter_config_file.txt");
			System.out.println("use idex " + DBCat.idxSelect);
		} catch(IOException e){
			e.printStackTrace();
		}
	}
}
