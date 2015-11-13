package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import btree.Rid;
import nio.TupleReader;

/**
 * The abstraction of a table file.
 * @author Guantian Zheng (gz94)
 *
 */
public class Table {
	
	public String name = "";
	public List<String> schema = null;
	
	private TupleReader tr = null;
	/**
	 * return a tuple based on its rid
	 * @param rid
	 * @return the satisfied tuple
	 */
//	public Tuple nextTuple(Rid rid){
//		try{
//			return tr.read(rid);
//		} catch(IOException e){
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * Read the next line of the table.
	 * @return the next line wrapped in a Tuple
	 */
	public Tuple nextTuple() {
		try {
			return tr.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Close the current buffered reader 
	 * and open a new one.
	 */
	public void reset() {
		try {
			tr.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor.
	 * @param name the table name / alias
	 * @param schema the schema
	 * @param br the file reader
	 */
	public Table(String name, List<String> schema, TupleReader tr) {
		this.name = name;
		this.schema = schema;
		this.tr = tr;
	}
	
}

