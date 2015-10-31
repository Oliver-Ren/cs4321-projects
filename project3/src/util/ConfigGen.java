package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.SortedSet;

public class ConfigGen {
	private File configFile;
	private int joinMethod = 0;
	private int joinBufferSize = 0;
	private int sortMethod = 0;
	private int sortBufferSize = 0;
	private boolean joinSeted = false;
	private boolean sortSeted = false;
	
	/**
	 * The name of the configuration.
	 */
	public static final String CONFIG_NAME = "plan_builder_config.txt";
	
	/**
	 * Tuple nested loop join.
	 */
	public static final int TNLJ = 0;
	
	/**
	 * Block nested loop join.
	 */
	public static final int BNLJ = 1;
	
	/**
	 * Sort merge join.
	 */
	public static final int SMJ = 2;
	
	/**
	 * In memory sort.
	 */
	public static final int MEM_SORT = 0;
	
	/**
	 * External Merge Sort.
	 */
	public static final int EM_SORT = 1;
	
	/**
	 * Construct the configuration generator.
	 * @param configDir the directory/folder the config is located.
	 */
	public ConfigGen(String configDir) {
		configFile = new File(configDir + File.separator + CONFIG_NAME);
	}
	
	/**
	 * Sets the Join method.
	 * @param method the method number.
	 * @param bufferSize the size of the buffer.
	 */
	public void setJoinMethod(int method, int bufferSize) {
		if (method != 0 && bufferSize <= 0) {
			throw new IndexOutOfBoundsException("The size is too small");
		}
		joinSeted = true;
		this.joinMethod = method;
		if (method != 0) {
			joinBufferSize = bufferSize;
		} else {
			joinBufferSize = 0;
		}
	}
	
	/**
	 * Sets the sort method.
	 * @param method the method number.
	 * @param bufferSize the size of the buffer.
	 */
	public void setSortMethod(int method, int bufferSize) {
		if (method != 0 && bufferSize <= 0) {
			throw new IndexOutOfBoundsException("The size is too small");
		}
		sortSeted = true;
		this.sortMethod = method;
		if (method != 0) {
			sortBufferSize = bufferSize;
		} else {
			sortBufferSize = 0;
		}
	}
	
	/**
	 * Generate the configuration file.
	 */
	public void gen() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
			writer.write(String.valueOf(joinMethod));
			if (joinBufferSize > 0) {
				writer.write(" " + String.valueOf(joinBufferSize));
			}
			
			writer.newLine();
			writer.write(String.valueOf(sortMethod));
			if (sortBufferSize > 0) {
				writer.write(" " + String.valueOf(sortBufferSize));
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	

}
