package client;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class SQLInterpreterTest {

	/**
	 * Tests if the configuration file is configured.
	 */
	@Test
	public void testConfig() {
		SQLInterpreter itpr = new SQLInterpreter();
		try {
			itpr.execute("tests/unit/interpreter/samples/interpreter_config_file.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
