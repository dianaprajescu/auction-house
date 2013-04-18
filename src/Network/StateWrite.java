package Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class StateWrite implements IStateClientNetwork {
	
	Object[] message;
	ByteBuffer buffer;
	SocketChannel channel;
	ClientNetwork clientNetwork;
	
	public StateWrite(SocketChannel channel, Object[] message, ClientNetwork clientNetwork) {
		this.channel = channel;
		this.message = message;
		this.clientNetwork = clientNetwork;
	}
	
	public StateWrite(SocketChannel channel, Object[] message2) {
		this.channel = channel;
		this.message = message2;
	}
	
	@Override
	public void execute() throws IOException {
		
		if ((int)message[0] == NetworkMethods.NEW_TRANSFER.getInt() )
		{
			int progress = (int)message[1];
			int serviceId = (int)message[2];
			int buyerId = (int)message[3];
			int sellerId = (int)message[4];
			ByteBuffer buf = (ByteBuffer)message[5];
			
			buffer = ByteBuffer.allocate(File.MESSAGE_LENGTH + 6 * Integer.SIZE / 8);
	    	
	    	buffer.putInt(File.MESSAGE_LENGTH + 5 * Integer.SIZE / 8);
	    	
	    	// Set message type.
	    	buffer.putInt(NetworkMethods.NEW_TRANSFER.getInt());
	    	
	    	// Progress
	    	buffer.putInt(progress);
	    	
	    	// Service id.
	    	buffer.putInt(serviceId);
	    	
	    	// Buyer id.
	    	buffer.putInt(buyerId);
	    	
	    	// Buyer id.
	    	buffer.putInt(sellerId);
	    	
	    	// Read from file.
	    	while (buf.hasRemaining())
	    	{
	    		buffer.put(buf.get());
	    	}
	    	
	    	buffer.flip();
		}
		else if ((int)message[0] == NetworkMethods.TRANSFER.getInt())
		{	
			System.out.println("StateWrite: sendTransfer");
			
			// Set buffer.
			buffer = clientNetwork.getBufferTransfer((int)message[1], (int)message[2]);
			
			if (buffer == null)
			{
				return;
			}
		}
		else
		{
			// Prepare buffer.
			prepareBuffer();
			
			// Put message in buffer.
			processMessage();
		}
		
		try{
			// Send message.
			while (buffer.hasRemaining()){
				channel.write(buffer);
			}
			
			if ((int)message[0] == NetworkMethods.LOGOUT.getInt())
			{
				this.clientNetwork.stopClient();
			}
		}
		catch(Exception e)
		{
			channel.close();
		}
	}

	@Override
	public void prepareBuffer() {
		buffer = ByteBuffer.allocate((message.length + 1) * Integer.SIZE / 8);
	}

	public void processMessage() {	
		// Set message length.
		buffer.putInt(message.length * Integer.SIZE / 8);
		
		// Put message in buffer.
		for (int i = 0; i < message.length; i++)
		{
			buffer.putInt((int)message[i]);
		}
		
		buffer.flip();
	}

}
