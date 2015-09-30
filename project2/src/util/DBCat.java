package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

/**
 * The <tt>DBCat</tt> class represents a database catalog which keeps track
 * of information such as where a file for a given table is located, what
 * the schema of different tables is, and so on.
 * <p>
 * The catalog is a global entity that various components of the system may
 * want to access, it adapts the Singleton Pattern which means at most one 
 * instance of this class could exist.
 * 
 * @author Guantian Zheng (gz94)
 * @author Chengxiang Ren (cr486)
 *
 */
public class DBCat {
	private static DBCat instance;	// The instance of DBCat;
	public static String inputDir = "samples/input/";
	public static String outputDir = "";
	
	// intentionally make the constructor private, which 
	// avoids instances being created outside the class
	private DBCat() {
		
	}
	
	/**
	 * Returns the instance of instance of the class and makes
	 * sure there are only one instance exist;
	 * @return the instance of instance of the class
	 */
	public static DBCat getInstance() {
		if (instance == null) {
			instance = new DBCat();
		}
		return instance;
	}
	
	public static HashMap<String, String[]> schemas = new HashMap<String, String[]>();
	
	public static Table getTable(String tabName) {
		try {
			return new Table(new FileReader(inputDir + "db/data/" + tabName + ".csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	
	
}
