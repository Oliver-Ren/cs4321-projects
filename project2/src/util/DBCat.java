package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

	public static String inputDir = "samples" + File.separator + "input" + File.separator;
	public static String outputDir = "samples" + File.separator + "output" + File.separator;
	public static String qryPath = "";
	public static String dbDir = "";
	public static String dataDir = "";
	public static String schemaPath = "";
	
	public static HashMap<String, List<String>> schemas = new HashMap<String, List<String>>();
	public static HashMap<String, String> aliases = new HashMap<String, String>();
	
	/**
	 * Reset the input and output directory.
	 * @param input the input directory
	 * @param output the output directory
	 */
	public static void resetDirs(String input, String output) {
		if (input != null) {
			inputDir = input + File.separator;
			qryPath = inputDir + "queries.sql";
			dbDir = inputDir + "db" + File.separator;
			dataDir = dbDir + "data" + File.separator;
			schemaPath = dbDir + "schema.txt";
		}
		
		if (output != null) {
			outputDir = output;
		}
	}
	
	/**
	 * Generate the file path of a table.
	 * @param tabName the table's name
	 * @return the file path
	 */
	public static String tabPath(String tabName) {
		return dataDir + tabName + ".csv";
	}
	
	/**
	 * Obtain the origianl file name for aliases.
	 * @param tabName the table name or an alias
	 * @return the original name
	 */
	private static String origName(String tabName) {
		if (aliases.containsKey(tabName))
			return aliases.get(tabName);
		return tabName;
	}
	
	/**
	 * Get a buffered reader of the table file.
	 * @param fileName the file name
	 * @return the buffered reader
	 */
	public static BufferedReader getTabReader(String fileName) {
		fileName = origName(fileName);
		try {
			return new BufferedReader(new FileReader(tabPath(fileName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Acquire the table schema.
	 * @param tabName the table name
	 * @return the schema as a list of strings
	 */
	public static List<String> getSchema(String tabName) {
		return schemas.get(origName(tabName));
	}
	
	/**
	 * Create a table with schema and alias.
	 * @param tabName the table name
	 * @return the created table
	 */
	public static Table getTable(String tabName) {
		BufferedReader br = getTabReader(tabName);
		if (br == null) return null;
		return new Table(tabName, getSchema(tabName), br);
	}
	
	// intentionally make the constructor private, which 
	// avoids instances being created outside the class
	private DBCat() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(schemaPath));
			String line = null;
			while ((line = br.readLine()) != null) {				
				String[] scm = line.split(" ");
				if (scm.length < 2) continue;
				
				String key = scm[0];
				List<String> val = new ArrayList<String>();
				for (int i = 1; i < scm.length; i++) {
					val.add(scm[i]);
				}
				
				schemas.put(key, val);
			}
			
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the instance of the DBCat and makes
	 * sure there are only one instance exist;
	 * 
	 * @return the instance of instance of the class
	 */
	public static DBCat getInstance() {
		if (instance == null) {
			resetDirs(inputDir, null);
			instance = new DBCat();
		}
		return instance;
	}
	
}
