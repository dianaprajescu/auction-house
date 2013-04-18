package Network;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

public class TransferTask extends SwingWorker<Integer, Integer> {
	private static final int MESSAGE_LENGTH = 1024 * 1024;

	private int serviceId;
	
	// The file size.
	private int count;
	
	// The client state.
	IStateClientNetwork state;
	
	public TransferTask(int count, int serviceId) {
		this.count = count;
		this.serviceId = serviceId;
	}

	protected Integer doInBackground() throws Exception {
		int i = 0;
		
		RandomAccessFile file = new RandomAccessFile("f" + this.serviceId, "rw");
        file.setLength(count * 1024 * 1024);
        
        FileChannel fileChannel = file.getChannel();  
		
		try
		{
			// Open new communication channel.
			SocketChannel channel = SocketChannel.open();
			 
	        // Open this channel in non blocking mode.
	        channel.configureBlocking(false);
	        
	        // Connect to server.
	        channel.connect(new InetSocketAddress(ServerNetwork.url, ServerNetwork.port));
	 
	        // Wait for connecting.
	        while (!channel.finishConnect());
	        
	        while (i++ < count)
	        {
	        	// Init buffer size.
	        	ByteBuffer buffer = ByteBuffer.allocate(MESSAGE_LENGTH + Integer.SIZE / 8);
	        	
	        	// Set messag type.
	        	buffer.putInt(NetworkMethods.TRANSFER.getInt());
	        	
	        	// Read from file.
	        	fileChannel.read(buffer);
	        	buffer.flip();
	        	
	        	// Send to server.
    			while (buffer.hasRemaining()){
    				channel.write(buffer);
    			}
	        		
	        	setProgress(i*100/count);
		        
		        // Execute current state.
		        state.execute();
	        }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			setProgress(0);
			  
			return new Integer(0);
		}
		
		setProgress(100);
  
		return new Integer(0);
	}

	protected void done() {
	}
}