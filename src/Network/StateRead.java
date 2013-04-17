package Network;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

import interfaces.INetwork;

public class StateRead implements IStateClientNetwork {

	SocketChannel channel;
	INetwork network;
	ByteBuffer buffer;
	ArrayList<Integer> message;
	
	public StateRead(SocketChannel channel, INetwork network) {
		this.channel = channel;
		this.network = network;
	}
	
	@Override
	public void execute() throws IOException {
		this.prepareBuffer();
		
		// Read from server.
		while (channel.read(buffer) > 0) {
			buffer.flip();
			
			int param;
			while(buffer.hasRemaining() && (param = buffer.getInt()) > 0)
			{
				message.add(param);
			}
		}
		
		if (message.size() > 0){
			this.processMessage();
		}
	}

	@Override
	public void prepareBuffer() {
		buffer = ByteBuffer.allocate(ServerNetwork.BUF_SIZE);
		message = new ArrayList<Integer>();
	}

	@Override
	public void processMessage() {
		System.out.println("Process message: " + message);
	}

}
