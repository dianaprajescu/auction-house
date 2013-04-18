package Network;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class ReceivedFile {
	
	FileChannel fileChannel;
	
	/** 
	 * Constructor.
	 * 
	 * @param serviceId
	 * @param count
	 * @throws IOException
	 */
	public ReceivedFile(int serviceId, int buyerId, int sellerId) throws IOException
	{
		String time = String.valueOf(System.currentTimeMillis());
		
		@SuppressWarnings("resource")
		RandomAccessFile file = new RandomAccessFile("f_" + serviceId + "_" + sellerId + "_" + buyerId + time, "rw");
        
        this.fileChannel = file.getChannel();
	}
	
	public void write(ByteBuffer bytes) throws IOException
	{
		while (this.fileChannel.write(bytes) > 0);
	}
}
