package util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

/**
 * The abstraction of a table file.
 * @author Guantian Zheng (gz94)
 *
 */
public class Table {
	
	public String name = "";
	public List<String> schema = null;
	
	private BufferedReader br = null;
	
	/**
	 * Read the next line of the table.
	 * @return the next line wrapped in a Tuple
	 */
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
	
	/**
	 * Close the current buffered reader 
	 * and open a new one.
	 */
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
	
	/**
	 * Constructor.
	 * @param name the table name / alias
	 * @param schema the schema
	 * @param br the file reader
	 */
	public Table(String name, List<String> schema, BufferedReader br) {
		this.name = name;
		this.schema = schema;
		this.br = br;
	}
	
}

