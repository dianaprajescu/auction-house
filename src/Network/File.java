package Network;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class File {
	private static final int MESSAGE_LENGTH = 1024;

	// Service id.
	private int serviceId;
	
	// Seller id.
	private int sellerId;
	
	// The file size.
	private int count;
	
	// Progress.
	private int progress;
	
	// The client state.
	IStateClientNetwork state;
	
	// The file channel.
	FileChannel fileChannel;
	
	/** 
	 * Constructor.
	 * 
	 * @param serviceId
	 * @param count
	 * @throws IOException
	 */
	public File(int count, int serviceId, int sellerId) throws IOException
	{
		this.serviceId = serviceId;
		this.count = count;
		this.progress = 0;
		this.sellerId = sellerId;
		
		@SuppressWarnings("resource")
		RandomAccessFile file = new RandomAccessFile("f" + this.serviceId, "rw");
        file.setLength(count * MESSAGE_LENGTH);
        
        this.fileChannel = file.getChannel();
	}
	
	/**
	 * Send message to server.
	 * 
	 * @param channel
	 * @throws IOException
	 */
	public ByteBuffer getBuffer() throws IOException
	{
		// Init buffer size.
    	ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH + 5 * Integer.SIZE / 8);
    	
    	buffer.putInt(MESSAGE_LENGTH + 4 * Integer.SIZE / 8);
    	
    	// Set message type.
    	buffer.putInt(NetworkMethods.TRANSFER.getInt());
    	
    	this.progress++;
    	
    	// Progress
    	buffer.putInt(this.getProgress());
    	
    	// Seller id.
    	buffer.putInt(this.sellerId);
    	
    	// Service id.
    	buffer.putInt(this.serviceId);
    	
    	// Read from file.
    	if (fileChannel.read(buffer) > 0)
    	{
    		buffer.flip();
    		
    		return buffer;
    	}
    	else
    	{
    		return null;
    	}
	}
	
	/**
	 * Get progress.
	 * 
	 * @return
	 */
	public int getProgress()
	{
		return this.progress * 100 / this.count;
	}
	
	/**
	 * Get progress.
	 * 
	 * @return
	 */
	public int getSellerId()
	{
		return this.sellerId;
	}
	
	/**
	 * Get progress.
	 * 
	 * @return
	 */
	public int getServiceId()
	{
		return this.serviceId;
	}
}
