package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
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
	
	public static String inputDir = "samples/input/";
	public static String outputDir = "output/";
	public static String qryPath = ""; // inputDir + "queries.sql";
	public static String dbDir = ""; // inputDir + "db/";
	public static String dataDir = ""; // dbDir + "data/";
	public static String schemaPath = ""; // dbDir + "schema.txt";
	
	public static HashMap<String, List<String>> schemas = new HashMap<String, List<String>>();
	public static HashMap<String, String> aliases = new HashMap<String, String>();
	
	public static void resetDirs(String input, String output) {
		if (input != null) {
			inputDir = input;
			qryPath = inputDir + "queries.sql";
			dbDir = inputDir + "db/";
			dataDir = dbDir + "data/";
			schemaPath = dbDir + "schema.txt";
		}
		
		if (output != null) {
			outputDir = output;
		}
	}
	
	public static String tabPath(String tabName) {
		return dataDir + tabName + ".csv";
	}
	
	public static BufferedReader getTabReader(String fileName) {
		try {
			return new BufferedReader(new FileReader(tabPath(fileName)));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Table getTable(String tabName) {
		String fileName = tabName;
		if (aliases.containsKey(fileName))
			fileName = aliases.get(fileName);
		BufferedReader br = getTabReader(fileName);
		if (br == null) return null;
		return new Table(tabName, br);
	}
	
	public List<String> getSchema(String tabName) {
		if (aliases.containsKey(tabName))
			tabName = aliases.get(tabName);
		return schemas.get(tabName);
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
				for (int i = 0; i < scm.length - 1; i++)
					val.add(scm[i + 1]);
				
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
