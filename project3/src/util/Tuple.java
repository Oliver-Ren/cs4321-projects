package util;

import java.io.IOException;
import java.io.PrintStream;

/**
 * The abstraction of a tuple.
 * @author Guantian Zheng (gz94)
 *
 */
public class Tuple {

	public int[] cols = null;
	
	/**
	 * Get the value of the i'th column.
	 * @param i index
	 * @return the value
	 */
	public int get(int i) {
		return cols[i];
	}
	
	/**
	 * Get the length of the tuple.
	 * @return the length
	 */
	public int length() {
		return cols.length;
	}
	
	/**
	 * Hashcode of the tuple.
	 * @return the hash code of the string form
	 */
	@Override
	public int hashCode() {
		return toString().hashCode();
	}
	
	/**
	 * Overriding the equals method by
	 * comparing two arrays element by element.
	 */
	@Override
	public boolean equals(Object obj) {
		Tuple tp = (Tuple) obj;
		
		int len1 = this.cols.length;
		int len2 = tp.cols.length;
		if (len1 != len2)
			return false;
		
		for (int i = 0; i < len1; i++)
			if (this.cols[i] != tp.cols[i])
				return false;
		
		return true;
	}
	
	/**
	 * Overriding the toString method by 
	 * separating each column with commas.
	 */
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
	
	/**
	 * Dump the tuple's value into a new line
	 * in the print stream.
	 * @param ps the print stream
	 */
	public void dump(PrintStream ps) {
		try {
			String str = toString() + '\n';
			ps.write(str.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor
	 * @param cols the tuple's columns
	 */
	public Tuple(int[] cols) {
		this.cols = cols;
	}
	
}
