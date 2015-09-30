package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class DBCat {

	public static String inputDir = "";
	public static String outputDir = "";
	
	public static HashMap<String, String[]> schemas = new HashMap<String, String[]>();
	
	public static Table getTable(String tabName) {
		try {
			return new Table(new FileReader(inputDir + "/" + tabName));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
}
