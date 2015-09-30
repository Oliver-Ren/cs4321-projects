package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class Table {
	
	public String name = "";
	public String alias = "";
	public String[] schema = null;
	
	FileReader fr = null;
	BufferedReader br = null;
	
	public Tuple nextTuple() {
		if (br == null) return null;
		
		try {
			String line = br.readLine();
			if (line == null) return null;
			String[] elems = line.split(",");
			int len = (schema == null) ? elems.length : schema.length;
			int[] cols = new int[len];
			for (int i = 0; i < len; i++) {
				cols[i] = Integer.valueOf(elems[i]);
			}
			return new Tuple(cols);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void reset() {
		if (br != null) {
			try {
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		br = new BufferedReader(fr);
	}
	
	public Table(FileReader fr) {
		if (fr == null) return;
		this.fr = fr;
		br = new BufferedReader(fr);
	}
}
