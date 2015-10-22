package nio;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import util.DBCat;
import util.Tuple;

public class BinaryFileReader implements TupleReader {
	/**
	 * name of the table
	 */
	public String name = "";
	public List<String> schema = null;
	/**
	 * the buffer take a page of size 4096 byte
	 */
	private ByteBuffer buffer = ByteBuffer.allocate(4096);
	private FileChannel channel = null;
	@Override
	public Tuple readTuple() {
		// TODO Auto-generated method stub
		
		return null;
	}
	
	/**
	 * Constructor that create a byteBufer to store 
	 * one page (4096 byte) every time
	 * @param: the file name needs to be read
	 */
	public BinaryFileReader(String fileName){
		// find the data file path
		String filePath = DBCat.tabPath(fileName);
		try{
			FileInputStream fis = new FileInputStream(filePath);
			channel = fis.getChannel();			
		} catch(FileNotFoundException e){
			System.out.println("file did not find");
			e.printStackTrace();
		}
		
		
		
		
	}

}
