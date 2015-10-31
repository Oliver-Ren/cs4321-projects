package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class takes a file, and sort the tuples in it in memory
 * using collections.sort
 * @author Mingyuanh
 *
 */
public class SortTuple {
	private static BufferedReader br;
	private static BufferedWriter bw;
	
	/**
	 * this method takes an input file, sort it, then returns the sorted list.
	 * @param InputfilePath:  need to include the fileName: eg: sandbox/boat
	 * @param OutputPath: need to include the fileName eg: sandbox/boat
	 */
	public static List<String> sortTuplesToList(String InputfilePath, String OutputPath)
	throws IOException {
		return null;
	}
	/**
	 * this method takes an input file, sort it, then output the sorted file
	 * @param InputfilePath:  need to include the fileName: eg: sandbox/boat
	 * @param OutputPath: need to include the fileName eg: sandbox/boat
	 */
	public static void sortTuples(String InputfilePath, String OutputPath)
	throws IOException{
		try{
			br = new BufferedReader(new FileReader(InputfilePath));
		}catch(IOException e){
			System.out.println("can't find the input directory");
			e.printStackTrace();
		}
		
		String tuple;
		ArrayList<String> list = new ArrayList<String>();
		while((tuple = br.readLine())!=null){
			list.add(tuple);
		}
		br.close();
		Collections.sort(list);
		bw = new BufferedWriter(new FileWriter(OutputPath));
		for(String x : list){
			bw.write(x);
			bw.newLine();
		}
		bw.close();
	}
//	public static void main(String[] args) throws IOException{
//		sortTuples("sandbox/Reserves_humanreadable","sandbox/sortedFile");
//	}

	
}
