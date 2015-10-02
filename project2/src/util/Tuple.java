package util;

import java.io.IOException;
import java.io.PrintStream;

public class Tuple {

	public int[] cols = null;
	
	public int get(int i) {
		return cols[i];
	}
	
	public int length() {
		return cols.length;
	}
	
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
		return sb.toString();
	}
	
	public void dump(PrintStream ps) {
		try {
			String str = toString() + '\n';
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
