package sandbox;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOExp {
	private static final int BSIZE = 1024;
	public static void main(String[] args) throws Exception{
		File path = new File("./sandbox/Boats");
		FileInputStream fin = new FileInputStream(path);
		FileChannel fc = fin.getChannel();
		ByteBuffer buffer = ByteBuffer.allocate(BSIZE);
		fc.read(buffer);
		buffer.flip();
		while (buffer.hasRemaining()) {
			System.out.println(buffer.getInt());
		}
		fc.close();
		fin.close();
	}
		
}
