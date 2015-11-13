package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import btree.BPlusTree;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.OrderByElement;
import nio.BinaryTupleReader;
import nio.BinaryTupleWriter;
import nio.NormalTupleReader;
import nio.TupleReader;
import operators.Operator;
import operators.logic.LogicOperator;
import operators.logic.LogicScanOp;
import operators.logic.LogicSortOp;
import visitors.PhysicalPlanBuilder;

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
	public static String tempDir = "temp" + File.separator;
	public static String configPath = "";
	public static String qryPath = "";
	public static String dbDir = "";
	/**
	 * in the format of 
	 */
	public static String dataDir = "";
	public static String schemaPath = "";
	public static String idxInfoPath = "";
	public static String idxsDir = "";
	
	public enum JoinMethod {
		TNLJ, BNLJ, SMJ;
	}
	
	public static JoinMethod joinMethod = JoinMethod.TNLJ;
	public static Integer joinBufPgs = null;
	
	public enum SortMethod {
		INMEM, EXTERN;
	}
	
	public static SortMethod sortMethod = SortMethod.INMEM;
	public static Integer sortBufPgs = null;
	
	public static boolean isBinary = true;
	
	public static boolean idxSelect = false;	// Should we use index for select
	
	public static HashMap<String, List<String>> schemas = 
			new HashMap<String, List<String>>();
	public static HashMap<String, String> aliases = 
			new HashMap<String, String>();
	public static HashMap<String, IndexInfo> idxInfo = 
			new HashMap<String, IndexInfo>();
	
	/**
	 * Reset the input and output directory.
	 * @param input the input directory
	 * @param output the output directory
	 */
	public static void resetDirs(String input, String output, String temp) {
		if (input != null) {
			inputDir = input + File.separator;
			configPath = inputDir + "plan_builder_config.txt";
			qryPath = inputDir + "queries.sql";
			dbDir = inputDir + "db" + File.separator;
			dataDir = dbDir + "data" + File.separator;
			schemaPath = dbDir + "schema.txt";
			idxInfoPath = dbDir + "index_info.txt";
			idxsDir = dbDir + "indexes" + File.separator;
		}
		
		if (output != null) {
			outputDir = output + File.separator;
		}
		
		if (temp != null) {
			tempDir = temp + File.separator;
		}
		
		resetConfig();
		resetSchemas();
		resetIdxInfo();
	}
	
	private static void defConfig() {
		joinMethod = JoinMethod.TNLJ;
		joinBufPgs = 1;
		sortMethod = SortMethod.INMEM;
		sortBufPgs = 3;
	}
	
	private static void resetConfig() {
		defConfig();
		BufferedReader br;
		
		try {
			br = new BufferedReader(new FileReader(configPath));
			String[] join = br.readLine().split(" ");
			String[] sort = br.readLine().split(" ");
			
			idxSelect = (br.readLine().equals("1"));
			
			System.out.println("join: " + Arrays.toString(join));
			System.out.println("sort: " + Arrays.toString(sort));
			
			if (join[0].equals("1")) {
				joinMethod = JoinMethod.BNLJ;
				joinBufPgs = Math.max(1, Integer.valueOf(join[1]));
			}
			else if (join[0].equals("2"))
				joinMethod = JoinMethod.SMJ;
			
			if (!sort[0].equals("0")) {
				sortMethod = SortMethod.EXTERN;
				sortBufPgs = Math.max(3, Integer.valueOf(sort[1]));
			}
			
			
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			defConfig();
		}
	}
	
	/**
	 * Reset the schemas in the input directory.
	 */
	private static void resetSchemas() {
		BufferedReader br;
		schemas.clear();
		
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
	
	private static void resetIdxInfo() {
		idxInfo.clear();
		// need also to clear indexes directory
		
		try (BufferedReader br = new BufferedReader(
				new FileReader(idxInfoPath))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] str = line.split(" ");
				String relt = str[0];
				String attr = str[1];
				boolean clust = (str[2].equals("1"));
				int ord = Integer.parseInt(str[3]);
				
				IndexInfo ii = new IndexInfo(relt, attr, clust, ord);
				idxInfo.put(relt, ii);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
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
	public static TupleReader getTabReader(String fileName) {
		fileName = dataDir + origName(fileName);
		try {
			return (isBinary) ? new BinaryTupleReader(fileName) :
				new NormalTupleReader(fileName);
			// return new BufferedReader(new FileReader(tabPath(fileName)));
		} catch (FileNotFoundException e) {
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
		TupleReader btr = getTabReader(tabName);
		if (btr == null) return null;
		return new Table(tabName, getSchema(tabName), btr);
	}
	
	public static Table getIndexTable(String tabName) {
		String orig = origName(tabName);
		if (!idxInfo.containsKey(orig))
			throw new IllegalArgumentException();
		String attr = idxInfo.get(orig).attr;
		
		String path = idxsDir + orig + '.' + attr;
		// TODO
		// make tr the reader of the index file at path
		TupleReader tr = null;
		return new Table(tabName, getSchema(tabName), tr);
	}
	
	public static IndexInfo getIndexInfo(String tabName) {
		return idxInfo.get(origName(tabName));
	}
	
	// intentionally make the constructor private, which 
	// avoids instances being created outside the class
	private DBCat() {
		resetDirs(inputDir, outputDir, tempDir);
	}
	
	/**
	 * Returns the instance of the DBCat and makes
	 * sure there are only one instance exist;
	 * 
	 * @return the instance of instance of the class
	 */
	public static DBCat getInstance() {
		if (instance == null) {
			instance = new DBCat();
		}
		return instance;
	}
	
}
