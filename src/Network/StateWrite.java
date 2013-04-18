package Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class StateWrite implements IStateClientNetwork {
	
	int[] message;
	ByteBuffer buffer;
	SocketChannel channel;
	ClientNetwork clientNetwork;
	
	public StateWrite(SocketChannel channel, int[] message, ClientNetwork clientNetwork) {
		this.channel = channel;
		this.message = message;
		this.clientNetwork = clientNetwork;
	}
	
	public StateWrite(SocketChannel channel, int[] message) {
		this.channel = channel;
		this.message = message;
	}
	
	@Override
	public void execute() throws IOException {
		if (message[0] > NetworkMethods.TRANSFER.getInt())
		{
			// Prepare buffer.
			prepareBuffer();
			
			// Put message in buffer.
			processMessage();
		}
		else
		{
			if (message[0] == NetworkMethods.START_TRANSFER.getInt())
			{
				this.clientNetwork.startTransfer(message[1], message[2]);
			}
			
			// Set buffer.
			buffer = clientNetwork.getBufferTransfer(message[1]);
			
			System.out.println(buffer.capacity());
			
			if (buffer == null)
			{
				return;
			}
		}
		
		try{
			// Send message.
			while (buffer.hasRemaining()){
				channel.write(buffer);
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
			buffer.putInt(message[i]);
		}
		
		buffer.flip();
	}

}
