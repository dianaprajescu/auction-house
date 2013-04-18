package Network;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class File {
	static final int MESSAGE_LENGTH = 1024;

	// Service id.
	private int serviceId;
	
	// Buyer id.
	private int buyerId;
	
	// The file size.
	private int count;
	
	// Seller id.
	private int sellerId;
	
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
	public File(int count, int serviceId, int buyerId, int sellerId) throws IOException
	{
		this.serviceId = serviceId;
		this.count = count;
		this.progress = 0;
		this.buyerId = buyerId;
		this.sellerId = sellerId;
		
		String time = String.valueOf(System.currentTimeMillis());
		
		@SuppressWarnings("resource")
		RandomAccessFile file = new RandomAccessFile("f_" + serviceId + "_" + sellerId + "_" + buyerId + time, "rw");
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
		if (progress == -1)
			return null;
		
		// Init buffer size.
    	ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH + 6 * Integer.SIZE / 8);
    	
    	buffer.putInt(MESSAGE_LENGTH + 5 * Integer.SIZE / 8);
    	
    	// Set message type.
    	buffer.putInt(NetworkMethods.TRANSFER.getInt());
    	
    	this.progress++;
    	
    	// Progress
    	buffer.putInt(this.getProgress());
    	
    	// Service id.
    	buffer.putInt(this.serviceId);
    	
    	// Buyer id.
    	buffer.putInt(this.buyerId);
    	
    	// Buyer id.
    	buffer.putInt(this.sellerId);
    	
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
		return this.progress < 0 ? this.progress : this.progress * 100 / this.count;
	}
	
	public void transferFailed()
	{
		this.progress = -1;
	}
	
	/**
	 * Get progress.
	 * 
	 * @return
	 */
	public int getBuyerId()
	{
		return this.buyerId;
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
