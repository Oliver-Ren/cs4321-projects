package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;


public class Table {
	
	public String name = "";
	public String alias = "";
	public List<String> schema = null;
	
	private BufferedReader br = null;
	
	public Tuple nextTuple() {
		try {
			String line = br.readLine();
			if (line == null) return null;
			String[] elems = line.split(",");
			int len = schema.size();
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
		
		br = DBCat.getTabReader(name);
	}
	
	public Table(String name, BufferedReader br) {
		if (!DBCat.schemas.containsKey(name)) {
			this.alias = name;
			this.name = DBCat.aliases.get(name);
		}
		else
			this.name = name;
		this.br = br;
		schema = DBCat.schemas.get(name);
	}
	
}
