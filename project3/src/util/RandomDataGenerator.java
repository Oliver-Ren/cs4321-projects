package util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


/**
 * This class is used for generating randoms tuples with values
 * in specific range
 * @author Mingyuanh
 *
 */
public class RandomDataGenerator {
	/**
	 * @param filePath: the path where file will be generated
	 * @param numOfTuple: number of tuples need to be generated
	 * @param range: the maximum tuple value
	 * @param numOfAttr: number of attributes
	 */
	public static void tuplesGenerator
	(String filePath, int numOfTuple, int range,int numOfAttr) 
			throws IOException{
		BufferedWriter bf = new 
				BufferedWriter(new FileWriter(filePath));
		Random rand = new Random();
		int index = 0;
		while(index < numOfTuple){
			int cnt = 0;
			int[] col = new int[numOfAttr];
			while(cnt<numOfAttr){
				col[cnt] = rand.nextInt(range);
				cnt++;
			}
			Tuple t = new Tuple(col);
			bf.write(t.toString());
			bf.newLine();
			index++;
		}
		bf.close();
		
	}
}

