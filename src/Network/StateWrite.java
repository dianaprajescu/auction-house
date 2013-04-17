package Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class StateWrite implements IStateClientNetwork {
	
	int[] message;
	ByteBuffer buffer;
	SocketChannel channel;
	
	public StateWrite(SocketChannel channel, int[] message) {
		this.channel = channel;
		this.message = message;
	}
	
	@Override
	public void execute() throws IOException {
		// Prepare buffer.
		prepareBuffer();
		
		// Put message in buffer.
		processMessage();
		
		// Send message.
		while (buffer.hasRemaining()){
			channel.write(buffer);
		}
	}

	@Override
	public void prepareBuffer() {
		buffer = ByteBuffer.allocate(ServerNetwork.BUF_SIZE);
	}

	@Override
	public void processMessage() {
		// Init buffer size.
		int k = ServerNetwork.BUF_SIZE;

		// Put message in buffer.
		for (int i = 0; i < message.length; i++, k-=4)
		{
			buffer.putInt(message[i]);
		}
		
		// Fill buffer with -1 at the end.
		while ( k > 0)
		{
			k-= 4;
			buffer.putInt(-1);
		}
		
		buffer.flip();
	}

}
