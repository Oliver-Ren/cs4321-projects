package util;

import java.io.IOException;
import java.io.PrintStream;

public class Tuple {

	int[] cols = null;
	
	@Override
	public boolean equals(Object obj) {
		Tuple tp = (Tuple) obj;
		if (this.cols.length != tp.cols.length)
			return false;
		int len = this.cols.length;
		for (int i = 0; i < len; i++)
			if (this.cols[i] != tp.cols[i])
				return false;
		return true;
	}
	
	@Override
	public String toString() {
		if (cols.length < 1) return "";
		StringBuilder sb = new StringBuilder(String.valueOf(cols[0]));
		int i = 1;
		while (i < cols.length) {
			sb.append(',');
			sb.append(String.valueOf(cols[i++]));
		}
		sb.append('\n');
		return sb.toString();
	}
	
	public void dump(PrintStream ps) {
		try {
			ps.write(toString().getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Tuple(int[] cols) {
		this.cols = cols;
	}
	
}
