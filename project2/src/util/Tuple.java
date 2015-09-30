package util;

import java.io.IOException;
import java.io.PrintStream;

public class Tuple {

	int[] cols = null;
	
	public void dump(PrintStream ps) {
		try {
			String str = cols.toString() + '\n';
			ps.write(str.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Tuple(int[] cols) {
		this.cols = cols;
	}
	
}
