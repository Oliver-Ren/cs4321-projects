package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;



import nio.BinaryTupleReader;

// this class provides methods to gather information from the 
// current exisiting database, and output a file called 
// stats.txt sitting in the input/db directory
public class Stats {
	String outputDir;
	String dataDir;
	String schemaPath;
	File statFile;
	BufferedReader br;
	BufferedWriter bw;
	HashMap<String, TableInfo> stat; // key: table name, 
	
	public Stats() throws IOException{
		
		outputDir = DBCat.dbDir;
		dataDir = DBCat.dataDir;
		schemaPath = DBCat.schemaPath;
		try {
			if(outputDir == null || dataDir == null || schemaPath == null){
				throw new FileNotFoundException();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
		// ???
		statFile = new File(outputDir + File.separator + "stat.txt" );	
		bw = new BufferedWriter(new FileWriter(statFile));
	}
	/**
	 * Gather infomation from the input data, and create a stat.txt file 
	 * located in /db/ directory
	 * @throws IOException
	 */
	public void gatherInfo() throws IOException{
		// read file from schema.txt
		File schema = new File(schemaPath);
		br = new BufferedReader(new FileReader(schema));
		stat = new HashMap<String,TableInfo>();
		String line = null;
		while((line = br.readLine()) !=null){
			
			String[] names = line.split("\\s+");
			String tabName = names[0];
			TableInfo tabInfo = 
					new TableInfo(tabName,new 
							String[names.length-1],new int[names.length-1],new int[names.length-1]);
			int cntTp = 0;
			// find each att's min and max
			// ==== do i need to initilize the value 
			int min[] = new int[names.length-1];
			int max[] = new int [names.length-1];
			for (int i = 0;  i < min.length; i++) {
				min[i] = Integer.MAX_VALUE;
			}
			for (int i = 0;  i < min.length; i++) {
				max[i] = Integer.MIN_VALUE;
			}			
			File tab = new File(dataDir + File.separator + tabName);
			BinaryTupleReader tr = new BinaryTupleReader(tab);
			Tuple tp  = null;
			while((tp = tr.read()) != null){
				cntTp++;
				for(int i = 0; i< min.length; i++){
					min[i] = Math.min(min[i], tp.get(i));
					max[i] = Math.max(max[i], tp.get(i));
				}
			}
			bw.write(tabName + " ");
			bw.write(cntTp + " ");
			for(int i = 1; i < names.length; i++){ // not count table name 
				bw.write(names[i]+",");
				bw.write(min[i-1]+",");
				bw.write(max[i-1] + " ");
				tabInfo.addAttr(names[i], min[i-1], max[i-1]);
				tabInfo.setTpNum(cntTp);
			}
			stat.put(tabName, tabInfo);
			bw.newLine(); // switch table
			
			
		}
		bw.close();		
	}
	
	/**
	 * return a hash map containing all the table names as key
	 * and its attrs name, range as value
	 * @return
	 */
	public HashMap<String,TableInfo> getMap(){		
		return stat;	
	}
	
		
}
