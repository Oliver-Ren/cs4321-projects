package util;

import java.io.IOException;
import java.io.PrintStream;

public class Tuple {

	int[] cols = null;
	
	public void dump(PrintStream ps) {
		try {
			if (cols.length < 1) return;
			StringBuilder sb = new StringBuilder(String.valueOf(cols[0]));
			int i = 1;
			while (i < cols.length) {
				sb.append(',');
				sb.append(String.valueOf(cols[i++]));
			}
			sb.append('\n');
			ps.write(sb.toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Tuple(int[] cols) {
		this.cols = cols;
	}
	
}
