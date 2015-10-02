package tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * This is a class for comparing two files line by line. Test harness
 * for the end-to-end test.
 * 
 * @author Chengxiang Ren
 *
 */
public class Diff {
	/**
	 * Returns a list of file names under this path
	 * @param path the input path
	 * @return list a sorted list of strings representing file name
	 */
	public static String[] dirList(String path) {
		File dirPath = new File(path);
		String[] list = dirPath.list();
		Arrays.sort(list);
		return list;
	}
	
	/**
	 * Returns true if two files have exactly the same content.
	 * @param path1 String path of file1
	 * @param path2 String path of file2
	 * @return true if two files have exactly the same content.
	 */
	public static boolean areTotallySame(String path1, String path2) {
		boolean areSame = false;
		try {
			byte[] f1 = Files.readAllBytes(Paths.get(path1));
			byte[] f2 = Files.readAllBytes(Paths.get(path2));
			areSame = Arrays.equals(f1, f2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return areSame;
	}
	
	/**
	 * This method is under debugging. Do not use!!!
	 * @param path1
	 * @param path2
	 * @return
	 */
	private static boolean areSame(String path1, String path2) {
		
		Boolean areSame = true;
		
		try {
			BufferedReader in1 = new BufferedReader(new FileReader(path1));
			BufferedReader in2 = new BufferedReader(new FileReader(path2));
			String s1 = null;
			String s2 = null;
			while ((s1 = in1.readLine()) != null 
					&& (s2 = in2.readLine()) != null) {
				System.out.println("s1 =" + s1 +"; s2 =" + s2);
				if (!s1.equals(s2)) {
					System.out.println("not same" + s1 + ", " + s2);
					areSame = false;
					break;
				}
			}
			
			if (!(s1 == null && s2 == null)) {
				System.out.println("haha" + s1 + " ," + s2);
				areSame = false;
			}
			
			in1.close();
			in2.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return areSame;
	}
}
