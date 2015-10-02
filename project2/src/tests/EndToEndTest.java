package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import client.SQLInterpreter;

public class EndToEndTest {

	@Test
	public void testPlainSelect() {
		SQLInterpreter itpr = new SQLInterpreter();
		String inPath = "sqltests/plainselect/input";
		String outPath = "sqltests/plainselect/output";	
		itpr.executeSelect(inPath, outPath);
	}

}
